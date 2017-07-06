/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2013 Neil C Smith.
 *
 * Copying and distribution of this file, with or without modification,
 * are permitted in any medium without royalty provided the copyright
 * notice and this notice are preserved.  This file is offered as-is,
 * without any warranty.
 *
 * Please visit http://neilcsmith.net if you need additional information or
 * have any questions.
 *
 */

package core;

import java.util.concurrent.atomic.AtomicReference;

import org.jaudiolibs.audioservers.AudioConfiguration;

import com.portaudio.BlockingStream;
import com.portaudio.DeviceInfo;
import com.portaudio.PortAudio;
import com.portaudio.StreamParameters;

/**
 * 07/03/2017
 * An implementation of the Beads AudioServer class for the Java bindings of the PortAudio library.
 *
 * AudioServers provide a run() method as it is intended that the application should
 * provide the Thread in which the server will run, however they do not extend
 * Runnable as the run() method throws an Exception, forcing the application to
 * deal with any problems starting the server.
 *
 * @author Emily Meuer
 */
public class PortAudioAudioServer {

	private enum State {

		New, Initialising, Active, Closing, Terminated
	};

	private AudioConfiguration		context;
	private AtomicReference<State>	state;
	private	boolean					stopped	= false;

	public PortAudioAudioServer(AudioConfiguration context)
	{
		this.context	= context;
		this.state = new AtomicReference<State>(State.New);

		System.out.println("this.context.getMaxBufferSize = " + this.context.getMaxBufferSize());

	}


	/**
	 * Using the implementation from JSAudioServer and JackAudioServer.
	 * @throws Exception
	 */
	public void run() throws Exception
	{
		if (!state.compareAndSet(State.New, State.Initialising)) {
			throw new IllegalStateException();
		}
		try {
			PortAudio.initialize();
		} catch (Exception ex) {
			//           state.set(State.Terminated);
			// TODO: call shutdown() here, too?
			this.shutdown();
			PortAudio.terminate();
			throw ex;
		} // try/catch

		if (state.compareAndSet(State.Initialising, State.Active)) {

			int framesPerBuffer = 256;
			int flags = 0;
			int	sampleFormat	= PortAudio.FORMAT_FLOAT_32;
			int sampleRate = 44100;
			int numFrames = sampleRate * 3; // multiply by number of seconds
			float[] floatBuffer = null;

			PortAudio.initialize();
			StreamParameters inParameters = new StreamParameters();
			inParameters.sampleFormat = sampleFormat;
			inParameters.device = PortAudio.getDefaultInputDevice();

			DeviceInfo info = PortAudio.getDeviceInfo( inParameters.device );
			inParameters.channelCount = info.maxInputChannels;
			
			System.out.println( "channelCount = " + inParameters.channelCount );
			
			inParameters.suggestedLatency = PortAudio
					.getDeviceInfo( inParameters.device ).defaultLowInputLatency;


			// TODO: should check sample format before writing?  
			// if( sampleFormat == PortAudio.FORMAT_FLOAT_32 ) - float[];
			// else if( sampleFormat == PortAudio.FORMAT_INT_16 ) - short[]
			floatBuffer = new float[numFrames * inParameters.channelCount];

			// Record a few seconds of audio.
			BlockingStream inStream = PortAudio.openStream( inParameters, null,
					sampleRate, framesPerBuffer, flags );

			System.out.println( "RECORDING - say something like testing 1,2,3..." );
			inStream.start();

//			while(this.isActive())
//			{
				// TODO: should check sample format before reading?
				inStream.read( floatBuffer, numFrames );

				Thread.sleep( 100 );
				int availableToRead = inStream.getReadAvailable();
				System.out.println( "availableToRead =  " + availableToRead );
//			}

			inStream.stop();
			inStream.close();
			System.out.println( "Finished recording. Begin Playback." );

			// Play back what we recorded.
			StreamParameters outParameters = new StreamParameters();
			outParameters.sampleFormat = sampleFormat;
			outParameters.channelCount = inParameters.channelCount;
			outParameters.device = PortAudio.getDefaultOutputDevice();
			outParameters.suggestedLatency = PortAudio
					.getDeviceInfo( outParameters.device ).defaultLowOutputLatency;

			BlockingStream outStream = PortAudio.openStream( null, outParameters,
					sampleRate, framesPerBuffer, flags );

			outStream.start();
			Thread.sleep( 100 );
			int availableToWrite = outStream.getWriteAvailable();
			System.out.println( "availableToWrite =  " + availableToWrite );

			System.out.println( "inStream = " + inStream );
			System.out.println( "outStream = " + outStream );
			// TODO: should check sample format before writing?
			outStream.write( floatBuffer, numFrames );

			outStream.stop();

			outStream.close();

			// Open input and output streams, using defaults

			/*
			 * For some reason, this only records for a few seconds:
			 * (Something to do with the nubmer of frames?)d
			 // No, it's sleeping! -- never mind. :(
			  */
			/*
        	StreamParameters	inputParams			= new StreamParameters();
        	StreamParameters	outputParams		= new StreamParameters();

        	// Get default devices and info:
        	int					inputDevice	= PortAudio.getDefaultInputDevice();
        	DeviceInfo			inputDeviceInfo		= PortAudio.getDeviceInfo(inputDevice);
        	int					outputDevice	= PortAudio.getDefaultOutputDevice();
        	DeviceInfo			outputDeviceInfo	= PortAudio.getDeviceInfo(outputDevice);

        	int	inputChannels		= inputDeviceInfo.maxInputChannels;
        	int	outputChannels		= inputDeviceInfo.maxInputChannels;
        	int	framesPerBuffer		= 256;
        	int	inputSampleRate		= (int)inputDeviceInfo.defaultSampleRate;
        	int	outputSampleRate	= (int)inputDeviceInfo.defaultSampleRate;

        	int					numFrames			= (int)(this.context.getSampleRate() * 3);
        	System.out.println("this.context.getSampleRate() = " + (this.context.getSampleRate() * 3));
        	// define parameters:
        	inputParams.device					= inputDevice;
        	inputParams.channelCount			= inputChannels;
        	inputParams.sampleFormat			= PortAudio.FORMAT_FLOAT_32;
        	inputParams.suggestedLatency		= inputDeviceInfo.defaultLowInputLatency;

        	outputParams.device					= outputDevice;
        	outputParams.channelCount			= outputChannels;
        	outputParams.sampleFormat			= PortAudio.FORMAT_FLOAT_32;
        	outputParams.suggestedLatency		= outputDeviceInfo.defaultLowOutputLatency;

        	// StreamParameters
        	// sample rate (int?)
        	// frames per buffer (int?)
        	// flags - ?
        	BlockingStream	inStream	= PortAudio.openStream(inputParams, null, 
        			inputSampleRate, framesPerBuffer, 0);
        	BlockingStream	outStream	= PortAudio.openStream(null, outputParams, 
        			outputSampleRate, framesPerBuffer, 0);

        	inStream.start();
			Thread.sleep( 100 );

        	System.out.println("PAAS.run: inStream.isActive = " + inStream.isActive());
        	// TODO: see if this is also a problem if I make a C program doing this.

        	int	writeAvailable;
        	int	readAvailable;

        	// TODO: remove these after testing
        	int	count	= 0;
        	boolean	readOverflow	= false;
        	boolean	writeOverflow	= false;

        	// Start here:
        	float[] inputBuffer		= new float[(numFrames * inputParams.channelCount)];
        	float[] outputBuffer	= new float[numFrames * outputParams.channelCount];
            	readAvailable	= inStream.getReadAvailable();
            	System.out.println("run() - before read: readAvailable = " + readAvailable);
            	inStream.read(inputBuffer, numFrames);
            	readAvailable	= inStream.getReadAvailable();
            	System.out.println("run() - after read: readAvailable = " + readAvailable);

//           while(this.isActive())
//           {

 //          }
 //           Thread.sleep(10000);

        	System.out.println("run(): about to stop the inStream");
        	inStream.stop();
        	inStream.close();

        	outStream.start();
			Thread.sleep( 100 );

        	writeAvailable	= outStream.getWriteAvailable();
        	System.out.println("run(): writeAvailable = " + writeAvailable +
        			"; count = " + count + "; numFrames = " + numFrames);
        	writeOverflow	= outStream.write(inputBuffer, numFrames);
        	count++;
        	outStream.stop();

        	System.out.println("run(): stopped streams; about to close them");
        	outStream.close();
        	System.out.println("run(): streams have been closed");
*/
			} // if active


			System.out.println("run(): about to terminate PortAudio");
			PortAudio.terminate();
			System.out.println("run(): about to call shutdown");
			this.shutdown();
			//       state.set(State.Terminated);

		} // run()

		/**
		 * Get the current AudioConfiguration. This value should remain constant while the
		 * server is processing audio.
		 * @return AudioConfiguration
		 */
		public AudioConfiguration getAudioContext()
		{
			return this.context;
		} // getAudioContext

		/**
		 * Check whether the server is active. This method can be called from another
		 * thread.
		 * @return true if active.
		 */
		public boolean isActive()
		{
			State st = state.get();
			return (st == State.Active || st == State.Closing);
		} // isActive

		/**
		 * Trigger the server to shut down. This method can be called from another
		 * thread, but does not guarantee that the server is shut down at the moment
		 * it returns. (Implementation from JSAudioServer and JackAudioServer.)
		 */
		public void shutdown() {
			State st;
			do {
				st = state.get();
				if (st == State.Terminated || st == State.Closing) {
					System.out.println("shutdown: about to close");
					break;
				}
			} while (!state.compareAndSet(st, State.Closing));
		}

	} // PortAudioAudioServer
