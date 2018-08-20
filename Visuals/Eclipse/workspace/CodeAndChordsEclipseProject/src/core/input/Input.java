package core.input;

//import core.input.RealTimeInput.DisposeHandler;
//import core.input.RealTimeInput.FrequencyEMM;
import net.beadsproject.beads.analysis.FeatureExtractor;
import net.beadsproject.beads.analysis.featureextractors.FFT;
import net.beadsproject.beads.analysis.featureextractors.PowerSpectrum;
import net.beadsproject.beads.analysis.segmenters.ShortFrameSegmenter;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.TimeStamp;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.Pitch;
import net.beadsproject.beads.ugens.Compressor;
import net.beadsproject.beads.ugens.Gain;
import processing.core.PApplet;

public abstract class Input implements MusicalInput {

	/**
	 * 08/30/2017
	 * Emily Meuer
	 * 
	 * Turned Input into abstract class,
	 * with children RealTimeInput, GeneratedInput (will use Melody/Instrument/Note),
	 * and RecordedInput.
	 */
	protected DisposeHandler disposeHandler;
	protected PApplet pa;
	protected AudioContext ac;

	protected float[] adjustedFundArray;
	protected Compressor compressor;
	protected	UGen[]                 uGenArray;
	// TODO: make private after testing
	protected	Gain[]		   gainArray;
	protected FFT[] fftArray;
	protected FrequencyEMM[] frequencyArray;
	protected float[] fundamentalArray;
	protected float[] amplitudeArray;
	protected int numInputs;
	protected int adjustedNumInputs;
	protected PowerSpectrum[] psArray;
	protected ShortFrameSegmenter[] sfsArray;
	protected boolean pause;
	protected	float	sensitivity;

	public Input() {
		super();
	}


	/**
	 *  As of 10/24/2016, does everything that was in the (int, AudioContext) constructor;
	 *  that is, initialize the Gain, add everything in the given UGen[] to it,
	 *  and analyze the frequencies w/SFS, PS, FFT, Frequency.
	 *
	 *  @param  uGenArray  a UGen[] whose members will be added to a gain, analyzed, and added to the AudioContext.
	 */
	protected void initInput(UGen[] uGenArray, int gainLevel)
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
					this.gainArray[i]	= new Gain(this.ac, gainLevel, gainLevel);
					// TODO: uncomment this line to hear what's going in the mics (e.g., through headphones):
					//				this.gainArray[i]	= new Gain(this.ac, 1, 0.5f);
				}

				this.gainArray[i].addInput(this.compressor);
				this.gainArray[i].addInput(this.uGenArray[i]);
				this.ac.out.addInput(this.gainArray[i]);
		} // gainArray


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
	 *  Fills the fundamentalArray and adjustedFundArray with the current pitches of each input line:
	 */
	public abstract void setFund();

	/**
	 *  @return  pitch (in Hertz) of the Input, adjusted to ignore frequencies below a certain volume.
	 */
	public float getAdjustedFund(int inputNum) {
		inputNumErrorCheck(inputNum, "getAdjustedFund(int)");

		setFund();
		return this.adjustedFundArray[inputNum];
	} // getAdjustedFund()

	/**
	 *  @return  pitch (in Hertz) of the Input, adjusted to ignore frequencies below a certain volume.
	 */
	public float getAdjustedFundAsHz(int inputNum) {
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
	public float getAdjustedFundAsMidiNote(int inputNum) {
		inputNumErrorCheck(inputNum, "getAdjustedFundAsMidiNote(int)");

		setFund();
		return Pitch.ftom(this.adjustedFundArray[inputNum]);
	} // getAdjustedFundAsMidiNote()

	/**
	 *  @return  pitch (in Hertz) of the Input.
	 */
	public float getFund(int inputNum) {
		inputNumErrorCheck(inputNum, "getFund(int)");

		setFund();
		return this.fundamentalArray[inputNum];
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
	public float getFundAsMidiNote(int inputNum) {
		inputNumErrorCheck(inputNum, "getFundAsMidiNote(int)");

		setFund();
		return Pitch.ftom(this.fundamentalArray[inputNum]);
	} // getFundAsMidiNote()

	/**
	 *  @return  pitch (in Hertz) of the first Input, adjusted to ignore frequencies below a certain volume.
	 */
	public float getAdjustedFund() {
		return getAdjustedFund(0);
	} // getAdjustedFund()

	/**
	 *  @return  pitch (in Hertz) of the first Input, adjusted to ignore frequencies below a certain volume.
	 */
	public float getAdjustedFundAsHz() {
		return getAdjustedFundAsHz(0);
	} // getAdjustedFundAsHz()

	/**
	 *  @return  pitch (in Hertz) of the first Input, adjusted to ignore frequencies below a certain volume.
	 */
	public float getAdjustedFundAsMidiNote() {
		return getAdjustedFundAsMidiNote(0);
	} // getAdjustedFundAsMidiNote()

	/**
	 *  @return  pitch (in Hertz) of the first Input.
	 */
	public float getFund() {
		return getFund(0);
	} // getFund()

	/**
	 *  @return  pitch (in Hertz) of the first Input.
	 */
	public float getFundAsHz() {
		return getFundAsHz(0);
	} // getFundAsHz()

	/**
	 *  @return  pitch of the first Input as a MIDI note.
	 */
	public float getFundAsMidiNote() {
		return getFundAsMidiNote(0);
	} // getFundAsMidiNote()

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
		return this.amplitudeArray[inputNum];
	} // getAmplitude

	/**
	 *  As of 6/13, the following is not true (a Compressor has been added to the chain instead):
	 *  Applies a 1:8 compressor for amp's over 400 and returns the resulting amplitude.
	 *
	 *  @return  float     amplitude of the first input line.
	 */
	public float getAmplitude() {
		//		float  amp  = this.frequencyArray[0].getAmplitude();
		//		return amp;

		this.setFund();
		return this.amplitudeArray[0];
	} // getAmplitude()


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
	 *  Error checker for ints sent to methods such as getFund, getAmplitude, etc.;
	 *  rejects numbers that are greater than the number of inputs or less than 1.
	 *
	 *  @param   inputNum  an int that is to be checked for suitability as an input line number.
	 *  @param   String    name of the method that called this method, used in the exception message.
	 */
	protected void inputNumErrorCheck(int inputNum, String method) {
		if (inputNum >= this.adjustedNumInputs) {
			IllegalArgumentException iae = new IllegalArgumentException("Input.inputNumErrorCheck(int), from " + method + ": int parameter " + inputNum + " is greater than " + this.adjustedNumInputs + ", the number of inputs.");
			iae.printStackTrace();
			throw iae;
		}
		if (inputNum < 0) {
			IllegalArgumentException iae = new IllegalArgumentException("Input.inputNumErrorCheck(int), from " + method + ": int parameter is " + inputNum + "; must be 1 or greater.");
			iae.printStackTrace();
			throw iae;
		}
	} // inputNumErrorCheck

	/**
	 * Pauses the Gain connected to ac.out.
	 * 
	 * @param pause boolean indicating whether to pause or un-pause the Gain
	 */
	public void pause(boolean pause) {	
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
	public void stop() {
		this.ac.stop();
	} // stop


	/*
	 * Emily's version of the Frequency class, allowing access to amplitude.
	 * 
	 * Original description:
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
		 * 
		 * This is where the harmonic product spectrum is calculated.
		 * 
		 * @param powerSpectrum	frequency-domain spectrum to be analyzed
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
			
			// This is where the hps squishing happens:
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

			// Pick the largest frequency from the hps spectrum:
			for (int band = FIRSTBAND; band < hps.length; band++) {
				double pwr = hps[band];
				if (pwr > pmax) {
					pmax = pwr;
					maxbin = band;
				} // if
			} // for

			// I (Emily) added the following line;
			// 10/5 edits may cause it to be a larger num than it was previously:
//			amplitude  = (float)pmax;
			amplitude	= powerSpectrum[maxbin];

			// cubic interpolation
			double yz = hps[maxbin];
			double ym;
			if(maxbin <= 0) {
				ym = hps[maxbin];
			} else {
				ym = hps[maxbin - 1];
			} // else

			double yp;
			if(maxbin < hps.length - 1) {
				yp  = hps[maxbin + 1];
			} else {
				yp  = hps[maxbin];
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

	public void setAmplitudeArray(float[] newVal) {
		if(newVal == null)
		{
			throw new IllegalArgumentException("Input.setAmplitudeArray: float[] parameter is null.");
		}

		if(newVal.length <= this.amplitudeArray.length)
		{
			for(int i = 0; i < newVal.length; i++)
			{
				this.amplitudeArray[i]	= newVal[i] * 10000;
				// TODO
			} // for
		} // if
	} // setAmplitudeArray
	
	public AudioContext getAudioContext()
	{
		return this.ac;
	}

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
} // Input