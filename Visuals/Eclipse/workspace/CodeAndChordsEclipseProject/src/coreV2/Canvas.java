package coreV2;

import processing.core.PApplet;

public class Canvas 
{
	private PApplet			parent;
	
	private float			displayX;
	private float			displayY;
	private float 			displayWidth;
	private float 			displayHeight;
	
	private boolean			isFullscreen;
	
	
	public Canvas(PApplet parent)
	{
		if(parent == null) throw new IllegalArgumentException("PApplet must not be null.");
		
		this.parent = parent;
		this.fullScreen();
	}
	
	public void setDisplay(int xPos, int yPos, int width, int height)
	{
		this.displayX = xPos;
		this.displayY = yPos;
		this.displayWidth = width;
		this.displayHeight = height;
		
		System.out.println("Display settings: " + xPos + " " + yPos + " " + width + " " + height);
		
		this.isFullscreen = false;
	}
	
	public int[] getCanvasDimensions()
	{
		return new int[] { (int)this.displayX, (int)this.displayY, (int)this.displayWidth, (int)this.displayHeight };
	}
	
	//TODO:  Create a method that returns a PShape covering all of the area not covered by the canvas
	
	public void fullScreen()
	{
		this.displayX = 0;
		this.displayY = 0;
		this.displayWidth = this.parent.width;
		this.displayHeight = this.parent.height;

		this.isFullscreen = true;
	}
	
	public void background()
	{
		this.parent.noStroke();
		this.parent.rect(this.displayX, this.displayY, this.displayWidth, this.displayHeight);
	}
	
	public void rect(int xPos, int yPos, int width, int height)
	{
		float x = (this.displayWidth/this.parent.width);
		float y = (this.displayHeight/this.parent.height);
		
		this.parent.rect(this.displayX + (xPos * x), this.displayY + (yPos * y), width*x, height*y);
	}
	
	public void ellipse(int xPos, int yPos, int width, int height)
	{
		float x = (this.displayWidth/this.parent.width);
		float y = (this.displayHeight/this.parent.height);
		
		this.parent.ellipse(this.displayX + (xPos * x), this.displayY + (yPos * y), width*x, height*y);
	}
	
	public void line(int x1, int y1, int x2, int y2)
	{
		float x = (this.displayWidth/this.parent.width);
		float y = (this.displayHeight/this.parent.height);
		
		this.parent.line(this.displayX + (x1 * x), this.displayY + (y1 * y), this.displayX + (x2 * x), this.displayY + (y2 * y));
	}
	
	public void text(int textSize, String text, int x, int y)
	{
		float Kx = (this.displayWidth/this.parent.width);
		float Ky = (this.displayHeight/this.parent.height);
		float Kt = (Kx + Ky) / 2;
		
		this.parent.textSize(textSize*Kt);
		
		this.parent.text(text, Kx * x + this.displayX, Ky * y + this.displayY);
	}
	
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
			this.parent.text("CODE+CHORDS", this.displayX/2, this.parent.height/18);
			
		}
	}//drawAppletBackground()
	

}
