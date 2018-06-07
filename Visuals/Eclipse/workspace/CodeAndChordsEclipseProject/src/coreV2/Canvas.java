package coreV2;

import processing.core.PApplet;

public class Canvas 
{
	private PApplet			pApp;
	
	private int				displayX;
	private int				displayY;
	private int 			displayWidth;
	private int 			displayHeight;
	
	private boolean			isFullscreen;
	
	
	public Canvas(PApplet pApplet)
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
		
		this.isFullscreen = false;
	}
	
	public void fullScreen()
	{
		this.displayX = 0;
		this.displayY = 0;
		this.displayWidth = this.pApp.width;
		this.displayHeight = this.pApp.height;
		
		this.isFullscreen = true;
	}
	
	public void background()
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
	
	public void drawAppletBackground()
	{
		if(!this.isFullscreen)
		{
			this.pApp.rect(0, 0, this.displayX, this.displayHeight);
			this.pApp.rect(	this.displayX + this.displayWidth, 
							0, 
							this.pApp.width - (this.displayX + this.displayWidth), 
							this.displayHeight);
			
			this.pApp.rect(this.displayX, 0, this.displayWidth, this.displayY);
			this.pApp.rect(	this.displayX, 
							this.displayY + this.displayHeight, 
							this.displayWidth,
							this.pApp.height - (this.displayY + this.displayHeight));
		}
	}//drawAppletBackground()
	

}
