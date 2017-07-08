package functionSketch_03;

import java.awt.Color;
import java.util.HashMap;

import controlP5.Button;
import controlP5.ControlEvent;
import controlP5.ControlP5;
import controlP5.Controller;
import controlP5.ControllerGroup;
import controlP5.Group;
import controlP5.ScrollableList;
import controlP5.Slider;
import processing.core.PApplet;

public class PolarShapes extends PApplet
{
	private ControlP5 cp5;

	private HashMap<String, float[]>  shapeLib;

	private String    nextShapeName;
	private float[]   currentShape;
	private float[]   nextShape;
	
	private float     steps     = 500; 
	private float     incrament = (2*PI)/ this.steps;

	private float     radius;

	//[shape number][a,b,m1,m2,n1,n2,n3]
	private float[][] superShapes;

	private float     xStretch;
	private float     yStretch;

	private float     morphTime;




	private void initializeShapeLib()
	{
		HashMap<String, float[]> lib = new HashMap<String, float[]>();

		int i = 0;

		//make a circle
		
		float[] circle = new float[(int) this.steps];
		
		for(float theta = 0; theta <= 2 * PI; theta += this.incrament )
		{
			circle[i] = 100;
			i++;
		}
		lib.put("circle", circle);

		//make a square
		i = 0;
		float[] square = new float [(int) this.steps];
		System.out.println("making square");
		
		
		
		for(float theta = 0; theta <= 2 * PI; theta += this.incrament )
		{
			square[i] = min( abs(100/cos(theta)), abs(100/sin(theta)) );
			//System.out.println(square[i]);
			i++;
		}

		lib.put("square", square);

		this.shapeLib = lib;

		for(i = 0; i < this.steps; i++)
		{
			this.currentShape[i] = this.shapeLib.get(this.nextShapeName)[i];
		}

		//make super shape array
		//  { a, b, m1, m2, n1, n2, n3 }

		this.superShapes[0] = new float[] {	1,	1,	5,	5,	1,	1,	1};
		this.superShapes[1] = new float[] {	1,	1,	3,	3,	1,	1,	1};
		this.superShapes[2] = new float[] {	1,	1,	7,	7,	1,	4,	1};
		this.superShapes[3] = new float[] {	1,	1,	9,	9,	1,	1.6f,1};
		this.superShapes[4] = new float[] {	1,	1,	13,	13,	1.2f,1.4f,1};



		this.nextShape = this.shapeLib.get(this.nextShapeName);

	}//initializeShapeLib()
	
	private float[] copy(float[] f)
	{
		float[] result = new float[f.length];
		for(int i = 0; i < f.length; i++)
		{
			result[i] = f[i];
		}
		
		return result;
	}

	public static void main(String[] args)
	{
		PApplet.main("functionSketch_03.PolarShapes");
	}

	public void settings()
	{
		this.size(925, 520);
	}

	public void setup()
	{
		this.background(0);

		this.radius = 1;
		this.morphTime = 30;

		this.currentShape = new float[(int) this.steps];
		this.nextShape = new float[(int) this.steps];
		this.nextShapeName = "square";

		this.superShapes = new float[5][7];

		this.initializeShapeLib();

		this.currentShape = this.copy(this.shapeLib.get("circle"));
		this.nextShape = this.copy(this.shapeLib.get("circle"));
		
		this.initializeControls();

	}

	private void initializeControls()
	{
		this.cp5 = new ControlP5(this);

		this.cp5.addGroup("shape");
		this.cp5.addGroup("morph");

		//Make the menu toggle buttons

		this.cp5.addButton("shapeMenu")
		.setPosition(2, 2)
		.setSize(147, 46)
		.setLabel("Shape Menu")
		.setColorBackground(Color.GRAY.getRGB());

		this.cp5.addButton("morphMenu")
		.setPosition(151, 2)
		.setSize(147, 46
				)
		.setLabel("Morph Menu")
		.setColorBackground(Color.GRAY.getRGB());

		//Make the morph menu controls

		this.cp5.addButton("circle")
		.setPosition(2, 50)
		.setSize(296, 48)
		.setLabel("Circle")
		.setGroup("morph");

		this.cp5.addButton("square")
		.setPosition(2, 100)
		.setSize(296, 48)
		.setLabel("Square")
		.setGroup("morph");

		this.cp5.addButton("shape1")
		.setPosition(2, 150)
		.setSize(296, 48)
		.setLabel("Shape 1")
		.setGroup("morph");

		this.cp5.addButton("shape2")
		.setPosition(2, 200)
		.setSize(296, 48)
		.setLabel("Shape 2")
		.setGroup("morph");

		this.cp5.addButton("shape3")
		.setPosition(2, 250)
		.setSize(296, 48)
		.setLabel("Shape 3")
		.setGroup("morph");

		this.cp5.addButton("shape4")
		.setPosition(2, 300)
		.setSize(296, 48)
		.setLabel("Shape 4")
		.setGroup("morph");

		this.cp5.addButton("shape5")
		.setPosition(2, 350)
		.setSize(296, 48)
		.setLabel("Shape 5")
		.setGroup("morph");

		this.cp5.addSlider("morphTime", 1, 100, 30, 2, 450, 296, 48)
		.setGroup("morph")
		.getCaptionLabel()
		.hide();

		//Make shape menu controls

		this.cp5.addScrollableList("shapeSelect")
		.setPosition(2,60)
		.setSize(296, 100)
		.setBarHeight(30)
		.setGroup("shape")
		.addItems(new String[] {"shape1", "shape2", "shape3", "shape4", "shape5"})
		.close();

		this.cp5.addSlider("radius", .01f, 3, 1, 2, 120, 296, 28)
		.setGroup("shape")
		.getCaptionLabel()
		.hide();
		
		this.cp5.addSlider("a", .01f, 3, 1, 2, 170, 296, 28)
		.setGroup("shape")
		.getCaptionLabel()
		.hide();
		
		this.cp5.addSlider("b", .01f, 3, 1, 2, 220, 296, 28)
		.setGroup("shape")
		.getCaptionLabel()
		.hide();
		
		((Slider) this.cp5.addSlider("m1", 0, 15, 1, 2, 270, 296, 28)
		.setGroup("shape"))
		.setNumberOfTickMarks(16)
		.snapToTickMarks(true)
		.showTickMarks(false)
		.getCaptionLabel()
		.hide();
		
		((Slider) this.cp5.addSlider("m2", 0, 15, 1, 2, 320, 296, 28)
		.setGroup("shape"))
		.setNumberOfTickMarks(16)
		.snapToTickMarks(true)
		.showTickMarks(false)
		.getCaptionLabel()
		.hide();
		
		this.cp5.addSlider("n1", 0, 10, 1, 2, 370, 296, 28)
		.setGroup("shape")
		.getCaptionLabel()
		.hide();
		
		this.cp5.addSlider("n2", 0, 10, 1, 2, 420, 296, 28)
		.setGroup("shape")
		.getCaptionLabel()
		.hide();
		
		this.cp5.addSlider("n3", 0, 10, 1, 2, 470, 296, 28)
		.setGroup("shape")
		.getCaptionLabel()
		.hide();
		
		this.cp5.addLabel("radiusLabel")
		.setPosition(130,105)
		.setText("Radius")
		.setGroup("shape");
		
		this.cp5.addLabel("aLabel")
		.setPosition(130,155)
		.setText("A Value")
		.setGroup("shape");
		
		this.cp5.addLabel("bLabel")
		.setPosition(130,205)
		.setText("B Value")
		.setGroup("shape");
		
		this.cp5.addLabel("m1Label")
		.setPosition(130,255)
		.setText("M1 Value")
		.setGroup("shape");
		
		this.cp5.addLabel("m2Label")
		.setPosition(130,305)
		.setText("M2 Value")
		.setGroup("shape");
		
		this.cp5.addLabel("n1Label")
		.setPosition(130,355)
		.setText("N1 Value")
		.setGroup("shape");
		
		this.cp5.addLabel("n2Label")
		.setPosition(130,405)
		.setText("N2 Value")
		.setGroup("shape");
		
		this.cp5.addLabel("n3Label")
		.setPosition(130,455)
		.setText("N3 Value")
		.setGroup("shape");

		//Set morph menu visible to start

		((Group) this.cp5.get("shape"))
		.setVisible(false);

		((Group) this.cp5.get("morph"))
		.setVisible(true);
		
		//Bring dropbox to front
		
		((ScrollableList) this.cp5.get("shapeSelect")
		.bringToFront())
		.setValue(0);

	}

	public void draw()
	{
		this.background(0);

		this.drawShape();

		this.fill(0);
		this.stroke(0);
		this.rect(0, 0, 300, 520);
		this.stroke(100);
		this.strokeWeight(3);
		this.line(301,0,301,520);
		
		//this.cp5.get("a").setValue(this.cp5.getValue("b"));

	}

	private void drawShape()
	{

		stroke(255);
		noFill();



		//find out how much distance is between the current shape and the next shape at each point

		float[] distance = new float[(int) this.steps];

		for(int i = 0; i < distance.length; i++)
		{
			distance[i] = this.nextShape[i] - this.currentShape[i];
		}

		//move each point 1 pixel towards the goal shape

		for(int i = 0; i < this.steps; i++)
		{
			if(distance[i] != 0)
			{
				this.currentShape[i] += (distance[i]/this.morphTime);
				if(Math.abs(this.currentShape[i] - this.nextShape[i]) <= 3) { this.currentShape[i] = this.nextShape[i]; }
			}
		}//for loop

		//draw the current shape

		beginShape();

		int i = 0;
		
		float x;
		float y;


		for(float theta = 0; theta <= 2 * PI; theta += this.incrament )
		{
			if(this.cp5.get("a").isVisible()) { this.currentShape = this.copy(this.nextShape); }

			x = (this.currentShape[i] * this.radius) * cos(theta);
			y = (this.currentShape[i] * this.radius) * sin(theta);

			x += (300 + (625/2));
			y += 260;

			vertex(x,y);

			i++;
		}
		
		float theta = 0;
		i = 0;
		
		x = (this.currentShape[i] * this.radius) * cos(theta);
		y = (this.currentShape[i] * this.radius) * sin(theta);

		x += (300 + (625/2));
		y += 260;

		vertex(x,y);

		endShape();

	}//drawShape()
	
	private void updateSuperShape()
	{
		int index = (int) this.cp5.get("shapeSelect").getValue();
		
		this.superShapes[index] = new float[] { 
				this.cp5.get("a").getValue(),
				this.cp5.get("b").getValue(),
				this.cp5.get("m1").getValue(),
				this.cp5.get("m2").getValue(),
				this.cp5.get("n1").getValue(),
				this.cp5.get("n2").getValue(),
				this.cp5.get("n3").getValue()};
		
		this.currentShape = this.copy(this.makeSuperShape(this.superShapes[index]));
		this.nextShape = this.copy(this.makeSuperShape(this.superShapes[index]));
	}
	
	private void setSliders()
	{
		int index = (int) this.cp5.get("shapeSelect").getValue();
		
		System.out.println(index);
		
		this.cp5.get("a").setValue(this.superShapes[index][0]);
		this.cp5.get("m1").setValue(this.superShapes[index][2]);
		this.cp5.get("n1").setValue(this.superShapes[index][4]);
		this.cp5.get("n2").setValue(this.superShapes[index][5]);
		this.cp5.get("n3").setValue(this.superShapes[index][6]);

	}

	public void controlEvent(ControlEvent theEvent) throws InterruptedException
	{
		String name = theEvent.getName();

		switch(name)
		{
		case "circle":
			this.nextShape = (float[])this.shapeLib.get("circle");
			this.nextShapeName = "circle";

			System.out.println("changing to circle");
			//this.printFloat(this.shapeLib.get("circle"));
			//this.printFloat(this.nextShape);
			break;

		case "square":
			this.nextShape = (float[])this.shapeLib.get("square");
			this.nextShapeName = "square";

			System.out.println("changing to square");

			//this.printFloat(this.shapeLib.get("square"));
			//this.printFloat(this.nextShape);
			break;
			
		case "shapeSelect":
			int index = (int) this.cp5.get("shapeSelect").getValue();
			
			this.currentShape = this.copy(this.makeSuperShape(this.superShapes[index]));
			this.nextShape = this.copy(this.makeSuperShape(this.superShapes[index]));
			
			//this.setSliders();
			
			break;

		case "shape1":
			this.nextShape = makeSuperShape(this.superShapes[0]);
			break;

		case "shape2":
			this.nextShape = makeSuperShape(this.superShapes[1]);
			break;

		case "shape3":
			this.nextShape = makeSuperShape(this.superShapes[2]);
			break;

		case "shape4":
			this.nextShape = makeSuperShape(this.superShapes[3]);
			break;

		case "shape5":
			this.nextShape = makeSuperShape(this.superShapes[4]);
			break;

		case "shapeMenu":
			((Group) this.cp5.get("morph"))
			.hide();

			((Group) this.cp5.get("shape"))
			.show();
			
			this.currentShape = this.copy(this.makeSuperShape(this.superShapes[0]));
			this.nextShape = this.copy(this.makeSuperShape(this.superShapes[0]));
			
			this.cp5.get("shapeSelect")
			.setValue(0);
			
			//this.setSliders();

			break;

		case "morphMenu":
			((Group) this.cp5.get("shape"))
			.hide();

			((Group) this.cp5.get("morph"))
			.show();

			break;

		case "radius":

			this.radius = this.cp5.getValue("radius");

			break;
			
		case "a":
			this.updateSuperShape();
			this.cp5.get("b").setValue(this.cp5.getValue("a"));
			break;
			
		case "b":
			this.updateSuperShape();
			this.cp5.get("a").setValue(this.cp5.getValue("b"));
			break;
			
		case "m1":
			this.updateSuperShape();
			this.cp5.get("m2").setValue(this.cp5.getValue("m1"));
			break;
			
		case "m2":
			this.updateSuperShape();
			this.cp5.get("m1").setValue(this.cp5.getValue("m2"));
			break;
			
		case "n1":
			this.updateSuperShape();
			break;
			
		case "n2":
			this.updateSuperShape();
			break;
			
		case "n3":
			this.updateSuperShape();
			break;

		case "morphTime":
			this.morphTime = this.cp5.getValue("morphTime");
			break;


		default:

			break;
		}//switch

	}//ControlEvent()

	private float[] makeSuperShape(float[] ss)
	{
		float[] shape = new float[(int) this.steps];

		float a = ss[0];
		float b = ss[1];
		float m1 = ss[2];
		float m2 = ss[3];
		float n1 = ss[4];
		float n2 = ss[5];
		float n3 = ss[6];

		int i = 0;
		float r = 0;

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



			shape[i] = r * 100;
			i++;
		}

		return shape;
	}

	private void printFloat(float[] f)
	{
		for(int i = 0; i < f.length; i++)
		{
			System.out.println(f[i]);
		}
	}



}
