package coreV2_visuals;

import controlP5.ControlEvent;
import coreV2.ColorFader;
import coreV2.ModuleDriver;
import coreV2.Visual;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

public class FireVisual extends Visual 
{

	private String[] 		controllers;
	private String[]		labels;

	private int				numChannels;

	private ColorFader[]	colors;

	//Processing video variables
	PGraphics buffer1;
	PGraphics buffer2;
	PImage cooling;
	int w;
	int h;

	float ystart;


	public FireVisual(ModuleDriver moduleDriver) 
	{
		super(moduleDriver, "Fire");

		this.numChannels = 1;

		this.colors = new ColorFader[numChannels];

		this.controllers = new String[] {""};
		this.labels = new String[] {""};
		this.makeControllers();

		//Setup
		this.w = this.parent.displayWidth;
		this.h = this.parent.displayHeight;

		buffer1 = this.parent.createGraphics(w, h);
		buffer2 = this.parent.createGraphics(w, h);
		cooling = this.parent.createImage(w, h, this.parent.RGB);


	}

	private void makeControllers()
	{

	}

	@Override
	public void controlEvent(ControlEvent theEvent) 
	{

	}

	private void setNumChannels(int numChannels)
	{

	}

	@Override
	public void drawVisual() 
	{
		//don't draw this visual when the menu is open
		if(this.moduleDriver.getCanvas().isFullscreen()) {
			this.drawFire();

		}
	}

	private void cool() {
		cooling.loadPixels();
		float xoff = 0.0f; // Start xoff at 0
		float increment = 0.02f;
		// For every x,y coordinate in a 2D space, calculate a noise value and produce a brightness value
		for (int x = 0; x < w; x++) {
			xoff += increment;   // Increment xoff 
			float yoff = ystart;   // For every xoff, start yoff at 0
			for (int y = 0; y < h; y++) {
				yoff += increment; // Increment yoff

				// Calculate noise and scale by 255
				float n = this.parent.noise(xoff, yoff);     
				float bright = this.parent.pow(n, 3) * 255;

				// Try using this line instead
				//float bright = random(0,255);

				// Set each pixel onscreen to a grayscale value
				cooling.pixels[x+y*w] = this.parent.color(bright);
			}
		}

		cooling.updatePixels();
		ystart += increment;
	}

	void fire(int rows) {
		buffer1.beginDraw();
		buffer1.loadPixels();
		for (int x = 0; x < w; x++) {
			for (int j = 0; j < rows; j++) {
				int y = h-(j+1);
				int index = x + y * w;
				buffer1.pixels[index] = this.parent.color(255);
			}
		}
		buffer1.updatePixels();
		buffer1.endDraw();
	}

	private void drawFire() {
		fire(2);
		if (this.parent.mousePressed) {
			buffer1.beginDraw();
			buffer1.fill(255);
			buffer1.noStroke();
			buffer1.ellipse(this.parent.mouseX, this.parent.mouseY, 100, 100); 
			buffer1.endDraw();
		}
		cool();
		this.parent.background(0);
		buffer2.beginDraw();
		buffer1.loadPixels();
		buffer2.loadPixels();
		for (int x = 1; x < w-1; x++) {
			for (int y = 1; y < h-1; y++) {
				int index0 = (x) + (y) * w;
				int index1 = (x+1) + (y) * w;
				int index2 = (x-1) + (y) * w;
				int index3 = (x) + (y+1) * w;
				int index4 = (x) + (y-1) * w;
				float c1 = buffer1.pixels[index1];
				float c2 = buffer1.pixels[index2];
				float c3 = buffer1.pixels[index3];
				float c4 = buffer1.pixels[index4];

				float c5 = cooling.pixels[index0];
				float newC = this.parent.brightness((int) c1) + this.parent.brightness((int) c2)+ this.parent.brightness((int) c3) + this.parent.brightness((int) c4);
				newC = (float) (newC * 0.25 - this.parent.brightness((int) c5));

				buffer2.pixels[index4] = this.parent.color(newC);
			}
		}
		buffer2.updatePixels();
		buffer2.endDraw();

		// Swap
		PGraphics temp = buffer1;
		buffer1 = buffer2;
		buffer2 = temp;

		this.parent.image(buffer2, 0, 0);
		this.parent.image(cooling, w, 0);
	}

	private void drawSineWave() {

	}

	@Override
	public int getNumControllers() {
		// TODO Auto-generated method stub
		return 0;
		//return this.controllers.length;
	}

	@Override
	public String getControllerName(int controllerNum) {
		return this.controllers[controllerNum];
	}

	@Override
	public String getLabelName(int controllerNum) {
		return this.labels[controllerNum];
	}

}
