package core.Archive_ModuleTemplate;

import java.awt.Color;

import controlP5.ControlEvent;
import controlP5.ControlFont;
import controlP5.ControlP5;
import controlP5.Toggle;
import core.Instrument;
import core.Melody;
import core.input.RealTimeInput;
import processing.core.PApplet;
import processing.core.PImage;

public abstract class ModuleTemplate {

	protected	PApplet	parent;
	
	protected	ControlP5	sidebarCP5;
	private		String		sidebarTitle;
	
	protected	int			leftAlign;
	protected	int			leftEdgeX;

	/**
	 * float[][] representing the colors that will be used by this sketch;
	 * must be instantiated by child class.
	 */
	private	DecimalFormat	decimalFormat	= new DecimalFormat("#.##");

	String  inputFile;

	private	PApplet		parent;
	//	public ControlP5 	nonSidebarCP5;
	// TODO set private post-testing:
	private ControlP5 	sidebarCP5;
	private	RealTimeInput		input;

	private	int			modTempNum;	// Number to distinguish different instances of the class in the same sketch

	private	int			leftAlign;
	private	int			leftEdgeX;
	private	int			originalLeftEdgeX;
	private	int			rectWidth;			// used for version with multiple ModuleTemplates within them
	private	int			rectHeight;
	private	int			topYVal;

	private	String		sidebarTitle;

	private	int			majMinChrom;
	private	String		curKey;
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

	private	float[][]	colors;
	private	float[]		curHue;
	private	float[]		goalHue;
	private	float[]		canvasColor;
	private	float[]		colorAdd;
	private	float[]		colorRange;

	private boolean[]	colorReachedArray;
	private	boolean		colorReached;

	private	RealTimeInput	input;
	private float	threshold;
	private boolean	nowBelow;
	
	private int		attRelTranPos;		// 0 = attack, 1 = release, 2 = transition
	private int 	checkpoint;		// For a timer that allows attack/release/transition sliders to be time-based.
	private	float[]	attRelTranVals;
	private float[] hueSatBrightnessMod; // This will store the hsb modulate values

	protected Melody		melody;
	protected String		curKey;
	protected	int			bpm;
	protected int 		majMinChrom;
	protected int			rangeOctave;
	protected Instrument	instrument;

	protected boolean	showScale;
	
	protected int		lastSetSliderId;
	protected int		lastSetTextfieldId;
	protected int		lastSetColorSelectButtonId;
	protected int		lastSetColorWheelId;
	protected int		lastSetColorSelectTextfieldId;

	
	public ModuleTemplate(PApplet parent, RealTimeInput input, String sidebarTitle)
	{
		this(parent, input, sidebarTitle, 0, 0, parent.width, parent.height, 0);
	} // Constructor - PApplet, Input, String
	
	public ModuleTemplate(PApplet parent, RealTimeInput input, String sidebarTitle, int leftEdgeX, int topYVal, int rectWidth, int rectHeight, int modTempNum)
	{
		System.out.println("topYVal = " + topYVal);
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
		this.originalLeftEdgeX	= leftEdgeX;
		this.leftEdgeX			= leftEdgeX;
		this.leftAlign	= (this.parent.width / 3) / 4;	// this value will be considered in relation to the position of sidebarGroup
		this.rectWidth	= rectWidth;
		this.rectHeight	= rectHeight;
		this.topYVal	= topYVal;
		
		this.modTempNum	= modTempNum;
		this.sidebarTitle	= sidebarTitle;
		
		this.leftAlign	= (this.parent.width / 3) / 4;
		this.leftEdgeX	= 0;

		this.curHue				= new float[3];
		this.goalHue			= new float[3];
		this.canvasColor		= new float[] { 0, 0, 0 };
		this.colorAdd			= new float[3];
		this.colorRange			= new float[3];
		
		this.colorReachedArray	= new boolean[] { false, false, false };
		this.colorReached		= false;
		this.nowBelow			= false;
		
		this.attRelTranPos	= 0;	// 0 = attack, 1 = release, 2 = transition
		this.attRelTranVals	= new float[] {		200, 200, 200	};	// attack, release, transition all begin at 200 millis
		this.hueSatBrightnessMod        = new float[3];
		
		this.checkpoint		= this.parent.millis() + 100;

		this.threshold		= 10;
		
		this.sidebarCP5		= new ControlP5(this.parent);
		

		this.melody			= new Melody(this.parent, this.input);
		this.instrument		= new Instrument(this.parent);
		this.curKey			= "A";
		this.bpm			= 120;
		this.majMinChrom	= 2;	// chromatic
		this.rangeOctave	= 3;
		
		this.initModuleTemplate();
	} // constructor

	private void initModuleTemplate()	
	{
		this.sidebarCP5.addGroup("allControls")
		.setValue(this.modTempNum);
		
		this.sidebarCP5.addGroup("sidebarGroup")
		.setBackgroundColor(this.parent.color(0))
		.setSize(this.parent.width / 3, this.parent.height + 1)
		.setPosition(this.originalLeftEdgeX, 0)
		.setVisible(false);

		// Add play button, hamburger and menu x:
		this.addOutsideButtons();

		// Add Menu and Title labels (after menuX, because it gets its x values from that):
		ControlFont	largerStandard	= new ControlFont(ControlP5.BitFontStandard58, 13);

		this.sidebarCP5.addTextlabel("title")
		.setGroup("sidebarGroup")
		.setPosition(75, 5)
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
		
		// TODO: depending on what kind of guide tones this needs, might move addGuideTonePopout() here

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
		addHideButtons(textYVals[0]);

		addSliders(textYVals[1], textYVals[2], textYVals[3], textYVals[4]);

		addKeySelector(textYVals[5]);
		this.setCurKey(this.curKey, this.majMinChrom);

		this.addGuideTonePopout(textYVals[5]);

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
		this.updateColors(this.curColorStyle);

		this.addCustomPitchColor(textYVals[15], noteYVals);

		addHSBSliders(modulateHSBVals);

		addModulateSliders(modulateYVals);


		this.hideTextLabels();

		this.curColorStyle	= ModuleTemplate.CS_RAINBOW;
		this.dichromFlag	= false;
		this.trichromFlag	= false;	

		this.sidebarCP5.getController("keyDropdown").bringToFront();
	} // initModuleTemplate
	
	protected void addOutsideButtons()
	{
		int	playX		= this.originalLeftEdgeX + this.rectWidth - 45;
		int	playY		= this.topYVal + 15;
		int	playWidth	= 30;
		int	playHeight	= 30;
		
		System.out.println("	playX = " + playX + "; playY = " + playY);

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
		.setPosition(new float[] { (playX - playWidth - 10), playY } )
		.setImages(pauseImage, images[0])
		.updateSize()
		.setVisible(false);

		int	hamburgerX		= this.originalLeftEdgeX + 10;
		int	hamburgerY		= this.topYVal + 13;
		int	hamburgerWidth	= 30;
		int	hamburgerHeight	= 30;

		PImage	hamburger	= this.parent.loadImage("hamburger.png");
		hamburger.resize(hamburgerWidth, hamburgerHeight);
		this.sidebarCP5.addButton("hamburger")
		.setPosition(new float[] { hamburgerX, hamburgerY })
		.setImage(hamburger)
		.setClickable(true)
		.updateSize();

		// Since this is part of sidebarGroup, its position *does* need to be relative to the group:
		int	menuXX			= 5;
		int	menuXY			= 5;
		int	menuXWidth		= 15;

		PImage	menuX	= this.parent.loadImage("menuX.png");
		menuX.resize(menuXWidth, 0);
		this.sidebarCP5.addButton("menuX")
		.setPosition(new float[] { menuXX, menuXY } )
		.setImage(menuX)
		.setGroup("sidebarGroup")
		.updateSize()
		.bringToFront();
	} // addOutsideButtons
	
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
				"scale"
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

		int	id	= 4;

		this.sidebarCP5.addTextlabel("hide")
		.setPosition(labelX, hideY + 4)
		.setGroup("sidebarGroup")
		.setValue("Hide");

		for(int i = 0; i < names.length; i++)
		{
			this.sidebarCP5.addToggle(names[i])
			.setPosition(xVals[i], hideY)
			.setWidth(hideWidth)
			.setGroup("sidebarGroup")
			.setId(id);
			this.sidebarCP5.getController(names[i]).getCaptionLabel().set(labels[i]).align(ControlP5.CENTER, ControlP5.CENTER);

			id	= id + 1;
		}

		this.showScale = true;
	} // addHideButtons

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
				this.goalHue[i]	= this.canvasColor[i];
			} // for - canvas
		} else {	
			for(int i = 0; i < this.goalHue.length; i++)
			{
				this.goalHue[i]	= this.getColors()[position][i];	
			} // for - colors
		} // else

	} // getGoalHue


	public void fade(int position)
	{
		if(position > this.getColors().length || position < -1) {
			throw new IllegalArgumentException("ModuleTemplate.getGoalHue: position " + position + 
					" is out of bounds; must be between -1 (signifying canvas color) or " + (getColors().length - 1));
		} // error checking

		if(this.input.getAdjustedFund() < this.threshold)	
		{	
			this.nowBelow	= true;
			
			for(int i = 0; i < this.goalHue.length; i++)
			{
				this.goalHue[i]	= this.canvasColor[i];
			} // for - canvas
			
		} else {
			this.nowBelow	= false;
			
			for(int i = 0; i < this.goalHue.length; i++)
			{
				this.goalHue[i]	= this.getColors()[position][i];	
			} // for - colors
			
		} // else

		if(this.checkpoint < this.parent.millis())
		{
			for(int i = 0; i < 3; i++)
			{
				if(this.curHue[i] < this.goalHue[i])
				{
					this.curHue[i]	=	this.curHue[i] + this.colorAdd[i];
				} else if(this.curHue[i] > this.goalHue[i])
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
		} else if(!this.nowBelow && colorReached) {
			// Or, if coming from one super-threshold note to another, use the transition value:
			this.attRelTranPos	= 2;
		} else if(this.nowBelow) {
			// Or, if volume fell below the threshold, switch to release value:
			this.attRelTranPos	= 1;
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
		if(curHuePos < 0 || curHuePos > this.getColors().length) {
			throw new IllegalArgumentException("Module_01_02.setup(): curHuePos " + curHuePos + " is out of the bounds of the colors; " + 
					"must be between 0 and " + this.getColors().length);
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
	 * Applies the values from this.hueSatBrightnessMod to the contents of super.colors.
	 * @param colors	super.colors
	 * @param hsbColors	this.hsbColors
	 */
	public void applyHSBModulate(float[][] colors, float[][] hsbColors)
	{
		if(colors == null || hsbColors == null) {
			throw new IllegalArgumentException("ModuleTemplate.applyHSBModulate: one of the float[] parameters is null (colors = " + colors + "; hsbColors = " + hsbColors);
		} // error checking

		float[] hsb = new float[3];

		for (int i = 0; i < colors.length; i++)
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
	
	/**
	 * Calls playMelody(key, bpm, scale, rangeOctave) with the curKey, bpm, rangeOctave instance vars
	 * and the string corresponding to the majMinChrom instance var ("major", "minor", or "chromatic").
	 * @param scale
	 */
	private void playMelody()
	{

		String[]	scales	= new String[] { "major", "minor", "chromatic" };
		this.melody.playMelody(this.curKey, this.bpm, scales[this.majMinChrom], this.rangeOctave, this.instrument);
	} // playMelody
	
	/**
	 * Displays the "sidebarGroup" of this.sidebarCP5
	 */
	protected void displaySidebar()
	{	
		this.sidebarCP5.getGroup("sidebarGroup").setVisible(true);
		this.leftEdgeX 	= this.parent.width / 3;

	} // displaySidebar
	
	public void controlEvent(ControlEvent controlEvent)
	{
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
			this.displaySidebar();
			controlEvent.getController().setVisible(false);
			this.sidebarCP5.getWindow().resetMouseOver();
		} // if - hamburger

		// MenuX button:
		if(controlEvent.getController().getName().equals("menuX"))
		{
			this.leftEdgeX	= 0;
			//			this.sidebarCP5.setVisible(false);
			this.sidebarCP5.getGroup("sidebarGroup").setVisible(false);
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

		// Hide scale:
		if(controlEvent.getName().equals("scale"))
		{
			this.setShowScale(!((Toggle) (controlEvent.getController())).getState());
		}

	} // controlEvent


	public int getCheckpoint()				{	return this.checkpoint;		}

	public void setCheckpoint(int newVal)	{	this.checkpoint	= newVal;	}

	public float getThreshold()				{	return this.threshold;		}
	
	public void setThreshold(float newVal)	{	this.threshold	= newVal;	}

	public float[][] getColors() {
		return colors;
	}

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

		int	id	= this.firstColorSelectId;		// 63 (as of 6/14)

		for(int i = 0; i < labels.length; i++)
		{
			this.sidebarCP5.addButton("button" + id)
			.setPosition(xVals[i], colorSelectY)
			.setWidth(colorSelectWidth)
			.setCaptionLabel(labels[i])
			.setGroup("sidebarGroup")
			.setId(id);
			this.sidebarCP5.getController("button" + id).getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);

			id	= id + 1;

			this.sidebarCP5.addColorWheel("colorWheel" + id)
			.setPosition(xVals[i], colorSelectY - 200)
			.setRGB(this.parent.color(255,0,0))
			.setLabelVisible(false)
			.setVisible(false)
			.setGroup("sidebarGroup")
			.setId(id);

			id	= id + 1;

			this.sidebarCP5.addTextfield("textfield" + id)
			.setPosition(xVals[i] + colorSelectWidth + colorSelectSpace, colorSelectY)
			.setWidth(textfieldWidth)
			.setAutoClear(false)
			.setVisible(false)
			.setLabelVisible(false)		// This has no effect either way.
			.setText("Code#")
			.setGroup("sidebarGroup")
			.setId(id);

			id	= id + 1;
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

		// Note Buttons, ColorWheels and corresponding Textfields will have id's of 24 or over;
		// Button id % 3 == 0; ColorWheel id % 3 == 1, Textfield id % 3 == 2.
		int	namePos	= 0;
		int	id		= this.firstCustomColorId;		// 24 (as of 6/14)
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
			.setVisible(false)
			//			.setLabelVisible(false)			// This has no effect either way.
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
			.setVisible(false)
			.setText("Code#")
			.setGroup("sidebarGroup")
			.setId(id);

			id = id + 1;
			namePos	= namePos + 1;
		} 
		namePos	= 0;
		for(int i = 0; i < noteNames1.length; i++)
		{// Second row of pitches:

			this.sidebarCP5.addButton("button" + id)
			.setPosition(noteX3, noteYVals[i])
			.setWidth(buttonWidth)
			.setLabel(noteNames3[namePos])
			.setId(id)
			.setGroup("sidebarGroup")
			.getCaptionLabel().toUpperCase(false);

			id = id + 1;

			this.sidebarCP5.addColorWheel("colorWheel" + id)
			.setPosition(noteX3, noteYVals[i] - 200)
			.setRGB(this.parent.color(colors[colorpos][0], colors[colorpos][1], colors[colorpos][2]))
			.setLabelVisible(false)
			.setVisible(false)
			.setGroup("sidebarGroup")
			.setId(id);

			colorpos = colorpos + 1;

			id = id + 1;

			this.sidebarCP5.addTextfield("textfield" + id)
			.setPosition(textfieldX3, noteYVals[i])
			.setWidth(textfieldWidth)
			.setAutoClear(false)
			.setVisible(false)
			.setText("Code#")
			.setGroup("sidebarGroup")
			.setId(id);

			id = id + 1;
			namePos	= namePos + 1;
		} 
		namePos	= 0;
		for(int i = 0; i < noteNames1.length; i++)
		{// Second row of pitches:

			this.sidebarCP5.addButton("button" + id)
			.setPosition(noteX4, noteYVals[i])
			.setWidth(buttonWidth)
			.setLabel(noteNames4[namePos])
			.setId(id)
			.setGroup("sidebarGroup")
			.getCaptionLabel().toUpperCase(false);

			id = id + 1;

			this.sidebarCP5.addColorWheel("colorWheel" + id)
			.setPosition(noteX4, noteYVals[i] - 200)
			.setRGB(this.parent.color(colors[colorpos][0], colors[colorpos][1], colors[colorpos][2]))
			.setLabelVisible(false)
			.setVisible(false)
			.setGroup("sidebarGroup")
			.setId(id);

			colorpos = colorpos + 1;

			id = id + 1;

			this.sidebarCP5.addTextfield("textfield" + id)
			.setPosition(textfieldX4, noteYVals[i])
			.setWidth(textfieldWidth)
			.setAutoClear(false)
			.setVisible(false)
			.setText("Code#")
			.setGroup("sidebarGroup")
			.setId(id);
			this.parent.ellipse(textfieldX4, noteYVals[i], 100, 100);

			id = id + 1;
			namePos	= namePos + 1;
		} 
		// for - second row of pitches

		Color	transparentBlack	= new Color(0, 0, 0, 200);
		int		transBlackInt		= transparentBlack.getRGB();

		this.sidebarCP5.addBackground("background")
		.setPosition(this.originalLeftEdgeX, 0)
		.setSize(this.parent.width / 3, this.parent.height)
		.setBackgroundColor(transBlackInt)
		.setGroup("sidebarGroup")
		.setVisible(false);
	} // addNoteColorSelectors


	/**
	 * Method called during instantiation to hide text labels of text fields
	 * TODO: do this during creation of Textfields/Sliders
	 * 
	 * Elena Ryan
	 * Added 1/24/17
	 */	
	private void hideTextLabels() {
		// Slider id's start at 1 and go up by 2:
		for(int i = 1; i < 20; i = i + 2)
		{
			this.sidebarCP5.getController("textfield"+i).getCaptionLabel().setVisible(false);
		}//hides slider labels

		// Textfield id's start at 26 and go up by 3, skipping 62:
		for(int i = 26; i < 75; i = i + 3)
		{			
			// no Textfield with id of 62:
			if(i == 62)
			{
				i	= i + 3;
			}

			this.sidebarCP5.getController("textfield"+i).getCaptionLabel().setVisible(false);
		}//hides text labels for colors

	}//hideTextLabels

	private void addHSBSliders(int[] hsb)
	{
		int	labelX			= 10;
		int	labelWidth		= 70;

		int	sliderWidth		= 170;
		int	sliderHeight	= 20;

		int	spacer			= 5;	// distance between slider and corresponding textfield
		int	tfWidth			= 40;	// width of Textfields

		String[]	names	= new String[] { "hueModLabel", "satModLabel", "brightModLabel" };
		String[]	values	= new String[] { "Hue", "Saturation", "Brightness" };

		int	id	= 14;		// this id picks up where the modulate sliders leave off

		for(int i = 0; i < hsb.length; i++)
		{
			// - Textlabel:
			this.sidebarCP5.addLabel(names[i])
			.setPosition(labelX, hsb[i] + 4)
			.setWidth(labelWidth)
			.setGroup("sidebarGroup")
			.setValue(values[i]);

			//	- Slider:
			this.sidebarCP5.addSlider("slider" + id)
			.setPosition(this.leftAlign, hsb[i])
			.setSize(sliderWidth, sliderHeight)
			.setSliderMode(Slider.FLEXIBLE)
			.setRange(-1, 1)
			.setValue(0)
			.setGroup("sidebarGroup")
			.setId(id);

			id	= id + 1;

			this.sidebarCP5.addTextfield("textfield" + id)
			.setPosition(this.leftAlign + sliderWidth + spacer, hsb[i])
			.setSize(tfWidth, sliderHeight)
			.setText(this.sidebarCP5.getController("slider" + (id-1)).getValue() + "")
			.setAutoClear(false)
			.setGroup("sidebarGroup")
			.setId(id);

			id	= id + 1;
		} // for   
	}//the HSB Sliders Heavily Adapted from modSlider Method

	/**
	 * Method called during instantiation, to initialize the color modulate sliders.
	 * 
	 * @param modulateYVals	int[] of the y values of the red, green, and blue sliders, respectively.
	 */
	private void addModulateSliders(int[] modulateYVals)
	{
		int	labelX			= 10;
		int	labelWidth		= 70;

		int	sliderWidth		= 170;
		int	sliderHeight	= 20;

		int	spacer			= 5;	// distance between slider and corresponding textfield
		int	tfWidth			= 40;	// width of Textfields

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
//			.setRange(-255, 255)
//			.setValue(0)
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
			//			// if avoids errors during instantiation:
			if(this.sidebarCP5.getController("button69") != null)
			{
				this.sidebarCP5.getController("button69").setLock(true);
			}
			if(this.sidebarCP5.getController("button72") != null)
			{
				this.sidebarCP5.getController("button72").setLock(true);
			}

		}

		// Dichromatic:
		if(this.curColorStyle == ModuleTemplate.CS_DICHROM)
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
			if(this.sidebarCP5.getController("button69") != null)
			{
				this.sidebarCP5.getController("button69").setLock(false);
			}
			if(this.sidebarCP5.getController("button72") != null)
			{
				this.sidebarCP5.getController("button72").setLock(true);
			}
		} // Dichromatic

		// Trichromatic:
		if(this.curColorStyle == ModuleTemplate.CS_TRICHROM)
		{
			// first time trichromatic has been called:
			if(!this.trichromFlag)
			{
				this.trichromatic_OneRGB(this.colors[0]);

				this.trichromFlag	= true;
			}

			// every other time:
			else
			{
				int colorPos2;
				int	colorPos3;

				if(this.getMajMinChrom() == 2)
				{
					colorPos2	= 4;
					colorPos3	= 8;
				} else {
					colorPos2	= 3;
					colorPos3	= 4;
				}

				this.trichromatic_ThreeRGB(this.colors[0], this.colors[colorPos2], this.colors[colorPos3]);
			} // else


			// Unlock all for Trichromatic:
			// if avoids errors during instantiation:
			if(this.sidebarCP5.getController("button69") != null)
			{
				this.sidebarCP5.getController("button69").setLock(false);
			}
			if(this.sidebarCP5.getController("button72") != null)
			{
				this.sidebarCP5.getController("button72").setLock(false);
			}
		} // Trichromatic

		// Custom:
		if(this.curColorStyle == ModuleTemplate.CS_CUSTOM)
		{
			// Populate the Textfields with the current colors in the colors array:
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


			//			this.applyColorModulate(this.colors, this.originalColors);
			//			this.applyHSBModulate(this.colors, this.hsbColors);
			((Toggle)(this.sidebarCP5.getController("chrom"))).setState(true);

			// (The functionality in controlEvent will check for custom, and if it is custom, they will set their position of colors to their internal color.)


			// (Will they need to check to make sure that the key is actually chromatic?)
		} // custom colorStyle

	} // updateColors
	
	public void legend(int goalHuePos)
	{
		this.legend(goalHuePos, 24);
	}

	public void legend(int goalHuePos, int textSize)
	{
		this.parent.textSize(textSize);

		//		String[]	notes	= this.getScale(this.curKeyOffset, this.majMinChrom);
		String[]	notes	= this.getScale(this.curKey, this.getMajMinChrom());

//		float  sideWidth1   = (this.rectWidth - this.leftEdgeX) / notes.length;
		// TODO: ignores sidebar pop-out for now
		float  sideWidth1   = this.rectWidth / notes.length;
		// TODO: height pretty arbitrary right now:
		float  sideHeight  = this.rectHeight / 7;
		float	addToLastRect	= this.rectWidth - (sideWidth1 * notes.length);
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
				this.parent.rect(leftEdgeX + (sideWidth1 * i), ((this.topYVal + this.rectHeight) - (sideHeight * 1.5f)), sideWidth2, (float) (sideHeight * 1.5));
				//      rect(0, (side * i), side * 1.5, side);
			} else {
				this.parent.rect(leftEdgeX + (sideWidth1 * i), (this.topYVal + this.rectHeight) - sideHeight, sideWidth2, sideHeight);
				//      rect(0, (side * i), side, side);
			}

//			System.out.println("	legend; this.topYVal = " + this.topYVal + "; drawing at " + ((this.topYVal + this.rectHeight) - sideHeight));
			this.parent.fill(0);
			this.parent.text(notes[i], (float) (leftEdgeX + (sideWidth1 * i) + (sideWidth1 * 0.35)), (this.topYVal + this.rectHeight) - 15);
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

	private void displaySidebar()
	{	
		this.sidebarCP5.getGroup("sidebarGroup").setVisible(true);
		this.setLeftEdgeX(this.originalLeftEdgeX + this.parent.width / 3);

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
	 * Calls playMelody(key, bpm, scale, rangeOctave) with the curKey, bpm, rangeOctave instance vars
	 * and the string corresponding to the majMinChrom instance var ("major", "minor", or "chromatic").
	 * @param scale
	 */
	private void playMelody()
	{

		String[]	scales	= new String[] { "major", "minor", "chromatic" };

		this.melody.playMelody(this.curKey, this.bpm, scales[this.majMinChrom], this.rangeOctave, this.instrument);
//		this.melody.playMelody(this.curKey, this.bpm, scales[this.majMinChrom], this.rangeOctave);
	} // playMelody

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
		float	percent		= 100 / this.scaleLength;

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
				this.hsbColors[i][j]	= rainbowColors[this.getMajMinChrom()][i][j];
			} // for - j (going through rgb values)
		} // for - i (going through colors)

	} // rainbow

	/**
	 * Applies the values of the Red Modulate/Green Modulate/Blue Modulate sliders and 
	 * calls applyHSBModulate() to apply the values of the Hue/Saturation/Brightness Modulate sliders.
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

			} // for - j
		} // for - i

		this.fillHSBColors();

		this.applyHSBModulate(this.colors, this.hsbColors);
	} // applyColorModulate

	/**
	 * Applies the values from this.hueSatBrightnessMod to the contents of this.colors.
	 * @param colors	this.colors
	 * @param hsbColors	this.hsbColors
	 */
	private void applyHSBModulate(float[][] colors, float[][] hsbColors)
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

	/**
	 * This method handles the functionality of all the buttons, sliders, and textFields;
	 * Notate bene: any classes that include a moduleTemplate *must* include a controlEvent(ControlEvent) method that calls this method.
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
			this.displaySidebar();
			controlEvent.getController().setVisible(false);
			this.sidebarCP5.getWindow().resetMouseOver();
		} // if - hamburger

		// MenuX button:
		if(controlEvent.getController().getName().equals("menuX"))
		{
			this.setLeftEdgeX(this.originalLeftEdgeX);
			//			this.sidebarCP5.setVisible(false);
			this.sidebarCP5.getGroup("sidebarGroup").setVisible(false);
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

		// Hide scale:
		if(controlEvent.getName().equals("scale"))
		{
			this.setShowScale(!((Toggle) (controlEvent.getController())).getState());
		}

		//		int	sliderIDCutoff	= this.lastTextfieldId;

		// Sliders (sliders have even id num and corresponding textfields have the next odd number)
		if(id % 2 == 0 && id < this.lastTextfieldId && id > -1)
		{
			try
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

				this.applyColorModulate(this.colors, this.originalColors);
			} // red/green/blue mod

			if(id == 14 || id == 16 || id == 18)
			{
				int pos = (id/2)-7;
				this.hueSatBrightnessMod[pos] = sliderValFloat;

				this.applyHSBModulate(colors, originalColors);
			}//hsb mod

			if(id == 20)
			{
				this.bpm	= Math.max(Math.min((int)sliderValFloat, 240), 0);
			}
			
			if(id == 22)
			{
				this.instrument.setVolume(Math.max(Math.min(sliderValFloat, 5), 0));
			}

			} catch (NullPointerException npe) {
				System.out.println("ModuleTemplate.controlEvent - sliders: caught NullPoint - curTextfield = " + (Textfield)this.sidebarCP5.getController("textfield" + (id + 1)));
			}

		} // sliders

		//The call for hsb changes
		/*
		 * the id numbers of the sliders should be 14 16 18, and the numSliders should change
		 * if(id == INSERT || id == INSERT || id == INSERT)
		 * int pos set position
		 * this.hsbMod[pos] == sliderValFloat
		 * So Basically, if the ID corresponds to the 
		 * HSB sliders, apply it's modulate
		 * Conversion to hsb then back to RGB
		 * 
		 */


		// Textfields
		if(id % 2 == 1 && id <= this.lastTextfieldId && id > 0)
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

			// getItem returns a Map of the color, state, value, name, etc. of that particular item
			//  in the ScrollableList:
			Map<String, Object> keyMap = this.sidebarCP5.get(ScrollableList.class, "keyDropdown").getItem(keyPos);

			// All we want is the name:
			String	key	= (String) keyMap.get("name");
			this.curKey	= key;
			this.curKeyOffset = keyPos;
			this.curKeyEnharmonicOffset	= this.enharmonicPos[getCurKeyOffset()];

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

			// Setting the input file:
			int	enharmonicPos	= this.enharmonicPos[keyPos];
			String	filename	= this.filenames[this.majMinChrom][enharmonicPos];
			this.inputFile	= "Piano Scale Reference Inputs/" + filename;

			if(!(this.getLeftEdgeX() == this.originalLeftEdgeX)) {
				this.displaySidebar();
			}

			// Attempts to make the list show in front of tonicColor button
			// (fruitless because tonicColor has been brought to the front; I moved it over instead.
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
		if( ( controlEvent.getId() >= this.firstCustomColorId ) && (controlEvent.getId() % 3 == 0) )
		{
			Button	curButton	= (Button)controlEvent.getController();

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
			//			this.updateColors(this.curColorStyle);
		} // custom pitch color selectors (i.e., show color wheel)

		// Custom pitch ColorWheels
		if(controlEvent.getId() > this.firstCustomColorId && (controlEvent.getId() % 3 == 1))
		{
			// only call updateColors() for colorSelect ColorWheels:
			if(controlEvent.getId() >= this.firstColorSelectId && controlEvent.getId() <= this.lastColorSelectId)
			{
				this.updateColors(this.curColorStyle);
			} // if

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

		} // custom pitch colorWheels

		// ColorWheel Textfields (id 23 or over; id % 3 == 2):
		if(controlEvent.getId() > this.firstCustomColorId && (controlEvent.getId() % 3 == 2))
		{
			// This call has to happen early on in the method so that, after making the dichrom/trichrom spectrum,
			// individual colors can be changed independently of the rest.
			//			this.updateColors(this.curColorStyle);

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

		} // ColorWheel Textfields


		// Color Style:
		if(controlEvent.getName().equals("rainbow") ||
				controlEvent.getName().equals("dichrom") ||
				controlEvent.getName().equals("trichrom") ||
				controlEvent.getName().equals("custom"))
		{
			Toggle	curToggle	= (Toggle) controlEvent.getController();


			/*
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
					toggleArray[i].setState(false);
				} else {
					toggleArray[i].setState(true);
				}

				// set broadcasting back to original setting:
				toggleArray[i].setBroadcast(broadcastState[i]);
			} // for - switch off all Toggles:

			// Call to rainbow() not in updateColors() so that it doesn't revert every time a button is pressed:
			if(this.curColorStyle == ModuleTemplate.CS_RAINBOW)
			{
				this.rainbow();
			}


			// These calls have to come after all the colors have been set,
			// so that originalColors and hsbColors can be filled with the latest:
			this.fillOriginalColors();
			this.fillHSBColors();
			this.resetModulateSlidersTextfields();
			this.applyColorModulate(this.colors, this.originalColors);

		} // colorStyle buttons

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
		} // ADSR Presets

	} // controlEvent

	/**
	 * Puts the contents of colors into originalColors.
	 */
	private void fillOriginalColors()
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

	/**
	 * Puts the contents of this.colors into this.hsbColors.
	 */
	private	void fillHSBColors()
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
	 * Sets the Sliders and Textfields for RGB and HSB color modulate to 0.
	 */
	private void resetModulateSlidersTextfields()
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
	
	public float[] getCurHue()				{	return this.curHue;	}
	
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
		
		this.attRelTranVals[position]	= val;
	}
	
	public int getLeftEdgeX()				{	return this.leftEdgeX;	}
	

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


	public float getART(int arORt)
	{
		if(arORt < 0 || arORt > this.attackReleaseTransition.length)
		{
			throw new IllegalArgumentException("ModuleTemplate.getART: parameter " + arORt + " is not a valid position in the array this.attackReleaseTransition.");
		} // error checking

		return	this.attackReleaseTransition[arORt];
	} // getART


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

	public	float[]	getCanvasColor()
	{
		return this.canvasColor;
	}

	public int getCheckpoint()				{	return this.checkpoint;	}

	public void setCheckpoint(int newVal)	{	this.checkpoint	= newVal;	}

	public float[][] getColors()
	{
		return this.colors;
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
	
} // ModuleTemplate
