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
	
	private	ModuleMenu			menu;

//	private Shape     			shape;

//	private ShapeEditor			shapeEditor;
	
	/**	holds the y values for all Controllers	*/
	private	int[]	yVals;

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
//		this.input	= new Input(this);
		this.input    = new Input(2, new AudioContext(), this);
		
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

//				this.moduleTemplate	= new ModuleTemplate02(this, this.input, "Module_02_AmplitudeHSB");
				this.menu	= new ModuleMenu(this, this, this.input, "Module_02_AmplitudeHSB", 4);
				
				this.yVals		= new int[18];
				// Seemed like a good starting position, related to the text - but pretty arbitrary:
				this.yVals[0]	= 26;
				int	distance	= (this.height - this.yVals[0]) / this.yVals.length;
				for(int i = 1; i < this.yVals.length; i++)
				{
					this.yVals[i]	= this.yVals[i - 1] + distance;
				} // fill yVals
				
				// From ModuleTemplate02:
				// Have to addColorSelect() first so that everything else can access the colors:
				String[]	buttonLabels	= new String[] {
						"Canvas", "1", "2", "3", "4"
				};
				//addColorSelect must be called first
				this.menu.addColorSelect(new int[] { this.yVals[8] }, buttonLabels, "Color Select", true);
//				this.menu.fillHSBColors();

				this.menu.addARTSliders(this.yVals[1], this.yVals[2], this.yVals[3]);

				this.menu.addShapeSizeSlider(this.yVals[15]);

				this.menu.addRangeSegments(this.yVals[7], 4, 4, "Dynamic\nSegments");

				this.menu.addHSBSliders(new int[] { this.yVals[4], this.yVals[5], this.yVals[6], });

				this.menu.addPianoThresholdSlider(this.yVals[9]);
				
				this.menu.addForteThresholdSlider(this.yVals[10]);
				
				int	verticalSpacer	= distance - this.menu.getSliderHeight();
				this.menu.addThresholdSliders(yVals[11], verticalSpacer);

				// TODO: may not need this call anymore:
				this.menu.addShapeCustomizationControls(this.yVals[16]);


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
			this.menu.setMenuVal();
		}

		background(this.menu.getCanvasColor()[0], this.menu.getCanvasColor()[1], this.menu.getCanvasColor()[2]);

		// pick the appropriate color by checking amplitude threshold
		float	curAmp		= this.input.getAmplitude();
		int		goalHuePos	= 0;


		for(int i = 0; i < this.menu.getThresholds().length; i++)
		{
			if(curAmp > this.menu.getThresholds()[i]) {
				goalHuePos	= i;
			} // if

			//			System.out.println("curAmp = " + curAmp);
		} // for

		//		System.out.println("curAmp " + curAmp + " was over thresholds[" + goalHuePos + "]: " + this.moduleTemplate.getThresholds()[goalHuePos]);

		// Now this threshold application happens in fade:
//		this.moduleTemplate.applyThresholdSBModulate(curAmp);
		this.menu.fade(goalHuePos);


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

		this.shape.setShapeScale(this.menu.getShapeSize());

		if(!this.shapeEditor.getIsRunning())
		{
			this.drawShape();
		}

		if(this.menu.isShowScale() && !this.shapeEditor.getIsRunning())
		{
			// draws the legend along the bottom of the screen:
			this.menu.legend(goalHuePos);

		} // if showScale

		this.shapeEditor.runMenu();

	} // draw

	private void drawShape()
	{
		int[]	curHue	= this.menu.getCurHue();
		this.fill(curHue[0], curHue[1], curHue[2]);
//		this.fill(255);

		float	shapeWidth	= (this.width - this.menu.getLeftEdgeX()) * (this.menu.getShapeSize() / 100);
		float	shapeHeight	= this.height * (this.menu.getShapeSize() / 100);

		//this.shapeMode(CORNER);
		PShape pShape;
		if(this.menu.getLeftEdgeX() == 0) pShape = this.shape.getPShape();
		else pShape = this.shape.getScaledPShape(new float[] {925, (925 - this.menu.getLeftEdgeX()), 1, 1});


		pShape.beginShape();
		pShape.fill(curHue[0], curHue[1], curHue[2]);
		pShape.stroke(curHue[0], curHue[1], curHue[2]);
		pShape.rotate(this.shapeEditor.getRotation());
		pShape.endShape();

		if(this.menu.getLeftEdgeX() == 0) this.shape(pShape, this.shapeEditor.getXPos(), this.shapeEditor.getYPos());
		else this.shape(pShape, PApplet.map(this.shapeEditor.getXPos(), 0, 925, this.menu.getLeftEdgeX(), 925), this.shapeEditor.getYPos());

	} // drawShape
	
	@Override
	public String[] getLegendText()
	{
		String[]	result	= new String[this.menu.getCurRangeSegments()];
		for(int i = 0; i < result.length; i++)
		{
			result[i]	= this.menu.getThresholds()[i] + "";
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

		if(!this.shapeEditor.getCP5().isMouseOver() && !this.menu.getCP5().isMouseOver())
		{
			if(this.shapeEditor.getIsRunning() && this.shapeEditor.getCP5().isVisible() && this.mouseX > this.shapeEditor.getAppletWidth() * (1 - this.shapeEditor.getScale()) && this.mouseY > this.shapeEditor.getAppletHeight() * (1 - this.shapeEditor.getScale()))
			{			
				this.shapeEditor.setXPos(this.shapeEditor.mapFullAppletXPos(this.mouseX));
				this.shapeEditor.setYPos(this.shapeEditor.mapFullAppletYPos(this.mouseY));
				this.shapeEditor.getCP5().getController("xPos").setValue(this.shapeEditor.getXPos());
				this.shapeEditor.getCP5().getController("yPos").setValue(this.shapeEditor.getYPos());

			}
			else if(this.menu.getLeftEdgeX() == 0 && !this.shapeEditor.getCP5().isVisible())
			{
				this.shapeEditor.setXPos(this.mouseX);
				this.shapeEditor.setYPos(this.mouseY);
				this.shapeEditor.getCP5().getController("xPos").setValue(this.shapeEditor.getXPos());
				this.shapeEditor.getCP5().getController("yPos").setValue(this.shapeEditor.getYPos());
			}
			else if(this.mouseX > this.menu.getLeftEdgeX() && !this.shapeEditor.getCP5().isVisible())
			{
				this.shapeEditor.setXPos(PApplet.map(this.mouseX - (this.width/3), 0, 2, 0, 3));
				this.shapeEditor.setYPos(this.mouseY);
				this.shapeEditor.getCP5().getController("xPos").setValue(this.shapeEditor.getXPos());
				this.shapeEditor.getCP5().getController("yPos").setValue(this.shapeEditor.getYPos());
			}
		}
	} // mouseClicked



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
