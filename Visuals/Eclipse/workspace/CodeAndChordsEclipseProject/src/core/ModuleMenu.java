package core;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import controlP5.Button;
import controlP5.ColorWheel;
import controlP5.ControlEvent;
import controlP5.ControlListener;
import controlP5.ControlP5;
import controlP5.ScrollableList;
import controlP5.Toggle;
import core.input.Input;
import core.input.RecordedInput;
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

	/**
	 * This list of notes is to be used for custom color select Button labels
	 */
	public	final String[] noteNames 	= new String[] {
			"A", "A#/Bb", "B", "C", "C#/Db", "D", "D#/Db", "E", "F", "F#/Gb", "G", "G#/Ab"
	}; // noteNames

	/**
	 * This String[] is used to change the order of the labels on the custom color select Buttons
	 * when a new key is selected.
	 */
	private	String[]	newNoteNames	= new String[this.noteNames.length];

	// Positions in filenames String[] here
	private	final int[]	enharmonicPos	= new int[] {
			0, 1, 1, 2, 3, 4, 4, 5, 6, 6, 7, 8, 9, 9, 10, 11, 11
	}; // enharmonicPos

	/**
	 * Colors roughly ROYGBIV; used for the rainbow() method.
	 */
	private final int[][][] rainbowColors	= new int[][][] { 
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
	private final	int[][]	scaleDegreeColors	= new int[][] {
		// major:
		new int[] { 0, 0, 1, 2, 2, 3, 4, 4, 4, 5, 6, 6 },
		// minor:
		new int[] { 0, 0, 1, 2, 2, 3, 4, 4, 5, 5, 6, 6 },
		// chromatic:
		new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 }
	}; // scaleDegreeColors

	/**
	 * This is the same idea as scaleDegreeColors, but each length 2 array indicates the starting and ending
	 * positions of the colors that are affected by a particular ColorWheel in a major or minor key
	 */
	protected	final	int[][][] colorPosStartEnd	=  new int [][][] {
		// major:
		new int[][] { 
			new int[] { 0, 1 }, 
			new int[] { 0, 1 }, 
			new int[] { 2, 2 },
			new int[] { 3, 4 },
			new int[] { 3, 4 },
			new int[] { 5, 5 },
			new int[] { 6, 8 },
			new int[] { 6, 8 },
			new int[] { 6, 8 },
			new int[] { 9, 9 },
			new int[] { 10, 11 },
			new int[] { 10, 11 },
		},
		// minor:
		new int[][] { 
			new int[] { 0, 1 }, 
			new int[] { 0, 1 }, 
			new int[] { 2, 2 },
			new int[] { 3, 4 },
			new int[] { 3, 4 },
			new int[] { 5, 5 },
			new int[] { 6, 7 },
			new int[] { 6, 7 },
			new int[] { 8, 9 },
			new int[] { 8, 9 },
			new int[] { 10, 11 },
			new int[] { 10, 11 },
		},
		// chromatic:
		new int[][] { 
			new int[] { 0, 0 }, 
			new int[] { 1, 1 }, 
			new int[] { 2, 2 },
			new int[] { 3, 3 },
			new int[] { 4, 4 },
			new int[] { 5, 5 },
			new int[] { 6, 6 },
			new int[] { 7, 7 },
			new int[] { 8, 8 },
			new int[] { 9, 9 },
			new int[] { 10, 10 },
			new int[] { 11, 11 },
		}
	}; // colorPosStartEnd

	protected float[][] superShapes = new float[][] {
		new float[] { 1, 1, 0, 0, 1, 1, 1 },
		new float[] { 1, 1, 5, 5, 1, 1, 1 },
		new float[] { 2, 2, 3, 3, 1, 1, 1 },
		new float[] { .7f, .7f, 8, 8, 1, 1, 1},
		new float[] { 1.4f, 1.4f, 4, 4, .3f, .5f, .7f }
	};

	protected	PApplet	parent;

	/**	Module to which this Menu belongs	*/
	protected	Module	module;

	/**	This is the parent.millis() when menuX is called, so that we don't call Hamburger when menuX is clicked	*/
	private		int			lastMenuXMillis;

	/**
	 * Color Styles: Rainbow, Dichromatic (fade from one color to another throughout the legend),
	 * Trichromatic (fade from color1 to  color2 and then from color2 to color3 throughout the legend),
	 * or Custom, which may be discontinued.
	 */
	public	static	int	CS_RAINBOW	= 0;
	public	static	int	CS_DICHROM	= 1;
	public	static	int	CS_TRICHROM	= 2;
	public	static	int	CS_CUSTOM	= 3;
	protected	int[]	curColorStyle;

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
	
	/**	ColorWheels that hold all the colors for the Module	*/
	protected	ColorWheel[]	colorSelect;

	/**	Current hue (as opposed to the goal hue, which may not have been reached)	 */
	private	int[][]			curHue;

	/**	Hue that corresponds to the current sound, but to which curHue may not yet have faded	*/
	private	int[][]			goalHue;
	
	/**	Current shapeSize (as opposed to the goal shapeSize, which may not have been reached)	 */
	private	int[]			curShapeSize;

	/**	ShapeSize that corresponds to the current amplitude, but to which curShapeSize may not yet have faded	*/
	private	int[]			goalShapeSize;
	
	/**	The amount that must be added each iteration through the fadeShapeSize loop to reach the goalShapeSize */
	private	int[]			shapeSizeAdd;
	
	/**	Indicates whether or not each shape has reached its goalSize */
	private	boolean[]		shapeSizeReached;
	
	/**	The distance between the current and goal size for each Shape */
	private	int[]			shapeSizeRange;

	/**	The color when sound is below the piano (lowest) threshold	*/
	protected	int[]		canvasColor;

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

	/**	Holds an alpha value (i.e., opacity) for the Module (all colors will use the same value)	*/
	private	int			alphaVal;

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
	protected	float[][] satBrightThresholdVals;

	/**	Hodls the values of the saturation percent and brightness percent threshold Sliders, respectively	*/
	protected	float[][]	satBrightPercentVals;
	
	/**	
	 * A number between 0 and 1 indicating the percent at which a Shape should be scaled
	 * given the current amplitude to allow a smooth growing or shrinking.
	 */
	protected	float[]	curAmpScale;
	
	/**	Used in smoothAmpScale to calculate curAmpScale */
	protected	float[]	goalAmpScale;
	
	protected	int[]	ampCheckpoint;

	/**	Flag denoting whether or not the current volume is below the threshold	*/
	private boolean[]	nowBelow;

	/**	Attack, Release, or Transition - 0 = attack, 1 = release, 2 = transition	*/
	private	int[]		attRelTranPos;

	/**	For a timer that allows attack/release/transition sliders to be time-based	*/
	private int[]	colorCheckpoint;
	
	/**	For a timer that allows shape size to change smoothly based on amplitude input	*/
	private int[]	shapeSizeCheckpoint;

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
	protected	float[]	shapeSize = new float[] {0};

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
	//	protected	int	numInputs;

	/**	If true, adjustments to Controllers affect all inputs	*/
	protected	boolean	global;

	/**	The input line currently affected by Controllers; only applies if !global	*/
	protected	int		currentInput;

	/**	Used to implement global vs. single input actions: 
	 * if global, startHere = 0 and endBeforeThis = module.totalNumInputs;
	 * if particular input, startHere = currentInput and endBeforeThis = (currentInput + 1).
	 */
	private	int	startHere;
	private	int	endBeforeThis;
	//	protected	Shape	shape;

	/**	Shapes, initialized by addShapeMenu(int)	*/
	//	protected	Shape[]	shapes;

	// TODO: considering replacing Module's Shape and ShapeEditor with these
	private	ShapeEditor	shapeEditor;

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
	protected	int	firstARTSliderId		= -1;
	protected	int	firstHSBSliderId		= -1;
	protected	int	firstRGBSliderId		= -1;
	protected	int	bpmSliderId				= -1;
	protected	int	volumeSliderId			= -1;
	protected	int	shapeSizeSliderId		= -1;
	protected	int	firstRangeSegmentsId	= -1;
	protected	int	pianoThresholdSliderId	= -1;
	protected	int	forteThresholdSliderId	= -1;
	protected	int	firstSatBrightThreshSliderId	= -1;
	protected	int	alphaSliderId			= -1;

	/**	Indicates whether or not amplitude determines height of bars (only applies to amplitude bar Modules) */
	protected boolean dynamicBars = false;

	/**
	 * Used to position the labels next to the controllers
	 */
	private	int	textYVals[];
	
	/**
	 * Used to position the controllers
	 */
	private	int	controllerXVals[];

	private	int	tabHeight	= 30;

	/**	For saving and loading saved color states */
	final JFileChooser fc = new JFileChooser();
	
	/**	For now, adding this so that the play Button can either start the guide tones or these tracks */
	private	RecordedInput recInput;
	
	private	boolean	useRecInput;
	
	private	boolean	recInputPlaying;
	
	private float[] 	amplitudeFollower;
	
	//this part of the amplitude follower will be replaced by a slider in the menu to allow for more consistent results
	private float[]		maxAmplitude;


	/**
	 * Constructor
	 * 
	 * @param parent	PApplet used to draw, etc.; will instantiate this.parent instance var
	 * @param input		Input for all audio input; will instantiate this.input
	 * @param sidebarTitle	String designating the title of the module to which this template corresponds
	 */
	public ModuleMenu(PApplet parent, Module module, Input input, int totalNumColorItems)
	{
		super(parent, parent.width, parent.height);

		this.parent			= parent;
		this.module			= module;
		this.input			= input;

		this.outsideButtonsCP5	= new ControlP5(this.parent);
		this.outsideButtonsCP5.addListener((ControlListener)this);

		this.setShowPlayStop(true);
		this.showPause		= false;
		this.showHamburger	= true;

		this.global			= true;
		this.currentInput	= 0;
		this.startHere		= 0;
		this.endBeforeThis	= this.module.getTotalNumInputs();

		this.melody			= new Melody(this.parent, this.input);
		this.instrument		= new Instrument(this.parent, this.input.getAudioContext());
		this.bpm			= 120;
		this.rangeOctave	= 3;
		this.curKey			= "A";
		this.majMinChrom	= 2;	// chromatic
		
		/*
		this.recInput	= new RecordedInput(this.module, new String[] {
				"6_Part_Scale1.wav",
				"6_Part_Scale2.wav",
				"6_Part_Scale3.wav",
				"6_Part_Scale4.wav",
				"6_Part_Scale5.wav",
				"6_Part_Scale6.wav",
				"6_Part_Scale7.wav"
		});
		
		this.recInput.pause(true);
		this.useRecInput		= false;
		this.recInputPlaying	= false;
		*/
	
		this.recInput	= new RecordedInput(this.module, new String[] {
				"6_Part_Scale1.wav",
				"6_Part_Scale2.wav",
				"6_Part_Scale3.wav",
				"6_Part_Scale4.wav",
				"6_Part_Scale5.wav",
				"6_Part_Scale6.wav",
				"6_Part_Scale7.wav",
				"cadenza/Here - Melody.wav",
				"cadenza/Here - Beatbox.wav"
		});
		
		this.recInput.pause(false);
		this.useRecInput		= true;
		this.recInputPlaying	= true;

		// ColorSelect will be filled in addColorSelect,
		// and, since global == true, this fill set this.colors, too.
		this.colorSelect		= new ColorWheel[totalNumColorItems];

		this.colors				= new int[this.module.getTotalNumInputs()][totalNumColorItems][3];
		this.hsbColors			= new int[this.module.getTotalNumInputs()][totalNumColorItems][3];
		this.curHue				= new int[this.module.getTotalNumInputs()][3];
		this.goalHue			= new int[this.module.getTotalNumInputs()][3];
		this.canvasColor		= new int[] { 1, 0, 0 };	// If this is set to rgb(0, 0, 0), the CW gets stuck in grayscale
		this.curColorStyle		= new int[this.module.getTotalNumInputs()];
		this.rainbow();

		this.curShapeSize		= new int[this.module.getTotalNumInputs()];
		this.goalShapeSize		= new int[this.module.getTotalNumInputs()];

		this.colorAdd			= new int[this.module.getTotalNumInputs()][3];
		this.colorRange			= new int[this.module.getTotalNumInputs()][3];

		this.colorReachedArray	= new boolean[this.module.getTotalNumInputs()][3];
		this.colorReached		= new boolean[this.module.getTotalNumInputs()];
		this.nowBelow			= new boolean[this.module.getTotalNumInputs()];
		this.fromColorSelect	= new boolean[this.module.getTotalNumInputs()];
		//		this.fromSpecialColors	= new boolean[this.module.getTotalNumInputs()];
		this.specialColorsPos	= new int[this.module.getTotalNumInputs()][3];

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

		this.alphaVal	= 255;

		this.dichromFlag	= false;
		this.trichromFlag	= false;

		this.attRelTranPos	= new int[this.module.getTotalNumInputs()];
		this.attRelTranVals	= new float[this.module.getTotalNumInputs()][3];
		this.colorCheckpoint		= new int[this.module.getTotalNumInputs()];
		this.shapeSizeCheckpoint	= new int[this.module.getTotalNumInputs()];
		
		this.curAmpScale	= new float[this.module.getTotalNumInputs()];
		this.ampCheckpoint	= new int[this.module.getTotalNumInputs()];

		for(int i = 0; i < this.attRelTranPos.length; i++)
		{
			this.attRelTranPos[i]	= 0;	// 0 = attack, 1 = release, 2 = transition
			this.attRelTranVals[i]	= new float[] {		200, 200, 200	};	// attack, release, transition all begin at 200 millis
			this.colorCheckpoint[i]		= this.parent.millis() + 100;
			this.shapeSizeCheckpoint[i]	= this.parent.millis() + 100;
			
			this.curAmpScale[i]	= 1;
			this.ampCheckpoint[i]	= this.parent.millis() + 100;
		}

		this.hueSatBrightnessMod        = new float[3];
		this.hueSatBrightPercentMod		= new float[3];
		this.redGreenBlueMod			= new float[3];

		this.satBrightThresholdVals	= new float[this.module.getTotalNumInputs()][2];
		this.satBrightPercentVals	= new float[this.module.getTotalNumInputs()][2];

		this.totalRangeSegments	= totalNumColorItems;
		this.curRangeSegments	= totalNumColorItems;

		this.thresholds		= new int[this.module.getTotalNumInputs()][totalNumColorItems];
		this.pianoThreshold	= new int[this.module.getTotalNumInputs()];
		this.forteThreshold	= new int[this.module.getTotalNumInputs()];

		for(int i = 0; i < this.thresholds.length; i++)
		{
			this.satBrightThresholdVals[i]	= new float[2];
			this.satBrightPercentVals[i]	= new float[2];

			this.pianoThreshold[i]	= 10;
			this.forteThreshold[i]	= 500;
			this.resetThresholds(i);
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

		
		this.controlP5.addGroup("sidebarGroup")
		.setBackgroundColor(this.parent.color(0))
		.setSize(this.sidebarWidth, this.parent.height + 1)
		.setPosition(0, 0)
		.setVisible(false);


		Color	transparentBlack	= new Color(0, 0, 0, 200);
		int		transBlackInt		= transparentBlack.getRGB();

		this.controlP5.addBackground("leftBackground")
		.setPosition(0, 0)
		.setSize(this.sidebarWidth, this.parent.height)
		.setBackgroundColor(transBlackInt)
		//.setGroup("sidebarGroup")
		.setVisible(false);

		this.controlP5.addBackground("topBackground")
		.setPosition(this.sidebarWidth, 0)
		.setSize(this.parent.width - this.sidebarWidth, (int)(this.parent.height - (this.parent.height * this.scale)))
		.setBackgroundColor(transBlackInt)
		//.setGroup("sidebarGroup")
		.setVisible(false);

		textYVals  		= new int[18];
		controllerXVals	= new int[3];

		// calculate y's
		// set y vals for first set of scrollbar labels:
		textYVals[0]	=	26;
		// Given our height = 250 and "hide" (textYVals[0]) starts at [40] - now 26 (1/17),
		// We want a difference of 27.  This gets that:
		int	yValDif = (int)((this.module.height - textYVals[0]) / 18);//(textYVals.length + noteYVals.length + modulateYVals.length));
		// ... but no smaller than 25:
		if(yValDif < 25) {
			yValDif	= 25;
		}

		for(int i = 1; i < textYVals.length; i++)
		{
			textYVals[i]	= textYVals[i - 1] + yValDif;
		} // for

		// Add extra space before "Pitch Color Codes":
		textYVals[textYVals.length - 3]	= textYVals[textYVals.length - 4] + (int)(yValDif * 1.5);
		textYVals[textYVals.length - 2]	= textYVals[textYVals.length - 3] + (int)(yValDif * 1);
		textYVals[textYVals.length - 1]	= textYVals[textYVals.length - 2] + (int)(yValDif * 1);

		controllerXVals	= new int[] {	
				0, 
				(this.module.width / 3) - 20, 
				((this.module.width / 3) * 2) - 40	
		};

		this.controlP5.controlWindow.setPositionOfTabs(this.leftAlign, this.textYVals[0] - 10);
		this.addLandingMenu();
		
		// Add play button, hamburger and menu x:
		this.addOutsideButtons();

		this.fc.setCurrentDirectory(new File("./savedColors/"));

		// Filter out all but .txt files:
		FileFilter filter = new FileNameExtensionFilter(".txt", "txt");
		this.fc.addChoosableFileFilter(filter);
		this.fc.removeChoosableFileFilter(this.fc.getAcceptAllFileFilter());
		
		this.maxAmplitude = new float[16];
		this.amplitudeFollower = new float[16];
		
		for(int i = 0; i < this.input.getNumInputs(); i++)
		{
			this.maxAmplitude[i] = 100;
			this.amplitudeFollower[i] = 0;
		}
	} // constructor

	/**
	 * Adds the Landing Menu by setting the label of the default tab.
	 */
	public void addLandingMenu()
	{
		this.controlP5.getTab("default")
		.setLabel("Landing\nMenu")
		.setWidth(50)
		.setHeight(this.tabHeight)
		.activateEvent(true)
		.getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);

		this.addHideButtons(this.controllerXVals[0], this.textYVals[1]);

		this.addInputNumSelect(this.controllerXVals[0], textYVals[5]);
		this.addInputSelect(this.controllerXVals[0], textYVals[4]);
	} // addLandingMenu

	/**
	 * Adds the Attack/Release/Transition Sliders, Piano and Forte Threshold Sliders,
	 * Saturation/Brightness Percent Sliders,
	 * and - optionally - the Key Select and Guide Tone Popout.
	 * 
	 * @param pitch		if true, includes the Key Select and Guide Tone Popout
	 */
	public void addSensitivityMenu(boolean pitch)
	{
		this.controlP5.addTab("sensitivity")
		.setLabel("Sensitivity\nMenu")
		.setWidth(50)
		.setHeight(this.tabHeight)
		.activateEvent(true)
		.getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);

		this.addARTSliders(this.controllerXVals[0], this.textYVals[1], this.textYVals[2], this.textYVals[3]);
		this.addPianoThresholdSlider(this.controllerXVals[0], this.textYVals[5]);
		this.addForteThresholdSlider(this.controllerXVals[0], this.textYVals[6]);

		this.addThresholdSliders(this.controllerXVals[0], this.textYVals[8], this.spacer);

		if(pitch)
		{
			this.addGuideTonePopout(controllerXVals[1], textYVals[2]);
			this.addKeySelector(controllerXVals[1], textYVals[2]);
			this.setCurKey("A", 2);
		}
	} // addSensitivityMenu

	/**
	 * Adds the normal, 12-note Color Menu, with "Canvas"/"Tonic"/etc. special colors and no range segments.
	 */
	public void addColorMenu()
	{
		String[] specialColors	= new String[] {
				"Canvas", "Tonic", "2nd Color", "3rd Color"
		}; // buttonLabels
		this.addColorMenu(this.noteNames, 3, false, specialColors, true, true, null, 0, 0, "color");
	} // addColorMenu()

	/**
	 * Adds the Color Select CWs, Special Colors CWs (optional), Color Style Buttons (optional),
	 * and Hue/Saturation/Brightness and Red/Green/Blue modulate Sliders
	 * 
	 * @param colorSelectLabels	String[] with text for each of the color select Buttons' labels
	 * @param numColorSelectRows	int indicating the number of rows of ColorSelect Buttons
	 * @param colorSelectCanvas		boolean indicating whether the Button for canvas is included with colorSelect
	 * @param specialColorLabels	String[] with text for each of the special colors Buttons' labels;
	 * 								if null, special colors Buttons will not be added
	 * @param specialColorCanvas		boolean indicating whether the Button for canvas is included with specialColors
	 * @param colorStyles	boolean indicating whether or not to include the color style Buttons
	 * @param rangeSegmentsLabel	label text for range segments group; if null, range segments will not be added
	 * @param maxRangeSegments	the maximum number of range segments (will be ignored if previous String param is null)
	 * @param defaultRangeSegments the default number of range segments (will be ignored if previous String param is null)
	 * @param tabName	String indicating to which Tab this group of Controllers should be added
	 * 					(for no Tab, use "default")
	 */
	public void addColorMenu(String[] colorSelectLabels, int numColorSelectRows, boolean colorSelectCanvas, String[] specialColorLabels, boolean specialColorCanvas, boolean colorStyles, String rangeSegmentsLabel, int maxRangeSegments, int defaultRangeSegments, String tabName)
	{
		if(numColorSelectRows == 0)
		{
			numColorSelectRows	= 1;
			System.err.print("ModuleMenu.addColorMenu: numColorSelectRows param was 0; changed to 1.");
		}

		this.controlP5.addTab(tabName)
		.setLabel(tabName + "\nMenu")
		.setWidth(50)
		.setHeight(this.tabHeight)
		.activateEvent(true)
		.getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);

		int[]	colorSelectYVals	= new int[numColorSelectRows];
		for(int i = 0; i < numColorSelectRows; i++)
		{
			colorSelectYVals[i]	= this.textYVals[(10 + i)];
		}

		// Adding ColorSelect first since everything to do with colors depends on that:
		this.addColorSelect(this.controllerXVals[0], colorSelectYVals, colorSelectLabels, "Custom Pitch\nColor Select", colorSelectCanvas, tabName);

		// ColorSelect and ColorStyle added out of order so that the 2nd Color
		// and 3rd Color select buttons will exist for the Rainbow ColorStyle
		// to lock them.
		//		this.addColorSelectButtons(textYVals[14]);

		if(specialColorLabels != null)
		{
			this.addSpecialColors(this.controllerXVals[0], this.textYVals[8], specialColorLabels, "Color Select", specialColorCanvas, tabName);
		}

		if(colorStyles)
		{
			// addColorStyleButtons will set the colorStyle to rainbow() first:
			this.addColorStyleButtons(this.controllerXVals[0], this.textYVals[6], tabName);			
		}

		this.addAlphaSlider(this.controllerXVals[0], this.textYVals[14], tabName);
		this.addSaveLoadColorsButtons(this.controllerXVals[0], this.textYVals[4], tabName);

		if(rangeSegmentsLabel != null)
		{
			this.addRangeSegments(this.controllerXVals[0], this.textYVals[16], maxRangeSegments, defaultRangeSegments, rangeSegmentsLabel, tabName);
		}

		// Add Hue/Saturation/Brightness and Red/Green/Blue modulate Sliders:
		this.addHSBSliders(this.controllerXVals[1], new int[] { this.textYVals[1], this.textYVals[2], this.textYVals[3] }, tabName);
		this.addModulateSliders(this.controllerXVals[2], new int[] { this.textYVals[1], this.textYVals[2], this.textYVals[3] }, tabName);

	} // addColorMenu

	public void addShapeMenu(int numShapes)
	{
		this.controlP5.addTab("shape")
		.setLabel("Shape\nMenu")
		.setWidth(50)
		.setHeight(this.tabHeight)
		.activateEvent(true)
		.getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);

		Shape[] shapes = new Shape[numShapes];

		for(int i = 0; i < shapes.length; i++)
		{
			shapes[i] = new Shape(this.module);

			for(int j = 0; j < 5; j++)
			{
				shapes[i].setShapeIndex(j);
				shapes[i].setCurrentShape("supershape", this.superShapes[j]);
			} // for - j
		} // for - i

		this.shapeEditor	= new ShapeEditor(this.module, shapes, this.module, 925, 520, this.controlP5);
		System.out.println("ModuleMenu.addShapeMenu: this.shapeEditor = " + this.shapeEditor);

		//		this.shapeEditor.setIsRunning(false);
		//		this.shapeEditor.getControlP5().getController("shapeSelect").setVisible(false);
		this.getShapeEditor().updateSliders();
	} // addShapeMenu

	public void hideSensitivityMenu()
	{
		this.controlP5.getTab("sensitivity").hide();
	} // hideSensitivityMenu

	public void hideColorMenu()
	{
		this.controlP5.getTab("color").hide();
	} // hideColorMenu

	public void hideColorMenu(String tabName)
	{
		if(this.controlP5.getTab(tabName) == null)
		{
			throw new IllegalArgumentException("ModuleMenu.hideColorMenu: no Tab with the name " + tabName);
		}
		this.controlP5.getTab(tabName).hide();
	} // hideColorMenu(String)

	public void hideShapeMenu()
	{
		this.controlP5.getTab("shape").hide();
	}

	public void showColorMenu()
	{
		this.controlP5.getTab("color").show();
	} // showColorMenu

	public void showColorMenu(String tabName)
	{
		if(this.controlP5.getTab(tabName) == null)
		{
			throw new IllegalArgumentException("ModuleMenu.showColorMenu: no Tab with the name " + tabName);
		}
		this.controlP5.getTab(tabName).show();
	} // hideColorMenu(String)

	public void showShapeMenu()
	{
		this.controlP5.getTab("shape").show();
	}

	/**
	 * Adds the ScrollableList that will let the user choose between Menu's
	 */
	public void addMenuList()
	{
		// Add the Menu List:
		this.controlP5.addScrollableList("menuList", this.leftAlign, 13, 150, 450)
		.setBarHeight(25)
		.setItemHeight(20)
		.bringToFront()
		.close();
	}


	/**
	 * Adds the Buttons, ColorWheels and Textfields for selecting custom colors.
	 * 
	 * @param yVals	yValue(s) for the row or rows of Buttons
	 * @param buttonLabels	String[] of labels for the color select Buttons; 
	 * 						length of this array determines the number of colorSelect Buttons;
	 * 						if including canvas in this row, the first element of this array must be "Canvas"
	 * @param tabName	String indicating to which Tab this group of Controllers should be added
	 * 					(for no Tab, use "default")
	 */
	public void addColorSelect(int xVal, int[] yVals, String[] buttonLabels, String labelText, boolean canvas, String tabName)
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
		// Warn the user that the first String in buttonLabels will be canvas color (but only if they didn't title it "canvas"):
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

		int		buttonsPerRow	= Math.max((buttonLabels.length) / yVals.length, 1);	// Math.max keeps it from being 0
		// the "- (10 / buttonsPerRow)" adds [this.rightEdgeSpacer pixels] at the end of the row:
		int		buttonWidth		= ((this.sidebarWidth - this.leftAlign - this.rightEdgeSpacer) / buttonsPerRow) - this.spacer;

		int[]	xVals	= new int[buttonsPerRow];

		for(int i = 0; i < xVals.length; i++)
		{
			xVals[i]	= xVal + this.leftAlign + ((buttonWidth + this.spacer) * i);
		}

		this.controlP5.addTextlabel("colorSelectLabel")
		.setPosition(xVal + labelX, yVals[0] + 4)
		.moveTo(tabName)
		.setValue(labelText);

		// Loop through all
		for(int i = 0; i < yVals.length; i++)
		{
			// Loop through each row
			for(int j = 0; j < xVals.length; j++)
			{
				if(i == 0 && j == 0 && canvas)
				{
					System.out.println("j = " + j + "; xVals.length = " + xVals.length + "; i = " + i + "; yVals.length = " + yVals.length + "; buttonLabelPos = " + buttonLabelPos + "; buttonLabels.length = " + buttonLabels.length);
					this.addColorWheelGroup(xVals[j], yVals[i], buttonWidth, buttonLabels[buttonLabelPos], this.canvasColor, tabName);
				} else {
					System.out.println("colorSelectPos = " + colorSelectPos + "; colorSelect.length = " + colorSelect.length + "; j = " + j + "; xVals.length = " + xVals.length + "; i = " + i + "; yVals.length = " + yVals.length + "; buttonLabelPos = " + buttonLabelPos + "; buttonLabels.length = " + buttonLabels.length);
					this.colorSelect[colorSelectPos]	= (ColorWheel)(this.addColorWheelGroup(xVals[j], yVals[i], buttonWidth, buttonLabels[buttonLabelPos], this.colors[this.currentInput][colorSelectPos], tabName))[1];
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
		.moveTo("global")	// "global" means it will show in all tabs
		.updateSize()
		.bringToFront();

		//		this.menuWidth = this.controlP5.getController("menuX").getWidth();
	} // addOutsideButtons


	/**
	 * Adds "hide buttons" - hide Menu, hide Play Button, and hide Legend
	 * TODO: maybe shouldn't add them to the LandingMenu Tab every time?
	 * 
	 * @param hideY	y value at which the row will be displayed
	 */
	public void addHideButtons(int xVal, int hideY)
	{
		int	hideWidth   = ( ( this.sidebarWidth - this.leftAlign - this.rightEdgeSpacer) / 3 ) - this.spacer;

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
		.setValue("Hide");

		for(int i = 0; i < names.length; i++)
		{
			this.controlP5.addToggle(names[i])
			.setPosition(xVals[i], hideY)
			.setWidth(hideWidth);
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
			this.addSliderGroup(xVal, yVals[i], labels[i], 100, 3000, 400, "sensitivity");
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
		this.addSliderGroup(xVal, yVal, "Piano \nThreshold", 2, 100, 10, "sensitivity");
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
		this.addSliderGroup(xVal, yVal, "Forte\nThreshold", this.minThreshold, 7000, this.forteThreshold[0], "sensitivity");
	} // addForteThresholdSlider


	/**
	 * Method called during instantiation to initialize the key selector drop-down menu (ScrollableList)
	 * and major/minor/chromatic selection buttons.
	 * 
	 * @param keyY	y value of the menu and buttons.
	 */
	public void addKeySelector(int xVal, int keyY)
	{
		int	listWidth		= 25;

		int	buttonWidth		= 50;
		int	toggleWidth		= ((this.sidebarWidth - this.leftAlign - listWidth - (this.spacer * 2) - this.rightEdgeSpacer - buttonWidth) / 3 ) - this.spacer;
		int	majorX			= xVal + this.leftAlign + listWidth + spacer;
		int	minorX			= xVal + this.leftAlign + listWidth + spacer + (toggleWidth + spacer);
		int	chromX			= xVal + this.leftAlign + listWidth + spacer + ((toggleWidth + spacer) * 2);
		int	guideToneX		= xVal + this.leftAlign + listWidth + spacer + ((toggleWidth + spacer) * 3);


		// "Key" Textlabel
		this.controlP5.addTextlabel("key")
		.setPosition(xVal + this.labelX, keyY + 4)
		.setValue("Key")
		.moveTo("sensitivity");


		// "Letter" drop-down menu (better name?)
		this.controlP5.addScrollableList("keyDropdown")
		.setPosition(xVal + this.leftAlign, keyY)
		.setWidth(listWidth)
		.setBarHeight(18)
		.setItems(this.allNotes)
		.setOpen(false)
		.setValue(0f)
		.moveTo("sensitivity")
		.getCaptionLabel().toUpperCase(false);

		// Maj/Min/Chrom Toggles
		// (These each have an internalValue - 0 = Major, 1 = Minor, and 2 = Chromatic - 
		//  and will set this.majMinChrom to their value when clicked.)
		this.controlP5.addToggle("major")
		.setPosition(majorX, keyY)
		.setWidth(toggleWidth)
		.setCaptionLabel("Major")
		.moveTo("sensitivity")
		.setInternalValue(0);
		this.controlP5.getController("major").getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);

		this.controlP5.addToggle("minor")
		.setPosition(minorX, keyY)
		.setWidth(toggleWidth)
		.setCaptionLabel("Minor")
		.moveTo("sensitivity")
		.setInternalValue(1);
		this.controlP5.getController("minor").getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);

		this.controlP5.addToggle("chrom")
		.setPosition(chromX, keyY)
		.setWidth(toggleWidth)
		.setCaptionLabel("Chrom.")
		.moveTo("sensitivity")
		.setInternalValue(2);
		this.controlP5.getController("chrom").getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);

		((Toggle)(this.controlP5.getController("chrom"))).setState(true);

		// Guide Tone pop-out Button:
		this.controlP5.addToggle("guideToneButton")
		.setPosition(guideToneX, keyY)
		.setWidth(buttonWidth)
		.setCaptionLabel("Guide Tones")
		.moveTo("sensitivity")
		.setValue(false);
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

		int		listSliderX		= /*xVal + */(popoutSpacer * 2) + labelWidth;

		int		rangeY			= popoutSpacer;
		int		adsrY			= (popoutSpacer * 2) + height;
		int		bpmY			= (popoutSpacer * 3) + (height * 2);
		int		volumeY			= (popoutSpacer * 4) + (height * 3);

		this.controlP5.addGroup("guideToneBackground")
		.setPosition(xVal + this.leftAlign, guideToneY + 20)
		.setSize(boxWidth, boxHeight)
		.setBackgroundColor(transBlackInt)
		.setVisible(false)
		.moveTo("sensitivity")	// Add the Group to this Tab and all the Controllers will come, too.
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
			this.addSliderGroup(/*xVal*/0, yVals[i], labelVals[i], listSliderX, sliderWidth, ranges[i][0], ranges[i][1], startingVals[i], textfieldWidth, "sensitivity");
			// Since addSliderGroup no longer allows one to specify the group of the Controllers,
			// do that manually, below:
			this.controlP5.getController("label" + (this.nextSliderId - 1)).setGroup("guideToneBackground");
			this.controlP5.getController("slider" + (this.nextSliderId - 1)).setGroup("guideToneBackground");
			this.controlP5.getController("textfield" + (this.nextSTextfieldId - 1)).setGroup("guideToneBackground");
		} // for

		// "ADSR Presets" Textlabel
		this.controlP5.addTextlabel("adsrPresets")
		.setPosition(/*xVal + */popoutSpacer, adsrY)
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
		.setPosition(/*xVal + */popoutSpacer, rangeY + 4)
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
			this.addSliderGroup(xVal, hsb[i], values[i], -1, 1, 0, "color");
		} // for   
	} // addHSBSliders

	/**
	 * Adds the hue, saturation, and brightness modulate sliders
	 * 
	 * @param xVal 	x value for the leftmost edge of this group
	 * @param hsb	array of y values for each slider
	 * @param tabName	String indicating to which Tab this group of Controllers should be added
	 * 					(for no Tab, use "default")
	 */
	public void addHSBSliders(int xVal, int[] hsb, String tabName)
	{
		String[]	values	= new String[] { "Hue", "Saturation", "Brightness" };

		this.firstHSBSliderId	= this.nextSliderId;

		for(int i = 0; i < hsb.length; i++)
		{
			this.addSliderGroup(xVal, hsb[i], values[i], -1, 1, 0, tabName);
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
			this.addSliderGroup(xVal, modulateYVals[i], values[i], -255, 255, 0, "color");
		} // for
	} // addModulateSliders

	/**
	 * Method called during instantiation, to initialize the RGB color modulate sliders.
	 * 
	 * @param xVal 	x value for the leftmost edge of this group
	 * @param modulateYVals	int[] of the y values of the red, green, and blue sliders, respectively.
	 * @param tabName	String indicating to which Tab this group of Controllers should be added
	 * 					(for no Tab, use "default")
	 */
	public void addModulateSliders(int xVal, int[] modulateYVals, String tabName)
	{
		this.redGreenBlueMod		 	= new float[3];

		String[]	values	= new String[] { "Red Modulate", "Green Mod.", "Blue Modulate" };

		this.firstRGBSliderId	= this.nextSliderId;
		for(int i = 0; i < modulateYVals.length; i++)
		{
			this.addSliderGroup(xVal, modulateYVals[i], values[i], -255, 255, 0, tabName);
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
		// TODO - does this Slider affect everyone or just the first Shape size?
		this.shapeSize[0]			= 1;
		this.shapeSizeSliderId	= this.nextSliderId;

		this.addSliderGroup(xVal, yVal, "Shape Size", 0.01f, 10, this.shapeSize[0]);
	} // addShapeSizeSlider


	/**
	 * Adds the Buttons for selecting the number of range segments.
	 * 
	 * @param xVal 	x value for the leftmost edge of this group
	 * @param yVal	y value for the row of Buttons
	 * @param numSegments	total number of segments (current number of segments will be set to this total)
	 * @param label	text to display on the label at the beginning of the row
	 * @param tabName	String indicating to which Tab this group of Controllers should be added
	 * 					(for no Tab, use "default")
	 */
	public void addRangeSegments(int xVal, int yVal, int numSegments, String label, String tabName)
	{
		this.addRangeSegments(xVal, yVal, numSegments, numSegments, label, tabName);
	}  // addRangeSegments(int, int, String)


	/**
	 * Adds the Buttons for selecting the number of range segments.
	 * 
	 * @param yVal	y value for the row of Buttons
	 * @param numSegments	total number of range segments
	 * @param defaultNumSegments	number of segments that are set as current at the beginning
	 * @param label	text to display on the label at the beginning of the row
	 * @param tabName	String indicating to which Tab this group of Controllers should be added
	 * 					(for no Tab, use "default")
	 */
	public void addRangeSegments(int xVal, int yVal, int numSegments, int defaultNumSegments, String label, String tabName)
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
		.setValue(label)
		.moveTo(tabName);

		this.firstRangeSegmentsId	= this.nextToggleId;

		for(int i = 0; i < numSegments; i++)
		{
			this.controlP5.addToggle("toggle" + this.nextToggleId)
			.setPosition(xVals[i], yVal)
			.setWidth(toggleWidth)
			.setLabel((i + 1) + "")
			.moveTo(tabName)
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
				this.addSliderGroup(xVal, yVal + (i * (verticalSpacer + this.sliderHeight)), labels[i], 0, 1, 0.7f, "sensitivity");

			} // if - Forte Thresholds

			// Percent Sliders
			if(i % 2 == 0)
			{
				this.controlP5.addLabel(names[i])
				.setPosition(xVal + this.labelX, yVal + (i * (verticalSpacer + this.sliderHeight)) + 4)
				.setValue(labels[i])
				.moveTo("sensitivity");

				this.controlP5.addSlider("slider" + this.nextSliderId)
				.setPosition(xVal + this.leftAlign, (yVal + (i * (verticalSpacer + this.sliderHeight))))
				.setSize(this.sliderWidth + this.spacer + this.textfieldWidth, this.sliderHeight)
				.setRange(-1, 1)
				.setValue(0)
				.setId(this.nextSliderId)
				.moveTo("sensitivity")
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
	 * @param tabName	String indicating to which Tab this group of Controllers should be added
	 * 					(for no Tab, use "default")
	 */
	public void addSpecialColors(int xVal, int yVal, String[] buttonLabels, String labelText, boolean canvas, String tabName)
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
		.moveTo(tabName)
		.setValue(labelText);

		// Loop through all
		for(int i = 0; i < xVals.length; i++)
		{
			if(canvas && i == 0)
			{
				this.addColorWheelGroup(xVals[i], yVal, buttonWidth, buttonLabels[i], this.canvasColor, tabName);
			} else {
				int	thisColorPos	= this.specialColorsPos[0][i - 1];
				this.addColorWheelGroup(xVals[i], yVal, buttonWidth, buttonLabels[i], new Color(this.colorSelect[thisColorPos].getRGB()), tabName);
			}
		} // for - i

		this.fillHSBColors();
	} // addSpecialColors


	/**
	 * Method called during instantiation to initialize the color style Toggles
	 * (Rainbow, Dichromatic, Trichromatic, and Custom).
	 * 
	 * @param xVal 	x value for the leftmost edge of this group
	 * @param colorStyleY	y value of the colorStyle Toggles
	 * @param tabName	String indicating to which Tab this group of Controllers should be added
	 * 					(for no Tab, use "default")
	 */
	public void addColorStyleButtons(int xVal, int colorStyleY, String tabName)
	{
		int	colorStyleWidth	= ((this.sidebarWidth - this.leftAlign - this.rightEdgeSpacer) / 4) - this.spacer;

		System.out.println("ColorStyle: colorStyleWidth = " + colorStyleWidth);

		int rainbowX     	= xVal + this.leftAlign;
		int dichromaticX	= xVal + this.leftAlign + colorStyleWidth + this.spacer;
		int trichromaticX	= xVal + this.leftAlign + (colorStyleWidth + this.spacer) * 2;
		int customX			= xVal + this.leftAlign + (colorStyleWidth + this.spacer) * 3;

		this.controlP5.addTextlabel("colorStyle")
		.setPosition(xVal + this.labelX, colorStyleY + 4)
		.moveTo(tabName)
		.setValue("Color Style");

		this.controlP5.addToggle("rainbow")
		.setPosition(rainbowX, colorStyleY)
		.setWidth(colorStyleWidth)
		.setCaptionLabel("Rainbow")
		.moveTo(tabName)
		.setInternalValue(ModuleMenu.CS_RAINBOW);
		this.controlP5.getController("rainbow").getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);

		this.controlP5.addToggle("dichrom")
		.setPosition(dichromaticX, colorStyleY)
		.setWidth(colorStyleWidth)
		.setCaptionLabel("Dichrom.")
		.moveTo(tabName)
		.setInternalValue(ModuleMenu.CS_DICHROM);
		this.controlP5.getController("dichrom").getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);

		this.controlP5.addToggle("trichrom")
		.setPosition(trichromaticX, colorStyleY)
		.setWidth(colorStyleWidth)
		.setCaptionLabel("Trichrom.")
		.moveTo(tabName)
		.setInternalValue(ModuleMenu.CS_TRICHROM);
		this.controlP5.getController("trichrom").getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);

		this.controlP5.addToggle("custom")
		.setPosition(customX, colorStyleY)
		.setWidth(colorStyleWidth)
		.setCaptionLabel("Custom")
		.moveTo(tabName)
		.setInternalValue(ModuleMenu.CS_CUSTOM);
		this.controlP5.getController("custom").getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);

		//	this.setColorStyle(ModuleMenu.CS_RAINBOW);
		this.controlP5.getController("rainbow").update();
	} // addColorStyleButtons


	public void addInputSelect(int xVal, int yVal)
	{
		int	toggleWidth	= 60;
		int	listWidth	= this.sidebarWidth - this.leftAlign - toggleWidth - this.spacer - this.rightEdgeSpacer;

		this.controlP5.addLabel("inputLabel")
		.setPosition(xVal + this.labelX, yVal + 4)
		.setStringValue("Input:");

		String[]	listItems	= new String[this.module.getTotalNumInputs()];

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
		.bringToFront()
		.getCaptionLabel().toUpperCase(false);
	} // addInputSelect


	public void addInputNumSelect(int xVal, int yVal)
	{
		// Add input number selection:
		String[]	numInputItems	= new String[this.module.getTotalNumInputs()];
		for(int i = 0; i < numInputItems.length; i++)
		{
			numInputItems[i]	= ((i + 1) + "");
		} // for

		this.controlP5.addScrollableList("numInputsList")
		.setPosition(xVal + this.leftAlign, yVal)
		.setItems(numInputItems)
		.setOpen(false)
		.bringToFront()
		.setCaptionLabel("Select number of inputs")
		.getCaptionLabel().toUpperCase(false);
	} // addInputNumSelect


	public void addAlphaSlider(int xVal, int yVal, String tabName)
	{
		this.alphaSliderId	= this.nextSliderId;
		this.addSliderGroup(xVal, yVal, "Alpha", 0, 255, 255, tabName);
	} // addAlphaSlider

	public void addSaveLoadColorsButtons(int xVal, int yVal, String tabName)
	{
		int	buttonWidth	= ((this.sidebarWidth - this.leftAlign) / 2) - this.spacer;
		
		this.controlP5.addButton("saveColors")
		.setPosition(this.leftAlign + xVal, yVal)
		.setWidth(buttonWidth)
		.moveTo(tabName)
		.setLabel("Save Colors");
		
		this.controlP5.addButton("loadColors")
		.setPosition(this.leftAlign + xVal + this.spacer + buttonWidth, yVal)
		.setWidth(buttonWidth)
		.moveTo(tabName)
		.setLabel("Load Colors");
	} // addSaveColorsButton


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
						this.goalHue[i][j]	= this.canvasColor[j];
					} // for - canvas
				} // for - i
			} else {
				for(int i = 0; i < this.goalHue.length; i++)
				{
					this.goalHue[this.currentInput][i]	= this.canvasColor[i];
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

	public void universalFade(int fadeThis)
	{
		for(int i = 0; i < 10; i++)
		{
			fadeThis++;
		} // for
	} // universalFade

	/**
	 * Takes the values of curHue from its current values to the values in goalHue
	 * over the time that is designated by the attack, release, and transition sliders
	 * 
	 * @param position	position in colors to which curHue should fade
	 */
	public void fadeColor(int position, int inputNum)
	{
		this.inputNumErrorCheck(inputNum);

		if(position > this.colorSelect.length || position < -1) {
			throw new IllegalArgumentException("ModuleTemplate.fadeColor: position " + position + 
					" is out of bounds; must be between -1 (signifying canvas color) or " + (this.colorSelect.length - 1));
		} // error checking

		float	curAmp;
		if(!this.recInputPlaying)
		{
			curAmp = this.input.getAmplitude(inputNum);
		} else {
			curAmp	= this.recInput.getAmplitude(inputNum);
		}

		if(curAmp < this.pianoThreshold[inputNum])	
		{
			this.nowBelow[inputNum]	= true;

			for(int i = 0; i < this.goalHue[inputNum].length; i++)
			{
				this.goalHue[inputNum][i]	= this.canvasColor[i];
			} // for - canvas

		} else {
			this.nowBelow[inputNum]	= false;

			this.goalHue[inputNum]	= this.applyThresholdSBModulate(curAmp, inputNum, position);
		} // else

		if(this.colorCheckpoint[inputNum] < this.parent.millis())
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

			this.colorCheckpoint[inputNum] = (this.parent.millis() + 50);
		} // if - adding every 50 millis

		/*
		System.out.println("curHue: " + this.curHue[0][0] + ", "
				+ this.curHue[0][1] + ", "
				+ this.curHue[0][2]);
		System.out.println("goalHue: " + this.goalHue[0][0] + ", "
						+ this.goalHue[0][1] + ", "
						+ this.goalHue[0][2]);

		System.out.println("input.getAmplitude() = " + input.getAmplitude());

		System.out.println("colorAdd: " + this.colorAdd[0][0] + ", "
				+ this.colorAdd[0][1] + ", "
				+ this.colorAdd[0][2]);
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

	} // fadeColor
	
	/**
	 * this method updates the amplitude follower and needs to be called every time in the draw loop
	 * for each input that is using it.
	 * 
	 * @param numInput:  Controls the input that is updated
	 * @param followerType:  Controls the style of amplitude follower that is implemented
	 */
	@SuppressWarnings("unused")
	public void updateAmplitudeFollower(int numInput, int followerType)
	{
		if(numInput >= this.module.curNumInputs)
		{
			followerType = 0;
			//System.out.println("WARNING: update amplitude follower was called on a null input");
		}
		
		//this variation of the amplitude follower always moves the follower half way from the
		//current amplitudeFollower value, to the value returned by input.getAmplitude
		if(followerType == 1)
		{
			if(!this.recInputPlaying)
			{
				this.amplitudeFollower[numInput] = (this.amplitudeFollower[numInput] + this.input.getAmplitude(numInput)) / 2;
			} else {
				this.amplitudeFollower[numInput] = (this.amplitudeFollower[numInput] + this.recInput.getAmplitude(numInput)) / 2;
			}
		}
		
		//this variation of the amplitude follower always moves the follower half way from the
		//current amplitudeFollower value, to the value returned by input.getAmplitude
		//but there is also a maxIncrament value that puts a limit on how much the amplitudeFollower
		//value can change in one increment
		if(followerType == 2)
		{
			float	amp;
			if(!this.recInputPlaying)
			{
				amp = this.input.getAmplitude(numInput);
			} else {
				amp	= this.recInput.getAmplitude(numInput);
			}
			
			if(this.maxAmplitude[numInput] < amp)
			{
				this.maxAmplitude[numInput] = amp;
			}
			
			float maxIncrament = this.maxAmplitude[numInput]/30;
			float incrament = (amp - this.amplitudeFollower[numInput])/2;
			
			if(Math.abs(incrament) < maxIncrament)
			{
				this.amplitudeFollower[numInput] = this.amplitudeFollower[numInput] + incrament;
			}
			else
			{
				this.amplitudeFollower[numInput] = this.amplitudeFollower[numInput] + (maxIncrament * (incrament/Math.abs(incrament)));
			}
			
			if(this.maxAmplitude[numInput] < amp)
			{
				this.maxAmplitude[numInput] = amp;
			}
		}
		
		/*
		 * jumps to a higher amplitude immediately, has a maximum increment when decreasing
		 */
		if(followerType == 3)
		{
			float amp = this.input.getAmplitude();
			
			if(amp > this.amplitudeFollower[numInput])
			{
				this.amplitudeFollower[numInput] = amp;
			}
			else
			{
				float maxIncramentDown = 10;
				
				if(this.amplitudeFollower[numInput] - amp > maxIncramentDown)
				{
					this.amplitudeFollower[numInput] = this.amplitudeFollower[numInput] - maxIncramentDown;
				}
				else
				{
					this.amplitudeFollower[numInput] = amp;
				}
			}
			
			if(amp > this.maxAmplitude[numInput])
			{
				this.maxAmplitude[numInput] = amp;
			}
		}
		
	}
	
	public float getAmplitudeFollower(int numInput)
	{
		return this.amplitudeFollower[numInput];
	}
	
	
	/**
	 * Takes the values of curHue from its current values to the values in goalHue
	 * over the time that is designated by the attack, release, and transition sliders
	 * 
	 * @param position	position in colors to which curHue should fade
	 */
	public void fadeShapeSize(int position, int inputNum)
	{
		this.inputNumErrorCheck(inputNum);

		if(position > this.colorSelect.length || position < -1) {
			throw new IllegalArgumentException("ModuleTemplate.fadeColor: position " + position + 
					" is out of bounds; must be between -1 (signifying canvas color) or " + (this.colorSelect.length - 1));
		} // error checking

		float	curAmp;
		if(!this.recInputPlaying)
		{
			curAmp = this.input.getAmplitude(inputNum);
		} else {
			curAmp	= this.recInput.getAmplitude(inputNum);
		}

		if(curAmp < this.pianoThreshold[inputNum])	
		{
			this.nowBelow[inputNum]	= true;

			/*
			for(int i = 0; i < this.goalHue[inputNum].length; i++)
			{
				this.goalHue[inputNum][i]	= this.canvasColor[i];
			} // for - canvas
*/
		} else {
			this.nowBelow[inputNum]	= false;
/*
			this.goalHue[inputNum]	= this.applyThresholdSBModulate(curAmp, inputNum, position);
			*/
		} // else

		if(this.shapeSizeCheckpoint[inputNum] < this.parent.millis())
		{
			for(int i = 0; i < 3; i++)
			{				
				// if the current hue is less than the goalHue - the colorAdd, then add colorAdd:
				if(this.curShapeSize[inputNum] < this.goalShapeSize[inputNum] - (this.shapeSizeAdd[inputNum] / 2))
				{
					this.curShapeSize[inputNum]	=	this.curShapeSize[inputNum] + this.shapeSizeAdd[inputNum];
				} else 
					// otherwise, if it's more than the goal Hue, even after adding half of colorAdd, then subtract:
					if(this.curShapeSize[inputNum] > this.goalShapeSize[inputNum] + (this.shapeSizeAdd[inputNum] / 2))
					{
						this.curShapeSize[inputNum]	=	this.curShapeSize[inputNum] - this.shapeSizeAdd[inputNum];
					}
			} // for - i

			this.shapeSizeCheckpoint[inputNum] = (this.parent.millis() + 50);
		} // if - adding every 50 millis

		/*
		System.out.println("curHue: " + this.curHue[0][0] + ", "
				+ this.curHue[0][1] + ", "
				+ this.curHue[0][2]);
		System.out.println("goalHue: " + this.goalHue[0][0] + ", "
						+ this.goalHue[0][1] + ", "
						+ this.goalHue[0][2]);

		System.out.println("input.getAmplitude() = " + input.getAmplitude());

		System.out.println("colorAdd: " + this.colorAdd[0][0] + ", "
				+ this.colorAdd[0][1] + ", "
				+ this.colorAdd[0][2]);
		 */

		float	lowBound;
		float	highBound;

		for (int i = 0; i < 3; i++)
		{
			lowBound	= this.goalShapeSize[inputNum] - 5;
			highBound	= this.goalShapeSize[inputNum] + 5;

			// Now check colors for whether they have moved into the boundaries:
			if(this.curShapeSize[inputNum] < highBound && this.curShapeSize[inputNum] > lowBound) {
				// if here, color has been reached.
				this.shapeSizeReached[inputNum]	= true;
			} else {
				this.shapeSizeReached[inputNum]	= false;
			}
		} // for

		// If all elements of the color are in range, then the color has been reached:
//		this.colorReached[inputNum]	= this.colorReachedArray[inputNum][0] && this.colorReachedArray[inputNum][1] && this.colorReachedArray[inputNum][2];

		// If coming from a low amplitude note and not yet reaching a color,
		// use the attack value to control the color change:
		if(!this.nowBelow[inputNum] && !shapeSizeReached[inputNum]) 
		{	
			this.attRelTranPos[inputNum]	= 0;
			//			System.out.println("	attack!!!!");
		} else if(!this.nowBelow[inputNum] && shapeSizeReached[inputNum]) {
			// Or, if coming from one super-threshold note to another, use the transition value:
			this.attRelTranPos[inputNum]	= 2;
			//			System.out.println("	transition.... transition [doooooo do dooo do do ] - transition!");
		} else if(this.nowBelow[inputNum]) {
			// Or, if volume fell below the threshold, switch to release value:
			this.attRelTranPos[inputNum]	= 1;
			//			System.out.println("	re....lent! re...coil! re...verse!");
		}

		// Calculate color ranges:
//		for(int i = 0; i < this.curShapeSize[inputNum].length; i++)
//		{
			this.shapeSizeRange[inputNum]	= Math.abs(this.goalShapeSize[inputNum] - this.curShapeSize[inputNum]);

			// divide the attack/release/transition value by 50
			// and divide colorRange by that value to find the amount to add each 50 millis.
			float addThis = (int)(this.shapeSizeRange[inputNum] / (this.attRelTranVals[inputNum][this.attRelTranPos[inputNum]] / 50));

			this.shapeSizeAdd[inputNum]	= (int)addThis;	
//		} // for

	} // fadeAmp
	
	/**
	 * In progress; will eventually allow shape size to change smoothly over time,
	 * like fade() does to color, rather than jumping as it currently does. (Mar. 2018)
	 * 
	 * @param inputNum
	 */
	public void smoothAmpScale(/*int curAmp,*/int inputNum)
	{
		this.inputNumErrorCheck(inputNum);

		float	curAmp;
		if(!this.recInputPlaying)
		{
			curAmp = this.input.getAmplitude(inputNum);
		} else {
			curAmp	= this.recInput.getAmplitude(inputNum);
		}

		if(curAmp < this.pianoThreshold[inputNum])	
		{
			this.goalAmpScale[inputNum]	= 0;
		} else {
			this.goalAmpScale[inputNum]	= curAmp / this.forteThreshold[inputNum];
		} // else

		if(this.ampCheckpoint[inputNum] < this.parent.millis())
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

			this.ampCheckpoint[inputNum] = (this.parent.millis() + 50);
		} // if - adding every 50 millis

		/*
		System.out.println("curHue: " + this.curHue[0][0] + ", "
				+ this.curHue[0][1] + ", "
				+ this.curHue[0][2]);
		System.out.println("goalHue: " + this.goalHue[0][0] + ", "
						+ this.goalHue[0][1] + ", "
						+ this.goalHue[0][2]);

		System.out.println("input.getAmplitude() = " + input.getAmplitude());

		System.out.println("colorAdd: " + this.colorAdd[0][0] + ", "
				+ this.colorAdd[0][1] + ", "
				+ this.colorAdd[0][2]);
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

	} // smoothAmpScale


	/**
	 * Converts the given color to HSB and sends it to dichromatic_OneHSB.
	 * (dichromatic_OneHSB will send it to _TwoHSB, which will set this.colors, changing the scale.)
	 * 
	 * @param rgbVals	float[] of RGB values defining the color for the tonic of the scale.
	 */
	public void dichromatic_OneRGB(int[] rgbVals, int inputNum)
	{
		if(rgbVals == null) {
			throw new IllegalArgumentException("Module_01_02.dichromatic_OneRGB: int[] parameter is null.");
		}

		float[]	hsbVals	= new float[3];
		Color.RGBtoHSB((int)rgbVals[0], (int)rgbVals[1], (int)rgbVals[2], hsbVals);

		this.dichromatic_OneHSB(hsbVals, inputNum);
	} // dichromatic_OneRGB

	/**
	 * Uses the given HSB color to find the color across it on the HSB wheel,
	 * converts both colors to RGB, and passes them as parameters to dichromatic_TwoRGB.
	 * 
	 * @param hue	float[] of HSB values defining the color at the tonic of the current scale.
	 */
	private void dichromatic_OneHSB(float[] hsbVals, int inputNum)
	{
		if(hsbVals == null) {
			throw new IllegalArgumentException("Module_01_02.dichromatic_OneHSB: float[] parameter hsbVals is null.");
		} // error checking

		// find the complement:
		float[]	hsbComplement	= new float[] { ((hsbVals[0] + 0.5f) % 1), 1, 1 };

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

		this.dichromatic_TwoRGB(rgbVals1, rgbVals2, inputNum);
	} // dichromatic_OneHSB(int)


	/**
	 * This method fills colors with the spectrum of colors between the given rgb colors.
	 * 
	 * @param rgbVals1	float[] of rgb values defining tonicColor.
	 * @param rgbVals2	float[] of rgb values defining the color of the last note of the scale.
	 */
	public void dichromatic_TwoRGB(int[] rgbVals1, int[] rgbVals2, int inputNum)
	{
		if(rgbVals1 == null || rgbVals2 == null) {
			throw new IllegalArgumentException("Module_01_02.dichromatic_TwoRGB: at least one of the float[] parameters is null.");
		} // error checking

		// Percent should be the percent of the difference between the first and second colors,
		// but the math doesn't work if we divide by 100 here, so that will happen later.
		float	percent		= 100 / this.scaleLength;

		// There will be a difference for red, green, and blue.
		//		float	difference;
		float	rDif	= rgbVals1[0] - rgbVals2[0];
		float	gDif	= rgbVals1[1] - rgbVals2[1];
		float	bDif	= rgbVals1[2] - rgbVals2[2];
/*
		System.out.println("Here we are in dichromatic!  rgbVals1 = rgb(" + rgbVals1[0] + ", "
				+ rgbVals1[1] + ", " + rgbVals1[2] + "); rgbVals2 = rgb(" + rgbVals2[0] + ", "
				+ rgbVals2[1] + ", " + rgbVals2[2] + "); "
				+ "\n\trDif = " + rDif + "; gDif = " + gDif + "; bDif = " + bDif);
*/
		// This array will hold the dichromatic spectrum:
		int[][]	dichromColors	= new int[this.scaleLength][3];
		
		// Fill dichromColors with the dichromatic spectrum:
		for(int i = 0; i < (dichromColors.length - 1); i++)
		{
			dichromColors[i][0]	= Math.round(rgbVals1[0] - (rDif * i * percent / 100));
			dichromColors[i][1]	= Math.round(rgbVals1[1] - (gDif * i * percent / 100));
			dichromColors[i][2]	= Math.round(rgbVals1[2] - (bDif * i * percent / 100));
		} // for - i
		
		// Fill the last position manually, in case the math rounds things too far and we don't quite get to it:
		for(int i = 0; i < rgbVals2.length; i++)
		{
			dichromColors[dichromColors.length - 1][i]	= rgbVals2[i];
		}

		// Now use scaleDegreeColors to fill this.colors with the dichrom spectrum at the appropriate positions for the current scale:
		int	dichromColorPos	= 0;

//		for(int i = this.startHere; i < this.endBeforeThis; i++)
//		{
			for(int j = 0; j < this.colors[inputNum].length && dichromColorPos < dichromColors.length; j++)
			{
				dichromColorPos	= this.getScaleDegreeColors()[this.majMinChrom][j];
	
				this.colors[inputNum][j]	= dichromColors[dichromColorPos];
			} // for - j
//		} // for - i

		this.fillHSBColors();

	} // dichromatic_TwoRGB

	/**
	 * Converts the given color to HSB and sends it to dichromatic_OneHSB.
	 * (dichromatic_OneHSB will send it to _TwoHSB, which will set this.colors, changing the scale.)
	 * 
	 * @param rgbVals	float[] of RGB values defining the color for the tonic of the scale.
	 */
	public void trichromatic_OneRGB(int[] rgbVals, int inputNum)
	{
		if(rgbVals == null) {
			throw new IllegalArgumentException("Module_01_02.trichromatic_OneRGB: int[] parameter is null.");
		}

		float[]	hsbVals	= new float[3];
		Color.RGBtoHSB((int)rgbVals[0], (int)rgbVals[1], (int)rgbVals[2], hsbVals);

		this.trichromatic_OneHSB(hsbVals, inputNum);
	} // trichromatic_OneRGB

	/**
	 * Uses the given HSB color to find the color across it on the HSB wheel,
	 * converts both colors to RGB, and passes them as parameters to trichromatic_ThreeRGB.
	 *
	 * @param hsbVals	float[] of HSB values defining the color at the tonic of the current scale.
	 */
	private void trichromatic_OneHSB(float[] hsbVals, int inputNum)
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

		this.trichromatic_ThreeRGB(rgbVals1, rgbVals2, rgbVals3, inputNum);
	} // trichromatic_OneHSB


	/**
	 * Calculates the colors between the 3 sets of given vals
	 * and fills colors with a spectrum fading between them.
	 * 
	 * @param rgbVals1	rgb vals for the tonic color
	 * @param rgbVals2	rgb vals for the sub-dominant for major/minor scales or "5th scale degree" (counting by half steps) for chromatic scales
	 * @param rgbVals3	rgb vals for the dominant for major/minor scales or the "9th scale degree" (counting by half steps) for chromatic scales
	 */
	public void trichromatic_ThreeRGB(int[] rgbVals1, int[] rgbVals2, int[] rgbVals3, int inputNum)
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

			divideBy1	= Math.max((color2pos - color1pos), 1);
			divideBy2	= Math.max((color3pos - color2pos), 1);
			divideBy3	= Math.max((this.scaleLength - color3pos), 1);
		} else {
			// These are their positions in trichromColors:
			color2pos	= 3;	// subdominant
			color3pos	= 4;	// dominant

			// (5 and 7 are the positions in colorSelect):
			//			color2pos	= 5;	// subdominant
			//			color3pos	= 7;	// dominant

			divideBy1	= 3;
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

//		for(int i = this.startHere; i < this.endBeforeThis; i++)
//		{
			for(int j = 0; j < this.colors[inputNum].length && trichromColorPos < trichromColors.length; j++)
			{
				trichromColorPos	= this.getScaleDegreeColors()[this.majMinChrom][j];

				this.colors[inputNum][j]	= trichromColors[trichromColorPos];
			} // for - j
//		} // for - i

		this.fillHSBColors();
	} //trichromatic_ThreeRGB


	/**
	 * Populates colors with rainbow colors (ROYGBIV - with a few more for chromatic scales).
	 */
	protected void rainbow()
	{
		for(int i = this.startHere; i < this.endBeforeThis; i++)
		{
			for(int j = 0; j < this.colors[i].length; j++)
			{
				this.colors[i][j][0]	= this.rainbowColors[this.majMinChrom][j][0];
				this.colors[i][j][1]	= this.rainbowColors[this.majMinChrom][j][1];
				this.colors[i][j][2]	= this.rainbowColors[this.majMinChrom][j][2];

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
	 * @param inputNum	the input for which to change the colorStyle
	 */
	public void setColorStyle(int newColorStyle, int inputNum)
	{
		if(inputNum >= this.curColorStyle.length || inputNum < 0)
		{
			throw new IllegalArgumentException("ModuleMenu.setColorStyle: inputNum " + inputNum + 
					" is outside the acceptable range; must be between 0 and " + this.curColorStyle.length + ".");
		}
		this.curColorStyle[inputNum]	= newColorStyle;

		// Rainbow:
		if(this.curColorStyle[inputNum] == ModuleMenu.CS_RAINBOW)
		{
			//	if avoids errors during instantiation:
			if(this.controlP5.getController("button" + (this.firstSpecialColorsCWId - 100)) != null)	
			{	
				this.controlP5.getController("button" + (this.firstSpecialColorsCWId - 100)).lock();	
			}
			if(this.controlP5.getController("button" + (this.firstSpecialColorsCWId - 99)) != null)		
			{	
				this.controlP5.getController("button" + (this.firstSpecialColorsCWId  - 99)).lock();	
			}
			if(this.controlP5.getController("button" + (this.firstSpecialColorsCWId - 98)) != null)		
			{	
				this.controlP5.getController("button" + (this.firstSpecialColorsCWId - 98)).lock();	
			}

			this.rainbow();
		} // if - rainbow

		// Dichromatic:
		if(this.curColorStyle[inputNum] == ModuleMenu.CS_DICHROM)
		{
//			this.specialColorsPos[inputNum][0]	= 0;
			
			// Determine the correct position for 2nd Color (minor scale will use a whole step before the tonic,
			// but major and chromatic use the leading tone):
			int	colorPos2;
			
			colorPos2	= this.colors[inputNum].length - 1;

				// For minor keys, choose the 2nd to last note; else choose the last note:
	/*			if(this.majMinChrom == 1)	{	colorPos2	= this.colors[inputNum].length - 2;	}
				else						{	colorPos2	= this.colors[inputNum].length - 1;	}
*/
				// Lock/unlock the appropriate specialColors Buttons:
				if(this.controlP5.getController("button" + (this.firstSpecialColorsCWId - 100)) != null)	{	this.controlP5.getController("button" + (this.firstSpecialColorsCWId - 100)).unlock();	}
				if(this.controlP5.getController("button" + (this.firstSpecialColorsCWId - 99)) != null)	{	this.controlP5.getController("button" + (this.firstSpecialColorsCWId  - 99)).unlock();	}
				if(this.controlP5.getController("button" + (this.firstSpecialColorsCWId - 98)) != null)	{	this.controlP5.getController("button" + (this.firstSpecialColorsCWId - 98)).lock();	}

				// First time to dichromatic, dichromFlag will be false, 
				// and the two colors will be set to contrast.			
				if(!this.dichromFlag)
				{
					this.dichromatic_OneRGB(this.colors[inputNum][0], inputNum);					

					this.dichromFlag	= true;
				} // first time
				// After the fjrst tjme, use current color values
				// (allows selection of 2nd color):
				else
				{
					// 1/12: Not doing this so that setting a color and then calling Dichrom will go from the newly set color:
					// Put the previous "2nd Color" into our new "2nd Color" position in colors, so that we can get the color directly from colors:
//					this.colors[inputNum][colorPos2]	= this.colors[inputNum][this.specialColorsPos[inputNum][1]];

					// Update specialColors positions for this scale:
					this.specialColorsPos[inputNum][0]	= 0;
					this.specialColorsPos[inputNum][1]	= colorPos2;

					this.dichromatic_TwoRGB(this.colors[inputNum][0], this.colors[inputNum][this.specialColorsPos[inputNum][1]], inputNum);
				}

		} // Dichromatic

		// Trichromatic:
		if(this.curColorStyle[inputNum] == ModuleMenu.CS_TRICHROM)
		{
			
			int	colorPos2	= 4;	// initializing for the first call
			int	colorPos3	= 8;

				// Turned off the "first time/remaining times" because it's still pretty interesting
				// and, I think, more intuitive, coming off of another color.  Dichromatic is boring coming off rainbow.
				// first time trichromatic has been called:
				if(!this.trichromFlag)
				{
					this.trichromatic_OneRGB(this.colors[inputNum][0], inputNum);
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
						// Positions have to be 5 and 7, not 3 and 4, since colors is filled all the way and we just ignore
						// non-diatonic tones, so 5 and 7 actually corresponds to the mediant and dominant scale degrees.

						colorPos2	= 5;
						colorPos3	= 7;
					} // else - colorPos for different scales
					
				} // else - all but the first time

				//				System.out.println("trichrom: setting colors[" + i + "][" + colorPos2 + "] to the color at position " + this.specialColorsPos[i][1] + 
				//						": rgb(" + this.colors[i][this.specialColorsPos[i][1]][0] + ", " + this.colors[i][this.specialColorsPos[i][1]][1] + ", " + this.colors[i][this.specialColorsPos[i][1]][2] + ")");
				//				System.out.println("trichrom: setting colors[" + i + "][" + colorPos3 + "] to the color at position " + this.specialColorsPos[i][2] + 
				//						": rgb(" + this.colors[i][this.specialColorsPos[i][2]][0] + ", " + this.colors[i][this.specialColorsPos[i][2]][1] + ", " + this.colors[i][this.specialColorsPos[i][2]][2] + ")");

				// Have to do this so that changing between keys doesn't change the specialColors:
				this.colors[inputNum][colorPos2]	= this.colors[inputNum][this.specialColorsPos[inputNum][1]];
				this.colors[inputNum][colorPos3]	= this.colors[inputNum][this.specialColorsPos[inputNum][2]];

				this.specialColorsPos[inputNum][0]	= 0;
				this.specialColorsPos[inputNum][1]	= colorPos2;
				this.specialColorsPos[inputNum][2]	= colorPos3;

				if(this.controlP5.getController("button" + (this.firstSpecialColorsCWId - 100)) != null)	{	this.controlP5.getController("button" + (this.firstSpecialColorsCWId - 100)).unlock();	}
				if(this.controlP5.getController("button" + (this.firstSpecialColorsCWId - 99)) != null)	{	this.controlP5.getController("button" + (this.firstSpecialColorsCWId  - 99)).unlock();	}
				if(this.controlP5.getController("button" + (this.firstSpecialColorsCWId - 98)) != null)	{	this.controlP5.getController("button" + (this.firstSpecialColorsCWId - 98)).unlock();	}

				this.trichromatic_ThreeRGB(this.colors[inputNum][0], this.colors[inputNum][colorPos2], this.colors[inputNum][colorPos3], inputNum);
			} // Trichromatic

	} // setColorStyle
	
	public void updateSpecialColorsPos(int colorStyle, int inputNum)
	{
		// Dichromatic:
		if(this.curColorStyle[inputNum] == ModuleMenu.CS_DICHROM)
		{
//			this.specialColorsPos[inputNum][1]	
		}
		// Trichromatic:
				if(this.curColorStyle[inputNum] == ModuleMenu.CS_TRICHROM)
				{
							if(this.majMinChrom == 2)
							{
								this.specialColorsPos[inputNum][1]	= 4;
								this.specialColorsPos[inputNum][2]	= 8;
							} else {
								// Positions have to be 5 and 7, not 3 and 4, since colors is filled all the way and we just ignore
								// non-diatonic tones, so 5 and 7 actually corresponds to the mediant and dominant scale degrees.

								this.specialColorsPos[inputNum][1]	= 5;
								this.specialColorsPos[inputNum][2]	= 7;
							} // else - colorPos for different scales
				} // Trichromatic
	}

	/**
	 * Applies the values of the Red Modulate/Green Modulate/Blue Modulate slcwIders.
	 */
	protected void applyRGBModulate()
	{
		int[]	color; //	= new int[3];

		for(int i = 0; i < this.colors.length; i++)
		{
			for(int j = 0; j < this.colors[i].length; j++)
			{
				color	= this.colors[i][j];

				for(int k = 0; k < color.length; k++)
				{
					// Adds redModulate to the red, greenModulate to the green, and blueModulate to the blue:
					color[k]	= Math.min(Math.max(color[k] + (int)this.redGreenBlueMod[k], 0), 255);
				} // for - k

				this.colors[i][j]	= color;
			} // for - j

			//			this.setColor(i, color, false);
			/*			this.setColorSelectCW(i, color);
			int	specialColorsPos	= this.arrayContains(this.specialColorsPos[this.currentInput], i);
			if(specialColorsPos > -1)
			{
				this.setSpecialColorsCW(specialColorsPos, color);
			}
			 */
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
	protected int[] applyThresholdSBModulate(float curAmp, int inputNum, int colorPos)
	{
		// Error checking:
		if(colorPos < 0 || colorPos >= this.colorSelect.length) {
			throw new IllegalArgumentException("ModuleTemplate.applyThresholdSBModulate: int parameter colorPos is " + colorPos + 
					", which is out of bounds; must be between 0 and " + (this.colorSelect.length - 1));
		}

		// Converts the current amplitude into a number between 0 and 100,
		// depending on where curAmp is in relation to the saturation or brightness forte threshold:
		float	satMappingVal		= Math.max(Math.min(PApplet.map(curAmp, 0, Math.max(this.satBrightThresholdVals[inputNum][0], this.minThreshold + 1), 0, 100), 100), 0);
		float	brightMappingVal	= Math.max(Math.min(PApplet.map(curAmp, 0, Math.max(this.satBrightThresholdVals[inputNum][1], this.minThreshold + 1), 0, 100), 100), 0);


		// Notice how hueSatBrightPercentMod is accessed at 1 and 2, since hue is also a part of it,
		// but satBrightPercentVals is accessed at 0 and 1, since it is only for saturation and brightness.
		this.hueSatBrightPercentMod[1]	= (this.satBrightPercentVals[inputNum][0] * satMappingVal) / 100;
		this.hueSatBrightPercentMod[2]	= (this.satBrightPercentVals[inputNum][1] * brightMappingVal) / 100;

		float[] hsb = new float[3];

		// Converts this position of hsbColors from RGB to HSB:
		Color.RGBtoHSB(this.hsbColors[inputNum][colorPos][0], this.hsbColors[inputNum][colorPos][1], this.hsbColors[inputNum][colorPos][2], hsb);

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
			this.hsbColors = new int[this.module.getTotalNumInputs()][this.colorSelect.length][3];
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
	 * Calls super.runMenu to show or hide the Controllers and shapeEditor.runMenu, if applicable.
	 */
	@Override
	public void runMenu()
	{
		// If super.runMenu is called first, the ShapeEditor covers the other Menu and neither work:
		if(this.shapeEditor != null)
		{
			this.shapeEditor.runMenu();
		}

		super.runMenu();
	} // runMenu

	/**
	 * Uses this.showPlayStop, this.showPause, and this.showHamburger to determine which of the 
	 * outside Buttons should be set to visible.
	 */
	public void showOutsideButtons()
	{
		this.outsideButtonsCP5.setVisible(true);

		this.outsideButtonsCP5.getController("play").setVisible(this.isShowPlayStop());
		this.outsideButtonsCP5.getController("pause").setVisible(this.showPause);
		this.showHamburger = true;
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
		// Can't call getController() on a Tab (which controlEvent() will try to do):
		if(controlEvent.isTab())
		{
			if(!controlEvent.getName().equals("shape"))
			{
				if(this.shapeEditor != null)
				{
					this.shapeEditor.isRunning	= false;
				}
			} else {
				this.shapeEditor.isRunning	= true;

			}
		} else {
			super.controlEvent(controlEvent);

			//		System.out.println("ModuleMenu.controlEvent: controlEvent = " + controlEvent);

			int	id	= controlEvent.getController().getId();
			// Play button:
			if(controlEvent.getController().getName().equals("play"))
			{
				boolean	val	= ((Toggle)controlEvent.getController()).getBooleanValue();
				this.input.pause(val);
				this.outsideButtonsCP5.getController("pause").setVisible(val);
				this.showPause	= val;

				//play button
				if(val)
				{
					if(!this.useRecInput)
					{
						this.playMelody();						
					} else {
						this.recInput.pause(false);
						this.recInputPlaying	= true;
					}
				} else {
					// Unpauses the pause button so that it is ready to be paused when
					// play is pressed again:
					((Toggle)this.outsideButtonsCP5.getController("pause")).setState(false);
					//				this.showPause	= false;
					
					if(!this.useRecInput)
					{
						this.melody.stop();						
					} else {
						this.recInput.pause(true);
						this.recInputPlaying	= false;
					}
				}

			} // if - play

			// Pause button:
			if(controlEvent.getController().getName().equals("pause"))
			{
				if(!this.useRecInput)
				{
					this.melody.pause(((Toggle)controlEvent.getController()).getBooleanValue());					
				} else {
					this.recInput.pause(((Toggle)controlEvent.getController()).getBooleanValue());
					this.recInputPlaying	= !(((Toggle)controlEvent.getController()).getBooleanValue());
				}
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
				this.isRunning		= true;

				System.out.println("controlEvent - hamburger: isRunning = " + this.isRunning);
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
				this.isRunning			= false;

				//			this.displaySidebar(false);
				/*			this.leftEdgeX	= 0;
				this.controlP5.getGroup("sidebarGroup").setVisible(false);
				 */
				//			this.outsideButtonsCP5.getController("hamburger").setVisible(true);

				this.outsideButtonsCP5.getController("hamburger").setVisible(!((Toggle)this.controlP5.getController("menuButton")).getBooleanValue());
				if(this.shapeEditor != null)
				{
					this.shapeEditor.isRunning	= false;	// In case it gets clicked from within the ShapeEditor Tab
				}
			} // if - menuX

			// Hide play button button:
			if(controlEvent.getName().equals("playButton"))
			{
				// Set the actual play button to visible/invisible:
				this.outsideButtonsCP5.getController("play").setVisible(!this.outsideButtonsCP5.getController("play").isVisible());
				this.outsideButtonsCP5.getController("pause").setVisible(((Toggle)this.outsideButtonsCP5.getController("play")).getBooleanValue() && this.outsideButtonsCP5.getController("play").isVisible());
				this.setShowPlayStop((this.outsideButtonsCP5.getController("play").isVisible()));
				this.showPause		= ((Toggle)this.outsideButtonsCP5.getController("play")).getBooleanValue() && this.outsideButtonsCP5.getController("play").isVisible();
			} // if - hidePlayButton


			// Hide menu button button:
			if(controlEvent.getName().equals("menuButton"))
			{
				// Hamburger is still able to be clicked because of a boolean isClickable added to 
				// Controller; automatically false, but able to be set to true.
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
				Toggle	curToggle	= (Toggle) controlEvent.getController();
				
				// Dichromatic and Trichromatic take care of this themselves:
				/*
				int[]	sc1	= new int[3];
				int[]	sc2	= new int[3];
				int[]	sc3	= new int[3];

				// Save previous special colors:
				for(int i = 0; i < this.colors.length; i++)
				{
					sc1	= this.colors[i][this.specialColorsPos[i][0]];
					sc2	= this.colors[i][this.specialColorsPos[i][1]];
					sc3	= this.colors[i][this.specialColorsPos[i][2]];
					
					System.out.println(i + ": specialColors at " + this.specialColorsPos[i][0] + 
							"[rgb(" + this.colors[i][this.specialColorsPos[i][0]][0] + ", " + this.colors[i][this.specialColorsPos[i][0]][1] + ", " + this.colors[i][this.specialColorsPos[i][0]][2] + ")], " + 
							this.specialColorsPos[i][1] + "[rgb(" + this.colors[i][this.specialColorsPos[i][1]][0] + ", " + this.colors[i][this.specialColorsPos[i][1]][1] + ", " + this.colors[i][this.specialColorsPos[i][1]][2] + ")], and " + 
							this.specialColorsPos[i][2] + "[rgb(" + this.colors[i][this.specialColorsPos[i][2]][0] + ", " + this.colors[i][this.specialColorsPos[i][2]][1] + ", " + this.colors[i][this.specialColorsPos[i][2]][2] + ")]");
				}
				*/
				
				// Update the key:
				this.setCurKey(this.curKey, (int) curToggle.internalValue());					
				
				// Call setColorStyle so that dichromatic and trichromatic can adjust for the key change:
				// (not using startHere and endBeforeThis because key has global effect every time)
				for(int i = 0; i < this.colors.length; i++)
				{
					// Update the colorStyle, which will update specialColorsPos since we have a new key:
					this.setColorStyle(this.curColorStyle[i], i);
				}

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

				for(int i = this.startHere; i < this.endBeforeThis; i++)
				{
					this.setColorStyle((int)curToggle.internalValue(), i);
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
						//						System.out.println("setting " + toggleArray[i] + " to false");
						toggleArray[i].setState(false);
					} else {
						//						System.out.println("setting " + toggleArray[i] + " to true");
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

				if(this.firstColorSelectCWId > -1)
				{
					this.updateCustomColorButtonLabels(curKeyEnharmonicOffset);
				}
			} // if

			// Guide Tone Generator:
			if(controlEvent.getName().equals("guideToneButton"))
			{
				if(((Toggle)controlEvent.getController()).getBooleanValue())
				{

					//				this.controlP5.setAutoDraw(false);
					this.controlP5.getGroup("leftBackground").setVisible(true);
					this.controlP5.getGroup("leftBackground").bringToFront();
					this.controlP5.getGroup("topBackground").setVisible(true);
					this.controlP5.getGroup("topBackground").bringToFront();

					this.controlP5.getController("guideToneButton").bringToFront();

				} else {

					//				this.controlP5.setAutoDraw(true);
					this.controlP5.getGroup("leftBackground").setVisible(false);
					this.controlP5.getGroup("topBackground").setVisible(false);
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

			// Input Select dropdown:
			if(controlEvent.getName() == "inputSelectDropdown")
			{
				controlEvent.getController().bringToFront();

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
				this.startHere		= this.currentInput;
				this.endBeforeThis	= this.currentInput + 1;

				// Restore this input's colorStyle:
				if(this.curColorStyle[this.currentInput] == ModuleMenu.CS_RAINBOW)
				{
					this.controlP5.getController("rainbow").update();
				}
				if(this.curColorStyle[this.currentInput] == ModuleMenu.CS_DICHROM)
				{
					this.controlP5.getController("dichrom").update();
				}
				if(this.curColorStyle[this.currentInput] == ModuleMenu.CS_TRICHROM)
				{
					this.controlP5.getController("trichrom").update();
				}
				if(this.curColorStyle[this.currentInput] == ModuleMenu.CS_CUSTOM)
				{
					this.controlP5.getController("custom").update();
				}

				this.fillHSBColors();
			} // input select dropdown

			if(controlEvent.getName().equals("global"))
			{
				this.startHere		= 0;
				this.endBeforeThis	= this.module.getTotalNumInputs();
			} // global

			if(controlEvent.getName().equals("numInputsList"))
			{
				this.module.setCurNumInputs((int)controlEvent.getValue() + 1);
				this.module.setSquareValues();
			} // numInputsList

			if(controlEvent.getName().equals("saveColors"))
			{
				this.saveColorState();
			}
			
			if(controlEvent.getName().equals("loadColors"))
			{
				this.loadColorState();
			}

			// Dynamic Bars:
			if(controlEvent.getController().getId() == 99999)
			{
				if(this.dynamicBars)
				{
					this.dynamicBars = false;
				}
				else
				{
					this.dynamicBars = true;
				}
			} // dynamic bars

		} // if !Tab
	} // controlEvent


	/**
	 * Called from MenuTemplate for Sliders that were added by the addSliderGroup() method
	 * and are connected to a Textfield (MenuTemplate has already updated the Textfield value).
	 */
	public void sliderEvent(int id, float val)
	{
		//		System.out.println("ModuleMenu: got sliderEvent with id " + id + " and val " + val);

		// Piano Threshold:
		if(id == this.pianoThresholdSliderId)
		{
			for(int i = 0; i < this.module.getTotalNumInputs(); i++)
			{
				this.pianoThreshold[i]	= (int)val;
				this.resetThresholds(i);
			}
		} // piano threshold Slider

		// Forte Threshold:
		if(id == this.forteThresholdSliderId)
		{
			for(int i = 0; i < this.module.getTotalNumInputs(); i++)
			{
				this.forteThreshold[i]	= (int)val;
				this.resetThresholds(i);
			}
		} // forte threshold Slider

		// Attack, Release, and Transition:
		if((id >= this.getFirstARTSliderId() && id < (this.getFirstARTSliderId() + 3)) && this.getFirstARTSliderId() != -1)
		{
			//			int	pos	= (id / 2) - 1;
			int	pos	= id - this.getFirstARTSliderId();
			//			this.attackReleaseTransition[pos]	= val;

			if(global)
			{
				for(int i = 0; i < this.module.getTotalNumInputs(); i++)
				{
					this.setAttRelTranVal(pos, i, val);
				} // for
			} else {
				this.setAttRelTranVal(pos, this.currentInput, val);
			} // else - not global
		} // attack/release/transition

		// Hue/Saturation/Brightness modulate
		if((id >= this.firstHSBSliderId && id < (this.firstHSBSliderId + 3)) && this.firstHSBSliderId != -1)
		{
			//			int pos = (id/2)-7;
			int pos = id - this.firstHSBSliderId;

			this.setHueSatBrightnessMod(pos, val);

			//					this.applyHSBModulate(this.hsbColors);
			this.applyHSBModulate();
		}//hsb mod

		// Red Modulate/Green Modulate/Blue Modulate:
		if((id >= this.firstRGBSliderId && id < (this.firstRGBSliderId + 3)) && this.firstRGBSliderId != -1)
		{
			//			int	pos	= (id / 2) - 4;		
			int	pos	= id - this.firstRGBSliderId;	// red = 0, green = 1, blue = 2

			this.redGreenBlueMod[pos]	= val;
			this.applyRGBModulate();
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
			for(int i = 0; i < this.shapeSize.length; i++)
			{
				this.shapeSize[i]	= val;
			}
		}

		// Saturation and Brightness Threshold and Percent Sliders:
		if( ( id >= this.firstSatBrightThreshSliderId ) && ( id < this.firstSatBrightThreshSliderId + 4 ) 
				&& this.firstSatBrightThreshSliderId != -1)
		{

			int		arrayPos	= (id - this.firstSatBrightThreshSliderId /*- 1*/) / 2;
			float	forteThreshPercent;

			for(int i = this.startHere; i < this.endBeforeThis; i++)
			{
				// Percent Sliders
				if((id - this.firstSatBrightThreshSliderId) % 2 == 0)
				{
					this.satBrightPercentVals[i][arrayPos]		= val;

					// The percent of forte slider that the threshold should be:
					forteThreshPercent	= this.controlP5.getValue("slider" + (id + 1));
					//				percentVal		= controlEvent.getValue();
				} else {
					// Threshold Sliders:

					forteThreshPercent	= val;
					this.satBrightPercentVals[i][arrayPos]		= this.controlP5.getValue("slider" + (id - 1));
				}

				this.satBrightThresholdVals[i][arrayPos]	= this.pianoThreshold[i] + (this.forteThreshold[i] * forteThreshPercent);

			} // for
		} // Saturation and Brightness Threshold and Percent Sliders

		// Alpha Slider:
		if(id > 0 && id == this.alphaSliderId)
		{
			this.alphaVal	= (int)val;
		} // alpha Slider
	} // sliderEvent

	/**
	 * Called from MenuTemplate for Buttons that were added by the addColorWheelGroup() method and are 
	 * connected to a CW and CWTextfield (MenuTemplate has already updated the CW and Textfield values).
	 */
	public void buttonEvent(int id)
	{
		System.out.print("ModuleMenu: got buttonEvent with id " + id);

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

		int	colorPos;
		int	cwId	= (id % 100) + 300;

		// if from colorSelect, set the ColorWheel to the correct color:
		if(cwId >= this.firstColorSelectCWId && cwId < (this.firstColorSelectCWId + colors[this.currentInput].length) && cwId != this.canvasColorSelectId)
		{
			colorPos	= (id % 100) - (this.firstColorSelectCWId % 100);
			System.out.println("Button event: about to call colorSelectCW for colorPos " + colorPos);
			/*
			 * Hey - it calls this with whatever is in colors currently... 
			 * So if dichrom put it there == oh wait, but that's for current input. So never mind. :P
			 */
			this.setColorSelectCW(colorPos, this.colors[this.currentInput][colorPos]);
		} else if(cwId >= this.firstSpecialColorsCWId && cwId < (this.firstSpecialColorsCWId + this.specialColorsPos[0].length))
		{
			// or if from specialColors, set that ColorWheel:
			colorPos	= (id % 100) - (this.firstSpecialColorsCWId % 100);
			System.out.println("specialColors colorPos = " + colorPos);
			this.setSpecialColorsCW(colorPos, this.colors[this.currentInput][this.specialColorsPos[this.currentInput][colorPos]]);
		}
	} // buttonEvent

	/**
	 * Called from MenuTemplate for ColorWheels that were added by the addColorWheelGroup() method and are 
	 * connected to a Button and CWTextfield (MenuTemplate has already updated the Textfield value).
	 */
	public void colorWheelEvent(int id, Color color)
	{
//		System.out.println("ModuleMenu: got colorWheelEvent with id " + id + " and color array " + color);

		// Either do everything once for the currentInput or do it for all inputs:

		int	colorPos;
		int	colorPosStartHere;
		int	colorPosEndHere;

		// if from ColorSelect or canvasColorSelect:
		if(( id >= this.firstColorSelectCWId && id < (this.firstColorSelectCWId + this.colorSelect.length ) ) 
				|| (id == this.canvasColorSelectId) )
		{
			colorPos	= id - this.firstColorSelectCWId;
		} else if(id >= this.firstSpecialColorsCWId && id < (this.firstSpecialColorsCWId + this.specialColorsPos[0].length))
		{
			// if from specialColors:
			colorPos	= this.specialColorsPos[this.currentInput][id - this.firstSpecialColorsCWId];
		} else {
			throw new IllegalArgumentException("ModuleMenu.colorWheelEvent: CW with id " + id + " is not from colorSelect or specialColors;" + 
					"firstColorSelectCWID = " + this.firstColorSelectCWId + ".");
		}

		/*
		protected	final	int[][]	scaleDegreeColors	= new int[][] {
		// major:
		new int[] { 0, 0, 1, 2, 2, 3, 4, 4, 4, 5, 6, 6 },
		// minor:
		new int[] { 0, 0, 1, 2, 2, 3, 4, 4, 5, 5, 6, 6 },
		// chromatic:
		new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 }
	}; // scaleDegreeColors

	// Put them each in little arrays of { colorPosStartHere, colorPosEndHere }
		 */

		// Set the appropriate colors:
		for(int i = this.startHere; i < this.endBeforeThis; i++)
		{
			// canvas color (does not affect notes):
			if( ( id % 100 ) == ( this.canvasColorSelectId % 100 ) )
			{
				this.canvasColor[0]	= color.getRed();
				this.canvasColor[1]	= color.getGreen();
				this.canvasColor[2]	= color.getBlue();

				// Ensures that the shape doesn't have to fade to this color if the amp is below the threshold:
				if(this.nowBelow[i])
				{
					this.curHue[i][0]	= color.getRed();
					this.curHue[i][1]	= color.getGreen();
					this.curHue[i][2]	= color.getBlue();
				}
			} else {
				// colors that are not canvasColor:		
				colorPosStartHere	= colorPosStartEnd[this.majMinChrom][colorPos][0];
				colorPosEndHere		= colorPosStartEnd[this.majMinChrom][colorPos][1];


				for(int j = colorPosStartHere; j < (colorPosEndHere + 1); j++)
				{
					this.colors[i][j][0]	= color.getRed();
					this.colors[i][j][1]	= color.getGreen();
					this.colors[i][j][2]	= color.getBlue();
				}
			} // else - not canvas// Set the colorStyle if we got something from specialColors:

		} // for

		// If from specialColors CW, make sure that colorStyle gets updated:
		if(id >= this.firstSpecialColorsCWId && id < (this.firstSpecialColorsCWId + this.specialColorsPos[0].length))
		{
			// if from specialColors:
			for(int i = this.startHere; i < this.endBeforeThis; i++)
			{
				this.setColorStyle(this.curColorStyle[i], i);
			}
		} // specialColors

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
	/*	protected	int	calculateNotePos(int id)
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

	/*		id	= id % 100;
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
	 */

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


	private void updateCustomColorButtonLabels(int enharmonicKeyPos)
	{
		for(int i = 0; i < this.totalRangeSegments; i++)
		{
			this.newNoteNames[i]	= this.noteNames[(enharmonicKeyPos + i) % this.newNoteNames.length];
		} // for - fill newNoteNames

		for(int i = 0; i < this.newNoteNames.length; i++)
		{
			((Button)this.controlP5.getController("button" + (this.firstColorSelectCWId - 100 + i))).setLabel(this.newNoteNames[i]);
		} // for
	} // updateCustomColorButtonLabels

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
		// Checking allNotes gives the right position for the dropdown menu, since it has all the keys separately, too:
		int	keyPos	= this.arrayContains(this.allNotes, key);
		int	enharmonicKeyPos;

		if(keyPos == -1)	{
			throw new IllegalArgumentException("Module_01_02.setCurKey: " + key + " is not a valid key.");
		}

		// Use the previous number to get the enharmonic position and rearrange the notes:
		enharmonicKeyPos	= this.enharmonicPos[keyPos];

		//System.out.println("key = " + key + "; keyPos = " + keyPos);


		this.majMinChrom	= majMinChrom;
		this.scaleLength	= this.getScale(key, majMinChrom).length;

		if(this.controlP5.getController("keyDropdown") != null)
		{
			this.controlP5.getController("keyDropdown").setValue(keyPos);
		}

		// Reset the text on the Custom Color Select buttons:
		if(this.firstColorSelectCWId > -1)
		{
			this.updateCustomColorButtonLabels(enharmonicKeyPos);
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
		if (inputNum >= this.module.getTotalNumInputs()) {
			IllegalArgumentException iae = new IllegalArgumentException("ModuleMenu.inputNumErrorCheck(int): int parameter " + inputNum + " is greater than " + this.module.getTotalNumInputs() + ", the number of inputs.");

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
				cw.setRGB(colorInt);
			} // if
		} else {
			System.err.println("ModuleMenu.setColor: firstColorSelectCWId == " + this.firstColorSelectCWId + "; did not attempt to set the ColorWheel at " + colorPos + ".");
		} // if/else
	} // setCSColorWheel

	/**
	 * Method to set a specialColors ColorWheel; used when switching to a new curInputNum,
	 * because it updates the ColorWheel without affecting this.colors.
	 * 
	 * @param colorPos
	 * @param color
	 */
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

	public void saveColorState()
	{
		int returnVal = fc.showSaveDialog(null);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			String	filename	= file.getName();
			if( (filename.length() < 4) || !(filename.substring(filename.length() - 4).equalsIgnoreCase(".txt")) )
			{
				file = new File(file.toString() + ".txt");
			}

			int	response	= -1;

			if(file.exists())
			{
				response = JOptionPane.showOptionDialog(null, 
						"That file already exists.  Overwrite file?", 
						"File exists",
						JOptionPane.OK_CANCEL_OPTION, 
						JOptionPane.QUESTION_MESSAGE, 
						null,
						null,
						null);

				System.out.println("response = " + response);
			}

			if(!file.exists() || (response == JOptionPane.OK_OPTION))
			{
				try
				{
					file.createNewFile();

					BufferedWriter	out	= new BufferedWriter(new FileWriter(file));
					out.write("*** *** ***\n");

					// Need these (colors.length, colors[0].length) in order to correctly interpret the data when loading it
					// and to notify the user if the numbers do not match his colors:
					out.write(this.colors.length + "\n");	// Number of inputs
					out.write(this.colors[0].length + "\n");	// Number of color items

					for(int i = 0; i < this.colors.length; i++)
					{
						for(int j = 0; j < this.colors[i].length; j++)
						{
							out.write(this.colors[i][j][0] + "\t" + this.colors[i][j][1] + "\t" + this.colors[i][j][2] + "\n");
						}
					}

					out.close();		

				} catch (IOException ioe) {
					System.out.println("ModuleMenu.saveColorState: caught IOException " + ioe);
					ioe.printStackTrace();
				}
			} // if - File did not exist or overwriting previous File
		} // if - chose to save File

	} // saveColorState
	
	public void loadColorState()
	{
		String[]	splitResults;	// Use this to hold the color values while they are being parsed to ints
		
		int returnVal = fc.showOpenDialog(null);
		
		if(returnVal == JFileChooser.APPROVE_OPTION)
		{
			File file	= fc.getSelectedFile();
			
			try
			{
				BufferedReader in	= new BufferedReader(new FileReader(file));
				
				String	stars	= in.readLine();

				if(stars.equals("*** *** ***"))
				{
					// else, go on to check the dimensions
					int	numInputs	= Integer.parseInt(in.readLine());
					int	numColors	= Integer.parseInt(in.readLine());
					
					System.out.println("numInputs = " + numInputs + "; numColors = " + numColors);
					
					if( (this.colors.length == numInputs) && (numColors == this.colors[0].length) )
					{
						// and if we make it this far, actually read the values into this.colors
						for(int i = 0; i < this.colors.length; i++)
						{
							for(int j = 0; j < this.colors[i].length; j++)
							{
								splitResults	= (in.readLine()).split("\t");
								
								for(int k = 0; k < splitResults.length; k++)
								{
									this.colors[i][j][k]	= Integer.parseInt(splitResults[k]);
								} // for - k
							} // for - j
						} // for - i
						
					} else {
						// Wrong dimensions
						JOptionPane.showMessageDialog(null, 
								"Sorry, the dimensions of that color file do not match the current settings.  " + 
						"Currently, colors.length == " + this.colors.length + " and colors[0].length == " + 
										this.colors[0].length + ", while the file has " + numInputs + " and " + 
						numColors + ", respectively.");
					} // if - correct dimensions
				} else {
					// Doesn't begin with "*** *** ***"
					JOptionPane.showMessageDialog(null, "Sorry, " + file.getName() + " is not a valid color file.");
				} // if - valid color file
				
				in.close();
			} catch (IOException ioe) {
				System.out.println("ModuleMenu.loadColorState: caught IOException " + ioe);
				ioe.printStackTrace();
			}
		}
	} // loadColorState

	public int[][] getCurHue()				{	return this.curHue;	}

	public int getCurColorStyle(int inputNum)			{	return this.curColorStyle[inputNum];	}

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
		if(position < 0 || position > this.attRelTranVals[0].length) {
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

	public int[] getCanvasColor() {
		return this.canvasColor;
	}
	
	public void setCanvasColor(int[] newCanvasColor)
	{
		if(this.canvasColor.length != newCanvasColor.length) {
			throw new IllegalArgumentException("ModuleMenu.setCanvasColor: int[] parameter is of wrong length (" + newCanvasColor.length + ") - should be length 3.");
		}

		for(int i = 0; i < this.canvasColor.length; i++)
		{
			this.canvasColor[i]	= newCanvasColor[i];
		}
	} // setCanvasColor

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

	public float[] getShapeSize() {
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
	
	public int getPianoThreshold(int inputNum)
	{
		this.inputNumErrorCheck(inputNum);
		
		return this.pianoThreshold[inputNum];
	}

	/**
	 * Sets the percent slider to the given value
	 * (or to the max or min value, if the value is out of range).
	 * 
	 * @param newVal	new value for the brightness percent slider, from -1 to 1
	 */
	public void setBrightnessPercentSlider(float newVal)
	{
		this.controlP5.getController("slider" + (this.firstSatBrightThreshSliderId + 2)).setValue(newVal);
	} // setBrightnessPercentSlider

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
		this.showHamburger	= true;
	}//set menu val


	public int getSliderHeight() {
		return this.sliderHeight;
	}

	public ControlP5 getOutsideButtonsCP5()
	{
		return this.outsideButtonsCP5;
	}

	public Instrument getInstrument()
	{
		return this.instrument;
	}

	public void setBPM(int bpm)
	{
		this.bpm = bpm;
	}

	/*
	public void setMenuList(String[] list)
	{
		((ScrollableList)this.controlP5.getController("menuList"))
		.addItems(list);
	}
*/
	
	public int getCurrentMenu()
	{
		return (int) this.controlP5.getController("menuList").getValue();
	}

	public boolean getDynamicBars()
	{
		return this.dynamicBars;
	}

	public void setDynamicBars(boolean newVal) {
		this.dynamicBars	= newVal;
	}


	public boolean isShowPlayStop() {
		return showPlayStop;
	}


	public void setShowPlayStop(boolean showPlayStop) {
		this.showPlayStop = showPlayStop;
	}


	public int getFirstARTSliderId() {
		return firstARTSliderId;
	}

	public int getAlphaVal()
	{
		return this.alphaVal;
	}

	public void setAlphaSlider(int newAlphaVal)
	{
		newAlphaVal	= Math.min(Math.max(0, newAlphaVal), 255);

		this.controlP5.getController("slider" + this.alphaSliderId).setValue(newAlphaVal);
	}

	public ShapeEditor getShapeEditor() {
		return shapeEditor;
	}

	public int getCurrentInput()
	{
		return this.currentInput;
	}

	public void setCurrentInput(int newCurrentInput)
	{
		this.currentInput	= newCurrentInput;

		if(!global)
		{
			this.startHere	= this.currentInput;
			this.endBeforeThis	= (this.currentInput + 1);
		}
	} // setCurrentInput

	public void setGlobal(boolean newGlobal)
	{
		//		this.global	= newGlobal;
		((Toggle)this.controlP5.getController("global")).setState(false);

		if(!newGlobal)
		{
			this.startHere	= this.currentInput;
			this.endBeforeThis	= (this.currentInput + 1);
		} else {
			this.startHere	= 0;
			this.endBeforeThis	= this.module.getTotalNumInputs();
		}
	}

	/**
	 * Use when you don't want the first call to trichrom to trigger auto-generated colors (Red-Green-Blue).
	 * @param newVal
	 */
	public void setTrichromFlag(boolean newVal)
	{
		this.trichromFlag	= newVal;
	}

	/**
	 * Use when you don't want the first call to dichrom to trigger auto-generated colors (Red-Pink).
	 * @param newVal
	 */
	public void setDichromFlag(boolean newVal)
	{
		this.dichromFlag	= newVal;
	}

	public int[][] getScaleDegreeColors() {
		return scaleDegreeColors;
	}
	
	public void setUseRecInput(boolean newVal)
	{
		this.useRecInput	= newVal;
	}
	
	public boolean getRecInputPlaying()
	{
		return this.recInputPlaying;
	}
	
	public RecordedInput getRecInput()
	{
		return this.recInput;
	}
	
	public boolean getUseRecInput()
	{
		return this.useRecInput;
	}


} // ModuleMenu
