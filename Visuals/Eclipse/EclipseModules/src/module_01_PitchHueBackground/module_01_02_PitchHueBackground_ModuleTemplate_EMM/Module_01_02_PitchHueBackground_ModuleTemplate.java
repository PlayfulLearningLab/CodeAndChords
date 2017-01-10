package module_01_PitchHueBackground.module_01_02_PitchHueBackground_ModuleTemplate_EMM;

import processing.core.*;
import interfascia.*;
import addons.Buttons;
import addons.HScrollbar;
import core.Input;

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

	// Lable for the scrollbar:
	GUIController controller;
	IFTextField   textField;
	IFLabel       label;

	int  hueMax;
	int  saturationMax;
	int  brightnessMax;

	Input  input;
	int  threshold;      // when amp is below this, the background will be black.

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
	boolean	showLeftArrow;

	boolean  sidebarOut;
	int      leftEdgeX;

	HScrollbar[]  scrollbarArray;
	HScrollbar[]	modulateScrollbarArray;

	int    scrollbarX;
	int		modulateScrollbarX;

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

	float[][]	rainbowColors;

	public void settings()
	{
		size(925, 520);
	} // settings

	public void setup() 
	{
		hueMax         = 360;
		saturationMax  = 300;
		brightnessMax  = 100;

		this.rainbowColors	= new float[][] {
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
			{ 255, 0, (float) 127.5 }, 
			{ 255, 0, 0 }
		};

		this.colors	= this.rainbowColors;

		//  input        = new Input(inputFile);
		input  = new Input();
		threshold    = 15;

		noStroke();
		background(0);

		//  curHuePos    = round(input.getAdjustedFundAsMidiNote(1) % 12) * 30;
		curHuePos    = round(input.getAdjustedFundAsMidiNote(1) % 12);
		curHue       = colors[curHuePos];
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
		legend();

		scrollbar = new HScrollbar(this, 10, 45, (width / 2) - 10, 18, 5);

		controller = new GUIController(this);

		IFLookAndFeel	modTemplate	= new IFLookAndFeel(this, IFLookAndFeel.MOD_TEMPLATE);
		//		println("modTemplate.activeColor = " + modTemplate.activeColor);
		controller.setLookAndFeel(modTemplate);
		controller.setVisible(false);

		// Creating the PShape as a square. The corner 
		// is 0,0 so that the center is at 40,40 
		// syntax: createShape(TRIANGLE, x1, y1, x2, y2, x3, y3)
		int  playDistanceFromEdge  = 20;
		int  playWidth  = 25;
		int  playHeight  = 30;
		float[][]  playButtonVerts  = new float[][] { new float[] { width - playDistanceFromEdge - playWidth, playDistanceFromEdge }, 
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

		int  stopSideSize  = 30;
		int  stopDistanceFromEdge  = 20;
		float[][]  stopButtonVerts  = new float[][] { new float[] { width - stopDistanceFromEdge - stopSideSize, stopDistanceFromEdge }, 
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


		float	aX		=	width / 2;		// arrow X coordinate (x val of left edge)
		float	aY		=	height / 2;		// arrow Y coordinate (y val of upmost point)
		int	aW		= 100;				// arrow width
		int	aH		= 100;				// arrow height (from highest to lowest points)
		stroke(255);
		float	oneQuarterY		= (float) (aY + (.25 * aH));
		float	oneHalfY		= (float) (aY + (.25 * aH));
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
		float[][]	leftArrowVerts	= new float[][] {
			new float[]	{ aX, oneQuarterY },
			new float[] { oneHalfX, oneHalfY },
			new float[]	{ oneHalfX, aY },
			new float[]	{ oneX, oneHalfY },
			new float[] { oneHalfX, oneY },
			new float[] { oneHalfX, threeQuartersY },
			new float[] { 20, 50 } ,//new float[] { aX, threeQuartersY },
			new float[] { aX, oneQuarterY }		// back where we started
		};
		this.leftArrow	= createShape(PShape.PATH);
		this.leftArrow.setPath(4, leftArrowVerts);
		this.leftArrow.beginShape();
		this.leftArrow.strokeWeight(2);
		this.leftArrow.stroke(255);
		this.leftArrow.noFill();
		this.leftArrow.endShape();

		this.sidebarOut  = false;
		this.leftEdgeX   = 0;

		this.scrollbarX  = (width / 3) / 4;
		this.modulateScrollbarX	= this.scrollbarX;

		/*
		hideY         = 40;
		thresholdY    = 65;
		attackY       = 90;
		releaseY      = 115;
		transitionY   = 140;
		keyY          = 165;
		rootColorY    = 190;
		colorStyleY   = 215;
		pitchColorCodesY  = 240;
	    a_deY			= 265;
	    ab_eY;
	    b_fY;
	    cd_fgY;
	    d_gaY;
	    redModulateY;
	    greenModulateY;
	    blueModulateY;
		 */
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


		hideWidth     = 71;
		int hideSpace	= 4;

		/*
    this.playButtonX  = ((width / 3) / 4) * 1;
   this.arrowX       = ((width / 3) / 4) * 2 + 15;
   this.scaleX       = ((width / 3) / 4) * 3 + 20;
		 */
		playButtonX  = scrollbarX;
		arrowX       = scrollbarX + hideWidth + hideSpace;
		scaleX       = scrollbarX + (+ hideWidth + hideSpace) * 2;

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

		this.noteTextFieldWidth	= 65;
		this.noteTextFieldX	= new int[] { this.scrollbarX, this.scrollbarX + this.noteTextFieldWidth + 70 };

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

	private void printArray(String[] array)
	{
		for(int i = 0; i < array.length - 1; i++)
		{
			print(array[i] + ", ");
		}
		print(array[array.length - 1] + ";\n");
	}

	public void draw()
	{

		stroke(255);
		if (input.getAmplitude() > threshold)
		{
			// if want something other than C to be the "tonic" (i.e., red),
			// add some number before multiplying.
			newHuePos  = round(input.getAdjustedFundAsMidiNote(1) % 12);
			newHue  = colors[newHuePos];
			//  newHue  = newHue * 30;  // this is for HSB, when newHue is the color's H value
		} else {
			newHue  = new float[] { 0, 0, 0 };
		} // else

		// set goalHue to the color indicated by the current pitch:
		if (newHuePos != goalHuePos)
		{
			goalHuePos  = newHuePos;
		} // if
		goalHue  = colors[goalHuePos];

		for (int i = 0; i < 3; i++)
		{
			if (curHue[i] > (goalHue[i] - attackTime))
			{
				curHue[i] = curHue[i] - attackTime;
			} else if (curHue[i] < (goalHue[i] + attackTime))
			{
				curHue[i]  = curHue[i] + attackTime;
			} // if
		} // for

		//  background(curHue[0], curHue[1], curHue[2]);
		fill(curHue[0], curHue[1], curHue[2]);
		rect(leftEdgeX, 0, width - leftEdgeX, height);

		stroke(255);
		//  triangle( 710, 10, 710, 30, 730, 20);
		if(!this.buttons[2].getState())
		{
			// draws the legend along the bottom of the screen:
			legend();
		}

		//  scrollbar.update();
		//  scrollbar.display();

		scrollbarPos  = scrollbar.getPos();
		attackTime      = map(scrollbarPos, scrollbar.getSposMin(), scrollbar.getSposMax(), attackTimeMin, attackTimeMax);

		//  label.setLabel("attackTime: " + attackTime);

		shape(this.playButton);
		shape(this.stopButton);

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
	} // draw()

	void displaySidebar()
	{
		this.controller.setVisible(true);

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

		return result;
	} // getScale

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

	void legend()
	{

		textSize(24);

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

		// 12/19: updating to be on the side.
		// 01/05: changing it back!
		float  sideWidth   = (width - leftEdgeX) / colors.length;
		float  sideHeight  = width / this.colors.length;
		//  float  side = height / colors.length;

		//	stroke(255);
		noStroke();

		for (int i = 0; i < this.colors.length; i++)
		{
			fill(this.colors[i][0], this.colors[i][1], this.colors[i][2]);
			if (i == goalHuePos) {
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

	/**
	 * Calls the other dichromatic function with saturation of 100 and hue of brightness of 50.
	 * @param hue	int specifying the hue of the color at the root of the scale.
	 */
	private void dichromaticHSB(int hue)
	{
		dichromaticHSB(hue, 100, 100);
	} // dichromatic(int)

	/**
	 * Fills the color array with colors from the specified hue, saturation, and brightness,
	 * to the color opposite on the color wheel (i.e., hue of new color = 360 - givenHue).
	 * 
	 * @param hue	int specifying hue of the color at the root of the scale.
	 * @param saturation	int specifying the saturation of the colors of the scale..
	 * @param brightness	int specifying the brightness of the colors of the scale.
	 */
	private void dichromaticHSB(int hue, int saturation, int brightness)
	{
		colorMode(HSB, 360, 100, 100);
		float[]	color1	= new float[] { hue, saturation, brightness };
		float[]	color2	= new float[] { (hue + 120) % 360, saturation, brightness };
		float	newHue	= (hue + 120) % 360;
		/*
		int	redDelta	= (color1[0] - color2[0]) / this.colors.length;
		int	greenDelta	= (color1[1] - color2[1]) / this.colors.length;
		int	blueDelta	= (color1[2] - color2[2]) / this.colors.length;
		 */
		float	hueDelta	= (newHue - hue) / this.colors.length;

		this.colors[0]	= color1;
		for(int i = 1; i < this.colors.length; i++)
		{
			this.colors[i][0]	= this.colors[i - 1][0] + hueDelta;
			this.colors[i][1]	= saturation;
			this.colors[i][2]	= brightness;
			println("colors[" + i + "][0] = " + colors[i][0] +
					"; colors[" + i + "][1] = " + colors[i][1] + 
					"; colors[" + i + "][2] = " + colors[i][2]);
		} // for - i
	} // dichromatic

	private void dichromaticRGB()
	{
		int[]	color1	= new int[] { 255, 0, 0 };
		int[]	color2	= new int[] { 0, 255, 0 };

		int	redDelta	= (color1[0] - color2[0]) / this.colors.length;
		int	greenDelta	= (color1[1] - color2[1]) / this.colors.length;
		int	blueDelta	= (color1[2] - color2[2]) / this.colors.length;
		println("redDelta = " + redDelta + "; greenDelta = " + greenDelta + "; blueDelta = " + blueDelta);

		for(int i = 0; i < color1.length; i++)
		{
			this.colors[0][i]	= color1[i];
		}
		for(int i = 1; i < this.colors.length; i++)
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
	}

	public void mousePressed()
	{
		// The following for loops stops either the SamplePlayers or the uGens getting audio input (and otherwise creating feedback):


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

	} // mousePressed


	public void actionPerformed(GUIEvent e) {		
		if (e.getMessage().equals("Completed")) {
			// This .setLabel prob. has value for getting values:
			label.setLabel(textField.getValue());
		}

		//Hide buttons:
		// PlayButton:
		if(e.getSource() == this.buttons[0])
		{
			this.playButton.setVisible(false);
			this.stopButton.setVisible(false);
		}

		// Arrow:
		if(e.getSource() == this.buttons[1])
		{
			println("Hide Arrow was pressed.");
			this.leftArrow.setVisible(!this.leftArrow.isVisible());
			int[]	vertexCodes	= this.leftArrow.getVertexCodes();
			println("this.leftArrow = " + this.leftArrow);
			for(int i = 0; i < vertexCodes.length; i++)
			{
				println("vertexCodes[" + i + "] = " + vertexCodes[i]);
			}
		}

		// Scale:
		if(e.getSource() == this.buttons[2])
		{
			// draw() looks at buttons[2].getState() and does not display if the button has been pressed.
		}

		// Color Style:
		// Rainbow:
		if(e.getSource() == this.buttons[3])
		{
			this.colors	= this.rainbowColors;
		}

		// Dichromatic:
		if(e.getSource() == this.buttons[4])
		{
			this.dichromaticHSB(0);
			// Calculate the mathematical relationship between them.
			// Should I just go red, green, blue, all together?
		} // dichromatic

		// Trichromatic:
		if(e.getSource() == this.buttons[5])
		{
			println("Color Style: Trichromatic was pressed.");
		}

		// Custom:
		if(e.getSource() == this.buttons[6])
		{
			println("Color Style: Custom was pressed.");
		}
	} // actionPerformed
} // class
