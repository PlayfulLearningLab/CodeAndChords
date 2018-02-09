<<<<<<< HEAD
import ddf.minim.*;
import ddf.minim.analysis.*;
import ddf.minim.effects.*;
import ddf.minim.ugens.*;
//import java.nio.file.*;
//import java.io.FileInputStream;

Minim  minimForAll;

void settings()
{
  //size(1000, 1000);
  fullScreen();
  // minim must be initialized outside of Input in order to pass the correct value of "this" to its constructor.
  minimForAll = new Minim(this);
}


public class InputPitch
{
  /*
    Emily Meuer
   06/07/2016
   
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

  //  Future: take an int that specifies the channel of this input?
  // It will have to know from what line to get the audio, so probably yes.

  /**
   * Constructor for creating an InputPitch object from an audio file.
   *
   * @param  filename  String specifying the audio file.
   */
  InputPitch(String filename)
  {
    /*
    if (minim == null) {  
      throw new IllegalArgumentException("InputClassPitch.constructor(Minim): Minim " + minim + " is null.");
    }
    */

    this.findFund     = 120;
//    this.minim        = minimForAll;

    try {
      this.player  = minimForAll.loadFile(filename);
    } 
    catch (NullPointerException npe) {
      throw new IllegalArgumentException("InputPitch.constructor(String): there was an error loading the file \"" + filename + "\" with the Minim " + minim + ".");
    }
    this.fft          = new FFT(player.bufferSize(), player.sampleRate());
    this.player.loop(); 

    this.sensitivity  = 0.01;
    this.source = this.player;
    this.setFund();
  } // constructor(String)

  /**
   * Constructor for creating an InputPitch object from line in.
   */
  InputPitch()
  {
    /*
    if (minim == null) {  
      throw new IllegalArgumentException("InputClassPitch.constructor(Minim): Minim " + minim + " is null.");
    }
*/

    this.findFund     = 120;
//    this.minim        = minimForAll;
    this.input        = minimForAll.getLineIn();     
    this.fft          = new FFT(input.bufferSize(), input.sampleRate());
    this.sensitivity  = 3;
    this.source = this.input;
    this.setFund();
  } // constructor()

//--
  /**
   * Returns the absolute path of the given file by calling sketchPath on it.
   * This method is necessary for instantiating a Minim object in this class using "this".
   *
   * @param  fileName  String whose absolute path is to be found.
   */
  /*  String sketchPath(String fileName)
   {
   println("sketchPath in InputPitch was called");
   PApplet pApplet = new PApplet();
   //    return pApplet.sketchPath(fileName);
   
   Path path = Paths.get(fileName);
   println("sketchPath: path.toString() = " + path.toString());
   return path.toString();
   } // sketchPath(String)
   
  /**
   * Creates an InputStream object from the file specified in the parameter.
   * This method is necessary for instantiating a Minim object in this class using "this".
   *
   * @param  fileName  String specifying a file to be used as input for an InputStream.
   */
  /*  InputStream createInput(String fileName)
   {
   PApplet pApplet = new PApplet();
   try {
   return new FileInputStream(fileName);
   } catch(Exception fnfe)  {  throw new IllegalArgumentException(fnfe.getMessage());  }
   //    return pApplet.createInput(fileName);
   } // createInput(String)
   */
// --


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
    this.fft.forward(this.source.mix);

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
    return this.source.mix.level();
  }

  void setSensitivity(float newSensitivity)
  {
    this.sensitivity = newSensitivity;
  }
=======
import ddf.minim.*;
import ddf.minim.analysis.*;
import ddf.minim.effects.*;
import ddf.minim.ugens.*;
//import java.nio.file.*;
//import java.io.FileInputStream;

Minim  minimForAll;

void settings()
{
  //size(1000, 1000);
  fullScreen();
  // minim must be initialized outside of Input in order to pass the correct value of "this" to its constructor.
  minimForAll = new Minim(this);
}


public class InputPitch
{
  /*
    Emily Meuer
   06/07/2016
   
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

  //  Future: take an int that specifies the channel of this input?
  // It will have to know from what line to get the audio, so probably yes.

  /**
   * Constructor for creating an InputPitch object from an audio file.
   *
   * @param  filename  String specifying the audio file.
   */
  InputPitch(String filename)
  {
    /*
    if (minim == null) {  
      throw new IllegalArgumentException("InputClassPitch.constructor(Minim): Minim " + minim + " is null.");
    }
    */

    this.findFund     = 120;
//    this.minim        = minimForAll;

    try {
      this.player  = minimForAll.loadFile(filename);
    } 
    catch (NullPointerException npe) {
      throw new IllegalArgumentException("InputPitch.constructor(String): there was an error loading the file \"" + filename + "\" with the Minim " + minim + ".");
    }
    this.fft          = new FFT(player.bufferSize(), player.sampleRate());
    this.player.loop(); 

    this.sensitivity  = 0.01;
    this.source = this.player;
    this.setFund();
  } // constructor(String)

  /**
   * Constructor for creating an InputPitch object from line in.
   */
  InputPitch()
  {
    /*
    if (minim == null) {  
      throw new IllegalArgumentException("InputClassPitch.constructor(Minim): Minim " + minim + " is null.");
    }
*/

    this.findFund     = 120;
//    this.minim        = minimForAll;
    this.input        = minimForAll.getLineIn();     
    this.fft          = new FFT(input.bufferSize(), input.sampleRate());
    this.sensitivity  = 3;
    this.source = this.input;
    this.setFund();
  } // constructor()

//--
  /**
   * Returns the absolute path of the given file by calling sketchPath on it.
   * This method is necessary for instantiating a Minim object in this class using "this".
   *
   * @param  fileName  String whose absolute path is to be found.
   */
  /*  String sketchPath(String fileName)
   {
   println("sketchPath in InputPitch was called");
   PApplet pApplet = new PApplet();
   //    return pApplet.sketchPath(fileName);
   
   Path path = Paths.get(fileName);
   println("sketchPath: path.toString() = " + path.toString());
   return path.toString();
   } // sketchPath(String)
   
  /**
   * Creates an InputStream object from the file specified in the parameter.
   * This method is necessary for instantiating a Minim object in this class using "this".
   *
   * @param  fileName  String specifying a file to be used as input for an InputStream.
   */
  /*  InputStream createInput(String fileName)
   {
   PApplet pApplet = new PApplet();
   try {
   return new FileInputStream(fileName);
   } catch(Exception fnfe)  {  throw new IllegalArgumentException(fnfe.getMessage());  }
   //    return pApplet.createInput(fileName);
   } // createInput(String)
   */
// --


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
    this.fft.forward(this.source.mix);

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
    return this.source.mix.level();
  }

  void setSensitivity(float newSensitivity)
  {
    this.sensitivity = newSensitivity;
  }
>>>>>>> 346fdda528fc720bc3ef683871dafc344f6c9010
} // class