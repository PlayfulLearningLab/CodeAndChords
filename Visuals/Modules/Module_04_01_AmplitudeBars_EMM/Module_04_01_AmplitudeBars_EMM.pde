/*
 *  11/03/2016
 *  Emily Meuer
 *
 *  Getting it to work for a demo -- uses Minim.
 *
 * Amanda Tenhoff
 * 1 October 2016
 * Module 04: Amplitude Bars
 * This module takes a single input and creates stacked bars whose stackheight
 * depends on amplitude.
 * 
 * This module does not deal with pitch at all.
 * This module does not use colorDecay.
 * This module uses the old colors.
 * 
 * Notes:
 * - needs to be more sensitive
 * - (e.g. very clearly be at zero when volume is 0)
 * - needs more range sensitivity
 * Input_Class line 174 -> println("amp is "+amp);
 * To get amplitude as a midi value, I added something in
 * the Input_Class, getAmplitudeAsMidi.
 * I don't know if that actually works but it is something.
 */
  
Input testInput;
AudioInput in;

int VolAdjust = 10;
//int PitAdjust = 10;

//int colorDecay = 2;

int stackheight = 15;

void setup()
{
  testInput  = new Input();  //calls the Input_Class
}

void draw()
{
  background(0);
  
  //float note = testInput.getFreqAsHz();
  float volume = testInput.getAmplitude();
  //int amplitude = round(volume);
  //println("amplitude is "+amplitude);
  int AmpMidi = round(testInput.getAmplitudeAsMidi());
  println("AmpMidi is "+AmpMidi);
  
  /* Add some SENSITIVITY here */
  
  //int ampmidi = round(testInput.getAdjustedFreqAsMidiNote());
  //println("ampmidi is "+ampmidi);

   //int pitch = round(note/PitAdjust);
   int i = round((AmpMidi*VolAdjust)/stackheight);

   println("i is "+i);
  if (i>stackheight)
  {
    i=stackheight;
  }
  
  int refy = round(3*height/4);
  int refx = round(width/11);
  int space = round(height/100);  //for universally determing the space between stacks
  int w = refx-space;  //for universally determining the width of the bars
  int h = round(height/30);  //for universally determining the height of the stacks
  int crefx = width/2;
  int halfwmin = crefx - w/2;
  int halfwplus = crefx + w/2;
    
  //RED
  stroke(255);
  rectMode(CORNERS);
  int oldj=0; //for reversing color stack (darkest on bottom;
              //stack is from bottom to top)
  //int oldv=i/(i+1); //have to add 1 because sometimes i is zero
  for(oldj=0; oldj<i; oldj++)
  {
    fill(255, 0, 0); //255 0 0
    rect(halfwmin-4*space-4*w,refy-(space+h)*oldj, halfwmin-4*space-3*w, (refy-h)-(space+h)*oldj); //makes boxes!
    //oldv = oldv+i/(i+1);
  }
  //oldv=i/(i+1);
  
  //ORANGE
  stroke(255);
  rectMode(CORNERS);
  for(oldj=0; oldj<i; oldj++)
  {
    fill(255, 145, 0); //255 145 0
    rect(halfwmin-3*space-3*w,refy-(space+h)*oldj,halfwmin-3*space-2*w,(refy-h)-(space+h)*oldj);
    //oldv = oldv + i/(i+1);
  }
  //oldv=i/(i+1);
  
  //YELLOW
  stroke(255);
  rectMode(CORNERS);
  for(oldj=0; oldj<i; oldj++)
  {
    fill(250, 255, 0); //250 255 0
    rect(halfwmin-2*space-2*w,refy-(space+h)*oldj,halfwmin-2*space-w,(refy-h)-(space+h)*oldj);
    //oldv = oldv+i/(i+1);
  }
  //oldv=i/(i+1);
  
  //LIGHT GREEN
  stroke(255);
  rectMode(CORNERS);
  for(oldj=0; oldj<i; oldj++)
  {
    fill(41, 255, 0); //41 255 0
    rect(halfwmin - space - w,refy-(space+h)*oldj,halfwmin - space,(refy-h)-(space+h)*oldj);
    //oldv=oldv+i/(i+1);
  }
  //oldv=i/(i+1);
  
  //FOREST GREEN
  stroke(255);
  rectMode(CORNERS);
  for(oldj=0; oldj<i; oldj++)
  {
    fill(0, 255, 232); //0 255 232
    rect(halfwmin,refy-(space+h)*oldj, halfwplus,(refy-h)-(space+h)*oldj);
    //oldv=oldv+i/(i+1);
  }
  //oldv=i/(i+1);
  
  //PERIWINKlE
  stroke(255);
  rectMode(CORNERS);
  for(oldj=0; oldj<i; oldj++)
  {
    fill(0, 129, 255); //0 129 255
    rect(halfwplus+space,refy-(space+h)*oldj,halfwplus+space+w,(refy-h)-(space+h)*oldj);
    //oldv=oldv+i/(i+1);
  }
  //oldv=i/(i+1);
  
  //VIOLET
  stroke(255);
  rectMode(CORNERS);
  for(oldj=0; oldj<i; oldj++)
  {
    fill(45, 0, 255); //45 0 255
    rect(halfwplus+2*space+w,refy-(space+h)*oldj,halfwplus+2*space+2*w,(refy-h)-(space+h)*oldj);
    //oldv=oldv+i/(i+1);
  }
  //oldv=i/(i+1);
  
  //FUCHSIA
  stroke(255);
  rectMode(CORNERS);
  for(oldj=0; oldj<i; oldj++)
  {
    fill(139, 0, 255); //139 0 255
    rect(halfwplus+3*space+2*w,refy-(space+h)*oldj,halfwplus+3*space+3*w,(refy-h)-(space+h)*oldj);
    //oldv=oldv+i/(i+1);
  }
  //oldv=i/(i+1);
  
  //PINK
  stroke(255);
  rectMode(CORNERS);
  for(oldj=0; oldj<i; oldj++)
  {
    fill(255, 0, 222); //255 0 222
    rect(halfwplus+4*space+3*w,refy-(space+h)*oldj,halfwplus+4*space+4*w,(refy-h)-(space+h)*oldj);
    //oldv=oldv+i/(i+1);
  }
  //oldv=i/(i+1);
  
}