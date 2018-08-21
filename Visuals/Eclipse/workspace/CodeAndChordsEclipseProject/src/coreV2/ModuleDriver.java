package coreV2;

import controlP5.ControlP5;
import controlP5.Tab;

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

import processing.core.PApplet;
import processing.core.PConstants;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

public class ModuleDriver implements PConstants
{
	/**
	 * The Parent PApplet that is passed in by the user.
	 */
	private PApplet 				parent;

	private ControlP5 				cp5;

	private InputHandler		 	inputHandler;

	private Follower[]				follower;

	private boolean					useFollowers;

	private ColorScheme				colorHandler;

	private Canvas 					canvas;

	private MenuGroup				menuGroup;

	
	public ModuleDriver(PApplet parent)
	{		
		if(parent == null) throw new IllegalArgumentException("PApplet parameter must not be null.");

		this.parent = parent;

		this.cp5 = new ControlP5(this.parent);
		this.cp5.getTab("default").hide();

		this.parent.registerMethod("pre", this);
		this.parent.registerMethod("keyEvent", this);

		this.inputHandler = InputHandler.makeInputHandler(this.parent);
		this.colorHandler = new ColorScheme(this, 0);
		this.canvas = new Canvas(this.parent);


		this.menuGroup = new MenuGroup(this);
		//this.menuGroup.addMenu(new InputMenu(this));
		//this.menuGroup.addMenu(new ColorMenu(this));
		//this.menu.addMenu(new SensitivityMenu(this));


		this.useFollowers = true;
		this.follower = new Follower[this.inputHandler.getActiveRealTimeInputs().length];
		for(int i = 0; i < this.follower.length; i++)
		{
			this.follower[i] = new Follower();
		}

	}

	public void pre()
	{
		if(this.colorHandler != null)
		{
			this.colorHandler.setColorToMatchMonoPitch();
		}

		this.canvas.drawAppletBackground();
	}//pre()

	public void keyEvent(KeyEvent e)
	{	
		if( e.getKey() == ' ' && this.menuGroup.canvasMenuActive() )
		{
			if(this.cp5.isVisible())
			{
				this.cp5.hide();
			}
			else
			{
				this.cp5.show();
			}
		}

	}

	public void mouseEvent(MouseEvent e)
	{

	}

	public PApplet getParent()
	{
		return this.parent;
	}

	public ControlP5 getCP5()
	{
		return this.cp5;
	}


	/*
	public Follower getFollower(int inputNum)
	{
		if(inputNum > this.totalNumInputs)
		{
			throw new IllegalArgumentException("input number is invalid: it is too high");
		}

		return this.follower[inputNum];
	}

	 */

	public Canvas getCanvas()
	{
		return this.canvas;
	}

	public ColorScheme getColorScheme()
	{
		return this.colorHandler;
	}

	public MenuGroup getMenuGroup()
	{
		return this.menuGroup;
	}

}
