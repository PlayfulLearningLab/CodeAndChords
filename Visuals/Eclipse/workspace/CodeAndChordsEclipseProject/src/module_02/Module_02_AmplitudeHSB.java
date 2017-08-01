package module_02;

import java.awt.Color;

import controlP5.ControlEvent;
import controlP5.ControlListener;
import core.Input;
import core.ModuleTemplate02;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PShape;
import core.Shape;

public class Module_02_AmplitudeHSB extends PApplet {

	private Input				input;
	private ModuleTemplate02	moduleTemplate;
	
	private Shape     			shape;
	
	private PShape 				shapeMenuFadedBackground;

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
		
		// TODO - might not be necessary: -- yep, if it's in there, the shape starts gray.
//		this.moduleTemplate.setCurHueColorRangeColorAdd(0);

		this.textSize(32);
				
		this.shapeMenuFadedBackground = this.createShape(PConstants.RECT, 0, 0, 925, 520);
		Color fadedBlack = new Color(0, 0, 0, .5f);
		this.shapeMenuFadedBackground.setFill(fadedBlack.getRGB());

		// create the shape
		
		//Ask Emily:  What does this do?
		this.shapeMode(CENTER);
		//		this.shape			= createShape(ELLIPSE, (this.width - this.moduleTemplate.getLeftEdgeX()) / 2, this.height / 2, this.width * (this.moduleTemplate.getShapeSize() / 100), this.height * (this.moduleTemplate.getShapeSize() / 100));
		//		this.shapeCenter	= (this.width - this.moduleTemplate.getLeftEdgeX()) / 2;
		
		this.shape = new Shape(this);
		shape.setCurrentShape("supershape", new float[] {1,1,5,5,1,1,1});
		
	} // setup

	public void draw()
	{
//		System.out.println("this.input.getAmplitude() = " + this.input.getAmplitude());
		
		// The following line is necessary so that key press shows the menu button
		if (keyPressed == true) 
		{
			this.moduleTemplate.setMenuVal();
		}

		background(this.moduleTemplate.getCanvasColor()[0], this.moduleTemplate.getCanvasColor()[1], this.moduleTemplate.getCanvasColor()[2]);

		System.out.println("canvasColor = rgb(" + this.moduleTemplate.getCanvasColor()[0] + 
				", " + this.moduleTemplate.getCanvasColor()[1] + ", " + this.moduleTemplate.getCanvasColor()[2] + ")");
		
		// pick the appropriate color by checking amplitude threshold
		float	curAmp		= this.input.getAmplitude();
		int		goalHuePos	= 0;
		
		this.moduleTemplate.applyThresholdSBModulate(curAmp);

		for(int i = 0; i < this.moduleTemplate.getThresholds().length; i++)
		{
			if(curAmp > this.moduleTemplate.getThresholds()[i]) {
				goalHuePos	= i;
			} // if
			
//			System.out.println("curAmp = " + curAmp);
		} // for

//		System.out.println("curAmp " + curAmp + " was over thresholds[" + goalHuePos + "]: " + this.moduleTemplate.getThresholds()[goalHuePos]);

		this.moduleTemplate.fade(goalHuePos);

		// draw the shape
		this.drawShape();


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
		
	} // draw

	private void drawShape()
	{	
		
		float[]	curHue	= this.moduleTemplate.getCurHue();
		this.fill(curHue[0], curHue[1], curHue[2]);

		float	shapeX		= ((this.width - this.moduleTemplate.getLeftEdgeX()) / 2) + this.moduleTemplate.getLeftEdgeX();
		float	shapeWidth	= (this.width - this.moduleTemplate.getLeftEdgeX()) * (this.moduleTemplate.getShapeSize() / 100);
		float	shapeHeight	= this.height * (this.moduleTemplate.getShapeSize() / 100);
		
		this.shapeMode(CORNER);
		PShape pShape = this.shape.getPShape();
		pShape.beginShape();
		pShape.fill(curHue[0], curHue[1], curHue[2]);
		pShape.endShape();
		this.shape(pShape, shapeX, this.height/2);
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
	
	private void drawShapeMenu()
	{
		shape(this.shapeMenuFadedBackground);
	}
	

} // Module_03_AmplitudeHSB
