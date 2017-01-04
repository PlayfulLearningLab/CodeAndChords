/*
  12/05/2016
  Previously "FrequencyAttempts" in CantusProject-InProgress/OldExamples
  
  Pitch Experiments
  05/26/2015
 */
 
 import ddf.minim.*;
 import ddf.minim.analysis.*;
 import ddf.minim.ugens.*;
 
 Minim      minim;
 FFT        fft;
 Frequency  freq;
 AudioInput input;
 AudioPlayer player;
 
 void setup()
 {
   size(512, 200);
   
   this.minim  = new Minim(this);
  this.player = this.minim.loadFile("Zikr.mp3", 4096);
//  this.player.loop();
   this.input  = minim.getLineIn();
   this.fft    = new FFT(input.bufferSize(), input.sampleRate());
//   this.fft    = new FFT(player.bufferSize(), player.sampleRate());
 } // setup
 
 void draw()
 {
   this.fft.forward(this.input.mix);
 //  this.fft.forward(this.player.mix);
   this.fft.logAverages(11,12);
   
   float  loudestFreq = 0;
   float  loudestFreqAmp  = 0;    // amplitude of the loudestAvg average band
   int    loudestAvg    = 0;      // average band w/the highest amplitude
   
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
    
    //this.freq  = Frequency.ofHertz(this.fft.getAverageCenterFrequency(i));
    //println("centerFreq = " + this.fft.getAverageCenterFrequency(i) + "; Frequency, as midi note: " + freq.asMidiNote());
    //text("Frequency, as MIDI note: " + freq.asMidiNote(), 5, 30);
    
    //background(loudestAvg * 2, 0, 0);
    background(loudestFreq/5, 0, 0);
  } // for - i
  
  //println("loudestAvg = " + loudestAvg + "; getAvgCenterFreq() = " + this.fft.getAverageCenterFrequency(loudestAvg));// + "; amp = " + loudestFreqAmp);

  // this.fft.getAverageCenterFrequency(loudestAvg)) returns the center frequency
  // in the loudest average band (calculated above).
 } // draw