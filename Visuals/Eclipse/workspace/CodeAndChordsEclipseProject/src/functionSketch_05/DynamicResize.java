package functionSketch_05;


/*
 * change the parent instance variables in the plug in classes when full screen is opened
 * 
 * 
 */
import processing.core.PApplet;

public class DynamicResize extends PApplet 
{

	PApplet displayApplet;
	boolean running = true;

	int i = 1;
	float time = 0;

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
		this.displayApplet = this;

		background(0);
		stroke(255);
	}

	public void draw()
	{
		if(this.running) this.drawShape();

		System.out.println(this.displayApplet);
	}

	public void drawShape()
	{
		this.displayApplet.stroke(255);
		this.displayApplet.background(0);
		this.displayApplet.ellipse(this.displayApplet.width/2, this.displayApplet.height/2, i%400 , i%400);
		//this.displayApplet.ellipse(500, 250, i%100 , i%100);

		i+= 2;
	}

	public void setDisplayApplet(PApplet applet)
	{
		this.displayApplet = applet;
	}

	public void mousePressed()
	{
		fullScreenModule.startNewPApplet(this);
		this.running = false;

		//this.displayApplet = fsm.getApplet();
		//System.out.println(this.displayApplet.millis());

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

}
