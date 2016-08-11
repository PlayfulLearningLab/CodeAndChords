/** //<>// //<>//
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
 */

int    barWidth = 10;
int    lastBar = -1;
int    howManyBars;

//int    hue;
float  saturation;

int    hueMax;
int    saturationMax;
int    brightnessMax;

Input  input;

int    delay;
color[]  bars;

int[]  majorScaleDegrees;

void setup() 
{
  size(640, 360);

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

  delay  = millis();
}

void draw() 
{
  //  barsMoveOnDelay(150);
  //  barsMoveOnPitchChange();
  barsMoveOnDelayMajorScale();
} // draw()

/**
 *  Moves the bars every 100 milliseconds, and changes colors only if the current pitch
 *  is in the major scale.
 */
void barsMoveOnDelayMajorScale()
{
  int  scaleDegree  = round(input.getAdjustedFundAsMidiNote(1) % 12);
  println("scaleDegree = " + scaleDegree);

  if (millis() > delay && (arrayContains(majorScaleDegrees, scaleDegree)))
  {
    saturation  = Math.min(input.getAmplitude(1), 100);
    fillAndDrawBars(scaleDegree * 30, saturation, brightnessMax);

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
  int hue  = round(input.getAdjustedFundAsMidiNote(1) % 12);
  hue = hue * 30;

    saturation  = Math.min(input.getAmplitude(1), 100);
  //  saturation  = map(saturation, 0, 200, 0, 100);
//  saturation  = saturationMax;

  if (color(hue, saturation, brightnessMax) != bars[bars.length - 1])
  {
    fillAndDrawBars(hue, saturation, brightnessMax);
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
    //    drawBars();

    saturation  = Math.min(input.getAmplitude(1), 100);
    fillAndDrawBars(round(input.getAdjustedFundAsMidiNote(1) % 12) * 30, saturationMax, brightnessMax);

    delay += millisDelay;
  } // if
} // barsMoveInTime

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

  println("hue = " + hue);

  bars[bars.length - 1] = color(hue, saturation, brightness);

  // Draw bars:
  for (int i = 0; i < bars.length; i++)
  {
    int barX = i * barWidth;

    fill(bars[i]);
    rect(barX, 0, barWidth, height);
  } // for
} // fillBars