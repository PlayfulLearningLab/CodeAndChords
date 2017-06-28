import javax.sound.sampled.*;  // allows us to access Mixer!

import org.tritonus.share.sampled.FloatSampleBuffer;

/*
  10/06/2016
 Emily Meuer
 
 Trying to access multiple lines from our A/D interface,
 using the Java Mixer.
 
 Uses info from /CantusProject-InProgress/Experiments_EMM/HardwareTrial1_EMM/
 
 Ports: might have to open to let sound in -- prob. not.
 Keep them closed, and the user won't have sound coming out (which is what we want!).
 */

Mixer.Info[] mixerInfoArray;
//Mixer.Info   mixerInfo;

float  amplitude;

// From HardwareTrial1_EMM.InputClass_EMM:
Mixer.Info[]  mixerInfo;
Mixer         mixer;

int               numBytesRead;
TargetDataLine    line;
byte[]            data;
AudioFormat       audioFormat;
FloatSampleBuffer  floatSampBuf;
float[]            buffer;


void setup()
{
  size(500, 300);
  //  mixerInfo  = getMixerInfo();

  printMixerInfo();
  mixerInfo  = AudioSystem.getMixerInfo();
  mixer  = AudioSystem.getMixer(mixerInfo[3]);
  Line.Info[] tli1 = mixer.getTargetLineInfo();
  println();
  for(int i = 0; i < tli1.length; i++)
  {
    println("tli1[" + i + "]:  " + tli1[i]);
  } // for

  floatSampBuf = new FloatSampleBuffer();

  // try avoids problem of line not being initialized.
  try {
    if (tli1.length < 1) {
      throw new IllegalArgumentException("HardwareTrial1.setup(): tli1 " + tli1 + " has length of " + tli1.length);
    } else {
      line = (TargetDataLine)mixer.getLine(tli1[0]);
    } // else
    // Assume that the TargetDataLine, line, has already
    // been obtained and opened.
    numBytesRead = 0;
    data = new byte[line.getBufferSize() / 5];

    //  making this fft is the correct idea, but should use the float[]:
    //   this.fft = new FFT(data.length, 44100);

    audioFormat = line.getFormat();

    int channelCount = floatSampBuf.getChannelCount();
    println("channelCount: " + channelCount);
    // might want to mixDownChannels() to get it all to 1?
  } // try
  catch(LineUnavailableException lnae) {
    throw new IllegalArgumentException("HardwareTrial1.setup(): mixer1 - line not available.");
  } // catch
} // setup

void draw()
{
  background(200);
  stroke(225, 75, 255);
  fill(225, 75, 255);

  float[] buffer1;
  float[] buffer2;

  try {
    line.open(audioFormat);
//    println("line: " + line);
    line.start();
    numBytesRead =  line.read(data, 0, data.length);
    floatSampBuf.initFromByteArray(data, 0, numBytesRead, audioFormat);
    buffer1 = floatSampBuf.getChannel(0);
    
    line.stop();
    
    for (int j = 0; j < buffer1.length - 1; j++)
    {
      line(j, 100+buffer1[j]*40, j+1, 100+buffer1[j+1]*40);
//      line(j, 300+buffer2[j]*40, j+1, 300+buffer2[j+1]*40);
//      println("[" + j + "] buffer1: " + buffer1[j] + "; buffer2: " + buffer2[j]);
    } // for
  } // try
  catch(LineUnavailableException lue) {
  }
} // draw()

// this is all currently happening in setup()
void getLineFromMixer(int mixerNum)
{
  println("getLineFromMixer(" + mixerNum + ")");
  this.mixerInfo  = AudioSystem.getMixerInfo();
  mixer  = AudioSystem.getMixer(mixerInfo[mixerNum]);
  Line.Info[] tli = mixer.getTargetLineInfo();
  println("sourceLineInfo.length: " + tli.length);


  floatSampBuf = new FloatSampleBuffer();

  // try avoids problem of line not being initialized.
  try {
    if (tli.length < 1) {
      throw new IllegalArgumentException("HardwareTrial1.setup(): tli1 " + tli + " has length of " + tli.length);
    } else {
      line = (TargetDataLine)mixer.getLine(tli[0]);
    } // else
    // Assume that the TargetDataLine, line, has already
    // been obtained and opened.
    numBytesRead = 0;

    // This size will be an issue when creating the FFT/performing the Fourier Transform
    data = new byte[line.getBufferSize() / 5];

    audioFormat = line.getFormat();

    int channelCount = floatSampBuf.getChannelCount();
    println("channelCount: " + channelCount);
    // might want to mixDownChannels() to get it all to 1?
  } // try
  catch(LineUnavailableException lnae) {
    throw new IllegalArgumentException("HardwareTrial1.setup(): mixer1 - line not available.");
  } // catch
} // getLineFromMixer(int)

void fillBuffer()
{
  println("fillBuffer()");
  try {
    this.line.open(audioFormat);
    this.line.start();
    this.numBytesRead =  this.line.read(data, 0, data.length);
    this.floatSampBuf.initFromByteArray(data, 0, this.numBytesRead, this.audioFormat);
    this.buffer = floatSampBuf.getChannel(0);

    this.amplitude = this.line.getLevel();

    this.line.stop();
    this.line.close();

    for (int j = 0; j < this.buffer.length - 1; j++)
    {
      line(j, 100+this.buffer[j]*40, j+1, 100+this.buffer[j+1]*40);
      //      println("[" + j + "] buffer1: " + buffer1[j] + "; buffer2: " + buffer2[j]);
    } // for
  }  // try
  catch(LineUnavailableException lue) {
    throw new IllegalArgumentException("InputClass_EMM.fillBuffer: caught LineUnavailableException.");
  } // catch()
} // fillBuffer

void printMixerInfo()
{
  Mixer.Info[] mixerInfo  = AudioSystem.getMixerInfo();

  for (int i = 0; i < mixerInfo.length; i++)
  {
    println(i + " = " + mixerInfo[i].getName());
  }
} // printMixerInfo