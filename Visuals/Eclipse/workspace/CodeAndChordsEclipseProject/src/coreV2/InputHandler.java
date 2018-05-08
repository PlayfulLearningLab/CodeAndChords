package coreV2;

import core.input.Input;
import core.input.RealTimeInput;
import core.input.RecordedInput;

public class InputHandler 
{
	private static InputHandler		inputHandler;

	private RealTimeInput 			realTimeInput;
	private RecordedInput 			recordedInput;
	
	private Input					curInput;
	
	private InputHandler()
	{
		System.out.println("Remember to set your inputs!");
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
		if(this.curInput == realTimeInput)
		{
			this.curInput = null;
		}
		
		this.realTimeInput = realTimeInput;
		
		if(this.recordedInput == null)
		{
			this.useRealTimeInput();
		}
		
	}
	
	public void setRecordedInput(RecordedInput recordedInput)
	{
		if(this.curInput == this.recordedInput)
		{
			this.curInput = null;
		}
		
		this.recordedInput = recordedInput;
		
		if(this.curInput == null)
		{
			this.useRecordedInput();
		}
	}
	
	public RealTimeInput getRealTimeInput()
	{
		return this.realTimeInput;
	}
	
	public RecordedInput getRecordedInput()
	{
		return this.recordedInput;
	}
	
	public void useRealTimeInput()
	{
		if(this.realTimeInput == null)
		{
			throw new IllegalArgumentException("real time input is null");
		}
		
		this.curInput = this.realTimeInput;
	}
	
	public void useRecordedInput()
	{
		if(this.recordedInput == null)
		{
			throw new IllegalArgumentException("recorded input is null");
		}
		
		this.curInput = this.recordedInput;
	}
	
	public void play()
	{
		if(this.curInput == this.realTimeInput)
		{
			throw new IllegalArgumentException("InputHandler is still set to real time input.  Use the useRecordedInput() method if you wish to play a recorded input.");
		}
		
		if(this.recordedInput == null)
		{
			throw new IllegalArgumentException("Cannot play recorded input because it has not been set.");
		}
		
		this.curInput.pause(false);
	}
	
	public void pause()
	{
		if(this.curInput == this.realTimeInput)
		{
			throw new IllegalArgumentException("InputHandler is still set to real time input.  Use the useRecordedInput() method if you wish to play a recorded input.");
		}
		
		if(this.realTimeInput == null)
		{
			throw new IllegalArgumentException("Cannot play recorded input because it has not been set.");
		}
		
		this.curInput.pause(true);
	}
	
	public float getMidiNote(int inputNum)
	{
		if(this.curInput == null)
		{
			throw new IllegalArgumentException("The currently selected input is null.  Remember to use the useRecordedInput and useRealTimeInput methods.");
		}
		
		return this.curInput.getAdjustedFundAsMidiNote(inputNum);
	}
	
	public float getAmplitude(int inputNum)
	{
		return this.curInput.getAmplitude(inputNum);
	}
	
}
