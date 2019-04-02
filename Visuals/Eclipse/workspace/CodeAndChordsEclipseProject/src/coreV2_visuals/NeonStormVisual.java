package coreV2_visuals;

import controlP5.ControlEvent;
import coreV2.ModuleDriver;
import coreV2.Visual;

public class NeonStormVisual extends Visual {

	private int[][]		clouds; // [cloud][x, y, alpha]

	public NeonStormVisual(ModuleDriver moduleDriver) 
	{
		super(moduleDriver, "Neon Storm");

		this.clouds = new int[100][3];
		for(int i = 0; i < this.clouds.length; i++)
		{
			this.clouds[i][0] = (int) (Math.random()*this.parent.width);
			this.clouds[i][1] = (int) (Math.random()*this.parent.height/5);
			this.clouds[i][2] = (int) (Math.random()*100);
		}

	}

	@Override
	public void controlEvent(ControlEvent theEvent) 
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void drawVisual() 
	{
		int[][] note = this.moduleDriver.getInputHandler().getAllMidiNotes();

		for(int input = 0; input < this.moduleDriver.getInputHandler().getNumChannels(); input++)
		{
			if(note[input][1] != -1)
			{
				int[] color = this.moduleDriver.getColorMenu().getColorSchemes()[0].getPitchColor(note[input][0]%12);
				this.parent.stroke(color[0], color[1], color[2]);
				this.parent.strokeWeight(1);

				for(int i = 0; i < 30; i++)
				{
					if(Math.random() * 100 < note[input][1])
					{
						int rand = (int) (Math.random()*this.parent.width);
						this.moduleDriver.getCanvas().line(rand, 0, rand, this.parent.height);
					}
				}
			}//if
		}
		



		this.parent.noStroke();
		int rectHeight = this.parent.height/(5*10);
		for(int i = 0; i < 10; i++)
		{
			this.parent.fill(0, 255 - 20*i);
			this.moduleDriver.getCanvas().rect(0, i*rectHeight, this.parent.width, rectHeight);
		}


		for(int i = 0; i < this.clouds.length; i++)
		{
			this.clouds[i][0] += (int) (Math.random()*10 - 5);
			this.clouds[i][1] += (int) (Math.random()*4 - 2);
			this.clouds[i][2] += (int) (Math.random()*6 - 3);

			if(this.clouds[i][0] > this.parent.width || this.clouds[i][0] < 0)
				this.clouds[i][0] = (int) (this.parent.width/4 + (Math.random() * this.parent.width/2));

			if(this.clouds[i][1] > this.parent.height/5 || this.clouds[i][1] < 0)
				this.clouds[i][1] = (int) (this.parent.height/20 + (Math.random() * this.parent.height/10));

			if(this.clouds[i][2] > 100 || this.clouds[i][2] <= 0)
				this.clouds[i][2] = (int) (Math.random() * 40);

			this.parent.fill(180, this.clouds[i][2]);
			this.parent.noStroke();

			this.moduleDriver.getCanvas().ellipse(this.clouds[i][0], this.clouds[i][1], 120, 120);
		}

	}

	@Override
	public int getNumControllers() 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getControllerName(int controllerNum) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLabelName(int controllerNum) 
	{
		// TODO Auto-generated method stub
		return null;
	}

}
