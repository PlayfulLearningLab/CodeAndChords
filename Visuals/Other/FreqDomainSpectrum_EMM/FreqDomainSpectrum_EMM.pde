/*
  10/27/2016
  Emily Meuer
  
  Drawing the Frequency-Domain spectrum to identify problems with pitch detection.
*/

Input  input;
int    maxbin;

void setup()
{
  size(800, 900);
  
  input  = new Input(1);
  
  while(millis() < 1000) {}
} // setup

void draw()
{
  drawHPS(input);
} // draw

void drawHPS(Input input)
{
  background(200);
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
  rect( freqEMM.hps[maxBin]*10, height, 1, - freqEMM.hps[maxBin] );
    
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
} // drawHPSSecondHighest