package coreV2_visuals;

import controlP5.ControlEvent;
import coreV2.ColorFader;
import coreV2.ModuleDriver;
import coreV2.Visual;
import processing.core.PApplet;

public class VerticalRegtanglesVisual extends Visual 
{
	
	private String[] 		controllers;
	private String[]		labels;
	
	private int				numChannels;
	
	private vertBar[]		bars;
	private ColorFader[]	colors;
	
	private int[]		activationOrder;
		
	public VerticalRegtanglesVisual(ModuleDriver moduleDriver) 
	{
		super(moduleDriver, "Vertical Rectangles");
		
		this.numChannels = 1;
		this.bars = new vertBar[numChannels];
		this.activationOrder = new int[numChannels];
		
		this.colors = new ColorFader[numChannels];
		
		for(int i = 0; i < this.bars.length; i++)
		{
			this.bars[i] = new vertBar(this.parent);
			this.activationOrder[i] = i;
			this.colors[i] = new ColorFader(this.parent);
		}
		
		this.makeControllers();
				
		
	}
	
	private void makeControllers()
	{
		this.controllers = new String[]{"growthSpeed"};
		this.labels = new String[]{"Growth Speed"};
		
		this.cp5.addSlider("growthSpeed")
		.setMin(0)
		.setMax(40)
		.setValue(25)
		.getCaptionLabel().setVisible(false);
		
		this.cp5.addLabel("Growth Speed");
	}

	@Override
	public void controlEvent(ControlEvent theEvent) 
	{
		if(theEvent.getName() == "numChannels")
		{
			this.setNumChannels((int) theEvent.getValue() + 1);
		}
		
		if(theEvent.getName() == "growthSpeed")
		{
			for(int i = 0; i < this.numChannels; i++)
			{
				this.bars[i].growthSpeed = (int) theEvent.getValue();
			}
		}
		
	}
	
	private void setNumChannels(int numChannels)
	{
		this.numChannels = numChannels;
		this.bars = new vertBar[numChannels];
		this.activationOrder = new int[numChannels];
		
		this.colors = new ColorFader[numChannels];
		
		for(int i = 0; i < this.bars.length; i++)
		{
			this.bars[i] = new vertBar(this.parent);
			this.activationOrder[i] = i;
			this.colors[i] = new ColorFader(this.parent);
		}
	}

	@Override
	public void drawVisual() 
	{
		int[][] midiNotes = this.moduleDriver.getInputHandler().getAllMidiNotes();
		
		//update activation order
		for(int i = 0; i < this.activationOrder.length - 1; i++)
		{
			if(this.bars[this.activationOrder[i]].width == 0)
			{
				int num = this.activationOrder[i];
				for(int i2 = i; i2 < this.activationOrder.length - 1; i2++)
				{
					this.activationOrder[i2] = this.activationOrder[i2 + 1];
				}
				this.activationOrder[this.activationOrder.length-1] = num;
			}
		}
		
		int xIndex = 0;
		int channel;
		
		for(int i = this.activationOrder.length-1; i >= 0; i--)
		{
			channel = this.activationOrder[i];
			
			if(midiNotes[channel][1] != -1)
			{
				//calculate and set goal width
				this.bars[channel].targetWidth = (int) (((float)this.parent.width/2/(float)this.numChannels) * (float)midiNotes[channel][1]/100f);
				
				//setColor
				int[] targetColor = this.moduleDriver.getColorMenu().getColorSchemes()[0].getPitchColor(midiNotes[channel][0%12]);
				this.colors[channel].setTargetColor(targetColor);
				
			}
			else
			{
				this.bars[channel].targetWidth = 0;
			}
			
			//update leftX
			this.bars[channel].leftX = xIndex;
			xIndex += this.bars[channel].width;
			
			//get current color
			int[] curColor = this.colors[channel].getColor();
			this.parent.fill(curColor[0], curColor[1], curColor[2]);
			
			//draw
			this.moduleDriver.getCanvas().rect((int) (this.parent.width/2 + this.bars[channel].leftX), 0, (int) this.bars[channel].width, this.parent.height);
			this.moduleDriver.getCanvas().rect((int) (this.parent.width/2 + 2 - this.bars[channel].leftX), 0, (int) -this.bars[channel].width, this.parent.height);

		}
		
	}

	@Override
	public int getNumControllers() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public String getControllerName(int controllerNum) {
		return this.controllers[controllerNum];
	}

	@Override
	public String getLabelName(int controllerNum) {
		return this.labels[controllerNum];
	}
	
	public class vertBar{
		
		private PApplet 	parent;
		
		private float		leftX;
		private float		width;
		
		private int			targetWidth;
		
		private float		wError;
		
		private int			growthSpeed;
		
		public vertBar(PApplet parent)
		{
			this.parent = parent;
			this.parent.registerMethod("pre", this);
			
			this.leftX = 0;
			this.width = 0;
			this.targetWidth = 0;
			
			this.growthSpeed = 25;
		}
		
		public void pre()
		{
			this.wError = this.targetWidth - this.width;
			
			this.width += this.wError/(50 - this.growthSpeed);
			
			if(Math.abs(this.width) < 1) this.width = 0;
		}
	}

}
