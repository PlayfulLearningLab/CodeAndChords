package module_01_05;

import coreV2.*;
import processing.core.PApplet;

public class Module_01_05_PolyphonicMidi extends PApplet
{
	private ModuleDriver 	driver;
	private InputHandler 	inputHandler;
	private Canvas			canvas;
	
	public static void main(String[] args)
	{
		PApplet.main("module_01_05.Module_01_05_PolyphonicMidi");
	}

	public void settings()
	{
		this.size(925, 520);
	}

	public void setup()
	{
		this.driver = new ModuleDriver(this);

		this.canvas = this.driver.getCanvas();

		MenuGroup menus = this.driver.getMenuGroup();
		this.inputHandler = this.driver.getInputHandler();
				
		this.noStroke();
	}

	public void draw()
	{
		int[][] activeNotes = this.inputHandler.getPolyMidiNotes();
		int numNotes = activeNotes.length;

		if(numNotes > 0)
		{
			ColorScheme cs = this.driver.getMenuGroup().getColorMenu().getColorSchemes()[0];
			
			for(int i = 0; i < numNotes; i++)
			{
				int[] rgb = cs.getPitchColor(activeNotes[i][0]);
				
				this.fill(rgb[0], rgb[1], rgb[2]);
				
				
				this.canvas.rect(	i * (this.width/numNotes), 
									0, 
									this.width/numNotes, 
									this.height);
			}
			
			this.canvas.rect(	(numNotes-1) * (this.width/numNotes) + (this.width/numNotes), 
								0, 
								5, 
								this.height);
		}
		else
		{
			this.fill(0,0,0);
			this.canvas.background();
			
		}


	}//draw()
}
