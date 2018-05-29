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

	private InputHandler()
	{
		System.out.println("Remember to set your inputs!");

		this.inputs			= new Input[2];
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


		// TODO - Emily isn't sold on the necessity of the following lines:
		/*
		if(this.curInput == realTimeInput)
		{
			this.curInput = null;
		}

		if(this.recordedInput == null)
		{
			this.useRealTimeInput();
		}
		*/
	}

	public void playableInput(Input playableInput)
	{
		this.inputs[1]		= playableInput;
		this.curInputNumber	= InputHandler.PLAYABLE_INPUT;
		
		// TODO - still not sold on this, and it does something different than setRealTimeInput:
		/*
		if(this.curInput == this.recordedInput)
		{
			this.curInput = null;
		}

		if(this.curInput == null)
		{
			this.useRecordedInput();
		}
		*/
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
