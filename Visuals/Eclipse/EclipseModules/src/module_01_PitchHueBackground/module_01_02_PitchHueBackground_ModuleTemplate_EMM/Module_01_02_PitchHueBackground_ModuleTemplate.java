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
 Current struggle = since they're on top of each other, they both contain....
 Probably set a boolean to see which if it goes to?

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

	private	static	char	CS_RAINBOW	= 1;
	private	static	char	CS_DICHROM	= 2;
	private	static	char	CS_TRICHROM	= 3;
	private	static	char	CS_CUSTOM	= 4;
	private	char	curColorStyle;

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
	int[]	majorScaleDegrees;
	int[]	minorScaleDegrees;
	int[][]	scaleDegrees;
	private	String[]	notesCtoBFlats;
	private	String[]	notesCtoBSharps;
	private	int		keyAddVal;		// this is added the Midi note values of the pitch before mod'ing, to get scale degree in correct key.

	int  hue;
	int  saturation;

	float[]  newHue;
	float[]  goalHue;
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

	int	majMinChrom; //		//	0 = major, 1 = minor, 2 = chromatic
	int	scaleLength; //
	String	curKey;

	private int[]	rootColor;
	
	ControlP5	cp5;
	ModuleTemplate	moduleTemplate;

	public void settings()
	{
		size(925, 520);
	} // settings

	public void setup() 
	{
		input  = new Input();
		this.moduleTemplate	= new ModuleTemplate(this, this.input, "Module_01_02_PitchHueBackground");
		this.moduleTemplate.initModuleTemplate();
		
		hueMax         = 360;
		saturationMax  = 300;
		brightnessMax  = 100;

		this.rainbowColors	= new float[][][] { 
			new float[][] {
				{ 255, 0, 0 }, 
				{ 255, (float) 127.5, 0 }, 
				{ 255, 255, 0 }, 
				{ (float) 127.5, 255, 0 },
				{ 0, 255, 255 },  
				{ 0, 0, 255 },
				{ (float) 127.5, 0, 255 }
			}, // major
			new float[][] {
				{ 255, 0, 0 }, 
				{ 255, (float) 127.5, 0 }, 
				{ 255, 255, 0 }, 
				{ (float) 127.5, 255, 0 },
				{ 0, 255, 255 },  
				{ 0, 0, 255 },
				{ (float) 127.5, 0, 255 }
			}, // minor
			new float[][] {
				{ 255, 0, 0 }, 
				{ 255, (float) 127.5, 0 }, 
				{ 255, 255, 0 }, 
				{ (float) 127.5, 255, 0 }, 
				{ 0, 255, 0 }, 
				{ 0, 255, (float) 127.5 }, 
				{ 0, 255, 255 }, 
				{ 0, (float) 127.5, 255 }, 
				{ 0, 0, 255 }, 
				{ (float) 127.5, 0, 255 }, 
				{ 255, 0, 255 }, 
				{ 255, 0, (float) 127.5 }
			} // chromatic
		}; // rainbowColors
	/*	
		this.notesCtoBFlats	= new String[] { 
				"C", 
				"Db", 
				"D", 
				"Eb",
				"E",
				"F", 
				"Gb", 
				"G",
				"Ab",
				"A",
				"Bb",
				"B"
		};
		
		this.notesCtoBSharps	= new String[] { 
				"C", 
				"C#", 
				"D", 
				"D#",
				"E",
				"F", 
				"F#", 
				"G",
				"G#",
				"A",
				"A#",
				"B"
		};
*/
//		this.colors	= new float[12][3];
/*
		this.curColorStyle	= this.CS_RAINBOW;
		this.rootColor	= new int[] { 255, 0, 0, };
		// Start chromatic, rainbow:
	*/	
//		this.curKey	= "D";
//		this.majMinChrom	= 2;

		//		this.setCurKey("G", 2);
		
//		this.rainbow();
		/*
		for(int i = 0; i < this.colors.length && i < this.rainbowColors[2].length; i++)
		{
			for(int j = 0; j < this.colors[i].length && j < this.rainbowColors[2][i].length; j++)
			{
				this.colors[i][j]	= this.rainbowColors[2][i][j];
			} // for - j (going through rgb values)
		} // for - i (going through colors)
		*/

		//  input        = new Input(inputFile);
		threshold    = 15;

		// TODO: keep this here? Move to Module Template?
		this.majorScaleDegrees  = new int[]  {
				0, 
				2, 
				4, 
				5, 
				7, 
				9, 
				11
		};

		this.minorScaleDegrees  = new int[]  {
				0, 
				2, 
				3, 
				5, 
				7, 
				8, 
				10
		};

		this.scaleDegrees	= new int[][] {
			this.majorScaleDegrees,
			this.minorScaleDegrees
		};

		noStroke();
		background(150);

		//  curHuePos    = round(input.getAdjustedFundAsMidiNote(1) % 12) * 30;
		curHuePos    = round(input.getAdjustedFundAsMidiNote(1) % 12);
		curHue       = this.moduleTemplate.getColors()[curHuePos];
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

		// draws the legend
		//legend();

		scrollbar = new HScrollbar(this, 10, 45, (width / 2) - 10, 18, 5);

		controller = new GUIController(this);

		IFLookAndFeel	modTemplate	= new IFLookAndFeel(this, IFLookAndFeel.MOD_TEMPLATE);
		//		println("modTemplate.activeColor = " + modTemplate.activeColor);
		controller.setLookAndFeel(modTemplate);
		controller.setVisible(false);

		// Making the play button:
		int  playDistanceFromEdge  = 20;
		int  playWidth  = 25;
		int  playHeight  = 30;
		float[][]  playButtonVerts  = new float[][] { 
			new float[] { width - playDistanceFromEdge - playWidth, playDistanceFromEdge }, 
			new float[] { width - playDistanceFromEdge - playWidth, playDistanceFromEdge + playHeight }, 
			new float[] { width - playDistanceFromEdge, playDistanceFromEdge + (playHeight / 2) }, 
			new float[] { width - playDistanceFromEdge - playWidth, playDistanceFromEdge }
		};
		this.playButton  = createShape(PShape.PATH);
		this.playButton.beginShape();		// strokeWeight, stoke, fill, etc., can only be set between beginShape and endShape.
		this.playButton.strokeWeight(2);
		this.playButton.stroke(255);
		this.playButton.noFill();
		this.playButton.endShape();
		// Setting vcount to 4 means connects the first and last points with a side, 
		// making it look nice and making mousePressed/contains() functionality work.
		this.playButton.setPath(4, playButtonVerts);
		this.showPlay  = true;

		// Making the stop button:
		int  stopSideSize  = 30;
		int  stopDistanceFromEdge  = 20;
		float[][]  stopButtonVerts  = new float[][] { 
			new float[] { width - stopDistanceFromEdge - stopSideSize, stopDistanceFromEdge }, 
			new float[] { width - stopDistanceFromEdge - stopSideSize, stopDistanceFromEdge + stopSideSize }, 
			new float[] { width - stopDistanceFromEdge, stopDistanceFromEdge + stopSideSize }, 
			new float[] { width - stopDistanceFromEdge, stopDistanceFromEdge }, 
			new float[] { width - stopDistanceFromEdge - stopSideSize, stopDistanceFromEdge }, 
		};
		this.stopButton  = createShape(PShape.PATH);
		this.stopButton.beginShape();
		this.stopButton.strokeWeight(2);
		this.stopButton.stroke(255);
		this.stopButton.noFill();
		this.stopButton.endShape();
		this.stopButton.setPath(5, stopButtonVerts);
		this.showStop  = false;
		this.stopButton.setVisible(false);

		// Making the left arrow:
		int arrowDistanceFromEdge	= 20;
		float	aX		= arrowDistanceFromEdge;		// arrow X coordinate (x val of left edge)
		float	aY		= arrowDistanceFromEdge;		// arrow Y coordinate (y val of upmost point)
		int	aW			= 25;				// arrow width
		int	aH			= 25;				// arrow height (from highest to lowest points)
		stroke(255);
		float	oneQuarterY		= (float) (aY + (.25 * aH));
		float	oneHalfY		= (float) (aY + (.5 * aH));
		float	threeQuartersY	= (float) (aY + (0.75 * aH));
		float	oneY			= aY + aH;
		float	oneHalfX		= (float)(aX + (0.5 * aW));
		float	oneX			= aX + aW;
		/*
		println("oneQuarterY = " + oneQuarterY +
				"\noneHalfY = " + oneHalfY +
				"\nthreeQuartersY = " + threeQuartersY +
				"\noneY = " + oneY +
				"\noneHalfX = " + oneHalfX +
				"\noneX = " + oneX);
		 */
		float[][]	rightArrowVerts	= new float[][] {
			new float[]	{ aX, oneQuarterY },
			new float[] { oneHalfX, oneQuarterY },
			new float[]	{ oneHalfX, aY },
			new float[]	{ oneX, oneHalfY },
			new float[] { oneHalfX, oneY },
			new float[] { oneHalfX, threeQuartersY },
			new float[] { aX, threeQuartersY },
			new float[] { aX, oneQuarterY }		// back where we started
		};

		this.rightArrow	= createShape(PShape.PATH);
		this.rightArrow.beginShape();
		this.rightArrow.strokeWeight(1);
		this.rightArrow.stroke(255);
		this.rightArrow.noFill();
		this.rightArrow.endShape();
		this.rightArrow.setPath(8, rightArrowVerts);
		this.showRightArrow	= true;

		// left arrow:
		aX	= (width / 3) + arrowDistanceFromEdge;
		// redefining these because I changed aX:
		oneQuarterY		= (float) (aY + (.25 * aH));
		oneHalfY		= (float) (aY + (.5 * aH));
		threeQuartersY	= (float) (aY + (0.75 * aH));
		oneY			= aY + aH;
		oneHalfX		= (float)(aX + (0.5 * aW));
		oneX			= aX + aW;

		float[][]	leftArrowVerts	= new float[][] {
			new float[] { aX, oneHalfY },
			new float[] { oneHalfX, aY },
			new float[] { oneHalfX, oneQuarterY },
			new float[] { oneX, oneQuarterY },
			new float[] { oneX, threeQuartersY },
			new float[] { oneHalfX, threeQuartersY },
			new float[] { oneHalfX, oneY },
			new float[] { aX, oneHalfY },
		};
		this.leftArrow	= createShape(PShape.PATH);
		this.leftArrow.setPath(8, leftArrowVerts);
		this.leftArrow.beginShape();
		this.leftArrow.strokeWeight(1);
		this.leftArrow.stroke(255);
		this.leftArrow.noFill();
		this.leftArrow.endShape();
		this.leftArrow.setVisible(false);

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

/*
		hideWidth     = 71;
		int hideSpace	= 4;

		/*
    this.playButtonX  = ((width / 3) / 4) * 1;
   this.arrowX       = ((width / 3) / 4) * 2 + 15;
   this.scaleX       = ((width / 3) / 4) * 3 + 20;
		 */
/*		playButtonX  = scrollbarX;
		arrowX       = scrollbarX + hideWidth + hideSpace;
		scaleX       = scrollbarX + (+ hideWidth + hideSpace) * 2;
*/
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
			// subtracting keyAddVal gets the number into the correct key 
			// (simply doing % 12 finds the scale degree in C major).
			//newHuePos  = round(input.getAdjustedFundAsMidiNote(1)) % 12;
			int	scaleDegree	= (round(input.getAdjustedFundAsMidiNote(1)) - this.moduleTemplate.getKeyAddVal()) % 12;
			
			// chromatic:
			if(this.majMinChrom == 2)
			{
				newHuePos	= scaleDegree;
			} else {
				// major or minor:
				int	inScale	= this.arrayContains(this.scaleDegrees[majMinChrom], scaleDegree);

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
		} else {
			newHue  = new float[] { 0, 0, 0 };
		} // else

		// set goalHue to the color indicated by the current pitch:
		if (newHuePos != goalHuePos)
		{
			goalHuePos  = newHuePos;
		} // if
		goalHue  = this.moduleTemplate.getColors()[goalHuePos];

		for (int i = 0; i < 3; i++)
		{
			if (curHue[i] > (goalHue[i] - this.moduleTemplate.getAttackTime()))
			{
				curHue[i] = curHue[i] - this.moduleTemplate.getAttackTime();
			} else if (curHue[i] < (goalHue[i] + this.moduleTemplate.getAttackTime()))
			{
				curHue[i]  = curHue[i] + this.moduleTemplate.getAttackTime();
			} // if
		} // for

		//  background(curHue[0], curHue[1], curHue[2]);
		fill(curHue[0], curHue[1], curHue[2]);
//		fill(100);
		rect(moduleTemplate.getLeftEdgeX(), 0, width - moduleTemplate.getLeftEdgeX(), height);
		stroke(255);
		//  triangle( 710, 10, 710, 30, 730, 20);
		if(!this.buttons[2].getState())
		{
			//TODO: if anything else in ModuleTemplate needs to be called every time in draw,
			// 		we'll just set up a draw() method in ModuleTemplate that does it all.
			
			// draws the legend along the bottom of the screen:
			this.moduleTemplate.legend(goalHuePos);
		}

		//  scrollbar.update();
		//  scrollbar.display();

		scrollbarPos  = scrollbar.getPos();
		attackTime      = map(scrollbarPos, scrollbar.getSposMin(), scrollbar.getSposMax(), attackTimeMin, attackTimeMax);

		//  label.setLabel("attackTime: " + attackTime);

		// TODO: remove traces of playButton/stopButton/leftArrow/rightArrow
		/*
		shape(this.playButton);
		shape(this.stopButton);
		shape(this.leftArrow);
		shape(this.rightArrow);
*/
		
		/*
		if (mousePressed && (mouseX < (width / 3)))
		{
			this.sidebarOut  = !this.sidebarOut;

			if (sidebarOut)
			{
				this.leftEdgeX  = width / 3;
				this.displaySidebar();
			} else {
				this.leftEdgeX  = 0;
				this.controller.setVisible(false);
			} // if - leftEdgeX
		} // if - mousePressed
		 */
		
//		this.moduleTemplate.update();
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

	private int arrayContains(String[] array, String element) {
		if(array == null) {
			throw new IllegalArgumentException("Module_01_02.arrayContains(String[], String): array parameter is null.");
		}
		if(element == null) {
			throw new IllegalArgumentException("Module_01_02.arrayContains(String[], String): String parameter is null.");
		}
		
		for (int i = 0; i < array.length; i++)
		{
			//    println("array[i] = " + array[i]);
			if (array[i] == element) {
				return i;
			} // if
		} // for

		return -1;
	}

	private void printArray(String[] array)
	{
		for(int i = 0; i < array.length - 1; i++)
		{
			print(array[i] + ", ");
		}
		print(array[array.length - 1] + ";\n");
	}
/*
	void displaySidebar()
	{
		this.controller.setVisible(true);
		this.leftEdgeX  = width / 3;

		stroke(255);
		fill(0);
		rect(0, 0, leftEdgeX, height);

		int  textX  = 5;

		String[]	textArray	= new String[] {
				"Hide",
				"Threshold",
				"Attack",
				"Release",
				"Transition",
				"Key",
				"Root Color",
				"Color Style",
				"Pitch Color Codes"				
		}; // textArray


		String[]	noteNames1	= new String[] {
				"A", "A#/Bb", "B", "C", "C#/Db", "D"
		}; // noteNames
		String[]	noteNames2	= new String[] {
				"D#/Eb", "E", "F", "F#/Gb", "G", "G#/Ab"
		}; // noteNames2

		String[]	modulateText	= new String[] {
				"Red Modulate", "Green Mod.", "Blue Modulate"
		}; // modulateText

		textSize(10);
		fill(255);
		text("Module_01_01_PitchHueBackground", 10, 15);

		for(int i = 0; i < textArray.length; i++)
		{
			text(textArray[i], textX, textYVals[i]);
		}

		for(int i = 0; i < noteNames1.length; i++)
		{
			text(noteNames1[i], noteNameX1, noteYVals[i]);
		}
		for(int i = 0; i < noteNames2.length; i++)
		{
			text(noteNames2[i], noteNameX2, noteYVals[i]);
		}

		for(int i = 0; i < modulateText.length; i++)
		{
			text(modulateText[i], textX, modulateYVals[i]);
		}

		for(int i = 0; i < scrollbarArray.length; i++)
		{
			scrollbarArray[i].update();
			scrollbarArray[i].display();
		} // for - update and display first set of scrollbars

		for(int i = 0; i < this.modulateScrollbarArray.length; i++)
		{
			modulateScrollbarArray[i].update();
			modulateScrollbarArray[i].display();
		} // for - update and display modulate color scrollbars
	} // displaySidebar
*/
	/*
	public String[] getScale(String key, int majMinChrom)
	{
		String[][] allNotes	= new String[][] {
			new String[] { "A" , "A" }, 
			new String[] { "A#", "Bb" }, 
			new String[] { "B" , "Cb" },
			new String[] { "C" , "C" },
			new String[] { "C#", "Db" }, 
			new String[] { "D" , "D" }, 
			new String[] { "D#", "Eb" }, 
			new String[] { "E" , "E" }, 
			new String[] { "F" , "F"}, 
			new String[] { "F#", "Gb" }, 
			new String[] { "G" , "G" }, 
			new String[] { "G#", "Ab" }
		};
		int[]	majorScale	= new int[] {
				2, 2, 1, 2, 2, 2, 1
		};
		int[]	minorScale	= new int[] {
				2, 1, 2, 2, 1, 2, 2
		};
		int[]	chromaticScale	= new int[] {
				1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1
		};
		int[][]	allScales	= new int[][] {
			majorScale,
			minorScale,
			chromaticScale
		};

		// find starting position in allNotes:
		boolean	flag	= false;
		int[]		startHere	= new int[2];
		for(int i = 0; i < allNotes.length; i++)
		{
			for(int j = 0; j < allNotes[i].length; j++)
			{
				if(allNotes[i][j] == key && flag == false)
				{
					startHere[0]	= i;
					startHere[1]	= j;
					flag	= true;
				} // if
			} // for - j
		} // for - i
		if(flag == false) 
		{
			throw new IllegalArgumentException("Module_01_02_PHB_ModuleTemplate.getScale: key " + key + " is not a valid key.");
		} // if - throw exception if key not found

		// Determine whether the key should use sharps or flats when choosing from enharmonic notes:
		// Be sure to include the space when doing indexOf - to avoid false positives with keys that are sharped
		String	sharps;
		String	flats;
		if(majMinChrom == 0 || majMinChrom == 2)
		{
			sharps	= "C G D A E B ";
			flats	= "F Bb Eb Ab Db Gb ";
		} else {
			sharps	= "A E B F# C# G# D#";
			flats	= "D G C F Bb Eb ";
		}
		String[]	sharpsAndFlats	= new String[] { sharps, flats };

		int	sharpOrFlat	= -1;
		for(int i = 0; i < sharpsAndFlats.length; i++)
		{
			if(sharpsAndFlats[i].indexOf(key + " ") > -1)
			{
				sharpOrFlat	= i;
			}
		}
		if(sharpOrFlat == -1) {
			throw new IllegalArgumentException("Module_01_02.getScale: key " + key + " is not supported at this time. Sorry! Try an enharmonic equivalent.");
		}

		String[]	result	= new String[allScales[majMinChrom].length];
		int	scalePos	= startHere[0];
		// i is position in result;
		// scalePos is position in allNotes
		for(int i = 0; i < result.length; i++)
		{
			result[i]	= allNotes[scalePos][sharpOrFlat];
			scalePos	= (scalePos + allScales[majMinChrom][i]) % allNotes.length;
		}

		this.scaleLength	= result.length;
		this.majMinChrom	= majMinChrom;

		return result;
	} // getScale
*/
	/*
	private void testGetScale()
	{
		String[][] allNotes	= new String[][] {
			new String[] { "A" , "A" }, 
			new String[] { "A#", "Bb" }, 
			new String[] { "B" , "B" },
			new String[] { "C" , "C" },
			new String[] { "C#", "Db" }, 
			new String[] { "D" , "D" }, 
			new String[] { "D#", "Eb" }, 
			new String[] { "E" , "E" }, 
			new String[] { "F" , "F"}, 
			new String[] { "F#", "Gb" }, 
			new String[] { "G" , "G" }, 
			new String[] { "G#", "Ab" }
		}; // allNotes
		for(int i = 0; i < allNotes.length; i++)
		{
			for(int j = 0; j < allNotes[i].length; j++)
			{
				try {
					String[]	scale	= getScale(allNotes[i][j], 0);
					println(allNotes[i][j] + " Major: ");
					printArray(scale);
				} catch(IllegalArgumentException iae) {
					println(iae.getMessage());
				}
			}
		}
		println();
		for(int i = 0; i < allNotes.length; i++)
		{
			for(int j = 0; j < allNotes[i].length; j++)
			{
				try {
					String[]	scale	= getScale(allNotes[i][j], 1);
					println(allNotes[i][j] + " Minor: ");
					printArray(scale);
				} catch(IllegalArgumentException iae) {
					println(iae.getMessage());
				}
			}
		}
		println();
		for(int i = 0; i < allNotes.length; i++)
		{
			for(int j = 0; j < allNotes[i].length; j++)
			{
				try {
					String[]	scale	= getScale(allNotes[i][j], 2);
					println(allNotes[i][j] + " Chromatic: ");
					printArray(scale);
				} catch(IllegalArgumentException iae) {
					println(iae.getMessage());
				}
			}
		}
	} // testGetScale
/*
	void legend()
	{

		textSize(24);
		/*
		String[] notes = new String[] {
				"C", 
				"C#", 
				"D", 
				"D#", 
				"E", 
				"F", 
				"F#", 
				"G", 
				"G#", 
				"A", 
				"A#", 
				"B", 
				"C"
		}; // notes
		 */
/*			String[]	notes	= this.getScale(this.curKey, this.majMinChrom);
		// 12/19: updating to be on the side.
		// 01/05: changing it back!
		float  sideWidth   = (width - leftEdgeX) / notes.length;
		float  sideHeight  = width / notes.length;
		//  float  side = height / colors.length;

		//	stroke(255);
		noStroke();

		for (int i = 0; i < notes.length; i++)
		{
			fill(this.colors[i][0], this.colors[i][1], this.colors[i][2]);
			/*
			if(i == 0)
			{
				for(int j = 0; j < this.colors[i].length; j++)
				{
					println("legend: colors[0][" + j + "] = " + colors[0][j]);
				}
			}
			 */
/*			if (i == goalHuePos) {
				rect(leftEdgeX + (sideWidth * i), (float)(height - (sideHeight * 1.5)), sideWidth, (float) (sideHeight * 1.5));
				//      rect(0, (side * i), side * 1.5, side);
			} else {
				rect(leftEdgeX + (sideWidth * i), height - sideHeight, sideWidth, sideHeight);
				//      rect(0, (side * i), side, side);
			}
			fill(0);
			text(notes[i], (float) (leftEdgeX + (sideWidth * i) + (sideWidth * 0.35)), height - 20);
		} // for
	} // legend
*/

	/**
	 * Converts the given color to HSB and sends it to dichromatic_OneHSB.
	 * (dichromatic_OneHSB will send it to _TwoHSB, which will set this.colors, changing the scale.)
	 * 
	 * @param rgbVals	float[] of RGB values defining the color for the root of the scale.
	 */
	public void dichromatic_OneRGB(int[] rgbVals)
	{
		if(rgbVals == null) {
			throw new IllegalArgumentException("Module_01_02.dichromatic_OneRGB: int[] parameter is null.");
		}

		float[]	hsbVals	= new float[3];
		Color.RGBtoHSB(rgbVals[0], rgbVals[1], rgbVals[2], hsbVals);

		for(int i = 0; i < hsbVals.length; i++)
		{
			println("dichromatic_OneRGB: hsbVals[" + i + "] = " + hsbVals[i]);
		}

		this.dichromatic_OneHSB(hsbVals);
	} // dichromatic_OneRGB

	/**
	 * Uses the given HSB color to find the color across it on the HSB wheel,
	 * converts both colors to RGB, and passes them as parameters to dichromatic_TwoRGB.
	 * 
	 * @param hue	float[] of HSB values defining the color at the root of the current scale.
	 */
	private void dichromatic_OneHSB(float[] hsbVals)
	{
		if(hsbVals == null) {
			throw new IllegalArgumentException("Module_01_02.dichromatic_OneHSB: float[] parameter hsbVals is null.");
		} // error checking

		// find the complement:
		float[]	hsbComplement	= new float[] { (float) ((hsbVals[0] + 0.5) % 1), 1, 1 };

		for(int i = 0; i < hsbVals.length; i++)
		{
			println("    hsbVals[" + i + "] = " + hsbVals[i]);
		}

		for(int i = 0; i < hsbComplement.length; i++)
		{
			println("    hsbComplement[" + i + "] = " + hsbComplement[i]);
		}

		// convert them both to RGB;
		float[]	rgbVals1	= new float[3];
		float[]	rgbVals2	= new float[3];

		int	rgb1	= Color.HSBtoRGB(hsbVals[0], hsbVals[1], hsbVals[2]);
		println("rgb1 = " + rgb1);
		Color	rgbColor1	=  new Color(rgb1);

		// Using individual get[Color]() functions b/c getComponents() uses a 0-1 range.
		rgbVals1[0]	= rgbColor1.getRed();
		rgbVals1[1]	= rgbColor1.getGreen();
		rgbVals1[2]	= rgbColor1.getBlue();	
		for(int i = 0; i < rgbVals1.length; i++)
		{
			println("    rgbVals1[" + i + "] = " + rgbVals1[i]);
		}

		int	rgb2	= Color.HSBtoRGB(hsbComplement[0], hsbComplement[1], hsbComplement[2]);
		println("rgb2 = " + rgb2);
		Color	rgbColor2	=  new Color(rgb2);

		// Using individual get[Color]() functions b/c getComponents() uses a 0-1 range.
		rgbVals2[0]	= rgbColor2.getRed();
		rgbVals2[1]	= rgbColor2.getGreen();
		rgbVals2[2]	= rgbColor2.getBlue();	
		for(int i = 0; i < rgbVals2.length; i++)
		{
			println("    rgbVals2[" + i + "] = " + rgbVals2[i]);
		}

		this.dichromatic_TwoRGB(rgbVals1, rgbVals2);
	} // dichromatic_OneHSB(int)

	/**
	 * This method fills colors with the spectrum of colors between the given rgb colors.
	 * 
	 * @param rgbVals1	float[] of rgb values defining rootColor.
	 * @param rgbVals2	float[] of rgb values defining the color of the last note of the scale.
	 */
	public void dichromatic_TwoRGB(float[] rgbVals1, float[] rgbVals2)
	{
		if(rgbVals1 == null || rgbVals2 == null) {
			throw new IllegalArgumentException("Module_01_02.dichromatic_TwoRGB: at least one of the float[] parameters is null.");
		} // error checking

		float	redDelta	= (rgbVals1[0] - rgbVals2[0]) / this.scaleLength;
		float	greenDelta	= (rgbVals1[1] - rgbVals2[1]) / this.scaleLength;
		float	blueDelta	= (rgbVals1[2] - rgbVals2[2]) / this.scaleLength;
		println("redDelta = " + redDelta + "; greenDelta = " + greenDelta + "; blueDelta = " + blueDelta);

		for(int i = 0; i < rgbVals1.length; i++)
		{
			this.colors[0][i]	= rgbVals1[i];
		}
		for(int i = 1; i < this.scaleLength && i < this.colors.length; i++)
		{
			for(int j = 0; j < this.colors[i].length; j++)
			{
				this.colors[i][0]	= this.colors[i - 1][0] - redDelta;
				this.colors[i][1]	= this.colors[i - 1][1] - greenDelta;
				this.colors[i][2]	= this.colors[i - 1][2] - blueDelta;
				println("colors[" + i + "][0] = " + colors[i][0] +
						"; colors[" + i + "][1] = " + colors[i][1] + 
						"; colors[" + i + "][2] = " + colors[i][2]);
			} // for - j
		} // for - i
	} // dichromatic_TwoRGB

	/**
	 * Converts the given color to HSB and sends it to dichromatic_OneHSB.
	 * (dichromatic_OneHSB will send it to _TwoHSB, which will set this.colors, changing the scale.)
	 * 
	 * @param rgbVals	float[] of RGB values defining the color for the root of the scale.
	 */
	public void trichromatic_OneRGB(int[] rgbVals)
	{
		if(rgbVals == null) {
			throw new IllegalArgumentException("Module_01_02.trichromatic_OneRGB: int[] parameter is null.");
		}

		float[]	hsbVals	= new float[3];
		Color.RGBtoHSB(rgbVals[0], rgbVals[1], rgbVals[2], hsbVals);

		for(int i = 0; i < hsbVals.length; i++)
		{
			println("trichromatic_OneRGB: hsbVals[" + i + "] = " + hsbVals[i]);
		}

		this.trichromatic_OneHSB(hsbVals);
	} // trichromatic_OneRGB

	/**
	 * ** This method should not be called w/out setting rootColor before hand.
	 * 
	 * Uses the given HSB color to find the color across it on the HSB wheel,
	 * converts both colors to RGB, and passes them as parameters to dichromatic_TwoRGB.
	 *
	 * @param hsbVals	float[] of HSB values defining the color at the root of the current scale.
	 */
	private void trichromatic_OneHSB(float[] hsbVals)
	{
		if(hsbVals == null) {
			throw new IllegalArgumentException("Module_01_02.dichromatic_OneHSB: float[] parameter hsbVals is null.");
		} // error checking

		// find the triad:
		float[]	hsbTriad1	= new float[] { (float) ((hsbVals[0] + 0.33) % 1), 1, 1 };
		float[]	hsbTriad2	= new float[] { (float) ((hsbVals[0] + 0.67) % 1), 1, 1 };

		for(int i = 0; i < hsbVals.length; i++)
		{
			println("    hsbVals[" + i + "] = " + hsbVals[i]);
		}

		for(int i = 0; i < hsbTriad1.length; i++)
		{
			println("    hsbTriad1[" + i + "] = " + hsbTriad1[i]);
		}

		// convert them both to RGB;
		float[]	rgbVals1	= new float[3];
		float[]	rgbVals2	= new float[3];
		float[]	rgbVals3	= new float[3];

		int	rgb1	= Color.HSBtoRGB(hsbVals[0], hsbVals[1], hsbVals[2]);
		println("rgb1 = " + rgb1);
		Color	rgbColor1	=  new Color(rgb1);

		// Using individual get[Color]() functions b/c getComponents() uses a 0-1 range.
		rgbVals1[0]	= rgbColor1.getRed();
		rgbVals1[1]	= rgbColor1.getGreen();
		rgbVals1[2]	= rgbColor1.getBlue();	
		for(int i = 0; i < rgbVals1.length; i++)
		{
			println("    rgbVals1[" + i + "] = " + rgbVals1[i]);
		}

		int	rgb2	= Color.HSBtoRGB(hsbTriad1[0], hsbTriad1[1], hsbTriad1[2]);
		println("rgb2 = " + rgb2);
		Color	rgbColor2	=  new Color(rgb2);

		// Using individual get[Color]() functions b/c getComponents() uses a 0-1 range.
		rgbVals2[0]	= rgbColor2.getRed();
		rgbVals2[1]	= rgbColor2.getGreen();
		rgbVals2[2]	= rgbColor2.getBlue();	
		for(int i = 0; i < rgbVals2.length; i++)
		{
			println("    rgbVals2[" + i + "] = " + rgbVals2[i]);
		}

		int	rgb3	= Color.HSBtoRGB(hsbTriad2[0], hsbTriad2[1], hsbTriad2[2]);
		println("rgb3 = " + rgb3);
		Color	rgbColor3	=  new Color(rgb3);

		// Using individual get[Color]() functions b/c getComponents() uses a 0-1 range.
		rgbVals3[0]	= rgbColor3.getRed();
		rgbVals3[1]	= rgbColor3.getGreen();
		rgbVals3[2]	= rgbColor3.getBlue();	
		for(int i = 0; i < rgbVals3.length; i++)
		{
			println("    rgbVals3[" + i + "] = " + rgbVals3[i]);
		}

		this.trichromatic_ThreeRGB(rgbVals1, rgbVals2, rgbVals3);
	} // trichromatic_OneHSB

	public void trichromatic_ThreeRGB(float[] rgbVals1, float[] rgbVals2, float[] rgbVals3)
	{
		if(rgbVals1 == null || rgbVals2 == null || rgbVals3 == null) {
			throw new IllegalArgumentException("Module_01_02.trichromatic_ThreeRGB: at least one of the float[] parameters is null.");
		} // error checking

		int	color1pos	= 0;
		int	color2pos;
		int	color3pos;

		if(this.majMinChrom == 2)
		{
			// if chromatic scale, put the colors equally throughout:
			color2pos	= this.scaleLength / 3;
			color3pos	= (this.scaleLength / 3) * 2;
		} else {
			color2pos	= 3;	// subdominant
			color3pos	= 4;	// dominant
		}
		println("color2pos = " + color2pos + "; color3pos = " + color3pos);

		// TODO: this might need to be divided by 4 to make it to the actual color (or dichr. should be colors.length - 1?):
		float	redDelta1	= (rgbVals1[0] - rgbVals2[0]) / (color2pos - color1pos);
		float	greenDelta1	= (rgbVals1[1] - rgbVals2[1]) / (color2pos - color1pos);
		float	blueDelta1	= (rgbVals1[2] - rgbVals2[2]) / (color2pos - color1pos);
		println("redDelta1 = " + redDelta1 + "; greenDelta1 = " + greenDelta1 + "; blueDelta1 = " + blueDelta1);

		float	redDelta2	= (rgbVals2[0] - rgbVals3[0]) / (color3pos - color2pos);
		float	greenDelta2	= (rgbVals2[1] - rgbVals3[1]) / (color3pos - color2pos);
		float	blueDelta2	= (rgbVals2[2] - rgbVals3[2]) / (color3pos - color2pos);
		println("redDelta2 = " + redDelta2 + "; greenDelta2 = " + greenDelta2 + "; blueDelta2 = " + blueDelta2);

		float	redDelta3	= (rgbVals3[0] - rgbVals1[0]) / (this.scaleLength - color3pos);
		float	greenDelta3	= (rgbVals3[1] - rgbVals1[1]) / (this.scaleLength - color3pos);
		float	blueDelta3	= (rgbVals3[2] - rgbVals1[2]) / (this.scaleLength - color3pos);
		println("redDelta2 = " + redDelta2 + "; greenDelta2 = " + greenDelta2 + "; blueDelta2 = " + blueDelta2);

		// fill first position with first color:
		for(int i = 0; i < rgbVals1.length; i++)
		{
			this.colors[0][i]	= rgbVals1[i];
		}

		// fill from first color to second color:
		for(int i = 1; i < color2pos + 1; i++)
		{
			for(int j = 0; j < this.colors[i].length; j++)
			{
				this.colors[i][0]	= this.colors[i - 1][0] - redDelta1;
				this.colors[i][1]	= this.colors[i - 1][1] - greenDelta1;
				this.colors[i][2]	= this.colors[i - 1][2] - blueDelta1;
			} // for - j

			println("tri: colors[" + i + "][0] = " + colors[i][0] +
					"; colors[" + i + "][1] = " + colors[i][1] + 
					"; colors[" + i + "][2] = " + colors[i][2]);
		} // for - first color to second color

		/*
		// put in the third color (the second should have been reached in the previous loop):
		for(int i = 0; i < rgbVals3.length; i++)
		{
			this.colors[4][i]	= rgbVals3[i];
		} // for - third color to first color
		 */

		// fill from second color to third color:
		for(int i = color2pos + 1; i < color3pos + 1; i++)
		{
			for(int j = 0; j < this.colors[i].length; j++)
			{
				this.colors[i][0]	= this.colors[i - 1][0] - redDelta2;
				this.colors[i][1]	= this.colors[i - 1][1] - greenDelta2;
				this.colors[i][2]	= this.colors[i - 1][2] - blueDelta2;
			} // for - j

			println("tri: colors[" + i + "][0] = " + colors[i][0] +
					"; colors[" + i + "][1] = " + colors[i][1] + 
					"; colors[" + i + "][2] = " + colors[i][2]);
		} // for - first color to second color

		// fill from third color back to first color:
		for(int i = color3pos + 1; i < this.scaleLength; i++)
		{
			for(int j = 0; j < this.colors[i].length; j++)
			{
				this.colors[i][0]	= this.colors[i - 1][0] - redDelta3;
				this.colors[i][1]	= this.colors[i - 1][1] - greenDelta3;
				this.colors[i][2]	= this.colors[i - 1][2] - blueDelta3;
			} // for - j
			println("tri: colors[" + i + "][0] = " + colors[i][0] +
					"; colors[" + i + "][1] = " + colors[i][1] + 
					"; colors[" + i + "][2] = " + colors[i][2]);
		} // for - third color to first color
	} //trichromatic_ThreeRGB

	/**
	 * Populates colors with rainbow colors (ROYGBIV - with a few more for chromatic scales).
	 */
/*	public void rainbow()
	{
		for(int i = 0; i < this.colors.length && i < this.rainbowColors[this.majMinChrom].length; i++)
		{
			for(int j = 0; j < this.colors[i].length && j < this.rainbowColors[this.majMinChrom][i].length; j++)
			{
				this.colors[i][j]	= this.rainbowColors[this.majMinChrom][i][j];
			} // for - j (going through rgb values)
		} // for - i (going through colors)

	} // rainbow
*/
	/*
	private void updateColors(char colorStyle)
	{
		if(colorStyle < 1 || colorStyle > 4) {
			throw new IllegalArgumentException("Module_01_02.updateColors: char paramter " + colorStyle + " is not recognized; must be 1 - 4.");
		}

		this.curColorStyle	= colorStyle;

		// Rainbow:
		if(this.curColorStyle == this.CS_RAINBOW)
		{
			this.moduleTemplate.rainbow();
		}

		// Dichromatic:
		if(this.curColorStyle == this.CS_DICHROM)
		{
			this.dichromatic_OneRGB(this.rootColor);
		}

		// Trichromatic:
		if(this.curColorStyle == this.CS_TRICHROM)
		{
			this.trichromatic_OneRGB(this.rootColor);
		}

		// Custom:
		if(this.curColorStyle == this.CS_CUSTOM)
		{
			println("Custom is still in the works.");
			
			// Fill textBoxes with current colors (should I do this anyway?  I don't think so, since they can't use them.)
			for(int i = 0; i < noteTextFieldArray.length; i++)
			{
				noteTextFieldArray[i].setValue("(" + this.colors[i][0] + "," + this.colors[i][1] + "," + this.colors[i][2] + ")");
				noteTextFieldArray[i].setVisiblePortionEnd(Math.min(14, noteTextFieldArray[i].getValue().length()));
				println("noteTextFieldArray[" + i + "] = " + noteTextFieldArray[i].getVisiblePortionStart() +
						 "; [i].getVisiblePortionEnd() = " + noteTextFieldArray[i].getVisiblePortionEnd());
			} // for - noteTextFieldArray
			// if [text box event], check color;
			// (if it doesn't pass, don't throw exception -- just make them try again).
			
			// if it does pass, then put that color into colors
			
		}
	} // updateColors
	*/
	/*
	public void setCurKey(String key, int majMinChrom)
	{
		// Check both sharps and flats, and take whichever one doesn't return -1:
		int	modPosition	= Math.max(this.arrayContains(this.notesCtoBFlats, key), this.arrayContains(this.notesCtoBSharps, key));
		
		if(modPosition == -1)	{
			throw new IllegalArgumentException("Module_01_02.setCurKey: " + key + " is not a valid key.");
		}
		println("setCurKey: modPosition = " + modPosition);
		
		this.majMinChrom	= majMinChrom;
		this.curKey			= key;
		this.keyAddVal		= modPosition;
	} // setCurKey
*/
	public void mousePressed()
	{
		// The following for loops stops either the SamplePlayers or the uGens getting audio input (and otherwise creating feedback):

/*
		if (this.showPlay)
		{
			for (int i = 0; i < input.getuGenArray().length; i++)
			{
				input.getuGenArray()[i].pause(true);
			} // for
			if (this.playButton.contains(mouseX, mouseY))
			{
				this.showPlay  = false;
				this.showStop  = true;

				this.input.uGenArrayFromSample(inputFile);
			} // if - playButton
		} else if (this.stopButton.contains(mouseX, mouseY))
		{
			for (int i = 0; i < input.getuGenArray().length; i++)
			{
				input.getuGenArray()[i].pause(true);
			} // for
			this.showStop  = false;
			this.showPlay  = true;

			this.input.uGenArrayFromNumInputs(1);
		} // if - stopButton

		// if the hidePlayButton button was NOT pressed:
		if(!this.buttons[0].getState())
		{
			this.playButton.setVisible(this.showPlay);
			this.stopButton.setVisible(this.showStop);
		} /*else {
			this.playButton.setVisible(false);
			this.playButton.setVisible(false);
		}*/
/*
		if(this.rightArrow.contains(mouseX, mouseY))
		{
			this.showRightArrow	= false;

			this.displaySidebar();

		} else if(this.leftArrow.contains(mouseX, mouseY))
		{
			this.showRightArrow	= true;

			this.leftEdgeX  = 0;
			this.controller.setVisible(false);
		}

		// if the hideArrowButton button was NOT pressed:
		if(!this.buttons[1].getState())
		{
			this.rightArrow.setVisible(this.showRightArrow);
			this.leftArrow.setVisible(!this.showRightArrow);
		}
*/
	} // mousePressed

	
	public void actionPerformed(GUIEvent e) {
		if (e.getMessage().equals("Completed")) {
			// This .setLabel prob. has value for getting values:
			label.setLabel(textField.getValue());
		}
		
		if(e.getSource() == buttons[0])
		{
			println("buttons[0] was clicked!");
		}

		//Hide buttons:
		// PlayButton:
		if(e.getSource() == this.buttons[0])
		{
			if(this.buttons[0].getState())
			{
				this.playButton.setVisible(false);
				this.stopButton.setVisible(false);
			} else {
				this.playButton.setVisible(this.showPlay);
				this.stopButton.setVisible(this.showStop);
			}
		} // playButton

		// Arrow:
		if(e.getSource() == this.buttons[1])
		{
			if(this.buttons[1].getState())
			{
				this.rightArrow.setVisible(false);
				this.leftArrow.setVisible(false);
			} else {
				this.rightArrow.setVisible(this.showRightArrow);
				this.leftArrow.setVisible(!this.showRightArrow);
			} // else
		} // if - hide arrow

		// Scale:
		if(e.getSource() == this.buttons[2])
		{
			// draw() looks at buttons[2].getState() and does not display if the button has been pressed.
		}

		// Color Style:
		// Rainbow:
/*		if(e.getSource() == this.buttons[3])
		{
			this.rainbow();

			for(int i = 4; i < this.buttons.length; i++)
			{
				this.buttons[i].setState(false);
			}
		} // rainbow
*/
		/*
		// Dichromatic:
		if(e.getSource() == this.buttons[4])
		{
			//			this.dichromatic_OneHSB(new float[] { (float)0.333, 1, 1, } );

			//			this.dichromatic_OneRGB(new int[] { 255, 0, 0, });
			this.rootColor	= new int[] { 255, 0, 0, };
			this.updateColors(Module_01_02_PitchHueBackground_ModuleTemplate.CS_DICHROM);

			for(int i = 3; i < this.buttons.length; i++)
			{
				if(i == 4 && i < this.buttons.length - 1) { 	i++;	}
				this.buttons[i].setState(false);
			}
		} // dichromatic
		// Trichromatic:
		if(e.getSource() == this.buttons[5])
		{
			this.rootColor	= new int[] { 255, 0, 0 };
			this.updateColors(Module_01_02_PitchHueBackground_ModuleTemplate.CS_TRICHROM);

			for(int i = 3; i < this.buttons.length; i++)
			{
				if(i == 5 && i < this.buttons.length - 1) { 	i++;	}
				this.buttons[i].setState(false);
			}
		}

		// Custom:
		if(e.getSource() == this.buttons[6])
		{
			println("Color Style: Custom was pressed.");
			this.updateColors(Module_01_02_PitchHueBackground_ModuleTemplate.CS_CUSTOM);

			for(int i = 3; i < 6; i++)
			{
				this.buttons[i].setState(false);
			}
		} // if
		*/
	} // actionPerformed
} // class
