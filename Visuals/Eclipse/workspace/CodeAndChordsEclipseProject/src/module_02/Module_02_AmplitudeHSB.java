package module_02;

import java.awt.Color;

import controlP5.ControlEvent;
import controlP5.ControlListener;
import core.Input;
import core.ModuleTemplate02;
import core.PortAudioAudioIO;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PShape;
import core.Shape;
import net.beadsproject.beads.core.AudioContext;

public class Module_02_AmplitudeHSB extends PApplet {

	private	DisposeHandler		disposeHandler;
	
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
		this.disposeHandler	= new DisposeHandler(this);
		
		// This uses the PortAudioAudioIO by default...
		this.input	= new Input();
		// ...but to use JavaSoundAudioIO rather than PortAudio:
		//		this.input	= new Input(2, new AudioContext());

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

		// pick the appropriate color by checking amplitude threshold
		float	curAmp		= this.input.getAmplitude();
		int		goalHuePos	= 0;


		for(int i = 0; i < this.moduleTemplate.getThresholds().length; i++)
		{
			if(curAmp > this.moduleTemplate.getThresholds()[i]) {
				goalHuePos	= i;
			} // if

			//			System.out.println("curAmp = " + curAmp);
		} // for

		//		System.out.println("curAmp " + curAmp + " was over thresholds[" + goalHuePos + "]: " + this.moduleTemplate.getThresholds()[goalHuePos]);

		// Now this threshold application happens in fade:
//		this.moduleTemplate.applyThresholdSBModulate(curAmp);
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

		int[]	curHue	= this.moduleTemplate.getCurHue();
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

	/**
	 * 08/01/2017
	 * Emily Meuer
	 * 
	 * Class to stop the Input (which needs to stop the AudioContext,
	 * because it needs to stop the AudioIO, esp. when it's using the PortAudioAudioIO,
	 * which needs to call PortAudio.terminate to avoid a weird set of 
	 * NoClassDefFoundError/ClassNotFoundException/BadFileDescriptor errors that will happen occassionaly on start-up).
	 * 
	 * Taken from https://forum.processing.org/two/discussion/579/run-code-on-exit-follow-up
	 *
	 */
	public class DisposeHandler {

//		PApplet	pa;
		
		Module_02_AmplitudeHSB	module;

		DisposeHandler(PApplet pa)
		{
			module	= (Module_02_AmplitudeHSB)pa;
			pa.registerMethod("dispose", this);
		}

		public void dispose()
		{
			this.module.input.stop();
		}
	} // DisposeHandler


} // Module_03_AmplitudeHSB