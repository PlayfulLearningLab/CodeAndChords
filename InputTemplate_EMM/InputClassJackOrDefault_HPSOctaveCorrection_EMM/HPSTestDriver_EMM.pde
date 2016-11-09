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
  size(800, 500);
} // settings

void setup()
{ 
  stroke(255);
  
  input  = new Input();
//  freqEMM = input.frequencyArray[0];
  maxBin  = 0;
  
  amplitudeRain  = new RainingNumbers(width / 3, "Amplitude:", 10, 100);
  frequencyRain  = new RainingNumbers(2 * width / 3, "Frequency:", 13, 200);
  background(amplitudeRain.backgroundColor);
  
  textAlign(LEFT);
} // setup

void draw()
{
  background(0);
//  amplitudeRain.rain(input.getAmplitude());
  frequencyRain.rain(input.getAdjustedFundAsHz());
  
  println("input.getAdjustedFundAsHz() = " + input.getAdjustedFundAsHz());

//  drawHPSSecondHighest(input);
} // draw

void drawHPS(Input input)
{
  int  maxBin  = 0;
  
  FrequencyEMM freqEMM  = input.frequencyArray[0];
  
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

void drawHPSSecondHighest(Input input)
{
  FrequencyEMM freqEMM  = input.frequencyArray[0];
  int maxBin            = 0;
  int secondMaxBin      = 0;
  
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
  rect( freqEMM.maxbin*10, height, 1, - freqEMM.hps[maxBin] );
    
  double  j;
    // find second peak:
    for (int band = 0; band < freqEMM.hps.length; band++)
    {
      j  = freqEMM.hps[band];
      if ((j > freqEMM.hps[secondMaxBin]) && (j < freqEMM.hps[maxBin]))
      {
        secondMaxBin  = band;
      } // if
    } // for - second peak
    
    stroke(50, 50, 255);
    rect(secondMaxBin * 10, height, 1, -freqEMM.hps[secondMaxBin]);
} // void