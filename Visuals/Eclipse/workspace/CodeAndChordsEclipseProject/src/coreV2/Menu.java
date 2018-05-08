package coreV2;

import processing.core.PApplet;

public abstract class Menu 
{
	private PApplet 	pApp;
	
	private int		 	canvasXPos;
	private int 		canvasYPos;
	private int 		canvasWidth;
	private int 		canvasHeight;
	
	public Menu(PApplet pApplet)
	{
		this.pApp = pApplet;
		
		this.canvasXPos = 0;
		this.canvasYPos = 260;
		this.canvasWidth = 462;
		this.canvasHeight = 260;
	}
	
	public void drawMenu()
	{
		
	}
	
	
}
