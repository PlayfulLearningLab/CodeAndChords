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

public class SceneGameOfLife_EMM extends PApplet {

/*
    10/09/2016
 Turned this scene into a stand-alone module.
 
 07/04/2016
 Emily Meuer
 Turning GameOfLife_ColorChange_EMM into a Scene class.
 
 From GameOfLife_ColorChange_EMM:
 * 07/03/2016
 * Emily Meuer
 *  Colors change each generation, based on LR inputs.
 *
 * Amanda Tenhoff
 * Emily Meuer
 * Game of Life
 * 6/26/2016
 * translation from JavaScript to Processing
 */

int liveColor;    // color of live squares; currently purple.
int deadColor;    // color of dead squares; currently white.

int      tileSize;    //  the board is this many tiles wide.
int[][]  newboard;    //  holds the values for the generation being currently calculated.
int[][]  oldboard;    //  holds the values for the previous generation that will determine the values in newboard.
int i=0;    // counts through new and oldboard.
int j=0;    // counts through new and oldboard.

float  red;
float  green  = 50;
float  blue;

String  seed;        // specifies whether the board will seed with a "ring", "square", or "rPentomino". 
int     waitUntil;   // keeps a long press from counting as many consecutive pressed.
int     genCount;    // keeps track of the number of generations.

public void setup()
{
  
  
  // Here's where we'll choose the seed:
  this.seed         = "ring";

  this.tileSize = width/10;

  this.newboard = new int[tileSize][height/10];
  this.oldboard = new int[tileSize][height/10];

  setLiveColor();
  this.deadColor  = color(255);

  this.genCount   = 0;
  this.waitUntil  = millis();
} // setup()

/**
 *  Called in draw() in the Zikr_Scenes_Inputs1_9_EMM tab;
 *  if this is the first generation, it seeds the board (seed specified in constructor),
 *  else it creates the next generation on mouse press.
 */
public void draw()
{
  strokeWeight(1);

  if (genCount == 0)
  {
    if (seed == "ring") {
      this.seedRing();
    } else if (seed == "square") {
      this.seedSquare();
    } else if (seed == "rPentomino") {
      this.seedRPentomino();
    } else {
      throw new IllegalArgumentException("GameOfLife.constructor: seed " + seed + " is not recognized; please use either \"ring\", \"square\", or \"rPentomino\".");
    } // seed if's
  } // genCount == 0

  if (mousePressed && millis() > this.waitUntil)
  {
    nextGeneration();
    this.waitUntil += 100;
  } // mousePressed
}// run()

/**
 *  Setter for the liveColor instance variable;
 *  can be modified to use pitchColor to determine the color.
 */
public void setLiveColor()
{
  //pitchColor();      // method inherited from Scene that sets red and blue depending on pitches of high and low inputs.
  //   green = (green + 30) % 255;
  //liveColor  = color(red, green, blue);
  liveColor = color(153, 49, 245);  //royal purple
} // setLiveColor

/**
 *  Getter for the liveColor instance variable.
 */
public int getLiveColor() {  
  return liveColor;
}

/**
 *  Loops through oldboard and determines the value of the corresponding cell in newboard
 *  based on how many dead and alive neighbors each oldboard cell has.
 */
public void nextGeneration()
{
  setLiveColor();

  for (i=0; i<oldboard.length; i++)
  {
    for (j=0; j<oldboard[i].length; j++)
    {
      int deadCount = getDeadCount(i, j);
      int liveCount = getAliveCount(i, j);

      if (oldboard[i][j] == 0)              //dealing with the dead :o
      {
        if (deadCount==3)
        {
          newboard[i][j] = 1;
        }//if dead
        else
        {
          newboard[i][j] = 0;
        }//else dead
      }//if get

      if (oldboard[i][j] == 1)              //dealing with the living
      {
        if (liveCount<2 || liveCount>3)
        {
          newboard[i][j] = 0;
        } else if (liveCount>=2 && liveCount<4)
        {
          newboard[i][j] = 1;
        }
      }//else if alive
    }//for j
  }//for i


  for (i=0; i<oldboard.length; i++)
  {
    for (j=0; j<oldboard[i].length; j++)
    {
      //      setLiveColor();
      int curLiveColor  = getLiveColor();

      //  resets oldboard to the values in newboard (since newboard is now the oldboard for the next generation),
      //  and draws the squares corresponding to each array position / cell.
      if (newboard[i][j]==0)
      {
        oldboard[i][j]=0;
        stroke(0);
        fill(deadColor);
        rect(i*10, j*10, 10, 10);
      } else
      {
        oldboard[i][j]=1;
        stroke(0);
        fill(curLiveColor);
        rect(i*10, j*10, 10, 10);
      }
    }
  }

  this.genCount++;
} // nextGeneration()

/**
 *  Seeds the board with a square.
 */
public void seedSquare()
{
  for (i=0; i<oldboard.length; i++)
  {
    for (j=0; j<oldboard[i].length; j++)
    {
      //   sets the middle 9 cells to alive and draws their squares in livecolor.
      if (i>=(oldboard.length)/2-1 && (i<=(oldboard.length)/2+1) &&(j<=(oldboard[i].length)/2+1) && (j>=(oldboard[i].length)/2-1))
      {
        stroke(0);
        fill(liveColor);
        rect(i*10, j*10, 10, 10);
        oldboard[i][j]  = 1;
      } else
      {
        stroke(0);
        fill(deadColor);
        rect(i*10, j*10, 10, 10);
        oldboard[i][j]  = 0;
      }
    } // for - j
  }// for - i
} // seedSquare

/**
 *  Seeds the board with a ring.
 */
public void seedRing()
{
  for (i=0; i<oldboard.length; i++)
  {
    for (j=0; j<oldboard[i].length; j++)
    {
      //   sets the 8 cells around the center to alive and draws their squares in livecolor.
      if ((i==(oldboard.length)/2-1 && (j<=(oldboard[i].length)/2+1) && 
        (j>=(oldboard[i].length)/2-1)) || ((i==(oldboard.length)/2+1) && 
        (j<=(oldboard[i].length)/2+1) && (j>=(oldboard[i].length)/2-1)))
      {
        stroke(0);
        fill(liveColor);
        rect(i*10, j*10, 10, 10);
        oldboard[i][j]  = 1;
      } else if ((i==(oldboard.length)/2 && j==(oldboard[i].length)/2+1) || (i==(oldboard.length)/2 && j==(oldboard[i].length)/2-1))
      {
        stroke(0);
        fill(liveColor);
        rect(i*10, j*10, 10, 10);
        oldboard[i][j]  = 1;
      } else
      {
        stroke(0);
        fill(deadColor);
        rect(i*10, j*10, 10, 10);
        oldboard[i][j]  = 0;
      }
    } // for - j
  }// for - i
} // seedRing

/**
 *  Seeds the board with an r-pentomino shape.
 */
public void seedRPentomino()
{
  for (i=0; i<oldboard.length; i++)
  {
    for (j=0; j<oldboard[i].length; j++)
    {
      //   sets the r-pentomino positions in the arrays to alive and draws their squares in livecolor.
      if (i==(oldboard.length)/2 && (j>=(oldboard[i].length)/2-1) &&(j<=(oldboard[i].length)/2+1))
      {
        stroke(0);
        fill(liveColor);
        rect(i*10, j*10, 10, 10);
        oldboard[i][j]  = 1;
      } else if (j==(oldboard[i].length)/2-1 && i==(oldboard.length)/2+1)
      {
        stroke(0);
        fill(liveColor);
        rect(i*10, j*10, 10, 10);
        oldboard[i][j]  = 1;
      } else if (j==(oldboard.length)/2 && i==(oldboard.length)/2-1)
      {
        stroke(0);
        fill(liveColor);
        rect(i*10, j*10, 10, 10);
        oldboard[i][j]  = 1;
      } else
      {
        stroke(0);
        fill(deadColor);
        rect(i*10, j*10, 10, 10);
        oldboard[i][j]  = 0;
      }
    } // for - j
  }// for - i (drawing the initial board, R-pentomino)
} // seedRPentomino

/**
 *  Counts how many dead neighbors a cell at a particular location has.
 *  
 *  @param  i  an int specifying the cell's location in the first dimension of the array.
 *  @param  j  an int specifying the cell's location in the second dimension of the array.
 *
 *  @return  int  the number of dead cells that surround the cell in question.
 */
public int getDeadCount(int i, int j)
{
  int dead=0;
  int neigh, bors;
  for (neigh=i-1; neigh<i+2; neigh++)
  {
    for (bors=j-1; bors<j+2; bors++)
    {
      if ( neigh>=0 && bors>=0 && 
        neigh < oldboard.length && bors < oldboard[neigh].length &&
        (oldboard[neigh][bors] == 0) && neigh!=i && bors!=j)
      {
        dead++;
      }
    }
  }
  return dead;
}//dead count

/**
 *  Counts how many living neighbors a cell at a particular location has.
 *  
 *  @param  i  an int specifying the cell's location in the first dimension of the array.
 *  @param  j  an int specifying the cell's location in the second dimension of the array.
 *
 *  @return  int  the number of living cells that surround the cell in question.
 */
public int getAliveCount(int i, int j)
{
  int alive=0;
  int neigh, bors;
  for (neigh=i-1; neigh<i+2; neigh++)
  {
    for (bors=j-1; bors<j+2; bors++)
    {
      if ( neigh>=0 && bors>=0 && 
        neigh < oldboard.length && bors < oldboard[neigh].length &&
        (oldboard[neigh][bors] == 0) && neigh!=i && bors!=j)
      {
        alive++;
      }
    }
  }
  return alive;
}//live count



















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
  public void settings() {  fullScreen(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--present", "--window-color=#FA0F0F", "--stop-color=#cccccc", "SceneGameOfLife_EMM" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
