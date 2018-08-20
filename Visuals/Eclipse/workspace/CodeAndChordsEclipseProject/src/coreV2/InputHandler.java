package coreV2;

import core.input.Input;
import core.input.MidiStreamInput;
import core.input.MusicalInput;
import core.input.RealTimeInput;
import core.input.RecordedInput;
import net.beadsproject.beads.core.AudioContext;

public class InputHandler 
{
	private static InputHandler		inputHandler;

	private ModuleDriver 			driver;

	public	static final int		REAL_TIME_INPUT	= 0;
	public	static final int		PLAYABLE_INPUT	= 1;
	public  static final int		MIDI_STREAM_INPUT = 2;

	private	MusicalInput[]			inputs;
	private	int						curInputNum;	// 0 - realTimeInput; 1 - playableInput

	private int						totalNumRealTimeInputs;
	private int[]					activeRealTimeInputs;

	private InputHandler(ModuleDriver driver)
	{
		System.out.println("Remember to set your inputs!");

		this.driver = driver;
		
		this.inputs			= new MusicalInput[3];

		this.totalNumRealTimeInputs = 1;
		this.activeRealTimeInputs = new int[] {0};
		
		//this.curInputNum = InputHandler.REAL_TIME_INPUT;
		//this.setRealTimeInput(new RealTimeInput(1, new AudioContext(), false, this.driver.getParent()));
		
		this.inputs[InputHandler.MIDI_STREAM_INPUT] = new MidiStreamInput();
		this.curInputNum = InputHandler.MIDI_STREAM_INPUT;
	}

	public static InputHandler getInputHandler(ModuleDriver driver)
	{
		if(InputHandler.inputHandler == null)
		{
			InputHandler.inputHandler = new InputHandler(driver);
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
		this.curInputNum	= InputHandler.REAL_TIME_INPUT;

	}

	public void setPlayableInput(Input playableInput)
	{
		this.inputs[1]		= playableInput;
		this.curInputNum	= InputHandler.PLAYABLE_INPUT;

	}
	
	public void setMidiStreamInput(MidiStreamInput midiStreamInput)
	{
		this.inputs[InputHandler.MIDI_STREAM_INPUT] = midiStreamInput;
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

		this.curInputNum	= InputHandler.REAL_TIME_INPUT;
	}

	public void useRecordedInput()
	{
		if(this.inputs[InputHandler.PLAYABLE_INPUT] == null)
		{
			throw new IllegalArgumentException("recorded input is null");
		}

		this.curInputNum	= InputHandler.PLAYABLE_INPUT;
	}
	
	public void useMidiStreamInput()
	{
		if(this.inputs[InputHandler.MIDI_STREAM_INPUT] == null)
		{
			throw new IllegalArgumentException("Midi Stream input is null");
		}

		this.curInputNum	= InputHandler.MIDI_STREAM_INPUT;
	}

//TODO: fixes for new interface
	public void switchInputs()
	{
		// Pause the current Input:
		//this.inputs[this.curInputNum].pause(true);

		// Go to the next Input and start it:
		this.curInputNum	= (this.curInputNum + 1) % 2;
		//this.inputs[this.curInputNum].pause(false);
	} // switch(boolean)

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
		return ((MidiStreamInput) this.inputs[2]).getCurNotes();
	}
	
	public int getMidiNote(int inputNum)
	{
		return this.inputs[this.curInputNum].getMidiNote();
	}

	public float getAmplitude(int inputNum)
	{
		return this.inputs[this.curInputNum].getAmplitude();
	}

}
