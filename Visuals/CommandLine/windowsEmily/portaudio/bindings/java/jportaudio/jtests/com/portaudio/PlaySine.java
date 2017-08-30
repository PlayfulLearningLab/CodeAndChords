
/** @file
 @ingroup bindings_java

 @brief Example that shows how to play sine waves using JPortAudio.
*/
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
//package portaudio;
=======
package com.portaudio;
>>>>>>> 2f3fbba... Reorganization - each machine has its own folder; Mac binaries compiled
=======
//package portaudio;
>>>>>>> 253f6a1... 32 and 64 bit dll's
=======
//package portaudio;
>>>>>>> d4b45fefb4875773f6f3f2fcfe6ccf11f2d52aa6

import com.portaudio.TestBasic.SineOscillator;

public class PlaySine
{
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD

=======
/*
>>>>>>> 253f6a1... 32 and 64 bit dll's
=======

>>>>>>> 542b456... Working PortAudio Java bindings in Eclipse on Windows
=======

>>>>>>> d4b45fefb4875773f6f3f2fcfe6ccf11f2d52aa6
	public static void main( String[] args )
	{
		PlaySine player = new PlaySine();
		player.play();
	}
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD

=======
>>>>>>> 2f3fbba... Reorganization - each machine has its own folder; Mac binaries compiled
=======
	*/
>>>>>>> 253f6a1... 32 and 64 bit dll's
=======

>>>>>>> 542b456... Working PortAudio Java bindings in Eclipse on Windows
=======

>>>>>>> d4b45fefb4875773f6f3f2fcfe6ccf11f2d52aa6
	/**
	 * Write a sine wave to the stream.
	 * @param stream
	 * @param framesPerBuffer
	 * @param numFrames
	 * @param sampleRate
	 */
	private void writeSineData( BlockingStream stream, int framesPerBuffer,
			int numFrames, int sampleRate )
	{
		float[] buffer = new float[framesPerBuffer * 2];
		SineOscillator osc1 = new SineOscillator( 200.0, sampleRate );
		SineOscillator osc2 = new SineOscillator( 300.0, sampleRate );
		int framesLeft = numFrames;
		while( framesLeft > 0 )
		{
			int index = 0;
			int framesToWrite = (framesLeft > framesPerBuffer) ? framesPerBuffer
					: framesLeft;
			for( int j = 0; j < framesToWrite; j++ )
			{
				buffer[index++] = (float) osc1.next();
				buffer[index++] = (float) osc2.next();
			}
			stream.write( buffer, framesToWrite );
			framesLeft -= framesToWrite;
		}
	}

	/**
	 * Create a stream on the default device then play sine waves.
	 */
	public void play()
	{
		PortAudio.initialize();

		// Get the default device and setup the stream parameters.
		int deviceId = PortAudio.getDefaultOutputDevice();
		DeviceInfo deviceInfo = PortAudio.getDeviceInfo( deviceId );
		double sampleRate = deviceInfo.defaultSampleRate;
		System.out.println( "  deviceId    = " + deviceId );
		System.out.println( "  sampleRate  = " + sampleRate );
		System.out.println( "  device name = " + deviceInfo.name );

		StreamParameters streamParameters = new StreamParameters();
		streamParameters.channelCount = 2;
		streamParameters.device = deviceId;
		streamParameters.suggestedLatency = deviceInfo.defaultLowOutputLatency;
		System.out.println( "  suggestedLatency = "
				+ streamParameters.suggestedLatency );

		int framesPerBuffer = 256;
		int flags = 0;
		
		// Open a stream for output.
		BlockingStream stream = PortAudio.openStream( null, streamParameters,
				(int) sampleRate, framesPerBuffer, flags );

		int numFrames = (int) (sampleRate * 4); // enough for 4 seconds

		stream.start();

		writeSineData( stream, framesPerBuffer, numFrames, (int) sampleRate );

		stream.stop();
		stream.close();

		PortAudio.terminate();
		System.out.println( "JPortAudio test complete." );
	}

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
=======
	public static void main( String[] args )
	{
		PlaySine player = new PlaySine();
		player.play();
	}
>>>>>>> 2f3fbba... Reorganization - each machine has its own folder; Mac binaries compiled
=======
>>>>>>> 253f6a1... 32 and 64 bit dll's
=======
>>>>>>> d4b45fefb4875773f6f3f2fcfe6ccf11f2d52aa6
}
