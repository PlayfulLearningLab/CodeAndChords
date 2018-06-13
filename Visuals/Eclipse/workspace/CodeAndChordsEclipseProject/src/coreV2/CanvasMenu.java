package coreV2;

import java.awt.Color;

import processing.core.PApplet;
import processing.core.PImage;

public class CanvasMenu extends MenuTemplate
{

	
	
	public CanvasMenu() 
	{
		super("Canvas Menu");
		
		this.addOutsideButtons();
		this.getGroup().show();
		
		System.out.println("Done");
	}
	
	/**
	 * Adds "outside buttons": hamburger and play/pause/stop Buttons
	 */
	public void addOutsideButtons()
	{
		PApplet parent = ModuleDriver.getModuleDriver();
		
		int	playX		= parent.width - 45;
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
		.setGroup(this.getGroup());

		this.getControlP5().addToggle("pause")
		.setPosition((playX - playWidth - 10), playY)
		.setImages(pauseImage, images[0])
		.updateSize()
		.setVisible(false)
		.setGroup(this.getGroup());

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
		.moveTo("global")	// "global" means it will show in all tabs
		.updateSize()
		.setGroup(this.getGroup());
	} // addOutsideButtons

	@Override
	public void sliderEvent(int id, float val) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buttonEvent(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void colorWheelEvent(int id, Color color) {
		// TODO Auto-generated method stub
		
	}

}
