package module_01_05;

import coreV2.InputHandler;
import coreV2.InputMenu;
import coreV2.MenuGroup;
import coreV2.ModuleDriver;
import processing.core.PApplet;

public class Module_01_05_MonophonicMidi extends PApplet
{

	private ModuleDriver driver;
	private InputHandler inputHandler;

	public static void main(String[] args)
	{
		PApplet.main("module_01_05.Module_01_05_MonophonicMidi");
	}

	public void settings()
	{
		this.size(925, 520);
	}

	public void setup()
	{
		this.driver = new ModuleDriver(this);

		this.inputHandler = this.driver.getInputHandler();

		MenuGroup menus = this.driver.getMenuGroup();
	}

	public void draw()
	{
		int[] RGB = this.driver.getCurrentColor(0);

		int r = RGB[0];
		int g = RGB[1];
		int b = RGB[2];

		this.fill(r, g, b);
		this.driver.getCanvas().background();
	}
}
