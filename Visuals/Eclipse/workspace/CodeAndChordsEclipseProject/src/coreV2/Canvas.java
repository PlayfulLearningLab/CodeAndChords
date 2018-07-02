package coreV2;

import processing.core.PApplet;

public class Canvas 
{
	private PApplet			parent;
	
	private int				displayX;
	private int				displayY;
	private int 			displayWidth;
	private int 			displayHeight;
	
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
		return new int[] { this.displayX, this.displayY, this.displayWidth, this.displayHeight };
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
		width = width*(this.displayWidth/this.parent.width);
		height = height*(this.displayHeight/this.parent.height);
		
		this.parent.rect(this.displayX + xPos, this.displayY + yPos, width, height);
	}
	
	public void ellipse(int xPos, int yPos, int width, int height)
	{
		width = width*(this.displayWidth/this.parent.width);
		height = height*(this.displayHeight/this.parent.height);
		
		this.parent.ellipse(this.displayX + xPos, this.displayY + yPos, width, height);
	}
	
	public void line(int xPos, int yPos, int width, int height)
	{
		width = width*(this.displayWidth/this.parent.width);
		height = height*(this.displayHeight/this.parent.height);
		
		this.parent.line(this.displayX + xPos, this.displayY + yPos, width, height);
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

		}
	}//drawAppletBackground()
	

}
