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

public class Module_08_BehindTheCurtain_EMM extends PApplet {

/*
  09/24/2016
  Emily Meuer
  
  Mathy, Matrix-inspired raining numbers.
  
  TODO:
   - Fix pile-up at the top of the list
   - Get average freq. over time, not just whatever second happens.
   - Silence holds the last number; probably don't want that?
   
   - Plotting points
   
   thoughts: maybe we don't want to start over until they're all gone?
             or start another column next to that, and start back at the first one when the second one finishes?
*/

Input           input;
RainingNumbers  amplitudeRain;
RainingNumbers   frequencyRain;
RainingNumbers   avgFreqRain;

public void setup()
{
  
  
  input  = new Input();
  amplitudeRain  = new RainingNumbers(50, "Amplitude:", 10, 100);
  frequencyRain  = new RainingNumbers((width / 3) + 50, "Frequency:", 13, 200);
  avgFreqRain  = new RainingNumbers((2 * width / 3) + 50, "Avg Freq:", 11, 200);
//  background(amplitudeRain.background[0], amplitudeRain.background[1], amplitudeRain.background[2]);
  background(amplitudeRain.backgroundColor);
  
  textAlign(LEFT);
} // setup

public void draw()
{
  amplitudeRain.rain(input.getAmplitude());
  frequencyRain.rain(input.getAdjustedFundAsHz());
  avgFreqRain.rain(input.getTimeAverageFundAsHz(1,200));
} // draw
 //<>// //<>// //<>// //<>// //<>// //<>//

















class Input
{
  /*
  ** This version contains getTimeAverageFundAsHz()
  
  
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
   *  @return  average pitch of the given input over the next given milliseconds, in Hz.
   */
  public float getTimeAverageFundAsHz(int inputNum, int millis)
  {
    float sum      = 0;
    int   endTime  = millis() + millis;
    int   count    = 0;
    
    while(millis() < endTime)
    {
      sum = sum + this.getAdjustedFundAsHz(inputNum);
      count++;
    } // while
    
    return sum / count;
  } // getTimeAverageFund
  
  /**
   *  @return  average pitch of the given input over the next given milliseconds, as a midi note.
   */
/*  float getTimeAverageFundAsMidiNote(int input, int millis)
  {
    // Should I do this by calling getTimeAverageFundAsHz and converting it,
    // or repeating the logic, but w/getAdjustedFundAsMidiNote()?
//    return ;
  } // getTimeAverageFund
*/

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
class RainingNumbers
{
  /*
  09/24/2016
   Emily Meuer
   
   Class whose instances rain down along a particular x value.
   */

  int     xPos;
  int     yDrawPos;
  int     yModPos;
  int     tail;      // how many trailing numbers should be left showing

  String  name;

  int     nextRain;
  int     delay; //        = 250; //500;
//  int[]   background   = new int[] { 200, 100, 100 };
  int[]   background       = new int[] { 0, 0, 0 };
  int   backgroundColor  = color(background[0], background[1], background[2]);
  
  
  /**
   *  Constructor that creates a RainingNumbers object with a delay of 250 millis.
   *
   *  @param  xPos  an int that determines the x position of the RainingNumbers
   *  @param  name  a String that will appear at the top of the column
   *  @param  tail  an int that determines when the numbers will begin to fade out
   */
  RainingNumbers(int xPos, String name, int tail)
  {
    this(xPos, name, tail, 250);
  } // constructor

  /**
   *  Constructor that makes a RainingNumbers object.
   *
   *  @param  xPos  an int that determines the x position of the RainingNumbers
   *  @param  name  a String that will appear at the top of the column
   *  @param  tail  an int that determines when the numbers will begin to fade out
   *  @param  delay  an int that determines after how many milliseconds the next number should appear
   */
  RainingNumbers(int xPos, String name, int tail, int delay)
  {
    this.xPos      = xPos;
    this.yDrawPos  = 100;
    this.yModPos   = 0;
    this.tail      = tail + 1;    // we're dealing w/mod math, and the given tail is the *length* we want for the tail.
    this.delay     = delay;
    this.name      = name;
    
    fill(0);
    textSize(24);
    text(name, xPos, 35);
    
    textSize(18);
    this.nextRain  = millis() + delay;
  } // constructor(int)

  // in the future, could play with the z-axis of the text().
//  void rain(String value)
  public void rain(float value)
  {
    int  tailDrawPos;            // position of value that will dissapear into background.
    int  tailModPos;             // used to calculate wrap-around to top of screen while keeping space for text at the top.
    
    // Print name of column at the top:
    fill(0, 204, 0);
    textSize(24);
    text(name, xPos, 55);
    
    if (millis() > this.nextRain)
    {
      // Draw the next value:
 //     stroke(0);
//      fill(0);

      textSize(18);
      text(value, xPos, yDrawPos);
      //      ellipse(xPos, yPos, 10, 10);
      yModPos   = (yModPos + 15) % (height - 100);
      yDrawPos  = yModPos + 100;
      
      /*
        The problem: I want the yPos values to go from 100 to height, then go back around.
        
        Mod by less than 100, and then add that much less? If I add then, makes a big gap every time.
        I need to add a small amt every time, except when I mod, which is when I need to add 100.
        Of course, I could do an if.  But what does it really mean?  It means that I --
        
        my position values: 100 - height.
        my mod values:      0 - (height - 100).
      */

      // Last value of the tail (to be completely blacked out this time around):
      tailDrawPos = ((height - 100) + yDrawPos - (this.tail * 15)) % (height - 100);
      tailModPos  = tailDrawPos - 100;

      for (int i = 0; i < 8; i++)
      {
        // draw increasingly less transparent squares to reveal the numbers on the tail end:
        stroke(this.background[0], this.background[1], this.background[2], i * 30);
        fill(this.background[0], this.background[1], this.background[2], i * 30);
        rect(xPos, tailDrawPos, 175, 15);

        //        textSize(18);
        //        text(value, xPos, tailPos);

        //        ellipse(xPos, tailPos, 15, 15);

        tailModPos = ((height - 100) + tailModPos - 15) % (height - 100);
        tailDrawPos = (tailModPos + 100) % height;
      } // for


      this.nextRain = this.nextRain + this.delay;
    } // if - delay
  } // rain
} // class - RainingNumbers
  public void settings() {  size(800, 500); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--present", "--window-color=#B7B7B7", "--stop-color=#cccccc", "Module_08_BehindTheCurtain_EMM" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
