package module_02;

import java.awt.Color;

import controlP5.ControlEvent;
import controlP5.ControlListener;
import controlP5.ControlP5;
import core.Input;
import core.Module;
import core.ModuleMenu;
import core.PortAudioAudioIO;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PShape;
import core.Shape;
import core.ShapeEditor;
import core.ShapeEditorInterface;
import core.Archive_ModuleTemplate.ModuleTemplate02;
import net.beadsproject.beads.core.AudioContext;

public class Module_02_AmplitudeHSB extends Module implements ShapeEditorInterface {

	//	private	DisposeHandler		disposeHandler;

	//	private Input				input;
	//	private ModuleTemplate02	moduleTemplate;

	//	private	ModuleMenu			menu;

	//	private Shape     			shape;

	//	private ShapeEditor			shapeEditor;

	/**	holds the y values for all Controllers	*/
	private	int[]	yVals;

	private	int		currentMenu;
	
	public static void main(String[] args) 
	{
		PApplet.main("module_02.Module_02_AmplitudeHSB");
	} // main
	/*
	public void settings()
	{
		size(925, 520);
	} // settings
	 */
	public void setup()
	{
		//		super.setup();
		//		this.disposeHandler	= new DisposeHandler(this);

		// Not specifying an AudioContext will use the PortAudioAudioIO:
				this.input	= new Input(this);
		//this.input    = new Input(1, new AudioContext(), this);
				
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


				this.shapeEditor = new ShapeEditor(this, this.shape, this, 925, 520);
				this.shapeEditor.setIsRunning(false);

				//				this.moduleTemplate	= new ModuleTemplate02(this, this.input, "Module_02_AmplitudeHSB");
				this.menu	= new ModuleMenu(this, this, this.input, "Module_02_AmplitudeHSB", 4);
				this.menu.setIsRunning(false);

				this.yVals		= new int[18];
				// Seemed like a good starting position, related to the text - but pretty arbitrary:
				this.yVals[0]	= 50;
				int	distance	= (this.height - this.yVals[0]) / this.yVals.length;
				for(int i = 1; i < this.yVals.length; i++)
				{
					this.yVals[i]	= this.yVals[i - 1] + distance;
				} // fill yVals

				// Have to addColorSelect() first so that everything else can access the colors:
				String[]	buttonLabels	= new String[] {
						"Canvas", "1", "2", "3", "4"
				};
				this.menu.addColorSelect(0, new int[] { this.yVals[8] }, buttonLabels, "Color Select", true);

				this.menu.addHideButtons(0, this.yVals[0]);

				this.menu.addARTSliders(0, this.yVals[1], this.yVals[2], this.yVals[3]);

				this.menu.addShapeSizeSlider(0, this.yVals[15]);

				this.menu.addRangeSegments(0, this.yVals[7], 4, 4, "Dynamic\nSegments");

				this.menu.addHSBSliders(0, new int[] { this.yVals[4], this.yVals[5], this.yVals[6], });

				this.menu.addPianoThresholdSlider(0, this.yVals[9]);

				this.menu.addForteThresholdSlider(0, this.yVals[10]);

				int	verticalSpacer	= distance - this.menu.getSliderHeight();
				this.menu.addThresholdSliders(0, yVals[11], verticalSpacer);
				
				this.menu.getInstrument().setADSR(1000, 500, 0, 0);
				this.menu.setBPM(30);

				// TODO - might not be necessary: -- yep, if it's in there, the shape starts gray.
				//		this.moduleTemplate.setCurHueColorRangeColorAdd(0);

				this.textSize(32);		
				
				this.menu.setMenuList(new String[] {"Canvas", "Module Menu", "Shape Editor"});


				//this.shapeMenuFadedBackground = this.createShape(this.RECT, 0, 0, 925, 520);
				//Color fadedBlack = new Color(0, 0, 0, .5f);
				//this.shapeMenuFadedBackground.setFill(fadedBlack.getRGB());

				// create the shape

				//this.shapeMode(CENTER);
				//		this.shape			= createShape(ELLIPSE, (this.width - this.moduleTemplate.getLeftEdgeX()) / 2, this.height / 2, this.width * (this.moduleTemplate.getShapeSize() / 100), this.height * (this.moduleTemplate.getShapeSize() / 100));
				//		this.shapeCenter	= (this.width - this.moduleTemplate.getLeftEdgeX()) / 2);
	} // setup

	public void draw()
	{
		System.out.println("this.input.getAmplitude() = " + this.input.getAmplitude());

		// The following line is necessary so that key press shows the menu button
		if (keyPressed == true) 
		{
			this.menu.setMenuVal();
		}

		background(this.menu.getCanvasColor()[0][0], this.menu.getCanvasColor()[0][1], this.menu.getCanvasColor()[0][2]);

		// pick the appropriate color by checking amplitude threshold
		float	curAmp		= this.input.getAmplitude();
		int		goalHuePos	= 0;


		for(int i = 0; i < this.menu.getThresholds()[0].length; i++)
		{
			if(curAmp > this.menu.getThresholds()[0][i]) {
				goalHuePos	= i;
			} // if

			//			System.out.println("curAmp = " + curAmp);
		} // for

		//		System.out.println("curAmp " + curAmp + " was over thresholds[" + goalHuePos + "]: " + this.moduleTemplate.getThresholds()[goalHuePos]);
		// Now this threshold application happens in fade:
		//		this.moduleTemplate.applyThresholdSBModulate(curAmp);
		this.menu.fade(goalHuePos, 0);


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
		
		if(this.currentMenu != this.menu.getCurrentMenu())
		{
			this.currentMenu = this.menu.getCurrentMenu();
			
			if(this.currentMenu == 0)
			{
				this.menu.setIsRunning(false);
				this.shapeEditor.setIsRunning(false);
			}
			else if(this.currentMenu == 1)
			{
				this.shapeEditor.setIsRunning(false);
				this.menu.setIsRunning(true);
			}
			else if(this.currentMenu == 2)
			{
				this.menu.setIsRunning(false);
				this.shapeEditor.setIsRunning(true);
			}
		}

		this.shape.setShapeScale(this.menu.getShapeSize());

		if(!this.shapeEditor.getIsRunning())
		{
			this.drawShape();
		}

		if(this.menu.isShowScale() && !this.shapeEditor.getIsRunning())
		{
			// draws the legend along the bottom of the screen:
			this.legend(goalHuePos, 0);

		} // if showScale

		this.shapeEditor.runMenu();
		this.menu.runMenu();

	} // draw

	private void drawShape()
	{
		int[]	curHue	= this.menu.getCurHue()[0];		
		this.fill(curHue[0], curHue[1], curHue[2]);
		//		this.fill(255);

//		float	shapeWidth	= (this.width - this.menu.getLeftEdgeX()) * (this.menu.getShapeSize() / 100);
//		float	shapeHeight	= this.height * (this.menu.getShapeSize() / 100);

		//this.shapeMode(CORNER);
		PShape pShape;
		/*		if(this.menu.getLeftEdgeX() == 0) pShape = this.shape.getPShape();
		else pShape = this.shape.getScaledPShape(new float[] {925, (925 - this.menu.getLeftEdgeX()), 1, 1});
		 */
		pShape = this.shape.getPShape();

		pShape.beginShape();
		pShape.fill(curHue[0], curHue[1], curHue[2]);
		pShape.stroke(curHue[0], curHue[1], curHue[2]);
		pShape.rotate(this.shape.getRotation());
		pShape.scale(this.menu.getCurrentScale());
		pShape.endShape();

		this.shape(pShape, this.menu.mapCurrentXPos(this.shape.getXPos()), this.menu.mapCurrentYPos(this.shape.getYPos()));
		/*		
		if(this.menu.getLeftEdgeX() == 0) this.shape(pShape, this.shapeEditor.getXPos(), this.shapeEditor.getYPos());
		else this.shape(pShape, PApplet.map(this.shapeEditor.getXPos(), 0, 925, this.menu.getLeftEdgeX(), 925), this.shapeEditor.getYPos());
		 */
	} // drawShape

	@Override
	public String[] getLegendText()
	{
		String[]	result	= new String[this.menu.getCurRangeSegments()];
		for(int i = 0; i < result.length; i++)
		{
			result[i]	= this.menu.getThresholds()[0][i] + "";
		} // for

		return result;
	} // fillLegendText

	public Shape getShape()
	{
		return this.shape;
	}

	/*
	public void setShapeEditorRunning(boolean isRunning)
	{
		this.shapeEditor.setIsRunning(isRunning);
	}
	 */
	public void mouseDragged()
	{
		this.mousePressed();
	}

	public void mousePressed()
	{
		//TODO: Is the hamburger button in a ControlP5 object not in this if statement?
		if(!this.shapeEditor.getControlP5().isMouseOver() && !this.menu.getControlP5().isMouseOver() && !this.menu.getOutsideButtonsCP5().isMouseOver())
		{
			// Map if running:
			if(this.shapeEditor.getIsRunning() || this.menu.getIsRunning())
			{	
				this.shape.setXPos( this.shapeEditor.mapFullAppletXPos( Math.max( this.shapeEditor.mapAdjustedMenuXPos(0), Math.min(this.mouseX, this.shapeEditor.mapFullAppletXPos(this.width) ) ) ) );
				this.shape.setYPos( this.shapeEditor.mapFullAppletYPos( Math.max( this.shapeEditor.mapAdjustedMenuYPos(0), Math.min(this.mouseY, this.shapeEditor.mapFullAppletYPos(this.height) ) ) ) );
				this.shapeEditor.updateSliders();
			}
			else
			{
				// If neither are running, just keep w/in bounds:
				this.shape.setXPos( Math.max( 0, Math.min(this.mouseX, this.width) ) );
				this.shape.setYPos( Math.max( 0, Math.min(this.mouseY, this.height) ) );
				this.shapeEditor.updateSliders();
			}
		} // if - not over either ControlP5
	} // mouseClicked



	/**
	 * 08/01/2017
	 * Emily Meuer
	 * 
	 * Class to stop the Input (which needs to stop the AudioContext,
	 * because it needs to stop the AudioIO, esp. when it's using the PortAudioAudioIO,
	 * which needs to call PortAudio.terminate to avoid a weird set of 
	 * NoClassDefFoundError/ClassNotFoundException/BadFileDescriptor errors that will happen ocassionaly on start-up).
	 * 
	 * Taken from https://forum.processing.org/two/discussion/579/run-code-on-exit-follow-up
	 *
	 */
	/*	public class DisposeHandler {

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
	 */

} // Module_03_AmplitudeHSB
