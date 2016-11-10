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
float    saturation;
boolean  saturationBool  = false;  // determines whether or not saturation is determined by amp.

boolean  timeBool  = true;
boolean  pitchBool = false;
boolean  scaleBool = false;
boolean[] bools = {
  timeBool, 
  pitchBool, 
  scaleBool
};

int    hueMax;
int    saturationMax;
int    brightnessMax;

Input  input;
int    threshold = 1;     // This is the amplitude below which color will be black.

int        delay;
float[][]  bars;          // bars[i][0], bars[i][1], bars[i][2] denote the color; 
// bars[i][3] is 0 to signify solid color and 1 for gradient.
//color[]  bars;


int[]  majorScaleDegrees;

GUIController  controller;
IFButton       timeButton;
IFButton       pitchButton;
IFButton       majorScaleButton;
IFButton       saturationButton;
IFButton[]     buttons = new IFButton[] {
  timeButton, 
  pitchButton, 
  majorScaleButton
} ;                // includes all but saturationButton, since this needs to not.

// Won't go at it this way.  Change the default one, if accessible.
IFLookAndFeel  darkGrayClicked;
IFLookAndFeel  defaultLAF; // = new IFLookAndFeel(IFLookAndFeel.DEFAULT);

int  buttonX = 20;

HScrollbar   scrollbar;
float        scrollbarPos;
IFTextField  textField;
IFLabel      label;
int          bpmMin  = 20;
int          bpmMax  = 600;
int          bpm;

int    controlledBy  = 0;

// Used by the legend:
int  newHuePos;
int  goalHuePos;
int  curHuePos;
int  goalHue;

void setup() 
{
  size(800, 400);

  hueMax         = 360;
  saturationMax  = 100;
  brightnessMax  = 100;

  colorMode(HSB, hueMax, saturationMax, brightnessMax);

  input          = new Input(1); 
  noStroke();
  background(0);

  howManyBars  = width / barWidth;
  bars         = new float[howManyBars][4];
  //  bars   = new color[howManyBars];
  // fills the color[] with white to start:
  for (int i = 0; i < bars.length; i++) 
  {
    bars[i][0]  = 0;
    bars[i][1]  = 0;
    bars[i][2]  = brightnessMax;
    bars[i][3]  = 0;
    //    bars[i]  = color(0, 0, brightnessMax);
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
  //  timeButton   = new IFButton("Time-based", buttonX, height - 140);
  buttons[0]   = new IFButton("Time-based", buttonX, height - 140);
  buttons[1]  = new IFButton("Pitch-based", buttonX, height - 120);
  buttons[2]  = new IFButton("C Maj scale (time-based)", buttonX, height - 100);
  saturationButton  = new IFButton("Saturation", buttonX + buttons[0].getWidth(), height - 100);

  buttons[0].addActionListener(this);
  buttons[1].addActionListener(this);
  buttons[2].addActionListener(this);
  saturationButton.addActionListener(this);

  controller.add(buttons[0]);
  controller.add(buttons[1]);
  controller.add(buttons[2]);
  controller.add(saturationButton);

  darkGrayClicked = new IFLookAndFeel(this, IFLookAndFeel.DEFAULT);
  darkGrayClicked.baseColor = color(240, saturationMax, 30);
  darkGrayClicked.textColor = color(255);
  darkGrayClicked.highlightColor = color(240, saturationMax, 15);

  defaultLAF = new IFLookAndFeel(this, IFLookAndFeel.DEFAULT);

  // set the timeButton's LAF because it is the default mode:
  buttons[0].setLookAndFeel(darkGrayClicked);


  scrollbar = new HScrollbar(width - 200, height - 90, 150, 18, 5);
  textField = new IFTextField("Text Field", width - 200, height - 125, 75);
  label     = new IFLabel("BPM: " + bpm, width - 197, height - 120);

  controller.add(textField);
  controller.add(label);

  textField.addActionListener(this);

  delay  = millis();
}

void draw() 
{
  noStroke();

  if (saturationBool) {
    saturation  = Math.min(input.getAmplitude(1)/ 2, 100);
  } else {
    saturation = saturationMax;
  } // else - saturation

  println("input.getAdjustedFund() = " + input.getAdjustedFund());
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

  scrollbar.update();
  scrollbar.display();

  // I think scrollbar.getPos() is confused.
  scrollbarPos  = scrollbar.getPos();
  bpm      = round(map(scrollbarPos, scrollbar.sposMin, scrollbar.sposMax, bpmMin, bpmMax));
//  println("scrollbarPos = " + scrollbarPos + "; scrollbar.sposMin = " + scrollbar.sposMin + "; scrollbar.sposMax = " + scrollbar.sposMax);
//  println("  scrollbar.ratio = " + scrollbar.ratio);
//  println("bpm = " + bpm + "; bpmMin = " + bpmMin + "; bpmMax = " + bpmMax);

  label.setLabel("BPM: " + bpm);
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
    if (input.getAmplitude() > threshold)
    {
      if (arrayContains(majorScaleDegrees, scaleDegree)) {
        // these pitches are diatonic:
        fillBars(scaleDegree * 30, saturation, brightnessMax, 0);
      } // else: do something for non-diatonic here
      else
      {
        // these pitches are non-diatonic:

        if (saturationBool) {
          // saturationButton is on:          
          fillBars(scaleDegree * 30, saturation, brightnessMax, 1);
        } else {
          // saturationButton is off:
          fillBars(scaleDegree * 30, (saturation * 0.25), brightnessMax, 0);
        }
        //       fillBars(scaleDegree * 30,
      } // else -- diatonic or not
    } else { 
      fillBars(0, 0, 100, 0);
    } // else - no sound

    drawBarsGradient();
    delay = millis() + 100;
  } // if - delay && contains
} // barsMoveOnDelayMajorScale

boolean  arrayContains(int[] array, int element)
{
  //  println("array.length = " + array.length);
  for (int i = 0; i < array.length; i++)
  {
    //    println("array[i] = " + array[i]);
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
    brightness = 100;
  } // else - rule

  if (color(hue, saturation, brightness) != color(bars[bars.length - 1][0], bars[bars.length - 1][1], bars[bars.length - 1][2]))
  {
    fillBars(hue, saturation, brightness, 0);
    drawBarsGradient();
    //    fillAndDrawBars(hue, saturation, brightness);
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
      //      fillAndDrawBars(newHuePos * 30, saturation, brightnessMax);
      fillBars(newHuePos * 30, saturation, brightnessMax, 0);
    } else {
      fillBars(0, 0, 100, 0);
    } // else - silence is white


    drawBarsGradient();
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
 *  Moves each color in the bars array up on position, and fills the last position
 *  with a color whose hue is derived from pitch earlier in the program
 *  and passed in as a parameter here.
 */
void fillBars(float hue, float saturation, float brightness, float diatonic)
{
  // move each color in the array up one position:
  for (int i = 0; i < bars.length - 1; i++)
  {
    //    bars[i] = bars[i+1];

    bars[i][0]  = bars[i+1][0];
    bars[i][1]  = bars[i+1][1];
    bars[i][2]  = bars[i+1][2];
    bars[i][3]  = bars[i+1][3];
  } // for

  // set the last position of the array to the current, pitch/amp-derived color:
  bars[bars.length - 1][0] = hue;
  bars[bars.length - 1][1] = saturation;
  bars[bars.length - 1][2] = brightness;
  bars[bars.length - 1][3] = diatonic;
} // fillBars(int, int, int, int)

/**
 *  Loops through the bars array and draws rectangles across the screen
 *  in the colors that are in their respective positions in the array;
 */
void drawBars()
{
  for (int i = 0; i < bars.length; i++)
  {
    int barX = i * barWidth;

    fill(bars[i][0], bars[i][1], bars[i][2]);
    rect(barX, 0, barWidth, height);
  } // for
} // drawBars()

/**
 *  Loops through the bars array and draws rectangles across the screen
 *  in the colors that are in their respective positions in the array,
 *  drawing those whose [i][3] position is 1 in a gradient from white to that color.
 */
void drawBarsGradient()
{
  for (int i = 0; i < bars.length; i++)
  {
    int barX = i * barWidth;

    // color the rectangle normally for diatonic tones:
    if (bars[i][3] == 0) {
      fill(bars[i][0], bars[i][1], bars[i][2]);
      rect(barX, 0, barWidth, height);
    } else {
      // color with a gradient for non-diatonic tones:
      // adapted from the Linear Gradient example here: https://processing.org/examples/lineargradient.html
      int y = 0;
      int x = barX;
      int h = height;
      int w = barWidth;
      color c1 = color(0, 0, brightnessMax);
      color c2 = color(bars[i][0], bars[i][1], bars[i][2]);

      for (int j = y; j <= y+h; j++) {
        float inter = map(j, y, y+h, 0, 1);
        color c = lerpColor(c1, c2, inter);
        stroke(c);
        line(x, j, x+w, j);
      } // for
    } // else

    // avoids colored outline of black "silence" bars:
    noStroke();
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
    //    bars[i] = bars[i+1];

    bars[i][0]  = bars[i+1][0];
    bars[i][1]  = bars[i+1][1];
    bars[i][2]  = bars[i+1][2];
    bars[i][3]  = bars[i+1][3];
  } // for

  bars[bars.length - 1][0] = hue;
  bars[bars.length - 1][1] = saturation;
  bars[bars.length - 1][2] = brightness;
  bars[bars.length - 1][3] = 0;

  // Draw bars:
  for (int i = 0; i < bars.length; i++)
  {
    int barX = i * barWidth;

    fill(bars[i][0], bars[i][1], bars[i][2]);
    rect(barX, 0, barWidth, height);
  } // for
} // fillBars

void actionPerformed(GUIEvent e)
{
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
} // actionPerformed