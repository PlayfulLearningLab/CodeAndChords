package module_02;

import java.awt.Color;

import controlP5.ControlEvent;
import controlP5.ControlListener;
import core.Input;
import core.ModuleTemplate02;
import processing.core.PApplet;
import processing.core.PShape;
import core.Shape;

public class Module_02_AmplitudeHSB extends PApplet {

	private Input				input;
	private ModuleTemplate02	moduleTemplate;
	
	private float[][]			superShapes;
	
	private Shape     			shape;
	
	private float  				x;
	private float				y;
	

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
		
		this.shape = new Shape(this);
		this.superShapes = new float[][] 
				{
					new float[] { 1, 1, 0, 0, 1, 1, 1 },
					new float[] { 1, 1, 5, 5, 1, 1, 1 },
					new float[] { 2, 2, 3, 3, 1, 1, 1 },
					new float[] { .7f, .7f, 8, 8, 1, 1, 1},
					new float[] { 1.4f, 1.4f, 4, 4, .3f, .5f, .7f }
				};
		
		for(int i = 0; i < 5; i++)
		{
			this.shape.setShapeIndex(i);
			shape.setCurrentShape("supershape", this.superShapes[i]);
		}
		
		
		this.moduleTemplate	= new ModuleTemplate02(this, this.input, "Module_02_AmplitudeHSB");
		
		this.x = ((this.width - this.moduleTemplate.getLeftEdgeX()) / 2) + this.moduleTemplate.getLeftEdgeX();
		this.y = this.height/2;
		

		this.textSize(32);
				
		
		
		//this.shapeMenuFadedBackground = this.createShape(this.RECT, 0, 0, 925, 520);
		//Color fadedBlack = new Color(0, 0, 0, .5f);
		//this.shapeMenuFadedBackground.setFill(fadedBlack.getRGB());

		// create the shape
		
		//Ask Emily:  What does this do?
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

		for(int i = 0; i < this.moduleTemplate.getThresholds().length; i++)
		{
			if(curAmp > this.moduleTemplate.getThresholds()[i]) {
				goalHuePos	= i;
			} // if
		} // for

//		System.out.println("curAmp " + curAmp + " was over thresholds[" + goalHuePos + "]: " + this.moduleTemplate.getThresholds()[goalHuePos]);

		this.moduleTemplate.fade(goalHuePos);


//		this.fill(255);
//		this.text(goalHuePos, this.moduleTemplate.getLeftEdgeX() + ((this.width - this.moduleTemplate.getLeftEdgeX()) / 2), this.height / 2);

		//		System.out.println("this.input.getAmplitude() = " + this.input.getAmplitude());
		
		if(this.moduleTemplate.isShowScale())
		{
			// draws the legend along the bottom of the screen:
			this.moduleTemplate.legend(goalHuePos);
		} // if showScale
		
		if(this.moduleTemplate.getShapeMenuIsOpen())
		{
			this.drawShapeMenu();
		}
		else
		{
			this.drawShape();
		}
		
		
	} // draw

	private void drawShape()
	{	
		
		float[]	curHue	= this.moduleTemplate.getCurHue();
		this.fill(curHue[0], curHue[1], curHue[2]);

		float	shapeWidth	= (this.width - this.moduleTemplate.getLeftEdgeX()) * (this.moduleTemplate.getShapeSize() / 100);
		float	shapeHeight	= this.height * (this.moduleTemplate.getShapeSize() / 100);
		
		this.shapeMode(CORNER);
		PShape pShape = this.shape.getPShape();
		pShape.beginShape();
		pShape.fill(curHue[0], curHue[1], curHue[2]);
		pShape.endShape();
		this.shape(pShape, this.x, this.y);
		this.shapeMode(CENTER);
		
		
		//this.stroke(Color.red.getRGB());
		//this.strokeWeight(10);
		//this.point(shapeX, this.height/2);
		
		

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
	
	public Shape getShape()
	{
		return this.shape;
	}
	

	private void drawShapeMenu()
	{
		this.stroke(0);
		this.strokeWeight(1);
		this.rect(175, 52, 740, 416);
		
		PShape ps = this.shape.getScaledPShape(new float[] {-1, 1, -.8f, .8f});
		
		ps.beginShape();
		ps.stroke(255);
		ps.fill(255);
		ps.endShape();
		
		float xMod = PApplet.map(this.x, 0, 925, 175, 915);
		float yMod = PApplet.map(this.y, 0, 520, 52, 468);
		
		this.shapeMode(CORNER);
		this.shape(ps, xMod, yMod);
		this.shapeMode(CENTER);
		
		//For space testing
/*
		this.strokeWeight(.5f);
		this.line(175, 52, 915, 468);
		
		this.stroke(Color.RED.getRGB());
		this.strokeWeight(7);
		this.point(xMod, yMod);
*/
		
		//set stroke back to normal
		this.stroke(0);
		this.strokeWeight(1);
		
		
	}
	
	public void setSuperShape(float val, int shapeNum, int paramNum)
	{
		this.superShapes[shapeNum][paramNum] = val;
	}
	
	public float[] getCurrentSuperShape()
	{
		return this.superShapes[this.shape.getShapeIndex()];
	}
	
	

} // Module_03_AmplitudeHSB
