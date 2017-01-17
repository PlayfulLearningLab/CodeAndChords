package core;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.Map;

import controlP5.Button;
import controlP5.ControlEvent;
import controlP5.ControlP5;
import controlP5.ControlP5Constants;
import controlP5.Controller;
import controlP5.ScrollableList;
import controlP5.Slider;
import controlP5.Textfield;
import controlP5.Textlabel;
import controlP5.Toggle;
import processing.core.PApplet;
import processing.core.PImage;

/**
 * Future:
 *  - look into putting things into Groups -- either all in one, or each section its own group?
 *  - ControlListener that takes everything and doesn't require Modules to have controlEvent()?
 *    (see A.S. answer: https://forum.processing.org/two/discussion/2692/controlp5-problems-creating-a-toggle-controller-with-custom-images-on-a-second-tab)
 * - Custom controllers: http://www.sojamo.de/libraries/controlP5/reference/controlP5/ControllerView.html
 * 
 * Emily Meuer
 * 01/11/2017
 * 
 * Putting all the pop-out sidebar/moduleTemplate stuff from Module_01_02 in this class.
 * 
 * @author Emily Meuer
 *
 */
public class ModuleTemplate {

	// Static var's: colorStyles
	private	static	char	CS_RAINBOW	= 1;
	private	static	char	CS_DICHROM	= 2;
	private	static	char	CS_TRICHROM	= 3;
	private	static	char	CS_CUSTOM	= 4;
	private	char	curColorStyle;

	// For rounding numbers in sliders to two digits:
	private	DecimalFormat	decimalFormat	= new DecimalFormat("#.##");

	// Choose input file here:
	// Raw:
	//String  inputFile  = "src/module_01_PitchHueBackground/module_01_02_PitchHueBackground_ModuleTemplate_EMM/Emily_CMajor-2016_09_2-16bit-44.1K Raw.wav";
	// Tuned:
	String  inputFile  = "src/module_01_PitchHueBackground/module_01_02_PitchHueBackground_ModuleTemplate_EMM/Emily_CMajor-2016_09_2-16bit-44.1K Tuned.wav";
	// Kanye:
	//String  inputFile  = "src/module_01_PitchHueBackground/module_01_02_PitchHueBackground_ModuleTemplate_EMM/Emily_CMajor-2016_09_2-16bit-44.1K Kanye.wav";

	// Global vars - TODO: all private!
	private	PApplet		parent;
	private ControlP5 	nonSidebarCP5;
	private ControlP5 	sidebarCP5;
	private	Input		input;

	private Toggle		play;
	private	Button		hamburger;
	private	Button		menuX;

	private	Textlabel	hideLabel;
	private	Toggle		hidePlayButton;
	private	Toggle		hideMenuButton;
	private	Toggle		hideScale;

	// These are prob. extraneous, since I can get them from the ControlP5 by knowing their label...
	private	Textlabel	thresholdLabel;
	private	Slider		threshold;
	private	Textfield	thresholdTF;
	private	Textlabel	attackLabel;
	private	Slider		attack;
	private	Textfield	attackTF;
	private	Textlabel	releaseLabel;
	private	Slider		release;
	private	Textfield	releaseTF;
	private	Textlabel	transitionLabel;
	private	Slider		transition;
	private	Textfield	transitionTF;

	private	int			leftAlign;
	private	int			leftEdgeX;
	private	int[]		leftEdgeXArray;

	private	String		sidebarTitle;

	private	int			scaleLength;
	private	int			majMinChrom;
	private	String		curKey;
	private int 		keyAddVal;		// amount that must be subtracted in legend() 
	// to line pitches up with the correct scale degree of the current key.

	private	final String[]	notesCtoBFlats	= new String[] { 
			"C", "Db", "D", "Eb", "E", "F", "Gb",  "G", "Ab", "A", "Bb", "B"
	};

	private final String[]	notesCtoBSharps	= new String[] { 
			"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"
	};

	private	final	int[][] scaleDegrees = new int[][] {
		// major:
		new int[]  { 0, 2, 4, 5, 7, 9, 11
		},
		// minor:
		new int[]  { 0, 2, 3, 5, 7, 8, 10
		}
	}; // scaleDegrees

	private	float[][]	colors;
	private int[] 		rootColor;

	int[]				textYVals;
	int[]				noteYVals;
	int[]				modulateYVals;


	public ModuleTemplate(PApplet parent, Input input, String sidebarTitle)
	{
		this.parent	= parent;
		this.input	= input;
		this.nonSidebarCP5	= new ControlP5(this.parent);
		this.sidebarCP5		= new ControlP5(this.parent);
		this.sidebarCP5.setVisible(false);
		this.sidebarTitle	= sidebarTitle;

		//		this.leftEdgeXArray	= new int[] { 0, this.parent.width / 3 };
		this.setLeftEdgeX(0);

		this.setColors(new float[12][3]);

		this.curColorStyle	= this.CS_RAINBOW;
		this.rootColor	= new int[] { 255, 0, 0, };

		this.setCurKey("A", 2);
		this.rainbow();

		this.textYVals		= new int[9];
		this.noteYVals		= new int[6];
		this.modulateYVals	= new int[3];

		//this.initModuleTemplate();
	} // ModuleTemplate

	// Methods:
	//TODO: make initModuleTemplate() private again, once it can be called from constructor.
	
	/**
	 * Called from constructor to calculate Y vals and call the methods for instantiating the necessary buttons;
	 * will eventually call different button methods depending on the module number.
	 */
	public void initModuleTemplate()
	{
		// Add play button, hamburger and menu x:
		addOutsideButtons();

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
		System.out.println("yValDif = " + yValDif);
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

		// leftAlign will be set in displaySidebar in relation to leftEdgeX, 
		// but the button functions need to use it earlier:
		this.leftAlign	= (this.parent.width / 3) / 4;

		// call add methods:
		addHideButtons(textYVals[0]);

		// TODO: pass it one Y and either a height and spacer or a distance between y's.
		addSliders(textYVals[1], textYVals[2], textYVals[3], textYVals[4]);
		
		addKeySelector(textYVals[5]);
		
		addModulateSliders(modulateYVals);
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
		this.play	= this.nonSidebarCP5.addToggle("play")
				.setPosition(playX, playY)
				.setImages(images)
				.updateSize();

		int	hamburgerX		= 10;
		int	hamburgerY		= 13;
		int	hamburgerWidth	= 30;
		int	hamburgerHeight	= 30;

		PImage	hamburger	= this.parent.loadImage("hamburger.png");
		hamburger.resize(hamburgerWidth, hamburgerHeight);
		this.hamburger	= this.nonSidebarCP5.addButton("hamburger")
				.setPosition(hamburgerX, hamburgerY)
				.setImage(hamburger)
				.updateSize();

		int	menuXX			= 5;
		int	menuXY			= 5;
		int	menuXWidth		= 15;
		int	menuXHeight		= 15;

		PImage	menuX	= this.parent.loadImage("menuX.png");
		menuX.resize(menuXWidth, 0);
		this.menuX	= this.sidebarCP5.addButton("menuX")
				.setPosition(menuXX, menuXY)
				.setImage(menuX)
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
						.setValue("Hide");

		this.hidePlayButton	= this.sidebarCP5.addToggle("playButton")
				.setPosition(playButtonX, hideY)
				.setWidth(hideWidth)
				.setId(4);
		this.hidePlayButton.getCaptionLabel().set("Play Button").align(ControlP5.CENTER, ControlP5.CENTER);


		this.hideMenuButton	= this.sidebarCP5.addToggle("menuButton")
				.setPosition(menuButtonX, hideY)
				.setWidth(hideWidth)
				.setId(5);
		this.hideMenuButton.getCaptionLabel().set("Menu Button").align(ControlP5.CENTER, ControlP5.CENTER);


		this.hideScale	= this.sidebarCP5.addToggle("scale")
				.setPosition(scaleX, hideY)
				.setWidth(hideWidth)
				.setId(6);
		this.hideScale.getCaptionLabel().set("Scale").align(ControlP5.CENTER, ControlP5.CENTER);

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

		this.thresholdLabel	= this.sidebarCP5.addLabel("thresholdLabel")
				.setPosition(labelX, thresholdY + 4)
				.setWidth(labelWidth)
				.setValue("Threshold");
		System.out.println("sliderWidth = " + sliderWidth + "; sliderHeight = " + sliderHeight);
		
		// Threshold slider:
		this.threshold	= this.sidebarCP5.addSlider("slider0")
				.setPosition(this.leftAlign, thresholdY)
				.setSize(sliderWidth, sliderHeight)
				.setSliderMode(Slider.FLEXIBLE)
				.setValue(10)
				.setLabelVisible(false)
				.setId(0);		

		// Threshold textfield:
		this.thresholdTF	= this.sidebarCP5.addTextfield("textfield1")
				.setPosition(this.leftAlign + sliderWidth + spacer, thresholdY)
				.setSize(tfWidth, sliderHeight)
				.setLabelVisible(false)
				.setText(this.threshold.getValue() + "")
				.setLabelVisible(false)
				.setAutoClear(false)
				.setId(1);

		// Test: not adding them as variables, seeing how that goes. :)

		// Attack group:
		//	- Textlabel:
		this.sidebarCP5.addLabel("attackLabel")
		.setPosition(labelX, attackY + 4)
		.setWidth(labelWidth)
		.setValue("Attack");

		//	- Slider:
		this.sidebarCP5.addSlider("slider2")
		.setPosition(this.leftAlign, attackY)
		.setSize(sliderWidth, sliderHeight)
		.setSliderMode(Slider.FLEXIBLE)
		.setValue(10)
		.setLabelVisible(false)
		.setId(2);
		
		//	- Textfield:
		this.sidebarCP5.addTextfield("textfield3")
		.setPosition(this.leftAlign + sliderWidth + spacer, attackY)
		.setSize(tfWidth, sliderHeight)
		.setText(this.sidebarCP5.getController("slider2").getValue() + "")
		.setLabelVisible(false)
		.setAutoClear(false)
		.setId(3);

		// Release:
		// - Textlabel:
		this.sidebarCP5.addLabel("releaseLabel")
		.setPosition(labelX, releaseY + 4)
		.setWidth(labelWidth)
		.setValue("Release");

		//	- Slider:
		this.sidebarCP5.addSlider("slider4")
		.setPosition(this.leftAlign, releaseY)
		.setSize(sliderWidth, sliderHeight)
		.setSliderMode(Slider.FLEXIBLE)
		.setValue(10)
		.setLabelVisible(false)
		.setId(4);
		
		//	- Textlabel:
		this.sidebarCP5.addTextfield("textfield5")
		.setPosition(this.leftAlign + sliderWidth + spacer, releaseY)
		.setSize(tfWidth, sliderHeight)
		.setText(this.sidebarCP5.getController("slider4").getValue() + "")
		.setLabelVisible(false)
		.setAutoClear(false)
		.setId(5);
		
		// Transition:
		// - Textlabel:
		this.sidebarCP5.addLabel("transitionLabel")
				.setPosition(labelX, transitionY + 4)
				.setWidth(labelWidth)
				.setValue("Transition");

		//	- Slider:
		this.sidebarCP5.addSlider("slider6")
				.setPosition(this.leftAlign, transitionY)
				.setSize(sliderWidth, sliderHeight)
				.setSliderMode(Slider.FLEXIBLE)
				.setValue(10)
				.setLabelVisible(false)
				.setId(6);
				
		//	- Textlabel:
		this.sidebarCP5.addTextfield("textfield7")
				.setPosition(this.leftAlign + sliderWidth + spacer, transitionY)
				.setSize(tfWidth, sliderHeight)
				.setText(this.sidebarCP5.getController("slider6").getValue() + "")
				.setLabelVisible(false)
				.setAutoClear(false)
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
		int	labelWidth		= 70;
		
		int	listWidth		= 65;
		int	spacer			= 5;

		int	toggleWidth		= 45;
		int	majorX			= this.leftAlign + listWidth + spacer;
		int	minorX			= this.leftAlign + listWidth + spacer + (toggleWidth + spacer);
		int	chromX			= this.leftAlign + listWidth + spacer + ((toggleWidth + spacer) * 2);
		
		String[] keyOptions	= new String[] {
				"A", "Bb", "B", "C", "Db", "D", "Eb", "E", "F", "F#", "Gb", "G", "Ab"
		};
		
		// "Key" Textlabel
		this.sidebarCP5.addTextlabel("key")
						.setPosition(labelX, keyY + 4)
						.setValue("Key");
		
		// "Letter" drop-down menu (better name?)
		this.sidebarCP5.addScrollableList("keyDropdown")
						.setPosition(this.leftAlign, keyY)
						.setWidth(listWidth)
						.setItems(keyOptions)
						.setOpen(false)
						.setLabel("Select a key:")
						.getCaptionLabel().toUpperCase(false);
		
		//	This is odd, but a way to get an item if we need it (see Processing examples for it in action):
		//this.sidebarCP5.get(ScrollableList.class, "keyDropdown").getItem("A");
		
		// Maj/Min/Chrom buttons
		// (These are buttons, rather than toggles, because they have a value -
		//  0 = Major, 1 = Minor, and 2 = Chromatic - and will set this.majMinChrom,
		//  rather than simply being on or off.)
		// Ids = 14-16 (start after modulateSliders)
		this.sidebarCP5.addToggle("major")
						.setPosition(majorX, keyY)
						.setWidth(toggleWidth)
						.setCaptionLabel("Major")
						.setInternalValue(0);
		this.sidebarCP5.getController("major").getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);
		
		this.sidebarCP5.addToggle("minor")
		.setPosition(minorX, keyY)
		.setWidth(toggleWidth)
		.setCaptionLabel("Minor")
		.setInternalValue(1);
		this.sidebarCP5.getController("minor").getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);
		
		
		this.sidebarCP5.addToggle("chrom")
		.setPosition(chromX, keyY)
		.setWidth(toggleWidth)
		.setCaptionLabel("Chromatic")
		.setInternalValue(2);
		this.sidebarCP5.getController("chrom").getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);
		
		((Toggle)(this.sidebarCP5.getController("chrom"))).setState(true);
	} // addKeySelector
	
	/**
	 * Method called during instantiation, to initialize the color modulate slilders.
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
		String[]	values	= new String[] { "Red Modulate", "Green mod.", "Blue modulate" };
		
		int	id	= 8;		// this id picks up where the transition textfield - "textfield7" - left off.

		for(int i = 0; i < modulateYVals.length; i++)
		{
			// - Textlabel:
			this.sidebarCP5.addLabel(names[i])
					.setPosition(labelX, modulateYVals[i] + 4)
					.setWidth(labelWidth)
					.setValue(values[i]);

			//	- Slider:
			this.sidebarCP5.addSlider("slider" + id)
					.setPosition(this.leftAlign, modulateYVals[i])
					.setSize(sliderWidth, sliderHeight)
					.setSliderMode(Slider.FLEXIBLE)
					.setValue(10)
					.setLabelVisible(false)
					.setId(id);
			
			id	= id + 1;
			
			//	- Textlabel:
			this.sidebarCP5.addTextfield("textfield" + id)
					.setPosition(this.leftAlign + sliderWidth + spacer, modulateYVals[i])
					.setSize(tfWidth, sliderHeight)
					.setText(this.sidebarCP5.getController("slider" + (id-1)).getValue() + "")
					.setLabelVisible(false)
					.setAutoClear(false)
					.setId(id);
			
			id	= id + 1;
		} // for
	} // addModulateSliders

	private void addColorStyleButtons(int colorStyleY)
	{
		int	colorStyleWidth	= 52;
		int	colorStyleSpace	= 4;

		int rainbowX     	= this.leftAlign;
		int dichromaticX	= this.leftAlign + colorStyleWidth + colorStyleSpace;
		int trichromaticX	= this.leftAlign + (colorStyleWidth + colorStyleSpace) * 2;
		int customX			= this.leftAlign + (colorStyleWidth + colorStyleSpace) * 3;

	} // addColorStyleButtons

	public void update()
	{
		this.thresholdTF.setValue(this.threshold.getValue());
		this.thresholdTF.setText(this.threshold.getValue() + "");
		System.out.println("this.threshold.getValue() = " + this.threshold.getValue() + 
				"this.threshold.getValuePosition = " + this.threshold.getValuePosition());
	} // update

	// addSliders
	/*
	 *  - set sliderX
	 *  - set sliderWidth
	 *  - set textFieldX (derive from the preceding vars? ;D need int spacer)
	 *  - calculate y's from the one given y?
	 *  - displayText
	 *  - addSliders
	 *  - addTextFields
	 */

	// addKeySelector

	// addRootColor

	// addColorStyle

	// addCustomPitchColor

	// addModulate

	// addTextFields

	public void legend(int goalHuePos)
	{

		this.parent.textSize(24);
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
		String[]	notes	= this.getScale(this.curKey, this.majMinChrom);
		// 12/19: updating to be on the side.
		// 01/05: changing it back!
		float  sideWidth   = (this.parent.width - leftEdgeX) / notes.length;
		float  sideHeight  = this.parent.width / notes.length;
		//  float  side = height / colors.length;

		//	stroke(255);
		this.parent.noStroke();

		for (int i = 0; i < notes.length; i++)
		{
			this.parent.fill(this.getColors()[i][0], this.getColors()[i][1], this.getColors()[i][2]);
			/*
			if(i == 0)
			{
				for(int j = 0; j < this.colors[i].length; j++)
				{
					println("legend: colors[0][" + j + "] = " + colors[0][j]);
				}
			}
			 */
			if (i == goalHuePos) {
				this.parent.rect(leftEdgeX + (sideWidth * i), (float)(this.parent.height - (sideHeight * 1.5)), sideWidth, (float) (sideHeight * 1.5));
				//      rect(0, (side * i), side * 1.5, side);
			} else {
				this.parent.rect(leftEdgeX + (sideWidth * i), this.parent.height - sideHeight, sideWidth, sideHeight);
				//      rect(0, (side * i), side, side);
			}
			this.parent.fill(0);
			this.parent.text(notes[i], (float) (leftEdgeX + (sideWidth * i) + (sideWidth * 0.35)), this.parent.height - 20);
		} // for
	} // legend

	void displaySidebar()
	{
		this.sidebarCP5.setVisible(true);
		this.setLeftEdgeX(this.parent.width / 3);
		this.leftAlign	= (this.getLeftEdgeX() / 4);

		this.parent.stroke(255);
		this.parent.fill(0);
		this.parent.rect(0, 0, getLeftEdgeX(), this.parent.height);

		int textX  		= 5;
		int	noteNameX1	= 40;
		int	noteNameX2 	= noteNameX1 + 135;

		String[]	textArray	= new String[] {
				"",
				"",
				"",
				"",
				"",
				"",
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
				"", "", ""
		}; // modulateText


		this.parent.fill(255);
		this.parent.textSize(12);
		this.parent.text(this.sidebarTitle, this.leftAlign, 17);

		this.parent.textSize(10);
		this.parent.text("Menu", this.menuX.getPosition()[0] + this.menuX.getWidth() + 3, 16);

		for(int i = 0; i < textArray.length; i++)
		{
			this.parent.text(textArray[i], textX, textYVals[i]);
		}

		for(int i = 0; i < noteNames1.length; i++)
		{
			this.parent.text(noteNames1[i], noteNameX1, noteYVals[i]);
		}
		for(int i = 0; i < noteNames2.length; i++)
		{
			this.parent.text(noteNames2[i], noteNameX2, noteYVals[i]);
		}

		for(int i = 0; i < modulateText.length; i++)
		{
			this.parent.text(modulateText[i], textX, modulateYVals[i]);
		}
		/*
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
		 */
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

		this.scaleLength	= result.length;
		this.majMinChrom	= majMinChrom;

		return result;
	} // getScale

	public void setCurKey(String key, int majMinChrom)
	{
		// Check both sharps and flats, and take whichever one doesn't return -1:
		int	modPosition	= Math.max(this.arrayContains(this.notesCtoBFlats, key), this.arrayContains(this.notesCtoBSharps, key));

		if(modPosition == -1)	{
			throw new IllegalArgumentException("Module_01_02.setCurKey: " + key + " is not a valid key.");
		}

		this.majMinChrom	= majMinChrom;
		this.curKey			= key;
		this.keyAddVal		= modPosition;
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

		for(int i = 0; i < rgbVals1.length; i++)
		{
			this.getColors()[0][i]	= rgbVals1[i];
		}
		for(int i = 1; i < this.scaleLength && i < this.getColors().length; i++)
		{
			for(int j = 0; j < this.getColors()[i].length; j++)
			{
				this.getColors()[i][0]	= this.getColors()[i - 1][0] - redDelta;
				this.getColors()[i][1]	= this.getColors()[i - 1][1] - greenDelta;
				this.getColors()[i][2]	= this.getColors()[i - 1][2] - blueDelta;
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

		if(this.majMinChrom == 2)
		{
			// if chromatic scale, put the colors equally throughout:
			color2pos	= this.scaleLength / 3;
			color3pos	= (this.scaleLength / 3) * 2;
		} else {
			color2pos	= 3;	// subdominant
			color3pos	= 4;	// dominant
		}

		// TODO: this might need to be divided by 4 to make it to the actual color (or dichr. should be colors.length - 1?):
		float	redDelta1	= (rgbVals1[0] - rgbVals2[0]) / (color2pos - color1pos);
		float	greenDelta1	= (rgbVals1[1] - rgbVals2[1]) / (color2pos - color1pos);
		float	blueDelta1	= (rgbVals1[2] - rgbVals2[2]) / (color2pos - color1pos);

		float	redDelta2	= (rgbVals2[0] - rgbVals3[0]) / (color3pos - color2pos);
		float	greenDelta2	= (rgbVals2[1] - rgbVals3[1]) / (color3pos - color2pos);
		float	blueDelta2	= (rgbVals2[2] - rgbVals3[2]) / (color3pos - color2pos);

		float	redDelta3	= (rgbVals3[0] - rgbVals1[0]) / (this.scaleLength - color3pos);
		float	greenDelta3	= (rgbVals3[1] - rgbVals1[1]) / (this.scaleLength - color3pos);
		float	blueDelta3	= (rgbVals3[2] - rgbVals1[2]) / (this.scaleLength - color3pos);

		// fill first position with first color:
		for(int i = 0; i < rgbVals1.length; i++)
		{
			this.getColors()[0][i]	= rgbVals1[i];
		}

		// fill from first color to second color:
		for(int i = 1; i < color2pos + 1; i++)
		{
			for(int j = 0; j < this.getColors()[i].length; j++)
			{
				this.getColors()[i][0]	= this.getColors()[i - 1][0] - redDelta1;
				this.getColors()[i][1]	= this.getColors()[i - 1][1] - greenDelta1;
				this.getColors()[i][2]	= this.getColors()[i - 1][2] - blueDelta1;
			} // for - j
		} // for - first color to second color


		// fill from second color to third color:
		for(int i = color2pos + 1; i < color3pos + 1; i++)
		{
			for(int j = 0; j < this.getColors()[i].length; j++)
			{
				this.getColors()[i][0]	= this.getColors()[i - 1][0] - redDelta2;
				this.getColors()[i][1]	= this.getColors()[i - 1][1] - greenDelta2;
				this.getColors()[i][2]	= this.getColors()[i - 1][2] - blueDelta2;
			} // for - j
		} // for - first color to second color

		// fill from third color back to first color:
		for(int i = color3pos + 1; i < this.scaleLength; i++)
		{
			for(int j = 0; j < this.getColors()[i].length; j++)
			{
				this.getColors()[i][0]	= this.getColors()[i - 1][0] - redDelta3;
				this.getColors()[i][1]	= this.getColors()[i - 1][1] - greenDelta3;
				this.getColors()[i][2]	= this.getColors()[i - 1][2] - blueDelta3;
			} // for - j
		} // for - third color to first color
	} //trichromatic_ThreeRGB

	/**
	 * Populates colors with rainbow colors (ROYGBIV - with a few more for chromatic scales).
	 */
	public void rainbow()
	{
		float[][][] rainbowColors = rainbowColors	= new float[][][] { 
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

		for(int i = 0; i < this.getColors().length && i < rainbowColors[this.majMinChrom].length; i++)
		{
			for(int j = 0; j < this.getColors()[i].length && j < rainbowColors[this.majMinChrom][i].length; j++)
			{
				this.getColors()[i][j]	= rainbowColors[this.majMinChrom][i][j];
			} // for - j (going through rgb values)
		} // for - i (going through colors)

	} // rainbow

	/**
	 * This method handles the functionality of all the buttons, sliders, and textFields;
	 * Notate bene: any classes that include a moduleTemplate *must* include a controlEvent(ControlEvent) method that calls this method.
	 * 
	 * TODO: there's a NullPointer causing InvocationTargetException on startup,
	 * and it seems to be related to this method.
	 * 
	 * @param theControlEvent	ControlEvent used to determine which controller needs to act.
	 */
	public void controlEvent(ControlEvent controlEvent)
	{
		System.out.println("ModuleTemplate: theControlEvent.getController() = " + controlEvent.getController());

		int	id	= controlEvent.getController().getId();
		// Play button:
		if(controlEvent.getController().getName().equals("play"))
		{
			for (int i = 0; i < input.getuGenArray().length; i++)
			{
				input.getuGenArray()[i].pause(true);
			} // for

			if(this.play.getBooleanValue())
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
			this.hamburger.setVisible(false);
		} // if - hamburger

		// MenuX button:
		if(controlEvent.getController().getName().equals("menuX"))
		{
			this.setLeftEdgeX(0);
			this.sidebarCP5.setVisible(false);
			this.hamburger.setVisible(!this.sidebarCP5.getController("menuButton").isActive());
		} // if - menuX

		// Hide play button button:
		if(controlEvent.getController().getId() == this.hidePlayButton.getId())
		{
			this.play.setVisible(!this.play.isVisible());
		} // if - hidePlayButton

		// TODO: I need to still use the hamburger, whether or not it is visible....
		// Hide menu button button:
		if(controlEvent.getController().getId() == this.hideMenuButton.getId())
		{
			this.hamburger.setVisible(!this.hamburger.isVisible());
		} // if - hidePlayButton

		// Hide "scale" will be referred to in draw()
		
		//TODO: set this cutoff in a more relevant place - perhaps when sliders are created?
		// (If I have a numSliders, it would be (numSliders * 2).
		int	sliderCutoff	= 14;

		// Sliders (sliders have odd id num and corresponding textfields have the next odd number)
		if(id % 2 == 0 && id < sliderCutoff)
		{
			Slider	curSlider	= (Slider)this.sidebarCP5.getController("slider" + id);
			Textfield	curTextfield	= (Textfield)this.sidebarCP5.getController("textfield" + (id + 1));
			
			curTextfield.setText(this.decimalFormat.format(curSlider.getValue()) + "");
		}
		
		// Textfields
		if(id % 2 == 1 && id < sliderCutoff && id > 0)
		{
			Textfield	curTextfield	= (Textfield)this.sidebarCP5.getController("textfield" + id);
			Slider		curSlider		= (Slider)this.sidebarCP5.getController("slider" + (id - 1));
			
			try	{
				curSlider.setValue(Float.parseFloat(curTextfield.getStringValue()));
			} catch(NumberFormatException nfe) {
				System.out.println("ModuleTemplate.controlEvent: string value " + curTextfield.getStringValue() + 
						"for controller " + curTextfield + " cannot be parsed to a float.  Please enter a number.");
			} // catch
		} // textField
		
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
			
			this.setCurKey(key, this.majMinChrom);
			this.displaySidebar();
		} // keyDropdown
		
		// Major/Minor/Chromatic buttons
		if(controlEvent.getName().equals("major") ||
				controlEvent.getName().equals("minor") ||
				controlEvent.getName().equals("chrom"))
		{
			Toggle	curToggle	= (Toggle) controlEvent.getController();
			System.out.println("Maj/Min/Chrom: internalValue() = " + curToggle.internalValue());
			this.setCurKey(this.curKey, (int) curToggle.internalValue());
			
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
		
		} // majMinChrom buttons
	} // controlEvent

	public int getLeftEdgeX() {
		return leftEdgeX;
	}

	public void setLeftEdgeX(int leftEdgeX) {
		this.leftEdgeX = leftEdgeX;
	}

	public float[][] getColors() {
		return colors;
	}

	public void setColors(float[][] colors) {
		this.colors = colors;
	}


	/*
	 * 01/11/2017 brainstorming:
	 * I'll have options for 
	 * 
	 * Do I want generic sliders?  I won't have a great way of accessing their results,
	 * but I'll also have more freedom in making ones.
	 * Otherwise, I can say the only ones you can use are the ones that I made.
	 * (Which makes sense, because then I don't need to set new range values each time,
	 * and maybe it can even  interact with Input -- do something when it crosses the threshold, etc.)
	 * 
	 * I don't want displaySidebar() to be a whole bunch of if's, though;
	 * Maybe: I can have an ArrayList of functions that are implemented for this particular instance,
	 * and I go through that array list and call the corresponding functions.
	 * Then I'll have another huge if() function that takes a number parameter and does the thing it's supposed to do.
	 *  ^ Not too bad, b/c it only counts toward text.  Other things are implementable once.
	 *  
	 *  Slight problem = how controlP5 deals w/events. I need a separate function for each button I might have,
	 *  but what if I make two of those buttons? (That doesn't make sense... Why would you have two hidePlay buttons?)
	 *  
	 *  Main problem: I really don't understand ControlP5 yet.
	 *  Solution:	I'll implement Module_01_02 w/ControlP5, not generically at all.
	 *  			Then, when I can see how it works, maybe I can make it generic.
	 *  			(Kind of what I was going to do, anyway; we'll take this one step at a time.)
	 */

}
