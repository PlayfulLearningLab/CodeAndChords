import processing.core.PApplet;

//import org.jaudiolibs.beads.AudioServerIO;
//import org.jaudiolibs.beads.*;

import net.beadsproject.beads.core.*;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.data.BufferFactory;
import net.beadsproject.beads.data.Pitch;
import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.data.SampleManager;
import net.beadsproject.beads.data.buffers.HanningWindow;
import net.beadsproject.beads.ugens.BiquadFilter;
import net.beadsproject.beads.ugens.Compressor;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.LPRezFilter;
import net.beadsproject.beads.ugens.SamplePlayer;
import net.beadsproject.beads.analysis.*;
import net.beadsproject.beads.analysis.featureextractors.*;

import org.jaudiolibs.beads.AudioServerIO;

// Might need these eventually:
import net.beadsproject.beads.core.io.JavaSoundAudioIO;
import org.jaudiolibs.audioservers.jack.JackAudioServer;
import org.jaudiolibs.audioservers.javasound.*;
import org.jaudiolibs.audioservers.*;
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

import javax.sound.sampled.AudioFormat;

public class Input extends PApplet {
	/*
	 * 11/04/2016
	 * 
	 * This looks super important for us:
	 * https://github.com/jaudiolibs/audioservers.
	 * 
	 * 10/05/2016 Using the Harmonic Product Spectrum to better locate the
	 * pitch.
	 ** 
	 * As of mid-July, 2016, the following is NOT true: "Notate bene: inputNum
	 * passed to constructor must be 4 greater than the actual desired number of
	 * inputs."
	 ** 
	 * Watch for that NullPointerException -- add a try-catch? ** - 8/17: added
	 * try/catch
	 * 
	 * (Why doesn't it need jna-jack__.jar in the code folder? B/c it's in the
	 * old one?)
	 * 
	 * Emily Meuer 07/06 Update: works with both the Behringer and Motu
	 * interfaces to get more than 4 inputs!
	 * 
	 * 06/29/2016
	 * 
	 * Updating InputClass_EMM to communicate with Jack; based on BeadsJNA.
	 * 
	 * To change in classes that implement this: - size()/settings() can be in
	 * main tab. - getFund() etc. takes an int parameter specifying which input
	 * is in question.
	 ** 
	 * If I pass it an AudioContext, do I have to worry about it having the
	 * wrong number of inputs or outputs? - an option would be to have them pass
	 * the AudioFormat, since that's what has channel nums, ut credo.
	 * 
	 */

	AudioContext ac;
	float[] adjustedFundArray; // holds the pitch, in hertz, of each input,
	// adjusted to ignore pitches below a certain
	// amplitude.
	float[]	amplitudeArray;
	BiquadFilter[]	lpbfArray;
	BiquadFilter[]	hpbfArray;
	Compressor[] compressorArray;
	// UGen inputsUGen; // initialized with the input from the AudioContext.
	UGen[] uGenArray;
	Gain[] gainArray;
	Gain mute;
	FFT[] fftArray; // holds the FFT for each input.
	FrequencyEMM[] frequencyArray; // holds the FrequencyEMM objects connected
	// to each input.
	float[] fundamentalArray; // holds the current pitch, in hertz, of each
	// input.
	HarmonicProductSpectrum[]	hpsArray;
	LPRezFilter	lprf;
	int numInputs; // number of lines / mics
	// float pitch; //
	PowerSpectrum[] psArray; // holds the PowerSpectrum objects connected to
	// each input.
	float sensitivity; // amplitude below which adjustedFreq will not be reset
	ShortFrameSegmenter[] sfsArray; // holds the ShortFrameSegmenter objects
	// connected to each input.
	// int waitUntil; // number of milliseconds to wait before checking for
	// another key
	private SampleManager sampleManager;
	private SpectralPeaks[]	spArray;

	/**
	 * Creates an Input object connected to Jack, with the given number of
	 * inputs.
	 *
	 * @param numInputs
	 *            an int specifying the number of lines in the AudioFormat.
	 */
	Input(int numInputs) {
		this(numInputs,
				new AudioContext(new AudioServerIO.Jack(), 512, AudioContext.defaultAudioFormat(numInputs, numInputs)));
	} // constructor - int, AudioContext

	/**
	 * Creates an Input object with the given number of inputs and particular
	 * AudioContext.
	 *
	 * @param numInputs
	 *            an int specifying the number of lines in the AudioFormat.
	 * @param audioContext
	 *            an AudioContext whose input lines will be procurred as a UGen
	 *            and used for the analysis calculations.
	 */
	Input(int numInputs, AudioContext audioContext) {
		if (numInputs < 1) {
			throw new IllegalArgumentException("Input.constructor(int, AudioContext): int parameter " + numInputs
					+ " is less than 1; must be 1 or greater.");
		} // if(numInputs < 1)
		if (audioContext == null) {
			throw new IllegalArgumentException(
					"Input.constructor(int, AudioContext): AudioContext parameter " + audioContext + " is null.");
		} // if(numInputs < 1)

		this.numInputs = numInputs;
		this.ac = audioContext;

		// creates an int[] of the input channel numbers - e.g., { 1, 2, 3, 4 }
		// for a 4 channel input.
		int[] inputNums = new int[this.numInputs];
		for (int i = 0; i < this.numInputs; i++) {
			inputNums[i] = i + 1;
			println("inputNums[" + i + "] = " + inputNums[i]);
		} // for

		// get the audio lines from the AudioContext:
		// this.inputsUGen = ac.getAudioInput(inputNums);

		// fill the uGenArray with UGens, each one from a particular line of the
		// AudioContext.
		uGenArray = new UGen[this.numInputs];
		for (int i = 0; i < uGenArray.length; i++) {
			// getAudioInput needs an int[] with the number of the particular
			// line.
			uGenArray[i] = ac.getAudioInput(new int[] { (i + 1) });
		}

		initInput(uGenArray);
	} // constructor(int)

	/**
	 * Constructor for creating a one (or two?)-channel Input object from the
	 * machine's default audio input device; does not require Jack.
	 */
	Input() {
		this(1, new AudioContext());
	} // constructor()

	/**
	 * Constructor for creating an Input object from an audio file.
	 *
	 * @param filename
	 *            String specifying the audio file.
	 */
	Input(String filename) {
		this(new String[] { filename });
	} // constructor(String)

	/**
	 * Constructor for creating an Input object from an audio file.
	 *
	 * @param filename
	 *            String specifying the audio file.
	 */
	Input(String[] filenames) {
		this.ac = new AudioContext();
		this.numInputs = filenames.length;
		this.sampleManager = new SampleManager();
		Sample[] samples = new Sample[filenames.length];

		try {

			for (int i = 0; i < samples.length; i++) {
				samples[i] = new Sample(sketchPath("") + "src/" + filenames[i]);
			} // for
		} catch (Exception e) {
			// if there is an error, show an error message (at the bottom of the
			// processing window)
			println("Exception while attempting to load sample!");
			e.printStackTrace(); // then print a technical description of the
			// error
			exit(); // and exit the program
		}

		for (int i = 0; i < samples.length; i++) {
			SampleManager.addToGroup("group", samples[i]);
		} // for

		uGenArray = new UGen[SampleManager.getGroup("group").size()];
		for (int i = 0; i < uGenArray.length; i++) {
			// Samples are not UGens, but SamplePlayers are; thus; make a
			// SamplePlayer to put in uGenArray.
			uGenArray[i] = new SamplePlayer(ac, SampleManager.getGroup("group").get(i));
		} // for

		initInput(uGenArray);
	} // constructor(String[])

	/**
	 * As of 10/24/2016, does everything that was in the (int, AudioContext)
	 * constructor; that is, initialize the Gain, add everything in the given
	 * UGen[] to it, and analyze the frequencies w/SFS, PS, FFT, Frequency.
	 *
	 * @param uGenArray
	 *            a UGen[] whose members will be added to a gain, analyzed, and
	 *            added to the AudioContext.
	 */
	void initInput(UGen[] uGenArray) {
		// TODO: check with multiple inputs; do I really need to put *everything* in arrays?
		// (I did so to begin with [1/2/2017] because I will need an individual PowerSpectrum passed to an 
		// individual FrequencyEMM for each line, and this seemed the simplest way to split them up.)

		this.lpbfArray	= new BiquadFilter[this.numInputs];
		this.hpbfArray	= new BiquadFilter[this.numInputs];
		//TODO: if things are weird, this might not need 2 channels (depends on the mics?):
		// uGenArray => high-pass filter; high-pass filter => low-pass filter => rest of the chain.
		for(int i = 0; i < this.numInputs; i++)
		{
			// low-pass filter set to 1046.5 (C6 - 2 octaves above middle C)
			this.lpbfArray[i]	= new BiquadFilter(this.ac, 2, BiquadFilter.LP);
			this.lpbfArray[i].setFrequency((float)1046.5);
			this.lpbfArray[i].addInput(uGenArray[i]);

			// high-pass filter set to 65.41 (C2)
			this.hpbfArray[i]	= new BiquadFilter(this.ac, 2, BiquadFilter.HP);
			this.hpbfArray[i].setFrequency((float)65.41);
			this.hpbfArray[i].addInput(uGenArray[i]);

		}

		/*
		 * Default compressor values: threshold - .5 attack - 1 decay - .5 knee
		 * - .5 ratio - 2 side-chain - the input audio
		 */
		// Create a compressor w/standard values:
		this.compressorArray	= new Compressor[this.numInputs];
		for(int i = 0; i < this.numInputs; i++)
		{
			this.compressorArray[i] = new Compressor(this.ac, 1);
			this.compressorArray[i].setRatio(8);
			this.compressorArray[i].addInput(hpbfArray[i]);
		}

		// Create a Gain, add the Compressor to the Gain,
		// add each of the UGens from uGenArray to the Gain, and add the Gain to
		// the AudioContext:
		this.gainArray	= new Gain[this.numInputs];
		for(int i = 0; i < this.numInputs; i++)
		{
			gainArray[i] = new Gain(this.ac, 1, (float) 0.5);
			gainArray[i].addInput(this.compressorArray[i]);
		}

		// Do the following in a method that can be passed a Gain, UGen[], and
		// AudioContext.
		/*
		for (int i = 0; i < this.numInputs; i++) {
			g.addInput(uGenArray[i]);
		} // for
		 */
		//		this.ac.out.addInput(g);

		// The ShortFrameSegmenter splits the sound into smaller, manageable
		// portions;
		// this creates an array of SFS's and adds the UGens to them:
		this.sfsArray = new ShortFrameSegmenter[this.numInputs];
		for (int i = 0; i < this.sfsArray.length; i++) {
			this.sfsArray[i] = new ShortFrameSegmenter(this.ac);
			while (this.sfsArray[i] == null) {
			}
			this.sfsArray[i].addInput(gainArray[i]);
		}


		// Creates an array of FFTs and adds them to the SFSs:
		this.fftArray = new FFT[this.numInputs];
		for (int i = 0; i < this.fftArray.length; i++) {
			this.fftArray[i] = new FFT();
			while (this.fftArray[i] == null) {
			}
			this.sfsArray[i].addListener(this.fftArray[i]);
		} // for


		// Creates an array of PowerSpectrums and adds them to the FFTs
		// (the PowerSpectrum is what will actually perform the FFT - 1/2/2017 update: that is false):
		this.psArray = new PowerSpectrum[this.numInputs];
		for (int i = 0; i < this.psArray.length; i++) {
			this.psArray[i] = new PowerSpectrum();
			while (this.psArray[i] == null) {
			}
			this.fftArray[i].addListener(this.psArray[i]);
		} // for

		// Creates an array of HarmonicProductSpectrums and adds them to the PowerSpectrums:
		this.hpsArray	= new HarmonicProductSpectrum[this.numInputs];
		println("this.hpsArray = " + this.hpsArray);
		for(int i = 0; i < this.numInputs; i++)
		{
			this.hpsArray[i]	= new HarmonicProductSpectrum();
			this.psArray[i].addListener(this.hpsArray[i]);
			println("	this.hpsArray[i] = " + this.hpsArray[i]);
		} // for - hps

		// Creates an array of SpectralPeaks and adds them to the HarmonicProductSpectrums
		// (this will aid in octave correction):
		this.spArray	= new SpectralPeaks[this.numInputs];
		println("this.spArray = " + this.spArray);
		for(int i = 0; i < this.numInputs; i++)
		{
			this.spArray[i]	= new SpectralPeaks(this.ac, 2);
			this.hpsArray[i].addListener(this.spArray[i]);
			println("	this.spArray[i] = " + this.spArray[i]);
		} // for - spectral peaks

		/*
		 * 1/2/2016: shouldn't need this section anymore
		// Creates an array of FrequencyEMMs and adds them to the PSs
		// (using my version of the Frequency class - an inner class further
		// down - to allow access to amplitude):
		this.frequencyArray = new FrequencyEMM[this.numInputs];
		for (int i = 0; i < this.frequencyArray.length; i++) {
			this.frequencyArray[i] = new FrequencyEMM(44100);
			while (this.frequencyArray[i] == null) {
			}
			this.psArray[i].addListener(frequencyArray[i]);
		} // for
		 */
		// Adds the SFSs (and everything connected to them) to the AudioContext:
		for (int i = 0; i < this.numInputs; i++) {
			this.ac.out.addDependent(sfsArray[i]);
		} // for - addDependent

		/*
		 * // trying to mute the output: mute = new Gain(this.ac, 1, 0);
		 * mute.addInput(this.ac.out); ac.out.addInput(mute);
		 */

		// Starts the AudioContext (and everything connected to it):
		this.ac.start();

		// Pitches with amplitudes below this number will be ignored by
		// adjustedFreq:
		this.sensitivity = (float) 0.2;
		// 1/2/2016: this was 10, but I think that I finally connected the filters correctly and brought it down!

		// Initializes the arrays that will hold the pitches and amplitudes:
		this.fundamentalArray = new float[this.numInputs];
		this.adjustedFundArray = new float[this.numInputs];
		this.amplitudeArray	= new float[this.numInputs];

		// Gets the ball rolling on analysis:
		this.setFund();
	} // initInput(UGen[])

	/**
	 * Subtracts 4 from the numInputs variable because I added 4 to account for
	 * the fact that the two interfaces together skip lines 5-8.l
	 *
	 * @return int number of input channels.
	 */
	int getNumInputs() {
		return this.numInputs;
	} // getNumInputs

	/**
	 * Fills the fundamentalArray and adjustedFundArray with the current pitches
	 * of each input line:
	 */
	void setFund() {
		// catching a NullPointer because I'm not sure why it happens and fear a
		// crash during a concert.
		try {
			float	highestAmpFreq;
			float	secondHighestAmpFreq;
			float	highestAmp;
			float	secondHighestAmp;

				// get the SpectralPeaks features:
				float[][][]	spfeaturesArray	= new float[this.numInputs][][];
				for(int i = 0; i < this.numInputs; i++)
				{
					spfeaturesArray[i]	= this.spArray[i].getFeatures();
					// First frequency is quieter but higher than the second:
//					println("spfeaturesArray[i][0][1] = " + spfeaturesArray[i][0][1] + "; spfeaturesArray[i][1][1] = " + spfeaturesArray[i][1][1]);
					if(spfeaturesArray[i][0][1] < spfeaturesArray[i][1][1])
					{
						println("Hmm, spfeaturesArray[i][0][1] < spfeaturesArray[i][1][1]... ");
						if(spfeaturesArray[i][0][0] > spfeaturesArray[i][1][1])

						{
							println("Warning! spfeaturesArray[i][0][1] < spfeaturesArray[i][1][1] && spfeaturesArray[i][0][0] > spfeaturesArray[i][1][1]");
						} // if
					} // outer if
			/*	
				for(int n = 0; n < spfeaturesArray.length; n++)
				{
					println("inputNum = " + i);
					for(int j = 0; j < spfeaturesArray[n].length; j++)
					{
						println("	j = " + j);
						for(int k = 0; k < spfeaturesArray[n][j].length; k++)
						{
							println("	spfeaturesArray[" + n + "][" + j + "][" + k + "] = " + spfeaturesArray[n][j][k]);
						}
					}
				}
*/
					
				// determine which is the highest and which is the secondHighest:
				// (TODO: I'm not going to check to make sure that higher freq and higher amplitude go together - not yet.)
				//((TODO: could also do something clever w/highest and secondhighest being 1 and 0...))
				if(spfeaturesArray[i][0][1] > spfeaturesArray[i][1][1])
				{
					highestAmpFreq			= spfeaturesArray[i][0][0];
					highestAmp				= spfeaturesArray[i][0][1];
					secondHighestAmpFreq	= spfeaturesArray[i][1][0];
					secondHighestAmp		= spfeaturesArray[i][1][1];
				} else {
					highestAmpFreq			= spfeaturesArray[i][1][0];
					highestAmp				= spfeaturesArray[i][1][1];
					secondHighestAmpFreq	= spfeaturesArray[i][0][0];
					secondHighestAmp		= spfeaturesArray[i][0][1];
				} // if

				int	freqError	= 5;
				// pick the secondHighest...
				//"if the second peak amplitude below initially chosen pitch is approximately 1/2 of the chosen pitch..."
				if(secondHighestAmpFreq < (highestAmpFreq * 0.5) + freqError &&
						secondHighestAmpFreq > (highestAmpFreq * 0.5) - freqError && 
						//"...AND the ratio of amplitudes is above a threshold (e.g., 0.2 for 5 harmonics)..."
						highestAmp / secondHighestAmp > 0.2 )
				{
					println("Made it to the correction zone.");
				} // if
				// for now, just use the highest.
				println("	highestAmpFreq = " + highestAmpFreq + "; i = " + i);
				this.fundamentalArray[i]	= highestAmpFreq;
				highestAmp	= (float) (highestAmp * Math.pow(10, 8));
				if(highestAmp > sensitivity)
				{
					// TODO: also set pitch here
					this.amplitudeArray[i]		= highestAmp;
	//				println("this.amplitudeArray[" + i + "] = " + this.amplitudeArray[i]);
				} else {
					println("highestAmp = " + highestAmp + " (wasn't loud enough to set amplitude)");
				}
			} // for
		} catch (NullPointerException npe) {
		} // catch
	} // setFund

	/**
	 * @return pitch (in Hertz) of the Input, adjusted to ignore frequencies
	 *         below a certain volume.
	 */
	float getAdjustedFund(int inputNum) {
		inputNumErrorCheck(inputNum, "getAdjustedFund(int)");

		setFund();
		return this.adjustedFundArray[inputNum - 1];
	} // getAdjustedFund()

	/**
	 * @return pitch (in Hertz) of the Input, adjusted to ignore frequencies
	 *         below a certain volume.
	 */
	float getAdjustedFundAsHz(int inputNum) {
		inputNumErrorCheck(inputNum, "getAdjustedFundAsHz(int)");

		return getAdjustedFund(inputNum);
		/*
		 * setFund(); return this.adjustedFundArray[inputNum - 1];
		 */
	} // getAdjustedFundAsHz()

	/**
	 * @return pitch of the Input as a MIDI note, adjusted to ignore sounds
	 *         below a certain volume.
	 */
	float getAdjustedFundAsMidiNote(int inputNum) {
		inputNumErrorCheck(inputNum, "getAdjustedFundAsMidiNote(int)");

		setFund();
		return Pitch.ftom(this.adjustedFundArray[inputNum - 1]);
	} // getAdjustedFundAsMidiNote()

	/**
	 * @return pitch (in Hertz) of the Input.
	 */
	float getFund(int inputNum) {
		inputNumErrorCheck(inputNum, "getFund(int)");

		setFund();
		return this.fundamentalArray[inputNum - 1];
	} // getFund()

	/**
	 * @return pitch (in Hertz) of the Input.
	 */
	float getFundAsHz(int inputNum) {
		inputNumErrorCheck(inputNum, "getFundAsHz(int)");

		return getFund(inputNum);
		/*
		 * setFund(); return this.fundamentalArray[inputNum - 1];
		 */
	} // getFundAsHz()

	/**
	 * @return pitch of the Input as a MIDI note.
	 */
	float getFundAsMidiNote(int inputNum) {
		inputNumErrorCheck(inputNum, "getFundAsMidiNote(int)");

		setFund();
		return Pitch.ftom(this.fundamentalArray[inputNum - 1]);
	} // getFundAsMidiNote()

	/**
	 * @return pitch (in Hertz) of the first Input, adjusted to ignore
	 *         frequencies below a certain volume.
	 */
	float getAdjustedFund() {
		return getAdjustedFund(1);
	} // getAdjustedFund()

	/**
	 * @return pitch (in Hertz) of the first Input, adjusted to ignore
	 *         frequencies below a certain volume.
	 */
	float getAdjustedFundAsHz() {
		return getAdjustedFundAsHz(1);
	} // getAdjustedFundAsHz()

	/**
	 * @return pitch (in Hertz) of the first Input, adjusted to ignore
	 *         frequencies below a certain volume.
	 */
	float getAdjustedFundAsMidiNote() {
		return getAdjustedFundAsMidiNote(1);
	} // getAdjustedFundAsMidiNote()

	/**
	 * @return pitch (in Hertz) of the first Input.
	 */
	float getFund() {
		return getFund(1);
	} // getFund()

	/**
	 * @return pitch (in Hertz) of the first Input.
	 */
	float getFundAsHz() {
		return getFundAsHz(1);
	} // getFundAsHz()

	/**
	 * @return pitch of the first Input as a MIDI note.
	 */
	float getFundAsMidiNote() {
		return getFundAsMidiNote(1);
	} // getFundAsMidiNote()

	/**
	 * Calculates the average frequency of multiple input lines.
	 *
	 * @param inputsToAverage
	 *            an int[] with the numbers of each of the lines whose frequency
	 *            is to be averaged.
	 *
	 * @return float The average pitch of the inputs whose numbers are given in
	 *         the int[] param.
	 */
	float getAverageFund(int[] inputsToAverage) {
		if (inputsToAverage == null) {
			throw new IllegalArgumentException("Input_Jack.getAverageFund: int[] parameter is null.");
		} // error checking
		if (inputsToAverage.length < 1) {
			throw new IllegalArgumentException("Input_Jack.getAverageFund: int[] parameter's length is "
					+ inputsToAverage.length + "; must be at least 1.");
		} // error checking

		float result = 0;

		// adds the freqencies of the specified inputs:
		for (int i = 0; i < inputsToAverage.length; i++) {
			result += this.getAdjustedFund(inputsToAverage[i]);
		} // for

		// divides to find the average:
		return result / inputsToAverage.length;
	} // getAverageFund(int[])

	/**
	 * Calculates the average frequency of multiple consecutive input lines,
	 * numbered from "firstInput" to "lastInput".
	 *
	 * @param firstInput
	 *            the number of the first input whose frequency is to be
	 *            averaged.
	 * @param lastInput
	 *            the number of the last input whose frequency is to be
	 *            averaged.
	 *
	 * @return float The average pitch of the inputs from "firstInput" to
	 *         "lastInput".
	 */
	float getAverageFund(int firstInput, int lastInput) {
		inputNumErrorCheck(firstInput, "getAverageFund(int, int) - first int");
		inputNumErrorCheck(lastInput, "getAverageFund(int, int) - second int");
		if (!(lastInput > firstInput)) {
			throw new IllegalArgumentException("InputClassJack.getAverageFund():  lastInput param " + lastInput
					+ " is not greater than firstInput param " + firstInput);
		} // error checking

		int curInput = firstInput;

		// creates an array and fills it with the ints denoting the inputs from
		// firstInput to lastInput:
		int[] inputsToAverage = new int[lastInput - firstInput + 1];
		for (int i = 0; i < inputsToAverage.length; i++) {
			inputsToAverage[i] = curInput;
			curInput++;
		} // for

		// calculates the average by calling the other getAverageFund on the
		// inputsToAverage array:
		return getAverageFund(inputsToAverage);
	} // getAverageFund

	/**
	 * Calculates the average frequency of multiple input lines.
	 *
	 * @param inputsToAverage
	 *            an int[] with the numbers of each of the lines whose amplitude
	 *            is to be averaged.
	 *
	 * @return float The average amplitude of the inputs whose numbers are given
	 *         in the int[] param.
	 */
	float getAverageAmp(int[] inputsToAverage) {
		if (inputsToAverage == null) {
			throw new IllegalArgumentException("Input_Jack.getAverageAmp: int[] parameter is null.");
		} // error checking
		if (inputsToAverage.length < 1) {
			throw new IllegalArgumentException("Input_Jack.getAverageAmp: int[] parameter's length is "
					+ inputsToAverage.length + "; must be at least 1.");
		} // error checking

		float result = 0;

		for (int i : inputsToAverage) {
			result += this.getAmplitude(i);
		} // for

		return result / inputsToAverage.length;
	} // getAverageAmp

	/**
	 * Calculates the average amplitude of multiple consecutive input lines,
	 * numbered from "firstInput" to "lastInput".
	 *
	 * @param firstInput
	 *            the number of the first input whose amplitude is to be
	 *            averaged.
	 * @param lastInput
	 *            the number of the last input whose amplitude is to be
	 *            averaged.
	 *
	 * @return float The average pitch of the inputs from "firstInput" to
	 *         "lastInput".
	 */
	float getAverageAmp(int firstInput, int lastInput) {
		inputNumErrorCheck(firstInput, "getAverageFund(int, int) - first int");
		inputNumErrorCheck(lastInput, "getAverageFund(int, int) - second int");
		if (!(lastInput > firstInput)) {
			throw new IllegalArgumentException("InputClassJack.getAverageFund():  lastInput param " + lastInput
					+ " is not greater than firstInput param " + firstInput);
		} // error checking

		int curInput = firstInput;

		int[] inputsToAverage = new int[lastInput - firstInput + 1];
		for (int i = 0; i < inputsToAverage.length; i++) {
			inputsToAverage[i] = curInput;
			curInput++;
		} // for

		return getAverageAmp(inputsToAverage);
	} // getAverageAmp

	/**
	 * Returns the amplitude of the given input line.
	 *
	 * @param inputNum
	 *            an int specifying a particular input line.
	 *
	 * @return float amplitude of the particular input line.
	 */
	float getAmplitude(int inputNum) {
		inputNumErrorCheck(inputNum, "getAmplitude(int)");

		return this.frequencyArray[inputNum - 1].getAmplitude();
	} // getAmplitude

	/**
	 * Applies a 1:8 compressor for amp's over 400 and returns the resulting
	 * amplitude.
	 *
	 * @return float amplitude of the first input line.
	 */
	float getAmplitude() {
		if(this.amplitudeArray == null)
		{
			this.amplitudeArray	= new float[this.numInputs];
		}
		
		float amp = this.amplitudeArray[0];

		// if(amp > 400) { amp = amp + ((amp - 400) / 8); }

		return amp;
	}

	/**
	 * Error checker for ints sent to methods such as getFund, getAmplitude,
	 * etc.; rejects numbers that are greater than the number of inputs or less
	 * than 1.
	 *
	 * @param inputNum
	 *            an int that is to be checked for suitability as an input line
	 *            number.
	 * @param String
	 *            name of the method that called this method, used in the
	 *            exception message.
	 */
	private void inputNumErrorCheck(int inputNum, String method) {
		if (inputNum > this.numInputs) {
			IllegalArgumentException iae = new IllegalArgumentException(
					"InputClass_Jack.inputNumErrorCheck(int), from " + method + ": int parameter " + inputNum
					+ " is greater than " + this.numInputs + ", the number of inputs.");
			iae.printStackTrace();
		}
		if (inputNum < 1) {
			IllegalArgumentException iae = new IllegalArgumentException("InputClass_Jack.inputNumErrorCheck(int), from "
					+ method + ": int parameter is " + inputNum + "; must be 1 or greater.");
			iae.printStackTrace();
		}
	} // inputNumErrorCheck

	/**
	 * Setter for sensitivity float instance var.
	 *
	 * @param newSensitivity
	 *            float with the value to which sensitivity is to be set.
	 */
	void setSensitivity(float newSensitivity) {
		this.sensitivity = newSensitivity;
	}

	public FrequencyEMM[]	getFrequencyArray()
	{
		return this.frequencyArray;
	}

	/**
	 * HarmonicProductSpectrum sums the harmonic product spectrum of a buffer from a PowerSpectrum.
	 * @author codeandchords
	 *
	 */
	public class HarmonicProductSpectrum extends FeatureExtractor<float[], float[]> {

		float[]	hps1;
		float[]	hps2;
		float[]	hps3;
		float[]	hps4;

		/**
		 * Instantiates a new HarmonicProductSpectrum.
		 */
		public HarmonicProductSpectrum() {
		}

		/* (non-Javadoc)
		 * (based on other beads process methods)
		 */
		public void process(TimeStamp startTime, TimeStamp endTime, float[] powerSpectrum) {
			features = new float[powerSpectrum.length];

			this.hps1	= new float[powerSpectrum.length];
			this.hps2	= new float[powerSpectrum.length];
			this.hps3	= new float[powerSpectrum.length];
			this.hps4	= new float[powerSpectrum.length];

			//Calculating HPS:
			for (int i = 0; i < hps1.length; i++) {
				hps1[i] = powerSpectrum[i];
			} // for

			// 2:
			int i;
			for (i = 0; (i * 2) < hps2.length; i++) {
				hps2[i] = powerSpectrum[i * 2];
			} // for

			// 3:
			for (i = 0; (i * 3) < hps3.length; i++) {
				hps3[i] = powerSpectrum[i * 3];
			} // for

			// 4:
			for (i = 0; (i * 4) < hps4.length; i++) {
				hps4[i] = powerSpectrum[i * 4];
			} // for

			for(i = 0; i < features.length; i++)
			{
				features[i]	= hps1[i] * hps2[i] * hps3[i] * hps4[i];
			} // for - hps

			//update the listeners
			forward(startTime, endTime);
		} // process

	} // HPS class


	/*
	 * This file is part of Beads. See http://www.beadsproject.net for all
	 * information. CREDIT: This class uses portions of code taken from MEAP.
	 * See readme/CREDITS.txt.
	 *
	 * 07/02/2016 Emily Meuer
	 *
	 * Edited to allow access to amplitude, so classes using these Frequencies
	 * can cut out some background noise.
	 * 
	 * Further edited in attempts to get a more accurate pitch.
	 */

	// package net.beadsproject.beads.analysis.featureextractors;

	/*
	 * import beads.FeatureExtractor; import beads.TimeStamp;
	 */

	/**
	 * Frequency processes spectral data forwarded to it by a
	 * {@link PowerSpectrum} to determine the best estimate for the frequency of
	 * the current signal.
	 * 
	 * TODO: try autocorrelation for pitch detection if HPS doesn't come through.
	 * 
	 * TODO: make FrequencyEMM and hps (float[]) private.
	 *
	 * @beads.category analysis
	 */
	public class FrequencyEMM extends FeatureExtractor<Float, float[]> {

		/** The Constant FIRSTBAND. */
		static final int FIRSTBAND = 3;

		/** The ratio bin2hz. */
		private float bin2hz;

		private int bufferSize;

		// TODO: make this private, post-testing.
		public float[] hps; // Harmonic Product Spectrum summed up here

		private float sampleRate;

		private float amplitude;

		// TODO: make these private at some point?
		public int maxBin;
		public int secondMaxBinBelow;

		public 	HanningWindow	hanningWindow;
		public	Buffer			hanningWindowBuffer;

		//		private	SpectralPeaks	spectralPeaks;

		/**
		 * Instantiates a new Frequency.
		 *
		 * @param sampleRate
		 *            The sample rate of the audio context
		 */
		public FrequencyEMM(float sampleRate) {
			bufferSize = -1;
			this.sampleRate = sampleRate;
			this.features = null;

			//			this.spectralPeaks	= new SpectralPeaks(ac, 2);

			/*
			 * TODO : remove this window stuff:
			this.hanningWindow	= new HanningWindow();
			this.hanningWindowBuffer = this.hanningWindow.getDefault();
/*			for(int i = 0; i < this.hanningWindowBuffer.buf.length; i++)
			{
				println("this.hanningWindowBuffer.buf[" + i + "] = " + this.hanningWindowBuffer.buf[i] + "; this.hanningWindowBuffer.getValueIndex(" + i + ") = " + this.hanningWindowBuffer.getValueIndex(i));
			} // for
			 */
		} // constructor

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.olliebown.beads.core.PowerSpectrumListener#calculateFeatures(
		 * float[])
		 */
		public synchronized void process(TimeStamp startTime, TimeStamp endTime, float[] powerSpectrum) {
			if (bufferSize != powerSpectrum.length) {
				bufferSize = powerSpectrum.length;
				bin2hz = sampleRate / (2 * bufferSize);
			} // if


			// NB: powerSpectrum.length = 256; bin2hz = 86.13281
			if (bufferSize != powerSpectrum.length) {
				bufferSize = powerSpectrum.length;
				bin2hz = sampleRate / (2 * bufferSize);
			} // if 

			float	maxBinFreq;
			float 	secondMaxBinBelowFreq;
			float	maxBinAmp;
			float	secondMaxBinBelowAmp;

			this.hps = new float[powerSpectrum.length];

			float[]	hps1	= new float[powerSpectrum.length];
			float[]	hps2	= new float[powerSpectrum.length];
			float[]	hps3	= new float[powerSpectrum.length];
			float[]	hps4	= new float[powerSpectrum.length];

			this.features = null;
			// now pick best peak from linspec
			double pmax = -1;
			int maxbin = 0;

			//Calculating HPS a little differently:
			for (int i = 0; i < hps1.length; i++) {
				hps1[i] = powerSpectrum[i];
			} // for

			// 2:
			int i;
			for (i = 0; (i * 2) < hps2.length; i++) {
				hps2[i] = powerSpectrum[i * 2];
			} // for

			// 3:
			for (i = 0; (i * 3) < hps3.length; i++) {
				hps3[i] = powerSpectrum[i * 3];
			} // for

			// 4:
			for (i = 0; (i * 4) < hps4.length; i++) {
				hps4[i] = powerSpectrum[i * 4];
			} // for

			for(i = 0; i < hps.length; i++)
			{
				hps[i]	= hps1[i] * hps2[i] * hps3[i] * hps4[i];
			} // for - hps

			// old method:
			/*
			for (int i = 0; i < hps.length; i++) {
				hps[i] = powerSpectrum[i];
			} // for

			// 2:
			int i;
			for (i = 0; (i * 2) < hps.length; i++) {
				hps[i] = hps[i] * powerSpectrum[i * 2];
			} // for

			// 3:
			for (i = 0; (i * 3) < hps.length; i++) {
				hps[i] = hps[i] * powerSpectrum[i * 3];
			} // for

			// 4:
			for (i = 0; (i * 4) < hps.length; i++) {
				hps[i] = hps[i] * powerSpectrum[i * 4];
			} // for
			 */

			int	spacer	= width / this.hps.length;
			int	divideBy	= 8;

			for (i = FIRSTBAND; i < this.hps.length; i++) {
				// find max and secondMax peaks:
				if (this.hps[i] > this.hps[maxBin]) {
					maxBin = i;
				}
				/*
				if (this.hps[i] > this.hps[secondMaxBinBelow] && this.hps[i] < this.hps[maxBin] && i < maxBin) {
					secondMaxBinBelow = i;
					println("i < maxBin = " + (i<maxBin) + "; i = " + i + "; maxBin = " + maxBin + "; secondMaxBinBelow = " + secondMaxBinBelow);
				} // if
				 */
			} // for

			secondMaxBinBelow	= FIRSTBAND;
			for(i = FIRSTBAND; i < maxBin; i++)
			{
				if(this.hps[i] > this.hps[secondMaxBinBelow] && this.hps[i] < this.hps[maxBin])
				{
					secondMaxBinBelow = i;
				}
			} // for - secondMaxBinBelow


			// only select the second peak if it's about half of the first one:
			int error  = 5;
			//"if the second peak amplitude below initially chosen pitch is approximately 1/2 of the chosen pitch..."
			if (Math.abs(maxBin - secondMaxBinBelow) < (secondMaxBinBelow + error) && 
					Math.abs(maxBin - secondMaxBinBelow) > (secondMaxBinBelow - error) &&
					//"...AND the ratio of amplitudes is above a threshold (e.g., 0.2 for 5 harmonics)..."
					((hps[secondMaxBinBelow] * 100) / (hps[maxBin] * 100) > 20))
			{
				//"...THEN select the lower octave peak as the pitch for the current frame."
				println("got within margin of error correction");

				maxBin  = secondMaxBinBelow;
			} // if

			//			println("Input.FreqEMM.process: maxBin = " + maxBin + "; secondMaxBinBelow = " + secondMaxBinBelow);


			/*
			 * for (int band = FIRSTBAND; band < powerSpectrum.length; band++) {
			 * double pwr = powerSpectrum[band]; if (pwr > pmax) { pmax = pwr;
			 * maxbin = band; } // if } // for
			 */

			// I added the following line;
			// 10/5 edits (i.e., hps) may cause it to be a larger num than it was previously:
			maxBinAmp				= this.hps[maxBin] * 100;
			secondMaxBinBelowAmp	= this.hps[secondMaxBinBelow] * 100;
			/*
	// 12/06: Put the following into the cubicInterp(int, float[]) function:

			// cubic interpolation
			// (11/20: replaced "maxbin" with "maxBin" so that hps detection can have effect!)
			double yz = powerSpectrum[maxBin];
			double ym;
			if (maxBin <= 0) {
				ym = powerSpectrum[maxBin];
			} else {
				ym = powerSpectrum[maxBin - 1];
			} // else

			double yp;
			if (maxBin < powerSpectrum.length - 1) {
				yp = powerSpectrum[maxBin + 1];
			} else {
				yp = powerSpectrum[maxBin];
			} // else

			double k = (yp + ym) / 2 - yz;
			double x0 = (ym - yp) / (4 * k);
			features = (float) (bin2hz * (maxBin + x0));

			forward(startTime, endTime);
			 */
			this.features	= this.cubicInterp(maxBin, powerSpectrum);
			this.amplitude 	= maxBinAmp;

			secondMaxBinBelowFreq	= this.cubicInterp(secondMaxBinBelow, powerSpectrum);
			//			println("this.features = " + this.features);
			//			println("secondMaxBinBelowFreq = " + secondMaxBinBelowFreq);

			float[][]	spfeatures	= spArray[0].getFeatures();
			/*
			println("this.features = " + this.features + "; secondMaxBinBelowFreq = " + secondMaxBinBelowFreq);
			for(i = 0; i < spfeatures.length; i++)
			{
				for(int j = 0; j < spfeatures[i].length; j++)
				{
					print("spfeatures[" + i + "][" + j + "] = " + spfeatures[i][j] + "; ");

				} // for - j
			} // for - i
			 */
			// We can ignore the times that they are both the same (happens often for the lowest bin):
			//TODO: is this ^ true?
			if(secondMaxBinBelowFreq < this.features)
			{
				/*
				secondMaxBinBelowFreq	= this.cubicInterp(secondMaxBinBelow, powerSpectrum);
				println("secondMaxBinBelowFreq = " + secondMaxBinBelowFreq);
				 */

				//				println("  first if; features = " + this.features + "; secondMaxBinBelowFreq = " + secondMaxBinBelowFreq);
				int	freqError	= 5;

				//"if the second peak amplitude below initially chosen pitch is approximately 1/2 of the chosen pitch..."
				/*				if(this.features - secondMaxBinBelowFreq < (secondMaxBinBelowFreq + freqError) &&
						this.features - secondMaxBinBelowFreq > (secondMaxBinBelowFreq - freqError) &&
				 */
				if(secondMaxBinBelowFreq < (this.features * 0.5) + freqError &&
						secondMaxBinBelowFreq > (this.features * 0.5) - freqError &&
						//"...AND the ratio of amplitudes is above a threshold (e.g., 0.2 for 5 harmonics)..."
						((this.hps[secondMaxBinBelow] * 100) / (this.hps[maxBin] * 100) > 20))
				{
					//"...THEN select the lower octave peak as the pitch for the current frame."
					println("second...Freq selected! original features = " + this.features + "; secondMaxBinBelowFreq = " + secondMaxBinBelowFreq);
					this.features	= secondMaxBinBelowFreq;
					this.amplitude	= secondMaxBinBelowAmp;
				} // if - choose lower octave freq
			} // if - secondMaxBinBelow < maxBin
			else
			{
				//				println("not first if");
			}

			forward(startTime, endTime);
		} // process

		/**
		 * This method takes the cubic interpolation out of process(),
		 * so that it can be performed on both maxBin and secondMaxBinBelow
		 * to allow for HPS octave correction.
		 * 
		 * @param bin	Either maxBin or secondMaxBinBelow.
		 * @param powerSpectrum	The powerSpectrum array from process()
		 * @return	the frequency that corresponds to that bin in the powerSpectrum
		 */
		private float cubicInterp(int bin, float[] powerSpectrum)
		{
			// TODO: I don't want negative frequencies. Is this the method where I should deal with them?

			// all these y-numbers will be used to figure out the percentage that must be multiplied against the bin
			// to then multiply it by "bin2hz" to get the exact frequency:
			double yz = powerSpectrum[bin];
			double ym;
			if (bin <= 0) {
				ym = powerSpectrum[bin];
			} else {
				ym = powerSpectrum[bin - 1];
			} // else

			double yp;
			if (bin < powerSpectrum.length - 1) {
				yp = powerSpectrum[bin + 1];
			} else {
				yp = powerSpectrum[bin];
			} // else

			double k = (yp + ym) / 2 - yz;
			double x0 = (ym - yp) / (4 * k);

			//println("  k = " + k + "\nx0 = " + x0);

			// discovering the actual frequency:
			return (float) (bin2hz * (bin + x0));
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.olliebown.beads.core.FrameFeatureExtractor#getFeatureDescriptions
		 * ()
		 */
		public String[] getFeatureDescriptions() {
			return new String[] { "frequency" };
		}

		/**
		 * @return float amplitude of the fundamental frequency (in unknown
		 *         units).
		 */
		public float getAmplitude() {
			return this.amplitude;
		}
	} // FrequencyEMM
} // Input class
