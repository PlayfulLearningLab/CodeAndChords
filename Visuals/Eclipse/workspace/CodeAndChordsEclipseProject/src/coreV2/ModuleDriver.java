package coreV2;

/**
 * 		Danny Mahota
 * 		5/28/2018
 * 		coreV2.ModuleDriver
 * 
 * 		Class Overview:
 * 			The coreV2 package is meant to take all of the repetitive work out of 
 * 		making new modules, and ModuleDriver is the heart of this package.  By starting
 * 		an instance of this driver, all of the individual parts needed to make up a
 * 		fully functioning Code+Chords module are automatically created and grouped into
 * 		one central location.  All of these parts can be seen listed and explained below.
 * 
 * 		-PApplet:		Code+Chords software is programmed in the java language, but 
 * 						relies largely on the processing library to work.  The steps 
 * 						needed to use processing as a java library are laid out here:
 * 
 * 								https://processing.org/tutorials/eclipse
 * 
 * 
 * 		-InputHandler:	Helps to coordinate multiple different input types so that they
 * 						can be used within the same module.  (i.e.  a module that uses
 * 						a RealTimeInput and a RecordedInput)
 * 
 * 
 * 		-ColorHandler:	An object that manages the colors assigned to each pitch, and any
 * 						fading between colors.
 * 
 * 		
 * 		-Follower:		An object that tracks a single, constantly changing value, while
 * 						resisting any sudden changes.  The purpose of this object is to
 * 						avoid choppy visual transitions due to sudden changes in the value
 * 						associated with the given visuals.
 * 
 * 						An example of this could be a module where amplitude controls shape
 * 						size.  If the amplitude were to come in as a series of very large
 * 						and fast changes, this could result in a skipping effect where the
 * 						shapes jump from one shape size to the next instead of morphing
 * 						gradually.  By acting as an intermediary value, a Follower could
 * 						solve this problem.
 * 
 * 
 * 		-Canvas:		The canvas is the area of the applet window that all reactive
 * 						visuals are drawn on.  By using the drawing functions available
 * 						within the Canvas class, all of your visuals will automatically
 * 						be scaled down to the correct size and position when the menus
 * 						are opened.
 * 
 * 
 * 		-MenuGroup:		This object groups together multiple menus so that they can easily
 * 						be interfaced from within the app.
 * 
 */

import core.input.RealTimeInput;
import net.beadsproject.beads.core.AudioContext;
import processing.core.PApplet;

public class ModuleDriver extends PApplet
{
	private static ModuleDriver		instance = null;
	
	private static ModuleOutline	module = null;

	//TODO: How could we make it so that these variables are in InputHandler
	private int						totalNumInputs;
	private int						curNumInputs;
	private int[]					activeInputs;


	private InputHandler		 	inputHandler;

	private Follower[]				follower;

	private boolean					useFollowers;

	private ColorHandler			colorHandler;

	private Canvas 					canvas;

	private MenuGroup				menu;


	public static void startModuleDriver(ModuleOutline module)
	{
		if(module == null) throw new IllegalArgumentException("parameter is null");
		
		if(ModuleDriver.module == null)
		{
			ModuleDriver.module = module;
			PApplet.main("coreV2.ModuleDriver");
		}
		else
		{
			System.err.println("ModuleDriver has already been started.");
		}
	}//startModuleDriver()

	public ModuleDriver()
	{		
		ModuleDriver.instance = this;
		ModuleDriver.module.setDriver(ModuleDriver.instance);
	}
	
	public static ModuleDriver getModuleDriver()
	{
		return ModuleDriver.instance;
	}

	public void settings()
	{
		this.size(920, 525);
	}

	public void setup()
	{
		this.setTotalNumInputs(1);

		this.inputHandler = InputHandler.getInputHandler();

		this.inputHandler.setRealTimeInput(new RealTimeInput(16, new AudioContext(), true, this));

		this.canvas = new Canvas();
		this.menu = new MenuGroup();

		this.useFollowers = true;
		this.follower = new Follower[this.totalNumInputs];
		for(int i = 0; i < this.totalNumInputs; i++)
		{
			this.follower[i] = new Follower();
		}

		this.colorHandler = new ColorHandler();

		ModuleDriver.module.moduleSetup();
	}//setup()

	public void draw()
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




		ModuleDriver.module.moduleDraw();
		
		this.stroke(20);
		this.fill(20);
		this.canvas.drawAppletBackground();
	}//draw()


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

	public Canvas getCanvas()
	{
		return this.canvas;
	}
	
	public ColorHandler getColorHandler()
	{
		return this.colorHandler;
	}
	
	public MenuGroup getMenuGroup()
	{
		return this.menu;
	}

}
