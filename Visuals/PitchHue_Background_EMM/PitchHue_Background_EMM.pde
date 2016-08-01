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
}

void draw() 
{
  // if want something other than C to be the "tonic" (i.e., red),
  // add some number before multiplying.
  hue  = round(input.getAdjustedFundAsMidiNote(1) % 12);
  saturation  = (int)Math.min(input.getAmplitude(1), 300);
  println("input.getAmplitude(1) = " + input.getAmplitude(1));
  background(hue * 30, saturation, brightnessMax);
}