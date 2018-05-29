package coreV2;

import processing.core.PApplet;

public class TestModule extends ModuleOutline
{	
	public static void main(String[] args)
	{
		new TestModule();
	}


	@Override
	public void moduleSetup() 
	{
		System.out.println("moduleSetup() code here");
	}


	@Override
	public void moduleDraw() 
	{
		System.out.println("moduleDraw() code here");

		try 
		{
			Thread.sleep(1000);
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}




}
