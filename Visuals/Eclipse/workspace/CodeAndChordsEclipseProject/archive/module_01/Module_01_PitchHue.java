package module_01;

import processing.core.*;
import core.Module;
import core.ModuleMenu;
import core.input.MicrophoneInput;
import net.beadsproject.beads.core.AudioContext;

public class Module_01_PitchHue extends Module
{
	/**
	 * 
	 *
	 * 08/01/2016
	 * Emily Meuer
	 *
	 * Background changes hue based on pitch.
	 *
	 * (Adapted from Examples => Color => Hue.)
	 */

	public static void main(String[] args)
	{

		PApplet.main("module_01.Module_01_PitchHue");
	} // main

	
	public void setup() 
	{
		this.input	= new MicrophoneInput(16, new AudioContext(), true, this);
		this.totalNumInputs	= this.input.getAdjustedNumInputs();
		this.curNumInputs	= 1;
		
		this.menu	= new ModuleMenu(this, this, this.input, 12);

		// Setup the menu:
		this.menu.addLandingMenu();
		this.menu.addSensitivityMenu(true);
		this.menu.addColorMenu();

	} // setup()

	public void draw()
	{
		//		System.out.println("input.getAdjustedFundAsMidiNote(1) = " + input.getAdjustedFundAsMidiNote(1));

		// The following line is necessary so that key press shows the menu button
		if (keyPressed == true) 
		{
			this.menu.setMenuVal();
		}

		// The usual drill: get the current pitch; use it to determine the color; get that color:
		int	scaleDegree	= (round(input.getAdjustedFundAsMidiNote(0)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;
		this.menu.fadeColor(scaleDegree, 0);
		fill(this.menu.getCurHue()[0][0], this.menu.getCurHue()[0][1], this.menu.getCurHue()[0][2]);

		// The mapCurrent_Pox() makes sure that the rectangle won't cover up the Menu when opened:
		rect(this.menu.mapCurrentXPos(0), this.menu.mapCurrentYPos(0), width - this.menu.mapCurrentXPos(0), this.menu.mapCurrentYPos(height));
		//		stroke(255);

		if(this.menu.isShowScale())
		{
			// draws the legend along the bottom of the screen:
			this.legend(scaleDegree, 0);
		} // if showScale
		
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