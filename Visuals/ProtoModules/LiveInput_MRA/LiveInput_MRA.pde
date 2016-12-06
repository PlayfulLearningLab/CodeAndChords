/*
  12/05/2016
  Moved to ProtoModules.

  Michaela Andrews
  Spring 2016
  
  Early live input experiments.
  Displays spectrum (rectangle heights = amplitude);
  changes background color on keypress.

*/

import ddf.minim.analysis.*;
import ddf.minim.*;

Minim       minim;
AudioPlayer play;
AudioInput  in;
FFT         fft;
color         fillVal;

void setup()
{
  size(700, 300);
  //MRA removed P3D so it would work with my computers hardware (OpenGL Issues)
  fillVal = 200;
  minim = new Minim(this);
  
  // specify that we want the audio buffers of the AudioPlayer
  // to be 1024 samples (or ms??) long because our FFT needs to have 
  // a power-of-two buffer size and this is a good size.
  
  play = minim.loadFile("Horn-54321.wav",4096);
  in = minim.getLineIn(Minim.STEREO, 256);  
  
  // loop the file indefinitely
//  play.loop();
  
  // create an FFT object that has a time-domain buffer 
  // the same size as jingle's sample buffer
  // note that this needs to be a power of two 
  // and that it means the size of the spectrum will be half as large.
  fft = new FFT( in.bufferSize(), in.sampleRate() );
  //fft = new FFT( play.bufferSize(), play.sampleRate() );
  
}

void draw()
{
  background(fillVal);
  //stroke(255);
  pitch();
  // perform a forward FFT on the samples in jingle's mix buffer,
  // which contains the mix of both the left and right channels of the file
  fft.forward( in.mix );
  //fft.forward( play.mix );
  
  for(int i = 0; i < fft.specSize(); i++)
  {
    // draw the line for frequency band i, scaling it up a bit so we can see it
    rect( i*10, height, 1, -fft.getBand(i)*8 );
  }
}

//trying to get the program to highlight the tallest column
void pitch()
{  
  for(int i = 0; i < fft.specSize(); i++){
    if ((fft.getBand(i) < fft.getBand(i-1)) && (fft.getBand(i) < fft.getBand(i+1))){
      stroke(200,0,250);}
    else { 
      stroke(255); }
  }
}
void keyPressed() {
  if (key == CODED) {
    if (keyCode == UP) {
      fillVal = color(150,75,250);} 
      else if (keyCode == DOWN) {
      fillVal = color(150,150,200);} 
      else if (keyCode == RIGHT) {
      fillVal = color(100,150,150);} 
      else if (keyCode == LEFT) {
      fillVal = color(100,100,200);} 
  } 
  else {fillVal = 255;}
}

void keyReleased() {
  fillVal = 200;
}


//IDEA - have the frequency bands horizontal 
//and only the tallest ones a color other than the background