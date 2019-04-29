package coreV2;

import controlP5.ControlEvent;
import controlP5.ControlListener;
import controlP5.ControlP5;
import controlP5.ControllerInterface;
import controlP5.Toggle;

/**
 * Controls the opening, closing, and switching between all menus.
 * 
 * @author Danny Mahota
 *
 */
public class MenuGroup implements ControlListener
{
	private ModuleDriver		driver;

	private CanvasMenu			canvasMenu;
	
	private MenuTemplate[] 		menuGroup;

	private MenuTemplate 		activeMenu;
	

	public MenuGroup(ModuleDriver driver)
	{
		this.driver = driver;
		driver.getCP5().addListener(this);

		this.menuGroup = new MenuTemplate[0];

		this.canvasMenu = new CanvasMenu(driver);
		this.activeMenu = this.canvasMenu;
		
		this.addMenu(new InputHandler(driver));
		this.addMenu(new ColorMenu(driver));
		this.addMenu(new VisualMenu(driver));

		this.driver.getCP5().controlWindow.setPositionOfTabs(this.driver.getParent().width/3 + 50, 15);
	}

	public void addMenu(MenuTemplate newMenu)
	{	
		MenuTemplate[] newGroup = new MenuTemplate[this.menuGroup.length + 1];

		for(int i = 0; i < this.menuGroup.length; i++)
		{
			newGroup[i] = this.menuGroup[i];
		}

		newGroup[this.menuGroup.length] = newMenu;
		this.menuGroup = newGroup;

		if(this.activeMenu.equals(this.canvasMenu))
		{
			this.hideTabs();
		}
	}

	public MenuTemplate[] getMenus()
	{
		return this.menuGroup;
	}

	public MenuTemplate getActiveMenu()
	{
		return this.activeMenu;
	}
	
	public ColorMenu getColorMenu()
	{
		return (ColorMenu) this.menuGroup[1];
	}

	public boolean canvasMenuActive()
	{
		boolean val = false;

		if(this.activeMenu.equals(this.canvasMenu)) 
		{
			val = true;
		}

		return val;
	}

	public void open()
	{
		if(this.menuGroup.length == 0) throw new IllegalArgumentException("There are no menus set");

		this.activeMenu = this.menuGroup[0];
		this.activeMenu.setCanvasSize();
		
		if(this.activeMenu.isTab())
		{
			this.driver.getCP5().getTab(this.activeMenu.getMenuTitle()).setActive(true);
		}

		this.showTabs();
	}

	public void switchTo(int menuNumber)
	{
		if(menuNumber > this.menuGroup.length) throw new IllegalArgumentException ("Invalid menuNumber");

		this.activeMenu = this.menuGroup[menuNumber];
		this.activeMenu.setCanvasSize();
	}

	public void close()
	{
		this.driver.getCanvas().fullScreen();
		
		if(this.activeMenu.isTab())
		{
			this.driver.getCP5().getTab(this.activeMenu.getMenuTitle()).setActive(false);
		}
		
		this.activeMenu = this.canvasMenu;

		this.hideTabs();
	}

	public void hideTabs()
	{
		for(int i = 0; i < this.menuGroup.length; i++)
		{
			if(this.menuGroup[i].isTab())
				this.driver.getCP5().getTab(this.menuGroup[i].getMenuTitle()).hide();
		}
	}

	public void showTabs()
	{
		for(int i = 0; i < this.menuGroup.length; i++)
		{
			if(this.menuGroup[i].isTab())
				this.driver.getCP5().getTab(this.menuGroup[i].getMenuTitle()).show();
		}
	}

	@Override
	public void controlEvent(ControlEvent theEvent) 
	{
		if(theEvent.isController())
		{

			if(theEvent.getController().getName() == "hamburger")
			{
				int	hamburgerX		= 10;
				int	hamburgerY		= 13;
				
				if(this.canvasMenuActive())
				{
					this.open();
					theEvent.getController().setPosition(this.driver.getParent().width/3 + 16, 23);
				}
				else
				{
					this.close();
					theEvent.getController().setPosition(hamburgerX, hamburgerY);
				}
			}

			if(theEvent.getController().getName() == "play")
			{
				if(this.driver.getCP5().getController("pause").isVisible())
				{
					this.driver.getCP5().getController("pause").hide();
				}
				else
				{
					if(((Toggle) this.driver.getCP5().getController("pause")).getBooleanValue())
					{
						((Toggle) this.driver.getCP5().getController("pause")).toggle();
					}

					this.driver.getCP5().getController("pause").show();
				}
			}


		}

		if(theEvent.isTab())
		{
			String tabName = theEvent.getTab().getName();

			for(int i = 0; i < this.menuGroup.length; i++)
			{
				if(tabName == this.menuGroup[i].getMenuTitle())
				{
					this.switchTo(i);
				}
			}
		}

	}

}
