package coreV2;

import coreV2_EffectsAndFilters.Drawable;
import processing.core.PApplet;

/**
 * 
 * Canvas is a class that is used to automatically resize and move the visuals when the
 * menus are open. This prevents stretching of the image by ensuring that it is scaled 
 * properly, and it eliminates the need to do this math for every new visual.
 * 
 * @author Danny Mahota
 *
 */
public class Canvas implements Drawable
{
	private PApplet				parent;
	
	private float				displayX;
	private float				displayY;
	private float 				displayWidth;
	private float 				displayHeight;
	
	private boolean				isFullscreen;
	
	
	public Canvas(PApplet parent)
	{
		if(parent == null) throw new IllegalArgumentException("PApplet must not be null.");
		
		this.parent = parent;
		this.fullScreen();
	}
	
	/**
	 * Set the size of the canvas.
	 * 
	 * @param xPos: The x coordinate of the top left corner of the canvas.
	 * @param yPos: The y coordinate of the top left corner of the canvas.
	 * @param width: The width of the canvas.
	 * @param height: The height of the canvas.
	 */
	public void setDisplay(int xPos, int yPos, int width, int height)
	{
		this.displayX = xPos;
		this.displayY = yPos;
		this.displayWidth = width;
		this.displayHeight = height;
		
		System.out.println("Display settings: " + xPos + " " + yPos + " " + width + " " + height);
		
		this.isFullscreen = false;
	}
	
	/**
	 * Returns the dimensions of the canvas in an array.
	 * 
	 * @return: int[] {displayXPosition, displayYPosition, displayWidth, displayHeight}
	 */
	public int[] getCanvasDimensions()
	{
		return new int[] { (int)this.displayX, (int)this.displayY, (int)this.displayWidth, (int)this.displayHeight };
	}
	
	//TODO:  Create a method that returns a PShape covering all of the area not covered by the canvas
	
	/**
	 * Sets the canvas to cover the full PApplet window.
	 */
	public void fullScreen()
	{
		this.displayX = 0;
		this.displayY = 0;
		this.displayWidth = this.parent.width;
		this.displayHeight = this.parent.height;

		this.isFullscreen = true;
	}
	
	/**
	 * Works similar to the PApplet method background(). Covers the entire canvas.
	 */
	public void background()
	{
		this.parent.noStroke();
		this.parent.rect(this.displayX, this.displayY, this.displayWidth, this.displayHeight);
	}
	
	/**
	 * Wrapper method for PApplet method rect(a,b,c,d).  Draws a rectangle on the canvas.
	 * 
	 * @param xPos: X position of rectangle top left corner.
	 * @param yPos: Y position of the rectangle top left corner.
	 * @param width: Rectangle width.
	 * @param height: Rectangle height.
	 */
	public void rect(int xPos, int yPos, int width, int height)
	{
		float x = (this.displayWidth/this.parent.width);
		float y = (this.displayHeight/this.parent.height);
		
		this.parent.rect(this.displayX + (xPos * x), this.displayY + (yPos * y), width*x, height*y);
	}
	
	/**
	 * Wrapper method for PApplet method ellipse(a,b,c,d). Draws an ellipse on the canvas.
	 * 
	 * @param xPos: X position of the ellipse center.
	 * @param yPos: Y position of the ellipse center.
	 * @param width: Width of the ellipse.
	 * @param height: Height of the ellipse.
	 */
	public void ellipse(int xPos, int yPos, int width, int height)
	{
		float x = (this.displayWidth/this.parent.width);
		float y = (this.displayHeight/this.parent.height);
		
		this.parent.ellipse(this.displayX + (xPos * x), this.displayY + (yPos * y), width*x, height*y);
	}
	
	/**
	 * Wrapper method for PApplet method line(a,b,c,d). Draws a line on the canvas.
	 *  
	 * @param x1: X position of point 1.
	 * @param y1: Y position of point 1.
	 * @param x2: X position of point 2.
	 * @param y2: Y position of point 2.
	 */
	public void line(int x1, int y1, int x2, int y2)
	{
		float x = (this.displayWidth/this.parent.width);
		float y = (this.displayHeight/this.parent.height);
		
		this.parent.line(this.displayX + (x1 * x), this.displayY + (y1 * y), this.displayX + (x2 * x), this.displayY + (y2 * y));
	}
	
	/**
	 * Wrapper method for PApplet method text(). Draws text on the canvas.
	 * 
	 * @param textSize: Size of the text.
	 * @param text: Text that will be written on the canvas.
	 * @param x: X position of text.
	 * @param y: Y position of text.
	 */
	public void text(int textSize, String text, int x, int y)
	{
		float Kx = (this.displayWidth/this.parent.width);
		float Ky = (this.displayHeight/this.parent.height);
		float Kt = (Kx + Ky) / 2;
		
		this.parent.textSize(textSize*Kt);
		
		this.parent.text(text, Kx * x + this.displayX, Ky * y + this.displayY);
	}
	
	/**
	 * Fills the portion of the PApplet window not covered by the canvas with a dark grey color.
	 */
	public void drawAppletBackground()
	{
		if(!this.isFullscreen)
		{
			this.parent.fill(30);
			
			this.parent.noStroke();
			
			this.parent.rect(0, 0, this.parent.width, this.displayY);
			this.parent.rect(0, 0, this.displayX, this.parent.height);
			this.parent.rect(this.parent.width, this.parent.height, - this.parent.width, -(this.parent.height - (this.displayHeight + this.displayY) ));
			this.parent.rect(this.parent.width, this.parent.height, -(this.parent.width - (this.displayWidth + this.displayX)), -this.parent.height );

			this.parent.stroke(100, 0 , 230);
			this.parent.strokeWeight(4);
			this.parent.noFill();
			
			this.parent.rect(2, 2, this.displayX - 4, this.parent.height - 4);
			this.parent.rect(this.displayX-2, 2, this.displayWidth, this.displayY - 4);
			this.parent.rect(2, 2, this.parent.width - 4, this.parent.height/9);
			
			this.parent.stroke(255);
			this.parent.fill(255);
			this.parent.textSize(28);
			this.parent.textAlign(this.parent.CENTER, this.parent.CENTER);
			this.parent.text("CODE+CHORDS", this.displayX/2, this.parent.height/25);
			this.parent.textSize(7);
			this.parent.text("1.0.1", this.displayX/2, this.parent.height/12);
		}
	}//drawAppletBackground()

	@Override
	public int getType() {
		return Drawable.CANVAS;
	}

	@Override
	public void setOutput(Drawable output) {
		System.err.println("The canvas object draws to the canvas, so it cannot output to another effect.");
		
	}
	

}
