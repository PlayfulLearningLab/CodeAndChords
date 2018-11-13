package coreV2;

import java.awt.Color;
import java.util.ArrayList;

import controlP5.ControlEvent;
import controlP5.ScrollableList;
import controlP5.Textlabel;
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

		
		//recorded inputs
		RecordedInput recInput1	= new RecordedInput(driver.getParent(), new String[] {	"6_Part_Scale1.wav", 
																						"6_Part_Scale2.wav", 
																						"6_Part_Scale3.wav", 
																						"6_Part_Scale4.wav"});

		recInput1.setInputName("4 Part Scale");
		this.addMusicalInput(recInput1);
		
		
		
		RecordedInput recInput2	= new RecordedInput(driver.getParent(), new String[] {	"WantingMemories_Melody.wav",
																						"WMBass_Later_Quiet.wav",
																						"WantingMemories_Alto.wav",
																						"WantingMemories_Soprano.wav",
																						"WMTenor_Medium.wav"});

		recInput2.setInputName("Wanting Memories");
		this.addMusicalInput(recInput2);
		
		this.controlP5.get("playableInput").setValue(0);
		
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
		
		if(theEvent.getName() == "play")
		{
			if(theEvent.getValue() == 1)
			{
				this.setUseRealTimeInput(false);
				((Input) this.getCurInput()).pause(false);
			}
			else
			{
				this.setUseRealTimeInput(true);
				
				for(int i = 0; i < this.playableInputs.length; i++)
				{
					if(this.playableInputs[i].getInputType() == "Recorded Input")
					{
						((Input) this.playableInputs[i]).pause(true);
					}
				}//for loop
					
			}//else
			
		}
		
		if(theEvent.getName() == "pause"  && !this.useRealTimeInput)
		{
			if(theEvent.getValue() == 1)
			{
				((Input) this.getCurInput()).pause(true);
			}
			else
			{
				((Input) this.getCurInput()).pause(false);
			}
		}
		
		if(theEvent.getName() == "playableInput")
		{
			if(this.controlP5.getController("play").getValue() == 1)
			{				
				this.controlP5.getController("play").setValue(0);
			}
			MusicalInput curPlayableInput = this.playableInputs[(int) this.controlP5.getController("playableInput").getValue()];
			if(!curPlayableInput.equals(null))
				((Textlabel) this.controlP5.get("playableInfo")).setText(this.makeInfoString(curPlayableInput));
		}
		
		if(theEvent.getName() == "realTimeInput")
		{

			MusicalInput curRealTimeInput = this.realTimeInputs[(int) this.controlP5.getController("realTimeInput").getValue()];
			if(!curRealTimeInput.equals(null))
				((Textlabel) this.controlP5.get("realTimeInfo")).setText(this.makeInfoString(curRealTimeInput));
		}
		

	}//ControlEvent

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

		inputList.addItem(  "" + musicalInput.getInputName() + "   ( " + musicalInput.getInputType() + " )", inputList.getItems().size());



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
		this.controlP5.addLabel("Real Time Inputs", 30, this.parent.height/3)
		.setTab(this.getMenuTitle());
		
		this.controlP5.addLabel("realTimeInfo")
		.setTab(this.getMenuTitle())
		.setMultiline(true)
		.setPosition(40, this.parent.height/3 + 60)
		.setSize(this.parent.width/3 - 50, this.parent.height/3 - 40)
		.setText("info here");
		
		this.controlP5.addScrollableList("realTimeInput")
		.setPosition(30, this.parent.height/3 + 15)
		.setWidth(250)
		.setBarHeight(30)
		.setItemHeight(30)
		.setItems(new String[] {})
		.setValue(0)
		.close()
		.setTab(this.getMenuTitle());
		
		this.controlP5.addLabel("Playable Inputs (Play Button)", 30, this.parent.height * 2/3)
		.setTab(this.getMenuTitle());
		
		this.controlP5.addLabel("playableInfo")
		.setTab(this.getMenuTitle())
		.setMultiline(true)
		.setPosition(40, this.parent.height*2/3 + 60)
		.setSize(this.parent.width/3 - 50, this.parent.height/3 - 40)
		.setText("info here");

		this.controlP5.addScrollableList("playableInput")
		.setPosition(30, this.parent.height * 2/3 + 15)
		.setWidth(250)
		.setBarHeight(30)
		.setItemHeight(30)
		.setItems(new String[] {})
		.setValue(0)
		.close()
		.setTab(this.getMenuTitle());
		
		this.controlP5.addScrollableList("Key Change")
		.setPosition(30, 70)
		.setWidth(100)
		.setBarHeight(30)
		.setItemHeight(30)
		.setItems(new String[] {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"})
		.setValue(0)
		.close()
		.setTab(this.getMenuTitle());
		
		
		this.controlP5.addScrollableList("Keys")
		.setPosition(200, 70)
		.setWidth(100)
		.setBarHeight(30)
		.setItemHeight(30)
		.setItems(new String[] {"Major", "Minor", "Chromatic", "Harmonic Minor", "Melodic Minor", "Major Pentatonic", "Minor Pentatonic"})
		.setValue(0)
		.close()
		.setTab(this.getMenuTitle());
	}
	
	private int[] getScale()
	{
		int[] currentScale;
		ScrollableList controller1 = (ScrollableList) this.controlP5.getController("Key Change");
		int key = (int) controller1.getValue();
		
		ScrollableList controller2 = (ScrollableList) this.controlP5.getController("realTimeInput");
		int scale = (int) controller2.getValue();
		
		if(scale == 0)//major
		{
			currentScale = new int[8];
			currentScale[0] = key;
			currentScale[1] = key + 2;
			currentScale[2] = key + 2;
			currentScale[3] = key + 1;
			currentScale[4] = key + 2;
			currentScale[5] = key + 2;
			currentScale[6] = key + 2;
			currentScale[7] = key + 1;
		}
		else if(scale == 1)//minor
		{
			currentScale = new int[8];
			currentScale[0] = key;
			currentScale[1] = key + 2;
			currentScale[2] = key + 1;
			currentScale[3] = key + 2;
			currentScale[4] = key + 2;
			currentScale[5] = key + 1;
			currentScale[6] = key + 2;
			currentScale[7] = key + 2;
		}
		else if(scale == 3)//harmonic minor
		{
			currentScale = new int[8];
			currentScale[0] = key;
			currentScale[1] = key + 2;
			currentScale[2] = key + 1;
			currentScale[3] = key + 2;
			currentScale[4] = key + 2;
			currentScale[5] = key + 1;
			currentScale[6] = key + 2;
			currentScale[7] = key + 1;
		}
		else if(scale == 4)//melodic minor
		{
			currentScale = new int[8];
			currentScale[0] = key;
			currentScale[1] = key + 2;
			currentScale[2] = key + 1;
			currentScale[3] = key + 2;
			currentScale[4] = key + 2;
			currentScale[5] = key + 2;
			currentScale[6] = key + 2;
			currentScale[7] = key + 1;
		}
		else if(scale == 5)//major pentatonic
		{
			currentScale = new int[6];
			currentScale[0] = key;
			currentScale[1] = key + 2;
			currentScale[2] = key + 2;
			currentScale[3] = key + 3;
			currentScale[4] = key + 2;
			currentScale[5] = key + 3;
		}
		else if(scale == 6)//minor pentatonic
		{
			currentScale = new int[6];
			currentScale[0] = key;
			currentScale[1] = key + 3;
			currentScale[2] = key + 2;
			currentScale[3] = key + 2;
			currentScale[4] = key + 3;
			currentScale[5] = key + 2;
		}
		else//chromatic
		{
			currentScale = new int[12];
			currentScale[0] = key;
			currentScale[1] = key + 1;
			currentScale[2] = key + 1;
			currentScale[3] = key + 1;
			currentScale[4] = key + 1;
			currentScale[5] = key + 1;
			currentScale[6] = key + 1;
			currentScale[7] = key + 1;
			currentScale[8] = key + 1;
			currentScale[9] = key + 1;
			currentScale[10] = key + 1;
			currentScale[11] = key + 1;
		}
		return currentScale;
	}
	
	private String makeInfoString(MusicalInput input)
	{
		String info = "";
		
		info += "Number of Inputs:   " + input.getTotalNumInputs() +"\n\n";
		info += "Polyphonic?         " + input.isPolyphonic() +"\n\n";
		
		return info;
	}

}
