package functionSketch_03;

import controlP5.Button;
import controlP5.ControlEvent;
import controlP5.ControlP5;
import controlP5.Slider;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PShape;

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
	
	private float rotation;
	
	private float morph;
	private PShape mShape;
	private PShape cShape;
	
	private float     steps     = 500; 
	private float     incrament = (2*PI)/ this.steps;




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
		
		this.rotation = 0;
		
		this.updatePositionSliders();

		this.background(0);
		
		this.updateMorph();
		this.makeSuperShape();


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
		this.x.addGroup("custom");

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
		
		this.x.addButton("customMenuButton")
		.setPosition(20, 160)
		.setSize(50, 50)
		.setLabel("Custom\nShape")
		.setGroup("main");


		this.x.addButton("closeMenuButton")
		.setPosition(this.menuWidth/4, 20)
		.setGroup("menu")
		.setSize(this.menuWidth/2, 50)
		.setLabel("Close Menu");

		this.x.addButton("closeButton")
		.setPosition(this.menuWidth/4, 20)
		.setSize(this.menuWidth/2, 50)
		.setVisible(false)
		.setLabel("Close Menu");

		this.x.addLabel("size")
		.setText("Size")
		.setGroup("menu")
		.setPosition(this.menuWidth/8,125);

		this.x.addSlider("sizeSlider", 0.1f, 2, 1, (0 + this.menuWidth/8), 150, 3*(this.menuWidth/4), 22)
		.setGroup("menu")
		.getCaptionLabel()
		.setVisible(false);


		this.x.addLabel("xPos")
		.setText("X Position")
		.setGroup("menu")
		.setPosition(this.menuWidth/8,200);

		this.x.addSlider("xPosSlider", 0f, (float)this.fullScreenX, 400, (0 + this.menuWidth/8), 225, 3*(this.menuWidth/4), 22)
		.setGroup("menu")
		.getCaptionLabel()
		.setVisible(false);

		this.x.addLabel("yPos")
		.setText("Y Position")
		.setGroup("menu")
		.setPosition(this.menuWidth/8,275);

		this.x.addSlider("yPosSlider", 0f, (float)this.fullScreenY, 200, (0 + this.menuWidth/8), 300, 3*(this.menuWidth/4), 22)
		.setGroup("menu")
		.getCaptionLabel()
		.setVisible(false);

		this.x.addLabel("height")
		.setText("Height")
		.setGroup("menu")
		.setPosition(this.menuWidth/8,425);

		this.x.addSlider("heightSlider", 0.1f, (float)this.fullScreenY, 100, (0 + this.menuWidth/8), 450, 3*(this.menuWidth/4), 22)
		.setGroup("menu")
		.getCaptionLabel()
		.setVisible(false);
		
		this.x.addLabel("rotation")
		.setText("Rotation")
		.setGroup("menu")
		.setPosition(this.menuWidth/8,475);

		this.x.addSlider("rotationSlider", -2*PI, 2*PI, 0, (0 + this.menuWidth/8), 490, 3*(this.menuWidth/4), 22)
		.setGroup("menu")
		.getCaptionLabel()
		.setVisible(false);

		this.x.addLabel("width")
		.setText("Width")
		.setGroup("menu")
		.setPosition(this.menuWidth/8,350);

		this.x.addSlider("widthSlider", 0.1f, (float)this.fullScreenX, 100, (0 + this.menuWidth/8), 375, 3*(this.menuWidth/4), 22)
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
		
		
		
		this.x.addSlider("a", .01f, 3, 1, 2, 170, 296, 28)
		.setGroup("custom")
		.getCaptionLabel()
		.hide();
		
		this.x.addSlider("b", .01f, 3, 1, 2, 220, 296, 28)
		.setGroup("custom")
		.getCaptionLabel()
		.hide();
		
		((Slider) this.x.addSlider("m1", 0, 15, 5, 2, 270, 296, 28))
		.setGroup("custom")
		.getCaptionLabel()
		.hide();
		
		((Slider) this.x.addSlider("m2", 0, 15, 5, 2, 320, 296, 28))
		.setGroup("custom")
		.getCaptionLabel()
		.hide();
		
		this.x.addSlider("n1", 0, 10, 1, 2, 370, 296, 28)
		.setGroup("custom")
		.getCaptionLabel()
		.hide();
		
		this.x.addSlider("n2", 0, 10, 1, 2, 420, 296, 28)
		.setGroup("custom")
		.getCaptionLabel()
		.hide();
		
		this.x.addSlider("n3", 0, 10, 1, 2, 470, 296, 28)
		.setGroup("custom")
		.getCaptionLabel()
		.hide();

		
		this.x.addLabel("aLabel")
		.setPosition(130,155)
		.setText("A Value")
		.setGroup("custom");
		
		this.x.addLabel("bLabel")
		.setPosition(130,205)
		.setText("B Value")
		.setGroup("custom");
		
		this.x.addLabel("m1Label")
		.setPosition(130,255)
		.setText("M1 Value")
		.setGroup("custom");
		
		this.x.addLabel("m2Label")
		.setPosition(130,305)
		.setText("M2 Value")
		.setGroup("custom");
		
		this.x.addLabel("n1Label")
		.setPosition(130,355)
		.setText("N1 Value")
		.setGroup("custom");
		
		this.x.addLabel("n2Label")
		.setPosition(130,405)
		.setText("N2 Value")
		.setGroup("custom");
		
		this.x.addLabel("n3Label")
		.setPosition(130,455)
		.setText("N3 Value")
		.setGroup("custom");
		
		
		this.x.getGroup("custom")
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
		
		PShape shape = null;
		
		switch(this.shape)
		{
		case "rect":
			shape = createShape(PConstants.RECT,this.displaySize(this.rectWidth/-2),this.rectHeight/-2,this.displaySize(this.rectWidth),this.rectHeight);
			//rect(this.displayPos(this.rectPosX),this.rectPosY,this.displaySize(this.rectWidth),this.rectHeight);
			break;

		case "square":
			shape = createShape(PConstants.RECT,this.displaySize(this.rectWidth/-2),this.rectHeight/-2,this.displaySize(this.rectWidth),this.rectWidth);
			//rect(this.displayPos(this.rectPosX),this.rectPosY,this.displaySize(this.rectWidth),this.rectWidth);
			break;

		case "ellipse":
			shape = createShape(PConstants.ELLIPSE,0,0,this.displaySize(this.rectWidth),this.rectHeight);
			//ellipse(this.displayPos(this.rectPosX+(this.rectWidth/2)),this.rectPosY+(this.rectHeight/2),this.displaySize(this.rectWidth),this.rectHeight);
			break;

		case "circle":
			shape = createShape(PConstants.ELLIPSE,0,0,this.displaySize(this.rectWidth),this.rectWidth);
			//ellipse(this.displayPos(this.rectPosX+(this.rectWidth/2)),this.rectPosY+(this.rectHeight/2),this.displaySize(this.rectWidth),this.rectWidth);
			break;
			
		case "morph":
			this.shape(this.mShape,this.displayPos(this.rectPosX), this.rectPosY);
			//this.drawMorph();
			//this.drawMorph(this.displayPos(this.rectPosX),this.rectPosY,this.displaySize(this.rectWidth),this.rectHeight);
			break;
			
		case "custom":
			this.shape(this.cShape, this.displayPos(this.rectPosX), this.rectPosY);
			break;

		default:

			break;
		}
		
		if(this.shape != "morph" && this.shape != "custom")
		{
			shape.rotate(this.rotation);
			shape(shape,this.displayPos(this.rectPosX), this.rectPosY);
		}
		

	}
	
	private void updateMorph()
	{
		float r = (this.rectWidth/2);
		
		PShape shape = this.createShape();
		
		shape.beginShape();
		
		shape.stroke(255);
		shape.fill(255);
		
		float incrament = 2*PI / 200;
		float x1 = 0;
		float y1 = 0;
		
		for(float theta = 0; theta <= 2 * PI; theta += incrament )
		{
			x1 = rad(theta, r) * cos(theta);
			y1 = rad(theta, r) * sin(theta);
			
			shape.vertex(this.displaySize(x1),y1);
		}
		shape.endShape();
		
		shape.rotate(this.rotation);
		
		this.mShape = shape;
		
		//shape.setVisible(true);
		
		//shape(shape,this.displayPos(this.rectPosX), this.rectPosY);
		
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

			if(this.shape == "square" || this.shape == "circle" || this.shape == "morph" || this.shape == "custom")
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
			.hide();

			this.x.getGroup("main")
			.show();

			System.out.println("closeMenuButton Done");

			break;

		case "closeButton":

			this.menuIsOpen = false;

			this.x.getGroup("shape")
			.hide();
			
			this.x.getGroup("custom")
			.hide();

			this.x.getGroup("main")
			.show();
			
			((Button) this.x.get("closeButton")).hide();

			System.out.println("closeButton Done");

			break;

		case "shapeMenuButton":
			this.menuIsOpen = true;

			this.x.getGroup("main").setVisible(false);

			this.x.getGroup("shape")
			.setVisible(true);
			
			((Button) this.x.get("closeButton")).setVisible(true);

			System.out.println("shape Done");

			break;
			
		case "customMenuButton":
			this.menuIsOpen = true;

			this.x.getGroup("main").setVisible(false);
			
			((Button) this.x.get("closeButton")).setVisible(true);

			this.x.getGroup("custom")
			.show(); 
			
			this.shape = "custom";

			System.out.println("custom Done");

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
			this.rectPosX = this.fullScreenX/2;
			this.rectPosY = this.fullScreenY/2;
			this.rectHeight = this.fullScreenY;
			this.rectWidth = this.fullScreenX;
			
			this.x.getController("sizeSlider").setValue(1);
			this.x.getController("xPosSlider").setValue(this.fullScreenX/2);
			this.x.getController("yPosSlider").setValue(this.fullScreenY/2);
			this.x.getController("heightSlider").setValue(this.fullScreenY);
			this.x.getController("widthSlider").setValue(this.fullScreenX);
			
		case "morph":
			this.shape = "morph";
			this.updateMorph();
			break;

		case "heightSlider":
			this.rectHeight = this.x.getValue("heightSlider") * this.x.getValue("sizeSlider");
			this.updatePositionSliders();
			this.updateMorph();
			break;

		case "widthSlider":
			this.rectWidth = this.x.getValue("widthSlider") * this.x.getValue("sizeSlider");
			this.updatePositionSliders();
			this.updateMorph();
			break;

		case "xPosSlider":
			this.rectPosX = this.x.getValue("xPosSlider");
			break;

		case "yPosSlider":
			this.rectPosY = this.x.getValue("yPosSlider");
			break;
			
		case "rotationSlider":
			this.rotation = this.x.getValue("rotationSlider");
			this.updateMorph();
			break;

		case "sizeSlider":
			this.rectHeight = this.x.getValue("heightSlider") * this.x.getValue("sizeSlider");
			this.rectWidth = this.x.getValue("widthSlider") * this.x.getValue("sizeSlider");
			this.updateMorph();

			this.updatePositionSliders();
			
			break;
			
		case "morphSlider":
			this.morph = this.x.getValue("morphSlider");
			this.updateMorph();
			break;

		default:
			System.out.println("default switch");
			break;
		}
		
		if (this.shape == "custom") { this.makeSuperShape(); }
		
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
	
	private PShape makeSuperShape()
	{
		float a = this.x.getValue("a");
		float b = this.x.getValue("b");
		float m1 = this.x.getValue("m1");
		float m2 = this.x.getValue("m2");
		float n1 = this.x.getValue("n1");
		float n2 = this.x.getValue("n2");
		float n3 = this.x.getValue("n3");

		float r;
		
		float x;
		float y;
		
		PShape ps = this.createShape();
		ps.beginShape();
		
		ps.stroke(255);
		ps.fill(255);

		for(float theta = 0; theta < 2*PI; theta += this.incrament)
		{
			float part1 = (1 / a) * cos(theta * m1 / 4);
			part1 = abs(part1);
			part1 = pow(part1, n2);

			float part2 = (1 / b) * sin(theta * m2 / 4);
			part2 = abs(part2);
			part2 = pow(part2, n3);

			float part3 = pow(part1 + part2, 1 / n1);

			if (part3 == 0) {
				r = 0;
			}
			else { r = (1 / part3); }
			
			x = (r * this.rectWidth * .5f) * cos(theta);
			y = (r * this.rectWidth * .5f) * sin(theta);

			
			System.out.println(x + "    " + y);
			ps.vertex(x,y);
		}
		
		ps.endShape();
		
		ps.rotate(this.rotation);
		
		this.cShape = ps;

		return ps;
	}

}
