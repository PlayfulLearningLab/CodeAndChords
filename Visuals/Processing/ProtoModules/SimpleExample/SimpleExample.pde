/*
  01/04/2017
 Updated to include current input class, to be used as a learning example.
 
 12/05/2016
 Previously "FrequencyAttempts" in CantusProject-InProgress/OldExamples
 
 Pitch Experiments
 05/26/2015
 */

// Declaring the Input variable:
Input  input;
float  amp;
float  pitch;

void setup()
{
  size(512, 200);

  // Initializing the Input variable:
  input  = new Input();
} // setup

void draw()
{
  amp  = input.getAmplitude();
  pitch  = input.getAdjustedFundAsHz();

  background(pitch/5, 0, 0);
} // draw