package module_01_PitchHueBackground.module_01_02_PitchHueBackground_ModuleTemplate_EMM;

import processing.core.*;
import interfascia.*;

import java.awt.Color;

import addons.Buttons;
import addons.HScrollbar;
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

	// TODO: Make most variables private.
	// Lable for the scrollbar:
	GUIController controller;
	IFTextField   textField;
	IFLabel       label;

	int  hueMax;
	int  saturationMax;
	int  brightnessMax;

	Input  input;
	int  threshold;      // when amp is below this, the background will be black.
	/*
	int[]	majorScaleDegrees;
	int[]	minorScaleDegrees;
	int[][]	scaleDegrees;
	*/
	private	String[]	notesCtoBFlats;
	private	String[]	notesCtoBSharps;
	private	int		keyAddVal;		// this is added the Midi note values of the pitch before mod'ing, to get scale degree in correct key.

	int  hue;
	int  saturation;

	float[]  newHue;
	// TODO: initialize in a better place.
	float[]  goalHue	= new float[3];
	float[]  curHue;
	float    attackTime;

	int  newHuePos;
	int  goalHuePos;
	int  curHuePos;

	int  newSaturation;
	int  goalSaturation;
	int  curSaturation;
	int  changeInSaturation;

	float[][]  colors;          // holds the RGB values for the colors responding to HSB: every 30th H with 100 S, 100 B

	HScrollbar  scrollbar;
	float       scrollbarPos;
	float       attackTimeMax  = 20;
	float       attackTimeMin  = 1;

	PFont    consolas;

	PShape  playButton;
	PShape  stopButton;
	boolean  showPlay;
	boolean  showStop;

	PShape	rightArrow;
	PShape	leftArrow;
	boolean	showRightArrow;

	boolean  sidebarOut;
	int      leftEdgeX;

	HScrollbar[]	scrollbarArray;
	HScrollbar[]	modulateScrollbarArray;

	int	scrollbarX;
	int	modulateScrollbarX;

	HScrollbar  thresholdScroll;
	HScrollbar  attackScroll;
	HScrollbar  releaseScroll;
	HScrollbar  transitionScroll;
	HScrollbar  keyScroll;
	HScrollbar  rootScroll;

	int  scrollWidth1;

	int    hideY;
	int    thresholdY;
	int    attackY;
	int    releaseY;
	int    transitionY;
	int    keyY;
	int    rootColorY;
	int    colorStyleY;
	int    pitchColorCodesY;
	int    a_deY;
	int    ab_eY;
	int    b_fY;
	int		cY;
	int    cd_fgY;
	int    d_gaY;
	int    redModulateY;
	int    greenModulateY;
	int    blueModulateY;
	int[]	textYVals	= new int[] {
			hideY,
			thresholdY,
			attackY,
			releaseY,
			transitionY,
			keyY,
			rootColorY,
			colorStyleY,
			pitchColorCodesY
	}; //textYVals
	int[]	noteYVals = new int[] {
			a_deY,
			ab_eY,
			b_fY,
			cY,
			cd_fgY,
			d_gaY
	}; // noteYVals
	int[]	modulateYVals	= new int[] {
			redModulateY,
			greenModulateY,
			blueModulateY,
	};

	int		playButtonX;
	int    arrowX;
	int    scaleX;

	int    hideWidth;

	int    rainbowX;
	int    dichromaticX;
	int    trichromaticX;
	int    customX;

	String[]  buttonLabels;
	IFButton[]  buttons;
	Buttons   buttonWrapper;

	int		noteNameX1;
	int		noteNameX2;

	IFTextField[]	textFieldArray;
	IFTextField[]	noteTextFieldArray;
	IFTextField[]	modulateTextFieldArray;
	int		textFieldX;
	int[]		noteTextFieldX;	// array because there is a different xPos for each column
	int		modulateTextFieldX;
	int		textFieldWidth;
	int		noteTextFieldWidth;
	int		modulateTextFieldWidth;

	float[][][]	rainbowColors;

	
	ControlP5	cp5;
	ModuleTemplate	moduleTemplate;
	
	boolean	aboveThreshold	= false;
	boolean	belowThreshold	= true;
	
	boolean		nowBelow			= false;
	boolean[]	colorReachedArray	= new boolean[] { false, false, false };
	boolean		colorReached		= false;
	int			attackReleaseTransition	= 0;	// 0 = attack, 1 = release, 2 = transition


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
		
		hueMax         = 360;
		saturationMax  = 300;
		brightnessMax  = 100;


		this.moduleTemplate.setCurKey("G", 2);

		threshold    = 15;

		noStroke();
		background(150);

		// Round, because the Midi notes come out with decimal places, and we want to get
		// to the real closest note, not just the next note down.
		// However, also have to find min, in case it rounds up to 12 (we want no more than 11).
		curHuePos    = Math.min(round(input.getAdjustedFundAsMidiNote(1) % 12), 11);

		if(curHuePos < 0 || curHuePos > this.moduleTemplate.getColors().length) {
			System.out.println("Module_01_02.setup(): curHuePos " + curHuePos + " is out of the bounds of the colors; setting to 0.");
			curHuePos	= 0;
		}

		curHue	= new float[] { 255, 255, 255 };
		// The following line caused problems!
		// (That is, it made that position in colors follow curHue as the latter changed.)
		// Never use it.
//		curHue	= this.moduleTemplate.colors[curHuePos];
		// would like to change more quickly, but there's a weird flicker if attackTime gets bigger:
		attackTime  = 10;

		curSaturation       = (int)Math.min(input.getAmplitude(1), 300);
		changeInSaturation  = 10;
		//  String[] fontList = PFont.list();
		//  printArray(fontList);

		//		consolas  = createFont("./data/Consolas.ttf", 10);
		//		textFont(consolas);
		// sets the text size used to display note names:
		textSize(24);

		scrollbar = new HScrollbar(this, 10, 45, (width / 2) - 10, 18, 5);

		controller = new GUIController(this);

		IFLookAndFeel	modTemplate	= new IFLookAndFeel(this, IFLookAndFeel.MOD_TEMPLATE);
		//		println("modTemplate.activeColor = " + modTemplate.activeColor);
		controller.setLookAndFeel(modTemplate);
		controller.setVisible(false);

		this.sidebarOut  = false;
		this.leftEdgeX   = 0;

		this.scrollbarX  = (width / 3) / 4;
		this.modulateScrollbarX	= this.scrollbarX;

		// set y vals for first set of scrollbar labels:
		textYVals[0]	=	40;
		// Given our height = 250 and "hide" (textYVals[0]) starts at 40,
		// We want a difference of 27.  This gets that:
		int	yValDif = (int)((height - textYVals[0]) / 18);//(textYVals.length + noteYVals.length + modulateYVals.length));
		// ... but no smaller than 25:
		if(yValDif < 25) {
			yValDif	= 25;
		}
		for(int i = 1; i < textYVals.length - 1; i++)
		{
			textYVals[i]	= textYVals[i - 1] + yValDif;
		} // for
		// Add extra space before "Pitch Color Codes":
		textYVals[textYVals.length - 1]	= textYVals[textYVals.length - 2] + (int)(yValDif * 1.5);

		// set y vals for the note names:
		noteYVals[0]	= textYVals[textYVals.length - 1] + yValDif;
		for(int i = 1; i < noteYVals.length; i++)
		{
			noteYVals[i]	= noteYVals[i - 1] + yValDif;
		}

		// set y vals for the color modulate scrollbars:
		// (add double space between this and previous group of note text fields)
		modulateYVals[0]	= noteYVals[noteYVals.length - 1] + (int)(yValDif * 1.5);
		for(int i = 1; i < modulateYVals.length; i++)
		{
			modulateYVals[i]	= modulateYVals[i - 1] + yValDif;
		}

		int	colorStyleWidth	= 52;
		int	colorStyleSpace	= 4;

		this.rainbowX     = scrollbarX;
		this.dichromaticX  = scrollbarX + colorStyleWidth + colorStyleSpace;
		this.trichromaticX  = scrollbarX + (colorStyleWidth + colorStyleSpace) * 2;
		this.customX  = scrollbarX + (colorStyleWidth + colorStyleSpace) * 3;

		this.buttons  = new IFButton[] {
				// annoyingly, buttons have to be placed15 pts higher than their corresponding text
				new IFButton("PlayButton", playButtonX, textYVals[0] - 15, hideWidth),
				new IFButton("Arrow", arrowX, textYVals[0] - 15, hideWidth), 
				new IFButton("Scale", scaleX, textYVals[0] - 15, hideWidth), 
				new IFButton("Rainbow", rainbowX, textYVals[7] - 15, colorStyleWidth), 	// 7: colorStyle
				new IFButton("Dichrom.", dichromaticX, textYVals[7] - 15, colorStyleWidth), 
				new IFButton("Trichrom.", trichromaticX, textYVals[7] - 15, colorStyleWidth), 
				new IFButton("Custom", customX, textYVals[7] - 15, colorStyleWidth), 
		}; // buttons

		for(int i = 0; i < this.buttons.length; i++)
		{
			this.buttons[i].addActionListener(this);
			this.controller.add(this.buttons[i]);
		} // for - add buttons to controller
		/*		
		this.buttons[0].addActionListener(this);
		this.controller.add(this.buttons[0]);
		 */
		this.buttons[0].fireEventNotification(this.controller, "setup: fired Event notification.");
		scrollWidth1  = 150;
		//  thresholdScroll  = new HScrollbar(scrollbarX, thresholdY - 5, scrollWidth1);
		/*
		scrollbarArray  = new HScrollbar[]  {
				new HScrollbar(this, scrollbarX, textYVals[1] - 5, scrollWidth1),
				new HScrollbar(this, scrollbarX, textYVals[2] - 5, scrollWidth1),
				new HScrollbar(this, scrollbarX, textYVals[3] - 5, scrollWidth1),
				new HScrollbar(this, scrollbarX, textYVals[4] - 5, scrollWidth1),
				new HScrollbar(this, scrollbarX, textYVals[5] - 5, scrollWidth1),
				new HScrollbar(this, scrollbarX, textYVals[6] - 5, scrollWidth1),
		};
		 */
		// don't want scrollbars next to Hide, ColorStyle, or PitchColorCodes -- thus the "i" math:
		this.scrollbarArray	= new HScrollbar[textYVals.length - 3];
		for(int i = 0; i < this.scrollbarArray.length; i++)
		{
			this.scrollbarArray[i]	= new HScrollbar(this, scrollbarX, textYVals[i + 1] - 5, scrollWidth1);
		} // for - initialize scrollbarArray

		this.modulateScrollbarArray	= new HScrollbar[modulateYVals.length];
		for(int i = 0; i < this.modulateScrollbarArray.length; i++)
		{
			this.modulateScrollbarArray[i]	= new HScrollbar(this, modulateScrollbarX, modulateYVals[i] - 5, scrollWidth1);
		} // for - initialize color modulate scrollbars

		String[]	textFieldLabels	= new String[] {
				"thresholdNum",
				"attackNum",
				"releaseNum",
				"transitionNum",
				"keyVal",
				"rootColorCode"
		}; // textFieldLabels

		textFieldX	= (int) this.scrollbarArray[0].getSposMax() + 20;
		textFieldWidth	= (width / 3) - textFieldX - 10;
		String[]	textFieldInitialContent	= new String[] {
				"Par#",
				"0:00",
				"0:00",
				"0:00",
				"C Chromatic",
				"Code#"
		};

		this.textFieldArray	= new IFTextField[] {
				new IFTextField("thresholdNum", textFieldX, textYVals[1] - 16, textFieldWidth, "Par"),
				new IFTextField("attackNum", textFieldX, textYVals[2] - 16, textFieldWidth, "0:00"),
				new IFTextField("releaseNum", textFieldX, textYVals[3] - 16, textFieldWidth, "0:00"),
				new IFTextField("transitionNum", textFieldX, textYVals[4] - 16, textFieldWidth, "0:00"),
				new IFTextField("keyVal", textFieldX, textYVals[5] - 16, textFieldWidth, "C Chromatic"),
				new IFTextField("rootColorCode", textFieldX, textYVals[6] - 16, textFieldWidth, "Code#"),
		};
		for(int i = 0; i < this.textFieldArray.length; i++)
		{
			//this.textFieldArray[i]	= new IFTextField(textFieldLabels[i], textFieldX, textYVals[i + 1] - 16, textFieldWidth, textFieldInitialContent[i]);
			this.textFieldArray[i].addActionListener(this);
			this.controller.add(this.textFieldArray[i]);
		}

		this.noteNameX1	= 40;
		this.noteNameX2 = this.noteNameX1 + 135;

		this.noteTextFieldWidth	= 85;
		this.noteTextFieldX	= new int[] { this.scrollbarX, this.scrollbarX + this.noteTextFieldWidth + 50 };

		this.noteTextFieldArray	= new IFTextField[noteYVals.length * 2];
		int	whichX;
		for(int i = 0; i < this.noteTextFieldArray.length; i++)
		{
			if(i < 6) {
				whichX	= 0;
			} else {
				whichX	= 1;
			}
			this.noteTextFieldArray[i]	= new IFTextField("noteTextFieldArray[" + i + "]", noteTextFieldX[whichX], noteYVals[i % noteYVals.length] - 16, noteTextFieldWidth, "Code#");
			this.noteTextFieldArray[i].addActionListener(this);
			this.controller.add(this.noteTextFieldArray[i]);
		}

		//		this.modulateTextFieldX	= (int)this.modulateScrollbarArray[0].getSposMax() + 25;
		this.modulateTextFieldX	= this.textFieldX;
		this.modulateTextFieldWidth	= this.textFieldWidth;
		this.modulateTextFieldArray	= new IFTextField[this.modulateYVals.length];
		for(int i = 0; i < this.modulateTextFieldArray.length; i++)
		{
			modulateTextFieldArray[i]	= new IFTextField("modulateTextFieldArray[" + i + "]", modulateTextFieldX, modulateYVals[i] - 16, modulateTextFieldWidth, "Code#");
			this.modulateTextFieldArray[i].addActionListener(this);
			this.controller.add(this.modulateTextFieldArray[i]);
		} // for - initialize modulate textField array

	
	} // setup()

	public void draw()
	{
		stroke(255);

		if (input.getAmplitude() > this.moduleTemplate.getThresholdLevel())
		{

			this.nowBelow	= false;
			
			// subtracting keyAddVal gets the number into the correct key 
			// (simply doing % 12 finds the scale degree in C major).
			//newHuePos  = round(input.getAdjustedFundAsMidiNote(1)) % 12;
			int	scaleDegree	= (round(input.getAdjustedFundAsMidiNote(1)) - this.moduleTemplate.getKeyAddVal() + 12) % 12;
			
			// chromatic:
			if(this.moduleTemplate.getMajMinChrom() == 2)

			{
				newHuePos	= scaleDegree;
			} else {
				// major or minor:

				int	inScale	= this.arrayContains(this.moduleTemplate.getScaleDegrees()[this.moduleTemplate.getMajMinChrom()], scaleDegree);

				if(inScale > -1)
				{
					newHuePos	= inScale;
//					println(newHuePos + " is the position in this scale.");
				} // if - check if degree is in the scale

			} // if - current scale is Major or Minor		


			if(newHuePos > this.moduleTemplate.getColors().length || newHuePos < 0)	{
				throw new IllegalArgumentException("Module_01_02.draw: newHuePos " + newHuePos + " is greater than colors.length (" + colors.length + ") or less than 0.");
			}
			newHue  = this.moduleTemplate.getColors()[newHuePos];

			//  newHue  = newHue * 30;  // this is for HSB, when newHue is the color's H value
			
			// set goalHue to the color indicated by the current pitch:
			if (newHuePos != goalHuePos)
			{
				goalHuePos  = newHuePos;
			} // if
			goalHue  = this.moduleTemplate.getColors()[goalHuePos];
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
		stroke(255);


		if(this.moduleTemplate.isShowScale())
		{
			//TODO: if anything else in ModuleTemplate needs to be called every time in draw,
			// 		we'll just set up a draw() method in ModuleTemplate that does it all.

			// This fill() is having no effect:
//			fill(150, 50, 150);
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
	
		/*
		for(int i = 0; i < this.moduleTemplate.getColors().length; i++)
		{
			System.out.println("this.moduleTemplate.getColors()[" + i + "][0] = " + this.moduleTemplate.colors[i][0] + 
					"; this.moduleTemplate.getColors()[" + i + "][1] = " + this.moduleTemplate.colors[i][1] + 
					"; this.moduleTemplate.getColors()[" + i + "][2] = " + this.moduleTemplate.colors[i][2]);
		}
*/
		
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
