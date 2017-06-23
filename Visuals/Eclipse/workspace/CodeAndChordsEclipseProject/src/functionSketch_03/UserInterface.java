package functionSketch_03;

import controlP5.Button; 
import controlP5.ControlEvent;
import controlP5.ControlP5;
import controlP5.Controller;
import processing.core.PApplet;

public class UserInterface extends PApplet {


	private int fullScreenX;// = 925;
	private int fullScreenY;// = 520;

	private int menuWidth;//   = 300;
	private int menuScreenX;// = this.fullScreenX - this.menuWidth;
	private int menuScreenY;// = this.fullScreenY;

	private ControlP5 x;

	private boolean menuIsOpen;

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
		this.fullScreenX = 925;
		this.fullScreenY = 520;
		this.menuWidth = 300;
		this.menuScreenX = this.fullScreenX - this.menuWidth;
		this.menuScreenY = this.fullScreenY;
		this.menuIsOpen = false;

		this.x = new ControlP5(this); 
 
		this.setUpControls();  

		this.background(0);
		this.stroke(100);
		this.strokeWeight(2);

		System.out.println("done with setup");
	}

	public void draw() 
	{
		background(0);
		if(this.menuIsOpen) { line(this.menuWidth, 0, this.menuWidth, this.fullScreenY); }
		//else{background(0);}

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

		this.x.addSlider("heightSlider", 0f, (float)this.fullScreenX, 300, (0 + this.menuWidth/8), 200, 3*(this.menuWidth/4), 22)
		.setGroup("menu")
		.getCaptionLabel()
		.setVisible(false);

		this.x.addSlider("widthSlider", 0f, (float)this.fullScreenY, 400, (0 + this.menuWidth/8), 300, 3*(this.menuWidth/4), 22)
		.setGroup("menu")
		.getCaptionLabel()
		.setVisible(false);;
		
		this.x.getGroup("menu")
		.setVisible(false);



	}


	//convenience methods to set screen back and forth when menu pops out
	private float[] toFullScreen(float x, float y)
	{
		float xVal = map(x, 0, this.menuScreenX, 0, this.fullScreenX);
		float yVal = map(y, 0, this.menuScreenY, 0, this.fullScreenY);

		return new float[] {xVal, yVal}; 
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

		default:
			System.out.println("default switch");
			break;
		}

		System.out.println("done with control event method");

	}

	//convenience methods to set screen back and forth when menu pops out
	private float[] toMenuScreen(float x, float y)
	{
		float xVal = map(x, 0, this.fullScreenX, 0, this.menuScreenX);
		float yVal = map(y, 0, this.fullScreenY, 0, this.menuScreenY);

		xVal = xVal + this.menuWidth;

		return new float[] {xVal, yVal}; 	}

}
