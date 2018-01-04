package smm_jan_2018;

import core.Module;
import core.ModuleMenu;
import core.input.RealTimeInput;
import processing.core.PApplet;

public class SMM_Demo extends Module {
	
	private	static	int	SCENE_CLAP			= 0;
	private	static	int	SCENE_RAINBOW_ROUND	= 1;
	private	static	int	SCENE_SOLOIST		= 2;
	private	static	int	SCENE_DRUM_VOCAL	= 3;
	private	static	int	SCENE_DUET			= 4;
	private	static	int	SCENE_QUARTET		= 5;
	private	int	curScene					= SMM_Demo.SCENE_CLAP;

	public static void main(String[] args) {
		PApplet.main("smm_jan_2018.SMM_Demo");
	}
	
	public void settings()
	{
		this.fullScreen();
	} // settings
	
	public void setup()
	{
		this.totalNumInputs	= 16;
		this.curNumInputs	= 1;
		this.input			= new RealTimeInput(this.totalNumInputs, false, this);
		
		this.menu	= new ModuleMenu(this, this, this.input, 12);
	} // setup
	
	public void draw()
	{
		
		
	} // draw
	
	public void keyPressed()
	{
		System.out.println("keyPressed! key = " + this.key);
	}
	
	@Override
	public String[] getLegendText() {
		// TODO Auto-generated method stub
		return null;
	}


}
