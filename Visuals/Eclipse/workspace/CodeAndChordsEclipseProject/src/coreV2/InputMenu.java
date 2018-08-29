package coreV2;

import java.awt.Color;
import java.util.List;

import controlP5.ControlEvent;
import controlP5.ControlListener;
import controlP5.ControlP5;
import controlP5.Controller;
import controlP5.ScrollableList;

public class InputMenu extends MenuTemplate
{
	private int 	pianoThreshold;
	private int		forteThreshold;
		
	public InputMenu(ModuleDriver driver) 
	{
		super("Input Menu", driver, true);
				
		this.controlP5.addScrollableList("realTimeInput")
		.setPosition(50, 100)
		.setWidth(200)
		.setBarHeight(30)
		.setItemHeight(30)
		.setItems(new String[] {InputHandler.typeNames[0]})
		.setValue(0)
		.close()
		.setTab(this.getMenuTitle());
		
		this.controlP5.addScrollableList("playableInput")
		.setPosition(150, 100)
		.setWidth(200)
		.setBarHeight(30)
		.setItemHeight(30)
		.addItems(new String[] {"none"})
		.setValue(0)
		.close()
		.setTab(this.getMenuTitle());
	}
	
	public void addInputType(int inputTypeNumber)
	{
		if(this.driver.getInputHandler().getInput(inputTypeNumber).isRealTime())
		{
			ScrollableList list = (ScrollableList) this.controlP5.getController("realTimeInput");
			list.addItem(InputHandler.typeNames[inputTypeNumber], list.getItems().size() );
			
		}
		else
		{
			ScrollableList list = (ScrollableList) this.controlP5.getController("playableInput");
			list.addItem(InputHandler.typeNames[inputTypeNumber], list.getItems().size() );
		}
	}

	@Override
	public void controlEvent(ControlEvent theEvent)
	{
		super.controlEvent(theEvent);
		
		System.out.println("Control Event");
		
		if(theEvent.getController().getName() == "inputType");
		{
			System.out.println("Setting input type");
			this.driver.getInputHandler().setCurInput(theEvent.getStringValue());
		}
	}

	@Override
	public void sliderEvent(int id, float val) 
	{
		
	}

	@Override
	public void buttonEvent(int id) 
	{
		
	}

	@Override
	public void colorWheelEvent(int id, Color color) 
	{
		
	}
	
	public int getPianoThreshold()
	{
		return this.pianoThreshold;
	}
	
	public int getForteThreshold()
	{
		return this.forteThreshold;
	}

}
