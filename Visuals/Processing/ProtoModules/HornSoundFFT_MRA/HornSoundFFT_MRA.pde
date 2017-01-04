/*
  12/05/2016
  Saved as ProtoModule.
  
  Frequency Spectrum FFT Visualization

  Michaela Andrews, Emily Meuer
  pre-5/27/2016
  
  Experiments with freqency to index function, et al.
  
  Prints numbers - index to freq;
  Shows rectangles in spectrum w/amplitudes as heights.
*/

import ddf.minim.analysis.*;
import ddf.minim.*;

Minim       minim;
AudioPlayer play;
FFT         fft;

void setup()
{
  size(700, 300, P3D);
  
  minim = new Minim(this);
  
  // specify that we want the audio buffers of the AudioPlayer
  // to be 1024 samples long because our FFT needs to have 
  // a power-of-two buffer size and this is a good size.
  play = minim.loadFile("Horn-54321.wav",4096);
  
  // loop the file indefinitely
  play.loop();
  
  // create an FFT object that has a time-domain buffer 
  // the same size as jingle's sample buffer
  // note that this needs to be a power of two 
  // and that it means the size of the spectrum will be half as large.
  fft = new FFT( play.bufferSize(), play.sampleRate() );
  
}

void draw()
{
  background(0);
  stroke(255);
  pitch();
  // fft.getFreq(float);
  
  // perform a forward FFT on the samples in jingle's mix buffer,
  // which contains the mix of both the left and right channels of the file
  fft.forward( play.mix );
//  print(fft.indexToFreq(1000));
  
  for(int i = 0; i < fft.specSize(); i++)
  {
    // draw the line for frequency band i, scaling it up a bit so we can see it
    rect( i*5, height, 1, -fft.getBand(i)*8 );
  }
}

// The following functionality exists in CodeAndChords/Visuals/Other/FreqDomainSpectrum_EMM:
//trying to get the program to highlight the tallest column
void pitch()
{ 
  float freq;
  freq = fft.getFreq(fft.getBand(0));
  
  for(int i = 0; i < fft.specSize(); i++)
  {
//    println(i + ": " + fft.indexToFreq(i));
     //if(fft.getFreq(fft.getBand(i)) != freq)  {  stroke(random(255),random(255),random(255));  }
/*    if (fft.getFreq(fft.getBand(i)) == 200)
    {
      stroke(200,0,250);
    }
      else  {  stroke(255);  }
      */
      //print (fft.getFreq(fft.getBand(i)));
  }
}

//IDEA - have the frequency bands horizontal 
//and only the tallest ones a color other than the background