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
RainingNumbers   rain1;
RainingNumbers   rain2;
RainingNumbers   rain3;
RainingNumbers   rain4;

void setup()
{
  size(800, 500);
  
  input  = new Input(6);
  rain1  = new RainingNumbers(75, "mic1:", 10, 200);
  rain2  = new RainingNumbers(200, "mic2:", 13, 200);
  rain3  = new RainingNumbers(350, "mic3:", 11, 200);
  rain4  = new RainingNumbers(450, "mic4:", 11, 200);
//  background(amplitudeRain.background[0], amplitudeRain.background[1], amplitudeRain.background[2]);
  background(rain1.backgroundColor);
  
  textAlign(LEFT);
} // setup

void draw()
{
  rain1.rain(input.getAdjustedFundAsHz(1));
  rain2.rain(input.getAmplitude(2));
  rain3.rain(input.getAdjustedFundAsHz(3));
  rain4.rain(input.getAmplitude(4));
} // draw