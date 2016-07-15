/*
  07/15/2016
  Michaela Andrews
  
  Vibrating Line
  Based off "Simple Oscilloscope" by Oolong
*/

import ddf.minim.*;
 
Minim minim;
AudioInput in;
 
void setup()
{
  size(800, 600, P3D);
 
  minim = new Minim(this);
 
  // use the getLineIn method of the Minim object to get an AudioInput
  in = minim.getLineIn();
  println (in.bufferSize());
  // uncomment this line to *hear* what is being monitored, in addition to seeing it
  //in.enableMonitoring();
  background(0);
}
 
void draw()
{
  //background(0);
  fill (0, 32, 0, 32);
  rect (0, 0, width, height);
  stroke (32);
  for (int i = 0; i < 11 ; i++){
    line (0, i*75, width, i*75);
    line (i*75+25, 0, i*75+25, height);
  }
  stroke (0);
  line (width/2, 0, width/2, height);
  line (0, height/2, width, height/2);
  stroke (128,255,128);
  int crossing=0;
  // draw the waveforms so we can see what we are monitoring
  for(int i = 0; i < in.bufferSize() - 1 && i<width+crossing; i++)
  {
    if (crossing==0&&in.left.get(i)<0&&in.left.get(i+1)>0) crossing=i;
    if (crossing!=0){
      line( i-crossing, height/2 + in.left.get(i)*300, i+1-crossing, height/2 + in.left.get(i+1)*300 );
    }
  }
}