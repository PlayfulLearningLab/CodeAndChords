package core;

/**
 * Emily Meuer
 * 01/11/2017
 * 
 * Putting all the pop-out sidebar/moduleTemplate stuff from Module_01_02 in this class.
 * 
 * @author Emily Meuer
 *
 */
public class ModuleTemplate {

	// Static var's: colorStyles
	
	// Global vars - TODO: all private!
	// Buttons:
	//		Play/Stop:
	//			images
	//			
	//		Hamburger/Menu X (probably different things now, but still connected)
	//		
	
	
	// Methods:
	
	//	initModuleTemplate():
	/*
	 * 	-- Play/Stop button:
	 * 		- load images
	 * 		- set x and y
	 * 		- addButton();
	 */
	
	
	
	
	
	/*
	 * 01/11/2017 brainstoring:
	 * I'll have options for 
	 * 
	 * Do I want generic sliders?  I won't have a great way of accessing their results,
	 * but I'll also have more freedom in making ones.
	 * Otherwise, I can say the only ones you can use are the ones that I made.
	 * (Which makes sense, because then I don't need to set new range values each time,
	 * and maybe it can even  interact with Input -- do something when it crosses the threshold, etc.)
	 * 
	 * I don't want displaySidebar() to be a whole bunch of if's, though;
	 * Maybe: I can have an ArrayList of functions that are implemented for this particular instance,
	 * and I go through that array list and call the corresponding functions.
	 * Then I'll have another huge if() function that takes a number parameter and does the thing it's supposed to do.
	 *  ^ Not too bad, b/c it only counts toward text.  Other things are implementable once.
	 *  
	 *  Slight problem = how controlP5 deals w/events. I need a separate function for each button I might have,
	 *  but what if I make two of those buttons? (That doesn't make sense... Why would you have two hidePlay buttons?)
	 *  
	 *  Main problem: I really don't understand ControlP5 yet.
	 *  Solution:	I'll implement Module_01_02 w/ControlP5, not generically at all.
	 *  			Then, when I can see how it works, maybe I can make it generic.
	 *  			(Kind of what I was going to do, anyway; we'll take this one step at a time.)
	 */
	
}
