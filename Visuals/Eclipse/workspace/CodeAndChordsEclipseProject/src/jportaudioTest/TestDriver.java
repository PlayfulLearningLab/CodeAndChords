package jportaudioTest;

import com.portaudio.PortAudio;

public class TestDriver {
	
	PortAudio	portAudio;

	public static void main(String[] args) {
		TestDriver	testDriver	= new TestDriver();
		testDriver.test();
	}
	
	public int test()
	{
		PortAudio.initialize();
		
		System.out.println("portAudio.getDefaultInputDevice() = " + PortAudio.getDefaultInputDevice());
		
		PortAudio.terminate();
		return 0;
	} // test

} // TestDriver
