package core;

import controlP5.ControlEvent;
import controlP5.ControlP5;
import controlP5.ScrollableList;
import controlP5.Toggle;
import processing.core.PApplet;

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
public class ModuleTemplate01 extends ModuleMenu {
	
	private	PApplet		parent;

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

	int[]				textYVals;
	int[]				modulateYVals;
	int[]               modulateHSBVals;
	int					colorSelectY;

	public ModuleTemplate01(PApplet parent, Input input, String sidebarTitle)
	{
		super(parent, null, input, sidebarTitle, 12);

		this.parent	= parent;
		this.input	= input;

		this.hsbColors      = new int[12][3];

		this.curColorStyle	= ModuleTemplate01.CS_RAINBOW;
		
		this.specialColorsPos	= new int[3];
		
		// textYVals will be used for sliders and buttons, including hsb and 
		// rgb modulate values.
		this.textYVals		 = new int[18];
		this.modulateYVals	 = new int[3];
		this.modulateHSBVals = new int[3];
		this.redGreenBlueMod		 	= new float[3];

		this.initModuleTemplate();
	} // ModuleTemplate

	// Methods:

	/**
	 * Called from constructor to calculate Y vals and call the methods for instantiating the necessary buttons;
	 * will eventually call different button methods depending on the module number.
	 */
	private void initModuleTemplate()
	{
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

		yValDif = 26;

		for(int i = 1; i < textYVals.length; i++)
		{
			textYVals[i]	= textYVals[i - 1] + yValDif;
		} // for

		// Add extra space before "Pitch Color Codes":
		textYVals[textYVals.length - 3]	= textYVals[textYVals.length - 4] + (int)(yValDif * 1.5);
		textYVals[textYVals.length - 2]	= textYVals[textYVals.length - 3] + (int)(yValDif * 1);
		textYVals[textYVals.length - 1]	= textYVals[textYVals.length - 2] + (int)(yValDif * 1);

		// call add methods (addHideButtons already called in parent):
		addSliders(textYVals[1], textYVals[2], textYVals[3], textYVals[4]);

		this.addGuideTonePopout(textYVals[5]);
		this.addKeySelector(textYVals[5]);
		this.setCurKey(this.curKey, this.majMinChrom);


		modulateHSBVals[0] = textYVals[6];
		modulateHSBVals[1] = textYVals[7];
		modulateHSBVals[2] = textYVals[8];

		modulateYVals[0]	= textYVals[9];
		modulateYVals[1]	= textYVals[10];
		modulateYVals[2]	= textYVals[11];

		// Adding ColorSelect first since everything to do with colors depends on that:
		String[] noteNames = new String[] {
				"A", "A#/Bb", "B", "C", "C#/Db", "D", "D#/Db", "E", "F", "F#/Gb", "G", "G#/Ab"
		}; // noteNames
		
		this.addColorSelect(new int[] { textYVals[15], textYVals[16], textYVals[17] }, noteNames, "Custom Pitch\nColor Select", false);
		

		// ColorSelect and ColorStyle added out of order so that the 2nd Color
		// and 3rd Color select buttons will exist for the Rainbow ColorStyle
		// to lock them.
//		this.addColorSelectButtons(textYVals[14]);
		String[] buttonLabels	= new String[] {
				"Canvas", "Tonic", "2nd Color", "3rd Color"
		}; // buttonLabels
		this.addSpecialColors(textYVals[14], buttonLabels, "Color Select", true);

		// addColorStyleButtons will set the colorStyle to rainbow() first:
		this.addColorStyleButtons(textYVals[13]);

		addHSBSliders(modulateHSBVals);

		addModulateSliders(modulateYVals);

		this.curColorStyle	= ModuleTemplate01.CS_RAINBOW;

		this.controlP5.getController("keyDropdown").bringToFront();
	} // initModuleTemplate


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

		int rainbowX     	= this.leftAlign;
		int dichromaticX	= this.leftAlign + colorStyleWidth + colorStyleSpace;
		int trichromaticX	= this.leftAlign + (colorStyleWidth + colorStyleSpace) * 2;
		int customX			= this.leftAlign + (colorStyleWidth + colorStyleSpace) * 3;

		this.controlP5.addTextlabel("colorStyle")
		.setPosition(labelX, colorStyleY + 4)
		.setGroup("sidebarGroup")
		.setValue("Color Style");

		this.controlP5.addToggle("rainbow")
		.setPosition(rainbowX, colorStyleY)
		.setWidth(colorStyleWidth)
		.setCaptionLabel("Rainbow")
		.setGroup("sidebarGroup")
		.setInternalValue(ModuleTemplate01.CS_RAINBOW);
		this.controlP5.getController("rainbow").getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);

		this.controlP5.addToggle("dichrom")
		.setPosition(dichromaticX, colorStyleY)
		.setWidth(colorStyleWidth)
		.setCaptionLabel("Dichrom.")
		.setGroup("sidebarGroup")
		.setInternalValue(ModuleTemplate01.CS_DICHROM);
		this.controlP5.getController("dichrom").getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);

		this.controlP5.addToggle("trichrom")
		.setPosition(trichromaticX, colorStyleY)
		.setWidth(colorStyleWidth)
		.setCaptionLabel("Trichrom.")
		.setGroup("sidebarGroup")
		.setInternalValue(ModuleTemplate01.CS_TRICHROM);
		this.controlP5.getController("trichrom").getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);

		this.controlP5.addToggle("custom")
		.setPosition(customX, colorStyleY)
		.setWidth(colorStyleWidth)
		.setCaptionLabel("Custom")
		.setGroup("sidebarGroup")
		.setInternalValue(ModuleTemplate01.CS_CUSTOM);
		this.controlP5.getController("custom").getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);

		this.setColorStyle(ModuleMenu.CS_RAINBOW);
	} // addColorStyleButtons

	
	/**
	 * Draws the scale legend at the bottom of the screen.
	 * 
	 * @param goalHuePos	current position in the scale; used to show the user their pitch
	 */
	public void legend(int goalHuePos)
	{

		this.parent.textSize(24);

		String[]	notes	= this.getScale(this.curKey, this.majMinChrom);

		float  sideWidth1   = (this.parent.width - leftEdgeX) / notes.length;
		float  sideHeight  = this.parent.width / 12;
		float	addToLastRect	= (this.parent.width - this.getLeftEdgeX()) - (sideWidth1 * notes.length);
		float	sideWidth2	= sideWidth1;

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

			this.parent.fill(this.getColor(colorPos)[0], this.getColor(colorPos)[1], this.getColor(colorPos)[2]);

			if (i == goalHuePos) {
				this.parent.rect(leftEdgeX + (sideWidth1 * i), (float)(this.parent.height - (sideHeight * 1.5)), sideWidth2, (float) (sideHeight * 1.5));
			} else {
				this.parent.rect(leftEdgeX + (sideWidth1 * i), this.parent.height - sideHeight, sideWidth2, sideHeight);
			}
			this.parent.fill(0);
			this.parent.text(notes[i], (float) (leftEdgeX + (sideWidth1 * i) + (sideWidth1 * 0.35)), this.parent.height - 20);
		} // for

	} // legend

	
	/**
	 * This method "catches" all ControlP5 ControlEvents, sends them to ModuleTemplate.controlEvent,
	 * and deals with a few module-specific Controllers on its own.
	 * 
	 * @param theControlEvent	ControlEvent used to determine which controller needs to act.
	 */
	public void controlEvent(ControlEvent controlEvent)
	{
		super.controlEvent(controlEvent);
//		System.out.println("ModuleTemplate01: theControlEvent.getController() = " + controlEvent.getController());

		int	id	= controlEvent.getController().getId();

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
	} // controlEvent

	
	/**
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
	 * Getter for curColorStyle instance var
	 * @return	float instance var this.curColorStyle
	 */
	public float getCurColorStyle() {
		return this.curColorStyle;
	} // get CurColorStyle
	
	public void runMenu()
	{
		
	} // runMenu
	
} // ModuleTemplate01 class
