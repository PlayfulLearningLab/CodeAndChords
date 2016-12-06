import ddf.minim.analysis.*;
import ddf.minim.*;

/*
  12/05/2016
  Moved to ProtoModules.

  Emily Meuer
  04/15/2016
  
  Image fades and reveals a background image in response to volume.
*/

PImage      amanda;
FFT         fft;
PImage      gator;
AudioInput  input;
int         level;
Minim       minim;

void setup()
{
  size(384,640);
  
  minim  = new Minim(this);
  input = minim.getLineIn();
  
  gator  = loadImage("gator_attack.png");
  amanda  = loadImage("amanda.png");
  gator.resize(384,640);  
} // setup()

void draw()
{
  // volume of input:
  level  = (int)Math.floor(input.mix.level() * 1500);
  background(gator);
  // volume used to adjust the transparency:
  tint(255,(Math.min(level,255)));
  image(amanda,0,0,384,640);
} // draw()