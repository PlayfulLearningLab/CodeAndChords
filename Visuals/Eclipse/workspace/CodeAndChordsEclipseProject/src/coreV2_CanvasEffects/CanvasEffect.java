package coreV2_CanvasEffects;

import java.awt.Color;
import processing.core.PApplet;

public abstract class CanvasEffect implements Drawable {
	
	protected PApplet			parent;
	protected Drawable			output;
	
	protected int				fillColor;
	protected int				strokeColor;
	
	private boolean				muteEffect;
		
	protected CanvasEffect(PApplet parent, Drawable output){
		this.parent = parent;
		this.output = output;
		this.muteEffect = false;
	}
	
	public void muteEffect(boolean mute){
		this.muteEffect = mute;
	}
	
	private void setColors()
	{
		this.fillColor = this.parent.g.fillColor;
		this.strokeColor = this.parent.g.strokeColor;
	}
	
	public void rect(int xPos, int yPos, int width, int height)
	{
		this.setColors();
		
		if(this.muteEffect)	this.output.rect(xPos, yPos, width, height);
		else 				this.drawRect(xPos, yPos, width, height);
	}
	
	public void ellipse(int xPos, int yPos, int width, int height)
	{
		this.setColors();
		
		if(this.muteEffect)	this.output.ellipse(xPos, yPos, width, height);
		else				this.drawEllipse(xPos, yPos, width, height);
	}
	
	public void line(int x1, int y1, int x2, int y2)
	{
		this.setColors();
		
		if(this.muteEffect)	this.output.line(x1, y1, x2, y2);
		else				this.drawLine(x1, y1, x2, y2);
	}
	
	public void background()
	{
		this.setColors();
		
		if(this.muteEffect)	this.output.background();
		else				this.drawBackground();
	}
	
	public void triggerEvent()
	{
		this.triggerEvent(0);
	}
	
	
	
	protected int[] getColorComponents(int rgb)
	{
		Color color = new Color(rgb);
		
		return new int[] {	(int) color.getRed(),
							(int) color.getGreen(),
							(int) color.getBlue(),
							(int) color.getAlpha()};
	}
	
	protected boolean isFill(){
		return this.parent.g.fill;
	}
	
	protected boolean isStroke(){
		return this.parent.g.stroke;
	}
	
	protected float getStrokeWeight(){
		return this.parent.g.strokeWeight;
	}
		
	
	public abstract void triggerEvent(int eventType);
	
	protected abstract void drawRect(int xPos, int yPos, int width, int height);
	
	protected abstract void drawEllipse(int xPos, int yPos, int width, int height);
	
	protected abstract void drawLine(int x1, int y1, int x2, int y2);
	
	protected abstract void drawBackground();
	
	
	
	
}
