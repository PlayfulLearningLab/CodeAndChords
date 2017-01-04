/*
  12/05/2016
  Moved to ProtoModules.
  
  A Circle gets created and remains on the screen.

// Emily Meuer
// Fall 2015

// Adapted from MicSignal2Ellipse (INSTINT files)
// From: 
// http://www.creativecoding.org/lesson/topics/audio/sound-in-processing
*/

import ddf.minim.*;
 
float x;
float y;
Minim    minim;
AudioInput input;
 
void setup () 
{
  size (960, 540);
  smooth();
  stroke (255, 25);
  noFill ();

  x = 0;
   
  // Gets the audio
  minim = new Minim (this);
  input = minim.getLineIn (Minim.STEREO, 512);
  background (255, 153, 51);
} // setup()
 
void draw () 
{
  // sets the ellipse size relative to width and imput volume:
  float dim = input.mix.level () * width;
  
  // changes color depending on input volume:
  x = input.mix.level () * 1000;
  fill((255-(x)),(255-(x*2)),(255-(x*1.25)));
  ellipse (480, 270, dim, dim);

} // draw()