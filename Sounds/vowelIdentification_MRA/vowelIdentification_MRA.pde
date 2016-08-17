/*
  08/16/2016
  Michaela Andrews
  
  Displaying frequency information
  Goal is to distinguish between vowels
  
  Modified examples from Ch. 7 and Ch. 9 of Sonifying Processing
  example from Ch. 7 modified for microphone input instead of mp3 input
  the Ch. 7 example in the book is based off of an examples by Ollie Brown included in the Beads download
  example from Ch. 9 to find strongest frequencies (without the reproduction part)
*/

int wait = 2000;

import beads.*;
AudioContext ac;
PowerSpectrum ps;
SpectralPeaks sp;
int numPeaks = 10;
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
  // the SpectralPeaks object sotres the N hightest peaks
  sp = new SpectralPeaks(ac, numPeaks);
  //connect the PowerSpectrum to the Frequency object
  ps.addListener(sp);
  // list the frame segmenter as a dependent, so that the
  ac.out.addDependent(sfs);
  // start processing audio
  ac.start();
} // setup()

void draw() {
  background(0);
// finding the strongest frequencies
  float pitchAndFormants[][] = sp.getFeatures();
  //if(millis() > wait){
  //    //printArray(features);
  //    wait = wait+1000;
  //    for(int j = 0; j < pitchAndFormants.length; j++){
  //        println(j, pitchAndFormants[j][0],", ", pitchAndFormants[j][1]);
  //      }
  //  }
// finding I ("ee")
  for(int i=0; i<pitchAndFormants.length; i++){
    if(pitchAndFormants[i][0] > 2000 && pitchAndFormants[i][1] > 0.001)
      background(0,0,100);
  }
// drawing the bars
  // The getFeatures() function is a key part of the Beads
  // analysis library. It returns an array of floats.
  // How this array of floats is defined (1 dimension, 2
  // dimensions ... etc) is based on the calling unit
  // generator. In this case, the PowerSpectrum returns an
  // array with the power of 256 spectral bands.
  stroke(222);
  float[] features = ps.getFeatures();
  if(features != null) { // if any features are returned
    for(int x = 0; x < width; x++) {
      int featureIndex = (x * features.length) / width; // figure out which featureIndex corresponds to this x-position
      // calculate the bar height for this feature
      int barHeight = Math.min((int)(features[featureIndex] * height), height - 1);
      line(x, height, x, height - barHeight); // draw a vertical line corresponding to the frequency represented by this x-position
    } // for
  } // if
} // draw()