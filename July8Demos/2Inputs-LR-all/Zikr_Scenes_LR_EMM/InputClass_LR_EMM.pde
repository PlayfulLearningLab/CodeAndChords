import ddf.minim.*;
import ddf.minim.analysis.*;
import ddf.minim.effects.*;
import ddf.minim.ugens.*;

Minim  minimForAll;

void settings()
{
  fullScreen();
  // minim must be initialized outside of Input in order to pass the correct value of "this" to its constructor.
  minimForAll = new Minim(this);
}

class Input
{
  /*
    Emily Meuer
   
   TODO: clean up constructors -- add an interface or abstract superclass?
   
   Updated 06/27/2016:
   Replaced AudioSource with an AudioBuffer, allowing right and left channels of the 
   same input to be treated as separate entities and possibly cheat the mono-input system.
   
   Created 06/07/2016:
   Class to detect the pitch (that is, fundamental frequency)
   of an AudioPlayer or AudioInput; works best with monophonic input.
   */

  Frequency  adjustedFund;      // disregards values below a certain amplitude level
  Frequency  adjustedPrevFund;
  float      amplitude;
  FFT        fft;
  float      findFund;
  Frequency  fundamental;
  float      fundamentalAmp;
  AudioInput input;
  Minim      minim;
  Frequency  prevFund;
  AudioPlayer player;
  float      sensitivity;  // amplitude below which adjustedFreq will not be reset
  AudioSource source;
  AudioBuffer  buffer;

  /**
   * Constructor for creating an Input object from an audio file.
   *
   * @param  filename  String specifying the audio file.
   */
  Input(String filename)
  {
    if (filename == null) {
      throw new IllegalArgumentException("InputClassPitch.constructor(String): String parameter " + filename + " is null.");
    }

    this.findFund     = 120;

    try {
      this.player  = minimForAll.loadFile(filename);
    } 
    catch (NullPointerException npe) {
      throw new IllegalArgumentException("Input.constructor(String): there was an error loading the file \"" + filename + "\" with the Minim " + minimForAll + 
        " (this minim initialized in settings()).");
    }
    this.fft          = new FFT(player.bufferSize(), player.sampleRate());
    this.player.loop(); 

    this.sensitivity  = 0.01;
    this.source = this.player;
    this.buffer = this.player.mix;
    this.setFund();
  } // constructor(String)

  /**
   * Constructor for creating an Input object from line in.
   */
  Input()
  {
    this.findFund     = 120;
    this.input        = minimForAll.getLineIn();     
    this.fft          = new FFT(input.bufferSize(), input.sampleRate());
    this.sensitivity  = 3;
    this.source = this.input;
    this.buffer       = this.input.mix;
    this.setFund();
  } // constructor()

  Input(boolean left, boolean right)
  {
    this.findFund     = 120;
    this.input        = minimForAll.getLineIn();     
    this.fft          = new FFT(input.bufferSize(), input.sampleRate());
    this.sensitivity  = 3;
    this.source       = this.input;

    if (left == right) {  // i.e., either (left && right) or (!left && !right)
      this.buffer = this.input.mix;
    } else if (left) {
      this.buffer = this.input.left;
    } else if (right) {
      this.buffer = this.input.right;
    }

    this.setFund();
  } // Input(boolean, boolean)

  /**
   * The following comments from InputClassFreq; this no longer uses averages:
   *
   * Performs a foward transform on the AudioInput instance var,
   * uses logAverages to group near frequencies and calculate
   * their average amplitude, determines which is the dominant frequency
   * (by which has the highest amplitude), and sets the Frequency instance
   * var to be equal to this dominant/loudest frequency.
   * Also sets the amplitude instance var.
   *
   * (To produce correct results, this function needs to be called repeatedly,
   * and should only be called from the constructor or in one of the getFreq functions.)
   */
  void setFund()
  { 
//    this.source = this.input;
    this.fft.forward(this.buffer);

    for (int i = 4; i < fft.specSize(); i++)
    {
      if (this.fft.getBand(i) > this.fft.getFreq(findFund))
      {     
        if (this.fft.indexToFreq(i) - findFund < (findFund / 2))
        {
          this.findFund = this.fft.indexToFreq(i);
        } // if
      } // if
    } // for

    // set the prevFund to be equal to the current fundamental,
    // unless the current fundamental has not been set,
    // in which case, set it to be the newly-found fundamental.
    if (this.fundamental == null) {  
      this.prevFund = Frequency.ofHertz(findFund);
    } else {  
      this.prevFund = this.fundamental;
    }

    // take the same precautions with setting adjustPrevFreq as when setting prevFreq
    if (this.adjustedFund == null) {  
      this.adjustedPrevFund = Frequency.ofHertz(findFund);
    } else {  
      this.adjustedPrevFund = this.adjustedFund;
    }

    this.fundamental = Frequency.ofHertz(findFund);
    // if adjustedFreq has not yet been initialized, set it to the
    // loudest frequency regardless of its amlitude and sensitivity.
    if (this.adjustedFund == null)
    {
      this.adjustedFund  = Frequency.ofHertz(findFund);
    }
    // in all other cases, only reset adjustedFreq to frequencies higher than the sensitivity:
    if (this.fft.getFreq(findFund) > this.sensitivity)    
    {  
      this.adjustedFund = Frequency.ofHertz(findFund);
    }

    this.amplitude  = this.fft.getFreq(findFund);
  } // setFund

  /**
   * Calls setFund(), then returns the freq Frequency instance var.
   */
  Frequency getFund()
  {
    this.setFund();
    return this.fundamental;
  } // getFund()

  /**
   * Calls setFund(), then returns the fundamental Frequency instance var in hertz.
   */
  float getFundAsHz()  
  {
    this.setFund();
    return this.fundamental.asHz();
  }

  /**
   * Calls setFund(), then returns the midi note value of the fundamental Frequency instance var.
   */
  float getFundAsMidiNote()  
  {
    this.setFund();
    return this.fundamental.asMidiNote();
  }

  /**
   * Calls setFund(), then returns the adjustedFund Frequency instance var.
   */
  Frequency getAdjustedFund()
  {
    this.setFund();
    return this.adjustedFund;
  } // getFund()

  /**
   * Calls setFund(), then returns the adjustedFund Frequency instance var in hertz.
   */
  float getAdjustedFundAsHz()  
  {
    this.setFund();
    return this.adjustedFund.asHz();
  }

  /**
   * Calls setFund(), then returns the midi note value of the adjustedFund Frequency instance var.
   */
  float getAdjustedFundAsMidiNote()  
  {
    this.setFund();
    return this.adjustedFund.asMidiNote();
  }

  /**
   * Calls setFund() before returning the amplitude ("level()") of the AudioInput instance var.
   * If you want only the amplitude of the particular frequency in quesion,
   * access the float instance var "amplitude" directly.
   *
   * @return  amplitude of the AudioInput instance var.
   */
  float getAmplitude() {
    this.setFund();
    return this.buffer.level();
  }
  

  AudioBuffer getBuffer() {
    return this.buffer;
  } // getBuffer()

  void setSensitivity(float newSensitivity)
  {
    this.sensitivity = newSensitivity;
  }
} // class