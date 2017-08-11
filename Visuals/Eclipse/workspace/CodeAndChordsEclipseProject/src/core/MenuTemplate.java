package core;

/*
 * All values are stored and set as relative to their position in the full canvas, 
 * not the scaled down version.  When you need these values relative to the scaled 
 * down screen, use the getAdjustedMenuXPos() and getAdjustedMenuYPos() functions. 
 */



import processing.core.PApplet;
import processing.core.PShape;

public abstract class MenuTemplate {

	private PApplet 	parent;
	
	private boolean 	isRunning;
	
	//these variables are stored relative to the size of the full applet,
	//NOT RELATIVE TO THE SMALLER WINDOW USED WHEN THE MENU IS OPEN
	private float 		xPos;
	private float 		yPos;
	private float 		rotation;

	private float 		appletWidth;
	private float 		appletHeight;

	private float 		scale;
	
	private PShape		menuBackground;


	public MenuTemplate(PApplet pApp, float appWidth, float appHeight)
	{
		this.isRunning = false;
		
		this.parent = pApp;
		
		this.appletWidth = appWidth;
		this.appletHeight = appHeight;
		
		this.xPos = appWidth / 2;
		this.yPos = appHeight / 2;
		this.rotation = 0;

		this.scale = .7f;
		
		this.menuBackground = this.parent.createShape();
		
		this.menuBackground.beginShape();
		
		this.menuBackground.vertex(0, 0);
		this.menuBackground.vertex(this.appletWidth, 0);
		this.menuBackground.vertex(this.appletWidth, this.mapAdjustedMenuYPos(0));
		this.menuBackground.vertex(this.mapAdjustedMenuXPos(0), this.mapAdjustedMenuYPos(0));
		this.menuBackground.vertex(this.mapAdjustedMenuXPos(0), this.appletHeight);
		this.menuBackground.vertex(0, this.appletHeight);
		this.menuBackground.vertex(0, 0);
		
		
		this.menuBackground.stroke(0);
		this.menuBackground.fill(0);
		
		this.menuBackground.endShape();
	}
	
	public void drawMenu()
	{
		this.parent.shape(this.menuBackground, 0, 0);
		
		this.parent.stroke(150);
		this.parent.strokeWeight(3);
		this.parent.noFill();
		
		this.parent.rect(	this.mapAdjustedMenuXPos(0), 
							this.mapAdjustedMenuYPos(0), 
							this.appletWidth * this.scale - 3, 
							this.appletHeight * this.scale - 3 );
		//System.out.println(this.scale);
		//System.out.println(this.mapAdjustedMenuXPos(0) + "    "  +  this.mapAdjustedMenuXPos(this.appletWidth));
		
	}

	public void setIsRunning(boolean isRunning) 
	{
		this.isRunning = isRunning;
	}
	
	public void setXPos(float xPos)
	{
		this.xPos = xPos;
	}

	public void setYPos(float yPos)
	{
		this.yPos = yPos;
	}

	public void setRotation(float rotation)
	{
		this.rotation = rotation;
	}
	
	public boolean getIsRunning()
	{
		return this.isRunning;
	}

	public float getXPos()
	{
		return this.xPos;
	}

	public float getYPos()
	{
		return this.yPos;
	}

	public float getRotation()
	{
		return this.rotation;
	}
	
	public float getAppletWidth()
	{
		return this.appletWidth;
	}
	
	public float getAppletHeight()
	{
		return this.appletHeight;
	}
	
	public float getScale()
	{
		return this.scale;
	}

	public float getAdjustedMenuXPos()
	{
		float adjustedX = PApplet.map(this.xPos, 0, 1, 0, this.scale);
		adjustedX += (this.appletWidth * (1 - this.scale));

		return adjustedX;
	}

	public float getAdjustedMenuYPos()
	{
		float adjustedY = PApplet.map(this.yPos, 0, 1, 0, this.scale);
		adjustedY += (this.appletHeight * (1 - this.scale));

		return adjustedY;
	}
	
	public float mapAdjustedMenuXPos(float fullAppletXPos)
	{
		float adjustedX = PApplet.map(fullAppletXPos, 0, 1, 0, this.scale);
		adjustedX += (this.appletWidth * (1 - this.scale));

		return adjustedX;
	}
	
	public float mapAdjustedMenuYPos(float fullAppletYPos)
	{
		float adjustedY = PApplet.map(fullAppletYPos, 0, 1, 0, this.scale);
		adjustedY += (this.appletHeight * (1 - this.scale));

		return adjustedY;
	}
	
	public float mapFullAppletXPos(float adjustedMenuXPos)
	{
		float fullX = adjustedMenuXPos - (this.appletWidth * (1 - this.scale));
		fullX = PApplet.map(fullX, 0, this.scale, 0, 1);

		return fullX;
	}
	
	public float mapFullAppletYPos(float adjustedMenuYPos)
	{
		float fullY = adjustedMenuYPos - (this.appletHeight * (1 - this.scale));
		fullY = PApplet.map(fullY, 0, this.scale, 0, 1);

		return fullY;
	}
	
	public abstract void runMenu();



}
