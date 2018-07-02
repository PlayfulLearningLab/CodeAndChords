package coreV2;

import core.input.Input;
import core.input.RealTimeInput;
import core.input.RecordedInput;
import net.beadsproject.beads.core.AudioContext;

public class InputHandler 
{
	private static InputHandler		inputHandler;

	private ModuleDriver 			driver;

	private	static final int		REAL_TIME_INPUT	= 0;
	private	static final int		PLAYABLE_INPUT	= 1;

	private	Input[]					inputs;
	private	int						curInputNumber;	// 0 - realTimeInput; 1 - playableInput

	//TODO: How could we make it so that these variables are in InputHandler
	private int						totalNumRealTimeInputs;
	private int[]					activeRealTimeInputs;

	private InputHandler(ModuleDriver driver)
	{
		System.out.println("Remember to set your inputs!");

		this.driver = driver;
		
		this.inputs			= new Input[2];

		this.totalNumRealTimeInputs = 1;
		this.activeRealTimeInputs = new int[] {0};
		this.curInputNumber = 0;
		
		this.setRealTimeInput(new RealTimeInput(1, new AudioContext(), false, this.driver.getParent()));
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
		this.curInputNumber	= InputHandler.REAL_TIME_INPUT;

	}

	public void playableInput(Input playableInput)
	{
		this.inputs[1]		= playableInput;
		this.curInputNumber	= InputHandler.PLAYABLE_INPUT;

	}

	public Input getRealTimeInput()
	{
		return this.inputs[InputHandler.REAL_TIME_INPUT];
	}

	public Input getPlayableInput()
	{
		return this.inputs[InputHandler.PLAYABLE_INPUT];
	}

	public void useRealTimeInput()
	{
		if(this.inputs[InputHandler.REAL_TIME_INPUT] == null)
		{
			throw new IllegalArgumentException("RealTimeInput is null");
		}

		this.curInputNumber	= InputHandler.REAL_TIME_INPUT;
	}

	public void useRecordedInput()
	{
		if(this.inputs[InputHandler.PLAYABLE_INPUT] == null)
		{
			throw new IllegalArgumentException("recorded input is null");
		}

		this.curInputNumber	= InputHandler.PLAYABLE_INPUT;
	}


	public void switchInputs()
	{
		// Pause the current Input:
		this.inputs[this.curInputNumber].pause(true);

		// Go to the next Input and start it:
		this.curInputNumber	= (this.curInputNumber + 1) % 2;
		this.inputs[this.curInputNumber].pause(false);
	} // switch(boolean)

	public void setTotalNumRealTimeInputs(int totalNumInputs)
	{
		if(totalNumInputs < 1) throw new IllegalArgumentException("There must be at least one input.");

		this.totalNumRealTimeInputs = totalNumInputs;
		this.activeRealTimeInputs = new int[this.totalNumRealTimeInputs];

		for(int i = 0; i < this.totalNumRealTimeInputs; i++)
		{
			this.activeRealTimeInputs[i] = i;
		}
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
	
	public float getMidiNote(int inputNum)
	{
		if(this.inputs[this.curInputNumber] == null)
		{
			throw new IllegalArgumentException("The currently selected Input is null.  Remember to use the useRecordedInput and useRealTimeInput methods.");
		}

		return this.inputs[this.curInputNumber].getAdjustedFundAsMidiNote(inputNum);
	}

	public float getAmplitude(int inputNum)
	{
		return this.inputs[this.curInputNumber].getAmplitude(inputNum);
	}

}
