package module_01;

import java.awt.Color;

import controlP5.ControlEvent;
import coreV2.*;
import processing.core.PApplet;

public class Module_01_Bubbles extends PApplet
{
	private ModuleDriver	driver;
	private InputHandler	inputHandler;
	private Canvas			canvas;
	private ColorFader 		colorFader;

	public static void main(String[] args)
	{
		PApplet.main("module_01.Module_01_Bubbles");
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

		this.driver.getCP5().getController("realTimeInput").setValue(1);

		this.noStroke();
		
	}

	public void draw()
	{
		//draw the background
		this.fill(0);
		this.canvas.background();
		
		
		int 	note 		= inputHandler.getMidiNote();
		int[] 	scale 		= inputHandler.getScale();
		int[] 	pitchColor 	= new int[] {-1,-1,-1};
		ColorScheme[] schemes = this.driver.getColorMenu().getColorSchemes();


			if((note) != -1)
			{
				pitchColor = schemes[0].getPitchColor(note);
			}
		
		//if the note was in the scale then set the color in the colorFader
		if(pitchColor[0] != -1)
		{
			colorFader.setTargetColor(pitchColor.clone());
		}
		
		//if the velocity is greater than the piano threshold then set the alpha to 255
		if(this.inputHandler.getAmplitude() > this.driver.getCP5().getController("piano").getValue())
		{
			colorFader.setTargetAlpha(255);
		}
		else // if not, set the alpha to 0
		{
			colorFader.setTargetAlpha(0);
		}
		
		//int[] curColor = colorFader.getColor();
		//this.fill(curColor[0], curColor[1], curColor[2], curColor[3]);

		//get the current color from color fader and fill it into the canvas
		//int[] curColor = colorFader.getColor();
		//this.fill(curColor[0], curColor[1], curColor[2], curColor[3]);
		//this.canvas.background();

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
			.setValue(500)
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
			.setValue(500)
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
			.setValue(500)
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