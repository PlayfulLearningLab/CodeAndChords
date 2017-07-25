package module_02;

import controlP5.ControlEvent;
import controlP5.ControlListener;
import core.Input;
import processing.core.PApplet;
import processing.core.PShape;

public class Module_02_AmplitudeHSB extends PApplet {

	private Input				input;
	private ModuleTemplate02	moduleTemplate;

	//	private PShape 	shape;
	//	int		shapeCenter;

//	float[]	thresholds;

	public static void main(String[] args)
	{
		PApplet.main("module_02.Module_02_AmplitudeHSB");
	} // main

	public void settings()
	{
		size(925, 520);
	} // settings

	public void setup()
	{		
		this.input	= new Input();
		this.moduleTemplate	= new ModuleTemplate02(this, this.input, "Module_02_AmplitudeHSB");

		// set amplitude thresholds
		this.moduleTemplate.thresholds	= new float[] {
				2,		// piano
				100,	// mezzo piano
				200,	// mezzo forte
				500	//forte
		}; // thresholds

		// Define them some default colors
		this.moduleTemplate.setColors(new float[][] {
			new float[] { 255, 0, 0 },
			new float[] { 0, 255, 0 },
			new float[] { 0, 0, 255 },
			new float[] { 150, 50, 150 }
		});

		this.textSize(32);

		// create the shape
		this.shapeMode(CENTER);
		//		this.shape			= createShape(ELLIPSE, (this.width - this.moduleTemplate.getLeftEdgeX()) / 2, this.height / 2, this.width * (this.moduleTemplate.getShapeSize() / 100), this.height * (this.moduleTemplate.getShapeSize() / 100));
		//		this.shapeCenter	= (this.width - this.moduleTemplate.getLeftEdgeX()) / 2;

		System.out.println("(this.moduleTemplate.getShapeSize() / 100) = " + ((float)this.moduleTemplate.getShapeSize() / 100f));
	} // setup

	public void draw()
	{
//		System.out.println("this.input.getAmplitude() = " + this.input.getAmplitude());
		
		// The following line is necessary so that key press shows the menu button
		if (keyPressed == true) 
		{
			this.moduleTemplate.setMenuVal();
		}

		background(150);

		// pick the appropriate color by checking amplitude threshold
		float	curAmp		= this.input.getAmplitude();
		int		goalHuePos	= 0;

		for(int i = 0; i < this.moduleTemplate.thresholds.length; i++)
		{
			if(curAmp > this.moduleTemplate.thresholds[i]) {
				goalHuePos	= i;
			} // if
		} // for

		this.moduleTemplate.fade(goalHuePos);

		// draw the shape
		this.drawShape();

		this.fill(255);
		this.text(goalHuePos, this.moduleTemplate.getLeftEdgeX() + ((this.width - this.moduleTemplate.getLeftEdgeX()) / 2), this.height / 2);

		//		System.out.println("this.input.getAmplitude() = " + this.input.getAmplitude());
		
		this.moduleTemplate.legend();
	} // draw

	private void drawShape()
	{	
		float[]	curHue	= this.moduleTemplate.getCurHue();
		this.fill(curHue[0], curHue[1], curHue[2]);

		float	shapeX		= ((this.width - this.moduleTemplate.getLeftEdgeX()) / 2) + this.moduleTemplate.getLeftEdgeX();
		float	shapeWidth	= (this.width - this.moduleTemplate.getLeftEdgeX()) * (this.moduleTemplate.getShapeSize() / 100);
		float	shapeHeight	= this.height * (this.moduleTemplate.getShapeSize() / 100);

		this.ellipse(shapeX, this.height / 2, shapeWidth, shapeHeight);

		// Began with PShape, but decided that that is not worth it at the moment:
		/*		this.shape.beginShape(ELLIPSE);

		// adjust any shape parameters
/*		this.shape.width	= (this.width - this.moduleTemplate.getLeftEdgeX()) * (this.moduleTemplate.getShapeSize() / 100);
		this.shape.height	= this.height * (this.moduleTemplate.getShapeSize() / 100);
		 */		
		/*		this.shape.scale((this.width - this.moduleTemplate.getLeftEdgeX()) * (this.moduleTemplate.getShapeSize() / 100), this.height * (this.moduleTemplate.getShapeSize() / 100));

		float[]	curHue	= this.moduleTemplate.getCurHue();
		this.shape.fill(curHue[0], curHue[1], curHue[2]);

		this.shape.endShape();

		shape(this.shape);
		 */

	} // drawShape

} // Module_03_AmplitudeHSB
