package coreV2_visuals;

import java.util.Random;
import processing.*;
import coreV2.Canvas;
import controlP5.ControlEvent;
import coreV2.ColorFader;
import coreV2.ColorScheme;
import coreV2.ModuleDriver;
import coreV2.Visual;

/**
 * @author Cullen Kittams
 * 
 */
public class StarsVisual extends Visual{
	private Canvas canvas;
	private ColorFader colorFader;
	
	protected String[] controllers;
	protected String[] labels;
	
	private int[] canvasDimensions;//[x,y,width,height]
	
	private int numOfStars = 600;
	private Star[] stars = new Star[numOfStars];
	
	private int speed = 30;
	private int crawlSpeed = 50;
	private int streakThreshold = 500;
	private int starSize = 4;
	
	private Random generator = new Random();
	
	
	public StarsVisual (ModuleDriver moduleDriver) {
		
		super(moduleDriver, "Stars");
		
		this.controllers = new String[] {"speed", "crawlSpeed", "starSize", "streakThreshold"};
		this.labels = new String[] {"Speed", "Crawl Speed", "Star Size (in pixels)","Streak Threshold"};
		
		this.canvas = this.moduleDriver.getCanvas();
		this.colorFader = new ColorFader(0,0,0,255,this.parent);
		this.colorFader.setReleaseDuration(300);
		this.colorFader.setAttackDuration(300);
		canvasDimensions = canvas.getCanvasDimensions();
		
		
		System.out.println(controllers[0]);
		System.out.println(colorFader.getColor());
		
		for (int i = 0; i < stars.length; i++) {
			stars[i] = new Star();
		}
		
		this.addSliders();
		
	}
	
	private void addSliders()
	{	
		this.cp5.addSlider(this.controllers[0], 1, 100)
		.setValue(30)
		.setDecimalPrecision(0)
		.getCaptionLabel().hide();
		this.cp5.addLabel(this.labels[0]);
		
		this.cp5.addSlider(this.controllers[1], 0, 100)
		.setValue(50)
		.setDecimalPrecision(0)
		.getCaptionLabel().hide();
		this.cp5.addLabel(this.labels[1]);
		
		this.cp5.addSlider(this.controllers[2], 1, 25)
		.setValue(4)
		.setDecimalPrecision(0)
		.getCaptionLabel().hide();
		this.cp5.addLabel(this.labels[2]);
		
		this.cp5.addSlider(this.controllers[3], 1, 100)
		.setValue(50)
		.setDecimalPrecision(0)
		.getCaptionLabel().hide();
		this.cp5.addLabel(this.labels[3]);
		

	}
	
	
	@Override
	public void controlEvent(ControlEvent theEvent) {
		
		
		if(theEvent.getName() == "speed"){
			System.out.println("Setting " + theEvent.getName());
			speed = (int)theEvent.getValue();	
		}
		
		if(theEvent.getName() == "crawlSpeed"){
			System.out.println("Setting " + theEvent.getName());
			crawlSpeed = (int)theEvent.getValue();	
		}

		if(theEvent.getName() == "starSize"){
			System.out.println("Setting " + theEvent.getName());
			starSize = (int)theEvent.getValue();
		}	
		if(theEvent.getName() == "streakThreshold"){
			System.out.println("Setting " + theEvent.getName());
			streakThreshold = (int)theEvent.getValue();
		}		
		
		
	}
	
	@Override
	public void drawVisual() {
		
		//draw the background and set it black
		this.parent.fill(0);
		this.canvas.background();

		int note = this.moduleDriver.getInputHandler().getAllMidiNotes()[0][0];//pitch, channel 1
		
		int[] scale = this.moduleDriver.getInputHandler().getScale();
		int[] pitchColor = new int[] {-1,-1,-1};
		ColorScheme[] schemes = this.moduleDriver.getColorMenu().getColorSchemes();
		
		float radius;
		int prevX;
		int prevY;
		
		//check if the note is in the scale
		for(int i = 0; i < scale.length; i++){ 
			if((note%12) == i){
				pitchColor = schemes[0].getPitchColor(i);
			}
		}

		//if the note was in the scale then set the color in the colorFader
		if(pitchColor[0] != -1){
			colorFader.setTargetColor(pitchColor.clone());
		}

		//if the velocity is greater than the piano threshold then set the alpha to 255
		if(this.moduleDriver.getInputHandler().getAllMidiNotes()[0][1] > 0){
			colorFader.setTargetAlpha(255);
		}
		else {// if not, set the alpha to 0
			colorFader.setTargetAlpha(0);
		}
		int[] curColor = colorFader.getColor();
		//get the current color from color fader and fill it into the canvas
		if(this.moduleDriver.getAmplitude() != 0) {
			
			this.parent.fill(curColor[0], curColor[1], curColor[2], curColor[3]);//rgb alpha
		} else {
			
			this.parent.fill(255);
		}
		
		
		
		this.parent.translate(canvasDimensions[2]/2, canvasDimensions[3]/2);	
		
		for (int i = 0; i < this.stars.length; i++) {
			
			//update the stars
			
			stars[i].zPos = (float) ((int) stars[i].zPos - (this.moduleDriver.getAmplitude()* (speed/100.0)) - crawlSpeed/100.0);// the size of the
			
			
			if (stars[i].zPos < 1) {
				//reconstructs the star so it is put in a new starting position when if moves offscreen
				stars[i].xPos = generator.nextInt((int)canvasDimensions[2]) - canvasDimensions[2]/2;
				stars[i].yPos = generator.nextInt((int)canvasDimensions[3]) - canvasDimensions[3]/2;
				stars[i].zPos = generator.nextInt(canvasDimensions[2]/2);
				stars[i].prevZ = stars[i].zPos;
			}
			//draw the stars
			
			int newX = (int)((stars[i].xPos/stars[i].zPos) * canvasDimensions[2]/2 );
			int newY = (int)((stars[i].yPos/stars[i].zPos) * canvasDimensions[3]/2);
			radius =  starSize - (stars[i].zPos/canvasDimensions[2])* starSize; // takes the 
			 
			canvas.ellipse(newX, newY , (int)radius, (int)radius);
			prevX = (int) ((stars[i].xPos/stars[i].prevZ) * canvasDimensions[2]/2);
			prevY = (int) ((stars[i].yPos/stars[i].prevZ) * canvasDimensions[3]/2);
			//System.out.println("X" + prevX);
			//System.out.println("Y" + prevY);
			
			//drawing the lines
			if(this.moduleDriver.getAmplitude() > streakThreshold) {
				int length;
				this.parent.stroke(curColor[0], curColor[1], curColor[2]);
				length = ((newX - prevX)*(newX - prevX)) + ((newY - prevY)*(newY - prevY));
				if( length > 10000) { //if the length if the line is greater than 100
					
				}
				canvas.line(newX, newY, prevX ,prevY) ;
				
				
			}
		}
		
				
		//This translates back to the original point so the normal overlays can be drawn on the correct places		
		this.parent.translate( -1 * canvasDimensions[2]/2, -1 * canvasDimensions[3]/2);
				
				
				
				
				
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
	
	public class Star{
		
		float xPos; //x position of where the star is.
		float yPos; //y position of where the star is.
		float zPos; // not really a true z position but gives the illusion of depth
		float prevZ; // this is used to trace a line and create the  streak of the Star
		
		
		
		public Star() {
			xPos = generator.nextInt((int)canvasDimensions[2]) + canvasDimensions[2]/2;
			yPos = generator.nextInt((int)canvasDimensions[3] + canvasDimensions[3]/2);
			zPos = generator.nextInt(canvasDimensions[2]/2);
			prevZ = zPos;
		}
		
		
		
		
	}

	
	
}

