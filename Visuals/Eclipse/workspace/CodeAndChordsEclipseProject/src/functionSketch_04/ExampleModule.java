package functionSketch_04;

import processing.core.PApplet;

public class ExampleModule extends PApplet { 
	
	
	private Shape shape = new Shape(this);
	

	public static void main(String[] args)
	{
		PApplet.main("functionSketch_04.ExampleModule");
	}
	
	public void settings()
	{
		this.size(925, 520);
	}
	
	public void setup()
	{
		this.background(0);
		shape.setCurrentShape("supershape", new float[] {1,1,5,5,1,1,1});
	}
	
	public void draw()
	{
		shape.drawShape();
	}
	
	
}
