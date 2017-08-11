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

	private PApplet parent;

	private Shape shape;

	private ControlP5 cp5;

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
	public ShapeEditor(PApplet parent, Shape shape, float fullAppletWidth, float fullAppletHeight) {

		super(parent, fullAppletWidth, fullAppletHeight);

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
		this.cp5 = new ControlP5(parent);
		this.cp5.addListener(this);

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
	public void runMenu() {
		if (super.getIsRunning()) {
			this.drawSE();
			if (!this.cp5.isVisible()) {
				this.cp5.show();		
				
			}
		} else if (this.cp5.isVisible()) {
			// if cp5 is visible but the ShapeEditor is not running, hide cp5
			this.cp5.hide();
		}

	}

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
		ps.rotate(super.getRotation());
		ps.endShape();
		this.parent.shape(ps, super.getAdjustedMenuXPos(), this.getAdjustedMenuYPos());

		super.drawMenu();
	}// drawSE

	/**
	 * This method is called in the constructor to create all of the controls in the
	 * side menu
	 */
	private void addMenuControls() {
		int numControllers = 8;
		float[] yVals = new float[(int) numControllers];

		float spacing = ((super.getAppletHeight() - 100) / numControllers) + 2;

		for (int i = 0; i < numControllers; i++) {
			yVals[i] = spacing * (i) + 100;
		}

		this.cp5.addSlider("size", .01f, 3, 1, (int) 20, (int) yVals[0], 230, 28)
				.getCaptionLabel()
				.hide();

		this.cp5.addSlider("numPoints", .01f, 15, 1, (int) 20, (int) yVals[1],
				(int) 230, 28).getCaptionLabel().hide();

		((Slider) this.cp5.addSlider("n1", .01f, 10, 1, (int) 20, (int) yVals[2],
				(int) 230, 28)).getCaptionLabel().hide();

		((Slider) this.cp5.addSlider("n2", .01f, 10, 1, (int) 20, (int) yVals[3],
				(int) 230, 28)).getCaptionLabel().hide();

		((Slider) this.cp5.addSlider("n3", .01f, 10, 1, (int) 20, (int) yVals[4],
				(int) 230, 28)).getCaptionLabel().hide();

		((Slider) this.cp5.addSlider("xPos", -500, 1500, this.getXPos(), (int) 20,
				(int) yVals[5], (int) 230, 28)).getCaptionLabel().hide();

		((Slider) this.cp5.addSlider("yPos", -500, 1000, this.getYPos(), (int) 20,
				(int) yVals[6], (int) 230, 28)).getCaptionLabel().hide();

		((Slider) this.cp5.addSlider("rotation", -PConstants.PI * 2, PConstants.PI * 2, 0,
				(int) 20, (int) yVals[7], (int) 230, 28))
		.getCaptionLabel().hide();

		this.cp5.addLabel("sizeLabel")
		.setPosition((int) 20, (float) (yVals[0] - (spacing / 3.5)))
		.setValue("Shape Size");

		this.cp5.addLabel("numPointsLabel")
		.setPosition((int) 20, (float) (yVals[1] - (spacing / 3.5)))
		.setValue("Number of Points");

		this.cp5.addLabel("n1Label")
		.setPosition(20, (float) (yVals[2] - (spacing / 3.5)))
		.setValue("N1");

		this.cp5.addLabel("n2Label")
		.setPosition((int) 20, (float) (yVals[3] - (spacing / 3.5)))
		.setValue("N2");

		this.cp5.addLabel("n3Label")
		.setPosition((int) 20, (float) (yVals[4] - (spacing / 3.5)))
		.setValue("N3");

		this.cp5.addLabel("xPosLabel")
		.setPosition((int) 20, (float) (yVals[5] - (spacing / 3.5)))
		.setValue("X Position");

		this.cp5.addLabel("yPosLabel")
		.setPosition((int) 20, (float) (yVals[6] - (spacing / 3.5)))
		.setValue("Y Position");

		this.cp5.addLabel("rotationLabel")
		.setPosition((int) 20, (float) (yVals[7] - (spacing / 3.5)))
		.setValue("Rotation");

		this.cp5.addScrollableList("shapeSelect")
		.setPosition(200, 5).setSize(150, 250)
		.setBarHeight(40).addItems(new String[] { "shape1", "shape2", "shape3", "shape4", "shape5" })
		.setValue(0).close();

		this.cp5.addButton("exitButton").setLabel("Close Shape Editor")
		.setPosition(400, 5).setSize(150, 40);

	}

	@Override
	public void controlEvent(ControlEvent theEvent) {

		switch (theEvent.getName()) {

		case "shapeSelect":
			this.shape.setShapeIndex((int) theEvent.getValue());
			System.out.println(theEvent.getValue());
			break;

		case "size":
			this.shape.setCurrentShape("supershape",
					new float[] { theEvent.getValue(), theEvent.getValue(), -1, -1, -1, -1, -1 });
			break;

		case "numPoints":
			float val = (float) Math.floor(theEvent.getValue());
			if (val == 0)
				theEvent.getController().setValue(.01f);
			else
				theEvent.getController().setValue(val);
			this.shape.setCurrentShape("supershape", new float[] { -1, -1, val, val, -1, -1, -1 });
			break;

		case "n1":
			this.shape.setCurrentShape("supershape", new float[] { -1, -1, -1, -1, theEvent.getValue(), -1, -1 });
			break;

		case "n2":
			this.shape.setCurrentShape("supershape", new float[] { -1, -1, -1, -1, -1, theEvent.getValue(), -1 });
			break;

		case "n3":
			this.shape.setCurrentShape("supershape", new float[] { -1, -1, -1, -1, -1, -1, theEvent.getValue() });
			break;

		case "xPos":
			super.setXPos(theEvent.getValue());
			break;

		case "yPos":
			super.setYPos(theEvent.getValue());
			break;

		case "rotation":
			super.setRotation(theEvent.getValue());
			break;

		case "exitButton":
			super.setIsRunning(false);
			break;

		}

	}

	public ControlP5 getCP5() {
		return this.cp5;
	}

	@Override
	public void sliderEvent(int id, float val) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sTextfieldEvent(int id, float val) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buttonEvent(int id, String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void colorWheelEvent(int id, int[] color) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cwTextfieldEvent(int id, String val) {
		// TODO Auto-generated method stub
		
	}

}// ShapeEditor
