package module_02;

import java.awt.Color;

import controlP5.ControlEvent;
import controlP5.ControlListener;
import controlP5.ControlP5;
import core.Input;
import core.ModuleTemplate02;
import core.PortAudioAudioIO;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PShape;
import core.Shape;
import core.ShapeEditor;
import core.ShapeEditorInterface;
import net.beadsproject.beads.core.AudioContext;

public class Module_02_AmplitudeHSB extends PApplet implements ShapeEditorInterface {

	private	DisposeHandler		disposeHandler;

	private Input				input;
	private ModuleTemplate02	moduleTemplate;

	private Shape     			shape;

	private ShapeEditor			shapeEditor;

	private float  				x;
	private float				y;
	private float 				rotation;

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
		//this.input	= new Input();
		this.input    = new Input(2, new AudioContext());

		this.shape = new Shape(this);
		float[][] superShapes = new float[][] 
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
					shape.setCurrentShape("supershape", superShapes[i]);
				}

				this.shapeEditor = new ShapeEditor(this, this.shape, 925, 520);
				this.shapeEditor.setIsRunning(false);

				this.moduleTemplate	= new ModuleTemplate02(this, this.input, "Module_02_AmplitudeHSB");

				this.x = ((this.width - this.moduleTemplate.getLeftEdgeX()) / 2) + this.moduleTemplate.getLeftEdgeX();
				this.y = this.height/2;
				this.rotation = 0;
				
				System.out.println("x val = " + this.x  + " and y val = " + this.y);


				// TODO - might not be necessary: -- yep, if it's in there, the shape starts gray.
				//		this.moduleTemplate.setCurHueColorRangeColorAdd(0);

				this.textSize(32);				


				//this.shapeMenuFadedBackground = this.createShape(this.RECT, 0, 0, 925, 520);
				//Color fadedBlack = new Color(0, 0, 0, .5f);
				//this.shapeMenuFadedBackground.setFill(fadedBlack.getRGB());

				// create the shape

				//this.shapeMode(CENTER);
				//		this.shape			= createShape(ELLIPSE, (this.width - this.moduleTemplate.getLeftEdgeX()) / 2, this.height / 2, this.width * (this.moduleTemplate.getShapeSize() / 100), this.height * (this.moduleTemplate.getShapeSize() / 100));
				//		this.shapeCenter	= (this.width - this.moduleTemplate.getLeftEdgeX()) / 2;

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


		//		this.fill(255);
		//		this.text(goalHuePos, this.moduleTemplate.getLeftEdgeX() + ((this.width - this.moduleTemplate.getLeftEdgeX()) / 2), this.height / 2);

		//		System.out.println("this.input.getAmplitude() = " + this.input.getAmplitude());

		/*
		 * need:
		 * 	drawShape()  		- draws the main shape
		 * 	runSE()     		- should be run every cycle, draws shape editor
		 * 	legend(goalHuePos)	- draws legend
		 * 
		 * 	if(this.moduleTemplate.isShowScale()
		 */

		this.shape.setShapeScale(this.moduleTemplate.getShapeSize());

		if(!this.shapeEditor.getIsRunning())
		{
			this.drawShape();
		}

		if(this.moduleTemplate.isShowScale())
		{
			// draws the legend along the bottom of the screen:
			this.moduleTemplate.legend(goalHuePos);

		} // if showScale
		
			float[] param = this.shapeEditor.runSE(this.x, this.y, this.rotation);

			if(param != null)
			{
				this.x = param[0];
				this.y = param[1];
				this.rotation = param[2];
			}


	} // draw

	private void drawShape()
	{	

		float[]	curHue	= this.moduleTemplate.getCurHue();
		//this.fill(curHue[0], curHue[1], curHue[2]);
		this.fill(255);

		float	shapeWidth	= (this.width - this.moduleTemplate.getLeftEdgeX()) * (this.moduleTemplate.getShapeSize() / 100);
		float	shapeHeight	= this.height * (this.moduleTemplate.getShapeSize() / 100);

		//this.shapeMode(CORNER);
		PShape pShape;
		if(this.moduleTemplate.getLeftEdgeX() == 0) pShape = this.shape.getPShape();
		else pShape = this.shape.getScaledPShape(new float[] {925, (925 - this.moduleTemplate.getLeftEdgeX()), 1, 1});


		pShape.beginShape();
		pShape.fill(curHue[0], curHue[1], curHue[2]);
		pShape.stroke(curHue[0], curHue[1], curHue[2]);
		pShape.rotate(this.rotation);
		pShape.endShape();

		if(this.moduleTemplate.getLeftEdgeX() == 0) this.shape(pShape, this.x, this.y);
		else this.shape(pShape, PApplet.map(this.x, 0, 925, this.moduleTemplate.getLeftEdgeX(), 925), this.y);

		//this.shapeMode(CENTER);


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

	public void setShapeEditorRunning(boolean isRunning)
	{
		this.shapeEditor.setIsRunning(isRunning);
	}

	public void mouseClicked()
	{
		float[] 	fArray;
		float		scale;
		ControlP5	seControlP5;

		if(this.shapeEditor.getIsRunning() && this.shapeEditor.getCP5().isVisible())
		{
			System.out.println("Mouse clicked!!!!  X = " + this.mouseX + " Y = " + this.mouseY);
			
			fArray = this.shapeEditor.getSEWindowSizeAndPlace();
			seControlP5 = this.shapeEditor.getCP5();
			scale = this.shapeEditor.getScale();
			
			this.x = PApplet.map(this.mouseX - (fArray[0]), 0, fArray[2], 0, 925);
			this.y = PApplet.map(this.mouseY - fArray[1], 0, fArray[3], 0, 520);
			seControlP5.getController("xPos").setValue(this.x);
			seControlP5.getController("yPos").setValue(this.y);
			
			System.out.println(fArray[0]);
			System.out.println(fArray[1]);
			System.out.println(fArray[2]);
			System.out.println(fArray[3]);
		}
		else if(this.moduleTemplate.getLeftEdgeX() == 0)
		{
			this.x = this.mouseX;
			this.y = this.mouseY;
		}
		else if(this.mouseX > this.moduleTemplate.getLeftEdgeX())
		{
			this.x = PApplet.map(this.mouseX, this.moduleTemplate.getLeftEdgeX(), 925, 0, 925);
			this.y = this.mouseY;
		}
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
