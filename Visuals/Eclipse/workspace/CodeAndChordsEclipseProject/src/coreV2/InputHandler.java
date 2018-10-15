package coreV2;

import java.awt.Color;
import java.util.ArrayList;

import controlP5.ControlEvent;
import controlP5.ScrollableList;
import core.input.Input;
import core.input.MidiStreamInput;
import core.input.MusicalInput;
import core.input.MicrophoneInput;
import core.input.RecordedInput;
import net.beadsproject.beads.core.AudioContext;
import processing.core.PApplet;

/*
 * Danny's TODO List:
 * 
 *   1: make all setter methods go through one method
 *   2: add a method that sets the scrollable lists to the right setting
 *   3: clean up moduleDriver
 */

public class InputHandler extends MenuTemplate
{
	//Using a the real time input or playable input?
	private boolean					useRealTimeInput;

	//Real Time Inputs
	private	MusicalInput[]			realTimeInputs;

	//Playable Inputs
	private	MusicalInput[]			playableInputs;




	/**
	 * Constructor
	 * 
	 * @param parent
	 */
	public InputHandler(ModuleDriver driver)
	{
		super("Input Menu", driver, true);
		this.makeControls();

		this.realTimeInputs = new MusicalInput[0];
		this.playableInputs = new MusicalInput[0];


		this.addMusicalInput(new MicrophoneInput(1, new AudioContext(), false, this.driver.getParent()));
		this.addMusicalInput(new MidiStreamInput());

		this.useRealTimeInput = true;

		this.controlP5.get("realTimeInput").setValue(1);


	}

	// Setter for useRealTimeInput boolean

	public void setUseRealTimeInput(boolean useRealTimeInput)
	{
		this.useRealTimeInput = useRealTimeInput;
	}




	//Stuff for dealing with input channels.  Needs some changes and different names





	// Get different note values

	private MusicalInput getCurInput()
	{
		MusicalInput curInput = null;

		if(this.useRealTimeInput)
		{
			curInput = this.realTimeInputs[(int) this.controlP5.getController("realTimeInput").getValue()];
		}
		else
		{
			curInput = this.playableInputs[(int) this.controlP5.getController("playableInput").getValue()];
		}
		
		return curInput;
	}

	public int getMidiNote()
	{
		MusicalInput curInput = this.getCurInput();		
		if(curInput == null) throw new IllegalArgumentException("Current input is null");
		
		return curInput.getMidiNote();
	}

	public float getAmplitude()
	{
		MusicalInput curInput = this.getCurInput();		
		if(curInput == null) throw new IllegalArgumentException("Current input is null");
		
		return curInput.getAmplitude();
	}
	

	public int[][] getPolyMidiNotes()
	{
		MusicalInput curInput = this.getCurInput();
		if(curInput == null) throw new IllegalArgumentException("Current input is null");
		
		int[][] midiNotes;
		
		if(curInput.isPolyphonic())
		{
			midiNotes = ((MidiStreamInput) curInput).getAllNotesAndAmps();
		}
		else
		{
			midiNotes = new int[1][2];
			midiNotes[0][0] = this.getMidiNote();
			midiNotes[0][1] = (int) this.getAmplitude();
		}
		
		return midiNotes;
	}




	@Override
	public void controlEvent(ControlEvent theEvent)
	{
		super.controlEvent(theEvent);

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

	public void addMusicalInput(MusicalInput musicalInput)
	{
		ScrollableList inputList = ((ScrollableList) this.controlP5.get("playableInput"));

		if(musicalInput.isRealTimeInput())
		{
			inputList = ((ScrollableList) this.controlP5.get("realTimeInput"));
		}

		inputList.addItem(musicalInput.getInputType(), inputList.getItems().size());



		MusicalInput[] oldList = this.playableInputs;
		MusicalInput[] newList;

		if(musicalInput.isRealTimeInput())
		{
			oldList = this.realTimeInputs;
		}

		newList = new MusicalInput[oldList.length + 1];

		for(int i = 0; i < oldList.length; i++)
		{
			newList[i] = oldList[i];
		}

		newList[oldList.length] = musicalInput;

		if(musicalInput.isRealTimeInput())
		{
			this.realTimeInputs = newList;
		}
		else
		{
			this.playableInputs = newList;
		}
		
	}

	private void makeControls()
	{
		this.controlP5.addScrollableList("realTimeInput")
		.setPosition(50, 100)
		.setWidth(200)
		.setBarHeight(30)
		.setItemHeight(30)
		.setItems(new String[] {})
		.setValue(0)
		.close()
		.setTab(this.getMenuTitle());

		this.controlP5.addScrollableList("playableInput")
		.setPosition(350, 100)
		.setWidth(200)
		.setBarHeight(30)
		.setItemHeight(30)
		.setItems(new String[] {})
		.setValue(0)
		.close()
		.setTab(this.getMenuTitle());
	}

}
