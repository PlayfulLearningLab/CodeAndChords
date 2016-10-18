import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

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

public class SceneDrawRosette_EMM extends PApplet {

class DrawRosette extends Scene
{
  /*
    06/29/2016
   Emily Meuer
   
   This scene draws a rosette, either on a timer or on an audio-input-triggered event.
   */

  // Calibrate:
  int[]  freqThresholds  = new int[] {     // these are the frequencies that need to be crossed to draw a new line.
    440, 
    460, 
    480, 
    530, 
    560, 
    600, 
    650  };
  
  /*
  // July 8 thresholds:
  int[]  freqThresholds  = new int[] {     // these are the frequencies that need to be crossed to draw a new line.
    150, 
    160, 
    190, 
    205, 
    230, 
    255, 
    325  };
    */
    
  Input  input;
  int    stroke;
  float  thresholdFreq;
  float  time  = 100;
  int    waitUntil;

  /*
   Frequencies:
   Eb 3      -  155.56
   Fb (E) 3  -  164.81
   G 3       -  196.00
   Ab 3      -  207.65
   Bb 3      -  233.08
   C 4       -  261.63
   Fb (E) 4  -  329.63
   */
  
  /**
   *  Constructor; creates a DrawRosette with the given Input and tenorCutoff.
   *
   *  @param  input        an Input that can be used to control when or in what color the rosettes are drawn.
   *  @param  tenorCutoff  int that specifies which mics belong to the tenors (this and all higher are tenors; lower numbers are basses).
   */
  public DrawRosette(Input input, int tenorCutoff)
  {
    this.input         = input;
    this.tenorCutoff   = tenorCutoff;
    this.stroke        = 0;
    
    this.thresholdFreq = 100;
    waitUntil          = millis();
  } // constructor
  
  /**
   *  Called in draw in the Zikr_Scenes_Inputs1_9_EMM tab.
   */
  public void run()
  {
    translate(width/2, height/2);
    drawAndRaiseThreshold(300);
  } // run()
  
  /**
   *  Draws one more stroke of rosetteThree each time a pitch threshold is passed,
   *  and raises the threshold that must be passed for the next stroke.
   *
   *  @param  radius  ** a float specifiying how long each stroke of the rosette is.
   */
  public void drawAndRaiseThreshold(float radius) 
  {
//    println("Draw.drawAndRaiseThreshold: input.getAverageFund(1, input.numInputs) = " + input.getAverageFund(1, input.numInputs));
    
    // Draws a line each time the pitch crosses a frequency threshold, and ups the threshold each time:
    if ( (stroke < 16) && (input.getAdjustedFundAsHz() > freqThresholds[stroke/3]) && millis() > waitUntil ) {
      waitUntil  = millis() + 300;
      drawRosetteThree(radius, stroke, originalThree);
  
      thresholdFreq += 100;
      stroke++;
    } // if
  } // drawAndRaiseThreshold(float)
  
  /**
   *  Draws one more stroke of rosetteThree every "time" milliseconds, 
   *  where "time" is a variable specified earlier in the class.
   *
   *  @param  radius  ** a float specifiying how long each stroke of the rosette is.
   */
  public void drawOnDelay(float radius)
  {
    // draws a line every second:
    if ( millis() > time  && stroke < 16) {
      drawRosetteThree(radius, stroke, originalThree);
  
//      println("stroke = " + stroke + "; time = " + time + "; millis() = " + millis());
  
      time = millis() + 1000;
      stroke++;
    } // if
  } // drawOnDelay
  
  /**
   *  Draws one more stroke of rosetteThree each time a pitch threshold is passed.
   *
   *  @param  radius         ** a float specifiying how long each stroke of the rosette is.
   *  @param  thresholdFreq  a float specifying which pitch must be met or passed to draw a stroke.
   */
  public void drawPastThreshold(float radius, float thresholdFreq)
  {
    // Force the threshold to be crossed multiple times before triggering?
   
    
    // Draws a line each time the pitch crossed a frequency threshold:
    if ( (input.getAdjustedFundAsHz() > thresholdFreq)  && (stroke < 16) ) {
      drawRosetteThree(radius, stroke, originalThree);
  
 //     println("stroke = " + stroke + "; input.getAdjustedFundAsHz() = " + input.getAdjustedFundAsHz(1) + "; thresholdFreq = " + thresholdFreq);
  
      stroke++;
    } // if
  } // drawPastThreshold(float)
  
  /**
   *  Draws the specified stroke of rosetteThree.
   *
   *  @param  radius         ** a float specifiying how long each stroke of the rosette is.
   *  @param  whichStroke    an int specifying which stroke should be drawn; must be between 0 and 15.
   *  @param  strokeColor    a color in which the rosette will be drawn.
   */
  public void drawRosetteThree(float radius, int whichStroke, int strokeColor) {
    if (whichStroke > 15) {
      throw new IllegalArgumentException("Zikr_Scenes_EMM.drawRosetteThree: int parameter " + whichStroke + " is greater than the number of lines in the rosette.");
    }
  
    float x1 = radius*cos(PI/4*3*whichStroke);
    float x2 = radius*cos(PI/4*3*(whichStroke+1));
    float y1 = radius*sin(PI/4*3*whichStroke);
    float y2 = radius*sin(PI/4*3*(whichStroke+1));
    strokeWeight(3);
    stroke(220);
    line(x1, y1, x2, y2);
    strokeWeight(2.5f);
    stroke(strokeColor);
    line(x1, y1, x2, y2);
  } // drawRosetteThree
  
  /**
   *  Sets stroke to 0.
   */
  public void reset()
  {
    this.stroke = 0;
  } // reset
} // DrawRosette


















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
/*
  10/09/2016
  Emily Meuer
  
  TODO:
   - add buttons
   - put options
   - remove tenorCutoff from Scene?
  
  Remember: when having an ' "Input" class not found' error,
  make sure that you have setup() and draw()! :P ;)
*/

DrawRosette  drawRosette;
Input        input;

public void settings()
{  
  fullScreen();
} // settings()

public void setup()
{ 
  input        = new Input();  
  drawRosette  = new DrawRosette(input, 1);
  
  background(0);
} // setup()


public void draw()
{
  drawRosette.run();
} // draw
abstract class Scene
{
  /*
    06/29/2016
   Emily Meuer
   
   Abstract class to allow a sketch to cycle through different scenes.
   
   (Could have a next and previous capability.)
   */
   
  // Calibrate:
  int  highPitch  = 500;    // num by which pitches will be divided before turning into a color;
                            // pitches above this will not be considered when changing the color,
                            // but the lower it is, the more each change in pitch will affect the color.

  float  red;      // red and blue are set in pitchColor;
  float  green;    // green must be set to 0 and += 30 each time something is drawn.
  float  blue;
  
  int  strokeColor;
  
  int  originalOne    = color(50, 50, 200);
  int  originalTwo    = color(50, 200, 50);
  int  originalThree  = color(200, 50, 50);
  // Original colors:
  //  one:    (50, 50, 200);
  //  two:    (50, 200, 50);
  //  three:  (200, 50, 50);

  float  x1, x2, y1, y2;  // Used for drawing the lines that make up each rosette

  Input  leftInput;
  Input  rightInput;
  
  Input  input;
  int    tenorCutoff;    // mics will be split into high and low, low mics all lines with numbers below this,
                         // high mics this mic and all with numbers above it.
                         // (e.g., if there are 9 mics and tenorCutoff = 5, low mics are 1 - 4, and high are 5 - 9.
  /**
   * All implementing subclasses must override this with their own run() functionality;
   * it will be called repeatedly in draw().
   */
  public void run() {
  }

  /**
   * Sets the color based on the pitch of the low and high groups of mics (basses and tenors).
   */
  public void pitchColor() {
    red   = Math.min(255 * input.getAdjustedFundAsHz() / highPitch, 255);
    blue  = Math.min(255 * input.getAdjustedFundAsHz() / highPitch, 255);
    /*
    red   = Math.min(255 * (input.getAverageFund(1, this.tenorCutoff - 1) / highPitch), 255);
    blue  = Math.min(255 * (input.getAverageFund(this.tenorCutoff, input.numInputs) / highPitch), 255);
    */
  } // pitchColor

  /**
   *  Outermost rosette.  Wide angles; has a large opening in the center.
   *
   *  @param  radius       ** a float with the length of the side (not actually a radius?)
   *  @param  strokeColor  color with which the rosette is to be drawn.
   */
  public void rosettePartOne(float radius, int strokeColor) {
    for (int i = 0; i < 4; i++) {
      x1 = radius*cos(PI/2*i);
      x2 = radius*cos(PI/2*(i+1));
      y1 = radius*sin(PI/2*i);
      y2 = radius*sin(PI/2*(i+1));
      strokeWeight(4);
      stroke(220);
      line(x1, y1, x2, y2);
      strokeWeight(3);
      stroke(strokeColor);
      line(x1, y1, x2, y2);
    }
    for (int i = 0; i < 4; i++) {
      x1 = radius*cos(PI/2*i+PI/4);
      x2 = radius*cos(PI/2*(i+1)+PI/4);
      y1 = radius*sin(PI/2*i+PI/4);
      y2 = radius*sin(PI/2*(i+1)+PI/4);
      strokeWeight(4);
      stroke(220);
      line(x1, y1, x2, y2);
      strokeWeight(3);
      stroke(strokeColor);
      line(x1, y1, x2, y2);
    }
  } // rosettePartOne

  /**
   *  Middle rosette.
   *
   *  @param  radius       ** a float with the length of the side (not actually a radius?)
   *  @param  strokeColor  color with which the rosette is to be drawn.
   */
  public void rosettePartTwo(float radius, int strokeColor) {
    for (int i = 0; i < 8; i++) {
      x1 = radius*cos(PI/4*3*i+PI/8);
      x2 = radius*cos(PI/4*3*(i+1)+PI/8);
      y1 = radius*sin(PI/4*3*i+PI/8);
      y2 = radius*sin(PI/4*3*(i+1)+PI/8);
      strokeWeight(4);
      stroke(220);
      line(x1, y1, x2, y2);
      strokeWeight(2);
      stroke(strokeColor);
      line(x1, y1, x2, y2);
    }
  } // rosettePartTwo

  /**
   *  Innermost rosette; has the smallest angles.
   *
   *  @param  radius       ** a float with the length of the side (not actually a radius?)
   *  @param  strokeColor  color with which the rosette is to be drawn.
   */
  public void rosettePartThree(float radius, int strokeColor) {
    for (int i = 0; i < 16; i++) {
      x1 = radius*cos(PI/4*3*i);
      x2 = radius*cos(PI/4*3*(i+1));
      y1 = radius*sin(PI/4*3*i);
      y2 = radius*sin(PI/4*3*(i+1));
      strokeWeight(1);
      stroke(220);
      line(x1, y1, x2, y2);
      strokeWeight(.5f);
      stroke(strokeColor);
      line(x1, y1, x2, y2);
    } // for
  } // rosettePartThree
  
  /**
   *  Performs any clean-up required for the scene to be called later in the sketch.
   */
  public void reset()  {}
} // Scene
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--present", "--window-color=#B7B7B7", "--stop-color=#cccccc", "SceneDrawRosette_EMM" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
