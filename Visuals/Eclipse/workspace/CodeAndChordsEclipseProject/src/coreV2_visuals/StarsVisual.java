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
 * This visual 
 */
public class StarsVisual extends Visual{
	private Canvas canvas;
	private ColorFader colorFader;
	
	protected String[] controllers;
	protected String[] labels;
	
	private int[] canvasDimensions;//[x,y,width,height]
	
	private int numOfStars = 600;
	private Star[] stars = new Star[numOfStars];
	
	private Random generator = new Random();
	
	
	public StarsVisual (ModuleDriver moduleDriver) {
		
		super(moduleDriver, "Stars");
		this.canvas = this.moduleDriver.getCanvas();
		this.colorFader = new ColorFader(this.parent);
		this.colorFader.setReleaseDuration(300);
		this.colorFader.setAttackDuration(300);
		canvasDimensions = canvas.getCanvasDimensions();
		
		
		
		for (int i = 0; i < stars.length; i++) {
			stars[i] = new Star();
		}
		
	}

	@Override
	public void controlEvent(ControlEvent theEvent) {
		// TODO Auto-generated method stub
		
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
			
			stars[i].zPos = (float) ((int) stars[i].zPos - (this.moduleDriver.getAmplitude()* 0.15) - .01);// the size of the
			
			
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
			radius =  4 - (stars[i].zPos/canvasDimensions[2])* 4; // takes the 
			 
			canvas.ellipse(newX, newY , (int)radius, (int)radius);
			prevX = (int) ((stars[i].xPos/stars[i].prevZ) * canvasDimensions[2]/2);
			prevY = (int) ((stars[i].yPos/stars[i].prevZ) * canvasDimensions[3]/2);
			//System.out.println("X" + prevX);
			//System.out.println("Y" + prevY);
			
			//drawing the lines
			if(this.moduleDriver.getAmplitude() > 500) {
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
	public int getNumControllers() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getControllerName(int controllerNum) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLabelName(int controllerNum) {
		// TODO Auto-generated method stub
		return null;
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

