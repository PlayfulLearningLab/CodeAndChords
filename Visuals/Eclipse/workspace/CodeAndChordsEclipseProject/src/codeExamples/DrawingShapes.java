/*
 * This example will show you how to make basic shapes using the Processing Library
 */
package codeExamples;

//imported programs
import processing.core.PApplet;

public class DrawingShapes extends PApplet { //extends PApplet so we can draw
	
	
	//Here in the main write the package and class that is the main. 
	public static void main(String[] args) {
		//codeExamples is the package and DrawingShapes is the class
		PApplet.main("codeExamples.DrawingShapes");
		
	}
	
	//Here we set the size of the screen to be used.
	public void settings() {
		size(900,300);
		
	}

	
	//In the draw section we draw each shape
	public void draw() {
		
		//Set the background to black. You can set it to white by setting the number to 255 and RGB by putting in the 3 numbers that represent their RBG components
		background(0);
		
		//Sets the fill to white so we can see the shapes easily against the black background
		fill(255);
		
		//Sets the outline of shapes to white.
		stroke(255);
		
		/*line is 2 points that are connected. The number is the x coordinate for point #1 and the second number is the y coordinate for point #1.
		 * The next two numbers are the x and y coordinates for point #2
		 */
		line(25,100,120,150);
		
		/*Rectangles are drawn by the rect command.
		 * The first 2 numbers are the x and y coordinates starting at the upper left corner of the rectangle.
		 * The 3rd number is the width of the rectangle
		 * The 4th number is the height of the rectangle
		 */
		rect(200,75,150,100);
		
		/*Ellipses can be used to draw circles and ovals
		 * the first two coordinates are the x and y coordinates of the center of the ellipse
		 * the 3rd number is the width of the ellipse
		 * the 4th number is the height of the ellipse
		 */
		
		//This ellipse is a circle
		ellipse(500, 150, 75,75);
		
		//This ellipse is an oval
		ellipse(650,150,100,75);
		
		/*Triangles are drawn by 3 points
		 * The first 2 numbers are the x and y coordinates for the first point
		 * The second 2 numbers are the x and y coordinates for the second point
		 * the third 2 numbers are the x and y coordinates for the third point
		 */
		triangle(750,50, 850,100, 800, 200);
		
	}
	

}
