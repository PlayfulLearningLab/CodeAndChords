package demoDrivers;

import coreV2.*;
import coreV2_visuals.*;
import processing.core.PApplet;

public class CoreV2Demo extends PApplet
{
	private ModuleDriver driver;
	
	public static void main(String[] args)
	{
		PApplet.main("demoDrivers.CoreV2Demo");
	}
	
	public void settings()
	{
		//fullScreen();
		this.size(925, 520);
		this.pixelDensity(this.displayDensity());
	}
	
	public void setup()
	{
		this.driver = new ModuleDriver(this);
		//((VisualMenu)this.driver.getMenuGroup().getMenus()[2]).addVisual(new FireVisual(this.driver));
		((VisualMenu)this.driver.getMenuGroup().getMenus()[2]).addVisual(new VerticalRegtanglesVisual(this.driver));
		((VisualMenu)this.driver.getMenuGroup().getMenus()[2]).addVisual(new NeonStormVisual(this.driver));
		((VisualMenu)this.driver.getMenuGroup().getMenus()[2]).addVisual(new GlitchVisual(this.driver));
		((VisualMenu)this.driver.getMenuGroup().getMenus()[2]).addVisual(new StarsVisual(this.driver));

		
		System.out.println("Pixel density: " + this.displayDensity());
		System.out.println("Pixel width/height: " + this.pixelWidth + ", " + this.pixelHeight);
	}
	
	public void draw()
	{

	}
	
}
