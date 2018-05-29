package coreV2;

import java.awt.Color;

import controlP5.ControlP5;
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



	public SensitivityMenu(PApplet pApplet, int appWidth, int appHeight, ControlP5 controlP5, ModuleDriver moduleDriver) 
	{
		super(pApplet, appWidth, appHeight, controlP5, moduleDriver);
		
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

			if(global)
			{
				for(int i = 0; i < this.moduleDriver.getTotalNumInputs(); i++)
				{
					this.setAttRelTranVal(pos, i, val);
				} // for
			} else {
				this.setAttRelTranVal(pos, this.currentInput, val);
			} // else - not global
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
}
