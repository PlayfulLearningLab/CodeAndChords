/*
  09/24/2016
  Emily Meuer
  
  Mathy, Matrix-inspired raining numbers.
  
  TODO:
   - Fix pile-up at the top of the list
   - Get average freq. over time, not just whatever second happens.
   
   thoughts: maybe we don't want to start over until they're all gone?
             or start another column next to that, and start back at the first one when the second one finishes?
*/

Input           input;
RainingNumbers  amplitudeRain;
RainingNumbers   frequencyRain;

void setup()
{
  size(800, 500);
  
  input  = new Input();
  amplitudeRain  = new RainingNumbers(100, "Amplitude:", 10);
  frequencyRain  = new RainingNumbers(300, "Frequency:", 13);
  background(amplitudeRain.background[0], amplitudeRain.background[1], amplitudeRain.background[2]);
  
  textAlign(LEFT);
} // setup

void draw()
{
  amplitudeRain.rain(input.getAmplitude());
  frequencyRain.rain(input.getAdjustedFundAsHz());
} // draw