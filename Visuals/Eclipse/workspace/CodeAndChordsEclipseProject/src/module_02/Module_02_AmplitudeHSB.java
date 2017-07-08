package module_02;

import controlP5.ControlEvent;
import core.Input;
import processing.core.PApplet;
import processing.core.PShape;

public class Module_02_AmplitudeHSB extends PApplet {
	
	private Input				input;
	private ModuleTemplate02	moduleTemplate;
	
	private PShape 	shape;
	int		shapeCenter;
	
	float[]	thresholds;
	
	public static void main(String[] args)
	{
		PApplet.main("module_02.Module_02_AmplitudeHSB");
	} // main
	
	public void settings()
	{
		size(925, 520);
	} // settings
	
	public void setup()
	{		
		this.input	= new Input();
		this.moduleTemplate	= new ModuleTemplate02(this, this.input, "Module_02_AmplitudeHSB");
		
		// set amplitude thresholds
		this.thresholds	= new float[] {
				2,		// piano
				5,	// mezzo piano
				10,	// mezzo forte
				15	//forte
		}; // thresholds
		
		// Define them some default colors
		this.moduleTemplate.setColors(new float[][] {
				new float[] { 255, 0, 0 },
				new float[] { 0, 255, 0 },
				new float[] { 0, 0, 255 },
				new float[] { 150, 50, 150 }
		});
		
		this.textSize(32);
		
		// create the shape
		this.shapeMode(CENTER);
		this.shape			= createShape(ELLIPSE, (this.width - this.moduleTemplate.getLeftEdgeX()) / 2, this.height / 2, 400, 400);
		this.shapeCenter	= (this.width - this.moduleTemplate.getLeftEdgeX()) / 2;
	} // setup
	
	public void draw()
	{
		
		// The following line is necessary so that key press shows the menu button
		if (keyPressed == true) 
		{
			this.moduleTemplate.setMenuVal();
		}
		
		background(150);
		
		// pick the appropriate color by checking amplitude threshold
		float	curAmp		= this.input.getAmplitude();
		int		goalHuePos	= 0;
		
		for(int i = 0; i < this.thresholds.length; i++)
		{
			if(curAmp > this.thresholds[i]) {
				goalHuePos	= i;
			} // if
		} // for
		
		this.moduleTemplate.fade(goalHuePos);
		
		// draw the shape
		this.drawShape();
		
		this.text(goalHuePos, this.moduleTemplate.getLeftEdgeX() + ((this.width - this.moduleTemplate.getLeftEdgeX()) / 2), this.height / 2);
		
//		System.out.println("this.input.getAmplitude() = " + this.input.getAmplitude());
	} // draw
	
	private void drawShape()
	{		
		this.shape.beginShape(ELLIPSE);
				
		// adjust any shape parameters
		float[]	curHue	= this.moduleTemplate.getCurHue();
		this.shape.fill(curHue[0], curHue[1], curHue[2]);

		this.shape.endShape();
		
		shape(this.shape);


	} // drawShape
	
	public void controlEvent(ControlEvent controlEvent)
	{
		this.moduleTemplate.controlEvent(controlEvent);
		
		if(controlEvent.getController().getName().equals("hamburger"))
		{
			System.out.println("Did we get here? hamburger");
			this.shape.translate(((this.width - this.moduleTemplate.getLeftEdgeX()) / 2) - ((this.width / 2) - this.moduleTemplate.getLeftEdgeX()), 0);
			this.shapeCenter	= (this.width / 2) + ((this.width - this.moduleTemplate.getLeftEdgeX()) / 2) - ((this.width / 2) - this.moduleTemplate.getLeftEdgeX());
		} // hamburger
		
		if(controlEvent.getController().getName().equals("menuX"))
		{
			System.out.println("Did we get here?");
			this.shape.translate((width / 2) - this.shapeCenter, 0);
			this.shapeCenter	= this.width / 2;
		} // menuX
	} // controlEvent
} // Module_03_AmplitudeHSB
