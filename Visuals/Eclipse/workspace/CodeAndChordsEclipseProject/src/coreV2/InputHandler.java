package coreV2;

import java.util.ArrayList;

import core.input.Input;
import core.input.MidiStreamInput;
import core.input.MusicalInput;
import core.input.RealTimeInput;
import core.input.RecordedInput;
import net.beadsproject.beads.core.AudioContext;
import processing.core.PApplet;

public class InputHandler 
{
	private ModuleDriver			driver;

	//Controls the switching between inputs
	private	MusicalInput[]			inputs;
	private	int						curInput;	// 0 - RealTimeInput; 1 - PlayableInput; 2  MidiStream
	
	//For real time input control
	private int						totalNumRealTimeInputs;
	private int[]					activeRealTimeInputs;


	//Static variables
	public	static final int		REAL_TIME_INPUT	= 0;
	public	static final int		PLAYABLE_INPUT	= 1;
	public  static final int		MIDI_STREAM_INPUT = 2;
	
	public static final String[] typeNames = new String[] {	"Real Time Input",
															"Playable Input",
															"MIDI Stream Input"};
	
	
	/**
	 * Constructor
	 * 
	 * @param parent
	 */
	public InputHandler(ModuleDriver driver)
	{
		System.out.println("InputHandler created!  Defaulting to single RealTimeInput.");
		
		this.driver = driver;
		
		this.inputs			= new MusicalInput[3];
		
		this.curInput = InputHandler.REAL_TIME_INPUT;
		
		this.inputs[InputHandler.REAL_TIME_INPUT] = new RealTimeInput(1, new AudioContext(), false, this.driver.getParent());
		
		this.totalNumRealTimeInputs = 1;
		this.activeRealTimeInputs = new int[] {0};
				
	}

	public void setRealTimeInput(RealTimeInput realTimeInput)
	{
		// Error checking:
		if(realTimeInput == null)	{
			throw new IllegalArgumentException("InputHandle.setRealTimeInput: RealTimeInput parameter is null.");
		}

		this.inputs[0]		= realTimeInput;
	}

	public void setPlayableInput(Input playableInput)
	{
		this.inputs[1]		= playableInput;
	}
	
	public void setMidiStreamInput(MidiStreamInput midiInput)
	{
		this.inputs[2] 		= midiInput;
	}
	
	public void setCurInput(int inputTypeNum)
	{
		if(inputTypeNum < 0 || inputTypeNum > 2) throw new IllegalArgumentException("that is not a valid input number");
		
		if(inputTypeNum == InputHandler.REAL_TIME_INPUT)  this.useRealTimeInput();
		else if(inputTypeNum == InputHandler.PLAYABLE_INPUT)  this.useRecordedInput();
		else if(inputTypeNum == InputHandler.MIDI_STREAM_INPUT)  this.useMidiStreamInput();
		
		this.driver.updateNumInputs();
	}
	
	public void setCurInput(String inputTypeName)
	{
		for(int i = 0; i < InputHandler.typeNames.length; i++)
		{
			if(inputTypeName == InputHandler.typeNames[i])
			{
				this.curInput = i;
			}
		}
		
		this.driver.updateNumInputs();
	}

	public void useRealTimeInput()
	{
		if(this.inputs[InputHandler.REAL_TIME_INPUT] == null)
		{
			throw new IllegalArgumentException("RealTimeInput is null");
		}

		this.curInput	= InputHandler.REAL_TIME_INPUT;
		
		this.driver.updateNumInputs();
	}

	public void useRecordedInput()
	{
		if(this.inputs[InputHandler.PLAYABLE_INPUT] == null)
		{
			throw new IllegalArgumentException("recorded input is null");
		}

		this.curInput	= InputHandler.PLAYABLE_INPUT;
		
		this.driver.updateNumInputs();
	}
	
	public void useMidiStreamInput()
	{
		if(this.inputs[InputHandler.MIDI_STREAM_INPUT] == null)
		{
			this.inputs[InputHandler.MIDI_STREAM_INPUT] = new MidiStreamInput();
		}

		this.curInput = InputHandler.MIDI_STREAM_INPUT;
		
		this.driver.updateNumInputs();
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
		
		this.driver.updateNumInputs();
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
		if(this.curInput != InputHandler.MIDI_STREAM_INPUT) throw new IllegalArgumentException("Must be a MIDI input to use getAllMidiNotes()");
		
		return ((MidiStreamInput) this.inputs[2]).getAllNotesAndAmps();
	}
	
	public int getMidiNote()
	{
		int defaultInputNum = 0;
		
		if(this.curInput == InputHandler.REAL_TIME_INPUT)
		{
			defaultInputNum = this.activeRealTimeInputs[0];
		}
		
		return this.getMidiNote(defaultInputNum);
	}
	
	public int getMidiNote(int inputNum)
	{
		return this.inputs[this.curInput].getMidiNote();
	}
	
	public float getAmplitude()
	{
		int defaultInputNum = 0;
		
		if(this.curInput == InputHandler.REAL_TIME_INPUT)
		{
			defaultInputNum = this.activeRealTimeInputs[0];
		}
		
		return this.getAmplitude(defaultInputNum);
	}

	public float getAmplitude(int inputNum)
	{
		return this.inputs[this.curInput].getAmplitude();
	}
	
	public int getCurNumInputs()
	{
		int num = 0;
		
		if(this.curInput == InputHandler.MIDI_STREAM_INPUT)
		{
			num = 1;
		}
		else
		{
			num = ((Input)  this.inputs[this.curInput]).getAdjustedNumInputs();
		}
		
		return num;
	}
	
	public String getCurInputType()
	{
		String type = "";
		
		if(this.curInput == InputHandler.REAL_TIME_INPUT)		type = "Real Time Input";
		if(this.curInput == InputHandler.PLAYABLE_INPUT)		type = "Playable Input";
		if(this.curInput == InputHandler.MIDI_STREAM_INPUT)	type = "MIDI Stream Input";
		
		return type;
	}
	
	public boolean hasType(int typeNumber)
	{
		boolean val = false;
		
		if(this.inputs[typeNumber] != null)
		{
			val = true;
		}
		
		return val;
	}
	
	public MusicalInput getInput(int inputNum)
	{
		if(!this.hasType(inputNum)) throw new IllegalArgumentException("Input handler does not have an input of this type.");
		
		return this.inputs[inputNum];
	}

}
