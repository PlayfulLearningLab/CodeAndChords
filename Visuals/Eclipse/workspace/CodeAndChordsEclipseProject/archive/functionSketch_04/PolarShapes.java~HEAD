package functionSketch_04;

import java.awt.Color;
import java.util.HashMap;

import controlP5.Button;
import controlP5.ControlEvent;
import controlP5.ControlP5;
import controlP5.ControlP5Constants;
import controlP5.Controller;
import controlP5.ControllerGroup;
import controlP5.Group;
import controlP5.ScrollableList;
import controlP5.Slider;
import processing.core.PApplet;
import processing.core.PShape;


/*
 * This is version 1 of shape morphing.
 * 
 * 		This version is all contained within the class PolarShapes, but moving forward,
 * 			an abstract class named Shape will be created so that different kinds of 
 * 			shapes can be created as child classes.  Supershape will be one such child,
 * 			named after the formula that is used to create it, the superformula
 */


@SuppressWarnings("unused")
public class PolarShapes extends PApplet
{
	private ControlP5 cp5;

	private HashMap<String, float[]>  shapeLib;

	private String    nextShapeName;
	private int       nextShapeIndex;
	private float[]   currentShape;
	private float[]   nextShape;

	private float     steps     = 468; 
	private float     incrament = (2*PI)/ this.steps;

	private float     radius;

	//[shape number][a,b,m1,m2,n1,n2,n3]
	private float[][] superShapes;

	private float     xStretch;
	private float     yStretch;

	private float     rotation;

	private float     morphTime = 30;
	private int       morphType = 0;

	private boolean   settingSliders = false;

	//acceleration stuff
	private float[]		velocity = new float[(int)this.steps];

	private float       accelerationIndex = 0;
	private float       accelerationSpeed = .6f;

	private float       decay = 20;
	private float       maxVelocity = 80;

	private int         triggerIndex = 0;
	private int         lastTriggerTime = this.millis();
	
	
	
	private float      	radForCusp;



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
		PApplet.main("functionSketch_04.PolarShapes");
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

		this.currentShape = this.copy(this.shapeLib.get("square"));
		this.nextShape = this.copy(this.shapeLib.get("square"));

		for(int i = 0; i < this.velocity.length; i++)
		{
			this.velocity[i] = 0;
		}

		this.initializeControls();

		System.out.println("done with setup");

	}

	private void initializeControls()
	{
		System.out.println("CHECKPOINT 1");

		this.cp5 = new ControlP5(this);

		System.out.println("CHECKPOINT 2");

		this.cp5.addGroup("shape");
		this.cp5.addGroup("morph");
		this.cp5.addGroup("velocity");

		//Make the menu toggle buttons

		this.cp5.addButton("shapeMenu")
		.setPosition(2, 2)
		.setSize(97, 46)
		.setLabel("Shape")
		.setColorBackground(Color.GRAY.getRGB());

		this.cp5.addButton("morphMenu")
		.setPosition(101, 2)
		.setSize(97, 46)
		.setLabel("Morph")
		.setColorBackground(Color.GRAY.getRGB());

		this.cp5.addButton("velocityMenu")
		.setPosition(201, 2)
		.setSize(97, 46)
		.setLabel("Velocity")
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

		this.cp5.addSlider("rotationSlider", -2*PI, 2*PI, 0, 2, 450, 296, 48)
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

		((Slider) this.cp5.addSlider("m1", 0, 15, 1, 2, 270, 296, 28))
		.setGroup("shape")
		.getCaptionLabel()
		.hide();

		((Slider) this.cp5.addSlider("m2", 0, 15, 1, 2, 320, 296, 28))
		.setGroup("shape")
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




		this.cp5.addScrollableList("morphTypeList")
		.setPosition(2,105)
		.setSize(296, 100)
		.setBarHeight(30)
		.setGroup("velocity")
		.addItems(new String[] {"Morph Type 1", "Morph Type 2", "shape3", "shape4", "shape5"})
		.close();

		this.cp5.addLabel("maxVelocity")
		.setPosition(130,155)
		.setText("Max Velocity")
		.setGroup("velocity");

		this.cp5.addLabel("accSpeed")
		.setPosition(130,205)
		.setText("Acceleration Speed")
		.setGroup("velocity");

		this.cp5.addLabel("morphTime")
		.setPosition(130,255)
		.setText("Morph Time")
		.setGroup("velocity");

		this.cp5.addLabel("decay")
		.setPosition(130,305)
		.setText("Decay")
		.setGroup("velocity");



		this.cp5.addSlider("maxVelocitySlider", 1, 200, 80, 2, 170, 296, 28)
		.setGroup("velocity")
		.getCaptionLabel()
		.hide();

		this.cp5.addSlider("accSpeedSlider", .01f, 1, .6f, 2, 220, 296, 28)
		.setGroup("velocity")
		.getCaptionLabel()
		.hide();

		((Slider) this.cp5.addSlider("morphTimeSlider", 1 , 100, 30, 2, 270, 296, 28))
		.setGroup("velocity")
		.getCaptionLabel()
		.hide();

		((Slider) this.cp5.addSlider("decaySlider", 1, 100, 25, 2, 320, 296, 28))
		.setGroup("velocity")
		.getCaptionLabel()
		.hide();



		//Set morph menu visible to start

		((Group) this.cp5.get("shape"))
		.setVisible(false);

		((Group) this.cp5.get("velocity"))
		.setVisible(false);

		((Group) this.cp5.get("morph"))
		.setVisible(true);

		//Bring dropbox to front

		this.cp5.get("shapeSelect").bringToFront();
		this.cp5.get("morphTypeList").bringToFront();
		//.setValue(0);

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

		if(this.cp5.get("maxVelocitySlider").isVisible() && this.millis() >= this.lastTriggerTime + 3000)
		{
			this.accelerationIndex = 1;
			this.nextShape = this.makeSuperShape(this.superShapes[this.triggerIndex%5]);
			this.nextShapeIndex = this.triggerIndex%5;
			this.triggerIndex++;
			this.lastTriggerTime = this.millis();
		}

		//this.cp5.get("a").setValue(this.cp5.getValue("b"));

	}

	private void drawShape()
	{
		//find out how much distance is between the current shape and the next shape at each point

		float[] distance = new float[(int) this.steps];

		for(int i = 0; i < distance.length; i++)
		{
			distance[i] = this.nextShape[i] - this.currentShape[i];
		}

		//move each point 1 pixel towards the goal shape

		for(int i = 0; i < this.steps; i++)
		{
			if(this.morphType == 0)
			{
				if(Math.abs(distance[i]) == 0.0)
				{
					this.currentShape[i] = this.nextShape[i];
				}
				else
				{
					this.velocity[i] += (distance[i]/this.morphTime)*(this.accelerationIndex/20) - (this.velocity[i]/this.decay);
					this.currentShape[i] += this.velocity[i];


					//if(Math.abs(this.currentShape[i] - this.nextShape[i]) <= .5) { this.currentShape[i] = this.nextShape[i]; }
				}

			}//morph type 0


			if(this.morphType == 1)
			{
				if(Math.abs(distance[i]) < 0.05f)
				{
					this.currentShape[i] = this.nextShape[i]; 
				}
				else
				{
					this.velocity[i] += (distance[i]/this.morphTime)*(this.accelerationIndex/20) - (this.velocity[i]/this.decay);
					this.currentShape[i] += this.velocity[i];

				}
			}

		}//for loop

		if(this.accelerationIndex < this.maxVelocity) this.accelerationIndex += this.accelerationSpeed;

		//draw the current shape

		PShape shape = this.createShape();

		shape.beginShape();
		shape.stroke(255);
		shape.noFill();

		int i = 0;
		float theta = 0;
		float x;
		float y;


		for(theta = 0; theta <= 2*PI; theta += this.incrament )
		{
			if(this.cp5.get("a").isVisible()) { this.currentShape = this.copy(this.nextShape); }

			x = (this.currentShape[i] * this.radius) * cos(theta);
			y = (this.currentShape[i] * this.radius) * sin(theta);
			//x += (300 + (625/2));
			//y += 260;

			//place marker

			shape.vertex(x,y);
			
			if(Math.abs(this.currentShape[i] - this.nextShape[i]) == 0 && i > 1 && i < this.steps - 3 && false) 
			{
				float cusp = this.checkForCuspBeforeNextVertex(1, new float[] {this.currentShape[i - 1], this.currentShape[i], this.currentShape[i + 1], this.currentShape[i + 2]}, this.incrament, (theta - this.incrament), 0 );
				x = (cusp*this.radius) * cos(this.radForCusp);
				y = (cusp*this.radius) * sin(this.radForCusp);
				System.out.println(cusp + "  -  " + this.radForCusp);  // this.radForCusp is always 0 -- find out why
				shape.vertex(x,y);
				
			}

			i++;
		}

		theta = 0;
		i = 0;

		x = (this.currentShape[i] * this.radius) * cos(theta);
		y = (this.currentShape[i] * this.radius) * sin(theta);

		//x += (300 + (625/2));
		//y += 260;

		shape.vertex(x,y);

		shape.rotate(this.rotation);

		shape.endShape();

		this.shape(shape, (300 + (625/2)), 260);

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

		this.nextShape = this.copy(this.makeSuperShape(this.superShapes[index]));
	}

	private void setSliders(int index)
	{		
		System.out.println(index);

		this.settingSliders = true;

		this.cp5.get("a").setValue(this.superShapes[index][0]);
		this.cp5.get("b").setValue(this.superShapes[index][1]);
		this.cp5.get("m1").setValue(this.superShapes[index][2]);
		this.cp5.get("m2").setValue(this.superShapes[index][3]);
		this.cp5.get("n1").setValue(this.superShapes[index][4]);
		this.cp5.get("n2").setValue(this.superShapes[index][5]);
		this.cp5.get("n3").setValue(this.superShapes[index][6]);

		this.settingSliders = false;

		this.updateSuperShape();

	}

	public void controlEvent(ControlEvent theEvent) throws InterruptedException
	{
		System.out.println("Starting ControlEvent()");

		String name = theEvent.getName();

		System.out.println("ControlEvent:	" + name);

		switch(name)
		{
		case "circle":
			this.nextShape = (float[])this.shapeLib.get("circle");
			this.nextShapeName = "circle";
			this.accelerationIndex = 1;
			this.resetVelocity();


			System.out.println("changing to circle");
			//this.printFloat(this.shapeLib.get("circle"));
			//this.printFloat(this.nextShape);
			break;

		case "square":
			this.nextShape = (float[])this.shapeLib.get("square");
			this.nextShapeName = "square";
			this.accelerationIndex = 1;
			this.resetVelocity();


			System.out.println("changing to square");

			//this.printFloat(this.shapeLib.get("square"));
			//this.printFloat(this.nextShape);
			break;

		case "shapeSelect":
			int index = (int) this.cp5.get("shapeSelect").getValue();

			//this.currentShape = this.copy(this.makeSuperShape(this.superShapes[index]));
			//this.nextShape = this.copy(this.makeSuperShape(this.superShapes[index]));

			this.setSliders(index);

			break;

		case "shape1":
			this.nextShape = makeSuperShape(this.superShapes[0]);
			this.nextShapeIndex = 0;
			this.accelerationIndex = 1;
			this.resetVelocity();
			break;

		case "shape2":
			this.nextShape = makeSuperShape(this.superShapes[1]);
			this.nextShapeIndex = 1;
			this.accelerationIndex = 1;
			this.resetVelocity();

			break;

		case "shape3":
			this.nextShape = makeSuperShape(this.superShapes[2]);
			this.nextShapeIndex = 2;
			this.accelerationIndex = 1;
			this.resetVelocity();

			break;

		case "shape4":
			this.nextShape = makeSuperShape(this.superShapes[3]);
			this.nextShapeIndex = 3;
			this.accelerationIndex = 1;
			this.resetVelocity();

			break;

		case "shape5":
			this.nextShape = makeSuperShape(this.superShapes[4]);
			this.nextShapeIndex = 4;
			this.accelerationIndex = 1;
			this.resetVelocity();

			break;

		case "shapeMenu":
			((Group) this.cp5.get("morph"))
			.hide();

			((Group) this.cp5.get("velocity"))
			.hide();

			((Group) this.cp5.get("shape"))
			.show();

			this.nextShape = this.copy(this.makeSuperShape(this.superShapes[0]));

			this.cp5.get("shapeSelect")
			.setValue(0);

			this.setSliders((int) this.cp5.get("shapeSelect").getValue());

			break;

		case "morphMenu":
			((Group) this.cp5.get("shape"))
			.hide();

			((Group) this.cp5.get("velocity"))
			.hide();

			((Group) this.cp5.get("morph"))
			.show();

			break;

		case "velocityMenu":
			((Group) this.cp5.get("shape"))
			.hide();

			((Group) this.cp5.get("velocity"))
			.show();

			((Group) this.cp5.get("morph"))
			.hide();

			break;

		case "radius":

			this.radius = this.cp5.getValue("radius");

			break;

		case "a":
			if (!this.settingSliders) this.updateSuperShape();
			//this.cp5.get("b").setValue(this.cp5.getValue("a"));
			break;

		case "b":
			if (!this.settingSliders) this.updateSuperShape();
			//this.cp5.get("a").setValue(this.cp5.getValue("b"));
			break;

		case "m1":
			//this.cp5.get("m1").setValue((int) this.cp5.getValue("m1"));
			if (!this.settingSliders) this.updateSuperShape();
			//this.cp5.get("m2").setValue(this.cp5.getValue("m1"));
			break;

		case "m2":
			//this.cp5.get("m2").setValue((int) this.cp5.getValue("m2"));
			if (!this.settingSliders) this.updateSuperShape();
			//this.cp5.get("m1").setValue(this.cp5.getValue("m2"));
			break;

		case "n1":
			if (!this.settingSliders) this.updateSuperShape();
			break;

		case "n2":
			if (!this.settingSliders) this.updateSuperShape();
			break;

		case "n3":
			if (!this.settingSliders) this.updateSuperShape();
			break;

		case "morphTime":
			this.morphTime = this.cp5.getValue("morphTime");
			break;

		case "maxVelocitySlider":
			this.maxVelocity = this.cp5.getValue(name);
			break;

		case "accSpeedSlider":
			this.accelerationSpeed = this.cp5.getValue(name);
			break;

		case "morphTimeSlider":
			this.morphTime = this.cp5.getValue(name);
			break;

		case "decaySlider":
			this.decay = this.cp5.getValue(name);
			break;

		case "rotationSlider":
			this.rotation = this.cp5.getValue("rotationSlider");
			break;

		case "morphTypeList":
			this.morphType = (int) this.cp5.getValue("morphTypeList");
			break;


		default:

			break;
		}//switch

		//if(this.cp5.get("a").isVisible()) this.updateSuperShape();

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

	private float calculateSuperShapePoint(float theta)
	{
		float r = 0;

		float a = this.superShapes[this.nextShapeIndex][0];
		float b = this.superShapes[this.nextShapeIndex][1];
		float m1 = this.superShapes[this.nextShapeIndex][2];
		float m2 = this.superShapes[this.nextShapeIndex][3];
		float n1 = this.superShapes[this.nextShapeIndex][4];
		float n2 = this.superShapes[this.nextShapeIndex][5];
		float n3 = this.superShapes[this.nextShapeIndex][6];

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


		return r*100;
	}

	private void printFloat(float[] f)
	{
		for(int i = 0; i < f.length; i++)
		{
			System.out.println(f[i]);
		}
	}

	private void resetVelocity()
	{
		for(int i = 0; i < this.velocity.length; i++)
		{
			//this.velocity[i] = 0;
		}
	}

	private float checkForCuspBeforeNextVertex(int callNum, float[] vals, float incrament, float theta0, float radToBreakpoint)
	{
		float poiThetaValue = -1;//poi: point of inflection

		if(callNum == 1)
		{
			float der1;
			float der2;
			
			radToBreakpoint = (float) (theta0 + (incrament*1.5));

			der1 = this.calculateDerivative(vals[0], vals[2], incrament);
			der2 = this.calculateDerivative(vals[1], vals[3], incrament);

			if((der1 * der2) < 0 )//this only checks for positive point of inflection which will give circles a hard time because every point qualifies
			{
				poiThetaValue = this.checkForCuspBeforeNextVertex(	callNum + 1, 
																	new float[] {	vals[0], 
																					vals[1], 
																					this.calculateSuperShapePoint((float)(theta0 + (incrament*1.5))), 
																					vals[2], 
																					vals[3]
																				}, 
																	incrament, 
																	theta0,
																	radToBreakpoint);
			}
		}//if(firstCall)
		else
		{
			float der1;
			float der2;
			float der3;

			System.out.println("call num: " + callNum);
			System.out.println("vals[] length: " + vals.length);
			
			der1 = this.calculateDerivative(vals[0], vals[2], incrament);
			der2 = this.calculateDerivative(vals[1], vals[3], incrament/2);
			der3 = this.calculateDerivative(vals[2], vals[4], incrament);
			
			//need to look at increment
			if( radToBreakpoint < PI/1000 ) //this segment is small enough
			{
				if((der1 * der2) < 0)
				{
					this.radForCusp = theta0 + radToBreakpoint;
					radToBreakpoint -= (incrament/Math.pow(2, (callNum)));
					poiThetaValue = this.checkForCuspBeforeNextVertex(callNum + 1, new float[] { vals[0], vals[1], this.calculateSuperShapePoint(radToBreakpoint), vals[2], vals[3] }, incrament, theta0, radToBreakpoint);
				}
				else if((der2 * der3) < 0)
				{
					this.radForCusp = theta0 + radToBreakpoint;
					radToBreakpoint -= (incrament/Math.pow(2, (callNum)));
					poiThetaValue = this.checkForCuspBeforeNextVertex(callNum + 1, new float[] { vals[0], vals[1], vals[2], this.calculateSuperShapePoint(radToBreakpoint), vals[3] }, incrament, theta0, radToBreakpoint);
				}
				else System.out.println("ERROR ERROR ERROR ERROR ERROR");
			}
		}

		return poiThetaValue;
	}

	private float calculateDerivative(float r1, float r2, float rad)
	{
		float derivative = (r2 - r1)/rad;

		return derivative;
	}

}
