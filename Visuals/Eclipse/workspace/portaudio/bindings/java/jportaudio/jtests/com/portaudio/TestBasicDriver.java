package com.portaudio;

public class TestBasicDriver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TestBasic	test	= new TestBasic();
		try {
			test.testRecordPlayShort();
			test.testRecordPlayFloat();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
