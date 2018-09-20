package module_01;

import processing.core.*;
import core.Module;
import core.ModuleMenu;
import core.input.RealTimeInput;
import net.beadsproject.beads.core.AudioContext;

public class Module_01_PitchHue_Kristina extends Module
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

		PApplet.main("module_01.Module_01_PitchHue_Kristina");
	} // main


	private RealTimeInput  input;
	
	public void setup() 
	{
		this.input	= new RealTimeInput(16, new AudioContext(), true, this);
		//this.input          = RealTimeInput() Elena give this the audiocontext in branch
		this.totalNumInputs	= this.input.getAdjustedNumInputs();
		this.curNumInputs	= 1;
		
		this.menu	= new ModuleMenu(this, this, this.input, 12);

		this.setSquareValues();

		// call add methods:
//<<<<<<< HEAD
		this.menu.addLandingMenu();
		this.menu.addSensitivityMenu(true);
		this.menu.addColorMenu();
/*=======
		
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
		curHuePos    = round(input.getAdjustedFundAsMidiNote(0)) % 12;
		

>>>>>>> RachelPractice
*/
	} // setup()

	public void draw()
	{
		//		System.out.println("input.getAdjustedFundAsMidiNote(1) = " + input.getAdjustedFundAsMidiNote(1));

		// The following line is necessary so that key press shows the menu button
		if (keyPressed == true) 
		{
			this.menu.setMenuVal();
		}

		int	scaleDegree	= (round(input.getAdjustedFundAsMidiNote(0)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;

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
	} // draw()
	
	public String[] getLegendText()
	{
		return this.menu.getScale(this.menu.getCurKey(), this.menu.getMajMinChrom());
	} // getLegendText
	
} // class