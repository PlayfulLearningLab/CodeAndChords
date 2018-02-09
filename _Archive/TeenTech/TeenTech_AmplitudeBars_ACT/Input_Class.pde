<<<<<<< HEAD
import ddf.minim.*;
import ddf.minim.analysis.*;
import ddf.minim.effects.*;
import ddf.minim.ugens.*;

class Input
{
  /*
    Emily Meuer
   05/28/2016
   
   Wrapper class to make pitch of a particular input easily accessible.
   */

// adjusted Freq doesn't work :(
  Frequency  adjustedFreq;  // freq which disregards values below a certain amplitude level
  Frequency  adjustedPrevFreq;
  float      amplitude;
  float      ampAsMidi;
  Minim      minim;
  FFT        fft;
  Frequency  freq;
  Frequency  prevFreq;
  AudioInput input;
  float      sensitivity;  // amplitude below which adjustedFreq will not be reset

  //  Future: take an int that specifies the channel of this input?
  // It will have to know from what line to get the audio, so probably yes.
  Input()
  {
    this.minim  = new Minim(this);
    this.input  = minim.getLineIn();
    this.fft    = new FFT(input.bufferSize(), input.sampleRate());
    this.sensitivity  = 5;
    this.setFreq();
  } // constructor

  /**
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
  void setFreq()
  { 
    this.fft.forward(this.input.mix);

    /*
    // each average should hopefully be about one half step,
     // since there are 11 averages and each is split into 12 parts.
     // (Could calculate smaller averages to get a closer frequency match, e.g. "this.fft.logAverages(11,48);"
     this.fft.logAverages(11,12);
     */

    float  loudestFreq = 0;
    float  loudestFreqAmp  = 0;    // amplitude of the loudestAvg average band
    int    loudestAvg    = 0;      // average band w/the highest amplitude

    /*
   for(int i = 0; i < this.fft.avgSize(); i++)
     {
     float lowFreq = this.fft.getAverageCenterFrequency(i) - (this.fft.getAverageBandWidth(i) / 2);
     float hiFreq  = this.fft.getAverageCenterFrequency(i) + (this.fft.getAverageBandWidth(i) / 2);
     float avgAmp = this.fft.calcAvg(lowFreq, hiFreq);
     
     if(avgAmp > loudestFreqAmp)  
     {  
     loudestAvg  = i;
     loudestFreqAmp  = avgAmp;
     loudestFreq = this.fft.getAverageCenterFrequency(i);
     } // if
     } // for
     */

    for (int i = 0; i < this.fft.specSize(); i++)
    {
      if (this.fft.getBand(i) > loudestFreqAmp)
      {
        loudestFreq = this.fft.indexToFreq(i);
        loudestFreqAmp = this.fft.getFreq(loudestFreq);
      } // if
    } // for

    // set the prevFreq to be equal to the current frequency,
    // unless the current frequency has not been set,
    // in which case, set it to be the newly-found loudestFreq.
    if(this.freq == null)  {  this.prevFreq = Frequency.ofHertz((float)loudestFreq);  }
    else                   {  this.prevFreq = this.freq;                              }
    
    // take the same precautions with setting adjustPrevFreq as when setting prevFreq
    if(this.adjustedFreq == null)  {  this.adjustedPrevFreq = Frequency.ofHertz((float)loudestFreq);  }
    else                           {  this.adjustedPrevFreq = this.adjustedFreq;  }
    
    this.freq = Frequency.ofHertz((float)loudestFreq);
    // if adjustedFreq has not yet been initialized, set it to the
    // loudest frequency regardless of its amlitude and sensitivity.
    if(this.adjustedFreq == null)
    {
      this.adjustedFreq  = Frequency.ofHertz((float)loudestFreq);
    }
    // in all other cases, only reset adjustedFreq to frequencies higher than the sensitivity:
    if(loudestFreqAmp > this.sensitivity)    
    {  
      this.adjustedFreq = Frequency.ofHertz((float)loudestFreq);  
    }
    
    this.amplitude  = loudestFreqAmp;
    //this.ampAsMidi  = this.fft.getFreq(getAdjustedFreqAsMidiNote);
  } // setFreq

  /**
   * Calls setFreq(), then returns the freq Frequency instance var.
   */
  Frequency getFreq()
  {
    this.setFreq();
    return this.freq;
  } // getFreq()

  /**
   * Calls setFreq(), then returns the freq Frequency instance var in hertz.
   */
  float getFreqAsHz()  
  {
    this.setFreq();
    println("freq is "+freq.asHz());
    return this.freq.asHz();
  }

  /**
   * Calls setFreq(), then returns the midi note value of the freq Frequency instance var.
   */
  float getFreqAsMidiNote()  
  {
    this.setFreq();
    return this.freq.asMidiNote();
  }
  
  /**
   * Calls setFreq(), then returns the adjustedFreq Frequency instance var.
   */
  Frequency getAdjustedFreq()
  {
    this.setFreq();
    return this.adjustedFreq;
  } // getFreq()

  /**
   * Calls setFreq(), then returns the adjustedFreq Frequency instance var in hertz.
   */
  float getAdjustedFreqAsHz()  
  {
    this.setFreq();
    return this.adjustedFreq.asHz();
  }

  /**
   * Calls setFreq(), then returns the midi note value of the adjustedFreq Frequency instance var.
   */
  float getAdjustedFreqAsMidiNote()  
  {
    this.setFreq();
    return this.adjustedFreq.asMidiNote();
  }

  /**
   * Calls setFreq(), then returns the value of the float amplitude instance var.
   */
  float getAmplitude() {
    this.setFreq();
    //println("amp is "+amplitude);
    return this.amplitude;
  }
  
  float getAmplitudeAsMidi()
  {
    int freqMidi = round(getAdjustedFreqAsMidiNote());
    ampAsMidi = this.fft.getFreq(freqMidi);
    //println("ampAsMidi is "+ampAsMidi);
    return this.ampAsMidi;
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

class Input
{
  /*
    Emily Meuer
   05/28/2016
   
   Wrapper class to make pitch of a particular input easily accessible.
   */

// adjusted Freq doesn't work :(
  Frequency  adjustedFreq;  // freq which disregards values below a certain amplitude level
  Frequency  adjustedPrevFreq;
  float      amplitude;
  float      ampAsMidi;
  Minim      minim;
  FFT        fft;
  Frequency  freq;
  Frequency  prevFreq;
  AudioInput input;
  float      sensitivity;  // amplitude below which adjustedFreq will not be reset

  //  Future: take an int that specifies the channel of this input?
  // It will have to know from what line to get the audio, so probably yes.
  Input()
  {
    this.minim  = new Minim(this);
    this.input  = minim.getLineIn();
    this.fft    = new FFT(input.bufferSize(), input.sampleRate());
    this.sensitivity  = 5;
    this.setFreq();
  } // constructor

  /**
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
  void setFreq()
  { 
    this.fft.forward(this.input.mix);

    /*
    // each average should hopefully be about one half step,
     // since there are 11 averages and each is split into 12 parts.
     // (Could calculate smaller averages to get a closer frequency match, e.g. "this.fft.logAverages(11,48);"
     this.fft.logAverages(11,12);
     */

    float  loudestFreq = 0;
    float  loudestFreqAmp  = 0;    // amplitude of the loudestAvg average band
    int    loudestAvg    = 0;      // average band w/the highest amplitude

    /*
   for(int i = 0; i < this.fft.avgSize(); i++)
     {
     float lowFreq = this.fft.getAverageCenterFrequency(i) - (this.fft.getAverageBandWidth(i) / 2);
     float hiFreq  = this.fft.getAverageCenterFrequency(i) + (this.fft.getAverageBandWidth(i) / 2);
     float avgAmp = this.fft.calcAvg(lowFreq, hiFreq);
     
     if(avgAmp > loudestFreqAmp)  
     {  
     loudestAvg  = i;
     loudestFreqAmp  = avgAmp;
     loudestFreq = this.fft.getAverageCenterFrequency(i);
     } // if
     } // for
     */

    for (int i = 0; i < this.fft.specSize(); i++)
    {
      if (this.fft.getBand(i) > loudestFreqAmp)
      {
        loudestFreq = this.fft.indexToFreq(i);
        loudestFreqAmp = this.fft.getFreq(loudestFreq);
      } // if
    } // for

    // set the prevFreq to be equal to the current frequency,
    // unless the current frequency has not been set,
    // in which case, set it to be the newly-found loudestFreq.
    if(this.freq == null)  {  this.prevFreq = Frequency.ofHertz((float)loudestFreq);  }
    else                   {  this.prevFreq = this.freq;                              }
    
    // take the same precautions with setting adjustPrevFreq as when setting prevFreq
    if(this.adjustedFreq == null)  {  this.adjustedPrevFreq = Frequency.ofHertz((float)loudestFreq);  }
    else                           {  this.adjustedPrevFreq = this.adjustedFreq;  }
    
    this.freq = Frequency.ofHertz((float)loudestFreq);
    // if adjustedFreq has not yet been initialized, set it to the
    // loudest frequency regardless of its amlitude and sensitivity.
    if(this.adjustedFreq == null)
    {
      this.adjustedFreq  = Frequency.ofHertz((float)loudestFreq);
    }
    // in all other cases, only reset adjustedFreq to frequencies higher than the sensitivity:
    if(loudestFreqAmp > this.sensitivity)    
    {  
      this.adjustedFreq = Frequency.ofHertz((float)loudestFreq);  
    }
    
    this.amplitude  = loudestFreqAmp;
    //this.ampAsMidi  = this.fft.getFreq(getAdjustedFreqAsMidiNote);
  } // setFreq

  /**
   * Calls setFreq(), then returns the freq Frequency instance var.
   */
  Frequency getFreq()
  {
    this.setFreq();
    return this.freq;
  } // getFreq()

  /**
   * Calls setFreq(), then returns the freq Frequency instance var in hertz.
   */
  float getFreqAsHz()  
  {
    this.setFreq();
    println("freq is "+freq.asHz());
    return this.freq.asHz();
  }

  /**
   * Calls setFreq(), then returns the midi note value of the freq Frequency instance var.
   */
  float getFreqAsMidiNote()  
  {
    this.setFreq();
    return this.freq.asMidiNote();
  }
  
  /**
   * Calls setFreq(), then returns the adjustedFreq Frequency instance var.
   */
  Frequency getAdjustedFreq()
  {
    this.setFreq();
    return this.adjustedFreq;
  } // getFreq()

  /**
   * Calls setFreq(), then returns the adjustedFreq Frequency instance var in hertz.
   */
  float getAdjustedFreqAsHz()  
  {
    this.setFreq();
    return this.adjustedFreq.asHz();
  }

  /**
   * Calls setFreq(), then returns the midi note value of the adjustedFreq Frequency instance var.
   */
  float getAdjustedFreqAsMidiNote()  
  {
    this.setFreq();
    return this.adjustedFreq.asMidiNote();
  }

  /**
   * Calls setFreq(), then returns the value of the float amplitude instance var.
   */
  float getAmplitude() {
    this.setFreq();
    //println("amp is "+amplitude);
    return this.amplitude;
  }
  
  float getAmplitudeAsMidi()
  {
    int freqMidi = round(getAdjustedFreqAsMidiNote());
    ampAsMidi = this.fft.getFreq(freqMidi);
    //println("ampAsMidi is "+ampAsMidi);
    return this.ampAsMidi;
  }
  
  void setSensitivity(float newSensitivity)
  {
    this.sensitivity = newSensitivity;
  }
>>>>>>> 346fdda528fc720bc3ef683871dafc344f6c9010
} // class