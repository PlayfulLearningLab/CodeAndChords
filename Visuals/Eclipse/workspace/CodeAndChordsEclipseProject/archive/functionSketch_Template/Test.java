package functionSketch_Template;

import controlP5.ControlEvent;
import processing.core.PApplet;

public class Test extends FSTemplate {

	
	
	public static void main(String[] args)
	{
		PApplet.main("functionSketch_Template.Test");

	}
	
	public void settings()
	{
		this.parentSettings();
	}
	
	public void setup()
	{
		this.parentSetup();
	}
	
	public void draw()
	{
		this.parentDraw();
	}
	
	public void controlEvent(ControlEvent theEvent)
	{
		String name = theEvent.getName();
		
		switch(name)
		{
			//add cases here
		
		
			default:
				this.parentControlEvent(name);
				break;
		}//switch
		
	}
	
	

}
