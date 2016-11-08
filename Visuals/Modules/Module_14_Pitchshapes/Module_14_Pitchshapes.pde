import interfascia.*;

/**
 * 11/1/16
 * Elena Ryan
 *
 * shape changes based on pitch.
 * SWITCH INPUT TO MOST RECENT VERSION
 * (Adapted from Examples => Color => Hue as well as Background_hue module)
 */


int  hueMax;
int  saturationMax;
int  brightnessMax;

Input  input;
int  threshold;      // when amp is below this, the background will be black.




shapeArray[] shapArr;

float time;

int  newShapePos;
int  newHuePos;
int  goalHuePos;
int  curHuePos;
int  oldHuePos;
float[] newHue;



float[][]  colors;          // holds the RGB values for the colors responding to HSB: every 30th H with 100 S, 100 B



void setup() 
{
  size(638, 360);
  time           = millis()+1000;
  

  //  colorMode(HSB, hueMax, saturationMax, brightnessMax);
  colors  = new float[][] {
    { 255, 0, 0 }, 
    { 255, 127.5, 0 }, 
    { 255, 255, 0 }, 
    { 127.5, 255, 0 }, 
    { 0, 255, 0 }, 
    { 0, 255, 127.5 }, 
    { 0, 255, 255 }, 
    { 0, 127.5, 255 }, 
    { 0, 0, 255 }, 
    { 127.5, 0, 255 }, 
    { 255, 0, 255 }, 
    { 255, 0, 127.5 }, 
    { 255, 0, 0 }
  };
  shapArr = new shapeArray[colors.length];
  for (int s = 0; s <colors.length; s++) {
        shapArr[s] =  new shapeArray(color(colors[s][0],colors[s][1],colors[s][2]),random(20,40), random(1, 10), s);
    
  }



  input        = new Input("C Major Scale");
  threshold    = 15;

  noStroke();
  background(0);

  curHuePos    = round(input.getAdjustedFundAsMidiNote(1) % 12);
  oldHuePos    = round(input.getAdjustedFundAsMidiNote(1) % 12);
 

  
  // sets the text size used to display note names:
  textSize(24);
  
  // draws the legend
  legend();

} // setup()

void draw() 
{
newShapePos   = round(input.getAdjustedFundAsMidiNote(1) % 12);

  if (millis() > time  || newShapePos != oldHuePos) {
    
  background(0);
  stroke(255);
  oldHuePos = round(input.getAdjustedFundAsMidiNote(1) % 12);
  if (input.getAmplitude() > threshold)
  {
    // if want something other than C to be the "tonic" (i.e., red),
    // add some number before multiplying.
    newShapePos  = round(input.getAdjustedFundAsMidiNote(1) % 12);
    newHuePos = newShapePos;
    newHue  = colors[newHuePos];
   // shapArr[newHuePos].display();
    //  newHue  = newHue * 30;  // this is for HSB, when newHue is the color's H value
  } else {
    newHue  = new float[] { 0, 0, 0 };
  } // else

  // set goalHue to the color indicated by the current pitch:
  if (newHuePos != goalHuePos)
  {
    goalHuePos  = newHuePos;
  } // if
  //goalHue  = colors[goalHuePos];
  
 /** for (int i = 0; i < 3; i++)
  {
    if (curHue[i] > (goalHue[i] - attackTime))
    {
      curHue[i] = curHue[i] - attackTime;
    } else if (curHue[i] < (goalHue[i] + attackTime))
    {
      curHue[i]  = curHue[i] + attackTime;
    } // if
  } // for*/



  
  shapArr[goalHuePos].display();
  stroke(255);
  // draws the legend along the bottom of the screen:
  legend();
  time = millis() +1000;
    }
} // draw()

void legend()
{
  String[] notes = new String[] {
    "C", 
    "C#", 
    "D", 
    "D#", 
    "E", 
    "F", 
    "F#", 
    "G", 
    "G#", 
    "A", 
    "A#", 
    "B",
    "C"
  }; // notes

  float  side = width / colors.length;

  stroke(255);

  for (int i = 0; i < colors.length; i++)
  {
    fill(colors[i][0], colors[i][1], colors[i][2]);
    if(i == goalHuePos) {
      rect((side * i), height - (side * 1.5), side, side * 1.5);
    } else {
      rect((side * i), height - side, side, side);
    }
    fill(0);
    text(notes[i], (side * i) + side/2 - 10, height - (side / 3));
  } // for
} // legend