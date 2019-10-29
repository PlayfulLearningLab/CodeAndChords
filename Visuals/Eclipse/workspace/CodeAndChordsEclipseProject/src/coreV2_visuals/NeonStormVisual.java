package coreV2_visuals;

import controlP5.ControlEvent;
import coreV2.ModuleDriver;
import coreV2.Visual;

public class NeonStormVisual extends Visual {

	private int[][]		clouds; // [cloud][x, y, alpha]
	
	private String[]	controllers;
	private String[]	labels;

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
		
		this.controllers = new String[] {"minCloudSize", "maxCloudSize", "minAlpha", "maxAlpha", "random"};
		this.labels = new String[] {"Min Cloud Size", "Max Cloud Size", "Min Alpha",
										"Max Alpha", "Randomness"};
		
		this.makeControls();

	}
	
	private void makeControls()
	{
		this.cp5.addSlider(this.controllers[0], 50, 200)
		.setValue(80)
		.getCaptionLabel().hide();
		this.cp5.addLabel(this.labels[0]);
		
		this.cp5.addSlider(this.controllers[1], 50, 200)
		.setValue(150)
		.getCaptionLabel().hide();
		this.cp5.addLabel(this.labels[1]);
		
		this.cp5.addSlider(this.controllers[2], 0, 100)
		.setValue(80)
		.getCaptionLabel().hide();
		this.cp5.addLabel(this.labels[2]);
		
		this.cp5.addSlider(this.controllers[3], 100, 255)
		.setValue(150)
		.getCaptionLabel().hide();
		this.cp5.addLabel(this.labels[3]);
		
		this.cp5.addSlider(this.controllers[4], 0, 100)
		.setValue(50)
		.getCaptionLabel().hide();
		this.cp5.addLabel(this.labels[4]);
	}

	@Override
	public void controlEvent(ControlEvent theEvent) 
	{
		

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
		int rectHeight = this.parent.height/(5*10);//50?
		for(int i = 0; i < 10; i++)
		{
			this.parent.fill(0, 255 - 20*i);
			this.moduleDriver.getCanvas().rect(0, i*rectHeight, this.parent.width, rectHeight);
		}
		
		int rand = (int) this.cp5.getController("random").getValue();


		for(int i = 0; i < this.clouds.length; i++)
		{
			this.clouds[i][0] += (int) (Math.random()*10*(rand/50) - 5);
			this.clouds[i][1] += (int) (Math.random()*4*(rand/50) - 2);
			this.clouds[i][2] += (int) (Math.random()*6*(rand/50) - 3);

			int maxA = (int) this.cp5.getController("maxAlpha").getValue();
			int minA = (int) this.cp5.getController("minAlpha").getValue();
			
			if(this.clouds[i][0] > this.parent.width || this.clouds[i][0] < 0)
				this.clouds[i][0] = (int) (this.parent.width/4 + (Math.random()*(rand/50) * this.parent.width/2));

			if(this.clouds[i][1] > this.parent.height/5 || this.clouds[i][1] < 0)
				this.clouds[i][1] = (int) (this.parent.height/20 + (Math.random()*(rand/50) * this.parent.height/10));

			if(this.clouds[i][2] > maxA || this.clouds[i][2] <= minA)
				this.clouds[i][2] = (int) ((minA+maxA)/2);

			this.parent.fill(180, this.clouds[i][2]);
			this.parent.noStroke();

			//int maxS = (int) this.cp5.getController("maxSize").getValue();
			//int minS = (int) this.cp5.getController("minSize").getValue();
			
			this.moduleDriver.getCanvas().ellipse(this.clouds[i][0], this.clouds[i][1], 120, 120);
		}

	}

	@Override
	public int getNumControllers() 
	{
		return this.controllers.length;
	}

	@Override
	public String getControllerName(int controllerNum) 
	{
		return this.controllers[controllerNum];
	}

	@Override
	public String getLabelName(int controllerNum) 
	{
		return this.labels[controllerNum];
	}

}
