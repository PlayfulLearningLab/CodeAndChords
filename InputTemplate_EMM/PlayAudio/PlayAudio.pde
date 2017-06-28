import beads.*;
import java.util.LinkedList;

/*
  10/11/2016:
  Tracks all start a second or two off;
  could I play the track where they're all together, but be reading them simultaneously?
  (it loses the real-time... but a recording *isn't* real-time...)
  
  09/23/2016
  Trying to just play a file!  Copied example from https://groups.google.com/forum/#!searchin/beadsproject/sampleplayer|sort:relevance/beadsproject/98Y7Sa6CRig/Zpw5SWZsLG4J.
*/

String sourceFile; // this will hold the path to our audio file
Sample        sample;
Sample        har1;
Sample        har2;
Sample        har3;
SampleManager  sm;
SamplePlayer sp; // the SamplePlayer class will be used to play the audio file
SamplePlayer  sp2;
Gain g;
Glide gainValue;
AudioContext ac;

int  count  = 0;

void setup()
{
  size(800, 600);
  
  sm  = new SampleManager();
  ac = new AudioContext(); // create our AudioContext

  // what file will we load into our SamplePlayer?
  // notice the use of the sketchPath function
  // this is a very useful function for loading external files in Processing
  sourceFile = sketchPath("Gittin.mp3");

  // whenever we load a file, we need to enclose the code in a Try/Catch block
  // Try/Catch blocks will inform us if the file can't be found
  try {  
    sample  = new Sample(sourceFile);
    
    har1  = new Sample(sketchPath("Horse and Rider 1.mp3"));
    har2  = new Sample(sketchPath("Horse and Rider 2.mp3"));
    har3  = new Sample(sketchPath("Horse and Rider 3.mp3"));
    
    // initialize our SamplePlayer, loading the file indicated by the sourceFile string
    sp = new SamplePlayer(ac, sample);
    sp2  = new SamplePlayer(ac, sample);
   }
  catch(Exception e)
  {
    // if there is an error, show an error message (at the bottom of the processing window)
    println("Exception while attempting to load sample!");
    e.printStackTrace(); // then print a technical description of the error
    exit(); // and exit the program
  }
  
  SampleManager.addToGroup("firstGroup", sample);
  SampleManager.addToGroup("firstGroup", sample);
  
  SampleManager.addToGroup("har", har1);
  SampleManager.addToGroup("har", har2);
  SampleManager.addToGroup("har", har3);
  
  
  // SamplePlayer can be set to be destroyed when it is done playing
  // this is useful when you want to load a number of different samples, but only play each one once
  // in this case, we would like to play the sample multiple times, so we set KillOnEnd to false
  sp.setKillOnEnd(false);
  sp2.setKillOnEnd(false);

  // as usual, we create a gain that will control the volume of our sample player
  gainValue = new Glide(ac, 0.0, 20);
  g = new Gain(ac, 1, 0.5);
//  g.addInput(sp); // connect the SamplePlayer to the Gain
//  g.addInput(sp2);

println("SampleManager.getGroup('har').size() = " + SampleManager.getGroup("har").size());

  for(int i = 0; i < SampleManager.getGroup("har").size(); i++)
  {
    g.addInput(new SamplePlayer(ac, SampleManager.getGroup("har").get(i)));
  } // for

  ac.out.addInput(g); // connect the Gain to the AudioContext

  ac.start(); // begin audio processing
  
  background(0); // set the background to black
  text("Click to demonstrate the SamplePlayer object.", 100, 100); // tell the user what to do!

//  gainValue.setValue((float)mouseX/(float)width); // set the gain based on mouse position
  gainValue.setValue(1);
  sp.setToLoopStart();
//  sp.start();
//  sp2.start();
 }

// although we're not drawing to the screen, we need to have a draw function
// in order to wait for mousePressed events
void draw()
{
  if(count == 0) {
    println("count = " + count);
//    startSamplePlayer();
    count++;
  } else {
    count++;
  }
}

void startSamplePlayer()
{
  println("in startSamplePlayer");
  gainValue.setValue(1); //(float)mouseX/(float)width); // set the gain based on mouse position
  println("gainValue = " + gainValue.getValue());
  sp.setToLoopStart(); // move the playback pointer to the first loop point (0.0)
  sp.start(); // play the audio file
  println("sp.paused = " + sp.isPaused());
} // startSamplePlayer
/*
// this routine is called whenever a mouse button is pressed on the Processing sketch
void mousePressed()
{
  gainValue.setValue((float)mouseX/(float)width); // set the gain based on mouse position
  sp.setToLoopStart(); // move the playback pointer to the first loop point (0.0)
  sp.start(); // play the audio file
}
*/