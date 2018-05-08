package coreV2;

import core.input.RealTimeInput;
import processing.core.PApplet;

public class ModuleDriver implements Runnable 
{
	private PApplet			pApplet;

	private int				totalNumInputs;

	private int				curNumInputs;

	private int[]			activeInputs;


	private InputHandler 	inputHandler;

	private Follower[]		follower;

	private boolean			useFollowers;

	private ColorHandler	colorHandler;

	private Canvas canvas;



	private MenuGroup menu;

	private boolean menuIsOpen;




	public ModuleDriver(PApplet pApplet, int numInputs)
	{
		this.pApplet = pApplet;
		this.totalNumInputs = numInputs;
		this.curNumInputs = this.totalNumInputs;
		this.activeInputs = new int[this.totalNumInputs];

		for(int i = 0; i < this.totalNumInputs; i++)
		{
			this.activeInputs[i] = i;
		}

		new Thread(this).start();
	}

	@Override
	public void run() 
	{
		this.setUp();

		while(true)
		{
			this.runModule();
		}

	}

	private void setUp()
	{
		this.inputHandler = InputHandler.getInputHandler();

		this.canvas = new Canvas(this.pApplet, 925, 520);
		this.menu = new MenuGroup();
		this.menuIsOpen = false;

		this.useFollowers = true;
		this.follower = new Follower[this.totalNumInputs];
		for(int i = 0; i < this.totalNumInputs; i++)
		{
			this.follower[i] = new Follower();
		}

		this.colorHandler = new ColorHandler();


	}

	public void setMenuIsOpen(boolean value)
	{
		this.menuIsOpen = value;
	}

	public boolean getMenuIsOpen()
	{
		return this.menuIsOpen;
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
	}

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
			throw new IllegalArgumentException("too many inputs in your array");
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

}
