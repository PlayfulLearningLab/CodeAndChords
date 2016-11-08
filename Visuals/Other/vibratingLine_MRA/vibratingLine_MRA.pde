/*
  07/15/2016
  Michaela Andrews
  
  Vibrating Line
    Based off "Simple Oscilloscope" by Oolong - super cool!
    https://forum.processing.org/two/discussion/1129/simple-oscilloscope
*/

int amp = 500;
import ddf.minim.*;
 
Minim minim;
AudioInput in;
 
void setup() {
  size(800, 600, P3D);
  minim = new Minim(this);
  in = minim.getLineIn();
  println (in.bufferSize());
  background(0);
} // void setup()
 
void draw() {
  //background(0);
  fill(20, 0, 50, 10);
  rect(0, 0, width, height);
  stroke (200);
  int crossing=0;
  // draw the waveforms so we can see what we are monitoring
  for(int i = 0; i < in.bufferSize() - 1 && i < width+crossing; i++) {
    if (crossing==0 && in.left.get(i) < 0 && in.left.get(i+1) > 0) {
      crossing = i;
    } // if
    if (crossing!=0){
      line( i-crossing, height/2 + in.left.get(i)*amp, i+1-crossing, height/2 + in.left.get(i+1)*amp );
    } // if
  } // for
} // void draw()