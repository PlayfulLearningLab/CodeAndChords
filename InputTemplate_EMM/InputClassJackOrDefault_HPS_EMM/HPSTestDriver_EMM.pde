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
FrequencyEMM    freqEMM;

int  maxBin;

void settings()
{
  size(1000, 500);
} // settings

void setup()
{ 
  stroke(255);
  
  input  = new Input();
  freqEMM = input.frequencyArray[0];
  maxBin  = 0;
  
  amplitudeRain  = new RainingNumbers(width / 3, "Amplitude:", 10, 100);
  frequencyRain  = new RainingNumbers(2 * width / 3, "Frequency:", 13, 200);
  background(amplitudeRain.backgroundColor);
  
  textAlign(LEFT);
} // setup

void draw()
{
  background(0);
  amplitudeRain.rain(input.getAmplitude());
//  frequencyRain.rain(input.getAdjustedFundAsHz());

//  drawFFT(input);

//println("input.getAdjustedFundAsHz() = " + input.getAdjustedFundAsHz());
} // draw

void drawFFT(Input input)
{
  stroke(255);
  for(int i = 0; i < freqEMM.hps.length; i++)
  {
    rect( i*10, height, 1, - freqEMM.hps[i] );
    
    if(freqEMM.hps[i] > freqEMM.hps[maxBin])
    {
      maxBin  = i;
    } // if
  } // for
  
  stroke(150, 50, 150);
  rect( maxBin*10, height, 1, - freqEMM.hps[maxBin] );
} // drawFFT