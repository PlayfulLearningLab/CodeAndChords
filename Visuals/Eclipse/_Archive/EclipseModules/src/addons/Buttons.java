<<<<<<< HEAD
package addons;

import interfascia.*;
import processing.core.PApplet;
/*
  10/24/2016
 Emily Meuer

 Wrapper class to make it easier to use interfascia buttons.
 */

public class Buttons
{
	PApplet	parent;
	GUIController    controller;
	IFButton[]       buttons;
	IFLookAndFeel    darkGrayClicked;  // so that buttons have a different look when they're "on".
	IFLookAndFeel    defaultLAF;        // the "off" state look.

	int  controlledBy  = 0;

	public Buttons(PApplet parent, GUIController controller, IFLookAndFeel defaultLAF, IFButton[] buttons)
	{
		this.parent	= parent;
		this.controller  = controller;
		this.buttons     = buttons;

		for (int i = 0; i < buttons.length; i++)
		{
			buttons[i].addActionListener(this);
			controller.add(buttons[i]);
		} // for
/*
		this.darkGrayClicked  = defaultLAF;
		darkGrayClicked.baseColor = parent.color(240, 100, 30);
		darkGrayClicked.textColor = parent.color(255);
		darkGrayClicked.highlightColor = parent.color(240, 100, 15);

		this.defaultLAF = defaultLAF;

		// set the first button's LAF because it is the default mode:
		buttons[0].setLookAndFeel(darkGrayClicked);
		*/
	} // constructor


	public void actionPerformed(GUIEvent e)
	{
		parent.println("e.getSource() = " + e.getSource());
		/*

   if (e.getSource() == buttons[0])
     {
     }
		 */
	} // actionPerformed
=======
package addons;

import interfascia.*;
import processing.core.PApplet;
/*
  10/24/2016
 Emily Meuer

 Wrapper class to make it easier to use interfascia buttons.
 */

public class Buttons
{
	PApplet	parent;
	GUIController    controller;
	IFButton[]       buttons;
	IFLookAndFeel    darkGrayClicked;  // so that buttons have a different look when they're "on".
	IFLookAndFeel    defaultLAF;        // the "off" state look.

	int  controlledBy  = 0;

	public Buttons(PApplet parent, GUIController controller, IFLookAndFeel defaultLAF, IFButton[] buttons)
	{
		this.parent	= parent;
		this.controller  = controller;
		this.buttons     = buttons;

		for (int i = 0; i < buttons.length; i++)
		{
			buttons[i].addActionListener(this);
			controller.add(buttons[i]);
		} // for
/*
		this.darkGrayClicked  = defaultLAF;
		darkGrayClicked.baseColor = parent.color(240, 100, 30);
		darkGrayClicked.textColor = parent.color(255);
		darkGrayClicked.highlightColor = parent.color(240, 100, 15);

		this.defaultLAF = defaultLAF;

		// set the first button's LAF because it is the default mode:
		buttons[0].setLookAndFeel(darkGrayClicked);
		*/
	} // constructor


	public void actionPerformed(GUIEvent e)
	{
		parent.println("e.getSource() = " + e.getSource());
		/*

   if (e.getSource() == buttons[0])
     {
     }
		 */
	} // actionPerformed
>>>>>>> 346fdda528fc720bc3ef683871dafc344f6c9010
} // Buttons class