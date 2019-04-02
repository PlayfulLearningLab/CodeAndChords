package coreV2;

import coreV2_visuals.BubblesVisual;
import processing.core.PApplet;

public class CoreV2Demo extends PApplet
{
	private ModuleDriver driver;
	
	public static void main(String[] args)
	{
		PApplet.main("coreV2.CoreV2Demo");
	}
	
	public void settings()
	{
		size(925, 520);
	}
	
	public void setup()
	{
		this.driver = new ModuleDriver(this);
		((VisualMenu)this.driver.getMenuGroup().getMenus()[2]).addVisual(new BubblesVisual(this.driver));
	}
	
	public void draw()
	{

	}
	
}
