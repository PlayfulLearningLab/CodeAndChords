package demo_01;

import controlP5.ControlP5;
import core.Module;
import core.ModuleMenu;
import core.input.RecordedInput;
import processing.core.PApplet;

public class Demo_01_VerticalBars_WantingMemories extends Module {
	/**
	 * 
	 * 
	 * 08/01/2016
	 * Emily Meuer, Dan Mahota
	 *
	 * Each vertical bar changes hue based on the pitch and height based on the amplitude of its 
	 * respective input;
	 * see Demo_01_VerticalBars.java for in-text explanatory comments.
	 */

	private float[]	barVel;
	private float[] barPos;

	public static void main(String[] args)
	{
		PApplet.main("demo_01.Demo_01_VerticalBars_WantingMemories");
	} // main

	private	RecordedInput	recordedInput;
	
	private	int	scaleDegree;
	/*	private	int		numInputs;

	private	int[]	xVals;
	private	int[]	yVals;
	private	int[]	rectWidths;
	private	int[]	rectHeights;
	 */

	public void settings()
	{
		fullScreen();
	}
	
	public void setup() 
	{
		this.verticalBarsDemo = true;
		this.amplitude = new float[16];
		this.barPos = new float[16];
		this.barVel = new float[16];

		for(int i = 0; i < this.amplitude.length; i++)
		{
			this.amplitude[i] = 0;
			this.barPos[i] = 0;
			this.barVel[i] = 0;
		}

		
		this.recordedInput	= new RecordedInput(this, new String[] { 
				"WantingMemories_Melody.wav",
				"WantingMemories_Alto.wav",
//				"WantingMemories_Bass.wav",
				"WMBass_Later_Quiet.wav",
				"WantingMemories_Soprano.wav",
//				"WantingMemories_Tenor.wav",
				"WMTenor_Medium.wav"
		});
		this.totalNumInputs	= this.recordedInput.getNumInputs();
		this.curNumInputs	= this.totalNumInputs;

		this.menu	= new ModuleMenu(this, this, this.recordedInput, 12);

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

		// call add methods (as of 2018, this is the old way to do it):

		this.menu.addHideButtons(controllerXVals[0], textYVals[1]);

		//		this.menu.addSliders(textYVals[1], textYVals[2], textYVals[3], textYVals[4]);
		this.menu.addPianoThresholdSlider(controllerXVals[0], textYVals[2]);

		// Adding inputNumSelect first so that inputSelect can be in front:
		this.menu.addInputNumSelect(controllerXVals[0], textYVals[5]);
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
		this.menu.addColorStyleButtons(controllerXVals[2], textYVals[3], "color");

		this.menu.addHSBSliders(controllerXVals[0], modulateHSBVals);

		this.menu.addModulateSliders(controllerXVals[0], modulateYVals);

		//		this.menu.setColorStyle(ModuleTemplate01.CS_RAINBOW);

		this.menu.getControlP5().getController("keyDropdown").bringToFront();

//		this.menu.setMenuList(new String[] {"Canvas", "Module Menu"});
		
		this.menu.getControlP5().addToggle("dynamicBars")
		.setSize(100, 40)
		.setPosition(700, 20)
		.setState(false)
		.setId(99999)
		.setLabel("Dynamic Bar Height")
		.getCaptionLabel()
		.align(ControlP5.CENTER, ControlP5.CENTER);
		

		this.menu.getOutsideButtonsCP5().getController("play").hide();

		this.scaleDegree	= 0;
	} // setup()


	public void draw()
	{
		this.background(0);

		for(int i = 0; i < this.curNumInputs; i++)
		{
			this.amplitude[i] = this.recordedInput.getAmplitude(i);

			//			System.out.println("input.getAdjustedFundAsMidiNote(" + (i + 1) + ") = " + input.getAdjustedFundAsMidiNote(i + 1) + 
			//					"; input.getAmplitude(" + (i + 1) + ") = " + input.getAmplitude(1 + 1));

			if(this.amplitude[i] > this.menu.getPianoThreshold(i))
			{
				this.scaleDegree	= (round(this.recordedInput.getAdjustedFundAsMidiNote(i)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;				
			}

			this.menu.fadeColor(this.scaleDegree, i);

			this.fill(this.menu.getCurHue()[i][0], this.menu.getCurHue()[i][1], this.menu.getCurHue()[i][2]);

			int	curX;
			int	curY;

			/*			if(this.menu.getIsRunning())
			{
				curX	= (int)this.menu.mapAdjustedMenuXPos(this.xVals[i]);
				curY	= (int)this.menu.mapAdjustedMenuYPos(this.yVals[i]);
			} else {
				curX	= this.xVals[i];
				curY	= this.yVals[i];
			}
			 */
			curX	= (int)this.menu.mapCurrentXPos(this.xVals[i]);
			curY	= (int)this.menu.mapCurrentYPos(this.yVals[i]);

			
			if(this.menu.getDynamicBars())
			{
				//Value from 0 to 1 to act as a percent of the screen that should be covered
				float amp = (float) Math.min(1, this.amplitude[i] / 100/*max amp*/);
				//amp = (float) Math.max(amp, .1);

				if(amp > this.barPos[i])
				{
					this.barVel[i] = (float) Math.min(this.barVel[i] + .02, .2);

					if(this.barVel[i] < 0)
					{
						this.barVel[i] = 0;
					}
				}
				else
				{
					this.barVel[i] = (float) Math.max(this.barVel[i] - .02, -.2);

					if(this.barVel[i] > 0)
					{
						this.barVel[i] = 0;
					}
				}


				//this.barPos[i] = Math.max(this.barPos[i] + this.barVel[i], 0);
				this.barPos[i] = this.barPos[i] + (amp - this.barPos[i])/10;


				//System.out.println("Input number " + i + " - Amplitude = " + this.amplitude[i]);
				//System.out.println("Input number " + i + " - amp = " + this.barPos[i]);

				this.rect(curX,this.menu.mapCurrentYPos((this.height/2)- this.barPos[i]*(this.height/2)), this.rectWidths[i], this.height*this.barPos[i]);
			}
			else
			{
				this.rect(curX, curY, this.rectWidths[i], this.rectHeights[i]);
			}
			

			//this.stroke(255);
			//this.strokeWeight(5);
			//this.line(0, (this.height/2)- amp*(this.height/2), this.width, (this.height/2)- amp*(this.height/2));
			
			if(this.menu.isShowScale())
			{
				this.legend(this.scaleDegree, i);
			} // isShowScale

		} // for

		this.menu.runMenu();

	} // draw()


	public String[] getLegendText()
	{
		return this.menu.getScale(this.menu.getCurKey(), this.menu.getMajMinChrom());
	} // getLegendText
	
}
