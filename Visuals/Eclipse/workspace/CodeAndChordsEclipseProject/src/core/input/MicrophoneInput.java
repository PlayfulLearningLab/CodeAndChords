package core.input;

import processing.core.PApplet;
import processing.sound.*;

//import org.jaudiolibs.beads.AudioServerIO;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.TimeStamp;
import net.beadsproject.beads.core.UGen;
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

import com.portaudio.PortAudio;

public class MicrophoneInput extends Input {
	//UGen                   inputsUGen;           // initialized with the input from the AudioContext.
//	private UGen[]                 uGenArray;
	// TODO: make private after testing
//	public Gain[]		   gainArray;
	Gain                   mute;
//	float                  sensitivity;          // amplitude below which adjustedFreq will not be reset
	boolean					skip5thru8;

	public MicrophoneInput(PApplet parent, int[] inputChannels)
	{
		if(inputChannels == null || inputChannels.length < 1)  {
			throw new IllegalArgumentException("Input.constructor(PApplet, int[]): int[] parameter number of channels is less than 1; at least 1 input must be specified");
		} // if(numInputs < 1)
		
		this.parent = parent;
		this.inputChannels = inputChannels.clone();
		
		try{
			PortAudio.getVersion();
			this.ac = new AudioContext(new PortAudioAudioIO(inputChannels.length), 512, AudioContext.defaultAudioFormat(inputChannels.length, inputChannels.length));
		}
		catch( UnsatisfiedLinkError e ){
			System.err.println("Port Audio could not be found.  Switching to default audio context.\n"
					+ "Multiple Inputs will NOT be enabled.");

			this.ac = this.getNewAudioContext();
			this.inputChannels = new int[] {1};
		}
		
		this.disposeHandler	= new DisposeHandler(this.parent, this);

		this.makeUGenArray();
	}
	
	protected void makeUGenArray()
	{
		// fill the uGenArray with UGens, each one from a particular line of the AudioContext.
		this.uGenArray  = new UGen[this.inputChannels.length];
		this.gainArray	= new Gain[this.inputChannels.length];
		
		UGen	audioInput	= this.ac.getAudioInput(this.inputChannels.clone());
		
		for (int i = 0; i < this.uGenArray.length; i++)
		{
			this.uGenArray[i]  = new Plug(this.ac, audioInput, this.inputChannels[i]);
			this.gainArray[i]	= new Gain(this.ac, 0, 0);
		} // for

		initInput(this.uGenArray, 0);
	} // uGenArrayFromNumInputs

	
	/**
	 *  Fills the fundamentalArray and adjustedFundArray with the current pitches of each input line:
	 */
	public void setFund() {
		// catching a NullPointer because I'm not sure why it happens and fear a crash during a concert.
		try
		{
			// TODO: maybe this won't be necessary once the threads are implemented.
			if(!this.pause)
			{
				for (int i = 0; i < this.inputChannels.length; i++)
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
	 *  Setter for sensitivity float instance var.
	 *
	 *  @param  newSensitivity  float with the value to which sensitivity is to be set.
	 */
	public void setSensitivity(float newSensitivity)
	{
		this.sensitivity = newSensitivity;
	}

	public UGen[] getuGenArray() {
		return uGenArray;
	}

	public void setuGenArray(UGen[] uGenArray) {
		this.uGenArray = uGenArray;
	}

	
	public int getMidiNote(int channelIndex) 
	{
		if(channelIndex > this.inputChannels.length) throw new IllegalArgumentException("that input does not exist");
		return (int) Math.round(this.getAdjustedFundAsMidiNote(channelIndex));
	}
	
	@Override
	public int getMidiNote() 
	{
		return (int) Math.round(this.getAdjustedFundAsMidiNote());
	}

	@Override
	public String getInputType() 
	{
		return "Microphone Input";
	}

	@Override
	public boolean isRealTimeInput() 
	{
		return true;
	}

	@Override
	public boolean isPolyphonic() 
	{
		return false;
	}

	@Override
	public String getInputName() {
		return this.inputName;
	}
	
	public void setInputName(String inputName)
	{
		this.inputName = inputName;
	}
	


} // Input class