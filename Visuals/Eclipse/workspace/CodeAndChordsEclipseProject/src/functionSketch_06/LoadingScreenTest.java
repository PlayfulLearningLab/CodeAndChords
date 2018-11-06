package functionSketch_06;

import coreV2.LogoAnimation;
import coreV2.ModuleDriver;
import processing.core.PApplet;

public class LoadingScreenTest extends PApplet implements Runnable
{
	private ModuleDriver			driver;
	
	private LogoAnimation			logo;
	private boolean					settingUp;
		
	public static void main(String[] args)
	{
		PApplet.main("functionSketch_06.LoadingScreenTest");
	}
	
	public void settings()
	{
		this.size(925, 520);
	}
	
	public void setup()
	{
		System.out.println("starting setup");
		
		this.logo = new LogoAnimation(this);
		
		Thread thread = new Thread(this);
		thread.start();
	}
	
	@Override
	public void run() 
	{
		System.out.println("running");
		
		this.driver = new ModuleDriver(this);
		this.logo.startFade();
		this.settingUp = false;
	}
	
	public void draw()
	{
		if(this.driver != null)
		{
			this.fill(120, 10, 160);
			this.driver.getCanvas().background();
		}
			
	}

	
	
}
