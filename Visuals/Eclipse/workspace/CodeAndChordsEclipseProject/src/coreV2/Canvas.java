package coreV2;

import processing.core.PApplet;

public class Canvas 
{
	private int				displayX;
	private int				displayY;
	private int 			displayWidth;
	private int 			displayHeight;
	
	private boolean			isFullscreen;
	
	
	public Canvas()
	{
		this.fullScreen();
	}
	
	public void setDisplay(int xPos, int yPos, int width, int height)
	{
		this.displayX = xPos;
		this.displayY = yPos;
		this.displayWidth = width;
		this.displayHeight = height;
		
		this.isFullscreen = false;
	}
	
	public int[] getCanvasDimensions()
	{
		return new int[] { this.displayX, this.displayY, this.displayWidth, this.displayHeight };
	}
	
	//TODO:  Create a method that returns a PShape covering all of the area not covered by the canvas
	
	public void fullScreen()
	{
		PApplet pApp = ModuleDriver.getModuleDriver();
		
		this.displayX = 0;
		this.displayY = 0;
		this.displayWidth = pApp.width;
		this.displayHeight = pApp.height;

		this.isFullscreen = true;
	}
	
	public void background()
	{
		PApplet pApp = ModuleDriver.getModuleDriver();
		
		pApp.rect(this.displayX, this.displayY, this.displayWidth, this.displayHeight);
	}
	
	public void rect(int xPos, int yPos, int width, int height)
	{
		PApplet pApp = ModuleDriver.getModuleDriver();
		
		width = width*(this.displayWidth/pApp.width);
		height = height*(this.displayHeight/pApp.height);
		
		pApp.rect(this.displayX + xPos, this.displayY + yPos, width, height);
	}
	
	public void ellipse(int xPos, int yPos, int width, int height)
	{
		PApplet pApp = ModuleDriver.getModuleDriver();
		
		width = width*(this.displayWidth/pApp.width);
		height = height*(this.displayHeight/pApp.height);
		
		pApp.ellipse(this.displayX + xPos, this.displayY + yPos, width, height);
	}
	
	public void line(int xPos, int yPos, int width, int height)
	{
		PApplet pApp = ModuleDriver.getModuleDriver();
		
		width = width*(this.displayWidth/pApp.width);
		height = height*(this.displayHeight/pApp.height);
		
		pApp.line(this.displayX + xPos, this.displayY + yPos, width, height);
	}
	
	public void drawAppletBackground()
	{
		PApplet pApp = ModuleDriver.getModuleDriver();
		
		if(!this.isFullscreen)
		{
			pApp.rect(0, 0, this.displayX, this.displayHeight);
			pApp.rect(	this.displayX + this.displayWidth, 
							0, 
							pApp.width - (this.displayX + this.displayWidth), 
							this.displayHeight);
			
			pApp.rect(this.displayX, 0, this.displayWidth, this.displayY);
			pApp.rect(	this.displayX, 
							this.displayY + this.displayHeight, 
							this.displayWidth,
							pApp.height - (this.displayY + this.displayHeight));
		}
	}//drawAppletBackground()
	

}
