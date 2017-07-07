package module_01_02;

import processing.core.*;

import core.Input;
import core.ModuleTemplate;
import core.PortAudioAudioIO;
import net.beadsproject.beads.core.AudioContext;
import	controlP5.*;

public class Module_01_02_PitchHue_MultipleInputs extends PApplet
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
	private	int		numInputs;

	private	int[]	xVals;
	private	int[]	yVals;
	private	int		rectWidth;
	private	int		rectHeight;


	private float[][]  goalHue;
	private float[][]  curHue;

	private int[]  newHuePos;
	private int[]  goalHuePos;
	private int[]  curHuePos;

	private ModuleTemplate[]	moduleTemplate;

	private boolean[]		nowBelow;
	private boolean[][]		colorReachedArray;
	private boolean[]		colorReached;
	private int[]			attRelTran;		// 0 = attack, 1 = release, 2 = transition

	private	float[][]		colorRange;
	private	float[][]		colorAdd;


	public void settings()
	{
		size(925, 520);
	} // settings

	public void setup() 
	{
		// TODO: test with more inputs than are supported
		this.numInputs	= 4;
		this.input	= new Input(this.numInputs);
		
		// Even number of inputs:
		if((this.numInputs % 2) == 0 && (this.numInputs < 9))
		{
			this.xVals	= new int[this.numInputs];
			this.yVals	= new int[this.numInputs];
			
			this.rectWidth	= this.width / (this.numInputs / 2);
			this.rectHeight	= this.height / 2;
			
			for(int i = 0; i < this.xVals.length; i++)
			{
				int xPos	= i % (this.numInputs / 2);
				int xVal	= xPos * (rectWidth);
				xVals[i]	= xVal;
				System.out.println(i + ": xPos = " + xPos + "; xVal = " + xVal);
			} // for - xVals
			
			for(int i = 0; i < this.yVals.length; i++)
			{
				int	yPos	= i / (this.numInputs / 2);
				int	yVal	= yPos * rectHeight;
				yVals[i]	= yVal;
				System.out.println(i + ": yPos = " + yPos + "; yVal = " + yVal);
			} // for - yVals
		} else {
			// odd - only 9:
			if(this.numInputs == 9)
			{
				this.xVals	= new int[] { 0, (this.width / 3), ((this.width / 3) * 2)	};
				this.yVals	= new int[] { 0, (this.height / 3), ((this.height / 3) * 2)	};
			} else {
				throw new IllegalArgumentException("Module_01_02: numInputs is " + this.numInputs + "; must be either 2, 4, 6, 8, or 9.");
			} // else
		} // else - odds

		this.goalHue		= new float[this.numInputs][3];
		this.curHue			= new float[this.numInputs][3];

		this.newHuePos		= new int[this.numInputs];
		this.goalHuePos		= new int[this.numInputs];
		this.curHuePos		= new int[this.numInputs];

		this.moduleTemplate	= new ModuleTemplate[this.numInputs];

		this.nowBelow			= new boolean[this.numInputs];
		this.colorReachedArray	= new boolean[this.numInputs][3];
		this.colorReached		= new boolean[this.numInputs];
		this.attRelTran			= new int[this.numInputs];		// 0 = attack, 1 = release, 2 = transition

		this.colorRange		= new float[this.numInputs][3];
		this.colorAdd		= new float[this.numInputs][3];
		//TODO: input will only initialize with the number of ins it can handle, and the numInputs here will not match that if it changes.
		for(int i = 0; i < this.numInputs; i++)
		{
			System.out.println("passing " + this.xVals[i] + ", " + this.yVals[i] + " as x and y to new ModuleTemplate");
			this.moduleTemplate[i]	= new ModuleTemplate(this, this.input, "Module_01_02_PitchHueBackground", this.xVals[i], this.yVals[i], this.rectWidth, this.rectHeight, i);
			
			if(this.moduleTemplate[i].getSidebarCP5() != null)
			{
//				this.moduleTemplate[i].getSidebarCP5().setVisible(false);
				this.moduleTemplate[i].setLeftEdgeX(this.xVals[i]);
			}

			this.nowBelow[i]			= false;
			this.colorReachedArray[i]	= new boolean[] { false, false, false };
			this.colorReached[i]		= false;
			this.attRelTran[i]			= 0;

			// Round, because the Midi notes come out with decimal places, and we want to get
			// to the real closest note, not just the next note down.
			// However, also have to find min, in case it rounds up to 12 (we want no more than 11).
			curHuePos[i]    = Math.min(round(input.getAdjustedFundAsMidiNote(i + 1) % 12), 11);
			this.curHue[i]		= new float[] { 255, 255, 255 };

			for(int j = 0; j < this.curHue[i].length; j++)
			{
				this.colorRange[i][j]	= Math.abs(this.goalHue[i][j] - this.curHue[i][j]);

				// divide the attack/release/transition value by 50
				// and divide colorRange by that value to find the amount to add each 50 millis.
				this.colorAdd[i][j]	= this.colorRange[i][j] / (this.moduleTemplate[i].getART(this.attRelTran[i]) / 50);
			} // for - j

		} // for - i

		noStroke();
		background(150);

	} // setup()

	public void draw()
	{	
		for(int i = 0; i < this.numInputs; i++)
		{
//			System.out.println("input.getAdjustedFundAsMidiNote(" + (i + 1) + ") = " + input.getAdjustedFundAsMidiNote(i + 1) + 
//					"; input.getAmplitude(" + (i + 1) + ") = " + input.getAmplitude(1 + 1));
			
			// The following line is necessary so that key press shows the menu button
			if (keyPressed == true) 
			{
				this.moduleTemplate[i].setMenuVal();
			} // if keyPressed

			if (input.getAmplitude(i + 1) > this.moduleTemplate[i].getThresholdLevel())
			{
				this.nowBelow[i]	= false;

				// subtracting keyAddVal gets the number into the correct key 
				// (simply doing % 12 finds the scale degree in C major).
				//newHuePos  = round(input.getAdjustedFundAsMidiNote(1)) % 12;
				int	scaleDegree	= (round(input.getAdjustedFundAsMidiNote(i + 1)) - this.moduleTemplate[i].getCurKeyEnharmonicOffset() + 3 + 12) % 12;

				newHuePos[i]	= scaleDegree;

				if(newHuePos[i] > this.moduleTemplate[i].getColors().length || newHuePos[i] < 0)	{
					throw new IllegalArgumentException("Module_01_02_PitchHue_MultipleInputs.draw: newHuePos " + newHuePos[i] + " is greater than colors.length (" + this.moduleTemplate[i].getColors().length + ") or less than 0.");
				}

				// set goalHue to the color indicated by the current pitch:
				if (newHuePos[i] != goalHuePos[i]) {
					goalHuePos[i]  = newHuePos[i];
				} // if

				for(int k = 0; k < this.goalHue[i].length; k++)
				{
					this.goalHue[i][k]	= this.moduleTemplate[i].getColors()[goalHuePos[i]][k];
				}


			} else {
				// volume not above the threshold:
				this.nowBelow[i]	= true;

				this.goalHue[i]	= new float[] { 
						this.moduleTemplate[i].getCanvasColor()[0],
						this.moduleTemplate[i].getCanvasColor()[1],
						this.moduleTemplate[i].getCanvasColor()[2]
				};

			} // else - above/below threshold


			if(this.moduleTemplate[i].getCheckpoint() < this.millis())
			{
				for(int l = 0; l < 3; l++)
				{
					if(this.curHue[i][l] < this.goalHue[i][l])
					{
						this.curHue[i][l]	=	this.curHue[i][l] + this.colorAdd[i][l];
					} else if(this.curHue[i][l] > this.goalHue[i][l])
					{
						this.curHue[i][l]	=	this.curHue[i][l] - this.colorAdd[i][l];
					}
				} // for - l

				this.moduleTemplate[i].setCheckpoint(this.millis() + 50);
			} // if - adding every 50 millis

			/*
			System.out.println("curHue: " + this.curHue[0] + ", " + 
					+ this.curHue[1] + ", "
					+ this.curHue[2]);
			System.out.println("goalHue: " + this.goalHue[0] + ", " + 
							+ this.goalHue[1] + ", "
							+ this.goalHue[2]);
			System.out.println("input.getAmplitude() = " + input.getAmplitude());
			 */

			float	lowBound;
			float	highBound;

			// TODO: is this loop doing anything necessary?
			// - setting colorReachedArray
			for (int m = 0; m < 3; m++)
			{
				lowBound	= this.goalHue[i][m] - 5;
				highBound	= this.goalHue[i][m] + 5;

				// First, check colors and add/subtract as necessary:
				if (this.curHue[i][m] >= highBound)
				{
					//					this.curHue[m] = this.curHue[m] - this.moduleTemplate.getART(attRelTran);
				} else if (this.curHue[i][m] <= lowBound)
				{
					//					this.curHue[m]  = this.curHue[m] + this.moduleTemplate.getART(attRelTran);
				} // if - adjust colors


				// Now check colors for whether they have moved into the boundaries:
				if(this.curHue[i][m] < highBound && this.curHue[i][m] > lowBound) {
					// if here, color has been reached.
					this.colorReachedArray[i][m]	= true;
				} else {
					this.colorReachedArray[i][m]	= false;
				}
			} // for

			// If all elements of the color are in range, then the color has been reached:
			this.colorReached[i]	= this.colorReachedArray[i][0] && this.colorReachedArray[i][1] && this.colorReachedArray[i][2];

			//  background(this.curHue[0], this.curHue[1], this.curHue[2]);
			fill(this.curHue[i][0], this.curHue[i][1], this.curHue[i][2]);		
			// Rectangle: "the first two parameters set the location of the upper-left corner, the third sets the width, and the fourth sets the height."
//			rect(moduleTemplate[i].getLeftEdgeX(), this.yVals[i], rectWidth - (this.xVals[i] - moduleTemplate[i].getLeftEdgeX()), rectHeight);
			//		stroke(255);
			rect(this.xVals[i], this.yVals[i], rectWidth, rectHeight);



			if(this.moduleTemplate[i].isShowScale())
			{
				this.moduleTemplate[i].setLeftEdgeX(this.xVals[i]);
				// draws the legend along the bottom of the screen:
				this.moduleTemplate[i].legend(goalHuePos[i], 14);
			}

			int	oldART	= this.attRelTran[i];

			// If coming from a low amplitude note and not yet reaching a color,
			// use the attack value to control the color change:
			if(!this.nowBelow[i] && !colorReached[i]) 
			{	
				this.attRelTran[i]	= 0;
			} else if(!this.nowBelow[i] && colorReached[i]) {
				// Or, if coming from one super-threshold note to another, use the transition value:
				this.attRelTran[i]	= 2;
			} else if(this.nowBelow[i]) {
				// Or, if volume fell below the threshold, switch to release value:
				this.attRelTran[i]	= 1;
			}

			if(this.attRelTran[i] != oldART)
			{			
				// Calculate color ranges:
				for(int n = 0; n < this.curHue[i].length; n++)
				{
					this.colorRange[i][n]	= Math.abs(this.goalHue[i][n] - this.curHue[i][n]);

					// divide the attack/release/transition value by 50
					// and divide colorRange by that value to find the amount to add each 50 millis.
					this.colorAdd[i][n]	= this.colorRange[i][n] / (this.moduleTemplate[i].getART(this.attRelTran[i]) / 50);
				}
			} // if
		} // for

	} // draw()


	/**
	 * All modules with a ModuleTemplate must include this method.
	 * 
	 * @param theControlEvent	a ControlEvent that will be passed to controlEvent in ModuleTemplate.
	 */
	public void controlEvent(ControlEvent theControlEvent)
	{
		try
		{
			int	i	= modTempNum;	
		} catch(Exception e)
		{
			// TODO: put the print back in
//			println("Module_01_02_PitchHue.controlEvent: caught Exception " + e + " from controlEvent " + theControlEvent);
			//			e.printStackTrace();
		}
	} // controlEvent




	/**
	 * Used in draw for determining whether a particular scale degree is in the 
	 * major or minor scale;
	 * returns the position of the element if it exists in the array,
	 * or -1 if the element is not in the array.
	 * 
	 * @param array		int[] to be searched for the given element
	 * @param element	int whose position in the given array is to be returned.
	 * @return		position of the given element in the given array, or -1 
	 * 				if the element does not exist in the array.
	 */
	private int  arrayContains(int[] array, int element)
	{
		if(array == null) {
			throw new IllegalArgumentException("Module_01_02_PitchHue_MultipleInputs.arrayContains(int[], int): array parameter is null.");
		}

		//  println("array.length = " + array.length);
		for (int i = 0; i < array.length; i++)
		{
			//    println("array[i] = " + array[i]);
			if (array[i] == element) {
				return i;
			} // if
		} // for

		return -1;
	} // arrayContains(int[], int)

} // class