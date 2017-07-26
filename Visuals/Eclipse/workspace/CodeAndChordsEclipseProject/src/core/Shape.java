package core;

import processing.core.PApplet;
import processing.core.PShape;

public class Shape {

	private PApplet 	pApp;

	private int 		steps;
	private float 		incrament;

	private float[] 	currentShape;
	private float[] 	nextShape;

	private float 		xPos;
	private float 		yPos;
	private float 		xStretch;
	private float 		yStretch;
	private float 		rotation;

	private boolean 	menuIsOpen;
	private float 		menuWidth;
	private float		screenWidth;

	//constructors	
	public Shape(PApplet p, float menuWidth)
	{
		this.pApp = p;
		this.initializeVariables();
		this.menuWidth = menuWidth;
	}

	public Shape(PApplet p, float menuWidth, int steps)
	{
		this.pApp = p;
		this.initializeVariables();
		this.steps = steps;
		this.menuWidth = menuWidth;
	}

	private void initializeVariables()
	{
		this.steps = 500;
		this.incrament = (float)(2*Math.PI)/this.steps;
		
		this.currentShape = new float[this.steps];
		this.nextShape = new float[this.steps];
		
		this.xPos = 925/2;
		this.yPos = 520/2;
		this.xStretch = 1;
		this.yStretch = 1;
		this.rotation = 0;

		this.menuIsOpen = false;
		this.screenWidth = 925;

		for(int i = 0; i < this.steps; i++)
		{
			this.currentShape[i] = 0;
			this.nextShape[i] = 50;
		}
	}


	//Setter Methods
	public void setPApplet(PApplet pa)
	{
		this.pApp = pa;
	}

	public void setSteps(int numSteps)
	{
		this.steps = numSteps;
		this.incrament = (float) (2*Math.PI)/this.steps;
		this.currentShape = new float[this.steps];
		this.nextShape = new float[this.steps];
	}

	public void setXPos (float xPos)
	{
		this.xPos  = xPos;
	}

	public void setYPos (float yPos)
	{
		this.yPos = yPos;
	}

	public void setXStretch (float xStretch)
	{
		this.xStretch = xStretch;
	}


	public void setYStretch (float yStretch)
	{
		this.yStretch = yStretch;
	}


	public void setRotation (float rotationInRadians)
	{
		this.rotation = rotationInRadians;
	}

	public void setMenuWidth(float menuWidth)
	{
		this.menuWidth = menuWidth;
	}

	public void setMenuIsOpen(boolean isOpen)
	{
		this.menuIsOpen = isOpen;
	}


	//Implemented Methods
	public PShape getPShape()
	{
		PShape shape = this.pApp.createShape();
		shape.beginShape();

		float x;
		float y;

		int i = 0;

		for(float theta = 0; theta < 2*Math.PI; theta += this.incrament)
		{
			x = (float) (this.currentShape[i]*Math.cos(theta));
			y = (float) (this.currentShape[i]*Math.sin(theta));

			//x = PApplet.map(x, 0, 1, 0, this.xStretch);
			//y = PApplet.map(y, 0, 1, 0, this.yStretch);

			System.out.println(x + "    -    " + y);
			
			shape.vertex(x, y);
			i++;
		}//for()

		shape.rotate(this.rotation);
		
		shape.endShape();

		return shape;


	}//drawShape()

	public void setCurrentShape(String shapeType, float[] paramaters)
	{
		float[] shape = new float[(int) this.steps];

		int i = 0;
		float r = 0;

		switch(shapeType){

		case "supershape":
			
			float a = paramaters[0];
			float b = paramaters[1];
			float m1 = paramaters[2];
			float m2 = paramaters[3];
			float n1 = paramaters[4];
			float n2 = paramaters[5];
			float n3 = paramaters[6];
			
			for(float theta = 0; theta < 2*Math.PI; theta += this.incrament)
			{
				float part1 = (float) ((1 / a) * Math.cos(theta * m1 / 4));
				part1 = Math.abs(part1);
				part1 = (float) Math.pow(part1, n2);

				float part2 = (float) ((1 / b) * Math.sin(theta * m2 / 4));
				part2 = Math.abs(part2);
				part2 = (float) Math.pow(part2, n3);

				float part3 = (float) Math.pow(part1 + part2, 1 / n1);

				if (part3 == 0) {
					r = 0;
				}
				else { r = (1 / part3); }

				shape[i] = r * 100;
				i++;
			}//for()
			break;
			
			default:
				System.out.println("setCurrentShape() Switch error: default");
				break;
			
		}//switch



		this.currentShape = shape;
	}
	
	public void setNextShape(String shapeType, float[] paramaters)
	{
		float[] shape = new float[(int) this.steps];

		int i = 0;
		float r = 0;

		switch(shapeType){

		case "supershape":
			
			float a = paramaters[0];
			float b = paramaters[1];
			float m1 = paramaters[2];
			float m2 = paramaters[3];
			float n1 = paramaters[4];
			float n2 = paramaters[5];
			float n3 = paramaters[6];
			
			for(float theta = 0; theta < 2*Math.PI; theta += this.incrament)
			{
				float part1 = (float) ((1 / a) * Math.cos(theta * m1 / 4));
				part1 = Math.abs(part1);
				part1 = (float) Math.pow(part1, n2);

				float part2 = (float) ((1 / b) * Math.sin(theta * m2 / 4));
				part2 = Math.abs(part2);
				part2 = (float) Math.pow(part2, n3);

				float part3 = (float) Math.pow(part1 + part2, 1 / n1);

				if (part3 == 0) {
					r = 0;
				}
				else { r = (1 / part3); }

				shape[i] = r * 100;
				i++;
			}//for()
			break;
			
			default:
				System.out.println("setNextShape() Switch error: default");
				break;
			
		}//switch



		this.nextShape = shape;
	}

}
