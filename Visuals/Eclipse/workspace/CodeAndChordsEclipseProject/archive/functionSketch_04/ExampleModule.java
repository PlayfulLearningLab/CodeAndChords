package functionSketch_04;

import core.Shape;
import processing.core.PApplet;
import processing.core.PShape;

public class ExampleModule extends PApplet { 
	
	
	private Shape shape = new Shape(this, 300);	

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
		PShape pShape = shape.getPShape();
		shape(pShape, 400, 250);
	}
	
	
}
