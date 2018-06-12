package coreV2;

import java.awt.Color;

public class NavigationMenu extends MenuTemplate
{
	String[] menuList;
	
	public NavigationMenu() 
	{
		super("Navigation Menu");
		
		this.menuList = new String[] { this.getMenuTitle() };
		
	}
	
	public void updateMenuList()
	{
		MenuGroup menus = ModuleDriver.getModuleDriver().getMenuGroup();
		
		this.menuList = new String[menus.getMenus().length];
		
		for(int i = 0; i < menus.getMenus().length; i++)
		{
			this.menuList[i] = menus.getMenus()[i].getMenuTitle();
		}
	}

	@Override
	public void drawMenu()
	{
		
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
