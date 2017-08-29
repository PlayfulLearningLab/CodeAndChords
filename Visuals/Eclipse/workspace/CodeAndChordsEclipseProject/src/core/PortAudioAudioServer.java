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
            state.set(State.Terminated);
            // TODO: call shutdown() here, too?
            PortAudio.terminate();
            throw ex;
        } // try/catch
        
        if (state.compareAndSet(State.Initialising, State.Active)) {
            // Open input and output streams, using defaults
        	
        	StreamParameters	inputParams			= new StreamParameters();
        	StreamParameters	outputParams		= new StreamParameters();

        	// Get default devices and info:
        	int					inputDevice	= PortAudio.getDefaultInputDevice();
        	DeviceInfo			inputDeviceInfo		= PortAudio.getDeviceInfo(inputDevice);
        	int					outputDevice	= PortAudio.getDefaultOutputDevice();
        	DeviceInfo			outputDeviceInfo	= PortAudio.getDeviceInfo(outputDevice);

        	
        	int					inputChannels		= inputDeviceInfo.maxInputChannels;
        	int					outputChannels		= inputDeviceInfo.maxInputChannels;
        	
        	double				inputSampleRate		= inputDeviceInfo.defaultSampleRate;
        	double				outputSampleRate	= inputDeviceInfo.defaultSampleRate;
        	
        	// define parameters:
        	inputParams.device					= inputDevice;
        	inputParams.channelCount			= inputChannels;
        	inputParams.sampleFormat			= PortAudio.FORMAT_FLOAT_32;
        	inputParams.suggestedLatency		= inputDeviceInfo.defaultHighInputLatency;
        	
        	outputParams.device					= outputDevice;
        	outputParams.channelCount			= outputChannels;
        	outputParams.sampleFormat			= PortAudio.FORMAT_FLOAT_32;
        	outputParams.suggestedLatency		= outputDeviceInfo.defaultHighInputLatency;
        	
        	// StreamParameters
        	// sample rate (int?)
        	// frames per buffer (int?)
        	// flags - ?
/*
        	<<<<<<< HEAD
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
/*		public AudioConfiguration getAudioContext()
		{
			return this.context;
		} // getAudioContext

		/**
		 * Check whether the server is active. This method can be called from another
		 * thread.
		 * @return true if active.
		 */
/*		public boolean isActive()
		{
			State st = state.get();
			return (st == State.Active || st == State.Closing);
		} // isActive

		/**
		 * Trigger the server to shut down. This method can be called from another
		 * thread, but does not guarantee that the server is shut down at the moment
		 * it returns. (Implementation from JSAudioServer and JackAudioServer.)
		 */
/*		public void shutdown() {
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
=======
*/
        	BlockingStream	stream	= PortAudio.openStream(inputParams, outputParams, 
        			(int)this.context.getSampleRate(), this.context.getMaxBufferSize(), 0);
        	
        	stream.start();
        	
        	System.out.println("PAAS.run: stream.isActive = " + stream.isActive());
        	// TODO: see if this is also a problem if I make a C program doing this.
        	
        	int	writeAvailable;
        	int	readAvailable;
        	while(stream.isActive())
        	{
            	writeAvailable	= stream.getWriteAvailable();
            	stream.write(this.inputBuffer, writeAvailable);
            	
            	readAvailable	= stream.getReadAvailable();
            	stream.read(this.outputBuffer, readAvailable);
        	} // while
        	

        	stream.stop();
        	stream.close();
        	
        } // if active
        
        PortAudio.terminate();
        state.set(State.Terminated);
    	
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
                break;
            }
        } while (!state.compareAndSet(st, State.Closing));
    }

} // PortAudioAudioServer
