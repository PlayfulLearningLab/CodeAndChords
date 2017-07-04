package multiInputTest;

import org.jaudiolibs.audioservers.AudioConfiguration;

import com.portaudio.*;
import processing.core.*;

import core.PortAudioAudioServer;
import net.beadsproject.beads.core.AudioContext;

public class Driver extends PApplet {
	
	PortAudioAudioServer paas;
	
	public static void main(String[] args) {
		PApplet.main("multiInputTest.Driver");
	} // main
	
	public void setup()
	{
		AudioContext ac	= new AudioContext();

		paas	= new PortAudioAudioServer(new AudioConfiguration(44100, 2, 2, 512, true));
		try {
			paas.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		Thread.sleep(2000);
		System.out.println("Driver: done sleeping!");
		
		
		paas.shutdown();
		System.out.println("paas.isActive = " + paas.isActive());
	} // setup
	
	public void run()
	{
		if(this.mousePressed)
		{
			paas.shutdown();
			println("tried to shutdown");
		} // if

	} // run

} // Driver
