package coreV2;

import controlP5.ControlP5;

public class MenuGroup 
{
	private MenuTemplate[] 		menuGroup;

	private MenuTemplate 		activeMenu;

	public MenuGroup()
	{
		this.menuGroup = new MenuTemplate[2];

		this.menuGroup[0] = new CanvasMenu();
		this.menuGroup[1] = new NavigationMenu();
		
		this.activeMenu = this.menuGroup[0];
	}

	public void addMenu(MenuTemplate menu)
	{	
		MenuTemplate[] newGroup = new MenuTemplate[this.menuGroup.length + 1];

		for(int i = 0; i < this.menuGroup.length; i++)
		{
			newGroup[i] = this.menuGroup[i];
		}

		newGroup[this.menuGroup.length] = menu;
		this.menuGroup = newGroup;

		((NavigationMenu) this.menuGroup[1]).updateMenuList();
	}

	public MenuTemplate[] getMenus()
	{
		return this.menuGroup;
	}

	public MenuTemplate getActiveMenu()
	{
		return this.activeMenu;
	}

	public boolean canvasMenuActive()
	{
		boolean val = false;

		if(this.activeMenu.getMenuTitle() == "Canvas Menu") 
		{
			val = true;
		}

		return val;
	}
	
	public ControlP5 getControlP5()
	{
		return this.menuGroup[0].getControlP5();
	}

	public void open()
	{
		this.activeMenu = this.menuGroup[1];
		this.activeMenu.setCanvasSize();
		this.activeMenu.getControlP5().getGroup(this.activeMenu.getMenuTitle()).show();
	}

	public void switchTo(int menuNumber)
	{
		if(menuNumber > this.menuGroup.length) throw new IllegalArgumentException ("Invalid menuNumber");

		this.activeMenu.getControlP5().getGroup(this.activeMenu.getMenuTitle()).hide();

		this.activeMenu = this.menuGroup[menuNumber];
		this.activeMenu.setCanvasSize();
		this.activeMenu.getControlP5().getGroup(this.activeMenu.getMenuTitle()).show();

	}

	public void close()
	{
		this.activeMenu.getControlP5().getGroup(this.activeMenu.getMenuTitle()).hide();
		ModuleDriver.getModuleDriver().getCanvas().fullScreen();
		this.activeMenu = this.menuGroup[0];
	}

}
