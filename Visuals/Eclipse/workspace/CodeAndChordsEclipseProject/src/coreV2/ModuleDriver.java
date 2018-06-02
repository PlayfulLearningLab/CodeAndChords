package coreV2;

import core.input.RealTimeInput;
import processing.core.PApplet;

/**
 * TODO: Emily is putting things that multiple Menus need access to in this class.
 * 
 * @author codeandchords
 *
 */
public class ModuleDriver implements Runnable 
{
	private PApplet			pApplet;

	private int				totalNumInputs;

	private int				curNumInputs;

	private int[]			activeInputs;
	
	/**	Number of the input currently affected by the Menu */
	private	int				currentInput;
	
	/**	Boolean indicating whether or not the Menu changes apply to all inputs or just the currentInput */
	private	boolean			global;

	private InputHandler 	inputHandler;

	private Follower[]		follower;

	private boolean			useFollowers;

	private ColorHandler	colorHandler;

	private Canvas canvas;
	

	private MenuGroup 	menu;
	
	/**	Used to implement global vs. single input actions: 
	 * if global, startHere = 0 and endBeforeThis = module.totalNumInputs;
	 * if particular input, startHere = currentInput and endBeforeThis = (currentInput + 1).
	 */
	private	int	startHere;
	private	int	endBeforeThis;




	public ModuleDriver(PApplet pApplet, int numInputs)
	{
		this.pApplet = pApplet;
		this.totalNumInputs = numInputs;
		this.curNumInputs = this.totalNumInputs;
		this.activeInputs = new int[this.totalNumInputs];
		this.currentInput	= 0;
		this.global			= true;
		this.startHere		= 0;
		this.endBeforeThis	= this.totalNumInputs;

		for(int i = 0; i < this.totalNumInputs; i++)
		{
			this.activeInputs[i] = i;
		}
		
		this.inputHandler = InputHandler.getInputHandler();
		
		this.inputHandler.setRealTimeInput(new RealTimeInput(this.totalNumInputs, this.pApplet));

		this.canvas = new Canvas(this.pApplet, 925, 520);
		this.menu = new MenuGroup(this.canvas, this);

		this.useFollowers = true;
		this.follower = new Follower[this.totalNumInputs];
		for(int i = 0; i < this.totalNumInputs; i++)
		{
			this.follower[i] = new Follower();
		}

		this.colorHandler = new ColorHandler(this.totalNumInputs, 12);

		//Thread thread = new Thread(this);
		//thread.setPriority(10);
		//thread.start();
	}

	@Override
	public void run() 
	{
		while(true)
		{
			this.runModule();
		}

	}


	public void runModule()
	{
		if(this.useFollowers)
		{
			int inputNum;
			for(int i = 0; i < this.curNumInputs; i++)
			{
				inputNum = this.activeInputs[i];
				this.follower[inputNum].update(this.inputHandler.getAmplitude(inputNum));
			}
		}
		
		if(this.colorHandler != null)
		{
			//update color handler
		}
		
		this.menu.runMenu();
	}
	
	/**
	 *  Error checker; rejects numbers that are greater than or equal to the number of inputs or less than 0.
	 *
	 *  @param   inputNum  an int that is to be checked for suitability as an input line number.
	 *  @param   String    name of the method that called this method, used in the exception message.
	 */
	protected void inputNumErrorCheck(int inputNum) {
		if (inputNum >= this.totalNumInputs) {
			IllegalArgumentException iae = new IllegalArgumentException("ModuleDriver.inputNumErrorCheck(int): int parameter " + inputNum + " is greater than " + this.totalNumInputs + ", the number of inputs.");

			iae.printStackTrace();
			throw iae;
		}
		if (inputNum < 0) {
			IllegalArgumentException iae = new IllegalArgumentException("ModuleDriver.inputNumErrorCheck(int): int parameter is " + inputNum + "; must be 1 or greater.");
			iae.printStackTrace();
			throw iae;
		}
	} // inputNumErrorCheck

	public Follower getFollower(int inputNum)
	{
		if(inputNum > this.totalNumInputs)
		{
			throw new IllegalArgumentException("input number is invalid: it is too high");
		}

		return this.follower[inputNum];
	}

	public InputHandler getInputHandler()
	{
		return this.inputHandler;
	}

	public void setActiveInputs(int[] activeInputs)
	{
		if(activeInputs.length > this.totalNumInputs)
		{
			throw new IllegalArgumentException("ModuleDriver.setActiveInputs: too many Inputs in your array");
		}
		for(int i = 0; i < activeInputs.length; i++)
		{
			if(activeInputs[i] > this.totalNumInputs)
			{
				throw new IllegalArgumentException("One of your inputs does not exist. Input num is too high.");

			}
		}
		
		this.activeInputs = activeInputs;
		this.curNumInputs = this.activeInputs.length;
	}
	
	public Canvas getCanvas()
	{
		return this.canvas;
	}
	
	public int getTotalNumInputs() {
		return this.totalNumInputs;
	} // getTotalNumInputs
	
	public int getCurrentInput() {
		return this.currentInput;
	}
	
	public boolean getGlobal() {
		return this.global;
	}
	
	public int getStartHere() {
		return this.startHere;
	}
	
	public int getEndBeforeThis() {
		return this.endBeforeThis;
	}
	
	public ColorHandler getColorHandler() {
		return this.colorHandler;
	}


}
