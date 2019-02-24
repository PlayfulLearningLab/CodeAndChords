package coreV2_visuals;

import controlP5.ControlEvent;
import controlP5.Controller;
import controlP5.Slider;
import coreV2.ModuleDriver;
import coreV2.Visual;

public class PitchColorVisual extends Visual {
	

	public PitchColorVisual(ModuleDriver moduleDriver) 
	{
		super(moduleDriver, "Pitch Color");
		
		
		this.addSliders();
		
		this.cp5.getGroup(this.name).setVisible(true);
	}
	
	private void addSliders()
	{
		this.controllers = new String[] {"transition", "attack", "release"};
		this.labels = new String[] {"Transition Time (ms)", "Attack Time (ms)", "Release Time (ms)"};
		
		for(int i = 0; i < this.labels.length; i++)
		{
			this.cp5.addLabel(this.labels[i]).setTab(this.tabName);
		}
		
		this.cp5.addSlider("transition")
		.setMin(30)
		.setMax(1500)
		.setDecimalPrecision(0)
		.setValue(500)
		.setTab(this.tabName)
		.getCaptionLabel().setVisible(false);
		
				
		this.cp5.addSlider("attack")
		.setMin(30)
		.setMax(1500)
		.setDecimalPrecision(0)
		.setValue(500)
		.setTab(this.tabName)
		.getCaptionLabel().setVisible(false);
		
		this.cp5.addSlider("release")
		.setMin(30)
		.setMax(1500)
		.setDecimalPrecision(0)
		.setValue(500)
		.setTab(this.tabName)
		.getCaptionLabel().setVisible(false);

	}

	@Override
	public void controlEvent(ControlEvent theEvent) 
	{
		
	}

	@Override
	public void drawVisual() 
	{
		
	}

	@Override
	public int getNumControllers() 
	{
		return this.controllers.length;
	}

}
