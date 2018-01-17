package module_01_02;

import processing.core.*;
import core.Module;
import core.ModuleMenu;
import core.input.RecordedInput;

public class Module_01_02_WantingMemories extends Module
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
		PApplet.main("module_01_02.Module_01_02_WantingMemories");
	} // main

	private	RecordedInput	recInput;
	
	private	int[]	canvasColor;
	
/*	private	int		numInputs;

	private	int[]	xVals;
	private	int[]	yVals;
	private	int[]	rectWidths;
	private	int[]	rectHeights;
*/
	
	public void setup() 
	{
		
		this.recInput	= new RecordedInput(this, new String[] { 
				"WantingMemories_Melody.wav",
				"WantingMemories_Alto.wav",
//				"WantingMemories_Bass.wav",
				"WMBass_Later_Quiet.wav",
				"WantingMemories_Soprano.wav",
//				"WantingMemories_Tenor.wav",
				"WMTenor_Medium.wav"
		});
//		this.input	= new RealTimeInput(16, new AudioContext(), true, this);
		//this.input          = RealTimeInput() Elena give this the audiocontext in branch
		this.totalNumInputs	= this.recInput.getAdjustedNumInputs();
		this.curNumInputs	= this.totalNumInputs;
		
		this.menu	= new ModuleMenu(this, this, this.recInput, 12);

		this.setSquareValues();

		// call add methods:
		this.menu.addLandingMenu();
		this.menu.addSensitivityMenu(true);
		this.menu.addColorMenu();
	} // setup()

	
	public void draw()
	{
		this.canvasColor	= this.menu.getCanvasColor();
		this.background(canvasColor[0], canvasColor[1], canvasColor[2]);
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
			
//			scaleDegree	= (round(input.getAdjustedFundAsMidiNote(i + 1)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;
			scaleDegree	= (round(this.recInput.getAdjustedFundAsMidiNote(i)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;

			this.menu.fade(scaleDegree, i);
			
			this.fill(this.menu.getCurHue()[i][0], this.menu.getCurHue()[i][1], this.menu.getCurHue()[i][2], this.menu.getAlphaVal());
			
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
		
		this.menu.runMenu();

	} // draw()
	
	
	public String[] getLegendText()
	{
		return this.menu.getScale(this.menu.getCurKey(), this.menu.getMajMinChrom());
	} // getLegendText

} // class