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
	private	float[]					inputBuffer;
	private	float[]					outputBuffer;


	public PortAudioAudioServer(AudioConfiguration context)
	{
		this.context	= context;
		this.state = new AtomicReference<State>(State.New);

		this.inputBuffer	= new float[this.context.getMaxBufferSize()];
		this.outputBuffer	= new float[this.context.getMaxBufferSize()];
		
		for(int i = 0; i < this.inputBuffer.length; i++)
		{
			this.inputBuffer[i]	= 0;
			this.outputBuffer[i]	= 0;
		}
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
		} // if Active

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
