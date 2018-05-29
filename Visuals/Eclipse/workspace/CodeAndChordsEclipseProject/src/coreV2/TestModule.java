package coreV2;

import processing.core.PApplet;

public class TestModule extends PApplet
{

	private ModuleDriver driver;
	
	
	public static void main(String[] args)
	{
		PApplet.main("coreV2.TestModule");
	}
	
	public void settings()
	{
		this.size(920, 525);
	}
	
	public void setup()
	{
		this.driver = new ModuleDriver(this, 1);
	}
	
	public void draw()
	{
		this.driver.runModule();
	}
	
	
}
