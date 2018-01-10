package core;

/**
 * This class is meant to act as a shape object for shapes that are built off of polar coordinates.
 *		To use, call the setCurrentShape() function to add shapes and then call the getCurrentShape()
 *		function to have your PShape returned to you.  The language of current shape and next shape 
 *		are used so that the Shape object offers a solid building block for shape morphing, though it 
 *		has not yet been implemented as of 8/7/17.
 */


import processing.core.PApplet;
import processing.core.PShape;

public class Shape {

	private PApplet 	pApp;

	private int 		steps;
	private float 		increment;

	private int   		numShapes;
	private int 		shapeIndex;

	private float 		xPos;
	private float 		yPos;
	private float 		rotation;

	private float[][]	currentShapeParameters;	
	//May need to add a string at some point to identify which type of shape the parameters make

	private float[][] 	currentShape;
	private float[] 	nextShape;

	private float 		xStretch;
	private float 		yStretch;
	private float 		shapeScale = 1;

	private float[]		CIRCLE = new float[] {1, 1, 0, 0, 1, 1, 1};
	private float[]		SQUARE= new float[] {1, 1, 4, 4, 1, 1, 1};
	private float[]		STAR= new float[] {1, 1, 5, 5, .35f, 1, 1};
	private float[]		PENTAGON= new float[] {1, 1, 5, 5, 1.5f, 1, 1};
	private float[]		FLOWER= new float[] {.8f, .8f, 14, 14, 2.5f, 9, 1};
	private float[]		SPLAT= new float[] {2, 2, 14, 14, 2.85f, 3.6f, 2};
	private float[]		X= new float[] {1, 1, 4, 4, (float).3, 1, 1};
	private float[]		BUTTERFLY = new float[] {1, 1, 4, 4, (float).2, (float).69, 10};
	private float[]		SUN= new float[] {1, 1, 15, 15, (float)1.25, (float)3.1, 3};
	private float[]		SNOWFLAKE= new float[] {(float).95, (float).95, 14, 14, (float).28, 9, 1};

	
	
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
		this.increment = (float)(2*Math.PI)/this.steps;

		this.numShapes = 5;
		this.shapeIndex = 0;

		this.currentShapeParameters = new float[numShapes][];

		this.currentShape = new float[this.numShapes][this.steps];
		this.nextShape = new float[this.steps];

		this.xPos = this.pApp.width/2;
		this.yPos = this.pApp.height/2;

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

	public void setShapeIndex(int index)
	{
		if(index < 0) throw new IllegalArgumentException("index can not be less than zero");
		if(index >= this.numShapes) throw new IllegalArgumentException("index can not be greater than or equal to the number of shapes");

		this.shapeIndex = index;
	}

	//Getter Methods

	public int getShapeIndex ()
	{
		return this.shapeIndex;
	}

	//Implemented Methods
	public PShape getPShape()
	{
		PShape shape = this.pApp.createShape();
		shape.beginShape();

		float x;
		float y;

		int i = 0;

		for(float theta = 0; theta < 2*Math.PI; theta += this.increment)
		{
			x = (float) (this.currentShape[this.shapeIndex][i]*Math.cos(theta));
			y = (float) (this.currentShape[this.shapeIndex][i]*Math.sin(theta));

			x = PApplet.map(x, 0, 1, 0, this.xStretch);
			y = PApplet.map(y, 0, 1, 0, this.yStretch);

			x *= this.shapeScale;
			y *= this.shapeScale;

			shape.vertex(x, y);
			i++;
		}//for()

		shape.rotate(this.rotation);

		shape.endShape();

		return shape;


	}//drawShape()

	public PShape getScaledPShape(float[] scale)
	{
		PShape shape = this.pApp.createShape();
		shape.beginShape();

		float x;
		float y;

		int i = 0;

		for(float theta = 0; theta < 2*Math.PI; theta += this.increment)
		{
			x = (float) (this.currentShape[this.shapeIndex][i]*Math.cos(theta));
			y = (float) (this.currentShape[this.shapeIndex][i]*Math.sin(theta));

			x = PApplet.map(x, 0, 1, 0, this.xStretch);
			y = PApplet.map(y, 0, 1, 0, this.yStretch);

			x *= this.shapeScale;
			y *= this.shapeScale;

			x = PApplet.map(x, 0, scale[0], 0, scale[1]);
			y = PApplet.map(y, 0, scale[2], 0, scale[3]);


			shape.vertex(x, y);
			i++;
		}//for()

		shape.rotate(this.rotation);

		shape.endShape();

		return shape;

	}

	public void setCurrentShape(String shapeType, float[] parameters)
	{
		for(int i = 0; i < parameters.length; i++)
		{
			if(parameters[i] == -1) parameters[i] = this.currentShapeParameters[this.shapeIndex][i];
		}

		this.currentShapeParameters[this.shapeIndex] = parameters;

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

			for(float theta = 0; theta < 2*Math.PI; theta += this.increment)
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

			for(float theta = 0; theta < 2*Math.PI; theta += this.increment)
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

	public void setCurrentShape(String shape)
	{
		this.xStretch = 1;
		this.yStretch = 1;
		
		shape = shape.toLowerCase();
		
		switch(shape)
		{

		case "circle":
			this.setCurrentShape("supershape", this.CIRCLE);
			break;

		case "square":
			this.setCurrentShape("supershape", this.SQUARE);
			this.rotation = (float) (Math.PI / 8);
			break;

		case "pentagon":
			this.setCurrentShape("supershape", this.PENTAGON);
			break;

		case "star":
			this.setCurrentShape("supershape", this.STAR);
			break;

		case "flower":
			this.setCurrentShape("supershape", this.FLOWER);
			break;
			
		case "splat":
			this.setCurrentShape("supershape", this.SPLAT);
			break;
			
		case "x":
			this.setCurrentShape("supershape", this.X);
			this.rotation = (float) (Math.PI / 8);
			break;
			
		case "butterfly":
			this.setCurrentShape("supershape", this.BUTTERFLY);
			break;
			
		case "sun":
			this.setCurrentShape("supershape", this.SUN);
			break;
			
		case "snowflake":
			this.setCurrentShape("supershape", this.SNOWFLAKE);
			break;

		}
	}

	public void setShapeScale(float scale)
	{
		this.shapeScale = scale;
	}

	public void setXPos(float xPos)
	{
		this.xPos = xPos;
	}

	public void setYPos(float yPos)
	{
		this.yPos = yPos;
	}

	public float getXPos()
	{
		return this.xPos;
	}

	public float getYPos()
	{
		return this.yPos;
	}

	public float getRotation()
	{
		return this.rotation;
	}

	public float getXStretch()
	{
		return this.xStretch;
	}

	public float getYStretch()
	{
		return this.yStretch;
	}

	public float[] getCurrentParameters()
	{
		/*
		System.out.println("------------------------------------------------------------");
		for(int i = 0; i < 7; i++)
			System.out.println(this.currentShapeParameters[this.shapeIndex][i]);
		*/
		return this.currentShapeParameters[this.shapeIndex];
	}
	
	public void drawShape(ModuleMenu menu, int curHueNum)
	{
		if(curHueNum < 0 || curHueNum > menu.getCurHue().length)
		{
			throw new IllegalArgumentException("Shape.drawShape: curHueNum " + curHueNum + " is out of bounds; must be between 0 and " + menu.getCurHue().length);
		} // error checking
		
		int[] curHue = menu.getCurHue()[curHueNum];
		System.out.println("drawShape: curHueNum = " + curHueNum + 
				"; curHue = (" + curHue[0] + ", " + curHue[1] + ", " + curHue[2] + ")");
		
		this.pApp.fill(curHue[0], curHue[1], curHue[2]);
		//		this.fill(255);

//		float	shapeWidth	= (this.width - this.menu.getLeftEdgeX()) * (this.menu.getShapeSize() / 100);
//		float	shapeHeight	= this.height * (this.menu.getShapeSize() / 100);

		//this.shapeMode(CORNER);
		PShape pShape;
		/*		if(this.menu.getLeftEdgeX() == 0) pShape = this.shape.getPShape();
		else pShape = this.shape.getScaledPShape(new float[] {925, (925 - this.menu.getLeftEdgeX()), 1, 1});
		 */
		pShape = this.getPShape();

		pShape.beginShape();
		pShape.fill(curHue[0], curHue[1], curHue[2], menu.getAlphaVal());
		pShape.stroke(curHue[0], curHue[1], curHue[2], menu.getAlphaVal());
		pShape.rotate(this.getRotation());
		pShape.scale(menu.getCurrentScale());
		pShape.endShape();

		this.pApp.shape(pShape, menu.mapCurrentXPos(this.getXPos()), menu.mapCurrentYPos(this.getYPos()));
		/*		
		if(this.menu.getLeftEdgeX() == 0) this.shape(pShape, this.shapeEditor.getXPos(), this.shapeEditor.getYPos());
		else this.shape(pShape, PApplet.map(this.shapeEditor.getXPos(), 0, 925, this.menu.getLeftEdgeX(), 925), this.shapeEditor.getYPos());
		 */
	} // drawShape


}//Shapes
