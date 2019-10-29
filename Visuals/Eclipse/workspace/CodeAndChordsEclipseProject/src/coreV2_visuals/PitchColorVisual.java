package coreV2_visuals;

import controlP5.ControlEvent;
import controlP5.Controller;
import controlP5.Slider;
import coreV2.Canvas;
import coreV2.ColorFader;
import coreV2.ColorScheme;
import coreV2.InputHandler;
import coreV2.ModuleDriver;
import coreV2.Visual;

public class PitchColorVisual extends Visual {

	private Canvas				canvas;
	private ColorFader 			colorFader;
	
	protected String[]			controllers;
	protected String[]			labels;

	public PitchColorVisual(ModuleDriver moduleDriver) 
	{
		super(moduleDriver, "Pitch Color");

		this.addSliders();
		this.canvas = this.moduleDriver.getCanvas();
		//this.colorFader = new ColorFader(this.parent);
		this.colorFader = new ColorFader(0,0,0,255,this.parent);
	}

	private void addSliders()
	{
		this.controllers = new String[] {"transition", "attack", "release"};
		this.labels = new String[] {
				"Transition Time (ms)", "Attack Time (ms)", "Release Time (ms)"};

		for(int i = 0; i < this.labels.length; i++)
		{
			this.cp5.addLabel(this.labels[i]).setTab(this.tabName);
		}
		
		
		/*
		this.cp5.addScrollableList("numChannels")
		//.setPosition(170, this.parent.height * 3/18 -10)
		.setWidth(this.parent.width/3 - (60 + 140))
		.setBarHeight(25)
		.setItemHeight(30)
		.setHeight(100)
		.setItems(new String[] {"1", "2", "3", "4", "5", "6", "7", "8",})
		.setValue(0)
		.close();
		
		*/
		this.cp5.addSlider("transition")
		.setMin(30)
		.setMax(1500)
		.setDecimalPrecision(0)
		.setValue(500)
		.setTab(this.tabName)
		.getCaptionLabel().setVisible(false);


		this.cp5.addSlider("attack")
		.setMin(30)
		.setMax(1500)
		.setDecimalPrecision(0)
		.setValue(500)
		.setTab(this.tabName)
		.getCaptionLabel().setVisible(false);

		this.cp5.addSlider("release")
		.setMin(30)
		.setMax(1500)
		.setDecimalPrecision(0)
		.setValue(500)
		.setTab(this.tabName)
		.getCaptionLabel().setVisible(false);

		
	}

	@Override
	public void controlEvent(ControlEvent theEvent) 
	{
		if(theEvent.getName() == "transition" && this.colorFader != null)
		{
			System.out.println("Setting " + theEvent.getName());
			this.colorFader.setTransitionDuration((int)theEvent.getValue());
		}

		if(theEvent.getName() == "attack" && this.colorFader != null)
		{
			System.out.println("Setting " + theEvent.getName());
			this.colorFader.setAttackDuration((int)theEvent.getValue());
		}

		if(theEvent.getName() == "release" && this.colorFader != null)
		{
			System.out.println("Setting " + theEvent.getName());
			this.colorFader.setReleaseDuration((int)theEvent.getValue());
		}
	}

	@Override
	public void drawVisual() 
	{
		//draw the background
		this.parent.fill(0);
		this.canvas.background();


		int 	note 		= this.moduleDriver.getInputHandler().getAllMidiNotes()[0][0];
		int[] 	scale 		= this.moduleDriver.getInputHandler().getScale();
		int[] 	pitchColor 	= new int[] {-1,-1,-1};
		ColorScheme[] schemes = this.moduleDriver.getColorMenu().getColorSchemes();

		//check if the note is in the scale
		for(int i = 0; i < scale.length; i++)
		{ 
			if((note%12) == scale[i])
			{
				pitchColor = schemes[0].getPitchColor(scale[i]);
			}
		}

		//if the note was in the scale then set the color in the colorFader
		if(pitchColor[0] != -1)
		{
			colorFader.setTargetColor(pitchColor.clone());
		} else {
			colorFader.setTargetColor(0, 0, 0);
		}

		//if the velocity is greater than the piano threshold then set the alpha to 255
		if(this.moduleDriver.getInputHandler().getAllMidiNotes()[0][1] > 0)
		{
			colorFader.setTargetAlpha(255);
		}
		else // if not, set the alpha to 0
		{
			colorFader.setTargetAlpha(0);
		}

		//get the current color from color fader and fill it into the canvas
		int[] curColor = colorFader.getColor();
		this.parent.fill(curColor[0], curColor[1], curColor[2], curColor[3]);
		this.canvas.background();
	}

	@Override
	public int getNumControllers() 
	{
		return this.controllers.length;
	}

	@Override
	public String getControllerName(int controllerNum) 
	{
		return this.controllers[controllerNum];
	}

	@Override
	public String getLabelName(int controllerNum) 
	{
		return this.labels[controllerNum];
	}

}
