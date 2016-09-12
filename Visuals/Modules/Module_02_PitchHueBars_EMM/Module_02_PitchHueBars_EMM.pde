import interfascia.*; //<>// //<>//

/**
 * 09/11/2016: Question about changing on/off state:
 *  Should I make a boolean for each button, or just keep track of how many times
 *  each has been pushed?  -- Probably just a boolean.  More obvious.
 *
 * 08/01/2016
 * Emily Meuer
 *
 * Moving bars change hue based on pitch and saturation based on amplitude.
 *
 *  Things to change to calibrate saturation:
 *   - saturationMax: will change how soon the saturation maxes out; higher values take higher volume.
 *   - saturation:    set in fillBars(); simply takes the amplitude as the saturation value,
 *                      so could map to allow more control.
 *
 * (Adapted from Examples => Color => Hue.)
 *
 *  09/08: Could try to do some cool enum case thing (see this example - 
 *  https://schneide.wordpress.com/2010/12/13/avoid-switch-use-enum/)
 *  but running into too much static weirdness to mess with it at this point.
 */

int    barWidth = 10;
int    lastBar = -1;
int    howManyBars;

//int    hue;
float  saturation;
boolean  saturationBool  = false;  // determines whether or not saturation is determined by amp.

boolean  timeBool  = true;
boolean  pitchBool = false;
boolean  scaleBool = false;

int    hueMax;
int    saturationMax;
int    brightnessMax;

Input  input;
int    threshold = 5;      // This is the amplitude below which color will be black.

int    delay;
color[]  bars;

int[]  majorScaleDegrees;

GUIController  controller;
IFButton       timeButton;
IFButton       pitchButton;
IFButton       majorScaleButton;
IFButton       saturationButton;

// Won't go at it this way.  Change the default one, if accessible.
IFLookAndFeel  darkGrayClicked;
IFLookAndFeel  defaultLAF; // = new IFLookAndFeel(IFLookAndFeel.DEFAULT);

color  offState;
color  onState;

int  buttonX = 20;

/*
  0 - time
 1 - pitch
 */
int    controlledBy  = 0;

// Used by the legend:
int  newHuePos;
int  goalHuePos;
int  curHuePos;
int  goalHue;

void setup() 
{
  size(640, 380);

  hueMax         = 360;
  saturationMax  = 100;
  brightnessMax  = 100;

  colorMode(HSB, hueMax, saturationMax, brightnessMax);

  input          = new Input(); 
  noStroke();
  background(0);

  howManyBars    = width / barWidth;
  bars   = new color[howManyBars];
  // fills the color[] with white to start:
  for (int i = 0; i < bars.length; i++) 
  {
    bars[i]  = color(0, 0, brightnessMax);
  } // for

  majorScaleDegrees  = new int[]  {
    0, 
    2, 
    4, 
    5, 
    7, 
    9, 
    11, 
    12
  };

  controller   = new GUIController(this);
  timeButton   = new IFButton("Time-based", buttonX, height - 140);
  pitchButton  = new IFButton("Pitch-based", buttonX, height - 120);
  majorScaleButton  = new IFButton("C Maj scale (time-based)", buttonX, height - 100);
  saturationButton  = new IFButton("Saturation", buttonX + timeButton.getWidth(), height - 100);

  timeButton.addActionListener(this);
  pitchButton.addActionListener(this);
  majorScaleButton.addActionListener(this);
  saturationButton.addActionListener(this);

  controller.add(timeButton);
  controller.add(pitchButton);
  controller.add(majorScaleButton);
  controller.add(saturationButton);

  darkGrayClicked = new IFLookAndFeel(this, IFLookAndFeel.DEFAULT);
  darkGrayClicked.baseColor = color(240, saturationMax, 30);
  darkGrayClicked.textColor = color(255);
  darkGrayClicked.highlightColor = color(240, saturationMax, 15);
  
  defaultLAF = new IFLookAndFeel(this, IFLookAndFeel.DEFAULT);
  
  // set the timeButton's LAF because it is the default mode:
  timeButton.setLookAndFeel(darkGrayClicked);
  
  delay  = millis();
}

void draw() 
{
  noStroke();

  if (saturationBool) {
    saturation  = Math.min(input.getAmplitude(1), 100);
  } else {
    saturation = saturationMax;
  } // else - saturation


  //  newHuePos  = round(curNote % 12);
  // set goalHue to the color indicated by the current pitch:

  if (controlledBy == 0)
  {
    barsMoveOnDelay(150);
  } else if (controlledBy == 1)
  {
    barsMoveOnPitchChange();
  } else if (controlledBy == 2)
  {
    barsMoveOnDelayMajorScale();
  } else {
    controlledBy = 0;
  } // else


  if (newHuePos != goalHuePos)
  {
    goalHuePos  = newHuePos;
  } // if
  goalHue  = goalHuePos * 30;

  legend();
} // draw()

/**
 *  Moves the bars every 100 milliseconds, and changes colors only if the current pitch
 *  is in the major scale.
 */
void barsMoveOnDelayMajorScale()
{
  int  scaleDegree  = round(input.getAdjustedFundAsMidiNote(1) % 12);
  newHuePos  = scaleDegree;

  if (millis() > delay)
  {
    /*
    if(saturationBool) {
     saturation  = Math.min(input.getAmplitude(1), 100);
     } else {
     saturation = saturationMax;
     } // else - saturation
     */

    if (input.getAmplitude() > threshold)
    {
      if (arrayContains(majorScaleDegrees, scaleDegree)) {
        fillAndDrawBars(scaleDegree * 30, saturation, brightnessMax);
      } // else: do something for non-diatonic here
    } else {
      fillAndDrawBars(0, 0, 0);
    } // else - 

    delay = millis() + 100;
  } // if - delay && contains
} // barsMoveOnDelayMajorScale

boolean  arrayContains(int[] array, int element)
{
  println("array.length = " + array.length);
  for (int i = 0; i < array.length; i++)
  {
    println("array[i] = " + array[i]);
    if (array[i] == element) {
      return true;
    } // if
  } // for

  return false;
} //

/**
 *  Call fillAndDrawBars whenever a new pitch is detected
 *  with the hue detected from that pitch's scale degree (and saturationMax and brightnessMax).
 *
 *  Not very elegant-looking right now, but it could be more of an operator error, due to my singing?
 */
void barsMoveOnPitchChange()
{
  int  hue;
  int  brightness;

  if (input.getAmplitude() > threshold)
  {
    hue = round(input.getAdjustedFundAsMidiNote(1) % 12);
    newHuePos = hue;
    hue = hue * 30;

    if (saturationBool) {
      saturation  = Math.min(input.getAmplitude(1), 100);
      //  saturation  = map(saturation, 0, 200, 0, 100);
    } else {
      saturation = saturationMax;
    }

    brightness = brightnessMax;
  } else {
    hue = 0;
    saturation = 0;
    brightness = 0;
  } // else - rule

  if (color(hue, saturation, brightness) != bars[bars.length - 1])
  {
    fillAndDrawBars(hue, saturation, brightness);
  }
} // barsMoveOnPitchChange

/**
 *  Calls the fillAndDrawBars function every millisDelay milliseconds,
 *  passing it the current scale degree (from 0 to 11) and saturationMax.
 *
 *  @param  millisDelay  milliseconds at which the the bars change;
 *  the longer this is, the fewer false whites come in (basically, it's less sensitive).
 */
void barsMoveOnDelay(int millisDelay)
{
  if (millis() > delay)
  {

    if (input.getAmplitude() > threshold) 
    {
      // round moves this to the nearest midi note:
      newHuePos = round(input.getAdjustedFundAsMidiNote()) % 12;
      fillAndDrawBars(newHuePos * 30, saturation, brightnessMax);
    } else {
      fillAndDrawBars(0, 0, 0);
    } // else - silence is black

    delay += millisDelay;
  } // if
} // barsMoveInTime

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

  float  side = width / 13;

  stroke(255);

  for (int i = 0; i < 13; i++)
  {
    fill(i * 30, saturationMax, brightnessMax);
    if (i == goalHuePos) {
      rect((side * i), height - (side * 1.5), side, side * 1.5);
    } else {
      rect((side * i), height - side, side, side);
    }
    fill(0);
    text(notes[i], (side * i) + side/2 - 10, height - (side / 3));
  } // for
} // legend

/**
 *  Loops through the bars array and draws rectangles across the screen
 *  in the colors that are in their respective positions in the array;
 *  
 *  ** Prob. not necessary, now that fillAndDrawBars combines both into one. **
 */
void drawBars()
{
  for (int i = 0; i < bars.length; i++)
  {
    int barX = i * barWidth;

    fill(bars[i]);
    rect(barX, 0, barWidth, height);
  } // for
} // drawBars()

/**
 *  Moves each color in the bars array up on position, and fills the last position
 *  with a color whose hue is derived from pitch earlier in the program
 *  and passed in as a parameter here.
 *
 *  @param  hue         an int, from 0 to 11, denoting the current scale degree of the input.
 *  @param  saturation  a float that will be used to determine the saturation of the HSB color.
 *  @param  brightness  an int that will be used to determine the brightness of the HSB color.
 */
void fillAndDrawBars(int hue, float saturation, int brightness)
{
  // move each color in the array up one position:
  for (int i = 0; i < bars.length - 1; i++)
  {
    bars[i] = bars[i+1];
  } // for

  /*
  hue  = round(input.getAdjustedFundAsMidiNote(1) % 12);
   println("hue = " + hue);
   hue  = hue * 30;
   */

  // The mapping isn't strictly necessary, since we could just set a higher saturationMax.
  // The hope is that it will allow it to be more intuitive and look less random.
  //  saturation  = Math.min(input.getAmplitude(1), 100);
  //  saturation  = map(saturation, 0, 200, 0, 100);
  //  saturation  = saturationMax;
  //  println("saturation = " + saturation);


  bars[bars.length - 1] = color(hue, saturation, brightness);

  // Draw bars:
  for (int i = 0; i < bars.length; i++)
  {
    int barX = i * barWidth;

    fill(bars[i]);
    rect(barX, 0, barWidth, height);
  } // for
} // fillBars

void actionPerformed(GUIEvent e)
{
  if (e.getSource() == timeButton)
  {
    println("timeButton was clicked.");
    controlledBy = 0;
    
    timeBool = !timeBool;
    
    if(timeBool) {
      timeButton.setLookAndFeel(darkGrayClicked);
    } else {
      timeButton.setLookAndFeel(defaultLAF);
    } // else
  } // if - timeButton

  if (e.getSource() == pitchButton)
  {
    println("pitchButton was clicked.");
    controlledBy = 1;
  } // if - pitchButton

  if (e.getSource() == majorScaleButton)
  {
    println("majorScaleButton was clicked.");
    controlledBy = 2;
  } // if - majorScaleButton

  if (e.getSource() == saturationButton)
  {
    println("saturationButton was clicked.");
    saturationBool = !saturationBool;
    
    if(saturationBool) {
      saturationButton.setLookAndFeel(darkGrayClicked);
    } else {
      saturationButton.setLookAndFeel(defaultLAF);
    } // else
  } // if  - saturationButton
} // actionPerformed