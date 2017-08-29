package core;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

import controlP5.Button;
import controlP5.ColorWheel;
import controlP5.ControlEvent;
import controlP5.ControlFont;
import controlP5.ControlP5;
import controlP5.ScrollableList;
import controlP5.Slider;
import controlP5.Textfield;
import controlP5.Toggle;
import processing.core.PApplet;
import processing.core.PImage;

/**
 * (For more info on updates beyond this point, see Trello board Module_01 PitchHue 
 *  and perhaps others as the modules progress.)
 * 
 * 06/16/2017 - ModuleTemplate_01_04
 * Emily Meuer, Dan Mahota, Kristen Andrews update:
 *  - Guide tones generated, not played through .mp3's
 *  - Guide tone generator options in pop-out
 *  - Pause functionality
 *  - Textfield labels invisible
 *  - Hue slider wraps around (instead of just turning red for all extreme values)
 * 	- Attack, Release, and Transition sliders are time-based (define the number of millis() each action will take).
 * 
 * 06/08/2017 - ModuleTemplate_01_03
 * Emily Meuer update:
 *  - Trichromatic and Dichromatic work completely, with independent color select, too.
 *  - VOLUME THROUGHPUT ISSUE is SOLVED!!! :D PTL!
 * 
 * 06/07/2017
 * Emily Meuer update:
 *  - working HSB and RGB modulate sliders
 *  - canvas color changes independently
 *  - Trichromatic works; Dichromatic 2nd Color works, but tonic still changes both tonic and 2nd Color (ruining the fade)
 *  - customColorSelect works in all keys
 * 
 * Future:
 *  - ControlListener that takes everything and doesn't require Modules to have controlEvent()?
 *    (see A.S. answer: https://forum.processing.org/two/discussion/2692/controlp5-problems-creating-a-toggle-controller-with-custom-images-on-a-second-tab)
 * - Custom controllers: http://www.sojamo.de/libraries/controlP5/reference/controlP5/ControllerView.html
 * 
 * Emily Meuer
 * 01/11/2017
 * 
 * Custom sidebar, originally for Module_01_02.
 * 
 * @author Emily Meuer et alia
 * 
 * Elena Ryan
 * 2/13/17 Edits
 * In order to make the Sidebar compatible with fullscreen 
 * must pass some value to Module Template that scales things 
 * appropriately for fullscreen
 * tests below
 * Rev: should add HSB sliders, etc., before calibrating fullscreen meas.
 * https://processing.org/reference/hue_.html gives
 * info on conversion of colors
 *
 */
public class ModuleTemplate01 extends ModuleTemplate {

	// TODO: change to private after testing
	public	static	float	CS_RAINBOW	= 1;
	public	static	float	CS_DICHROM	= 2;
	public	static	float	CS_TRICHROM	= 3;
	public  static	float	CS_CUSTOM	= 4;
	private	float	curColorStyle;
	//	private boolean menuVis = false;

	// TODO: get rid of this (use local variable) later:
	public float[][] trichromColors;

	/**
	 * DecimalFormat used for rounding the text corresponding to Sliders and Colorwheels.
	 */
	//	private	DecimalFormat	decimalFormat	= new DecimalFormat("#.##");

	//	String  inputFile;

	private	PApplet		parent;
	//	public ControlP5 	nonSidebarCP5;
	// TODO set private post-testing:
	//	public ControlP5 	sidebarCP5;
	private	Input		input;


	//	private	int			leftAlign;
	//	private	int			leftEdgeX;

	//	private	String		sidebarTitle;

	//	private	int			majMinChrom;
	//	private	String		curKey;
	//	private	int			scaleLength;
	//	private int 		curKeyOffset;
	//	private int 		curKeyEnharmonicOffset;
	// to line pitches up with the correct scale degree of the current key.
	/*
	private	final String[]	notesAtoAbFlats	= new String[] { 
			"A", "Bb", "B", "C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab"
	};

	private final String[]	notesAtoGSharps	= new String[] { 
			"A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#"
	};
	 */
	/*	// ALL notes here
	private	final String[]	allNotes	= new String[] {
			"A", "A#", "Bb", "B", "C", "C#", "Db", "D", "D#", "Eb", "E", "F", "F#", "Gb", "G", "G#", "Ab"
	}; // allNotes
	 */
	// Positions in filenames String[] here
	/*	private	final int[]	enharmonicPos	= new int[] {
			0, 1, 1, 2, 3, 4, 4, 5, 6, 6, 7, 8, 9, 9, 10, 11, 11
	}; // enharmonicPos
	 */
	// File names here
	/*	private	final	String[][] filenames	= new String[][] {
		// major:
		new String[] {
				"Major/A Major.wav",
				"Major/A#_Bb Major.wav",
				"Major/B Major.wav",
				"Major/C Major.wav",
				"Major/C#_Db Major.wav",
				"Major/D Major.wav",
				"Major/D#_Eb Major.wav",
				"Major/E Major.wav",
				"Major/F Major.wav",
				"Major/F#_Gb Major.wav",
				"Major/G Major.wav",
				"Major/G#_Ab Major.wav"
		},
		// minor:
		new String[] {
				"Minor/A Minor.wav",
				"Minor/A#_Bb Minor.wav",
				"Minor/B Minor.wav",
				"Minor/C Minor.wav",
				"Minor/C#_Db Minor.wav",
				"Minor/D Minor.wav",
				"Minor/D#_Eb Minor.wav",
				"Minor/E Minor.wav",
				"Minor/F Minor.wav",
				"Minor/F#_Gb Minor.wav",
				"Minor/G Minor.wav",
				"Minor/G#_Ab Minor.wav"
		},
		// chromatic:
		new String[] {
				"Chromatic/A Chromatic.wav",
				"Chromatic/A#_Bb Chromatic.wav",
				"Chromatic/B Chromatic.wav",
				"Chromatic/C Chromatic.wav",
				"Chromatic/C#_Db Chromatic.wav",
				"Chromatic/D Chromatic.wav",
				"Chromatic/D#_Eb Chromatic.wav",
				"Chromatic/E Chromatic.wav",
				"Chromatic/F Chromatic.wav",
				"Chromatic/F#_Gb Chromatic.wav",
				"Chromatic/G Chromatic.wav",
				"Chromatic/G#_Ab Chromatic.wav"
		}
	}; // filenames
	 */
	private	final	int[][] scaleDegrees = new int[][] {
		// major:
		new int[]  { 0, 2, 4, 5, 7, 9, 11
		},
		// minor:
		new int[]  { 0, 2, 3, 5, 7, 8, 10
		},
		// chromatic:
		new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11
		}
	}; // scaleDegrees

	// Each int signifies the position in dichromColors/trichromColors/rainbowColors that is used to fill 
	// this.colors at the corresponding position in scaleDegreeColors[this.majMinChrom]:
	private	final	int[][]	scaleDegreeColors	= new int[][] {
		// major:
		new int[] { 0, 0, 1, 2, 2, 3, 4, 4, 4, 5, 6, 6 },
		// minor:
		new int[] { 0, 0, 1, 2, 2, 3, 4, 4, 5, 5, 6, 6 },
		// chromatic:
		new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 }
	}; // scaleDegreeColors

	//private	float[][]	colors;
	//	private	float[][]	originalColors;	// filled in the Custom color style to allow RGB modifications to colors
	//	private float[][]   hsbColors; //the current colors at which hsb is altering
	//	private	float[]		canvasColor;	// the color of the background when there is no sound.

	int[]				textYVals;
	int[]				noteYVals;
	int[]				modulateYVals;
	int[]               modulateHSBVals;
	int					colorSelectY;

	//	private	boolean		showScale;

	//	private	float		threshold;

	//	private	float[]		attackReleaseTransition;

	//	private	float[]	redGreenBlueMod;	// this will store the red/green/blue modulate values

	//	private float[] hueSatBrightnessMod; // This will store the hsb modulate values

	private	boolean	dichromFlag;
	private	boolean	trichromFlag;

	//	private	int		checkpoint;		// For a timer that allows attack/release/transition sliders to be time-based.

	// TODO: make private:
	//	public Melody		melody;
	//	public	Instrument	instrument;
	//	private	int			bpm;
	//	private	int			rangeOctave;

	// Sliders/Textfields and Buttons/ColorWheels/Textfields all connected by id's:
	// 		Sliders: id's start at 0; id % 2 == 0
	//		Textfields: id's start at 1; id % 2 == 1
	//
	// Color select:
	// 		Button's: id % 3 == 0
	// 		ColorWheel's: id % 3 == 1
	// 		TextField's: id % 3 == 2
	//	private	int			lastTextfieldId		= 23;
	//	private	int			volumeSliderId		= 22;
	//	private	int			volumeTextfieldId	= lastTextfieldId;

	//	private	int			firstCustomColorId	= -1;

	//	private	int			canvasColorSelectId	= 63;
	//	private	int			firstColorSelectId;
	//	private	int			lastColorSelectId;
	//	private	int	firstHSBSliderId;
	//	private	int	firstRGBSliderId;

	public ModuleTemplate01(PApplet parent, Input input, String sidebarTitle)
	{
		super(parent, input, sidebarTitle);

		this.parent	= parent;
		this.input	= input;

		// ControlP5 for playButton and hamburger - now put them on the same ControlP5, with a group for the sidebar controls:
		//		this.nonSidebarCP5	= new ControlP5(this.parent);

		// ControlP5 for most of the sidebar elements:
		//		this.sidebarCP5		= new ControlP5(this.parent);

		// This technically works, but it's horribly blurry:
		//		this.sidebarCP5.setFont(this.parent.createFont("Consolas", 10) );


		// ControlP5 for ColorWheels (having a separate one allows us to setAutoDraw(false) on the other CP5,
		// draw a transparent rectangle over those controllers, and then draw the ColorWheel on top of that):
		//		this.colorWheelCP5	= new ControlP5(this.parent);
		//		this.sidebarCP5.setVisible(false);


		//		this.leftEdgeXArray	= new int[] { 0, this.parent.width / 3 };
		//	this.setLeftEdgeX(0);
		//		this.leftAlign	= (this.parent.width / 3) / 4;

		//		this.sidebarTitle	= sidebarTitle;

		this.colors = new float[12][3];
		this.originalColors	= new float[12][3];
		this.hsbColors      = new float[12][3];
		//		this.canvasColor	= new float[] { 0, 0, 0 }; // canvas is black to begin with.

		this.curColorStyle	= ModuleTemplate01.CS_RAINBOW;
		// The following will happen in rainbow():
		//		this.tonicColor	= new int[] { 255, 0, 0, };
		this.dichromFlag	= false;
		this.trichromFlag	= false;

		//		this.rainbow();

		/*
		this.curKey			= "A";
		this.majMinChrom	= 2;
		 */
		// Can't call setCurKey just yet, because the dropdown list hasn't been initialized,
		// and it is called as part of setCurKey.
		//		this.setCurKey(this.curKey, this.majMinChrom);

		// textYVals will be used for sliders and buttons, including hsb and 
		// rgb modulate values - everything but the custom pitch color buttons.
		this.textYVals		 = new int[16];
		this.noteYVals		 = new int[3];
		this.modulateYVals	 = new int[3];
		this.modulateHSBVals = new int[3];

		//		this.attackReleaseTransition	= new float[3];
		this.redGreenBlueMod		 	= new float[3];
		//		this.hueSatBrightnessMod        = new float[3];
		/*
		this.checkpoint		= this.parent.millis() + 100;

		this.bpm			= 120;
		this.rangeOctave	= 3;
		this.instrument	= new Instrument(this.parent);
		this.melody		= new Melody(this.parent, this.input);
		 */
		this.initModuleTemplate();
	} // ModuleTemplate

	// Methods:

	/**
	 * Called from constructor to calculate Y vals and call the methods for instantiating the necessary buttons;
	 * will eventually call different button methods depending on the module number.
	 */
	private void initModuleTemplate()
	{
		/*
		this.sidebarCP5.addGroup("sidebarGroup")
		.setBackgroundColor(this.parent.color(0))
		.setSize(this.parent.width / 3, this.parent.height + 1)
		.setVisible(false);

		// Add play button, hamburger and menu x:
	//	addOutsideButtons();

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
		 */
		// calculate y's
		// set y vals for first set of scrollbar labels:
		textYVals[0]	=	26;
		// Given our height = 250 and "hide" (textYVals[0]) starts at [40] - now 26 (1/17),
		// We want a difference of 27.  This gets that:
		int	yValDif = (int)((this.parent.height - textYVals[0]) / 18);//(textYVals.length + noteYVals.length + modulateYVals.length));
		// ... but no smaller than 25:
		if(yValDif < 25) {
			yValDif	= 25;
		}
		//System.out.println("yValDif = " + yValDif);
		yValDif = 26;


		for(int i = 1; i < textYVals.length; i++)
		{
			textYVals[i]	= textYVals[i - 1] + yValDif;
		} // for

		// Add extra space before "Pitch Color Codes":
		//		textYVals[textYVals.length - 1]	= textYVals[textYVals.length - 2] + (int)(yValDif * 1.5);

		// set y vals for the note names:
		noteYVals[0]	= textYVals[textYVals.length - 1] + yValDif;
		for(int i = 1; i < noteYVals.length; i++)
		{
			noteYVals[i]	= noteYVals[i - 1] + yValDif;
		}


		// call add methods:
		//		addHideButtons(textYVals[0]);

		addSliders(textYVals[1], textYVals[2], textYVals[3], textYVals[4]);

		this.addGuideTonePopout(textYVals[5]);
		this.addKeySelector(textYVals[5]);
		this.setCurKey(this.curKey, this.majMinChrom);


		//we want hsb to go here
		//HERE LIES THE HSBVALS
		modulateHSBVals[0] = textYVals[6];
		modulateHSBVals[1] = textYVals[7];
		modulateHSBVals[2] = textYVals[8];

		modulateYVals[0]	= textYVals[9];
		modulateYVals[1]	= textYVals[10];
		modulateYVals[2]	= textYVals[11];

		//		addTonicColorSelector(textYVals[12]);

		// ColorSelect and ColorStyle added out of order so that the 2nd Color
		// and 3rd Color select buttons will exist for the Rainbow ColorStyle
		// to lock them.
		this.addColorSelectButtons(textYVals[14]);

		addColorStyleButtons(textYVals[13]);

		// This call to rainbow() used to be the last in initInput(),
		// but has to be called before addCustomPitchColor() so that this.colors will be filled 
		// before the ColorWheels are created and the ColorWheels can be set to the colors in this.colors.
		// If the call comes at the end, the ColorWheels start black and end grayscale.
		this.rainbow();
		this.fillOriginalColors();
		this.fillHSBColors();
		//		this.updateColors(this.curColorStyle);

		this.addCustomPitchColor(textYVals[15], noteYVals);

		addHSBSliders(modulateHSBVals);

		addModulateSliders(modulateYVals);

		this.updateColors(this.curColorStyle);

		//		this.hideTextLabels();

		this.curColorStyle	= ModuleTemplate01.CS_RAINBOW;
		this.dichromFlag	= false;
		this.trichromFlag	= false;	

		this.sidebarCP5.getController("keyDropdown").bringToFront();
	} // initModuleTemplate


	/*
	 *  - alignLeft (x var to pass to the add functions)
	 *  - yValues (will pass the appropriate one to each of the functions)
	 *  
	 */
	/*	private void addOutsideButtons()
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
		.updateSize();
	} // addOutsideButtons
	 */

	/**
	 * Method called during initialization to instatiate the Threshold, Attack, Release,
	 * and Transition sliders.
	 * 
	 * Sliders have an odd and Textfields an even ID number, all less than this.lastTextfieldId (no duplicates allowed).
	 * This will be used to connect them in controlEvent.
	 * Names are based on ids; format: "slider" OR "textfield + [id]
	 *  - thresholdSlider	= "slider0", thresholdTF	= "textfield1"
	 *  - attackSlider	= "slider2", attackTF	= "textfield3"
	 *  - releaseSlider	= "slider4", releaseTF	= "textfield5"
	 *  - transitionSlider	= "slider6", transitionTF	= "textfield7"
	 * 
	 * @param thresholdY	y value of the Threshold slider group
	 * @param attackY	y value of the Attack slider group
	 * @param releaseY		y value of the Release slider group
	 * @param transitionY	y value of the Transition slider group
	 */
	/*	private void addSliders(int thresholdY, int attackY, int releaseY, int transitionY)
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
			.setSize(sliderWidth, this.sliderHeight)
			.setSliderMode(Slider.FLEXIBLE)
			.setRange(lowRange, highRange)
			.setValue(startingValue)
			.setLabelVisible(false)
			.setGroup("sidebarGroup")
			.setId(this.nextSliderId);

			this.nextSliderId	= this.nextSliderId + 1;

			this.sidebarCP5.addTextfield("textfield" + this.nextSTextfieldId)
			.setPosition(this.leftAlign + sliderWidth + spacer, yVals[i])
			.setSize(tfWidth, this.sliderHeight)
			.setText(this.sidebarCP5.getController("slider" + (this.nextSTextfieldId - 100)).getValue() + "")
			//			.setLabelVisible(false)
			.setAutoClear(false)
			.setGroup("sidebarGroup")
			.setId(this.nextSTextfieldId)
			.getCaptionLabel().setVisible(false);

			this.nextSTextfieldId	= this.nextSTextfieldId + 1;

		} // for

		// TODO: what even is Threshold? Are we able to measure decibels?
		//		this.setthreshold(10);

	} // addSliders
	 */


	/**
	 * Method called during instantiation to initialize the key selector drop-down menu (ScrollableList)
	 * and major/minor/chromatic selection buttons.
	 * 
	 * @param keyY	y value of the menu and buttons.
	 */
	/*	private void addKeySelector(int	keyY)
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
	/*		this.sidebarCP5.addTextlabel("key")
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
	 */
	/**
	 * TODO these comments - perhaps explain placement of guideToneY (should it be the same number as
	 * is sent to addKeySelector?)
	 * 
	 * @param guideToneY
	 */
	/*	private void addGuideTonePopout(int guideToneY)
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
		//		.setHeight(boxHeight - (popoutSpacer * 2))
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
		//		.setHeight(boxHeight - (popoutSpacer * 2))
		.setBarHeight(18)
		.setItemHeight(18)
		.setItems(this.melody.getRangeList())
		.setValue(0f)
		.setOpen(false)
		.setGroup("guideToneBackground")
		.bringToFront()
		.getCaptionLabel().toUpperCase(false);

		//		this.instrument.setVolume(0.2f);

	} // addGuideTonePopout
	 */

	/**
	 * Method called during instantiation to initialize the color style Toggles
	 * (Rainbow, Dichromatic, Trichromatic, and Custom).
	 * 
	 * @param colorStyleY	y value of the colorStyle Toggles
	 */
	private void addColorStyleButtons(int colorStyleY)
	{
		int	colorStyleWidth	= 49;
		int	colorStyleSpace	= 6;

		int	labelX			= 10;

		int rainbowX     	= this.leftAlign;
		int dichromaticX	= this.leftAlign + colorStyleWidth + colorStyleSpace;
		int trichromaticX	= this.leftAlign + (colorStyleWidth + colorStyleSpace) * 2;
		int customX			= this.leftAlign + (colorStyleWidth + colorStyleSpace) * 3;

		this.sidebarCP5.addTextlabel("colorStyle")
		.setPosition(labelX, colorStyleY + 4)
		.setGroup("sidebarGroup")
		.setValue("Color Style");

		this.sidebarCP5.addToggle("rainbow")
		.setPosition(rainbowX, colorStyleY)
		.setWidth(colorStyleWidth)
		.setCaptionLabel("Rainbow")
		.setGroup("sidebarGroup")
		.setInternalValue(ModuleTemplate01.CS_RAINBOW);
		this.sidebarCP5.getController("rainbow").getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);

		this.sidebarCP5.addToggle("dichrom")
		.setPosition(dichromaticX, colorStyleY)
		.setWidth(colorStyleWidth)
		.setCaptionLabel("Dichrom.")
		.setGroup("sidebarGroup")
		.setInternalValue(ModuleTemplate01.CS_DICHROM);
		this.sidebarCP5.getController("dichrom").getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);

		this.sidebarCP5.addToggle("trichrom")
		.setPosition(trichromaticX, colorStyleY)
		.setWidth(colorStyleWidth)
		.setCaptionLabel("Trichrom.")
		.setGroup("sidebarGroup")
		.setInternalValue(ModuleTemplate01.CS_TRICHROM);
		this.sidebarCP5.getController("trichrom").getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);

		this.sidebarCP5.addToggle("custom")
		.setPosition(customX, colorStyleY)
		.setWidth(colorStyleWidth)
		.setCaptionLabel("Custom")
		.setGroup("sidebarGroup")
		.setInternalValue(ModuleTemplate01.CS_CUSTOM);
		this.sidebarCP5.getController("custom").getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);

		((Toggle) this.sidebarCP5.getController("rainbow")).setState(true);
	} // addColorStyleButtons

	private void addColorSelectButtons(int colorSelectY)
	{
		int	colorSelectWidth	= 49;
		int	colorSelectSpace	= 6;
		int	textfieldWidth		= 100;

		int	labelX			= 10;

		int canvasX     	= this.leftAlign;
		int tonicX			= this.leftAlign + colorSelectWidth + colorSelectSpace;
		int secondColorX	= this.leftAlign + (colorSelectWidth + colorSelectSpace) * 2;
		int thirdColorX		= this.leftAlign + (colorSelectWidth + colorSelectSpace) * 3;

		String[]	labels	= new String[] { 
				"Canvas",
				"Tonic",
				"2nd Color",
				"3rd Color"
		};

		int[]	xVals	= new int[] {
				canvasX,
				tonicX,
				secondColorX,
				thirdColorX
		};

		this.sidebarCP5.addTextlabel("colorSelect")
		.setPosition(labelX, colorSelectY + 4)
		.setGroup("sidebarGroup")
		.setValue("Color Select");

		// id's pick up after custom pitch colors (which should end at 59;
		// starting at 63 here to be sure to have space).
		// Button's: id % 3 == 0
		// ColorWheel's: id % 3 == 1
		// TextField's: id % 3 == 2

		//		int	id	= this.nextButtonId;

		this.canvasColorSelectId	= this.nextButtonId;
		this.firstColorSelectId		= this.canvasColorSelectId;

		for(int i = 0; i < labels.length; i++)
		{
			// Last button:
			if(i == (labels.length - 1))
			{
				this.lastColorSelectId	= this.nextButtonId;
			} // if - last button

			System.out.println("addColorSelectButtons: this.nextButtonId = " + this.nextButtonId);
			System.out.println("addColorSelectButtons: this.nextColorWheelId = " + this.nextColorWheelId);
			System.out.println("addColorSelectButtons: this.nextCWTextfieldId = " + this.nextCWTextfieldId);

			this.sidebarCP5.addButton("button" + this.nextButtonId)
			.setPosition(xVals[i], colorSelectY)
			.setWidth(colorSelectWidth)
			.setCaptionLabel(labels[i])
			.setGroup("sidebarGroup")
			.setId(this.nextButtonId);
			this.sidebarCP5.getController("button" + this.nextButtonId).getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);

			this.nextButtonId	= this.nextButtonId + 1;

			this.sidebarCP5.addColorWheel("colorWheel" + this.nextColorWheelId)
			.setPosition(xVals[i], colorSelectY - 200)
			.setRGB(this.parent.color(255,0,0))
			.setLabelVisible(false)
			.setVisible(false)
			.setGroup("sidebarGroup")
			.setId(this.nextColorWheelId);

			this.nextColorWheelId	= this.nextColorWheelId + 1;

			this.sidebarCP5.addTextfield("textfield" + this.nextCWTextfieldId)
			.setPosition(xVals[i] + colorSelectWidth + colorSelectSpace, colorSelectY)
			.setWidth(textfieldWidth)
			.setAutoClear(false)
			.setVisible(false)
			.setLabelVisible(false)		// This has no effect either way.
			.setText("Code#")
			.setGroup("sidebarGroup")
			.setId(this.nextCWTextfieldId)
			.getCaptionLabel().setVisible(false);

			this.nextCWTextfieldId	= this.nextCWTextfieldId + 1;
		}

	} // addColorSelectButtons

	/**
	 * Method called during instantiation to initialize note buttons and their corresponding ColorWheels;
	 * make sure that rainbow() has already been called (or that this.colors has been filled some other way).
	 * 
	 * @param noteYVals	int[] of y values for each note button
	 */
	private void addCustomPitchColor(int labelYVal, int[] noteYVals)
	{
		int spacer1			= 5;	// between buttons and textfields
		int	spacer2			= 15;	// between the two rows of pitches
		int	labelX			= 10;

		int	buttonWidth		= 30;
		int	textfieldWidth	= 100;

		int	noteX1			= this.leftAlign - 40;
		int	textfieldX1		= noteX1 + buttonWidth + spacer1;

		int	noteX2			= textfieldX1 + /*textfieldWidth +*/ spacer2;
		int	textfieldX2		= noteX2 + buttonWidth + spacer1;

		int	noteX3			= textfieldX2 + /*textfieldWidth +*/ spacer2;
		int	textfieldX3		= noteX3 + buttonWidth + spacer1;

		int	noteX4			= textfieldX3 + /*textfieldWidth +*/ spacer2;
		int	textfieldX4		= noteX4 + buttonWidth + spacer1;

		int	colorWheelX		= textfieldX1;


		String[]	noteNames1	= new String[] {
				"A", "A#/Bb", "B"
		}; // noteNames
		String[]	noteNames2	= new String[] {
				"C", "C#/Db", "D"
		}; // noteNames2
		String[]	noteNames3	= new String[] {
				"D#/Db", "E", "F"
		}; // noteNames2
		String[]	noteNames4	= new String[] {
				"F#/Gb", "G", "G#/Ab"
		}; // noteNames2

		this.sidebarCP5.addTextlabel("customPitchColor")
		.setPosition(labelX, labelYVal + 4)
		.setGroup("sidebarGroup")
		.setValue("Custom Pitch Color");

		this.firstCustomColorId	= this.nextButtonId;

		// Note Buttons, ColorWheels and corresponding Textfields will have id's of 24 or over;
		// Button id % 3 == 0; ColorWheel id % 3 == 1, Textfield id % 3 == 2.
		int	namePos	= 0;
		//		int	id		= this.firstCustomColorId;		// 24 (as of 6/14)
		int colorpos  = 0;

		// First row of pitches:
		for(int i = 0; i < noteNames1.length; i++)
		{
			// Needs to be added to sidebarCP5 so it is still visible to turn off the ColorWheel:
			this.sidebarCP5.addButton("button" + this.nextButtonId)
			.setPosition(noteX1, noteYVals[i])
			.setWidth(buttonWidth)
			.setLabel(noteNames1[namePos])
			.setId(this.nextButtonId)
			.setGroup("sidebarGroup")
			.getCaptionLabel().toUpperCase(false);

			this.nextButtonId = this.nextButtonId + 1;

			this.sidebarCP5.addColorWheel("colorWheel" + this.nextColorWheelId)
			.setPosition(colorWheelX, noteYVals[i] - 200)		// 200 = height of ColorWheel
			.setRGB(this.parent.color(getColors()[colorpos][0], getColors()[colorpos][1], getColors()[colorpos][2]))
			.setLabelVisible(false)
			.setVisible(false)
			.setGroup("sidebarGroup")
			.setId(this.nextColorWheelId);

			colorpos = colorpos + 1;

			this.nextColorWheelId = this.nextColorWheelId + 1;

			this.sidebarCP5.addTextfield("textfield" + this.nextCWTextfieldId)
			.setPosition(textfieldX1, noteYVals[i])
			.setWidth(textfieldWidth)
			.setAutoClear(false)
			.setVisible(false)
			//			.setLabelVisible(false)			// This has no effect either way.
			.setText("Code#")
			.setGroup("sidebarGroup")
			.setId(this.nextCWTextfieldId)
			.getCaptionLabel().setVisible(false);


			this.nextCWTextfieldId = this.nextCWTextfieldId + 1;
			namePos	= namePos + 1;
		}// first row of pitches

		namePos	= 0;
		for(int i = 0; i < noteNames1.length; i++)
		{// Second row of pitches:

			this.sidebarCP5.addButton("button" + this.nextButtonId)
			.setPosition(noteX2, noteYVals[i])
			.setWidth(buttonWidth)
			.setLabel(noteNames2[namePos])
			.setId(this.nextButtonId)
			.setGroup("sidebarGroup")
			.getCaptionLabel().toUpperCase(false);

			this.nextButtonId = this.nextButtonId + 1;

			this.sidebarCP5.addColorWheel("colorWheel" + this.nextColorWheelId)
			.setPosition(noteX2, noteYVals[i] - 200)
			.setRGB(this.parent.color(getColors()[colorpos][0], getColors()[colorpos][1], getColors()[colorpos][2]))
			.setLabelVisible(false)
			.setVisible(false)
			.setGroup("sidebarGroup")
			.setId(this.nextColorWheelId);

			colorpos = colorpos + 1;

			this.nextColorWheelId = this.nextColorWheelId + 1;

			this.sidebarCP5.addTextfield("textfield" + this.nextCWTextfieldId)
			.setPosition(textfieldX2, noteYVals[i])
			.setWidth(textfieldWidth)
			.setAutoClear(false)
			.setVisible(false)
			.setText("Code#")
			.setGroup("sidebarGroup")
			.setId(this.nextCWTextfieldId)
			.getCaptionLabel().setVisible(false);

			this.nextCWTextfieldId = this.nextCWTextfieldId + 1;
			namePos	= namePos + 1;
		} 
		namePos	= 0;
		for(int i = 0; i < noteNames1.length; i++)
		{// Second row of pitches:

			this.sidebarCP5.addButton("button" + this.nextButtonId)
			.setPosition(noteX3, noteYVals[i])
			.setWidth(buttonWidth)
			.setLabel(noteNames3[namePos])
			.setId(this.nextButtonId)
			.setGroup("sidebarGroup")
			.getCaptionLabel().toUpperCase(false);

			this.nextButtonId = this.nextButtonId + 1;

			this.sidebarCP5.addColorWheel("colorWheel" + this.nextColorWheelId)
			.setPosition(noteX3, noteYVals[i] - 200)
			.setRGB(this.parent.color(getColors()[colorpos][0], getColors()[colorpos][1], getColors()[colorpos][2]))
			.setLabelVisible(false)
			.setVisible(false)
			.setGroup("sidebarGroup")
			.setId(this.nextColorWheelId);

			colorpos = colorpos + 1;

			this.nextColorWheelId = this.nextColorWheelId + 1;

			this.sidebarCP5.addTextfield("textfield" + this.nextCWTextfieldId)
			.setPosition(textfieldX3, noteYVals[i])
			.setWidth(textfieldWidth)
			.setAutoClear(false)
			.setVisible(false)
			.setText("Code#")
			.setGroup("sidebarGroup")
			.setId(this.nextCWTextfieldId)
			.getCaptionLabel().setVisible(false);

			this.nextCWTextfieldId = this.nextCWTextfieldId + 1;
			namePos	= namePos + 1;
		} 
		namePos	= 0;
		for(int i = 0; i < noteNames1.length; i++)
		{// Second row of pitches:

			this.sidebarCP5.addButton("button" + this.nextButtonId)
			.setPosition(noteX4, noteYVals[i])
			.setWidth(buttonWidth)
			.setLabel(noteNames4[namePos])
			.setId(this.nextButtonId)
			.setGroup("sidebarGroup")
			.getCaptionLabel().toUpperCase(false);

			this.nextButtonId = this.nextButtonId + 1;

			this.sidebarCP5.addColorWheel("colorWheel" + this.nextColorWheelId)
			.setPosition(noteX4, noteYVals[i] - 200)
			.setRGB(this.parent.color(getColors()[colorpos][0], getColors()[colorpos][1], getColors()[colorpos][2]))
			.setLabelVisible(false)
			.setVisible(false)
			.setGroup("sidebarGroup")
			.setId(this.nextColorWheelId);

			colorpos = colorpos + 1;

			this.nextColorWheelId = this.nextColorWheelId + 1;

			this.sidebarCP5.addTextfield("textfield" + this.nextCWTextfieldId)
			.setPosition(textfieldX4, noteYVals[i])
			.setWidth(textfieldWidth)
			.setAutoClear(false)
			.setVisible(false)
			.setText("Code#")
			.setGroup("sidebarGroup")
			.setId(this.nextCWTextfieldId)
			.getCaptionLabel().setVisible(false);

			this.nextCWTextfieldId = this.nextCWTextfieldId + 1;
			namePos	= namePos + 1;
		} 
		// for - second row of pitches

	} // addNoteColorSelectors
	/*
	private void addHSBSliders(int[] hsb)
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
	 */
	/**
	 * Method called during instantiation, to initialize the color modulate sliders.
	 * 
	 * @param modulateYVals	int[] of the y values of the red, green, and blue sliders, respectively.
	 */
	/*	private void addModulateSliders(int[] modulateYVals)
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
	 */

	/*
	public void update()
	{
		this.sidebarCP5.getController("textfield1").setValue(this.sidebarCP5.getController("slider0").getValue());
		((Textfield)this.sidebarCP5.getController("textfield1")).setText(this.sidebarCP5.getController("slider0").getValue() + "");

	} // update
	 */
	private void updateColors(float colorStyle)
	{

		if(colorStyle < 1 || colorStyle > 4) {
			throw new IllegalArgumentException("Module_01_02.updateColors: char paramter " + colorStyle + " is not recognized; must be 1 - 4.");
		}


		this.curColorStyle	= colorStyle;

		// Rainbow:
		if(this.curColorStyle == ModuleTemplate01.CS_RAINBOW)
		{
			//			// if avoids errors during instantiation:
			if(this.sidebarCP5.getController("button" + (this.canvasColorSelectId + 2)) != null)
			{
				this.sidebarCP5.getController("button" + (this.canvasColorSelectId + 2)).setLock(true);
			}
			if(this.sidebarCP5.getController("button" + (this.canvasColorSelectId + 3)) != null)
			{
				this.sidebarCP5.getController("button" + (this.canvasColorSelectId + 3)).setLock(true);
			}

			// Update tonic Textfield and ColorWheel:
			//			this.updateTextfield((this.canvasColorSelectId % 100) + 400, 0);

		} // if - rainbow

		// Dichromatic:
		if(this.curColorStyle == ModuleTemplate01.CS_DICHROM)
		{
			// First time to dichromatic, dichromFlag will be false, 
			// and the two colors will be set to contrast.
			if(!this.dichromFlag)
			{
				this.dichromatic_OneRGB(this.colors[0]);

				this.dichromFlag	= true;
			} // first time
			// After the first time, use current color values
			// (allows selection of 2nd color):
			else
			{
				this.dichromatic_TwoRGB(this.colors[0], this.colors[this.colors.length - 1], true);
			}

			// Unlock 2nd Color Button, but keep 3rd Color locked:
			// if avoids errors during instantiation:
			if(this.sidebarCP5.getController("button" + (this.canvasColorSelectId + 2)) != null)
			{
				this.sidebarCP5.getController("button" + (this.canvasColorSelectId + 2)).setLock(false);
			}
			if(this.sidebarCP5.getController("button" + (this.canvasColorSelectId + 3)) != null)
			{
				this.sidebarCP5.getController("button" + (this.canvasColorSelectId + 3)).setLock(true);
			}

		} // Dichromatic

		// Trichromatic:
		if(this.curColorStyle == ModuleTemplate01.CS_TRICHROM)
		{
			int	colorPos2	= 4;	// initializing for the first call
			int	colorPos3	= 8;
			// first time trichromatic has been called:
			if(!this.trichromFlag)
			{
				this.trichromatic_OneRGB(this.colors[0]);

				this.trichromFlag	= true;
			}

			// every other time:
			else
			{
				if(this.majMinChrom == 2)
				{
					colorPos2	= 4;
					colorPos3	= 8;
				} else {
					//				colorPos2	= 3;
					//				colorPos3	= 4;

					// Positions have to be 5 and 7, not 3 and 4, since colors is filled all the way and we just ignore
					// non-diatonic tones, so 5 and 7 actually corresponds to the mediant and dominant scale degrees.

					colorPos2	= 5;
					colorPos3	= 7;
				}

				this.trichromatic_ThreeRGB(this.colors[0], this.colors[colorPos2], this.colors[colorPos3]);
				//				this.updateCustomPitchCWs();
			} // else


			// Unlock all for Trichromatic:
			// if avoids errors during instantiation:
			if(this.sidebarCP5.getController("button" + (this.canvasColorSelectId + 2)) != null)
			{
				this.sidebarCP5.getController("button" + (this.canvasColorSelectId + 2)).setLock(false);
			}
			if(this.sidebarCP5.getController("button" + (this.canvasColorSelectId + 3)) != null)
			{
				this.sidebarCP5.getController("button" + (this.canvasColorSelectId + 3)).setLock(false);
			}

		} // Trichromatic

		// Custom:
		if(this.curColorStyle == ModuleTemplate01.CS_CUSTOM)
		{

			((Toggle)(this.sidebarCP5.getController("chrom"))).setState(true);

			// (The functionality in controlEvent will check for custom, and if it is custom, they will set their position of colors to their internal color.)


			// (Will they need to check to make sure that the key is actually chromatic?)
		} // custom colorStyle

		System.out.println("updating custom pitch ColorWheels now...");
		// Populate the CustomPitch ColorWheels with the current colors:
		this.updateCustomPitchCWs();

	} // updateColors
	/*
	public void updateTextfield(int id, int colorPos)
	{
		Textfield curTextfield	= (Textfield)this.sidebarCP5.getController("textfield" + id);
		if(curTextfield != null)
		{
			curTextfield.setText("rgb(" + (int)this.colors[colorPos][0] + ", " + (int)this.colors[colorPos][1] + ", " + (int)this.colors[colorPos][2] + ")");
			curTextfield.submit();
		} // if
	} // updateTextfield
	 *//*
	private void updateColorWheel(int id, int colorPos)
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
		((ColorWheel)this.sidebarCP5.getController("colorWheel" + id)).setRGB(rgbInt);
	} // updateColorWheel
	  */

	private void updateColorSelectCWs()
	{
		if(this.canvasColorSelectId > -1)
		{

			if(this.curColorStyle == ModuleTemplate01.CS_RAINBOW)
			{
				int colorSelectId	= (this.canvasColorSelectId % 100) + 301;
				this.updateColorWheel(colorSelectId, 0);
			} // if - rainbow

			if(this.curColorStyle == ModuleTemplate01.CS_DICHROM)
			{
				int colorSelectId	= (this.canvasColorSelectId % 100) + 301;
				this.updateColorWheel(colorSelectId, 0);
				this.updateColorWheel(colorSelectId + 1, (this.colors.length - 1));
			} // if - dichromatic

			if(this.curColorStyle == ModuleTemplate01.CS_TRICHROM)
			{
				int colorSelectId	= (this.canvasColorSelectId % 100) + 301;

				int	colorPos2;
				int	colorPos3;

				if(this.majMinChrom == 2)
				{
					colorPos2	= 4;
					colorPos3	= 8;
				} else {
					colorPos2	= 3;
					colorPos3	= 4;
				}

				this.updateColorWheel(colorSelectId, 0);
				this.updateColorWheel(colorSelectId + 1, colorPos2);
				this.updateColorWheel(colorSelectId + 2, colorPos3);
			} // if - trichromatic
		} // if canvasColorSelectId > -1
		else
		{
			System.out.println("ModuleTemplate01.updateColorSelectCWs: did not update ColorWheels - canvasColorSelectId is " + this.canvasColorSelectId);
		} // else - let the user know we didn't do it
	} // updateColorSelectCWs

	private void updateCustomPitchCWs()
	{
		if(this.firstCustomColorId > -1)
		{
			int	id	= (this.firstCustomColorId % 100) + 300;	// ColorWheels start at 300

			for(int colorPos = 0; colorPos < this.colors.length; colorPos++)
			{
				updateColorWheel(id, colorPos);
				id	= id + 1;
			} // for - colorPos
		} else {
			System.out.println("ModuleTemplate01.updateCustomPitchCWs: did not update ColorWheels - firstCustomColorId is " + this.firstCustomColorId);
		} // else - let the user know we didn't do it
	} // updateCustomPitchCWs

	public void legend(int goalHuePos)
	{

		this.parent.textSize(24);

		//		String[]	notes	= this.getScale(this.curKeyOffset, this.majMinChrom);
		String[]	notes	= this.getScale(this.curKey, this.majMinChrom);

		float  sideWidth1   = (this.parent.width - leftEdgeX) / notes.length;
		float  sideHeight  = this.parent.width / 12;
		float	addToLastRect	= (this.parent.width - this.getLeftEdgeX()) - (sideWidth1 * notes.length);
		float	sideWidth2	= sideWidth1;
		//  float  side = height / colors.length;

		//	stroke(255);
		this.parent.noStroke();

		int	scaleDegree;
		int	colorPos;
		// This find the correct goal position for major and minor scales (and has no effect on chromatic):
		goalHuePos	= this.scaleDegreeColors[this.majMinChrom][goalHuePos];

		// All notes but the last:
		for (int i = 0; i < notes.length; i++)
		{			
			if(i == notes.length - 1)
			{
				sideWidth2	= sideWidth1 + addToLastRect;
			}
			// colors is filled all the way and only picked at the desired notes:
			scaleDegree	= this.scaleDegrees[this.majMinChrom][i];
			colorPos	= scaleDegree;

			this.parent.fill(this.colors[colorPos][0], this.colors[colorPos][1], this.colors[colorPos][2]);
			//			this.parent.fill(255);

			if (i == goalHuePos) {
				this.parent.rect(leftEdgeX + (sideWidth1 * i), (float)(this.parent.height - (sideHeight * 1.5)), sideWidth2, (float) (sideHeight * 1.5));
				//      rect(0, (side * i), side * 1.5, side);
			} else {
				this.parent.rect(leftEdgeX + (sideWidth1 * i), this.parent.height - sideHeight, sideWidth2, sideHeight);
				//      rect(0, (side * i), side, side);
			}
			this.parent.fill(0);
			this.parent.text(notes[i], (float) (leftEdgeX + (sideWidth1 * i) + (sideWidth1 * 0.35)), this.parent.height - 20);
		} // for
		/*
		// Last note:
		int	i	= notes.length - 1;

		scaleDegree	= this.getScaleDegrees()[this.getMajMinChrom()][i];
		this.parent.fill(this.colors[scaleDegree][0], this.colors[scaleDegree][1], this.colors[scaleDegree][2]);
		//			this.parent.fill(255);

		if (i == goalHuePos) {
			this.parent.rect(leftEdgeX + (sideWidth * i), (float)(this.parent.height - (sideHeight * 1.5)), sideWidth + addToLastRect, (float) (sideHeight * 1.5));
			//      rect(0, (side * i), side * 1.5, side);
		} else {
			this.parent.rect(leftEdgeX + (sideWidth * i), this.parent.height - sideHeight, sideWidth + addToLastRect, sideHeight);
			//      rect(0, (side * i), side, side);
		}
		this.parent.fill(0);
		this.parent.text(notes[i], (float) (leftEdgeX + (sideWidth * i) + (sideWidth * 0.35)), this.parent.height - 20);
		 */
		/*
		// TODO: remove after fixing trichrom-maj/minor bug:
		// Testing to see what's really in colors:
		for(int i = 0; i < this.colors.length; i++)
		{
			this.parent.fill(this.colors[i][0], this.colors[i][1], this.colors[i][2]);
			this.parent.ellipse(this.parent.width / 3 * 2, i * 30 + 60, 30, 30);
		} // for
		 */
	} // legend

	/*
	private void displaySidebar()
	{	
		this.sidebarCP5.getGroup("sidebarGroup").setVisible(true);
		this.setLeftEdgeX(this.parent.width / 3);

	} // displaySidebar
	 */
	/*
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
	/*		String[][] majorScales	= new String[][] {
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

	//	} // setCurKey

	/**
	 * Calls playMelody(key, bpm, scale, rangeOctave) with the curKey, bpm, rangeOctave instance vars
	 * and the string corresponding to the majMinChrom instance var ("major", "minor", or "chromatic").
	 * @param scale
	 */
	/*	private void playMelody()
	{

		String[]	scales	= new String[] { "major", "minor", "chromatic" };

		this.melody.playMelody(this.curKey, this.bpm, scales[this.majMinChrom], this.rangeOctave, this.instrument);
//		this.melody.playMelody(this.curKey, this.bpm, scales[this.majMinChrom], this.rangeOctave);
	} // playMelody
	 */
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
	/*	private int arrayContains(String[] array, String element) {
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
	 */

	/**
	 * Converts the given color to HSB and sends it to dichromatic_OneHSB.
	 * (dichromatic_OneHSB will send it to _TwoHSB, which will set this.colors, changing the scale.)
	 * 
	 * @param rgbVals	float[] of RGB values defining the color for the tonic of the scale.
	 */
	public void dichromatic_OneRGB(float[] rgbVals)
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

		// TODO: this assumes maximum saturation and brightness; can we use the set values instead?
		// find the complement:
		float[]	hsbComplement	= new float[] { (float) ((hsbVals[0] + 0.5) % 1), 1, 1 };

		// convert them both to RGB;
		float[]	rgbVals1	= new float[3];
		float[]	rgbVals2	= new float[3];

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
	public void dichromatic_TwoRGB(float[] rgbVals1, float[] rgbVals2, boolean fillFirstToLast)
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
		float	difference;

		// Loop through red, then green, then blue
		// (could do it like normal, but then would have to calculate difference each time;
		// those who save processor cycles store up treasure in Heaven):
		for(int i = 0; i < 3; i++)
		{
			difference	= rgbVals1[i] - rgbVals2[i];

			for(int j = 0; j < this.colors.length - 1; j++)
			{
				// Take the percent of the difference multiplied by the position in the array,
				// subtracting it from the first color to deal with negatives correctly
				// (and dividing by 100 because percent requires it but to do so earlier would create smaller numbers than Java likes to deal with).
				this.colors[j][i]	= this.colors[0][i] - (difference * j * percent / 100);
			} // for - j
		} // for - i

		// Fill the last color manually, because if we don't,
		// it can't seem to calculate correctly when the tonic color is changed:
		for(int i = 0; i < rgbVals2.length; i++)
		{
			this.colors[this.colors.length - 1][i]	= rgbVals2[i];
		}
	} // dichromatic_TwoRGB

	/**
	 * Converts the given color to HSB and sends it to dichromatic_OneHSB.
	 * (dichromatic_OneHSB will send it to _TwoHSB, which will set this.colors, changing the scale.)
	 * 
	 * @param rgbVals	float[] of RGB values defining the color for the tonic of the scale.
	 */
	public void trichromatic_OneRGB(float[] rgbVals)
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
		float[]	rgbVals1	= new float[3];
		float[]	rgbVals2	= new float[3];
		float[]	rgbVals3	= new float[3];

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

		float	redDelta1	= (rgbVals1[0] - rgbVals2[0]) / (color2pos - color1pos);
		float	greenDelta1	= (rgbVals1[1] - rgbVals2[1]) / (color2pos - color1pos);
		float	blueDelta1	= (rgbVals1[2] - rgbVals2[2]) / (color2pos - color1pos);

		float	redDelta2	= (rgbVals2[0] - rgbVals3[0]) / (color3pos - color2pos);
		float	greenDelta2	= (rgbVals2[1] - rgbVals3[1]) / (color3pos - color2pos);
		float	blueDelta2	= (rgbVals2[2] - rgbVals3[2]) / (color3pos - color2pos);

		float	redDelta3	= (rgbVals3[0] - rgbVals1[0]) / (this.scaleLength - color3pos);
		float	greenDelta3	= (rgbVals3[1] - rgbVals1[1]) / (this.scaleLength - color3pos);
		float	blueDelta3	= (rgbVals3[2] - rgbVals1[2]) / (this.scaleLength - color3pos);
		/*		
		System.out.println("redDelta1 = " + redDelta1 + "; greenDelta1 = " + greenDelta1 + "; blueDelta1 = " + blueDelta1);
		System.out.println("redDelta2 = " + redDelta2 + "; greenDelta2 = " + greenDelta1 + "; blueDelta2 = " + blueDelta2);
		System.out.println("redDelta3 = " + redDelta3 + "; greenDelta3 = " + greenDelta1 + "; blueDelta3 = " + blueDelta3);
		 */
		// This array has the trichromatic spectrum:
		//		float[][]	trichromColors	= new float[this.scaleLength][3];
		this.trichromColors	= new float[this.scaleLength][3];

		// fill first position with first color:
		for(int i = 0; i < rgbVals1.length; i++)
		{
			trichromColors[0][i]	= rgbVals1[i];
		}

		// fill from first color to second color:
		for(int i = 1; i < color2pos + 1; i++)
		{
			for(int j = 0; j < trichromColors[i].length; j++)
			{
				trichromColors[i][0]	= trichromColors[i - 1][0] - redDelta1;
				trichromColors[i][1]	= trichromColors[i - 1][1] - greenDelta1;
				trichromColors[i][2]	= trichromColors[i - 1][2] - blueDelta1;
			} // for - j
		} // for - first color to second color


		// fill from second color to third color:
		for(int i = color2pos + 1; i < color3pos + 1; i++)
		{
			for(int j = 0; j < trichromColors[i].length; j++)
			{
				trichromColors[i][0]	= trichromColors[i - 1][0] - redDelta2;
				trichromColors[i][1]	= trichromColors[i - 1][1] - greenDelta2;
				trichromColors[i][2]	= trichromColors[i - 1][2] - blueDelta2;
			} // for - j
		} // for - first color to second color

		// fill from third color back to first color:
		for(int i = color3pos + 1; i < this.scaleLength; i++)
		{
			for(int j = 0; j < trichromColors[i].length; j++)
			{
				trichromColors[i][0]	= trichromColors[i - 1][0] - redDelta3;
				trichromColors[i][1]	= trichromColors[i - 1][1] - greenDelta3;
				trichromColors[i][2]	= trichromColors[i - 1][2] - blueDelta3;
			} // for - j
		} // for - third color to first color

		// fill colors with the trichrom spectrum; some colors will be repeated, as designated in scaleDegreeColors:
		int	trichromColorPos	= 0;
		for(int i = 0; i < this.colors.length && trichromColorPos < trichromColors.length; i++)
		{
			trichromColorPos	= this.scaleDegreeColors[this.majMinChrom][i];

			this.colors[i][0]	= trichromColors[trichromColorPos][0];
			this.colors[i][1]	= trichromColors[trichromColorPos][1];
			this.colors[i][2]	= trichromColors[trichromColorPos][2];
		} // for

		this.updateCustomPitchCWs();
	} //trichromatic_ThreeRGB

	/**
	 * Populates colors with rainbow colors (ROYGBIV - with a few more for chromatic scales).
	 */
	public void rainbow()
	{
		// Filling colors all the way, regardless of the scale,
		// and then we'll just pick out the colors at scaleDegrees[majMinChrom] for major or minor:
		float[][][] rainbowColors	= new float[][][] { 
			new float[][] {
				{ 255, 0, 0 }, 
				{ 255, 0, 0 },
				{ 255, (float) 127.5, 0 }, 
				{ 255, 255, 0 }, 
				{ 255, 255, 0 }, 
				{ (float) 127.5, 255, 0 },
				{ 0, 255, 255 },
				{ 0, 255, 255 },
				{ 0, 255, 255 },  
				{ 0, 0, 255 },
				{ (float) 127.5, 0, 255 },
				{ (float) 127.5, 0, 255 }
			}, // major
			new float[][] {
				{ 255, 0, 0 }, 
				{ 255, 0, 0 },
				{ 255, (float) 127.5, 0 }, 
				{ 255, 255, 0 }, 
				{ 255, 255, 0 }, 
				{ (float) 127.5, 255, 0 },
				{ 0, 255, 255 },
				{ 0, 255, 255 },
				{ 0, 0, 255 },
				{ 0, 0, 255 },
				{ (float) 127.5, 0, 255 },
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

		for(int i = 0; i < this.colors.length && i < rainbowColors[this.majMinChrom].length; i++)
		{
			for(int j = 0; j < this.colors[i].length && j < rainbowColors[this.majMinChrom][i].length; j++)
			{
				//				this.getColors()[i][j]	= rainbowColors[this.getMajMinChrom()][i][j];
				this.colors[i][j]	= rainbowColors[this.majMinChrom][i][j];
				this.hsbColors[i][j]	= rainbowColors[this.majMinChrom][i][j];
			} // for - j (going through rgb values)
		} // for - i (going through colors)


		// Populate the Textfields with the current colors in the colors array,
		// but only if the custom color buttons have already been initialized
		// (rainbow() is called before that in the constructor so that there will be colors for 
		// the ColorWheels and Textfields to use when they are created):
		if(this.firstCustomColorId > -1)
		{
			int	id	= (this.firstCustomColorId % 100) + 300;	// CWTextfields start at 400

			for(int colorPos = 0; colorPos < this.colors.length; colorPos++)
			{
				this.updateColorWheel(id, colorPos);

				id	= id + 1;
			} // for - colorPos

		} // if - firstCustomColorId > -1

	} // rainbow

	/**
	 * Applies the values of the Red Modulate/Green Modulate/Blue Modulate sliders and 
	 * calls applyHSBModulate() to apply the values of the Hue/Saturation/Brightness Modulate sliders.
	 */
	/*	private void applyColorModulate(float[][] colors, float[][] originalColors)
	{
		if(colors == null || originalColors == null) {
			throw new IllegalArgumentException("ModuleTemplate.applyColorModulate: one of the float[] parameters is null (colors = " + colors + "; originalColors = " + originalColors);
		} // error checking

		for(int i = 0; i < this.colors.length; i++)
		{
			for(int j = 0; j < this.colors[i].length; j++)
			{
				// Adds redModulate to the red, greenModulate to the green, and blueModulate to the blue:
				colors[i][j]	= originalColors[i][j] + this.redGreenBlueMod[j];

			} // for - j
		} // for - i

		this.fillHSBColors();

		super.applyHSBModulate(this.colors, this.hsbColors);
	} // applyColorModulate
	 */
	/**
	 * Applies the values from this.hueSatBrightnessMod to the contents of this.colors.
	 * @param colors	this.colors
	 * @param hsbColors	this.hsbColors
	 */
	/*	private void applyHSBModulate(float[][] colors, float[][] hsbColors)
	{
		if(colors == null || hsbColors == null) {
			throw new IllegalArgumentException("ModuleTemplate.applyColorModulate: one of the float[] parameters is null (colors = " + colors + "; originalColors = " + originalColors);
		}


		float[] hsb = new float[3];

		for (int i = 0; i < this.colors.length; i++)
		{
			// Converts this position of hsbColors from RGB to HSB:
			Color.RGBtoHSB((int)hsbColors[i][0], (int)hsbColors[i][1], (int)hsbColors[i][2], hsb);

			//			((((hsb[i1] + this.hueSatBrightnessMod[i1]) * 100) % 100) / 100)
			// Applies the status of the sliders to the newly-converted color:

			hsb[0] = (hsb[0] + this.hueSatBrightnessMod[0] + 1) % 1;

			hsb[1] = Math.max(Math.min(hsb[1] + this.hueSatBrightnessMod[1], 1), 0);
			hsb[2] = Math.max(Math.min(hsb[2] + this.hueSatBrightnessMod[2], 1), 0);

			// Converts the color back to RGB:
			int oc = Color.HSBtoRGB(hsb[0], hsb[1],  hsb[2]);
			Color a = new Color(oc);

			// Fills colors with the new color:
			colors[i][0] = (float)a.getRed();
			colors[i][1] = (float)a.getGreen();
			colors[i][2] = (float)a.getBlue();


		} // for
	} // applyHSBModulate
	 */
	/*//	public void useSliderVal(int id, float val)
	{
		// TODO - error checking
		//		System.out.println("useSliderVal: id = " + id);
/*
		// Threshold:
		if(id == 0)
		{
			this.setThreshold(val);
		}

		// Attack, Release, and Transition:
		if(id == 1 || id == 2 || id == 3)
		{
			//			int	pos	= (id / 2) - 1;
			int	pos	= id - 1;
			//			this.attackReleaseTransition[pos]	= sliderValFloat;
			this.setAttRelTranVal(pos, val);
		}
		/*
		// Hue/Saturation/Brightness modulate
		if(id >= this.firstHSBSliderId && id < (this.firstHSBSliderId + 3))
		{
			//			int pos = (id/2)-7;
			int pos = id - 7;

			this.setHueSatBrightnessMod(pos, val);
			this.applyHSBModulate(getColors(), originalColors);
		}//hsb mod

		// Red Modulate/Green Modulate/Blue Modulate:
		if(id == 4 || id == 5 || id == 6)
		{
			//			int	pos	= (id / 2) - 4;		
			int	pos	= id - 4;	// red = 0, green = 1, blue = 2
			this.redGreenBlueMod[pos]	= val;

			this.applyColorModulate(this.colors, this.originalColors);
		} // red/green/blue mod

	 */
	/*		if(id == 10)
		{
			this.bpm	= Math.max(Math.min((int)val, 240), 0);
		}

		if(id == 11)
		{
			this.instrument.setVolume(Math.max(Math.min(val, 5), 0));
		}
	 */
	//	} // useSliderVal

	/**
	 * This method handles the functionality of all the buttons, sliders, and textFields;
	 * Notate bene: any classes that include a moduleTemplate *must* include a controlEvent(ControlEvent) method that calls this method.
	 * 
	 * @param theControlEvent	ControlEvent used to determine which controller needs to act.
	 */
	public void controlEvent(ControlEvent controlEvent)
	{
		super.controlEvent(controlEvent);
		//		System.out.println("ModuleTemplate01: theControlEvent.getController() = " + controlEvent.getController());

		int	id	= controlEvent.getController().getId();

		// Sliders (sliders have id num < 100 and corresponding textfields have slider id + 100)
		//		if(id % 2 == 0 && id < this.lastTextfieldId)
		if(id > -1 && id < 100)
		{
			// Color modulation applied in parent class;
			// this just updates the ColorWheels (which are specific to this child)

			// Hue/Saturation/Brightness modulate
			if(this.firstHSBSliderId != -1 && 
					id >= this.firstHSBSliderId && id < (this.firstHSBSliderId + 3))
			{
				this.updateColorSelectCWs();
				this.updateColorSelectCWs();
			}//hsb mod

			// Red Modulate/Green Modulate/Blue Modulate:
			if(this.firstRGBSliderId != -1 &&
					id >= this.firstRGBSliderId && id < (this.firstRGBSliderId + 3))
			{
				this.updateColorSelectCWs();
				this.updateColorSelectCWs();
			} // red/green/blue mod

		} // sliders

		// ColorWheels
		if(id > 299 && id < 400)
		{
			// get current color:
			ColorWheel	curCW	= (ColorWheel)controlEvent.getController();

			int	rgbColor	= curCW.getRGB();
			Color	color	= new Color(rgbColor);

			id	= controlEvent.getId();

			// ignore canvas color, which has been set by the parent, but set notes for all others:
			if((id % 100) != (this.canvasColorSelectId % 100))
			{
				int notePos	= this.calculateNotePos(id);	// the position in colors that is to be changed.

				// error checking
				if(notePos < 0 || notePos > this.colors.length)	{
					throw new IllegalArgumentException("ModuleTemplate.controlEvent - custom color Textfields: " +
							"notePos " + notePos + " from id " + id + " is not a valid note position; " +
							"it should be between 0 and " + this.colors.length);
				} // error checking

				this.colors[notePos][0]	= color.getRed();
				this.colors[notePos][1]	= color.getGreen();
				this.colors[notePos][2]	= color.getBlue();
			} // else - not canvas

		} // ColorWheels

		// Major/Minor/Chromatic buttons
		if(controlEvent.getName().equals("major") ||
				controlEvent.getName().equals("minor") ||
				controlEvent.getName().equals("chrom"))
		{		
			Toggle	curToggle	= (Toggle) controlEvent.getController();
			this.setCurKey(this.curKey, (int) curToggle.internalValue());
			//			this.majMinChrom	= (int) curToggle.internalValue();

			// Turn off the other two:
			Toggle[] toggleArray	= new Toggle[] {
					(Toggle)this.sidebarCP5.getController("major"),
					(Toggle)this.sidebarCP5.getController("minor"),
					(Toggle)this.sidebarCP5.getController("chrom"),
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
				((ScrollableList)this.sidebarCP5.getController("rangeDropdown"))
				.setItems(this.melody.getRangeList())
				.setValue(0f);
			} catch(ClassCastException cce) {
				throw new IllegalArgumentException("ModuleTemplate.controlEvent - keyDropdown: error setting rangeList ScrollableList.");
			} // catch

		} // majMinChrom buttons


		// Color Selection: 
		// Buttons, ColorWheels and corresponding Textfields will have id's of 21 or over;
		// Button id % 3 == 0; ColorWheel id % 3 == 1, Textfield id % 3 == 2.

		// Custom pitch color selector buttons:
		//		if( ( controlEvent.getId() >= this.firstCustomColorId ) && (controlEvent.getId() % 3 == 0) )
		//		{

		// only call updateColors() for colorSelect Buttons, ColorWheels, or Textfields:
		if(id >= 200 && id < 500 && (id % 100) >= (this.firstColorSelectId % 100) && (id % 100) <= (this.lastColorSelectId % 100))
		{
			this.updateColors(this.curColorStyle);
			this.updateCustomPitchCWs();

			/*			// if any of the colorSelect *Buttons* we pressed, update the ColorWheels
			if(id == this.canvasColorSelectId + 1)
			{
				int colorSelectId	= (this.canvasColorSelectId % 100) + 301;
				this.updateColorWheel(colorSelectId, 0);
				this.updateColorWheel(colorSelectId + 1, (this.colors.length - 1));

			}
			 */
		} // ColorWheels

		/*			Button	curButton	= (Button)controlEvent.getController();

			// draw slightly transparent rectangle:
			if(curButton.getBooleanValue())
			{

				this.sidebarCP5.getGroup("background").setVisible(true);
				this.sidebarCP5.getGroup("background").bringToFront();

				// only call updateColors() for colorSelect Buttons:
				if(controlEvent.getId() >= this.firstColorSelectId && controlEvent.getId() <= this.lastColorSelectId)
				{
					this.updateColors(this.curColorStyle);
				}

			} else {

				this.sidebarCP5.setAutoDraw(true);
				this.sidebarCP5.getGroup("background").setVisible(false);
				this.displaySidebar();
			}

			this.sidebarCP5.getController("button" + (controlEvent.getId())).bringToFront();
			this.sidebarCP5.getController("colorWheel" + (controlEvent.getId() + 1)).bringToFront();
			this.sidebarCP5.getController("textfield" + (controlEvent.getId() + 2)).bringToFront();

			this.sidebarCP5.getController("colorWheel" + (controlEvent.getId() + 1)).setVisible(curButton.getBooleanValue());
			this.sidebarCP5.getController("textfield" + (controlEvent.getId() + 2)).setVisible(curButton.getBooleanValue());

			this.fillOriginalColors();
			this.fillHSBColors();
			this.resetModulateSlidersTextfields();
			this.applyColorModulate(this.colors, this.originalColors);

			// Not calling updateColors() here because it should only be called by colorSelect buttons:
		 */		//			this.updateColors(this.curColorStyle);
		//		} // custom pitch color selectors (i.e., show color wheel)

		// Custom pitch ColorWheels
		/*		if(controlEvent.getId() > this.firstCustomColorId && (controlEvent.getId() % 3 == 1))
//		{
			// only call updateColors() for colorSelect ColorWheels:
			if(controlEvent.getId() >= this.firstColorSelectId && controlEvent.getId() <= this.lastColorSelectId)
			{
				this.updateColors(this.curColorStyle);
			} // if
/*
			// get current color:
			ColorWheel	curCW	= (ColorWheel)controlEvent.getController();


			int	rgbColor	= curCW.getRGB();
			Color	color	= new Color(rgbColor);

			// Set corresponding Textfield with color value:
			Textfield	curColorTF	= (Textfield)this.sidebarCP5.getController("textfield" + (controlEvent.getId() + 1));
			curColorTF.setText("rgb(" + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue() + ")");


			int	notePos;	// the position in colors that is to be changed.
			id	= controlEvent.getId();

			// canvas color (does not affect notes):
			if(id == 64)
			{
				this.canvasColor[0]	= color.getRed();
				this.canvasColor[1]	= color.getGreen();
				this.canvasColor[2]	= color.getBlue();
			}

			else
			{
				notePos	= this.calculateNotePos(id);

				// error checking
				if(notePos < 0 || notePos > this.colors.length)	{
					throw new IllegalArgumentException("ModuleTemplate.controlEvent - custom color Textfields: " +
							"notePos " + notePos + " from id " + id + " is not a valid note position; " +
							"it should be between 0 and " + this.colors.length);
				} // error checking

				this.colors[notePos][0]	= color.getRed();
				this.colors[notePos][1]	= color.getGreen();
				this.colors[notePos][2]	= color.getBlue();

			}

			//			this.updateColors(this.curColorStyle);
		 */
		//		} // custom pitch colorWheels

		// ColorWheel Textfields (id 23 or over; id % 3 == 2):
		if(controlEvent.getId() > this.firstCustomColorId && (controlEvent.getId() % 3 == 2))
		{
			// This call has to happen early on in the method so that, after making the dichrom/trichrom spectrum,
			// individual colors can be changed independently of the rest.
			//			this.updateColors(this.curColorStyle);
			/*
			id	= controlEvent.getId();
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
					((ColorWheel)this.sidebarCP5.getController("colorWheel" + (id - 1))).setRGB(rgbInt);


					// canvas color (does not affect notes):
					if(id == this.canvasColorSelectId)
					{
						this.canvasColor[0]	= red;
						this.canvasColor[1]	= green;
						this.canvasColor[2]	= blue;
					}

					else
					{
						int	notePos	= this.calculateNotePos(id);


						// error checking
						if(notePos < 0 || notePos > this.colors.length)	{
							throw new IllegalArgumentException("ModuleTemplate.controlEvent - custom color Textfields: " +
									"notePos " + notePos + " from id " + id + " is not a valid note position; " +
									"it should be between 0 and " + this.colors.length);
						} // error checking

						this.colors[notePos][0]	= red;
						this.colors[notePos][1]	= green;
						this.colors[notePos][2]	= blue;
					} // else
				} // if - rgb

				// Setting the corresponding ColorWheel:
				//					ColorWheel	curColorWheel	= (ColorWheel)this.sidebarCP5.getController("textfield" + (controlEvent.getId() + 1));


			} catch(Exception e) {
				System.out.println("Sorry, that is not recognized as a valid color (note that colors must be defined by Integer values). Exception message: "
						+ e.getMessage());
			} // catch
			 */
		} // ColorWheel Textfields


		// Color Style:
		if(controlEvent.getName().equals("rainbow") ||
				controlEvent.getName().equals("dichrom") ||
				controlEvent.getName().equals("trichrom") ||
				controlEvent.getName().equals("custom"))
		{
			Toggle	curToggle	= (Toggle) controlEvent.getController();


			System.out.println("curToggle = " + curToggle.internalValue());
			/*
			if(this.originalColors == null) {
				this.originalColors = new float[super.colors.length][3];
			}
			for(int i = 0; i < this.originalColors.length; i++)
			{
				for(int j = 0; j < this.originalColors[i].length; j++)
				{
					this.originalColors[i][j]	= super.colors[i][j];
				}
			}
			 */
			// Set tonic color/call correct function for the new colorStyle:
			this.updateColors(curToggle.internalValue());


			// Turn off the other Toggles:
			Toggle[] toggleArray	= new Toggle[] {
					(Toggle)this.sidebarCP5.getController("rainbow"),
					(Toggle)this.sidebarCP5.getController("dichrom"),
					(Toggle)this.sidebarCP5.getController("trichrom"),
					(Toggle)this.sidebarCP5.getController("custom")
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

			// Call to rainbow() not in updateColors() so that it doesn't revert every time a button is pressed:
			if(this.curColorStyle == ModuleTemplate01.CS_RAINBOW)
			{
				this.rainbow();

			}


			// Here, fill the CW and TF with the current colors?

			// These calls have to come after all the colors have been set,
			// so that originalColors and hsbColors can be filled with the latest:
			this.fillOriginalColors();
			this.fillHSBColors();
			this.resetModulateSlidersTextfields();
			this.applyColorModulate(this.colors, this.originalColors);

			this.updateColorSelectCWs();

			/*
			if(this.curColorStyle == ModuleTemplate01.CS_RAINBOW)
			{
				int colorSelectId	= (this.canvasColorSelectId % 100) + 301;
				this.updateColorWheel(colorSelectId, 0);
			} // if - rainbow

			if(this.curColorStyle == ModuleTemplate01.CS_DICHROM)
			{
				int colorSelectId	= (this.canvasColorSelectId % 100) + 301;
				this.updateColorWheel(colorSelectId, 0);
				this.updateColorWheel(colorSelectId + 1, (this.colors.length - 1));
			} // if - dichromatic

			if(this.curColorStyle == ModuleTemplate01.CS_TRICHROM)
			{
				int colorSelectId	= (this.canvasColorSelectId % 100) + 301;

				int	colorPos2;
				int	colorPos3;

				if(this.getMajMinChrom() == 2)
				{
					colorPos2	= 4;
					colorPos3	= 8;
				} else {
					colorPos2	= 3;
					colorPos3	= 4;
				}

				this.updateColorWheel(colorSelectId, 0);
				this.updateColorWheel(colorSelectId + 1, colorPos2);
				this.updateColorWheel(colorSelectId + 2, colorPos3);
			} // if - trichromatic
			 */
		} // colorStyle buttons
		/*
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
				this.displaySidebar();
			}

			this.sidebarCP5.getGroup("guideToneBackground").bringToFront();
			this.sidebarCP5.getGroup("guideToneBackground").setVisible(((Toggle) controlEvent.getController()).getBooleanValue());

			// TODO:
			//			this.instrument.setVolume(0.2f);
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
		 */
	} // controlEvent

	/**
	 * Puts the contents of colors into originalColors.
	 */
	/*	private void fillOriginalColors()
	{
		if(this.colors == null)
		{
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
	 */

	/**
	 * Puts the contents of this.colors into this.hsbColors.
	 */
	/*	private	void fillHSBColors()
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
	 */

	/**
	 * Sets the Sliders and Textfields for RGB and HSB color modulate to 0.
	 */
	/*	private void resetModulateSlidersTextfields()
	{
		int	id	= 8;

		for(int i = 0; i < 6 && id < 20; i++)
		{
			try
			{
				this.sidebarCP5.getController("slider" + id).setValue(0);

				id	= id + 2;
			}
			catch(NullPointerException npe)
			{
				System.out.println("ModuleTemplate.resetModulateSlidersTextfields(): caught NullPointerException "
						+ "(possibly during initialization); ");
				npe.printStackTrace();
			}

		}
	}
	 */

	/**
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
		notePos	= ( id - (this.firstCustomColorId % 100) - this.curKeyEnharmonicOffset + 12) % 12;


		int	modCanvasId	= this.canvasColorSelectId % 100;
		if(id >= modCanvasId && id < (modCanvasId + 4))
		{			
			// Canvas:
			if(id == modCanvasId)	{
				throw new IllegalArgumentException("ModuleTemplate.calculateNotePos(int): id 64 should not be passed to this function, as it does not correspond to a note.");
			}

			// Tonic:
			if(id == (modCanvasId + 1))	{	notePos	= 0;	}

			// 2nd Color:
			if(id == (modCanvasId + 2))
			{
				if(this.curColorStyle == ModuleTemplate01.CS_DICHROM)
				{
					// for Dichromatic, this is the last color:
					notePos	= this.colors.length - 1;
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
	/*
	public int getLeftEdgeX() {
		return leftEdgeX;
	}

	public void setLeftEdgeX(int leftEdgeX) {
		this.leftEdgeX = leftEdgeX;
	}
	/*
	public boolean isShowScale() {
		return showScale;
	}

	public void setShowScale(boolean showScale) {
		this.showScale = showScale;
	}
	 */
	/*
	public float getThreshold() {
		return threshold;
	}

	public void setThreshold(float threshold) {
		this.threshold = threshold;
	}
	 */
	/*
	public float getART(int arORt)
	{
		if(arORt < 0 || arORt > this.attackReleaseTransition.length)
		{
			throw new IllegalArgumentException("ModuleTemplate.getART: parameter " + arORt + " is not a valid position in the array this.attackReleaseTransition.");
		} // error checking

		return	this.attackReleaseTransition[arORt];
	} // getART
	 */
	/*
	public float getRedModulate() {
		return this.redGreenBlueMod[0];
	}

	public float getGreenModulate() {
		return this.redGreenBlueMod[1];
	}

	public float getBlueModulate() {
		return this.redGreenBlueMod[2];
	}
	 */

	/*
	public int getCurKeyEnharmonicOffset() {
		return curKeyEnharmonicOffset;
	}

	public int getCurKeyOffset() {
		return curKeyOffset;
	}
	 */	
	public float getCurColorStyle() {
		return this.curColorStyle;
	} // get CurColorStyle
	/*
	public	float[]	getCanvasColor()
	{
		return this.canvasColor;
	}
	 */
	/*
	public int getCheckpoint()				{	return this.checkpoint;	}

	public void setCheckpoint(int newVal)	{	this.checkpoint	= newVal;	}
	 */





	/**
	 * communicates with keyPressed event in draw() of driver
	 * shows menu button on key press
	 * added 1/26/17 Elena Ryan
	 */
	/*	public void setMenuVal() {
		//this.menuVis = true;	
		((Toggle)this.sidebarCP5.getController("menuButton")).setState(false);
		this.sidebarCP5.getController("hamburger").setVisible(true);
	}//set menu val
	 */


}