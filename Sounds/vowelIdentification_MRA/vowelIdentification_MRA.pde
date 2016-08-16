/*
  08/16/2016
  Michaela Andrews
  
  Displaying frequency information
  
  Modified example from Ch. 7 of Sonifying Processing for microphone input
  instead of mp3 input. The example in the book is based off of an example
  by Ollie Brown included in the Beads download.
*/


import beads.*;
AudioContext ac;
PowerSpectrum ps;
void setup() {
  size(600,600);

  ac = new AudioContext();
  UGen microphoneIn = ac.getAudioInput();
  // set up a master gain object
  Gain g = new Gain(ac, 1, 0.5);
  g.addInput(microphoneIn);
  ac.out.addInput(g);

  // In this block of code, we build an analysis chain
  // the ShortFrameSegmenter breaks the audio into short,
  // discrete chunks.
  ShortFrameSegmenter sfs = new ShortFrameSegmenter(ac);
  sfs.addInput(ac.out);

  FFT fft = new FFT();
  // connect the FFT object to the ShortFrameSegmenter
  sfs.addListener(fft);

  // the PowerSpectrum pulls the Amplitude information from
  // the FFT calculation (essentially)
  ps = new PowerSpectrum();
  // connect the PowerSpectrum to the FFT
  fft.addListener(ps);
  // list the frame segmenter as a dependent, so that the
  // AudioContext knows when to update it.
  ac.out.addDependent(sfs);
  // start processing audio
  ac.start();
} // setup()


void draw() {
  background(0);
  stroke(222);

  // The getFeatures() function is a key part of the Beads
  // analysis library. It returns an array of floats.
  // How this array of floats is defined (1 dimension, 2
  // dimensions ... etc) is based on the calling unit
  // generator. In this case, the PowerSpectrum returns an
  // array with the power of 256 spectral bands.
  float[] features = ps.getFeatures();

  // if any features are returned
  if(features != null) { // for each x coordinate in the Processing window
    for(int x = 0; x < width; x++) { // figure out which featureIndex corresponds to this x-position
      int featureIndex = (x * features.length) / width;
      // calculate the bar height for this feature
      int barHeight = Math.min((int)(features[featureIndex] * height), height - 1);
      // draw a vertical line corresponding to the frequency
      // represented by this x-position
      line(x, height, x, height - barHeight);
    } // for
  } // if
} // draw()