package codeExamples;

import processing.core.PApplet;
//all visuals will be extend PApplet what this does is set up 
// the screen because the PApplet is a GUI (graphical user interface)
public class WritngYourName extends PApplet{ 

	public static void main(String[] args)
	{
		//In the main all you need to write is the package.className
		//where package is the 
		PApplet.main("codeExamples.WritngYourName");
	}
	/*
	 * Settings is run before the visual is drawn.
	 * It is used to set up the screen. You will put only the screen size here usually
	 */
	public void settings()
	{
		this.size(925, 520); // Sets the size of the screen that will be used
		//fullscreen(); //alternatively you can choose to make it fullscreen
		this.pixelDensity(this.displayDensity()); //This scales the pixels with the type of compuputer screen to make it look better
		
	}
	
	// The draw method constantly loops and updates. It starts after settings
	public void draw() {
		background(0); //This sets the background to black. 0 is black, 255 is white, and any number inbetween is gray 
		textSize(64); //This sets the textsize of the letters.
		
		
		/* This sets the color of what will be drawn or written.
		 * the first number is the amount of red between 0 to 255
		 * the second is the amount of green between 0 to 255
		 * the third is the amount of blue between 0 to 255
		 */
		fill(100, 80, 255); 
		
		/*
		 * text writes out whatever you want onto the screen.
		 * the 1st number is the x location with 0 starting on the left side and increasing as it moves right
		 * the 2nd number is the y location with 0 starting at the top and moving downward as it increases
		 */
		text("Y", 100, 200);

		fill(50, 200, 100);
		text("O", 200, 200);

		fill(250, 250, 50);
		text("U", 300, 200);

		fill(250, 150, 50);
		text("R", 400, 200);

		fill(200, 0, 100);
		text("N", 550, 200);
		
		fill(255, 0, 0);
		text("A", 650, 200);
		
		fill(255,255,0);
		text("M", 750, 200);
		
		fill(0,255,255);
		text("E", 850, 200);
	}

}
