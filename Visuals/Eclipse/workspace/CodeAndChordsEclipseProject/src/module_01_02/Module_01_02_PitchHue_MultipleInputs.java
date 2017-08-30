package module_01_02;

import processing.core.*;

import core.Input;
import core.Module;
import core.ModuleMenu;
import core.PortAudioAudioIO;
import core.Shape;
import core.Archive_ModuleTemplate.ModuleTemplate01;
import net.beadsproject.beads.core.AudioContext;
import	controlP5.*;

public class Module_01_02_PitchHue_MultipleInputs extends Module
{
	/**
	 * 
	 * 
 1/4/2016
 Emily

	 * 08/01/2016
	 * Emily Meuer

	 *
	 * Background changes hue based on pitch.
	 *
	 * (Adapted from Examples => Color => Hue.)
	 */

	public static void main(String[] args)
	{

		//Says cannot find or load main class???  This should not be an issue
		PApplet.main("module_01_02.Module_01_02_PitchHue_MultipleInputs");
		//PApplet.main("module_01_PitchHueBackground.module_01_02_PitchHueBackground_ModuleTemplate_EMM.Module_01_02_PitchHueBackground_ModuleTemplate");
	} // main

	/*
	// Choose input file here:
	// Raw:
	//String  inputFile  = "src/module_01_PitchHueBackground/module_01_02_PitchHueBackground_ModuleTemplate_EMM/Emily_CMajor-2016_09_2-16bit-44.1K Raw.wav";
	// Tuned:
	String  inputFile  = "src/module_01_PitchHueBackground/module_01_02_PitchHueBackground_ModuleTemplate_EMM/Emily_CMajor-2016_09_2-16bit-44.1K Tuned.wav";
	// Kanye:
	//String  inputFile  = "src/module_01_PitchHueBackground/module_01_02_PitchHueBackground_ModuleTemplate_EMM/Emily_CMajor-2016_09_2-16bit-44.1K Kanye.wav";
	 */

	private Input  input;
/*	private	int		numInputs;

	private	int[]	xVals;
	private	int[]	yVals;
	private	int[]	rectWidths;
	private	int[]	rectHeights;
*/
	
	public void setup() 
	{
		// TODO: test with more inputs than are supported
//		this.input	= new Input(2, this);
		this.input	= new Input(16, true, this);
		this.totalNumInputs	= this.input.getAdjustedNumInputs();
		this.curNumInputs	= 2;
		
		this.menu	= new ModuleMenu(this, this, this.input, "Module_01_02_PitchHueBackground", 12);
		this.shapes	= new Shape[12];
		for(int i = 0; i < this.shapes.length; i++)
		{
			this.shapes[i]	= new Shape(this);
			this.shapes[i].setCurrentShape("supershape", 
					new float[] { 1, 1, 4, 4, 1, 1, 1 } );
		} // for - i
		
		this.setSquareValues();
		
		int[]	textYVals  		= new int[18];
		int[]	modulateYVals	= new int[3];
		int[]	modulateHSBVals	= new int[3];
		int[]	controllerXVals	= new int[3];
//		int					colorSelectY;
		
		// calculate y's
		// set y vals for first set of scrollbar labels:
		textYVals[0]	=	26;
		// Given our height = 250 and "hide" (textYVals[0]) starts at [40] - now 26 (1/17),
		// We want a difference of 27.  This gets that:
		int	yValDif = (int)((this.height - textYVals[0]) / 18);//(textYVals.length + noteYVals.length + modulateYVals.length));
		// ... but no smaller than 25:
		if(yValDif < 25) {
			yValDif	= 25;
		}
		yValDif = 26;

		for(int i = 1; i < textYVals.length; i++)
		{
			textYVals[i]	= textYVals[i - 1] + yValDif;
		} // for

		// Add extra space before "Pitch Color Codes":
		textYVals[textYVals.length - 3]	= textYVals[textYVals.length - 4] + (int)(yValDif * 1.5);
		textYVals[textYVals.length - 2]	= textYVals[textYVals.length - 3] + (int)(yValDif * 1);
		textYVals[textYVals.length - 1]	= textYVals[textYVals.length - 2] + (int)(yValDif * 1);
		
		controllerXVals	= new int[] {	
				0, 
				(this.width / 3) - 20, 
				((this.width / 3) * 2) - 40	
			};

		// call add methods:
		
		this.menu.addHideButtons(controllerXVals[0], textYVals[1]);
		
//		this.menu.addSliders(textYVals[1], textYVals[2], textYVals[3], textYVals[4]);
		this.menu.addPianoThresholdSlider(controllerXVals[0], textYVals[2]);

		// Adding inputNumSelect first so that inputSelect can be in front:
		this.menu.addInputNumSelect(controllerXVals[0], textYVals[5]);
<<<<<<< HEAD

=======
>>>>>>> 726f4d5... Commit after aborting the rebase
		this.menu.addInputSelect(controllerXVals[0], textYVals[4]);
		
		this.menu.addARTSliders(controllerXVals[1], textYVals[1], textYVals[2], textYVals[3]);

		this.menu.addGuideTonePopout(controllerXVals[2], textYVals[2]);
		this.menu.addKeySelector(controllerXVals[2], textYVals[2]);
		this.menu.setCurKey("A", 2);

		modulateHSBVals[0] = textYVals[6];
		modulateHSBVals[1] = textYVals[7];
		modulateHSBVals[2] = textYVals[8];

		modulateYVals[0]	= textYVals[10];
		modulateYVals[1]	= textYVals[11];
		modulateYVals[2]	= textYVals[12];

		// Adding ColorSelect first since everything to do with colors depends on that:
		String[] noteNames = new String[] {
				"A", "A#/Bb", "B", "C", "C#/Db", "D", "D#/Db", "E", "F", "F#/Gb", "G", "G#/Ab"
		}; // noteNames
		
		this.menu.addColorSelect(controllerXVals[0], new int[] { textYVals[15], textYVals[16], textYVals[17] }, noteNames, "Custom Pitch\nColor Select", false);
		

		// ColorSelect and ColorStyle added out of order so that the 2nd Color
		// and 3rd Color select buttons will exist for the Rainbow ColorStyle
		// to lock them.
//		this.addColorSelectButtons(textYVals[14]);
		String[] buttonLabels	= new String[] {
				"Canvas", "Tonic", "2nd Color", "3rd Color"
		}; // buttonLabels
		this.menu.addSpecialColors(controllerXVals[0], textYVals[14], buttonLabels, "Color Select", true);

		// addColorStyleButtons will set the colorStyle to rainbow() first:
		this.menu.addColorStyleButtons(controllerXVals[2], textYVals[3]);

		this.menu.addHSBSliders(controllerXVals[0], modulateHSBVals);

		this.menu.addModulateSliders(controllerXVals[0], modulateYVals);

//		this.menu.setColorStyle(ModuleTemplate01.CS_RAINBOW);

		this.menu.getControlP5().getController("keyDropdown").bringToFront();

	} // setup()

	
	public void draw()
	{
		int	scaleDegree;
		
		// The following line is necessary so that key press shows the menu button
		if (keyPressed == true && !this.menu.getIsRunning()) 
		{
			this.menu.setMenuVal();
		} // if keyPressed
		
		for(int i = 0; i < this.curNumInputs; i++)
		{
//			System.out.println("input.getAdjustedFundAsMidiNote(" + (i + 1) + ") = " + input.getAdjustedFundAsMidiNote(i + 1) + 
//					"; input.getAmplitude(" + (i + 1) + ") = " + input.getAmplitude(1 + 1));
			
			scaleDegree	= (round(input.getAdjustedFundAsMidiNote(i + 1)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;

			this.menu.fade(scaleDegree, i);
			
			this.fill(this.menu.getCurHue()[i][0], this.menu.getCurHue()[i][1], this.menu.getCurHue()[i][2]);
			
			int	curX;
			int	curY;
			
			curX	= (int)this.menu.mapCurrentXPos(this.xVals[i]);
			curY	= (int)this.menu.mapCurrentYPos(this.yVals[i]);
			this.rect(curX, curY, this.rectWidths[i], this.rectHeights[i]);
						
			if(this.menu.isShowScale())
			{
				this.legend(scaleDegree, i);
			}
		} // for
		
		this.drawShapes();
		
		this.menu.runMenu();
<<<<<<< HEAD
=======
				
>>>>>>> 726f4d5... Commit after aborting the rebase
		// TODO - trying to find the trichromatic major/minor customPitchColor bug:
/*	if(this.menu.getCurColorStyle() == ModuleTemplate01.CS_TRICHROM)
				{
					for(int i = 0; i < menu.trichromColors.length; i++)
					{
						this.fill(menu.trichromColors[i][0], menu.trichromColors[i][1], menu.trichromColors[i][2]);
						this.ellipse(this.width / 2, i * 30 + 60, 30, 30);
					}
				} // if		
*/

	} // draw()
	
	private void drawShapes()
	{
		int[]	curHue;
		for(int i = 0; i < this.curNumInputs; i++)
		{
			
		curHue	= this.menu.getCurHue()[i];		
		this.fill(curHue[0], curHue[1], curHue[2]);
		//		this.fill(255);

//		float	shapeWidth	= (this.width - this.menu.getLeftEdgeX()) * (this.menu.getShapeSize() / 100);
//		float	shapeHeight	= this.height * (this.menu.getShapeSize() / 100);

		//this.shapeMode(CORNER);
		PShape pShape;
		/*		if(this.menu.getLeftEdgeX() == 0) pShape = this.shape.getPShape();
		else pShape = this.shape.getScaledPShape(new float[] {925, (925 - this.menu.getLeftEdgeX()), 1, 1});
		 */
		pShape = this.shapes[i].getPShape();

		pShape.beginShape();
		pShape.fill(curHue[0], curHue[1], curHue[2]);
		pShape.stroke(curHue[0], curHue[1], curHue[2]);
		pShape.rotate(this.shapes[i].getRotation());
		pShape.scale(this.menu.getCurrentScale());
		pShape.endShape();

		this.shape(pShape, this.menu.mapCurrentXPos(this.shapes[i].getXPos()), this.menu.mapCurrentYPos(this.shapes[i].getYPos()));
		} // for - i
	} // drawShape	
	
	public String[] getLegendText()
	{
		return this.menu.getScale(this.menu.getCurKey(), this.menu.getMajMinChrom());
	} // getLegendText
	
	/**
	 * Calculates the x and y values for the squares given the number of inputs.
	 */
/*	private void setSquareValues()
	{

		// Rectangles are always the same height, so will be set in a loop every time:
		this.rectHeights	= new int[this.numInputs];

		// Setting xVals and yVals and width and height of rectangles:
		// Even number of inputs:
		if(this.numInputs % 2 == 0 && this.numInputs != 12)
		{
			this.rectWidths		= new int[this.numInputs];
			this.rectHeights	= new int[this.numInputs];
			for(int i = 0; i < this.rectWidths.length; i++)
			{
				this.rectWidths[i]	= this.width / (this.numInputs / 2);
				this.rectHeights[i]	= this.height / 2;
			} // for

			this.xVals	= new int[this.numInputs];
			this.yVals	= new int[this.numInputs];

			for(int i = 0; i < this.xVals.length; i++)
			{
				int xPos	= i % (this.numInputs / 2);
				int xVal	= xPos * (this.rectWidths[i]);
				xVals[i]	= xVal;
				System.out.println(i + ": xPos = " + xPos + "; xVal = " + xVal);
			} // for - xVals

			for(int i = 0; i < this.yVals.length; i++)
			{
				int	yPos	= i / (this.numInputs / 2);
				int	yVal	= yPos * this.rectHeights[i];
				yVals[i]	= yVal;
				System.out.println(i + ": yPos = " + yPos + "; yVal = " + yVal);
			} // for - yVals
		} // even number of inputs
		else if(this.numInputs == 1)
		{
			this.rectWidths		= new int[] {	this.width	};
			this.rectHeights	= new int[]	{	this.height	};

			this.xVals	= new int[] {	0	};
			this.yVals	= new int[] {	0	};
		} // 1
		else if(this.numInputs == 3)
		{
			this.rectWidths		= new int[] {	
					this.width,
					(this.width / 2), (this.width / 2)
			};
			for(int i = 0; i < this.rectHeights.length; i++)
			{
				this.rectHeights[i]	= this.height / 2;
			}

			this.xVals	= new int[] { 
					0,
					0,	(this.width / 2)
			};
			this.yVals	= new int[] {
					0,
					(this.height / 2), (this.height / 2)
			};
		} // 3
		else if(this.numInputs == 5)
		{
			this.rectWidths	= new int[] {
					(this.width / 2),	(this.width / 2),
					(this.width / 3), (this.width / 3), (this.width / 3)
			};
			for(int i = 0; i < this.rectHeights.length; i++)
			{
				this.rectHeights[i]	= this.height / 2;
			}

			this.xVals	= new int[] {
					0,				(this.width / 2),	
					0,	(this.width / 3), ((this.width / 3) * 2)
			};
			this.yVals	= new int[] {
					0,				0,
					(this.height / 2), (this.height / 2), (this.height / 2)
			};
		} // 5
		else if(this.numInputs == 7)
		{
			this.rectWidths	= new int[] {
					(this.width / 2),	(this.width / 2),
					(this.width / 2), (this.width / 3), (this.width / 3),
					(this.width / 2),	(this.width / 2)
			};
			for(int i = 0; i < this.rectHeights.length; i++)
			{
				this.rectHeights[i]	= this.height / 3;
			}

			this.xVals	= new int[] {
					0,				(this.width / 2),	
					0,	(this.width / 3), ((this.width / 3) * 2),
					0,				(this.width / 2)
			};
			this.yVals	= new int[] {
					0,				0,
					(this.height / 3), (this.height / 3), (this.height / 3),
					(this.height / 3) * 2, (this.height / 3) * 2, (this.height / 3) * 2
			};
		} // 7
		else if(this.numInputs == 9)
		{
			this.rectWidths		= new int[this.numInputs];
			for(int i = 0; i < this.rectWidths.length; i++)
			{
				this.rectWidths[i]	= (this.width / 3);
				this.rectHeights[i]	= (this.height / 3);
			} // for

			this.xVals	= new int[] {
					0, this.width/3, (this.width/3) * 2,
					0, this.width/3, (this.width/3) * 2,
					0, this.width/3, (this.width/3) * 2
			};
			this.yVals	= new int[] {
					0, 0, 0,
					this.height/3, this.height/3, this.height/3, 
					((this.height / 3) * 2), ((this.height / 3) * 2), ((this.height / 3) * 2)
			};
		} // 9
		else if(this.numInputs == 11)
		{
			this.rectWidths		= new int[this.numInputs];
			for(int i = 0; i < this.rectWidths.length; i++)
			{
				if(i < 4 || i > 6)
				{
					this.rectWidths[i]	= (this.width / 4);
				} else {
					// middle row has only 3:
					this.rectWidths[i]	= (this.width / 3);
				}

				this.rectHeights[i]	= (this.height / 3);
			} // for

			this.xVals	= new int[] {
					0, this.width/4, this.width/2, (this.width/4) * 3,
					0, this.width/3, (this.width/3) * 2,
					0, this.width/4, this.width/2, (this.width/4) * 3,
			};
			this.yVals	= new int[] {
					0, 0, 0, 0,
					this.height/3, this.height/3, this.height/3, 
					((this.height / 3) * 2), ((this.height / 3) * 2), ((this.height / 3) * 2), ((this.height / 3) * 2)
			};
		} // 11
		else if(this.numInputs == 12)
		{
			this.rectWidths		= new int[this.numInputs];
			for(int i = 0; i < this.rectWidths.length; i++)
			{
				this.rectWidths[i]	= (this.width / 4);
				this.rectHeights[i]	= (this.height / 3);
			} // for

			this.xVals	= new int[] {
					0, this.width/4, this.width/2, (this.width/4) * 3,
					0, this.width/4, this.width/2, (this.width/4) * 3,
					0, this.width/4, this.width/2, (this.width/4) * 3,
			};
			this.yVals	= new int[] {
					0, 0, 0, 0,
					this.height/3, this.height/3, this.height/3, this.height/3, 
					((this.height / 3) * 2), ((this.height / 3) * 2), ((this.height / 3) * 2), ((this.height / 3) * 2)
			};
		} // 12
	} // set Square Vals

*/
} // class