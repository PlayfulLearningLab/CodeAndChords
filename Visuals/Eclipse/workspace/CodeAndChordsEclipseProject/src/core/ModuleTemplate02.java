package core;

import java.awt.Color;

import controlP5.ColorWheel;
import controlP5.ControlEvent;
import controlP5.ControlListener;
import controlP5.ControlP5;
import controlP5.Slider;
import controlP5.Textfield;
import processing.core.PApplet;
import core.Shape;

public class ModuleTemplate02 extends ModuleTemplate {

	// TODO: try the plugTo method to match slider vals to variables and simplify controlEvent

	private	int[]	yVals;

	private	float	forteThreshold;
	private	int	satForteThresh;
	private	int	brightnessForteThresh;
	private	int	saturation;
	private	int	brightness;

	private	int	firstThresholdSliderId	= -1;
	private	int	firstColorSelectCWId	= -1;

	// thresholds is not private so that the module can access it
	float[]	thresholds;

	public ModuleTemplate02(PApplet parent, Input input, String sidebarTitle)
	{
		super(parent, input, sidebarTitle);

		this.yVals		= new int[18];
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

		this.colors	= new float[this.totalRangeSegments][3];
		this.colors	= new float[][] {
			new float[] { 255, 0, 0 },
			new float[] { 0, 255, 0 },
			new float[] { 0, 0, 255 },
			new float[] { 150, 50, 150 },
		};
		/*		for(int i = 0; i < this.colors.length; i++)
		{
			for(int j = 0; j < this.colors[i].length; j++)
			{
				this.colors[i][j]	= 0;
			} // for - j
		} // for - i
		 */		
		//		this.spacer	= 5;

		// already called addHideButtons in superclass with y-val of 26.
		//		addInputThresholdSlider(this.yVals[1]);

		this.addSliders(this.yVals[1], this.yVals[3], this.yVals[4], this.yVals[5]);

		this.addShapeSizeSlider(this.yVals[2]);
		
		this.addRangeSegments(this.yVals[6], 4, 4, "Dynamic\nSegments");

		this.addColorSelectButtons(this.yVals[7]);

		this.addHSBSliders(new int[] { this.yVals[8], this.yVals[9], this.yVals[10] });

		int	verticalSpacer	= distance - this.sliderHeight;
		this.addThresholdSliders(yVals[11], verticalSpacer);
		//		this.initInput();
	} // constructor

	private void initInput()
	{
		this.setAttRelTranVal(0, 100);
		this.setAttRelTranVal(1, 100);
		this.setAttRelTranVal(2, 100);

		this.sidebarCP5.addButton("testButton");
	} // initInput

//	@Override
	protected void addHideButtons(int	hideY)
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

		float  sideWidth1   = (this.parent.width - leftEdgeX) / this.thresholds.length;
		float  sideHeight  = this.parent.width / 12;	// pretty arbitrary
		float	addToLastRect	= (this.parent.width - this.getLeftEdgeX()) - (sideWidth1 * this.thresholds.length);
		float	sideWidth2	= sideWidth1;

		this.parent.noStroke();

		// All notes but the last:
		for (int i = 0; i < this.thresholds.length; i++)
		{			
			if(i == this.thresholds.length - 1)
			{
				sideWidth2	= sideWidth1 + addToLastRect;
			}

			this.parent.fill(this.colors[i][0], this.colors[i][1], this.colors[i][2]);

			if (i == goalHuePos) {
				this.parent.rect(leftEdgeX + (sideWidth1 * i), (float)(this.parent.height - (sideHeight * 1.5)), sideWidth2, (float) (sideHeight * 1.5));
			} else {
				this.parent.rect(leftEdgeX + (sideWidth1 * i), this.parent.height - sideHeight, sideWidth2, sideHeight);
			}

			this.parent.fill(0);
			this.parent.text(this.thresholds[i], (float) (leftEdgeX + (sideWidth1 * i) + (sideWidth1 * 0.1)), this.parent.height - 20);
		} // for

		/*
			// TODO: remove after fixing trichrom-maj/minor bug:
			// Testing to see what's really in colors:
			for(int i = 0; i < this.colors.length; i++)
			{
				this.parent.fill(this.colors[i][0], this.colors[i][1], this.colors[i][2]);
				this.parent.ellipse(this.parent.width / 3 * 2, i * 30 + 60, 30, 30);
			} // for
		 */
	} // legend

	private void addThresholdSliders(int yVal, int verticalSpacer)
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
				"Color: Forte\nThreshold",
				"Saturation",
				"Sat: Forte\nThreshold",
				"Brightness",
				"Bright: Forte\nThreshold"
		}; // labels

		/*		this.thresholds[this.thresholds.length - 1]	= defaultThreshold;
		for(int i = 0; i < this.thresholds.length; i++)
		{
			this.thresholds[i]	= this.threshold + (i * (defaultThreshold / this.thresholds.length));
		}
		 */

		this.firstThresholdSliderId	= this.nextSliderId;

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
				.setRange(10, 7000)
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
				.setId(this.nextSliderId)
				.getCaptionLabel().setVisible(false);

				this.nextSliderId	= this.nextSliderId + 1;
				// Also need to increment nextSTextfieldId so that they don't get out of sync
				// (since this slider had no connected Textfield).
				this.nextSTextfieldId	= this.nextSTextfieldId + 1;
			} // if - "As-Is, percent sliders
		} // for

	} // addThresholdSliders

	private	void addColorSelectButtons(int yVal)
	{
		int	colorSelectSpace	= 5;
		int	colorSelectWidth	= (((this.parent.width / 3) - this.leftAlign - 10) / 5) - colorSelectSpace;
		int	textfieldWidth		= 100;

		int	labelX			= 10;

		int canvasX	= this.leftAlign;
		int x1		= this.leftAlign + colorSelectWidth + colorSelectSpace;
		int x2		= this.leftAlign + (colorSelectWidth + colorSelectSpace) * 2;
		int x3		= this.leftAlign + (colorSelectWidth + colorSelectSpace) * 3;
		int x4		= this.leftAlign + (colorSelectWidth + colorSelectSpace) * 4;

		String[]	labels	= new String[] { 
				"Canvas",
				"1",
				"2",
				"3",
				"4"
		};

		int[]	xVals	= new int[] {
				canvasX,
				x1,
				x2,
				x3,
				x4
		};

		this.sidebarCP5.addTextlabel("colorSelect")
		.setPosition(labelX, yVal + 4)
		.setGroup("sidebarGroup")
		.setValue("Color Select");

		this.canvasColorSelectId	= this.nextButtonId;
		this.firstColorSelectId		= this.canvasColorSelectId;
		this.firstColorSelectCWId	= this.nextColorWheelId;

		float[]	curColor	= new float[3];

		for(int i = 0; i < labels.length; i++)
		{
			if(i == 0) {
				curColor[0] = this.canvasColor[0];
				curColor[1] = this.canvasColor[1];
				curColor[2] = this.canvasColor[2];
			} else {
				curColor	= this.colors[i - 1];
			}

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

	protected int calculateNotePos(int pos)
	{
		return -1;
	}
	
	/**
	 * Uses this.threshold, this.forteThreshold and this.curRangeSegments 
	 * to recalculate the length of and values within this.thresholds.
	 */
	private	void resetThresholds()
	{
		float	segmentValue	= (this.forteThreshold - this.threshold) / (this.curRangeSegments - 1);
		System.out.println("dynamic segment buttons: forteThreshold = " + this.forteThreshold + 
				"; segmentValue = " + segmentValue);

		this.thresholds	= new float[this.curRangeSegments];
		for(int i = 0; i < this.thresholds.length; i++)
		{
			this.thresholds[i]	= this.threshold + segmentValue * i;
			System.out.println("	this.thresholds[" + i + "] = " + this.thresholds[i]);

		} // for
	}

	//	public void moduleTemplateControlEvent(ControlEvent controlEvent)
	public void controlEvent(ControlEvent controlEvent)
	{
		super.controlEvent(controlEvent);

		//		System.out.println("ModuleTemplate02.controlEvent: controlEvent = " + controlEvent);

		int	id	= controlEvent.getId();

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
/*
		// Forte threshold slider
		if(this.firstThresholdSliderId > -1 && id == this.firstThresholdSliderId)
		{
			this.forteThreshold	= controlEvent.getValue();

			float	segmentValue	= (this.forteThreshold - this.threshold) / this.totalRangeSegments;

			System.out.println("forte threshold slider: segmentValue = " + segmentValue);

			if(this.thresholds == null)
			{
				if(this.totalRangeSegments == 0)	{	this.totalRangeSegments	= 2;	}
				this.thresholds	= new float[this.totalRangeSegments];
			}

			this.thresholds[this.thresholds.length - 1]	= controlEvent.getValue();
			for(int i = 0; i < this.thresholds.length; i++)
			{
				this.thresholds[i]	= this.threshold + (segmentValue * i);
			} // for
		} // Forte threshold slider
*/
		// ColorWheels
		if(this.firstColorSelectCWId > -1 && 
				(id > this.firstColorSelectCWId) && id <= (this.firstColorSelectCWId + this.colors.length))
		{
			// get current color:
			ColorWheel	curCW	= (ColorWheel)controlEvent.getController();

			int	rgbColor	= curCW.getRGB();
			Color	color	= new Color(rgbColor);

			// Set corresponding Textfield with color value:
			Textfield	curColorTF	= (Textfield)this.sidebarCP5.getController("textfield" + (controlEvent.getId() + 100));
			curColorTF.setText("rgb(" + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue() + ")");

			int	colorPos	= id - this.firstColorSelectCWId - 1;
			this.colors[colorPos][0]	= color.getRed();
			this.colors[colorPos][1]	= color.getGreen();
			this.colors[colorPos][2]	= color.getBlue();
		} // ColorWheels
	} // controlEvent

	public float[] getThresholds()
	{
		return this.thresholds;
	}

} // ModuleTemplate02
