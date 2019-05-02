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
		this.controllers = new String[]{"growthSpeed", "maxWidth"};
		this.labels = new String[]{"Growth Speed", "Max Width"};
		
		this.cp5.addSlider("growthSpeed")
		.setMin(0)
		.setMax(40)
		.setValue(25)
		.getCaptionLabel().setVisible(false);
		
		this.cp5.addLabel("Growth Speed");
		
		
		this.cp5.addSlider("maxWidth")
		.setMin(0)
		.setMax(100)
		.setValue(25)
		.getCaptionLabel().setVisible(false);
		
		this.cp5.addLabel("Max Width");
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
		
		int widthSum = 0;
		
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
			else
			{
				widthSum += 2*this.bars[this.activationOrder[i]].width;
			}
		}
		
		widthSum += 2* this.bars[this.activationOrder[this.activationOrder.length-1]].width;
		
		float scale = 1;
		
		if(widthSum > this.parent.width)
		{
			scale = ((float)this.parent.width/ (float) widthSum);
		}
		
		int xIndex = 0;
		int channel;
		
		for(int i = this.activationOrder.length-1; i >= 0; i--)
		{
			channel = this.activationOrder[i];
			
			if(midiNotes[channel][1] != -1)
			{
				//calculate and set goal width
				this.bars[channel].targetWidth = (int) ((int) (((float)this.parent.width/2) * ((float)this.cp5.getController("maxWidth").getValue()/100f) * (float)midiNotes[channel][1]/100f) * scale);
				
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
			xIndex += this.bars[channel].width*scale;
			
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
		return this.controllers.length;
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
