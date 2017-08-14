package core;

import java.awt.Color;
import java.text.DecimalFormat;

import controlP5.Button;
import controlP5.ColorWheel;
import controlP5.ControlEvent;
import controlP5.ControlListener;
import controlP5.ControlP5;
import controlP5.Controller;
import controlP5.Slider;
import controlP5.Textfield;

/*
 * All values are stored and set as relative to their position in the full canvas, 
 * not the scaled down version.  When you need these values relative to the scaled 
 * down screen, use the getAdjustedMenuXPos() and getAdjustedMenuYPos() functions. 
 */



import processing.core.PApplet;
import processing.core.PShape;

/**
 * 
 * @author Dan Mahota, Emily Meuer
 *
 */
public abstract class MenuTemplate implements ControlListener {

	private PApplet 	parent;
	
	protected	ControlP5	controlP5;
	
	private boolean 	isRunning;
	
	//these variables are stored relative to the size of the full applet,
	//NOT RELATIVE TO THE SMALLER WINDOW USED WHEN THE MENU IS OPEN
	private float 		xPos;
	private float 		yPos;
	private float 		rotation;

	private float 		appletWidth;
	private float 		appletHeight;

	private float 		scale;
	
	private PShape		menuBackground;
	
	protected	int			leftAlign;
	protected	int			leftEdgeX;

	protected	int			labelX;
	protected	int			labelWidth;
	protected	int			spacer;
	protected	int			textfieldWidth;
	protected	int			sliderWidth;
	protected	int			sliderHeight;
	
	/**
	 * The following are id's that are used within the add____ methods to keep id numbering consistent.
	 * They are initially set to 0 (nextSliderId), 100 (nextSTextfieldId), 200 (nextButtonId), 
	 * 300 (nextColorWheelId), 400 (nextCWTextfieldId) and 500 (nextToggleId), and incremented as Controllers are added.
	 */
	protected	int	nextSliderId;
	protected	int	nextSTextfieldId;	// Textfield next to a slider
	protected	int	nextButtonId;	// for Buttons that open a ColorWheel
	protected	int	nextColorWheelId;		// ColorWheels
	protected	int	nextCWTextfieldId;	// Textfield under a ColorWheels
	protected	int	nextToggleId;
	
	/**	DecimalFormat used for rounding the text corresponding to Sliders and Colorwheels.	*/
	protected	DecimalFormat	decimalFormat	= new DecimalFormat("#.##");


	public MenuTemplate(PApplet pApp, float appWidth, float appHeight)
	{
		this.isRunning = false;
		
		this.parent = pApp;
		
		this.controlP5		= new ControlP5(this.parent);
		this.controlP5.addListener((ControlListener)this);
		
		this.appletWidth = appWidth;
		this.appletHeight = appHeight;
		
		this.xPos = appWidth / 2;
		this.yPos = appHeight / 2;
		this.rotation = 0;

		this.scale = .7f;
		
		this.menuBackground = this.parent.createShape();
		
		this.menuBackground.beginShape();
		
		this.menuBackground.vertex(0, 0);
		this.menuBackground.vertex(this.appletWidth, 0);
		this.menuBackground.vertex(this.appletWidth, this.mapAdjustedMenuYPos(0));
		this.menuBackground.vertex(this.mapAdjustedMenuXPos(0), this.mapAdjustedMenuYPos(0));
		this.menuBackground.vertex(this.mapAdjustedMenuXPos(0), this.appletHeight);
		this.menuBackground.vertex(0, this.appletHeight);
		this.menuBackground.vertex(0, 0);
		
		
		this.menuBackground.stroke(0);
		this.menuBackground.fill(0);
		
		this.menuBackground.endShape();
		

		this.nextSliderId		= 0;
		this.nextSTextfieldId	= 100;
		this.nextButtonId		= 200;
		this.nextColorWheelId	= 300;
		this.nextCWTextfieldId	= 400;
		this.nextToggleId		= 500;
	} // constructor
	
	public void drawMenu()
	{
		this.parent.shape(this.menuBackground, 0, 0);
		
		this.parent.stroke(150);
		this.parent.strokeWeight(3);
		this.parent.noFill();
		
		this.parent.rect(	this.mapAdjustedMenuXPos(0), 
							this.mapAdjustedMenuYPos(0), 
							this.appletWidth * this.scale - 3, 
							this.appletHeight * this.scale - 3 );
		//System.out.println(this.scale);
		//System.out.println(this.mapAdjustedMenuXPos(0) + "    "  +  this.mapAdjustedMenuXPos(this.appletWidth));
		
	} // drawMenu
	
	/**
	 * This method should be called in children's controlEvent if they used addSliderGroup()
	 * or addColorWheelGroup and want to use the Slider/Textfield and Button/ColorWheel/Textfield 
	 * functionality.
	 * 
	 * It will be called automatically from ControlP5 Controllers.
	 * 
	 * @param controlEvent	event from the current Controller
	 */
	public void controlEvent(ControlEvent controlEvent)
	{
//		System.out.println("MenuTemplate.controlEvent: controlEvent = " + controlEvent);
		
		int	id	= controlEvent.getController().getId();
		// Sliders
		if(id > -1 && id < 100)
		{
			// TODO: was catching a NullPointer here, but I think that's unnecessary; but if NPE errors, try doing that again.
				Slider	curSlider	= (Slider)this.controlP5.getController("slider" + id);

				float	sliderValFloat	= curSlider.getValue();

				if((Textfield)this.controlP5.getController("textfield" + (id + 100)) != null)
				{
					Textfield	curTextfield	= (Textfield)this.controlP5.getController("textfield" + (id + 100));

					String	sliderValString	= this.decimalFormat.format(curSlider.getValue());

					curTextfield.setText(sliderValString);			
				} // if connected to Textfield
				
				this.sliderEvent(id, sliderValFloat);
		} // Sliders
		
		// Textfields
		if(id > 99 && id < 200)
		{
			Textfield	curTextfield	= (Textfield)this.controlP5.getController("textfield" + id);
			Slider		curSlider		= (Slider)this.controlP5.getController("slider" + (id - 100));

			try	{
				curSlider.setValue(Float.parseFloat(curTextfield.getStringValue()));

			} catch(NumberFormatException nfe) {
				//System.out.println("ModuleTemplate.controlEvent: string value " + curTextfield.getStringValue() + 
				//"for controller " + curTextfield + " cannot be parsed to a float.  Please enter a number.");
			} // catch
		} // textField
		
		// Color Select Buttons (to show ColorWheel)
		if(id > 199 && id < 300)
		{
			Button	curButton	= (Button)controlEvent.getController();

			// draw slightly transparent rectangle:
			if(curButton.getBooleanValue())
			{

				this.controlP5.getGroup("background").setVisible(true);
				this.controlP5.getGroup("background").bringToFront();

			} else {

				//				this.fillHSBColors();
				// TODO: might need to fillOriginalColors here, too, at some point?				

				this.controlP5.setAutoDraw(true);
				this.controlP5.getGroup("background").setVisible(false);
				//				this.displaySidebar(false);
			}

			this.controlP5.getController("button" + (controlEvent.getId())).bringToFront();
			this.controlP5.getController("colorWheel" + (controlEvent.getId() + 100)).bringToFront();
			this.controlP5.getController("textfield" + (controlEvent.getId() + 200)).bringToFront();

			this.controlP5.getController("colorWheel" + (controlEvent.getId() + 100)).setVisible(curButton.getBooleanValue());
			this.controlP5.getController("textfield" + (controlEvent.getId() + 200)).setVisible(curButton.getBooleanValue());

			this.buttonEvent(id);
		} // CW Buttons
		
		// ColorWheels
		if(id > 299 && id < 400)
		{
			// get current color:
			ColorWheel	curCW	= (ColorWheel)controlEvent.getController();

			int	rgbColor	= curCW.getRGB();
			Color	color	= new Color(rgbColor);

			// Set corresponding Textfield with color value:
			Textfield	curColorTF	= (Textfield)this.controlP5.getController("textfield" + (controlEvent.getId() + 100));
			curColorTF.setText("rgb(" + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue() + ")");
			
			this.colorWheelEvent(id, color);
		} // ColorWheels

		// ColorWheel Textfields
		if(id > 399 && id < 500)
		{
			id	= controlEvent.getId();
			System.out.println("controlEvent: event for a ColorWheel Textfield, id " + id);

			// Getting color value from the Textfield:
			String[]	tfValues	= controlEvent.getStringValue().split("[(,)]");
			for(int i = 0; i < tfValues.length; i++)
			{
				tfValues[i]	= tfValues[i].trim().toLowerCase();
			} // for

			try
			{
				if(tfValues[0].equals("rgb"))
				{
					// Get color values:
					int	red		= Integer.parseInt(tfValues[1]);
					int	green	= Integer.parseInt(tfValues[2]);
					int	blue	= Integer.parseInt(tfValues[3]);

					// Constrain to 0-255:
					red		= Math.min(255, Math.max(0, red));
					green	= Math.min(255, Math.max(0, green));
					blue	= Math.min(255, Math.max(0, blue));

					// Set corresponding ColorWheel:
					//					Color	rgbColor	= new Color(this.tonicColor[0], this.tonicColor[1], this.tonicColor[2]);
					Color	rgbColor	= new Color(red, green, blue);
					int		rgbInt		= rgbColor.getRGB();
					((ColorWheel)this.controlP5.getController("colorWheel" + (id - 100))).setRGB(rgbInt);

				} // if - rgb

			} catch(Exception e) {
				System.out.println("Sorry, '" + controlEvent.getStringValue() + "' is not recognized as a valid color (note that colors must be defined by Integer values). Exception message: "
						+ e.getMessage());
			} // catch
		} // ColorWheel Textfields
	} // controlEvent
	
	/**
	 * Child classes will implement this method to use the values of Sliders added by 
	 * this.addSliderGroup() (corresponding Textfield has been set in controlEvent).
	 * 
	 * @param id	id of Slider
	 * @param val	value of Slider
	 */
	public abstract void sliderEvent(int id, float val);

	/**
	 * Child classes will implement this method to use the Buttons connected to ColorWheels and Textfields
	 * (corresponding CW and Textfield have already been shown or hidden in controlEvent).
	 * 
	 * @param id	id of the current Button
	 */
	public abstract void buttonEvent(int id);
	
	/**
	 * Child classes will implement this method to use the ColorWheels connected to Buttons and Textfields
	 * (corresponding Textfield has already been updated in controlEvent).
	 * @param id
	 * @param color
	 */
	public abstract void colorWheelEvent(int id, Color color);
	
	
	/**
	 * Adds a Label with given text, Slider of given lowest value, highest value, and starting value, with default 
	 * width and x value at this.leftAlign, and Textfield with default width to "sidebarGroup" at given y value.
	 * 
	 * @param yVal	y value for the whole group (Label will, of course, be 4 pixels higher in order to look centered)
	 * @param labelText	text for the Label
	 * @param lowRange	Slider lowest value
	 * @param highRange	Slider highest value
	 * @param startingVals	Slider default/starting value
	 */
	protected void addSliderGroup(int yVal, String labelText, float lowRange, float highRange, float startingVals)
	{
		this.addSliderGroup(yVal, labelText, this.leftAlign, this.sliderWidth, lowRange, highRange, startingVals, this.textfieldWidth, "sidebarGroup");
	} // addSliderGroup - use default width
	
	/**
	 * Adds a Label with given text, Slider with given x value, width, lowest value, 
	 * highest value, and starting value, and Textfield with given width to given group at the given y value.
	 * 
	 * @param yVal	y value for the whole group (Label will, of course, be 4 pixels higher in order to look centered)
	 * @param labelText	text for the Label
	 * @param sliderX	x value for the Slider
	 * @param sliderWidth	Slider width
	 * @param lowRange	Slider lowest value
	 * @param highRange	Slider highest value
	 * @param startingVals	Slider default/starting value
	 * @param textfieldWidth	Textfield width
	 * @param group	String indicating to which group these Sliders and Textfields should belong
	 */
	protected void addSliderGroup(int yVal, String labelText, int sliderX, int sliderWidth, float lowRange, float highRange, float startingVals, int textfieldWidth, String group)
	{
		this.controlP5.addLabel("label" + this.nextSliderId)
		.setPosition(labelX, yVal + 4)
		.setWidth(labelWidth)
		.setGroup(group)
		.setValue(labelText);

		this.controlP5.addSlider("slider" + this.nextSliderId)
		.setPosition(sliderX, yVal)
		.setSize(sliderWidth, this.sliderHeight)
		.setRange(lowRange, highRange)
		.setValue(startingVals)
		.setSliderMode(Slider.FLEXIBLE)
		.setLabelVisible(false)
		.setGroup(group)
		.setId(this.nextSliderId);

		this.nextSliderId	= this.nextSliderId + 1;

		this.controlP5.addTextfield("textfield" + this.nextSTextfieldId)
		.setPosition(sliderX + sliderWidth + spacer, yVal)
		.setSize(this.textfieldWidth, this.sliderHeight)
		.setText(this.controlP5.getController("slider" + (this.nextSTextfieldId - 100)).getValue() + "")
		.setAutoClear(false)
		.setGroup(group)
		.setId(this.nextSTextfieldId)
		.getCaptionLabel().setVisible(false);

		this.nextSTextfieldId	= this.nextSTextfieldId + 1;
	} // addSliderGroup - define width

	/**
	 * Adds a connected Button, ColorWheel, and Textfield to this.controlP5, in group "sidebarGroup",
	 * by making a color from the int[] and calling addColorWheelGroup(int, int, int, String, Color)
	 * 
	 * @param x	x value of Button and ColorWheel
	 * @param y	y value of Button
	 * @param buttonWidth	width of Button
	 * @param buttonLabel	text to put on the Button
	 * @param colo	int[] with the red, green, blue values for the desired Color
	 */
	protected Controller[] addColorWheelGroup(int x, int y, int buttonWidth, String buttonLabel, int[] rgbColor)
	{
		if(rgbColor == null) {
			throw new IllegalArgumentException("ModuleTemplate.addColorWheelGroup: int[] parameter is null.");
		}
		if(rgbColor.length != 3) {
			throw new IllegalArgumentException("ModuleTemplate.addColorWheelGroup: int[] parameter has length " + rgbColor.length + 
					"; must be length 3.");
		} // error checking

		return this.addColorWheelGroup(x, y, buttonWidth, buttonLabel, new Color(rgbColor[0], rgbColor[1], rgbColor[2]));
	} // addColorWheelGroup

	/**
	 * Adds a connected Button, ColorWheel, and Textfield to this.controlP5, in group "sidebarGroup"
	 * 
	 * @param x	x value of Button and ColorWheel
	 * @param y	y value of Button
	 * @param buttonWidth	width of Button
	 * @param buttonLabel	text to put on the Button
	 * @param color	Color to set the ColorWheel and Textfield ("rgb([red], [green], [blue])")
	 */
	protected Controller[] addColorWheelGroup(int x, int y, int buttonWidth, String buttonLabel, Color color)
	{
		Button		button;
		ColorWheel	colorWheel;
		Textfield	textfield;

		if(buttonLabel == null) {
			throw new IllegalArgumentException("ModuleTemplate.addColorWheelGroup: String parameter is null.");
		}
		// Add Button:
		button	= this.controlP5.addButton("button" + this.nextButtonId)
				.setPosition(x, y)
				.setWidth(buttonWidth)
				.setLabel(buttonLabel)
				.setId(this.nextButtonId)
				.setGroup("sidebarGroup");
		button.getCaptionLabel().toUpperCase(false);

		this.nextButtonId = this.nextButtonId + 1;

		colorWheel	= this.controlP5.addColorWheel("colorWheel" + this.nextColorWheelId)
				.setPosition(x, y - 200)
				.setRGB(color.getRGB())
				.setLabelVisible(false)
				.setVisible(false)
				.setGroup("sidebarGroup")
				.setId(this.nextColorWheelId);

		this.nextColorWheelId = this.nextColorWheelId + 1;					

		textfield	= this.controlP5.addTextfield("textfield" + this.nextCWTextfieldId)
				.setPosition(x + buttonWidth + this.spacer, y)
				.setAutoClear(false)
				.setVisible(false)
				.setText("rgb(" + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue() + ")")
				.setGroup("sidebarGroup")
				.setId(this.nextCWTextfieldId);
		textfield.getCaptionLabel().setVisible(false);

		this.nextCWTextfieldId = this.nextCWTextfieldId + 1;

		return new Controller[] { button, colorWheel, textfield };
	} // addColorWheelGroup

	public void setIsRunning(boolean isRunning) 
	{
		this.isRunning = isRunning;
	}
	
	public void setXPos(float xPos)
	{
		this.xPos = xPos;
	}

	public void setYPos(float yPos)
	{
		this.yPos = yPos;
	}

	public void setRotation(float rotation)
	{
		this.rotation = rotation;
	}
	
	public boolean getIsRunning()
	{
		return this.isRunning;
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
	
	public float getAppletWidth()
	{
		return this.appletWidth;
	}
	
	public float getAppletHeight()
	{
		return this.appletHeight;
	}
	
	public float getScale()
	{
		return this.scale;
	}

	public float getAdjustedMenuXPos()
	{
		float adjustedX = PApplet.map(this.xPos, 0, 1, 0, this.scale);
		adjustedX += (this.appletWidth * (1 - this.scale));

		return adjustedX;
	}

	public float getAdjustedMenuYPos()
	{
		float adjustedY = PApplet.map(this.yPos, 0, 1, 0, this.scale);
		adjustedY += (this.appletHeight * (1 - this.scale));

		return adjustedY;
	}
	
	public float mapAdjustedMenuXPos(float fullAppletXPos)
	{
		float adjustedX = PApplet.map(fullAppletXPos, 0, 1, 0, this.scale);
		adjustedX += (this.appletWidth * (1 - this.scale));

		return adjustedX;
	}
	
	public float mapAdjustedMenuYPos(float fullAppletYPos)
	{
		float adjustedY = PApplet.map(fullAppletYPos, 0, 1, 0, this.scale);
		adjustedY += (this.appletHeight * (1 - this.scale));

		return adjustedY;
	}
	
	public float mapFullAppletXPos(float adjustedMenuXPos)
	{
		float fullX = adjustedMenuXPos - (this.appletWidth * (1 - this.scale));
		fullX = PApplet.map(fullX, 0, this.scale, 0, 1);

		return fullX;
	}
	
	public float mapFullAppletYPos(float adjustedMenuYPos)
	{
		float fullY = adjustedMenuYPos - (this.appletHeight * (1 - this.scale));
		fullY = PApplet.map(fullY, 0, this.scale, 0, 1);

		return fullY;
	}
	
	public abstract void runMenu();



}
