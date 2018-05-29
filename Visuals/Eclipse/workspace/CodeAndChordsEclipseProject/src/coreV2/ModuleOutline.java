package coreV2;

/**
 * 		Danny Mahota
 * 		5/28/2018
 * 		coreV2.ModuleOutline
 * 
 * 		Class Overview:
 * 			This abstract class serves as an outline for all new modules.  Because the
 * 		constructor starts the ModuleDriver, all that is left for the module creator to
 * 		do is implement the moduleSetup() and moduleDraw() methods.
 */

public abstract class ModuleOutline 
{
	/**
	 * Stores an instance of the ModuleDriver so that the child class has access to all
	 * of the get and set methods associated with the driver
	 */
	protected ModuleDriver driver;
	
	/**
	 * Constructor that starts the module driver.  By starting the driver, a PApplet will
	 * be created and the moduleSetup() and moduleDraw() functions in the child class will
	 * be called with respect to the setup() and draw() functions associated with the PApplet
	 */
	public ModuleOutline()
	{
		ModuleDriver.startModuleDriver(this);
	}
	
	/**
	 * A method that the driver uses to set the class variable driver
	 * @param driver
	 */
	public void setDriver(ModuleDriver driver)
	{
		this.driver = driver;
	}
	
	/**
	 * abstract method that is called by the driver once at the end of the 
	 * PApplet setup method
	 */
	public abstract void moduleSetup();
	
	/**
	 * abstract method that is called by the driver once at the end of every
	 * draw() loop
	 */
	public abstract void moduleDraw();
	
}
