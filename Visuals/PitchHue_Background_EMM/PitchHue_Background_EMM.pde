/**
 * 08/01/2016
 * Emily Meuer
 *
 * Background changes hue based on pitch.
 *
 * (Adapted from Examples => Color => Hue.)
 */

int  hueMax;
int  saturationMax;
int  brightnessMax;

Input  input;

int  hue;
int  saturation;

int  newHue;
int  goalHue;
int  curHue;
int  changeInHue;

int  newSaturation;
int  goalSaturation;
int  curSaturation;
int  changeInSaturation;

void setup() 
{
  size(640, 360);
  
  hueMax         = 360;
  saturationMax  = 300;
  brightnessMax  = 100;
  
  colorMode(HSB, hueMax, saturationMax, brightnessMax);
  
  input          = new Input();
  
  noStroke();
  background(0);
  
  curHue       = round(input.getAdjustedFundAsMidiNote(1) % 12) * 30;
  changeInHue  = 5;
  
  curSaturation       = (int)Math.min(input.getAmplitude(1), 300);
  changeInSaturation  = 10;
}

void draw() 
{
  // if want something other than C to be the "tonic" (i.e., red),
  // add some number before multiplying.
  newHue  = round(input.getAdjustedFundAsMidiNote(1) % 12);
  newHue  = newHue * 30;
  
  if(newHue != goalHue)
  {
    goalHue  = newHue;
  } // if
  
  if(curHue > (goalHue + changeInHue))
  {
    curHue = curHue - changeInHue;
  } else if(curHue < (goalHue - changeInHue))
  {
    curHue  = curHue + changeInHue;
  } // if
  
  // Adjust saturation with amplitude by uncommenting the following lines and 
  // commenting the "curSaturation = saturationMax" line.
  /*
  newSaturation  = (int)Math.min(input.getAmplitude(1), 300);
  
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
  
  println("curSaturation = " + curSaturation + "; goalSaturation = " + goalSaturation);
  background(curHue, curSaturation, brightnessMax);
}