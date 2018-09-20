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
	
	public static final String[] 	realTimeInputNames = new String[] {	"Microphone Input",
																		"MIDI Stream Input"};
	//Playable Inputs
	private	MusicalInput[]			playableInputs;

	public static final String[] 	playableInputNames = new String[] {	"Recorded Input"};
	
	//For real time input control
	private int						totalNumRealTimeInputs;
	private int[]					activeRealTimeInputs;

	
	/**
	 * Constructor
	 * 
	 * @param parent
	 */
	public InputHandler(ModuleDriver driver)
	{
		super("Input Menu", driver, true);
		this.makeControls();
						
		//Set up the real time inputs
		this.realTimeInputs	= new MusicalInput[2];
		
		this.setMicrophoneInput(new MicrophoneInput(1, new AudioContext(), false, this.driver.getParent()));
		this.setMidiStreamInput(new MidiStreamInput());	
		
		this.controlP5.get("realTimeInput").setValue(2);
		
		this.totalNumRealTimeInputs = 1;
		this.activeRealTimeInputs = new int[] {0};
		
		this.useRealTimeInput = true;
		
		//set up the playableInputs
		this.playableInputs = new MusicalInput[1];
		
			

		
	}
	
	
	public void setUseRealTimeInput(boolean useRealTimeInput)
	{
		this.useRealTimeInput = useRealTimeInput;
	}
	
	
	public void setMicrophoneInput(MicrophoneInput microphoneInput)
	{
		if(this.realTimeInputs[0] == null)
		{
			this.addRealTimeInput(InputHandler.realTimeInputNames[0]);
		}
		
		this.realTimeInputs[0] = microphoneInput;
	}
	
	public void setMidiStreamInput(MidiStreamInput midiStreamInput)
	{
		if(this.realTimeInputs[1] == null)
		{
			this.addRealTimeInput(InputHandler.realTimeInputNames[1]);
		}
		
		this.realTimeInputs[1] = midiStreamInput;
	}
	
	public void setRecordedInput(RecordedInput recordedInput)
	{
		if(this.playableInputs[0] == null)
		{
			this.addPlayableInput(InputHandler.playableInputNames[0]);
		}
		
		this.playableInputs[0] = recordedInput;
	}
	
	
	
	
	private MusicalInput getCurInput()
	{
		MusicalInput input = null;
		
		if(this.useRealTimeInput)
		{
			if(this.controlP5.getController("realTimeInput").getValue() - 1 != -1)
				input = this.realTimeInputs[(int) this.controlP5.getController("realTimeInput").getValue() - 1];
		}
		else
		{
			if(this.controlP5.getController("realTimeInput").getValue() - 1 != -1)
				input = this.playableInputs[(int) this.controlP5.getController("playableInput").getValue() - 1];
		}
		
		return input;
	}


	public void setActiveRealTimeInputs(int[] activeInputs)
	{
		if(activeInputs.length > this.totalNumRealTimeInputs)
		{
			throw new IllegalArgumentException("ModuleDriver.setActiveInputs: You passed in an array that is too big.  There are " + this.totalNumRealTimeInputs + " inputs total.");

		}

		for(int i = 0; i < activeInputs.length; i++)
		{
			if(activeInputs[i] > this.totalNumRealTimeInputs)
			{
				throw new IllegalArgumentException("One of your inputs does not exist. Input num " + activeInputs[i] + " is too high.");
			}
		}

		this.activeRealTimeInputs = activeInputs;

		this.driver.updateNumInputs(this.getCurNumInputs());
	}


	public int getCurNumRealTimeInputs()
	{
		return this.activeRealTimeInputs.length;
	}

	public int[] getActiveRealTimeInputs()
	{		
		return this.activeRealTimeInputs;
	}

	public int[][] getPolyMidiNotes()
	{
		return ((MidiStreamInput) this.realTimeInputs[1]).getAllNotesAndAmps();
	}

	public int getMidiNote()
	{
		int defaultInputNum = 0;

		if(this.useRealTimeInput)// && this.rtIndex == 0)
		{
			defaultInputNum = this.activeRealTimeInputs[0];
		}

		return this.getMidiNote(defaultInputNum);
	}

	public int getMidiNote(int inputNum)
	{
		int val = 0;
		
		if(this.getCurInput() != null)
			val = this.getCurInput().getMidiNote();
		
		return val;
	}

	public float getAmplitude()
	{
		int defaultInputNum = 0;

		if(this.useRealTimeInput)// && this.rtIndex == 0)
		{
			defaultInputNum = this.activeRealTimeInputs[0];
		}

		return this.getAmplitude(defaultInputNum);
	}

	public float getAmplitude(int inputNum)
	{
		int val = 0;
		
		if(this.getCurInput() != null)
			val = (int) this.getCurInput().getAmplitude();
		
		return val;
	}

	public int getCurNumInputs()
	{
		int num = 1;

		if(!(this.useRealTimeInput))// && this.rtIndex == 1))
		{
			num = ( (Input)this.getCurInput() ).getAdjustedNumInputs();
		}

		return num;
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
	
	private void makeControls()
	{
		this.controlP5.addScrollableList("realTimeInput")
		.setPosition(50, 100)
		.setWidth(200)
		.setBarHeight(30)
		.setItemHeight(30)
		.setItems(new String[] {"none"})
		.setValue(0)
		.close()
		.setTab(this.getMenuTitle());
		
		this.controlP5.addScrollableList("playableInput")
		.setPosition(350, 100)
		.setWidth(200)
		.setBarHeight(30)
		.setItemHeight(30)
		.setItems(new String[] {"none"})
		.setValue(0)
		.close()
		.setTab(this.getMenuTitle());
	}

}
