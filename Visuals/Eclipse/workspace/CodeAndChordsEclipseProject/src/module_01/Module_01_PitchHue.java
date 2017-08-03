package module_01;

import processing.core.*;

import core.Input;
import core.ModuleTemplate01;
import core.PortAudioAudioIO;
import net.beadsproject.beads.core.AudioContext;
import	controlP5.*;

public class Module_01_PitchHue extends PApplet
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

		PApplet.main("module_01.Module_01_PitchHue");
		//PApplet.main("module_01_PitchHueBackground.module_01_02_PitchHueBackground_ModuleTemplate_EMM.Module_01_02_PitchHueBackground_ModuleTemplate");
	} // main


	private int  curHuePos;

	private Input  input;
	private ModuleTemplate01	moduleTemplate;


	public void settings()
	{
//		fullScreen();
		size(925, 520);
	} // settings

	public void setup() 
	{
		
		this.input  = new Input();
		this.moduleTemplate	= new ModuleTemplate01(this, this.input, "Module_01_PitchHue");

		noStroke();
		background(150);		

		// Round, because the Midi notes come out with decimal places, and we want to get
		// to the real closest note, not just the next note down.
		// However, also have to find min, in case it rounds up to 12 (we want no more than 11).
		curHuePos    = Math.min(round(input.getAdjustedFundAsMidiNote(1) % 12), 11);
		this.moduleTemplate.setCurHueColorRangeColorAdd(curHuePos);

	} // setup()

	public void draw()
	{

		//		System.out.println("input.getAdjustedFundAsMidiNote(1) = " + input.getAdjustedFundAsMidiNote(1));

		// The following line is necessary so that key press shows the menu button
		if (keyPressed == true) 
		{
			this.moduleTemplate.setMenuVal();
		}

		int	scaleDegree	= (round(input.getAdjustedFundAsMidiNote(1)) - this.moduleTemplate.getCurKeyEnharmonicOffset() + 3 + 12) % 12;
		this.moduleTemplate.fade(scaleDegree);

		fill(this.moduleTemplate.getCurHue()[0], this.moduleTemplate.getCurHue()[1], this.moduleTemplate.getCurHue()[2]);
		rect(moduleTemplate.getLeftEdgeX(), 0, width - moduleTemplate.getLeftEdgeX(), height);
		//		stroke(255);

		if(this.moduleTemplate.isShowScale())
		{
			// draws the legend along the bottom of the screen:
			this.moduleTemplate.legend(scaleDegree);
		} // if showScale
		
/*
		// TODO - trying to find the trichromatic major/minor customPitchColor bug:
		if(this.moduleTemplate.getCurColorStyle() == ModuleTemplate01.CS_TRICHROM)
		{
			for(int i = 0; i < moduleTemplate.trichromColors.length; i++)
			{
				this.fill(moduleTemplate.trichromColors[i][0], moduleTemplate.trichromColors[i][1], moduleTemplate.trichromColors[i][2]);
				this.ellipse(this.width / 2, i * 30 + 60, 30, 30);
			}
		} // if		
*/
	} // draw()
} // class