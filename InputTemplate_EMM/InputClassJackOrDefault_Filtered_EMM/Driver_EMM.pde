import beads.Glide;

/*
  07/06/2016
 Emily Meuer
 
 Sketch to test which mic routes to which input line.
 */

Input  input;
int    wide;
int    high;

AudioContext ac;
Glide        glide;
Gain         g;
Sample        sample;
SamplePlayer  samplePlayer;

void settings()
{
  size(400, 400);
} // settings()

void setup()
{
  size(400, 400);

  //  input  = new Input(12, new AudioContext());
  //  input  = new Input();
  wide   = 4;
  high   = 4;
  
  String path  = sketchPath() + "/Gittin.mp3";

  try
  {
//    sample = SampleManager.sample(path);
    sample = new Sample(path);
    samplePlayer = new SamplePlayer(new AudioContext(), sample);
  } 
  catch(Exception e)
  {
    e.printStackTrace();
    throw new IllegalArgumentException("InputClassJackOrDefault_Filtered.Driver.setup(): error loading sample " + path);
  } // catch
  samplePlayer.setKillOnEnd(false);
  
  ac = new AudioContext();
  
  glide = new Glide(ac, 0.0, 20);
  g = new Gain(ac, 1, glide);
  g.addInput(samplePlayer);
  
  ac.out.addInput(g);

  ac.start();
  
   background(0); // set the background to black
 text("Click to demonstrate the SamplePlayer object.",
 100, 100); // tell the user what to do!
  
  /*
  samplePlayer.setToLoopStart();
  samplePlayer.start();
  */
} // setup()

void draw()
{
  //  background(100);
  /*
  background(input.getAdjustedFund(1) / 2, 50, input.getAdjustedFund(1) / 2);
   color  curColor;
   
   for(int i = 1; i < input.getNumInputs(); i++)
   {
   curColor  = color(i * 18, i * 10, i * 18);
   fill(curColor);
   
   //    println("input.getAmplitude(" + i + ") = " + input.getAmplitude(i));
   if(input.getAmplitude(i) > 3.5)
   {
   int x  = ((i-1) % wide) * (width / wide);
   int y  = ((i-1) / wide) * (height / high);
   rect(x, y, 100, 100);
   
   textSize(32);
   fill(255);
   text(i, (x+25), (y+50));
   } // if - amplitude
   } // for
   */
} // draw()

// this routine is called whenever a mouse button is
// pressed on the Processing sketch
void mousePressed()
{
 // set the gain based on mouse position
 glide.setValue((float)mouseX/(float)width);
 // move the playback pointer to the first loop point (0.0)
 samplePlayer.setToLoopStart();
 samplePlayer.start();
 
 println("samplePlayer should be going...");
}