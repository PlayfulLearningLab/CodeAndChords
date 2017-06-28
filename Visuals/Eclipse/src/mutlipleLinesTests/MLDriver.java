package mutlipleLinesTests;

import processing.core.PApplet;
import processing.sound.AudioIn;

public class MLDriver extends PApplet {

	Test1	testInput;
	
	public static void main(String[] args)
	{
		PApplet.main("mutlipleLinesTests.MLDriver");
	} // main
	
	public void settings()
	{
		size(700, 500);
	}
	
	public void setup()
	{
		
		this.testInput	= new Test1(this);
	} // setup
	
	public void draw()
	{
		this.testInput.ampEllipse();
	} // draw
}
