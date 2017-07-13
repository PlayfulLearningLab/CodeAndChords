package module_02;

import controlP5.ControlEvent;
import controlP5.ControlP5;
import core.Input;
import core.ModuleTemplate;
import processing.core.PApplet;

public class ModuleTemplate02 extends ModuleTemplate {
	
	int[]	yVals;
	
	int		spacer;	// determines the distance between the Sliders and Textfields

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
		
		this.spacer	= 10;
		
		// already called addHideButtons in superclass with y-val of 26.
		addInputThresholdSlider(this.yVals[1]);
		
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
	
	private	void addInputThresholdSlider(int yVal)
	{
		float	defaultInThresh	= 10;
		
		int	labelX		= 10;
		int	sliderWidth	= 170;
		
		int	sliderX		= this.leftAlign;
		int	textFieldX	= this.leftAlign + sliderWidth + this.spacer;
		
		
		this.sidebarCP5.addLabel("inputThreshold")
		.setPosition(labelX, yVal)
		.setValue("Input \nThreshold")
		.setGroup("sidebarGroup");
		
		this.sidebarCP5.addSlider("inputThreshold")
		.setPosition(sliderX, yVal)
		.setValue(defaultInThresh)
		.setGroup("sidebarGroup");
	} // addInputThresholdSlider

	public void useSliderVal(int id, float val)
	{
		
	} // useSliderVal
	
	protected int calculateNotePos(int pos)
	{
		return -1;
	}
	
	public void controlEvent(ControlEvent controlEvent)
	{
		super.controlEvent(controlEvent);
	} // controlEvent
} // ModuleTemplate02
