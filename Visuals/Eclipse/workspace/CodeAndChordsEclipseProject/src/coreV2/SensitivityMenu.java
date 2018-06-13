package coreV2;

import java.awt.Color;
import java.util.Map;

import controlP5.Button;
import controlP5.ControlEvent;
import controlP5.ControlP5;
import controlP5.ScrollableList;
import controlP5.Toggle;
import core.Instrument;
import core.Melody;
import processing.core.PApplet;

public class SensitivityMenu extends MenuTemplate
{

	/** ALL notes here	*/
	protected	final String[]	allNotes	= new String[] {
			"A", "A#", "Bb", "B", "C", "C#", "Db", "D", "D#", "Eb", "E", "F", "F#", "Gb", "G", "G#", "Ab"
	}; // allNotes

	/** Positions of each note in allNotes, where enharmonic notes have the same position */
	private	final int[]	enharmonicPos	= new int[] {
			0, 1, 1, 2, 3, 4, 4, 5, 6, 6, 7, 8, 9, 9, 10, 11, 11
	}; // enharmonicPos
	
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
	
	/**
	 * These lists of notes allow the position of any given note to be found in the current scale.
	 */
	protected	final String[]	notesAtoAbFlats	= new String[] { 
			"A", "Bb", "B", "C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab"
	};

	protected final String[]	notesAtoGSharps	= new String[] { 
			"A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#"
	};
	

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

	/**	Holds the values of the saturation percent and brightness percent threshold Sliders, respectively	*/
	protected	float[][]	satBrightPercentVals;

	/**	Stores the values of the attack, release, and transition sliders	*/
	private	float[][]	attRelTranVals;

	/**	Attack, Release, or Transition - 0 = attack, 1 = release, 2 = transition	*/
	private	int[]		attRelTranPos;

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
	 * The following id's will be set by the add____() methods and used to identify Controllers in controlEvent().
	 */
	protected	int	firstARTSliderId		= -1;
	protected	int	pianoThresholdSliderId	= -1;
	protected	int	forteThresholdSliderId	= -1;
	protected	int	bpmSliderId				= -1;
	protected	int	volumeSliderId			= -1;



	public SensitivityMenu() 
	{
		//super(pApplet, appWidth, appHeight, controlP5, moduleDriver);
		super("Sensitivity");

		this.controlP5.addTab("sensitivity")
		.setLabel("Sensitivity\nMenu")
		.setWidth(50)
		.setHeight(this.tabHeight)
		.activateEvent(true)
		.getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);

		// TODO: convenience method in MenuTemplate to calculate spacing;
		// then each MenuTemplate will be responsible for passing its own values.
		this.addARTSliders(this.controllerXVals[0], this.textYVals[1], this.textYVals[2], this.textYVals[3]);
		this.addPianoThresholdSlider(this.controllerXVals[0], this.textYVals[5]);
		this.addForteThresholdSlider(this.controllerXVals[0], this.textYVals[6]);

		this.addThresholdSliders(this.controllerXVals[0], this.textYVals[8], this.spacer);

		this.addGuideTonePopout(controllerXVals[1], textYVals[2]);
		this.addKeySelector(controllerXVals[1], textYVals[2]);
		this.setCurKey("A", 2);
	}

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
	 * Uses this.threshold, this.forteThreshold and this.curRangeSegments 
	 * to recalculate the length of and values within this.thresholds.
	 */
	private	void resetThresholds(int pos)
	{
		this.moduleDriver.inputNumErrorCheck(pos);

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


	@Override
	public void sliderEvent(int id, float val) {
		// Piano Threshold:
		if(id == this.pianoThresholdSliderId)
		{
			for(int i = 0; i < this.moduleDriver.getTotalNumInputs(); i++)
			{
				this.pianoThreshold[i]	= (int)val;
				this.resetThresholds(i);
			}
		} // piano threshold Slider

		// Forte Threshold:
		if(id == this.forteThresholdSliderId)
		{
			for(int i = 0; i < this.moduleDriver.getTotalNumInputs(); i++)
			{
				this.forteThreshold[i]	= (int)val;
				this.resetThresholds(i);
			}
		} // forte threshold Slider

		// Attack, Release, and Transition:
		if((id >= this.firstARTSliderId && id < (this.firstARTSliderId + 3)) && this.firstARTSliderId != -1)
		{
			//			int	pos	= (id / 2) - 1;
			int	pos	= id - this.firstARTSliderId;
			//			this.attackReleaseTransition[pos]	= val;

			for(int i = this.moduleDriver.getStartHere(); i < this.moduleDriver.getEndBeforeThis(); i++)
			{
				this.setAttRelTranVal(pos, i, val);	
			}
			
		} // attack/release/transition


		if( id == this.bpmSliderId)
		{
			this.bpm	= Math.max(Math.min((int)val, 240), 0);
		}

		if(id == this.volumeSliderId)
		{
			this.instrument.setVolume(Math.max(Math.min(val, 5), 0));
		}

	} // sliderEvent

	@Override
	public void buttonEvent(int id) {

	} // buttonEvent

	@Override
	public void colorWheelEvent(int id, Color color) {

	} // colorWheelEvent

	public void controlEvent(ControlEvent controlEvent) {
		// TODO - test out exactly who has to call super.controlEvent:
		super.controlEvent(controlEvent);
		
		// Can't call getController() on a Tab (which controlEvent() will try to do):
		if(!controlEvent.isTab())
		{

			//		System.out.println("ModuleMenu.controlEvent: controlEvent = " + controlEvent);

			int	id	= controlEvent.getController().getId();

			// Major/Minor/Chromatic buttons
			if(controlEvent.getName().equals("major") ||
					controlEvent.getName().equals("minor") ||
					controlEvent.getName().equals("chrom"))
			{
				Toggle	curToggle	= (Toggle) controlEvent.getController();

				// Update the key:
				this.setCurKey(this.curKey, (int) curToggle.internalValue());					

				// Call setColorStyle so that dichromatic and trichromatic can adjust for the key change:
				// (not using startHere and endBeforeThis because key has global effect every time)
				for(int i = 0; i < this.moduleDriver.getColorHandler().colors.length; i++)
					// TODO - getting colors like this for real? ^
				{
					// Update the colorStyle, which will update specialColorsPos since we have a new key:
					this.moduleDriver.getColorHandler().setColorStyle(this.moduleDriver.getColorHandler().getColorStyle(i), i);
				}

				// TODO - the problem: what to do when Menus interact?
				
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

		} // else - not Tab
	} // controlEvent
>>>>>>> library
}
