package coreV2;

public class MenuGroup 
{

	private ModuleDriver	driver;
	
	private MenuTemplate[] 			menuGroup;
	
	private boolean			menuOpen;
	
	private MenuTemplate 			activeMenu;
	
	public MenuGroup(ModuleDriver driver)
	{
		this.driver = driver;
		
		this.menuGroup = new MenuTemplate[1];
		
		this.menuGroup[0] = new NavigationMenu();
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
	}
	
	public void open()
	{
		this.activeMenu = this.menuGroup[0];
		this.menuOpen = true;
		this.activeMenu.setCanvasSize();
	}
	
	public void close()
	{
		this.menuOpen = false;
		this.driver.getCanvas().fullScreen();
	}
	
	public void runMenu()
	{
		if(this.menuOpen)
		{
			this.activeMenu.drawMenu();
		}
	}
	
}
