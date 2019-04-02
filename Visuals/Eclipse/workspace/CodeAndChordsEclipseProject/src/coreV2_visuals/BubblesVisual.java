package coreV2_visuals;

import controlP5.ControlEvent;
import coreV2.ColorFader;
import coreV2.ModuleDriver;
import coreV2.Visual;
import processing.core.PApplet;

public class BubblesVisual extends Visual 
{
	
	private String[] 		controllers;
	private String[]		labels;
	
	private int				numChannels;
	
	private Bubbles[]		bubble;
	private ColorFader[]	colors;
	
	private int[]		activationOrder;
		
	public BubblesVisual(ModuleDriver moduleDriver) 
	{
		super(moduleDriver, "Bubbles");
		
		this.numChannels = 1;
		this.bubble = new Bubbles[numChannels];
		this.activationOrder = new int[numChannels];
		
		this.colors = new ColorFader[numChannels];
		
		for(int i = 0; i < this.bubble.length; i++)
		{
			this.bubble[i] = new Bubbles(this.parent);
			this.activationOrder[i] = i;
			this.colors[i] = new ColorFader(this.parent);
		}
		this.makeControllers();
	}
	
	private void makeControllers()
	{
		this.controllers = new String[]{"speed"};
		this.labels = new String[]{"speed"};
		
		this.cp5.addSlider("speed")
		.setMin(0)
		.setMax(40)
		.setValue(25)
		.getCaptionLabel().setVisible(false);
		
		this.cp5.addLabel("speed");
	}

	@Override
	public void controlEvent(ControlEvent theEvent) 
	{
		if(theEvent.getName() == "numChannels")
		{
			this.setNumChannels((int) theEvent.getValue() + 1);
		}
		
		if(theEvent.getName() == "speed")
		{
			for(int i = 0; i < this.numChannels; i++)
			{
				this.bubble[i].speed = (int) theEvent.getValue();
			}
		}
	}
	
	private void setNumChannels(int numChannels)
	{
		this.numChannels = numChannels;
		this.bubble = new Bubbles[numChannels];
		this.activationOrder = new int[numChannels];
		
		this.colors = new ColorFader[numChannels];
		//System.out.println("bub = " + this.bubble.length);
		for(int i = 0; i < this.bubble.length; i++)
		{
			this.bubble[i] = new Bubbles(this.parent);
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
			if(this.bubble[this.activationOrder[i]].width == 0)
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
		//System.out.println(this.activationOrder.length);
		for(int i = this.activationOrder.length-1; i >= 0; i--)
		{
			channel = this.activationOrder[i];
			//System.out.println(channel);
			if((channel < 4) && midiNotes[channel][1] != -1)
			{
				//calculate and set goal width
				this.bubble[channel].targetWidth = (int) (((float)this.parent.width/2/(float)this.numChannels) * (float)midiNotes[channel][1]/100f);
				
				//calculate and set goal width
				this.bubble[channel].targetHeight = (int) (((float)this.parent.height/2/(float)this.numChannels) * (float)midiNotes[channel][1]/100f);
				
				//setColor
				int[] targetColor = this.moduleDriver.getColorMenu().getColorSchemes()[0].getPitchColor(midiNotes[channel][0%12]);
				this.colors[channel].setTargetColor(targetColor);
				
			}
			else
			{
				this.bubble[channel].targetWidth = 0;
				this.bubble[channel].targetHeight = 0;
			}
			
			//update leftX
			this.bubble[channel].leftX = xIndex;
			xIndex += this.bubble[channel].width;
			
			//get current color
			int[] curColor = this.colors[channel].getColor();
			this.parent.fill(curColor[0], curColor[1], curColor[2]);
			
			//System.out.println(this.activationOrder.length);
			if(this.activationOrder.length == 1)
			{
				this.moduleDriver.getCanvas().ellipse(450, 250, (int) this.bubble[channel].width*2, (int) this.bubble[channel].width *2);
				//this.moduleDriver.getCanvas().ellipse((int) (this.parent.width/2 + 2 - this.bubble[channel].leftX), 250, (int) -this.bubble[channel].width*2,(int) this.bubble[channel].width*2);
			}
			else if(this.activationOrder.length == 2)
			{
				if(channel == 0)
				{
					this.moduleDriver.getCanvas().ellipse(250, 250, (int) this.bubble[channel].width*2, (int) this.bubble[channel].width *2);
					//this.moduleDriver.getCanvas().ellipse((int) (this.parent.width/2 + 2 - this.bubble[channel].leftX), 100, (int) -this.bubble[channel].width*2,(int) this.bubble[channel].width*2);
				}
				else if(channel == 1)
				{
					this.moduleDriver.getCanvas().ellipse(650, 250, (int) this.bubble[channel].width*2, (int) this.bubble[channel].width *2);
					//this.moduleDriver.getCanvas().ellipse((int) (this.parent.width/2 + 2 - this.bubble[channel].leftX), 250, (int) -this.bubble[channel].width*2,(int) this.bubble[channel].width*2);
				}
			}
			else if(this.activationOrder.length == 3)
			{
				if(channel == 0)
				{
					this.moduleDriver.getCanvas().ellipse(200, 100, (int) this.bubble[channel].width*2, (int) this.bubble[channel].width *2);
					//this.moduleDriver.getCanvas().ellipse((int) (this.parent.width/2 + 2 - this.bubble[channel].leftX), 100, (int) -this.bubble[channel].width*2,(int) this.bubble[channel].width*2);
				}
				else if(channel == 1)
				{
					this.moduleDriver.getCanvas().ellipse(450, 400, (int) this.bubble[channel].width*2, (int) this.bubble[channel].width *2);
					//this.moduleDriver.getCanvas().ellipse((int) (this.parent.width/2 + 2 - this.bubble[channel].leftX), 250, (int) -this.bubble[channel].width*2,(int) this.bubble[channel].width*2);
				}
				else if(channel == 2)
				{
					this.moduleDriver.getCanvas().ellipse(700, 100, (int) this.bubble[channel].width*2, (int) this.bubble[channel].width *2);
					//this.moduleDriver.getCanvas().ellipse((int) (this.parent.width/2 + 2 - this.bubble[channel].leftX), 250, (int) -this.bubble[channel].width*2,(int) this.bubble[channel].width*2);
				}
			}
			else if(this.activationOrder.length > 3)
			{
				if(channel == 0)
				{
					this.moduleDriver.getCanvas().ellipse(200, 100, (int) this.bubble[channel].width*2, (int) this.bubble[channel].width *2);
					//this.moduleDriver.getCanvas().ellipse((int) (this.parent.width/2 + 2 - this.bubble[channel].leftX), 100, (int) -this.bubble[channel].width*2,(int) this.bubble[channel].width*2);
				}
				else if(channel == 1)
				{
					this.moduleDriver.getCanvas().ellipse(200, 300, (int) this.bubble[channel].width*2, (int) this.bubble[channel].width *2);
					//this.moduleDriver.getCanvas().ellipse((int) (this.parent.width/2 + 2 - this.bubble[channel].leftX), 250, (int) -this.bubble[channel].width*2,(int) this.bubble[channel].width*2);
				}
				else if(channel == 2)
				{
					this.moduleDriver.getCanvas().ellipse(700, 100, (int) this.bubble[channel].width*2, (int) this.bubble[channel].width *2);
					//this.moduleDriver.getCanvas().ellipse((int) (this.parent.width/2 + 2 - this.bubble[channel].leftX), 250, (int) -this.bubble[channel].width*2,(int) this.bubble[channel].width*2);
				}
				else if(channel == 3)
				{
					this.moduleDriver.getCanvas().ellipse(700, 300, (int) this.bubble[channel].width*2, (int) this.bubble[channel].width *2);
					//this.moduleDriver.getCanvas().ellipse((int) (this.parent.width/2 + 2 - this.bubble[channel].leftX), 250, (int) -this.bubble[channel].width*2,(int) this.bubble[channel].width*2);
				}
			}
			//draw
			//this.moduleDriver.getCanvas().ellipse((int) (this.parent.width/2 + this.bubble[channel].leftX), 250, (int) this.bubble[channel].width*2, (int) this.bubble[channel].width *2);
			//this.moduleDriver.getCanvas().ellipse((int) (this.parent.width/2 + 2 - this.bubble[channel].leftX), 250, (int) -this.bubble[channel].width*2,(int) this.bubble[channel].width*2);

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
	
	public class Bubbles{
		
		private PApplet 	parent;
		
		private float		leftX;
		private float		width;
		private float		height;
		
		private int			targetWidth;
		private int 		targetHeight;
		
		private float		wError;
		
		private int			speed;
		
		public Bubbles(PApplet parent)
		{
			this.parent = parent;
			this.parent.registerMethod("pre", this);
			
			this.leftX = 0;
			//this.width = targetWidth;
			this.targetWidth = 0;
			this.targetHeight = 0;
			
			this.speed = 25;
		}
		
		public void pre()
		{
			this.wError = this.targetWidth - this.width;
			
			this.width += this.wError/(50 - this.speed);
			
			if(Math.abs(this.width) < 1) this.width = 0;
			//this.width = this.targetWidth;
			
			this.wError = this.targetHeight - this.height;
			
			this.height += this.wError/(50 - this.speed);
			
			if(Math.abs(this.height) < 1) this.height = 0;
		}
	}

}
