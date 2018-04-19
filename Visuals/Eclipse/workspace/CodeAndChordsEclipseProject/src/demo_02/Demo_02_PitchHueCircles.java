package demo_02;

import processing.core.*;
import core.Module;
import core.ModuleMenu;
import core.input.RealTimeInput;
import core.input.RecordedInput;

public class Demo_02_PitchHueCircles extends Module
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
		PApplet.main("demo_02.Demo_02_PitchHueCircles");
	} // main

	
	private	int[]	circleDiams;
	
	public void settings()
	{
		fullScreen();
	}
	
	public void setup() 
	{
		this.input	= new RealTimeInput(4, this);

		
		this.totalNumInputs	= this.input.getAdjustedNumInputs();
		this.curNumInputs	= this.totalNumInputs;
		
		this.menu	= new ModuleMenu(this, this, this.input, 12);

		this.circleDiams	= new int[] { 200, 350, 500, 650 };
		
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

//		yValDif = 26;

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
		this.menu.addForteThresholdSlider(controllerXVals[0], textYVals[3]);

		// Adding inputNumSelect first so that inputSelect can be in front:
//		this.menu.addInputNumSelect(controllerXVals[0], textYVals[5]);
		
		this.menu.addGuideTonePopout(controllerXVals[0], textYVals[5]);

		this.menu.addKeySelector(controllerXVals[0], textYVals[5]);
		this.menu.setCurKey("A", 2);
		
		this.menu.addARTSliders(controllerXVals[1], textYVals[1], textYVals[2], textYVals[3]);
		
		this.menu.addInputSelect(controllerXVals[0], textYVals[4]);		


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
		
		this.menu.addColorSelect(controllerXVals[0], new int[] { textYVals[15], textYVals[16], textYVals[17] }, noteNames, "Custom Pitch\nColor Select", false, "color");
		

		// ColorSelect and ColorStyle added out of order so that the 2nd Color
		// and 3rd Color select buttons will exist for the Rainbow ColorStyle
		// to lock them.
//		this.addColorSelectButtons(textYVals[14]);
		String[] buttonLabels	= new String[] {
				"Canvas", "Tonic", "2nd Color", "3rd Color"
		}; // buttonLabels
		this.menu.addSpecialColors(controllerXVals[0], textYVals[14], buttonLabels, "Color Select", true, "color");

		// addColorStyleButtons will set the colorStyle to rainbow() first:
		this.menu.addColorStyleButtons(controllerXVals[0], textYVals[13], "color");

		this.menu.addHSBSliders(controllerXVals[0], modulateHSBVals);

		this.menu.addModulateSliders(controllerXVals[0], modulateYVals);
		
		this.menu.addThresholdSliders(controllerXVals[2], textYVals[1], 5);
		this.menu.setBrightnessPercentSlider(1);

//		this.menu.setColorStyle(ModuleTemplate01.CS_RAINBOW);

		this.menu.getControlP5().getController("keyDropdown").bringToFront();
		
		this.menu.getOutsideButtonsCP5().getController("play").hide();


	} // setup()

	
	public void draw()
	{
		int	scaleDegree;			
		
		this.background(this.menu.getCanvasColor()[0], this.menu.getCanvasColor()[1], this.menu.getCanvasColor()[2]);
		
		
		for(int i = (this.curNumInputs - 1); i >= 0; i--)
//		for(int i = 0; i < this.curNumInputs; i++)
		{
//			System.out.println("input.getAdjustedFundAsMidiNote(" + (i) + ") = " + recordedInput.getAdjustedFundAsMidiNote(i) + 
//					"; input.getAmplitude(" + (i) + ") = " + recordedInput.getAmplitude(i));
		
			scaleDegree	= (round(input.getFundAsMidiNote(i)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;
//			scaleDegree	= (round(this.recordedInput.getAdjustedFundAsMidiNote(i)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;
			
//			System.out.println(i + ": scaleDegree = " + scaleDegree);
			
			this.menu.fade(scaleDegree, i);
			
			this.fill(this.menu.getCurHue()[i][0], this.menu.getCurHue()[i][1], this.menu.getCurHue()[i][2]);

			int	curX;
			int	curY;
			
			curX	= (int)this.menu.mapCurrentXPos(this.width / 2);
			curY	= (int)this.menu.mapCurrentYPos(this.height / 2);
			this.ellipse(curX, curY, this.circleDiams[i], this.circleDiams[i]);
			
			if(this.menu.isShowScale())
			{
//				this.legend(scaleDegree, i);
				this.okGoLegend(scaleDegree, i);
			}

		} // for
		
		this.menu.runMenu();


	} // draw()
	
	
	public String[] getLegendText()
	{
		return this.menu.getScale(this.menu.getCurKey(), this.menu.getMajMinChrom());
	} // getLegendText
	
} // class