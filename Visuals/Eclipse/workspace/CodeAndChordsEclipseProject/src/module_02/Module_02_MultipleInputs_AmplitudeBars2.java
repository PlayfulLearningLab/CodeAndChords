package module_02;

import processing.core.*;
import core.Module;
import core.ModuleMenu;
import core.input.MicrophoneInput;
import net.beadsproject.beads.core.AudioContext;

public class Module_02_MultipleInputs_AmplitudeBars2 extends Module
{
	public static void main(String[] args) 
	{
		PApplet.main("module_02.Module_02_MultipleInputs_AmplitudeBars2");
	} // main
	
	public void settings()
	{
		fullScreen();
	} // settings

	public void setup()
	{
		fill(255,255,255); 
		
		this.input	= new MicrophoneInput(16, new AudioContext(), true, this);
		this.totalNumInputs	= this.input.getAdjustedNumInputs();
		this.curNumInputs	= 10;

	}
	public void draw()
	{
		background(0);
		int x;
		int x2;
		int y;
		int s1;
		int s2;
		int p;
		
		x = 200;
		x2 = 350;
		y = 100;
		s1 = 100;
		p = 4; //This is used to adjust the sensitivity of the getAmplitude() method 
		s2 = (int) (this.input.getAmplitude())/p;
		
		
		for(int i=0; i<5; i++) //This will draw 5 downward growing rectangles
		{
			if(s2>100) //This will cause s2 to not grow past the desired point
			{
				s2 = 300;
			}
			rectMode(CORNER); //This is used to draw the rectangle from the upper left corner 
			//rect(x,y,s1,500+s2); 
			this.moduleDriver.getCanvas().rect((int)(this.parent.width/2 + this.);
			x = x + 300;
		}
		
		for(int j=0; j<5; j++) //This will draw 5 upward growing rectangles
		{
		
			if(s2>100) //This will cause s2 to not grow past the desired point
			{
				s2 = 200;
			}
			rectMode(CORNERS); //This is used to draw the rectangle from the upper left AND lower right corner
			//rect(x2,300-s2,x2+100,900); 
			this.moduleDriver.getCanvas().rect((int)(this.parent.width/2 + this.rect);
			x2 = x2 + 300;
		}
	}

	@Override
	public String[] getLegendText() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
