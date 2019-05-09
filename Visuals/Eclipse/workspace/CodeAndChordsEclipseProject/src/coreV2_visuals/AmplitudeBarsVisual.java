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

/*
import processing.core.*;
import core.Module;
import core.ModuleMenu;
import core.input.MicrophoneInput;
import net.beadsproject.beads.core.AudioContext;
*/

public class AmplitudeBarsVisual extends Visual 
{
	private Canvas				canvas;
	private ColorFader 			colorFader;
	
	protected String[]			controllers;
	protected String[]			labels;
	
	public AmplitudeBarsVisual(ModuleDriver moduleDriver) 
	{
		super(moduleDriver, "Amplitude Bars");

		//this.addSliders();
		this.canvas = this.moduleDriver.getCanvas();
		this.colorFader = new ColorFader(this.parent);
	}

	@Override
	public void controlEvent(ControlEvent theEvent) 
	{

	}

	@Override
	public void drawVisual() 
	{
	    this.canvas.background();
		int x;
		int x2;
		int y;
		int s1;
		int s2;
		int p;
		
		x = 200;
		x2 = 350;
		y = 100;
		s1 = 100;
		p = 4; //This is used to adjust the sensitivity of the getAmplitude() method 
		s2 = (int) (this.input.getAmplitude())/p;
		
		
		for(int i=0; i<5; i++) //This will draw 5 downward growing rectangles
		{
			if(s2>100) //This will cause s2 to not grow past the desired point
			{
				s2 = 300;
			}
			canvas.rectMode(CORNER); //This is used to draw the rectangle from the upper left corner 
			canvas.rect(x,y,s1,500+s2); 
			//this.moduleDriver.getCanvas().rect((int)(this.parent.width/2 + this. x,y,s1,500+s2);
			x = x + 300;
		}
		
		for(int j=0; j<5; j++) //This will draw 5 upward growing rectangles
		{
		
			if(s2>100) //This will cause s2 to not grow past the desired point
			{
				s2 = 200;
			}
			canvas.rectMode(CORNERS); //This is used to draw the rectangle from the upper left AND lower right corner
			canvas.rect(x2,300-s2,x2+100,900); 
		
			x2 = x2 + 300;
		}
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
