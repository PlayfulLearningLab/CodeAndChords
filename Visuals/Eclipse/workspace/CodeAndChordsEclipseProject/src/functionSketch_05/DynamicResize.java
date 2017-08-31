package functionSketch_05;

import processing.core.PApplet;

public class DynamicResize extends PApplet 
{

	PApplet p1;
	
	
	public static void main(String args[])
	{
		PApplet.main("functionSketch_05.DynamicResize");
	}
	
	public void settings()
	{
			size(925,520);		
	}
	
	public void setup()
	{
		
		
	}
	
	public void draw()
	{

	}
	
	public void mousePressed()
	{
		fullScreenModule fsm = new fullScreenModule();
		fsm.startNewPApplet();
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	
}
