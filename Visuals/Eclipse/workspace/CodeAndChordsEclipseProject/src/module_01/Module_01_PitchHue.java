package module_01;

import processing.core.*;
import core.Module;
import core.ModuleMenu;
import core.PortAudioAudioIO;
import core.Archive_ModuleTemplate.ModuleTemplate01;
import core.input.RealTimeInput;
import module_02.Module_02_AmplitudeHSB;
import net.beadsproject.beads.core.AudioContext;
import	controlP5.*;

public class Module_01_PitchHue extends Module
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

//	private	DisposeHandler	disposeHandler;

	private int  curHuePos;

	private RealTimeInput  input;
//	private ModuleTemplate01	moduleTemplate;
	
//	private	ModuleMenu	menu;

/*	public void settings()
	{
//		fullScreen();
		size(925, 520);
	} // settings
*/
	
	public void setup() 
	{
		int[]				textYVals;
		int[]				modulateYVals;
		int[]               modulateHSBVals;
//		int					colorSelectY;		

		textYVals		 = new int[18];
		modulateYVals	 = new int[3];
		modulateHSBVals	= new int[3];
		
//	this.disposeHandler	= new DisposeHandler(this);
		this.input = new RealTimeInput(1, new AudioContext(), this);
	//	this.input  = new RealTimeInput(this);
//		this.moduleTemplate	= new ModuleTemplate01(this, this.input, "Module_01_PitchHue");
		this.menu	= new ModuleMenu(this, this, this.input, "Module_01_PitchHue", 12);
		
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

		// call add methods:
		
		this.menu.addHideButtons(0, textYVals[0]);
		
//		this.menu.addSliders(textYVals[1], textYVals[2], textYVals[3], textYVals[4]);
		this.menu.addPianoThresholdSlider(0, textYVals[1]);
		
		this.menu.addARTSliders(0, textYVals[2], textYVals[3], textYVals[4]);

		this.menu.addGuideTonePopout(0, textYVals[5]);
		this.menu.addKeySelector(0, textYVals[5]);
		this.menu.setCurKey("A", 2);

		modulateHSBVals[0] = textYVals[6];
		modulateHSBVals[1] = textYVals[7];
		modulateHSBVals[2] = textYVals[8];

		modulateYVals[0]	= textYVals[9];
		modulateYVals[1]	= textYVals[10];
		modulateYVals[2]	= textYVals[11];

		// Adding ColorSelect first since everything to do with colors depends on that:
		String[] noteNames = new String[] {
				"A", "A#/Bb", "B", "C", "C#/Db", "D", "D#/Db", "E", "F", "F#/Gb", "G", "G#/Ab"
		}; // noteNames
		
		this.menu.addColorSelect(0, new int[] { textYVals[15], textYVals[16], textYVals[17] }, noteNames, "Custom Pitch\nColor Select", false);
		

		// ColorSelect and ColorStyle added out of order so that the 2nd Color
		// and 3rd Color select buttons will exist for the Rainbow ColorStyle
		// to lock them.
//		this.addColorSelectButtons(textYVals[14]);
		String[] buttonLabels	= new String[] {
				"Canvas", "Tonic", "2nd Color", "3rd Color"
		}; // buttonLabels
		this.menu.addSpecialColors(0, textYVals[14], buttonLabels, "Color Select", true);

		// addColorStyleButtons will set the colorStyle to rainbow() first:
		this.menu.addColorStyleButtons(0, textYVals[13]);

		this.menu.addHSBSliders(0, modulateHSBVals);

		this.menu.addModulateSliders(0, modulateYVals);

		this.menu.setColorStyle(ModuleTemplate01.CS_RAINBOW, 0);

		this.menu.getControlP5().getController("keyDropdown").bringToFront();

		noStroke();
		background(150);		

		// Round, because the Midi notes come out with decimal places, and we want to get
		// to the real closest note, not just the next note down.
		// However, also have to find min, in case it rounds up to 12 (we want no more than 11).
//		curHuePos    = Math.min(round(input.getAdjustedFundAsMidiNote(1) % 12), 11);
		
		// Moved the % 12 from the above line out of round() so that we don't have to min() from 12 to 11:
		curHuePos    = round(input.getFundAsMidiNote(1)) % 12;
		
		// TODO - can it run w/out this?
//		this.menu.setCurHueColorRangeColorAdd(curHuePos);

	} // setup()

	public void draw()
	{
		//		System.out.println("input.getAdjustedFundAsMidiNote(1) = " + input.getAdjustedFundAsMidiNote(1));

		// The following line is necessary so that key press shows the menu button
		if (keyPressed == true) 
		{
			this.menu.setMenuVal();
		}

		int	scaleDegree	= (round(input.getFundAsMidiNote(1)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;
		this.menu.fade(scaleDegree, 0);

		fill(this.menu.getCurHue()[0][0], this.menu.getCurHue()[0][1], this.menu.getCurHue()[0][2]);
		rect(this.menu.mapCurrentXPos(0), this.menu.mapCurrentYPos(0), width - this.menu.mapCurrentXPos(0), this.menu.mapCurrentYPos(height));
		//		stroke(255);

		if(this.menu.isShowScale())
		{
			// draws the legend along the bottom of the screen:
			this.legend(scaleDegree, 0);
		} // if showScale
		
		this.menu.runMenu();
		

		// TODO - trying to find the trichromatic major/minor customPitchColor bug:
/*		if(this.moduleTemplate.getCurColorStyle() == ModuleTemplate01.CS_TRICHROM)
		{
			for(int i = 0; i < moduleTemplate.trichromColors.length; i++)
			{
				this.fill(moduleTemplate.trichromColors[i][0], moduleTemplate.trichromColors[i][1], moduleTemplate.trichromColors[i][2]);
				this.ellipse(this.width / 2, i * 30 + 60, 30, 30);
			}
		} // if		
		*/
	} // draw()
	
	public String[] getLegendText()
	{
		return this.menu.getScale(this.menu.getCurKey(), this.menu.getMajMinChrom());
	} // getLegendText
	

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
		
		Module_01_PitchHue	module;

		DisposeHandler(PApplet pa)
		{
			module	= (Module_01_PitchHue)pa;
			pa.registerMethod("dispose", this);
		}

		public void dispose()
		{
			this.module.input.stop();
		}
	} // DisposeHandler
	*/
} // class