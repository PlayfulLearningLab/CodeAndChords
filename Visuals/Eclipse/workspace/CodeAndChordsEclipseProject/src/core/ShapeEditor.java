package core;

import java.awt.Color;

import controlP5.CColor;
import controlP5.ControlEvent;
import controlP5.ControlListener;
import controlP5.ControlP5;
import controlP5.Slider;
import processing.core.PApplet;

public class ShapeEditor implements ControlListener{
	
	private PApplet 	parent;
	
	private ControlP5	cp5;
	
	private boolean 	shapeEditorVisible = true;
	
	//these refer to the size of the shape editor window, not the full module window
	private float		seWidth;
	private float 		seHeight;
	private float		seXPos = 0;
	private float		seYPos = 0;
	private float 		menuWidth;
	
	/*
	 * The scaledWindowBoolean is set true if the first constructor is used, ensuring that the
	 * 		shape editor window is scaled with the module window that it is running inside of.
	 * 		This means that the ShapeEditor will always accurately represent both the size and
	 * 		the shape with respect to the module.  If it is false, then the shape editor can 
	 * 		only represent one of these correctly.
	 * 
	 * When ScaledWindow is false, the ShapeEditor can either represent the position and relative
	 * 		shape correctly, with the trueShapeMode flag set as false, or it can represent the 
	 * 		true shape by setting trueShapeMode true, but keep in mind, when this flag is set true,
	 * 		the position and relative screen coverage will be inaccurate.
	 * 
	 */
	private boolean		scaledWindow;
	private boolean		trueShapeMode;
	
	
	
	
	/**
	 * 
	 * This constructor should be used to create a scaled version of the shaper editor 
	 * 		that is scaled based off of the window that the shape is displayed in
	 * 
	 * @param parent
	 * @param fullAppletWidth 		width of the window that this shape will be displayed in
	 * @param fullAppletHeight 		height of the window that this shape will be displayed in
	 */
	public ShapeEditor(PApplet parent, float fullAppletWidth, float fullAppletHeight)
	{
		//make sure the PApplet isn't null and then set the parent instance variable
		if(parent == null) throw new IllegalArgumentException("PApplet parameter parent is null");
		this.parent = parent;
		
		this.cp5 = new ControlP5(parent);
		
		//make sure the window values are within a reasonable range
		if(fullAppletWidth < 100 || fullAppletWidth > 2000 || fullAppletHeight < 50 || fullAppletHeight > 1000)
		{
				throw new IllegalArgumentException("Make sure your applet dimensions are correct");
		}
		
		/*Scale the screen size down to create a set of dimensions that the shape editor window
		 * 		will operate inside of.
		 * 
		 * By using this scaling method it will ensure that the shape being created will look the
		 * 		same in both the module and the shape editor, ensuring that it is easy to create 
		 * 		the shape you want to create without having to guess what it will look like after 
		 * 		it is put into the module
		 */
		float scaleFactor = .8f;
		float scaledWidth = fullAppletWidth * scaleFactor;
		float scaledHeight = fullAppletHeight * scaleFactor;
		
		float menuWidth = fullAppletWidth - scaledWidth;
		
		//ensures that the width of the menu is at least 200 pixels wide
		while(menuWidth < 200)
		{
			scaleFactor = scaleFactor - .01f;
			
			scaledWidth = fullAppletWidth * scaleFactor;
			scaledHeight = fullAppletHeight * scaleFactor;
			
			menuWidth = fullAppletWidth - scaledWidth;
		}
		
		if(menuWidth > 300) menuWidth = 300;
		
		this.seWidth = scaledWidth;
		this.seHeight = scaledHeight;
		this.menuWidth = menuWidth;
		
		this.seXPos = 0;
		this.seYPos = (fullAppletHeight - scaledHeight)/2;
		
		this.scaledWindow = true;
		this.trueShapeMode = false;
		
		this.addMenuControls();
		
	}//constructor
	
	
	/**
	 * Use this constructor to create a shape editor with custom dimensions
	 * 
	 * 
	 * @param parent
	 * @param shapeWindowWidth		width of the shape editor window
	 * @param shapeWindowHeight		height of the shape editor window
	 * @param menuWidth				width of the menu (this is added on to the shapeWindowWidth)
	 */
	public ShapeEditor(PApplet parent, float shapeWindowWidth, float shapeWindowHeight, float menuWidth)
	{
		//make sure the PApplet isn't null and then set the parent instance variable
				if(parent == null) throw new IllegalArgumentException("PApplet parameter parent is null");
				this.parent = parent;
				
				this.cp5 = new ControlP5(this.parent);
				
				//make sure the window values are within a reasonable range
				if(shapeWindowWidth < 50 || shapeWindowWidth > 2000 || shapeWindowHeight < 50 || shapeWindowHeight > 2000)
				{
						throw new IllegalArgumentException("Make sure your applet dimensions are correct");
				}
				
				this.seWidth = shapeWindowWidth;
				this.seHeight = shapeWindowHeight;
				this.menuWidth = menuWidth;
				
				this.scaledWindow = false;
				this.trueShapeMode = false;
				
				this.addMenuControls();
				
	}//constructor
	
	public void runSE(boolean shapeMenuRunning)
	{
		if(shapeMenuRunning)
		{
			this.drawSE();
			if(!this.cp5.isVisible())
			{
				this.cp5.show();
			}
		}
		else if(this.cp5.isVisible())
		{
			this.cp5.hide();
		}
	}
	
	private void drawSE()
	{
		this.parent.stroke(0);
		this.parent.fill(0); 
		this.parent.rect(this.seXPos, this.seYPos, (this.seWidth + this.menuWidth), this.seHeight);
		
		this.parent.stroke(50);
		this.parent.fill(50);
		this.parent.rect((this.seXPos + this.menuWidth), this.seYPos, -3, this.seHeight);


	}//drawSE
	
	private void addMenuControls()
	{	
		this.cp5.addSlider("a", .01f, 3, 1, 15, 120, 150, 28)
		.getCaptionLabel()
		.hide();

		this.cp5.addSlider("b", .01f, 3, 1, 15, 170, 150, 28)
		.getCaptionLabel()
		.hide();

		((Slider) this.cp5.addSlider("m1", 0, 15, 1, 15, 220, 150, 28))
		.getCaptionLabel()
		.hide();

		((Slider) this.cp5.addSlider("m2", 0, 15, 1, 15, 270, 150, 28))
		.getCaptionLabel()
		.hide();
		
		this.cp5.addScrollableList("shapeSelect")
		.setPosition(15,70)
		.setSize(150, 100)
		.setBarHeight(30)
		.addItems(new String[] {"shape1", "shape2", "shape3", "shape4", "shape5"})
		.close();
		
	}


	@Override
	public void controlEvent(ControlEvent theEvent) {
		
		
	}
	
	
	
	
	
}//ShapeEditor
