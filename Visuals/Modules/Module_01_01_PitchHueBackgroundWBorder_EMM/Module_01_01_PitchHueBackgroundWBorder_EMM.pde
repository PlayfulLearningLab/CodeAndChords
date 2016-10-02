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

float[]  newHue;
float[]  goalHue;
float[]  curHue;
float    hueDelta;

int  newHuePos;
int  goalHuePos;
int  curHuePos;

float  curNote;
float  newNote;

float[]  newHueBorder;
float[]  goalHueBorder;
float[]  curHueBorder;

int  newSaturation;
int  goalSaturation;
int  curSaturation;
int  changeInSaturation;

float[][]  colors;          // holds the RGB values for the colors responding to HSB: every 30th H with 100 S, 100 B

HScrollbar  scrollbar;
float       scrollbarPos;
float       hueDeltaMax  = 20;
float       hueDeltaMin  = 1;

void setup() 
{
  size(700, 600);

  hueMax         = 360;
  saturationMax  = 300;
  brightnessMax  = 100;

  //  colorMode(HSB, hueMax, saturationMax, brightnessMax);
  colors  = new float[][] {
    { 255, 0, 0 }, 
    { 255, 127.5, 0 }, 
    { 255, 255, 0 }, 
    { 127.5, 255, 0 }, 
    { 0, 255, 0 }, 
    { 0, 255, 127.5 }, 
    { 0, 255, 255 }, 
    { 0, 127.5, 255 }, 
    { 0, 0, 255 }, 
    { 127.5, 0, 255 }, 
    { 255, 0, 255 }, 
    { 255, 0, 127.5 }, 
    { 255, 0, 0 }
  };

  /*
  // this draws rectangles of the colors across the screen:
   for (int i = 0; i < colors.length; i++)
   {
   fill(colors[i][0], colors[i][1], colors[i][2]);
   rect((width / colors.length) * i, 0, (width / colors.length), height);
   } // for
   */

  input        = new Input();
  threshold    = 15;

  noStroke();
  background(0);

//  curHuePos    = round(input.getAdjustedFundAsMidiNote(1) % 12) * 30;
  curHuePos    = round(input.getAdjustedFundAsMidiNote(1) % 12);
  curHue       = colors[curHuePos];
  // would like to change more quickly, but there's a weird flicker if hueDelta gets bigger:
  hueDelta  = 10;

  curSaturation       = (int)Math.min(input.getAmplitude(1), 300);
  changeInSaturation  = 10;

  
  // sets the text size used to display note names:
  textSize(24);
  
  // draws the legend
  legend();
  
  scrollbar = new HScrollbar(10, height - 90, (width / 2) - 10, 18, 5);

  controller = new GUIController(this);
  textField  = new IFTextField("Text Field", 10, height - 127, 130);
  label      = new IFLabel("hueDelta: " + hueDelta, 13, height - 122);
  
  controller.add(textField);
  controller.add(label);
  
  textField.addActionListener(this);
  
  curNote  = input.getAdjustedFundAsMidiNote();
} // setup()

void draw() 
{
  background(0);
  stroke(255);
  
  if (input.getAmplitude() > threshold)
  {
    newNote = input.getAdjustedFundAsMidiNote();
    if(newNote == curNote) {
      println("  Note was held.");
    } else {
      println("New note! curNote = " + curNote + "; newNote = " + newNote);
      curNote = newNote;
    } // else - set curNote
    
    // The following finds the color from the particular note.
    // if want something other than C to be the "tonic" (i.e., red),
    // add some number before multiplying.
    newHuePos  = round(curNote % 12);
    newHue  = colors[newHuePos];
    //  newHue  = newHue * 30;  // this is for HSB, when newHue is the color's H value
  } else {
    newHue  = new float[] { 0, 0, 0 };
  } // else

  // set goalHue to the color indicated by the current pitch:
  if (newHuePos != goalHuePos)
  {
    goalHuePos  = newHuePos;
  } // if
  goalHue  = colors[goalHuePos];
  
  blur(curHue, goalHue, hueDelta);

  // Adjust saturation with amplitude by uncommenting the following lines and 
  // commenting the "curSaturation = saturationMax" line.
  // ** This no longer works because of RGB! :(
  // ^ Chillax and change the color mode.
  /*
  newSaturation  = (int)Math.min(input.getAmplitude(1), 300);
   println("input.getAmplitude(1) = " + input.getAmplitude(1));
   
   if(newSaturation != goalSaturation) {
   goalSaturation  = newSaturation;
   } //if
   
   if(curSaturation > goalSaturation) {
   curSaturation = curSaturation - changeInSaturation;
   } else if(curSaturation < goalSaturation) {
   curSaturation = curSaturation + changeInSaturation;
   } // if
   */

  curSaturation  = saturationMax;

// Switched from setting the background to drawing a rectangle, to allow Turrell border.
//  background(curHue[0], curHue[1], curHue[2]);
  fill(curHue[0], curHue[1], curHue[2]);
  rect(20, 20, width - 40, height - 90, 10);
  
  stroke(255);
  // draws the legend along the bottom of the screen:
  legend();
  
  scrollbar.update();
  scrollbar.display();
  
  scrollbarPos  = scrollbar.getPos();
  hueDelta      = map(scrollbarPos, scrollbar.sposMin, scrollbar.sposMax, hueDeltaMin, hueDeltaMax);
  
  label.setLabel("hueDelta: " + hueDelta);

/*
  // ellipse for testing different colors - for my benefit, but could be cool.
  fill(255, 127.5, 0);
//  fill(colors[0][0], colors[0][1], colors[0][2]);
  ellipse(width / 2, height / 2, 100, 100);
*/
} // draw()

void blur(float[] curHueArray, float[] goalHueArray, float hueDelta)
{
  for (int i = 0; i < 3; i++)
  {
    if (curHueArray[i] > (goalHueArray[i] - hueDelta))
    {
      curHueArray[i] = curHueArray[i] - hueDelta;
    } else if (curHueArray[i] < (goalHueArray[i] + hueDelta))
    {
      curHueArray[i]  = curHueArray[i] + hueDelta;
    } // if
  } // for
} // blur

void legend()
{
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

  float  side = width / colors.length;

  stroke(255);

  for (int i = 0; i < colors.length; i++)
  {
    fill(colors[i][0], colors[i][1], colors[i][2]);
    if(i == goalHuePos) {
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