package coreV2_visuals;

import java.util.Random;

import coreV2.Canvas;
import controlP5.ControlEvent;
import coreV2.ColorFader;
import coreV2.ColorScheme;
import coreV2.ModuleDriver;
import coreV2.Visual;

public class StarsVisual extends Visual{
	private Canvas				canvas;
	private ColorFader 			colorFader;
	
	protected String[]			controllers;
	protected String[]			labels;
	private Random generator = new Random();
	
	public StarsVisual (ModuleDriver moduleDriver) {
		super(moduleDriver, "Stars");
		this.canvas = this.moduleDriver.getCanvas();
		this.colorFader = new ColorFader(this.parent);
	}

	@Override
	public void controlEvent(ControlEvent theEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawVisual() {
		//draw the background
				this.parent.fill(0);
				this.canvas.background();


				int 	note 		= this.moduleDriver.getInputHandler().getAllMidiNotes()[0][0];
				int[] 	scale 		= this.moduleDriver.getInputHandler().getScale();
				int[] 	pitchColor 	= new int[] {-1,-1,-1};
				ColorScheme[] schemes = this.moduleDriver.getColorMenu().getColorSchemes();
				int lastNote = 0;
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

				//get the current color from color fader and fill it into the canvas
				int[] curColor = colorFader.getColor();
				this.parent.fill(curColor[0], curColor[1], curColor[2], curColor[3]);
				
				//this.canvas.background();
				if (lastNote != note) {
					int xValue = generator.nextInt(825) + 50;
					int yValue = generator.nextInt(200) + 60;
					
					
				}
				
				
				lastNote = note;
				
				
				
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
		Random generator = new Random();
		
		
		public Star() {
			xPos = generator.nextInt();
		}
	}
}

