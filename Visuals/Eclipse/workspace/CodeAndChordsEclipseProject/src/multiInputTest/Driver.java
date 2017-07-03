package multiInputTest;

import org.jaudiolibs.audioservers.AudioConfiguration;

import com.portaudio.*;

import core.PortAudioAudioServer;
import net.beadsproject.beads.core.AudioContext;

public class Driver {
	
	
	public static void main(String[] args) {
		
		AudioContext ac	= new AudioContext();

		/*
		 * Constructor for AudioConfiguration (must be passed to AudioServer):
		public AudioConfiguration(float sampleRate,
	            int inputChannelCount,
	            int outputChannelCount,
	            int maxBufferSize,
	            boolean fixedBufferSize)
	            */
		PortAudioAudioServer paas	= new PortAudioAudioServer(new AudioConfiguration(44100, 2, 2, 512, true));
		try {
			paas.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		paas.shutdown();
		System.out.println("paas.isActive = " + paas.isActive());
	} // main

} // Driver
