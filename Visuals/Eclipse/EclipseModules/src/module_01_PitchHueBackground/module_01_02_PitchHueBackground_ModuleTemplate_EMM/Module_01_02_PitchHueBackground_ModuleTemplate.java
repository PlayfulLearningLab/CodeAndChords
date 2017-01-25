package module_01_PitchHueBackground.module_01_02_PitchHueBackground_ModuleTemplate_EMM;

import processing.core.*;

import core.Input;
import core.ModuleTemplate;
import	controlP5.*;

public class Module_01_02_PitchHueBackground_ModuleTemplate extends PApplet
{

	/**
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
		PApplet.main("module_01_PitchHueBackground.module_01_02_PitchHueBackground_ModuleTemplate_EMM.Module_01_02_PitchHueBackground_ModuleTemplate");
	} // main

	// Choose input file here:
	// Raw:
	//String  inputFile  = "src/module_01_PitchHueBackground/module_01_02_PitchHueBackground_ModuleTemplate_EMM/Emily_CMajor-2016_09_2-16bit-44.1K Raw.wav";
	// Tuned:
	String  inputFile  = "src/module_01_PitchHueBackground/module_01_02_PitchHueBackground_ModuleTemplate_EMM/Emily_CMajor-2016_09_2-16bit-44.1K Tuned.wav";
	// Kanye:
	//String  inputFile  = "src/module_01_PitchHueBackground/module_01_02_PitchHueBackground_ModuleTemplate_EMM/Emily_CMajor-2016_09_2-16bit-44.1K Kanye.wav";


	private Input  input;


	// TODO: initialize in a better place.
	private float[]  goalHue	= new float[3];
	private float[]  curHue;

	private int  newHuePos;
	private int  goalHuePos;
	private int  curHuePos;

	private float[][]  colors;          // holds the RGB values for the colors responding to HSB: every 30th H with 100 S, 100 B
	private ModuleTemplate	moduleTemplate;
	private boolean		nowBelow			= false;
	private boolean[]	colorReachedArray	= new boolean[] { false, false, false };
	private boolean		colorReached		= false;
	private int			attackReleaseTransition	= 0;	// 0 = attack, 1 = release, 2 = transition


	public void settings()
	{
		size(925, 520);
	} // settings

	public void setup() 
	{
		input  = new Input();
		this.moduleTemplate	= new ModuleTemplate(this, this.input, "Module_01_02_PitchHueBackground");
		this.moduleTemplate.initModuleTemplate();
		
		this.moduleTemplate.setCurKey("A", 2);
		this.moduleTemplate.rainbow();


		this.moduleTemplate.setCurKey("G", 2);


		//  input        = new Input(inputFile);

		noStroke();
		background(150);
		
		// Round, because the Midi notes come out with decimal places, and we want to get
		// to the real closest note, not just the next note down.
		// However, also have to find min, in case it rounds up to 12 (we want no more than 11).
		curHuePos    = Math.min(round(input.getAdjustedFundAsMidiNote(1) % 12), 11);
		if(curHuePos < 0 || curHuePos > this.moduleTemplate.colors.length) {
			System.out.println("Module_01_02.setup(): curHuePos " + curHuePos + " is out of the bounds of the colors; setting to 0.");
			curHuePos	= 0;
		}

		curHue	= new float[] { 255, 255, 255 };
		// The following line caused problems!
		// (That is, it made that position in colors follow curHue as the latter changed.)
		// Never use it.
//		curHue	= this.moduleTemplate.colors[curHuePos];

	
	} // setup()

	public void draw()
	{
		if (input.getAmplitude() > this.moduleTemplate.getThresholdLevel())
		{

			this.nowBelow	= false;
			
			// subtracting keyAddVal gets the number into the correct key 
			// (simply doing % 12 finds the scale degree in C major).
			//newHuePos  = round(input.getAdjustedFundAsMidiNote(1)) % 12;
			int	scaleDegree	= (round(input.getAdjustedFundAsMidiNote(1)) - this.moduleTemplate.getKeyAddVal() + 12) % 12;
/*			System.out.println("key = " + moduleTemplate.curKey + "; keyAddVal = " + 
					moduleTemplate.getKeyAddVal() + "; scaleDegree = " + scaleDegree);
	*/		
			// chromatic:
			if(this.moduleTemplate.getMajMinChrom() == 2) {
				newHuePos	= scaleDegree;
			} else {
				// major or minor:

				int	inScale	= this.arrayContains(this.moduleTemplate.getScaleDegrees()[this.moduleTemplate.getMajMinChrom()], scaleDegree);

				if(inScale > -1) {
					newHuePos	= inScale;
//					println(newHuePos + " is the position in this scale.");
				} // if - check if degree is in the scale

			} // if - current scale is Major or Minor		


			if(newHuePos > this.moduleTemplate.colors.length || newHuePos < 0)	{
				throw new IllegalArgumentException("Module_01_02.draw: newHuePos " + newHuePos + " is greater than colors.length (" + colors.length + ") or less than 0.");
			}

			//  newHue  = newHue * 30;  // this is for HSB, when newHue is the color's H value
			
			// set goalHue to the color indicated by the current pitch:
			if (newHuePos != goalHuePos) {
				goalHuePos  = newHuePos;
			} // if
			goalHue  = this.moduleTemplate.colors[goalHuePos];
		} else {
			// volume not above the threshold:
			this.nowBelow	= true;
			
			goalHue	= new float[] { 0, 0, 0 };
			
		} // else

		
		float	lowBound;
		float	highBound;

		for (int i = 0; i < 3; i++)
		{
			lowBound	= goalHue[i] - this.moduleTemplate.getAttackReleaseTransition(attackReleaseTransition);
			highBound	= goalHue[i] + this.moduleTemplate.getAttackReleaseTransition(attackReleaseTransition);
			
			
			// First, check colors and add/subtract as necessary:
			if (curHue[i] >= highBound)
			{
				curHue[i] = curHue[i] - this.moduleTemplate.getAttackReleaseTransition(attackReleaseTransition);
			} else if (curHue[i] <= lowBound)
			{
				curHue[i]  = curHue[i] + this.moduleTemplate.getAttackReleaseTransition(attackReleaseTransition);
			} // if - adjust colors
			
			
			// Now check colors for whether they have moved into the boundaries:
			if(curHue[i] < highBound && curHue[i] > lowBound) {
				// if here, color has been reached.
				this.colorReachedArray[i]	= true;
			} else {
				this.colorReachedArray[i]	= false;
			}
		} // for
		
		// If all elements of the color are in range, then the color has been reached:
		this.colorReached	= this.colorReachedArray[0] && this.colorReachedArray[1] && this.colorReachedArray[2];

		//  background(curHue[0], curHue[1], curHue[2]);
		fill(curHue[0], curHue[1], curHue[2]);		
		rect(moduleTemplate.getLeftEdgeX(), 0, width - moduleTemplate.getLeftEdgeX(), height);
//		stroke(255);


		if(this.moduleTemplate.isShowScale())
		{
			//TODO: if anything else in ModuleTemplate needs to be called every time in draw,
			// 		we'll just set up a draw() method in ModuleTemplate that does it all.

			// draws the legend along the bottom of the screen:
			this.moduleTemplate.legend(goalHuePos);
		}

		// If coming from a low amplitude note and not yet reaching a color,
		// use the attack value to control the color change:
		if(!this.nowBelow && !colorReached) {
			this.attackReleaseTransition	= 0;
			
		} else if(!this.nowBelow && colorReached) {
			// Or, if coming from one super-threshold note to another, use the transition value:
			this.attackReleaseTransition	= 2;
		} else if(this.nowBelow) {
			// Or, if volume fell below the threshold, switch to release value:
			this.attackReleaseTransition	= 1;
		}
		
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
			println("Module_01_02.controlEvent: theControlEvent = " + theControlEvent +
					"; this.moduleTemplate = " + this.moduleTemplate);

			this.moduleTemplate.controlEvent(theControlEvent);	
		} catch(Exception e)
		{
			println("Module_01_02.controlEvent: caught Exception " + e);
			e.printStackTrace();
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
			throw new IllegalArgumentException("Module_01_02.arrayContains(int[], int): array parameter is null.");
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
