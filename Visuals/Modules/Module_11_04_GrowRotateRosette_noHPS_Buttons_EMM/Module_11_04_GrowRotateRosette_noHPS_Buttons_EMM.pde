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

Buttons          buttons;
RosetteV3Rotate  rosetteV3Rotate;
Input            input;
int              controlledBy = 0;
IFButton[]       buttonArray;

GUIController    controller;

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
  buttons      = new Buttons(controller, 
    new IFLookAndFeel(this, IFLookAndFeel.DEFAULT), 
    buttonArray);
  input        = new Input();  
  rosetteV3Rotate  = new RosetteV3Rotate(input);

  background(0);
} // setup()

void draw()
{
  rosetteV3Rotate.run();
} // draw