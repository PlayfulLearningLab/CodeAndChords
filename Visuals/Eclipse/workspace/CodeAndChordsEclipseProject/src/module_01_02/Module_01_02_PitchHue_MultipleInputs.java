package module_01_02;

import processing.core.*;
import core.Module;
import core.ModuleMenu;
import core.input.RealTimeInput;
import net.beadsproject.beads.core.AudioContext;

public class Module_01_02_PitchHue_MultipleInputs extends Module
{
	/**
	 * 
	 * 
	 * 08/01/2016
	 * Emily Meuer
	 *
	 * Square for each input, and the background color changes hue based on pitch;
	 * see Module_01_PitchHue.java for explanatory comments.
	 */

	public static void main(String[] args)
	{
		PApplet.main("module_01_02.Module_01_02_PitchHue_MultipleInputs");
	} // main
	
	public void setup() 
	{
		this.input	= new RealTimeInput(16, new AudioContext(), true, this);
		this.totalNumInputs	= this.input.getAdjustedNumInputs();
		this.curNumInputs	= 2;
		
		this.menu	= new ModuleMenu(this, this, this.input, 12);

		// Determines the size and (x, y) positions of each square based on the current number of inputs:
		this.setSquareValues();

		// Call add methods:
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
		/*		if (keyPressed == true && !this.menu.getIsRunning()) 
		{
			this.menu.setMenuVal();
		} // if keyPressed
		 */
		
		for(int i = 0; i < this.curNumInputs; i++)
		{
//			System.out.println("input.getAdjustedFundAsMidiNote(" + (i + 1) + ") = " + input.getAdjustedFundAsMidiNote(i + 1) + 
//					"; input.getAmplitude(" + (i + 1) + ") = " + input.getAmplitude(1 + 1));
			
			scaleDegree	= (round(input.getAdjustedFundAsMidiNote(i)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;

			this.menu.fadeColor(scaleDegree, i);
			
			this.fill(this.menu.getCurHue()[i][0], this.menu.getCurHue()[i][1], this.menu.getCurHue()[i][2]);
			
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
	 * Each Module instance has to define what to show as the legend (scale) along the bottom.
	 * 
	 * @return	String[] of the current scale
	 */
	public String[] getLegendText()
	{
		return this.menu.getScale(this.menu.getCurKey(), this.menu.getMajMinChrom());
	} // getLegendText

} // class