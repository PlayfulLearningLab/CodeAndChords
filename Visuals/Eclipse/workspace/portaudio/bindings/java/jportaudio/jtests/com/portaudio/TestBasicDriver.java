package com.portaudio;

public class TestBasicDriver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TestBasic	test	= new TestBasic();
		try {
<<<<<<< HEAD
<<<<<<< HEAD
			test.testRecordPlayShort();
=======
>>>>>>> b3e9caa... AudioServer can get data, but not continuously
=======
			test.testRecordPlayShort();
>>>>>>> 01202f7... PortAudioAudioIO runs smoothly, if not completely correctly
			test.testRecordPlayFloat();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
