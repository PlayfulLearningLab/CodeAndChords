import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import interfascia.*; 
import org.jaudiolibs.beads.AudioServerIO; 
import org.jaudiolibs.beads.*; 
import beads.AudioContext; 
import beads.AudioIO; 
import beads.IOAudioFormat; 
import beads.UGen; 
import beads.Gain; 
import beads.ShortFrameSegmenter; 
import beads.FFT; 
import beads.PowerSpectrum; 
import beads.Frequency; 
import beads.Pitch; 
import beads.Compressor; 
import javax.sound.sampled.AudioFormat; 
import beads.FeatureExtractor; 
import beads.TimeStamp; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Module_02_PitchHueBars_EMM extends PApplet {

 //<>// //<>//

/**
 * 09/11/2016: Question about changing on/off state:
 *  Should I make a boolean for each button, or just keep track of how many times
 *  each has been pushed?  -- Probably just a boolean.  More obvious.
 *
 * 08/01/2016
 * Emily Meuer
 *
 * Moving bars change hue based on pitch and saturation based on amplitude.
 *
 *  Things to change to calibrate saturation:
 *   - saturationMax: will change how soon the saturation maxes out; higher values take higher volume.
 *   - saturation:    set in fillBars(); simply takes the amplitude as the saturation value,
 *                      so could map to allow more control.
 *
 * (Adapted from Examples => Color => Hue.)
 *
 *  09/08: Could try to do some cool enum case thing (see this example - 
 *  https://schneide.wordpress.com/2010/12/13/avoid-switch-use-enum/)
 *  but running into too much static weirdness to mess with it at this point.
 */

int    barWidth = 10;
int    lastBar = -1;
int    howManyBars;

//int    hue;
float    saturation;
boolean  saturationBool  = false;  // determines whether or not saturation is determined by amp.

boolean  timeBool  = true;
boolean  pitchBool = false;
boolean  scaleBool = false;
boolean[] bools = {
  timeBool, 
  pitchBool, 
  scaleBool
};

int    hueMax;
int    saturationMax;
int    brightnessMax;

Input  input;
int    threshold = 1;     // This is the amplitude below which color will be black.

int        delay;
float[][]  bars;          // bars[i][0], bars[i][1], bars[i][2] denote the color; 
// bars[i][3] is 0 to signify solid color and 1 for gradient.
//color[]  bars;


int[]  majorScaleDegrees;

GUIController  controller;
IFButton       timeButton;
IFButton       pitchButton;
IFButton       majorScaleButton;
IFButton       saturationButton;
IFButton[]     buttons = new IFButton[] {
  timeButton, 
  pitchButton, 
  majorScaleButton
} ;                // includes all but saturationButton, since this needs to not.

// Won't go at it this way.  Change the default one, if accessible.
IFLookAndFeel  darkGrayClicked;
IFLookAndFeel  defaultLAF; // = new IFLookAndFeel(IFLookAndFeel.DEFAULT);

int  buttonX = 20;

HScrollbar   scrollbar;
float        scrollbarPos;
IFTextField  textField;
IFLabel      label;
int          bpmMin  = 20;
int          bpmMax  = 600;
int          bpm;

int    controlledBy  = 0;

// Used by the legend:
int  newHuePos;
int  goalHuePos;
int  curHuePos;
int  goalHue;

public void setup() 
{
  

  hueMax         = 360;
  saturationMax  = 100;
  brightnessMax  = 100;

  colorMode(HSB, hueMax, saturationMax, brightnessMax);

  input          = new Input(); 
  noStroke();
  background(0);

  howManyBars  = width / barWidth;
  bars         = new float[howManyBars][4];
  //  bars   = new color[howManyBars];
  // fills the color[] with white to start:
  for (int i = 0; i < bars.length; i++) 
  {
    bars[i][0]  = 0;
    bars[i][1]  = 0;
    bars[i][2]  = brightnessMax;
    bars[i][3]  = 0;
    //    bars[i]  = color(0, 0, brightnessMax);
  } // for

  majorScaleDegrees  = new int[]  {
    0, 
    2, 
    4, 
    5, 
    7, 
    9, 
    11, 
    12
  };

  controller   = new GUIController(this);
  //  timeButton   = new IFButton("Time-based", buttonX, height - 140);
  buttons[0]   = new IFButton("Time-based", buttonX, height - 140);
  buttons[1]  = new IFButton("Pitch-based", buttonX, height - 120);
  buttons[2]  = new IFButton("C Maj scale (time-based)", buttonX, height - 100);
  saturationButton  = new IFButton("Saturation", buttonX + buttons[0].getWidth(), height - 100);

  buttons[0].addActionListener(this);
  buttons[1].addActionListener(this);
  buttons[2].addActionListener(this);
  saturationButton.addActionListener(this);

  controller.add(buttons[0]);
  controller.add(buttons[1]);
  controller.add(buttons[2]);
  controller.add(saturationButton);

  darkGrayClicked = new IFLookAndFeel(this, IFLookAndFeel.DEFAULT);
  darkGrayClicked.baseColor = color(240, saturationMax, 30);
  darkGrayClicked.textColor = color(255);
  darkGrayClicked.highlightColor = color(240, saturationMax, 15);

  defaultLAF = new IFLookAndFeel(this, IFLookAndFeel.DEFAULT);

  // set the timeButton's LAF because it is the default mode:
  buttons[0].setLookAndFeel(darkGrayClicked);


  scrollbar = new HScrollbar(width - 200, height - 90, 150, 18, 5);
  textField = new IFTextField("Text Field", width - 200, height - 125, 75);
  label     = new IFLabel("BPM: " + bpm, width - 197, height - 120);

  controller.add(textField);
  controller.add(label);

  textField.addActionListener(this);

  delay  = millis();
}

public void draw() 
{
  noStroke();

  if (saturationBool) {
    saturation  = Math.min(input.getAmplitude(1)/ 2, 100);
  } else {
    saturation = saturationMax;
  } // else - saturation


  //  newHuePos  = round(curNote % 12);
  // set goalHue to the color indicated by the current pitch:

  if (controlledBy == 0)
  {
    barsMoveOnDelay(150);
  } else if (controlledBy == 1)
  {
    barsMoveOnPitchChange();
  } else if (controlledBy == 2)
  {
    barsMoveOnDelayMajorScale();
  } else {
    controlledBy = 0;
  } // else


  if (newHuePos != goalHuePos)
  {
    goalHuePos  = newHuePos;
  } // if
  goalHue  = goalHuePos * 30;

  legend();

  scrollbar.update();
  scrollbar.display();

  // I think scrollbar.getPos() is confused.
  scrollbarPos  = scrollbar.getPos();
  bpm      = round(map(scrollbarPos, scrollbar.sposMin, scrollbar.sposMax, bpmMin, bpmMax));
//  println("scrollbarPos = " + scrollbarPos + "; scrollbar.sposMin = " + scrollbar.sposMin + "; scrollbar.sposMax = " + scrollbar.sposMax);
//  println("  scrollbar.ratio = " + scrollbar.ratio);
  println("bpm = " + bpm + "; bpmMin = " + bpmMin + "; bpmMax = " + bpmMax);

  label.setLabel("BPM: " + bpm);
} // draw()

/**
 *  Moves the bars every 100 milliseconds, and changes colors only if the current pitch
 *  is in the major scale.
 */
public void barsMoveOnDelayMajorScale()
{
  int  scaleDegree  = round(input.getAdjustedFundAsMidiNote(1) % 12);
  newHuePos  = scaleDegree;

  if (millis() > delay)
  {
    if (input.getAmplitude() > threshold)
    {
      if (arrayContains(majorScaleDegrees, scaleDegree)) {
        // these pitches are diatonic:
        fillBars(scaleDegree * 30, saturation, brightnessMax, 0);
      } // else: do something for non-diatonic here
      else
      {
        // these pitches are non-diatonic:

        if (saturationBool) {
          // saturationButton is on:          
          fillBars(scaleDegree * 30, saturation, brightnessMax, 1);
        } else {
          // saturationButton is off:
          fillBars(scaleDegree * 30, (saturation * 0.25f), brightnessMax, 0);
        }
        //       fillBars(scaleDegree * 30,
      } // else -- diatonic or not
    } else { 
    } // else - no sound

    drawBarsGradient();
    delay = millis() + 100;
  } // if - delay && contains
} // barsMoveOnDelayMajorScale

public boolean  arrayContains(int[] array, int element)
{
  //  println("array.length = " + array.length);
  for (int i = 0; i < array.length; i++)
  {
    //    println("array[i] = " + array[i]);
    if (array[i] == element) {
      return true;
    } // if
  } // for

  return false;
} //

/**
 *  Call fillAndDrawBars whenever a new pitch is detected
 *  with the hue detected from that pitch's scale degree (and saturationMax and brightnessMax).
 *
 *  Not very elegant-looking right now, but it could be more of an operator error, due to my singing?
 */
public void barsMoveOnPitchChange()
{
  int  hue;
  int  brightness;

  if (input.getAmplitude() > threshold)
  {
    hue = round(input.getAdjustedFundAsMidiNote(1) % 12);
    newHuePos = hue;
    hue = hue * 30;

    if (saturationBool) {
      saturation  = Math.min(input.getAmplitude(1), 100);
      //  saturation  = map(saturation, 0, 200, 0, 100);
    } else {
      saturation = saturationMax;
    }

    brightness = brightnessMax;
  } else {
    hue = 0;
    saturation = 0;
    brightness = 0;
  } // else - rule

  if (color(hue, saturation, brightness) != color(bars[bars.length - 1][0], bars[bars.length - 1][1], bars[bars.length - 1][2]))
  {
    fillBars(hue, saturation, brightness, 0);
    drawBarsGradient();
    //    fillAndDrawBars(hue, saturation, brightness);
  }
} // barsMoveOnPitchChange

/**
 *  Calls the fillAndDrawBars function every millisDelay milliseconds,
 *  passing it the current scale degree (from 0 to 11) and saturationMax.
 *
 *  @param  millisDelay  milliseconds at which the the bars change;
 *  the longer this is, the fewer false whites come in (basically, it's less sensitive).
 */
public void barsMoveOnDelay(int millisDelay)
{
  if (millis() > delay)
  {

    if (input.getAmplitude() > threshold) 
    {
      // round moves this to the nearest midi note:
      newHuePos = round(input.getAdjustedFundAsMidiNote()) % 12;
      //      fillAndDrawBars(newHuePos * 30, saturation, brightnessMax);
      fillBars(newHuePos * 30, saturation, brightnessMax, 0);
    } else {
      fillBars(0, 0, 0, 0);
    } // else - silence is black


    drawBarsGradient();
    delay += millisDelay;
  } // if
} // barsMoveInTime

public void legend()
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

  float  side = width / 13;

  stroke(255);

  for (int i = 0; i < 13; i++)
  {
    fill(i * 30, saturationMax, brightnessMax);
    if (i == goalHuePos) {
      rect((side * i), height - (side * 1.5f), side, side * 1.5f);
    } else {
      rect((side * i), height - side, side, side);
    }
    fill(0);
    text(notes[i], (side * i) + side/2 - 10, height - (side / 3));
  } // for
} // legend

/**
 *  Moves each color in the bars array up on position, and fills the last position
 *  with a color whose hue is derived from pitch earlier in the program
 *  and passed in as a parameter here.
 */
public void fillBars(float hue, float saturation, float brightness, float diatonic)
{
  // move each color in the array up one position:
  for (int i = 0; i < bars.length - 1; i++)
  {
    //    bars[i] = bars[i+1];

    bars[i][0]  = bars[i+1][0];
    bars[i][1]  = bars[i+1][1];
    bars[i][2]  = bars[i+1][2];
    bars[i][3]  = bars[i+1][3];
  } // for

  // set the last position of the array to the current, pitch/amp-derived color:
  bars[bars.length - 1][0] = hue;
  bars[bars.length - 1][1] = saturation;
  bars[bars.length - 1][2] = brightness;
  bars[bars.length - 1][3] = diatonic;
} // fillBars(int, int, int, int)

/**
 *  Loops through the bars array and draws rectangles across the screen
 *  in the colors that are in their respective positions in the array;
 */
public void drawBars()
{
  for (int i = 0; i < bars.length; i++)
  {
    int barX = i * barWidth;

    fill(bars[i][0], bars[i][1], bars[i][2]);
    rect(barX, 0, barWidth, height);
  } // for
} // drawBars()

/**
 *  Loops through the bars array and draws rectangles across the screen
 *  in the colors that are in their respective positions in the array,
 *  drawing those whose [i][3] position is 1 in a gradient from white to that color.
 */
public void drawBarsGradient()
{
  for (int i = 0; i < bars.length; i++)
  {
    int barX = i * barWidth;

    // color the rectangle normally for diatonic tones:
    if (bars[i][3] == 0) {
      fill(bars[i][0], bars[i][1], bars[i][2]);
      rect(barX, 0, barWidth, height);
    } else {
      // color with a gradient for non-diatonic tones:
      // adapted from the Linear Gradient example here: https://processing.org/examples/lineargradient.html
      int y = 0;
      int x = barX;
      int h = height;
      int w = barWidth;
      int c1 = color(0, 0, brightnessMax);
      int c2 = color(bars[i][0], bars[i][1], bars[i][2]);

      for (int j = y; j <= y+h; j++) {
        float inter = map(j, y, y+h, 0, 1);
        int c = lerpColor(c1, c2, inter);
        stroke(c);
        line(x, j, x+w, j);
      } // for
    } // else

    // avoids colored outline of black "silence" bars:
    noStroke();
  } // for
} // drawBars()

/**
 *  Moves each color in the bars array up on position, and fills the last position
 *  with a color whose hue is derived from pitch earlier in the program
 *  and passed in as a parameter here.
 *
 *  @param  hue         an int, from 0 to 11, denoting the current scale degree of the input.
 *  @param  saturation  a float that will be used to determine the saturation of the HSB color.
 *  @param  brightness  an int that will be used to determine the brightness of the HSB color.
 */
public void fillAndDrawBars(int hue, float saturation, int brightness)
{
  // move each color in the array up one position:
  for (int i = 0; i < bars.length - 1; i++)
  {
    //    bars[i] = bars[i+1];

    bars[i][0]  = bars[i+1][0];
    bars[i][1]  = bars[i+1][1];
    bars[i][2]  = bars[i+1][2];
    bars[i][3]  = bars[i+1][3];
  } // for

  bars[bars.length - 1][0] = hue;
  bars[bars.length - 1][1] = saturation;
  bars[bars.length - 1][2] = brightness;
  bars[bars.length - 1][3] = 0;

  // Draw bars:
  for (int i = 0; i < bars.length; i++)
  {
    int barX = i * barWidth;

    fill(bars[i][0], bars[i][1], bars[i][2]);
    rect(barX, 0, barWidth, height);
  } // for
} // fillBars

public void actionPerformed(GUIEvent e)
{
  if (e.getSource() != saturationButton)
  {

    if (e.getSource() == buttons[0])
    {
      println("timeButton was clicked.");
      controlledBy = 0;
      //      timeBool = !timeBool;

      /*
      if (timeBool) {
       timeButton.setLookAndFeel(darkGrayClicked);
       } else {
       timeButton.setLookAndFeel(defaultLAF);
       } // else
       */
    } // if - timeButton

    if (e.getSource() == buttons[1])
    {
      println("pitchButton was clicked.");
      controlledBy = 1;
      //     pitchBool    = !pitchBool;
    } // if - pitchButton

    if (e.getSource() == buttons[2])
    {
      println("majorScaleButton was clicked.");
      controlledBy = 2;
      //     scaleBool = !scaleBool;
    } // if - majorScaleButton


    // whichever button was just clicked is turned on or off:
    bools[controlledBy] = !bools[controlledBy];
    println("bools[controlledBy] = " + bools[controlledBy]);

    for (int i = 0; i < bools.length; i++)
    {
      if (i != controlledBy) {
        // all buttons not currently clicked are set to false:
        bools[i] = false;
        buttons[i].setLookAndFeel(defaultLAF);
      } // if
    } // for

    if (bools[controlledBy]) {
      // if the button was turned on, set to clicked:
      buttons[controlledBy].setLookAndFeel(darkGrayClicked);
    } else {
      // if the button was turned off, set to unclicked:
      buttons[controlledBy].setLookAndFeel(defaultLAF);
    } // else
  } // not saturationButton
  else {
    println("saturationButton was clicked.");
    saturationBool = !saturationBool;

    if (saturationBool) {
      saturationButton.setLookAndFeel(darkGrayClicked);
    } else {
      saturationButton.setLookAndFeel(defaultLAF);
    } // else
  } // if  - saturationButton
} // actionPerformed


















class Input
{
  /*
  10/05/2016
  Using the Harmonic Product Spectrum to better locate the pitch.
  
   ** As of mid-July, 2016, the following is NOT true:
    "Notate bene: inputNum passed to constructor must be 4 greater
    than the actual desired number of inputs."
  
   ** Watch for that NullPointerException -- add a try-catch? ** 
       - 8/17: added try/catch
   
   (Why doesn't it need jna-jack__.jar in the code folder?  B/c it's in the old one?)
   
   Emily Meuer
   07/06 Update: works with both the Behringer and Motu interfaces
   to get more than 4 inputs!
   
   06/29/2016
   
   Updating InputClass_EMM to communicate with Jack;
   based on BeadsJNA.
   
   To change in classes that implement this:
   - size()/settings() can be in main tab.
   - getFund() etc. takes an int parameter specifying which input is in question.
   
   ** If I pass it an AudioContext, do I have to worry about it having the wrong number of inputs or outputs?
     - an option would be to have them pass the AudioFormat, since that's what has channel nums, ut credo.
   
   */

  AudioContext           ac;
  float[]                adjustedFundArray;    // holds the pitch, in hertz, of each input, adjusted to ignore pitches below a certain amplitude.
  Compressor             compressor;
//  UGen                   inputsUGen;           // initialized with the input from the AudioContext.
  UGen[]                 uGenArray;
  Gain                   g;
  Gain                   mute;
  FFT[]                  fftArray;             // holds the FFT for each input.
  FrequencyEMM[]         frequencyArray;       // holds the FrequencyEMM objects connected to each input.
  float[]                fundamentalArray;     // holds the current pitch, in hertz, of each input.
  int                    numInputs;            // number of lines / mics
//  float                  pitch;                // 
  PowerSpectrum[]        psArray;              // holds the PowerSpectrum objects connected to each input.
  float                  sensitivity;          // amplitude below which adjustedFreq will not be reset
  ShortFrameSegmenter[]  sfsArray;             // holds the ShortFrameSegmenter objects connected to each input.
//  int                    waitUntil;            // number of milliseconds to wait before checking for another key 
  
  /**
   *  Creates an Input object connected to Jack, with the given number of inputs.
   *
   *  @param  numInputs  an int specifying the number of lines in the AudioFormat.
   */
  Input(int numInputs)
  {
      this(numInputs, new AudioContext(new AudioServerIO.Jack(), 512, AudioContext.defaultAudioFormat(numInputs, numInputs)));
  } // constructor - int, AudioContext
  
  /**
   *  Creates an Input object with the given number of inputs and particular AudioContext.
   *
   *  @param  numInputs     an int specifying the number of lines in the AudioFormat.
   *  @param  audioContext  an AudioContext whose input lines will be procurred as a UGen and used for the analysis calculations.
   */
  Input(int numInputs, AudioContext audioContext)
  {
    if(numInputs < 1)  {
      throw new IllegalArgumentException("Input.constructor(int, AudioContext): int parameter " + numInputs + " is less than 1; must be 1 or greater.");
    } // if(numInputs < 1)
    if(audioContext == null) {
      throw new IllegalArgumentException("Input.constructor(int, AudioContext): AudioContext parameter " + audioContext + " is null.");
    } // if(numInputs < 1)
      
    this.numInputs  = numInputs;
    this.ac = audioContext;

    // creates an int[] of the input channel numbers - e.g., { 1, 2, 3, 4 } for a 4 channel input.
    int[]  inputNums  = new int[this.numInputs];
    for (int i = 0; i < this.numInputs; i++)
    {
      inputNums[i]  = i + 1;
      println("inputNums[" + i + "] = " + inputNums[i]);
    } // for

    // get the audio lines from the AudioContext:
//    this.inputsUGen = ac.getAudioInput(inputNums);

    // fill the uGenArray with UGens, each one from a particular line of the AudioContext.
    uGenArray  = new UGen[this.numInputs];
    for (int i = 0; i < uGenArray.length; i++)
    {
      // getAudioInput needs an int[] with the number of the particular line.
      uGenArray[i]  = ac.getAudioInput(new int[] {(i + 1)});
    }

    /*
    Default compressor values:
      threshold - .5
      attack - 1
      decay - .5
      knee - .5
      ratio - 2
      side-chain - the input audio
    */
    // Create a compressor w/standard values:
    this.compressor  = new Compressor(this.ac, 1);
    this.compressor.setRatio(8.0f);

    // Create a Gain, add the Compressor to the Gain,
    // add each of the UGens from uGenArray to the Gain, and add the Gain to the AudioContext:
    g = new Gain(this.ac, 1, 0.5f);
    g.addInput(this.compressor);
    for (int i = 0; i < this.numInputs; i++)
    {
      g.addInput(uGenArray[i]); //<>//
    } // for
    ac.out.addInput(g); //<>//

    // The ShortFrameSegmenter splits the sound into smaller, manageable portions;
    // this creates an array of SFS's and adds the UGens to them:
    this.sfsArray  = new ShortFrameSegmenter[this.numInputs];
    for (int i = 0; i < this.sfsArray.length; i++)
    {
      this.sfsArray[i] = new ShortFrameSegmenter(ac);
      while (this.sfsArray[i] == null) {
      }
      this.sfsArray[i].addInput(uGenArray[i]); //<>//
    }

    // Creates an array of FFTs and adds them to the SFSs:
    this.fftArray  = new FFT[this.numInputs];
    for (int i = 0; i < this.fftArray.length; i++)
    {
      this.fftArray[i] = new FFT();
      while (this.fftArray[i] == null) {
      }
      this.sfsArray[i].addListener(this.fftArray[i]); //<>//
    } // for

    // Creates an array of PowerSpectrum's and adds them to the FFTs
    // (the PowerSpectrum is what will actually perform the FFT):
    this.psArray  = new PowerSpectrum[this.numInputs];
    for (int i = 0; i < this.psArray.length; i++)
    {
      this.psArray[i] = new PowerSpectrum();
      while (this.psArray[i] == null) {
      }
      this.fftArray[i].addListener(psArray[i]); //<>//
    } // for

    // Creates an array of FrequencyEMMs and adds them to the PSs
    // (using my version of the Frequency class - an inner class further down - to allow access to amplitude):
    this.frequencyArray  = new FrequencyEMM[this.numInputs];
    for (int i = 0; i < this.frequencyArray.length; i++)
    {
      this.frequencyArray[i] = new FrequencyEMM(44100);
      while (this.frequencyArray[i] == null) {
      }
      this.psArray[i].addListener(frequencyArray[i]); //<>//
    } // for

    // Adds the SFSs (and everything connected to them) to the AudioContext:
    for (int i = 0; i < this.numInputs; i++)
    {
      ac.out.addDependent(sfsArray[i]); //<>//
    } // for - addDependent

    // Pitches with amplitudes below this number will be ignored by adjustedFreq:
    this.sensitivity  = 10;
    
/*
    // trying to mute the output:
    mute = new Gain(this.ac, 1, 0);
    mute.addInput(this.ac.out);
    ac.out.addInput(mute);
*/

    // Starts the AudioContext (and everything connected to it):
    this.ac.start();

    // Initializes the arrays that will hold the pitches:
    this.fundamentalArray = new float[this.numInputs];
    this.adjustedFundArray = new float[this.numInputs];
    
    // Gets the ball rolling on analysis:
    this.setFund();
  } // constructor(int)

  /**
   * Constructor for creating a one (or two?)-channel Input object 
   * from the machine's default audio input device;
   * does not require Jack.
   */
  Input()
  {
    this(1, new AudioContext());
  } // constructor()

  /**
   *  TODO:  add Beads functionality
   * Constructor for creating an Input object from an audio file.
   *
   * @param  filename  String specifying the audio file.
   */
  Input(String filename)
  {
    this();
    println("InputClassJack_EMM: the constructor Input(String) is undergoing construction.  Your Input object will take audio from the default audio device.");
  } // constructor(String)

  /**
   * Subtracts 4 from the numInputs variable because I added 4
   * to account for the fact that the two interfaces together skip lines 5-8.l
   *
   * @return  int  number of input channels.
   */
  public int  getNumInputs() {
    return this.numInputs;
  } // getNumInputs

  /**
   *  Fills the fundamentalArray and adjustedFundArray with the current pitches of each input line:
   */
  public void setFund()
  { 
    // catching a NullPointer because I'm not sure why it happens and fear a crash during a concert.
    try
    {
      for (int i = 0; i < this.numInputs; i++)
      {
        //     println("setFund(); this.frequencyArray[i] = " + this.frequencyArray[i].getFeatures());
  
        // want to assign the value of .getFeatures() to a variable and check for null,
        // but can't, b/c it returns a float. :/  (So that must not be exactly the problem.)
        if (this.frequencyArray[i].getFeatures() != null) {
          //       println("i = " + i);
          //       println("setFund(); this.fundamentalArray[i] = " + this.fundamentalArray[i] + "this.frequencyArray[i].getFeatures() = " + this.frequencyArray[i].getFeatures());
          this.fundamentalArray[i] = this.frequencyArray[i].getFeatures();
  
          // ignores pitches with amplitude lower than "sensitivity":
          if (this.frequencyArray[i].getAmplitude() > this.sensitivity) {
            this.adjustedFundArray[i]  = this.fundamentalArray[i];
          } // if: amp > sensitivity
        } // if: features() != null
      } // if: > numInputs
    } catch(NullPointerException npe)  {}
  } // setFund

  /**
   *  @return  pitch (in Hertz) of the Input, adjusted to ignore frequencies below a certain volume.
   */
  public float  getAdjustedFund(int inputNum) {
    inputNumErrorCheck(inputNum, "getAdjustedFund(int)");

    setFund();
    return this.adjustedFundArray[inputNum - 1];
  } // getAdjustedFund()

  /**
   *  @return  pitch (in Hertz) of the Input, adjusted to ignore frequencies below a certain volume.
   */
  public float  getAdjustedFundAsHz(int inputNum) {
    inputNumErrorCheck(inputNum, "getAdjustedFundAsHz(int)");
    
    return getAdjustedFund(inputNum);
/*
    setFund();
    return this.adjustedFundArray[inputNum - 1];
    */
  } // getAdjustedFundAsHz()

  /**
   *  @return  pitch of the Input as a MIDI note, 
   * adjusted to ignore sounds below a certain volume.
   */
  public float  getAdjustedFundAsMidiNote(int inputNum) {
    inputNumErrorCheck(inputNum, "getAdjustedFundAsMidiNote(int)");

    setFund();
    return Pitch.ftom(this.adjustedFundArray[inputNum - 1]);
  } // getAdjustedFundAsMidiNote()

  /**
   *  @return  pitch (in Hertz) of the Input.
   */
  public float  getFund(int inputNum) {
    inputNumErrorCheck(inputNum, "getFund(int)");

    setFund();
    return this.fundamentalArray[inputNum - 1];
  } // getFund()

  /**
   *  @return  pitch (in Hertz) of the Input.
   */
  public float getFundAsHz(int inputNum) {
    inputNumErrorCheck(inputNum, "getFundAsHz(int)");
    
    return getFund(inputNum);
/*
    setFund();
    return this.fundamentalArray[inputNum - 1];
    */
  } // getFundAsHz()

  /**
   *  @return  pitch of the Input as a MIDI note.
   */
  public float  getFundAsMidiNote(int inputNum) {
    inputNumErrorCheck(inputNum, "getFundAsMidiNote(int)");

    setFund();
    return Pitch.ftom(this.fundamentalArray[inputNum - 1]);
  } // getFundAsMidiNote()
  
  /**
   *  @return  pitch (in Hertz) of the first Input, adjusted to ignore frequencies below a certain volume.
   */
  public float  getAdjustedFund() {
    return getAdjustedFund(1);
  } // getAdjustedFund()

  /**
   *  @return  pitch (in Hertz) of the first Input, adjusted to ignore frequencies below a certain volume.
   */
  public float  getAdjustedFundAsHz() {
    return getAdjustedFundAsHz(1);
  } // getAdjustedFundAsHz()
  
  /**
   *  @return  pitch (in Hertz) of the first Input, adjusted to ignore frequencies below a certain volume.
   */
  public float  getAdjustedFundAsMidiNote() {
    return getAdjustedFundAsMidiNote(1);
  } // getAdjustedFundAsMidiNote()

  /**
   *  @return  pitch (in Hertz) of the first Input.
   */
  public float  getFund() {
    return getFund(1);
  } // getFund()

  /**
   *  @return  pitch (in Hertz) of the first Input.
   */
  public float getFundAsHz() {
    return getFundAsHz(1);
  } // getFundAsHz()

  /**
   *  @return  pitch of the first Input as a MIDI note.
   */
  public float  getFundAsMidiNote() {
    return getFundAsMidiNote(1);
  } // getFundAsMidiNote()

  /**
   *  Calculates the average frequency of multiple input lines.
   *
   *  @param   inputsToAverage  an int[] with the numbers of each of the lines whose frequency is to be averaged.
   *
   *  @return  float            The average pitch of the inputs whose numbers are given in the int[] param.
   */
  public float  getAverageFund(int[] inputsToAverage)
  {
    if (inputsToAverage == null) {
      throw new IllegalArgumentException("Input_Jack.getAverageFund: int[] parameter is null.");
    } // error checking
    if (inputsToAverage.length < 1) {
      throw new IllegalArgumentException("Input_Jack.getAverageFund: int[] parameter's length is " + inputsToAverage.length + "; must be at least 1.");
    } // error checking

    float  result  = 0;

    // adds the freqencies of the specified inputs:
    for (int i = 0; i < inputsToAverage.length; i++)
    {
      result  += this.getAdjustedFund(inputsToAverage[i]);
    } // for

    // divides to find the average:
    return result/inputsToAverage.length;
  } // getAverageFund(int[])

  /**
   *  Calculates the average frequency of multiple consecutive input lines,
   *  numbered from "firstInput" to "lastInput".
   *
   *  @param   firstInput  the number of the first input whose frequency is to be averaged.
   *  @param   lastInput   the number of the last input whose frequency is to be averaged.
   *
   *  @return  float            The average pitch of the inputs from "firstInput" to "lastInput".
   */
  public float getAverageFund(int firstInput, int lastInput)
  {
    inputNumErrorCheck(firstInput, "getAverageFund(int, int) - first int");
    inputNumErrorCheck(lastInput, "getAverageFund(int, int) - second int");
    if (!(lastInput > firstInput)) {  
      throw new IllegalArgumentException("InputClassJack.getAverageFund():  lastInput param " + lastInput + " is not greater than firstInput param " + firstInput);
    } // error checking

    int  curInput  = firstInput;

    // creates an array and fills it with the ints denoting the inputs from firstInput to lastInput:
    int[]  inputsToAverage  = new int[lastInput - firstInput + 1];
    for (int i = 0; i < inputsToAverage.length; i++)
    {
      inputsToAverage[i]  = curInput;
      curInput++;
    } // for

    // calculates the average by calling the other getAverageFund on the inputsToAverage array:
    return getAverageFund(inputsToAverage);
  } // getAverageFund

  /**
   *  Calculates the average frequency of multiple input lines.
   *
   *  @param   inputsToAverage  an int[] with the numbers of each of the lines whose amplitude is to be averaged.
   *
   *  @return  float  The average amplitude of the inputs whose numbers are given in the int[] param.
   */
  public float  getAverageAmp(int[] inputsToAverage)
  {
    if (inputsToAverage == null) {
      throw new IllegalArgumentException("Input_Jack.getAverageAmp: int[] parameter is null.");
    } // error checking
    if (inputsToAverage.length < 1) {
      throw new IllegalArgumentException("Input_Jack.getAverageAmp: int[] parameter's length is " + inputsToAverage.length + "; must be at least 1.");
    } // error checking

    float  result  = 0;

    for (int i : inputsToAverage) {
      result  += this.getAmplitude(i);
    } // for

    return result/inputsToAverage.length;
  } // getAverageAmp

  /**
   *  Calculates the average amplitude of multiple consecutive input lines,
   *  numbered from "firstInput" to "lastInput".
   *
   *  @param   firstInput  the number of the first input whose amplitude is to be averaged.
   *  @param   lastInput   the number of the last input whose amplitude is to be averaged.
   *
   *  @return  float            The average pitch of the inputs from "firstInput" to "lastInput".
   */
  public float getAverageAmp(int firstInput, int lastInput)
  {
    inputNumErrorCheck(firstInput, "getAverageFund(int, int) - first int");
    inputNumErrorCheck(lastInput, "getAverageFund(int, int) - second int");
    if (!(lastInput > firstInput)) {  
      throw new IllegalArgumentException("InputClassJack.getAverageFund():  lastInput param " + lastInput + " is not greater than firstInput param " + firstInput);
    } // error checking

    int  curInput  = firstInput;

    int[]  inputsToAverage  = new int[lastInput - firstInput + 1];
    for (int i = 0; i < inputsToAverage.length; i++)
    {
      inputsToAverage[i]  = curInput;
      curInput++;
    } // for

    return getAverageAmp(inputsToAverage);
  } // getAverageAmp

  /**
   *  Returns the amplitude of the given input line.
   *
   *  @param   inputNum  an int specifying a particular input line.
   *
   *  @return  float     amplitude of the particular input line.
   */
  public float getAmplitude(int inputNum) {
    inputNumErrorCheck(inputNum, "getAmplitude(int)");

    return this.frequencyArray[inputNum - 1].getAmplitude();
  } // getAmplitude
  
  /**
   *  Applies a 1:8 compressor for amp's over 400 and returns the resulting amplitude.
   *
   *  @return  float     amplitude of the first input line.
   */
  public float getAmplitude()  
  {
    float  amp  = this.frequencyArray[0].getAmplitude();
    
 //   if(amp > 400)  {  amp = amp + ((amp - 400) / 8);  }
    
    return amp;
  }

  /**
   *  Error checker for ints sent to methods such as getFund, getAmplitude, etc.;
   *  rejects numbers that are greater than the number of inputs or less than 1.
   *
   *  @param   inputNum  an int that is to be checked for suitability as an input line number.
   *  @param   String    name of the method that called this method, used in the exception message.
   */
  private void inputNumErrorCheck(int inputNum, String method) {
    if (inputNum > this.numInputs) {
      IllegalArgumentException iae = new IllegalArgumentException("InputClass_Jack.inputNumErrorCheck(int), from " + method + ": int parameter " + inputNum + " is greater than " + this.numInputs + ", the number of inputs.");
      iae.printStackTrace();
    }
    if (inputNum < 1) {
      IllegalArgumentException iae = new IllegalArgumentException("InputClass_Jack.inputNumErrorCheck(int), from " + method + ": int parameter is " + inputNum + "; must be 1 or greater.");
      iae.printStackTrace();
    }
  } // inputNumErrorCheck

  /**
   *  Setter for sensitivity float instance var.
   *
   *  @param  newSensitivity  float with the value to which sensitivity is to be set.
   */
  public void setSensitivity(float newSensitivity)
  {
    this.sensitivity = newSensitivity;
  }
} // Input class

/*
 * This file is part of Beads. See http://www.beadsproject.net for all information.
 * CREDIT: This class uses portions of code taken from MEAP. See readme/CREDITS.txt.
 *
 *  07/02/2016
 *  Emily Meuer
 *
 *  Edited to allow access to amplitude, so classes using these Frequencies
 *  can cut out some background noise.
 */

//package net.beadsproject.beads.analysis.featureextractors;




/**
 * Frequency processes spectral data forwarded to it by a {@link PowerSpectrum}
 * to determine the best estimate for the frequency of the current signal.
 *
 * @beads.category analysis
 */
class FrequencyEMM extends FeatureExtractor<Float, float[]> {

  /** The Constant FIRSTBAND. */
  static final int FIRSTBAND = 3;

  /** The ratio bin2hz. */
  private float bin2hz;

  private int bufferSize;
  
  private  float[]  hps;      // Harmonic Product Spetrum summed up here

  private float sampleRate;

  private float amplitude;

  /**
   * Instantiates a new Frequency.
   *
   * @param sampleRate The sample rate of the audio context
   */
  public FrequencyEMM(float sampleRate) {
    bufferSize = -1;
    this.sampleRate = sampleRate;
    features = null;
  }

  /* (non-Javadoc)
   * @see com.olliebown.beads.core.PowerSpectrumListener#calculateFeatures(float[])
   */
  public synchronized void process(TimeStamp startTime, TimeStamp endTime, float[] powerSpectrum) {
    if (bufferSize != powerSpectrum.length) {
      bufferSize = powerSpectrum.length;
      bin2hz = sampleRate / (2 * bufferSize);
    } // if
    
    hps  = new float[powerSpectrum.length];
    
    features = null;
    // now pick best peak from linspec
    double pmax = -1;
    int maxbin = 0;
    
    for(int i = 0; i < hps.length; i++)
    {
      hps[i]  = powerSpectrum[i];
    } // for
    
    // 2:
    int  i;
    for(i = 0; (i * 2) < hps.length; i++)
    {
      hps[i]  = hps[i] + powerSpectrum[i*2];
    } // for
    
    // 3:
    for(i = 0; (i * 3) < hps.length; i++)
    {
      hps[i]  = hps[i] + powerSpectrum[i*3];
    } // for
    
    // 4:
    for(i = 0; (i * 4) < hps.length; i++)
    {
      hps[i]  = hps[i] + powerSpectrum[i*4];
    } // for
    
    for (int band = FIRSTBAND; band < hps.length; band++) {
      double pwr = hps[band];
      if (pwr > pmax) {
        pmax = pwr;
        maxbin = band;
      } // if
    } // for
    
    // Previously selected the frequency w/maxAmplitude by the following:
 /*
    for (int band = FIRSTBAND; band < powerSpectrum.length; band++) {
      double pwr = powerSpectrum[band];
      if (pwr > pmax) {
        pmax = pwr;
        maxbin = band;
    } // for
*/
    // 10/5 edits are going to push this number super high.
    // I added the following line:
    amplitude  = (float)pmax;

    // cubic interpolation (i.e., estimating values btwn known data points)
    double yz = powerSpectrum[maxbin];
    
    // replaced the following ternary with this if-then, for clarity:
//    double ym = maxbin <= 0? powerSpectrum[maxbin] : powerSpectrum[maxbin - 1];
    double ym;
    if(maxbin <= 0) {
      ym = powerSpectrum[maxbin];
    } else {
      ym = powerSpectrum[maxbin - 1];
    } // else
//    println("FrequencyEMM.process: ym = " + ym);
    
//    double yp = maxbin < powerSpectrum.length - 1 ? powerSpectrum[maxbin + 1] : powerSpectrum[maxbin];
    double yp;
    if(maxbin < powerSpectrum.length - 1) {
      yp  = powerSpectrum[maxbin + 1];
    } else {
      yp  = powerSpectrum[maxbin];
    } // else
//    println("FrequencyEMM.process: yp = " + yp);double k = (yp + ym) / 2 - yz;
    
    double k = (yp + ym) / 2 - yz;
    double x0 = (ym - yp) / (4 * k);
    features = (float)(bin2hz * (maxbin + x0));
//    println("features = " + features);

    forward(startTime, endTime);
  } // process

  /* (non-Javadoc)
   * @see com.olliebown.beads.core.FrameFeatureExtractor#getFeatureDescriptions()
   */
  public String[] getFeatureDescriptions() {
    return new String[]{"frequency"};
  }

  /**
   * @return float  amplitude of the fundamental frequency (in unknown units).
   */
  public float getAmplitude() {  
    return this.amplitude;
  }
}
/**
 *  from https://processing.org/examples/scrollbar.html
 */

class HScrollbar {
  int swidth, sheight;    // width and height of bar
  float xpos, ypos;       // x and y position of bar
  float spos, newspos;    // x position of slider
  float sposMin, sposMax; // max and min values of slider
  int loose;              // how loose/heavy
  boolean over;           // is the mouse over the slider?
  boolean locked;
  float ratio;

  HScrollbar (float xp, float yp, int sw, int sh, int l) {
    swidth = sw;
    sheight = sh;
    int widthtoheight = sw - sh;
    ratio = (float)sw / (float)widthtoheight;
    xpos = xp;
    ypos = yp-sheight/2;
    spos = xpos + swidth/2 - sheight/2;
    newspos = spos;
    sposMin = xpos;
    sposMax = xpos + swidth - sheight;
    loose = l;
  }

  public void update() {
    if (overEvent()) {
      over = true;
    } else {
      over = false;
    }
    if (mousePressed && over) {
      locked = true;
    }
    if (!mousePressed) {
      locked = false;
    }
    if (locked) {
      newspos = constrain(mouseX-sheight/2, sposMin, sposMax);
    }
    if (abs(newspos - spos) > 1) {
      spos = spos + (newspos-spos)/loose;
    }
  }

  public float constrain(float val, float minv, float maxv) {
    return min(max(val, minv), maxv);
  }

  public boolean overEvent() {
    if (mouseX > xpos && mouseX < xpos+swidth &&
       mouseY > ypos && mouseY < ypos+sheight) {
      return true;
    } else {
      return false;
    }
  }

  public void display() {
    noStroke();
    fill(204);
    rect(xpos, ypos, swidth, sheight);
    if (over || locked) {
      fill(0, 0, 0);
    } else {
      fill(102, 102, 102);
    }
    rect(spos, ypos, sheight, sheight);
  }

  public float getPos() {
    // Convert spos to be values between
    // 0 and the total width of the scrollbar
    return spos * ratio;
  }
}
  public void settings() {  size(800, 400); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--present", "--window-color=#B7B7B7", "--stop-color=#0A0A0A", "Module_02_PitchHueBars_EMM" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
