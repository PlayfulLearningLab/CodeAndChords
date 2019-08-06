package coreV2_visuals;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import controlP5.ControlEvent;
import controlP5.ControlListener;
import coreV2.ColorFader;
import coreV2.ModuleDriver;
import coreV2.Visual;
import processing.core.PConstants;

public class GlitchVisual extends Visual{

	private String[] 				controllers;
	private String[] 				labels;
	
	private int 					numChannels;
	private int[]					xPos;
	
	private LinkedList<int[]>[]		colors;
	
	private int						timeColorAddedLast;
	private int						rectSize;
	
	private int						numShapes;
	private int						colorFade;
	private int						sizeIncrease;
	private int						randomness;
	

	public GlitchVisual(ModuleDriver moduleDriver) 
	{
		super(moduleDriver, "Glitch");
		
		this.controllers = new String[] {"numRects", "colorFade", "sizeIncrease", "randomness"};
		this.labels = new String[] {"Number of Rectangles", "Color Fade", 
										"Size Increase", "Randomness2"};
		
		this.numChannels = 1;
		this.colors = new LinkedList[] {new LinkedList<int[]>()};
		
		this.xPos = new int[] {this.parent.width/2};
		this.rectSize = 100;
		this.timeColorAddedLast = 0;
		
		this.numShapes = 30;
		this.colorFade = 50;
		this.sizeIncrease = 10;
		this.randomness = 10;
		
		this.makeControls();
	}
	
	private void makeControls()
	{
		this.cp5.addSlider(this.controllers[0], 10, 100)
		.setValue(30)
		.getCaptionLabel().hide();
		this.cp5.addLabel(this.labels[0]);
		
		this.cp5.addSlider(this.controllers[1], 10, 200)
		.setValue(50)
		.getCaptionLabel().hide();
		this.cp5.addLabel(this.labels[1]);
		
		this.cp5.addSlider(this.controllers[2], 0, 25)
		.setValue(10)
		.getCaptionLabel().hide();
		this.cp5.addLabel(this.labels[2]);
		
		this.cp5.addSlider(this.controllers[3], 0, 50)
		.setValue(0)
		.getCaptionLabel().hide();
		this.cp5.addLabel(this.labels[3]);
	
	}

	@Override
	public void controlEvent(ControlEvent theEvent) {
		if(theEvent.getName()=="numChannels"){
			this.numChannels = (int) theEvent.getValue() + 1;
			this.xPos = new int[this.numChannels];
			
			int xVal = this.parent.width / this.numChannels;
			
			this.colors = new LinkedList[this.numChannels];
			
			for(int i = 0; i < this.numChannels; i++) {
				this.xPos[i] = i*xVal + (xVal/2);
				this.colors[i] = new LinkedList<int[]>();
			}
			
			this.rectSize = xVal/2;
		}
		else if(theEvent.getName()==  this.controllers[0]) //number of rectangles
		{
			this.numShapes = (int) theEvent.getValue();
		}
		else if(theEvent.getName()==  this.controllers[1]) //color fade
		{
			this.colorFade = (int) theEvent.getValue();
		}
		else if(theEvent.getName()==  this.controllers[2]) //size increase
		{
			this.sizeIncrease = (int) theEvent.getValue();
		}
		else if(theEvent.getName()==  this.controllers[3]) //randomness
		{
			this.randomness = (int) theEvent.getValue();
		}

	}

	@Override
	public void drawVisual() 
	{
		this.parent.rectMode(PConstants.CENTER);
		
		int[][] note = this.moduleDriver.getInputHandler().getAllMidiNotes();
		
		boolean addNewColor = false;
		if(this.parent.millis() > this.timeColorAddedLast + this.colorFade) {
			addNewColor = true;
			this.timeColorAddedLast = this.parent.millis();
		}
		
		for(int i = 0; i < this.numChannels; i++) {
			
			if(addNewColor) {
				int[] color = this.moduleDriver.getColorMenu().getColorSchemes()[0].getPitchColor(note[i][0]%12);
				this.colors[i].addFirst(color);
				
				while(this.colors[i].size() > this.numShapes) {
					this.colors[i].removeLast();
				}
			}
			
			this.drawVisualForInput(i, this.xPos[i], this.parent.height/2);
		}
		
		this.parent.rectMode(PConstants.CORNER);
	}
	
	private void drawVisualForInput(int channelNumber, int centerX, int centerY) 
	{	
		Iterator<int[]> iter = this.colors[channelNumber].iterator();
		int[] color;
		
		int i = 0;
		
		int size = this.rectSize;
		int alpha = 255;
		int randX = 0;
		int randY = 0;
		
		this.parent.noFill();
		
		while(iter.hasNext()) {
			color = iter.next();
			this.parent.stroke(color[0], color[1], color[2], alpha);
			
			this.moduleDriver.getCanvas().rect(centerX + randX, centerY + randY, size, size);
			
			i++;
			
			size += this.sizeIncrease;
			
			randX = (int) ((Math.random() - .5) * (float)this.randomness);
			randY = (int) ((Math.random() - .5) * (float)this.randomness);
			alpha = (int) (255 - 255*((float)i/(float)this.numShapes));
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
