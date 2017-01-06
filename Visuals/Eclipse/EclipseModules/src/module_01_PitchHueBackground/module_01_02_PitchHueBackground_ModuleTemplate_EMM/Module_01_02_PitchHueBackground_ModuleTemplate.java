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
	//String  inputFile  = "module_01_PitchHueBackground/module_01_02_PitchHueBackground_ModuleTemplate_EMM/Emily_CMajor-2016_09_2-16bit-44.1K Raw.wav";
	// Tuned:
	String  inputFile  = "module_01_PitchHueBackground/module_01_02_PitchHueBackground_ModuleTemplate_EMM/Emily_CMajor-2016_09_2-16bit-44.1K Tuned.wav";
	// Kanye:
	//String  inputFile  = "module_01_PitchHueBackground/module_01_02_PitchHueBackground_ModuleTemplate_EMM/Emily_CMajor-2016_09_2-16bit-44.1K Kanye.wav";

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
	

	public void settings()
	{
		size(925, 520);
	} // settings

	public void setup() 
	{
		hueMax         = 360;
		saturationMax  = 300;
		brightnessMax  = 100;

		//  colorMode(HSB, hueMax, saturationMax, brightnessMax);
		colors  = new float[][] {
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

		IFLookAndFeel	modTemplate	= new IFLookAndFeel(IFLookAndFeel.MOD_TEMPLATE);
//		controller.setLookAndFeel(modTemplate);
		
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
		this.stopButton.setPath(5, stopButtonVerts);
		this.showStop  = false;
		this.stopButton.setVisible(false);

		this.sidebarOut  = false;
		this.leftEdgeX   = 0;

		this.scrollbarX  = (width / 3) / 4;
		this.modulateScrollbarX	= this.scrollbarX + 10;

		/*
    this.playButtonX  = ((width / 3) / 4) * 1;
   this.arrowX       = ((width / 3) / 4) * 2 + 15;
   this.scaleX       = ((width / 3) / 4) * 3 + 20;
		 */
		playButtonX  = scrollbarX;
		arrowX       = scrollbarX + 80;
		scaleX       = scrollbarX + 140;

		hideWidth     = 75;

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
		int yValDif	= 25;
		for(int i = 1; i < textYVals.length; i++)
		{
			textYVals[i]	= textYVals[i - 1] + yValDif;
		} // for

		// set y vals for the note names:
		noteYVals[0]	= textYVals[textYVals.length - 1] + yValDif;
		for(int i = 1; i < noteYVals.length; i++)
		{
			noteYVals[i]	= noteYVals[i - 1] + yValDif;
		}

		// set y vals for the color modulate scrollbars:
		modulateYVals[0]	= noteYVals[noteYVals.length - 1] + yValDif;
		for(int i = 1; i < modulateYVals.length; i++)
		{
			modulateYVals[i]	= modulateYVals[i - 1] + yValDif;
		}

		this.rainbowX     = scrollbarX;
		this.dichromaticX  = scrollbarX + 49;
		this.trichromaticX  = scrollbarX + 112;
		this.customX  = scrollbarX + 175;

		this.buttons  = new IFButton[] {
				// annoyingly, buttons have to be placed15 pts higher than their corresponding text
				new IFButton("PlayButton", playButtonX, textYVals[0] - 15, hideWidth),
				new IFButton("Arrow", arrowX, textYVals[0] - 15, hideWidth - 20), 
				new IFButton("Scale", scaleX, textYVals[0] - 15, hideWidth - 35), 
				new IFButton("Rainbow", rainbowX, textYVals[7] - 15, 45), 	// 7: colorStyle
				new IFButton("Dichromatic", dichromaticX, textYVals[7] - 15, 60), 
				new IFButton("Trichromatic", trichromaticX, textYVals[7] - 15, 60), 
				new IFButton("Custom", customX, textYVals[7] - 15, 50), 
		}; // buttons

		buttonWrapper  = new Buttons(this, controller, modTemplate, this.buttons);

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
		// don't want scrollbars next to Hide, ColorStyle, or PitchColorCodes -- thus the weird "i" math:
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
		
		this.noteNameX1	= 20;
		this.noteNameX2 = (int)((width / 3) * 0.5) + this.noteNameX1;
		
		this.noteTextFieldX	= new int[] { this.noteNameX1 + 50, this.noteNameX2 + 50 };
		this.noteTextFieldWidth	= 75;
		
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
		
		this.modulateTextFieldX	= (int)this.modulateScrollbarArray[0].getSposMax() + 25;
		this.modulateTextFieldWidth	= 60;
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
		// draws the legend along the bottom of the screen:
		legend();

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
			} // if - leftEdgeX
		} // if - mousePressed
	} // draw()

	void displaySidebar()
	{
		// TODO: make buttons clickable only when sidebar is open?
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
				"Red Modulate", "Green Modulate", "Blue Modulate"
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

		/*
  thresholdScroll.update();  
  thresholdScroll.display();
		 */
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
		float  sideHeight  = width / colors.length;
		//  float  side = height / colors.length;

		stroke(255);

		for (int i = 0; i < colors.length; i++)
		{
			fill(colors[i][0], colors[i][1], colors[i][2]);
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

	public void mousePressed()
	{
		// The following for loops stops either the SamplePlayers or the uGens getting audio input (and otherwise creating feedback):
		for (int i = 0; i < input.getuGenArray().length; i++)
		{
			input.getuGenArray()[i].pause(true);
		} // for

		if (this.showPlay)
		{
			if (this.playButton.contains(mouseX, mouseY))
			{
				this.showPlay  = false;
				this.showStop  = true;

				this.input.uGenArrayFromSample(inputFile);
			} // if - playButton
		} else {
			if (this.stopButton.contains(mouseX, mouseY))
			{
				this.showStop  = false;
				this.showPlay  = true;

				this.input.uGenArrayFromNumInputs(1);
			} // if - stopButton
		} // else - showPlay/showStop

		this.playButton.setVisible(this.showPlay);
		this.stopButton.setVisible(this.showStop);
	} // mousePressed

	// TODO: make buttons clickable only when sidebar is open?
	void actionPerformed(GUIEvent e) {
		if (e.getMessage().equals("Completed")) {
			// This .setLabel prob. has value for getting values:
			label.setLabel(textField.getValue());
		}
	} // actionPerformed
} // class
