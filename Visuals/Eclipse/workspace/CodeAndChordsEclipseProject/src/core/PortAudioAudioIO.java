package core;


import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;

import com.portaudio.BlockingStream;
import com.portaudio.PortAudio;
import com.portaudio.StreamParameters;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.AudioIO;
import net.beadsproject.beads.core.AudioUtils;
import net.beadsproject.beads.core.IOAudioFormat;
import net.beadsproject.beads.core.UGen;

public class PortAudioAudioIO extends AudioIO {

	/** The default system buffer size. */
//	public static final int DEFAULT_SYSTEM_BUFFER_SIZE = 5000;

	/** Thread for running realtime audio. */
	private Thread audioThread;

	/** The priority of the audio thread. */
	private int threadPriority; 
	
	private	BlockingStream	outStream;
	private	BlockingStream	inStream;
	
	private	int				numInChannels;
	private	int				numOutChannels;
	
	public PortAudioAudioIO() {
		this(2);
	}
	
	public PortAudioAudioIO(int numChannels) {
//		this.systemBufferSizeInFrames = systemBufferSize;
		this.numInChannels	= numChannels;
		this.numOutChannels	= numChannels;
		setThreadPriority(Thread.MAX_PRIORITY);
	}
	
	/**
	 * Initializes JavaSound.
	 */
	public boolean create() {
		PortAudio.initialize();
		
		StreamParameters outParameters = new StreamParameters();
//		outParameters.sampleFormat = PortAudio.FORMAT_FLOAT_32; // maybe use int_16>  b/c AudioContext.getAudioFormat.bitDepth = 16; was previously .FORMAT_FLOAT_32;
		outParameters.device = PortAudio.getDefaultOutputDevice();
		if(this.numOutChannels > PortAudio.getDeviceInfo( outParameters.device ).maxOutputChannels)
		{
			System.out.println("PortAudioAudioIO: given number of channels was " + this.numOutChannels + 
					", which is greater than " + PortAudio.getDeviceInfo( outParameters.device ).maxOutputChannels + 
					", the maximum for the default device. this.numChannels has been changed to the default device maxOutputChannels.");
			this.numOutChannels	= PortAudio.getDeviceInfo( outParameters.device ).maxOutputChannels;
		}
		outParameters.channelCount = this.numOutChannels;
		outParameters.suggestedLatency = PortAudio.getDeviceInfo( outParameters.device ).defaultLowInputLatency;

		int sampleRate = (int) context.getSampleRate();
		int framesPerBuffer = context.getBufferSize();
		int flags = 0;

		this.outStream = PortAudio.openStream( null, outParameters, sampleRate, framesPerBuffer, flags );
		this.outStream.start();
		
		return true;
	} // create
	
	/**
	 * Sets the priority of the audio thread.
	 * Default priority is Thread.MAX_PRIORITY.
	 *  
	 * @param priority 
	 */
	public void setThreadPriority(int priority) {
		this.threadPriority = priority;
		if(audioThread != null) audioThread.setPriority(threadPriority);
	}
	
	/**
	 * @return The priority of the audio thread.
	 */
	public int getThreadPriority() {
		return this.threadPriority;
	}

	/** Shuts down JavaSound elements, SourceDataLine and Mixer. */
	protected boolean destroy() {
		if(this.inStream != null)
		{
			if(this.inStream.isActive())	{	this.inStream.stop();	}
			this.inStream.close();
		} // inStream not null
		
		if(this.outStream != null)
		{
			if(this.outStream.isActive())	{	this.outStream.stop();	}
			this.outStream.close();
		} // outStream not null
		
		PortAudio.terminate();
		return true;
	}

	/** Starts the audio system running. */
	@Override
	protected boolean start() {
		audioThread = new Thread(new Runnable() {
			public void run() {
				//create JavaSound stuff only when needed
				create();
				//start the update loop
				runRealTime();
				//return from above method means context got stopped, so now clean up
				destroy();
			}
		});
		audioThread.setPriority(threadPriority);
		audioThread.start();
		return true;
	}
	
	/** Update loop called from within audio thread (created in start() method). */
	private void runRealTime() {		
		AudioContext context = getContext();
		IOAudioFormat ioAudioFormat = getContext().getAudioFormat();
//		AudioFormat audioFormat = 
//				new AudioFormat(ioAudioFormat.sampleRate, ioAudioFormat.bitDepth, ioAudioFormat.outputs, ioAudioFormat.signed, ioAudioFormat.bigEndian);
		
		int bufferSizeInFrames = context.getBufferSize();
		// TODO: might need to use audioFormat.getChannels() instead of ioAudioFormat.outputs
		float[] interleavedOutput = new float[this.numOutChannels * bufferSizeInFrames];

		while (context.isRunning()) {
			update(); // this propagates update call to context
			for (int i = 0, counter = 0; i < bufferSizeInFrames; ++i) {
				for (int j = 0; j < this.numOutChannels; ++j) {
					interleavedOutput[counter++] = (int) context.out.getValue(j, i);
				}
			} // for - i

			// TODO: maybe just write for availableToWrite, not whole buffer?
			this.outStream.write( interleavedOutput, bufferSizeInFrames );
		} // while
		
		/*
		sourceDataLine.start();
		while (context.isRunning()) {
			update(); // this propagates update call to context
			for (int i = 0, counter = 0; i < bufferSizeInFrames; ++i) {
				for (int j = 0; j < audioFormat.getChannels(); ++j) {
					interleavedOutput[counter++] = context.out.getValue(j, i);
				}
			}
			AudioUtils.floatToByte(bbuf, interleavedOutput,
					audioFormat.isBigEndian());
			sourceDataLine.write(bbuf, 0, bbuf.length);
		}
		*/
	} // runRealTime

	@Override
	protected UGen getAudioInput(int[] channels) {
		IOAudioFormat ioAudioFormat = getContext().getAudioFormat();
		AudioFormat audioFormat = 
				new AudioFormat(ioAudioFormat.sampleRate, ioAudioFormat.bitDepth, ioAudioFormat.inputs, ioAudioFormat.signed, ioAudioFormat.bigEndian);
		return new PortAudioInput(getContext(), audioFormat, this.inStream, this.numInChannels, channels);
	}

	/**
	 * PortAudioInput gathers audio from the default PortAudio audio input device.
	 * @beads.category input
	 */
	private class PortAudioInput extends UGen {

		/** The audio format. */
		private AudioFormat audioFormat;
		
		/**	The blocking stream.	*/
		private	BlockingStream	inStream;
		
		/** Flag to tell whether PortAudio has been initialized. */
		private boolean portAudioInitialized;
		
		private float[] 	interleavedSamples;
		private	float[][]	nonInterleavedSamples;
		private	int			numChannels;
		private	int[]		channels;

		/**
		 * Instantiates a new RTInput.
		 * 
		 * @param context
		 *            the AudioContext.
		 * @param audioFormat
		 *            the AudioFormat.
		 */
		PortAudioInput(AudioContext context, AudioFormat audioFormat, BlockingStream inStream, int numChannels, int[] channels) {
			super(context, numChannels);
			this.audioFormat 	= audioFormat;
			this.inStream		= inStream;
			this.numChannels	= numInChannels;
			
			this.channels		= new int[channels.length];
			for(int i = 0; i < this.channels.length; i++)
			{
				this.channels[i]	= channels[i];
				System.out.println("this.channels[" + i + "] = " + this.channels[i]);
			}
			
			this.portAudioInitialized = false;
		} // constructor
		
		/**
		 * Set up JavaSound. Requires that JavaSound has been set up in AudioContext.
		 */
		public void initPortAudio() {
			StreamParameters inParameters = new StreamParameters();
			inParameters.sampleFormat = PortAudio.FORMAT_FLOAT_32; // Using b/c AudioContext.getAudioFormat.bitDepth = 16; was previously .FORMAT_FLOAT_32;
			inParameters.device = PortAudio.getDefaultInputDevice();
			if(this.numChannels > PortAudio.getDeviceInfo( inParameters.device ).maxInputChannels)
			{
				System.out.println("PortAudioInput: given number of channels was " + this.numChannels + 
						", which is greater than " + PortAudio.getDeviceInfo( inParameters.device ).maxInputChannels + 
						", the maximum for the default device. this.numChannels has been changed to the default device maxInputChannels.");
				this.numChannels	= PortAudio.getDeviceInfo( inParameters.device ).maxInputChannels;
			}
			inParameters.channelCount = this.numChannels;
			inParameters.suggestedLatency = PortAudio.getDeviceInfo( inParameters.device ).defaultLowInputLatency;

			int sampleRate = (int) context.getSampleRate();
			int framesPerBuffer = context.getBufferSize();
			int flags = 0;

			inStream = PortAudio.openStream( inParameters, null, sampleRate, framesPerBuffer, flags );
			inStream.start();

			this.interleavedSamples 	= new float[context.getBufferSize() * this.numChannels];
			this.nonInterleavedSamples	= new float[this.numChannels][context.getBufferSize() * this.numChannels];

			portAudioInitialized = true;
		} // initPortAudio
		

		/* (non-Javadoc)
		 * @see com.olliebown.beads.core.UGen#calculateBuffer()
		 */
		@Override
		public void calculateBuffer() {
			if(!portAudioInitialized) {
				initPortAudio();
			}

			inStream.read( interleavedSamples, bufferSize );
			
			AudioUtils.deinterleave(this.interleavedSamples, this.numChannels, bufferSize, this.nonInterleavedSamples);
//			AudioUtils.deinterleave(this.interleavedSamples, this.numChannels, bufferSize, bufOut);
			
//			bufOut	= new float[this.channels.length][this.bufferSize];
			int	bufPos		= 0;
			int	nextChannel;
			for(int i = 0; i < this.channels.length; i++)
			{
				nextChannel	= (this.channels[i] - 1);
				
				// Check to see if the current channel was asked for:
				if( nextChannel < this.nonInterleavedSamples.length)
				{
					if(bufPos >= bufOut.length) {
						throw new IllegalArgumentException("PortAudioInput.caluculateBuffer: bufPos " + bufPos + " is not within bounds.");
					} // error checking
					
					for(int j = 0; j < bufOut[bufPos].length; j++)
					{
						bufOut[bufPos][j]	= this.nonInterleavedSamples[nextChannel][j];
					} // for - j
					
					bufPos	= bufPos + 1;
				} // if
			} // for - i
			
		} // caluculateBuffer

	} // class PortAudioInput
	
} // class PortAudioAudioIO
