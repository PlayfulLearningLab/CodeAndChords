/*

  11/10/2016
  Working on buttons;
   - Why is it drawing one in the corner?
   - Only works b/c mousePressed is in "main" sketch. Need a better way.

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


Button  testButton;

void settings()
{  
  fullScreen();
} // settings()

void setup()
{ 

  controller   = new GUIController(this);
  int  buttonX  = 100;
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
    
    println("buttonArray[" + i + "].getY() " + buttonArray[i].getY());
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
  
  testButton  = new Button(100, 100, 50, color(0), color(150, 50, 150), color(200, 50, 150), 0, true);
  
} // setup()

void mousePressed() {
    if (testButton.over) {
      testButton.state  = (testButton.state + 1) % 2;
      println("button was pressed");
    }
  } // mousePressed

void draw()
{
//  rosetteV3Rotate.run();
  /*
  for(int i = 0; i < buttonArray.length; i++)
  {
    buttonArray[i].draw();
  } // for
  
  */
  
  testButton.update();
} // draw

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