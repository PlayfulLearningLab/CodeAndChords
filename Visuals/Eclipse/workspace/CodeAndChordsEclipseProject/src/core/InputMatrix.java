package core;

import processing.core.PApplet;

/**
 * Sep. 6, 2017
 * 
 * Provides a GUI to allow the user to assign inputs (e.g., pitch of line 1, amplitude of line 2) 
 * to particular events (e.g., shape size/color/brightness) and stores those assignments
 * for access by Modules.
 * 
 * @author Emily Meuer
 *
 */
public class InputMatrix {

	/**	This will be the Module that creates the InputMatrix	*/
	protected	PApplet	parent;

	protected	int[][] events;
	
	// TODO - maybe use this:
//	protected	String[] eventNames;
	
	/**
	 * Constructor
	 * 
	 * @param numEvents	number of events in this matrix
	 * @param module	Module calling the InputMatrix; will be stored in the PApplet instance variable
	 */
	public InputMatrix(int numEvents, Module module)
	{
		// Error checking:
		if(numEvents < 0)	{
			throw new IllegalArgumentException("InputMatrix.constructor: int parameter is less than 0.");
		}
		if(module == null)	{
			throw new IllegalArgumentException("InputMatrix.constructor: Module parameter is null.");
		}
		
		this.parent	= module;
		this.events	= new int[numEvents][module.getTotalNumInputs()];
		for(int i = 0; i < this.events.length; i++)
		{
			for(int j = 0; j < this.events[i].length; j++)
			{
				this.events[i][j]	= -1;
			} // for - j
		} // for - i
	} // constructor
	
	/**
	 * Assigns the given input to the given event.
	 * 
	 * @param eventNum	number of the event
	 * @param inputNum	number of the input
	 * @param inputType	0 for pitch, 1 for amplitude
	 */
	public void assignInput(int eventNum, int inputNum, int inputType)
	{
		// Error checking
		if(eventNum < 0 || eventNum >= this.events.length)	{
			throw new IllegalArgumentException("InputMatrix.assignInput: eventNum parameter (" + eventNum + ") is out of bounds.");
		}
		if(inputNum < 0 || inputNum >= this.events[eventNum].length)	{
			throw new IllegalArgumentException("InputMatrix.assignInput: inputNum parameter (" + inputNum + ") is out of bounds.");
		}
		if(inputType < 0 || inputType > 2)	{
			throw new IllegalArgumentException("InputMatrix.assignInput: inputType parameter (" + inputType + ") is out of bounds; must be 0, 1, or 2.");
		}
		
		this.events[eventNum][inputNum]	= inputType;
	} // assignInput
	
} // InputMatrix
