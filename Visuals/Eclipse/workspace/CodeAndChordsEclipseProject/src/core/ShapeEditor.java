package core;

/**
 * This class is meant to be a plugin of sorts into a module.  To use a ShapeEditor object to
 * 		edit your shapes, create a shape editor object and then call runSE() in draw().
 * 		The ShapeEditor is then opened and closed by manipulating the isRunning varibale, 
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

	private Shape	shape;
	
	private	Module	module;
	
	
	private int 	SIZE_ID;
	private int		NUM_POINTS_ID;
	private int		N1_ID;
	private int		N2_ID;
	private int		N3_ID;
	private int		XPOS_ID;
	private int		YPOS_ID;
	private int		ROTATION_ID;

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
	public ShapeEditor(PApplet parent, Shape shape, Module module, float fullAppletWidth, float fullAppletHeight) {

		super(parent, fullAppletWidth, fullAppletHeight);
		
		this.module	= module;

		// make sure the PApplet isn't null and then set the parent instance variable
		if (parent == null)
			throw new IllegalArgumentException("PApplet parameter parent is null");
		else
			this.parent = parent;

		// make sure the shape object isn't null and then initialize
		if (shape == null)
			throw new IllegalArgumentException("Shape parameter is null");
		else
			this.shape = shape;

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
		
		if(super.getIsRunning()) 
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

	/**
	 * This function is called repeatedly when the ShapeEditor isRunning to draw all
	 * of the menu features that are drawn with processing tools
	 */
	private void drawSE() 
	{
		PShape ps = this.shape.getPShape();
		ps.beginShape();
		ps.stroke(255);
		ps.fill(255);
		ps.scale(super.getScale());
		ps.rotate(this.shape.getRotation());
		ps.endShape();
		this.parent.shape(ps, super.mapAdjustedMenuXPos(this.shape.getXPos()), this.mapAdjustedMenuYPos(this.shape.getYPos()));

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
			yVals[i] = spacing * (i) + 175;
		}
		
		//TODO: how to write text
		this.parent.fill(255);
		this.parent.stroke(255);
		this.parent.text("Shape Editor", 10, yVals[0]);
		
		this.SIZE_ID = this.nextSliderId;
		this.addSliderGroup((int) yVals[1], "size", .01f, 3, 1);
		
		this.NUM_POINTS_ID = this.nextSliderId;
		this.addSliderGroup((int) yVals[2], "numPoints", 0, 15, 1);
		
		this.N1_ID = this.nextSliderId;
		this.addSliderGroup((int) yVals[3], "n1", .01f, 10, 1);
		
		this.N2_ID = this.nextSliderId;
		this.addSliderGroup((int) yVals[4], "n2", .01f, 10, 1);
		
		this.N3_ID = this.nextSliderId;
		this.addSliderGroup((int) yVals[5], "n3", .01f, 10, 1);
		
		this.XPOS_ID = this.nextSliderId;
		this.addSliderGroup((int) yVals[6], "xPos", -500, 1500, 0);
		
		this.YPOS_ID = this.nextSliderId;
		this.addSliderGroup((int) yVals[7], "yPos", -500, 1000, 0);
		
		this.ROTATION_ID = this.nextSliderId;
		this.addSliderGroup((int) yVals[8], "rotation", -2*(float)Math.PI, 2 * (float)Math.PI, 0);
		

		this.controlP5.addScrollableList("shapeSelect")
		.setPosition(10, 10).setSize(240, 250)
		.setBarHeight(40).addItems(new String[] { "shape1", "shape2", "shape3", "shape4", "shape5" })
		.setValue(0)
		.setItemHeight(25)
		.close();

		this.controlP5.addButton("exitButton").setLabel("Close Shape Editor")
		.setPosition(this.parent.width - 160, 10)
		.setSize(150, 40);

	}

	@Override
	public void controlEvent(ControlEvent theEvent) {
		
		super.controlEvent(theEvent);
		
		switch (theEvent.getName()) {

		case "shapeSelect":
			this.shape.setShapeIndex((int) theEvent.getValue());
			this.updateSliders();
			System.out.println(theEvent.getValue());
			break;

		case "exitButton":
			super.setIsRunning(false);
			this.module.menu.setIsRunning(true);
			this.module.menu.showOutsideButtons();
			break;

		}

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
			this.shape.setCurrentShape("supershape", 
										new float[] { val, val, -1, -1, -1, -1, -1 });
		}
		
		if(id == this.NUM_POINTS_ID)
		{
			val = (float) Math.floor(val);
			this.shape.setCurrentShape("supershape", 
										new float[] { -1, -1, val, val, -1, -1, -1 });
		}
		
		if(id == this.N1_ID)
		{
			this.shape.setCurrentShape("supershape", 
					new float[] { -1, -1, -1, -1, val, -1, -1 });
		}
		
		if(id == this.N2_ID)
		{
			this.shape.setCurrentShape("supershape", 
					new float[] { -1, -1, -1, -1, -1, val, -1 });
		}
		
		if(id == this.N3_ID)
		{
			this.shape.setCurrentShape("supershape", 
					new float[] { -1, -1, -1, -1, -1, -1, val });
		}
		
		if(id == this.XPOS_ID)
		{
			this.shape.setXPos(val);
		}
		
		if(id == this.YPOS_ID)
		{
			this.shape.setYPos(val);
		}
		
		if(id == this.ROTATION_ID)
		{
			this.shape.setRotation(val);
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
		float[] param = this.shape.getCurrentParameters();
		
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
		
		if(this.shape.getXPos() != this.controlP5.getController("slider" + this.XPOS_ID).getValue())
		{
			this.controlP5.getController("slider" + this.XPOS_ID).setValue(this.shape.getXPos());
		}
		
		if(this.shape.getYPos() != this.controlP5.getController("slider" + this.YPOS_ID).getValue())
		{
			this.controlP5.getController("slider" + this.YPOS_ID).setValue(this.shape.getYPos());
		}
		
		if(this.shape.getRotation() != this.controlP5.getController("slider" + this.ROTATION_ID).getValue())
		{
			this.controlP5.getController("slider" + this.ROTATION_ID).setValue(this.shape.getRotation());
		}
		
	}

}// ShapeEditor
