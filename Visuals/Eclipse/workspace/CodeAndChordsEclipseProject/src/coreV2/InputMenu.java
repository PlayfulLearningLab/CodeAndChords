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

	public InputMenu(ModuleDriver driver, InputHandler inputHandler) 
	{
		super("Input Menu", driver, true);
						
		this.controlP5.addScrollableList("realTimeInput")
		.setPosition(50, 100)
		.setWidth(200)
		.setBarHeight(30)
		.setItemHeight(30)
		.setItems(new String[] {"none"})
		.close()
		.setTab(this.getMenuTitle());
		
		this.controlP5.addScrollableList("playableInput")
		.setPosition(350, 100)
		.setWidth(200)
		.setBarHeight(30)
		.setItemHeight(30)
		.setItems(new String[] {"none"})
		.close()
		.setTab(this.getMenuTitle());
				
		System.out.println("String value:  " + this.controlP5.getController("playableInput").getStringValue());
	}
	
	public void addRealTimeInput(String inputType)
	{
		((ScrollableList) this.controlP5.get("realTimeInput"))
		.addItem(inputType, ((ScrollableList) this.controlP5.get("realTimeInput")).getItems().size());
	}
	
	public void addPlayableInput(String inputType)
	{
		((ScrollableList) this.controlP5.get("playableInput"))
		.addItem(inputType, ((ScrollableList) this.controlP5.get("playableInput")).getItems().size());
	}

	@Override
	public void controlEvent(ControlEvent theEvent)
	{
		super.controlEvent(theEvent);
		
		System.out.println("EVENT!!!");
		
		if(theEvent.getName() == "realTimeInput")
		{
			System.out.println("Current Real Time Input: " + this.getCurRealTimeInput());
			//this.inputHandler.setRealTimeInput(this.getCurRealTimeInput());
		}
		
		if(theEvent.getName() == "playableInput")
		{
			System.out.println("Current Playable Input: " + this.getCurPlayableInput());
			//this.inputHandler.setPlayableInput(this.getCurPlayableInput());
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

	
	public String getCurRealTimeInput()
	{
		ScrollableList controller = (ScrollableList) this.controlP5.getController("realTimeInput");
		int val = (int) controller.getValue();
		
		String itemText = controller.getItem(val).toString();
		
		int textStart = itemText.indexOf("text=") + 5;
		int textEnd = itemText.indexOf(',', textStart);
		
		String listText = itemText.substring(textStart, textEnd);

		return listText;
	}
	
	public String getCurPlayableInput()
	{
		ScrollableList controller = (ScrollableList) this.controlP5.getController("playableInput");
		int val = (int) controller.getValue();
		
		String itemText = controller.getItem(val).toString();
		
		int textStart = itemText.indexOf("text=") + 5;
		int textEnd = itemText.indexOf(',', textStart);
		
		String listText = itemText.substring(textStart, textEnd);

		return listText;
	}
	
	public ControlP5 getCP5()
	{
		return this.getControlP5();
	}

}
