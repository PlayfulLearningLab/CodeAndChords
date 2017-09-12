package core;

import java.util.HashMap;

import controlP5.ControlP5;
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
//	protected	PApplet	parent;
	
	protected	Module	parent;

	protected	int[] events;
	
	/**	
	 * This will be the ControlP5 that the Controllers in the matrix call
	 * (each little box is an invisible, clickable Controller).
	 */
	private		ControlP5	controlP5;
	
	// TODO - this is to come.  Right now, we're only linking the numbers of inputs and events.
	private	static class Options
	{
		protected	static	int	PITCH	= 0;
		protected	static	int	AMP		= 1;
		
		protected	static	int	COLOR	= 2;
		protected	static	int	HUE		= 3;
		protected	static	int	SAT		= 4;
		protected	static	int	BRIGHT	= 5;
		protected	static	int	RED		= 6;
		protected	static	int	GREEN	= 7;
		protected	static	int	BLUE	= 8;
		protected	static	int	SIZE	= 9;
		protected	static	int	ROTATE	= 10;
	} // InputOptions
	
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
		this.events	= new int[numEvents];
		this.controlP5	= new ControlP5(this.parent);
		/*
		this.events	= new int[numEvents][module.getTotalNumInputs()];
		for(int i = 0; i < this.events.length; i++)
		{
			for(int j = 0; j < this.events[i].length; j++)
			{
				this.events[i][j]	= -1;
			} // for - j
		} // for - i
*/
	} // constructor
	
	public void assignInput(int eventNum, int input)
	{
		// Error checking
		if(eventNum < 0 || eventNum >= this.events.length)	{
			throw new IllegalArgumentException("InputMatrix.assignInput: eventNum parameter (" + eventNum + ") is out of bounds.");
		}
		if(input < 0 || input > ((Module)this.parent).getTotalNumInputs())
		{
			throw new IllegalArgumentException("InputMatrix.assignInput: input parameter (" + input + ") is out of bounds.");			
		}
		
		this.events[eventNum]	= input;
	} // assignInput
	
	public void drawMatrix()
	{
		int	textX	= 10;
		int	leftX	= 100;
		int	topY	= 100;

		System.out.println("parent input = " + (this.parent).input);
		int	numInputs	= ((Module)this.parent).input.getAdjustedNumInputs();
		
		int	squareWidth	= (this.parent.width - textX) / numInputs;
		int	squareHeight	= (this.parent.height - topY) / events.length;
		
		this.parent.fill(150, 50);
		this.parent.rect(0, 0, this.parent.width, this.parent.height);
		this.parent.fill(255);
		
		for(int i = 0; i < this.events.length; i++)
		{
			for(int j = 0; j < numInputs; j++)
			{
				if(i == 0)
				{
					this.parent.line(textX + (squareWidth * j), topY + (squareHeight * j), textX + (squareWidth * j), this.parent.height);
				}
			} // for - j (inputs)
		} // for - i (events)
	} // drawMatrix
	
	/**
	 * Assigns the given input to the given event.
	 * 
	 * @param eventNum	number of the event
	 * @param inputNum	number of the input
	 * @param inputType	0 for pitch, 1 for amplitude
	 */
/*	public void assignInput(int eventNum, int inputNum, int inputType)
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
	*/
	
	// Something to draw the visual matrix/selector
	
	// Something to give back the inputs for each event
	public int getInput(int eventNum)
	{
		if(eventNum < 0 || eventNum > this.events.length) {
			throw new IllegalArgumentException("InputMatrix: int parameter (" + eventNum + ") is out of bounds.");
		}
		return this.events[eventNum];
	}
	
	// In Module: use ^
	
	public void printMatrix()
	{
		System.out.print("{ ");
		for(int i = 0; i < this.events.length; i++)
		{
			System.out.print(this.events[i] + ", ");
		}
		System.out.println("}");
	}
	
	public int getNumEvents()
	{
		return this.events.length;
	}
	/*
	public void printMatrix()
	{
		for(int i = 0; i < this.events.length; i++)
		{
			System.out.print(i + " = { ");
			
			for(int j = 0; j < this.events[i].length; j++)
			{
				System.out.print(this.events[i][j] + ", ");
				
			} // for - j
			
			System.out.println("}");
		} // for - i
	}
	*/
	
} // InputMatrix
