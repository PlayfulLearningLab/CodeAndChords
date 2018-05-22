package coreV2;

import processing.core.PApplet;

public class Canvas 
{
	private PApplet			pApp;
	
	private int				displayX;
	private int				displayY;
	private int 			displayWidth;
	private int 			displayHeight;
	
	
	public Canvas(PApplet pApplet, int width, int height)
	{
		this.pApp = pApplet;
		
		this.fullScreen();
	}
	
	public void setDisplay(int xPos, int yPos, int width, int height)
	{
		this.displayX = xPos;
		this.displayY = yPos;
		this.displayWidth = width;
		this.displayHeight = height;
	}
	
	public void fullScreen()
	{
		this.displayX = 0;
		this.displayY = 0;
		this.displayWidth = this.pApp.width;
		this.displayHeight = this.pApp.height;
	}
	
	public void background(int color)
	{
		this.pApp.rect(this.displayX, this.displayY, this.displayWidth, this.displayHeight);
	}
	
	public void rect(int xPos, int yPos, int width, int height)
	{
		width = width*(this.displayWidth/this.pApp.width);
		height = height*(this.displayHeight/this.pApp.height);
		
		this.pApp.rect(this.displayX + xPos, this.displayY + yPos, width, height);
	}
	
	public void ellipse(int xPos, int yPos, int width, int height)
	{
		width = width*(this.displayWidth/this.pApp.width);
		height = height*(this.displayHeight/this.pApp.height);
		
		this.pApp.ellipse(this.displayX + xPos, this.displayY + yPos, width, height);
	}
	
	public void line(int xPos, int yPos, int width, int height)
	{
		width = width*(this.displayWidth/this.pApp.width);
		height = height*(this.displayHeight/this.pApp.height);
		
		this.pApp.line(this.displayX + xPos, this.displayY + yPos, width, height);
	}
	
	

}
