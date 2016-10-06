/*
  09/24/2016
  Emily Meuer
  
  Mathy, Matrix-inspired raining numbers.
  
  TODO:
   - Fix pile-up at the top of the list
   - Get average freq. over time, not just whatever second happens.
   - Silence holds the last number; probably don't want that?
   
   - Plotting points
   
   thoughts: maybe we don't want to start over until they're all gone?
             or start another column next to that, and start back at the first one when the second one finishes?
*/

Input           input;
RainingNumbers  amplitudeRain;
RainingNumbers   frequencyRain;

void settings()
{
  size(800, 500);
} // settings

void setup()
{ 
  stroke(255);
  
  input  = new Input();
  amplitudeRain  = new RainingNumbers(width / 3, "Amplitude:", 10, 100);
  frequencyRain  = new RainingNumbers(2 * width / 3, "Frequency:", 13, 200);
  background(amplitudeRain.backgroundColor);
  
  textAlign(LEFT);
} // setup

void draw()
{
//  amplitudeRain.rain(input.getAmplitude());
  frequencyRain.rain(input.getAdjustedFundAsHz());
} // draw