import ddf.minim.analysis.*;
import ddf.minim.*;

/*
  12/05/2016
  Moved to ProtoModules; this was preliminary pitch detection work
  (trying to find something other than the loudest frequency).
  
  Push the arrow keys to change the background color!
  
  Similar visual to HornSoundFFT_MRA (a Frequency domain FFT visualization), 
  but with an audio loop.

  Emily Meuer
  06/06/2016
  
  Building off of LiveInput_MRA to highlight the rectangle corresponding to the current pitch.

   
*/


Minim       minim;
AudioPlayer play;
AudioInput  in;
FFT         fft;
color       fillVal;
float       fundamental;

void setup()
{
  size(700, 300);
  //MRA removed P3D so it would work with my computers hardware (OpenGL Issues)
  fillVal = 200;
  
  minim = new Minim(this);
  
  // specify that we want the audio buffers of the AudioPlayer
  // to be 1024 samples (or ms??) long because our FFT needs to have 
  // a power-of-two buffer size and this is a good size.
  
  play = minim.loadFile("Zikr - parts - Bass.mp3", 8192); //4096);
//  in = minim.getLineIn(Minim.STEREO, 256);  
  
  // loop the file indefinitely
  play.loop();
  
  // create an FFT object that has a time-domain buffer 
  // the same size as jingle's sample buffer
  // note that this needs to be a power of two 
  // and that it means the size of the spectrum will be half as large.
//  fft = new FFT( in.bufferSize(), in.sampleRate() );
  fft = new FFT( play.bufferSize(), play.sampleRate() );
  
}

void draw()
{
  background(fillVal);
  stroke(255);
  
  // perform a forward FFT on the samples in jingle's mix buffer,
  // which contains the mix of both the left and right channels of the file
  fft.forward( play.mix );
  this.fundamental = 140;
  
/*       
   G4 391.995
   F4 349.228
   E4 329.628
   D4 293.665
   C4 261.626
*/  

  // find the fundamental:
  for(int i = 5; i < fft.specSize(); i++)
  {
    if (this.fft.getBand(i) > this.fft.getFreq(fundamental))
      {
          if(this.fft.indexToFreq(i) - fundamental < (fundamental / 2))
          {
            fundamental = this.fft.indexToFreq(i);
          }
      } // if
      
    // draw the line for frequency band i, scaling it up a bit so we can see it
    rect( i*10, height, 1, -fft.getBand(i)*8 );
  } // for
  
  stroke(150, 0, 150);
  int index = this.fft.freqToIndex(fundamental);
  rect( index*10, height, 1, -this.fft.getBand(index)*8 );
  
//  println("fundamental: " + fundamental);
} // draw

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