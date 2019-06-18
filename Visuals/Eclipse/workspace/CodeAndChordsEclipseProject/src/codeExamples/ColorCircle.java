package codeExamples;

//These are the programs that we need to import
import coreV2.ModuleDriver;
import processing.core.PApplet;

/*
 * This class is for drawing a circle that changes color with the pitch
 */
public class ColorCircle extends PApplet{
	
	/*
	 * The ModuleDriver is a class that does a lot of the work in code and chords.
	 * For probably every visual that inputs sound you will need ModuleDriver object.
	 * One of the things it does is it takes in sound and outputs color to the screen.
	 */
	
	ModuleDriver module; 
	
	//All we have to do in main is write the name of the class being used as the main
	public static void main(String[] args)
	{
		PApplet.main("codeExamples.ColorCircle");
	}
	
	//Screen settings are set up here
	public void settings()
	{
		//This sets up the size
		this.size(925, 520);
		//This scales the pixels so it looks better
		this.pixelDensity(this.displayDensity());
	}
	
	public void setup() {
		//Initialize the Module Driver object here
		module = new ModuleDriver(this);
		
		//This simplifies the program a bit so there are less settingst that need to be worried about
		module.getCP5().hide();
	}
	
	//The draw class continuously loops through and draws what is in the method.
	public void draw() {
		//background sets the background color. Here we are doing white with 255, 0 is black, inbetween is gray. RGB can also be used similair to how color is set in example 1
		//background(125,0,255);
		background(255);
		
		//stroke is the outline of the shapes drawn. Here we are making it black so we can see it against the white background.
		stroke(0);
		
		//Since we want the ellipse in the middle, we can set is so that it is positioned at half the height and half the width.
		ellipse(width/2,height/2,100,100); 
	}
	
	
}