package coreV2;

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

import processing.core.PShape;

/**
 * Abstract class for a menu that maintains the correct aspect ratio for objects in the module
 * while scaling them down to make room for the menus above and on the left side.
 * 
 * All values are stored and set as relative to their position in the full canvas, 
 * not the scaled down version.  When you need these values relative to the scaled 
 * down screen, use the getAdjustedMenuXPos() and getAdjustedMenuYPos() functions.
 * 
 * Originally from core; moved to coreV2 on 5/25/2018.
 * 
 * @author Dan Mahota, Emily Meuer
 *
 */
public abstract class MenuTemplate implements ControlListener {

	private	String			menuTitle;
	
	/**	ControlP5 for this instance of MenuTemplate	*/
	protected	ControlP5	controlP5;

	/**	Indicates whether or not the Menu is open	*/
	protected boolean 	isRunning;

	/**	Blacks out the area behind the Menu	*/
	private PShape		menuBackground;

	/**	Width of the Menu on the left side of the Module	*/
	protected	int			sidebarWidth;

	/**	X value along which all the leftmost Sliders, Buttons, etc. are aligned	*/
	protected	int			leftAlign;

	/**	0 if Menu is closed, this.sidebarWidth if Menu is open	*/
	//	protected	int			leftEdgeX;

	/**	X value for the Labels along the left edge of the Menu	*/
	protected	int			labelX;

	/**	Width of Labels; used for calculating Button width when number of Buttons is specified by the user	*/
	protected	int			labelWidth;

	/**	Space between Controllers	*/
	protected	int			spacer;

	/**	Space along the right hand side of the Menu	*/
	protected	int			rightEdgeSpacer;

	/**	Width of Textfields next to Sliders	*/
	protected	int			textfieldWidth;

	/**	Width of Sliders	*/
	protected	int			sliderWidth;

	/**	Height of SliderS	*/
	protected	int			sliderHeight;

	//	protected	int[][][]	colors;

	//	protected	int		currentInput;


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

	private int				 	canvasXPos;
	private int 				canvasYPos;
	private int 				canvasWidth;
	private int 				canvasHeight;




	/**
	 * Constructor
	 * 
	 */
	public MenuTemplate(String menuTitle)
	{
		this.menuTitle = menuTitle;
		
		this.isRunning = false;

		this.canvasXPos = 0;
		this.canvasYPos = 260;
		this.canvasWidth = 462;
		this.canvasHeight = 260;

		this.controlP5	= new ControlP5(ModuleDriver.getModuleDriver());
		this.controlP5.addListener(this);

		this.controlP5.addGroup(menuTitle);

		
		// Creating the menuBackground:
		this.menuBackground = ModuleDriver.getModuleDriver().createShape();

		this.menuBackground.beginShape();
		
		this.menuBackground.vertex(0, 0);
		this.menuBackground.vertex(ModuleDriver.getModuleDriver().width, 0);
		this.menuBackground.vertex(ModuleDriver.getModuleDriver().width, this.canvasYPos);
		this.menuBackground.vertex(this.canvasXPos, this.canvasYPos);
		this.menuBackground.vertex(this.canvasXPos, ModuleDriver.getModuleDriver().height);
		this.menuBackground.vertex(0, ModuleDriver.getModuleDriver().height);
		this.menuBackground.vertex(0, 0);

		this.menuBackground.stroke(20);
		this.menuBackground.fill(20);

		this.menuBackground.endShape();
		
		// Use this.scale to determine the size of the sidebar:
		this.sidebarWidth	= (int)(ModuleDriver.getModuleDriver().width - this.canvasWidth);

		// ... and then use the size of the sidebar to determine the sizes of the Controllers:
		this.leftAlign			= (int)(this.sidebarWidth / 4);
		this.labelX				= 10;
		this.labelWidth			= (int)(this.sidebarWidth / 4.4);
		this.spacer				= (int)(this.sidebarWidth / 61.6);
		this.rightEdgeSpacer	= this.labelX;
		this.textfieldWidth		= (int)(this.sidebarWidth / 7.7);
		this.sliderWidth		= (int)(this.sidebarWidth / 1.8);
		this.sliderHeight		= ModuleDriver.getModuleDriver().height / 26;


		this.nextSliderId		= 1;
		this.nextSTextfieldId	= 101;
		this.nextButtonId		= 201;
		this.nextColorWheelId	= 301;
		this.nextCWTextfieldId	= 401;
		this.nextToggleId		= 501;

	} // constructor

	/**
	 * This method draws the Menu and should be called repeatedly whenever the Menu is open.
	 */
	public void drawMenu()
	{
		ModuleDriver.getModuleDriver().shape(this.menuBackground, 0, 0);

		ModuleDriver.getModuleDriver().noStroke();
		ModuleDriver.getModuleDriver().noFill();
	} // drawMenu

	public void setCanvasSize()
	{
		(ModuleDriver.getModuleDriver()).getCanvas().setDisplay(this.canvasXPos, this.canvasYPos, this.canvasWidth, this.canvasHeight);
	}
	
	public String getMenuTitle()
	{
		return this.menuTitle;
	}



	/**
	 * This method should be called in children's controlEvent if they used addSliderGroup()
	 * or addColorWheelGroup and want to use the Slider/Textfield and Button/ColorWheel/Textfield 
	 * functionality.
	 * 
	 * It will be called automatically from Controllers.
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
				System.out.println("ModuleTemplate.controlEvent: string value " + curTextfield.getStringValue() + 
						"for controller " + curTextfield + " cannot be parsed to a float.  Please enter a number.");
			} // catch
		} // textField

		// Color Select Buttons (to show ColorWheel)
		if(id > 199 && id < 300)
		{
			Button	curButton	= (Button)controlEvent.getController();

			// draw slightly transparent rectangle:
			if(curButton.getBooleanValue())
			{

				this.controlP5.getGroup("leftBackground").setVisible(true);
				this.controlP5.getGroup("leftBackground").bringToFront();

			} else {

				//				this.fillHSBColors();
				// TODO: might need to fillOriginalColors here, too, at some point?				

				this.controlP5.setAutoDraw(true);
				this.controlP5.getGroup("leftBackground").setVisible(false);
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
	 * width and x value at this.leftAlign, and Textfield with default width at given y value.
	 * 
	 * @param yVal	y value for the whole group (Label will, of course, be 4 pixels higher in order to look centered)
	 * @param labelText	text for the Label
	 * @param lowRange	Slider lowest value
	 * @param highRange	Slider highest value
	 * @param startingVals	Slider default/starting value
	 */
	protected void addSliderGroup(int yVal, String labelText, float lowRange, float highRange, float startingVals)
	{
		this.addSliderGroup(0, yVal, labelText, lowRange, highRange, startingVals);
	} // addSliderGroup - use default width


	/**
	 * Adds a Label with given text, Slider of given x value, lowest value, highest value, and starting value, with default 
	 * width, and Textfield with default width at given y value; tab == "default".
	 * 
	 * @param xVal	x value for the leftmost edge of the group (i.e., label will be at xVal + this.labelX)
	 * @param yVal	y value for the whole group (Label will, of course, be 4 pixels higher in order to look centered)
	 * @param labelText	text for the Label
	 * @param lowRange	Slider lowest value
	 * @param highRange	Slider highest value
	 * @param startingVals	Slider default/starting value
	 */
	protected void addSliderGroup(int xVal, int yVal, String labelText, float lowRange, float highRange, float startingVals)
	{
		this.addSliderGroup(xVal, yVal, labelText, (xVal + this.leftAlign), this.sliderWidth, lowRange, highRange, startingVals, this.textfieldWidth, "default");
	} // addSliderGroup - use default width

	/**
	 * Adds a Label with given text, Slider of given x value, lowest value, highest value, starting value, and tab, with default 
	 * width, and Textfield with default width at given y value.
	 * 
	 * @param xVal	x value for the leftmost edge of the group (i.e., label will be at xVal + this.labelX)
	 * @param yVal	y value for the whole group (Label will, of course, be 4 pixels higher in order to look centered)
	 * @param labelText	text for the Label
	 * @param lowRange	Slider lowest value
	 * @param highRange	Slider highest value
	 * @param startingVals	Slider default/starting value
	 */
	protected void addSliderGroup(int xVal, int yVal, String labelText, float lowRange, float highRange, float startingVals, String tab)
	{
		this.addSliderGroup(xVal, yVal, labelText, (xVal + this.leftAlign), this.sliderWidth, lowRange, highRange, startingVals, this.textfieldWidth, tab);
	} // addSliderGroup - specify tab but use default width


	/**	 
	 * Adds a Label with given text at xVal + this.labelX, Slider with given width, lowest value, 
	 * highest value, and starting value, and Textfield with given width to the given group at the given y value.
	 * 
	 * @param xVal	x value for the leftmost edge of the group (i.e., label will be at xVal + this.labelX)
	 * @param yVal	y value for the whole group (Label will, of course, be 4 pixels higher in order to look centered)
	 * @param labelText	text for the Label
	 * @param sliderX	x for Slider
	 * @param sliderWidth	Slider width
	 * @param lowRange	Slider lowest value
	 * @param highRange	Slider highest value
	 * @param startingVals	Slider default/starting value
	 * @param textfieldWidth	Textfield width
	 * @param group	String indicating to which group these Sliders and Textfields should belong
	 */
	protected void addSliderGroup(int xVal, int yVal, String labelText, int sliderX, int sliderWidth, float lowRange, float highRange, float startingVals, int textfieldWidth, String tab)
	{
		this.controlP5.addLabel("label" + this.nextSliderId)
		.setPosition(xVal + this.labelX, yVal + 4)
		.setWidth(labelWidth)
		.moveTo(tab)
		.setValue(labelText);

		this.controlP5.addSlider("slider" + this.nextSliderId)
		.setPosition(sliderX, yVal)
		.setSize(sliderWidth, this.sliderHeight)
		.setRange(lowRange, highRange)
		.setValue(startingVals)
		.setSliderMode(Slider.FLEXIBLE)
		.setLabelVisible(false)
		.moveTo(tab)
		.setId(this.nextSliderId);

		this.nextSliderId	= this.nextSliderId + 1;

		this.controlP5.addTextfield("textfield" + this.nextSTextfieldId)
		.setPosition(sliderX + sliderWidth + spacer, yVal)
		.setSize(this.textfieldWidth, this.sliderHeight)
		.setText(this.controlP5.getController("slider" + (this.nextSTextfieldId - 100)).getValue() + "")
		.setAutoClear(false)
		.moveTo(tab)
		.setId(this.nextSTextfieldId)
		.getCaptionLabel().setVisible(false);

		this.nextSTextfieldId	= this.nextSTextfieldId + 1;
	} // addSliderGroup - define width

	/**
	 * Adds a connected Button, ColorWheel, and Textfield to this.controlP5 in the "default" tab
	 * by making a color from the int[] and calling addColorWheelGroup(int, int, int, String, Color)
	 * 
	 * @param x	x value of Button and ColorWheel
	 * @param y	y value of Button
	 * @param buttonWidth	width of Button
	 * @param buttonLabel	text to put on the Button
	 * @param rgbColor	int[] with the red, green, blue values for the desired Color
	 * @param tab	String indicating the tab to which this ColorWheel group will belong
	 */
	protected Controller[] addColorWheelGroup(int x, int y, int buttonWidth, String buttonLabel, int[] rgbColor, String tab)
	{
		if(rgbColor == null) {
			throw new IllegalArgumentException("ModuleTemplate.addColorWheelGroup: int[] parameter is null.");
		}
		if(rgbColor.length != 3) {
			throw new IllegalArgumentException("ModuleTemplate.addColorWheelGroup: int[] parameter has length " + rgbColor.length + 
					"; must be length 3.");
		} // error checking

		return this.addColorWheelGroup(x, y, buttonWidth, buttonLabel, new Color(rgbColor[0], rgbColor[1], rgbColor[2]), tab);
	} // addColorWheelGroup

	/**
	 * Adds a connected Button, ColorWheel, and Textfield to this.controlP5.
	 * 
	 * @param x	x value of Button and ColorWheel
	 * @param y	y value of Button
	 * @param buttonWidth	width of Button
	 * @param buttonLabel	text to put on the Button
	 * @param color	Color to set the ColorWheel and Textfield ("rgb([red], [green], [blue])")
	 * @param tab	String indicating the tab to which this ColorWheel group will belong
	 */
	protected Controller[] addColorWheelGroup(int x, int y, int buttonWidth, String buttonLabel, Color color, String tab)
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
				.moveTo(tab);
		button.getCaptionLabel().toUpperCase(false);

		this.nextButtonId = this.nextButtonId + 1;

		colorWheel	= this.controlP5.addColorWheel("colorWheel" + this.nextColorWheelId)
				.setPosition(x, y - 200)
				.setRGB(color.getRGB())
				.setLabelVisible(false)
				.setVisible(false)
				.setId(this.nextColorWheelId)
				.moveTo(tab);

		this.nextColorWheelId = this.nextColorWheelId + 1;					

		textfield	= this.controlP5.addTextfield("textfield" + this.nextCWTextfieldId)
				.setPosition(x + buttonWidth + this.spacer, y)
				.setAutoClear(false)
				.setVisible(false)
				.setText("rgb(" + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue() + ")")
				.setId(this.nextCWTextfieldId)
				.moveTo(tab);
		textfield.getCaptionLabel().setVisible(false);

		this.nextCWTextfieldId = this.nextCWTextfieldId + 1;

		return new Controller[] { button, colorWheel, textfield };
	} // addColorWheelGroup


	/**
	 * Setter for this.isRunning
	 * 
	 * @param isRunning	boolean indicating whether or not the Menu is open
	 */
	public void setIsRunning(boolean isRunning) 
	{
		this.isRunning = isRunning;
	}

	public boolean getIsRunning()
	{
		return this.isRunning;
	}

	public ControlP5 getControlP5()
	{
		return this.controlP5;
	}


} // MenuTemplate
