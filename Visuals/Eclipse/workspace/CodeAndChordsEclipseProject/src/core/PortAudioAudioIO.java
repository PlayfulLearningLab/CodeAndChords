package core;

//package net.beadsproject.beads.core.io;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import com.portaudio.BlockingStream;
import com.portaudio.DeviceInfo;
import com.portaudio.PortAudio;
import com.portaudio.StreamParameters;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.AudioIO;
import net.beadsproject.beads.core.AudioUtils;
import net.beadsproject.beads.core.IOAudioFormat;
import net.beadsproject.beads.core.UGen;

public class PortAudioAudioIO extends AudioIO {

	/** The default system buffer size. */
	public static final int DEFAULT_SYSTEM_BUFFER_SIZE = 5000;
	
	/** The mixer. */
	private Mixer mixer;

	/** The source data line. */
	private SourceDataLine sourceDataLine;
	
	/** The system buffer size in frames. */
	private int systemBufferSizeInFrames;

	/** Thread for running realtime audio. */
	private Thread audioThread;

	/** The priority of the audio thread. */
	private int threadPriority; 
	
	/** The current byte buffer. */
	private byte[] bbuf;
	
	/**
	 * The current float buffer.
	 */
	private	float[] buffer;
	
	private	BlockingStream	outStream;
	private	BlockingStream	inStream;
	
	public PortAudioAudioIO() {
		this(DEFAULT_SYSTEM_BUFFER_SIZE);
	}
	
	public PortAudioAudioIO(int systemBufferSize) {
		this.systemBufferSizeInFrames = systemBufferSize;
		setThreadPriority(Thread.MAX_PRIORITY);
	}
	
	/**
	 * Initialises JavaSound.
	 */
	public boolean create() {
		PortAudio.initialize();
		
		StreamParameters outParameters = new StreamParameters();
//		outParameters.sampleFormat = PortAudio.FORMAT_FLOAT_32; // maybe use int_16>  b/c AudioContext.getAudioFormat.bitDepth = 16; was previously .FORMAT_FLOAT_32;
		outParameters.device = PortAudio.getDefaultOutputDevice();
		outParameters.channelCount = PortAudio.getDeviceInfo( outParameters.device ).maxOutputChannels;
		outParameters.suggestedLatency = PortAudio.getDeviceInfo( outParameters.device ).defaultLowInputLatency;

		int sampleRate = (int) context.getSampleRate();
		int framesPerBuffer = context.getBufferSize();
		int flags = 0;

		this.outStream = PortAudio.openStream( null, outParameters, sampleRate, framesPerBuffer, flags );
		this.outStream.start();
		
		return true;
		
		/*getDefaultMixerIfNotAlreadyChosen();
		if (mixer == null) {
			return false;
		}
		DataLine.Info info = new DataLine.Info(SourceDataLine.class,
				audioFormat);
		try {
			sourceDataLine = (SourceDataLine) mixer.getLine(info);
			if (systemBufferSizeInFrames < 0)
				sourceDataLine.open(audioFormat);
			else
				sourceDataLine.open(audioFormat, systemBufferSizeInFrames
						* audioFormat.getFrameSize());
		} catch (LineUnavailableException ex) {
			System.out
					.println(getClass().getName() + " : Error getting line\n");
		}
		return true;
		*/
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
		float[] interleavedOutput = new float[ioAudioFormat.outputs * bufferSizeInFrames];
		int availableToWrite = this.outStream.getWriteAvailable();
		
		System.out.println( "runRealTime - this.outStream: availableToWrite =  " + availableToWrite );


		while (context.isRunning()) {
			update(); // this propagates update call to context
			for (int i = 0, counter = 0; i < bufferSizeInFrames; ++i) {
				for (int j = 0; j < ioAudioFormat.outputs; ++j) {
					System.out.println("interleavedOutput[" + counter + "] = " + (interleavedOutput[counter]));
					interleavedOutput[counter++] = (int) context.out.getValue(j, i);
				}
			} // for - i
			
			System.out.println(this.outStream.getInfo().structVersion);

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
		//TODO not properly implemented, this does not respond to channels arg.
		IOAudioFormat ioAudioFormat = getContext().getAudioFormat();
		AudioFormat audioFormat = 
				new AudioFormat(ioAudioFormat.sampleRate, ioAudioFormat.bitDepth, ioAudioFormat.inputs, ioAudioFormat.signed, ioAudioFormat.bigEndian);
		return new PortAudioInput(getContext(), audioFormat, this.inStream);
	}

	/**
	 * PortAudioInput gathers audio from the JavaSound audio input device.
	 * @beads.category input
	 */
	private class PortAudioInput extends UGen {

		/** The audio format. */
		private AudioFormat audioFormat;
		
		/** The target data line. */
		
		/**	The blocking stream.	*/
		private	BlockingStream	inStream;
		
		/** Flag to tell whether JavaSound has been initialised. */
		private boolean portAudioInitialized;
		
		private float[] interleavedSamples;

		/**
		 * Instantiates a new RTInput.
		 * 
		 * @param context
		 *            the AudioContext.
		 * @param audioFormat
		 *            the AudioFormat.
		 */
		PortAudioInput(AudioContext context, AudioFormat audioFormat, BlockingStream inStream) {
			super(context, audioFormat.getChannels());
			this.audioFormat = audioFormat;
			this.inStream	= inStream;
			this.portAudioInitialized = false;
		}
		
		/**
		 * Set up JavaSound. Requires that JavaSound has been set up in AudioContext.
		 */
		public void initPortAudio() {
			interleavedSamples = new float[context.getBufferSize() * audioFormat.getChannels()];

			StreamParameters inParameters = new StreamParameters();
			inParameters.sampleFormat = PortAudio.FORMAT_FLOAT_32; // Using b/c AudioContext.getAudioFormat.bitDepth = 16; was previously .FORMAT_FLOAT_32;
			inParameters.device = PortAudio.getDefaultInputDevice();
			inParameters.channelCount = PortAudio.getDeviceInfo( inParameters.device ).maxInputChannels;
			inParameters.suggestedLatency = PortAudio.getDeviceInfo( inParameters.device ).defaultLowInputLatency;

			int sampleRate = (int) context.getSampleRate();
			int framesPerBuffer = context.getBufferSize();
			int flags = 0;

			inStream = PortAudio.openStream( inParameters, null, sampleRate, framesPerBuffer, flags );
			inStream.start();

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
//			targetDataLine.read(bbuf, 0, bbuf.length);
			inStream.read( interleavedSamples, bufferSize );
//			AudioUtils.byteToFloat(interleavedSamples, bbuf, audioFormat.isBigEndian());
			AudioUtils.deinterleave(interleavedSamples, audioFormat.getChannels(), bufferSize, bufOut);
		} // caluculateBuffer

	} // class PortAudioInput
	
} // class PortAudioAudioIO
