package coreV2;
//Make buttons, Implementing control event method-->tells program what to do when buttons are clicked, no save/load color, add color wheel group method next to add sllider wheel group method
import java.awt.Color;

import controlP5.ColorWheel;
import controlP5.ControlEvent;
import controlP5.ControlP5;
import controlP5.Controller;
import controlP5.Label;

public class ColorMenu extends MenuTemplate
{
	String[] noteNames;
	
	ColorScheme[] colorSchemes;
	
	private boolean setupComplete = false;
	
	//parameters for button placement
	
	private int buttonWidth = 40;
	private int buttonHeight = 20;

	private int xPos = 10;

	private int yStart = 190;
	private int yIncrament = buttonHeight + 6;
	

	public ColorMenu(ModuleDriver driver) 
	{
		super("Color Menu", driver, true);
		
		this.noteNames = new String[] {"C", "C#/Db", "D", "D#/Eb", "E", "F", "F#/Gb", "G", "G#/Ab", "A", "A#/Bb", "B"};

		this.colorSchemes = new ColorScheme[] {new ColorScheme(this.driver)};
		this.parent.registerMethod("draw", this);
		
		this.addDannyButtons();
		this.addColorCustomizationControls();
		
		this.setupComplete = true;
	}
	

	public void addDannyButtons()
	{

		Label.setUpperCaseDefault(false);

		for(int i = 0; i < this.noteNames.length; i++)
		{
			this.controlP5.addToggle(this.noteNames[i])
			.setPosition(xPos, yStart + (i * yIncrament))
			.setSize(buttonWidth, buttonHeight)
			.setCaptionLabel(this.noteNames[i])
			.setValue(false)
			.setTab(this.getMenuTitle())
			.getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);
		}
	}
	
	public void draw()
	{
		if(this.driver.getMenuGroup().getActiveMenu().equals(this))
		{
			this.drawColorIndicators();
		}
	}
	
	public void drawColorIndicators()
	{	
		System.out.println("drawing");
		
		for(int i = 0; i < this.noteNames.length; i++)
		{
			int[] RGB = this.colorSchemes[0].getPitchColor(i);
			
			this.parent.fill(RGB[0], RGB[1], RGB[2]);
			this.parent.rect(xPos + buttonWidth + 10, yStart +i*yIncrament, 70, buttonHeight);
		}
	}
	
	private void addColorCustomizationControls()
	{
		this.controlP5.addColorWheel("colorPicker", 150, yStart, 140)
		.setTab(this.getMenuTitle())
		.getCaptionLabel()
		.setVisible(false);
		
		int sliderX = 150;
		int sliderY = yStart + 180;
		
		int sliderH = 20;
		int sliderW = 140;
		
		int yPadding = 20;
		
		this.controlP5.addSlider("redSlider")
		.setMin(0)
		.setMax(255)
		.setPosition(sliderX, sliderY)
		.setSize(sliderW, sliderH)
		.setDecimalPrecision(0)
		.setTab(this.getMenuTitle())
		.getCaptionLabel().setVisible(false);
		
		this.controlP5.addLabel("Red")
		.setPosition(sliderX, sliderY - 12)
		.setColor(255)
		.setTab(this.getMenuTitle());
		
		sliderY += (sliderH + yPadding);
		
		this.controlP5.addSlider("greenSlider")
		.setMin(0)
		.setMax(255)
		.setPosition(sliderX, sliderY)
		.setSize(sliderW, sliderH)
		.setDecimalPrecision(0)
		.setTab(this.getMenuTitle())
		.getCaptionLabel().setVisible(false);
		
		this.controlP5.addLabel("Green")
		.setPosition(sliderX, sliderY - 12)
		.setColor(255)
		.setTab(this.getMenuTitle());
		
		sliderY += (sliderH + yPadding);
		
		this.controlP5.addSlider("blueSlider")
		.setMin(0)
		.setMax(255)
		.setPosition(sliderX, sliderY)
		.setSize(sliderW, sliderH)
		.setDecimalPrecision(0)
		.setTab(this.getMenuTitle())
		.getCaptionLabel().setVisible(false);
		
		this.controlP5.addLabel("Blue")
		.setPosition(sliderX, sliderY - 12)
		.setColor(255)
		.setTab(this.getMenuTitle());
	}
		
	public void addButtons()
	{
		this.controlP5.addToggle("rainbow")
		.setPosition(50, 100)
		.setWidth(50)
		.setCaptionLabel("Rainbow")
		.setValue(true)
		.setTab(this.getMenuTitle())
		.setInternalValue(0);
		
		this.controlP5.getController("rainbow")
		.getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);
		
		this.getControlP5().addToggle("dichrom")
		.setPosition(100, 100)
		.setWidth(50)
		.setCaptionLabel("Dichrom")
		.setTab(this.getMenuTitle())
		.setInternalValue(0);
		
		this.controlP5.getController("dichrom")
		.getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);
		
		this.getControlP5().addToggle("trichrom")
		.setPosition(150, 100)
		.setWidth(50)
		.setCaptionLabel("Trichrom")
		.setTab(this.getMenuTitle())
		.setInternalValue(0);
		
		this.controlP5.getController("trichrom")
		.getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);
		
		this.getControlP5().addToggle("custom")
		.setPosition(200, 100)
		.setWidth(50)
		.setCaptionLabel("Custom")
		.setTab(this.getMenuTitle())
		.setInternalValue(0);
		
		this.controlP5.getController("custom")
		.getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);
		
		this.getControlP5().addToggle("canvas")
		.setPosition(50, 150)
		.setWidth(50)
		.setCaptionLabel("Canvas")
		.setTab(this.getMenuTitle())
		.setInternalValue(0);
		
		this.controlP5.getController("canvas")
		.getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);
		
		this.getControlP5().addToggle("tonic")
		.setPosition(100, 150)
		.setWidth(100)
		.setCaptionLabel("Tonic")
		.setTab(this.getMenuTitle())
		.setInternalValue(0);
		
		this.controlP5.getController("tonic")
		.getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);
		
		this.getControlP5().addToggle("2ndcolor")
		.setPosition(150, 150)
		.setWidth(50)
		.setCaptionLabel("2nd Color")
		.setTab(this.getMenuTitle())
		.setInternalValue(0);
		
		this.controlP5.getController("2ndcolor")
		.getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);
		
		this.getControlP5().addToggle("3rdcolor")
		.setPosition(200, 150)
		.setWidth(50)
		.setCaptionLabel("3rd Color")
		.setTab(this.getMenuTitle())
		.setInternalValue(0);
		
		this.controlP5.getController("3rdcolor")
		.getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);
		
		
		this.getControlP5().addToggle("a")
		.setPosition(50, 200)
		.setWidth(50)
		.setCaptionLabel("A")
		.setTab(this.getMenuTitle())
		.setInternalValue(0);
		
		this.controlP5.getController("a")
		.getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);
		
		
		this.getControlP5().addToggle("a#/bb")
		.setPosition(100, 200)
		.setWidth(50)
		.setCaptionLabel("A#/Bb")
		.setTab(this.getMenuTitle())
		.setInternalValue(0);
		
		this.controlP5.getController("a#/bb")
		.getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);
		
		
		this.getControlP5().addToggle("b")
		.setPosition(150, 200)
		.setWidth(50)
		.setCaptionLabel("B")
		.setTab(this.getMenuTitle())
		.setInternalValue(0);
		
		this.controlP5.getController("b")
		.getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);
		
		
		this.getControlP5().addToggle("c")
		.setPosition(200, 200)
		.setWidth(50)
		.setCaptionLabel("C")
		.setTab(this.getMenuTitle())
		.setInternalValue(0);
		
		this.controlP5.getController("c")
		.getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);
		
		
		this.getControlP5().addToggle("c#/db")
		.setPosition(50, 220)
		.setWidth(50)
		.setCaptionLabel("C#/Db")
		.setTab(this.getMenuTitle())
		.setInternalValue(0);
		
		this.controlP5.getController("c#/db")
		.getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);
		
		
		this.getControlP5().addToggle("d")
		.setPosition(100, 220)
		.setWidth(50)
		.setCaptionLabel("D")
		.setTab(this.getMenuTitle())
		.setInternalValue(0);
		
		this.controlP5.getController("d")
		.getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);
		
		
		this.getControlP5().addToggle("d#/db")
		.setPosition(150, 220)
		.setWidth(50)
		.setCaptionLabel("D#/Db")
		.setTab(this.getMenuTitle())
		.setInternalValue(0);
		
		this.controlP5.getController("d#/db")
		.getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);
		
		this.getControlP5().addToggle("e")
		.setPosition(200, 220)
		.setWidth(50)
		.setCaptionLabel("E")
		.setTab(this.getMenuTitle())
		.setInternalValue(0);
		
		this.controlP5.getController("e")
		.getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);
		
		this.getControlP5().addToggle("f")
		.setPosition(50, 240)
		.setWidth(50)
		.setCaptionLabel("F")
		.setTab(this.getMenuTitle())
		.setInternalValue(0);
		
		this.controlP5.getController("f")
		.getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);
		
		this.getControlP5().addToggle("f#/gb")
		.setPosition(100, 240)
		.setWidth(50)
		.setCaptionLabel("F#/Gb")
		.setTab(this.getMenuTitle())
		.setInternalValue(0);
		
		this.controlP5.getController("f#/gb")
		.getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);
		
		this.getControlP5().addToggle("g")
		.setPosition(150, 240)
		.setWidth(50)
		.setCaptionLabel("G")
		.setTab(this.getMenuTitle())
		.setInternalValue(0);
		
		this.controlP5.getController("g")
		.getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);
		
		this.getControlP5().addToggle("g#/ab")
		.setPosition(200, 240)
		.setWidth(50)
		.setCaptionLabel("G#/Ab")
		.setTab(this.getMenuTitle())
		.setInternalValue(0);
		
		this.controlP5.getController("g#/ab")
		.getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);
	}
	
	public void addSliders()
	{
		
		
		this.getControlP5().addSlider("hue")
		.setPosition(300, 50)
		.setSize(200,25)
		.setCaptionLabel("Hue")
		.setTab(this.getMenuTitle())
		.setValue(0);
		
		this.getControlP5().addSlider("saturation")
		.setPosition(300, 90)
		.setSize(200,25)
		.setCaptionLabel("Saturation")
		.setTab(this.getMenuTitle())
		.setValue(0);
		
		this.getControlP5().addSlider("brightness")
		.setPosition(300, 130)
		.setSize(200,25)
		.setCaptionLabel("Brightness")
		.setTab(this.getMenuTitle())
		.setValue(0);
		
		this.getControlP5().addSlider("redmod")
		.setPosition(600, 50)
		.setSize(200,25)
		.setCaptionLabel("Red Mod.")
		.setTab(this.getMenuTitle())
		.setValue(0);
		
		this.getControlP5().addSlider("greenmod")
		.setPosition(600, 90)
		.setSize(200,25)
		.setCaptionLabel("Green Mod.")
		.setTab(this.getMenuTitle())
		.setValue(0);
		
		this.getControlP5().addSlider("bluemod")
		.setPosition(600, 130)
		.setSize(200,25)
		.setCaptionLabel("Blue Mod.")
		.setTab(this.getMenuTitle())
		.setValue(0);
		
		this.getControlP5().addSlider("alpha")
		.setPosition(50, 300)
		.setSize(200,25)
		.setLabel("Alpha")
		.setTab(this.getMenuTitle())
		.setValue(0);
		
	}
	
	public void controlEvent(ControlEvent theEvent)
	{
		int pitchButtonIndex = this.isPitchButton(theEvent);
		if(pitchButtonIndex != -1 && theEvent.getController().getValue() == 1)
		{
			for(int i = 0; i < this.noteNames.length; i++)
			{
				if(i != pitchButtonIndex && this.controlP5.getController(this.noteNames[i]).getValue() == 1)
				{
					this.controlP5.getController(this.noteNames[i]).setValue(0);
				}
			}
			
			int[] curRGB = this.colorSchemes[0].getPitchColor(pitchButtonIndex);
			int curColor = new Color(curRGB[0], curRGB[1], curRGB[2]).getRGB();
			
			ColorWheel cw = (ColorWheel) this.controlP5.getController("colorPicker");
			
			cw.setRGB(curColor);
			
			if(cw.r() != this.controlP5.getController("redSlider").getValue())
				this.controlP5.getController("redSlider").setValue(cw.r());
			
			if(cw.g() != this.controlP5.getController("greenSlider").getValue())
				this.controlP5.getController("greenSlider").setValue(cw.g());
			
			if(cw.b() != this.controlP5.getController("blueSlider").getValue())
				this.controlP5.getController("blueSlider").setValue(cw.b());
		}
		
		if(this.isColorSlider(theEvent) && this.setupComplete && (this.controlP5.getController("redSlider").isMousePressed() || this.controlP5.getController("greenSlider").isMousePressed() || this.controlP5.getController("blueSlider").isMousePressed()))
				
		{
			int[] RGB = new int[] {		(int) this.controlP5.getController("redSlider").getValue(),
										(int) this.controlP5.getController("greenSlider").getValue(),
										(int) this.controlP5.getController("blueSlider").getValue()};
			
			int newColor = new Color(RGB[0], RGB[1], RGB[2]).getRGB();
			
			if(newColor != ((ColorWheel)this.controlP5.getController("colorPicker")).getRGB())
				( (ColorWheel)this.controlP5.getController("colorPicker")).setRGB(newColor);
						
		}
		
		if(theEvent.getName() == "colorPicker" && this.controlP5.getController("colorPicker").isMousePressed())
		{
			ColorWheel cw = (ColorWheel) this.controlP5.getController("colorPicker");
			
			if(cw.r() != this.controlP5.getController("redSlider").getValue())
				this.controlP5.getController("redSlider").setValue(cw.r());
			
			if(cw.g() != this.controlP5.getController("greenSlider").getValue())
				this.controlP5.getController("greenSlider").setValue(cw.g());
			
			if(cw.b() != this.controlP5.getController("blueSlider").getValue())
				this.controlP5.getController("blueSlider").setValue(cw.b());
		}
		
		if(this.getActivePitchColor() != -1 && this.isNewColor())
		{			
			int newColor = ((ColorWheel)this.controlP5.getController("colorPicker")).getRGB();
			
			int[] curRGB = this.colorSchemes[0].getPitchColor(this.getActivePitchColor());
			int curColor = new Color(curRGB[0], curRGB[1], curRGB[2]).getRGB();
			
			if(newColor != curColor)
			{
				this.colorSchemes[0].setColor(this.getActivePitchColor(), newColor);
			}
		}
		
	}
	
	@Override
	public void sliderEvent(int id, float val) 
	{
		
	}

	@Override
	public void buttonEvent(int id) 
	{
		
	}

	@Override
	public void colorWheelEvent(int id, Color color) 
	{
		
	}
	
	private int isPitchButton(ControlEvent theEvent)
	{
		int value = -1;
		
		for(int i = 0; i < this.noteNames.length; i++)
		{
			if(theEvent.getName() == this.noteNames[i])
			{
				value = i;
			}
		}
		
		return value;
	}
	
	private boolean isColorSlider(ControlEvent theEvent)
	{
		boolean value = theEvent.isFrom("redSlider") || theEvent.isFrom("greenSlider") || theEvent.isFrom("blueSlider");
		
		return value;
	}
	
	private int getActivePitchColor()
	{
		int val = -1;
		
		for(int i = 0; val == -1 && i < this.noteNames.length ; i++)
		{
			if(this.setupComplete && this.controlP5.getController(this.noteNames[i]).getValue() == 1)
			{
				val = i;
			}
		}
		
		return val;
	}
	
	private boolean isNewColor()
	{
		boolean val = this.controlP5.getController("colorPicker").isMouseOver()
				||	this.controlP5.getController("redSlider").isMouseOver()
				||	this.controlP5.getController("greenSlider").isMouseOver()
				||	this.controlP5.getController("blueSlider").isMouseOver();
		
		return val;
	}
	
	public ColorScheme[] getColorSchemes()
	{
		return this.colorSchemes;
	}
}