package core;

import java.awt.Color;

import controlP5.CColor;
import controlP5.ControlEvent;
import controlP5.ControlListener;
import controlP5.ControlP5;
import controlP5.Slider;
import processing.core.PApplet;
import processing.core.PShape;

public class ShapeEditor implements ControlListener{
	
	private PApplet 	parent;
	
	private boolean 	isRunning;
	
	private Shape 		shape;
	
	private ControlP5	cp5;
		
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
	public ShapeEditor(PApplet parent, Shape shape,float fullAppletWidth, float fullAppletHeight)
	{
		//make sure the PApplet isn't null and then set the parent instance variable
		if(parent == null) throw new IllegalArgumentException("PApplet parameter parent is null");
		else this.parent = parent;
		
		if(shape == null) throw new IllegalArgumentException("Shape parameter is null");
		else this.shape = shape;
				
		this.cp5 = new ControlP5(parent);
		this.cp5.addListener(this);
		
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
		
		this.cp5.addBackground("b1")
		.setPosition(0, 0)
		.setSize((int)fullAppletWidth, (int)((fullAppletHeight - this.seHeight)/2) + 1)
		.setBackgroundColor( (new Color((int)0,(int)0,(int)0, (int)200)).getRGB());
		
		this.cp5.addBackground("b2")
		.setPosition(0, (int)(fullAppletHeight) - ((fullAppletHeight - this.seHeight)/2))
		.setSize((int)fullAppletWidth, (int)((fullAppletHeight - this.seHeight)/2) + 2)
		.setBackgroundColor( (new Color((int)0,(int)0,(int)0, (int)200)).getRGB());
		
		if(this.seXPos != 0)
		{
			this.cp5.addBackground("b3")
			.setPosition(0, (int)(fullAppletHeight - this.seHeight)/2)
			.setSize((int)(fullAppletWidth - this.seWidth)/2, (int)this.seHeight)
			.setBackgroundColor( (new Color((int)0,(int)0,(int)0, (int)200)).getRGB());
			
			this.cp5.addBackground("b4")
			.setPosition((int) fullAppletWidth - ((fullAppletWidth - this.seWidth)/2), (int)(fullAppletHeight - this.seHeight)/2)
			.setSize((int)(fullAppletWidth - this.seWidth)/2, (int)this.seHeight)
			.setBackgroundColor( (new Color((int)0,(int)0,(int)0, (int)200)).getRGB());
		}
		
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
	public ShapeEditor(PApplet parent, Shape shape, float shapeWindowWidth, float shapeWindowHeight, float menuWidth)
	{
		//make sure the PApplet isn't null and then set the parent instance variable
				if(parent == null) throw new IllegalArgumentException("PApplet parameter parent is null");
				else this.parent = parent;
				
				if(shape == null) throw new IllegalArgumentException("Shape parameter is null");
				else this.shape = shape;
								
				this.cp5 = new ControlP5(this.parent);
				this.cp5.addListener(this);
				
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
	
	public void runSE()
	{		
		if(this.isRunning)
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
		System.out.println("drawSE() is running");
		
		this.parent.stroke(0);
		this.parent.fill(0); 
		this.parent.rect(this.seXPos, this.seYPos, (this.seWidth + this.menuWidth), this.seHeight);
		
		//this.parent.shapeMode(this.parent.CORNER);
		
	
		PShape ps = this.shape.getPShape();
		ps.beginShape();
		ps.stroke(255);
		ps.fill(255);
		ps.endShape();
		this.parent.shape(ps, (this.seXPos + this.menuWidth + (this.seWidth/2)), (this.seYPos + (this.seHeight/2)));

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
		
		//this.parent.shapeMode(this.parent.CENTER);

	}//drawSE
	
	private void addMenuControls()
	{	
		int  		numControllers = 6;  
		float[] 	yVals = new float[(int)numControllers];
		
		float spacing = (this.seHeight / numControllers) + 2;
		
		for(int i = 0; i < numControllers; i++)
		{
			yVals[i] = spacing*(1+i);
		}
		
		this.cp5.addScrollableList("shapeSelect")
		.setPosition((int)(this.seXPos + this.menuWidth/8),yVals[0])
		.setSize((int)(this.menuWidth /4 * 3), 100)
		.setBarHeight(30)
		.addItems(new String[] {"shape1", "shape2", "shape3", "shape4", "shape5"})
		.setValue(0)
		.close();
		
		this.cp5.addSlider("size", .01f, 3, 1, (int)(this.seXPos + (this.menuWidth/8)), (int)yVals[1], (int)(this.menuWidth /4 * 3), 28)
		.getCaptionLabel()
		.hide();

		this.cp5.addSlider("numPoints", .01f, 15, 1, (int)(this.seXPos + (this.menuWidth/8)), (int)yVals[2], (int)(this.menuWidth /4 * 3), 28)
		.getCaptionLabel()
		.hide();

		((Slider) this.cp5.addSlider("n1", .01f, 10, 1, (int)(this.seXPos + this.menuWidth/8), (int)yVals[3], (int)(this.menuWidth /4 * 3), 28))
		.getCaptionLabel()
		.hide();

		((Slider) this.cp5.addSlider("n2", .01f, 10, 1, (int)(this.seXPos + this.menuWidth/8), (int)yVals[4], (int)(this.menuWidth /4 * 3), 28))
		.getCaptionLabel()
		.hide();
		
		((Slider) this.cp5.addSlider("n3", .01f, 10, 1, (int)(this.seXPos + this.menuWidth/8), (int)yVals[5], (int)(this.menuWidth /4 * 3), 28))
		.getCaptionLabel()
		.hide();
		
		this.cp5.addLabel("sizeLabel")
		.setPosition(15, yVals[1] - (spacing/3))
		.setValue("Shape Size");
		
		this.cp5.addLabel("numPointsLabel")
		.setPosition(15, yVals[2] - (spacing/3))
		.setValue("Number of Points");
		
		this.cp5.addLabel("n1Label")
		.setPosition(15, yVals[3] - (spacing/3))
		.setValue("N1");
		
		this.cp5.addLabel("n2Label")
		.setPosition(15, yVals[4] - (spacing/3))
		.setValue("N2");
		
		this.cp5.addLabel("n3Label")
		.setPosition(15, yVals[5] - (spacing/3))
		.setValue("N3");
		
		this.cp5.getController("shapeSelect")
		.bringToFront();
		
		this.cp5.addButton("exitButton")
		.setLabel("Close Shape Editor")
		.setPosition( (this.menuWidth + this.seWidth + this.seXPos)/2 - 75, 5)
		.setSize(150, 40);
		
	}
	
	public void setIsRunning(boolean isRunningFlag)
	{
		this.isRunning = isRunningFlag;
		if(isRunningFlag) 
		{
			this.cp5.get("b1").bringToFront();
			this.cp5.get("exitButton").bringToFront();
		}
	}
	
	public boolean getIsRunning()
	{
		return this.isRunning;
	}

	@Override
	public void controlEvent(ControlEvent theEvent) {
		
		switch(theEvent.getName()){
		
		case "shapeSelect":
			this.shape.setShapeIndex((int)theEvent.getValue());
			System.out.println(theEvent.getValue());
			break;
		
		case "size":
			this.shape.setCurrentShape("supershape", new float[] { theEvent.getValue(), theEvent.getValue(), -1, -1, -1, -1, -1 });
			break;
			
		case "numPoints":
			float val = (float) Math.floor(theEvent.getValue());
			if(val == 0) theEvent.getController().setValue(.01f);
			else theEvent.getController().setValue(val);
			this.shape.setCurrentShape("supershape", new float[] { -1, -1, val, val, -1, -1, -1 });
			break;
			
		case "n1":
			this.shape.setCurrentShape("supershape", new float[] { -1,-1,-1, -1, theEvent.getValue(), -1, -1 });
			break;
			
		case "n2":
			this.shape.setCurrentShape("supershape", new float[] { -1,-1,-1, -1, -1, theEvent.getValue(), -1 });
			break;
			
		case "n3":
			this.shape.setCurrentShape("supershape", new float[] { -1,-1,-1, -1, -1, -1, theEvent.getValue() });
			break;
		
		case "exitButton":
			this.isRunning = false;
			break;
			
		}
		
	}
	
	
	
	
	
}//ShapeEditor
