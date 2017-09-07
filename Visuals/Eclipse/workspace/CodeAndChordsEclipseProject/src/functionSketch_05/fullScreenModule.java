package functionSketch_05;

import processing.core.PApplet;

public class fullScreenModule extends PApplet
{
	static Object	parent;	
	
	public static void startNewPApplet(Object parent)
	{
		PApplet.main("functionSketch_05.fullScreenModule");
		fullScreenModule.parent = parent;
	}
	
	public void settings()
	{
		this.fullScreen();
	}
	
	public void setup()
	{
		((DynamicResize) fullScreenModule.parent).setDisplayApplet(this);
		
		//background(0);
	}
	
	public void draw()
	{
		((DynamicResize) fullScreenModule.parent).drawShape();
		System.out.println("1");
		

	}
	
	public void mousePressed()
	{
		this.getApplet().exit();
	}
	
	public PApplet getApplet()
	{
		return this;
	}
	
}
