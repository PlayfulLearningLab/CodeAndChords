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
	
	protected String[]			controllers;
	protected String[]			labels;
	
	public Visual(ModuleDriver moduleDriver, String name)
	{
		this.moduleDriver = moduleDriver;
		this.parent = moduleDriver.getParent();
		this.cp5 = this.moduleDriver.getCP5();
		this.name = name;
		this.tabName = "Visual Menu";
		this.cp5.addListener(this);
	}
	
	public abstract void drawVisual();
	
	public abstract int getNumControllers();
	
	public String getControllerName(int controllerNum)
	{
		return this.controllers[controllerNum];
	}
	
	public String getLabelName(int controllerNum)
	{
		return this.labels[controllerNum];
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public void hide()
	{
		for(int i = 0; i < this.getNumControllers(); i++)
		{
			this.cp5.getController(this.controllers[i]).hide();
			this.cp5.getController(this.labels[i]).hide();
		}
	}
	
	public void show()
	{
		for(int i = 0; i < this.getNumControllers(); i++)
		{
			this.cp5.getController(this.controllers[i]).show();
			this.cp5.getController(this.labels[i]).show();
		}
	}
}
