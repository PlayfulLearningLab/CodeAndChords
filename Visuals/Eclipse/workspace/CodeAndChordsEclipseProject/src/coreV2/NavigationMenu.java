package coreV2;

/*
 * This menu was replaced by the use of tabs
 */


import java.awt.Color;

import processing.core.PApplet;

public class NavigationMenu extends MenuTemplate
{
	String[] menuList;
	
	public NavigationMenu(ModuleDriver driver) 
	{
		super("Navigation Menu", driver, true);
		
		//this.updateMenuList();
		
	}
	
	public void updateMenuList()
	{
		MenuTemplate[] menus = this.driver.getMenuGroup().getMenus();
		//System.out.println("Length = " + menus.getMenus().length);
		
		this.menuList = new String[menus.length];
		
		for(int i = 0; i < menus.length; i++)
		{
			this.menuList[i] = menus[i].getMenuTitle();
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
