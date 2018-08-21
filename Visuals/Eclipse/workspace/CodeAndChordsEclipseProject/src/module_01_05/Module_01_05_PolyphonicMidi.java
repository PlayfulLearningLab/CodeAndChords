package module_01_05;

import coreV2.*;
import processing.core.PApplet;

public class Module_01_05_PolyphonicMidi extends PApplet
{
	private ModuleDriver driver;
	private InputHandler inputHandler;

	public static void main(String[] args)
	{
		PApplet.main("module_01_05.Module_01_05_PolyphonicMidi");
	}

	public void settings()
	{
		this.size(925, 520);
	}

	public void setup()
	{
		this.driver = new ModuleDriver(this);
		
		this.inputHandler = InputHandler.getInputHandler();
		
		this.inputHandler.useMidiStreamInput();
		
		MenuGroup menus = this.driver.getMenuGroup();
		menus.addMenu(new InputMenu(this.driver));
	}

	public void draw()
	{
		int[] RGB = this.driver.getColorScheme().getCurrentColor();

		int r = RGB[0];
		int g = RGB[1];
		int b = RGB[2];

		this.fill(r, g, b);
		this.driver.getCanvas().background();
	}
}
