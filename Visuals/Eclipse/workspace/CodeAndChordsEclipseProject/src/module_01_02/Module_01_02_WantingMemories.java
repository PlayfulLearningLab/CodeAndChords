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
	 * 08/01/2016
	 * Emily Meuer
	 *
	 * Square for each input, and the background color changes hue based on pitch; plays Wanting Memories;
	 * see Module_01_PitchHue.java for explanatory comments.
	 */

	public static void main(String[] args)
	{
		PApplet.main("module_01_02.Module_01_02_WantingMemories");
	} // main

	private	RecordedInput	recInput;
	
	
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

		// Determines the size and (x, y) positions of each square based on the current number of inputs:
		this.setSquareValues();

		// call add methods:
		this.menu.addLandingMenu();
		this.menu.addSensitivityMenu(true);
		this.menu.addColorMenu();
	} // setup()

	
	public void draw()
	{
		int	scaleDegree;
		int	curX;
		int	curY;
		
		// The following line is necessary so that key press shows the menu button
		if (keyPressed == true && !this.menu.getIsRunning()) 
		{
			this.menu.setMenuVal();
		} // if keyPressed
		
		for(int i = 0; i < this.curNumInputs; i++)
		{
//			System.out.println("input.getAdjustedFundAsMidiNote(" + (i + 1) + ") = " + input.getAdjustedFundAsMidiNote(i + 1) + 
//					"; input.getAmplitude(" + (i + 1) + ") = " + input.getAmplitude(1 + 1));

			scaleDegree	= (round(this.recInput.getAdjustedFundAsMidiNote(i)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;


			this.menu.fadeColor(scaleDegree, i);
			
			this.fill(this.menu.getCurHue()[i][0], this.menu.getCurHue()[i][1], this.menu.getCurHue()[i][2], this.menu.getAlphaVal());

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
	
	
	/**
	 * Every Module instance has to define what to show as the legend (scale) along the bottom.
	 * 
	 * @return	String[] of the current scale
	 */
	public String[] getLegendText()
	{
		return this.menu.getScale(this.menu.getCurKey(), this.menu.getMajMinChrom());
	} // getLegendText

} // class