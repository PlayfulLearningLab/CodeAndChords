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

public class SceneGrowRotateRosette_EMM extends PApplet {

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

RosetteV3Rotate  rosetteV3Rotate;
Input            input;

public void settings()
{  
  fullScreen();
} // settings()

public void setup()
{ 
  input        = new Input();  
  rosetteV3Rotate  = new RosetteV3Rotate(input);
  
  background(0);
} // setup()


public void draw()
{
  rosetteV3Rotate.run();
} // draw


















class Input
{
  /*
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
      g.addInput(uGenArray[i]);
    } // for
    ac.out.addInput(g);

    // The ShortFrameSegmenter splits the sound into smaller, manageable portions;
    // this creates an array of SFS's and adds the UGens to them:
    this.sfsArray  = new ShortFrameSegmenter[this.numInputs];
    for (int i = 0; i < this.sfsArray.length; i++)
    {
      this.sfsArray[i] = new ShortFrameSegmenter(ac);
      while (this.sfsArray[i] == null) {
      }
      this.sfsArray[i].addInput(uGenArray[i]);
    }

    // Creates an array of FFTs and adds them to the SFSs:
    this.fftArray  = new FFT[this.numInputs];
    for (int i = 0; i < this.fftArray.length; i++)
    {
      this.fftArray[i] = new FFT();
      while (this.fftArray[i] == null) {
      }
      this.sfsArray[i].addListener(this.fftArray[i]);
    } // for

    // Creates an array of PowerSpectrum's and adds them to the FFTs
    // (the PowerSpectrum is what will actually perform the FFT):
    this.psArray  = new PowerSpectrum[this.numInputs];
    for (int i = 0; i < this.psArray.length; i++)
    {
      this.psArray[i] = new PowerSpectrum();
      while (this.psArray[i] == null) {
      }
      this.fftArray[i].addListener(psArray[i]);
    } // for

    // Creates an array of FrequencyEMMs and adds them to the PSs
    // (using my version of the Frequency class - an inner class further down - to allow access to amplitude):
    this.frequencyArray  = new FrequencyEMM[this.numInputs];
    for (int i = 0; i < this.frequencyArray.length; i++)
    {
      this.frequencyArray[i] = new FrequencyEMM(44100);
      while (this.frequencyArray[i] == null) {
      }
      this.psArray[i].addListener(frequencyArray[i]);
    } // for

    // Adds the SFSs (and everything connected to them) to the AudioContext:
    for (int i = 0; i < this.numInputs; i++)
    {
      ac.out.addDependent(sfsArray[i]);
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
    }
    features = null;
    // now pick best peak from linspec
    double pmax = -1;
    int maxbin = 0;
    for (int band = FIRSTBAND; band < powerSpectrum.length; band++) {
      double pwr = powerSpectrum[band];
      if (pwr > pmax) {
        pmax = pwr;
        maxbin = band;
      }
    } // for

    // I added the following line:
    amplitude  = (float)pmax;

    // cubic interpolation
    double yz = powerSpectrum[maxbin];
    double ym = maxbin <= 0? powerSpectrum[maxbin] : powerSpectrum[maxbin - 1];
    double yp = maxbin < powerSpectrum.length - 1 ? powerSpectrum[maxbin + 1] : powerSpectrum[maxbin];
    double k = (yp + ym) / 2 - yz;
    double x0 = (ym - yp) / (4 * k);
    features = (float)(bin2hz * (maxbin + x0));

    forward(startTime, endTime);
  }

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
abstract class Scene
{
  /*
    10/09/2016
    Removed tenorCutoff for these demos.
  
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
//  int    tenorCutoff;    // mics will be split into high and low, low mics all lines with numbers below this,
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
abstract class RosetteV3 extends Scene
{
  /*
    06/29/2016
   Emily Meuer
   
   Abstract, base rosette class.
   */

  // The following specify the length of the radius of each of the 6 rosettes:
  float radius1;
  float radius2;
  float radius3;
  float radius4;
  float radius5;
  float radius6;

  float  rotateBy;    // amt by which the rosette should be rotated.

  /**
   *  Constructor; makes a RosetteV3 with the given Input and pre-determined radii.
   *
   *  @param  input        an Input, used to get bass pitch.
   */
  RosetteV3(Input  input)
  {
//    this.leftInput  = leftInput;
//    this.rightInput = rightInput;
      this.input      = input;

    this.rotateBy  = 0;

    this.radius1 = 40;
    this.radius2 = 100;
    this.radius3 = 270;
    this.radius4 = radius3 * 1.4f;
    this.radius5 = 500;
    this.radius6 = 725;
  } // RosetteV3

  /**
   *  Called in draw() in the Zikr_Scenes_Inputs1_9_EMM tab;
   *  draws 6 rosettes with the above radii and rotated by rotateBy and -rotateBy (every other).
   */
  public void run()
  {
    background(0);
    translate(width/2, height/2);

    float pitch = input.getAdjustedFund();

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

    if (pitch > 160) {
      pushMatrix();
      rotate(radians(rotateBy));
      rosettePartThree(radius1, originalThree);
      popMatrix();
    }
    if (pitch > 190) {
      pushMatrix();
      rotate(radians(-rotateBy));
      rosettePartThree(radius2, originalTwo);
      popMatrix();
    }
    if (pitch > 200) {
      pushMatrix();
      rotate(radians(rotateBy));
      rosettePartTwo(radius3, originalTwo);
      popMatrix();
    }
    if (pitch > 225) {
      pushMatrix();
      rotate(radians(-rotateBy));
      rosettePartTwo(radius4, originalTwo);
      popMatrix();
    }
    if (pitch > 255) {
      pushMatrix();
      rotate(radians(rotateBy));
      rosettePartOne(radius5, originalOne);
      popMatrix();
    }
    if (pitch > 325) {
      pushMatrix();
      rotate(radians(-rotateBy));
      rosettePartOne(radius6, originalOne);
      popMatrix();
    }
  } // run()
}// RosetteV3 
class RosetteV3Colors extends RosetteV3
{
  /*
    06/29/2016
   Emily Meuer
   
   Class that shows rosettes based on the pitch of one input
   and rotates the rosettes based on the pitch of another input.
   */
   
   // Calibrate:
 /*  int[]  growFrequencies = {    // a rosette is drawn each time the "low voice" avg frequency
     160,                        // passes one of these points.
     190,
     200,
     225,
     255,
     325
   }; // growFrequencies
*/ 

   int[]  growFrequencies = {    // a rosette is drawn each time the "low voice" avg frequency
     150,                        // passes one of these points.
     550,
     600,
     650,
     700,
     750
   }; // growFrequencies
   
  /**
   *  Constructor; makes a RosetteV3Colors with the given Input and tenorCutoff.
   *
   *  @param  input        an Input, used to get bass and tenor pitch and amp.
   *  @param  tenorCutoff  an int at which the mics divide into basses (below) and tenors (this and all above).
   */
  RosetteV3Colors(Input input)
  {
    super(input);
    
//    this.tenorCutoff = tenorCutoff;
  } // RosetteV3

  /**
   *  Called in draw() in the Zikr_Scenes_Inputs1_9_EMM tab;
   *  draws 6 rosettes with the above radii and rotated by rotateBy and -rotateBy (every other) -
   *  as in RosetteV3.run(), but with more green in each rosette.
   */
  public void run()
  {
    background(0);
    translate(width/2, height/2);

    float pitch = input.getAdjustedFund();
    
    pitchColor();
    green  = 0;

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

    if (pitch > 160) {
      pushMatrix();
      rotate(radians(rotateBy));
      rosettePartThree(radius1, color(red, green, blue));
      popMatrix();
      green = (green + 50) % 255;
    }
    if (pitch > 190) {
      pushMatrix();
      rotate(radians(-rotateBy));
      rosettePartThree(radius2, color(red, green, blue));
      popMatrix();
      green = (green + 50) % 255;
    }
    if (pitch > 200) {
      pushMatrix();
      rotate(radians(rotateBy));
      rosettePartTwo(radius3, color(red, green, blue));
      popMatrix();
      green = (green + 50) % 255;
    }
    if (pitch > 225) {
      pushMatrix();
      rotate(radians(-rotateBy));
      rosettePartTwo(radius4, color(red, green, blue));
      popMatrix();
      green = (green + 50) % 255;
    }
    if (pitch > 255) {
      pushMatrix();
      rotate(radians(rotateBy));
      rosettePartOne(radius5, color(red, green, blue));
      popMatrix();
      green = (green + 50) % 255;
    }
    if (pitch > 325) {
      pushMatrix();
      rotate(radians(-rotateBy));
      rosettePartOne(radius6, color(red, green, blue));
      popMatrix();
      green = (green + 50) % 255;
    }
  } // run()
}// RosetteV3 
class RosetteV3Rotate extends RosetteV3Colors
{
  /*
    06/29/2016
   Emily Meuer
   
   Class that shows rosettes based on the pitch of one input
   and rotates the rosettes based on the pitch of another input.
   */
   
   // Calibrate:
   int  amp  = 3;  // pitches with amplitudes below this will not change rotation.
   int  changeInRotation  = 400;  // higher number = slower rotation
                                  // (pitch is divided by this number, and the resulting decimal value is added to rotateBy).

  /**
   *  Constructor; makes a RosetteV3Rotate with the given Input and tenorCutoff.
   *
   *  @param  input        an Input, used to get bass and tenor pitch and amp.
   *  @param  tenorCutoff  an int at which the mics divide into basses (below) and tenors (this and all above).
   */
  RosetteV3Rotate(Input input)
  {
    super(input);
    
//    this.tenorCutoff  = tenorCutoff;
  } // RosetteV3Rotate

  /**
   *  Called in draw() in the Zikr_Scenes_Inputs1_9_EMM tab;
   *  calculates rotateBy based on the average tenor pitch and calls RosetteV3Colors.run().
   */
  public void run()
  {
    if (input.getAmplitude() > amp) {
      super.rotateBy = (super.rotateBy + (input.getAdjustedFund() / changeInRotation)) % 360;
    }
    
    super.run();
  } // run()
}// RosetteV3 
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--present", "--window-color=#9B0F9B", "--stop-color=#cccccc", "SceneGrowRotateRosette_EMM" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
