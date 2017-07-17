package core;

import java.awt.Color;
import java.text.DecimalFormat;

import controlP5.Button;
import controlP5.ColorWheel;
import controlP5.ControlEvent;
import controlP5.ControlFont;
import controlP5.ControlP5;
import controlP5.Slider;
import controlP5.Textfield;
import controlP5.Toggle;
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
	 * must be initiated by child class.
	 */
	protected	float[][]	colors;
	private	float[]		curHue;
	private	float[]		goalHue;
	protected	float[]		canvasColor;
	private	float[]		colorAdd;
	private	float[]		colorRange;

	/**
	 * The current colors which hsb sliders are altering;
	 * must be initiated by child class.
	 */
	protected float[][]   hsbColors; //the current colors at which hsb is altering

	/**
	 * filled in the Custom color style to allow RGB modifications to colors;
	 * must be initiated by child class.
	 */
	protected	float[][]	originalColors;


	private boolean[]	colorReachedArray;
	private	boolean		colorReached;

	private	Input	input;
	private float	threshold;
	private boolean	nowBelow;

	private	int		attRelTranPos;		// 0 = attack, 1 = release, 2 = transition
	private int 	checkpoint;		// For a timer that allows attack/release/transition sliders to be time-based.
	private	float[]	attRelTranVals;

	protected	float[]	redGreenBlueMod;	// this will store the red/green/blue modulate values
	protected	float[] hueSatBrightnessMod; // This will store the hsb modulate values

	protected Melody		melody;
	protected String		curKey;
	protected	int			bpm;
	protected int 		majMinChrom;
	protected int			rangeOctave;
	protected Instrument	instrument;

	protected boolean	showScale;

	protected int		sliderHeight;

	protected int		lastSetSliderId;
	protected int		lastSetTextfieldId;
	protected int		lastSetColorSelectButtonId;
	protected int		lastSetColorWheelId;
	protected int		lastSetColorSelectTextfieldId;

	/**
	 * If a child class adds a canvas color select Button,
	 * they should set this variable to the value of the Button's id, for use in controlEvent:
	 */
	protected	int	canvasColorSelectId	= -1;
	
	/**
	 * If a child class adds color modulate sliders (RGB or HSB)
	 * they should set this variable to the value of the Button's id, for use in resetModulateSliders:
	 */
	protected	int	firstColorModSliderId	= -1;
	
	protected	int	firstColorSelectId	= -1;
	
	protected	int	lastColorSelectId	= -1;
	
	protected	int	thresholdSliderId	= -1;
	
	protected	int	firstARTSliderId	= -1;
	protected	int	firstHSBSliderId	= -1;
	protected	int	firstRGBSliderId	= -1;
	protected	int	bpmSliderId			= -1;
	protected	int	volumeSliderId		= -1;


	/**
	 * DecimalFormat used for rounding the text corresponding to Sliders and Colorwheels.
	 */
	private	DecimalFormat	decimalFormat	= new DecimalFormat("#.##");

	protected	int	nextSliderId;
	protected	int	nextSTextfieldId;	// Textfield next to a slider
	protected	int	nextButtonId;	// for Buttons that open a ColorWheel
	protected	int	nextColorWheelId;		// ColorWheels
	protected	int	nextCWTextfieldId;	// Textfield under a ColorWheels


	public ModuleTemplate(PApplet parent, Input input, String sidebarTitle)
	{
		this.parent			= parent;
		this.input			= input;
		this.sidebarTitle	= sidebarTitle;

		this.leftAlign	= (this.parent.width / 3) / 4;
		this.leftEdgeX	= 0;

		this.sliderHeight	= 20;

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

		this.nextSliderId		= 0;
		this.nextSTextfieldId	= 100;
		this.nextButtonId		= 200;
		this.nextColorWheelId	= 300;
		this.nextCWTextfieldId	= 400;

		this.initModuleTemplate();
	} // constructor

	private void initModuleTemplate()	
	{
		this.sidebarCP5.addGroup("sidebarGroup")
		.setBackgroundColor(this.parent.color(0))
		.setSize(this.parent.width / 3, this.parent.height + 1)
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

		// TODO: depending on what kind of guide tones this needs, might move addGuideTonePopout() here

	} // initModuleTemplate

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
				System.out.println("setGoalHue: canvasColor[" + i + "] " + this.canvasColor[i]);
				this.goalHue[i]	= this.canvasColor[i];
			} // for - canvas
		} else {	
			for(int i = 0; i < this.goalHue.length; i++)
			{
				this.goalHue[i]	= this.getColors()[position][i];	
			} // for - colors
		} // else

	} // setGoalHue


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
	 * Applies the values of the Red Modulate/Green Modulate/Blue Modulate sliders and 
	 * calls applyHSBModulate() to apply the values of the Hue/Saturation/Brightness Modulate sliders.
	 */
	protected void applyColorModulate(float[][] colors, float[][] originalColors)
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
	 * Applies the values from this.hueSatBrightnessMod to the contents of super.colors.
	 * @param colors	super.colors
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
	 * Puts the contents of this.colors into this.hsbColors;
	 * must be called when colors is initialized.
	 */
	protected	void fillHSBColors()
	{
		if(this.colors == null)
		{
			throw new IllegalArgumentException("ModuleTemplate.fillHSBColors: super.colors is null.");
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

		// Sliders
		if(id > -1 && id < 100)
		{
			try
			{
				Slider	curSlider	= (Slider)this.sidebarCP5.getController("slider" + id);
				Textfield	curTextfield	= (Textfield)this.sidebarCP5.getController("textfield" + (id + 100));
				String	sliderValString	= this.decimalFormat.format(curSlider.getValue());

				curTextfield.setText(sliderValString);

				float	sliderValFloat	= Float.parseFloat(sliderValString);
				

				// Hue/Saturation/Brightness modulate
				if(this.firstHSBSliderId != -1 && 
						id >= this.firstHSBSliderId && id < (this.firstHSBSliderId + 3))
				{
					//			int pos = (id/2)-7;
					int pos = id - this.firstHSBSliderId;

					this.setHueSatBrightnessMod(pos, sliderValFloat);
					this.applyHSBModulate(this.colors, originalColors);
				}//hsb mod
				
				// Red Modulate/Green Modulate/Blue Modulate:
				if(this.firstRGBSliderId != -1 &&
						id >= this.firstRGBSliderId && id < (this.firstRGBSliderId + 3))
				{
					//			int	pos	= (id / 2) - 4;		
					int	pos	= id - this.firstRGBSliderId;	// red = 0, green = 1, blue = 2

					this.redGreenBlueMod[pos]	= sliderValFloat;
					this.applyColorModulate(this.colors, this.originalColors);
				} // red/green/blue mod

				else
				{
					this.useSliderVal(id, sliderValFloat);
				}
				
			} catch (NullPointerException npe) {
				System.out.println("ModuleTemplate.controlEvent - sliders: caught NullPoint - curTextfield = " + (Textfield)this.sidebarCP5.getController("textfield" + (id + 1)));
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

				// Do the following only in ModuleTemplate01:
				/*				// only call updateColors() for colorSelect Buttons:
				if(controlEvent.getId() >= this.firstColorSelectId && controlEvent.getId() <= this.lastColorSelectId)
				{
					this.updateColors(this.curColorStyle);
				}
				 */
			} else {

				this.sidebarCP5.setAutoDraw(true);
				this.sidebarCP5.getGroup("background").setVisible(false);
				this.displaySidebar();
			}

			this.sidebarCP5.getController("button" + (controlEvent.getId())).bringToFront();
			this.sidebarCP5.getController("colorWheel" + (controlEvent.getId() + 100)).bringToFront();
			this.sidebarCP5.getController("textfield" + (controlEvent.getId() + 200)).bringToFront();

			this.sidebarCP5.getController("colorWheel" + (controlEvent.getId() + 100)).setVisible(curButton.getBooleanValue());
			this.sidebarCP5.getController("textfield" + (controlEvent.getId() + 200)).setVisible(curButton.getBooleanValue());

			this.fillOriginalColors();
			this.fillHSBColors();
			this.resetModulateSlidersTextfields();
			this.applyColorModulate(this.colors, this.originalColors);

			// Not calling updateColors() here because it should only be called by colorSelect buttons:
			//			this.updateColors(this.curColorStyle);
		} // Color Select Buttons

		// ColorWheels
		if(id > 299 && id < 400)
		{
			// get current color:
			ColorWheel	curCW	= (ColorWheel)controlEvent.getController();


			int	rgbColor	= curCW.getRGB();
			Color	color	= new Color(rgbColor);

			// Set corresponding Textfield with color value:
			Textfield	curColorTF	= (Textfield)this.sidebarCP5.getController("textfield" + (controlEvent.getId() + 100));
			curColorTF.setText("rgb(" + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue() + ")");


			int	notePos;	// the position in colors that is to be changed.
			id	= controlEvent.getId();

			// canvas color (does not affect notes):
			if((id % 100) == (this.canvasColorSelectId % 100))
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


					// canvas color (does not affect notes):
					if((id % 100) == (this.canvasColorSelectId % 100))
					{
						System.out.println("	id % 100 = " + (id % 100));
						
						this.canvasColor[0]	= red;
						this.canvasColor[1]	= green;
						this.canvasColor[2]	= blue;
					}

					else
					{
						// TODO - totally error checking! Don't forget to change this back!!
//						int	notePos	= this.calculateNotePos(id);
						int	notePos	= 0;

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

			} catch(Exception e) {
				System.out.println("Sorry, that is not recognized as a valid color (note that colors must be defined by Integer values). Exception message: "
						+ e.getMessage());
			} // catch
		} // ColorWheel Textfields
	} // controlEvent
	
	protected abstract int calculateNotePos(int id);

	/**
	 * Sets the Sliders and Textfields for RGB and HSB color modulate to 0.
	 */
	protected void resetModulateSlidersTextfields()
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

	} // resetModulateSlidersTextfields

	public void useSliderVal(int id, float val)	{	}

	public int getCheckpoint()				{	return this.checkpoint;		}

	public void setCheckpoint(int newVal)	{	this.checkpoint	= newVal;	}

	public float getThreshold()				{	return this.threshold;		}

	public void setThreshold(float newVal)	{	this.threshold	= newVal;	}

	public float[][] getColors() {
		return colors;
	}

	// TODO: does this work or just create a pointer?
	public void setColors(float[][] colors) {
		this.colors = colors;
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
