package coreV2;

import controlP5.ControlListener;
import controlP5.ControlP5;
import controlP5.Controller;
import controlP5.Group;
import processing.core.PApplet;

public abstract class Visual implements ControlListener
{
	protected ModuleDriver 		moduleDriver;
	
	protected PApplet 			parent;
	
	protected ControlP5			cp5;
	
	protected String			name;
		
	protected String			tabName;
	
	protected boolean			autoFormatControllers;
	
	public Visual(ModuleDriver moduleDriver, String name)
	{
		this.moduleDriver = moduleDriver;
		this.parent = moduleDriver.getParent();
		this.cp5 = this.moduleDriver.getCP5();
		this.name = name;
		this.tabName = "Visual Menu";
		this.cp5.addListener(this);
		this.autoFormatControllers = true;
	}
	
	public abstract void drawVisual();
	
	public abstract int getNumControllers();
	
	public abstract String getControllerName(int controllerNum);
	
	public abstract String getLabelName(int controllerNum);
	
	public String getName()
	{
		return this.name;
	}
	
	public boolean getAutoFormatControllers()
	{
		return this.autoFormatControllers;
	}
	
	public void hide()
	{
		for(int i = 0; i < this.getNumControllers(); i++)
		{
			this.cp5.getController(this.getControllerName(i)).hide();
			this.cp5.getController(this.getLabelName(i)).hide();
		}
	}
	
	public void show()
	{
		for(int i = 0; i < this.getNumControllers(); i++)
		{
			this.cp5.getController(this.getControllerName(i)).show();
			this.cp5.getController(this.getLabelName(i)).show();
		}
	}
}
