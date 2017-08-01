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

	/**
	 * float[][] representing the colors that will be used by this sketch;
	 * must be initiated by child class.
	 */
	protected	float[][]	colors;

	/**	
	 * The colors used to draw the legend along the bottom of the module;
	 * will not be affected by threshold saturation or brightness sliders
	 * and must be initiated by the child class.
	 */
	protected	float[][]	legendColors;

	/**	Current hue (as opposed to the goal hue, which may not have been reached)	 */
	private	float[]			curHue;

	/**	Hue that corresponds to the current sound, but to which curHue may not yet have faded	*/
	private	float[]			goalHue;

	/**	The color when sound is below the threshold	*/
	protected	float[]		canvasColor;

	/**	The color of the background	(only applicable for modules with a shape)	*/
	//	protected	float[]		backgroundColor;

	/**	The amount that must be added every 50 or so milliseconds to fade to the goal color	*/
	private	float[]			colorAdd;

	/**	The difference between the R, G, and B values of 2 colors that are being faded between	*/
	private	float[]			colorRange;

	/**	The current colors which hsb sliders are altering; must be initiated by child class.	*/
	protected float[][]   hsbColors; //the current colors at which hsb is altering

	/**	Filled in the Custom color style to allow RGB modifications to colors;	must be initiated by child class.	*/
	protected	float[][]	originalColors;

	/**	Flags whether the curHue R, G, and B values have come within an acceptable range of the goalHue	*/
	private boolean[]	colorReachedArray;

	/**	True if all values in the colorReachedArray are true; used to determine fade speed (whether this is attack, release, or transition)	*/
	private	boolean		colorReached;

	/**	Input from which the class will get all its audio data	*/
	protected	Input	input;

	/**	Volume below which input will be ignored	*/
	protected float	threshold;

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

	/**	
	 * All of the following are id's that have the potential to be used in controlEvent;
	 * if a child class adds any of these components (e.g., a canvasColorSelect Button or RGB mod sliders),
	 * they should initiate the corresponding one of these variables to the id of either the Button or Slider in question.
	 */
	protected	int	canvasColorSelectId	= -1;
	//	protected	int	backgroundColorSelectId	= -1;
	protected	int	firstColorModSliderId	= -1;
	protected	int	firstColorSelectId	= -1;
	protected	int	lastColorSelectId	= -1;
	protected	int	firstCustomColorId	= -1;
	protected	int	thresholdSliderId	= -1;
	protected	int	firstARTSliderId	= -1;
	protected	int	firstHSBSliderId	= -1;
	protected	int	firstRGBSliderId	= -1;
	protected	int	bpmSliderId			= -1;
	protected	int	volumeSliderId		= -1;
	protected	int	shapeSizeSliderId	= -1;
	protected	int	firstRangeSegmentsId	= -1;

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
	public ModuleTemplate(PApplet parent, Input input, String sidebarTitle)
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

		this.curHue				= new float[3];
		this.goalHue			= new float[3];
		this.canvasColor		= new float[] { 1, 0, 0 };	// If this is set to rgb(0, 0, 0), the CW gets stuck in grayscale
		this.colorAdd			= new float[3];
		this.colorRange			= new float[3];

		this.colorReachedArray	= new boolean[] { false, false, false };
		this.colorReached		= false;
		this.nowBelow			= false;

		this.attRelTranPos	= 0;	// 0 = attack, 1 = release, 2 = transition
		this.attRelTranVals	= new float[] {		200, 200, 200	};	// attack, release, transition all begin at 200 millis
		this.hueSatBrightnessMod        = new float[3];
		this.hueSatBrightPercentMod		= new float[3];

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
		//		.setPosition(this.originalLeftEdgeX, 0)
		.setPosition(0, 0)
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

				//				this.attackReleaseTransition[i - 1]	= startingValue;
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

		/*
		String[] keyOptions	= new String[] {
				"A", "A#", "Bb", "B", "C", "C#/Db", "D", "D#/Eb", "E", "F", "F#/Gb", "G", "G#/Ab"
		};
		 */

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
		// "Pop-out with range drop-down and envelope preset Buttons" and BPM.

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
		int		textfieldX		= boxWidth - popoutSpacer - textfieldWidth;

		int		rangeY			= popoutSpacer;
		int		adsrY			= (popoutSpacer * 2) + height;
		int		bpmY			= (popoutSpacer * 3) + (height * 2);
		int		volumeY			= (popoutSpacer * 4) + (height * 3);

		this.sidebarCP5.addGroup("guideToneBackground")
		.setPosition(this.leftAlign, guideToneY + 20)
		.setSize(boxWidth, boxHeight)
		//		.setBackgroundColor(this.parent.color(150, 50, 150))	// <-- testing purposes
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

		//		int	id	= 20;

		this.bpmSliderId	= this.nextSliderId;
		this.volumeSliderId	= this.nextSliderId + 1;

		for(int i = 0; i < labels.length; i++)
		{
			// BPM Textlabel:
			this.sidebarCP5.addLabel(labels[i])
			.setPosition(popoutSpacer, yVals[i] + 4)
			//		.setWidth(labelWidth)
			.setGroup("guideToneBackground")
			.setValue(labelVals[i]);

			this.sidebarCP5.addSlider("slider" + this.nextSliderId)
			.setPosition(listSliderX, yVals[i])
			.setSize(sliderWidth, height)
			.setSliderMode(Slider.FLEXIBLE)
			.setRange(ranges[i][0], ranges[i][1])
			.setValue(startingVals[i])
			.setLabelVisible(false)
			.setGroup("guideToneBackground")
			.setId(this.nextSliderId);

			this.nextSliderId	= this.nextSliderId + 1;

			this.sidebarCP5.addTextfield("textfield" + this.nextSTextfieldId)
			.setPosition(textfieldX, yVals[i])
			.setSize(textfieldWidth, height)
			.setText(this.sidebarCP5.getController("slider" + (this.nextSTextfieldId - 100)).getValue() + "")
			.setLabelVisible(false)
			.setAutoClear(false)
			.setGroup("guideToneBackground")
			.setId(this.nextSTextfieldId)
			.getCaptionLabel().setVisible(false);

			this.nextSTextfieldId	= this.nextSTextfieldId + 1;
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

		//		int	id	= 14;		// this id picks up where the modulate sliders leave off

		this.firstHSBSliderId	= this.nextSliderId;

		for(int i = 0; i < hsb.length; i++)
		{
			// - Textlabel:
			this.sidebarCP5.addLabel(names[i])
			.setPosition(labelX, hsb[i] + 4)
			.setWidth(labelWidth)
			.setGroup("sidebarGroup")
			.setValue(values[i]);

			//	- Slider:
			this.sidebarCP5.addSlider("slider" + this.nextSliderId)
			.setPosition(this.leftAlign, hsb[i])
			.setSize(sliderWidth, this.sliderHeight)
			.setSliderMode(Slider.FLEXIBLE)
			.setRange(-1, 1)
			.setValue(0)
			.setGroup("sidebarGroup")
			.setId(this.nextSliderId);

			this.nextSliderId	= this.nextSliderId + 1;

			// - Textfield:
			this.sidebarCP5.addTextfield("textfield" + this.nextSTextfieldId)
			.setPosition(this.leftAlign + sliderWidth + spacer, hsb[i])
			.setSize(tfWidth, this.sliderHeight)
			.setText(this.sidebarCP5.getController("slider" + (this.nextSTextfieldId-100)).getValue() + "")
			.setAutoClear(false)
			.setGroup("sidebarGroup")
			.setId(this.nextSTextfieldId)
			.getCaptionLabel().setVisible(false);

			this.nextSTextfieldId	= this.nextSTextfieldId + 1;
		} // for   
	}//the HSB Sliders Heavily Adapted from modSlider Method

	/**
	 * Method called during instantiation, to initialize the RGB color modulate sliders.
	 * 
	 * @param modulateYVals	int[] of the y values of the red, green, and blue sliders, respectively.
	 */
	protected void addModulateSliders(int[] modulateYVals)
	{
		int	labelX			= 10;
		int	labelWidth		= 70;

		int	sliderWidth		= 170;

		int	spacer			= 5;	// distance between slider and corresponding textfield
		int	tfWidth			= 40;	// width of Textfields

		String[]	names	= new String[] { "redModLabel", "greenModLabel", "blueModLabel" };
		String[]	values	= new String[] { "Red Modulate", "Green Mod.", "Blue Modulate" };

		//		int	id	= 8;		// this id picks up where the transition textfield - "textfield7" - left off.

		this.firstRGBSliderId	= this.nextSliderId;
		for(int i = 0; i < modulateYVals.length; i++)
		{
			// - Textlabel:
			this.sidebarCP5.addLabel(names[i])
			.setPosition(labelX, modulateYVals[i] + 4)
			.setWidth(labelWidth)
			.setGroup("sidebarGroup")
			.setValue(values[i]);

			//	- Slider:
			this.sidebarCP5.addSlider("slider" + this.nextSliderId)
			.setPosition(this.leftAlign, modulateYVals[i])
			.setSize(sliderWidth, this.sliderHeight)
			.setSliderMode(Slider.FLEXIBLE)
			.setRange(-255, 255)
			//			.setValue(0)
			//.setLabelVisible(false)
			.setGroup("sidebarGroup")
			.setId(this.nextSliderId);

			this.nextSliderId	= this.nextSliderId + 1;

			//	- Textlabel:
			this.sidebarCP5.addTextfield("textfield" + this.nextSTextfieldId)
			.setPosition(this.leftAlign + sliderWidth + spacer, modulateYVals[i])
			.setSize(tfWidth, this.sliderHeight)
			.setText(this.sidebarCP5.getController("slider" + (this.nextSTextfieldId-100)).getValue() + "")
			.setAutoClear(false)
			.setGroup("sidebarGroup")
			.setId(this.nextSTextfieldId)
			.getCaptionLabel().setVisible(false);

			this.nextSTextfieldId	= this.nextSTextfieldId + 1;
		} // for
	} // addModulateSliders

	/**
	 * Adds the slider for adjusting shape size
	 * 
	 * @param yVal	y value for this slider
	 */
	protected void addShapeSizeSlider(int yVal)
	{
		this.shapeSize	= 50;

		// first add label:
		this.sidebarCP5.addLabel("shapeSize")
		.setPosition(this.labelX, yVal)
		.setValue("Shape Size")
		.setGroup("sidebarGroup");

		this.shapeSizeSliderId	= this.nextSliderId;

		this.sidebarCP5.addSlider("slider" + this.nextSliderId)
		.setPosition(this.leftAlign, yVal)
		.setSize(this.sliderWidth, this.sliderHeight)
		.setSliderMode(Slider.FLEXIBLE)
		.setRange(1, 100)
		.setValue(this.shapeSize)
		.setLabelVisible(false)
		.setGroup("sidebarGroup")
		.setId(this.nextSliderId);

		this.nextSliderId	= this.nextSliderId + 1;

		this.sidebarCP5.addTextfield("textfield" + this.nextSTextfieldId)
		.setPosition(this.leftAlign + sliderWidth + this.spacer, yVal)
		.setSize(this.textfieldWidth, this.sliderHeight)
		.setText(this.sidebarCP5.getController("slider" + (this.nextSTextfieldId - 100)).getValue() + "")
		.setAutoClear(false)
		.setGroup("sidebarGroup")
		.setId(this.nextSTextfieldId)
		.getCaptionLabel().setVisible(false);

		this.nextSTextfieldId	= this.nextSTextfieldId + 1;
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
		//		.setWidth(this.labelWidth)
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
	 * Sets this.goalHue to the value of the given position in this.colors
	 * (important that it sets this in colors and not legendColors, since colors
	 * has the correct saturation and brightness for this particular volume)
	 * 
	 * @param position	either a position in colors or -1 for canvas color
	 */
	public void setGoalHue(int position)
	{
		if(position > this.getColors().length || position < -1) {
			throw new IllegalArgumentException("ModuleTemplate.getGoalHue: position " + position + 
					" is out of bounds; must be between -1 (signifying canvas color) or " + (getColors().length - 1));
		} // error checking

		if(position == -1)	
		{	
			for(int i = 0; i < this.goalHue.length; i++)
			{
				System.out.println("setGoalHue: canvasColor[" + i + "] " + this.canvasColor[i]);
				this.goalHue[i]	= this.canvasColor[i];
			} // for - canvas
		} else {	
			for(int i = 0; i < this.goalHue.length; i++)
			{
				this.goalHue[i]	= this.colors[position][i];	
			} // for - colors
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
		if(position > this.getColors().length || position < -1) {
			throw new IllegalArgumentException("ModuleTemplate.getGoalHue: position " + position + 
					" is out of bounds; must be between -1 (signifying canvas color) or " + (getColors().length - 1));
		} // error checking

		if(this.input.getAmplitude() < this.threshold)	
		{
			this.nowBelow	= true;

			for(int i = 0; i < this.goalHue.length; i++)
			{
				this.goalHue[i]	= this.canvasColor[i];
				//				System.out.println("this.canvasColor[" + i + "] = " + this.canvasColor[i] + 
				//						"; this.goalHue[" + i + "] = " + this.goalHue[i]);
			} // for - canvas

		} else {
			this.nowBelow	= false;

			for(int i = 0; i < this.goalHue.length; i++)
			{
				this.goalHue[i]	= this.colors[position][i];	
			} // for - colors

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
					else
					{
						//					System.out.println("It thinks that curHue[" + i + "] is set; curHue[" + i + "] = " + this.curHue[i] + "; goalHue[" + i + "] = " + this.goalHue[i]);
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

		//		System.out.print("amplitude = " + this.input.getAmplitude() + ": ");

		// If coming from a low amplitude note and not yet reaching a color,
		// use the attack value to control the color change:
		if(!this.nowBelow && !colorReached) 
		{	
			this.attRelTranPos	= 0;
			//			System.out.println("attack!!!!");
		} else if(!this.nowBelow && colorReached) {
			// Or, if coming from one super-threshold note to another, use the transition value:
			this.attRelTranPos	= 2;
			//			System.out.println("transition.... transition [doooooo do dooo do do ] - transition!");
		} else if(this.nowBelow) {
			// Or, if volume fell below the threshold, switch to release value:
			this.attRelTranPos	= 1;
			//			System.out.println("re....lent! re...coil! re...verse!");
		}

		if(this.attRelTranPos != oldARTpos)
		{			
			// Calculate color ranges:
			for(int i = 0; i < this.curHue.length; i++)
			{
				this.colorRange[i]	= Math.abs(this.goalHue[i] - this.curHue[i]);

				// divide the attack/release/transition value by 50
				// and divide colorRange by that value to find the amount to add each 50 millis.
				this.colorAdd[i]	= this.colorRange[i] / (this.attRelTranVals[this.attRelTranPos] / 50);
			} // for
		} // if

	} // fade

	/**
	 * Classes using ModuleTemplate should call this method in setup() with a position in colors.
	 * 
	 * @param curHuePos	position in ModuleTemplate.colors
	 */
	public void setCurHueColorRangeColorAdd(int curHuePos)
	{
		if(curHuePos < 0 || curHuePos > this.colors.length) {
			throw new IllegalArgumentException("Module_01_02.setup(): curHuePos " + curHuePos + " is out of the bounds of the colors; " + 
					"must be between 0 and " + this.colors.length);
		} // error checking

		this.curHue	= new float[] { 255, 255, 255 };
		// The following line caused problems!
		// (That is, it made that position in colors follow curHue as the latter changed.)
		// Never use it.
		//		curHue	= this.colors[curHuePos];

		for(int i = 0; i < this.curHue.length; i++)
		{
			this.colorRange[i]	= Math.abs(this.goalHue[i] - this.curHue[i]);

			// divide the attack/release/transition value by 50
			// and divide colorRange by that value to find the amount to add each 50 millis.
			this.colorAdd[i]	= this.colorRange[i] / (this.attRelTranVals[this.attRelTranPos] / 50);
		}
	} // setCurHue

	/**
	 * Applies the values of the Red Modulate/Green Modulate/Blue Modulate sliders and 
	 * calls applyHSBModulate() to apply the values of the Hue/Saturation/Brightness Modulate sliders.
	 */
	protected void applyColorModulate(float[][] colors, float[][] originalColors)
	{
		if(colors == null || originalColors == null) {
			throw new IllegalArgumentException("ModuleTemplate.applyColorModulate: one of the float[] parameters is null (colors = " + colors + "; originalColors = " + originalColors);
		} // error checking

		for(int i = 0; i < colors.length; i++)
		{
			for(int j = 0; j < colors[i].length; j++)
			{
				// Adds redModulate to the red, greenModulate to the green, and blueModulate to the blue:
				colors[i][j]	= originalColors[i][j] + this.redGreenBlueMod[j];

			} // for - j
		} // for - i
		/*
		// TODO: this might cause problems.... 
		// applyHSBModulate will be called on this.colors twice and this.legendColors only once.
		this.fillHSBColors();
		 */
		this.applyHSBModulate(this.colors, this.hsbColors);
	} // applyColorModulate

	/**
	 * Applies the values from this.hueSatBrightnessMod to the contents of the colors array parameter.
	 * @param colors	this.colors or this.legendColors
	 * @param hsbColors	this.hsbColors
	 */
	protected void applyHSBModulate(float[][] colors, float[][] hsbColors)
	{
		if(colors == null || hsbColors == null) {
			throw new IllegalArgumentException("ModuleTemplate.applyHSBModulate: one of the float[] parameters is null (colors = " + colors + "; hsbColors = " + hsbColors);
		} // error checking

		float[] hsb = new float[3];

		for (int i = 0; i < colors.length; i++)
		{
			// Converts this position of hsbColors from RGB to HSB:
			Color.RGBtoHSB((int)hsbColors[i][0], (int)hsbColors[i][1], (int)hsbColors[i][2], hsb);

			// Applies the status of the sliders to the newly-converted color:
			hsb[0] = (hsb[0] + this.hueSatBrightnessMod[0] + this.hueSatBrightPercentMod[0] + 1) % 1;
			hsb[1] = Math.max(Math.min(hsb[1] + this.hueSatBrightnessMod[1] + this.hueSatBrightPercentMod[1], 1), 0);
			hsb[2] = Math.max(Math.min(hsb[2] + this.hueSatBrightnessMod[2] + this.hueSatBrightPercentMod[2], 1), 0);

			// Converts the color back to RGB:
			int oc = Color.HSBtoRGB(hsb[0], hsb[1],  hsb[2]);
			Color a = new Color(oc);

			// Fills colors with the new color:
			colors[i][0] = (float)a.getRed();
			colors[i][1] = (float)a.getGreen();
			colors[i][2] = (float)a.getBlue();
		} // for
	} // applyHSBModulate

	/**
	 * Puts the contents of this.colors into this.hsbColors;
	 * must be called when colors is initialized.
	 */
	protected	void fillHSBColors()
	{
		if(this.colors == null)
		{
			throw new IllegalArgumentException("ModuleTemplate.fillHSBColors: this.colors is null.");
		}

		if(this.hsbColors == null) {
			this.hsbColors = new float[this.colors.length][3];
		}

		for(int i = 0; i < this.hsbColors.length; i++)
		{
			for(int j = 0; j < this.hsbColors[i].length; j++)
			{
				this.hsbColors[i][j]	= this.colors[i][j];
			}
		} // for
	} // fillhsbColors

	/**
	 * Puts the contents of colors into originalColors.
	 */
	protected void fillOriginalColors()
	{
		if(this.colors == null) {
			throw new IllegalArgumentException("ModuleTemplate.fillOriginalColors: this.colors is null.");
		}

		if(this.originalColors == null) {
			this.originalColors = new float[this.colors.length][3];
		}

		for(int i = 0; i < this.originalColors.length; i++)
		{
			for(int j = 0; j < this.originalColors[i].length; j++)
			{
				this.originalColors[i][j]	= this.colors[i][j];
			}
		} // for
	} // fillOriginalColors

	/**
	 * Calls playMelody(key, bpm, scale, rangeOctave) with the curKey, bpm, rangeOctave instance vars
	 * and the string corresponding to the majMinChrom instance var ("major", "minor", or "chromatic").
	 */
	private void playMelody()
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
		System.out.println("ModuleTemplate.controlEvent: controlEvent = " + controlEvent);

		if(controlEvent.getController().getName().equals("stopContext"))
		{
			System.out.println("	here's where we'll try to stop the AudioContext");
			
			this.input.ac.stop();
		}
		
		int	id	= controlEvent.getController().getId();
		// Play button:
		if(controlEvent.getController().getName().equals("play"))
		{
			boolean	val	= ((Toggle)controlEvent.getController()).getBooleanValue();
			this.input.pause();
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

					// Only fill hsbColors the first time (to avoid over modulating):
					if(this.hsbColors == null)
					{
						this.fillHSBColors();
					}
					this.applyHSBModulate(this.colors, this.hsbColors);
					this.applyHSBModulate(this.legendColors, this.hsbColors);
				}//hsb mod

				// Red Modulate/Green Modulate/Blue Modulate:
				if(this.firstRGBSliderId != -1 &&
						id >= this.firstRGBSliderId && id < (this.firstRGBSliderId + 3))
				{
					//			int	pos	= (id / 2) - 4;		
					int	pos	= id - this.firstRGBSliderId;	// red = 0, green = 1, blue = 2

					this.redGreenBlueMod[pos]	= sliderValFloat;
					this.applyColorModulate(this.colors, this.originalColors);
					this.applyColorModulate(this.legendColors, this.originalColors);
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
					this.shapeSize	= (int)sliderValFloat;
				}

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
			//			this.fillHSBColors();

			// Reset whichever of the sliders is applicable:
			if(this.firstRGBSliderId > -1)
			{
				this.resetRGBSlidersTextfields();
			}
			if(this.firstHSBSliderId > -1)
			{
				this.resetHSBSlidersTextfields();
			}

			// Apply the colorModuleate
			if(this.firstRGBSliderId > -1 && this.firstHSBSliderId > -1)
			{
				this.applyColorModulate(this.colors, this.originalColors);
				this.applyColorModulate(this.legendColors, this.originalColors);
			} else {
				if(this.firstHSBSliderId > -1)
				{
					this.applyHSBModulate(this.colors, this.hsbColors);
					this.applyHSBModulate(this.legendColors, this.hsbColors);
				}
			} // if - RGBSliderId > -1

		} // Color Select Buttons

		// ColorWheels
		if(id > 299 && id < 400)
		{
			System.out.println("ColorWheel event from id " + id);
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

				if(this.nowBelow)
				{
					this.curHue[0]	= color.getRed();
					this.curHue[1]	= color.getGreen();
					this.curHue[2]	= color.getBlue();
				}
			} // if - canvas
			/*			else if( ( this.backgroundColorSelectId != -1 ) && 
					( ( id % 100 ) == ( this.backgroundColorSelectId % 100 ) ) )
			{
				this.backgroundColor[0]	= color.getRed();
				this.backgroundColor[1]	= color.getGreen();
				this.backgroundColor[2]	= color.getBlue();
			} // if - background
			 */
			else
			{
				int	colorPos	= this.calculateNotePos(id);

				System.out.println("colorPos = " + colorPos);

				this.colors[colorPos][0]	= color.getRed();
				this.colors[colorPos][1]	= color.getGreen();
				this.colors[colorPos][2]	= color.getBlue();

				this.legendColors[colorPos][0]	= color.getRed();
				this.legendColors[colorPos][1]	= color.getGreen();
				this.legendColors[colorPos][2]	= color.getBlue();

			} // else - not canvas

			// Fill HSB colors so that it's always up to date
			// (tried filling it in the Button part of controlEvent, but that wasn't enough).
			this.fillHSBColors();

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
				if(this.firstColorSelectId > -1)
				{
					int	curButtonId;
					boolean	state;
					for(int i = 0; i < this.totalRangeSegments; i++)
					{
						curButtonId	= this.firstColorSelectId + i;

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
				System.out.println("this.sidebarCP5.getController('rangeDropdown') = " + this.sidebarCP5.getController("rangeDropdown"));
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
				System.out.println("this.sidebarCP5.getGroup('background') = " + this.sidebarCP5.getGroup("background"));
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
	 * Sets the Sliders and Textfields for RGB and HSB color modulate to 0.
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

		// The following happen in controlEvent - "keyDropdown"
		/*
		this.curKey			= key;
		this.setKeyAddVal(modPosition);
		 */

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
	}

	/**
	 * Updates the ColorWheel with the given id to the color at colorPos in this.colors
	 * 
	 * @param id	int indicating the ColorWheel to be updated
	 * @param colorPos	int indicating the position in colors that will be used to set the ColorWheel
	 */
	protected void updateColorWheelFromColors(int id, int colorPos)
	{
		if(colorPos < 0 || colorPos >= this.colors.length)	{
			throw new IllegalArgumentException("ModuleTemplate01.updateColorWheel: colorPos parameter " + colorPos + " is out of bounds; must be within 0 and " + (this.colors.length - 1));
		} // error checking

		int	red		= (int)this.colors[colorPos][0];
		int	green	= (int)this.colors[colorPos][1];
		int	blue	= (int)this.colors[colorPos][2];

		// Constrain to 0-255:
		red		= Math.min(255, Math.max(0, red));
		green	= Math.min(255, Math.max(0, green));
		blue	= Math.min(255, Math.max(0, blue));

		// Set corresponding ColorWheel:
		//					Color	rgbColor	= new Color(this.tonicColor[0], this.tonicColor[1], this.tonicColor[2]);
		Color	rgbColor	= new Color(red, green, blue);
		int		rgbInt		= rgbColor.getRGB();
		System.out.println("this.sidebarCP5.getController(colorWheel" + id + ") = " + this.sidebarCP5.getController("colorWheel" + id));
		((ColorWheel)this.sidebarCP5.getController("colorWheel" + id)).setRGB(rgbInt);
	} // updateColorWheel

	/**
	 * Updates the ColorWheel with the given id to the color at colorPos in this.legendColors
	 * 
	 * @param id	int indicating the ColorWheel to be updated
	 * @param colorPos	int indicating the position in colors that will be used to set the ColorWheel
	 */
	protected void updateColorWheelFromLegendColors(int id, int colorPos)
	{
		if(colorPos < 0 || colorPos >= this.colors.length)	{
			throw new IllegalArgumentException("ModuleTemplate01.updateColorWheel: colorPos parameter " + colorPos + " is out of bounds; must be within 0 and " + (this.colors.length - 1));
		} // error checking

		int	red		= (int)this.legendColors[colorPos][0];
		int	green	= (int)this.legendColors[colorPos][1];
		int	blue	= (int)this.legendColors[colorPos][2];

		// Constrain to 0-255:
		red		= Math.min(255, Math.max(0, red));
		green	= Math.min(255, Math.max(0, green));
		blue	= Math.min(255, Math.max(0, blue));

		// Set corresponding ColorWheel:
		//					Color	rgbColor	= new Color(this.tonicColor[0], this.tonicColor[1], this.tonicColor[2]);
		Color	rgbColor	= new Color(red, green, blue);
		int		rgbInt		= rgbColor.getRGB();
		System.out.println("this.sidebarCP5.getController(colorWheel" + id + ") = " + this.sidebarCP5.getController("colorWheel" + id));
		((ColorWheel)this.sidebarCP5.getController("colorWheel" + id)).setRGB(rgbInt);
	} // updateColorWheel

	public int getCheckpoint()				{	return this.checkpoint;		}

	public void setCheckpoint(int newVal)	{	this.checkpoint	= newVal;	}

	public float getThreshold()				{	return this.threshold;		}

	public void setThreshold(float newVal)	{	this.threshold	= newVal;	}

	public float[][] getColors() {
		return colors;
	}

	public float[] getCurHue()				{	return this.curHue;	}

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

	public float[] getCanvasColor() {
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

	/*
	public float getHueModulate() {
		return this.hueSatBrightnessMod[0];
	}

	public float getSaturationModulate() {
		return this.hueSatBrightnessMod[1];
	}

	public float getBrightnessModulate() {
		return this.hueSatBrightnessMod[3];
	}
	 */

	public int getCurKeyEnharmonicOffset() {
		return curKeyEnharmonicOffset;
	}

	public float getShapeSize() {
		return this.shapeSize;
	}

	public ControlP5 getSidebarCP5()
	{
		return this.sidebarCP5;
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

	/*	public float[] getBackgroundColor()
	{
		return this.backgroundColor;
	}
	 */
} // ModuleTemplate
