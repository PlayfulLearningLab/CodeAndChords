package coreV2;

public class MenuGroup 
{

	private Menu[] 		menuGroup;
	private Canvas 		canvas;
	
	private	ModuleDriver	moduleDriver;
	
	private boolean		menuOpen;
	
	private Menu 		activeMenu;
	
	public MenuGroup(Canvas canvas, ModuleDriver moduleDriver)
	{
		this.menuGroup = new Menu[1];
		this.canvas = canvas;
		
		this.moduleDriver	= moduleDriver;
		
		this.menuGroup[0] = new NavigationMenu(this.canvas);
		this.activeMenu = this.menuGroup[0];
	}
	
	public void addMenu(Menu menu)
	{
		menu.setCanvas(this.canvas);
		
		Menu[] newGroup = new Menu[this.menuGroup.length + 1];
		
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
		
		this.canvas.fullScreen();
	}
	
	public void runMenu()
	{
		if(this.menuOpen)
		{
			this.activeMenu.drawMenu();
			
		}
	}
	
}
