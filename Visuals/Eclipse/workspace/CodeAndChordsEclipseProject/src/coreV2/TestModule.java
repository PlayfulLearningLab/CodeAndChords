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
		this.size(925, 520);
	}
	
	public void setup()
	{
		this.driver = new ModuleDriver(this);
		System.out.println("done");
	}
	
	public void draw()
	{
		this.fill(0);
		this.driver.getCanvas().background();
	}


}
