package multiInputTest;

<<<<<<< HEAD
import org.jaudiolibs.audioservers.AudioConfiguration;

import com.portaudio.*;
import processing.core.*;
import core.PortAudioAudioIO;
import core.PortAudioAudioServer;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.IOAudioFormat;

public class Driver extends PApplet {
	
	PortAudioAudioServer paas;
	
	public static void main(String[] args) {
		PApplet.main("multiInputTest.Driver");
	} // main
	
	public void setup()
	{
		AudioContext ac	= new AudioContext(new PortAudioAudioIO());
		ac.start();
		
		IOAudioFormat	ioAudioFormat	= ac.getAudioFormat();
		System.out.println("ioAudioFormat.bitDepth = " + ioAudioFormat.bitDepth);
/*
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
		*/
	} // setup
	
	public void run()
	{
		if(this.mousePressed)
		{
			paas.shutdown();
			println("tried to shutdown");
		} // if

	} // run
=======
import com.portaudio.*;

public class Driver {

	public static void main(String[] args) {
		PortAudio.initialize();
		
		System.out.println("PortAudio.getDeviceCount() = " + PortAudio.getDeviceCount());
		
		PortAudio.terminate();

	} // main
>>>>>>> 08e6ab7... Added .dylib and lportaudio.jar to Eclipse

} // Driver
