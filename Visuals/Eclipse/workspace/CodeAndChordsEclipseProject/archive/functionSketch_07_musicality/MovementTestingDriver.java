package functionSketch_07_musicality;

import processing.core.PApplet;

public class MovementTestingDriver extends PApplet
{

	private MovementHelper 	mh;
		
	private int 			nextSampleTime;
	
	public static void main(String[] args)
	{
		PApplet.main("functionSketch_07_musicality.MovementTestingDriver");
	}

	public void settings()
	{
		size(925, 520);
	}

	public void setup()
	{
		this.mh = new MovementHelper(this, 2);
		this.nextSampleTime = 0;
	}

	public void draw()
	{
		if(this.millis() > this.nextSampleTime)
		{
			this.mh.input(this.mouseY);
			this.nextSampleTime += 2000;
		}
		
		this.background(0);
		this.noStroke();
		this.fill(150);

		this.rect(0, this.height, this.width, this.convertHeight(this.mh.getVal()));
		

		//draw the bar height
		this.strokeWeight(10);
		this.stroke(70, 5, 200);
		this.line(0, this.mh.getLastInput(), this.width, this.mh.getLastInput());

		//draw the bar height
		this.strokeWeight(6);
		this.stroke(170, 5, 100);
		this.line(0, this.mouseY, this.width, this.mouseY);

	}

	private int convertHeight(int input)
	{
		return 0 - (this.height - input);
	}



}
