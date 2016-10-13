import org.jaudiolibs.beads.AudioServerIO; //<>//
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

import javax.sound.sampled.AudioFormat;

class Input
{
  /*
  Notate bene: inputNum passed to constructor must be 4 greater
  than the actual desired number of inputs.
  
   ** Watch for that NullPointerException -- add a try-catch? **
   
   Emily Meuer
   07/06 Update: works with both the Behringer and Motu interfaces
   to get more than 4 inputs!
   
   06/29/2016
   
   Updating InputClass_EMM to communicate with Jack;
   based on BeadsJNA.
   
   To change in classes that implement this:
   - size()/settings() can be in main tab.
   - getFund() etc. takes an int parameter specifying which input is in question.
   
   TODO:  
   - Skip input lines 5-8 more elegantly.
   - Make a folder in lib for this - can it then be imported?
   */

  AudioContext           ac;
  float                  adjustedFund;
  float[]                adjustedFundArray;
  UGen                   inputsUGen;
  FFT                    fft;
  FFT[]                  fftArray;
  FrequencyEMM           frequency;
  FrequencyEMM[]         frequencyArray;
  float                  fundamental;
  float[]                fundamentalArray;
  int                    numInputs;
  float                  pitch;
  PowerSpectrum          ps;
  PowerSpectrum[]        psArray;
  float                  sensitivity;  // amplitude below which adjustedFreq will not be reset
  ShortFrameSegmenter    sfs;
  ShortFrameSegmenter[]  sfsArray;
  int                    waitUntil;

  Input(int numInputs)
  {
    this.numInputs  = numInputs;
    //this.numInputs  = numInputs + 4;
    this.ac = new AudioContext(new AudioServerIO.Jack(), 512, AudioContext.defaultAudioFormat(numInputs, numInputs));

    // creates an int[] of the input channel numbers - e.g., { 1, 2, 3, 4 } for a 4 channel input.
    int[]  inputNums  = new int[this.numInputs];
    for (int i = 0; i < this.numInputs; i++)
    {
      inputNums[i]  = i + 1;
    } // for

    this.inputsUGen = ac.getAudioInput(inputNums);

    UGen[]  uGenArray  = new UGen[this.numInputs];
    for (int i = 0; i < uGenArray.length; i++)
    {
      uGenArray[i]  = ac.getAudioInput(new int[] {(i + 1)});
    }

    // Sonifying Processing and George P. do this:
    Gain g = new Gain(this.ac, 1, 0.5);
    for (int i = 0; i < this.numInputs; i++)
    {
      g.addInput(uGenArray[i]);
    } // for
    ac.out.addInput(g); //<>//

    this.sfsArray  = new ShortFrameSegmenter[this.numInputs];
    for (int i = 0; i < this.sfsArray.length; i++)
    {
      this.sfsArray[i] = new ShortFrameSegmenter(ac);
      while (this.sfsArray[i] == null) {
      }
      this.sfsArray[i].addInput(uGenArray[i]); //<>//
    }

    this.fftArray  = new FFT[this.numInputs];
    for (int i = 0; i < this.fftArray.length; i++)
    {
      this.fftArray[i] = new FFT();
      while (this.fftArray[i] == null) {
      }
      this.sfsArray[i].addListener(this.fftArray[i]); //<>//
    } // for

    // The PowerSpectrum is what will actually perform the FFT:
    this.psArray  = new PowerSpectrum[this.numInputs];
    for (int i = 0; i < this.psArray.length; i++)
    {
      this.psArray[i] = new PowerSpectrum();
      while (this.psArray[i] == null) {
      }
      this.fftArray[i].addListener(psArray[i]); //<>//
    } // for

    // Using my version of the Frequency class instead to allow access to amplitude.
    this.frequencyArray  = new FrequencyEMM[this.numInputs];
    for (int i = 0; i < this.frequencyArray.length; i++)
    {
      this.frequencyArray[i] = new FrequencyEMM(44100);
      while (this.frequencyArray[i] == null) {
      }
      this.psArray[i].addListener(frequencyArray[i]); //<>//
    } // for //<>//

    for (int i = 0; i < this.numInputs; i++)
    {
      ac.out.addDependent(sfsArray[i]); //<>//
    } // for - addDependent

    this.sensitivity  = 3; //<>//

    this.ac.start();

    this.fundamentalArray = new float[this.numInputs];
    this.adjustedFundArray = new float[this.numInputs];

    this.setFund();
  } // constructor(int)

  /** //<>//
   * Constructor for creating a one (or two?)-channel Input object 
   * from the machine's default audio input device;
   * does not require Jack.
   */
  Input()
  {
    // Could just call this(1) if I add a way to specify the AudioContext as well.

    this.ac  = new AudioContext();
    //<>//
    this.inputsUGen  = ac.getAudioInput();

    // Sonifying Processing and George P. do this:
    Gain g = new Gain(this.ac, 1, 0.5);
    g.addInput(inputsUGen);
    ac.out.addInput(g);

    sfs = new ShortFrameSegmenter(ac);
    sfs.addInput(ac.out);

    this.fft = new FFT();
    sfs.addListener(fft);

    // The PowerSpectrum is what will actually perform the FFT:
    ps = new PowerSpectrum(); //<>//

    fft.addListener(ps);
    /*
     frequency = new Frequency(44100.0f);
     // connect the PowerSpectrum to the Frequency object
     ps.addListener(frequency); //<>//
     */
    // Using my version of the Frequency class instead to allow access to amplitude.

    frequency  = new FrequencyEMM(44100);
    ps.addListener(frequency);

    ac.out.addDependent(sfs);

    this.sensitivity  = 1;

    this.numInputs    = 1;

    this.ac.start();

    this.setFund();
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
  int  getNumInputs() {
    return this.numInputs - 4;
  } // getNumInputs

  /**
   * 
   */
  void setFund()
  { 
    for (int i = 0; i < this.numInputs; i++)
    {
      //     println("setFund(); this.frequencyArray[i] = " + this.frequencyArray[i].getFeatures());

      // want to assign the value of .getFeatures() to a variable and check for null,
      // but can't, b/c it returns a float. :/
      if (this.frequencyArray[i].getFeatures() != null) {
        //       println("i = " + i);
        //       println("setFund(); this.fundamentalArray[i] = " + this.fundamentalArray[i] + "this.frequencyArray[i].getFeatures() = " + this.frequencyArray[i].getFeatures());
        this.fundamentalArray[i] = this.frequencyArray[i].getFeatures();

        if (this.frequencyArray[i].getAmplitude() > this.sensitivity) {
          this.adjustedFundArray[i]  = this.fundamentalArray[i];
        } // if: amp > sensitivity
      } // if: features() != null
    } // if: > numInputs
  } // setFund

  /**
   *  @return  pitch of the Input, adjusted to ignore frequencies below a certain volume.
   */
  float  getAdjustedFund(int inputNum) {
    inputNumErrorCheck(inputNum, "getAdjustedFund(int)");

    if (inputNum > 4 && inputNum < this.numInputs - 4) {
      inputNum  = inputNum + 4;
    } // if

    setFund();
    return this.adjustedFundArray[inputNum - 1];
  } // getAdjustedFund()

  /**
   *  @return  pitch of the Input, adjusted to ignore frequencies below a certain volume.
   */
  float  getAdjustedFundAsHz(int inputNum) {
    inputNumErrorCheck(inputNum, "getAdjustedFundAsHz(int)");

    if (inputNum > 4 && inputNum < this.numInputs - 4) {
      inputNum  = inputNum + 4;
    } // if

    setFund();
    return this.adjustedFundArray[inputNum - 1];
  } // getAdjustedFund()

  /**
   *  @return  pitch of the Input as a MIDI note, 
   * adjusted to ignore sounds below a certain volume.
   */
  float  getAdjustedFundAsMidiNote(int inputNum) {
    inputNumErrorCheck(inputNum, "getAdjustedFundAsMidiNote(int)");

    if (inputNum > 4 && inputNum < this.numInputs - 4) {
      inputNum  = inputNum + 4;
    } // if

    setFund();
    return Pitch.ftom(this.adjustedFundArray[inputNum - 1]);
  } // getAdjustedFund()

  /**
   *  @return  pitch of the Input.
   */
  float  getFund(int inputNum) {
    inputNumErrorCheck(inputNum, "getFund(int)");

    if (inputNum > 4 && inputNum < this.numInputs - 4) {
      inputNum  = inputNum + 4;
    } // if

    setFund();
    return this.fundamentalArray[inputNum - 1];
  } // getAdjustedFund()

  /**
   *  @return  pitch of the Input.
   */
  float getFundAsHz(int inputNum) {
    inputNumErrorCheck(inputNum, "getFundAsHz(int)");

    if (inputNum > 4 && inputNum < this.numInputs - 4) {
      inputNum  = inputNum + 4;
    } // if

    setFund();
    return this.fundamentalArray[inputNum - 1];
  } // getAdjustedFund()

  /**
   *  @return  pitch of the Input as a MIDI note.
   */
  float  getFundAsMidiNote(int inputNum) {
    inputNumErrorCheck(inputNum, "getFundAsMidiNote(int)");

    if (inputNum > 4 && inputNum < this.numInputs - 4) {
      inputNum  = inputNum + 4;
    } // if

    setFund();
    return Pitch.ftom(this.fundamentalArray[inputNum - 1]);
  } // getAdjustedFund()

  /**
   *  @return  float  The average pitch of the inputs whose numbers are given in the int[] param.
   */
  float  getAverageFund(int[] inputsToAverage)
  {
    if (inputsToAverage == null) {
      throw new IllegalArgumentException("Input_Jack.getAverageFund: int[] parameter is null.");
    } // error checking
    if (inputsToAverage.length < 1) {
      throw new IllegalArgumentException("Input_Jack.getAverageFund: int[] parameter's length is " + inputsToAverage.length + "; must be at least 1.");
    } // error checking

    float  result  = 0;

    for (int i = 0; i < inputsToAverage.length; i++)
    {
      result  += this.getAdjustedFund(inputsToAverage[i]);
    } // for

    return result/inputsToAverage.length;
  } // getAverageFund(int[])

  float getAverageFund(int firstInput, int lastInput)
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

    return getAverageFund(inputsToAverage);
  } // getAverageFund

  /**
   *  @return  float  The average pitch of the inputs whose numbers are given in the int[] param.
   */
  float  getAverageAmp(int[] inputsToAverage)
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

  float getAverageAmp(int firstInput, int lastInput)
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
   * @return  amplitude of the Frequency instance var.
   */
  float getAmplitude(int inputNum) {
    inputNumErrorCheck(inputNum, "getAmplitude(int)");

    if (inputNum > 4 && inputNum < this.numInputs - 4) {
      inputNum  = inputNum + 4;
    } // if

    return this.frequencyArray[inputNum - 1].getAmplitude();
  } // getAmplitude

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
  void setSensitivity(float newSensitivity)
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

import beads.FeatureExtractor;
import beads.TimeStamp;

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