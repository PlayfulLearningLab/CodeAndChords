package coreV2;

import java.awt.Color;

import controlP5.ControlP5;
import controlP5.Tab;
import coreV2_EffectsAndFilters.Drawable;

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

/**
 * 
 * 	@author Danny Mahota
 * 
 * 	ModuleDriver is the core of the CODE+CHORDS software. Upon creation, the ModuleDriver
 * 		creates all the components needed to make visuals that react to sound. This includes:
 * 		
 * 		- MenuGroup			an organizational tool that handles all the menus
 * 		- MenuTemplate		an abstract class for building menus that can be added to the 
 * 								MenuGroup
 * 
 * 		- InputHandler 		a menu for switching between inputs (microphone, midi, etc.)
 * 		- ColorMenu 		a menu for choosing switching colors
 * 		- VisualMenu		a menu for switching between various visuals
 * 		
 * 		- Visual			an abstract class for creating new visuals. These are then easily
 * 								plugged into the visual menu to be included as options.
 * 
 * 		- Drawable			an interface for creating effect chains. Everything drawn to the
 * 								effectChain instance variable will be passed through the
 * 								effects and filters that have been assigned before drawing
 * 								to the canvas
 * 		- Canvas			an object that automatically scales and translates the visual
 * 		- ColorScheme		a wrapper class for holding groups of colors
 * 
 * 		- ColorFader		a utility class for easily creating smooth, time dependent fading
 * 								between colors
 * 
 * 
 * 	Check out the README for a more in depth explanation of how everything works! CoreV2 was designed
 * 		to be modular in nature, so there are lots of fun ways you can add onto it!
 *
 */
public class ModuleDriver implements PConstants
{
	/**
	 * parent is the PApplet that is passed in by the user.
	 */
	private PApplet 				parent;
	
	/**
	 * ControlP5 is the library used to create all menu controls
	 */
	private ControlP5 				cp5;

	/**
	 * Canvas is an object that automatically scales and translates the visual
	 */
	private Canvas 					canvas;

	/**
	 * MenuGroup holds an array of menus, responsible for switching between them
	 */
	private MenuGroup				menuGroup;
	
	/**
	 * An output object that will apply effects and filters, then draw to the canvas
	 */
	private Drawable				effectChain;
	
	


	public ModuleDriver(PApplet parent)
	{
		if(parent == null) throw new IllegalArgumentException("PApplet parameter must not be null.");
		
		this.parent = parent;

		//Create an instance of ControlP5 for making menu controls
		this.cp5 = new ControlP5(this.parent);
		this.cp5.getTab("default").hide();
		
		this.cp5.hide();
				
		//Create the Canvas (The re-sizable part of the screen the visual will go on)
		this.canvas = new Canvas(this.parent);
		
		this.effectChain = this.canvas;
		
		//Create the menu system
		this.menuGroup = new MenuGroup(this);
		
		//register the necessary library methods with the PApplet parent
		this.parent.registerMethod("pre", this);
		this.parent.registerMethod("keyEvent", this);
		
		this.cp5.show();
		
	}

	/**
	 * pre() is called by the PApplet repeatedly, just before the draw() method
	 */
	public void pre()
	{
		this.canvas.drawAppletBackground();
		
	}//pre()


	/**
	 * A method called by processing every time a key is used.
	 * 
	 * @param e
	 */
	public void keyEvent(KeyEvent e)
	{	
		//This allows the user to hide the play, pause, and hamburger buttons by pressing the space bar
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

	// ***** getter methods *****
	
	public PApplet getParent()
	{
		return this.parent;
	}

	public ControlP5 getCP5()
	{
		return this.cp5;
	}

	public Canvas getCanvas()
	{
		return this.canvas;
	}
	
	public Drawable getEffectChain(){
		return this.effectChain;
	}
	
	public void setEffectChain(Drawable[] effects){
		
		/*
		 * Sort effectChain while setting: effects first, then filters, and end with the canvas
		 * 	- 	The size of this array will always be small so 
		 * 		runtime and space aren't a concern, but it is important
		 * 		to maintain the sequence of the effects and the sequence
		 * 		of the Filters.
		 */
		
		//Set the canvas as the root because it will always be the final output
		this.effectChain = this.canvas;
		
		//Then build the chain backwards
		//Note: this is similar to a linked list structure
		
		//Iterate through the array backwards to preserve the order of the Filters
		for(int i = effects.length-1; i >= 0; i--)
		{
			if(effects[i].getType() == Drawable.FILTER)
			{
				effects[i].setOutput(this.effectChain);
				this.effectChain = effects[i];
			}
		}

		//Iterate through the array backwards to preserve the order of the Effects
		for(int i = effects.length-1; i >= 0; i--)
		{
			if(effects[i].getType() == Drawable.EFFECT)
			{
				effects[i].setOutput(this.effectChain);
				this.effectChain = effects[i];
			}
		}
	}

	public MenuGroup getMenuGroup()
	{	
		return this.menuGroup;
	}
	
	public InputHandler getInputHandler()
	{			
		return (InputHandler) this.menuGroup.getMenus()[0];
	}
	

	public ColorMenu getColorMenu()
	{			
		return (ColorMenu) this.menuGroup.getMenus()[1];
	}
	
	public VisualMenu getVisualMenu()
	{			
		return (VisualMenu) this.menuGroup.getMenus()[2];
	}


}
