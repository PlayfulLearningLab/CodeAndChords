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
import controlP5.Controller;
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
	
//	private	int	trichromCounts	= 0;
/*
	private	static	float	CS_RAINBOW	= 1;
	private	static	float	CS_DICHROM	= 2;
	// TODO: make private after fixing trichrom error
	public static	float	CS_TRICHROM	= 3;
	private	static	float	CS_CUSTOM	= 4;
	private	float	curColorStyle;
	*/
	//	private boolean menuVis = false;


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

	// Each int signifies the position in dichromColors/trichromColors/rainbowColors that is used to fill 
	// this.colors at the corresponding position in scaleDegreeColors[this.majMinChrom]:
/*	private	final	int[][]	scaleDegreeColors	= new int[][] {
		// major:
		new int[] { 0, 0, 1, 2, 2, 3, 4, 4, 4, 5, 6, 6 },
		// minor:
		new int[] { 0, 0, 1, 2, 2, 3, 4, 4, 5, 5, 6, 6 },
		// chromatic:
		new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 }
	}; // scaleDegreeColors
*/
	int[]				textYVals;
	int[]				noteYVals;
	int[]				modulateYVals;
	int[]               modulateHSBVals;
	int					colorSelectY;

/*	private	boolean	dichromFlag;
	private	boolean	trichromFlag;
*/
	public ModuleTemplate01(PApplet parent, Input input, String sidebarTitle)
	{
		// TODO: how am I going to deal with minor scales??
		super(parent, input, sidebarTitle, 12);

		this.parent	= parent;
		this.input	= input;

//		this.colors 		= new float[12][3];
//		this.legendColors	= new int[12][3];
//		this.originalColors	= new int[12][3];
		this.hsbColors      = new int[12][3];

		this.curColorStyle	= ModuleTemplate01.CS_RAINBOW;
		
		this.specialColorsPos	= new int[3];
		this.setColorStyle(this.curColorStyle);
		
		// The following will happen in rainbow():
		//		this.tonicColor	= new int[] { 255, 0, 0, };
/*		this.dichromFlag	= false;
		this.trichromFlag	= false;
*/
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
		this.textYVals		 = new int[18];
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

		addColorStyleButtons(textYVals[13]);

		// This call to rainbow() used to be the last in initInput(),
		// but has to be called before addCustomPitchColor() so that this.colors will be filled 
		// before the ColorWheels are created and the ColorWheels can be set to the colors in this.colors.
		// If the call comes at the end, the ColorWheels start black and end grayscale.
		this.rainbow();
//		this.fillOriginalColors();
		// addColorSelect() will fillHSBColors
//		this.fillHSBColors();
		//		this.updateColors(this.curColorStyle);

//		this.addCustomPitchColor(textYVals[15], noteYVals);

		addHSBSliders(modulateHSBVals);

		addModulateSliders(modulateYVals);

//		this.updateColors(this.curColorStyle);

		//		this.hideTextLabels();

		this.curColorStyle	= ModuleTemplate01.CS_RAINBOW;
//		this.dichromFlag	= false;
//		this.trichromFlag	= false;	

		this.sidebarCP5.getController("keyDropdown").bringToFront();
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

//		int	labelX			= 10;

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

//		int	labelX			= 10;

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

		this.canvasColorSelectId	= this.nextButtonId;
//		this.firstColorSelectId		= this.canvasColorSelectId;

		
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
		} // for

	} // addColorSelectButtons

	/**
	 * Method called during instantiation to initialize note buttons and their corresponding ColorWheels;
	 * make sure that rainbow() has already been called (or that this.colors has been filled some other way).
	 * 
	 * @param noteYVals	int[] of y values for each note button
	 */
/*	private void addCustomPitchColor(int labelYVal, int[] noteYVals)
	{
		int spacer1			= 5;	// between buttons and textfields
		int	spacer2			= 15;	// between the two rows of pitches

		int	buttonWidth		= 30;
		int	textfieldWidth	= 100;

		int	noteX1			= this.leftAlign - 40;
		int	textfieldX1		= noteX1 + buttonWidth + spacer1;

		int	noteX2			= textfieldX1 + spacer2;
		int	textfieldX2		= noteX2 + buttonWidth + spacer1;

		int	noteX3			= textfieldX2 + spacer2;
		int	textfieldX3		= noteX3 + buttonWidth + spacer1;

		int	noteX4			= textfieldX3 + spacer2;
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

//		this.firstCustomColorId	= this.nextButtonId;
		this.firstColorSelectCWId		= this.nextColorWheelId;


		int	namePos	= 0;
		int colorpos  = 0;

		// First column of pitches:
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
			.setText("Code#")
			.setGroup("sidebarGroup")
			.setId(this.nextCWTextfieldId)
			.getCaptionLabel().setVisible(false);


			this.nextCWTextfieldId = this.nextCWTextfieldId + 1;
			namePos	= namePos + 1;
		}// first column of pitches

		namePos	= 0;
		for(int i = 0; i < noteNames1.length; i++)
		{// Second column of pitches:

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
		} // second column of pitches
		
		namePos	= 0;
		for(int i = 0; i < noteNames1.length; i++)
		{// Third column of pitches:

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
		} // third column of pitches
		
		namePos	= 0;
		
		for(int i = 0; i < noteNames1.length; i++)
		{// Fourth column of pitches:

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
		} // fourth column of pitches

	} // addNoteColorSelectors
	*/
	
	/**
	 * Sets the value colors for each new colorStyle
	 * by calling rainbow(), dichromOneRGB, etc.
	 * 
	 * @param colorStyle	the desired colorStyle; this will be used to set the curColorStyle instance var
	 */
/*	private void updateColors(int colorStyle)
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
				this.trichromatic_OneRGB(this.getColor(0));

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
/*
				System.out.println("updateColors: about to call trichrom_3RGB with rgb(" + 
						this.colors[0][0] + ", " + this.colors[0][1] + ", " + this.colors[0][2] + "), rgb(" +
						this.colors[colorPos2][0] + ", " + this.colors[colorPos2][1] + ", " + this.colors[colorPos2][2] + "), (rgb" + 
						this.colors[colorPos3][0] + ", " + this.colors[colorPos3][1] + ", " + this.colors[colorPos3][2] + ")");
*/
//				this.trichromatic_ThreeRGB(this.getColor(0), this.getColor(colorPos2), this.getColor(colorPos3));
				//				this.updateCustomPitchCWs();
/*			} // else


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
		// TODO: I think we should get rid of custom!
		if(this.curColorStyle == ModuleTemplate01.CS_CUSTOM)
		{
			((Toggle)(this.sidebarCP5.getController("chrom"))).setState(true);

			// (The functionality in controlEvent will check for custom, and if it is custom, they will set their position of colors to their internal color.)


			// (Will they need to check to make sure that the key is actually chromatic?)
		} // custom colorStyle

		// Populate the CustomPitch ColorWheels with the current colors:
// TODO: if things are weird, might be b/c of this:
//		this.updateCustomPitchCWs();

	} // updateColors
	*/
				
	protected void setColorStyle(int newColorStyle)
	{
		this.curColorStyle	= newColorStyle;
		System.out.println("	--------------------------------setColorStyle: curColorStyle = " + this.curColorStyle);

		// Rainbow:
		if(this.curColorStyle == ModuleTemplate01.CS_RAINBOW)
		{
			//	if avoids errors during instantiation:
			if(this.sidebarCP5.getController("button" + (this.firstSpecialColorsCWId - 100)) != null)	{	this.sidebarCP5.getController("button" + (this.firstSpecialColorsCWId - 100)).lock();	}
			if(this.sidebarCP5.getController("button" + (this.firstSpecialColorsCWId - 99)) != null)	{	this.sidebarCP5.getController("button" + (this.firstSpecialColorsCWId  - 99)).lock();	}
			if(this.sidebarCP5.getController("button" + (this.firstSpecialColorsCWId - 98)) != null)	{	this.sidebarCP5.getController("button" + (this.firstSpecialColorsCWId - 98)).lock();	}
	
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
/*			if(!this.dichromFlag)
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
	*/		
		

			// Unlock 2nd Color Button, but keep 3rd Color locked:
			// if avoids errors during instantiation:
/*			if(this.sidebarCP5.getController("button" + (this.canvasColorSelectId + 2)) != null)
			{
				this.sidebarCP5.getController("button" + (this.canvasColorSelectId + 2)).setLock(false);
			}
			if(this.sidebarCP5.getController("button" + (this.canvasColorSelectId + 3)) != null)
			{
				this.sidebarCP5.getController("button" + (this.canvasColorSelectId + 3)).setLock(true);
			}
*/
		} // Dichromatic

		// Trichromatic:
		if(this.curColorStyle == ModuleTemplate01.CS_TRICHROM)
		{
			int	colorPos2	= 4;	// initializing for the first call
			int	colorPos3	= 8;
			
			// first time trichromatic has been called:
/*			if(!this.trichromFlag)
			{
				this.trichromatic_OneRGB(this.getColor(0));

				this.trichromFlag	= true;
			}

			// every other time:
			else
			{
*/				
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
			
			this.specialColorsPos[0]	= 0;
			
			// For minor keys, choose the 2nd to last note; else choose the last note:
			this.specialColorsPos[1]	= colorPos2;
			this.specialColorsPos[2]	= colorPos3;
			
			if(this.sidebarCP5.getController("button" + (this.firstSpecialColorsCWId - 100)) != null)	{	this.sidebarCP5.getController("button" + (this.firstSpecialColorsCWId - 100)).unlock();	}
			if(this.sidebarCP5.getController("button" + (this.firstSpecialColorsCWId - 99)) != null)	{	this.sidebarCP5.getController("button" + (this.firstSpecialColorsCWId  - 99)).unlock();	}
			if(this.sidebarCP5.getController("button" + (this.firstSpecialColorsCWId - 98)) != null)	{	this.sidebarCP5.getController("button" + (this.firstSpecialColorsCWId - 98)).unlock();	}
			
/*
				System.out.println("updateColors: about to call trichrom_3RGB with rgb(" + 
						this.colors[0][0] + ", " + this.colors[0][1] + ", " + this.colors[0][2] + "), rgb(" +
						this.colors[colorPos2][0] + ", " + this.colors[colorPos2][1] + ", " + this.colors[colorPos2][2] + "), (rgb" + 
						this.colors[colorPos3][0] + ", " + this.colors[colorPos3][1] + ", " + this.colors[colorPos3][2] + ")");
*/
			this.trichromatic_ThreeRGB(this.getColor(0), this.getColor(colorPos2), this.getColor(colorPos3));
				//				this.updateCustomPitchCWs();
//			} // else


			// Unlock all for Trichromatic:
			// if avoids errors during instantiation:
/*			if(this.sidebarCP5.getController("button" + (this.canvasColorSelectId + 2)) != null)
			{
				this.sidebarCP5.getController("button" + (this.canvasColorSelectId + 2)).setLock(false);
			}
			if(this.sidebarCP5.getController("button" + (this.canvasColorSelectId + 3)) != null)
			{
				this.sidebarCP5.getController("button" + (this.canvasColorSelectId + 3)).setLock(false);
			}
			*/

		} // Trichromatic

	} // setColorStyle


	/**
	 * Updates the colors of the colorSelect ColorWheels, which will then send an event to their connected Textfields.
	 */
/*	private void updateColorSelectCWs()
	{
		// If to avoid calling these before the Buttons/CWs/TFs have been initialized:
		if(this.canvasColorSelectId > -1)
		{

			if(this.curColorStyle == ModuleTemplate01.CS_RAINBOW)
			{
				int colorSelectId	= (this.canvasColorSelectId % 100) + 301;
				this.updateColorWheelFromColors(colorSelectId, 0);
			} // if - rainbow

			if(this.curColorStyle == ModuleTemplate01.CS_DICHROM)
			{
				int colorSelectId	= (this.canvasColorSelectId % 100) + 301;
				this.updateColorWheelFromColors(colorSelectId, 0);
				this.updateColorWheelFromColors(colorSelectId + 1, (this.colors.length - 1));
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

				this.updateColorWheelFromColors(colorSelectId, 0);
				this.updateColorWheelFromColors(colorSelectId + 1, colorPos2);
				this.updateColorWheelFromColors(colorSelectId + 2, colorPos3);
			} // if - trichromatic
		} // if canvasColorSelectId > -1
		else
		{
			System.out.println("ModuleTemplate01.updateColorSelectCWs: did not update ColorWheels - canvasColorSelectId is " + this.canvasColorSelectId);
		} // else - let the user know we didn't do it
	} // updateColorSelectCWs

	/**
	 * Updates the colors of the custom pitch ColorWheels, which will then send an event to their connected Textfields.
	 */
	// 8/3 - commenting out b/c we don't even want to use this anymore:
/*	private void updateCustomPitchCWs()
	{
		// Make sure that the Buttons/CWs/TFs have been added before we try to do this:
		if(this.firstCustomColorId > -1)
		{
			int	id	= (this.firstCustomColorId % 100) + 300;	// ColorWheels start at 300

			for(int colorPos = 0; colorPos < this.colors.length; colorPos++)
			{
				updateColorWheelFromColors(id, colorPos);
				id	= id + 1;
			} // for - colorPos
		} else {
			System.out.println("ModuleTemplate01.updateCustomPitchCWs: did not update ColorWheels - firstCustomColorId is " + this.firstCustomColorId);
		} // else - let the user know we didn't do it

	} // updateCustomPitchCWs
*/
	
	/**
	 * Draws the scale legend at the bottom of the screen.
	 * 
	 * @param goalHuePos	current position in the scale; used to show the user their pitch
	 */
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

			this.parent.fill(this.getColor(i)[0], this.getColor(i)[1], this.getColor(i)[2]);
//			this.parent.fill(this.colors[colorPos][0], this.colors[colorPos][1], this.colors[colorPos][2]);
//			this.parent.fill(this.legendColors[colorPos][0], this.legendColors[colorPos][1], this.legendColors[colorPos][2]);
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


		// TODO: remove after fixing trichrom-maj/minor bug:
		// Testing to see what's really in colors:
		for(int i = 0; i < this.colorSelect.length; i++)
		{
			this.parent.fill(this.getColor(i)[0], this.getColor(i)[1], this.getColor(i)[2]);
			this.parent.ellipse(this.parent.width / 3 * 2, i * 30 + 60, 30, 30);
		} // for

	} // legend


	/**
	 * Converts the given color to HSB and sends it to dichromatic_OneHSB.
	 * (dichromatic_OneHSB will send it to _TwoHSB, which will set this.colors, changing the scale.)
	 * 
	 * @param rgbVals	float[] of RGB values defining the color for the tonic of the scale.
	 */
/*	public void dichromatic_OneRGB(int[] rgbVals)
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
/*	private void dichromatic_OneHSB(float[] hsbVals)
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
/*	public void dichromatic_TwoRGB(int[] rgbVals1, int[] rgbVals2, boolean fillFirstToLast)
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
/*			for(int i = 0; i < this.colorSelect.length - 1; i++)
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
		
/*		for(int i = 0; i < rgbVals2.length; i++)
		{
			this.colors[this.colors.length - 1][i]	= rgbVals2[i];
		}
		*/
//	} // dichromatic_TwoRGB

	/**
	 * Converts the given color to HSB and sends it to dichromatic_OneHSB.
	 * (dichromatic_OneHSB will send it to _TwoHSB, which will set this.colors, changing the scale.)
	 * 
	 * @param rgbVals	float[] of RGB values defining the color for the tonic of the scale.
	 */
/*	public void trichromatic_OneRGB(int[] rgbVals)
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
/*	private void trichromatic_OneHSB(float[] hsbVals)
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
/*	public void trichromatic_ThreeRGB(int[] rgbVals1, int[] rgbVals2, int[] rgbVals3)
	{
		if(rgbVals1 == null || rgbVals2 == null || rgbVals3 == null) {
			throw new IllegalArgumentException("Module_01_02.trichromatic_ThreeRGB: at least one of the float[] parameters is null.");
		} // error checking

		this.trichromCounts++;
		System.out.println("	this.trichromCounts = " + this.trichromCounts + "----------------------------------------------------------------------------------------------------------");
		
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

		int	redDelta1	= (int)((rgbVals1[0] - rgbVals2[0]) / (color2pos - color1pos));
		int	greenDelta1	= (int)((rgbVals1[1] - rgbVals2[1]) / (color2pos - color1pos));
		int	blueDelta1	= (int)((rgbVals1[2] - rgbVals2[2]) / (color2pos - color1pos));

		int	redDelta2	= (int)((rgbVals2[0] - rgbVals3[0]) / (color3pos - color2pos));
		int	greenDelta2	= (int)((rgbVals2[1] - rgbVals3[1]) / (color3pos - color2pos));
		int	blueDelta2	= (int)((rgbVals2[2] - rgbVals3[2]) / (color3pos - color2pos));

		int	redDelta3	= (int)((rgbVals3[0] - rgbVals1[0]) / (this.scaleLength - color3pos));
		int	greenDelta3	= (int)((rgbVals3[1] - rgbVals1[1]) / (this.scaleLength - color3pos));
		int	blueDelta3	= (int)((rgbVals3[2] - rgbVals1[2]) / (this.scaleLength - color3pos));
		/*		
		System.out.println("redDelta1 = " + redDelta1 + "; greenDelta1 = " + greenDelta1 + "; blueDelta1 = " + blueDelta1);
		System.out.println("redDelta2 = " + redDelta2 + "; greenDelta2 = " + greenDelta1 + "; blueDelta2 = " + blueDelta2);
		System.out.println("redDelta3 = " + redDelta3 + "; greenDelta3 = " + greenDelta1 + "; blueDelta3 = " + blueDelta3);
		 */
		// This array has the trichromatic spectrum:
//		int[][]	trichromColors	= new int[this.scaleLength][3];
/*		trichromColors	= new int[this.scaleLength][3];

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
		for(int i = 0; i < this.colorSelect.length && trichromColorPos < trichromColors.length; i++)
		{
			// TODO: I don't think that these positions are being set correctly :/
			trichromColorPos	= this.scaleDegreeColors[this.majMinChrom][i];
			
			this.setColor(i, trichromColors[trichromColorPos]);

/*			this.colors[i][0]	= trichromColors[trichromColorPos][0];
			this.colors[i][1]	= trichromColors[trichromColorPos][1];
			this.colors[i][2]	= trichromColors[trichromColorPos][2];
			*/
//		} // for

//		this.updateCustomPitchCWs();
//	} //trichromatic_ThreeRGB

	/**
	 * Populates colors with rainbow colors (ROYGBIV - with a few more for chromatic scales).
	 */
/*	public void rainbow()
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
		
		int	id	= this.firstColorSelectCWId;
		ColorWheel	curCW	= (ColorWheel)this.sidebarCP5.getController("colorWheel" + id);

		for(int i = 0; i < this.colors.length && i < rainbowColors[this.majMinChrom].length; i++)
		{
			Color	rgbColor	= new Color(rainbowColors[this.majMinChrom][i][0], rainbowColors[this.majMinChrom][i][1], rainbowColors[this.majMinChrom][i][2]);
			((ColorWheel)this.sidebarCP5.getController("colorWheel" + (id - 100))).setRGB(rgbColor.getRGB());

			id	= id + 1;
			
/*			for(int j = 0; j < this.colors[i].length && j < rainbowColors[this.majMinChrom][i].length; j++)
			{
				//				this.getColors()[i][j]	= rainbowColors[this.getMajMinChrom()][i][j];
				this.colors[i][j]	= rainbowColors[this.majMinChrom][i][j];
				this.hsbColors[i][j]	= rainbowColors[this.majMinChrom][i][j];
			} // for - j (going through rgb values)
			*/
/*		} // for - i (going through colors)
		
		this.fillHSBColors();


		// Populate the Textfields with the current colors in the colors array,
		// but only if the custom color buttons have already been initialized
		// (rainbow() is called before that in the constructor so that there will be colors for 
		// the ColorWheels and Textfields to use when they are created):
/*		if(this.firstCustomColorId > -1)
		{
			int	id	= (this.firstCustomColorId % 100) + 300;	// CWTextfields start at 400

			for(int colorPos = 0; colorPos < this.colors.length; colorPos++)
			{
				this.updateColorWheelFromColors(id, colorPos);

				id	= id + 1;
			} // for - colorPos

		} // if - firstCustomColorId > -1
*/
//	} // rainbow

	
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

		// Sliders (sliders have id num < 100 and corresponding textfields have slider id + 100)
		//		if(id % 2 == 0 && id < this.lastTextfieldId)
		if(id > -1 && id < 100)
		{
			// Color modulation applied in parent class;
			// this just updates the ColorWheels (which are specific to this child)

			// Hue/Saturation/Brightness modulate
/*			if(this.firstHSBSliderId != -1 && 
					id >= this.firstHSBSliderId && id < (this.firstHSBSliderId + 3))
			{
				this.updateColorSelectCWs();
				this.updateCustomPitchCWs();
			}//hsb mod
*/
			// Red Modulate/Green Modulate/Blue Modulate:
			if(this.firstRGBSliderId != -1 &&
					id >= this.firstRGBSliderId && id < (this.firstRGBSliderId + 3))
			{
				// TODO - this shouldn't be necessary:
//				this.updateColorSelectCWs();
//				this.updateCustomPitchCWs();
			} // red/green/blue mod

		} // sliders

		// Major/Minor/Chromatic buttons
		if(controlEvent.getName().equals("major") ||
				controlEvent.getName().equals("minor") ||
				controlEvent.getName().equals("chrom"))
		{
			System.out.println("New scale quality: " + controlEvent.getName());
			
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


		// only call updateColors() for colorSelect Buttons, ColorWheels, or Textfields:
/*		if(id >= 200 && id < 500 && (id % 100) >= (this.firstColorSelectId % 100) && (id % 100) <= (this.lastColorSelectId % 100))
		{
			System.out.println("------------ Making sure that this is ONLY ColorSelect Buttons/CWs/TFs: id = " + id + " ------------");
			// TODO - this needs to happen... but maybe not as often....
			this.updateColors(this.curColorStyle);
			this.updateCustomPitchCWs();
		} // ColorWheels
		*/


		// Color Style:
		if(controlEvent.getName().equals("rainbow") ||
				controlEvent.getName().equals("dichrom") ||
				controlEvent.getName().equals("trichrom") ||
				controlEvent.getName().equals("custom"))
		{
			Toggle	curToggle	= (Toggle) controlEvent.getController();

			// Set tonic color/call correct function for the new colorStyle:
//			this.updateColors((int)curToggle.internalValue());

			this.setColorStyle((int)curToggle.internalValue());
			

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

			// These calls have to come after all the colors have been set,
			// so that originalColors and hsbColors can be filled with the latest:
//			this.fillOriginalColors();
//			this.fillHSBColors();
			this.resetModulateSlidersTextfields();
//			this.applyColorModulate(this.originalColors);

			// TODO: this shouldn't be necessary anymore.
//			this.updateColorSelectCWs();
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

	/**
	 * Getter for curColorStyle instance var
	 * @return	float instance var this.curColorStyle
	 */
	public float getCurColorStyle() {
		return this.curColorStyle;
	} // get CurColorStyle
	
} // ModuleTemplate01 class
