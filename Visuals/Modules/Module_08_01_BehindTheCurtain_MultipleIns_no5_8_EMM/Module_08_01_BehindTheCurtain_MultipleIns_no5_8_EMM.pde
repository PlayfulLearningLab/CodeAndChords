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

RainingNumbers   rain5;
RainingNumbers   rain6;
RainingNumbers   rain7;
RainingNumbers   rain8;
RainingNumbers   rain9;
RainingNumbers   rain10;

RainingNumbers[]  rains;

void setup()
{
//  size(800, 500);
  fullScreen();
  
  int[]  tailLengths  = {  10, 14, 12  };
  
  input  = new Input(12);
  
  int  spacing  = width / input.getNumInputs();
  rains  = new RainingNumbers[input.getNumInputs()];
  
  for(int i = 0; i < input.getNumInputs(); i++)
  {
    rains[i] = new RainingNumbers(spacing * i, ("mic" + (i+1)), tailLengths[i % tailLengths.length]);
  } // for

  /*
  rain1  = new RainingNumbers(spacing * 0, "mic1:", 10, 200);
  rain2  = new RainingNumbers(spacing * 1, "mic2:", 13, 200);
  rain3  = new RainingNumbers(spacing * 2, "mic3:", 11, 200);
  rain4  = new RainingNumbers(spacing * 3, "mic4:", 11, 200);
  rain5  = new RainingNumbers(spacing * 4, "mic5:", 10, 200);
  rain6  = new RainingNumbers(spacing * 5, "mic6:", 13, 200);
  rain7  = new RainingNumbers(spacing * 6, "mic7:", 11, 200);
  rain8  = new RainingNumbers(spacing * 7, "mic8:", 11, 200);
  rain9  = new RainingNumbers(spacing * 8, "mic9:", 11, 200);
  rain10  = new RainingNumbers(spacing * 9, "mic10:", 11, 200);
  */
//  background(amplitudeRain.background[0], amplitudeRain.background[1], amplitudeRain.background[2]);
  background(rains[0].backgroundColor);
  
  textAlign(LEFT);
} // setup

void draw()
{
  for(int i = 0; i < rains.length; i++)
  {
    rains[i].rain(input.getAdjustedFund(i + 1));
  } // for
  
  /*
  rain1.rain(input.getAmplitude(1));
  rain2.rain(input.getAmplitude(2));
  rain3.rain(input.getAmplitude(3));
  rain4.rain(input.getAmplitude(4));
  rain5.rain(input.getAmplitude(9));
  rain6.rain(input.getAmplitude(10));
  rain7.rain(input.getAmplitude(11));
  rain8.rain(input.getAmplitude(12));
  rain9.rain(input.getAmplitude(13));
  rain10.rain(input.getAmplitude(14));
  */
} // draw