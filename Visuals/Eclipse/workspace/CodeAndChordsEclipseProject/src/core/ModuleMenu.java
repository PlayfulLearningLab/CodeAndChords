package core;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.Map;

import controlP5.Button;
import controlP5.CColor;
import controlP5.ColorWheel;
import controlP5.ControlEvent;
import controlP5.ControlFont;
import controlP5.ControlListener;
import controlP5.ControlP5;
import controlP5.Controller;
import controlP5.ScrollableList;
import controlP5.Slider;
import controlP5.Textfield;
import controlP5.Toggle;
import core.Archive_ModuleTemplate.ModuleTemplate01;
import processing.core.PApplet;
import processing.core.PImage;

/**
 * July 2017
 * 
 * Class for creating a sidebar Menu for a Module;
 * previously was ModuleTemplate.
 * 
 * @author Emily Meuer
 *
 */
public class ModuleMenu extends MenuTemplate  {

	public int trichromCounts	= 0;
	
	// TODO - for testing trichromatic bug:
//	public int[][] trichromColors;

	/**
	 * These lists of notes allow the position of any given note to be found in the current scale.
	 */
	protected	final String[]	notesAtoAbFlats	= new String[] { 
			"A", "Bb", "B", "C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab"
	};

	protected final String[]	notesAtoGSharps	= new String[] { 
			"A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#"
	};

	// ALL notes here
	protected	final String[]	allNotes	= new String[] {
			"A", "A#", "Bb", "B", "C", "C#", "Db", "D", "D#", "Eb", "E", "F", "F#", "Gb", "G", "G#", "Ab"
	}; // allNotes

	// Positions in filenames String[] here
	private	final int[]	enharmonicPos	= new int[] {
			0, 1, 1, 2, 3, 4, 4, 5, 6, 6, 7, 8, 9, 9, 10, 11, 11
	}; // enharmonicPos

	/**
	 * Colors roughly ROYGBIV; used for the rainbow() method.
	 */
	private int[][][] rainbowColors	= new int[][][] { 
		new int[][] {
			{ 255, 0, 0 }, 
			{ 255, 0, 0 },
			{ 255, 127, 0 }, 
			{ 255, 255, 0 }, 
			{ 255, 255, 0 }, 
			{ 127, 255, 0 },
			{ 0, 255, 255 },
			{ 0, 255, 255 },
			{ 0, 255, 255 },  
			{ 0, 0, 255 },
			{ 127, 0, 255 },
			{ 127, 0, 255 }
		}, // major
		new int[][] {
			{ 255, 0, 0 }, 
			{ 255, 0, 0 },
			{ 255, 127, 0 }, 
			{ 255, 255, 0 }, 
			{ 255, 255, 0 }, 
			{ 127, 255, 0 },
			{ 0, 255, 255 },
			{ 0, 255, 255 },
			{ 0, 0, 255 },
			{ 0, 0, 255 },
			{ 127, 0, 255 },
			{ 127, 0, 255 }
		}, // minor
		new int[][] {
			{ 255, 0, 0 }, 
			{ 255, 127, 0 }, 
			{ 255, 255, 0 }, 
			{ 127, 255, 0 }, 
			{ 0, 255, 0 }, 
			{ 0, 255, 127 }, 
			{ 0, 255, 255 }, 
			{ 0, 127, 255 }, 
			{ 0, 0, 255 }, 
			{ 127, 0, 255 }, 
			{ 255, 0, 255 }, 
			{ 255, 0, 127 }
		} // chromatic
	}; // rainbowColors

	/**
	 * Each int signifies the position in dichromColors/trichromColors/rainbowColors that is used to fill
	 * this.colors at the corresponding position in scaleDegreeColors[this.majMinChrom]:
	 */
	protected	final	int[][]	scaleDegreeColors	= new int[][] {
		// major:
		new int[] { 0, 0, 1, 2, 2, 3, 4, 4, 4, 5, 6, 6 },
		// minor:
		new int[] { 0, 0, 1, 2, 2, 3, 4, 4, 5, 5, 6, 6 },
		// chromatic:
		new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 }
	}; // scaleDegreeColors

	protected	PApplet	parent;

	/**	Module to which this Menu belongs	*/
	protected	Module	module;

	/**	Name (typically name of the Module) to be displayed at the top of the Menu	*/
	private		String		sidebarTitle;

	//	private     float		menuWidth;
	//	private     boolean  	menuIsOpen = false;

	/**	This is the parent.millis() when menuX is called, so that we don't call Hamburger when menuX is clicked	*/
	private		int			lastMenuXMillis;

	/**
	 * Color Styles: Rainbow, Dichromatic (fade from one color to another throughout the legend),
	 * Trichromatic (fade from color1 to  color2 and then from color2 to color3 throughout the legend),
	 * or Custom, which may be discontinued.
	 */
	public	static	int	CS_RAINBOW	= 1;
	public	static	int	CS_DICHROM	= 2;
	public	static	int	CS_TRICHROM	= 3;
	public	static	int	CS_CUSTOM	= 4;
	protected	int	curColorStyle;

	/**	ControlP5 for the play/stop, pause, and hamburger Buttons	*/
	protected	ControlP5	outsideButtonsCP5;

	/**
	 * booleans indicating whether or not the play/stop, pause, and hamburger Buttons
	 * should be visible; used when another Menu, such as ShapeEditor, is opened and then closed.
	 */
	protected	boolean		showPlayStop;
	protected	boolean		showPause;
	protected	boolean		showHamburger;

	/**	false before dichromatic has been called for the first time; true following that.	*/
	protected	boolean	dichromFlag;

	/**	false before trichromatic has been called for the first time; true following that.	*/
	protected	boolean	trichromFlag;

	/**	Holds the rgb values for all the colors, which will be updated by the ColorWheels of the current input num	*/
	protected	int[][][]	colors;

	/**	ColorWheels that hold all the colors for the Module; replaces int[][] this.colors	*/
	protected	ColorWheel[]	colorSelect;

	/**	Current hue (as opposed to the goal hue, which may not have been reached)	 */
	private	int[][]			curHue;

	/**	Hue that corresponds to the current sound, but to which curHue may not yet have faded	*/
	private	int[][]			goalHue;

	/**	The color when sound is below the threshold	*/
	protected	int[][]		canvasColor;

	/**	The amount that must be added every 50 or so milliseconds to fade to the goal color	*/
	private	int[][]			colorAdd;

	/**	The difference between the R, G, and B values of 2 colors that are being faded between	*/
	private	int[][]			colorRange;

	/**	The current colors which hsb sliders are altering.	*/
	protected int[][][]   hsbColors;

	/**	Flags whether the curHue R, G, and B values have come within an acceptable range of the goalHue	*/
	private boolean[][]	colorReachedArray;

	/**	True if all values in the colorReachedArray are true; used to determine fade speed (whether this is attack, release, or transition)	*/
	private	boolean[]		colorReached;

	/**	Input from which the class will get all its audio data	*/
	protected	Input	input;

	/**	Amplitude thresholds	*/
	protected	int[][]	thresholds;

	/**	The minimum value for threshold Sliders	*/
	protected	int	minThreshold;

	/**	The lowest threshold; sounds below this will be ignored	*/
	protected	int[]	pianoThreshold;

	/**	The highest amplitude threshold	*/
	protected	int[]	forteThreshold;

	/**	Holds the values of the saturation threshold and brightness threshold Sliders, respectively	*/
	protected	float[] satBrightThresholdVals;

	/**	Hodls the values of the saturation percent and brightness percent threshold Sliders, respectively	*/
	protected	float[]	satBrightPercentVals;

	/**	Flag denoting whether or not the current volume is below the threshold	*/
	private boolean[]	nowBelow;

	/**	Attack, Release, or Transition - 0 = attack, 1 = release, 2 = transition	*/
	private	int[]		attRelTranPos;

	/**	For a timer that allows attack/release/transition sliders to be time-based	*/
	private int[]	checkpoint;

	/**	Stores the values of the attack, release, and transition sliders	*/
	private	float[][]	attRelTranVals;

	/**	Stores the values of the red, green, and blue modulate sliders	*/
	protected	float[]	redGreenBlueMod;

	/**	Stores the values of the hue, saturation, and brightness modulate sliders	*/
	protected	float[] hueSatBrightnessMod;

	/**	
	 * Stores the values the hue/saturation/brightness percent sliders
	 * that affect and are dependent on thresholds;
	 * this variable will be included in applyHSBModulate, but child class must fill it.
	 */
	protected	float[]	hueSatBrightPercentMod;

	/**	Melody object that guide tones will use to play scales	*/
	protected Melody		melody;

	/**	Letter (and '#' or 'b', if necessary) designating the current key	*/
	protected String		curKey;

	/**	The current key is this many keys away from "A" (the first in allNotes)	*/
	protected int 		curKeyOffset;

	/**	The current key is this many letter names away from "A," with enharmonic keys (e.g., C# and Db) together counting for one position	*/
	protected int 		curKeyEnharmonicOffset;

	/**	Length of the current scale (chromatic is longer than major and minor)	*/
	protected	int			scaleLength;

	/**	Current beats per minute	*/
	protected	int			bpm;

	/**	Current scale quality: 0 = major, 1 = minor, 2 = chromatic	*/
	protected int 		majMinChrom;

	/**	The current octave of the guide tones	*/
	protected int			rangeOctave;

	/**	Instrument object used to play the guide tone scales	*/
	protected Instrument	instrument;

	/**	Signifies whether or not the legend at the bottom of the screen is to be visible	*/
	protected boolean	showScale;


	/**	Size of the shape (if there is one) from 1-100, designating the diameter in relation to the sketch canvas
	 * (e.g., shapeSize of 50 means that the shape diameter will be 50% of the sketch size)	*/
	protected	float	shapeSize;


	//	private boolean shapeMenuIsOpen;

	/**
	 * The current number of range segements (i.e., sections into which the spectrum, be it of amplitude or frequency, is split).
	 * 
	 * This class's controlEvent() will set this.curRangeSegments, 
	 * but it is up to child classes to implement the variable how they see fit.
	 */
	protected	int		curRangeSegments;

	/**	The total number of range segments available to this instance	*/
	protected	int		totalRangeSegments;

	/**	Which position in colorSelect each specialColor controls.	*/
	protected	int[][]	specialColorsPos;

	/**	True when a ColorWheel Button is a colorSelect Button; false if specialColors	*/
	protected	boolean[]	fromColorSelect;

	/**	True when a ColorWheel Button is a specialColors Button; false if colorSelect	*/
	//	protected	boolean[]	fromSpecialColors;

	/**	The current number of input lines	*/
	protected	int	numInputs;

	/**	If true, adjustments to Controllers affect all inputs	*/
	protected	boolean	global;

	/**	The input line currently affected by Controllers; only applies if !global	*/
	protected	int		currentInput;

	/**	
	 * All of the following are id's that have the potential to be used in controlEvent;
	 * if a child class adds any of these components (e.g., a canvasColorSelect Button or RGB mod sliders),
	 * they should initiate the corresponding one of these variables to the id of either the Button or Slider in question.
	 */
	protected	int	canvasColorSelectId;//		= -1;
	protected	int	firstColorModSliderId;//	= -1;
	protected	int	firstColorSelectCWId;//	= -1;
	protected	int	firstSpecialColorsCWId;//	= -1;
	protected	int	lastColorSelectId;//		= -1;
	protected	int	firstARTSliderId;//		= -1;
	protected	int	firstHSBSliderId;//		= -1;
	protected	int	firstRGBSliderId;//		= -1;
	protected	int	bpmSliderId;//				= -1;
	protected	int	volumeSliderId;//			= -1;
	protected	int	shapeSizeSliderId;//		= -1;
	protected	int	firstRangeSegmentsId;//	= -1;
	protected	int	pianoThresholdSliderId;//	= -1;
	protected	int	forteThresholdSliderId;//	= -1;
	protected	int	firstSatBrightThreshSliderId;//	= -1;

	/**
	 * Constructor
	 * 
	 * @param parent	PApplet used to draw, etc.; will instantiate this.parent instance var
	 * @param input		Input for all audio input; will instantiate this.input
	 * @param sidebarTitle	String designating the title of the module to which this template corresponds
	 */
	public ModuleMenu(PApplet parent, Module module, Input input, String sidebarTitle, int totalNumColorItems)
	{
		super(parent, parent.width, parent.height);

		this.parent			= parent;
		this.module			= module;
		this.input			= input;
		this.sidebarTitle	= sidebarTitle;

		this.outsideButtonsCP5	= new ControlP5(this.parent);
		this.outsideButtonsCP5.addListener((ControlListener)this);

		this.showPlayStop	= true;
		this.showPause		= false;
		this.showHamburger	= true;

		System.out.println("this.parent.height = " + (this.parent.height));

		/*
		this.leftAlign	= (this.parent.width / 3) / 4;
		this.leftEdgeX	= 0;

		this.labelX			= 10;
		this.labelWidth		= 70;
		this.spacer			= 5;
		this.textfieldWidth	= 40;
		this.sliderWidth	= 170;
		this.sliderHeight	= 20;
		 */
		this.numInputs		= this.input.getAdjustedNumInputs();
		this.global			= true;
		this.currentInput	= 0;

		// TODO: this might run into problems when we adjust for 5-8:
		// ColorSelect will be filled in addColorSelect,
		// and, since global == true, this fill set this.colors, too.
		this.colorSelect		= new ColorWheel[totalNumColorItems];
		this.colors				= new int[this.numInputs][totalNumColorItems][3];
		this.hsbColors			= new int[this.numInputs][totalNumColorItems][3];
		this.curHue				= new int[this.numInputs][3];
		this.goalHue			= new int[this.numInputs][3];
		this.canvasColor		= new int[this.numInputs][3];
		for(int i = 0; i < this.colors.length; i++)
		{
			for(int j = 0; j < this.colors[i].length; j++)
			{
				this.colors[i][j]	= this.rainbowColors[2][j];
			}
			this.canvasColor[i]	= new int[] { 1, 0, 0 };	// If this is set to rgb(0, 0, 0), the CW gets stuck in grayscale
		}
		this.colorAdd			= new int[this.numInputs][3];
		this.colorRange			= new int[this.numInputs][3];

		this.colorReachedArray	= new boolean[this.numInputs][3];
		this.colorReached		= new boolean[this.numInputs];
		this.nowBelow			= new boolean[this.numInputs];
		this.fromColorSelect	= new boolean[this.numInputs];
		//		this.fromSpecialColors	= new boolean[this.numInputs];
		this.specialColorsPos	= new int[this.numInputs][3];
		for(int i = 0; i < this.colorReachedArray.length; i++)
		{
			this.colorReachedArray[i]	= new boolean[] { false, false, false };
			this.colorReached[i]		= false;
			this.nowBelow[i]			= false;

			this.fromColorSelect[i]		= true;
			//			this.fromSpecialColors[i]	= false;
			
			// Getting ready for trichromatic:
			this.specialColorsPos[i]	= new int[] { 0, 4, 8 };
		}


		this.dichromFlag	= false;
		this.trichromFlag	= false;

		this.attRelTranPos	= new int[this.numInputs];
		this.attRelTranVals	= new float[this.numInputs][3];
		this.checkpoint		= new int[this.numInputs];
		for(int i = 0; i < this.attRelTranPos.length; i++)
		{
			this.attRelTranPos[i]	= 0;	// 0 = attack, 1 = release, 2 = transition
			this.attRelTranVals[i]	= new float[] {		200, 200, 200	};	// attack, release, transition all begin at 200 millis
			this.checkpoint[i]		= this.parent.millis() + 100;
		}

		this.hueSatBrightnessMod        = new float[3];
		this.hueSatBrightPercentMod		= new float[3];

		this.satBrightThresholdVals	= new float[2];
		this.satBrightPercentVals	= new float[2];

		this.totalRangeSegments	= totalNumColorItems;
		this.curRangeSegments	= totalNumColorItems;

		this.thresholds		= new int[this.numInputs][totalNumColorItems];
		this.pianoThreshold	= new int[this.numInputs];
		this.forteThreshold	= new int[this.numInputs];
		for(int i = 0; i < this.thresholds.length; i++)
		{
			this.pianoThreshold[i]	= 10;
			this.forteThreshold[i]	= 500;
			this.resetThresholds(i);

			System.out.println("pianoThreshold[" + i + "] = " + this.pianoThreshold[i] +
					"; forteThreshold[" + i + "] = " + this.forteThreshold[i]);
		} // for - initialize Thresholds

		// set amplitude thresholds
		/*		this.thresholds	= new int[] {
				10,		// piano
				100,	// mezzo piano
				200,	// mezzo forte
				500	//forte
		}; // thresholds
		 */

		this.minThreshold	= 101;

		//		this.shapeMenuIsOpen	= false;

		this.melody			= new Melody(this.parent, this.input);
		this.instrument		= new Instrument(this.parent);
		this.bpm			= 120;
		this.rangeOctave	= 3;
		this.curKey			= "A";
		this.majMinChrom	= 2;	// chromatic

		this.controlP5.addGroup("sidebarGroup")
		.setBackgroundColor(this.parent.color(0))
		.setSize(this.sidebarWidth, this.parent.height + 1)
		.setPosition(0, 0)
		.setVisible(false);


		Color	transparentBlack	= new Color(0, 0, 0, 200);
		int		transBlackInt		= transparentBlack.getRGB();

		this.controlP5.addBackground("background")
		.setPosition(0, 0)
		.setSize(this.sidebarWidth, this.parent.height)
		.setBackgroundColor(transBlackInt)
		//.setGroup("sidebarGroup")
		.setVisible(false);

		// Add play button, hamburger and menu x:
		this.addOutsideButtons();

		// Add Menu and Title labels (after menuX, because it gets its x values from that):
		ControlFont	largerStandard	= new ControlFont(ControlP5.BitFontStandard58, 13);

		this.controlP5.addTextlabel("title")
		//.setGroup("sidebarGroup")
		.setPosition(this.leftAlign, 5)
		.setFont(largerStandard)
		//			.setFont(this.parent.createFont("Consolas", 12, true))	// This is so blurry....
		.setValue(this.sidebarTitle);

		float	menuXX		= this.controlP5.getController("menuX").getPosition()[0];
		float	menuWidth	= this.controlP5.getController("menuX").getWidth();

		this.controlP5.addTextlabel("menu")
		.setPosition(menuXX + menuWidth + 3, 10)
		.setHeight(15)
		//.setGroup("sidebarGroup")
		.setValue("Menu");

	} // constructor


	/**
	 * Adds the Buttons, ColorWheels and Textfields for selecting custom colors.
	 * 
	 * @param yVals	yValue(s) for the row or rows of Buttons
	 * @param buttonLabels	String[] of labels for the color select Buttons; 
	 * 						length of this array determines the number of colorSelect Buttons;
	 * 						if including canvas in this row, the first element of this array must be "Canvas"
	 */
	public void addColorSelect(int xVal, int[] yVals, String[] buttonLabels, String labelText, boolean canvas)
	{
		// error checking
		if(yVals == null)	{
			throw new IllegalArgumentException("ModuleTemplate.addColorSelect: int[] parameter is null.");
		}
		if(yVals.length == 0)	{
			throw new IllegalArgumentException("ModuleTemplate.addColorSelect: int[] parameter " + yVals + " is empty.");
		}
		if(buttonLabels == null)	{
			throw new IllegalArgumentException("ModuleTemplate.addColorSelect: String[] parameter is null.");
		}
		if(labelText == null)	{
			throw new IllegalArgumentException("ModuleTemplate.addColorSelect: String parameter (labelText) is null.");
		}
		// Warn the use that the first String in buttonLabels will be canvas color (but only if the didn't title it "canvas"):
		if(canvas && !buttonLabels[0].equalsIgnoreCase("canvas"))
		{
			System.err.println("ModuleTemplate.addColorSelect: colorSelect Button with label text '" + buttonLabels[0] + "' will control the canvas color.");
		}

		// Position in rainbowColors:
		int	buttonLabelPos	= 0;
		int	colorSelectPos	= 0;


		if(canvas)
		{
			this.canvasColorSelectId	= this.nextColorWheelId;
			this.firstColorSelectCWId	= this.nextColorWheelId + 1;

		} else {
			this.firstColorSelectCWId	= this.nextColorWheelId;
		}

		int		buttonsPerRow	= (buttonLabels.length) / yVals.length;
		// the "- (10 / buttonsPerRow)" adds [this.rightEdgeSpacer pixels] at the end of the row:
		int		buttonWidth		= ((this.sidebarWidth - this.leftAlign - this.rightEdgeSpacer) / buttonsPerRow) - this.spacer;

		System.out.println("ColorSelect: buttonWidth = " + buttonWidth);

		int[]	xVals	= new int[buttonsPerRow];
		for(int i = 0; i < xVals.length; i++)
		{
			xVals[i]	= xVal + this.leftAlign + ((buttonWidth + this.spacer) * i);
			System.out.println("    xVals[" + i + "] = " + xVals[i]);
		}

		this.controlP5.addTextlabel("colorSelectLabel")
		.setPosition(xVal + labelX, yVals[0] + 4)
		//.setGroup("sidebarGroup")
		.setValue(labelText);

		// Loop through all
		for(int i = 0; i < yVals.length; i++)
		{
			// Loop through each row
			for(int j = 0; j < xVals.length; j++)
			{
				if(i == 0 && j == 0 && canvas)
				{
					this.addColorWheelGroup(xVals[j], yVals[i], buttonWidth, buttonLabels[buttonLabelPos], this.canvasColor[0]);
				} else {
					System.out.println("colorSelectPos = " + colorSelectPos);
//					this.colors[colorSelectPos]	= this.rainbowColors[this.majMinChrom][colorSelectPos];
					this.colorSelect[colorSelectPos]	= (ColorWheel)(this.addColorWheelGroup(xVals[j], yVals[i], buttonWidth, buttonLabels[buttonLabelPos], this.colors[this.currentInput][colorSelectPos]))[1];
					colorSelectPos	= colorSelectPos + 1;
				}

				buttonLabelPos	= buttonLabelPos + 1;

			} // for - j
		} // for - i

		this.fillHSBColors();
	} // addColorSelect


	/**
	 * Adds "outside buttons": hamburger and play/pause/stop Buttons
	 */
	public void addOutsideButtons()
	{
		int	playX		= this.parent.width - 45;
		int	playY		= 15;
		int	playWidth	= 30;
		int	playHeight	= 30;

		// add play button:
		PImage[]	images	= { 
				this.parent.loadImage("playButton.png"),
				this.parent.loadImage("stopButton.png")
		};
		PImage	pauseImage	= this.parent.loadImage("pauseButton.png");

		images[0].resize(playWidth - 5, playHeight);
		images[1].resize(playWidth, playHeight);
		pauseImage.resize(playWidth, playHeight);

		this.outsideButtonsCP5.addToggle("play")
		.setPosition(playX, playY)
		.setImages(images)
		.updateSize();

		this.outsideButtonsCP5.addToggle("pause")
		.setPosition((playX - playWidth - 10), playY)
		.setImages(pauseImage, images[0])
		.updateSize()
		.setVisible(false);

		int	hamburgerX		= 10;
		int	hamburgerY		= 13;
		int	hamburgerWidth	= 30;
		int	hamburgerHeight	= 30;

		PImage	hamburger	= this.parent.loadImage("hamburger.png");
		hamburger.resize(hamburgerWidth, hamburgerHeight);
		this.outsideButtonsCP5.addButton("hamburger")
		.setPosition(hamburgerX, hamburgerY)
		.setImage(hamburger)
		.setClickable(true)
		.updateSize();

		int	menuXX			= 5;
		int	menuXY			= 5;
		int	menuXWidth		= 15;

		PImage	menuX	= this.parent.loadImage("menuX.png");
		menuX.resize(menuXWidth, 0);
		this.controlP5.addButton("menuX")
		.setPosition(menuXX, menuXY)
		.setImage(menuX)
		//.setGroup("sidebarGroup")
		.updateSize()
		.bringToFront();

		//		this.menuWidth = this.controlP5.getController("menuX").getWidth();
	} // addOutsideButtons


	/**
	 * Adds "hide buttons" - hide Menu, hide Play Button, and hide Legend
	 * 
	 * @param hideY	y value at which the row will be displayed
	 */
	public void addHideButtons(int xVal, int	hideY)
	{
		int	hideWidth   = ( ( this.sidebarWidth - this.leftAlign - this.rightEdgeSpacer) / 3 ) - this.spacer;
		int hideSpace	= 4;

		//		int	labelX		= 10;
		int	playButtonX	= xVal + this.leftAlign;
		int	menuButtonX	= xVal + this.leftAlign + hideWidth + this.spacer;
		int	scaleX		= xVal + this.leftAlign + (+ hideWidth + this.spacer) * 2;

		String[]	names	= new String[] { 
				"playButton",
				"menuButton",
				"legend"
		};
		String[]	labels	= new String[] {
				"Play Button",
				"Menu Button",
				"Legend"
		};
		int[]	xVals	= new int[] {
				playButtonX,
				menuButtonX,
				scaleX
		};

		this.controlP5.addTextlabel("hide")
		.setPosition(this.labelX, hideY + 4)
		//.setGroup("sidebarGroup")
		.setValue("Hide");

		for(int i = 0; i < names.length; i++)
		{
			this.controlP5.addToggle(names[i])
			.setPosition(xVals[i], hideY)
			.setWidth(hideWidth);
			//.setGroup("sidebarGroup");
			this.controlP5.getController(names[i]).getCaptionLabel().set(labels[i]).align(ControlP5.CENTER, ControlP5.CENTER);
		}

		this.showScale = true;
	} // addHideButtons


	/**
	 * Adds the attack, release, and transition Sliders.
	 * 
	 * @param xVal		x value for the leftmost edge of the whole group
	 * @param attackY	y value for attack Slider
	 * @param releaseY	y value for release Slider
	 * @param transitionY	y value for transition Slider
	 */
	public void addARTSliders(int xVal, int attackY, int releaseY, int transitionY)
	{
		String[]	labels	= new String[] {
				"Attack",
				"Release",
				"Transition"
		}; // labels

		int[]		yVals	= new int[] {
				attackY,
				releaseY,
				transitionY
		}; // yVals

		this.firstARTSliderId	= this.nextSliderId;

		for(int i = 0; i < labels.length; i++)
		{
			this.addSliderGroup(xVal, yVals[i], labels[i], 100, 3000, 200);
		}
	} // addARTSliders

	/**
	 * Adds the piano (minimum) threshold Slider.
	 * 
	 * @param xVal	x value for leftmost edge of whole group
	 * @param yVal	y value of Slider
	 */
	public void addPianoThresholdSlider(int xVal, int yVal)
	{
		this.pianoThresholdSliderId	= this.nextSliderId;
		this.addSliderGroup(yVal, "Piano \nThreshold", 2, 100, 10);
	} // addPianoThresholdSlider

	/**
	 * Adds the forte (maximum) threshold Slider.
	 * 
	 * @param xVal	x value for leftmost edge of whole group
	 * @param yVal	y value of Slider
	 */
	public void addForteThresholdSlider(int xVal, int yVal)
	{
		this.forteThresholdSliderId	= this.nextSliderId;
		this.addSliderGroup(xVal, yVal, "Forte\nThreshold", this.minThreshold, 7000, this.forteThreshold[0]);
	} // addForteThresholdSlider


	/**
	 * Method called during instantiation to initialize the key selector drop-down menu (ScrollableList)
	 * and major/minor/chromatic selection buttons.
	 * 
	 * @param keyY	y value of the menu and buttons.
	 */
	public void addKeySelector(int xVal, int keyY)
	{

		//		int	labelX			= 10;

		int	listWidth		= 25;
		//		int	spacer			= 5;

		int	buttonWidth		= 50;
		int	toggleWidth		= ((this.sidebarWidth - this.leftAlign - listWidth - (this.spacer * 2) - this.rightEdgeSpacer - buttonWidth) / 3 ) - this.spacer;
		int	majorX			= xVal + this.leftAlign + listWidth + spacer;
		int	minorX			= xVal + this.leftAlign + listWidth + spacer + (toggleWidth + spacer);
		int	chromX			= xVal + this.leftAlign + listWidth + spacer + ((toggleWidth + spacer) * 2);
		int	guideToneX		= xVal + this.leftAlign + listWidth + spacer + ((toggleWidth + spacer) * 3);


		// "Key" Textlabel
		this.controlP5.addTextlabel("key")
		.setPosition(xVal + this.labelX, keyY + 4)
		//.setGroup("sidebarGroup")
		.setValue("Key");


		// "Letter" drop-down menu (better name?)
		this.controlP5.addScrollableList("keyDropdown")
		.setPosition(xVal + this.leftAlign, keyY)
		.setWidth(listWidth)
		.setBarHeight(18)
		.setItems(this.allNotes)
		.setOpen(false)
		//		.setLabel("Select a key:")
		.setValue(0f)
		//.setGroup("sidebarGroup")
		.getCaptionLabel().toUpperCase(false);

		// Maj/Min/Chrom Toggles
		// (These each have an internalValue - 0 = Major, 1 = Minor, and 2 = Chromatic - 
		//  and will set this.majMinChrom to their value when clicked.)
		this.controlP5.addToggle("major")
		.setPosition(majorX, keyY)
		.setWidth(toggleWidth)
		.setCaptionLabel("Major")
		//.setGroup("sidebarGroup")
		.setInternalValue(0);
		this.controlP5.getController("major").getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);

		this.controlP5.addToggle("minor")
		.setPosition(minorX, keyY)
		.setWidth(toggleWidth)
		.setCaptionLabel("Minor")
		//.setGroup("sidebarGroup")
		.setInternalValue(1);
		this.controlP5.getController("minor").getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);

		this.controlP5.addToggle("chrom")
		.setPosition(chromX, keyY)
		.setWidth(toggleWidth)
		.setCaptionLabel("Chrom.")
		//.setGroup("sidebarGroup")
		.setInternalValue(2);
		this.controlP5.getController("chrom").getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);

		((Toggle)(this.controlP5.getController("chrom"))).setState(true);

		// Guide Tone pop-out Button:
		this.controlP5.addToggle("guideToneButton")
		.setPosition(guideToneX, keyY)
		.setWidth(buttonWidth)
		//		.setWidth(toggleWidth)
		.setCaptionLabel("Guide Tones")
		.setValue(false);
		//.setGroup("sidebarGroup");
		this.controlP5.getController("guideToneButton").getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);

	} // addKeySelector


	/**
	 * Adds the guide tone pop-out with range and envelope preset select dropdowns, bpm and volume sliders.
	 * 
	 * @param guideToneY	y value for the top of the pop-out; 
	 * 						for ModuleTemplate_01, this is same as the value for addKeySelector
	 */
	public void addGuideTonePopout(int xVal, int guideToneY)
	{

		Color	transparentBlack	= new Color(0, 0, 0, 240);
		int		transBlackInt		= transparentBlack.getRGB();

		int		boxWidth		= 220;
		int		boxHeight		= 130;

		int		labelWidth		= 30;
		int		listWidth		= 155;
		int		sliderWidth		= 120;
		int		textfieldWidth	= 30;
		int		popoutSpacer	= 12;

		int		height			= 18;

		int		listSliderX		= xVal + (popoutSpacer * 2) + labelWidth;
		//		int		textfieldX		= boxWidth - popoutSpacer - textfieldWidth;

		int		rangeY			= popoutSpacer;
		int		adsrY			= (popoutSpacer * 2) + height;
		int		bpmY			= (popoutSpacer * 3) + (height * 2);
		int		volumeY			= (popoutSpacer * 4) + (height * 3);

		this.controlP5.addGroup("guideToneBackground")
		.setPosition(xVal + this.leftAlign, guideToneY + 20)
		.setSize(boxWidth, boxHeight)
		.setBackgroundColor(transBlackInt)
		.setVisible(false)
		.hideBar();

		String[]	labels		= new String[]	{ "bpmLabel", 	"volumeLabel" };
		int[]		yVals		= new int[]		{ bpmY, 		volumeY	};
		String[]	labelVals	= new String[]	{ "BPM",	"Volume"	};
		float[][]	ranges		= new float[][]	{
			// bpm range:
			new float[] { 40, 	240 },
			// volume range:
			new float[] { 0.01f,	5	}
		};
		float[]		startingVals	= new float[]	{ 120, 1	};

		this.bpmSliderId	= this.nextSliderId;
		this.volumeSliderId	= this.nextSliderId + 1;

		for(int i = 0; i < labels.length; i++)
		{
			this.addSliderGroup(xVal, yVals[i], labelVals[i], listSliderX, sliderWidth, ranges[i][0], ranges[i][1], startingVals[i], textfieldWidth, "guideToneBackground");
		} // for

		// "ADSR Presets" Textlabel
		this.controlP5.addTextlabel("adsrPresets")
		.setPosition(xVal + popoutSpacer, adsrY)
		.setGroup("guideToneBackground")
		.setValue("ADSR \nPresets");

		// ArrayList of ADSR options:
		String[]	adsrItems	= new String[] {
				"Even",
				"Long Attack",
				"Long Decay",
				"High Sustain",
				"Low Sustain",
				"Long Release"
		};

		this.controlP5.addScrollableList("adsrPresetsDropdown")
		.setPosition(listSliderX, adsrY)
		.setWidth(listWidth)
		.setBarHeight(18)
		.setItemHeight(18)
		.setItems(adsrItems)
		.setValue(0f)		// sets it to the first item
		.setOpen(false)
		.setGroup("guideToneBackground")
		.bringToFront()
		.getCaptionLabel().toUpperCase(false);

		// "Range" Textlabel
		this.controlP5.addTextlabel("range")
		.setPosition(xVal + popoutSpacer, rangeY + 4)
		.setGroup("guideToneBackground")
		.setValue("Range");

		// Update Melody's scale:
		String[] scales	= new String[] { "major", "minor", "chromatic" };
		this.melody.setScale(scales[this.majMinChrom]);
		this.melody.setRangeList();

		this.controlP5.addScrollableList("rangeDropdown")
		.setPosition(listSliderX, rangeY)
		.setWidth(listWidth)
		.setBarHeight(18)
		.setItemHeight(18)
		.setItems(this.melody.getRangeList())
		.setValue(0f)
		.setOpen(false)
		.setGroup("guideToneBackground")
		.bringToFront()
		.getCaptionLabel().toUpperCase(false);

	} // addGuideTonePopout


	/**
	 * Adds the hue, saturation, and brightness modulate sliders
	 * 
	 * @param xVal 	x value for the leftmost edge of this group
	 * @param hsb	array of y values for each slider
	 */
	public void addHSBSliders(int xVal, int[] hsb)
	{
		String[]	values	= new String[] { "Hue", "Saturation", "Brightness" };

		this.firstHSBSliderId	= this.nextSliderId;

		for(int i = 0; i < hsb.length; i++)
		{
			this.addSliderGroup(xVal, hsb[i], values[i], -1, 1, 0);
		} // for   
	} // addHSBSliders


	/**
	 * Method called during instantiation, to initialize the RGB color modulate sliders.
	 * 
	 * @param xVal 	x value for the leftmost edge of this group
	 * @param modulateYVals	int[] of the y values of the red, green, and blue sliders, respectively.
	 */
	public void addModulateSliders(int xVal, int[] modulateYVals)
	{
		this.redGreenBlueMod		 	= new float[3];

		String[]	values	= new String[] { "Red Modulate", "Green Mod.", "Blue Modulate" };

		this.firstRGBSliderId	= this.nextSliderId;
		for(int i = 0; i < modulateYVals.length; i++)
		{
			this.addSliderGroup(xVal, modulateYVals[i], values[i], -255, 255, 0);
		} // for
	} // addModulateSliders


	/**
	 * Adds the slider for adjusting shape size
	 * 
	 * @param xVal 	x value for the leftmost edge of this group
	 * @param yVal	y value for this slider
	 */
	public void addShapeSizeSlider(int xVal, int yVal)
	{
		this.shapeSize			= 1;
		this.shapeSizeSliderId	= this.nextSliderId;

		this.addSliderGroup(xVal, yVal, "Shape Size", 0.01f, 10, this.shapeSize);
	} // addShapeSizeSlider


	/**
	 * Adds the Buttons for selecting the number of range segments.
	 * 
	 * @param xVal 	x value for the leftmost edge of this group
	 * @param yVal	y value for the row of Buttons
	 * @param numSegments	total number of segments (current number of segments will be set to this total)
	 * @param label	text to display on the label at the beginning of the row
	 */
	public void addRangeSegments(int xVal, int yVal, int numSegments, String label)
	{
		this.addRangeSegments(xVal, yVal, numSegments, numSegments, label);
	}  // addRangeSegments(int, int, String)


	/**
	 * Adds the Buttons for selecting the number of range segments.
	 * 
	 * @param yVal	y value for the row of Buttons
	 * @param numSegments	total number of range segments
	 * @param defaultNumSegments	number of segments that are set as current at the beginning
	 * @param label	text to display on the label at the beginning of the row
	 */
	public void addRangeSegments(int xVal, int yVal, int numSegments, int defaultNumSegments, String label)
	{
		if(defaultNumSegments > numSegments) {
			throw new IllegalArgumentException("ModuleTemplate.addRangeSegments: defaultNumSegments " + defaultNumSegments + " is greater than total segments, " + numSegments);
		}
		/*
		this.totalRangeSegments	= numSegments;
		System.out.println("just set totalRangeSegments to " + this.totalRangeSegments);
		this.curRangeSegments	= defaultNumSegments;
		 */
		float	toggleSpace	= (this.sidebarWidth) - this.leftAlign - this.rightEdgeSpacer /*(this.labelX) - this.labelWidth - this.spacer */;
		int		toggleWidth	= (int)(toggleSpace / numSegments) - this.spacer;

		int[]	xVals	= new int[numSegments];
		for(int i = 0; i < xVals.length; i++)
		{
			xVals[i]	= xVal + (this.leftAlign + (i * (toggleWidth + spacer)));
		}

		this.controlP5.addLabel("rangeSegments")
		.setPosition(xVal + this.labelX, yVal)
		.setValue(label);
		//.setGroup("sidebarGroup");

		this.firstRangeSegmentsId	= this.nextToggleId;
		System.out.println("firstRangeSegmentsId = " + this.firstRangeSegmentsId);

		for(int i = 0; i < numSegments; i++)
		{
			this.controlP5.addToggle("toggle" + this.nextToggleId)
			.setPosition(xVals[i], yVal)
			.setWidth(toggleWidth)
			.setLabel((i + 1) + "")
			//.setGroup("sidebarGroup")
			.setId(this.nextToggleId)
			.setInternalValue(i + 1);
			this.controlP5.getController("toggle" + this.nextToggleId).getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);


			if(i == (this.curRangeSegments - 1))
			{
				((Toggle) this.controlP5.getController("toggle" + this.nextToggleId)).setState(true);
			}

			this.nextToggleId	= this.nextToggleId + 1;
		} // for - adding Toggles
	} // addRangeSegments


	/**
	 * Adds the "Color: Forte Threshold", "Saturation", "Saturation: Forte Threshold", 
	 * "Brightness", and "Brightness: Forte Threshold" group of Sliders/Textfields
	 * 
	 * @param xVal 	x value for the leftmost edge of this group
	 * @param yVal	y value of forte threshold
	 * @param verticalSpacer	vertical space between sliders
	 */
	public void addThresholdSliders(int xVal, int yVal, int verticalSpacer)
	{

		// Since some i's will add a couple rows of labels and sliders,
		// this variable keeps track of which "level" of y the next thing should be added to.

		String[]	names	= new String[] {
				"saturation",
				"saturationForteThresh",
				"brightness",
				"brightnessForteThresh"
		}; // names

		String[]	labels = new String[] {
				"Saturation",
				"Sat: Forte\nThreshold",
				"Brightness",
				"Bright: Forte\nThreshold"
		}; // labels

		this.firstSatBrightThreshSliderId	= this.nextSliderId;
		System.out.println("firstSatBrightThreshSliderId = " + firstSatBrightThreshSliderId);

		for(int i = 0; i < names.length; i++)
		{
			// Forte Thresholds
			if(i % 2 == 1)
			{
				this.addSliderGroup(xVal, yVal + (i * (verticalSpacer + this.sliderHeight)), labels[i], this.minThreshold, 7000, this.forteThreshold[0]);

			} // if - Forte Thresholds

			// Percent Sliders
			if(i % 2 == 0)
			{
				this.controlP5.addLabel(names[i])
				.setPosition(xVal + this.labelX, yVal + (i * (verticalSpacer + this.sliderHeight)) + 4)
				.setValue(labels[i]);
				//.setGroup("sidebarGroup");

				this.controlP5.addSlider("slider" + this.nextSliderId)
				.setPosition(xVal + this.leftAlign, (yVal + (i * (verticalSpacer + this.sliderHeight))))
				.setSize(this.sliderWidth + this.spacer + this.textfieldWidth, this.sliderHeight)
				.setRange(-1, 1)
				.setValue(0)
				//.setGroup("sidebarGroup")
				.setId(this.nextSliderId)
				.getCaptionLabel().setVisible(false);

				this.nextSliderId	= this.nextSliderId + 1;
				// Also need to increment nextSTextfieldId so that they don't get out of sync
				// (since this slider had no connected Textfield).
				this.nextSTextfieldId	= this.nextSTextfieldId + 1;
			} // if - percent sliders
		} // for

	} // addThresholdSliders


	/**
	 * Adds the Button/ColorWheel/Textfield groups for colors that will have a special function,
	 * e.g., "Tonic", "2nd Color", and "3rd Color" in Module_01.
	 * 
	 * @param xVal 	x value for the leftmost edge of this group
	 * @param yVal	the y value for the row
	 * @param buttonLabels	String[] of text for the buttons; if canvas color in this row of Buttons, first item should be "Canvas"
	 * @param labelText	text of the Label on the far left of the row
	 * @param canvas	boolean indicating whether or not the Canvas color Button is included in this row
	 */
	public void addSpecialColors(int xVal, int yVal, String[] buttonLabels, String labelText, boolean canvas)
	{
		// error checking
		if(buttonLabels == null)	{
			throw new IllegalArgumentException("ModuleTemplate.addSpecialColors: String[] parameter is null.");
		}
		if(labelText == null)	{
			throw new IllegalArgumentException("ModuleTemplate.addSpecialColors: String parameter (labelText) is null.");
		}
		// Warn the use that the first String in buttonLabels will be canvas color (but only if the didn't title it "canvas"):
		if(canvas && !buttonLabels[0].equalsIgnoreCase("canvas"))
		{
			System.err.println("ModuleTemplate.addSpecialColors: colorSelect Button with label text '" + buttonLabels[0] + "' will control the canvas color.");
		}

		if(canvas)
		{
			this.canvasColorSelectId	= this.nextColorWheelId;
			this.firstSpecialColorsCWId	= this.nextColorWheelId + 1;

		} else {
			this.firstSpecialColorsCWId	= this.nextColorWheelId;

		}
		// the "- (10 / buttonsPerRow)" adds a 10 pixel space at the end of the row:
		int		buttonWidth		= (((this.sidebarWidth) - this.leftAlign) / buttonLabels.length) - this.spacer - (10 / buttonLabels.length);
		int[]	xVals	= new int[buttonLabels.length];
		for(int i = 0; i < xVals.length; i++)
		{
			xVals[i]	= xVal + this.leftAlign + ((buttonWidth + this.spacer) * i);
		}

		this.controlP5.addTextlabel("specialColorsLabel")
		.setPosition(xVal + this.labelX, yVal + 4)
		//.setGroup("sidebarGroup")
		.setValue(labelText);

		// Loop through all
		for(int i = 0; i < xVals.length; i++)
		{
			if(canvas && i == 0)
			{
				this.addColorWheelGroup(xVals[i], yVal, buttonWidth, buttonLabels[i], this.canvasColor[0]);
			} else {
				int	thisColorPos	= this.specialColorsPos[0][i - 1];
				this.addColorWheelGroup(xVals[i], yVal, buttonWidth, buttonLabels[i], new Color(this.colorSelect[thisColorPos].getRGB()));
			}
		} // for - i

		this.fillHSBColors();
	} // addSpecialColors

	/**
	 * TODO - needs commenting or can go.
	 * 
	 * @param yVal
	 */
	public void addShapeCustomizationControls(int yVal)
	{
		this.controlP5.addButton("shapeMenuButton")
		.setPosition(this.leftAlign, yVal)
		.setHeight(this.sliderHeight)
		.setWidth(this.sidebarWidth - this.leftAlign - this.rightEdgeSpacer)
		.setLabel("Shape Menu");
		//.setGroup("sidebarGroup");

		this.controlP5.addGroup("shapeMenuGroup");


		int fb = new Color((int)0,(int)0,(int)0, (int)250).getRGB();
		CColor fadedBackground = new CColor();
		//fadedBackground.setAlpha();

		this.controlP5.addSlider("a", .01f, 3, 1, 15, 120, 150, 28)
		.setGroup("shapeMenuGroup")
		.getCaptionLabel()
		.hide();

		this.controlP5.addSlider("b", .01f, 3, 1, 15, 170, 150, 28)
		.setGroup("shapeMenuGroup")
		.getCaptionLabel()
		.hide();

		((Slider) this.controlP5.addSlider("m1", 0, 15, 1, 15, 220, 150, 28))
		.setGroup("shapeMenuGroup")
		.getCaptionLabel()
		.hide();

		((Slider) this.controlP5.addSlider("m2", 0, 15, 1, 15, 270, 150, 28))
		.setGroup("shapeMenuGroup")
		.getCaptionLabel()
		.hide();

		this.controlP5.addSlider("n1", 0, 10, 1, 15, 320, 150, 28)
		.setGroup("shapeMenuGroup")
		.getCaptionLabel()
		.hide();

		this.controlP5.addSlider("n2", 0, 10, 1, 15, 370, 150, 28)
		.setGroup("shapeMenuGroup")
		.getCaptionLabel()
		.hide();

		this.controlP5.addSlider("n3", 0, 10, 1, 15, 420, 150, 28)
		.setGroup("shapeMenuGroup")
		.getCaptionLabel()
		.hide();

		this.controlP5.addScrollableList("shapeSelect")
		.setPosition(15,70)
		.setSize(150, 100)
		.setBarHeight(30)
		.setGroup("shapeMenuGroup")
		.addItems(new String[] {"shape1", "shape2", "shape3", "shape4", "shape5"})
		.close()
		//		.setValue(this.module2.getShape().getShapeIndex());
		.setValue(0f);



		this.controlP5.getGroup("shapeMenuGroup")
		.setVisible(false);

	} // addShapeCustomizationControls


	/**
	 * Method called during instantiation to initialize the color style Toggles
	 * (Rainbow, Dichromatic, Trichromatic, and Custom).
	 * 
	 * @param xVal 	x value for the leftmost edge of this group
	 * @param colorStyleY	y value of the colorStyle Toggles
	 */
	public void addColorStyleButtons(int xVal, int colorStyleY)
	{
		//		int		buttonWidth		= ((this.sidebarWidth - this.leftAlign - this.rightEdgeSpacer) / buttonsPerRow) - this.spacer;
		int	colorStyleWidth	= ((this.sidebarWidth - this.leftAlign - this.rightEdgeSpacer) / 4) - this.spacer;
		//		int	colorStyleSpace	= 6;

		System.out.println("ColorStyle: colorStyleWidth = " + colorStyleWidth);

		int rainbowX     	= xVal + this.leftAlign;
		int dichromaticX	= xVal + this.leftAlign + colorStyleWidth + this.spacer;
		int trichromaticX	= xVal + this.leftAlign + (colorStyleWidth + this.spacer) * 2;
		int customX			= xVal + this.leftAlign + (colorStyleWidth + this.spacer) * 3;

		System.out.println("    rainbowX = " + rainbowX + "\n    dichromaticX = " + dichromaticX
				+ "\n    trichromaticX = " + trichromaticX + "\n    customX = " + customX);

		this.controlP5.addTextlabel("colorStyle")
		.setPosition(xVal + this.labelX, colorStyleY + 4)
		//.setGroup("sidebarGroup")
		.setValue("Color Style");

		this.controlP5.addToggle("rainbow")
		.setPosition(rainbowX, colorStyleY)
		.setWidth(colorStyleWidth)
		.setCaptionLabel("Rainbow")
		//.setGroup("sidebarGroup")
		.setInternalValue(ModuleTemplate01.CS_RAINBOW);
		this.controlP5.getController("rainbow").getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);

		this.controlP5.addToggle("dichrom")
		.setPosition(dichromaticX, colorStyleY)
		.setWidth(colorStyleWidth)
		.setCaptionLabel("Dichrom.")
		//.setGroup("sidebarGroup")
		.setInternalValue(ModuleTemplate01.CS_DICHROM);
		this.controlP5.getController("dichrom").getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);

		this.controlP5.addToggle("trichrom")
		.setPosition(trichromaticX, colorStyleY)
		.setWidth(colorStyleWidth)
		.setCaptionLabel("Trichrom.")
		//.setGroup("sidebarGroup")
		.setInternalValue(ModuleTemplate01.CS_TRICHROM);
		this.controlP5.getController("trichrom").getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);

		this.controlP5.addToggle("custom")
		.setPosition(customX, colorStyleY)
		.setWidth(colorStyleWidth)
		.setCaptionLabel("Custom")
		//.setGroup("sidebarGroup")
		.setInternalValue(ModuleTemplate01.CS_CUSTOM);
		this.controlP5.getController("custom").getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);

		this.setColorStyle(ModuleMenu.CS_RAINBOW);
	} // addColorStyleButtons


	public void addInputSelect(int xVal, int yVal)
	{
		int	toggleWidth	= 60;
		int	listWidth	= this.sidebarWidth - this.leftAlign - toggleWidth - this.spacer - this.rightEdgeSpacer;

		this.controlP5.addLabel("inputLabel")
		.setPosition(xVal + this.labelX, yVal + 4)
		.setStringValue("Input:");

		String[]	listItems	= new String[this.numInputs];
		for(int i = 0; i < listItems.length; i++)
		{
			listItems[i]	= (i + 1) + "";
		} // for - fill listItems

		// Global controls Button:
		this.controlP5.addToggle("global")
		.setPosition(this.leftAlign, yVal)
		.setWidth(toggleWidth)
		.setCaptionLabel("Global")
		.setState(true)
		.plugTo(this)
		.getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);

		// Input num selector:
		this.controlP5.addScrollableList("inputSelectDropdown")
		.setPosition(xVal + this.leftAlign + toggleWidth + this.spacer, yVal)
		.setWidth(listWidth)
		.setBarHeight(18)
		.setItems(listItems)
		.setOpen(false)
		.setCaptionLabel("Select an input")
		//		.setValue(0f)
		.getCaptionLabel().toUpperCase(false);
	} // addInputSelect

	/**
	 * Sets this.goalHue to the value of the given position in this.colors
	 * (important that it sets this in colors and not legendColors, since colors
	 * has the correct saturation and brightness for this particular volume)
	 * 
	 * @param position	either a position in colors or -1 for canvas color
	 */
	public void setGoalHue(int position)
	{
		if(position > this.colorSelect.length || position < -1) {
			throw new IllegalArgumentException("ModuleTemplate.getGoalHue: position " + position + 
					" is out of bounds; must be between -1 (signifying canvas color) or " + (this.colorSelect.length - 1));
		} // error checking

		// Canvas color:
		if(position == -1)	
		{
			// If global, update all goalHues and canvas Colors:
			if(global)
			{
				for(int i = 0; i < this.goalHue.length; i++)
				{
					for(int j = 0; j < this.goalHue[i].length; j++)
					{
						this.goalHue[i][j]	= this.canvasColor[i][j];
					} // for - canvas
				} // for - i
			} else {
				for(int i = 0; i < this.goalHue.length; i++)
				{
					this.goalHue[this.currentInput][i]	= this.canvasColor[this.currentInput][i];
				} // for - i
			} // else - not global
		} else {
			// Not canvas color:

			// If global, update this position for all colors:
			if(global)
			{
				for(int i = 0; i < this.goalHue.length; i++)
				{
					for(int j = 0; j < this.goalHue[i].length; j++)
					{
						this.goalHue[i]	= this.getColor(i);
					} // for - canvas
				} // for - i
			} else {
				// ... but if not, just do it for the current one:
				for(int i = 0; i < this.goalHue.length; i++)
				{
					this.goalHue[this.currentInput]	= this.getColor(position);
				} // for - i
			} // else - not global
		} // else - not canvas color

	} // setGoalHue


	/**
	 * Takes the values of curHue from its current values to the values in goalHue
	 * over the time that is designated by the attack, release, and transition sliders
	 * 
	 * @param position	position in colors to which curHue should fade
	 */
	public void fade(int position, int inputNum)
	{
		this.inputNumErrorCheck(inputNum);

		if(position > this.colorSelect.length || position < -1) {
			throw new IllegalArgumentException("ModuleTemplate.getGoalHue: position " + position + 
					" is out of bounds; must be between -1 (signifying canvas color) or " + (this.colorSelect.length - 1));
		} // error checking


		// Add one to inputNum because getAmplitude is working on a 1-to-numInputs range (rather than 0-to-[numInputs-1]):
		float	curAmp	= this.input.getAmplitude(inputNum + 1);

		if(curAmp < this.pianoThreshold[inputNum])	
		{
//			System.out.println("We're below the threshold....");
			this.nowBelow[inputNum]	= true;

			for(int i = 0; i < this.goalHue[inputNum].length; i++)
			{
				this.goalHue[inputNum][i]	= this.canvasColor[inputNum][i];
			} // for - canvas

		} else {
			this.nowBelow[inputNum]	= false;

			this.goalHue[inputNum]	= this.applyThresholdSBModulate(curAmp, inputNum, position);
		} // else

		if(this.checkpoint[inputNum] < this.parent.millis())
		{
			for(int i = 0; i < 3; i++)
			{				
				// if the current hue is less than the goalHue - the colorAdd, then add colorAdd:
				if(this.curHue[inputNum][i] < this.goalHue[inputNum][i] - (this.colorAdd[inputNum][i] / 2))
				{
					this.curHue[inputNum][i]	=	this.curHue[inputNum][i] + this.colorAdd[inputNum][i];
				} else 
					// otherwise, if it's more than the goal Hue, even after adding half of colorAdd, then subtract:
					if(this.curHue[inputNum][i] > this.goalHue[inputNum][i] + (this.colorAdd[inputNum][i] / 2))
					{
						this.curHue[inputNum][i]	=	this.curHue[inputNum][i] - this.colorAdd[inputNum][i];
					}
			} // for - i

			this.checkpoint[inputNum] = (this.parent.millis() + 50);
		} // if - adding every 50 millis

		/*	
		System.out.println("curHue: " + this.curHue[0] + ", " + 
				+ this.curHue[1] + ", "
				+ this.curHue[2]);
		System.out.println("goalHue: " + this.goalHue[0] + ", " + 
						+ this.goalHue[1] + ", "
						+ this.goalHue[2]);

		System.out.println("input.getAmplitude() = " + input.getAmplitude());

		System.out.println("colorAdd: " + this.colorAdd[0] + ", " + 
				+ this.colorAdd[1] + ", "
				+ this.colorAdd[2]);
		 */ 

		float	lowBound;
		float	highBound;

		for (int i = 0; i < 3; i++)
		{
			lowBound	= this.goalHue[inputNum][i] - 5;
			highBound	= this.goalHue[inputNum][i] + 5;

			// Now check colors for whether they have moved into the boundaries:
			if(this.curHue[inputNum][i] < highBound && this.curHue[inputNum][i] > lowBound) {
				// if here, color has been reached.
				this.colorReachedArray[inputNum][i]	= true;
			} else {
				this.colorReachedArray[inputNum][i]	= false;
			}
		} // for

		// If all elements of the color are in range, then the color has been reached:
		this.colorReached[inputNum]	= this.colorReachedArray[inputNum][0] && this.colorReachedArray[inputNum][1] && this.colorReachedArray[inputNum][2];

		// If coming from a low amplitude note and not yet reaching a color,
		// use the attack value to control the color change:
		if(!this.nowBelow[inputNum] && !colorReached[inputNum]) 
		{	
			this.attRelTranPos[inputNum]	= 0;
			//			System.out.println("	attack!!!!");
		} else if(!this.nowBelow[inputNum] && colorReached[inputNum]) {
			// Or, if coming from one super-threshold note to another, use the transition value:
			this.attRelTranPos[inputNum]	= 2;
			//			System.out.println("	transition.... transition [doooooo do dooo do do ] - transition!");
		} else if(this.nowBelow[inputNum]) {
			// Or, if volume fell below the threshold, switch to release value:
			this.attRelTranPos[inputNum]	= 1;
			//			System.out.println("	re....lent! re...coil! re...verse!");
		}

		// Calculate color ranges:
		for(int i = 0; i < this.curHue[inputNum].length; i++)
		{
			this.colorRange[inputNum][i]	= Math.abs(this.goalHue[inputNum][i] - this.curHue[inputNum][i]);

			// divide the attack/release/transition value by 50
			// and divide colorRange by that value to find the amount to add each 50 millis.
			float addThis = (int)(this.colorRange[inputNum][i] / (this.attRelTranVals[inputNum][this.attRelTranPos[inputNum]] / 50));

			this.colorAdd[inputNum][i]	= (int)addThis;	
		} // for

	} // fade


	/**
	 * TODO - trying to get rid of this; these should be initialized in constructor anyway.
	 * 
	 * Classes using ModuleTemplate should call this method in setup() with a position in colors.
	 * 
	 * @param curHuePos	position in ModuleTemplate.colors
	 */
	/*	public void setCurHueColorRangeColorAdd(int curHuePos)
	{
		if(curHuePos < 0 || curHuePos > this.colorSelect.length) {
			throw new IllegalArgumentException("Module_01_02.setup(): curHuePos " + curHuePos + " is out of the bounds of the colors; " + 
					"must be between 0 and " + this.colorSelect.length);
		} // error checking

		this.curHue	= new int[] { 255, 255, 255 };
		// The following line caused problems!
		// (That is, it made that position in colors follow curHue as the latter changed.)
		// Never use it.
		//		curHue	= this.colors[curHuePos];

		for(int i = 0; i < this.curHue.length; i++)
		{
			this.colorRange[i]	= Math.abs(this.goalHue[i] - this.curHue[i]);

			// divide the attack/release/transition value by 50
			// and divide colorRange by that value to find the amount to add each 50 millis.
			this.colorAdd[i]	= (int)(this.colorRange[i] / (this.attRelTranVals[this.attRelTranPos] / 50));
		}
	} // setCurHue
	 */

	/**
	 * Uses the values of the specialColors CWs to apply the current colorStyle.
	 */
	protected void applySpecialColors()
	{
		// Rainbow:
		if(this.curColorStyle == ModuleTemplate01.CS_RAINBOW)
		{
			this.rainbow();
		} // if - rainbow

		// Dichromatic:
		if(this.curColorStyle == ModuleTemplate01.CS_DICHROM)
		{

			// First time to dichromatic, dichromFlag will be false, 
			// and the two colors will be set to contrast.			
			if(!this.dichromFlag)
			{
				this.dichromatic_OneRGB(this.getColor(0));

				this.dichromFlag	= true;
			} // first time
			// After the first time, use current color values
			// (allows selection of 2nd color):
			else
			{
				this.dichromatic_TwoRGB(this.getColor(0), this.getColor(this.colorSelect.length - 1), true);
			}

		} // Dichromatic

		// Trichromatic:
		if(this.curColorStyle == ModuleTemplate01.CS_TRICHROM)
		{			
			int	colorPos2	= 4;	// initializing for the first call
			int	colorPos3	= 8;

			if(this.majMinChrom == 2)
			{
				colorPos2	= 4;
				colorPos3	= 8;
			} else {

				colorPos2	= 5;
				colorPos3	= 7;
			}

			this.trichromatic_ThreeRGB(this.getColor(0), this.getColor(colorPos2), this.getColor(colorPos3));

		} // Trichromatic
	} // applySpecialColors


	/**
	 * Converts the given color to HSB and sends it to dichromatic_OneHSB.
	 * (dichromatic_OneHSB will send it to _TwoHSB, which will set this.colors, changing the scale.)
	 * 
	 * @param rgbVals	float[] of RGB values defining the color for the tonic of the scale.
	 */
	public void dichromatic_OneRGB(int[] rgbVals)
	{
		if(rgbVals == null) {
			throw new IllegalArgumentException("Module_01_02.dichromatic_OneRGB: int[] parameter is null.");
		}

		float[]	hsbVals	= new float[3];
		Color.RGBtoHSB((int)rgbVals[0], (int)rgbVals[1], (int)rgbVals[2], hsbVals);

		this.dichromatic_OneHSB(hsbVals);
	} // dichromatic_OneRGB

	/**
	 * Uses the given HSB color to find the color across it on the HSB wheel,
	 * converts both colors to RGB, and passes them as parameters to dichromatic_TwoRGB.
	 * 
	 * @param hue	float[] of HSB values defining the color at the tonic of the current scale.
	 */
	private void dichromatic_OneHSB(float[] hsbVals)
	{
		if(hsbVals == null) {
			throw new IllegalArgumentException("Module_01_02.dichromatic_OneHSB: float[] parameter hsbVals is null.");
		} // error checking

		// find the complement:
		float[]	hsbComplement	= new float[] { (float) ((hsbVals[0] + 0.5) % 1), 1, 1 };

		// convert them both to RGB;
		int[]	rgbVals1	= new int[3];
		int[]	rgbVals2	= new int[3];

		System.out.println("dichromatic_OneHSB: rgbVals2 = rgb(" + rgbVals2[0] + ", " + + rgbVals2[1] + ", " + rgbVals2[2] + ")");

		int	rgb1	= Color.HSBtoRGB(hsbVals[0], hsbVals[1], hsbVals[2]);
		Color	rgbColor1	=  new Color(rgb1);

		// Using individual get[Color]() functions b/c getComponents() uses a 0-1 range.
		rgbVals1[0]	= rgbColor1.getRed();
		rgbVals1[1]	= rgbColor1.getGreen();
		rgbVals1[2]	= rgbColor1.getBlue();	

		int	rgb2	= Color.HSBtoRGB(hsbComplement[0], hsbComplement[1], hsbComplement[2]);
		Color	rgbColor2	=  new Color(rgb2);

		// Using individual get[Color]() functions b/c getComponents() uses a 0-1 range.
		rgbVals2[0]	= rgbColor2.getRed();
		rgbVals2[1]	= rgbColor2.getGreen();
		rgbVals2[2]	= rgbColor2.getBlue();	

		this.dichromatic_TwoRGB(rgbVals1, rgbVals2, true);
	} // dichromatic_OneHSB(int)


	/**
	 * This method fills colors with the spectrum of colors between the given rgb colors.
	 * 
	 * @param rgbVals1	float[] of rgb values defining tonicColor.
	 * @param rgbVals2	float[] of rgb values defining the color of the last note of the scale.
	 */
	public void dichromatic_TwoRGB(int[] rgbVals1, int[] rgbVals2, boolean fillFirstToLast)
	{
		if(rgbVals1 == null || rgbVals2 == null) {
			throw new IllegalArgumentException("Module_01_02.dichromatic_TwoRGB: at least one of the float[] parameters is null.");
		} // error checking

		// Percent should be the percent of the difference between the first and second colors,
		// but the math doesn't work if we divide by 100 here, so that will happen later.
		//		float	percent		= 100 / this.scaleLength;

		// For a minor scale, divide by 11 so that the last note of the scale will be 2ndColor
		// (rather than the leading tone, which doesn't show up in the minor scale - we just display natural minor)
		// Everything else gets 12.
		float	percent;
		if(this.majMinChrom == 1)
		{
			percent	= 100 / 11;
		} else 
		{
			percent		= 100 / 12;
		}

		// There will be a difference for red, green, and blue.
		//		float	difference;
		float	rDif	= rgbVals1[0] - rgbVals2[0];
		float	gDif	= rgbVals1[1] - rgbVals2[1];
		float	bDif	= rgbVals1[2] - rgbVals2[2];

		System.out.println("gDif = " + gDif + "; (gDif * percent / 100) = " + (gDif * percent / 100));

		int[]	curColor	= this.getColor(0);
		int[]	newColor	= new int[3];

		// Loop through red, then green, then blue
		// (could do it like normal, but then would have to calculate difference each time;
		// those who save processor cycles store up treasure in Heaven):
		/*		for(int i = 0; i < 3; i++)
		{
			difference	= rgbVals1[i] - rgbVals2[i];
		 */
		for(int i = 0; i < this.colors.length; i++)
		{

			for(int j = 0; j < this.colors[i].length - 1; j++)
			{
				// Take the percent of the difference multiplied by the position in the array,
				// subtracting it from the first color to deal with negatives correctly
				// (and dividing by 100 because percent requires it but to do so earlier would create smaller numbers than Java likes to deal with).
				//				this.colors[j][i]	= this.colors[0][i] - (difference * j * percent / 100);

				newColor[0]	= (int)(curColor[0] - (rDif * j * percent / 100));
				newColor[1]	= (int)(curColor[1] - (gDif * j * percent / 100));
				newColor[2]	= (int)(curColor[2] - (bDif * j * percent / 100));

				System.out.println("dichrom: newColor[0] = " + newColor[0] + "; newColor[1] = " + newColor[1] + "; newColor[2] = " + newColor[2]);

				this.colors[i][j][0]	= newColor[0];
				this.colors[i][j][1]	= newColor[1];
				this.colors[i][j][2]	= newColor[2];
				//			this.setColor(i, newColor, false);
				/*			this.setColorSelectCW(i, newColor);
			int	specialColorsPos	= this.arrayContains(this.specialColorsPos[this.currentInput], i);
			if(specialColorsPos > -1)
			{
				this.setSpecialColorsCW(specialColorsPos, newColor);
			}
				 */			
			} // for - j

			this.colors[i][this.colors[i].length - 1]	= rgbVals2;
		} // for - i


		// Fill the last color manually, because if we don't,
		// it can't seem to calculate correctly when the tonic color is changed:
		for(int i = 0; i < this.colors.length; i++)
		{
			this.colors[i][this.colors[i].length - 1]	= rgbVals2;
		}
		//		this.setColor(this.colorSelect.length - 1, rgbVals2, false);	
		/*
		this.setColorSelectCW(this.colorSelect.length - 1, rgbVals2);
		int	specialColorsPos	= this.arrayContains(this.specialColorsPos[this.currentInput], (this.colorSelect.length - 1));
		if(specialColorsPos > -1)
		{
			this.setSpecialColorsCW(specialColorsPos, newColor);
		}
		 */

		this.fillHSBColors();

	} // dichromatic_TwoRGB


	/**
	 * Converts the given color to HSB and sends it to dichromatic_OneHSB.
	 * (dichromatic_OneHSB will send it to _TwoHSB, which will set this.colors, changing the scale.)
	 * 
	 * @param rgbVals	float[] of RGB values defining the color for the tonic of the scale.
	 */
	public void trichromatic_OneRGB(int[] rgbVals)
	{
		if(rgbVals == null) {
			throw new IllegalArgumentException("Module_01_02.trichromatic_OneRGB: int[] parameter is null.");
		}

		float[]	hsbVals	= new float[3];
		Color.RGBtoHSB((int)rgbVals[0], (int)rgbVals[1], (int)rgbVals[2], hsbVals);

		this.trichromatic_OneHSB(hsbVals);
	} // trichromatic_OneRGB

	/**
	 * Uses the given HSB color to find the color across it on the HSB wheel,
	 * converts both colors to RGB, and passes them as parameters to dichromatic_TwoRGB.
	 *
	 * @param hsbVals	float[] of HSB values defining the color at the tonic of the current scale.
	 */
	private void trichromatic_OneHSB(float[] hsbVals)
	{
		if(hsbVals == null) {
			throw new IllegalArgumentException("Module_01_02.dichromatic_OneHSB: float[] parameter hsbVals is null.");
		} // error checking

		// find the triad:
		float[]	hsbTriad1	= new float[] { (float) ((hsbVals[0] + 0.33) % 1), 1, 1 };
		float[]	hsbTriad2	= new float[] { (float) ((hsbVals[0] + 0.67) % 1), 1, 1 };

		// convert them both to RGB;
		int[]	rgbVals1	= new int[3];
		int[]	rgbVals2	= new int[3];
		int[]	rgbVals3	= new int[3];

		int	rgb1	= Color.HSBtoRGB(hsbVals[0], hsbVals[1], hsbVals[2]);
		Color	rgbColor1	=  new Color(rgb1);

		// Using individual get[Color]() functions b/c getComponents() uses a 0-1 range.
		rgbVals1[0]	= rgbColor1.getRed();
		rgbVals1[1]	= rgbColor1.getGreen();
		rgbVals1[2]	= rgbColor1.getBlue();	

		int	rgb2	= Color.HSBtoRGB(hsbTriad1[0], hsbTriad1[1], hsbTriad1[2]);
		Color	rgbColor2	=  new Color(rgb2);

		// Using individual get[Color]() functions b/c getComponents() uses a 0-1 range.
		rgbVals2[0]	= rgbColor2.getRed();
		rgbVals2[1]	= rgbColor2.getGreen();
		rgbVals2[2]	= rgbColor2.getBlue();	

		int	rgb3	= Color.HSBtoRGB(hsbTriad2[0], hsbTriad2[1], hsbTriad2[2]);
		Color	rgbColor3	=  new Color(rgb3);

		// Using individual get[Color]() functions b/c getComponents() uses a 0-1 range.
		rgbVals3[0]	= rgbColor3.getRed();
		rgbVals3[1]	= rgbColor3.getGreen();
		rgbVals3[2]	= rgbColor3.getBlue();	

		this.trichromatic_ThreeRGB(rgbVals1, rgbVals2, rgbVals3);
	} // trichromatic_OneHSB


	/**
	 * Calculates the colors between the 3 sets of given vals
	 * and fills colors with a spectrum fading between them.
	 * 
	 * @param rgbVals1	rgb vals for the tonic color
	 * @param rgbVals2	rgb vals for the sub-dominant for major/minor scales or "5th scale degree" (counting by half steps) for chromatic scales
	 * @param rgbVals3	rgb vals for the dominant for major/minor scales or the "9th scale degree" (counting by half steps) for chromatic scales
	 */
	public void trichromatic_ThreeRGB(int[] rgbVals1, int[] rgbVals2, int[] rgbVals3)
	{
		if(rgbVals1 == null || rgbVals2 == null || rgbVals3 == null) {
			throw new IllegalArgumentException("Module_01_02.trichromatic_ThreeRGB: at least one of the float[] parameters is null.");
		} // error checking

		this.trichromCounts++;
		System.out.println("trichromCounts = " + this.trichromCounts);

		int	color1pos	= 0;
		int	color2pos;
		int	color3pos;

		int	divideBy1;
		int	divideBy2;
		int	divideBy3;

		if(this.majMinChrom == 2)
		{
			// if chromatic scale, put the colors equally throughout:
			color2pos	= this.scaleLength / 3;
			color3pos	= (this.scaleLength / 3) * 2;

			divideBy1	= color2pos - color1pos;
			divideBy2	= color3pos - color2pos;
			divideBy3	= this.scaleLength - color3pos;
		} else {
			// These are their positions in trichromColors:
			color2pos	= 3;	// subdominant
			color3pos	= 4;	// dominant

			// (5 and 7 are the positions in colorSelect):
			//			color2pos	= 5;	// subdominant
			//			color3pos	= 7;	// dominant

			divideBy1	= 3;
			divideBy2	= 1;
			divideBy3	= 2;
		}

		int	redDelta1	= (int)((rgbVals1[0] - rgbVals2[0]) / divideBy1);
		int	greenDelta1	= (int)((rgbVals1[1] - rgbVals2[1]) / divideBy1);
		int	blueDelta1	= (int)((rgbVals1[2] - rgbVals2[2]) / divideBy1);

		int	redDelta2	= (int)((rgbVals2[0] - rgbVals3[0]) / divideBy2);
		int	greenDelta2	= (int)((rgbVals2[1] - rgbVals3[1]) / divideBy2);
		int	blueDelta2	= (int)((rgbVals2[2] - rgbVals3[2]) / divideBy2);

		int	redDelta3	= (int)((rgbVals3[0] - rgbVals1[0]) / divideBy3);
		int	greenDelta3	= (int)((rgbVals3[1] - rgbVals1[1]) / divideBy3);
		int	blueDelta3	= (int)((rgbVals3[2] - rgbVals1[2]) / divideBy3);

		/*		
		System.out.println("redDelta1 = " + redDelta1 + "; greenDelta1 = " + greenDelta1 + "; blueDelta1 = " + blueDelta1);
		System.out.println("redDelta2 = " + redDelta2 + "; greenDelta2 = " + greenDelta1 + "; blueDelta2 = " + blueDelta2);
		System.out.println("redDelta3 = " + redDelta3 + "; greenDelta3 = " + greenDelta1 + "; blueDelta3 = " + blueDelta3);
		 */
		// This array has the trichromatic spectrum:
		int[][]	trichromColors	= new int[this.scaleLength][3];
//		trichromColors	= new int[this.scaleLength][3];
		trichromColors	= new int[this.scaleLength][3];

		// fill first position with first color:
		for(int i = 0; i < rgbVals1.length; i++)
		{
			trichromColors[0][i]	= rgbVals1[i];
		}

		// fill from first color to second color:
		for(int i = 1; i < color2pos; i++)
		{
			//			for(int j = 0; j < trichromColors[i].length; j++)
			//			{
			trichromColors[i][0]	= trichromColors[i - 1][0] - redDelta1;
			trichromColors[i][1]	= trichromColors[i - 1][1] - greenDelta1;
			trichromColors[i][2]	= trichromColors[i - 1][2] - blueDelta1;
			//			} // for - j
		} // for - first color to second color

		// Fill second color (in case the math is a little off, since we are rounding to ints, and it doesn't quite make it back):
		trichromColors[color2pos][0]	= rgbVals2[0];
		trichromColors[color2pos][1]	= rgbVals2[1];
		trichromColors[color2pos][2]	= rgbVals2[2];
		System.out.println("Trichromatic 2nd color = rgb(" + rgbVals2[0] + ", " + rgbVals2[1] + ", " + rgbVals2[2] + ")");
//		Exception	e	= new Exception("Just detective work.");
//		e.printStackTrace();

		// fill from second color to third color:
		for(int i = color2pos + 1; i < color3pos; i++)
		{
			//			for(int j = 0; j < trichromColors[i].length; j++)
			//			{
			trichromColors[i][0]	= trichromColors[i - 1][0] - redDelta2;
			trichromColors[i][1]	= trichromColors[i - 1][1] - greenDelta2;
			trichromColors[i][2]	= trichromColors[i - 1][2] - blueDelta2;
			//			} // for - j
		} // for - first color to second color

		// Fill third color (in case the math is a little off, since we are rounding to ints, and it doesn't quite make it back):
		trichromColors[color3pos][0]	= rgbVals3[0];
		trichromColors[color3pos][1]	= rgbVals3[1];
		trichromColors[color3pos][2]	= rgbVals3[2];

		// fill from third color back to first color:
		for(int i = color3pos + 1; i < this.scaleLength; i++)
		{
			//			for(int j = 0; j < trichromColors[i].length; j++)
			//			{
			trichromColors[i][0]	= trichromColors[i - 1][0] - redDelta3;
			trichromColors[i][1]	= trichromColors[i - 1][1] - greenDelta3;
			trichromColors[i][2]	= trichromColors[i - 1][2] - blueDelta3;
			//			} // for - j
		} // for - third color to first color

		// fill colors with the trichrom spectrum; some colors will be repeated, as designated in scaleDegreeColors:
		int	trichromColorPos	= 0;
		for(int i = 0; i < this.colors.length; i++)
		{

			for(int j = 0; j < this.colorSelect.length && trichromColorPos < trichromColors.length; j++)
			{
				trichromColorPos	= this.scaleDegreeColors[this.majMinChrom][j];
				if(j == color2pos)
				{
					System.out.println("  Setting color at pos " + j + " to rgb(" + trichromColors[trichromColorPos][0] + ", " + trichromColors[trichromColorPos][1] + ", " + trichromColors[trichromColorPos][2] + ")");
				}
				//			this.setColor(i, trichromColors[trichromColorPos], false);
				/*			this.setColorSelectCW(i, rgbVals2);
			int	specialColorsPos	= this.arrayContains(this.specialColorsPos[this.currentInput], i);
			if(specialColorsPos > -1)
			{
				this.setSpecialColorsCW(specialColorsPos, trichromColors[trichromColorPos]);
			}
				 */
				this.colors[i][j]	= trichromColors[trichromColorPos];
			} // for - j
		} // for - i

		this.fillHSBColors();
	} //trichromatic_ThreeRGB


	/**
	 * Populates colors with rainbow colors (ROYGBIV - with a few more for chromatic scales).
	 */
	protected void rainbow()
	{
		for(int i = 0; i < this.colors.length; i++)
		{
			for(int j = 0; j < this.colorSelect.length && j < this.rainbowColors[this.majMinChrom].length; j++)
			{
				this.colors[i][j]	= this.rainbowColors[this.majMinChrom][j];

				//				this.setColor(i, this.rainbowColors[this.majMinChrom][i], true);this.setColorSelectCW(i, rgbVals2);
				/*			
				this.setColorSelectCW(i, this.rainbowColors[this.majMinChrom][i]);
				int	specialColorsPos	= this.arrayContains(this.specialColorsPos[this.currentInput], i);
				if(specialColorsPos > -1)
				{
					this.setSpecialColorsCW(specialColorsPos, this.rainbowColors[this.majMinChrom][i]);
				}
				 */
			} // for - i (going through colors)

		}

		this.fillHSBColors();
	} // rainbow

	/**
	 * Sets the ColorStyle to the given ColorStyle
	 * and locks or unlocks the appropriate Buttons (e.g., in Rainbow, Tonic, 2ndColor and 3rdColor
	 * Buttons should all be unlocked, but for Dichromatic, only 3rdColor should be locked).
	 * 
	 * @param newColorStyle	int between 1 and 4 indicating the new ColorStyle
	 */
	public void setColorStyle(int newColorStyle)
	{
		this.curColorStyle	= newColorStyle;

		// Rainbow:
		if(this.curColorStyle == ModuleTemplate01.CS_RAINBOW)
		{
			//	if avoids errors during instantiation:
			if(this.controlP5.getController("button" + (this.firstSpecialColorsCWId - 100)) != null)	{	this.controlP5.getController("button" + (this.firstSpecialColorsCWId - 100)).lock();	}
			if(this.controlP5.getController("button" + (this.firstSpecialColorsCWId - 99)) != null)	{	this.controlP5.getController("button" + (this.firstSpecialColorsCWId  - 99)).lock();	}
			if(this.controlP5.getController("button" + (this.firstSpecialColorsCWId - 98)) != null)	{	this.controlP5.getController("button" + (this.firstSpecialColorsCWId - 98)).lock();	}

			this.rainbow();
		} // if - rainbow

		// Dichromatic:
		if(this.curColorStyle == ModuleTemplate01.CS_DICHROM)
		{
			for(int i = 0; i < this.numInputs; i++)
			{

				this.specialColorsPos[i][0]	= 0;

				// For minor keys, choose the 2nd to last note; else choose the last note:
				if(this.majMinChrom == 1)	{	this.specialColorsPos[i][1]	= this.colorSelect.length - 2;	}
				else						{	this.specialColorsPos[i][1]	= this.colorSelect.length - 1;	}

				if(this.controlP5.getController("button" + (this.firstSpecialColorsCWId - 100)) != null)	{	this.controlP5.getController("button" + (this.firstSpecialColorsCWId - 100)).unlock();	}
				if(this.controlP5.getController("button" + (this.firstSpecialColorsCWId - 99)) != null)	{	this.controlP5.getController("button" + (this.firstSpecialColorsCWId  - 99)).unlock();	}
				if(this.controlP5.getController("button" + (this.firstSpecialColorsCWId - 98)) != null)	{	this.controlP5.getController("button" + (this.firstSpecialColorsCWId - 98)).lock();	}

				// First time to dichromatic, dichromFlag will be false, 
				// and the two colors will be set to contrast.			
				if(!this.dichromFlag)
				{
					this.dichromatic_OneRGB(this.colors[this.currentInput][0]);
					//					this.dichromatic_OneRGB(this.getColor(0));

					this.dichromFlag	= true;
				} // first time
				// After the fjrst tjme, use current color values
				// (allows selection of 2nd color):
				else
				{
					System.out.println("passing colors at positions 0 and " + (this.colors[this.currentInput].length - 1) 
							+ " to dichromatic:\n\trgb(" + this.colors[this.currentInput][0][0] + ", " + this.colors[this.currentInput][0][1] + ", " + this.colors[this.currentInput][0][2]
									+ ");\n\trgb(" + this.colors[this.currentInput][this.colors[this.currentInput].length - 1][0]
											+ ", " + this.colors[this.currentInput][this.colors[this.currentInput].length - 1][1]
													+ ", " + this.colors[this.currentInput][this.colors[this.currentInput].length - 1][2] + ")");

					this.dichromatic_TwoRGB(this.colors[this.currentInput][0], this.colors[this.currentInput][this.colors[this.currentInput].length - 1], false);
					//					this.dichromatic_TwoRGB(this.getColor(0), this.getColor(this.colorSelect.length - 1), true);
				}
			} // for

		} // Dichromatic

		// Trichromatic:
		if(this.curColorStyle == ModuleTemplate01.CS_TRICHROM)
		{
			int	colorPos2	= 4;	// initializing for the first call
			int	colorPos3	= 8;

			for(int i = 0; i < this.numInputs; i++)
			{

				// Turned off the "first time/remaining times" because it's still pretty interesting
				// and, I think, more intuitive, coming off of another color.  Dichromatic is boring coming off rainbow.
				// first time trichromatic has been called:
				if(!this.trichromFlag)
				{
					this.trichromatic_OneRGB(this.getColor(0));
					this.trichromFlag	= true;
				}
				// every other time:
				else
				{
					// TODO: colorPos2 and 3 are always going to be 4 and 8 when this is called?
					System.out.println("Trichrom called more than one time");
					// This makes sure that, even if the positions of the specialColors change,
					// the colors themselves will remain constant from one style/scale to the next:
					System.out.println("	In setColorStyle, 2nd color = rgb(" + this.getColor(this.specialColorsPos[i][1])[0] + ", "
							+ this.getColor(this.specialColorsPos[i][1])[2] + ", " + this.getColor(this.specialColorsPos[i][1])[2] + ")");
					//					this.setColor(colorPos2, this.getColor(this.specialColorsPos[i][1]), false);
					//					this.setColor(colorPos3, this.getColor(this.specialColorsPos[i][2]), false);
/*					this.setColorSelectCW(colorPos2, this.getColor(this.specialColorsPos[i][1]));
					this.setColorSelectCW(colorPos3, this.getColor(this.specialColorsPos[i][2]));
					int	specialColorsPos	= this.arrayContains(this.specialColorsPos[this.currentInput], colorPos2);
					this.setSpecialColorsCW(specialColorsPos, this.getColor(this.specialColorsPos[i][1]));
					specialColorsPos	= this.arrayContains(this.specialColorsPos[this.currentInput], colorPos3);
					this.setSpecialColorsCW(specialColorsPos, this.getColor(this.specialColorsPos[i][2]));
*/
					
					if(this.majMinChrom == 2)
					{
						colorPos2	= 4;
						colorPos3	= 8;
					} else {
						// Positions have to be 5 and 7, not 3 and 4, since colors is filled all the way and we just ignore
						// non-diatonic tones, so 5 and 7 actually corresponds to the mediant and dominant scale degrees.

						colorPos2	= 5;
						colorPos3	= 7;
					} // else - colorPos for different scales
				} // else - all but the first time
				
				this.colors[i][colorPos2]	= this.colors[i][this.specialColorsPos[i][1]];
				this.colors[i][colorPos3]	= this.colors[i][this.specialColorsPos[i][2]];


				this.specialColorsPos[i][0]	= 0;
				this.specialColorsPos[i][1]	= colorPos2;
				this.specialColorsPos[i][2]	= colorPos3;

				if(this.controlP5.getController("button" + (this.firstSpecialColorsCWId - 100)) != null)	{	this.controlP5.getController("button" + (this.firstSpecialColorsCWId - 100)).unlock();	}
				if(this.controlP5.getController("button" + (this.firstSpecialColorsCWId - 99)) != null)	{	this.controlP5.getController("button" + (this.firstSpecialColorsCWId  - 99)).unlock();	}
				if(this.controlP5.getController("button" + (this.firstSpecialColorsCWId - 98)) != null)	{	this.controlP5.getController("button" + (this.firstSpecialColorsCWId - 98)).unlock();	}

				this.trichromatic_ThreeRGB(this.getColor(0), this.getColor(colorPos2), this.getColor(colorPos3));
			} // for
		} // Trichromatic

	} // setColorStyle

	/**
	 * Applies the values of the Red Modulate/Green Modulate/Blue Modulate sliders.
	 */
	protected void applyRGBModulate()
	{
		int[]	color; //	= new int[3];

		for(int i = 0; i < this.colorSelect.length; i++)
		{
			color	= this.getColor(i);

			for(int j = 0; j < color.length; j++)
			{
				// Adds redModulate to the red, greenModulate to the green, and blueModulate to the blue:
				color[j]	= Math.min(Math.max(color[j] + (int)this.redGreenBlueMod[j], 0), 255);
			} // for - j

			//			this.setColor(i, color, false);
			this.setColorSelectCW(i, color);
			int	specialColorsPos	= this.arrayContains(this.specialColorsPos[this.currentInput], i);
			if(specialColorsPos > -1)
			{
				this.setSpecialColorsCW(specialColorsPos, color);
			}
		} // for - i

	} // applyRGBModulate

	/**
	 * Applies the values from this.hueSatBrightnessMod to the contents of the colors array parameter.
	 * @param colors	this.colors or this.legendColors
	 * @param hsbColors	this.hsbColors
	 */
	protected void applyHSBModulate()
	{
		if(this.hsbColors == null)	{	this.fillHSBColors();	}
		
		for(int i = 0; i < this.colors.length; i++)
		{
//			if(this.firstColorSelectCWId > 0)
//			{


				for (int j = 0; j < this.colors[i].length; j++)
				{
					float[] hsb 			= new float[3];
					
					// Converts this position of hsbColors from RGB to HSB:
					Color.RGBtoHSB(this.hsbColors[i][j][0], this.hsbColors[i][j][1], this.hsbColors[i][j][2], hsb);

					// Applies the status of the sliders to the newly-converted color:
					hsb[0] = (hsb[0] + this.hueSatBrightnessMod[0] + this.hueSatBrightPercentMod[0] + 1) % 1;
					hsb[1] = Math.max(Math.min(hsb[1] + this.hueSatBrightnessMod[1] + this.hueSatBrightPercentMod[1], 1), 0);
					hsb[2] = Math.max(Math.min(hsb[2] + this.hueSatBrightnessMod[2] + this.hueSatBrightPercentMod[2], 1), 0);

					// Converts the color back to RGB:
					int oc = Color.HSBtoRGB(hsb[0], hsb[1],  hsb[2]);
					Color a = new Color(oc);
					
					this.colors[i][j]	= new int[] { a.getRed(), a.getGreen(), a.getBlue() };
					
					// Sets the ColorWheels to the newly modded color:
					//					this.setColor(i, new int[] { a.getRed(), a.getGreen(), a.getBlue() }, false);
/*					this.setColorSelectCW(i, new int[] { a.getRed(), a.getGreen(), a.getBlue() });
					int	specialColorsPos	= this.arrayContains(this.specialColorsPos[this.currentInput], i);
					if(specialColorsPos > -1)
					{
						this.setSpecialColorsCW(specialColorsPos, new int[] { a.getRed(), a.getGreen(), a.getBlue() });
					}
					*/
					//			((ColorWheel)this.controlP5.getController("colorWheel" + id)).setRGB(a.getRGB());
				} // for 
//			} else {
//				System.out.println("ModuleTemplate.applyHSBModulate: firstColorSelectCWId == " + this.firstColorSelectCWId + "; did not attempt to set ColorWheels.");
//			} // else - let the user know that CWs not there and we didn't set them
		} // for

	} // applyHSBModulate


	/**
	 * Applies the values of the threshold saturation and brightness Sliders 
	 * to the color of the ColorWheel at the given position and returns the affected color.
	 * 
	 * @param curAmp	the current amplitude
	 * @param colorPos	the position in this.colorSelect to which the sat/brightness should be applied
	 * @return			the color with saturation and brightness adjustments
	 */
	protected int[] applyThresholdSBModulate(float curAmp, int inpuNum, int colorPos)
	{
		// Error checking:
		if(colorPos < 0 || colorPos >= this.colorSelect.length) {
			throw new IllegalArgumentException("ModuleTemplate.applyThresholdSBModulate: int parameter colorPos is " + colorPos + 
					", which is out of bounds; must be between 0 and " + (this.colorSelect.length - 1));
		}

		// Converts the current amplitude into a number between 0 and 100,
		// depending on where curAmp is in relation to the saturation or brightness forte threshold:
		float	satMappingVal		= Math.max(Math.min(PApplet.map(curAmp, 0, Math.max(this.satBrightThresholdVals[0], this.minThreshold + 1), 0, 100), 100), 0);
		float	brightMappingVal	= Math.max(Math.min(PApplet.map(curAmp, 0, Math.max(this.satBrightThresholdVals[1], this.minThreshold + 1), 0, 100), 100), 0);


		// Notice how hueSatBrightPercentMod is accessed at 1 and 2, since hue is also a part of it,
		// but satBrightPercentVals is accessed at 0 and 1, since it is only for saturation and brightness.
		this.hueSatBrightPercentMod[1]	= (this.satBrightPercentVals[0] * satMappingVal) / 100;
		this.hueSatBrightPercentMod[2]	= (this.satBrightPercentVals[1] * brightMappingVal) / 100;

		float[] hsb = new float[3];

		// Converts this position of hsbColors from RGB to HSB:
		Color.RGBtoHSB(this.hsbColors[inpuNum][colorPos][0], this.hsbColors[inpuNum][colorPos][1], this.hsbColors[inpuNum][colorPos][2], hsb);

		for(int j = 0; j < this.hueSatBrightPercentMod.length; j++)
		{
			if(this.hueSatBrightPercentMod[j] <= 0)
			{
				if(j == 0) {
					hsb[0] = (hsb[0] + this.hueSatBrightnessMod[0] + this.hueSatBrightPercentMod[0] + 1) % 1;
				} else {

					hsb[j] = Math.max(Math.min(hsb[j] + this.hueSatBrightnessMod[j] + this.hueSatBrightPercentMod[j], 1), 0);
				}  // if/else - == 0
			} // if percent greater than 0
			else
			{
				if(j == 0) {
					hsb[j] = (hsb[j] + this.hueSatBrightnessMod[j] + this.hueSatBrightPercentMod[j] + 1) % 1;
				} else {
					hsb[j] = Math.max(Math.min(this.hueSatBrightnessMod[j] + this.hueSatBrightPercentMod[j], 1), 0);
				}  // if/else - == 0

			} // else - percent less than 0
		} // for - hueSatBrightPercentMod

		// Converts the color back to RGB:
		int oc = Color.HSBtoRGB(hsb[0], hsb[1],  hsb[2]);
		Color a = new Color(oc);

		return new int[] { a.getRed(), a.getGreen(), a.getBlue() };
	} // applyThresholdSBModulate


	/**
	 * Puts the contents of this.colors into this.hsbColors so that HSB sliders will have a base to go off of
	 * (else they won't know how much to add to the colors each time).
	 * It is important to call this method whenever there has been a change of colors, e.g., at the end of
	 * rainbow() or trichromatic_ThreeRGB.
	 */
	protected	void fillHSBColors()
	{
		if(this.hsbColors == null) {
			this.hsbColors = new int[this.numInputs][this.colorSelect.length][3];
		}

		// Only do this after the colors have been initialized:
		if(this.firstColorSelectCWId > 0)
		{
			for(int i = 0; i < this.hsbColors.length; i++)
			{
				for(int j = 0; j < this.hsbColors[i].length; j++)
				{
					this.hsbColors[i][j]	= this.colors[i][j];
				} // for - j
			} // for - i
		} else {
			// Let the user know what we skipped this:
			System.err.println("ModuleTemplate.fillHSBColors: firstColorSelectCWId == " + this.firstColorSelectCWId + 
					"; did not fill hsbColors.");
		} // else
	} // fillhsbColors


	/**
	 * Calls playMelody(key, bpm, scale, rangeOctave) with the curKey, bpm, rangeOctave instance vars
	 * and the string corresponding to the majMinChrom instance var ("major", "minor", or "chromatic").
	 */
	protected void playMelody()
	{

		String[]	scales	= new String[] { "major", "minor", "chromatic" };
		this.melody.playMelody(this.curKey, this.bpm, scales[this.majMinChrom], this.rangeOctave, this.instrument);
	} // playMelody


	/**
	 * Displays the "sidebarGroup" of this.controlP5
	 */
	protected void displaySidebar(boolean show)
	{	
		//		this.controlP5.getGroup("sidebarGroup").setVisible(show);
		if(show)
		{
//			this.leftEdgeX 	= this.sidebarWidth;
		} else {
//			this.leftEdgeX	= 0;
		}

	} // displaySidebar

	/**
	 * Calls super.runMenu to show or hide the Controllers,
	 * but also sets leftEdgeX depending on whether or not the Menu is open.
	 */
	@Override
	public void runMenu()
	{
		super.runMenu();

		if(this.getIsRunning())
		{
//			this.leftEdgeX	= this.sidebarWidth;
		} else {
//			this.leftEdgeX	= 0;
		}
	} // runMenu

	/**
	 * Uses this.showPlayStop, this.showPause, and this.showHamburger to determine which of the 
	 * outside Buttons should be set to visible.
	 */
	public void showOutsideButtons()
	{
		this.outsideButtonsCP5.setVisible(true);

		this.outsideButtonsCP5.getController("play").setVisible(this.showPlayStop);
		this.outsideButtonsCP5.getController("pause").setVisible(this.showPause);
		this.outsideButtonsCP5.getController("hamburger").setVisible(this.showHamburger);
	} // showOutsideButtons


	/**
	 * Since ModuleTemplate implements ControlListener, it needs to have this method
	 * to "catch" the controlEvents from the ControlP5 (controlP5).
	 * 
	 * Any child classes that want to also use controlEvents can have their own controlEvent
	 * as long as they call super.controlEvent first.
	 */
	public void controlEvent(ControlEvent controlEvent)
	{
		super.controlEvent(controlEvent);

		//		System.out.println("ModuleMenu.controlEvent: controlEvent = " + controlEvent);

		int	id	= controlEvent.getController().getId();
		// Play button:
		if(controlEvent.getController().getName().equals("play"))
		{
			boolean	val	= ((Toggle)controlEvent.getController()).getBooleanValue();
			this.input.pause(true);
			this.outsideButtonsCP5.getController("pause").setVisible(val);
			this.showPause	= val;

			if(val)
			{
				this.playMelody();
			} else {
				// Unpauses the pause button so that it is ready to be paused when
				// play is pressed again:
				((Toggle)this.outsideButtonsCP5.getController("pause")).setState(false);
				//				this.showPause	= false;
				this.melody.stop();
			}

		} // if - play

		// Pause button:
		if(controlEvent.getController().getName().equals("pause"))
		{
			this.melody.pause(((Toggle)controlEvent.getController()).getBooleanValue());
		}

		// Hamburger button:
		if(controlEvent.getController().getName().equals("hamburger"))
		{
			// Make sure that we don't call this when we just mean to call menuX:
			if(this.parent.millis() > (this.lastMenuXMillis + 10))
			{
				this.setIsRunning(true);
				controlEvent.getController().setVisible(false);
			}
			this.showHamburger	= false;

			/*			
			this.controlP5.getWindow().resetMouseOver();
			this.menuIsOpen = true;
			this.displaySidebar(true);
			 */
		} // if - hamburger

		// MenuX button:
		if(controlEvent.getController().getName().equals("menuX"))
		{
			this.lastMenuXMillis	= this.parent.millis();
			this.setIsRunning(false);

			//			this.displaySidebar(false);
			/*			this.leftEdgeX	= 0;
			this.controlP5.getGroup("sidebarGroup").setVisible(false);
			 */
			//			this.outsideButtonsCP5.getController("hamburger").setVisible(true);
			this.outsideButtonsCP5.getController("hamburger").setVisible(!((Toggle)this.controlP5.getController("menuButton")).getBooleanValue());
			this.showHamburger	= false;
		} // if - menuX

		// Hide play button button:
		if(controlEvent.getName().equals("playButton"))
		{
			// Set the actual play button to visible/invisible:
			this.outsideButtonsCP5.getController("play").setVisible(!this.outsideButtonsCP5.getController("play").isVisible());
			this.outsideButtonsCP5.getController("pause").setVisible(((Toggle)this.outsideButtonsCP5.getController("play")).getBooleanValue() && this.outsideButtonsCP5.getController("play").isVisible());
			this.showPlayStop	= (this.outsideButtonsCP5.getController("play").isVisible());
			this.showPause		= ((Toggle)this.outsideButtonsCP5.getController("play")).getBooleanValue() && this.outsideButtonsCP5.getController("play").isVisible();
		} // if - hidePlayButton


		// Hide menu button button:
		if(controlEvent.getName().equals("menuButton"))
		{
			// Hamburger is still able to be clicked because of a boolean isClickable added to 
			//Controller; automatically false, but able to be set to true.
			// A Controller must be visible and/or clickable to respond to click.
			if(!this.getIsRunning())
			{
				this.outsideButtonsCP5.getController("hamburger").setVisible(!((Toggle)this.controlP5.getController("menuButton")).getBooleanValue());
			}
			this.showHamburger	= !((Toggle)this.controlP5.getController("menuButton")).getBooleanValue();
		} // if - hidePlayButton

		// Hide legend:
		if(controlEvent.getName().equals("legend"))
		{
			this.setShowScale(!((Toggle) (controlEvent.getController())).getState());
		}

		// Toggles
		if(id > 499 && id < 600)
		{
			// Range Segments:
			if(id >= this.firstRangeSegmentsId && id < (this.firstRangeSegmentsId + this.totalRangeSegments))
			{
				// Turn off the other Toggles:
				Toggle[] toggleArray	= new Toggle[this.totalRangeSegments];
				for(int i = 0; i < toggleArray.length; i++)
				{
					toggleArray[i]	= (Toggle) this.controlP5.getController("toggle" + (this.firstRangeSegmentsId + i));
				} // for - fill toggleArray

				boolean[]	broadcastState	= new boolean[toggleArray.length];
				for(int i = 0; i < toggleArray.length; i++)
				{
					// save the current broadcast state of the controller:
					broadcastState[i]	= toggleArray[i].isBroadcast();

					// turn off broadcasting to avoid endless looping in this method:
					toggleArray[i].setBroadcast(false);

					// switch off the ones that weren't just clicked, but keep the current one on:
					if(!controlEvent.getController().getName().equals(toggleArray[i].getName()))
					{
						toggleArray[i].setState(false);
					} else {
						toggleArray[i].setState(true);
						this.curRangeSegments	= (int)toggleArray[i].internalValue();
						this.resetThresholds(this.currentInput);
					}

					// set broadcasting back to original setting:
					toggleArray[i].setBroadcast(broadcastState[i]);
				} // for - switch off all Toggles

				// Make Buttons corresponding to non-existent thresholds unclickable:
				// (but don't do so before the Buttons have been initialized)
				if(this.firstColorSelectCWId > 0)
				{
					int	curButtonId;
					boolean	state;
					for(int i = 0; i < this.totalRangeSegments; i++)
					{
						curButtonId	= this.firstColorSelectCWId - 100 + i;

						if(i < this.curRangeSegments)	{	state	= false;		} 
						else 							{	state	= true;	}

						System.out.println("curButtonId = " + curButtonId + "; state = " + state);

						this.controlP5.getController("button" + curButtonId).setLock(state);
					} // for - making Buttons unclickable

					System.out.println("this.curRangeSegments = " + this.curRangeSegments);

				}
			} // range segments
		} // Toggles

		// Major/Minor/Chromatic buttons
		if(controlEvent.getName().equals("major") ||
				controlEvent.getName().equals("minor") ||
				controlEvent.getName().equals("chrom"))
		{
			System.out.println("New scale quality: " + controlEvent.getName());

			Toggle	curToggle	= (Toggle) controlEvent.getController();
			this.setCurKey(this.curKey, (int) curToggle.internalValue());
			//			this.majMinChrom	= (int) curToggle.internalValue();
			this.setColorStyle(this.curColorStyle);

			// Turn off the other two:
			Toggle[] toggleArray	= new Toggle[] {
					(Toggle)this.controlP5.getController("major"),
					(Toggle)this.controlP5.getController("minor"),
					(Toggle)this.controlP5.getController("chrom"),
			};
			boolean[]	broadcastState	= new boolean[toggleArray.length];
			for(int i = 0; i < toggleArray.length; i++)
			{
				// save the current broadcast state of the controller:
				broadcastState[i]	= toggleArray[i].isBroadcast();

				// turn off broadcasting to avoid endless looping in this method:
				toggleArray[i].setBroadcast(false);

				// only switch off the ones that weren't just clicked:
				if(!controlEvent.getController().getName().equals(toggleArray[i].getName()))
				{
					toggleArray[i].setState(false);
				}

				// set broadcasting back to original setting:
				toggleArray[i].setBroadcast(broadcastState[i]);
			} // for - switch off all Toggles:

			// Update Melody's scale:
			String[] scales	= new String[] { "major", "minor", "chromatic" };
			this.melody.setScale(scales[this.majMinChrom]);
			this.melody.setRangeList();
			try
			{
				((ScrollableList)this.controlP5.getController("rangeDropdown"))
				.setItems(this.melody.getRangeList())
				.setValue(0f);
			} catch(ClassCastException cce) {
				throw new IllegalArgumentException("ModuleTemplate.controlEvent - keyDropdown: error setting rangeList ScrollableList.");
			} // catch

		} // majMinChrom buttons

		// Color Style:
		if(controlEvent.getName().equals("rainbow") ||
				controlEvent.getName().equals("dichrom") ||
				controlEvent.getName().equals("trichrom") ||
				controlEvent.getName().equals("custom"))
		{
			Toggle	curToggle	= (Toggle) controlEvent.getController();

			this.setColorStyle((int)curToggle.internalValue());
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Turn off the other Toggles:
			Toggle[] toggleArray	= new Toggle[] {
					(Toggle)this.controlP5.getController("rainbow"),
					(Toggle)this.controlP5.getController("dichrom"),
					(Toggle)this.controlP5.getController("trichrom"),
					(Toggle)this.controlP5.getController("custom")
			};

			boolean[]	broadcastState	= new boolean[toggleArray.length];
			for(int i = 0; i < toggleArray.length; i++)
			{
				// save the current broadcast state of the controller:
				broadcastState[i]	= toggleArray[i].isBroadcast();

				// turn off broadcasting to avoid endless looping in this method:
				toggleArray[i].setBroadcast(false);

				// switch off the ones that weren't just clicked, but keep the current one on:
				if(!controlEvent.getController().getName().equals(toggleArray[i].getName()))
				{
					System.out.println("setting " + toggleArray[i] + " to false");
					toggleArray[i].setState(false);
				} else {
					System.out.println("setting " + toggleArray[i] + " to true");
					toggleArray[i].setState(true);
				}

				// set broadcasting back to original setting:
				toggleArray[i].setBroadcast(broadcastState[i]);
			} // for - switch off all Toggles:

			this.resetModulateSlidersTextfields();
		} // colorStyle buttons


		// Key dropdown ScrollableList:
		if(controlEvent.getName().equals("keyDropdown"))
		{
			// keyPos is the position of the particular key in the Scrollable List:
			int	keyPos	= (int)controlEvent.getValue();

			// getItem returns a Map of the color, state, value, name, etc. of that particular item
			//  in the ScrollableList:
			Map<String, Object> keyMap = this.controlP5.get(ScrollableList.class, "keyDropdown").getItem(keyPos);

			// All we want is the name:
			String	key	= (String) keyMap.get("name");
			this.curKey	= key;
			this.curKeyOffset = keyPos;
			this.curKeyEnharmonicOffset	= this.enharmonicPos[this.curKeyOffset];

			// Update Melody's key and rangeList selection:
			this.melody.setKey(this.curKey);
			this.melody.setRangeList();
			try
			{
				((ScrollableList)this.controlP5.getController("rangeDropdown"))
				.setItems(this.melody.getRangeList())
				.setValue(0f);
			} catch(ClassCastException cce) {
				throw new IllegalArgumentException("ModuleTemplate.controlEvent - keyDropdown: error setting rangeList ScrollableList.");
			} // catch
		} // if

		// Guide Tone Generator:
		if(controlEvent.getName().equals("guideToneButton"))
		{
			if(((Toggle)controlEvent.getController()).getBooleanValue())
			{

				//				this.controlP5.setAutoDraw(false);
				this.controlP5.getGroup("background").setVisible(true);
				this.controlP5.getGroup("background").bringToFront();
				this.controlP5.getController("guideToneButton").bringToFront();

			} else {

				//				this.controlP5.setAutoDraw(true);
				this.controlP5.getGroup("background").setVisible(false);
			}

			this.controlP5.getGroup("guideToneBackground").bringToFront();
			this.controlP5.getGroup("guideToneBackground").setVisible(((Toggle) controlEvent.getController()).getBooleanValue());

		} // Guide Tone Generator

		// ADSR Presets Scrollable List:
		if(controlEvent.getName().equals("adsrPresetsDropdown"))
		{
			int	adsrPos	= (int)controlEvent.getValue();

			System.out.println("adsrPos = " + adsrPos);

			if(adsrPos >= 0 && adsrPos < this.instrument.getADSRPresets().length)
			{
				this.instrument.setADSR(adsrPos);
			} else {
				throw new IllegalArgumentException("ModuleTemplate.controlEvent: adsrPos" + adsrPos + " is out of range.");
			}
		} // ADSR Presets

		// Range Octave Scrollable List:
		if(controlEvent.getName().equals("rangeDropdown"))
		{
			int	rangeOctave	= (int)controlEvent.getValue() + 3;

			if(rangeOctave >= 3 && rangeOctave <= 5)
			{
				this.rangeOctave	= rangeOctave;
			} else {
				throw new IllegalArgumentException("ModuleTemplate.controlEvent: rangeOctave " + rangeOctave + " is out of range.");
			}
		} // rangeDropdown

		// Shape Menu Button:
		if(controlEvent.getName() == "shapeMenuButton")
		{
			//open the menu
			//			this.shapeMenuIsOpen = true;

			//set the shape select list
			System.out.println(this.module.getShape().getShapeIndex());
			this.controlP5.getController("shapeSelect").setValue(this.module.getShape().getShapeIndex());

			//make the shape menu visible
			this.controlP5.getGroup("shapeMenuGroup")
			.setVisible(false);

			//hide the other controls
			//			this.controlP5.getGroup("sidebarGroup").setVisible(false);
			//			this.controlP5.setVisible(false);
			this.outsideButtonsCP5.setVisible(false);

			this.controlP5.getController("menuX").update();

			this.module.setShapeEditorRunning(true);
		}//shapeMenuButton

		// Shape Select dropdown:
		if(controlEvent.getName() == "shapeSelect")
		{
			this.module.getShape().setShapeIndex((int) this.controlP5.getController("shapeSelect").getValue());
		} // shape select

		// Input Select dropdown:
		if(controlEvent.getName() == "inputSelectDropdown")
		{
			// Save these colors: -- no longer necessary, since getColor() uses this.colors, too
/*			for(int i = 0; i < this.colorSelect.length; i++)
			{
				this.colors[this.currentInput][i]	= this.getColor(i);
			}
*/
			
			// Switch to newly selected input num:
			this.currentInput	= (int)controlEvent.getValue();
			System.out.println("currentInput = " + this.currentInput);
			// Turn off global:
			((Toggle)this.controlP5.getController("global")).setState(false);

			// Set the colorWheels to our new current input:
/*			for(int i = 0; i < this.specialColorsPos[this.currentInput].length; i++)
			{
				//				this.setColor(i, this.colors[this.currentInput][i], false);
				this.setColorSelectCW(i, this.colors[this.currentInput][i]);
				if(this.firstSpecialColorsCWId > 0)
				{
					this.setSpecialColorsCW(i, this.colors[this.currentInput][i]);
				}
			} // for - set ColorWheels

			this.fillHSBColors();
			*/
		} // input select dropdown

	} // controlEvent


	/**
	 * Called from MenuTemplate for Sliders that were added by the addSliderGroup() method
	 * and are connected to a Textfield (MenuTemplate has already updated the Textfield value).
	 */
	public void sliderEvent(int id, float val)
	{
		System.out.println("ModuleMenu: got sliderEvent with id " + id + " and val " + val);

		// Piano Threshold:
		if(id == this.pianoThresholdSliderId)
		{
			for(int i = 0; i < this.numInputs; i++)
			{
				this.pianoThreshold[i]	= (int)val;
				this.resetThresholds(i);
			}
		} // piano threshold Slider

		// Forte Threshold:
		if(id == this.forteThresholdSliderId)
		{
			for(int i = 0; i < this.numInputs; i++)
			{
				this.forteThreshold[i]	= (int)val;
				this.resetThresholds(i);
			}
		} // forte threshold Slider

		// Attack, Release, and Transition:
		if(id >= this.firstARTSliderId && id < (this.firstARTSliderId + 3))
		{
			//			int	pos	= (id / 2) - 1;
			int	pos	= id - this.firstARTSliderId;
			//			this.attackReleaseTransition[pos]	= val;

			if(global)
			{
				for(int i = 0; i < this.numInputs; i++)
				{
					this.setAttRelTranVal(pos, i, val);
				} // for
			} else {
				this.setAttRelTranVal(pos, this.currentInput, val);
			} // else - not global
		} // attack/release/transition

		// Hue/Saturation/Brightness modulate
		if(id >= this.firstHSBSliderId && id < (this.firstHSBSliderId + 3))
		{
			//			int pos = (id/2)-7;
			int pos = id - this.firstHSBSliderId;

			this.setHueSatBrightnessMod(pos, val);

			//					this.applyHSBModulate(this.hsbColors);
			this.applyHSBModulate();
		}//hsb mod

		// Red Modulate/Green Modulate/Blue Modulate:
		if(id >= this.firstRGBSliderId && id < (this.firstRGBSliderId + 3))
		{
			//			int	pos	= (id / 2) - 4;		
			int	pos	= id - this.firstRGBSliderId;	// red = 0, green = 1, blue = 2

			this.redGreenBlueMod[pos]	= val;
			this.applyRGBModulate();
			// TODO!!!
			//					this.applyColorModulate(this.legendColors, this.originalColors);
		} // red/green/blue mod

		if( id == this.bpmSliderId)
		{
			this.bpm	= Math.max(Math.min((int)val, 240), 0);
		}

		if(id == this.volumeSliderId)
		{
			this.instrument.setVolume(Math.max(Math.min(val, 5), 0));
		}

		if(id == this.shapeSizeSliderId)
		{
			this.shapeSize	= val;
		}

		// Saturation and Brightness Threshold and Percent Sliders:
		if( ( id >= this.firstSatBrightThreshSliderId ) && ( id < this.firstSatBrightThreshSliderId + 4 ) )
		{
			System.out.println("did get into this event of Sliders");
			int		arrayPos	= (id - this.firstSatBrightThreshSliderId /*- 1*/) / 2;
			// Percent Sliders
			if((id - this.firstSatBrightThreshSliderId) % 2 == 0)
			{
				System.out.println("percent Slider?");
				this.satBrightPercentVals[arrayPos]		= val;
				this.satBrightThresholdVals[arrayPos]	= this.controlP5.getValue("slider" + (id + 1));
				//				percentVal		= controlEvent.getValue();
			} else {
				System.out.println("threshold Slider?");
				// Threshold Sliders
				this.satBrightThresholdVals[arrayPos]	= val;
				this.satBrightPercentVals[arrayPos]		= this.controlP5.getValue("slider" + (id - 1));
			}
		} // Saturation and Brightness Threshold and Percent Sliders
	} // sliderEvent

	/**
	 * Called from MenuTemplate for Buttons that were added by the addColorWheelGroup() method and are 
	 * connected to a CW and CWTextfield (MenuTemplate has already updated the CW and Textfield values).
	 */
	public void buttonEvent(int id)
	{
		System.out.println("ModuleMenu: got buttonEvent with id " + id);

		this.fillHSBColors();

		// Reset whichever of the sliders is applicable:
		if(this.firstRGBSliderId > 0)
		{
			this.resetRGBSlidersTextfields();
			this.applyRGBModulate();
		}
		if(this.firstHSBSliderId > 0)
		{
			this.resetHSBSlidersTextfields();
			this.applyHSBModulate();
		}

		// If there are special colors, check to see if this color corresponds to one of them
		// in order to correctly set the boolean flags:
		if(this.firstSpecialColorsCWId > 0)
		{
			// Either do everything once for the currentInput or do it for all inputs:
			int	startHere;
			int	endBeforeThis;

			if(global)	
			{	
				startHere		= 0;
				endBeforeThis	= this.numInputs;
			} else {
				startHere		= this.currentInput;
				endBeforeThis	= this.currentInput + 1;
			}

			for(int i = startHere; i < endBeforeThis; i++)
			{

				// Position in colorSelect (if this CW is in colorSelect):
				int	colorPos	= (id % 100) - (this.firstColorSelectCWId % 100);
				if(colorPos >= 0 && colorPos < this.colorSelect.length /*&& 
					(this.arrayContains(this.specialColorsPos, colorPos) > -1) */)
				{
					this.fromColorSelect[i]	= true;
					//					this.fromSpecialColors[i]	= false;					
				} // if - this CW is in colorSelect
				else
				{
					// Check to see if this CW is a specialColors CW:
					colorPos	= (id % 100) - (this.firstSpecialColorsCWId % 100);
					if(colorPos >= 0 && colorPos < this.specialColorsPos.length)
					{
						//						this.fromSpecialColors[i]	= true;
						this.fromColorSelect[i]	= false;
					} // if
				} // else - for CWs not in colorSelect
			} // for

		} // if - specialColors
	} // buttonEvent

	/**
	 * Called from MenuTemplate for ColorWheels that were added by the addColorWheelGroup() method and are 
	 * connected to a Button and CWTextfield (MenuTemplate has already updated the Textfield value).
	 */
	public void colorWheelEvent(int id, Color color)
	{
		//		System.out.println("ModuleMenu: got colorWheelEvent with id " + id + " and color array " + color);

		// Either do everything once for the currentInput or do it for all inputs:
		int	startHere;
		int	endBeforeThis;
		int	colorPos;

		if(global)	
		{	
			startHere		= 0;
			endBeforeThis	= this.numInputs;
		} else {
			startHere		= this.currentInput;
			endBeforeThis	= this.currentInput + 1;
		}

		// if from ColorSelect or canvasColorSelect:
		if(( id >= this.firstColorSelectCWId && id < (this.firstColorSelectCWId + this.colorSelect.length ) ) 
				|| (id == this.canvasColorSelectId) )
		{
			colorPos	= id - this.firstColorSelectCWId;
		} else if(id >= this.firstSpecialColorsCWId && id < (this.firstSpecialColorsCWId + this.specialColorsPos[0].length))
		{
			// if from specialColors:
			colorPos	= this.specialColorsPos[this.currentInput][id - this.firstSpecialColorsCWId];
			
			System.out.println("controlEvent - specialColors: colorPos = " + colorPos);
			
			this.applySpecialColors();

		} else {
			throw new IllegalArgumentException("ModuleMenu.colorWheelEvent: CW with id " + id + " is not from colorSelect or specialColors;" + 
					"firstColorSelectCWID = " + this.firstColorSelectCWId + ".");
		}

		System.out.println("  ----- colorPos = " + colorPos);

		for(int i = startHere; i < endBeforeThis; i++)
		{
			// canvas color (does not affect notes):
			if( ( id % 100 ) == ( this.canvasColorSelectId % 100 ) )
			{
				this.canvasColor[i][0]	= color.getRed();
				this.canvasColor[i][1]	= color.getGreen();
				this.canvasColor[i][2]	= color.getBlue();

				// Ensures that the shape doesn't have to fade to this color if the amp is below the threshold:
				if(this.nowBelow[i])
				{
					this.curHue[i][0]	= color.getRed();
					this.curHue[i][1]	= color.getGreen();
					this.curHue[i][2]	= color.getBlue();
				}
			} else {
				// colors that are not canvasColor:
				this.colors[i][colorPos][0]	= color.getRed();
				this.colors[i][colorPos][1]	= color.getGreen();
				this.colors[i][colorPos][2]	= color.getBlue();
			} // else - not canvas



		} // for

		//		this.setColorStyle(this.curColorStyle)

		/*	
			// If there are special colors,
			// check to see if this color corresponds to one or is one:
			if(this.firstSpecialColorsCWId > 0)
			{
				if(colorPos >= 0 && colorPos < this.colorSelect.length)
				{
//					System.out.println("Color select color for colorPos " + colorPos);

					// Set this.colors:
					this.colors[this.currentInput][colorPos][0]	= color.getRed();
					this.colors[this.currentInput][colorPos][1]	= color.getGreen();
					this.colors[this.currentInput][colorPos][2]	= color.getBlue();

					// Check to see if this position corresponds to a special color
					// (and is from a colorSelect, to make sure that they don't just keep calling back and forth):
					int	specialColorsPos	= this.arrayContains(this.specialColorsPos[i], colorPos);
					if(specialColorsPos > -1 && this.fromColorSelect[i])
					{
						((ColorWheel)this.controlP5.getController("colorWheel" + (specialColorsPos + this.firstSpecialColorsCWId))).setRGB(color.getRGB());

					} // if - this CW connects to a specialColor							
				} // if - this CW is in colorSelect
				else
				{
					// Check to see if this CW is a specialColors CW:
					colorPos	= id - this.firstSpecialColorsCWId;
					if(colorPos >= 0 && colorPos < this.specialColorsPos[i].length)
					{
						System.out.println("SpecialColor for specialColorPos " + colorPos);

						// Make sure that they don't just keep calling back and forth:
						if(this.fromSpecialColors[i])
						{
							int	colorSelectPos	= this.specialColorsPos[i][colorPos];
//							((ColorWheel)this.controlP5.getController("colorWheel" + (colorSelectPos + this.firstColorSelectCWId))).setRGB(color.getRGB());
							this.setColor(colorSelectPos, new int[] { color.getRed(), color.getGreen(), color.getBlue() }, this.currentInput);

							this.setColorStyle(this.curColorStyle);

						} // if - only make one call per pair
					} // if
				} // else - for CWs not in colorSelect

			} // if - there are indeed specialColors
/*
			this.colors[this.currentInput][colorPos][0]	= color.getRed();
			this.colors[this.currentInput][colorPos][1]	= color.getGreen();
			this.colors[this.currentInput][colorPos][2]	= color.getBlue();
		 */

		//		} // for

	} // colorWheelEvent


	/**
	 * Method from ModuleTemplate01 that may not be applicable any longer.
	 * 
	 * Method to calculate the position in colors that should change.
	 * For example, the custom pitch color buttons each correspond to a note,
	 * whose position needs to be determined by curKey and majMinChrom,
	 * and Tonic, 2nd Color and 3rd Color buttons differ based on ColorStyle and scale quality.
	 * 
	 * @param id	int denoting the id of the current Event
	 * @return	the position in colors that is to be changed
	 */
	protected	int	calculateNotePos(int id)
	{		
		int	notePos;
		/*
		//		notePos	= ((id + 2) / 3) - 9;
		//		notePos	= (notePos + keyAddVal - 3 + this.colors.length) % this.colors.length;

		// Bring the id down to the nearest multiple of 3: 
		// (this way, the same equation can be used whether the call comes from a Button, ColorWheel, or Textfield.
		id	= id - (id % 3);

		// Dividing by 3 makes all the id's consecutive numbers (rather than multiples of 3);
		// subtracting 8 brings A to 0, A# to 1, B to 2, etc;
		// subtracting curKeyEnharmonicOffset adjusts for the particular key;
		// adding 12 and modding by 12 avoids negative numbers.
		notePos	= ( ( id / 3 ) - 8 - this.curKeyEnharmonicOffset + 12 ) % 12;
		 */
		// Modding by 100 brings all into the 0-100 range - button range;
		// subtracting curKeyEnharmonicOffset adjusts for the particular key;
		// adding 12 and modding by 12 avoids negative numbers.

		id	= id % 100;
		notePos	= ( id - (this.firstColorSelectCWId % 100) - this.curKeyEnharmonicOffset + 12) % 12;


		int	modCanvasId	= this.canvasColorSelectId % 100;
		if(id >= modCanvasId && id < (modCanvasId + 4))
		{			
			// Canvas:
			if(id == modCanvasId)	{
				throw new IllegalArgumentException("ModuleTemplate.calculateNotePos(int): id " + id + " should not be passed to this function, as it does not correspond to a note.");
			}

			// Tonic:
			if(id == (modCanvasId + 1))	{	notePos	= 0;	}

			// 2nd Color:
			if(id == (modCanvasId + 2))
			{
				if(this.curColorStyle == ModuleTemplate01.CS_DICHROM)
				{
					// for Dichromatic, this is the last color:
					notePos	= this.colorSelect.length - 1;
				} else if(this.curColorStyle == ModuleTemplate01.CS_TRICHROM)
				{
					// for tri, it's in the middle:
					if(this.majMinChrom == 2)
					{
						// chromatic:
						notePos	= 4;
					} else {
						// major and minor:
						// Position has to be 5, not 3, since colors is filled all the way and we just ignore
						// non-diatonic tones, so 5 actually corresponds to the mediant scale degree.
						//						notePos	= 3;
						notePos	= 5;
					} // maj/min/Chrom
				} // trichromatic
			} // 2nd color

			//3rd color:
			if(id == (modCanvasId + 3))
			{
				// only applies to trichromatic:
				if(this.curColorStyle == ModuleTemplate01.CS_TRICHROM)
				{
					if(this.majMinChrom == 2)
					{
						// chromatic:
						notePos	= 8;
					} else {
						// major and minor:
						// Position has to be 7, not 4, since colors is filled all the way and we just ignore
						// non-diatonic tones, so 7 actually corresponds to the dominant scale degree.
						//						notePos	= 4;
						notePos	= 7;
					} // maj/min/Chrom
				} // trichromatic
			} // 3rd color
		} // id > 63

		return	notePos;
	} // calculateNotePos


	/**
	 * Sets the Sliders and Textfields for RGB and HSB color modulate to 0
	 * and calls this.fillHSBColors().
	 */
	protected void resetModulateSlidersTextfields()
	{
		if(this.firstHSBSliderId > 0 && this.firstRGBSliderId > 0)
		{

			int	hsbId	= this.firstHSBSliderId;
			int	rgbId	= this.firstRGBSliderId;

			for(int i = 0; i < 3; i++)
			{
				this.controlP5.getController("slider" + hsbId).setValue(0);
				this.controlP5.getController("slider" + rgbId).setValue(0);
				hsbId	= hsbId + 1;
				rgbId	= rgbId + 1;
			} // for

			this.fillHSBColors();
		} else {
			System.out.println("ModuleTemplate.resetModulateSlidersTextfields: either HSB or RGB sliders have not yet been initialized " +
					"(firstHSBSliderId = " + this.firstHSBSliderId + " and firstRGBSliderId = " + this.firstRGBSliderId + ")");
		} // else - let the user know that we ignored this method call

	} // resetModulateSlidersTextfields

	/**
	 * Sets the RGB modulate Sliders and Textfields to 0
	 */
	protected void resetRGBSlidersTextfields()
	{
		if(this.firstRGBSliderId > 0)
		{
			int	rgbId	= this.firstRGBSliderId;

			for(int i = 0; i < 3; i++)
			{
				this.controlP5.getController("slider" + rgbId).setValue(0);
				rgbId	= rgbId + 1;
			} // for
		} else {
			System.out.println("ModuleTemplate.resetRGBSlidersTextfields: RGB sliders have not yet been initialized " +
					"(firstRGBSliderId = " + this.firstRGBSliderId + ")");
		} // else - let the user know that we ignored this method call
	} // resetRGBSlidersTextfields

	/**
	 * Sets the HSB modulate Sliders and Textfields to 0
	 */
	protected void resetHSBSlidersTextfields()
	{
		if(this.firstHSBSliderId > 0)
		{
			int	hsbId	= this.firstHSBSliderId;

			for(int i = 0; i < 3; i++)
			{
				this.controlP5.getController("slider" + hsbId).setValue(0);
				hsbId	= hsbId + 1;
			} // for
		} else {
			System.out.println("ModuleTemplate.resetHSBSlidersTextfields: HSB sliders have not yet been initialized " +
					"(firstHSBSliderId = " + this.firstHSBSliderId + ")");
		} // else - let the user know that we ignored this method call
	} // resetHSBSlidersTextfields


	/**
	 * Uses this.threshold, this.forteThreshold and this.curRangeSegments 
	 * to recalculate the length of and values within this.thresholds.
	 */
	private	void resetThresholds(int pos)
	{
		this.inputNumErrorCheck(pos);

		float	segmentValue;
		if(this.curRangeSegments == 1)
		{
			segmentValue	= this.pianoThreshold[pos];
		} else {
			segmentValue	= (this.forteThreshold[pos] - this.pianoThreshold[pos]) / (this.curRangeSegments - 1);
		}

		//		System.out.println("dynamic segment buttons: forteThreshold = " + this.forteThreshold + 
		//				"; segmentValue = " + segmentValue);

		//		this.thresholds	= new float[this.curRangeSegments];
		for(int i = 0; i < this.curRangeSegments; i++)
		{
			this.thresholds[pos][i]	= this.pianoThreshold[pos] + (int)segmentValue * i;
		} // for
	} // resetThresholds


	/**
	 * Given the name of a key (e.g., "A#", "Bb") and the quality (0 for major, 1 for minor, 2 for chromatic), 
	 * returns the appropriate scale (minor = natural minor).
	 * 
	 * @param key	String indicating the key of the scale
	 * @param majMinChrom	int indicating quality of the scale: 0 = major, 1 = minor, 2 = chromatic
	 * @return	String[] with the notes of this particular scale
	 */
	public String[] getScale(String key, int majMinChrom)
	{
		// find keyPos -- hey ! maybe I can just pass in keyPos.
		int	keyPos = this.arrayContains(this.allNotes, key);
		if(keyPos == -1) {
			throw new IllegalArgumentException("ModuleTemplate.getScale: key " + key + " is not a valid key.");
		}
		/*
		return this.getScale(keyPos, majMinChrom);
	} // getScale(String, int)

	public String[] getScale(int keyPos, int majMinChrom)
	{*/
		String[][] majorScales	= new String[][] {
			new String[] { "A", "B", "C#", "D", "E", "F#", "G#" },
			new String[] { "A#", "B#", "C##", "D#", "E#", "F##", "G##" },
			new String[] { "Bb", "C", "D", "Eb", "F", "G", "A" },
			new String[] { "B", "C#", "D#", "E", "F#", "G#", "A#" },
			new String[] { "C", "D", "E", "F", "G", "A", "B" },
			new String[] { "C#", "D#", "E#", "F#", "G#", "A#", "B#" },
			new String[] { "Db", "Eb", "F", "Gb", "Ab", "Bb", "C" },
			new String[] { "D", "E", "F#", "G", "A", "B", "C#" },
			new String[] { "D#", "E#", "F##", "G#", "A#", "B#", "C##" },
			new String[] { "Eb", "F", "G", "Ab", "Bb", "C", "D" },
			new String[] { "E", "F#", "G#", "A", "B", "C#", "D#" },
			new String[] { "F", "G", "A", "Bb", "C", "D", "E" },
			new String[] { "F#", "G#", "A#", "B", "C#", "D#", "E#" },
			new String[] { "Gb", "Ab", "Bb", "Cb", "Db", "Eb", "F" },
			new String[] { "G", "A", "B", "C", "D", "E", "F#" },
			new String[] { "G#", "A#", "B#", "C#", "D#", "E#", "F##" },
			new String[] { "Ab", "Bb", "C", "Db", "Eb", "F", "G" }
		}; // majorScales

		String[][] minorScales	= new String[][] {
			new String[] { "A", "B", "C", "D", "E", "F", "G" },
			new String[] { "A#", "B#", "C#", "D#", "E#", "F#", "G#" },
			new String[] { "Bb", "C", "Db", "Eb", "F", "Gb", "Ab" },
			new String[] { "B", "C#", "D", "E", "F#", "G", "A" },
			new String[] { "C", "D", "Eb", "F", "G", "Ab", "Bb" },
			new String[] { "C#", "D#", "E", "F#", "G#", "A", "B" },
			new String[] { "Db", "Eb", "Fb", "Gb", "Ab", "Bbb", "Cb" },
			new String[] { "D", "E", "F", "G", "A", "Bb", "C" },
			new String[] { "D#", "E#", "F#", "G#", "A#", "B", "C#" },
			new String[] { "Eb", "F", "Gb", "Ab", "Bb", "Cb", "Db" },
			new String[] { "E", "F#", "G", "A", "B", "C", "D" },
			new String[] { "F", "G", "Ab", "Bb", "C", "Db", "Eb" },
			new String[] { "F#", "G#", "A", "B", "C#", "D", "E" },
			new String[] { "Gb", "Ab", "Bbb", "Cb", "Db", "Ebb", "Fb" },
			new String[] { "G", "A", "Bb", "C", "D", "Eb", "F" },
			new String[] { "G#", "A#", "B", "C#", "D#", "E", "F#" },
			new String[] { "Ab", "Bb", "Cb", "Db", "Eb", "Fb", "Gb" }
		}; // majorScales


		if(keyPos > majorScales.length) {
			throw new IllegalArgumentException("ModuleTemplate.getScale(int, int): int param " + keyPos + " is greater than majorScales.length (" + majorScales.length + ").");
		}

		String[] result;

		if(majMinChrom == 0)
		{
			// major:
			result	= new String[7];
			for(int i = 0; i < result.length; i++)
			{
				result[i]	= majorScales[keyPos][i]; 
			} // for
		} else if (majMinChrom == 1) {
			// minor:
			result	= new String[7];
			for(int i = 0; i < result.length; i++)
			{
				result[i]	= minorScales[keyPos][i]; 
			} // for
		} else {
			// chromatic:
			result	= new String[12];

			// find whether scale should use sharps or flats:
			//			boolean	sharps	= true;
			int	notePos	= this.arrayContains(this.notesAtoGSharps, majorScales[keyPos][0]);
			if(notePos > -1) 
			{
				for(int i = 0; i < result.length; i++)
				{
					result[i]	= this.notesAtoGSharps[notePos];
					notePos	= (notePos + 1) % result.length;
				} // for
			} else {
				notePos	= this.arrayContains(this.notesAtoAbFlats, majorScales[keyPos][0]);
				for(int i = 0; i < result.length; i++)
				{
					result[i]	= this.notesAtoAbFlats[notePos];
					notePos	= (notePos + 1) % result.length;
				} // for
			}
		} // else - chromatic

		return result;

	} // getScale


	/**
	 * Updates the keyDropdown ScrollableList and sets the current key and all 
	 * connected variables: this.majMinChrom, this.scaleLength, this.curKey, this.keyAddVal.
	 * 
	 * @param key	String indicating the key (e.g., "D", "Eb")
	 * @param majMinChrom	int indicating quality of the scale: 0 = major, 1 = minor, 2 = chromatic
	 */
	public void setCurKey(String key, int majMinChrom)
	{
		// Check both sharps and flats, and take whichever one doesn't return -1:
		int	keyPos	= this.arrayContains(this.allNotes, key);

		if(keyPos == -1)	{
			throw new IllegalArgumentException("Module_01_02.setCurKey: " + key + " is not a valid key.");
		}

		//System.out.println("key = " + key + "; keyPos = " + keyPos);


		this.majMinChrom	= majMinChrom;
		this.scaleLength	= this.getScale(key, majMinChrom).length;

		if(this.controlP5.getController("keyDropdown") != null)
		{
			this.controlP5.getController("keyDropdown").setValue(keyPos);
		}

	} // setCurKey


	/**
	 * Used in draw for determining whether a particular scale degree is in the 
	 * major or minor scale;
	 * returns the position of the element if it exists in the array,
	 * or -1 if the element is not in the array.
	 * 
	 * @param array		String[] to be searched for the given element
	 * @param element	String whose position in the given array is to be returned.
	 * @return		position of the given element in the given array, or -1 
	 * 				if the element does not exist in the array.
	 */
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
	} // arrayContains

	/**
	 * Used in controlEvent for determining whether a given color is in specialColors;
	 * returns the position of the element if it exists in the array,
	 * or -1 if the element is not in the array.
	 * 
	 * @param array		String[] to be searched for the given element
	 * @param element	String whose position in the given array is to be returned.
	 * @return		position of the given element in the given array, or -1 
	 * 				if the element does not exist in the array.
	 */
	private int arrayContains(int[] array, int element) {
		if(array == null) {
			throw new IllegalArgumentException("Module_01_02.arrayContains(String[], String): array parameter is null.");
		}

		for (int i = 0; i < array.length; i++)
		{
			//    println("array[i] = " + array[i]);
			if (array[i] == element) {
				return i;
			} // if
		} // for

		return -1;
	} // arrayContains

	/**
	 *  Error checker; rejects numbers that are greater than or equal to the number of inputs or less than 0.
	 *
	 *  @param   inputNum  an int that is to be checked for suitability as an input line number.
	 *  @param   String    name of the method that called this method, used in the exception message.
	 */
	private void inputNumErrorCheck(int inputNum) {
		if (inputNum >= this.numInputs) {
			IllegalArgumentException iae = new IllegalArgumentException("ModuleMenu.inputNumErrorCheck(int): int parameter " + inputNum + " is greater than " + this.numInputs + ", the number of inputs.");
			iae.printStackTrace();
			throw iae;
		}
		if (inputNum < 0) {
			IllegalArgumentException iae = new IllegalArgumentException("ModuleMenu.inputNumErrorCheck(int): int parameter is " + inputNum + "; must be 1 or greater.");
			iae.printStackTrace();
			throw iae;
		}
	} // inputNumErrorCheck

	public int[][][] getColors()
	{
		return this.colors;
	} // getColors


	/**
	 * Returns the current color of the ColorWheel in the given position in this.colorSelect as an int.
	 * 
	 * @param colorPos	Position of color; must be from 0 to (this.colorSelect.length - 1)
	 * @return	the current color of the ColorWheel in the given position in this.colorSelect as an int
	 */
	public int[] getColor(int colorPos)
	{
		return this.getColor(colorPos, this.currentInput);
	}

	public int[] getColor(int colorPos, int inputNum)
	{
		if(colorPos < 0 || colorPos >= this.colorSelect.length) {
			throw new IllegalArgumentException("ModuleTemplate.getColor: in parameter " + colorPos + 
					" is out of bounds; must be within 0 to " + (this.colorSelect.length - 1));
		} // error checking

		return this.colors[inputNum][colorPos];
		/*
		Color	curColor	= new Color(this.colorSelect[colorPos].getRGB());

		// Making a new array rather than using getComponents so that we will have ints, not floats:
		return	new int[] { curColor.getRed(), curColor.getGreen(), curColor.getBlue() };
		 */
	} // getColor

	public void setColorSelectCW(int colorPos, int[] color)
	{
		// Error checking:
		if(colorPos < 0 || colorPos >= this.colorSelect.length) {
			throw new IllegalArgumentException("ModuleMenu.setColorSelectCW: int parameter " + colorPos + 
					" is out of bounds; must be between 0 and " + (this.colorSelect.length - 1));
		}
		if(color == null) {
			throw new IllegalArgumentException("ModuleMenu.setColorSelectCW: float[] parameter is null.");
		}

		// Only do this if colorSelect CWs have actually been initialized:
		if(this.firstColorSelectCWId > 0) 
		{
			//			throw new IllegalArgumentException("ModuleTemplate.setColor: firstColorSelectCWId == -1; did not attempt to set the ColorWheel.");

			// Adjust for out-of-range parameters
			for(int i = 0; i < color.length; i++)
			{
				int	originalColor	= color[i];
				color[i]	= Math.min(Math.max(color[i], 0), 255);

				// Let the user know if you made an adjustment:
				if(originalColor != color[i]) {
					System.out.println("ModuleMenu.setColorSelectCW: adjusted position " + i + " of color " + colorPos + " from " + originalColor + " to " + color[i] + ".");
				}
			} // for - color adjust

			int	colorInt	= (new Color(color[0], color[1], color[2])).getRGB();
			ColorWheel	cw	= ((ColorWheel)this.controlP5.getController("colorWheel" + (this.firstColorSelectCWId + colorPos)));

			// Set the colorSelect ColorWheel:
			if(cw.getRGB() != colorInt)
			{
				System.out.println("--- controlEvent: setting ColorWheel");
				cw.setRGB(colorInt);
			} // if
			else {
				System.out.println("----- controlEvent: not setting ColorWheel");
			}
		} else {
			System.err.println("ModuleMenu.setColor: firstColorSelectCWId == " + this.firstColorSelectCWId + "; did not attempt to set the ColorWheel at " + colorPos + ".");
		} // if/else
	} // setCSColorWheel

	public void setSpecialColorsCW(int colorPos, int[] color)
	{
		// Error checking:
		if(colorPos < 0 || colorPos >= this.specialColorsPos[this.currentInput].length) {
			throw new IllegalArgumentException("ModuleMenu.setSpecialColorsCW: int parameter " + colorPos + 
					" is out of bounds; must be between 0 and " + (this.specialColorsPos[this.currentInput].length - 1));
		}
		if(color == null) {
			throw new IllegalArgumentException("ModuleMenu.setSpecialColorsCW: float[] parameter is null.");
		}
		
		System.out.println("\t---- this.specialColorsPos[this.currentInput].length = " + this.specialColorsPos[this.currentInput].length + "; problem specialColor pos = " + colorPos);

		// Only do this if colorSelect CWs have actually been initialized:
		if(this.firstSpecialColorsCWId > 0) 
		{
			// Adjust for out-of-range parameters
			for(int i = 0; i < color.length; i++)
			{
				int	originalColor	= color[i];
				color[i]	= Math.min(Math.max(color[i], 0), 255);

				// Let the user know if you made an adjustment:
				if(originalColor != color[i]) {
					System.out.println("ModuleMenu.setSpecialColorsCW: adjusted position " + i + " of color " + colorPos + " from " + originalColor + " to " + color[i] + ".");
				}
			} // for - color adjust

			int	colorInt	= (new Color(color[0], color[1], color[2])).getRGB();
			ColorWheel	cw	= ((ColorWheel)this.controlP5.getController("colorWheel" + (this.firstSpecialColorsCWId + colorPos)));

			// Set the colorSelect ColorWheel:
			if(cw.getRGB() != colorInt)
			{
				cw.setRGB(colorInt);
			} // if
		} else {
			System.err.println("ModuleTemplate.setSpecialColorsCW: firstSpecialColorsCWId == " + this.firstSpecialColorsCWId + "; did not attempt to set the ColorWheel at " + colorPos + ".");
		} // if/else
	} // setSpecialColorsCW

	/**
	 * Sets the ColorWheel at colorPos to the given color
	 * 
	 * @param colorPos	position of ColorWheel in this.colorSelect
	 * @param color	rgb color
	 */
	public void setColor(int colorPos, int[] color, boolean fromColorWheel)
	{
		this.setColor(colorPos, color, this.currentInput, fromColorWheel);
	} // setColor(int, int[])

	public void setColor(int colorPos, Color color, int inputNum, boolean fromColorWheel)
	{
		this.setColor(colorPos, new int[] { color.getRed(), color.getGreen(), color.getBlue() }, inputNum, fromColorWheel);
	}

	public void setColor(int colorPos, int[] color, int inputNum, boolean fromColorWheel)
	{
		// Error checking:
		if(colorPos < 0 || colorPos >= this.colorSelect.length) {
			throw new IllegalArgumentException("ModuleTemplate.setColor: int parameter " + colorPos + 
					" is out of bounds; must be between 0 and " + (this.colorSelect.length - 1));
		}
		if(color == null) {
			throw new IllegalArgumentException("ModuleTemplate.setColor: float[] parameter is null.");
		}

		// Only do this if colorSelect CWs have actually been initialized:
		//		if(this.firstColorSelectCWId > 0) 
		//		{
		//			throw new IllegalArgumentException("ModuleTemplate.setColor: firstColorSelectCWId == -1; did not attempt to set the ColorWheel.");

		// Adjust for out-of-range parameters
		for(int i = 0; i < color.length; i++)
		{
			int	originalColor	= color[i];
			color[i]	= Math.min(Math.max(color[i], 0), 255);

			// Let the user know if you made an adjustment:
			if(originalColor != color[i]) {
				System.out.println("ModuleTemplate.setColor: adjusted position " + i + " of color " + colorPos + " from " + originalColor + " to " + color[i] + ".");
			}
		} // for - color adjust

		int	colorInt	= (new Color(color[0], color[1], color[2])).getRGB();

		// Only set the colorSelect ColorWheel if this input is current (or we are global)
		// and this is not from a CW event:
		if( ( inputNum == this.currentInput || global ) 
				&& !this.fromColorSelect[inputNum] 
						&& !fromColorWheel)
		{
			//				((ColorWheel)this.controlP5.getController("colorWheel" + (this.firstColorSelectCWId + colorPos))).setRGB(colorInt);
		} // if - set colorSelect CW

		// Set the specialColors ColorWheel if this colorPos corresponds to a specialColor 
		// and this didn't come from a specialColors CW:
		if(this.arrayContains(this.specialColorsPos[inputNum], colorPos) > -1 
				&& this.fromColorSelect[inputNum]
						&& !fromColorWheel)
		{
			int specialColorsPos	= this.arrayContains(this.specialColorsPos[inputNum], colorPos);
			//			((ColorWheel)this.controlP5.getController("colorWheel" + (this.firstSpecialColorsCWId + specialColorsPos))).setRGB(colorInt);
		} // if - set specialColors CW

		// If global, set the color at this position for all inputs:
		if(global)
		{
			for(int i = 0; i < this.colors.length; i++)
			{
				this.colors[i][colorPos][0]	= color[0];
				this.colors[i][colorPos][1]	= color[1];
				this.colors[i][colorPos][2]	= color[2];
			}
		} else {
			// Otherwise, just set it for the given input:
			this.colors[inputNum][colorPos][0]	= color[0];
			this.colors[inputNum][colorPos][1]	= color[1];
			this.colors[inputNum][colorPos][2]	= color[2];
		}
		//		} else {
		//			System.err.println("ModuleTemplate.setColor: firstColorSelectCWId == " + this.firstColorSelectCWId + "; did not attempt to set the ColorWheel at " + colorPos + ".");
		//		} // if/else
	} // setColor

	/*
	public int getCheckpoint()				{	return this.checkpoint;		}

	public void setCheckpoint(int newVal)	{	this.checkpoint	= newVal;	}

	public float getPianoThreshold()				{	return this.pianoThreshold;		}

	public void setPianoThreshold(int newVal)	{	this.pianoThreshold	= newVal;	}
	 */

	public int[][] getCurHue()				{	return this.curHue;	}
	
	public int getCurColorStyle()			{	return this.curColorStyle;	}

	/**
	 * Returns the current attack, release, or transition value indicated by the parameter:
	 * 0 for attack, 1 for release, and 2 for transition
	 * 
	 * @param attRelTranPos	int indicating attack (0), release (1), or transition (2)
	 * @return	current attack, release, or transition value
	 */
	/*	public float getAttRelTranVal(int attRelTranPos)
	{
		return this.attRelTranVals[attRelTranPos];
	}
	 */
	/**
	 * Sets either attack, release, or transition to the given value
	 * 
	 * @param position 0 for attack, 1 for release, or 2 for transition
	 * @param val	value to set either attack, release, or transition
	 */
	public void setAttRelTranVal(int position, int inputNum, float val) {
		if(position < 0 || position > this.attRelTranVals.length) {
			throw new IllegalArgumentException("ModuleTemplate.setAttRelTranVal: position " + position + " is out of range; must be 0, 1, or 2.");
		} // error checking

		this.attRelTranVals[inputNum][position]	= val;
	}

	/**
	 * Sets either global hue, saturation, or brightness modulate to the given value
	 * 
	 * @param position 0 for hue, 1 for saturation, or 2 for brightness
	 * @param val	value to set either hue, saturation, or brightness
	 */
	public void setHueSatBrightnessMod(int position, float val) {
		if(position < 0 || position > this.hueSatBrightnessMod.length) {
			throw new IllegalArgumentException("ModuleTemplate.setHueSatBrightnessMod: position " + position + " is out of range; must be 0, 1, or 2.");
		} // error checking

		this.hueSatBrightnessMod[position]	= val;
	}

//	public int getLeftEdgeX()				{	return this.leftEdgeX;	}

	/**
	 * Getter for showScale instance variable
	 * @return	this.showScale
	 */
	public boolean isShowScale() {
		return this.showScale;
	}

	public void setShowScale(boolean showScale) {
		this.showScale = showScale;
	}

	public int[][] getCanvasColor() {
		return this.canvasColor;
	}

	public float getRedModulate() {
		return this.redGreenBlueMod[0];
	}

	public float getGreenModulate() {
		return this.redGreenBlueMod[1];
	}

	public float getBlueModulate() {
		return this.redGreenBlueMod[2];
	}


	public int getCurKeyEnharmonicOffset() {
		return curKeyEnharmonicOffset;
	}

	public String getCurKey() {
		return this.curKey;
	}

	public int getMajMinChrom() {
		return this.majMinChrom;
	}

	public float getShapeSize() {
		return this.shapeSize;
	}

	/**
	 * Getter for this.thresholds
	 * 
	 * @return	this.thresholds instance variable
	 */
	public int[][] getThresholds()
	{
		return this.thresholds;
	}

	/**
	 * Getter for this.curRangeSegments
	 * 
	 * @return	this.curRangeSegments instance variable
	 */
	public int getCurRangeSegments() {
		return this.curRangeSegments;
	} // getCurRangeSegments

	/**
	 * communicates with keyPressed event in draw() of driver
	 * shows menu button on key press
	 * added 1/26/17 Elena Ryan
	 */
	public void setMenuVal() {
		//this.menuVis = true;	
		((Toggle)this.controlP5.getController("menuButton")).setState(false);
		this.outsideButtonsCP5.getController("hamburger").setVisible(true);
	}//set menu val


	public int getSliderHeight() {
		return this.sliderHeight;
	}
	
	public ControlP5 getOutsideButtonsCP5()
	{
		return this.outsideButtonsCP5;
	}

} // ModuleTemplate
