package core;

import java.awt.Color;

import controlP5.Background;
import controlP5.CColor;
import controlP5.ColorWheel;
import controlP5.ControlEvent;
import controlP5.ControlP5;
import controlP5.Controller;
import controlP5.ControllerGroup;
import controlP5.ControllerInterface;
import controlP5.Slider;
import controlP5.Textfield;
import controlP5.Toggle;
import processing.core.PApplet;

/**
 * July 2017
 * 
 * ModuleTemplate class for Module_02_AmplitudeHSB
 * 
 * @author Emily Meuer
 *
 */
public class ModuleTemplate02 extends ModuleTemplate {

	/**	holds the y values for all Controllers	*/
	private	int[]	yVals;

	/**	Amplitude thresholds	*/
	private	float[]	thresholds;

	/**	The highest amplitude threshold	*/
//	private	float	forteThreshold;

	/**	The minimum value for threshold Sliders	*/
//	private	float	minThreshold;

	/**	The id used to identify the Color/Brightness/Saturation threshold sliders	 */
//	private	int	firstThresholdSliderId	= -1;

	/**	Holds the values of the saturation threshold and brightness threshold Sliders, respectively	*/
	private	float[] satBrightThresholdVals;

	/**	Hodls the values of the saturation percent and brightness percent threshold Sliders, respectively	*/
//	private	float[]	satBrightPercentVals;


	/**
	 * Constructor
	 * 
	 * @param parent	the Module that uses this template
	 * @param input		the Input that the Module uses
	 * @param sidebarTitle	String indicating the title of this module
	 */
	public ModuleTemplate02(PApplet parent, Input input, String sidebarTitle)
	{
		super(parent, input, sidebarTitle, 4);

		this.shapeMenuIsOpen = false;

		this.yVals		= new int[18];
		// Seemed like a good starting position, related to the text - but pretty arbitrary:
		this.yVals[0]	= 26;
		int	distance	= (this.parent.height - this.yVals[0]) / this.yVals.length;
		for(int i = 1; i < this.yVals.length; i++)
		{
			this.yVals[i]	= this.yVals[i - 1] + distance;
		}

		// set amplitude thresholds
		this.thresholds	= new float[] {
				2,		// piano
				100,	// mezzo piano
				200,	// mezzo forte
				500	//forte
		}; // thresholds
		this.forteThreshold	= this.thresholds[this.thresholds.length - 1];
		this.minThreshold	= 101;

		this.satBrightThresholdVals	= new float[2];
		this.satBrightPercentVals	= new float[2];

		/*
		this.colors	= new float[][] {
			new float[] { 255, 0, 0 },
			new float[] { 0, 255, 0 },
			new float[] { 0, 0, 255 },
			new float[]	{ 255, 255, 0 }
//			new float[] { 150, 50, 150 },
		};


		this.legendColors	= new float[this.colors.length][3];
		for(int i = 0; i < this.colors.length; i++)
		{
			for(int j = 0; j < this.colors[i].length; j++)
			{
				this.legendColors[i][j]	= this.colors[i][j];
			} // for - j
		} // for - i
		 */

		// already called addHideButtons in superclass with y-val of 26.
		
		// Have to addColorSelect() first so that everything else can access the colors:
		String[]	buttonLabels	= new String[] {
				"Canvas", "1", "2", "3", "4"
		};
		this.addColorSelect(new int[] { this.yVals[7] }, buttonLabels, "Color Select", true);
		this.fillHSBColors();

		this.addSliders(this.yVals[1], this.yVals[3], this.yVals[4], this.yVals[5]);

		this.addShapeSizeSlider(this.yVals[2]);

		this.addRangeSegments(this.yVals[6], 4, 4, "Dynamic\nSegments");

		
//		this.addColorSelectButtons(this.yVals[7]);

		this.addHSBSliders(new int[] { this.yVals[8], this.yVals[9], this.yVals[10], });

		int	verticalSpacer	= distance - this.sliderHeight;
		this.addThresholdSliders(yVals[11], verticalSpacer);

		this.addShapeCustomizationControls(this.yVals[16]);
		//		this.initInput();
	} // constructor

	/**
	 * Overrides the generic ModuleTemplate addHideButtons(int) so that our legend Button 
	 * can be called "Thresholds" (rather than Module01's "Scale")
	 * 
	 * @param hideY	y value at which this row should be added
	 */
	@Override
	protected void addHideButtons(int hideY)
	{
		int	hideWidth   = 69;
		int hideSpace	= 4;

		int	labelX		= 10;
		int	playButtonX	= this.leftAlign;
		int	menuButtonX	= this.leftAlign + hideWidth + hideSpace;
		int	thresholdsX	= this.leftAlign + (( hideWidth + hideSpace) * 2);

		String[]	names	= new String[] { 
				"playButton",
				"menuButton",
				"legend"
		};
		String[]	labels	= new String[] {
				"Play Button",
				"Menu Button",
				"Thresholds"
		};
		int[]	xVals	= new int[] {
				playButtonX,
				menuButtonX,
				thresholdsX
		};

		this.sidebarCP5.addTextlabel("hide")
		.setPosition(labelX, hideY + 4)
		.setGroup("sidebarGroup")
		.setValue("Hide");

		for(int i = 0; i < names.length; i++)
		{
			this.sidebarCP5.addToggle(names[i])
			.setPosition(xVals[i], hideY)
			.setWidth(hideWidth)
			.setGroup("sidebarGroup");
			this.sidebarCP5.getController(names[i]).getCaptionLabel().set(labels[i]).align(ControlP5.CENTER, ControlP5.CENTER);
		}

		this.showScale	= true;
	} // addHideButtons


	/**
	 * Draws the thresholds legend at the bottom of the screen.
	 * 
	 * @param goalHuePos	current position in the threshold list; used to show the user their general amplitude level
	 */
	public void legend(int goalHuePos)
	{
		this.parent.textSize(24);

		float	sideWidth1   = (this.parent.width - this.leftEdgeX) / this.thresholds.length;
		float	sideHeight  = this.parent.width / 12;	// pretty arbitrary
		float	addToLastRect	= (this.parent.width - this.leftEdgeX) - (sideWidth1 * this.thresholds.length);
		float	sideWidth2	= sideWidth1;

		this.parent.noStroke();

		for (int i = 0; i < this.thresholds.length; i++)
		{
			if(i == this.thresholds.length - 1)
			{
				sideWidth2	= sideWidth1 + addToLastRect;
			}

			/*						System.out.println("this.colors[" + i + "][0] = " + this.colors[i][0] + "; this.colors[" + i + "][1] = " +
								this.colors[i][1] + "; this.colors[" + i + "][2] = " + this.colors[i][2]);
						System.out.println("this.legendColors[" + i + "][0] = " + this.legendColors[i][0] + "; this.legendColors[" + i + "][1] = " +
								this.legendColors[i][1] + "; this.legendColors[" + i + "][2] = " + this.legendColors[i][2]);
			 */
			//			this.parent.fill(this.colors[i][0], this.colors[i][1], this.colors[i][2]);
//			this.parent.fill(this.legendColors[i][0], this.legendColors[i][1], this.legendColors[i][2]);
			this.parent.fill(this.getColor(i)[0], this.getColor(i)[1], this.getColor(i)[2]);


			if (i == goalHuePos) {
				this.parent.rect(leftEdgeX + (sideWidth1 * i), (float)(this.parent.height - (sideHeight * 1.5)), sideWidth2, (float) (sideHeight * 1.5));
			} else {
				this.parent.rect(leftEdgeX + (sideWidth1 * i), this.parent.height - sideHeight, sideWidth2, sideHeight);
			}

			this.parent.fill(0);
			this.parent.text(this.thresholds[i], (float) (leftEdgeX + (sideWidth1 * i) + (sideWidth1 * 0.1)), this.parent.height - 20);
		} // for

	} // legend

	/**
	 * Adds the "Color: Forte Threshold", "Saturation", "Saturation: Forte Threshold", 
	 * "Brightness", and "Brightness: Forte Threshold" group of Sliders/Textfields
	 * 
	 * @param yVal	y value of forte threshold
	 * @param verticalSpacer	vertical space between sliders
	 */
/*	private void addThresholdSliders(int yVal, int verticalSpacer)
	{
		int	textfieldX	= this.leftAlign + this.sliderWidth + this.spacer;

		// Since some i's will add a couple rows of labels and sliders,
		// this variable keeps track of which "level" of y the next thing should be added to.
		//		float	yPos		= 0;

		String[]	names	= new String[] {
				"forteThresh",
				"saturation",
				"saturationForteThresh",
				"brightness",
				"brightnessForteThresh"
		}; // names

		String[]	labels = new String[] {
				"Forte\nThreshold",
				"Saturation",
				"Sat: Forte\nThreshold",
				"Brightness",
				"Bright: Forte\nThreshold"
		}; // labels
		/*	
		String[]	hsbSliderNames	= new String[] {
				"hueSlider",
				"saturationSlider",
				"brightnessSlider"

		}; // hsbSliderNames
		 */
/*		this.firstThresholdSliderId	= this.nextSliderId;

		for(int i = 0; i < names.length; i++)
		{			
			this.sidebarCP5.addLabel(names[i])
			.setPosition(this.labelX, yVal + (i * (verticalSpacer + this.sliderHeight)))
			.setValue(labels[i])
			.setGroup("sidebarGroup");

			// Forte Thresholds
			if(i % 2 == 0)
			{
				this.sidebarCP5.addSlider("slider" + this.nextSliderId)
				.setPosition(this.leftAlign, yVal + (i * (verticalSpacer + this.sliderHeight)))
				.setSize(this.sliderWidth, this.sliderHeight)
				.setSliderMode(Slider.FLEXIBLE)
				.setRange(this.minThreshold, 7000)
				.setValue(this.forteThreshold)
				.setLabelVisible(false)
				.setId(this.nextSliderId)
				.setGroup("sidebarGroup");

				this.nextSliderId	= this.nextSliderId + 1;

				this.sidebarCP5.addTextfield("textfield" + this.nextSTextfieldId)
				.setPosition(textfieldX, yVal + (i * (verticalSpacer + this.sliderHeight)))
				.setWidth(this.textfieldWidth)
				.setText(this.sidebarCP5.getController("slider" + (this.nextSTextfieldId - 100)).getValue() + "")
				.setAutoClear(false)
				.setGroup("sidebarGroup")
				.setId(this.nextSTextfieldId)
				.getCaptionLabel().setVisible(false);

				this.nextSTextfieldId	= this.nextSTextfieldId + 1;

			} // if - Forte Thresholds

			// percent sliders
			if(i % 2 == 1)
			{
				this.sidebarCP5.addSlider("slider" + this.nextSliderId)
				.setPosition(this.leftAlign, (yVal + (i * (verticalSpacer + this.sliderHeight))))
				.setSize(this.sliderWidth + this.spacer + this.textfieldWidth, this.sliderHeight)
				.setRange(-1, 1)
				.setValue(0)
				.setGroup("sidebarGroup")
				//				.plugTo(this)
				.setId(this.nextSliderId)
				.getCaptionLabel().setVisible(false);

				this.nextSliderId	= this.nextSliderId + 1;
				// Also need to increment nextSTextfieldId so that they don't get out of sync
				// (since this slider had no connected Textfield).
				this.nextSTextfieldId	= this.nextSTextfieldId + 1;
			} // if - percent sliders
		} // for

	} // addThresholdSliders
*/
	
	/**
	 * Adds the color select Buttons (allows selection of color for each threshold)
	 * 
	 * @param yVal	y value for this row of Buttons
	 */
	/*	private	void addColorSelectButtons(int yVal)
	{
		int	colorSelectSpace	= 5;
		int	colorSelectWidth	= (((this.parent.width / 3) - this.leftAlign - 10) / (this.totalRangeSegments + 1)) - colorSelectSpace;
		int	textfieldWidth		= 100;

		int	labelX			= 10;

		int x1			= this.leftAlign;
		int x2			= this.leftAlign + (colorSelectWidth + colorSelectSpace);
		int x3			= this.leftAlign + (colorSelectWidth + colorSelectSpace) * 2;
		int x4			= this.leftAlign + (colorSelectWidth + colorSelectSpace) * 3;
		int x5			= this.leftAlign + (colorSelectWidth + colorSelectSpace) * 4;
		//		int	backgroundX	= this.leftAlign;
		//		int canvasX		= this.leftAlign + ( ( (this.parent.width / 3) / 2 ) - this.leftAlign - 10);

		String[]	labels	= new String[] {
				"Canvas",
				"1",
				"2",
				"3",
				"4"
		};

		int[]	xVals	= new int[] {
				x1,
				x2,
				x3,
				x4,
				x5
		};

		this.sidebarCP5.addTextlabel("colorSelect")
		.setPosition(labelX, yVal + 4)
		.setGroup("sidebarGroup")
		.setValue("Color Select");

		this.canvasColorSelectId		= this.nextButtonId;
//		this.firstColorSelectId			= this.nextButtonId + 1;

		float[]	curColor	= new float[3];

//		int	yVal;
//		int	buttonWidth;

		for(int i = 0; i < labels.length; i++)
		{
/*			if(i == 4) {
				/*				curColor[0] = this.backgroundColor[0];
				curColor[1] = this.backgroundColor[1];
				curColor[2] = this.backgroundColor[2];
	 */
	/*				curColor	= new float[] { 255, 0, 0 };
//				this.backgroundColorSelectId	= this.nextButtonId;

				yVal		= yVal2;
				buttonWidth	= colorSelectWidth * 2;
			} else */
	/*			if(i == 0) {
				curColor[0] = this.canvasColor[0];
				curColor[1] = this.canvasColor[1];
				curColor[2] = this.canvasColor[2];

//				curColor	= new float[] { 150, 150, 50 };

//				yVal	= yVal2;
//				buttonWidth	= colorSelectWidth * 2;
			} else {
				curColor	= this.colors[i - 1];

//				yVal	= yVal1;
//				buttonWidth	= colorSelectWidth;
			}


			System.out.println("curColor[0] = " + curColor[0] + 
					"; curColor[1] = " + curColor[1] +
					"; curColor[2] = " + curColor[2]);

			this.sidebarCP5.addButton("button" + this.nextButtonId)
			.setPosition(xVals[i], yVal)
			.setWidth(colorSelectWidth)
			.setCaptionLabel(labels[i])
			.setGroup("sidebarGroup")
			.setId(this.nextButtonId)
			.getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);

			this.nextButtonId	= this.nextButtonId + 1;

			this.sidebarCP5.addColorWheel("colorWheel" + this.nextColorWheelId)
			.setPosition(xVals[i], yVal - 200)
			.setRGB(this.parent.color(curColor[0], curColor[1], curColor[2]))
			.setLabelVisible(false)
			.setVisible(false)
			.setGroup("sidebarGroup")
			.setId(this.nextColorWheelId);

			this.nextColorWheelId	= this.nextColorWheelId + 1;

			if(i > 3)	{	textfieldWidth	= textfieldWidth - colorSelectSpace;	}

			this.sidebarCP5.addTextfield("textfield" + this.nextCWTextfieldId)
			.setPosition(xVals[i] + colorSelectWidth + colorSelectSpace, yVal)
			.setWidth(textfieldWidth)
			.setAutoClear(false)
			.setVisible(false)
			.setText("rgb(" + curColor[0] + ", " + curColor[1] + ", " + curColor[2] + ")")
			.setGroup("sidebarGroup")
			.setId(this.nextCWTextfieldId)
			.getCaptionLabel().setVisible(false);

			this.nextCWTextfieldId	= this.nextCWTextfieldId + 1;
		} // for
	} // addColorSelectButtons
	 */

	/**
	 * Given the id of a ColorWheel or Textfield, returns the corresponding position in colors
	 */
	protected int calculateNotePos(int id)
	{
		// error checking
		if((id % 100) == (this.canvasColorSelectId % 100))
		{
			throw new IllegalArgumentException("ModuleTemplate.calculateNotePos: int parameter is canvasColorSelectId (" + this.canvasColorSelectId +
					"), which should not be sent to this method, since it does not designate a position in this.colors.");
		}
		/*		if((id % 100) == (this.backgroundColorSelectId % 100))
		{
			throw new IllegalArgumentException("ModuleTemplate.calculateNotePos: int parameter is backgroundColorSelectId (" + this.backgroundColorSelectId +
					"), which should not be sent to this method, since it does not designate a position in this.colors.");
		}
		 */
		// TODO - correct?
		return (id % 100) - (this.firstColorSelectCWId % 100);
	} // calculateNotePos

	/**
	 * Uses this.threshold, this.forteThreshold and this.curRangeSegments 
	 * to recalculate the length of and values within this.thresholds.
	 */
	private	void resetThresholds()
	{
		float	segmentValue;
		if(this.curRangeSegments == 1)
		{
			segmentValue	= this.threshold;
		} else {
			segmentValue	= (this.forteThreshold - this.threshold) / (this.curRangeSegments - 1);
		}

		//		System.out.println("dynamic segment buttons: forteThreshold = " + this.forteThreshold + 
		//				"; segmentValue = " + segmentValue);

		this.thresholds	= new float[this.curRangeSegments];
		for(int i = 0; i < this.thresholds.length; i++)
		{
			this.thresholds[i]	= this.threshold + segmentValue * i;
		} // for
	} // resetThresholds

	/**
	 * Applies the values of the threshold saturation and brightness Sliders 
	 * to the color of the ColorWheel at the given position and returns the affected color.
	 * 
	 * @param curAmp	the current amplitude
	 * @param colorPos	the position in this.colorSelect to which the sat/brightness should be applied
	 * @return			the color with saturation and brightness adjustments
	 */
/*	public float[] applyThresholdSBModulate(float curAmp, int colorPos)
	{
		// Error checking:
		if(colorPos < 0 || colorPos >= this.colorSelect.length) {
			throw new IllegalArgumentException("ModuleTemplate.applyThresholdSBModulate: int parameter colorPos is " + colorPos + 
					", which is out of bounds; must be between 0 and " + (this.colorSelect.length - 1));
		}
		// TODO: fill it every time?
		// No, that causes errors; but not filling it was the huge color mystery error.
		//		this.fillHSBColors();

		if(this.hsbColors == null)	{	this.fillHSBColors();	}
		//		this.applyHSBModulate(this.colors, this.hsbColors);

		// Converts the current amplitude into a number between 0 and 100,
		// depending on where curAmp is in relation to the saturation or brightness forte threshold:
		float	satMappingVal		= Math.max(Math.min(PApplet.map(curAmp, 0, Math.max(this.satBrightThresholdVals[0], this.minThreshold + 1), 0, 100), 100), 0);
		float	brightMappingVal	= Math.max(Math.min(PApplet.map(curAmp, 0, Math.max(this.satBrightThresholdVals[1], this.minThreshold + 1), 0, 100), 100), 0);

		// Notice how hueSatBrightPercentMod is accessed at 1 and 2, since hue is also a part of it,
		// but satBrightPercentVals is accessed at 0 and 1, since it is only for saturation and brightness.
		this.hueSatBrightPercentMod[1]	= (this.satBrightPercentVals[0] * satMappingVal) / 100;
		this.hueSatBrightPercentMod[2]	= (this.satBrightPercentVals[1] * brightMappingVal) / 100;

		float[] hsb = new float[3];

		//		for (int i = 0; i < this.colorSelect.length; i++)
		//		{
		// Converts this position of hsbColors from RGB to HSB:
		Color.RGBtoHSB((int)this.hsbColors[colorPos][0], (int)this.hsbColors[colorPos][1], (int)this.hsbColors[colorPos][2], hsb);

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

		/*			hsb[0] = (hsb[0] + this.hueSatBrightnessMod[0] + this.hueSatBrightPercentMod[0] + 1) % 1;
			hsb[1] = Math.max(Math.min(hsb[1] + this.hueSatBrightnessMod[1] + this.hueSatBrightPercentMod[1], 1), 0);
			hsb[2] = Math.max(Math.min(hsb[2] + this.hueSatBrightnessMod[2] + this.hueSatBrightPercentMod[2], 1), 0);
		 */
		// Converts the color back to RGB:
/*		int oc = Color.HSBtoRGB(hsb[0], hsb[1],  hsb[2]);
		Color a = new Color(oc);

		// Don't want to set the ColorWheel to this;
		// just want to use it in fade().....

		// Fills colors with the new color:
		/*			this.colors[i][0] = (float)a.getRed();
			this.colors[i][1] = (float)a.getGreen();
			this.colors[i][2] = (float)a.getBlue();
		 */
//	} // for
	
/*	return a.getColorComponents(null);
} // applyThresholdSBModulate
*/

/**
 * Used to catch ControlEvents from ControlP5 Controllers
 * 
 * @param controlEvent	the current ControlEvent
 */
public void controlEvent(ControlEvent controlEvent)
{
	super.controlEvent(controlEvent);

	//		System.out.println("ModuleTemplate02.controlEvent: controlEvent = " + controlEvent);

	int	id	= controlEvent.getId();

	/*
		if(id != this.canvasColorSelectId && id != this.backgroundColorSelectId && id > 299 && id < 400)
		{
			int	colorPos	= this.calculateNotePos(id);
			System.out.println("colors[colorPos][0] = " + this.colors[colorPos][0]);
		}
	 */

	// Re-calculate thresholds for dynamicSegment Buttons, lowThreshold and forteThreshold sliders:
	if( ( (this.firstRangeSegmentsId > -1) && 
			(id >= this.firstRangeSegmentsId) && id < (this.firstRangeSegmentsId + this.totalRangeSegments) )
			|| (this.firstThresholdSliderId > -1 && id == this.firstThresholdSliderId)
			|| (this.thresholdSliderId > -1 && id == this.thresholdSliderId) 
			)
	{
		if(id == this.firstThresholdSliderId) {
			this.forteThreshold	= controlEvent.getValue();
		}

		this.resetThresholds();
	} // dynamic segment buttons


	// Saturation and Brightness Percent Sliders:
	/*		if(controlEvent.getName().equals("saturationSlider") || 
				controlEvent.getName().equals("brightnessSlider"))
		{

			// The Sliders automatically set their corresponding variables,
			// so all we have to do is call applyHSBModulate():

			if(this.hsbColors == null)	{	this.fillHSBColors();	}			
			this.applyHSBModulate(this.colors, this.hsbColors);
		} // if - sat/brightness percent Sliders
	 */


	// Saturation and Brightness Threshold and Percent Sliders:
	if(this.firstThresholdSliderId != -1 &&
			( ( id > this.firstThresholdSliderId ) && ( id < this.firstThresholdSliderId + 5 ) ) )
	{
		int		arrayPos	= (id - this.firstThresholdSliderId - 1) / 2;
		/*			
			float	mappingVal;
			float	thresholdVal;
			float	curAmp		= this.input.getAmplitude();

			float	percentVal;
		 */

		// Percent Sliders
/*		if((id - this.firstThresholdSliderId) % 2 == 1)
		{
			this.satBrightPercentVals[arrayPos]		= controlEvent.getValue();
			this.satBrightThresholdVals[arrayPos]	= this.sidebarCP5.getValue("slider" + (id + 1));
			//				percentVal		= controlEvent.getValue();
		} else {
			// Threshold Sliders
			this.satBrightThresholdVals[arrayPos]	= controlEvent.getValue();
			this.satBrightPercentVals[arrayPos]		= this.sidebarCP5.getValue("slider" + (id - 1));
		}
*/
		//			System.out.println("thresholdVal = " + thresholdVal);
		//			System.out.println("percentVal = " + percentVal);
		/*			mappingVal	= PApplet.map(curAmp, 0, Math.max(thresholdVal, this.minThreshold + 1), 0, 100);
			System.out.println("mappingVal = " + mappingVal);

			this.hueSatBrightPercentMod[arrayPos]	= (percentVal * mappingVal) / 100;
		 */

		//			if(this.hsbColors == null)	{	this.fillHSBColors();	}	
		//			this.applyHSBModulate(this.colors, this.hsbColors);
	} // if - sat/brightness threshold Sliders

	// TODO:
	//		this.legend(0);

	/*
	 * So what's going on?
	 * 
	 * When I draw the legend in controlEvent but outside of any ifs, it will get the color
	 * that I'm currently changing correct.  But the other colors revert back to their
	 * original states!
	 * So: when I am actively moving a ColorWheel, it's updating,
	 * but when I stop, it "goes back" - how does it go back?
	 * 		I'll check ModuleTemplate.controlEvent...
	 * 		Nothing there.
	 * 
	 * And - I know that it has something to do with setting the canvas color?
	 * Not true; I just know that that defined that it couldn't show the last one;
	 * this might be a completely different issue.
	 * Actually... both canvasColor and backgroundColor work fine. :/
	 */

	// ColorWheels - now this all happens in ModuleTemplate (post 7/26):
	/*		if( (id > 299) && id <= 400)
		{
			// get current color:
			ColorWheel	curCW	= (ColorWheel)controlEvent.getController();

			int	rgbColor	= curCW.getRGB();
			Color	color	= new Color(rgbColor);

			// Set corresponding Textfield with color value:
			Textfield	curColorTF	= (Textfield)this.sidebarCP5.getController("textfield" + (controlEvent.getId() + 100));
			curColorTF.setText("rgb(" + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue() + ")");

			//			int	colorPos	= id - this.firstColorSelectCWId - 1;
			int colorPos	= this.calculateNotePos(id);
			this.colors[colorPos][0]	= color.getRed();
			this.colors[colorPos][1]	= color.getGreen();
			this.colors[colorPos][2]	= color.getBlue();

		} // ColorWheels
*/
	} // controlEvent

	if(controlEvent.getName() == "shapeMenuButton")
	{
		this.shapeMenuIsOpen = true;

	}//shapeMenuButton
} // controlEvent

/**
 * Getter for this.thresholds
 * 
 * @return	this.thresholds instance variable
 */
public float[] getThresholds()
{
	return this.thresholds;
}

public boolean getShapeMenuIsOpen()
{
	return this.shapeMenuIsOpen;
}

} // ModuleTemplate02
