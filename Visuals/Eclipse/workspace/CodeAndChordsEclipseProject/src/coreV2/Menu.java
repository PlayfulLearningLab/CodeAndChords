package coreV2;

import processing.core.PApplet;

public abstract class Menu 
{
	private PApplet 			pApp;
	
	protected Canvas			canvas;
	
	private String				menuName;
	
	private int				 	canvasXPos;
	private int 				canvasYPos;
	private int 				canvasWidth;
	private int 				canvasHeight;
	
	public Menu(Canvas canvas)
	{
		this.canvas = canvas;
		
		this.canvasXPos = 0;
		this.canvasYPos = 260;
		this.canvasWidth = 462;
		this.canvasHeight = 260;
	}
	
	public Canvas getCanvas()
	{
		return this.canvas;
	}
	
	public void setCanvas(Canvas canvas)
	{
		this.canvas = canvas;
	}
	
	public void setCanvasSize()
	{
		this.canvas.setDisplay(this.canvasXPos, this.canvasYPos, this.canvasWidth, this.canvasHeight);
	}
	
	abstract public void drawMenu();
	
	
	
}
