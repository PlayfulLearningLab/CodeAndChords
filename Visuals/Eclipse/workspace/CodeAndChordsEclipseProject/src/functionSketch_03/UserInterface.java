package functionSketch_03;

import controlP5.ControlEvent;
import controlP5.ControlP5;
import controlP5.Slider;
import processing.core.PApplet;

public class UserInterface extends PApplet {


	private int fullScreenX = 925;
	private int fullScreenY = 520;

	private int menuWidth = 300;
	private int menuScreenX = this.fullScreenX - this.menuWidth;

	private ControlP5 x;

	private boolean menuIsOpen = false;

	private String shape = "rect";

	private float rectHeight;
	private float rectWidth;
	private float rectPosX;
	private float rectPosY;
	
	private float morph;




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
		
		this.updatePositionSliders();

		this.background(0);


		System.out.println("done with setup");
	}

	public void draw() 
	{
		background(0);
		this.updateShape();

		if(this.menuIsOpen) 
		{ 
			this.stroke(0);
			this.fill(0);
			this.rect(0,0,this.menuWidth, this.fullScreenY);

			this.stroke(100);
			this.strokeWeight(2);
			line(this.menuWidth-2, 0, this.menuWidth-2, this.fullScreenY); 
		}



	}

	private void setUpControls()
	{
		this.x.addGroup("menu");
		this.x.addGroup("main");
		this.x.addGroup("shape");

		this.x.addButton("menuButton")
		.setPosition(20, 20)
		.setSize(50, 50)
		.setLabel("Position\nMenu")
		.setGroup("main");

		this.x.addButton("shapeMenuButton")
		.setPosition(20, 90)
		.setSize(50, 50)
		.setLabel("Shape\nMenu")
		.setGroup("main");


		this.x.addButton("closeMenuButton")
		.setPosition(this.menuWidth/4, 20)
		.setGroup("menu")
		.setSize(this.menuWidth/2, 50)
		.setLabel("Close Menu");

		this.x.addButton("closeButton")
		.setPosition(this.menuWidth/4, 20)
		.setGroup("shape")
		.setSize(this.menuWidth/2, 50)
		.setLabel("Close Menu");

		this.x.addLabel("size")
		.setText("Size")
		.setGroup("menu")
		.setPosition(this.menuWidth/8,125);

		this.x.addSlider("sizeSlider", 0f, 2, 1, (0 + this.menuWidth/8), 150, 3*(this.menuWidth/4), 22)
		.setGroup("menu")
		.getCaptionLabel()
		.setVisible(false);


		this.x.addLabel("xPos")
		.setText("X Position")
		.setGroup("menu")
		.setPosition(this.menuWidth/8,200);

		this.x.addSlider("xPosSlider", 0f, (float)this.fullScreenX, 100, (0 + this.menuWidth/8), 225, 3*(this.menuWidth/4), 22)
		.setGroup("menu")
		.getCaptionLabel()
		.setVisible(false);

		this.x.addLabel("yPos")
		.setText("Y Position")
		.setGroup("menu")
		.setPosition(this.menuWidth/8,275);

		this.x.addSlider("yPosSlider", 0f, (float)this.fullScreenY, 100, (0 + this.menuWidth/8), 300, 3*(this.menuWidth/4), 22)
		.setGroup("menu")
		.getCaptionLabel()
		.setVisible(false);

		this.x.addLabel("height")
		.setText("Height")
		.setGroup("menu")
		.setPosition(this.menuWidth/8,425);

		this.x.addSlider("heightSlider", 0f, (float)this.fullScreenY, 100, (0 + this.menuWidth/8), 450, 3*(this.menuWidth/4), 22)
		.setGroup("menu")
		.getCaptionLabel()
		.setVisible(false);

		this.x.addLabel("width")
		.setText("Width")
		.setGroup("menu")
		.setPosition(this.menuWidth/8,350);

		this.x.addSlider("widthSlider", 0f, (float)this.fullScreenX, 100, (0 + this.menuWidth/8), 375, 3*(this.menuWidth/4), 22)
		.setGroup("menu")
		.getCaptionLabel()
		.setVisible(false);

		this.x.addButton("rect")
		.setGroup("shape")
		.setPosition(30,100)
		.setSize(100,30);

		this.x.addButton("ellipse")
		.setGroup("shape")
		.setPosition(30,150)
		.setSize(100,30);

		this.x.addButton("square")
		.setGroup("shape")
		.setPosition(30,200)
		.setSize(100,30);

		this.x.addButton("circle")
		.setGroup("shape")
		.setPosition(30,250)
		.setSize(100,30);
		
		this.x.addButton("fill")
		.setGroup("shape")
		.setPosition(30,300)
		.setSize(100,30)
		.setLabel("fill screen");
		
		this.x.addButton("morph")
		.setGroup("shape")
		.setPosition(30,350)
		.setSize(100,30)
		.setLabel("morph");
		
		this.x.addSlider("morphSlider", -3f, 3, 0, 150, 350, 120, 30)
		.setGroup("shape")
		.getCaptionLabel()
		.setVisible(false);

		this.x.getGroup("menu")
		.setVisible(false);

		this.x.getGroup("shape")
		.setVisible(false);



	}

	public void updateShape()
	{
		this.fill(255);
		stroke(250);
		strokeWeight(3);
		
		switch(this.shape)
		{
		case "rect":
			rect(this.displayPos(this.rectPosX),this.rectPosY,this.displaySize(this.rectWidth),this.rectHeight);
			break;

		case "square":
			rect(this.displayPos(this.rectPosX),this.rectPosY,this.displaySize(this.rectHeight),this.rectHeight);
			break;

		case "ellipse":
			ellipse(this.displayPos(this.rectPosX+(this.rectWidth/2)),this.rectPosY+(this.rectHeight/2),this.displaySize(this.rectWidth),this.rectHeight);
			break;

		case "circle":
			ellipse(this.displayPos(this.rectPosX+(this.rectWidth/2)),this.rectPosY+(this.rectHeight/2),this.displaySize(this.rectWidth),this.rectWidth);
			break;
			
		case "morph":
			this.drawMorph(this.displayPos(this.rectPosX),this.rectPosY,this.displaySize(this.rectWidth),this.rectHeight);
			break;

		default:

			break;
		}


	}
	
	private void drawMorph(float xPos, float yPos, float width, float height)
	{
		float r = width;
		
		beginShape();
		
		for(float theta = 0; theta <= 2 * PI + .1; theta += .1f )
		{
			float x = rad(theta, r) * cos(theta);
			float y = rad(theta, r) * sin(theta);
			
			x += (xPos + width/2);
			y += (yPos + height/2);
			
			vertex(x,y);
		}
		
		endShape();
		
	}
	
	private float rad(float theta, float r)
	{
		float circle = r;
		float square = min( abs(r/cos(theta)), abs(r/sin(theta)) );
		
		float output = square + (circle - square)*this.morph;
		
		return output;
	}
	
	private void updatePositionSliders()
	{
		((Slider)this.x.getController("xPosSlider")).setRange(-(this.rectWidth), this.fullScreenX+this.rectWidth);
		((Slider)this.x.getController("yPosSlider")).setRange(-this.rectHeight, this.fullScreenY+this.rectHeight);
	}


	//handles ControlP5 events
	public void controlEvent(ControlEvent theEvent)
	{
		String name = theEvent.getName();

		switch(name)
		{
		case "menuButton":

			this.menuIsOpen = true;

			this.x.getGroup("main").setVisible(false);

			this.x.getGroup("menu")
			.setVisible(true);

			if(this.shape == "square" || this.shape == "circle")
			{
				this.x.getController("heightSlider").hide();
				this.x.get("height").hide();
			}
			else
			{
				this.x.getController("heightSlider").show();
				this.x.get("height").show();
			}


			System.out.println("menuButton Done");

			break;

		case "closeMenuButton":

			this.menuIsOpen = false;

			this.x.getGroup("menu")
			.setVisible(false);

			this.x.getGroup("main")
			.setVisible(true);

			System.out.println("closeMenuButton Done");

			break;

		case "closeButton":

			this.menuIsOpen = false;

			this.x.getGroup("shape")
			.setVisible(false);

			this.x.getGroup("main")
			.setVisible(true);

			System.out.println("closeMenuButton Done");

			break;

		case "shapeMenuButton":
			this.menuIsOpen = true;

			this.x.getGroup("main").setVisible(false);

			this.x.getGroup("shape")
			.setVisible(true);

			System.out.println("shape Done");

			break;

		case "rect":
			this.shape = "rect";
			break;

		case "ellipse":
			this.shape = "ellipse";
			break;

		case "square":
			this.shape = "square";
			break;

		case "circle":
			this.shape = "circle";
			break;
			
		case "fill":
			this.shape = "rect";
			this.rectPosX = 0;
			this.rectPosY = 0;
			this.rectHeight = this.fullScreenY;
			this.rectWidth = this.fullScreenX;
			
			this.x.getController("sizeSlider").setValue(1);
			this.x.getController("xPosSlider").setValue(0);
			this.x.getController("yPosSlider").setValue(0);
			this.x.getController("heightSlider").setValue(this.fullScreenY);
			this.x.getController("widthSlider").setValue(this.fullScreenX);
			
		case "morph":
			this.shape = "morph";
			break;

		case "heightSlider":
			this.rectHeight = this.x.getValue("heightSlider") * this.x.getValue("sizeSlider");
			this.updatePositionSliders();
			break;

		case "widthSlider":
			this.rectWidth = this.x.getValue("widthSlider") * this.x.getValue("sizeSlider");
			this.updatePositionSliders();
			break;

		case "xPosSlider":
			this.rectPosX = this.x.getValue("xPosSlider");
			break;

		case "yPosSlider":
			this.rectPosY = this.x.getValue("yPosSlider");
			break;

		case "sizeSlider":
			this.rectHeight = this.x.getValue("heightSlider") * this.x.getValue("sizeSlider");
			this.rectWidth = this.x.getValue("widthSlider") * this.x.getValue("sizeSlider");

			this.updatePositionSliders();
			
			break;
			
		case "morphSlider":
			this.morph = this.x.getValue("morphSlider");
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
