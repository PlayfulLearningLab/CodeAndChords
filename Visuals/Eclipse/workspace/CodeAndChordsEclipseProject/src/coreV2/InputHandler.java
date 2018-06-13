package coreV2;

import core.input.Input;
import core.input.RealTimeInput;
import core.input.RecordedInput;

public class InputHandler 
{
	private static InputHandler		inputHandler;
	
	private	static final int		REAL_TIME_INPUT	= 0;
	private	static final int		PLAYABLE_INPUT	= 1;

	private	Input[]					inputs;
	private	int						curInputNumber;	// 0 - realTimeInput; 1 - playableInput
	
	// TODO - initialize these:
	private	int	curNumInputs;
	private	int	totalNumInputs;

	private InputHandler()
	{
		System.out.println("Remember to set your inputs!");

		this.inputs			= new Input[2];
		

		this.totalNumInputs = numInputs;
		this.curNumInputs = this.totalNumInputs;
		this.activeInputs = new int[this.totalNumInputs];
		this.currentInput	= 0;
	}

	public static InputHandler getInputHandler()
	{
		if(InputHandler.inputHandler == null)
		{
			InputHandler.inputHandler = new InputHandler();
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
	

	//TODO: talk to Emily about how we handle input numbers.  I know we are using an int[]
	//		so that we can account for a board that skips numbers (just one example), but
	//		how do we make sure that this model is unbreakable when we don't know the board
	//		configuration?
	public void setTotalNumInputs(int totalNumInputs)
	{
		if(totalNumInputs < 1) throw new IllegalArgumentException("There must be at least one input.");

		this.totalNumInputs = totalNumInputs;
		this.curNumInputs = this.totalNumInputs;
		this.activeInputs = new int[this.totalNumInputs];

		for(int i = 0; i < this.totalNumInputs; i++)
		{
			this.activeInputs[i] = i;
		}
	}
	

	public void setActiveInputs(int[] activeInputs)
	{
		if(activeInputs.length != this.totalNumInputs)
		{
			throw new IllegalArgumentException("ModuleDriver.setActiveInputs: You passed in an array of the wrong size.  There are " + this.totalNumInputs + " inputs total.");

		}
		for(int i = 0; i < activeInputs.length; i++)
		{
			if(activeInputs[i] > this.totalNumInputs)
			{
				throw new IllegalArgumentException("One of your inputs does not exist. Input num " + activeInputs[i] + " is too high.");

			}
		}

		this.activeInputs = activeInputs;
		this.curNumInputs = this.activeInputs.length;
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
