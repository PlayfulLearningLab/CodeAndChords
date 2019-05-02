package module_02;

import processing.core.*;
import core.Module;
import core.ModuleMenu;
import core.input.MicrophoneInput;
import net.beadsproject.beads.core.AudioContext;

public class Module_02_MultipleInputs_AmplitudeSquares extends Module{
	
	public static void main(String[] args) 
	{
		PApplet.main("module_02.Module_02_MultipleInputs_AmplitudeSquares");
	} // main
	
	public void settings()
	{
		fullScreen();
		//size(925, 520);
	} // settings

	public void setup()
	{
		fill(10,20,40);
	}
	public void draw()
	{
		background(0);
		int r;
		int c;
		r = 0;
		c = 0;
		for(int j=0; j<10; j++)
		{
			for(int i=0; i<20; i++)
			{
				rect(r,c,width/20,width/20);
				r = r + width/20;
				if(r==width || c==height)
				{
					r = 0;
					c = c+ height/10;
				}
			}
		}
	}

	@Override
	public String[] getLegendText() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
