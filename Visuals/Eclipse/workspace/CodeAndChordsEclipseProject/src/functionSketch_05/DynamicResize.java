package functionSketch_05;

import processing.core.PApplet;

public class DynamicResize extends PApplet 
{

	private int fullScreenWidth;
	private int fullScreenHeight;
	
	
	public static void main(String args[])
	{
		PApplet.main("functionSketch_05.DynamicResize");
	}
	
	public void settings()
	{
		this.fullScreen();
		
	}
	
	public void setup()
	{
		this.fullScreenWidth = this.width;
		this.fullScreenHeight = this.height;
		
		surface.setResizable(true);
		
		surface.setSize(925, 520);
		
		System.out.println(this.width + "    " + this.height);
		
		
	}
	
	public void draw()
	{
		//surface.setSize(this.width - 1, this.height - 1);
		if(this.height < 300)
		{
			this.fullScreen();
			while(true);
		}
	}
	
}
