import interfascia.*;

/**
 * 08/01/2016
 * Emily Meuer
 *
 * Background changes hue based on pitch.
 *
 * (Adapted from Examples => Color => Hue.)
 */

// Choose input file here:
String  inputFile  = "Emily_CMajor-2016_09_2-16bit-44.1K Raw.wav";
//String  inputFile  = "Emily_CMajor-2016_09_2-16bit-44.1K Tuned.wav";
//String  inputFile  = "Emily_CMajor-2016_09_2-16bit-44.1K Kanye.wav";

// Lable for the scrollbar:
GUIController controller;
IFTextField   textField;
IFLabel       label;

int  hueMax;
int  saturationMax;
int  brightnessMax;

Input  input;
int  threshold;      // when amp is below this, the background will be black.

int  hue;
int  saturation;

float[]  newHue;
float[]  goalHue;
float[]  curHue;
float    attackTime;

int  newHuePos;
int  goalHuePos;
int  curHuePos;

int  newSaturation;
int  goalSaturation;
int  curSaturation;
int  changeInSaturation;

float[][]  colors;          // holds the RGB values for the colors responding to HSB: every 30th H with 100 S, 100 B

HScrollbar  scrollbar;
float       scrollbarPos;
float       attackTimeMax  = 20;
float       attackTimeMin  = 1;

void setup() 
{
  size(750, 422);



  hueMax         = 360;
  saturationMax  = 300;
  brightnessMax  = 100;

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

  /*
  // this draws rectangles of the colors across the screen:
   for (int i = 0; i < colors.length; i++)
   {
   fill(colors[i][0], colors[i][1], colors[i][2]);
   rect((width / colors.length) * i, 0, (width / colors.length), height);
   } // for
   */

  input        = new Input(inputFile);
  threshold    = 15;

  noStroke();
  background(0);

  //  curHuePos    = round(input.getAdjustedFundAsMidiNote(1) % 12) * 30;
  curHuePos    = round(input.getAdjustedFundAsMidiNote(1) % 12);
  curHue       = colors[curHuePos];
  // would like to change more quickly, but there's a weird flicker if attackTime gets bigger:
  attackTime  = 10;

  curSaturation       = (int)Math.min(input.getAmplitude(1), 300);
  changeInSaturation  = 10;


  // sets the text size used to display note names:
  textSize(24);

  // draws the legend
  legend();

  scrollbar = new HScrollbar(10, 45, (width / 2) - 10, 18, 5);

  controller = new GUIController(this);
  textField  = new IFTextField("Text Field", 10, 10, 150);
  label      = new IFLabel("attackTime: " + attackTime, 15, 15);

  controller.add(textField);
  controller.add(label);

  textField.addActionListener(this);

  println("scrollbar.ratio = " + scrollbar.ratio);
} // setup()

void draw() 
{
  stroke(255);
  if (input.getAmplitude() > threshold)
  {
    // if want something other than C to be the "tonic" (i.e., red),
    // add some number before multiplying.
    newHuePos  = round(input.getAdjustedFundAsMidiNote(1) % 12);
    newHue  = colors[newHuePos];
    //  newHue  = newHue * 30;  // this is for HSB, when newHue is the color's H value
  } else {
    newHue  = new float[] { 0, 0, 0 };
  } // else

  // set goalHue to the color indicated by the current pitch:
  if (newHuePos != goalHuePos)
  {
    goalHuePos  = newHuePos;
  } // if
  goalHue  = colors[goalHuePos];

  for (int i = 0; i < 3; i++)
  {
    if (curHue[i] > (goalHue[i] - attackTime))
    {
      curHue[i] = curHue[i] - attackTime;
    } else if (curHue[i] < (goalHue[i] + attackTime))
    {
      curHue[i]  = curHue[i] + attackTime;
    } // if
  } // for

  // Adjust saturation with amplitude by uncommenting the following lines and 
  // commenting the "curSaturation = saturationMax" line.
  // ** This no longer works because of RGB! :(
  // ^ Chillax and change the color mode.
  /*
  newSaturation  = (int)Math.min(input.getAmplitude(1), 300);
   println("input.getAmplitude(1) = " + input.getAmplitude(1));
   
   if(newSaturation != goalSaturation) {
   goalSaturation  = newSaturation;
   } //if
   
   if(curSaturation > goalSaturation) {
   curSaturation = curSaturation - changeInSaturation;
   } else if(curSaturation < goalSaturation) {
   curSaturation = curSaturation + changeInSaturation;
   } // if
   */

  curSaturation  = saturationMax;

  //  println("curSaturation = " + curSaturation + "; goalSaturation = " + goalSaturation);
  background(curHue[0], curHue[1], curHue[2]);

  stroke(255);
  triangle( 710, 10, 710, 30, 730, 20);
  // draws the legend along the bottom of the screen:
  legend();

  scrollbar.update();
  scrollbar.display();

  scrollbarPos  = scrollbar.getPos();
  attackTime      = map(scrollbarPos, scrollbar.sposMin, scrollbar.sposMax, attackTimeMin, attackTimeMax);

  label.setLabel("attackTime: " + attackTime);

  /*
  // ellipse for testing different colors - for my benefit, but could be cool.
   fill(255, 127.5, 0);
   //  fill(colors[0][0], colors[0][1], colors[0][2]);
   ellipse(width / 2, height / 2, 100, 100);
   */
} // draw()

void legend()
{

  textSize(15);

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

  // 12/19: updating to be on the side.
  //  float  side = width / colors.length;
  float  side = height / colors.length;

  stroke(255);

  for (int i = 0; i < colors.length; i++)
  {
    fill(colors[i][0], colors[i][1], colors[i][2]);
    if (i == goalHuePos) {
      //      rect((side * i), height - (side * 1.5), side, side * 1.5);
      rect(0, (side * i), side * 1.5, side);
    } else {
      //      rect((side * i), height - side, side, side);
      rect(0, (side * i), side, side);
    }
    fill(0);
    text(notes[i], 7, (side * i) + side * 0.75);
  } // for
} // legend

// Not sure that this really does anything right now:
void actionPerformed(GUIEvent e) {
  if (e.getMessage().equals("Completed")) {
    label.setLabel(textField.getValue());
  }
} // actionPerformed