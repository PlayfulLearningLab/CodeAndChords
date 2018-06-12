package coreV2;

public class MenuGroup 
{
	private MenuTemplate[] 		menuGroup;
	
	private boolean				menuOpen;
	
	private MenuTemplate 		activeMenu;
	
	public MenuGroup()
	{
		this.menuGroup = new MenuTemplate[1];
		
		this.menuGroup[0] = new NavigationMenu();
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
		
		((NavigationMenu) this.menuGroup[0]).updateMenuList();
	}
	
	public MenuTemplate[] getMenus()
	{
		return this.menuGroup;
	}
	
	public void open()
	{
		this.activeMenu = this.menuGroup[0];
		this.menuOpen = true;
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
		this.menuOpen = false;
		ModuleDriver.getModuleDriver().getCanvas().fullScreen();
	}
	
	public void runMenu()
	{
		if(this.menuOpen)
		{
			this.activeMenu.drawMenu();
		}
	}
	
}
