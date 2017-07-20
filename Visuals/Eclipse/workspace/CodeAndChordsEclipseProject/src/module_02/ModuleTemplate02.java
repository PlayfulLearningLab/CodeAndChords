package module_02;

import controlP5.ControlEvent;
import controlP5.ControlListener;
import controlP5.ControlP5;
import controlP5.Slider;
import core.Input;
import core.ModuleTemplate;
import processing.core.PApplet;

public class ModuleTemplate02 extends ModuleTemplate {
	
	// TODO: try the plugTo method to match slider vals to variables and simplify controlEvent
	
	private	int[]	yVals;
	
	private	int	forteThreshold;
	private	int	satForteThresh;
	private	int	brightnessForteThresh;
	private	int	saturation;
	private	int	brightness;
	
	private	int	firstThresholdSliderId	= -1;
	
	private	float[]	thresholds;

	public ModuleTemplate02(PApplet parent, Input input, String sidebarTitle)
	{
		super(parent, input, sidebarTitle);
		
		this.yVals		= new int[17];
		this.yVals[0]	= 26;
		int	distance	= (this.parent.height - this.yVals[0]) / this.yVals.length;
		for(int i = 1; i < this.yVals.length; i++)
		{
			this.yVals[i]	= this.yVals[i - 1] + distance;
		}
				
//		this.spacer	= 5;
		
		// already called addHideButtons in superclass with y-val of 26.
//		addInputThresholdSlider(this.yVals[1]);
		
		this.addSliders(this.yVals[1], this.yVals[3], this.yVals[4], this.yVals[5]);
		
		this.addShapeSizeSlider(this.yVals[2]);
		
		this.addRangeSegments(this.yVals[6], 4, 4, "Dynamic\nSegments");
		
		int	verticalSpacer	= distance - this.sliderHeight;
		this.addThresholdSliders(yVals[7], 3);
		
//		this.initInput();
	} // constructor
	
	private void initInput()
	{
		this.setAttRelTranVal(0, 100);
		this.setAttRelTranVal(1, 100);
		this.setAttRelTranVal(2, 100);
		
		this.sidebarCP5.addButton("testButton");
	} // initInput
	
	@Override
	protected void addHideButtons(int	hideY)
	{
		int	hideWidth   = 69;
		int hideSpace	= 4;

		int	labelX		= 10;
		int	playButtonX	= this.leftAlign;
		int	menuButtonX	= this.leftAlign + hideWidth + hideSpace;

		String[]	names	= new String[] { 
				"playButton",
				"menuButton"
		};
		String[]	labels	= new String[] {
				"Play Button",
				"Menu Button"
		};
		int[]	xVals	= new int[] {
				playButtonX,
				menuButtonX
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

	} // addHideButtons
	
	private void addThresholdSliders(int yVal, int verticalSpacer)
	{
		int	textfieldX	= this.leftAlign + this.sliderWidth + this.spacer;
		
		int	defaultThreshold	= 3000;
		
		if(this.totalRangeSegments <= 0)
		{
			this.totalRangeSegments	= 2;
		}
		this.thresholds	= new float[this.totalRangeSegments];	

		
		// Since some i's will add a couple rows of labels and sliders,
		// this variable keeps track of which "level" of y the next thing should be added to.
		float	yPos		= 0;
		
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
		
		String[]	sliderNames	= new String[] {
				"slider5",
//				"forteThreshold",
				"slider6",
				"slider7",
				"slider8",
				"slider9"
		}; // sliderNames
		
		String[]	textfieldNames	= new String[] {
				"textfield105",
//				"forteThresholdTF",
				"textfield106",
				"textfield107",
				"textfield108",
				"textfield109"
		}; // sliderNames

		this.forteThreshold	= defaultThreshold;
		this.thresholds[this.thresholds.length - 1]	= defaultThreshold;
		for(int i = 0; i < this.thresholds.length; i++)
		{
			this.thresholds[i]	= this.threshold + (i * (defaultThreshold / this.thresholds.length));
		}
		
		System.out.println("addThresholdSlider.... hello!");
		
		this.firstThresholdSliderId	= this.nextSliderId;
		System.out.println("firstThresholdSliderId = " + this.firstThresholdSliderId);
		
		for(int i = 0; i < names.length; i++)
		{
			System.out.println("i = " + i + "; yPos = " + yPos + "; names[" + i + "] = " + names[i]);
			
			this.sidebarCP5.addLabel(names[i])
			.setPosition(this.labelX, yVal + (yPos * (verticalSpacer + this.sliderHeight)))
			.setValue(labels[i])
			.setGroup("sidebarGroup");
			
			// Forte Thresholds
			if(i % 2 == 0)
			{
				this.sidebarCP5.addSlider("slider" + this.nextSliderId)
				.setPosition(this.leftAlign, yVal + (yPos * (verticalSpacer + this.sliderHeight)))
				.setSize(this.sliderWidth, this.sliderHeight)
				.setSliderMode(Slider.FLEXIBLE)
				.setRange(10, 7000)
				.setValue(defaultThreshold)
				.setLabelVisible(false)
//				.plugTo(this)
				.setId(this.nextSliderId)
				.setGroup("sidebarGroup");
				
				this.nextSliderId	= this.nextSliderId + 1;
				
				this.sidebarCP5.addTextfield("textfield" + this.nextSTextfieldId)
				.setPosition(textfieldX, yVal + (yPos * (verticalSpacer + this.sliderHeight)))
				.setWidth(this.textfieldWidth)
				.setText(this.sidebarCP5.getController("slider" + (this.nextSTextfieldId - 100)).getValue() + "")
				.setAutoClear(false)
				.setGroup("sidebarGroup")
//				.plugTo(this)
				.setId(this.nextSTextfieldId)
				.getCaptionLabel().setVisible(false);
				
				this.nextSTextfieldId	= this.nextSTextfieldId + 1;
				
				yPos	= yPos + 1.5f;
				System.out.println("	yPos = " + yPos);
			} // if - Forte Thresholds
			
			// "As-Is" labels, percent sliders
			if(i % 2 == 1)
			{
				this.sidebarCP5.addLabel(names[i] + "AsIs")
				.setPosition(this.leftAlign + (this.sliderWidth / 2),
						(float) (yVal + (yPos * (verticalSpacer + this.sliderHeight))))
				.setSize(this.sliderWidth, this.sliderHeight)
				.setValue("(As-Is)")
				.setGroup("sidebarGroup");
				
				yPos	= yPos + 1;
				System.out.println("	yPos = " + yPos);
				
				this.sidebarCP5.addSlider("slider" + this.nextSliderId)
				.setPosition(this.leftAlign, 
						(float) (yVal + (yPos * (verticalSpacer + this.sliderHeight)) - (verticalSpacer * 2)) )
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
				yPos	= yPos + 1;
				System.out.println("	yPos = " + yPos);
			} // if - "As-Is, percent sliders
		} // for
		// Add labels
		
		// Add pairs of sliders
	} // addThresholdSliders
	
	public void useSliderVal(int id, float val)
	{
		
	} // useSliderVal
	
	protected int calculateNotePos(int pos)
	{
		return -1;
	}
	
//	public void moduleTemplateControlEvent(ControlEvent controlEvent)
	public void controlEvent(ControlEvent controlEvent)
	{
		super.controlEvent(controlEvent);
		
		int	id	= controlEvent.getId();

		if(id == (this.firstThresholdSliderId + 100))
		{
			System.out.println("This is that first TF; what's the deal?");
		}
		
		if(id >= this.firstThresholdSliderId && id < (this.firstThresholdSliderId + this.totalRangeSegments))
		{
//			System.out.println("Cool! A threshold slider!");
		}
	} // controlEvent

} // ModuleTemplate02
