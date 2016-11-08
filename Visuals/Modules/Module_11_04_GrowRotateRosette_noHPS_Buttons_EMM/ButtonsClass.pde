import interfascia.*;
/*
  10/24/2016
 Emily Meuer
 
 Wrapper class to make it easier to use interfascia buttons.
 */

class Buttons
{
  GUIController    controller;
  IFButton[]       buttons;
  IFLookAndFeel    darkGrayClicked;  // so that buttons have a different look when they're "on".
  IFLookAndFeel    defaultLAF;        // the "off" state look.

  int  controlledBy  = 0;

  Buttons(GUIController controller, IFLookAndFeel defaultLAF, IFButton[] buttons)
  {
    this.controller  = controller;
    this.buttons     = buttons;

    for (int i = 0; i < buttons.length; i++)
    {
      buttons[i].addActionListener(this);
      println("this = " + this);
      controller.add(buttons[i]);
      println("button " + i + " has been added.");
    } // for

    this.darkGrayClicked  = defaultLAF;
    darkGrayClicked.baseColor = color(240, 100, 30);
    darkGrayClicked.textColor = color(255);
    darkGrayClicked.highlightColor = color(240, 100, 15);

    this.defaultLAF = defaultLAF;

    // set the first button's LAF because it is the default mode:
    buttons[0].setLookAndFeel(darkGrayClicked);
  } // constructor


  void actionPerformed(GUIEvent e)
  {
    println("e.getSource() = " + e.getSource());
    /*
  if (e.getSource() != saturationButton)
     {
     
     if (e.getSource() == buttons[0])
     {
     println("timeButton was clicked.");
     controlledBy = 0;
     //      timeBool = !timeBool;
     
    /*
     if (timeBool) {
     timeButton.setLookAndFeel(darkGrayClicked);
     } else {
     timeButton.setLookAndFeel(defaultLAF);
     } // else
     */
    /*
    } // if - timeButton
     
     if (e.getSource() == buttons[1])
     {
     println("pitchButton was clicked.");
     controlledBy = 1;
     //     pitchBool    = !pitchBool;
     } // if - pitchButton
     
     if (e.getSource() == buttons[2])
     {
     println("majorScaleButton was clicked.");
     controlledBy = 2;
     //     scaleBool = !scaleBool;
     } // if - majorScaleButton
     
     
     // whichever button was just clicked is turned on or off:
     bools[controlledBy] = !bools[controlledBy];
     println("bools[controlledBy] = " + bools[controlledBy]);
     
     for (int i = 0; i < bools.length; i++)
     {
     if (i != controlledBy) {
     // all buttons not currently clicked are set to false:
     bools[i] = false;
     buttons[i].setLookAndFeel(defaultLAF);
     } // if
     } // for
     
     if (bools[controlledBy]) {
     // if the button was turned on, set to clicked:
     buttons[controlledBy].setLookAndFeel(darkGrayClicked);
     } else {
     // if the button was turned off, set to unclicked:
     buttons[controlledBy].setLookAndFeel(defaultLAF);
     } // else
     } // not saturationButton
     else {
     println("saturationButton was clicked.");
     saturationBool = !saturationBool;
     
     if (saturationBool) {
     saturationButton.setLookAndFeel(darkGrayClicked);
     } else {
     saturationButton.setLookAndFeel(defaultLAF);
     } // else
     } // if  - saturationButton
     */
  } // actionPerformed
} // Buttons