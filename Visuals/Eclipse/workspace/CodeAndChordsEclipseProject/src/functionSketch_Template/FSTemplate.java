package functionSketch_Template;

import java.awt.Color;

import controlP5.Button;
import controlP5.ControlEvent;
import controlP5.ControlP5;
import controlP5.ControllerGroup;
import controlP5.Group;
import processing.core.PApplet;

public abstract class FSTemplate extends PApplet {
	
	private ControlP5 	cp5;
	
	private int       	fullScreenX 	= 925;
	private int       	fullScreenY 	= 520;
	private int      	menuWidth		= 300;
	private int 		menuScreenX 	= this.fullScreenX - this.menuWidth;
	
	private boolean   	menuIsOpen 		= false;
	
	public ControlP5 getCP5() { return this.cp5; }

	
	public void parentSettings()
	{
		this.size(this.fullScreenX, this.fullScreenY);
	}
	
	public void parentSetup()
	{		
		this.cp5 = new ControlP5(this);
		
		this.cp5.addGroup("menuControls");
		
		//create the menu
		this.cp5.addButton("menuButton")
			.setSize(50, 50)
			.setPosition(5, 5)
			.setLabel("Menu");
		
		this.cp5.addButton("closeMenuButton")
			.setSize( 200, 50)
			.setPosition( 50 ,450)
			.setLabel("Close Menu")
			.setGroup("menuControls");
		
		this.cp5.addScrollableList("menuTabList", 20, 10, 260, 30)
			.setGroup("menuControls")
			.setLabel("Choose A Menu")
			.close()
			.setBarHeight(30);
		
	}
	
	public void parentDraw()
	{
		this.background(0);

		if(this.menuIsOpen) 
		{ 
			this.cp5.get("menuButton").hide();
			this.cp5.getGroup("menuControls").show();
			
			
			this.stroke(20);
			this.fill(20);
			this.rect(0,0,this.menuWidth, this.fullScreenY);

			this.stroke(Color.MAGENTA.getRGB());
			this.strokeWeight(5);
			this.line(this.menuWidth, 0, this.menuWidth, this.fullScreenY); 
		}
		else 
		{ 
			this.cp5.get("menuButton").show(); 
			this.cp5.getGroup("menuControls").hide();
		}
	}
	
	public abstract void controlEvent(ControlEvent theEvent);
	
	public void parentControlEvent(String controllerName)
	{
		switch(controllerName)
		{
		case "menuButton":
			
			this.menuIsOpen = true;
			break;
			
		case "closeMenuButton":
			
			this.menuIsOpen = false;
			break;
			
		}//switch
	}//parentControlEvent()
	
	
	private float displayPos(float num)
	{
		if(this.menuIsOpen)
		{
			num = this.map(num, 0, this.fullScreenX, 0, this.menuScreenX) + this.menuWidth;
		}

		return num;
	}

	private float displaySize(float num)
	{
		if(this.menuIsOpen)
		{
			num = this.map(num, 0, this.fullScreenX, 0, this.menuScreenX);
		}

		return num;
	}
	

}
