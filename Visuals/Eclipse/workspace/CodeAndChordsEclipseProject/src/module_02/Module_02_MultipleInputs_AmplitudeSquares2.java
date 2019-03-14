package module_02;

import processing.core.*;
import core.Module;
import core.ModuleMenu;
import core.input.MicrophoneInput;
import net.beadsproject.beads.core.AudioContext;

public class Module_02_MultipleInputs_AmplitudeSquares2 extends Module{
	
	public static void main(String[] args) 
	{
		PApplet.main("module_02.Module_02_MultipleInputs_AmplitudeSquares2");
	} // main
	
	public void settings()
	{
		//size(300,300,P3D);
		fullScreen(P3D);
	} // settings

	public void setup()
	{
		fill(100,20,40);
		
		this.input	= new MicrophoneInput(16, new AudioContext(), true, this);
		this.totalNumInputs	= this.input.getAdjustedNumInputs();
		this.curNumInputs	= 10;

	}
	public void draw()
	{
		/*background(0);
		int x;
		int y;
		int s1;
		int s2;
		
		x = 100;
		y = 200;
		s1 = 300;
		s2 = (int) (this.input.getAmplitude());
		
		for(int j=0; j<2; j++)
		{
			for(int i=0; i<5; i++)
			{
			
				if( x >= width-100)
				{
					y = y + 400;
					x = 100;
				}
				if((int)this.input.getAmplitude()>300)
				{
					s2 = 300;
				}
			rect(x,y,s1,s2); 
		
				x = x + s1 + 50;
			}
		}*/
		Canvas.box(2,3,5);
	}

	@Override
	public String[] getLegendText() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
