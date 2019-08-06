package codeExamples;

//These are import libraries, they are programs that we use that are stored outside this program
import coreV2.ModuleDriver;
import processing.core.PApplet;


public class AmplitudeBar extends PApplet{//This class too extends PApplet so we can have the program draw on the computer screen.
	
	//These things here are called instance variables. 
	
	//noteAmplitude is a decimal number for how loud the audio input is
	float noteAmplitude;
	
	//ModuleDriver is what runs alot of things in Code + Chords
	ModuleDriver module;
	
	public static void main(String[] args)
	{
		PApplet.main("codeExamples.AmplitudeBar");
	}
	
	//settings sets the screen size
	public void settings()
	{
		this.size(925, 520);
		this.pixelDensity(this.displayDensity());
	}
	
	//The setup function sets things up before drawing that are not related to the screen.
	public void setup() {
		//initializes the module
		module = new ModuleDriver(this);
		//This simplifies the program a bit so there are less settingst that need to be worried about
		module.getCP5().hide();
	}
	
	
	public void draw() {
		//This makes the background black
		background(0);
		
		//module.getAmplitude get how loud the input is.
		noteAmplitude = module.getAmplitude();
		
		
		//This draws the rectangle and makes it so that the louder the input, the taller the rectangle.
		rect(400,520,100,-4 * noteAmplitude); // we multiply by negative 4 so the rectangle is a taller and moves upward because 0 for y coordinate is at the top.
	}
	
	
}
