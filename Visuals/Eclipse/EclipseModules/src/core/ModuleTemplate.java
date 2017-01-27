package core;

import java.awt.Color;
import java.text.DecimalFormat;
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
 */
public class ModuleTemplate {

	private	static	float	CS_RAINBOW	= 1;
	private	static	float	CS_DICHROM	= 2;
	private	static	float	CS_TRICHROM	= 3;
	private  static	float	CS_CUSTOM	= 4;
	private	float	curColorStyle;
//	private boolean menuVis = false;

	/**
	 * DecimalFormat used for rounding the text corresponding to Sliders and Colorwheels.
	 */
	private	DecimalFormat	decimalFormat	= new DecimalFormat("#.##");

	String  inputFile;
	
	// Global vars - TODO: all private!
	private	PApplet		parent;
	//	public ControlP5 	nonSidebarCP5;
	public ControlP5 	sidebarCP5;
	private	Input		input;


	private	int			leftAlign;
	private	int			leftEdgeX;

	private	String		sidebarTitle;

	private	int			majMinChrom;
	public	String		curKey;
	private	int			scaleLength;
	private int 		curKeyOffset;
	private int 		curKeyEnharmonicOffset;
	// to line pitches up with the correct scale degree of the current key.

	private	final String[]	notesAtoAbFlats	= new String[] { 
			"A", "Bb", "B", "C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab"
	};

	private final String[]	notesAtoGSharps	= new String[] { 
			"A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#"
	};

	// ALL notes here
	private	final String[]	allNotes	= new String[] {
			"A", "A#", "Bb", "B", "C", "C#", "Db", "D", "D#", "Eb", "E", "F", "F#", "Gb", "G", "G#", "Ab"
	}; // allNotes

	// Positions in filenames String[] here
	private	final int[]	enharmonicPos	= new int[] {
			0, 1, 1, 2, 3, 4, 4, 5, 6, 6, 7, 8, 9, 9, 10, 11, 11
	}; // enharmonicPos

	// File names here
	private	final	String[][] filenames	= new String[][] {
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

	public	float[][]	colors;
	private int[] 		rootColor;
	private	float[][]	originalColors;	// filled in the Custom color style to allow RGB modifications to colors

	int[]				textYVals;
	int[]				noteYVals;
	int[]				modulateYVals;

	private	boolean		showScale;

	private	float		thresholdLevel;

	private	float[]		attackReleaseTransition;

	private	float[]	redGreenBlueMod;	// this will store the red/green/blue modulate values

	public ModuleTemplate(PApplet parent, Input input, String sidebarTitle)
	{
		this.parent	= parent;
		this.input	= input;

		// ControlP5 for playButton and hamburger - now put them on the same ControlP5, with a group for the sidebar controls:
		//		this.nonSidebarCP5	= new ControlP5(this.parent);

		// ControlP5 for most of the sidebar elements:
		this.sidebarCP5		= new ControlP5(this.parent);

		// This technically works, but it's horribly blurry:
		//		this.sidebarCP5.setFont(this.parent.createFont("Consolas", 10) );


		// ControlP5 for ColorWheels (having a separate one allows us to setAutoDraw(false) on the other CP5,
		// draw a transparent rectangle over those controllers, and then draw the ColorWheel on top of that):
		//		this.colorWheelCP5	= new ControlP5(this.parent);
		//		this.sidebarCP5.setVisible(false);


		//		this.leftEdgeXArray	= new int[] { 0, this.parent.width / 3 };
		this.setLeftEdgeX(0);
		this.leftAlign	= (this.parent.width / 3) / 4;

		this.sidebarTitle	= sidebarTitle;

		this.colors 		= new float[12][3];
		this.rootColor		= new int[3];
		this.originalColors	= new float[12][3];

		this.curColorStyle	= ModuleTemplate.CS_RAINBOW;
		// The following will happen in rainbow():
		//		this.rootColor	= new int[] { 255, 0, 0, };

		this.curKey			= "A";
		this.majMinChrom	= 2;

		// Can't call setCurKey just yet, because the dropdown list hasn't been initialized,
		// and it is called as part of setCurKey.
		//		this.setCurKey(this.curKey, this.majMinChrom);
		this.rainbow();

		this.textYVals		= new int[9];
		this.noteYVals		= new int[6];
		this.modulateYVals	= new int[3];

		this.attackReleaseTransition	= new float[3];
		this.redGreenBlueMod		 	= new float[3];

		//TODO: make initModuleTemplate() private again, once it can be called from constructor.
		//this.initModuleTemplate();
	} // ModuleTemplate

	// Methods:

	/**
	 * Called from constructor to calculate Y vals and call the methods for instantiating the necessary buttons;
	 * will eventually call different button methods depending on the module number.
	 */
	public void initModuleTemplate()
	{
		this.sidebarCP5.addGroup("sidebarGroup")
		.setBackgroundColor(this.parent.color(0))
		.setSize(this.parent.width / 3, this.parent.height + 1)
		.setVisible(false);

		// Add play button, hamburger and menu x:
		addOutsideButtons();

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

		// call add methods:
		addHideButtons(textYVals[0]);

		// TODO: pass it one Y and either a height and spacer or a distance between y's.
		addSliders(textYVals[1], textYVals[2], textYVals[3], textYVals[4]);

		addKeySelector(textYVals[5]);

		addRootColorSelector(textYVals[6]);

		addColorStyleButtons(textYVals[7]);

		this.addCustomPitchColor(textYVals[8], noteYVals);

		addModulateSliders(modulateYVals);

		hideTextLabels();

		this.sidebarCP5.getController("keyDropdown").bringToFront();
	} // initModuleTemplate

	
	/*
	 *  - alignLeft (x var to pass to the add functions)
	 *  - yValues (will pass the appropriate one to each of the functions)
	 *  TODO: how calculate these y values?  (for now, imagine they are correct...)
	 *  
	 */
	private void addOutsideButtons()
	{
		int	playX		= this.parent.width - 45;
		int	playY		= 15;
		int	playWidth	= 30;
		int	playHeight	= 30;

		// add play button:
		PImage[]	images	= { this.parent.loadImage("playButton.png"), this.parent.loadImage("stopButton.png") };

		images[0].resize(playWidth - 5, playHeight);
		images[1].resize(playWidth, playHeight);
		this.sidebarCP5.addToggle("play")
		.setPosition(playX, playY)
		.setImages(images)
		.updateSize();

		int	hamburgerX		= 10;
		int	hamburgerY		= 13;
		int	hamburgerWidth	= 30;
		int	hamburgerHeight	= 30;

		PImage	hamburger	= this.parent.loadImage("hamburger.png");
		hamburger.resize(hamburgerWidth, hamburgerHeight);
		this.sidebarCP5.addButton("hamburger")
		.setPosition(hamburgerX, hamburgerY)
		.setImage(hamburger)
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

	private void addHideButtons(int	hideY)
	{
		//TODO: can all labels connected to this controller be Center aligned automatically?
		int	hideWidth     = 70;
		int hideSpace	= 4;

		int	labelX		= 10;
		int	playButtonX	= this.leftAlign;
		int	menuButtonX	= this.leftAlign + hideWidth + hideSpace;
		int	scaleX		= this.leftAlign + (+ hideWidth + hideSpace) * 2;

		this.sidebarCP5.addTextlabel("hide")
		.setPosition(labelX, hideY + 4)
		.setGroup("sidebarGroup")
		.setValue("Hide");

		this.sidebarCP5.addToggle("playButton")
		.setPosition(playButtonX, hideY)
		.setWidth(hideWidth)
		.setGroup("sidebarGroup")
		.setId(4);
		this.sidebarCP5.getController("playButton").getCaptionLabel().set("Play Button").align(ControlP5.CENTER, ControlP5.CENTER);


		this.sidebarCP5.addToggle("menuButton")
		.setPosition(menuButtonX, hideY)
		.setWidth(hideWidth)
		.setGroup("sidebarGroup")
		.setId(5);
		this.sidebarCP5.getController("menuButton").getCaptionLabel().set("Menu Button").align(ControlP5.CENTER, ControlP5.CENTER);


		this.sidebarCP5.addToggle("scale")
		.setPosition(scaleX, hideY)
		.setWidth(hideWidth)
		.setGroup("sidebarGroup")
		.setId(6);
		this.showScale = true;
		this.sidebarCP5.getController("scale").getCaptionLabel().set("Scale").align(ControlP5.CENTER, ControlP5.CENTER);

	} // addHideButtons

	/**
	 * Method called during initialization to instatiate the Threshold, Attack, Release,
	 * and Transition sliders.
	 * 
	 * Sliders have an odd and Textfields an even ID number, all less than 10 (no duplicates allowed).
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
	private void addSliders(int thresholdY, int attackY, int releaseY, int transitionY)
	{
		int	labelX			= 10;
		int	labelWidth		= 70;

		int	sliderWidth		= 145;
		int	sliderHeight	= 20;

		int	spacer			= 5;
		int	tfWidth			= 70;

		this.sidebarCP5.addLabel("thresholdLabel")

				.setPosition(labelX, thresholdY + 4)
				.setWidth(labelWidth)
				.setGroup("sidebarGroup")
				.setValue("Threshold");
		//System.out.println("sliderWidth = " + sliderWidth + "; sliderHeight = " + sliderHeight);


		// Threshold slider:
		this.sidebarCP5.addSlider("slider0")
		.setPosition(this.leftAlign, thresholdY)
		.setSize(sliderWidth, sliderHeight)
		.setSliderMode(Slider.FLEXIBLE)
		.setRange(2, 100)
		.setValue(10)
		.setLabelVisible(false)
		.setGroup("sidebarGroup")
		.setId(0);
		this.setThresholdLevel(10);

		// Threshold textfield:
		this.sidebarCP5.addTextfield("textfield1")
		.setPosition(this.leftAlign + sliderWidth + spacer, thresholdY)
		.setSize(tfWidth, sliderHeight)
		.setText(this.sidebarCP5.getController("slider0").getValue() + "")
		.setLabelVisible(false)
				//.setText(this.sidebarCP5.getController("slider0").getValue() + "")
				//.setLabelVisible(false)

		.setAutoClear(false)
		.setGroup("sidebarGroup")
		.setId(1);

		// Test: not adding them as variables, seeing how that goes. :)

		// Attack group:
		//	- Textlabel:
		this.sidebarCP5.addLabel("attackLabel")
		.setPosition(labelX, attackY + 4)
		.setWidth(labelWidth)
		.setGroup("sidebarGroup")
		.setValue("Attack");

		//	- Slider:
		this.sidebarCP5.addSlider("slider2")
		.setPosition(this.leftAlign, attackY)
		.setSize(sliderWidth, sliderHeight)
		.setSliderMode(Slider.FLEXIBLE)
		.setRange(2, 50)
		.setValue(10)
		.setLabelVisible(false)
		.setGroup("sidebarGroup")
		.setId(2);

		// Setting attack for reference by Module:
		this.attackReleaseTransition[0]	= 10;

		//	- Textfield:
		this.sidebarCP5.addTextfield("textfield3")
		.setPosition(this.leftAlign + sliderWidth + spacer, attackY)
		.setSize(tfWidth, sliderHeight)
		.setText(this.sidebarCP5.getController("slider2").getValue() + "")
		.setAutoClear(false)
		.setGroup("sidebarGroup")
		.setId(3);


		// Release:
		// - Textlabel:
		this.sidebarCP5.addLabel("releaseLabel")
		.setPosition(labelX, releaseY + 4)
		.setWidth(labelWidth)
		.setGroup("sidebarGroup")
		.setValue("Release");

		//	- Slider:
		this.sidebarCP5.addSlider("slider4")
		.setPosition(this.leftAlign, releaseY)
		.setSize(sliderWidth, sliderHeight)
		.setSliderMode(Slider.FLEXIBLE)
		.setRange(2, 50)
		.setValue(10)
		.setLabelVisible(false)
		.setGroup("sidebarGroup")
		.setId(4);

		// Setting release for reference by Module:
		this.attackReleaseTransition[1]	= 10;

		//	- Textlabel:
		this.sidebarCP5.addTextfield("textfield5")
		.setPosition(this.leftAlign + sliderWidth + spacer, releaseY)
		.setSize(tfWidth, sliderHeight)
		.setText(this.sidebarCP5.getController("slider4").getValue() + "")
		.setAutoClear(false)
		.setGroup("sidebarGroup")
		.setId(5);

		// Transition:
		// - Textlabel:
		this.sidebarCP5.addLabel("transitionLabel")
		.setPosition(labelX, transitionY + 4)
		.setWidth(labelWidth)
		.setGroup("sidebarGroup")
		.setValue("Transition");

		//	- Slider:
		this.sidebarCP5.addSlider("slider6")
		.setPosition(this.leftAlign, transitionY)
		.setSize(sliderWidth, sliderHeight)
		.setSliderMode(Slider.FLEXIBLE)
		.setRange(2, 50)
		.setValue(10)
		.setLabelVisible(false)
		.setGroup("sidebarGroup")
		.setId(6);

		// Setting transition for reference by Module:
		this.attackReleaseTransition[2]	= 10;

		//	- Textlabel:
		this.sidebarCP5.addTextfield("textfield7")
		.setPosition(this.leftAlign + sliderWidth + spacer, transitionY)
		.setSize(tfWidth, sliderHeight)
		.setText(this.sidebarCP5.getController("slider6").getValue() + "")
		.setAutoClear(false)
		.setGroup("sidebarGroup")
		.setId(7);

	} // addSliders



	/**
	 * Method called during instantiation to initialize the key selector drop-down menu (ScrollableList)
	 * and major/minor/chromatic selection buttons.
	 * 
	 * @param keyY	y value of the menu and buttons.
	 */
	private void addKeySelector(int	keyY)
	{

		int	labelX			= 10;

		int	listWidth		= 65;
		int	spacer			= 5;

		int	toggleWidth		= 45;
		int	majorX			= this.leftAlign + listWidth + spacer;
		int	minorX			= this.leftAlign + listWidth + spacer + (toggleWidth + spacer);
		int	chromX			= this.leftAlign + listWidth + spacer + ((toggleWidth + spacer) * 2);
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
		.setCaptionLabel("Chromatic")
		.setGroup("sidebarGroup")
		.setInternalValue(2);
		this.sidebarCP5.getController("chrom").getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);

		((Toggle)(this.sidebarCP5.getController("chrom"))).setState(true);
	} // addKeySelector

	/**
	 * Method called during instantiation to initialize the root color selector.
	 * 
	 * @param rootColorY	y value of the root color selector.
	 */
	private void addRootColorSelector(int rootColorY)
	{
		int	labelX			= 10;
		int	buttonX			= this.leftAlign;
		int	buttonWidth		= 50;
		int	textfieldX		= buttonX + buttonWidth + 5;
		int	textfieldWidth	= 90;

		this.sidebarCP5.addTextlabel("rootColor")
		.setPosition(labelX, rootColorY + 4)
		.setGroup("sidebarGroup")
		.setValue("Root Color");

		// Buttons, ColorWheels and corresponding Textfields will have id's of 21 or over;
		// Button id % 3 == 0; ColorWheel id % 3 == 1, Textfield id % 3 == 2.

		// Needs to be added to sidebarCP5 so it is still visible to turn off the ColorWheel:
		// (name follows conventions for customPitchColor buttons)
		this.sidebarCP5.addButton("rootColorButton")
		.setPosition(buttonX, rootColorY)
		.setWidth(buttonWidth)
		.setLabel("Root")
		.setGroup("sidebarGroup")
		.setId(21);

		//for(int i = 0; i < ; i++)
		//{
		this.sidebarCP5.addColorWheel("rootColorWheel")
		.setPosition(this.leftAlign, rootColorY + 20)
		.setRGB(this.parent.color(255,0,0))
		.setLabelVisible(false)
		.setVisible(false)
		.setGroup("sidebarGroup")
		.setId(22);
		//}

		this.sidebarCP5.addTextfield("rootColorTF")
		.setPosition(textfieldX, rootColorY)
		.setWidth(textfieldWidth)
		.setAutoClear(false)
		.setText("Code#")
		.setGroup("sidebarGroup")
		.setId(23);
	} // addRootColorSelector

	/**
	 * Method called during instantiation to initialize the color style Toggles
	 * (Rainbow, Dichromatic, Trichromatic, and Custom).
	 * 
	 * @param colorStyleY	y value of the colorStyle Toggles
	 */
	private void addColorStyleButtons(int colorStyleY)
	{
		int	colorStyleWidth	= 50;
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
		.setInternalValue(ModuleTemplate.CS_RAINBOW);
		this.sidebarCP5.getController("rainbow").getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);

		this.sidebarCP5.addToggle("dichrom")
		.setPosition(dichromaticX, colorStyleY)
		.setWidth(colorStyleWidth)
		.setCaptionLabel("Dichrom.")
		.setGroup("sidebarGroup")
		.setInternalValue(ModuleTemplate.CS_DICHROM);
		this.sidebarCP5.getController("dichrom").getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);

		this.sidebarCP5.addToggle("trichrom")
		.setPosition(trichromaticX, colorStyleY)
		.setWidth(colorStyleWidth)
		.setCaptionLabel("Trichrom.")
		.setGroup("sidebarGroup")
		.setInternalValue(ModuleTemplate.CS_TRICHROM);
		this.sidebarCP5.getController("trichrom").getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);

		this.sidebarCP5.addToggle("custom")
		.setPosition(customX, colorStyleY)
		.setWidth(colorStyleWidth)
		.setCaptionLabel("Custom")
		.setGroup("sidebarGroup")
		.setInternalValue(ModuleTemplate.CS_CUSTOM);
		this.sidebarCP5.getController("custom").getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);

		((Toggle) this.sidebarCP5.getController("rainbow")).setState(true);
	} // addColorStyleButtons

	/**
	 * Method called during instantiation to initialize note buttons and their corresponding ColorWheels.
	 * 
	 * @param noteYVals	int[] of y values for each note button
	 */
	private void addCustomPitchColor(int labelYVal, int[] noteYVals)
	{
		int spacer1			= 5;	// between buttons and textfields
		int	spacer2			= 15;	// between the two rows of pitches
		int	labelX			= 10;

		int	buttonWidth		= 30;
		int	textfieldWidth	= 90;

		int	noteX1			= this.leftAlign - 40;
		int	textfieldX1		= noteX1 + buttonWidth + spacer1;

		int	noteX2			= textfieldX1 + textfieldWidth + spacer2;
		int	textfieldX2		= noteX2 + buttonWidth + spacer1;

		int	colorWheelX		= textfieldX1;


		String[]	noteNames1	= new String[] {
				"A", "A#/Bb", "B", "C", "C#/Db", "D"
		}; // noteNames
		String[]	noteNames2	= new String[] {
				"D#/Eb", "E", "F", "F#/Gb", "G", "G#/Ab"
		}; // noteNames2

		this.sidebarCP5.addTextlabel("customPitchColor")
		.setPosition(labelX, labelYVal + 4)
		.setGroup("sidebarGroup")
		.setValue("Custom Pitch Color");

		// Note Buttons, ColorWheels and corresponding Textfields will have id's of 24 or over;
		// Button id % 3 == 0; ColorWheel id % 3 == 1, Textfield id % 3 == 2.
		int	namePos	= 0;
		int	id		= 24;
		int colorpos  = 0;
		
		// First row of pitches:
		for(int i = 0; i < noteNames1.length; i++)
		{
			// Needs to be added to sidebarCP5 so it is still visible to turn off the ColorWheel:
			this.sidebarCP5.addButton("button" + id)
			.setPosition(noteX1, noteYVals[i])
			.setWidth(buttonWidth)
			.setLabel(noteNames1[namePos])
			.setId(id)
			.setGroup("sidebarGroup")
			.getCaptionLabel().toUpperCase(false);

			id = id + 1;
			
			this.sidebarCP5.addColorWheel("colorWheel" + id)
			.setPosition(colorWheelX, noteYVals[i] - 200)		// 200 = height of ColorWheel
			.setRGB(this.parent.color(colors[colorpos][0], colors[colorpos][1], colors[colorpos][2]))
			.setLabelVisible(false)
			.setVisible(false)
			.setGroup("sidebarGroup")
			.setId(id);
			
			colorpos = colorpos + 1;
			
			id = id + 1;

			this.sidebarCP5.addTextfield("textfield" + id)
			.setPosition(textfieldX1, noteYVals[i])
			.setWidth(textfieldWidth)
			.setAutoClear(false)
			.setText("Code#")
			.setGroup("sidebarGroup")
			.setId(id);

			id = id + 1;
			namePos	= namePos + 1;
		}// first row of pitches

		namePos	= 0;
		for(int i = 0; i < noteNames1.length; i++)
		{// Second row of pitches:
		
			this.sidebarCP5.addButton("button" + id)
			.setPosition(noteX2, noteYVals[i])
			.setWidth(buttonWidth)
			.setLabel(noteNames2[namePos])
			.setId(id)
			.setGroup("sidebarGroup")
			.getCaptionLabel().toUpperCase(false);

			id = id + 1;

			this.sidebarCP5.addColorWheel("colorWheel" + id)
			.setPosition(noteX2, noteYVals[i] - 200)
			.setRGB(this.parent.color(colors[colorpos][0], colors[colorpos][1], colors[colorpos][2]))
			.setLabelVisible(false)
			.setVisible(false)
			.setGroup("sidebarGroup")
			.setId(id);
			
			colorpos = colorpos + 1;

			id = id + 1;

			this.sidebarCP5.addTextfield("textfield" + id)
			.setPosition(textfieldX2, noteYVals[i])
			.setWidth(textfieldWidth)
			.setAutoClear(false)
			.setText("Code#")
			.setGroup("sidebarGroup")
			.setId(id);

			id = id + 1;
			namePos	= namePos + 1;
		} 
		// for - second row of pitches

		Color	transparentBlack	= new Color(0, 0, 0, 200);
		int		transBlackInt		= transparentBlack.getRGB();

		this.sidebarCP5.addBackground("background")
		.setPosition(0, 0)
		.setSize(this.parent.width / 3, this.parent.height)
		.setBackgroundColor(transBlackInt)
		.setGroup("sidebarGroup")
		.setVisible(false);
	} // addNoteColorSelectors
	

	/**
	 * Method called during instatiation to hide text labels of text fields
	 * Elena Ryan
	 * Added 1/24/17
	 */	
	private void hideTextLabels() {
		for(int i = 1; i<14; i++){
			if(i%2 == 1){
				this.sidebarCP5.getController("textfield"+i).getCaptionLabel().setVisible(false);
			}
		}//hides slider labels

		this.sidebarCP5.getController("rootColorTF").getCaptionLabel().setVisible(false);

		for(int i = 24;i<60; i++){
			if(i%3 == 2){
				this.sidebarCP5.getController("textfield"+i).getCaptionLabel().setVisible(false);
			}
		}//hides text labels for colors

	}//hideTextLabels

	/**
	 * Method called during instantiation, to initialize the color modulate sliders.
	 * 
	 * @param modulateYVals	int[] of the y values of the red, green, and blue sliders, respectively.
	 */
	private void addModulateSliders(int[] modulateYVals)
	{
		int	labelX			= 10;
		int	labelWidth		= 70;

		int	sliderWidth		= 145;
		int	sliderHeight	= 20;

		int	spacer			= 5;	// distance between slider and corresponding textfield
		int	tfWidth			= 70;	// width of Textfields

		String[]	names	= new String[] { "redModLabel", "greenModLabel", "blueModLabel" };
		String[]	values	= new String[] { "Red Modulate", "Green Mod.", "Blue Modulate" };

		int	id	= 8;		// this id picks up where the transition textfield - "textfield7" - left off.

		for(int i = 0; i < modulateYVals.length; i++)
		{
			// - Textlabel:
			this.sidebarCP5.addLabel(names[i])
			.setPosition(labelX, modulateYVals[i] + 4)
			.setWidth(labelWidth)
			.setGroup("sidebarGroup")
			.setValue(values[i]);

			//	- Slider:
			this.sidebarCP5.addSlider("slider" + id)
			.setPosition(this.leftAlign, modulateYVals[i])
			.setSize(sliderWidth, sliderHeight)
			.setSliderMode(Slider.FLEXIBLE)
			.setRange(-255, 255)
			.setValue(0)
			//.setLabelVisible(false)
			.setGroup("sidebarGroup")
			.setId(id);

			id	= id + 1;

			//	- Textlabel:
			this.sidebarCP5.addTextfield("textfield" + id)
			.setPosition(this.leftAlign + sliderWidth + spacer, modulateYVals[i])
			.setSize(tfWidth, sliderHeight)
			.setText(this.sidebarCP5.getController("slider" + (id-1)).getValue() + "")
			.setAutoClear(false)
			.setGroup("sidebarGroup")
			.setId(id);

			id	= id + 1;
		} // for
	} // addModulateSliders



	public void update()
	{
		this.sidebarCP5.getController("textfield1").setValue(this.sidebarCP5.getController("slider0").getValue());
		((Textfield)this.sidebarCP5.getController("textfield1")).setText(this.sidebarCP5.getController("slider0").getValue() + "");

	} // update

	private void updateColors(float colorStyle)
	{
		
		if(colorStyle < 1 || colorStyle > 4) {
			throw new IllegalArgumentException("Module_01_02.updateColors: char paramter " + colorStyle + " is not recognized; must be 1 - 4.");
		}

		this.curColorStyle	= colorStyle;

		// Rainbow:
		if(this.curColorStyle == ModuleTemplate.CS_RAINBOW)
		{
			this.rainbow();
			System.out.print("     RAINBOW");
		}

		// Dichromatic:
		if(this.curColorStyle == ModuleTemplate.CS_DICHROM)
		{
			this.dichromatic_OneRGB(/*this.rootColor*/this.colors[0]);
			System.out.print("     DICHROM");
		}

		// Trichromatic:
		if(this.curColorStyle == ModuleTemplate.CS_TRICHROM)
		{
			this.trichromatic_OneRGB(/*this.rootColor*/this.colors[0]);
			System.out.print("     TRICHROM");
		}

		// Custom:
		if(this.curColorStyle == ModuleTemplate.CS_CUSTOM)
		{			
			// First, set the key to chromatic:
			this.setCurKey("A", 2);
			System.out.print("     CUSTOM");
			// Then populate the textfields with the current colors in the colors array:
			// (textfield id's start at 23 and go up by 3)
			int	id	= 26;
			Textfield	curTextfield;
			
			for(int colorPos = 0; colorPos < this.colors.length; colorPos++)
			{
				
				curTextfield 	= (Textfield)this.sidebarCP5.getController("textfield" + id);
				curTextfield.setText("rgb(" + this.colors[colorPos][0] + ", " + this.colors[colorPos][1] + ", " + this.colors[colorPos][2] + ")");
				id	= id + 3;
			} // for - colorPos

			
			// Applies the values of the Red Modulate/Green Modulate/Blue Modulate sliders:
			

			this.applyColorModulate(this.colors, this.originalColors);
			((Toggle)(this.sidebarCP5.getController("chrom"))).setState(true);

			// (The functionality in controlEvent will check for custom, and if it is custom, they will set their position of colors to their internal color.)
			

			// (Will they need to check to make sure that the key is actually chromatic?)
		} // custom colorStyle
		
	} // updateColors

	public void legend(int goalHuePos)
	{

		this.parent.textSize(24);

//		String[]	notes	= this.getScale(this.curKeyOffset, this.majMinChrom);
		String[]	notes	= this.getScale(this.curKey, this.getMajMinChrom());

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
			scaleDegree	= this.getScaleDegrees()[this.getMajMinChrom()][i];
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
	} // legend

	void displaySidebar()
	{	
		this.sidebarCP5.getGroup("sidebarGroup").setVisible(true);
		this.setLeftEdgeX(this.parent.width / 3);

	} // displaySidebar

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

/*
		for(int i = 0; i < result.length; i++)
		{
			System.out.println("  result[" + i + "] = " + result[i]);
		}
*/
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
	private int arrayContains(int[] array, int element) {
		if(array == null) {
			throw new IllegalArgumentException("Module_01_02.arrayContains(int[], int): array parameter is null.");
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
	 * Converts the given color to HSB and sends it to dichromatic_OneHSB.
	 * (dichromatic_OneHSB will send it to _TwoHSB, which will set this.colors, changing the scale.)
	 * 
	 * @param rgbVals	float[] of RGB values defining the color for the root of the scale.
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
	 * @param hue	float[] of HSB values defining the color at the root of the current scale.
	 */
	private void dichromatic_OneHSB(float[] hsbVals)
	{
		if(hsbVals == null) {
			throw new IllegalArgumentException("Module_01_02.dichromatic_OneHSB: float[] parameter hsbVals is null.");
		} // error checking

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

		// Create an array the length of the current scale
		// and fill it with the dichromatic spectrum:
		float[][]	dichromColors	= new float[this.scaleLength][3];

		for(int i = 0; i < rgbVals1.length; i++)
		{
			dichromColors[0][i]	= rgbVals1[i];
		}
		for(int i = 1; i < dichromColors.length; i++)
		{
			for(int j = 0; j < this.colors[i].length; j++)
			{
				dichromColors[i][0]	= dichromColors[i - 1][0] - redDelta;
				dichromColors[i][1]	= dichromColors[i - 1][1] - greenDelta;
				dichromColors[i][2]	= dichromColors[i - 1][2] - blueDelta;
			} // for - j
		} // for - i

		// Fill colors with either the contents of the dichromatic color array
		// or with black, depending on whether or not a scale degree is diatonic:
		int	dichromColorPos	= 0;
		for(int i = 0; i < this.colors.length && dichromColorPos < dichromColors.length; i++)
		{
			dichromColorPos	= this.scaleDegreeColors[this.majMinChrom][i];

			this.colors[i][0]	= dichromColors[dichromColorPos][0];
			this.colors[i][1]	= dichromColors[dichromColorPos][1];
			this.colors[i][2]	= dichromColors[dichromColorPos][2];

			/*
			if(this.arrayContains(this.scaleDegrees[this.majMinChrom], i) != -1)
			{
				// if scale degree is diatonic:
				this.colors[i][0]	= dichromColors[dichromColorPos][0];
				this.colors[i][1]	= dichromColors[dichromColorPos][1];
				this.colors[i][2]	= dichromColors[dichromColorPos][2];

				dichromColorPos	= dichromColorPos + 1;
			} else {
				// if the scale degree is not diatonic:
				this.colors[i]	= new float[] { 0, 0, 0 };
			}
			 */
		} // for - filling colors
	} // dichromatic_TwoRGB

	/**
	 * Converts the given color to HSB and sends it to dichromatic_OneHSB.
	 * (dichromatic_OneHSB will send it to _TwoHSB, which will set this.colors, changing the scale.)
	 * 
	 * @param rgbVals	float[] of RGB values defining the color for the root of the scale.
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

	public void trichromatic_ThreeRGB(float[] rgbVals1, float[] rgbVals2, float[] rgbVals3)
	{
		if(rgbVals1 == null || rgbVals2 == null || rgbVals3 == null) {
			throw new IllegalArgumentException("Module_01_02.trichromatic_ThreeRGB: at least one of the float[] parameters is null.");
		} // error checking

		int	color1pos	= 0;
		int	color2pos;
		int	color3pos;

		if(this.getMajMinChrom() == 2)
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

		// This array has the trichromatic spectrum:
		float[][]	trichromColors	= new float[this.scaleLength][3];

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

		// fill colors with either the trichrom spectrum (diatonic notes) or black (non-diatonic notes):
		int	trichromColorPos	= 0;
		for(int i = 0; i < this.colors.length && trichromColorPos < trichromColors.length; i++)
		{
			trichromColorPos	= this.scaleDegreeColors[this.majMinChrom][i];

			this.colors[i][0]	= trichromColors[trichromColorPos][0];
			this.colors[i][1]	= trichromColors[trichromColorPos][1];
			this.colors[i][2]	= trichromColors[trichromColorPos][2];

			/*
				// note is diatonic
				this.colors[i][0]	= trichromColors[trichromColorPos][0];
				this.colors[i][1]	= trichromColors[trichromColorPos][1];
				this.colors[i][2]	= trichromColors[trichromColorPos][2];

				trichromColorPos	= trichromColorPos + 1;
			} else {
				// not is non-diatonic
				this.colors[i]	= new float[] { 0, 0, 0 };
			}
			 */

		} // for
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

		for(int i = 0; i < this.colors.length && i < rainbowColors[this.getMajMinChrom()].length; i++)
		{
			for(int j = 0; j < this.colors[i].length && j < rainbowColors[this.getMajMinChrom()][i].length; j++)
			{
				//				this.getColors()[i][j]	= rainbowColors[this.getMajMinChrom()][i][j];
				this.colors[i][j]	= rainbowColors[this.getMajMinChrom()][i][j];
			} // for - j (going through rgb values)
		} // for - i (going through colors)

	} // rainbow

	/**
	 * Applies the values of the Red Modulate/Green Modulate/Blue Modulate sliders.
	 */
	private void applyColorModulate(float[][] colors, float[][] originalColors)
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

				//TODO: Also pass in redGreenBlueMod?
			} // for - j
		} // for - i
	} // applyColorModulate

	/**
	 * This method handles the functionality of all the buttons, sliders, and textFields;
	 * Notate bene: any classes that include a moduleTemplate *must* include a controlEvent(ControlEvent) method that calls this method.
	 * 
	 * @param theControlEvent	ControlEvent used to determine which controller needs to act.
	 */
	public void controlEvent(ControlEvent controlEvent)
	{
		//System.out.println("ModuleTemplate: theControlEvent.getController() = " + controlEvent.getController());


		int	id	= controlEvent.getController().getId();
		// Play button:
		if(controlEvent.getController().getName().equals("play"))
		{
			for (int i = 0; i < input.getuGenArray().length; i++)
			{
				input.getuGenArray()[i].pause(true);			
			} // for
			

			if(((Toggle)controlEvent.getController()).getBooleanValue())
			{
				this.input.uGenArrayFromSample(this.inputFile);
			} else {
				this.input.uGenArrayFromNumInputs(1);
			}
			
		} // if - play

		// Hamburger button:
		if(controlEvent.getController().getName().equals("hamburger"))
		{
			this.displaySidebar();
			controlEvent.getController().setVisible(false);
			this.sidebarCP5.getWindow().resetMouseOver();
		} // if - hamburger

		// MenuX button:
		if(controlEvent.getController().getName().equals("menuX"))
		{
			this.setLeftEdgeX(0);
			//			this.sidebarCP5.setVisible(false);
			this.sidebarCP5.getGroup("sidebarGroup").setVisible(false);
			//this.sidebarCP5.getController("hamburger").setVisible(true);
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
			this.sidebarCP5.getController("hamburger").setVisible(false);
			//this.sidebarCP5.getController("hamburger").setVisible(!this.sidebarCP5.getController("hamburger").isVisible());
		} // if - hidePlayButton

		// Hide scale:
		if(controlEvent.getName().equals("scale"))
		{
			this.setShowScale(!((Toggle) (controlEvent.getController())).getState());
		}

		//TODO: set this cutoff in a more relevant place - perhaps when sliders are created?
		// (If I have a numSliders, it would be (numSliders * 2).
		int	sliderCutoff	= 14;

		// Sliders (sliders have odd id num and corresponding textfields have the next odd number)
		if(id % 2 == 0 && id < sliderCutoff)
		{
			Slider	curSlider	= (Slider)this.sidebarCP5.getController("slider" + id);
			Textfield	curTextfield	= (Textfield)this.sidebarCP5.getController("textfield" + (id + 1));
			String	sliderValString	= this.decimalFormat.format(curSlider.getValue());

			curTextfield.setText(sliderValString);

			float	sliderValFloat	= Float.parseFloat(sliderValString);

			// Threshold:
			if(id == 0)
			{
				this.setThresholdLevel(sliderValFloat);
			}

			// Attack, Release, and Transition:
			if(id == 2 || id == 4 || id == 6)
			{
				int	pos	= (id / 2) - 1;
				this.attackReleaseTransition[pos]	= sliderValFloat;
			}

			// Red Modulate/Green Modulate/Blue Modulate:
			if(id == 8 || id == 10 || id == 12)
			{
				int	pos	= (id / 2) - 4;		// red = 0, green = 1, blue = 2
				this.redGreenBlueMod[pos]	= sliderValFloat;

				if(this.curColorStyle == ModuleTemplate.CS_CUSTOM)
				{
					this.applyColorModulate(this.colors, this.originalColors);
				}
			} // red/green/blue mod
		}

		// Textfields
		if(id % 2 == 1 && id < sliderCutoff && id > 0)
		{
			Textfield	curTextfield	= (Textfield)this.sidebarCP5.getController("textfield" + id);
			Slider		curSlider		= (Slider)this.sidebarCP5.getController("slider" + (id - 1));

			try	{
				curSlider.setValue(Float.parseFloat(curTextfield.getStringValue()));
			} catch(NumberFormatException nfe) {
				//System.out.println("ModuleTemplate.controlEvent: string value " + curTextfield.getStringValue() + 
						//"for controller " + curTextfield + " cannot be parsed to a float.  Please enter a number.");
			} // catch
		} // textField

		// Key dropdown ScrollableList:
		if(controlEvent.getName().equals("keyDropdown"))
		{
			// keyPos is the position of the particular key in the Scrollable List:
			int	keyPos	= (int)controlEvent.getValue();
			System.out.println("  keyDropdown: keyPos = " + keyPos);

			// getItem returns a Map of the color, state, value, name, etc. of that particular item
			//  in the ScrollableList:
			Map<String, Object> keyMap = this.sidebarCP5.get(ScrollableList.class, "keyDropdown").getItem(keyPos);
			// All we want is the name:
			String	key	= (String) keyMap.get("name");
			this.curKey	= key;
			this.curKeyOffset = keyPos;
			this.curKeyEnharmonicOffset	= this.enharmonicPos[getCurKeyOffset()];

			// Setting the input file:
			int	enharmonicPos	= this.enharmonicPos[keyPos];
			String	filename	= this.filenames[this.majMinChrom][enharmonicPos];
			this.inputFile	= "Piano Scale Reference Inputs/" + filename;

			if(!(this.getLeftEdgeX() == 0)) {
				this.displaySidebar();
			}

			// Attempts to make the list show in front of rootColor button
			// (fruitless because rootColor has been brought to the front; I moved it over instead.
			//  - but this can help show how to access the different types of the items).
			/*
			ScrollableList	sl	= ((ScrollableList)controlEvent.getController());
			List<HashMap>	itemList	= sl.getItems();

			for(int i = 0; i < itemList.size(); i++)
			{
				System.out.println("itemList.get(i) = " + itemList.get(i).getClass());

				HashMap	curItem	= itemList.get(i);
				System.out.println("curItem.get('view') = " + curItem.get("view").getClass());
	/*			
				ScrollableList.ScrollableListView	viewSL	= (ScrollableList.ScrollableListView)curItem.get("view");
				System.out.println("viewSL = " + viewSL);

			} // for - i

			if(sl.isOpen())
			{
				this.sidebarCP5.setAutoDraw(false);
				controlEvent.getController().bringToFront();
				controlEvent.getController().draw(this.parent.g);
			} else {
				this.sidebarCP5.setAutoDraw(true);
			}
			 */

		} // keyDropdown

		// Major/Minor/Chromatic buttons
		if(controlEvent.getName().equals("major") ||
				controlEvent.getName().equals("minor") ||
				controlEvent.getName().equals("chrom"))
		{		
			Toggle	curToggle	= (Toggle) controlEvent.getController();
			System.out.println("maj/min/chrom buttons: this.curKey = " + this.curKey + "; this.arrayContains(this.allNotes, 'G') = " + this.arrayContains(this.allNotes, "G"));
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

			//this.updateColors(this.curColorStyle);
		
		} // majMinChrom buttons


		// Root color selector button:
		if(controlEvent.getName().equals("rootColorButton"))
		{
			Button	rootButton	= (Button)controlEvent.getController();
			// draw slightly transparent rectangle:
			if(rootButton.getBooleanValue())
			{
				// Want to turn off automatic drawing so that our transparent rectangle can go on top of the controllers.
				//				this.sidebarCP5.setAutoDraw(false);

				// Draw all the controllers:
				//				this.sidebarCP5.draw();

				// Then cover with a rectangle (black, w/alpha of 50):
				this.sidebarCP5.getGroup("background").setVisible(true);
				this.sidebarCP5.getGroup("background").bringToFront();

				this.sidebarCP5.getController("rootColorButton").bringToFront();
				this.sidebarCP5.getController("rootColorWheel").bringToFront();
				this.sidebarCP5.getController("rootColorTF").bringToFront();

				ColorWheel	rootCW	= (ColorWheel)this.sidebarCP5.getController("rootColorWheel");
				int	rgbColor	= rootCW.getRGB();
				Color	color	= new Color(rgbColor);

				Textfield	rootColorTF	= (Textfield)this.sidebarCP5.getController("rootColorTF");
				rootColorTF.setText("rgb(" + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue() + ")");

				this.rootColor[0]	= color.getRed();
				this.rootColor[1]	= color.getGreen();
				this.rootColor[2]	= color.getBlue();

				this.colors[0][0]	= color.getRed();
				this.colors[0][1]	= color.getGreen();
				this.colors[0][2]	= color.getBlue();

				//				this.updateColors(this.curColorStyle);
			} else {
				this.sidebarCP5.getGroup("background").setVisible(false);
				this.displaySidebar();
			}

			this.sidebarCP5.getController("rootColorWheel").setVisible(rootButton.getBooleanValue());
			this.updateColors(this.curColorStyle);
		} // Root color selector button (i.e., show color wheel)

		// Root color Textfield
		if(controlEvent.getName().equals("rootColorTF"))
		{
			String[]	tfValues	= controlEvent.getStringValue().split("[(,)]");
			for(int i = 0; i < tfValues.length; i++)
			{
				tfValues[i]	= tfValues[i].trim().toLowerCase();
				//System.out.println("tfValues[" + i + "] = " + tfValues[i]);
			} // for

			try
			{
				if(tfValues[0].equals("rgb"))
				{
					// Get color values:
					float	red		= Float.parseFloat(tfValues[1]);
					float	green	= Float.parseFloat(tfValues[2]);
					float	blue	= Float.parseFloat(tfValues[3]);

					// Constrain to 0-255:
					red		= Math.min(255, Math.max(0, red));
					green	= Math.min(255, Math.max(0, green));
					blue	= Math.min(255, Math.max(0, blue));

					// Set root color and update all colors:
					this.rootColor[0]	= (int) red;
					this.rootColor[1]	= (int) green;
					this.rootColor[2]	= (int) blue;

					this.updateColors(this.curColorStyle);

					// Set corresponding ColorWheel:
					Color	rgbColor	= new Color(this.rootColor[0], this.rootColor[1], this.rootColor[2]);
					int		rgbInt		= rgbColor.getRGB();
					((ColorWheel)this.sidebarCP5.getController("rootColorWheel")).setRGB(rgbInt);
				}

			} catch(Exception e) {
				System.out.println("Sorry, that is not recognized as a valid color. Please try again.");
			}
		} // root color Textfield

		// Root Color Wheel
		if(controlEvent.getName().equals("rootColorWheel"))
		{
			ColorWheel	rootCW	= (ColorWheel)controlEvent.getController();
			int	rgbColor	= rootCW.getRGB();
			Color	color	= new Color(rgbColor);

			Textfield	rootColorTF	= (Textfield)this.sidebarCP5.getController("rootColorTF");
			if(rootColorTF != null)
			{
				rootColorTF.setText("rgb(" + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue() + ")");
			}

			this.rootColor[0]	= color.getRed();
			this.rootColor[1]	= color.getGreen();
			this.rootColor[2]	= color.getBlue();

			this.colors[0][0]	= color.getRed();
			this.colors[0][1]	= color.getGreen();
			this.colors[0][2]	= color.getBlue();

			this.updateColors(this.curColorStyle);
		} // root color wheel


		// Color Selection: 
		// Buttons, ColorWheels and corresponding Textfields will have id's of 21 or over;
		// Button id % 3 == 0; ColorWheel id % 3 == 1, Textfield id % 3 == 2.

		// Custom pitch color selector buttons:
		if(controlEvent.getId() > 23 && (controlEvent.getId() % 3 == 0))
		{
			Button	curButton	= (Button)controlEvent.getController();

			// draw slightly transparent rectangle:
			if(curButton.getBooleanValue())
			{

				this.sidebarCP5.getGroup("background").setVisible(true);
				this.sidebarCP5.getGroup("background").bringToFront();

			} else {

				this.sidebarCP5.setAutoDraw(true);
				this.sidebarCP5.getGroup("background").setVisible(false);
				this.displaySidebar();
			}

			this.sidebarCP5.getController("button" + (controlEvent.getId())).bringToFront();
			this.sidebarCP5.getController("colorWheel" + (controlEvent.getId() + 1)).bringToFront();
			this.sidebarCP5.getController("textfield" + (controlEvent.getId() + 2)).bringToFront();

			this.sidebarCP5.getController("colorWheel" + (controlEvent.getId() + 1)).setVisible(curButton.getBooleanValue());
			//			this.updateColors(this.curColorStyle);
		} // custom pitch color selectors (i.e., show color wheel)

		// Custom pitch ColorWheels
		if(controlEvent.getId() > 23 && (controlEvent.getId() % 3 == 1))
		{
			if(this.curColorStyle == ModuleTemplate.CS_CUSTOM)
			{
				// get current color:
				ColorWheel	rootCW	= (ColorWheel)controlEvent.getController();
				int	rgbColor	= rootCW.getRGB();
				Color	color	= new Color(rgbColor);

				// Set corresponding Textfield with color value:
				Textfield	rootColorTF	= (Textfield)this.sidebarCP5.getController("textfield" + (controlEvent.getId() + 1));
				rootColorTF.setText("rgb(" + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue() + ")");

				id	= controlEvent.getId();
				int	notePos	= ((id + 2) / 3) - 9;
				//			notePos	= (notePos + keyAddVal - 3 + this.colors.length) % this.colors.length;

				this.colors[notePos][0]	= color.getRed();
				this.colors[notePos][1]	= color.getGreen();
				this.colors[notePos][2]	= color.getBlue();


				//System.out.println(controlEvent.getController() + ": notePos = " + notePos);
			}
		} // custom pitch color wheels

		// ColorWheel Textfields (id 23 or over; id % 3 == 2):
		if(controlEvent.getId() > 23 && (controlEvent.getId() % 3 == 2))
		{
			if(this.curColorStyle == ModuleTemplate.CS_CUSTOM)
			{
				id	= controlEvent.getId();
				int	notePos	= ((id + 2) / 3) - 9;

				//System.out.println(controlEvent.getController() + ": notePos = " + notePos);

				// error checking
				if(notePos < 0 || notePos > this.colors.length)	{
					throw new IllegalArgumentException("ModuleTemplate.controlEvent - custom color Textfields: " +
							"notePos " + notePos + " from id " + id + " is not a valid note position; " +
							"it should be between 0 and " + this.colors.length);
				} // error checking

				//System.out.println(controlEvent.getController() + ": notePos = " + notePos);

				// Getting color value from the Textfield:
				String[]	tfValues	= controlEvent.getStringValue().split("[(,)]");
				for(int i = 0; i < tfValues.length; i++)
				{
					tfValues[i]	= tfValues[i].trim().toLowerCase();
					//System.out.println("tfValues[" + i + "] = " + tfValues[i]);
				} // for

				try
				{
					if(tfValues[0].equals("rgb"))
					{
						// Get color values:
						float	red		= Float.parseFloat(tfValues[1]);
						float	green	= Float.parseFloat(tfValues[2]);
						float	blue	= Float.parseFloat(tfValues[3]);

						// Constrain to 0-255:
						red		= Math.min(255, Math.max(0, red));
						green	= Math.min(255, Math.max(0, green));
						blue	= Math.min(255, Math.max(0, blue));

						// Set corresponding ColorWheel:
						Color	rgbColor	= new Color(this.rootColor[0], this.rootColor[1], this.rootColor[2]);
						int		rgbInt		= rgbColor.getRGB();
						((ColorWheel)this.sidebarCP5.getController("colorWheel" + (id - 1))).setRGB(rgbInt);

						this.colors[notePos][0]	= red;
						this.colors[notePos][1]	= green;
						this.colors[notePos][2]	= blue;
					} // if - rgb

				} catch(Exception e) {
					System.out.println("Sorry, that is not recognized as a valid color. Please try again.");
				} // catch
			} // if - CS_Custom

		} // ColorWheel Textfields


		// Color Style:
		if(controlEvent.getName().equals("rainbow") ||
				controlEvent.getName().equals("dichrom") ||
				controlEvent.getName().equals("trichrom") ||
				controlEvent.getName().equals("custom"))
		{
			Toggle	curToggle	= (Toggle) controlEvent.getController();

			// TODO: might need to only call when curToggle is Custom
			if(this.originalColors == null) {
				this.originalColors = new float[this.colors.length][3];
			}
			for(int i = 0; i < this.originalColors.length; i++)
			{
				for(int j = 0; j < this.originalColors[i].length; j++)
				{
					this.originalColors[i][j]	= this.colors[i][j];
				}
			}

			// Set root color/call correct function for the new colorStyle:
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
					toggleArray[i].setState(false);
				} else {
					toggleArray[i].setState(true);
				}

				// set broadcasting back to original setting:
				toggleArray[i].setBroadcast(broadcastState[i]);
			} // for - switch off all Toggles:

		} // colorStyle buttons

		// ModulateSliders:

	} // controlEvent

	public int getLeftEdgeX() {
		return leftEdgeX;
	}

	public void setLeftEdgeX(int leftEdgeX) {
		this.leftEdgeX = leftEdgeX;
	}

	public boolean isShowScale() {
		return showScale;
	}

	public void setShowScale(boolean showScale) {
		this.showScale = showScale;
	}

	public float getThresholdLevel() {
		return thresholdLevel;
	}

	public void setThresholdLevel(float thresholdLevel) {
		this.thresholdLevel = thresholdLevel;
	}


	public float getAttackReleaseTransition(int arORt)
	{
		if(arORt < 0 || arORt > this.attackReleaseTransition.length)
		{
			throw new IllegalArgumentException("ModuleTemplate.getAttackReleaseTransition: parameter " + arORt + " is not a valid position in the array this.attackReleaseTransition.");
		} // error checking

		return	this.attackReleaseTransition[arORt];
	} // getAttackReleaseTransition

	public float getRedModulate() {
		return this.redGreenBlueMod[0];
	}

	public float getGreenModulate() {
		return this.redGreenBlueMod[1];
	}

	public float getBlueModulate() {
		return this.redGreenBlueMod[2];
	}

	public int getMajMinChrom() {
		return majMinChrom;
	}

	public int[][] getScaleDegrees() {
		return scaleDegrees;
	}

	public int getCurKeyEnharmonicOffset() {
		return curKeyEnharmonicOffset;
	}

	public int getCurKeyOffset() {
		return curKeyOffset;
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
	


}
