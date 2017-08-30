package core;

import processing.core.PApplet;
import processing.sound.*;

//import org.jaudiolibs.beads.AudioServerIO;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.TimeStamp;
import net.beadsproject.beads.core.UGen;
//import net.beadsproject.beads.core.io.JavaSoundAudioIO;
import net.beadsproject.beads.data.Pitch;
import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.data.SampleManager;
import net.beadsproject.beads.ugens.Compressor;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.MonoPlug;
import net.beadsproject.beads.ugens.MultiWrapper;
import net.beadsproject.beads.ugens.Plug;
import net.beadsproject.beads.ugens.SamplePlayer;
import net.beadsproject.beads.analysis.*;
import net.beadsproject.beads.analysis.featureextractors.*;
import net.beadsproject.beads.analysis.featureextractors.FFT;

//import org.jaudiolibs.beads.AudioServerIO;

/*
//Might need these eventually:
import net.beadsproject.beads.core.io.JavaSoundAudioIO;
import org.jaudiolibs.audioservers.jack.JackAudioServer;
import org.jaudiolibs.audioservers.javasound.*;
 */

/*
import beads.AudioContext
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
 */
import net.beadsproject.beads.analysis.segmenters.ShortFrameSegmenter;

import java.util.Set;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;

import org.jaudiolibs.beads.AudioServerIO;

public class Input {
	/**
	 * 08/01/2017
	 * 
	 * Emily Meuer
	 * 
	 * Version 02:
	 *  - Input no longer extends PApplet
	 *  - String[] filenames no longer accepted as constructor parameters
	 */
	
	DisposeHandler		disposeHandler;
	PApplet				pa;

	AudioContext         	ac;
	float[]                adjustedFundArray;    // holds the pitch, in hertz, of each input, adjusted to ignore pitches below a certain amplitude.
	Compressor             compressor;
	//UGen                   inputsUGen;           // initialized with the input from the AudioContext.
	private UGen[]                 uGenArray;
	// TODO: make private after testing
	public Gain[]		   gainArray;
	Gain                   mute;
	FFT[]                  fftArray;             // holds the FFT for each input.
	FrequencyEMM[]         frequencyArray;       // holds the FrequencyEMM objects connected to each input.
	float[]                fundamentalArray;     // holds the current pitch, in hertz, of each input.
	float[]					amplitudeArray;		// holds the current amplitude of each input; format undefined
	int                    numInputs;            // number of lines / mics
	int						adjustedNumInputs;
	//float                  pitch;                // 
	PowerSpectrum[]        psArray;              // holds the PowerSpectrum objects connected to each input.
	float                  sensitivity;          // amplitude below which adjustedFreq will not be reset
	ShortFrameSegmenter[]  sfsArray;             // holds the ShortFrameSegmenter objects connected to each input.
	//int                    waitUntil;            // number of milliseconds to wait before checking for another key 
	boolean					pause;
	boolean					skip5thru8;

	/**
	 *  Creates an Input object connected to PortAudio, with the given number of inputs.
	 *
	 *  @param  numInputs  an int specifying the number of lines in the AudioFormat.
	 */
	public Input(int numInputs, PApplet pa)
	{
//		this(numInputs, new AudioContext(new AudioServerIO.JavaSound(), 512, AudioContext.defaultAudioFormat(numInputs, numInputs)));
		this(numInputs, new AudioContext(new PortAudioAudioIO(numInputs), 512, AudioContext.defaultAudioFormat(numInputs, numInputs)), pa);
		
	} // constructor - int, AudioContext
	
	/**
	 *  Creates an Input object connected to PortAudio, with the given number of inputs,
	 * as well as the option to skip inputs 5-8 (this assumes that you will have adjusted for the skip
	 * and passed in 4 more inputs than you plan to need; e.g., numInputs = 16 for 12 lines).
	 *
	 *  @param  numInputs  an int specifying the number of lines in the AudioFormat.
	 */
	public Input(int numInputs, boolean skip5thru8, PApplet pa)
	{
		this(numInputs, new AudioContext(new PortAudioAudioIO(numInputs), 512, AudioContext.defaultAudioFormat(numInputs, numInputs)), skip5thru8, pa);

	} // constructor - int, AudioContext

	/**
	 *  Creates an Input object with the given number of inputs and particular AudioContext.
	 *
	 *  @param  numInputs     an int specifying the number of lines in the AudioFormat.
	 *  @param  audioContext  an AudioContext whose input lines will be procured as a UGen and used for the analysis calculations.
	 */
	public Input(int numInputs, AudioContext audioContext, PApplet pa)
	{
		this(numInputs, audioContext, false, pa);
	} // int, AudioContext
	
	/**
	 * Creates an Input object with the given number of inputs and the particular AudioContext,
	 * as well as the option to skip inputs 5-8 (this assumes that you will have adjusted for the skip
	 * and passed in 4 more inputs than you plan to need; e.g., numInputs = 16 for 12 lines).
	 * @param numInputs
	 * @param audioContext
	 * @param skip5thru8
	 */
	public Input(int numInputs, AudioContext audioContext, boolean skip5thru8, PApplet pa)
	{
		this(numInputs, audioContext, false, pa);
	} // int, AudioContext
	
	/**
	 * Creates an Input object with the given number of inputs and the particular AudioContext,
	 * as well as the option to skip inputs 5-8 (this assumes that you will have adjusted for the skip
	 * and passed in 4 more inputs than you plan to need; e.g., numInputs = 16 for 12 lines).
	 * @param numInputs
	 * @param audioContext
	 * @param skip5thru8
	 */
	public Input(int numInputs, AudioContext audioContext, boolean skip5thru8, PApplet pa)
	{
		if(numInputs < 1)  {
			throw new IllegalArgumentException("Input.constructor(int, AudioContext): int parameter " + numInputs + " is less than 1; must be 1 or greater.");
		} // if(numInputs < 1)
		if(audioContext == null) {
			throw new IllegalArgumentException("Input.constructor(int, AudioContext): AudioContext parameter " + audioContext + " is null.");
		} // if(numInputs < 1)

		this.numInputs  = numInputs;
		this.ac 		= audioContext;
		this.skip5thru8	= skip5thru8;
		
		if(this.skip5thru8)
		{
			this.adjustedNumInputs	= this.numInputs - 4;
			System.out.println("this.adjustedNumInputs = " + this.adjustedNumInputs);
		} else {
			this.adjustedNumInputs	= this.numInputs;
		} // 
		
		this.disposeHandler	= new DisposeHandler(pa, this);
		System.out.println("just registered DisposeHandler for " + pa);

		this.uGenArrayFromNumInputs(this.numInputs);
	} // constructor(int, AudioContext, boolean)
	
	/**
	 * Constructor for creating an Input object with 2 lines, 
	 * using the right and left channels of default in.
	 * 
	 * @param leftAndRight	Simply distinguishes this constructor from the others
	 */
/*	public Input(boolean leftAndRight)
	{
		this.numInputs	= 1;
		AudioIn	in1	= new AudioIn(this, 0);
		in1.play();
		/*
		this.numInputs	= 2;
		this.ac = new AudioContext();

		//TODO: might not need this:
		// creates an int[] of the input channel numbers - e.g., { 1, 2, 3, 4 } for a 4 channel input.
		int[]  inputNums  = new int[this.numInputs];
		for (int i = 0; i < this.numInputs; i++)
		{
			inputNums[i]  = i + 1;
			println("inputNums[" + i + "] = " + inputNums[i]);
		} // for
		
		// fill the uGenArray with UGens, each one from a particular line of the AudioContext.
		UGen	leftRightUGen	= this.ac.getAudioInput(inputNums);
		UGen	leftUGen	= new Throughput(this.ac, 1);
		UGen	rightUGen	= new Throughput(this.ac, 1);
		
		leftUGen.addInput(0, leftRightUGen, 0);
		rightUGen.addInput(0, leftRightUGen, 1);
		
		this.uGenArray = new UGen[this.numInputs];
		this.uGenArray[0]	= leftUGen;
		this.uGenArray[1]	= rightUGen;
//		this.uGenArray	= (UGen[])leftRightUGen.getConnectedInputs().toArray();

		// get the audio lines from the AudioContext:
		//  this.inputsUGen = ac.getAudioInput(inputNums);

		/*
		for (int i = 0; i < getuGenArray().length; i++)
		{
			// getAudioInput needs an int[] with the number of the particular line.
			getuGenArray()[i]  = ac.getAudioInput(new int[] {(i + 1)});
		}

		initInput(this.uGenArray);

	} // constructor(boolean, boolean)
		*/
	
	/**
	 * Constructor for creating a one (or two?)-channel Input object 
	 * from the machine's default audio input device;
	 * does not require Jack.
	 */
	public Input(PApplet pa)
	{
		this(2, pa); //, new AudioContext());
	} // constructor()

	public void uGenArrayFromNumInputs(int numInputs)
	{
		// TODO: make sure we set this everywhere else
//		this.numInputs  = numInputs;

		// creates an int[] of the input channel numbers - e.g., { 1, 2, 3, 4 } for a 4 channel input.
		int[][]	inputNums2d	= new int[this.numInputs][1];
		int[]	inputNums1d	= new int[this.numInputs];
		for (int i = 0; i < inputNums2d.length; i++)
		{
			inputNums2d[i][0]	= (i + 1);
			inputNums1d[i]		= (i + 1);
		} // for

		// get the audio lines from the AudioContext:
		//    this.inputsUGen = ac.getAudioInput(inputNums);

		// fill the uGenArray with UGens, each one from a particular line of the AudioContext.
		this.uGenArray  = new UGen[this.adjustedNumInputs];
		this.gainArray	= new Gain[this.adjustedNumInputs];
		System.out.println("uGenArray.length = " + this.uGenArray.length);
		
		UGen	audioInput	= this.ac.getAudioInput(inputNums1d);
		
		int	channelPos	= 0;
		for (int i = 0; i < uGenArray.length; i++)
		{
//			uGenArray[i]  = this.ac.getAudioInput(inputNums[i]);
			if(channelPos == 4 && this.skip5thru8)
			{
				channelPos	= 8;
			}
			
			uGenArray[i]  = new Plug(this.ac, audioInput, channelPos);
			System.out.println("Input: uGenArray[" + i + "] = " + uGenArray[i]);
			this.gainArray[i]	= new Gain(this.ac, 0, 0);
			
			channelPos	= channelPos + 1;
		} // for

		initInput(uGenArray);
	} // uGenArrayFromNumInputs


	/**
	 *  As of 10/24/2016, does everything that was in the (int, AudioContext) constructor;
	 *  that is, initialize the Gain, add everything in the given UGen[] to it,
	 *  and analyze the frequencies w/SFS, PS, FFT, Frequency.
	 *
	 *  @param  uGenArray  a UGen[] whose members will be added to a gain, analyzed, and added to the AudioContext.
	 */
	private void initInput(UGen[] uGenArray)
	{
		
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
		this.compressor.setRatio(8);

		// Create a Gain, add the Compressor to the Gain,
		// add each of the UGens from uGenArray to the Gain, and add the Gain to the AudioContext:
//		g = new Gain(this.ac, 1, (float) 0.5);

		this.gainArray	= new Gain[this.adjustedNumInputs];
		for(int i = 0; i < this.gainArray.length; i++)
		{
			if(this.gainArray[i] == null)
			{
				this.gainArray[i]	= new Gain(this.ac, 0, 0);
				// TODO: uncomment this line to hear what's going in the mics (e.g., through headphones):
//				this.gainArray[i]	= new Gain(this.ac, 1, 0.5f);
			}
			
			this.gainArray[i].addInput(this.compressor);
			this.gainArray[i].addInput(this.uGenArray[i]);
			this.ac.out.addInput(this.gainArray[i]);
		}


		// The ShortFrameSegmenter splits the sound into smaller, manageable portions;
		// this creates an array of SFS's and adds the UGens to them:
		this.sfsArray  = new ShortFrameSegmenter[this.adjustedNumInputs];
		for (int i = 0; i < this.sfsArray.length; i++)
		{
			this.sfsArray[i] = new ShortFrameSegmenter(this.ac);
			while (this.sfsArray[i] == null) {
			}
			this.sfsArray[i].addInput(uGenArray[i]);
		}

		// Creates an array of FFTs and adds them to the SFSs:
		this.fftArray  = new FFT[this.adjustedNumInputs];
		for (int i = 0; i < this.fftArray.length; i++)
		{
			this.fftArray[i] = new FFT();
			while (this.fftArray[i] == null) {
			}
			this.sfsArray[i].addListener(this.fftArray[i]);
		} // for

		// Creates an array of PowerSpectrum's and adds them to the FFTs
		// (the PowerSpectrum is what will actually perform the FFT):
		this.psArray  = new PowerSpectrum[this.adjustedNumInputs];
		for (int i = 0; i < this.psArray.length; i++)
		{
			this.psArray[i] = new PowerSpectrum();
			while (this.psArray[i] == null) {
			}
			this.fftArray[i].addListener(psArray[i]);
		} // for

		// Creates an array of FrequencyEMMs and adds them to the PSs
		// (using my version of the Frequency class - an inner class further down - to allow access to amplitude):
		this.frequencyArray  = new FrequencyEMM[this.adjustedNumInputs];
		for (int i = 0; i < this.frequencyArray.length; i++)
		{
			this.frequencyArray[i] = new FrequencyEMM(44100);
			while (this.frequencyArray[i] == null) {
			}
			this.psArray[i].addListener(frequencyArray[i]);
		} // for

		// Adds the SFSs (and everything connected to them) to the AudioContext:
		for (int i = 0; i < this.adjustedNumInputs; i++)
		{
			this.ac.out.addDependent(sfsArray[i]);
		} // for - addDependent


		// Starts the AudioContext (and everything connected to it):
			this.ac.start();

		// Pitches with amplitudes below this number will be ignored by adjustedFreq:
		this.sensitivity  = 10;

		// Initializes the arrays that will hold the pitches:
		this.fundamentalArray = new float[this.adjustedNumInputs];
		this.adjustedFundArray = new float[this.adjustedNumInputs];
		this.amplitudeArray	= new float[this.adjustedNumInputs];

		// Gets the ball rolling on analysis:
		this.setFund();
	} // initInput(UGen[])

	/**
	 * @return  int  number of input channels.
	 */
	public int  getNumInputs() {
		return this.numInputs;
	} // getNumInputs
	
	/**
	 * 
	 * @return	int number of input channels, adjusted for skipping lines 5-8 (if skipped, will be 4 less than this.numInputs)
	 */
	public int getAdjustedNumInputs() {
		return this.adjustedNumInputs;
	} // getAdjustedNumInputs

	/**
	 *  Fills the fundamentalArray and adjustedFundArray with the current pitches of each input line:
	 */
	public void setFund()
	{
		// catching a NullPointer because I'm not sure why it happens and fear a crash during a concert.
		try
		{
			// TODO: maybe this won't be necessary once the threads are implemented.
			if(!this.pause)
			{
				for (int i = 0; i < this.adjustedNumInputs; i++)
				{
					//     println("setFund(); this.frequencyArray[i] = " + this.frequencyArray[i].getFeatures());

					// want to assign the value of .getFeatures() to a variable and check for null,
					// but can't, b/c it returns a float. :/  (So that must not be exactly the problem.)
					if (this.frequencyArray[i].getFeatures() != null) {
						//       println("i = " + i);
						//       println("setFund(); this.fundamentalArray[i] = " + this.fundamentalArray[i] + "this.frequencyArray[i].getFeatures() = " + this.frequencyArray[i].getFeatures());
						this.fundamentalArray[i] = this.frequencyArray[i].getFeatures();
						this.amplitudeArray[i]	= this.frequencyArray[i].getAmplitude(); // * 100;

						// ignores pitches with amplitude lower than "sensitivity":
						if (this.frequencyArray[i].getAmplitude() > this.sensitivity) {
							this.adjustedFundArray[i]  = this.fundamentalArray[i];
						} // if: amp > sensitivity
					} // if: features() != null
				} // if: > numInputs
			}
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
		
		this.setFund();

//		return this.frequencyArray[inputNum - 1].getAmplitude();
		return this.amplitudeArray[inputNum - 1];
	} // getAmplitude

	/**
	 *  As of 6/13, the following is not true (a Compressor has been added to the chain instead):
	 *  Applies a 1:8 compressor for amp's over 400 and returns the resulting amplitude.
	 *
	 *  @return  float     amplitude of the first input line.
	 */
	public float getAmplitude()  
	{
//		float  amp  = this.frequencyArray[0].getAmplitude();
//		return amp;

		this.setFund();
		return this.amplitudeArray[0];
	}

	/**
	 *  Error checker for ints sent to methods such as getFund, getAmplitude, etc.;
	 *  rejects numbers that are greater than the number of inputs or less than 1.
	 *
	 *  @param   inputNum  an int that is to be checked for suitability as an input line number.
	 *  @param   String    name of the method that called this method, used in the exception message.
	 */
	private void inputNumErrorCheck(int inputNum, String method) {
		if (inputNum > this.adjustedNumInputs) {
			IllegalArgumentException iae = new IllegalArgumentException("Input.inputNumErrorCheck(int), from " + method + ": int parameter " + inputNum + " is greater than " + this.adjustedNumInputs + ", the number of inputs.");
			iae.printStackTrace();
		}
		if (inputNum < 1) {
			IllegalArgumentException iae = new IllegalArgumentException("Input.inputNumErrorCheck(int), from " + method + ": int parameter is " + inputNum + "; must be 1 or greater.");
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

	/*
import beads.FeatureExtractor;
import beads.TimeStamp;
	 */

	public UGen[] getuGenArray() {
		return uGenArray;
	}

	public void setuGenArray(UGen[] uGenArray) {
		this.uGenArray = uGenArray;
	}

	/**
	 * Frequency processes spectral data forwarded to it by a {@link PowerSpectrum}
	 * to determine the best estimate for the frequency of the current signal.
	 *
	 * @beads.category analysis
	 */
	private class FrequencyEMM extends FeatureExtractor<Float, float[]> {

		/** The Constant FIRSTBAND. */
		static final int FIRSTBAND = 3;

		/** The ratio bin2hz. */
		private float bin2hz;

		private int bufferSize;

		private  float[]  hps;      // Harmonic Product Spectrum summed up here

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

			for (int band = FIRSTBAND; band < powerSpectrum.length; band++) {
				double pwr = powerSpectrum[band];
				if (pwr > pmax) {
					pmax = pwr;
					maxbin = band;
				} // if
			} // for

			// I added the following line;
			// 10/5 edits may cause it to be a larger num than it was previously:
			amplitude  = (float)pmax;

			// cubic interpolation
			double yz = powerSpectrum[maxbin];
			double ym;
			if(maxbin <= 0) {
				ym = powerSpectrum[maxbin];
			} else {
				ym = powerSpectrum[maxbin - 1];
			} // else

			double yp;
			if(maxbin < powerSpectrum.length - 1) {
				yp  = powerSpectrum[maxbin + 1];
			} else {
				yp  = powerSpectrum[maxbin];
			} // else

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
	} // FrequencyEMM

	public void setFundamentalArray(float[] newVal) {
		if(newVal == null)
		{
			throw new IllegalArgumentException("Input.setFundamentalArray: float[] parameter is null.");
		}
		
		if(newVal.length <= this.fundamentalArray.length)
		{
			for(int i = 0; i < newVal.length; i++)
			{
				this.fundamentalArray[i]	= newVal[i];
			} // for
		} // if
	} // setFundamentalArray
	
	public void setFundamental(float newVal, int inputNum)
	{
		if(inputNum > this.adjustedNumInputs)
		{
			throw new IllegalArgumentException("Input.setFundamental: int parameter " + inputNum + 
					" is out of bounds; must be within within 0 and " + (this.adjustedNumInputs - 1));
		}
		
		this.fundamentalArray[inputNum]	= newVal;
	} // setFundamental

	public void setAdjustedFundArray(float[] newVal) {
		if(newVal == null)
		{
			throw new IllegalArgumentException("Input.setAdjustedFundArray: float[] parameter is null.");
		}
		
		if(newVal.length <= this.adjustedFundArray.length)
		{
			for(int i = 0; i < newVal.length; i++)
			{
				this.adjustedFundArray[i]	= newVal[i];
			} // for
		} // if
	} // setAdjustedFundArray
	

	public void setAdjustedFund(float newVal, int inputNum)
	{
		if(inputNum > this.adjustedNumInputs)
		{
			throw new IllegalArgumentException("Input.setAdjustedFund: int parameter " + inputNum + 
					" is out of bounds; must be within within 0 and " + (this.adjustedNumInputs - 1));
		}
		
		this.adjustedFundArray[inputNum]	= newVal;
	} // setAdjustedFund
	
	public void setAmplitudeArray(float[] newVal) {
		if(newVal == null)
		{
			throw new IllegalArgumentException("Input.setAmplitudeArray: float[] parameter is null.");
		}
		
		if(newVal.length <= this.amplitudeArray.length)
		{
			for(int i = 0; i < newVal.length; i++)
			{
				this.amplitudeArray[i]	= newVal[i];
			} // for
		} // if
	} // setAmplitudeArray

	public void setAmplitude(float newVal, int inputNum)
	{
		if(inputNum > this.adjustedNumInputs)
		{
			throw new IllegalArgumentException("Input.setAmplitude: int parameter " + inputNum + 
					" is out of bounds; must be within within 0 and " + (this.adjustedNumInputs - 1));
		}
		
		this.amplitudeArray[inputNum]	= newVal;
	} // setAmplitude
	
	/**
	 * Pauses the Gain connected to ac.out.
	 * 
	 * @param pause boolean indicating whether to pause or un-pause the Gain
	 */
	public void pause(boolean pause)
	{	
		System.out.println("Input.pause: pause = " + pause);
		this.pause	= pause;
		
		this.ac.out.pause(this.pause);
		
		/*
		for(int i = 0; i < this.uGenArray.length; i++)
		{
			uGenArray[i].pause(pause);
		}
		*/
	} // pause
	

	/**
	 * Stops the AudioContext, which will stop the AudioIO.
	 */
	public void stop()
	{
		this.ac.stop();
	} // stop
	
	/**
	 * 08/01/2017
	 * Emily Meuer
	 * 
	 * Class to stop the Input (which needs to stop the AudioContext,
	 * because it needs to stop the AudioIO, esp. when it's using the PortAudioAudioIO,
	 * which needs to call PortAudio.terminate to avoid a weird set of 
	 * NoClassDefFoundError/ClassNotFoundException/BadFileDescriptor errors that will happen occasionally on start-up).
	 * 
	 * Taken from https://forum.processing.org/two/discussion/579/run-code-on-exit-follow-up
	 *
	 */
	public class DisposeHandler {

		Input	input;

		DisposeHandler(PApplet pa, Input input)
		{
			pa.registerMethod("dispose", this);
			this.input	= input;
		}

		public void dispose()
		{
			this.input.stop();
		}
	} // DisposeHandler
	
	/**
	 * Getter for this.ac
	 * @return	AudioContext instance variable
	 */
	public AudioContext getAudioContext()
	{
		return this.ac;
	} // getAudioContext
	
	public class DisposeHandler {

//		PApplet	pa;
		Input	input;

		DisposeHandler(PApplet pa)
		{
			this.input	= (Input)pa;
			pa.registerMethod("dispose", this);
		}

		public void dispose()
		{
			this.input.ac.stop();
			if(this.input.ac.getAudioIO().getClass().getName().equalsIgnoreCase("PortAudioAudioIO"))
			{
				((PortAudioAudioIO)this.input.ac.getAudioIO()).stop();
			} // if - PAAIO
			
			println("Closing sketch");
//			((PortAudioAudioIO)this.module.input.getAudioContext().getAudioIO()).destroy();
		}
	} // DisposeHandler

	
} // Input class