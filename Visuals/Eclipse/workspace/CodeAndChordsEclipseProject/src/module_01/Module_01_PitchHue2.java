package module_01;

import java.awt.Color;

import controlP5.ControlEvent;
import coreV2.*;
import processing.core.PApplet;

public class Module_01_PitchHue2 extends PApplet
{
	private ModuleDriver	driver;
	private InputHandler	inputHandler;
	private Canvas			canvas;
	private ColorFader 		colorFader;
	
	public static void main(String[] args)
	{
		PApplet.main("module_01.Module_01_PitchHue2");
	}
	
	public void settings()
	{
		this.size(925, 520);
	}
	
	public void setup()
	{
		this.driver = new ModuleDriver(this);

		this.canvas = this.driver.getCanvas();

		MenuGroup menus = this.driver.getMenuGroup();
		this.inputHandler = this.driver.getInputHandler();
		
		visualMenu visualMenu = new visualMenu(driver);
		menus.addMenu(visualMenu);
				
		this.noStroke();
	}
	
	public void draw()
	{
		int inputNum;
		int note;
		int[] scale;
		int[] colors = new int[] {0,0,0};
		int[] result;

		
		note = inputHandler.getMidiNote();
		scale = inputHandler.getScale();
		inputNum = scale.length;
		ColorScheme[] schemes = this.driver.getColorMenu().getColorSchemes();
		
		for(int i = 0; i < inputNum + 1; i++)
		{ 
			if((note%12) == i)
			{
				colors = schemes[0].getPitchColor(i);
			}
		}
		
		colorFader.setTargetColor(colors[0], colors[1], colors[2]);
		result = colorFader.getColor();
		this.fill(result[0], result[1], result[2]);
		this.canvas.background();
	}
	
	public class visualMenu extends MenuTemplate
	{
		public visualMenu(ModuleDriver driver)
		{
			super("VISUAL MENU", driver, true);
			colorFader = new ColorFader(0,0,0,this.parent);
			
			this.addButtons();
		}
		
		private void addButtons()
		{
			this.controlP5.addSlider("transition")
			.setMin(30)
			.setMax(1500)
			.setPosition(10, 70)
			.setSize(140, 20)
			.setDecimalPrecision(0)
			.setTab(this.getMenuTitle())
			.getCaptionLabel().setVisible(false);
			
			this.controlP5.addLabel("Transition Time (MilliSeconds)")
			.setPosition(10, 60)
			.setColor(255)
			.setTab(this.getMenuTitle());
			
			
			this.controlP5.addSlider("attack")
			.setMin(30)
			.setMax(1500)
			.setPosition(10, 120)
			.setSize(140, 20)
			.setDecimalPrecision(0)
			.setTab(this.getMenuTitle())
			.getCaptionLabel().setVisible(false);
			
			this.controlP5.addLabel("Attack Time (MilliSeconds)")
			.setPosition(10, 110)
			.setColor(255)
			.setTab(this.getMenuTitle());
			
			this.controlP5.addSlider("release")
			.setMin(30)
			.setMax(1500)
			.setPosition(10, 180)
			.setSize(140, 20)
			.setDecimalPrecision(0)
			.setTab(this.getMenuTitle())
			.getCaptionLabel().setVisible(false);
			
			this.controlP5.addLabel("Release Time (MilliSeconds)")
			.setPosition(10, 170)
			.setColor(255)
			.setTab(this.getMenuTitle());
		}
		
		public void controlEvent(ControlEvent theEvent)
		{
			if(theEvent.getName() == "transition")
			{
				colorFader.setTransitionDuration((int)theEvent.getValue());
			}
			
			if(theEvent.getName() == "attack")
			{
				colorFader.setAttackDuration((int)theEvent.getValue());
			}
			
			if(theEvent.getName() == "release")
			{
				colorFader.setReleaseDuration((int)theEvent.getValue());
			}
		}

		@Override
		public void sliderEvent(int id, float val) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void buttonEvent(int id) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void colorWheelEvent(int id, Color color) {
			// TODO Auto-generated method stub
			
		}
	}
}