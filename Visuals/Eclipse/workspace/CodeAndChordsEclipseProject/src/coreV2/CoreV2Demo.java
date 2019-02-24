package coreV2;

import processing.core.PApplet;

public class CoreV2Demo extends PApplet
{
	private ModuleDriver driver;
	
	public static void main(String[] args)
	{
		PApplet.main("coreV2.CoreV2Demo");
	}
	
	public void settings()
	{
		size(925, 520);
	}
	
	public void setup()
	{
		this.driver = new ModuleDriver(this);
	}
	
	public void draw()
	{

	}
	
}
