package coreV2;

import java.awt.Color;

import processing.core.PApplet;
import processing.core.PImage;

public class CanvasMenu extends MenuTemplate
{
	public CanvasMenu(ModuleDriver driver) 
	{
		super("Canvas Menu", driver, false);
		
		this.addOutsideButtons();
	}
	
	/**
	 * Adds "outside buttons": hamburger and play/pause/stop Buttons
	 */
	public void addOutsideButtons()
	{
		this.controlP5.addGroup(this.getMenuTitle());
		
		int	playX		= this.parent.width - 45;
		int	playY		= 15;
		int	playWidth	= 30;
		int	playHeight	= 30;

		// add play button:
		PImage[]	images	= { 
				parent.loadImage("playButton.png"),
				parent.loadImage("stopButton.png")
		};
		PImage	pauseImage	= parent.loadImage("pauseButton.png");

		images[0].resize(playWidth - 5, playHeight);
		images[1].resize(playWidth, playHeight);
		pauseImage.resize(playWidth, playHeight);
		
		this.getControlP5().addToggle("play")
		.setPosition(playX, playY)
		.setImages(images)
		.updateSize()
		.setClickable(true)
		.setGroup(this.getMenuTitle())
		.setTab("global");
		
		this.getControlP5().addToggle("pause")
		.setPosition((playX - playWidth - 10), playY)
		.setImages(pauseImage, images[0])
		.updateSize()
		.setVisible(false)
		.setGroup(this.getMenuTitle())
		.setTab("global");

		// Add hamburger and menuX:
		int	hamburgerX		= 10;
		int	hamburgerY		= 13;
		int	hamburgerWidth	= 30;
		int	hamburgerHeight	= 30;
		
		//int	menuXX			= 5;
		//int	menuXY			= 5;
		int	menuXWidth			= 15;
		
		PImage[]	hamXImages	= { 
				parent.loadImage("hamburger.png"),
				parent.loadImage("menuX.png")
		};
		hamXImages[0].resize(hamburgerWidth, hamburgerHeight);
		hamXImages[1].resize(menuXWidth, 0);


		PImage	hamburger	= parent.loadImage("hamburger.png");
		hamburger.resize(hamburgerWidth, hamburgerHeight);
		this.getControlP5().addToggle
		("hamburger")
		.setPosition(hamburgerX, hamburgerY)
		.setImages(hamXImages)
		.setClickable(true)
		.updateSize()
		.setGroup(this.getMenuTitle())
		.setTab("global");
		
	} // addOutsideButtons
	
	@Override
	public void setCanvasSize()
	{
		this.driver.getCanvas().fullScreen();
	}

	@Override
	public void sliderEvent(int id, float val) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buttonEvent(int id) 
	{
		
		
	}

	@Override
	public void colorWheelEvent(int id, Color color) {
		// TODO Auto-generated method stub
		
	}

}
