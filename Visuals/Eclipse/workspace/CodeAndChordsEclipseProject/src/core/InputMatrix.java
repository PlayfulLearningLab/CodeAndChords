package core;

import controlP5.ControlP5;

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

	protected	int[][]	events;	// The input number and dataType (0 for pitch, 1 for amplitude) for each input
	protected	String[]	eventNames;
	
	protected	int		squareWidth;
	protected	int		squareHeight;
	protected	int		textX;
	protected	int		leftX;
	protected	int		topY;
	protected	int		textSize;
	
	private	int	ellipseX;
	
	protected boolean	visible;
	
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
		this(numEvents, module, null);
	} // constructor(int, Module)
	
	public InputMatrix(int numEvents, Module module, String[] eventNames)
	{
		// Error checking:
		if(numEvents < 0)	{
			throw new IllegalArgumentException("InputMatrix.constructor: int parameter is less than 0.");
		}
		if(module == null)	{
			throw new IllegalArgumentException("InputMatrix.constructor: Module parameter is null.");
		}
		
		// Initialize eventNames if null (the other constructor sends it as null):
		if(eventNames == null)
		{
			this.eventNames	=  new String[numEvents];
			for(int i = 0; i < this.eventNames.length; i++)
			{
				this.eventNames[i]	= "event" + i;
			} // for
		} // if - initalize eventNames
		
		this.parent	= module;
		this.events	= new int[numEvents][2];
		this.controlP5	= new ControlP5(this.parent);
		
		this.textX	= 10;
		this.leftX	= 100;
		this.topY	= 50;
		// Dividing by 2 allows for two dataTypes for each Input:
		this.squareWidth	= (this.parent.width - textX) / (this.parent.getTotalNumInputs() * 2);
		this.squareHeight	= (this.parent.height - (topY * 2)) / events.length;

		this.textSize	= 12;
		this.parent.textSize(this.textSize);
		
		this.visible	= false;

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
	} // constructor(int, Module, String[])
	
	public void assignInput(int eventNum, int input, int dataType)
	{
		// Error checking
		if(eventNum < 0 || eventNum >= this.events.length)	{
			throw new IllegalArgumentException("InputMatrix.assignInput: eventNum parameter (" + eventNum + ") is out of bounds.");
		}
		if(input < 0 || input > ((Module)this.parent).getTotalNumInputs())
		{
			throw new IllegalArgumentException("InputMatrix.assignInput: input parameter (" + input + ") is out of bounds.");			
		}
		if(dataType < 0 || dataType > 1) {
			throw new IllegalArgumentException("InputMatrix.assignInput: dataType parameter (" + dataType + ") is out of bounds.");			
		}
		
		this.events[eventNum][0]	= input;
		this.events[eventNum][1]	= dataType;
	} // assignInput
	
	public void drawMatrix()
	{
		this.visible	= true;
		
//		int	textX	= 10;
		int	leftX	= 100;
//		int	topY	= 100;

		int	numInputs	= ((Module)this.parent).input.getAdjustedNumInputs() * 2;
		
/*		int	squareWidth	= (this.parent.width - textX) / numInputs;
		int	squareHeight	= (this.parent.height - topY) / events.length;
	*/	
		this.parent.fill(0, 200);
//		this.parent.fill(255);
		this.parent.rect(0, 0, this.parent.width, this.parent.height);
		this.parent.fill(255);
		this.parent.stroke(255);
		this.parent.textSize(this.textSize);
//		this.parent.strokeWeight(20);
		
		// Events are along the side (i determines y position):
		for(int i = 0; i < this.events.length; i++)
		{
			this.parent.text(eventNames[i], textX, this.topY + (i * squareHeight) + this.textSize);
			
			// Inputs are along the top (j determines x position):
			for(int j = 0; j < numInputs; j++)
			{
				// Draw ellipse at the proper position:
				if(this.events[i][0] == j)
				{
					// Set ellipse position for pitch and amplitude:
					// (Multiplying by events[i][1] will set amplitude over one squareWidth)
					this.ellipseX	= leftX + (j * 2 * squareWidth) + (squareWidth * this.events[i][1]) + (squareWidth / 2);

					/*					if(this.events[i][1] == 0)
					{
						this.ellipseX	= leftX + (j * 2 * squareWidth) + (squareWidth / 2);
					} else {
						// The key here is the " + 1 " :
						this.ellipseX	= leftX + (j * 2 * squareWidth) + squareWidth + (squareWidth / 2);
					}
					*/
					this.parent.ellipse(this.ellipseX, topY + (i * squareHeight) + (squareHeight / 2), 10, 10);
				}
				
				if(i == 0)
				{
					// Label with inputNum and dataType:
					if(j % 2 == 0)
					{
						this.parent.text((j / 2) + ":", leftX + (squareWidth * j) + (squareWidth / 2), topY);
					}
					
					this.parent.line(leftX + (squareWidth * j), topY, leftX + (squareWidth * j), this.parent.height - squareHeight);
				}
				
			} // for - j (inputs)
			
			this.parent.line(leftX, topY + (squareHeight * i), this.parent.width, topY + (squareHeight * i));

		} // for - i (events)
	} // drawMatrix
	
	public void assignInputFromLoc(int xLoc, int yLoc)
	{
		if(xLoc >= this.leftX && yLoc >= this.topY)
		{
			int	inputNum	= (int)((xLoc - this.leftX) / this.squareWidth);
			int	eventNum	= (int)((yLoc - this.topY) / this.squareHeight);
			
			// This assumed that our first "input" is input0_pitch and second is input0_amp:
			int	dataType	= inputNum % 2;
			inputNum		= (int)inputNum / 2;
			
			System.out.println("inputNum = " + inputNum + "; eventNum = " + eventNum + "; dataType = " + dataType);
			
			this.assignInput(eventNum, inputNum, dataType);
		} // if
	} // assignInputFromLoc
	
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
	public int[] getInput(int eventNum)
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
	
	public boolean isVisible()
	{
		return this.visible;
	}
	
	public void setVisible(boolean newVal)
	{
		this.visible	= newVal;
	}
	
	public void mousePressed()
	{
		if(this.visible)
		{
			this.assignInputFromLoc(this.parent.mouseX, this.parent.mouseY);
		}
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
