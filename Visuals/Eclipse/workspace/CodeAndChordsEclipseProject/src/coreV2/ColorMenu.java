package coreV2;
//Make buttons, Implementing control event method-->tells program what to do when buttons are clicked, no save/load color, add color wheel group method next to add sllider wheel group method
import java.awt.Color;

import controlP5.ColorWheel;
import controlP5.ControlEvent;
import controlP5.ControlP5;
import controlP5.Controller;
import controlP5.Label;
import controlP5.ScrollableList;

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
	
	private int[][]  colors = new int[3][3];
	

	public ColorMenu(ModuleDriver driver) 
	{
		super("Color Menu", driver, true);
		
		this.noteNames = new String[] {"C", "C#/Db", "D", "D#/Eb", "E", "F", "F#/Gb", "G", "G#/Ab", "A", "A#/Bb", "B"};

		this.colorSchemes = new ColorScheme[] {new ColorScheme(this.driver)};
		this.parent.registerMethod("draw", this);
		this.addChromatic();
		this.addDannyButtons();
		this.addColorCustomizationControls();
		this.setupComplete = true;
	}
	
	public void addChromatic()
	{
		this.controlP5.addButton("Generate")
		.setPosition(150, 50)
		.setSize(50, 25)
		.setCaptionLabel("Generate")
		.setTab(this.getMenuTitle())
		.getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);
		
		
		this.controlP5.addColorWheel("Wheel", 300, 50, 100)
		.setTab(this.getMenuTitle())
		.getCaptionLabel()
		.setVisible(false);
		
		this.controlP5.addToggle("1st Color")
		.setPosition(100, 100)
		.setSize(50, 25)
		.setCaptionLabel("1st Color")
		.setValue(false)
		.setTab(this.getMenuTitle())
		.getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);
		
		this.controlP5.addToggle("2nd Color")
		.setPosition(100, 130)
		.setSize(50, 25)
		.setCaptionLabel("2nd Color")
		.setValue(false)
		.setTab(this.getMenuTitle())
		.getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);
		
		this.controlP5.addToggle("3rd Color")
		.setPosition(100, 160)
		.setSize(50, 25)
		.setCaptionLabel("3rd Color")
		.setValue(false)
		.setTab(this.getMenuTitle())
		.getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);
		
		this.controlP5.getController("1st Color").hide();
		this.controlP5.getController("2nd Color").hide();
		this.controlP5.getController("3rd Color").hide();
		
		this.controlP5.addScrollableList("Colors")
		.setPosition(10, 70)
		.setWidth(100)
		.setBarHeight(30)
		.setItemHeight(30)
		.setItems(new String[] {"DiChromatic", "TriChromatic", "Rainbow"})
		.setValue(2)
		.close()
		.setTab(this.getMenuTitle());
		

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
		
		if(this.driver.getMenuGroup().getActiveMenu().equals(this))
		{
			this.drawChromatic();
		}
	}
	
	
	public void drawChromatic()
	{
		if(this.controlP5.getController("Colors").getValue() == 0)
		{
			this.parent.fill(colors[0][0],colors[0][1],colors[0][2]);
			this.parent.rect(150, 100, 25, 25);
			
			this.parent.fill(colors[1][0],colors[1][1],colors[1][2]);
			this.parent.rect(150, 130, 25, 25);
		}
		if(this.controlP5.getController("Colors").getValue() == 1)
		{
			this.parent.fill(colors[0][0],colors[0][1],colors[0][2]);
			this.parent.rect(150, 100, 25, 25);
			
			this.parent.fill(colors[1][0],colors[1][1],colors[1][2]);
			this.parent.rect(150, 130, 25, 25);
			
			this.parent.fill(colors[2][0],colors[2][1],colors[2][2]);
			this.parent.rect(150, 160, 25, 25);
		}
		
	}
	public void drawColorIndicators()
	{	
		//System.out.println("drawing");
		
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
			{
				( (ColorWheel)this.controlP5.getController("colorPicker")).setRGB(newColor);
			}
				
						
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
		
		
		if(theEvent.getName() == "Colors")
		{
			if(this.controlP5.getController("Colors").getValue() == 0)
			{
				this.controlP5.getController("1st Color").show();
				this.controlP5.getController("2nd Color").show();
				this.controlP5.getController("3rd Color").hide();
			}
			
			if(this.controlP5.getController("Colors").getValue() == 1)
			{
				this.controlP5.getController("1st Color").show();
				this.controlP5.getController("2nd Color").show();
				this.controlP5.getController("3rd Color").show();
			}
			
			if(this.controlP5.getController("Colors").getValue() == 2)
			{
				this.controlP5.getController("1st Color").hide();
				this.controlP5.getController("2nd Color").hide();
				this.controlP5.getController("3rd Color").hide();
			}
		}
		

		if(theEvent.getName() == "1st Color" && setupComplete != false)
		{
			if(this.controlP5.getController("2nd Color").getValue() == 1)
			{
				this.controlP5.getController("2nd Color").setValue(0);
			}
			if(this.controlP5.getController("3rd Color").getValue() == 1)
			{
				this.controlP5.getController("3rd Color").setValue(0);
			}
		}
		
		
		if(theEvent.getName() == "2nd Color" && setupComplete != false)
		{
			if(this.controlP5.getController("1st Color").getValue() == 1)
			{
				this.controlP5.getController("1st Color").setValue(0);
			}
			if(this.controlP5.getController("3rd Color").getValue() == 1)
			{
				this.controlP5.getController("3rd Color").setValue(0);
			}
		}
		
		if(theEvent.getName() == "3rd Color" && setupComplete != false)
		{
			if(this.controlP5.getController("2nd Color").getValue() == 1)
			{
				this.controlP5.getController("2nd Color").setValue(0);
			}
			if(this.controlP5.getController("1st Color").getValue() == 1)
			{
				this.controlP5.getController("1st Color").setValue(0);
			}
		}
		
		if(theEvent.getName() == "Wheel")
		{
			ColorWheel wheel = (ColorWheel) this.controlP5.getController("Wheel");
			if(this.controlP5.getController("1st Color").getValue() == 1)
			{
				colors[0][0] = wheel.r();
				colors[0][1] = wheel.g();
				colors[0][2] = wheel.b();
			}
			if(this.controlP5.getController("2nd Color").getValue() == 1)
			{
				colors[1][0] = wheel.r();
				colors[1][1] = wheel.g();
				colors[1][2] = wheel.b();				
			}
			if(this.controlP5.getController("3rd Color").getValue() == 1)
			{
				colors[2][0] = wheel.r();
				colors[2][1] = wheel.g();
				colors[2][2] = wheel.b();				
			}
		}
		if(theEvent.getName() == "Generate" && setupComplete != false)
		{
			if(this.controlP5.getController("Colors").getValue() == 0)
			{
				float	rDif	= colors[0][0] - colors[1][0];
				float	gDif	= colors[0][1] - colors[1][1];
				float	bDif	= colors[0][2] - colors[1][2];
				
				float	percent		= 100 / 11;
				colorSchemes[0].setColor(0, colors[0][0], colors[0][1], colors[0][2]);
				colorSchemes[0].setColor(11, colors[1][0], colors[1][1], colors[1][2]);
				
				for(int i = 1; i < 11; i++)
				{
					colorSchemes[0].setColor(i, Math.round(colors[0][0] - (rDif * i * percent / 100)), Math.round(colors[0][1] - (gDif * i * percent / 100)), Math.round(colors[0][2] - (bDif * i * percent / 100)));
				} 
			}
			
			if(this.controlP5.getController("Colors").getValue() == 1)
			{
				int	color1pos	= 0;
				int	color2pos;
				int	color3pos;

				int	divideBy1;
				int	divideBy2;
				int	divideBy3;
				
				color2pos	= 6;	// subdominant
				color3pos	= 11;
				divideBy1	= 6;
				divideBy2	= 6;
				
				
				int	redDelta1	= (int)((colors[0][0] - colors[1][0]) / divideBy1);
				int	greenDelta1	= (int)((colors[0][1] - colors[1][1]) / divideBy1);
				int	blueDelta1	= (int)((colors[0][2] - colors[1][2]) / divideBy1);

				int	redDelta2	= (int)((colors[1][0] - colors[2][0]) / divideBy2);
				int	greenDelta2	= (int)((colors[1][1] - colors[2][1]) / divideBy2);
				int	blueDelta2	= (int)((colors[1][2] - colors[2][2]) / divideBy2);

				
				colorSchemes[0].setColor(0, colors[0][0], colors[0][1], colors[0][2]);
				for(int i = 1; i < color2pos; i++)
				{
					int[] list = colorSchemes[0].getPitchColor(i-1);
					colorSchemes[0].setColor(i, list[0] - redDelta1, list[1] - greenDelta1, list[2] - blueDelta1);

				} // for - first color to second color
				colorSchemes[0].setColor(6, colors[1][0], colors[1][1], colors[1][2]);
				for(int i = color2pos + 1; i < color3pos; i++)
				{
					int[] list = colorSchemes[0].getPitchColor(i-1);
					colorSchemes[0].setColor(i, list[0] - redDelta2, list[1] - greenDelta2, list[2] - blueDelta2);
				} // for - first color to second color

				colorSchemes[0].setColor(11, colors[2][0], colors[2][1], colors[2][2]);
			}
			if(this.controlP5.getController("Colors").getValue() == 2)
			{
				colorSchemes[0].setPitchColors(0);
			
			}
		}
		
		if(theEvent.getName() == "Key Change" || theEvent.getName() == "Keys")
		{
			int[] scale = this.driver.getInputHandler().getScale();
			
			for(int i = 0; i < 12; i++)
			{
				if(contains(i, scale) == false)
				{
					//can't use button
					this.controlP5.getController(this.noteNames[i]).setColorBackground(Color.DARK_GRAY.getRGB());
					this.controlP5.getController(this.noteNames[i]).setColorForeground(Color.GRAY.getRGB());
					this.controlP5.getController(this.noteNames[i]).setColorActive(Color.LIGHT_GRAY.getRGB());
					this.controlP5.getController(this.noteNames[i]).lock();
				}
				else
				{
					//can use button
					this.controlP5.getController(this.noteNames[i]).setColorBackground(theEvent.getController().getColor().getBackground());
					this.controlP5.getController(this.noteNames[i]).setColorForeground(theEvent.getController().getColor().getForeground());
					this.controlP5.getController(this.noteNames[i]).setColorActive(theEvent.getController().getColor().getActive());
					this.controlP5.getController(this.noteNames[i]).unlock();
				}
			}
				
		}
	}

	
	private boolean contains(int number, int[] array)
	{ 
		boolean result;
		result = false;
		
		for(int i = 0; i < array.length; i++)
		{
			if(number == array[i])
			{
				result = true;
			}
		}
		return result;
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