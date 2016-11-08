import interfascia.*;

/**
 * 08/01/2016
 * Emily Meuer
 *
 * Background changes hue based on pitch.
 *
 * (Adapted from Examples => Color => Hue.)
 *
 *  TODO:
 *    - why is C (in legend) curHue, not red?
 */

// Lable for the scrollbar:
GUIController controller;
IFTextField   textField;
IFLabel       label;

int  hueMax;
int  saturationMax;
int  brightnessMax;

Input  input;
int  threshold;      // when amp is below this, the background will be black.

int  hue;
int  saturation;

int  newSaturation;
int  goalSaturation;
int  curSaturation;
int  changeInSaturation;

HScrollbar  scrollbar;
float       scrollbarPos;
float       attackTimeMax  = 20;
float       attackTimeMin  = 1;

PitchHueSquares  phSquares;

void setup() 
{
  size(700, 600);

  hueMax         = 360;
  saturationMax  = 300;
  brightnessMax  = 100;

  input        = new Input(13);
  println("input = " + input);
  threshold    = 15;

  noStroke();
  background(0);
  
  curSaturation       = (int)Math.min(input.getAmplitude(1), 300);
  changeInSaturation  = 10;
  
  phSquares    = new PitchHueSquares(9, 13);

  
  // sets the text size used to display note names:
  textSize(24);
  
  // draws the legend
  legend();
  
  scrollbar = new HScrollbar(10, height - 90, (width / 2) - 10, 18, 5);

  controller = new GUIController(this);
  textField  = new IFTextField("Text Field", 10, height - 127, 130);
//  label      = new IFLabel("attackTime: " + attackTime, 13, height - 122);
  
  controller.add(textField);
//  controller.add(label);
  
  textField.addActionListener(this);
  
//  curNote  = input.getAdjustedFundAsMidiNote();
} // setup()

void draw() 
{
  background(0);
  stroke(255);
  
  for(int i = 1; i <= phSquares.numSquares; i++)
  {
    phSquares.run(i);
  } // for
  
//  phSquares.run();
  
  stroke(255);
  // draws the legend along the bottom of the screen:
  legend();
  
  scrollbar.update();
  scrollbar.display();
  
  scrollbarPos  = scrollbar.getPos();
//  attackTime      = map(scrollbarPos, scrollbar.sposMin, scrollbar.sposMax, attackTimeMin, attackTimeMax);
  
//  label.setLabel("attackTime: " + attackTime);

} // draw()

/*
void blur(float[] curHueArray, float[] goalHueArray, float attackTime)
{
  for (int i = 0; i < 3; i++)
  {
    if (curHueArray[i] > (goalHueArray[i] - attackTime))
    {
      curHueArray[i] = curHueArray[i] - attackTime;
    } else if (curHueArray[i] < (goalHueArray[i] + attackTime))
    {
      curHueArray[i]  = curHueArray[i] + attackTime;
    } // if
  } // for
} // blur
*/

void legend()
{
  int  inputControllingLegend = 0;
  
  String[] notes = new String[] {
    "C", 
    "C#", 
    "D", 
    "D#", 
    "E", 
    "F", 
    "F#", 
    "G", 
    "G#", 
    "A", 
    "A#", 
    "B",
    "C"
  }; // notes

  float  side = width / phSquares.colors.length;

  stroke(255);

  for (int i = 0; i < phSquares.colors.length; i++)
  {
    fill(phSquares.colors[i][0], phSquares.colors[i][1], phSquares.colors[i][2]);
    if(i == phSquares.goalHuePos[inputControllingLegend]) {
      rect((side * i), height - (side * 1.5), side, side * 1.5);
    } else {
      rect((side * i), height - side, side, side);
    }
    fill(0);
    text(notes[i], (side * i) + side/2 - 10, height - (side / 3));
  } // for
} // legend

// Not sure that this really does anything right now:
// ^ No, it doesn't, but this is where we will put things that we want to happen
// when the button is pressed, etc.
void actionPerformed(GUIEvent e) {
  if (e.getMessage().equals("Completed")) {
    label.setLabel(textField.getValue());
  }
} // actionPerformed