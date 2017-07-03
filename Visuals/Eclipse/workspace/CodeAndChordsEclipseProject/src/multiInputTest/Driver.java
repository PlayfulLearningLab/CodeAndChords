package multiInputTest;

import com.portaudio.*;

public class Driver {

	public static void main(String[] args) {
		PortAudio.initialize();
		
		System.out.println("PortAudio.getDeviceCount() = " + PortAudio.getDeviceCount());
		
		PortAudio.terminate();

	} // main

} // Driver
