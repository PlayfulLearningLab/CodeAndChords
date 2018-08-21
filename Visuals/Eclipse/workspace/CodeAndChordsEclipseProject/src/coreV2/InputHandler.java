package coreV2;

import core.input.Input;
import core.input.MidiStreamInput;
import core.input.MusicalInput;
import core.input.RealTimeInput;
import core.input.RecordedInput;
import net.beadsproject.beads.core.AudioContext;
import processing.core.PApplet;

public class InputHandler 
{
	private static InputHandler		inputHandler;

	public	static final int		REAL_TIME_INPUT	= 0;
	public	static final int		PLAYABLE_INPUT	= 1;
	public  static final int		MIDI_STREAM_INPUT = 2;

	private	MusicalInput[]			inputs;
	private	int						inputType;	// 0 - RealTimeInput; 1 - PlayableInput; 2  MidiStream

	private int						totalNumRealTimeInputs;
	private int[]					activeRealTimeInputs;

	private InputHandler(PApplet parent)
	{
		System.out.println("InputHandler created!  Defaulting to single RealTimeInput.");
		
		this.inputs			= new MusicalInput[3];
		
		this.inputType = this.REAL_TIME_INPUT;
		
		this.inputs[this.REAL_TIME_INPUT] = new RealTimeInput(1, new AudioContext(), false, parent);

		this.totalNumRealTimeInputs = 1;
		this.activeRealTimeInputs = new int[] {0};
		
	}

	public static InputHandler makeInputHandler(PApplet parent)
	{
		if(InputHandler.inputHandler == null)
		{
			InputHandler.inputHandler = new InputHandler(parent);
		}

		return InputHandler.inputHandler;
	}
	
	public static InputHandler getInputHandler()
	{
		if(InputHandler.inputHandler == null)
		{
			throw new IllegalArgumentException("No InputHandler has been created yet.  "
												+ "Call 'makeInputHandler(PApplet parent)' to initialize the InputHandler");
		}
		
		return InputHandler.inputHandler;
	}

	public void setRealTimeInput(RealTimeInput realTimeInput)
	{
		// Error checking:
		if(realTimeInput == null)	{
			throw new IllegalArgumentException("InputHandle.setRealTimeInput: RealTimeInput parameter is null.");
		}

		this.inputs[0]		= realTimeInput;
		this.inputType	= InputHandler.REAL_TIME_INPUT;

	}

	public void setPlayableInput(Input playableInput)
	{
		this.inputs[1]		= playableInput;
		this.inputType	= InputHandler.PLAYABLE_INPUT;

	}

	public MusicalInput getRealTimeInput()
	{
		return this.inputs[InputHandler.REAL_TIME_INPUT];
	}

	public MusicalInput getPlayableInput()
	{
		return this.inputs[InputHandler.PLAYABLE_INPUT];
	}

	public void useRealTimeInput()
	{
		if(this.inputs[InputHandler.REAL_TIME_INPUT] == null)
		{
			throw new IllegalArgumentException("RealTimeInput is null");
		}

		this.inputType	= InputHandler.REAL_TIME_INPUT;
	}

	public void useRecordedInput()
	{
		if(this.inputs[InputHandler.PLAYABLE_INPUT] == null)
		{
			throw new IllegalArgumentException("recorded input is null");
		}

		this.inputType	= InputHandler.PLAYABLE_INPUT;
	}
	
	public void useMidiStreamInput()
	{
		if(this.inputs[InputHandler.MIDI_STREAM_INPUT] == null)
		{
			this.inputs[InputHandler.MIDI_STREAM_INPUT] = new MidiStreamInput();
		}

		this.inputType = InputHandler.MIDI_STREAM_INPUT;
		System.out.println("***** Input Type: " + this.inputType);
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
	}


	public int getCurNumRealTimeInputs()
	{
		return this.activeRealTimeInputs.length;
	}
	
	public int[] getActiveRealTimeInputs()
	{		
		return this.activeRealTimeInputs;
	}
	
	public int[][] getAllMidiNotes(int inputNum)
	{
		return ((MidiStreamInput) this.inputs[2]).getAllNotesAndAmps();
	}
	
	public int getMidiNote(int inputNum)
	{
		return this.inputs[this.inputType].getMidiNote();
	}

	public float getAmplitude(int inputNum)
	{
		return this.inputs[this.inputType].getAmplitude();
	}

}
