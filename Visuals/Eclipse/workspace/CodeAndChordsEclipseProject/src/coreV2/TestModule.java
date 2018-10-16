package coreV2;

import controlP5.ControlEvent;
import core.input.MidiStreamInput;
import processing.core.PApplet;
import processing.event.KeyEvent;

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
	}

	public void draw()
	{
		int[] RGB = this.driver.getCurrentColor(0);

		int r = RGB[0];
		int g = RGB[1];
		int b = RGB[2];

		this.fill(r, g, b);
		this.driver.getCanvas().background();
	}


}
