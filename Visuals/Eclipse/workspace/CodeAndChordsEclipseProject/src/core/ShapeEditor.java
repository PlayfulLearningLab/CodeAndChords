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

public class ShapeEditor implements ControlListener {

	private PApplet parent;

	private boolean isRunning;

	private Shape shape;

	private ControlP5 cp5;

	// these refer to the size of the shape editor window, not the full module
	// window
	private float seWidth;
	private float seHeight;
	private float seXPos = 0;
	private float seYPos = 0;
	private float menuWidth;
	private float scale = -1;

	private float shapeXPos;
	private float shapeYPos;
	private float shapeRotation;

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
	private boolean scaledWindow;
	private boolean trueShapeMode;

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

		// make sure the window values are within a reasonable range
		if (fullAppletWidth < 100 || fullAppletWidth > 2000 || fullAppletHeight < 50 || fullAppletHeight > 1000) {
			throw new IllegalArgumentException("Make sure your applet dimensions are correct");
		}

		/*
		 * Scale the screen size down to create a set of dimensions that the shape
		 * editor window will operate inside of.
		 * 
		 * By using this scaling method it will ensure that the shape being created will
		 * look the same in both the module and the shape editor, ensuring that it is
		 * easy to create the shape you want to create without having to guess what it
		 * will look like after it is put into the module
		 * 
		 * This code attempts to find the highest scale factor that can be used to fit
		 * both the scaled down screen and the side menu bar onto the screen
		 */
		float scaleFactor = .8f;
		float scaledWidth = fullAppletWidth * scaleFactor;
		float scaledHeight = fullAppletHeight * scaleFactor;

		float menuWidth = fullAppletWidth - scaledWidth;

		// ensures that the width of the menu is at least 200 pixels wide
		while (menuWidth < 200) {
			scaleFactor = scaleFactor - .01f;

			scaledWidth = fullAppletWidth * scaleFactor;
			scaledHeight = fullAppletHeight * scaleFactor;

			menuWidth = fullAppletWidth - scaledWidth;
		}

		this.scale = scaleFactor;

		// Initialize all of the instance variables using the scaleFactor that was found
		// above

		if (menuWidth > 300)
			menuWidth = 300;

		this.seWidth = scaledWidth;
		this.seHeight = scaledHeight;
		this.menuWidth = menuWidth;

		this.seXPos = (fullAppletWidth - (this.seWidth + this.menuWidth)) / 2;
		this.seYPos = (fullAppletHeight - scaledHeight) / 2;

		this.shapeXPos = (fullAppletWidth / 2) * this.scale;
		this.shapeYPos = (fullAppletHeight / 2) * this.scale;
		this.shapeRotation = 0;

		// add background objects around the shape editor pop out in order to blur the
		// images that
		// can be seen behind
		this.cp5.addBackground("b1").setPosition(0, 0)
				.setSize((int) fullAppletWidth, (int) ((fullAppletHeight - this.seHeight) / 2) + 1)
				.setBackgroundColor((new Color((int) 0, (int) 0, (int) 0, (int) 200)).getRGB());

		this.cp5.addBackground("b2").setPosition(0, (int) (fullAppletHeight) - ((fullAppletHeight - this.seHeight) / 2))
				.setSize((int) fullAppletWidth, (int) ((fullAppletHeight - this.seHeight) / 2) + 2)
				.setBackgroundColor((new Color((int) 0, (int) 0, (int) 0, (int) 200)).getRGB());

		if (this.seXPos != 0) {
			this.cp5.addBackground("b3").setPosition(0, (int) (fullAppletHeight - this.seHeight) / 2)
					.setSize((int) (fullAppletWidth - this.seWidth) / 2, (int) this.seHeight)
					.setBackgroundColor((new Color((int) 0, (int) 0, (int) 0, (int) 200)).getRGB());

			this.cp5.addBackground("b4")
					.setPosition((int) fullAppletWidth - ((fullAppletWidth - this.seWidth) / 2),
							(int) (fullAppletHeight - this.seHeight) / 2)
					.setSize((int) (fullAppletWidth - this.seWidth) / 2, (int) this.seHeight)
					.setBackgroundColor((new Color((int) 0, (int) 0, (int) 0, (int) 200)).getRGB());
		}

		// shape mode options (unimplemented as of 8/7/17)
		this.scaledWindow = true;
		this.trueShapeMode = false;

		// calls function to create all of the cp5 controls needed
		this.addMenuControls();

	}// constructor

	/**
	 * Use this constructor to create a shape editor with custom dimensions
	 * 
	 * 
	 * @param parent
	 * @param shapeWindowWidth
	 *            width of the shape editor window
	 * @param shapeWindowHeight
	 *            height of the shape editor window
	 * @param menuWidth
	 *            width of the menu (this is added on to the shapeWindowWidth)
	 */
	public ShapeEditor(PApplet parent, Shape shape, float shapeWindowWidth, float shapeWindowHeight, float menuWidth) {
		// make sure the PApplet isn't null and then set the parent instance variable
		if (parent == null)
			throw new IllegalArgumentException("PApplet parameter parent is null");
		else
			this.parent = parent;

		if (shape == null)
			throw new IllegalArgumentException("Shape parameter is null");
		else
			this.shape = shape;

		this.cp5 = new ControlP5(this.parent);
		this.cp5.addListener(this);

		// make sure the window values are within a reasonable range
		if (shapeWindowWidth < 50 || shapeWindowWidth > 2000 || shapeWindowHeight < 50 || shapeWindowHeight > 2000) {
			throw new IllegalArgumentException("Make sure your applet dimensions are correct");
		}

		this.seWidth = shapeWindowWidth;
		this.seHeight = shapeWindowHeight;
		this.menuWidth = menuWidth;

		this.scaledWindow = false;
		this.trueShapeMode = false;

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
	public float[] runSE(float xPos, float yPos, float rotation) {
		
		float[] returnVal = null;
		
		if (this.isRunning) {

			// draws the menu
			this.drawSE();
			if (!this.cp5.isVisible()) {
				// if the menu is running but cp5 controls are not visible, it will show the cp5
				// controls
				// and then update the x and y positions based on the parameters passed in
				// because
				// it knows this is the first call that the parent has made to display the shape
				// window, so it should check if the parent has changed the position values
				// itself

				this.cp5.show();
				//this.shapeXPos = xPos;
				//this.shapeYPos = yPos;
				//this.shapeRotation = rotation;
			}
		} else if (this.cp5.isVisible()) {
			// if cp5 is visible but the ShapeEditor is not running, hide cp5
			this.cp5.hide();
			returnVal = new float[] { PApplet.map(this.shapeXPos, 0, this.scale, 0, 1),
					PApplet.map(this.shapeYPos, 0, this.scale, 0, 1), this.shapeRotation };
		}

		// return the position and rotation variables so that the parent can update
		// itself
		return returnVal;
	}

	/**
	 * This function is called repeatedly when the ShapeEditor isRunning to draw all
	 * of the menu features that are drawn with processing tools
	 */
	private void drawSE() {
		this.parent.stroke(0);
		this.parent.fill(0);
		this.parent.rect(this.seXPos, this.seYPos, (this.seWidth + this.menuWidth), this.seHeight);

		// this.parent.shapeMode(this.parent.CORNER);

		PShape ps = this.shape.getPShape();
		ps.beginShape();
		ps.stroke(255);
		ps.fill(255);
		if (this.scale != -1)
			ps.scale(this.scale);
		ps.rotate(this.shapeRotation);
		ps.endShape();
		this.parent.shape(ps, this.shapeXPos + this.menuWidth + this.seXPos, this.shapeYPos + this.seYPos);

		this.parent.rect(this.seXPos, this.seYPos, this.menuWidth, this.seHeight);

		this.parent.stroke(Color.CYAN.getRGB());
		this.parent.fill(Color.CYAN.getRGB());
		this.parent.rect((this.seXPos + this.menuWidth), this.seYPos, -3, this.seHeight);

		this.parent.stroke(Color.CYAN.getRGB());
		this.parent.fill(Color.CYAN.getRGB());
		this.parent.rect((this.seXPos), this.seYPos, this.seWidth + this.menuWidth, 3);

		this.parent.stroke(Color.CYAN.getRGB());
		this.parent.fill(Color.CYAN.getRGB());
		this.parent.rect((this.seXPos), this.seYPos + this.seHeight, this.seWidth + this.menuWidth, -3);

		this.parent.stroke(Color.CYAN.getRGB());
		this.parent.fill(Color.CYAN.getRGB());
		this.parent.rect((this.seXPos + this.menuWidth + this.seWidth), this.seYPos, -3, this.seHeight);

		this.parent.stroke(Color.CYAN.getRGB());
		this.parent.fill(Color.CYAN.getRGB());
		this.parent.rect((this.seXPos), this.seYPos, 3, this.seHeight);

		// this.parent.shapeMode(this.parent.CENTER);

	}// drawSE

	/**
	 * This method is called in the constructor to create all of the controls in the
	 * side menu
	 */
	private void addMenuControls() {
		int numControllers = 8;
		float[] yVals = new float[(int) numControllers];

		float spacing = ((this.seHeight - 30) / numControllers) + 2;

		for (int i = 0; i < numControllers; i++) {
			yVals[i] = this.seYPos + spacing * (i) + 25;
		}

		this.cp5.addSlider("size", .01f, 3, 1, (int) (this.seXPos + (this.menuWidth / 8)), (int) yVals[0],
				(int) (this.menuWidth / 4 * 3), 28).getCaptionLabel().hide();

		this.cp5.addSlider("numPoints", .01f, 15, 1, (int) (this.seXPos + (this.menuWidth / 8)), (int) yVals[1],
				(int) (this.menuWidth / 4 * 3), 28).getCaptionLabel().hide();

		((Slider) this.cp5.addSlider("n1", .01f, 10, 1, (int) (this.seXPos + this.menuWidth / 8), (int) yVals[2],
				(int) (this.menuWidth / 4 * 3), 28)).getCaptionLabel().hide();

		((Slider) this.cp5.addSlider("n2", .01f, 10, 1, (int) (this.seXPos + this.menuWidth / 8), (int) yVals[3],
				(int) (this.menuWidth / 4 * 3), 28)).getCaptionLabel().hide();

		((Slider) this.cp5.addSlider("n3", .01f, 10, 1, (int) (this.seXPos + this.menuWidth / 8), (int) yVals[4],
				(int) (this.menuWidth / 4 * 3), 28)).getCaptionLabel().hide();

		((Slider) this.cp5.addSlider("xPos", -500, 1500, this.shapeXPos, (int) (this.seXPos + this.menuWidth / 8),
				(int) yVals[5], (int) (this.menuWidth / 4 * 3), 28)).getCaptionLabel().hide();

		((Slider) this.cp5.addSlider("yPos", -500, 1000, this.shapeYPos, (int) (this.seXPos + this.menuWidth / 8),
				(int) yVals[6], (int) (this.menuWidth / 4 * 3), 28)).getCaptionLabel().hide();

		((Slider) this.cp5.addSlider("rotation", -PConstants.PI * 2, PConstants.PI * 2, 0,
				(int) (this.seXPos + this.menuWidth / 8), (int) yVals[7], (int) (this.menuWidth / 4 * 3), 28))
						.getCaptionLabel().hide();

		this.cp5.addLabel("sizeLabel")
				.setPosition((int) (this.seXPos + (this.menuWidth / 8)), (float) (yVals[0] - (spacing / 3.5)))
				.setValue("Shape Size");

		this.cp5.addLabel("numPointsLabel")
				.setPosition((int) (this.seXPos + (this.menuWidth / 8)), (float) (yVals[1] - (spacing / 3.5)))
				.setValue("Number of Points");

		this.cp5.addLabel("n1Label")
				.setPosition((int) (this.seXPos + (this.menuWidth / 8)), (float) (yVals[2] - (spacing / 3.5)))
				.setValue("N1");

		this.cp5.addLabel("n2Label")
				.setPosition((int) (this.seXPos + (this.menuWidth / 8)), (float) (yVals[3] - (spacing / 3.5)))
				.setValue("N2");

		this.cp5.addLabel("n3Label")
				.setPosition((int) (this.seXPos + (this.menuWidth / 8)), (float) (yVals[4] - (spacing / 3.5)))
				.setValue("N3");

		this.cp5.addLabel("xPosLabel")
				.setPosition((int) (this.seXPos + (this.menuWidth / 8)), (float) (yVals[5] - (spacing / 3.5)))
				.setValue("X Position");

		this.cp5.addLabel("yPosLabel")
				.setPosition((int) (this.seXPos + (this.menuWidth / 8)), (float) (yVals[6] - (spacing / 3.5)))
				.setValue("Y Position");

		this.cp5.addLabel("rotationLabel")
				.setPosition((int) (this.seXPos + (this.menuWidth / 8)), (float) (yVals[7] - (spacing / 3.5)))
				.setValue("Rotation");

		this.cp5.addScrollableList("shapeSelect")
				.setPosition((this.menuWidth + this.seWidth) / 2 + this.seXPos - 25 - 150, 5).setSize(150, 250)
				.setBarHeight(40).addItems(new String[] { "shape1", "shape2", "shape3", "shape4", "shape5" })
				.setValue(0).close();

		this.cp5.addButton("exitButton").setLabel("Close Shape Editor")
				.setPosition((this.menuWidth + this.seWidth) / 2 + this.seXPos + 25, 5).setSize(150, 40);

	}

	public void setIsRunning(boolean isRunningFlag) {
		this.isRunning = isRunningFlag;
		if (isRunningFlag) {
			this.cp5.get("b1").bringToFront();
			this.cp5.get("exitButton").bringToFront();
			this.cp5.get("shapeSelect").bringToFront();
		}
	}

	public boolean getIsRunning() {
		return this.isRunning;
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
			this.shapeXPos = theEvent.getValue();
			break;

		case "yPos":
			this.shapeYPos = theEvent.getValue();
			break;

		case "rotation":
			this.shapeRotation = theEvent.getValue();
			break;

		case "exitButton":
			this.isRunning = false;
			break;

		}

	}

	public float[] getSEWindowSizeAndPlace() {
		
		System.out.println("seXPos = " + this.seXPos);
		System.out.println("seYPos = " + this.seYPos);
		System.out.println("menuWidth = " + this.menuWidth);
		System.out.println("seWidth = " + this.seWidth);
		System.out.println("seHeight = " + this.seHeight);
		
		return new float[] { 	this.seXPos + this.menuWidth, 
								this.seYPos, 
								this.seXPos + this.menuWidth + this.seWidth,
								this.seYPos + this.seHeight };
	}

	public ControlP5 getCP5()
	{
		return this.cp5;
	}
	
	public float getScale()
	{
		return this.scale;
	}
	
	
	
}// ShapeEditor
