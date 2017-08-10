package core;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.Map;

import controlP5.Button;
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
import processing.core.PApplet;
import processing.core.PImage;

/**
 * July 2017
 * 
 * Abstract class for all the ModuleTemplate (sidebar) components that are needed for more than one module.
 * 
 * @author Emily Meuer
 *
 */
public abstract class ModuleTemplate implements ControlListener  {


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

	protected	ControlP5	sidebarCP5;
	private		String		sidebarTitle;

	private     float		menuWidth;
	private     boolean  	menuIsOpen = false;

	protected	int			leftAlign;
	protected	int			leftEdgeX;

	protected	int			labelX;
	protected	int			labelWidth;
	protected	int			spacer;
	protected	int			textfieldWidth;
	protected	int			sliderWidth;
	protected	int			sliderHeight;

	protected	static	int	CS_RAINBOW	= 1;
	protected	static	int	CS_DICHROM	= 2;
	protected	static	int	CS_TRICHROM	= 3;
	protected	static	int	CS_CUSTOM	= 4;
	protected	int	curColorStyle;

	/**	false before dichromatic has been called for the first time; true following that.	*/
	protected	boolean	dichromFlag;

	/**	false before trichromatic has been called for the first time; true following that.	*/
	protected	boolean	trichromFlag;

	/**	ColorWheels that hold all the colors for the Module; replaces int[][] this.colors	*/
	protected	ColorWheel[]	colorSelect;

	/**	Current hue (as opposed to the goal hue, which may not have been reached)	 */
	private	int[]			curHue;

	/**	Hue that corresponds to the current sound, but to which curHue may not yet have faded	*/
	private	int[]			goalHue;

	/**	The color when sound is below the threshold	*/
	protected	int[]		canvasColor;

	/**	The amount that must be added every 50 or so milliseconds to fade to the goal color	*/
	private	int[]			colorAdd;
	
	// TODO initialize elsewhere:
//	private float[]			colorAddDecimals	= new float[3];

	/**	The difference between the R, G, and B values of 2 colors that are being faded between	*/
	private	int[]			colorRange;

	/**	The current colors which hsb sliders are altering.	*/
	protected int[][]   hsbColors;

	/**	Flags whether the curHue R, G, and B values have come within an acceptable range of the goalHue	*/
	private boolean[]	colorReachedArray;

	/**	True if all values in the colorReachedArray are true; used to determine fade speed (whether this is attack, release, or transition)	*/
	private	boolean		colorReached;

	/**	Input from which the class will get all its audio data	*/
	protected	Input	input;

	/**	Volume below which input will be ignored	*/
	protected float	threshold;

	/**	The minimum value for threshold Sliders	*/
	protected	float	minThreshold;
	
	/**	The highest amplitude threshold	*/
	protected	float	forteThreshold;

	/**	Holds the values of the saturation threshold and brightness threshold Sliders, respectively	*/
	protected	float[] satBrightThresholdVals;

	/**	Hodls the values of the saturation percent and brightness percent threshold Sliders, respectively	*/
	protected	float[]	satBrightPercentVals;

	/**	Flag denoting whether or not the current volume is below the threshold	*/
	private boolean	nowBelow;

	/**	Attack, Release, or Transition - 0 = attack, 1 = release, 2 = transition	*/
	private	int		attRelTranPos;

	/**	For a timer that allows attack/release/transition sliders to be time-based	*/
	private int 	checkpoint;

	/**	Stores the values of the attack, release, and transition sliders	*/
	private	float[]	attRelTranVals;

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
	protected	int[]	specialColorsPos;

	/**	True when a ColorWheel Button is a colorSelect Button; false if specialColors	*/
	protected	boolean	fromColorSelect;

	/**	True when a ColorWheel Button is a specialColors Button; false if colorSelect	*/
	protected	boolean	fromSpecialColors;

	/**	
	 * All of the following are id's that have the potential to be used in controlEvent;
	 * if a child class adds any of these components (e.g., a canvasColorSelect Button or RGB mod sliders),
	 * they should initiate the corresponding one of these variables to the id of either the Button or Slider in question.
	 */
	protected	int	canvasColorSelectId		= -1;
	protected	int	firstColorModSliderId	= -1;
	protected	int	firstColorSelectCWId	= -1;
	protected	int	firstSpecialColorsCWId	= -1;
	protected	int	lastColorSelectId		= -1;
	protected	int	thresholdSliderId		= -1;
	protected	int	firstARTSliderId		= -1;
	protected	int	firstHSBSliderId		= -1;
	protected	int	firstRGBSliderId		= -1;
	protected	int	bpmSliderId				= -1;
	protected	int	volumeSliderId			= -1;
	protected	int	shapeSizeSliderId		= -1;
	protected	int	firstRangeSegmentsId	= -1;

	/**	The id used to identify the Color/Brightness/Saturation threshold sliders	 */
	protected	int	firstThresholdSliderId	= -1;

	/**	DecimalFormat used for rounding the text corresponding to Sliders and Colorwheels.	*/
	protected	DecimalFormat	decimalFormat	= new DecimalFormat("#.##");

	/**
	 * The following are id's that are used within the add____ methods to keep id numbering consistent.
	 * They are initially set to 0 (nextSliderId), 100 (nextSTextfieldId), 200 (nextButtonId), 
	 * 300 (nextColorWheelId), 400 (nextCWTextfieldId) and 500 (nextToggleId), and incremented as Controllers are added.
	 */
	protected	int	nextSliderId;
	protected	int	nextSTextfieldId;	// Textfield next to a slider
	protected	int	nextButtonId;	// for Buttons that open a ColorWheel
	protected	int	nextColorWheelId;		// ColorWheels
	protected	int	nextCWTextfieldId;	// Textfield under a ColorWheels
	protected	int	nextToggleId;

	/**
	 * Constructor
	 * 
	 * @param parent	PApplet used to draw, etc.; will instantiate this.parent instance var
	 * @param input		Input for all audio input; will instantiate this.input
	 * @param sidebarTitle	String designating the title of the module to which this template corresponds
	 */
	public ModuleTemplate(PApplet parent, Input input, String sidebarTitle, int totalNumColorItems)
	{
		this.parent			= parent;
		this.input			= input;
		this.sidebarTitle	= sidebarTitle;

		this.leftAlign	= (this.parent.width / 3) / 4;
		this.leftEdgeX	= 0;

		this.labelX			= 10;
		this.labelWidth		= 70;
		this.spacer			= 5;
		this.textfieldWidth	= 40;
		this.sliderWidth	= 170;
		this.sliderHeight	= 20;

		// TODO: this might run into problems when we adjust for 5-8:
		this.colorSelect		= new ColorWheel[totalNumColorItems];
		this.curHue				= new int[3];
		this.goalHue			= new int[3];
		this.canvasColor		= new int[] { 1, 0, 0 };	// If this is set to rgb(0, 0, 0), the CW gets stuck in grayscale
		this.colorAdd			= new int[3];
		this.colorRange			= new int[3];

		this.colorReachedArray	= new boolean[] { false, false, false };
		this.colorReached		= false;
		this.nowBelow			= false;

		this.dichromFlag	= false;
		this.trichromFlag	= false;

		this.attRelTranPos	= 0;	// 0 = attack, 1 = release, 2 = transition
		this.attRelTranVals	= new float[] {		200, 200, 200	};	// attack, release, transition all begin at 200 millis
		this.hueSatBrightnessMod        = new float[3];
		this.hueSatBrightPercentMod		= new float[3];

		this.satBrightThresholdVals	= new float[2];
		this.satBrightPercentVals	= new float[2];

		this.checkpoint		= this.parent.millis() + 100;

		this.threshold		= 10;

		this.sidebarCP5		= new ControlP5(this.parent);
		this.sidebarCP5.addListener((ControlListener)this);


		this.melody			= new Melody(this.parent, this.input);
		this.instrument		= new Instrument(this.parent);
		this.curKey			= "A";
		this.bpm			= 120;
		this.majMinChrom	= 2;	// chromatic
		this.rangeOctave	= 3;

		this.nextSliderId		= 0;
		this.nextSTextfieldId	= 100;
		this.nextButtonId		= 200;
		this.nextColorWheelId	= 300;
		this.nextCWTextfieldId	= 400;
		this.nextToggleId		= 500;

		this.initModuleTemplate();
	} // constructor

	/**
	 * Called at the end of the constructor to add the sidebar title,
	 * "outside buttons" (hamburger and play/pause/stop cluster)
	 * and "hide buttons" (top row of sidebar, for hiding Scale, PlayButton, MenuButton).
	 */
	private void initModuleTemplate()	
	{
		this.sidebarCP5.addGroup("sidebarGroup")
		.setBackgroundColor(this.parent.color(0))
		.setSize(this.parent.width / 3, this.parent.height + 1)
		.setVisible(false);


		Color	transparentBlack	= new Color(0, 0, 0, 200);
		int		transBlackInt		= transparentBlack.getRGB();

		this.sidebarCP5.addBackground("background")
		.setPosition(0, 0)
		.setSize(this.parent.width / 3, this.parent.height)
		.setBackgroundColor(transBlackInt)
		.setGroup("sidebarGroup")
		.setVisible(false);

		// Add play button, hamburger and menu x:
		this.addOutsideButtons();

		// Add Menu and Title labels (after menuX, because it gets its x values from that):
		ControlFont	largerStandard	= new ControlFont(ControlP5.BitFontStandard58, 13);

		this.sidebarCP5.addTextlabel("title")
		.setGroup("sidebarGroup")
		.setPosition(this.leftAlign, 5)
		.setFont(largerStandard)
		//			.setFont(this.parent.createFont("Consolas", 12, true))	// This is so blurry....
		.setValue(this.sidebarTitle);

		float	menuXX		= this.sidebarCP5.getController("menuX").getPosition()[0];
		float	menuWidth	= this.sidebarCP5.getController("menuX").getWidth();

		this.sidebarCP5.addTextlabel("menu")
		.setPosition(menuXX + menuWidth + 3, 10)
		.setHeight(15)
		.setGroup("sidebarGroup")
		.setValue("Menu");

		this.addHideButtons(26);

	} // initModuleTemplate

	/**
	 * Adds the Buttons, ColorWheels and Textfields for selecting custom colors.
	 * 
	 * @param yVals	yValue(s) for the row or rows of Buttons
	 * @param buttonLabels	String[] of labels for the color select Buttons; 
	 * 						length of this array determines the number of colorSelect Buttons;
	 * 						if including canvas in this row, the first element of this array must be "Canvas"
	 */
	protected void addColorSelect(int[] yVals, String[] buttonLabels, String labelText, boolean canvas)
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
		// the "- (10 / buttonsPerRow)" adds a 10 pixel space at the end of the row:
		int		buttonWidth		= (((this.parent.width / 3) - this.leftAlign) / buttonsPerRow) - this.spacer - (10 / buttonsPerRow);
		int[]	xVals	= new int[buttonsPerRow];
		for(int i = 0; i < xVals.length; i++)
		{
			xVals[i]	= this.leftAlign + ((buttonWidth + this.spacer) * i);
		}

		this.sidebarCP5.addTextlabel("colorSelectLabel")
		.setPosition(labelX, yVals[0] + 4)
		.setGroup("sidebarGroup")
		.setValue(labelText);

		// Loop through all
		for(int i = 0; i < yVals.length; i++)
		{
			// Loop through each row
			for(int j = 0; j < xVals.length; j++)
			{
				if(i == 0 && j == 0 && canvas)
				{
					this.addColorWheelGroup(xVals[j], yVals[i], buttonWidth, buttonLabels[buttonLabelPos], this.canvasColor);
				} else {
					this.colorSelect[colorSelectPos]	= (ColorWheel)(this.addColorWheelGroup(xVals[j], yVals[i], buttonWidth, buttonLabels[buttonLabelPos], this.rainbowColors[this.majMinChrom][colorSelectPos]))[1];
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
	protected void addOutsideButtons()
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

		this.sidebarCP5.addToggle("play")
		.setPosition(playX, playY)
		.setImages(images)
		.updateSize();

		this.sidebarCP5.addToggle("pause")
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
		this.sidebarCP5.addButton("hamburger")
		.setPosition(hamburgerX, hamburgerY)
		.setImage(hamburger)
		.setClickable(true)
		.updateSize();

		int	menuXX			= 5;
		int	menuXY			= 5;
		int	menuXWidth		= 15;

		PImage	menuX	= this.parent.loadImage("menuX.png");
		menuX.resize(menuXWidth, 0);
		this.sidebarCP5.addButton("menuX")
		.setPosition(menuXX, menuXY)
		.setImage(menuX)
		.setGroup("sidebarGroup")
		.updateSize()
		.bringToFront();

		this.menuWidth = this.sidebarCP5.getController("menuX").getWidth();
	} // addOutsideButtons

	
	/**
	 * Adds "hide buttons" - hide Menu, hide Play Button, and hide Scale
	 * 
	 * @param hideY	y value at which the row will be displayed
	 */
	protected void addHideButtons(int	hideY)
	{
		int	hideWidth   = 69;
		int hideSpace	= 4;

		int	labelX		= 10;
		int	playButtonX	= this.leftAlign;
		int	menuButtonX	= this.leftAlign + hideWidth + hideSpace;
		int	scaleX		= this.leftAlign + (+ hideWidth + hideSpace) * 2;

		String[]	names	= new String[] { 
				"playButton",
				"menuButton",
				"legend"
		};
		String[]	labels	= new String[] {
				"Play Button",
				"Menu Button",
				"Scale"
		};
		int[]	xVals	= new int[] {
				playButtonX,
				menuButtonX,
				scaleX
		};

		this.sidebarCP5.addTextlabel("hide")
		.setPosition(labelX, hideY + 4)
		.setGroup("sidebarGroup")
		.setValue("Hide");

		for(int i = 0; i < names.length; i++)
		{
			this.sidebarCP5.addToggle(names[i])
			.setPosition(xVals[i], hideY)
			.setWidth(hideWidth)
			.setGroup("sidebarGroup");
			this.sidebarCP5.getController(names[i]).getCaptionLabel().set(labels[i]).align(ControlP5.CENTER, ControlP5.CENTER);
		}

		this.showScale = true;
	} // addHideButtons


	/**
	 * Method called during initialization to instatiate the Threshold, Attack, Release,
	 * and Transition sliders.
	 * 
	 * Slider id numbers are from 0-99, and their corresponding Textfields are (sliderId + 100) - all between 100 and 199.
	 * Names are based on ids; format: "slider" OR "textfield + [id]
	 *  - thresholdSlider	= "slider0", thresholdTF	= "textfield100"
	 *  - attackSlider	= "slider1", attackTF	= "textfield101"
	 *  - releaseSlider	= "slider2", releaseTF	= "textfield102"
	 *  - transitionSlider	= "slider3", transitionTF	= "textfield103"
	 * 
	 * @param thresholdY	y value of the Threshold slider group
	 * @param attackY	y value of the Attack slider group
	 * @param releaseY		y value of the Release slider group
	 * @param transitionY	y value of the Transition slider group
	 */
	protected void addSliders(int thresholdY, int attackY, int releaseY, int transitionY)
	{
		int	labelX			= 10;
		int	labelWidth		= 70;

		int	sliderWidth		= 170;

		int	spacer			= 5;
		int	tfWidth			= 40;

		String[]	names	= new String[] {
				"thresholdLabel",
				"attackLabel",
				"releaseLabel",
				"transitionLabel"
		}; // names

		String[]	labels	= new String[] {
				"Threshold",
				"Attack",
				"Release",
				"Transition"
		}; // labels

		int[]		yVals	= new int[] {
				thresholdY,
				attackY,
				releaseY,
				transitionY
		}; // yVals

		//		int	id	= 0;
		int	lowRange;
		int	highRange;
		int	startingValue;

		this.thresholdSliderId	= this.nextSliderId;
		this.firstARTSliderId	= this.nextSliderId + 1;

		for(int i = 0; i < 4; i++)
		{
			this.sidebarCP5.addLabel(names[i])
			.setPosition(labelX, yVals[i] + 4)
			.setWidth(labelWidth)
			.setGroup("sidebarGroup")
			.setValue(labels[i]);

			// Threshold has its own range:
			if(i == 0)
			{
				lowRange		= 2;
				highRange		= 100;
				startingValue	= 10;
			} else {
				lowRange		= 100;
				highRange		= 3000;
				startingValue	= 200;

			}

			this.sidebarCP5.addSlider("slider" + this.nextSliderId)
			.setPosition(this.leftAlign, yVals[i])
			.setSize(this.sliderWidth, this.sliderHeight)
			.setRange(lowRange, highRange)
			.setValue(startingValue)
			.setSliderMode(Slider.FLEXIBLE)
			.setLabelVisible(false)
			.setGroup("sidebarGroup")
			.setId(this.nextSliderId);

			this.nextSliderId	= this.nextSliderId + 1;

			this.sidebarCP5.addTextfield("textfield" + this.nextSTextfieldId)
			.setPosition(this.leftAlign + sliderWidth + spacer, yVals[i])
			.setSize(tfWidth, this.sliderHeight)
			.setText(this.sidebarCP5.getController("slider" + (this.nextSTextfieldId - 100)).getValue() + "")
			.setAutoClear(false)
			.setGroup("sidebarGroup")
			.setId(this.nextSTextfieldId)
			.getCaptionLabel().setVisible(false);

			this.nextSTextfieldId	= this.nextSTextfieldId + 1;

		} // for

	} // addSliders
	
	/**
	 * Adds the attack, release, and transition Sliders.
	 * 
	 * @param attackY	y value for attack Slider
	 * @param releaseY	y value for release Slider
	 * @param transitionY	y value for transition Slider
	 */
	protected void addARTSliders(int attackY, int releaseY, int transitionY)
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
			this.addSliderGroup(yVals[i], labels[i], 100, 3000, 200);
		}
	} // addARTSliders
	
	/**
	 * Adds the piano (minimum) threshold Slider.
	 * 
	 * @param yVal	y value of Slider
	 */
	protected void addPianoThresholdSlider(int yVal)
	{
		this.addSliderGroup(yVal, "Piano Threshold", 2, 100, 10);
	} // addPianoThresholdSlider


	/**
	 * Method called during instantiation to initialize the key selector drop-down menu (ScrollableList)
	 * and major/minor/chromatic selection buttons.
	 * 
	 * @param keyY	y value of the menu and buttons.
	 */
	protected void addKeySelector(int	keyY)
	{

		int	labelX			= 10;

		int	listWidth		= 30;
		int	spacer			= 5;

		int	toggleWidth		= 38;
		int	buttonWidth		= 52;
		int	majorX			= this.leftAlign + listWidth + spacer;
		int	minorX			= this.leftAlign + listWidth + spacer + (toggleWidth + spacer);
		int	chromX			= this.leftAlign + listWidth + spacer + ((toggleWidth + spacer) * 2);
		int	guideToneX		= this.leftAlign + listWidth + spacer + ((toggleWidth + spacer) * 3);


		// "Key" Textlabel
		this.sidebarCP5.addTextlabel("key")
		.setPosition(labelX, keyY + 4)
		.setGroup("sidebarGroup")
		.setValue("Key");


		// "Letter" drop-down menu (better name?)
		this.sidebarCP5.addScrollableList("keyDropdown")
		.setPosition(this.leftAlign, keyY)
		.setWidth(listWidth)
		.setBarHeight(18)
		.setItems(this.allNotes)
		.setOpen(false)
		.setLabel("Select a key:")
		.setGroup("sidebarGroup")
		.getCaptionLabel().toUpperCase(false);

		// Maj/Min/Chrom Toggles
		// (These each have an internalValue - 0 = Major, 1 = Minor, and 2 = Chromatic - 
		//  and will set this.majMinChrom to their value when clicked.)
		this.sidebarCP5.addToggle("major")
		.setPosition(majorX, keyY)
		.setWidth(toggleWidth)
		.setCaptionLabel("Major")
		.setGroup("sidebarGroup")
		.setInternalValue(0);
		this.sidebarCP5.getController("major").getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);

		this.sidebarCP5.addToggle("minor")
		.setPosition(minorX, keyY)
		.setWidth(toggleWidth)
		.setCaptionLabel("Minor")
		.setGroup("sidebarGroup")
		.setInternalValue(1);
		this.sidebarCP5.getController("minor").getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);

		this.sidebarCP5.addToggle("chrom")
		.setPosition(chromX, keyY)
		.setWidth(toggleWidth)
		.setCaptionLabel("Chrom.")
		.setGroup("sidebarGroup")
		.setInternalValue(2);
		this.sidebarCP5.getController("chrom").getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);

		((Toggle)(this.sidebarCP5.getController("chrom"))).setState(true);

		// Guide Tone pop-out Button:
		this.sidebarCP5.addToggle("guideToneButton")
		.setPosition(guideToneX, keyY)
		.setWidth(buttonWidth)
		.setCaptionLabel("Guide Tones")
		.setValue(false)
		.setGroup("sidebarGroup");
		this.sidebarCP5.getController("guideToneButton").getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);

	} // addKeySelector

	/**
	 * Adds the guide tone pop-out with range and envelope preset select dropdowns, bpm and volume sliders.
	 * 
	 * @param guideToneY	y value for the top of the pop-out; 
	 * 						for ModuleTemplate_01, this is same as the value for addKeySelector
	 */
	protected void addGuideTonePopout(int guideToneY)
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

		int		listSliderX		= (popoutSpacer * 2) + labelWidth;
//		int		textfieldX		= boxWidth - popoutSpacer - textfieldWidth;

		int		rangeY			= popoutSpacer;
		int		adsrY			= (popoutSpacer * 2) + height;
		int		bpmY			= (popoutSpacer * 3) + (height * 2);
		int		volumeY			= (popoutSpacer * 4) + (height * 3);

		this.sidebarCP5.addGroup("guideToneBackground")
		.setPosition(this.leftAlign, guideToneY + 20)
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
			this.addSliderGroup(yVals[i], labelVals[i], listSliderX, sliderWidth, ranges[i][0], ranges[i][1], startingVals[i], textfieldWidth, "guideToneBackground");
		} // for

		// "ADSR Presets" Textlabel
		this.sidebarCP5.addTextlabel("adsrPresets")
		.setPosition(popoutSpacer, adsrY)
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

		this.sidebarCP5.addScrollableList("adsrPresetsDropdown")
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
		this.sidebarCP5.addTextlabel("range")
		.setPosition(popoutSpacer, rangeY + 4)
		.setGroup("guideToneBackground")
		.setValue("Range");

		// Update Melody's scale:
		String[] scales	= new String[] { "major", "minor", "chromatic" };
		this.melody.setScale(scales[this.majMinChrom]);
		this.melody.setRangeList();

		this.sidebarCP5.addScrollableList("rangeDropdown")
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
	 * @param hsb	array of y values for each slider
	 */
	protected void addHSBSliders(int[] hsb)
	{
		int	labelX			= 10;
		int	labelWidth		= 70;

		int	sliderWidth		= 170;

		int	spacer			= 5;	// distance between slider and corresponding textfield
		int	tfWidth			= 40;	// width of Textfields

		String[]	names	= new String[] { "hueModLabel", "satModLabel", "brightModLabel" };
		String[]	values	= new String[] { "Hue", "Saturation", "Brightness" };


		this.firstHSBSliderId	= this.nextSliderId;

		for(int i = 0; i < hsb.length; i++)
		{
			this.addSliderGroup(hsb[i], values[i], -1, 1, 0);
		} // for   
	} // addHSBSliders

	
	/**
	 * Method called during instantiation, to initialize the RGB color modulate sliders.
	 * 
	 * @param modulateYVals	int[] of the y values of the red, green, and blue sliders, respectively.
	 */
	protected void addModulateSliders(int[] modulateYVals)
	{
		String[]	values	= new String[] { "Red Modulate", "Green Mod.", "Blue Modulate" };

		this.firstRGBSliderId	= this.nextSliderId;
		for(int i = 0; i < modulateYVals.length; i++)
		{
			this.addSliderGroup(modulateYVals[i], values[i], -255, 255, 0);
		} // for
	} // addModulateSliders

	
	/**
	 * Adds the slider for adjusting shape size
	 * 
	 * @param yVal	y value for this slider
	 */
	protected void addShapeSizeSlider(int yVal)
	{
		this.shapeSize			= 1;
		this.shapeSizeSliderId	= this.nextSliderId;
		
		this.addSliderGroup(yVal, "Shape Size", 0.01f, 10, this.shapeSize);
	} // addShapeSizeSlider

	
	/**
	 * Adds the Buttons for selecting the number of range segments.
	 * 
	 * @param yVal	y value for the row of Buttons
	 * @param numSegments	total number of segments (current number of segments will be set to this total)
	 * @param label	text to display on the label at the beginning of the row
	 */
	protected void addRangeSegments(int yVal, int numSegments, String label)
	{
		this.addRangeSegments(yVal, numSegments, numSegments, label);
	}  // addRangeSegments(int, int, String)

	
	/**
	 * Adds the Buttons for selecting the number of range segments.
	 * 
	 * @param yVal	y value for the row of Buttons
	 * @param numSegments	total number of range segments
	 * @param defaultNumSegments	number of segments that are set as current at the beginning
	 * @param label	text to display on the label at the beginning of the row
	 */
	protected void addRangeSegments(int yVal, int numSegments, int defaultNumSegments, String label)
	{
		if(defaultNumSegments > numSegments) {
			throw new IllegalArgumentException("ModuleTemplate.addRangeSegments: defaultNumSegments " + defaultNumSegments + " is greater than total segments, " + numSegments);
		}

		this.totalRangeSegments	= numSegments;
		System.out.println("just set totalRangeSegments to " + this.totalRangeSegments);
		this.curRangeSegments	= defaultNumSegments;

		float	toggleSpace	= (this.parent.width / 3) - (this.labelX) - this.labelWidth - this.spacer;
		int		toggleWidth	= (int)(toggleSpace / numSegments) - this.spacer;

		int[]	xVals	= new int[numSegments];
		for(int i = 0; i < xVals.length; i++)
		{
			xVals[i]	= (this.leftAlign + (i * (toggleWidth + spacer)));
		}

		this.sidebarCP5.addLabel("rangeSegments")
		.setPosition(this.labelX, yVal)
		.setValue(label)
		.setGroup("sidebarGroup");

		this.firstRangeSegmentsId	= this.nextToggleId;
		System.out.println("firstRangeSegmentsId = " + this.firstRangeSegmentsId);

		for(int i = 0; i < numSegments; i++)
		{
			this.sidebarCP5.addToggle("toggle" + this.nextToggleId)
			.setPosition(xVals[i], yVal)
			.setWidth(toggleWidth)
			.setLabel((i + 1) + "")
			.setGroup("sidebarGroup")
			.setId(this.nextToggleId)
			.setInternalValue(i + 1);
			this.sidebarCP5.getController("toggle" + this.nextToggleId).getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);


			if(i == (this.curRangeSegments - 1))
			{
				((Toggle) this.sidebarCP5.getController("toggle" + this.nextToggleId)).setState(true);
			}

			this.nextToggleId	= this.nextToggleId + 1;
		} // for - adding Toggles
	} // addRangeSegments
	

	/**
	 * Adds the "Color: Forte Threshold", "Saturation", "Saturation: Forte Threshold", 
	 * "Brightness", and "Brightness: Forte Threshold" group of Sliders/Textfields
	 * 
	 * @param yVal	y value of forte threshold
	 * @param verticalSpacer	vertical space between sliders
	 */
	protected void addThresholdSliders(int yVal, int verticalSpacer)
	{
		int	textfieldX	= this.leftAlign + this.sliderWidth + this.spacer;

		// Since some i's will add a couple rows of labels and sliders,
		// this variable keeps track of which "level" of y the next thing should be added to.
		//		float	yPos		= 0;

		String[]	names	= new String[] {
				"forteThresh",
				"saturation",
				"saturationForteThresh",
				"brightness",
				"brightnessForteThresh"
		}; // names

		String[]	labels = new String[] {
				"Forte\nThreshold",
				"Saturation",
				"Sat: Forte\nThreshold",
				"Brightness",
				"Bright: Forte\nThreshold"
		}; // labels
		/*	
		String[]	hsbSliderNames	= new String[] {
				"hueSlider",
				"saturationSlider",
				"brightnessSlider"

		}; // hsbSliderNames
		 */
		this.firstThresholdSliderId	= this.nextSliderId;

		for(int i = 0; i < names.length; i++)
		{
			// Forte Thresholds
			if(i % 2 == 0)
			{
				this.addSliderGroup(yVal + (i * (verticalSpacer + this.sliderHeight)), labels[i], this.minThreshold, 7000, this.forteThreshold);

			} // if - Forte Thresholds

			// percent sliders
			if(i % 2 == 1)
			{
				this.sidebarCP5.addLabel(names[i])
				.setPosition(this.labelX, yVal + (i * (verticalSpacer + this.sliderHeight)) + 4)
				.setValue(labels[i])
				.setGroup("sidebarGroup");
				
				this.sidebarCP5.addSlider("slider" + this.nextSliderId)
				.setPosition(this.leftAlign, (yVal + (i * (verticalSpacer + this.sliderHeight))))
				.setSize(this.sliderWidth + this.spacer + this.textfieldWidth, this.sliderHeight)
				.setRange(-1, 1)
				.setValue(0)
				.setGroup("sidebarGroup")
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
	 * @param yVal	the y value for the row
	 * @param buttonLabels	String[] of text for the buttons; if canvas color in this row of Buttons, first item should be "Canvas"
	 * @param labelText	text of the Label on the far left of the row
	 * @param canvas	boolean indicating whether or not the Canvas color Button is included in this row
	 */
	protected void addSpecialColors(int yVal, String[] buttonLabels, String labelText, boolean canvas)
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

		this.fromColorSelect	= true;
		this.fromSpecialColors	= false;
		this.specialColorsPos	= new int[buttonLabels.length];

		if(canvas)
		{
			this.canvasColorSelectId	= this.nextColorWheelId;
			this.firstSpecialColorsCWId	= this.nextColorWheelId + 1;

		} else {
			this.firstSpecialColorsCWId	= this.nextColorWheelId;

		}
		// the "- (10 / buttonsPerRow)" adds a 10 pixel space at the end of the row:
		int		buttonWidth		= (((this.parent.width / 3) - this.leftAlign) / buttonLabels.length) - this.spacer - (10 / buttonLabels.length);
		int[]	xVals	= new int[buttonLabels.length];
		for(int i = 0; i < xVals.length; i++)
		{
			xVals[i]	= this.leftAlign + ((buttonWidth + this.spacer) * i);
		}

		this.sidebarCP5.addTextlabel("specialColorsLabel")
		.setPosition(labelX, yVal + 4)
		.setGroup("sidebarGroup")
		.setValue(labelText);

		// Loop through all
		for(int i = 0; i < xVals.length; i++)
		{
			if(canvas && i == 0)
			{
				this.addColorWheelGroup(xVals[i], yVal, buttonWidth, buttonLabels[i], this.canvasColor);
			} else {
				int	thisColorPos	= this.specialColorsPos[i - 1];
				this.addColorWheelGroup(xVals[i], yVal, buttonWidth, buttonLabels[i], new Color(this.colorSelect[thisColorPos].getRGB()));
			}
		} // for - i

		this.fillHSBColors();
	} // addColorSelect
	
	/**
	 * Adds a Label with given text, Slider of given lowest value, highest value, and starting value, with default 
	 * width and x value at this.leftAlign, and Textfield with default width to "sidebarGroup" at given y value.
	 * 
	 * @param yVal	y value for the whole group (Label will, of course, be 4 pixels higher in order to look centered)
	 * @param labelText	text for the Label
	 * @param lowRange	Slider lowest value
	 * @param highRange	Slider highest value
	 * @param startingVals	Slider default/starting value
	 */
	protected void addSliderGroup(int yVal, String labelText, float lowRange, float highRange, float startingVals)
	{
		this.addSliderGroup(yVal, labelText, this.leftAlign, this.sliderWidth, lowRange, highRange, startingVals, this.textfieldWidth, "sidebarGroup");
	} // addSliderGroup - use default width
	
	/**
	 * Adds a Label with given text, Slider with given x value, width, lowest value, 
	 * highest value, and starting value, and Textfield with given width to given group at the given y value.
	 * 
	 * @param yVal	y value for the whole group (Label will, of course, be 4 pixels higher in order to look centered)
	 * @param labelText	text for the Label
	 * @param sliderX	x value for the Slider
	 * @param sliderWidth	Slider width
	 * @param lowRange	Slider lowest value
	 * @param highRange	Slider highest value
	 * @param startingVals	Slider default/starting value
	 * @param textfieldWidth	Textfield width
	 * @param group	String indicating to which group these Sliders and Textfields should belong
	 */
	protected void addSliderGroup(int yVal, String labelText, int sliderX, int sliderWidth, float lowRange, float highRange, float startingVals, int textfieldWidth, String group)
	{
		this.sidebarCP5.addLabel("label" + this.nextSliderId)
		.setPosition(labelX, yVal + 4)
		.setWidth(labelWidth)
		.setGroup(group)
		.setValue(labelText);

		this.sidebarCP5.addSlider("slider" + this.nextSliderId)
		.setPosition(sliderX, yVal)
		.setSize(sliderWidth, this.sliderHeight)
		.setRange(lowRange, highRange)
		.setValue(startingVals)
		.setSliderMode(Slider.FLEXIBLE)
		.setLabelVisible(false)
		.setGroup(group)
		.setId(this.nextSliderId);

		this.nextSliderId	= this.nextSliderId + 1;

		this.sidebarCP5.addTextfield("textfield" + this.nextSTextfieldId)
		.setPosition(sliderX + sliderWidth + spacer, yVal)
		.setSize(this.textfieldWidth, this.sliderHeight)
		.setText(this.sidebarCP5.getController("slider" + (this.nextSTextfieldId - 100)).getValue() + "")
		.setAutoClear(false)
		.setGroup(group)
		.setId(this.nextSTextfieldId)
		.getCaptionLabel().setVisible(false);

		this.nextSTextfieldId	= this.nextSTextfieldId + 1;
	} // addSliderGroup - define width

	/**
	 * Adds a connected Button, ColorWheel, and Textfield to this.sidebarCP5, in group "sidebarGroup",
	 * by making a color from the int[] and calling addColorWheelGroup(int, int, int, String, Color)
	 * 
	 * @param x	x value of Button and ColorWheel
	 * @param y	y value of Button
	 * @param buttonWidth	width of Button
	 * @param buttonLabel	text to put on the Button
	 * @param colo	int[] with the red, green, blue values for the desired Color
	 */
	protected Controller[] addColorWheelGroup(int x, int y, int buttonWidth, String buttonLabel, int[] rgbColor)
	{
		if(rgbColor == null) {
			throw new IllegalArgumentException("ModuleTemplate.addColorWheelGroup: int[] parameter is null.");
		}
		if(rgbColor.length != 3) {
			throw new IllegalArgumentException("ModuleTemplate.addColorWheelGroup: int[] parameter has length " + rgbColor.length + 
					"; must be length 3.");
		} // error checking

		return this.addColorWheelGroup(x, y, buttonWidth, buttonLabel, new Color(rgbColor[0], rgbColor[1], rgbColor[2]));
	} // addColorWheelGroup

	/**
	 * Adds a connected Button, ColorWheel, and Textfield to this.sidebarCP5, in group "sidebarGroup"
	 * 
	 * @param x	x value of Button and ColorWheel
	 * @param y	y value of Button
	 * @param buttonWidth	width of Button
	 * @param buttonLabel	text to put on the Button
	 * @param color	Color to set the ColorWheel and Textfield ("rgb([red], [green], [blue])")
	 */
	protected Controller[] addColorWheelGroup(int x, int y, int buttonWidth, String buttonLabel, Color color)
	{
		Button		button;
		ColorWheel	colorWheel;
		Textfield	textfield;

		if(buttonLabel == null) {
			throw new IllegalArgumentException("ModuleTemplate.addColorWheelGroup: String parameter is null.");
		}
		// Add Button:
		button	= this.sidebarCP5.addButton("button" + this.nextButtonId)
				.setPosition(x, y)
				.setWidth(buttonWidth)
				.setLabel(buttonLabel)
				.setId(this.nextButtonId)
				.setGroup("sidebarGroup");
		button.getCaptionLabel().toUpperCase(false);

		this.nextButtonId = this.nextButtonId + 1;

		colorWheel	= this.sidebarCP5.addColorWheel("colorWheel" + this.nextColorWheelId)
				.setPosition(x, y - 200)
				.setRGB(color.getRGB())
				.setLabelVisible(false)
				.setVisible(false)
				.setGroup("sidebarGroup")
				.setId(this.nextColorWheelId);

		this.nextColorWheelId = this.nextColorWheelId + 1;					

		textfield	= this.sidebarCP5.addTextfield("textfield" + this.nextCWTextfieldId)
				.setPosition(x + buttonWidth + this.spacer, y)
				.setAutoClear(false)
				.setVisible(false)
				.setText("rgb(" + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue() + ")")
				.setGroup("sidebarGroup")
				.setId(this.nextCWTextfieldId);
		textfield.getCaptionLabel().setVisible(false);

		this.nextCWTextfieldId = this.nextCWTextfieldId + 1;

		return new Controller[] { button, colorWheel, textfield };
	} // addColorWheelGroup

	
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

		if(position == -1)	
		{	
			for(int i = 0; i < this.goalHue.length; i++)
			{
				this.goalHue[i]	= this.canvasColor[i];
			} // for - canvas
		} else {
			this.goalHue	= this.getColor(position);
		} // else

	} // setGoalHue

	
	/**
	 * Takes the values of curHue from its current values to the values in goalHue
	 * over the time that is designated by the attack, release, and transition sliders
	 * 
	 * @param position	position in colors to which curHue should fade
	 */
	public void fade(int position)
	{
		if(position > this.colorSelect.length || position < -1) {
			throw new IllegalArgumentException("ModuleTemplate.getGoalHue: position " + position + 
					" is out of bounds; must be between -1 (signifying canvas color) or " + (this.colorSelect.length - 1));
		} // error checking

		float	curAmp	= this.input.getAmplitude();

		if(curAmp < this.threshold)	
		{
			this.nowBelow	= true;

			for(int i = 0; i < this.goalHue.length; i++)
			{
				this.goalHue[i]	= this.canvasColor[i];
			} // for - canvas

		} else {
			this.nowBelow	= false;

			this.goalHue	= this.applyThresholdSBModulate(curAmp, position);
		} // else

		if(this.checkpoint < this.parent.millis())
		{
			for(int i = 0; i < 3; i++)
			{				
				// if the current hue is less than the goalHue - the colorAdd, then add colorAdd:
				if(this.curHue[i] < this.goalHue[i] - (this.colorAdd[i] / 2))
				{
					this.curHue[i]	=	this.curHue[i] + this.colorAdd[i];
				} else 
					// otherwise, if it's more than the goal Hue, even after adding half of colorAdd, then subtract:
					if(this.curHue[i] > this.goalHue[i] + (this.colorAdd[i] / 2))
					{
						this.curHue[i]	=	this.curHue[i] - this.colorAdd[i];
					}
			} // for - i

			this.checkpoint = (this.parent.millis() + 50);
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
			lowBound	= this.goalHue[i] - 5;
			highBound	= this.goalHue[i] + 5;

			// Now check colors for whether they have moved into the boundaries:
			if(this.curHue[i] < highBound && this.curHue[i] > lowBound) {
				// if here, color has been reached.
				this.colorReachedArray[i]	= true;
			} else {
				this.colorReachedArray[i]	= false;
			}
		} // for

		// If all elements of the color are in range, then the color has been reached:
		this.colorReached	= this.colorReachedArray[0] && this.colorReachedArray[1] && this.colorReachedArray[2];

		int	oldARTpos	= this.attRelTranPos;

		// If coming from a low amplitude note and not yet reaching a color,
		// use the attack value to control the color change:
		if(!this.nowBelow && !colorReached) 
		{	
			this.attRelTranPos	= 0;
//			System.out.println("	attack!!!!");
		} else if(!this.nowBelow && colorReached) {
			// Or, if coming from one super-threshold note to another, use the transition value:
			this.attRelTranPos	= 2;
//			System.out.println("	transition.... transition [doooooo do dooo do do ] - transition!");
		} else if(this.nowBelow) {
			// Or, if volume fell below the threshold, switch to release value:
			this.attRelTranPos	= 1;
//			System.out.println("	re....lent! re...coil! re...verse!");
		}

//		if(this.attRelTranPos != oldARTpos)
//		{			
			// Calculate color ranges:
			for(int i = 0; i < this.curHue.length; i++)
			{
				this.colorRange[i]	= Math.abs(this.goalHue[i] - this.curHue[i]);
				
//				System.out.println("colorAddDecimals[" + i + "] = " + colorAddDecimals[i]);
				
				// if colorAdd reached 1, add 1 to colorAdd:
/*				if(this.colorAddDecimals[i] > 1)	
				{	
					this.colorAdd[i]++;	
//					this.colorAddDecimals[i]	= this.colorAddDecimals[i] % 1;
				}
*/
				// divide the attack/release/transition value by 50
				// and divide colorRange by that value to find the amount to add each 50 millis.
				float addThis = (int)(this.colorRange[i] / (this.attRelTranVals[this.attRelTranPos] / 50));
/*				if(addThis < 1)	
				{	
					this.colorAddDecimals[i]	= this.colorAddDecimals[i] + addThis;	
				} else {	 */
					this.colorAdd[i]	= (int)addThis;	
//				}
			} // for
//		} // if

	} // fade

	
	/**
	 * Classes using ModuleTemplate should call this method in setup() with a position in colors.
	 * 
	 * @param curHuePos	position in ModuleTemplate.colors
	 */
	public void setCurHueColorRangeColorAdd(int curHuePos)
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
		for(int i = 0; i < this.colorSelect.length - 1; i++)
		{
			// Take the percent of the difference multiplied by the position in the array,
			// subtracting it from the first color to deal with negatives correctly
			// (and dividing by 100 because percent requires it but to do so earlier would create smaller numbers than Java likes to deal with).
			//				this.colors[j][i]	= this.colors[0][i] - (difference * j * percent / 100);

			newColor[0]	= (int)(curColor[0] - (rDif * i * percent / 100));
			newColor[1]	= (int)(curColor[1] - (gDif * i * percent / 100));
			newColor[2]	= (int)(curColor[2] - (bDif * i * percent / 100));

			System.out.println("newColor[0] = " + newColor[0] + "; newColor[1] = " + newColor[1] + "; newColor[2] = " + newColor[2]);

			this.setColor(i, newColor);
		} // for - i
		//		} // for - i

		// Fill the last color manually, because if we don't,
		// it can't seem to calculate correctly when the tonic color is changed:
		this.setColor(this.colorSelect.length - 1, rgbVals2);

		this.fillHSBColors();

		/*		for(int i = 0; i < rgbVals2.length; i++)
		{
			this.colors[this.colors.length - 1][i]	= rgbVals2[i];
		}
		 */
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

			divideBy1	= 4;
			divideBy2	= 1;
			divideBy3	= 3;
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
		for(int i = 0; i < this.colorSelect.length && trichromColorPos < trichromColors.length; i++)
		{
			trichromColorPos	= this.scaleDegreeColors[this.majMinChrom][i];
			this.setColor(i, trichromColors[trichromColorPos]);

		} // for

		this.fillHSBColors();
	} //trichromatic_ThreeRGB


	/**
	 * Populates colors with rainbow colors (ROYGBIV - with a few more for chromatic scales).
	 */
	protected void rainbow()
	{
		for(int i = 0; i < this.colorSelect.length && i < this.rainbowColors[this.majMinChrom].length; i++)
		{
			this.setColor(i, this.rainbowColors[this.majMinChrom][i]);
		} // for - i (going through colors)

		this.fillHSBColors();
	} // rainbow

	protected void setColorStyle(int newColorStyle)
	{
		this.curColorStyle	= newColorStyle;

		// Rainbow:
		if(this.curColorStyle == ModuleTemplate01.CS_RAINBOW)
		{
			//	if avoids errors during instantiation:
			if(this.sidebarCP5.getController("button" + (this.firstSpecialColorsCWId - 100)) != null)	{	this.sidebarCP5.getController("button" + (this.firstSpecialColorsCWId - 100)).lock();	}
			if(this.sidebarCP5.getController("button" + (this.firstSpecialColorsCWId - 99)) != null)	{	this.sidebarCP5.getController("button" + (this.firstSpecialColorsCWId  - 99)).lock();	}
			if(this.sidebarCP5.getController("button" + (this.firstSpecialColorsCWId - 98)) != null)	{	this.sidebarCP5.getController("button" + (this.firstSpecialColorsCWId - 98)).lock();	}

			this.rainbow();
		} // if - rainbow

		// Dichromatic:
		if(this.curColorStyle == ModuleTemplate01.CS_DICHROM)
		{
			this.specialColorsPos[0]	= 0;

			// For minor keys, choose the 2nd to last note; else choose the last note:
			if(this.majMinChrom == 1)	{	this.specialColorsPos[1]	= this.colorSelect.length - 2;	}
			else						{	this.specialColorsPos[1]	= this.colorSelect.length - 1;	}

			if(this.sidebarCP5.getController("button" + (this.firstSpecialColorsCWId - 100)) != null)	{	this.sidebarCP5.getController("button" + (this.firstSpecialColorsCWId - 100)).unlock();	}
			if(this.sidebarCP5.getController("button" + (this.firstSpecialColorsCWId - 99)) != null)	{	this.sidebarCP5.getController("button" + (this.firstSpecialColorsCWId  - 99)).unlock();	}
			if(this.sidebarCP5.getController("button" + (this.firstSpecialColorsCWId - 98)) != null)	{	this.sidebarCP5.getController("button" + (this.firstSpecialColorsCWId - 98)).lock();	}

			// First time to dichromatic, dichromFlag will be false, 
			// and the two colors will be set to contrast.			
			if(!this.dichromFlag)
			{
				System.out.println("We are calling dichrom, right?");
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

			// Turned off the "first time/remaining times" because it's still pretty interesting
			// and, I think, more intuitive, coming off of another color.  Dichromatic is boring coming off rainbow.
			// first time trichromatic has been called:
			if(!this.trichromFlag)
			{
				//				this.trichromatic_OneRGB(this.getColor(0));
				this.trichromFlag	= true;
			}
			// every other time:
			else
			{
				// This makes sure that, even if the positions of the specialColors change,
				// the colors themselves will remain constant from one style/scale to the next:
				this.setColor(colorPos2, this.getColor(this.specialColorsPos[1]));
				this.setColor(colorPos3, this.getColor(this.specialColorsPos[2]));
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


			this.specialColorsPos[0]	= 0;
			this.specialColorsPos[1]	= colorPos2;
			this.specialColorsPos[2]	= colorPos3;

			if(this.sidebarCP5.getController("button" + (this.firstSpecialColorsCWId - 100)) != null)	{	this.sidebarCP5.getController("button" + (this.firstSpecialColorsCWId - 100)).unlock();	}
			if(this.sidebarCP5.getController("button" + (this.firstSpecialColorsCWId - 99)) != null)	{	this.sidebarCP5.getController("button" + (this.firstSpecialColorsCWId  - 99)).unlock();	}
			if(this.sidebarCP5.getController("button" + (this.firstSpecialColorsCWId - 98)) != null)	{	this.sidebarCP5.getController("button" + (this.firstSpecialColorsCWId - 98)).unlock();	}

			this.trichromatic_ThreeRGB(this.getColor(0), this.getColor(colorPos2), this.getColor(colorPos3));
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


			this.setColor(i, color);
		} // for - i

	} // applyRGBModulate

	/**
	 * Applies the values from this.hueSatBrightnessMod to the contents of the colors array parameter.
	 * @param colors	this.colors or this.legendColors
	 * @param hsbColors	this.hsbColors
	 */
	protected void applyHSBModulate()
	{
		// TODO: also need to implement it for legendColors!  So can't just get rid of the two parameters entirely.

		if(this.firstColorSelectCWId != -1)
		{
			if(this.hsbColors == null)	{	this.fillHSBColors();	}

			float[] hsb 			= new float[3];

			for (int i = 0; i < this.colorSelect.length; i++)
			{
				// Converts this position of hsbColors from RGB to HSB:
				Color.RGBtoHSB(this.hsbColors[i][0], this.hsbColors[i][1], this.hsbColors[i][2], hsb);

				// Applies the status of the sliders to the newly-converted color:
				hsb[0] = (hsb[0] + this.hueSatBrightnessMod[0] + this.hueSatBrightPercentMod[0] + 1) % 1;
				hsb[1] = Math.max(Math.min(hsb[1] + this.hueSatBrightnessMod[1] + this.hueSatBrightPercentMod[1], 1), 0);
				hsb[2] = Math.max(Math.min(hsb[2] + this.hueSatBrightnessMod[2] + this.hueSatBrightPercentMod[2], 1), 0);

				// Converts the color back to RGB:
				int oc = Color.HSBtoRGB(hsb[0], hsb[1],  hsb[2]);
				Color a = new Color(oc);

				// Sets the ColorWheels to the newly modded color:
				this.setColor(i, new int[] { a.getRed(), a.getGreen(), a.getBlue() });
				//			((ColorWheel)this.sidebarCP5.getController("colorWheel" + id)).setRGB(a.getRGB());
			} // for 
		} else {
			System.out.println("ModuleTemplate.applyHSBModulate: firstColorSelectCWId == " + this.firstColorSelectCWId + "; did not attempt to set ColorWheels.");
		} // else - let the user know that CWs not there and we didn't set them

	} // applyHSBModulate

	
	/**
	 * Applies the values of the threshold saturation and brightness Sliders 
	 * to the color of the ColorWheel at the given position and returns the affected color.
	 * 
	 * @param curAmp	the current amplitude
	 * @param colorPos	the position in this.colorSelect to which the sat/brightness should be applied
	 * @return			the color with saturation and brightness adjustments
	 */
	protected int[] applyThresholdSBModulate(float curAmp, int colorPos)
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
		Color.RGBtoHSB(this.hsbColors[colorPos][0], this.hsbColors[colorPos][1], this.hsbColors[colorPos][2], hsb);

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
			this.hsbColors = new int[this.colorSelect.length][3];
		}

		// Only do this after the colors have been initialized:
		if(this.firstColorSelectCWId != -1)
		{
			for(int i = 0; i < this.hsbColors.length; i++)
			{
				this.hsbColors[i]	= this.getColor(i);
			} // for
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
	 * Displays the "sidebarGroup" of this.sidebarCP5
	 */
	protected void displaySidebar(boolean show)
	{	
		this.sidebarCP5.getGroup("sidebarGroup").setVisible(show);
		if(show)
		{
			this.leftEdgeX 	= this.parent.width / 3;
		} else {
			this.leftEdgeX	= 0;
		}

	} // displaySidebar

	/**
	 * Since ModuleTemplate implements ControlListener, it needs to have this method
	 * to "catch" the controlEvents from the ControlP5 (sidebarCP5).
	 * 
	 * Any child classes that want to also use controlEvents can have their own controlEvent
	 * as long as they call super.controlEvent first.
	 */
	public void controlEvent(ControlEvent controlEvent)
	{
//		System.out.println("ModuleTemplate.controlEvent: controlEvent = " + controlEvent);

		int	id	= controlEvent.getController().getId();
		// Play button:
		if(controlEvent.getController().getName().equals("play"))
		{
			boolean	val	= ((Toggle)controlEvent.getController()).getBooleanValue();
			this.input.pause(true);
			this.sidebarCP5.getController("pause").setVisible(val);

			if(val)
			{
				this.playMelody();
			} else {
				// Unpauses the pause button so that it is ready to be paused when
				// play is pressed again:
				((Toggle)this.sidebarCP5.getController("pause")).setState(false);
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
			this.menuIsOpen = true;
			this.displaySidebar(true);
			controlEvent.getController().setVisible(false);
			this.sidebarCP5.getWindow().resetMouseOver();
		} // if - hamburger

		// MenuX button:
		if(controlEvent.getController().getName().equals("menuX"))
		{
			this.displaySidebar(false);
			/*			this.leftEdgeX	= 0;
			this.sidebarCP5.getGroup("sidebarGroup").setVisible(false);
			 */
			this.sidebarCP5.getController("hamburger").setVisible(true);
			this.sidebarCP5.getController("hamburger").setVisible(!((Toggle)this.sidebarCP5.getController("menuButton")).getBooleanValue());
		} // if - menuX

		// Hide play button button:
		if(controlEvent.getName().equals("playButton"))
		{
			// Set the actual play button to visible/invisible:
			this.sidebarCP5.getController("play").setVisible(!this.sidebarCP5.getController("play").isVisible());
		} // if - hidePlayButton


		// Hide menu button button:
		if(controlEvent.getName().equals("menuButton"))
		{
			// Hamburger is still able to be clicked because of a boolean isClickable added to 
			//Controller; automatically false, but able to be set to true.
			// A Controller must be visible and/or clickable to respond to click.
			this.sidebarCP5.getController("hamburger").setVisible(!((Toggle)this.sidebarCP5.getController("menuButton")).getBooleanValue());
		} // if - hidePlayButton

		// Hide legend:
		if(controlEvent.getName().equals("legend"))
		{
			this.setShowScale(!((Toggle) (controlEvent.getController())).getState());
		}

		// Sliders
		if(id > -1 && id < 100)
		{
			try
			{
				Slider	curSlider	= (Slider)this.sidebarCP5.getController("slider" + id);

				float	sliderValFloat	= curSlider.getValue();

				if((Textfield)this.sidebarCP5.getController("textfield" + (id + 100)) != null)
				{
					Textfield	curTextfield	= (Textfield)this.sidebarCP5.getController("textfield" + (id + 100));

					String	sliderValString	= this.decimalFormat.format(curSlider.getValue());

					curTextfield.setText(sliderValString);			
				} // if connected to Textfield


				// Threshold:
				if(this.thresholdSliderId != -1 && id == this.thresholdSliderId)
				{
					this.setThreshold(sliderValFloat);
				} // threshold slider

				// Attack, Release, and Transition:
				if(this.firstARTSliderId != -1 &&
						id >= this.firstARTSliderId && id < (this.firstARTSliderId + 3))
				{
					//			int	pos	= (id / 2) - 1;
					int	pos	= id - 1;
					//			this.attackReleaseTransition[pos]	= sliderValFloat;
					this.setAttRelTranVal(pos, sliderValFloat);
				} // attack/release/transition

				// Hue/Saturation/Brightness modulate
				if(this.firstHSBSliderId != -1 && 
						id >= this.firstHSBSliderId && id < (this.firstHSBSliderId + 3))
				{
					//			int pos = (id/2)-7;
					int pos = id - this.firstHSBSliderId;

					this.setHueSatBrightnessMod(pos, sliderValFloat);

					//					this.applyHSBModulate(this.hsbColors);
					this.applyHSBModulate();
				}//hsb mod

				// Red Modulate/Green Modulate/Blue Modulate:
				if(this.firstRGBSliderId != -1 &&
						id >= this.firstRGBSliderId && id < (this.firstRGBSliderId + 3))
				{
					//			int	pos	= (id / 2) - 4;		
					int	pos	= id - this.firstRGBSliderId;	// red = 0, green = 1, blue = 2

					this.redGreenBlueMod[pos]	= sliderValFloat;
					this.applyRGBModulate();
					// TODO!!!
					//					this.applyColorModulate(this.legendColors, this.originalColors);
				} // red/green/blue mod

				if(this.bpmSliderId != -1 && id == this.bpmSliderId)
				{
					this.bpm	= Math.max(Math.min((int)sliderValFloat, 240), 0);
				}

				if(this.volumeSliderId != -1 && id == this.volumeSliderId)
				{
					this.instrument.setVolume(Math.max(Math.min(sliderValFloat, 5), 0));
				}

				if(this.shapeSizeSliderId != -1 && id == this.shapeSizeSliderId)
				{
					this.shapeSize	= sliderValFloat;
				}

				// Saturation and Brightness Threshold and Percent Sliders:
				if(this.firstThresholdSliderId != -1 &&
						( ( id > this.firstThresholdSliderId ) && ( id < this.firstThresholdSliderId + 5 ) ) )
				{
					int		arrayPos	= (id - this.firstThresholdSliderId - 1) / 2;
					// Percent Sliders
					if((id - this.firstThresholdSliderId) % 2 == 1)
					{
						this.satBrightPercentVals[arrayPos]		= controlEvent.getValue();
						this.satBrightThresholdVals[arrayPos]	= this.sidebarCP5.getValue("slider" + (id + 1));
						//				percentVal		= controlEvent.getValue();
					} else {
						// Threshold Sliders
						this.satBrightThresholdVals[arrayPos]	= controlEvent.getValue();
						this.satBrightPercentVals[arrayPos]		= this.sidebarCP5.getValue("slider" + (id - 1));
					}
				} // Saturation and Brightness Threshold and Percent Sliders

			} catch (NullPointerException npe) {
				System.out.println("ModuleTemplate.controlEvent - sliders: caught NullPointer; curTextfield = " + (Textfield)this.sidebarCP5.getController("textfield" + (id + 100)));
			} // catch
		} // if - slider

		// Textfields
		if(id > 99 && id < 200)
		{
			Textfield	curTextfield	= (Textfield)this.sidebarCP5.getController("textfield" + id);
			Slider		curSlider		= (Slider)this.sidebarCP5.getController("slider" + (id - 100));

			try	{
				curSlider.setValue(Float.parseFloat(curTextfield.getStringValue()));

			} catch(NumberFormatException nfe) {
				//System.out.println("ModuleTemplate.controlEvent: string value " + curTextfield.getStringValue() + 
				//"for controller " + curTextfield + " cannot be parsed to a float.  Please enter a number.");
			} // catch
		} // textField

		// Color Select Buttons (to show ColorWheel)
		if(id > 199 && id < 300)
		{
			Button	curButton	= (Button)controlEvent.getController();

			// draw slightly transparent rectangle:
			if(curButton.getBooleanValue())
			{

				this.sidebarCP5.getGroup("background").setVisible(true);
				this.sidebarCP5.getGroup("background").bringToFront();

			} else {

				//				this.fillHSBColors();
				// TODO: might need to fillOriginalColors here, too, at some point?				

				this.sidebarCP5.setAutoDraw(true);
				this.sidebarCP5.getGroup("background").setVisible(false);
				//				this.displaySidebar(false);
			}

			this.sidebarCP5.getController("button" + (controlEvent.getId())).bringToFront();
			this.sidebarCP5.getController("colorWheel" + (controlEvent.getId() + 100)).bringToFront();
			this.sidebarCP5.getController("textfield" + (controlEvent.getId() + 200)).bringToFront();

			this.sidebarCP5.getController("colorWheel" + (controlEvent.getId() + 100)).setVisible(curButton.getBooleanValue());
			this.sidebarCP5.getController("textfield" + (controlEvent.getId() + 200)).setVisible(curButton.getBooleanValue());

			//			this.fillOriginalColors();
			this.fillHSBColors();

			// Reset whichever of the sliders is applicable:
			if(this.firstRGBSliderId > -1)
			{
				this.resetRGBSlidersTextfields();
				this.applyRGBModulate();
			}
			if(this.firstHSBSliderId > -1)
			{
				this.resetHSBSlidersTextfields();
				this.applyHSBModulate();
			}

			// If there are special colors, check to see if this color corresponds to one of them
			// in order to correctly set the boolean flags:
			if(this.firstSpecialColorsCWId != -1)
			{
				// Position in colorSelect (if this CW is in colorSelect):
				int	colorPos	= (id % 100) - (this.firstColorSelectCWId % 100);
				if(colorPos >= 0 && colorPos < this.colorSelect.length /*&& 
						(this.arrayContains(this.specialColorsPos, colorPos) > -1) */)
				{
					this.fromColorSelect	= true;
					this.fromSpecialColors	= false;
					// Check to see if this position corresponds to a special color:
					/*				int	specialColorsPos	= this.arrayContains(this.specialColorsPos, colorPos);
					if(specialColorsPos > -1)
					{
						// Increment the counter for this color:
						this.specialColorsCounts[specialColorsPos]++;
						System.out.println("colorSelectCW: specialColorsCounts[" + specialColorsPos + "] = " + this.specialColorsCounts[specialColorsPos]);

					} // if - this CW connects to a specialColor	
					 */						
				} // if - this CW is in colorSelect
				else
				{
					// Check to see if this CW is a specialColors CW:
					colorPos	= (id % 100) - (this.firstSpecialColorsCWId % 100);
					if(colorPos >= 0 && colorPos < this.specialColorsPos.length)
					{
						this.fromSpecialColors	= true;
						this.fromColorSelect	= false;
						// Increment the counter for this color:
						/*						this.specialColorsCounts[colorPos]++;
						System.out.println("specialColorCW: specialColorsCounts[" + specialColorsPos + "] = " + this.specialColorsCounts[colorPos]);
						 */
					} // if
				} // else - for CWs not in colorSelect

			} // if - specialColors
		} // Color Select Buttons

		// ColorWheels
		if(id > 299 && id < 400)
		{
			// get current color:
			ColorWheel	curCW	= (ColorWheel)controlEvent.getController();

			int	rgbColor	= curCW.getRGB();
			Color	color	= new Color(rgbColor);

			// Set corresponding Textfield with color value:
			Textfield	curColorTF	= (Textfield)this.sidebarCP5.getController("textfield" + (controlEvent.getId() + 100));
			curColorTF.setText("rgb(" + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue() + ")");

			// canvas color (does not affect notes):
			if( ( this.canvasColorSelectId != -1) && 
					( ( id % 100 ) == ( this.canvasColorSelectId % 100 ) ) )
			{
				this.canvasColor[0]	= color.getRed();
				this.canvasColor[1]	= color.getGreen();
				this.canvasColor[2]	= color.getBlue();

				// Ensures that the shape doesn't have to fade to this color if the amp is below the threshold:
				if(this.nowBelow)
				{
					this.curHue[0]	= color.getRed();
					this.curHue[1]	= color.getGreen();
					this.curHue[2]	= color.getBlue();
				}
			} // if - canvas

			// If there are special colors,
			// check to see if this color corresponds to one or is one:
			if(this.firstSpecialColorsCWId != -1)
			{
				// Position in colorSelect (if this CW is in colorSelect):
				int	colorPos	= id - this.firstColorSelectCWId;
				if(colorPos >= 0 && colorPos < this.colorSelect.length)
				{
					// Check to see if this position corresponds to a special color:
					int	specialColorsPos	= this.arrayContains(this.specialColorsPos, colorPos);
					if(specialColorsPos > -1)
					{
						// Increment the counter for this color:
						//						this.specialColorsCounts[specialColorsPos]++;
						//						System.out.println("colorSelectCW: specialColorsCounts[" + specialColorsPos + "] = " + this.specialColorsCounts[specialColorsPos]);

						// Make sure that they don't just keep calling back and forth:
						//						if(this.specialColorsCounts[specialColorsPos] % 2 == 1)
						if(this.fromColorSelect)
						{
							((ColorWheel)this.sidebarCP5.getController("colorWheel" + (specialColorsPos + this.firstSpecialColorsCWId))).setRGB(color.getRGB());

						} // if - only make one call per pair
					} // if - this CW connects to a specialColor							
				} // if - this CW is in colorSelect
				else
				{
					// Check to see if this CW is a specialColors CW:
					colorPos	= id - this.firstSpecialColorsCWId;
					if(colorPos >= 0 && colorPos < this.specialColorsPos.length)
					{
						// Increment the counter for this color:
						//						this.specialColorsCounts[colorPos]++;
						//						System.out.println("specialColorCW: specialColorsCounts[" + specialColorsPos + "] = " + this.specialColorsCounts[colorPos]);

						// Make sure that they don't just keep calling back and forth:
						//						if(this.specialColorsCounts[colorPos] % 2 == 1)
						if(this.fromSpecialColors)
						{
							int	colorSelectPos	= this.specialColorsPos[colorPos];
							((ColorWheel)this.sidebarCP5.getController("colorWheel" + (colorSelectPos + this.firstColorSelectCWId))).setRGB(color.getRGB());

							//							this.applySpecialColors();
							this.setColorStyle(this.curColorStyle);

						} // if - only make one call per pair
					} // if
				} // else - for CWs not in colorSelect

			} // if - specialColors

		} // ColorWheels

		// ColorWheel Textfields
		if(id > 399 && id < 500)
		{
			id	= controlEvent.getId();
			System.out.println("controlEvent: event for a ColorWheel Textfield, id " + id);

			// Getting color value from the Textfield:
			String[]	tfValues	= controlEvent.getStringValue().split("[(,)]");
			for(int i = 0; i < tfValues.length; i++)
			{
				tfValues[i]	= tfValues[i].trim().toLowerCase();
			} // for

			try
			{
				if(tfValues[0].equals("rgb"))
				{
					// Get color values:
					int	red		= Integer.parseInt(tfValues[1]);
					int	green	= Integer.parseInt(tfValues[2]);
					int	blue	= Integer.parseInt(tfValues[3]);

					// Constrain to 0-255:
					red		= Math.min(255, Math.max(0, red));
					green	= Math.min(255, Math.max(0, green));
					blue	= Math.min(255, Math.max(0, blue));

					// Set corresponding ColorWheel:
					//					Color	rgbColor	= new Color(this.tonicColor[0], this.tonicColor[1], this.tonicColor[2]);
					Color	rgbColor	= new Color(red, green, blue);
					int		rgbInt		= rgbColor.getRGB();
					((ColorWheel)this.sidebarCP5.getController("colorWheel" + (id - 100))).setRGB(rgbInt);

				} // if - rgb

			} catch(Exception e) {
				System.out.println("Sorry, '" + controlEvent.getStringValue() + "' is not recognized as a valid color (note that colors must be defined by Integer values). Exception message: "
						+ e.getMessage());
			} // catch
		} // ColorWheel Textfields

		// Toggles
		if(id > 499 && id < 600)
		{
			// Range Segments:
			if(this.firstRangeSegmentsId != -1 &&
					id >= this.firstRangeSegmentsId && id < (this.firstRangeSegmentsId + this.totalRangeSegments))
			{
				// Turn off the other Toggles:
				Toggle[] toggleArray	= new Toggle[this.totalRangeSegments];
				for(int i = 0; i < toggleArray.length; i++)
				{
					toggleArray[i]	= (Toggle) this.sidebarCP5.getController("toggle" + (this.firstRangeSegmentsId + i));
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
					}

					// set broadcasting back to original setting:
					toggleArray[i].setBroadcast(broadcastState[i]);
				} // for - switch off all Toggles

				// Make Buttons corresponding to non-existent thresholds unclickable:
				// (but don't do so before the Buttons have been initialized)
				if(this.firstColorSelectCWId > -1)
				{
					int	curButtonId;
					boolean	state;
					for(int i = 0; i < this.totalRangeSegments; i++)
					{
						curButtonId	= this.firstColorSelectCWId - 100 + i;

						if(i < this.curRangeSegments)	{	state	= false;		} 
						else 							{	state	= true;	}

						System.out.println("curButtonId = " + curButtonId + "; state = " + state);

						this.sidebarCP5.getController("button" + curButtonId).setLock(state);
					} // for - making Buttons unclickable

					System.out.println("this.curRangeSegments = " + this.curRangeSegments);

				}
			} // range segments
		} // Toggles

		// Key dropdown ScrollableList:
		if(controlEvent.getName().equals("keyDropdown"))
		{
			// keyPos is the position of the particular key in the Scrollable List:
			int	keyPos	= (int)controlEvent.getValue();

			// getItem returns a Map of the color, state, value, name, etc. of that particular item
			//  in the ScrollableList:
			Map<String, Object> keyMap = this.sidebarCP5.get(ScrollableList.class, "keyDropdown").getItem(keyPos);

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
				((ScrollableList)this.sidebarCP5.getController("rangeDropdown"))
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

				//				this.sidebarCP5.setAutoDraw(false);
				this.sidebarCP5.getGroup("background").setVisible(true);
				this.sidebarCP5.getGroup("background").bringToFront();
				this.sidebarCP5.getController("guideToneButton").bringToFront();

			} else {

				//				this.sidebarCP5.setAutoDraw(true);
				this.sidebarCP5.getGroup("background").setVisible(false);
			}

			this.sidebarCP5.getGroup("guideToneBackground").bringToFront();
			this.sidebarCP5.getGroup("guideToneBackground").setVisible(((Toggle) controlEvent.getController()).getBooleanValue());

		} // Guide Tone Generator

		// ADSR Presets Scrollable List:
		if(controlEvent.getName().equals("adsrPresetsDropdown"))
		{
			controlEvent.getController().bringToFront();

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
			//			controlEvent.getController().bringToFront();

			int	rangeOctave	= (int)controlEvent.getValue() + 3;

			if(rangeOctave >= 3 && rangeOctave <= 5)
			{
				this.rangeOctave	= rangeOctave;
			} else {
				throw new IllegalArgumentException("ModuleTemplate.controlEvent: rangeOctave " + rangeOctave + " is out of range.");
			}
		} // rangeDropdown

	} // controlEvent

	/**
	 * Subclasses should implement this to return a position in colors after receiving the id of a Controller
	 * (e.g., id's from customColorColorWheels and Textfields return the position in colors that corresponds
	 * to the note that they represent).
	 * 
	 * @param id	id of a Controller
	 * @return		position in colors that the change to that Controller should affect
	 */
	protected abstract int calculateNotePos(int id);

	/**
	 * Sets the Sliders and Textfields for RGB and HSB color modulate to 0
	 * and calls this.fillHSBColors().
	 */
	protected void resetModulateSlidersTextfields()
	{
		if(this.firstHSBSliderId > -1 && this.firstRGBSliderId > -1)
		{

			int	hsbId	= this.firstHSBSliderId;
			int	rgbId	= this.firstRGBSliderId;

			for(int i = 0; i < 3; i++)
			{
				this.sidebarCP5.getController("slider" + hsbId).setValue(0);
				this.sidebarCP5.getController("slider" + rgbId).setValue(0);
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
		if(this.firstRGBSliderId > -1)
		{
			int	rgbId	= this.firstRGBSliderId;

			for(int i = 0; i < 3; i++)
			{
				this.sidebarCP5.getController("slider" + rgbId).setValue(0);
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
		if(this.firstHSBSliderId > -1)
		{
			int	hsbId	= this.firstHSBSliderId;

			for(int i = 0; i < 3; i++)
			{
				this.sidebarCP5.getController("slider" + hsbId).setValue(0);
				hsbId	= hsbId + 1;
			} // for
		} else {
			System.out.println("ModuleTemplate.resetHSBSlidersTextfields: HSB sliders have not yet been initialized " +
					"(firstHSBSliderId = " + this.firstHSBSliderId + ")");
		} // else - let the user know that we ignored this method call
	} // resetHSBSlidersTextfields

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

		this.sidebarCP5.getController("keyDropdown").setValue(keyPos);

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
	 * TODO: might want this color as a float[] rather than as an int?
	 * 
	 * Returns the current color of the ColorWheel in the given position in this.colorSelect as an int.
	 * 
	 * @param colorPos	Position of color; must be from 0 to (this.colorSelect.length - 1)
	 * @return	the current color of the ColorWheel in the given position in this.colorSelect as an int
	 */
	public int[] getColor(int colorPos)
	{
		if(colorPos < 0 || colorPos >= this.colorSelect.length) {
			throw new IllegalArgumentException("ModuleTemplate.getColor: in parameter " + colorPos + 
					" is out of bounds; must be within 0 to " + (this.colorSelect.length - 1));
		} // error checking

		Color	curColor	= new Color(this.colorSelect[colorPos].getRGB());

		// Making a new array rather than using getComponents so that we will have ints, not floats:
		return	new int[] { curColor.getRed(), curColor.getGreen(), curColor.getBlue() };
	} // getColor

	/**
	 * Sets the ColorWheel at colorPos to the given color
	 * 
	 * @param colorPos	position of ColorWheel in this.colorSelect
	 * @param color	rgb color
	 */
	public void setColor(int colorPos, int[] color)
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
		if(this.firstColorSelectCWId != -1) {
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

			((ColorWheel)this.sidebarCP5.getController("colorWheel" + (this.firstColorSelectCWId + colorPos))).setRGB(colorInt);

		} else {
			System.err.println("ModuleTemplate.setColor: firstColorSelectCWId == " + this.firstColorSelectCWId + "; did not attempt to set the ColorWheel at " + colorPos + ".");
		} // if/else
	} // setColor

	public int getCheckpoint()				{	return this.checkpoint;		}

	public void setCheckpoint(int newVal)	{	this.checkpoint	= newVal;	}

	public float getThreshold()				{	return this.threshold;		}

	public void setThreshold(float newVal)	{	this.threshold	= newVal;	}

	public int[] getCurHue()				{	return this.curHue;	}

	public float getAttRelTranVal(int attRelTranPos)
	{
		return this.attRelTranVals[attRelTranPos];
	}

	public void setAttRelTranVal(int position, float val) {
		if(position < 0 || position > this.attRelTranVals.length) {
			throw new IllegalArgumentException("ModuleTemplate.setAttRelTranVal: position " + position + " is out of range; must be 0, 1, or 2.");
		} // error checking

		this.attRelTranVals[position]	= val;
	}

	public void setHueSatBrightnessMod(int position, float val) {
		if(position < 0 || position > this.hueSatBrightnessMod.length) {
			throw new IllegalArgumentException("ModuleTemplate.setHueSatBrightnessMod: position " + position + " is out of range; must be 0, 1, or 2.");
		} // error checking

		this.hueSatBrightnessMod[position]	= val;
	}

	public int getLeftEdgeX()				{	return this.leftEdgeX;	}


	public boolean isShowScale() {
		return showScale;
	}

	public void setShowScale(boolean showScale) {
		this.showScale = showScale;
	}

	public int[] getCanvasColor() {
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

	public float getShapeSize() {
		return this.shapeSize;
	}

	/**
	 * communicates with keyPressed event in draw() of driver
	 * shows menu button on key press
	 * added 1/26/17 Elena Ryan
	 */
	public void setMenuVal() {
		//this.menuVis = true;	
		((Toggle)this.sidebarCP5.getController("menuButton")).setState(false);
		this.sidebarCP5.getController("hamburger").setVisible(true);
	}//set menu val

	public float getMenuWidth()
	{
		return this.menuWidth;
	}

	public boolean menuIsOpen()
	{
		return this.menuIsOpen;
	}

} // ModuleTemplate
