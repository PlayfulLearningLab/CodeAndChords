package core;

/**
 * This class is meant to be a plugin of sorts into a module.  To use a ShapeEditor object to
 * 		edit your shapes, create a shape editor object and then call runSE() in draw().
 * 		The ShapeEditor is then opened and closed by manipulating the isRunning variable, 
 * 		through the use of the setIsRunning() function.
 */

import java.awt.Color;

import controlP5.CColor;
import controlP5.ControlEvent;
import controlP5.ControlListener;
import controlP5.ControlP5;
import controlP5.Slider;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PShape;

public class ShapeEditor extends MenuTemplate implements ControlListener {

	//	private PApplet parent;

//	public	Shape	shape;
	private Shape[]	shapes;
	private int		shapeIndex;
	private int		numActiveShapes;

	private	Module	module;

	private boolean	slidersInitialized = false;

	private int 	SIZE_ID;
	private int		NUM_POINTS_ID;
	private int		N1_ID;
	private int		N2_ID;
	private int		N3_ID;
	private int		XPOS_ID;
	private int		YPOS_ID;
	private int		ROTATION_ID;
	private int 	YSTRETCH_ID;
	private int		XSTRETCH_ID;

	//	private ControlP5 cp5;

	/*
	 * The scaledWindowBoolean is set true if the first constructor is used,
	 * ensuring that the shape editor window is scaled with the module window that
	 * it is running inside of. This means that the ShapeEditor will always
	 * accurately represent both the size and the shape with respect to the module.
	 * If it is false, then the shape editor can only represent one of these
	 * correctly.
	 * 
	 * When ScaledWindow is false, the ShapeEditor can either represent the position
	 * and relative shape correctly, with the trueShapeMode flag set as false, or it
	 * can represent the true shape by setting trueShapeMode true, but keep in mind,
	 * when this flag is set true, the position and relative screen coverage will be
	 * inaccurate.
	 * 
	 */


	// TODO: I don't think we need a PApplet since we have a Module, which extends PApplet.
	// Anything that needs a PApplet can instead call the Module
	// (and that goes for fullAppletWidth/Height, too, since they can be accessed via the Module).
	//		- Emily
	
	/**
	 * 
	 * This constructor should be used to create a scaled version of the shaper
	 * editor that is scaled based off of the window that the shape is displayed in
	 * 
	 * THIS IS THE PREFERED CONSTRUCOR
	 * 
	 * @param parent
	 * @param fullAppletWidth
	 *            width of the window that this shape will be displayed in
	 * @param fullAppletHeight
	 *            height of the window that this shape will be displayed in
	 */
	public ShapeEditor(PApplet parent, Shape shape, Module module, float fullAppletWidth, float fullAppletHeight) 
	{
		this(parent, new Shape[] {shape}, module, fullAppletWidth, fullAppletHeight, new ControlP5(parent));
	}
	

	/**
	 * Same constructor as above, but with a ControlP5.
	 * This allows the ShapeEditor be another tab in the ModuleMenu.
	 * 
	 * @param parent
	 * @param shape
	 * @param module
	 * @param fullAppletWidth
	 * @param fullAppletHeight
	 * @param controlP5
	 */
	public ShapeEditor(PApplet parent, Shape shape, Module module, float fullAppletWidth, float fullAppletHeight, ControlP5 controlP5) 
	{
		this(parent, new Shape[] {shape}, module, fullAppletWidth, fullAppletHeight, controlP5);
	}// constructor

	/**
	 * 
	 * This constructor should be used to create a scaled version of the shaper
	 * editor that is scaled based off of the window that the shape is displayed in
	 * 
	 * THIS IS THE PREFERED CONSTRUCOR
	 * 
	 * @param parent
	 * @param fullAppletWidth
	 *            width of the window that this shape will be displayed in
	 * @param fullAppletHeight
	 *            height of the window that this shape will be displayed in
	 */
	public ShapeEditor(PApplet parent, Shape[] shapes, Module module, float fullAppletWidth, float fullAppletHeight) {
		this(parent, shapes, module, fullAppletWidth, fullAppletHeight, new ControlP5(parent));
	}
	
	/**
	 * Almost the same as previous constructor, but with a ControlP5.
	 * This allows the ShapeEditor be another tab in the ModuleMenu.
	 * 
	 * @param parent
	 * @param shapes
	 * @param module
	 * @param fullAppletWidth
	 * @param fullAppletHeight
	 * @param controlP5
	 */
	public ShapeEditor(PApplet parent, Shape[] shapes, Module module, float fullAppletWidth, float fullAppletHeight, ControlP5 controlP5) {

		super(parent, fullAppletWidth, fullAppletHeight);

		this.module	= module;

		// make sure the PApplet isn't null and then set the parent instance variable
		if (parent == null)
			throw new IllegalArgumentException("PApplet parameter parent is null");
		else
			this.parent = parent;

		// make sure the shape object isn't null and then initialize
		if (shapes == null || shapes.length == 0)
		{
			throw new IllegalArgumentException("Shape parameter is null");
		} else {
			this.shapes = shapes;			
		}
		
		this.numActiveShapes = 1;
		this.shapeIndex = 0;
		
		this.isRunning	= false;
		this.controlP5	= controlP5;
		this.controlP5.addListener(this);
		// Have to update the nextId's so that these Controllers don't overwrite the existing Menu ones:
		this.nextButtonId	= this.module.menu.nextButtonId;
		this.nextSliderId	= this.module.menu.nextSliderId;
		this.nextSTextfieldId	= this.module.menu.nextSTextfieldId;
		this.nextColorWheelId	= this.module.menu.nextColorWheelId;
		this.nextCWTextfieldId	= this.module.menu.nextCWTextfieldId;
		this.nextToggleId	= this.module.menu.nextToggleId;

		// create a new ControlP5 object to use
		/*		this.cp5 = new ControlP5(parent);
		this.cp5.addListener(this);
		this.cp5.setVisible(false);
		 */

		// calls function to create all of the cp5 controls needed
		this.addMenuControls();

	}// constructor

	/**
	 * 
	 * @param xPos
	 *            x position of the shape in the main window
	 * @param yPos
	 *            y position of the shape in the main window
	 * @param rotation
	 *            rotation of the shape in the main window
	 * @return returns a float[] with the three parameters passed in, accounting for
	 *         any changes that were made to the position and rotation of the shape
	 */
	public void runMenu() 
	{
		super.runMenu();

//		System.out.println("is this what's happening? super.getIsRunning = " + super.getIsRunning() + "; this.isRunning = " + this.isRunning);
//		if(super.getIsRunning()) 
		if(this.isRunning)
		{
			this.drawSE();
		}

		/*		if (super.getIsRunning()) {
			this.drawSE();
			if (!this.cp5.isVisible()) {
				this.cp5.show();		

			}
		} else if (this.cp5.isVisible()) {
			// if cp5 is visible but the ShapeEditor is not running, hide cp5
			this.cp5.hide();
		}
		 */
	} // runMenu
	
	// TODO - moved here from Module:
	public void drawShape(int shapeIndex)
	{
		shapes[shapeIndex].drawShape(this.module.menu, shapeIndex);
	}
	
	public void drawShapes()
	{
		for(int i = 0; i < this.module.curNumInputs; i++)
		{
			shapes[i].drawShape(this.module.menu, i);
		}
	}

	/**
	 * This function is called repeatedly when the ShapeEditor isRunning to draw all
	 * of the menu features that are drawn with processing tools
	 */
	private void drawSE() 
	{
		PShape ps = this.shapes[this.shapeIndex].getPShape();
		ps.beginShape();
		ps.stroke(255);
		ps.fill(255);
		ps.scale(super.getScale());
		ps.rotate(this.shapes[this.shapeIndex].getRotation());
		ps.endShape();
		this.parent.shape(ps, super.mapAdjustedMenuXPos(this.shapes[this.shapeIndex].getXPos()), this.mapAdjustedMenuYPos(this.shapes[this.shapeIndex].getYPos()));

		super.drawMenu();
	}// drawSE

	/**
	 * This method is called in the constructor to create all of the controls in the
	 * side menu
	 */
	private void addMenuControls() {
		int numControllers = 16;
		float[] yVals = new float[(int) numControllers];

		float spacing = ((this.parent.height - 20) / numControllers) + 2;

		for (int i = 0; i < numControllers; i++) {
			yVals[i] = spacing * (i) + 150;
		}

		//TODO: how to write text
		this.parent.fill(255);
		this.parent.stroke(255);
		this.parent.text("Shape Editor", 10, yVals[0]);

		this.SIZE_ID = this.nextSliderId;
		this.addSliderGroup(0, (int) yVals[1], "Size", .01f, 3, 1, "shape");

		this.NUM_POINTS_ID = this.nextSliderId;
		this.addSliderGroup(0, (int) yVals[2], "Number of\nPoints", 0, 15, 1, "shape");

		this.N1_ID = this.nextSliderId;
		this.addSliderGroup(0, (int) yVals[3], "Shape\nFullness", .01f, 10, 1, "shape");

		this.N2_ID = this.nextSliderId;
		this.addSliderGroup(0, (int) yVals[4], "Vertex 1\nExageration", .01f, 10, 1, "shape");

		this.N3_ID = this.nextSliderId;
		this.addSliderGroup(0, (int) yVals[5], "Vertex 2\nExageration", .01f, 10, 1, "shape");

		this.XPOS_ID = this.nextSliderId;
		this.addSliderGroup(0, (int) yVals[6], "X-Axis\nLocation", -500, 1500, 0, "shape");

		this.YPOS_ID = this.nextSliderId;
		this.addSliderGroup(0, (int) yVals[7], "Y-Axis\nLocation", -500, 1000, 0, "shape");

		this.ROTATION_ID = this.nextSliderId;
		this.addSliderGroup(0, (int) yVals[8], "Rotation", -2*(float)Math.PI, 2 * (float)Math.PI, 0, "shape");

		this.XSTRETCH_ID = this.nextSliderId;
		this.addSliderGroup(0, (int) yVals[9], "Width", .01f, 5, 1, "shape");

		this.YSTRETCH_ID = this.nextSliderId;
		this.addSliderGroup(0, (int) yVals[10], "Height", .01f, 5, 1, "shape");


		this.controlP5.addScrollableList("shapeSelect")
		.setPosition(10, 10).setSize(240, 250)
		.setBarHeight(40).addItems(new String[] { "shape1", "shape2", "shape3", "shape4", "shape5" })
		.setValue(0)
		.setItemHeight(25)
		.close()
		.moveTo("shape")
		.setVisible(false);

		this.controlP5.addButton("Circle")
		.setSize(100, 40)
		.moveTo("shape")
		.setPosition(this.parent.width*(1 - this.getScale()) + 15, 10);

		this.controlP5.addButton("Square")
		.setSize(100, 40)
		.moveTo("shape")
		.setPosition(this.parent.width*(1 - this.getScale()) + 125, 10);

		this.controlP5.addButton("Star")
		.setSize(100, 40)
		.moveTo("shape")
		.setPosition(this.parent.width*(1 - this.getScale()) + 235, 10);

		this.controlP5.addButton("Pentagon")
		.setSize(100, 40)
		.moveTo("shape")
		.setPosition(this.parent.width*(1 - this.getScale()) + 345, 10);

		this.controlP5.addButton("Flower")
		.setSize(100, 40)
		.moveTo("shape")
		.setPosition(this.parent.width*(1 - this.getScale()) + 455, 10);
		
		this.controlP5.addButton("Splat")
		.setSize(100, 40)
		.moveTo("shape")
		.setPosition(this.parent.width*(1 - this.getScale()) + 15, 60);

		this.controlP5.addButton("X")
		.setSize(100, 40)
		.moveTo("shape")
		.setPosition(this.parent.width*(1 - this.getScale()) + 125, 60);

		this.controlP5.addButton("Snowflake")
		.setSize(100, 40)
		.moveTo("shape")
		.setPosition(this.parent.width*(1 - this.getScale()) + 235, 60);

		this.controlP5.addButton("Sun")
		.setSize(100, 40)
		.moveTo("shape")
		.setPosition(this.parent.width*(1 - this.getScale()) + 345, 60);

		this.controlP5.addButton("Butterfly")
		.setSize(100, 40)
		.moveTo("shape")
		.setPosition(this.parent.width*(1 - this.getScale()) + 455, 60);
		
		this.slidersInitialized = true;

		String[] numList = new String[this.shapes.length];
		for(int i = 0; i < numList.length; i++)
		{
			numList[i] = "" + (i + 1);
		}
		
		this.controlP5.addScrollableList("shapeIndex", 100, 100, 150, 50)
		.addItems(numList)
		.moveTo("shape");
	}

	@Override
	public void controlEvent(ControlEvent theEvent) {

		// Can't call getController() on a Tab (which controlEvent() will try to do):
		if(theEvent.isTab())
		{
			System.out.println("Hey! Tab! Yay! theEvent.getName() = " + theEvent.getName());
			this.isRunning	= !this.isRunning;
		} else {
			super.controlEvent(theEvent);
			
			switch (theEvent.getName()) {

			case "shapeSelect":
				this.shapes[this.shapeIndex].setShapeIndex((int) theEvent.getValue());
				this.updateSliders();
				System.out.println(theEvent.getValue());
				break;
				
			case "shapeIndex":
				this.shapeIndex = (int) theEvent.getValue();
				break;

			case "Square":
				this.shapes[this.shapeIndex].setCurrentShape("square");
				break;

			case "Circle":
				this.shapes[this.shapeIndex].setCurrentShape("circle");
				break;

			case "Pentagon":
				this.shapes[this.shapeIndex].setCurrentShape("pentagon");
				break;

			case "Star":
				this.shapes[this.shapeIndex].setCurrentShape("star");
				break;

			case "Flower":
				this.shapes[this.shapeIndex].setCurrentShape("flower");
				break;
				
			case "Splat":
				this.shapes[this.shapeIndex].setCurrentShape("splat");
				break;
				
			case "Snowflake":
				this.shapes[this.shapeIndex].setCurrentShape("snowflake");
				break;
				
			case "Sun":
				this.shapes[this.shapeIndex].setCurrentShape("sun");
				break;
				
			case "X":
				this.shapes[this.shapeIndex].setCurrentShape("x");
				break;
				
			case "Butterfly":
				this.shapes[this.shapeIndex].setCurrentShape("butterfly");
				break;

			} // switch
		} // else - not Tab
		this.updateSliders();

	}
	/*
	public ControlP5 getCP5() {
		return this.controlP5;
	}
	 */

	@Override
	public void sliderEvent(int id, float val) {
		// TODO Auto-generated method stub

		if(id == this.SIZE_ID)
		{
			this.shapes[this.shapeIndex].setCurrentShape("supershape", 
					new float[] { val, val, -1, -1, -1, -1, -1 });
		}

		if(id == this.NUM_POINTS_ID)
		{
			val = (float) Math.floor(val);
			this.shapes[this.shapeIndex].setCurrentShape("supershape", 
					new float[] { -1, -1, val, val, -1, -1, -1 });
		}

		if(id == this.N1_ID)
		{
			this.shapes[this.shapeIndex].setCurrentShape("supershape", 
					new float[] { -1, -1, -1, -1, val, -1, -1 });
		}

		if(id == this.N2_ID)
		{
			this.shapes[this.shapeIndex].setCurrentShape("supershape", 
					new float[] { -1, -1, -1, -1, -1, val, -1 });
		}

		if(id == this.N3_ID)
		{
			this.shapes[this.shapeIndex].setCurrentShape("supershape", 
					new float[] { -1, -1, -1, -1, -1, -1, val });
		}

		if(id == this.XPOS_ID)
		{
			this.shapes[this.shapeIndex].setXPos(val);
		}

		if(id == this.YPOS_ID)
		{
			this.shapes[this.shapeIndex].setYPos(val);
		}

		if(id == this.ROTATION_ID)
		{
			this.shapes[this.shapeIndex].setRotation(val);
		}

		if(id == this.XSTRETCH_ID)
		{
			this.shapes[this.shapeIndex].setXStretch(val);
		}

		if(id == this.YSTRETCH_ID)
		{
			this.shapes[this.shapeIndex].setYStretch(val);
		}

		this.updateSliders();

	}

	@Override
	public void buttonEvent(int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void colorWheelEvent(int id, Color color) {
		// TODO Auto-generated method stub

	}

	public void updateSliders()
	{
		if(this.slidersInitialized)
		{

			float[] param = this.shapes[this.shapeIndex].getCurrentParameters();

			if(param[0] != this.controlP5.getController("slider" + this.SIZE_ID).getValue())
			{
				this.controlP5.getController("slider" + this.SIZE_ID).setValue(param[0]);
			}

			if(param[2] != this.controlP5.getController("slider" + this.NUM_POINTS_ID).getValue())
			{
				this.controlP5.getController("slider" + this.NUM_POINTS_ID).setValue(param[2]);
			}

			if(param[4] != this.controlP5.getController("slider" + this.N1_ID).getValue())
			{
				this.controlP5.getController("slider" + this.N1_ID).setValue(param[4]);
			}

			if(param[5] != this.controlP5.getController("slider" + this.N2_ID).getValue())
			{
				this.controlP5.getController("slider" + this.N2_ID).setValue(param[5]);
			}

			if(param[6] != this.controlP5.getController("slider" + this.N3_ID).getValue())
			{
				this.controlP5.getController("slider" + this.N3_ID).setValue(param[6]);
			}

			if(this.shapes[this.shapeIndex].getXPos() != this.controlP5.getController("slider" + this.XPOS_ID).getValue())
			{
				this.controlP5.getController("slider" + this.XPOS_ID).setValue(this.shapes[this.shapeIndex].getXPos());
			}

			if(this.shapes[this.shapeIndex].getYPos() != this.controlP5.getController("slider" + this.YPOS_ID).getValue())
			{
				this.controlP5.getController("slider" + this.YPOS_ID).setValue(this.shapes[this.shapeIndex].getYPos());
			}

			if(this.shapes[this.shapeIndex].getRotation() != this.controlP5.getController("slider" + this.ROTATION_ID).getValue())
			{
				this.controlP5.getController("slider" + this.ROTATION_ID).setValue(this.shapes[this.shapeIndex].getRotation());
			}

			if(this.shapes[this.shapeIndex].getXStretch() != this.controlP5.getController("slider" + this.XSTRETCH_ID).getValue())
			{
				this.controlP5.getController("slider" + this.XSTRETCH_ID).setValue(this.shapes[this.shapeIndex].getXStretch());
			}

			if(this.shapes[this.shapeIndex].getYStretch() != this.controlP5.getController("slider" + this.YSTRETCH_ID).getValue())
			{
				this.controlP5.getController("slider" + this.YSTRETCH_ID).setValue(this.shapes[this.shapeIndex].getYStretch());
			}

		}
	}
	
	public void setNumActiveShapes(int numActiveShapes)
	{
		this.numActiveShapes = numActiveShapes;
	}
	
	public Shape getShape()
	{
		return this.shapes[0];
	} // getShape
	
	public Shape[] getShapes()
	{
		return this.shapes;
	} // getShapes

}// ShapeEditor
