package coreV2;

import processing.core.PApplet;

public class Canvas 
{
	private int				displayX;
	private int				displayY;
	private int 			displayWidth;
	private int 			displayHeight;
	
	
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
	
	

}
