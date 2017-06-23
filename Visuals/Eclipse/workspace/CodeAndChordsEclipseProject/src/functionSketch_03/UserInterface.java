package functionSketch_03;

import controlP5.Button; 
import controlP5.ControlEvent;
import controlP5.ControlP5;
import controlP5.Controller;
import processing.core.PApplet;

public class UserInterface extends PApplet {


	private int fullScreenX = 925;
	private int fullScreenY = 520;

	private int menuWidth = 300;
	private int menuScreenX = this.fullScreenX - this.menuWidth;

	private ControlP5 x;

	private boolean menuIsOpen = false;

	private float rectHeight;
	private float rectWidth;
	private float rectPosX;
	private float rectPosY;




	public static void main(String[] args)
	{
		PApplet.main("functionSketch_03.UserInterface");
	}// main 


	public void settings()  
	{
		this.size(925, 520);
	}

	public void setup()
	{ 

		this.x = new ControlP5(this); 

		this.setUpControls();  
		this.rectWidth = this.x.getValue("widthSlider");
		this.rectHeight = this.x.getValue("heightSlider");
		this.rectPosX = this.x.getValue("xPosSlider");
		this.rectPosY = this.x.getValue("yPosSlider");

		this.background(0);


		System.out.println("done with setup");
	}

	public void draw() 
	{
		background(0);
		if(this.menuIsOpen) 
		{ 
			this.stroke(100);
			this.strokeWeight(2);
			line(this.menuWidth, 0, this.menuWidth, this.fullScreenY); 
		}

		this.updateRectangle();

	}

	private void setUpControls()
	{
		this.x.addGroup("menu");

		this.x.addButton("menuButton")
		.setPosition(20, 20)
		.setSize(50, 50)
		.setLabel("Menu"); 


		this.x.addButton("closeMenuButton")
		.setPosition(this.menuWidth/4, 20)
		.setGroup("menu")
		.setSize(this.menuWidth/2, 50)
		.setLabel("Close Menu");

		this.x.addLabel("xPos")
		.setText("X Position")
		.setGroup("menu")
		.setPosition(this.menuWidth/8,125);
		
		this.x.addSlider("xPosSlider", 0f, (float)this.fullScreenX, 100, (0 + this.menuWidth/8), 150, 3*(this.menuWidth/4), 22)
		.setGroup("menu")
		.getCaptionLabel()
		.setVisible(false);
		
		this.x.addLabel("yPos")
		.setText("Y Position")
		.setGroup("menu")
		.setPosition(this.menuWidth/8,225);

		this.x.addSlider("yPosSlider", 0f, (float)this.fullScreenX, 100, (0 + this.menuWidth/8), 250, 3*(this.menuWidth/4), 22)
		.setGroup("menu")
		.getCaptionLabel()
		.setVisible(false);
		
		this.x.addLabel("height")
		.setText("Rectangle Height")
		.setGroup("menu")
		.setPosition(this.menuWidth/8,325);

		this.x.addSlider("heightSlider", 0f, (float)this.fullScreenX, 100, (0 + this.menuWidth/8), 350, 3*(this.menuWidth/4), 22)
		.setGroup("menu")
		.getCaptionLabel()
		.setVisible(false);
		
		this.x.addLabel("width")
		.setText("Rectangle Width")
		.setGroup("menu")
		.setPosition(this.menuWidth/8,425);

		this.x.addSlider("widthSlider", 0f, (float)this.fullScreenY, 100, (0 + this.menuWidth/8), 450, 3*(this.menuWidth/4), 22)
		.setGroup("menu")
		.getCaptionLabel()
		.setVisible(false);;

		this.x.getGroup("menu")
		.setVisible(false);



	}

	public void updateRectangle()
	{
		stroke(250);
		strokeWeight(3);
		rect(this.displayPos(this.rectPosX),this.rectPosY,this.displaySize(this.rectWidth),this.rectHeight);
	}


	//handles ControlP5 events
	public void controlEvent(ControlEvent theEvent)
	{
		String name = theEvent.getName();

		switch(name)
		{
		case "menuButton":

			this.menuIsOpen = true;

			this.x.getController("menuButton").hide();

			this.x.getGroup("menu")
			.setVisible(true);


			System.out.println("menuButton Done");

			break;

		case "closeMenuButton":

			this.menuIsOpen = false;

			this.x.getGroup("menu")
			.setVisible(false);

			this.x.getController("menuButton")
			.setVisible(true);

			System.out.println("closeMenuButton Done");

			break;

		case "heightSlider":
			this.rectHeight = this.x.getValue("heightSlider");
			break;

		case "widthSlider":
			this.rectWidth = this.x.getValue("widthSlider");
			break;
			
		case "xPosSlider":
			this.rectPosX = this.x.getValue("xPosSlider");
			break;
			
		case "yPosSlider":
			this.rectPosY = this.x.getValue("yPosSlider");
			break;

		default:
			System.out.println("default switch");
			break;
		}

		System.out.println("done with control event method");

	}

	//checks if menu is open and adjusts values accordingly
	private float displayPos(float x)
	{
		if(this.menuIsOpen)
		{
			x = map(x, 0, this.fullScreenX, 0, this.menuScreenX);
			x = x + this.menuWidth;
		}

		return x;
	}

	private float displaySize(float x)
	{
		if(this.menuIsOpen)
		{
			x = map(x, 0, this.fullScreenX, 0, this.menuScreenX);
		}

		return x;
	}

}
