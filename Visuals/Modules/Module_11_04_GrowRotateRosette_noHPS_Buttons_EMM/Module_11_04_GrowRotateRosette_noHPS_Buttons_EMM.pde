/*
  10/09/2016
 Emily Meuer
 
 TODO:
 - add buttons
 - put options
 - remove tenorCutoff from Scene?
 
 Remember: when having an ' "Input" class not found' error,
 make sure that you have setup() and draw()! :P ;)
 */

//Buttons          buttons;
RosetteV3Rotate  rosetteV3Rotate;
Input            input;
int              controlledBy = 0;
IFButton[]       buttonArray;

//GUIController    controller;

// These are the variables from the Button class:

GUIController    controller;
IFButton[]       buttons;
IFLookAndFeel    darkGrayClicked;  // so that buttons have a different look when they're "on".
IFLookAndFeel    defaultLAF;        // the "off" state look.

void settings()
{  
  fullScreen();
} // settings()

void setup()
{ 

  controller   = new GUIController(this);
  int  buttonX  = 20;
  buttonArray      = new IFButton[] {
    // buttons here
    new IFButton("Time-based", buttonX, height - 140), 
    new IFButton("Pitch-based", buttonX, height - 120), 
    new IFButton("C Maj scale (time-based)", buttonX, height - 100)
  }; // buttons
  /*
  buttons      = new Buttons(controller, 
   new IFLookAndFeel(this, IFLookAndFeel.DEFAULT), 
   buttonArray);
   */
  input        = new Input();  
  rosetteV3Rotate  = new RosetteV3Rotate(input);

  // This is what happens in the Buttons constructor:
  this.controller  = controller;
  this.buttons     = buttons;

  //    for (int i = 0; i < buttons.length; i++)
  for (int i = 0; i < buttonArray.length; i++)
  {
    buttonArray[i].addActionListener(this);
    println("this = " + this);
    controller.add(buttonArray[i]);
    println("button " + i + " has been added.");
  } // for

  this.defaultLAF = new IFLookAndFeel(this, IFLookAndFeel.DEFAULT);

  this.darkGrayClicked  = this.defaultLAF;
  println("this.darkGrayClicked = " + this.darkGrayClicked);
  println("this.darkGrayClicked.baseColor = " + this.darkGrayClicked.baseColor);
  this.darkGrayClicked.baseColor = color(240, 100, 30);
  this.darkGrayClicked.textColor = color(255);
  this.darkGrayClicked.highlightColor = color(240, 100, 15);

  // set the first button's LAF because it is the default mode:
  buttonArray[0].setLookAndFeel(darkGrayClicked);

  background(0);
} // setup()

void draw()
{
  rosetteV3Rotate.run();
} // draw