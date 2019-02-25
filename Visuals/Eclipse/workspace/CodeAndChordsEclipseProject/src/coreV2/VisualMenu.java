package coreV2;

import java.awt.Color;

import controlP5.ControlEvent;
import controlP5.Controller;
import controlP5.ScrollableList;
import coreV2_visuals.PitchColorVisual;

public class VisualMenu extends MenuTemplate {
	
	private final float[][] 	controllerXY;
	private final int[]			controllerWH;
	
	private final float[][]		labelXY;
	
	private Visual[]			visuals;
	private int					curVisual;

	public VisualMenu(ModuleDriver driver) 
	{
		super("Visual Menu", driver, true);
		
		float[][] xy = new float[6][2];
		float[][] lxy = new float[6][2];
		
		for(int i = 0; i < xy.length; i++)
		{
			xy[i][0] = (this.parent.width/3)/10;
			xy[i][1] = this.parent.height/3 + 30 + 60*i;
			
			lxy[i][0] = xy[i][0];
			lxy[i][1] = xy[i][1] - 15;
		}
		
		this.controllerXY = xy;
		this.controllerWH = new int[] {this.parent.width/3*4/5, 25};
		this.labelXY = lxy;
		
		this.makeVisualDropdown();
		
		this.visuals = new Visual[0];
		
		this.addVisual(new PitchColorVisual(this.driver));
		this.curVisual = 0;
		this.controlP5.getController("VisualSelect").setValue(0);
		
		this.parent.registerMethod("pre", this);
		
	}
	
	public void pre()
	{
		this.parent.fill(0);
		this.driver.getCanvas().background();
		this.visuals[this.curVisual].drawVisual();
	}
	
	private void makeVisualDropdown()
	{
		this.controlP5.addScrollableList("VisualSelect")
		.setPosition(30, this.parent.height * 1/9)
		.setWidth(250)
		.setBarHeight(30)
		.setItemHeight(30)
		.setItems(new String[] {})
		.setValue(0)
		.close()
		.setTab(this.getMenuTitle());
	}
	
	public void addVisual(Visual visual)
	{
		if(visual.equals(null)) throw new IllegalArgumentException("Visual object is null");
		
		Visual[] oldList = this.visuals;
		this.visuals = new Visual[this.visuals.length + 1];
		
		for(int i = 0; i < this.visuals.length - 1; i++)
		{
			this.visuals[i] = oldList[i];
		}
		
		this.visuals[this.visuals.length - 1] = visual;
		
		((ScrollableList)this.controlP5.getController("VisualSelect")).addItems(new String[] {visual.getName()});
	}
	
	private void formatControllers(Visual visual)
	{
		Controller controller;
		
		for(int i = 0; i < visual.getNumControllers(); i++)
		{
			controller = this.controlP5.getController(visual.getControllerName(i));
			
			controller.setSize(this.controllerWH[0], this.controllerWH[1]);
			controller.setPosition(this.controllerXY[i][0], this.controllerXY[i][1]);
		}
	}
	
	private void formatLabels(Visual visual)
	{
		Controller controller;
		
		for(int i = 0; i < visual.getNumControllers(); i++)
		{
			controller = this.controlP5.getController(visual.getLabelName(i));
			
			controller.setPosition(this.labelXY[i][0], this.labelXY[i][1]);
			controller.setSize(this.controllerWH[0], this.controllerWH[1]);
		}
	}
	
	public void controlEvent(ControlEvent theEvent)
	{
		if(theEvent.getName() == "VisualSelect")
		{
			this.visuals[this.curVisual].hide();
			this.curVisual = (int) theEvent.getValue();
			this.formatControllers(this.visuals[this.curVisual]);
			this.formatLabels(this.visuals[this.curVisual]);
			this.visuals[this.curVisual].show();
		}
	}
	
	

	@Override
	public void sliderEvent(int id, float val) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buttonEvent(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void colorWheelEvent(int id, Color color) {
		// TODO Auto-generated method stub
		
	}

}
