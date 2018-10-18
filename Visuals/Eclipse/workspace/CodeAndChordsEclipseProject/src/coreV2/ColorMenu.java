package coreV2;
//Make buttons, Implementing control event method-->tells program what to do when buttons are clicked, no save/load color, add color wheel group method next to add sllider wheel group method
import java.awt.Color;

import controlP5.ControlP5;
import controlP5.Label;

public class ColorMenu extends MenuTemplate
{
	String[] noteNames;
	
	ColorScheme[] colorSchemes;
	

	public ColorMenu(ModuleDriver driver) 
	{
		super("Color Menu", driver, true);
		
		this.noteNames = new String[] {"A", "A#/Bb", "B", "C", "C#/Db", "D", "D#/Eb", "E", "F", "F#/Gb", "G", "G#/Ab"};

		
		this.addDannyButtons();
	}
	

	public void addDannyButtons()
	{

		Label.setUpperCaseDefault(false);
		
		int buttonWidth = 40;
		int buttonHeight = 20;

		int x = 20;

		int yStart = 190;
		int yIncrament = buttonHeight + 6;

		for(int i = 0; i < noteNames.length; i++)
		{
			this.controlP5.addToggle(noteNames[i])
			.setPosition(x, yStart + (i * yIncrament))
			.setSize(buttonWidth, buttonHeight)
			.setCaptionLabel(noteNames[i])
			.setValue(false)
			.setTab(this.getMenuTitle())
			.getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);
		}
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
}