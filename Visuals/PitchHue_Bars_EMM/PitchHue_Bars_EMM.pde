/**
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

int    hue;
float  saturation;

int    hueMax;
int    saturationMax;
int    brightnessMax;

Input  input;

int    delay;
color[]  bars;

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
  
  delay  = millis();
  
  howManyBars    = width / barWidth;
  bars   = new color[howManyBars];
  // fills the color[] with white to start:
  for(int i = 0; i < bars.length; i++) 
  {
    bars[i]  = color(0, 0, brightnessMax);
  } // for
}

void draw() 
{
  if(millis() > delay)
  {
    drawBars();
  
    fillBars();
    
    // milliseconds at which the the bars change;
    // the longer this is, the fewer false whites come in (basically, it's less sensitive).
    delay += 150;
  } // if
} // draw()

/**
 *  Loops through the bars array and draws rectangles across the screen
 *  in the colors that are in their respective positions in the array.
 */
void drawBars()
{
  for(int i = 0; i < bars.length; i++)
  {
    int barX = i * barWidth;
    
    fill(bars[i]);
    rect(barX, 0, barWidth, height);
  } // for
} // drawBars()

/**
 *  Moves each color in the bars array up on position, and fills the last position
 *  with a color whose hue is derived from pitch and whose saturation is derived from amplitude.
 */
void fillBars()
{
  for(int i = 0; i < bars.length - 1; i++)
  {
    bars[i] = bars[i+1];
  } // for
  
  hue  = round(input.getAdjustedFundAsMidiNote(1) % 12);
  println("hue = " + hue);
  hue  = hue * 30;
  
  // The mapping isn't strictly necessary, since we could just set a higher saturationMax.
  // The hope is that it will allow it to be more intuitive and look less random.
//  saturation  = Math.min(input.getAmplitude(1), 100);
//  saturation  = map(saturation, 0, 200, 0, 100);
  saturation  = saturationMax;
  println("saturation = " + saturation);
  
  bars[bars.length - 1] = color(hue, saturation, brightnessMax);
} // fillBars