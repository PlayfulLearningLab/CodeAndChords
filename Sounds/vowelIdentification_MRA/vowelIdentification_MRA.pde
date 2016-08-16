import beads.*;

AudioContext  ac;
ShortFrameSegmenter  sfs;
FFT  fft;
PowerSpectrum  ps;
Gain  g;

void setup() {
  size(600, 400);
  ac = new AudioContext();
  UGen microphoneIn = ac.getAudioInput();
  g = new Gain(ac, 1, 0.5);
  g.addInput(microphoneIn);
  ac.out.addInput(g);
  sfs = new ShortFrameSegmenter(ac);
  sfs.addInput(microphoneIn);
  fft = new FFT();
  sfs.addListener(fft);
  ps = new PowerSpectrum();
  fft.addListener(ps);
  ac.out.addDependent(sfs);
  ac.start();
} // setup()

void draw() {
  float[] features = ps.getFeatures();
  println(features[0]);
} // draw()