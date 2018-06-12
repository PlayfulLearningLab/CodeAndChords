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
		
	}


	@Override
	public void moduleDraw() 
	{
		this.driver.background(0);
	}




}
