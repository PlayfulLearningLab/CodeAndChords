package core;

import processing.core.PApplet;
import processing.core.PShape;

public class Shape {

	private PApplet 	pApp;

	private int 		steps;
	private float 		incrament;
	
	private int   		numShapes;
	private int 		shapeIndex;

	private float[][] 	currentShape;
	private float[] 	nextShape;

	private float 		xStretch;
	private float 		yStretch;
	private float 		rotation;

	//constructors	
	public Shape(PApplet p)
	{
		if(p == null) throw new IllegalArgumentException("PApplet parameter cannot be null");
		
		this.pApp = p;
		this.steps = 500;
		this.initializeVariables();
	}

	public Shape(PApplet p, int steps)
	{
		if(p == null) throw new IllegalArgumentException("PApplet parameter cannot be null");
		if(steps < 10) throw new IllegalArgumentException("steps must be greater than or equal to ten");
		if(steps > 1000) throw new IllegalArgumentException("steps must be less than 1000");
		
		this.pApp = p;
		this.steps = steps;
		this.initializeVariables();
	}

	private void initializeVariables()
	{
		this.incrament = (float)(2*Math.PI)/this.steps;
		
		this.numShapes = 5;
		this.shapeIndex = 0;
		
		this.currentShape = new float[this.numShapes][this.steps];
		this.nextShape = new float[this.steps];

		this.xStretch = 1;
		this.yStretch = 1;
		this.rotation = 0;

		for(int i = 0; i < this.steps; i++)
		{
			for(int i2 = 0; i2 < this.numShapes; i2++)
			{
				this.currentShape[i2][i] = 0;
			}
			this.nextShape[i] = 100;
		}
	}


	//Setter Methods
	public void setPApplet(PApplet pa)
	{
		if(pa == null) throw new IllegalArgumentException("PApplet parameter cannot be null");
		this.pApp = pa;
	}
	
	public void setXStretch (float xStretch)
	{
		if(xStretch <= 0) throw new IllegalArgumentException("xStretch must be greater than 0");
		this.xStretch = xStretch;
	}

	public void setYStretch (float yStretch)
	{
		if(yStretch <= 0) throw new IllegalArgumentException("yStretch must be greater than 0");
		this.yStretch = yStretch;
	}

	public void setRotation (float rotationInRadians)
	{
		this.rotation = rotationInRadians;
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
			x = (float) (this.currentShape[this.shapeIndex][i]*Math.cos(theta));
			y = (float) (this.currentShape[this.shapeIndex][i]*Math.sin(theta));

			x = PApplet.map(x, 0, 1, 0, this.xStretch);
			y = PApplet.map(y, 0, 1, 0, this.yStretch);
			
			shape.vertex(x, y);
			i++;
		}//for()

		shape.rotate(this.rotation);
		
		shape.endShape();

		return shape;


	}//drawShape()

	public void setCurrentShape(String shapeType, float[] parameters)
	{
		float[] shape = new float[(int) this.steps];

		int i = 0;
		float r = 0;

		switch(shapeType){

		case "supershape":

			if(parameters.length != 7) throw new IllegalArgumentException("A supershape takes 7 parameters and you passed in " + parameters.length);

			float a = parameters[0];
			float b = parameters[1];
			float m1 = parameters[2];
			float m2 = parameters[3];
			float n1 = parameters[4];
			float n2 = parameters[5];
			float n3 = parameters[6];
			
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
			
			default: throw new IllegalArgumentException("Parameter shapeType did not match any valid shape types");
			
		}//switch



		this.currentShape[this.shapeIndex] = shape;
	}
	
	public void setNextShape(String shapeType, float[] parameters)
	{
		float[] shape = new float[(int) this.steps];

		int i = 0;
		float r = 0;

		switch(shapeType){

		case "supershape":
			if(parameters.length != 7) throw new IllegalArgumentException("A supershape takes 7 parameters and you passed in " + parameters.length);

			float a = parameters[0];
			float b = parameters[1];
			float m1 = parameters[2];
			float m2 = parameters[3];
			float n1 = parameters[4];
			float n2 = parameters[5];
			float n3 = parameters[6];
			
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
			
			default: throw new IllegalArgumentException("Parameter shapeType did not match any valid shape types");
			
		}//switch



		this.nextShape = shape;
	}

	
	
}//Shapes
