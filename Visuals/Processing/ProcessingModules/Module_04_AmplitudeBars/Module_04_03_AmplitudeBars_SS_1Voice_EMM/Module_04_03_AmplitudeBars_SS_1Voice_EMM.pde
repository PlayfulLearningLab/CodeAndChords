/**
 * TO DO:
 * - add 2 bars
 * --- dark green
 * --- pink
 * - drumbox controlled by input 12 (16)
 * - background balls controlled by input 12 (16)
 */

// Calibrate:
int  volAdjust             = 20;  // divide amp by this.
int  volAdjust1            = 3;    // multiply
float speed                = 10;   // divide amp by this to get a ball move speed.
float amplify              = 0.05;    // multiply amp by this to slow the ball speed.
int  whichInputMovesBalls  = 12;  // this line moves the balls in the background.
int  drumBoxInput          = 12;
int  drumBoxThreshold      = 75;

int stackheight            = 15;  //easily adjusting number of stacks for bars

int colorDecay = 5; //adjusting constant for color decay of bars

Input myIns;   //this is for the new input class

// variables for balls:
//Input  in;  //Michaela has this for new class? Or from old?
LeadBall myLeadBall;

Ball[] myBall;
//how many balls across the screen
int balls = 30;

// divide by these:
/*
int b1VolAdjust = 100;
int b2VolAdjust = 100;
int t1VolAdjust = 100;
int t2VolAdjust = 100;

int b1PitAdjust = 10;
int b2PitAdjust = 10;
int t1PitAdjust = 10;
int t2PitAdjust = 10;
*/

int  allPitchAdjust = 10;


float  redVol;
float  orangeVol;
float  yellowVol;
float  lightGreenVol;
float  forestGreenVol;
float  cyanVol;
float  periwinkleVol;
float  indigoVol;
float  purpleVol;
float  fuchsiaVol;
float  pinkVol;

float  redPitch;
float  orangePitch;
float  yellowPitch;
float  lightGreenPitch;
float  forestGreenPitch;
float  cyanPitch;
float  periwinklePitch;
float  indigoPitch;
float  purplePitch;
float  fuchsiaPitch;
float  pinkPitch;


void setup()
{
  fullScreen();

  this.myIns = new Input();

  // setup for background balls:
  myLeadBall = new LeadBall();
  myBall = new Ball[balls];
  for (int i = 0; i < balls; i++) {
    myBall[i] = new Ball(i);
  }//for
}

void draw()
{
  try 
  {  
    background(0);

    // drawing the balls in the background:
    myLeadBall.move();
    //ARRAY OBJECTS STEP 4 (for loop)
    for (int i = 0; i < myBall.length; i++) {
      myBall[i].move();
    }//for


    int refy = round(3*height/4);
    //println("refy is "+refy);
    int refx = round(width/13);
    int space = round(height/100);  //for universally determing the space between stacks
    int w = refx-space;  //for universally determining the width of the bars
    //println("w is "+w);
    int h = round(height/30);  //for universally determining the height of the stacks
    //println("h is "+h);
    //int off = space/2;
    int crefx = width/2;
    int halfwmin = crefx - w/2;
    int halfwplus = crefx + w/2;

    /* rp = red and pink
     * of = orange and fuchsia
     * yp = yellow and purple
     * ggp = light green, forest green, and periwinkle
     */


      redVol  = myIns.getAmplitude() / volAdjust;
      orangeVol  = myIns.getAmplitude() / volAdjust;
      yellowVol  = myIns.getAmplitude() / volAdjust;
      lightGreenVol  = myIns.getAmplitude() / volAdjust;
      forestGreenVol  = myIns.getAmplitude() / volAdjust;
      cyanVol = myIns.getAmplitude() / volAdjust;
      periwinkleVol  = myIns.getAmplitude() / volAdjust;
      indigoVol = myIns.getAmplitude() / volAdjust;
      purpleVol  = myIns.getAmplitude() / volAdjust;
      fuchsiaVol  = myIns.getAmplitude() / volAdjust;
      pinkVol  = myIns.getAmplitude() / volAdjust;

      redPitch  = myIns.getAdjustedFund();
      orangePitch  = myIns.getAdjustedFund();
      yellowPitch  = myIns.getAdjustedFund();
      lightGreenPitch  = myIns.getAdjustedFund();
      forestGreenPitch  = myIns.getAdjustedFund();
      cyanPitch = myIns.getAdjustedFund();
      periwinklePitch  = myIns.getAdjustedFund();
      indigoPitch = myIns.getAdjustedFund();
      purplePitch  = myIns.getAdjustedFund();
      fuchsiaPitch  = myIns.getAdjustedFund();
      pinkPitch  = myIns.getAdjustedFund();

    //1 RED
    stroke(255);
    rectMode(CORNERS);
    //  int pitchb1 = round(rppit/b1PitAdjust);
    //  int volb1 = round(rpvol);
    int pitch1 = round(redPitch/allPitchAdjust);
    int vol1 = round(redVol);
    if (vol1>stackheight)
    {
      vol1=stackheight;
    }

    int j=0;                  //stack is from bottom to top
    int old1=pitch1/(vol1+1); //have to add 1 because sometimes volume is zero
    for (j=0; j<vol1; j++)
    {
      fill(255-colorDecay*old1, 0, 0); //224 36 71 (255 0 0)
      //rect(width-9*refx-w,refy-(space+h)*j, width-8*refx-w-off, (refy-h)-(space+h)*j); //makes boxes!
      rect(halfwmin-5*space-5*w, refy-(space+h)*j, halfwmin-5*space-4*w, (refy-h)-(space+h)*j);
      old1 = old1+pitch1/(vol1+1);
    }
    old1 = pitch1/(vol1+1);

    //2 ORANGE
    stroke(255);
    rectMode(CORNERS);
    //  int pitchb2 = round(ofpit/b2PitAdjust);
    //  int volb2 = round(ofvol);
    int pitch2 = round(orangePitch/allPitchAdjust);
    int vol2 = round(orangeVol);
    if (vol2>stackheight)
    {
      vol2=stackheight;
    }

    int old2 = pitch2/(vol2+1);
    for (j=0; j<vol2; j++)
    {
      fill(255-colorDecay*old2, 145-colorDecay*old2, 0); //255 90 8 (255 145 0)
      //rect(width-8*refx-w,refy-(space+h)*j,width-7*refx-w-off,(refy-h)-(space+h)*j);
      rect(halfwmin-4*space-4*w, refy-(space+h)*j, halfwmin-4*space-3*w, (refy-h)-(space+h)*j);
      old2 = old2 + pitch2/(vol2+1);
    }
    old2=pitch2/(vol2+1);

    //3 YELLOW
    stroke(255);
    rectMode(CORNERS);
    //  int pitcht1 = round(yppit/t1PitAdjust);
    //  int volt1 = round(ypvol);
    int pitch3 = round(yellowPitch/allPitchAdjust);
    int vol3 = round(yellowVol);
    if (vol3>stackheight)
    {
      vol3=stackheight;
    }

    int old3 = pitch3/(vol3+1);
    for (j=0; j<vol3; j++)
    {
      fill(255-colorDecay*old3, 234-colorDecay*old3, 44-colorDecay*old3); //255 234 44 good how it is probably
      //rect(width-7*refx-w,refy-(space+h)*j,width-6*refx-w-off,(refy-h)-(space+h)*j);
      rect(halfwmin-3*space-3*w, refy-(space+h)*j, halfwmin-3*space-2*w, (refy-h)-(space+h)*j);
      old3 = old3+pitch3/(vol3+1);
    }
    old3=pitch3/(vol3+1);

    //4 ELECTRIC GREEN
    stroke(255);
    rectMode(CORNERS);
        int pitch4 = round(lightGreenPitch/allPitchAdjust);
    int vol4 = round(lightGreenVol);
    if (vol4>stackheight)
    {
      vol4=stackheight;
    }

    int old4 = pitch4/(vol4+1);
    for (j=0; j<vol4; j++)
    {
      fill(255-colorDecay*old4, 234-colorDecay*old4, 44-colorDecay*old4); //255 234 44 good how it is probably
      //rect(width-7*refx-w,refy-(space+h)*j,width-6*refx-w-off,(refy-h)-(space+h)*j);
      rect(halfwmin-2*space-2*w, refy-(space+h)*j, halfwmin-2*space-w, (refy-h)-(space+h)*j);
      old4 = old4+pitch4/(vol4+1);
    }
    old4=pitch4/(vol4+1);

    //5 FOREST GREEN
    stroke(255);
    rectMode(CORNERS);
    //  int pitcht2 = round(ggppit/t2PitAdjust);
    //  int volt2 = round(ggpvol);
    int pitch5 = round(forestGreenPitch/allPitchAdjust);
    int vol5 = round(forestGreenVol);
    if (vol5>stackheight)
    {
      vol5=stackheight;
    }
    int old5 = pitch5/(vol5+1);
    for (j=0; j<vol5; j++)
    {
      fill(41-colorDecay*old5, 255-colorDecay*old5, 0); //162 211 2 (41 255 0)
      //rect(width-6*refx-w,refy-(space+h)*j,width-5*refx-w-off,(refy-h)-(space+h)*j);
      rect(halfwmin - space - w, refy-(space+h)*j, halfwmin - space, (refy-h)-(space+h)*j);
      old5=old5+pitch5/(vol5+1);
    }
    old5=pitch5/(vol5+1);

    //6 CYAN
    stroke(255);
    rectMode(CORNERS);
    // are these really necessary?  (Might be helpful for calibration.)
    int pitch6 = round(cyanPitch / allPitchAdjust);
    int vol6  = round(cyanVol);
    if (vol6>stackheight)
    {
      vol6=stackheight;
    }
    int old6  = pitch6 / (vol6 + 1);
    for (j=0; j<vol6; j++)
    {
      fill(0, 255-colorDecay*old6, 232-colorDecay*old6); //1 100 65 (0 255 232 was CYAN)
      //rect(width-5*refx-w,refy-(space+h)*j, width-4*refx-w-off,(refy-h)-(space+h)*j);
      rect(halfwmin, refy-(space+h)*j, halfwplus, (refy-h)-(space+h)*j);
      old6=old6+pitch6/(Math.max(pitch6+1, 1));
    }
    old6=pitch6/(vol6+1);

    //7 PERRIWINKLE
    stroke(255);
    rectMode(CORNERS);
    int pitch7 = round(periwinklePitch / allPitchAdjust);
    int vol7  = round(periwinkleVol);
    if (vol7>stackheight)
    {
      vol7=stackheight;
    }
    int old7  = pitch7 / (vol7 + 1);
    for (j=0; j<vol7; j++)
    {
      fill(0-colorDecay*old7, 129-colorDecay*old7, 255-colorDecay*old7); //149 176 250 (0 129 255)
      //rect(width-4*refx-w,refy-(space+h)*j,width-3*refx-w-off,(refy-h)-(space+h)*j);
      rect(halfwplus+space, refy-(space+h)*j, halfwplus+space+w, (refy-h)-(space+h)*j);
      old7=old7+pitch7/(vol7+1);
    }
    old7=pitch7/(vol7+1);

    //8 INDIGO
    stroke(255);
    rectMode(CORNERS);
    int pitch8 = round(indigoPitch / allPitchAdjust);
    int vol8  = round(indigoVol);
    if (vol8>stackheight)
    {
      vol8=stackheight;
    }
    int old8  = pitch8 / (vol8 + 1);
    for (j=0; j<vol8; j++)
    {
      fill(45-colorDecay*old8, 0, 255-colorDecay*old8); //122 73 155 (45 0 255 was indigo)
      //rect(width-3*refx-w,refy-(space+h)*j,width-2*refx-w-off,(refy-h)-(space+h)*j);
      rect(halfwplus+2*space+w, refy-(space+h)*j, halfwplus+2*space+2*w, (refy-h)-(space+h)*j);
      old8=old8+pitch8/(vol8+1);
    }
    old8=pitch8/(vol8+1);

    //9 PURPLE
    stroke(255);
    rectMode(CORNERS);
    int pitch9 = round(purplePitch / allPitchAdjust);
    int vol9  = round(purpleVol);
    if (vol9>stackheight)
    {
      vol9=stackheight;
    }
    int old9  = pitch9 / (vol9 + 1);
    for (j=0; j<vol9; j++)
    {
      fill(139-colorDecay*old9, 0, 255-colorDecay*old9); //247 3 222 (139 0 255 was purple)
      //rect(width-2*refx-w,refy-(space+h)*j,width-refx-w-off,(refy-h)-(space+h)*j);
      rect(halfwplus+3*space+2*w, refy-(space+h)*j, halfwplus+3*space+3*w, (refy-h)-(space+h)*j);
      old9=old9+pitch9/(vol9+1);
    }
    old9=pitch9/(vol9+1);

    //10 FUCHSIA
    stroke(255);
    rectMode(CORNERS);
    //  int pitchPink = round(pinkPitch / allPitchAdjust);
    //  int volPink  = round(pinkVol);
    int pitch10 = round(fuchsiaPitch / allPitchAdjust);
    int vol10  = round(fuchsiaVol);
    if (vol10>stackheight)
    {
      vol10=stackheight;
    }
    int old10  = pitch10 / (vol10 + 1);
    for (j=0; j<vol10; j++)
    {
      fill(255-(colorDecay/2)*old10, 0, 222-(colorDecay/2)*old10); //255 130 180 (255 0 222 was fuchsia)
      //rect(width-refx-w,refy-(space+h)*j,width-refx,(refy-h)-(space+h)*j);
      rect(halfwplus+4*space+3*w, refy-(space+h)*j, halfwplus+4*space+4*w, (refy-h)-(space+h)*j);
      old10=old10+pitch10/(vol10+1);
    }
    old10=pitch10/(vol10+1);
    
    //11 PINK
    stroke(255);
    rectMode(CORNERS);
    //  int pitchPink = round(pinkPitch / allPitchAdjust);
    //  int volPink  = round(pinkVol);
    int pitch11 = round(pinkPitch / allPitchAdjust);
    int vol11  = round(pinkVol);
    if (vol11>stackheight)
    {
      vol11=stackheight;
    }
    int old11  = pitch11 / (vol11 + 1);
    for (j=0; j<vol11; j++)
    {
      fill(255-(colorDecay/2)*old11, 0, 222-(colorDecay/2)*old11); //255 130 180 (255 0 222 was fuchsia)
      //rect(width-refx-w,refy-(space+h)*j,width-refx,(refy-h)-(space+h)*j);
      rect(halfwplus+5*space+4*w, refy-(space+h)*j, halfwplus+5*space+5*w, (refy-h)-(space+h)*j);
      old11=old11+pitch11/(vol11+1);
    }
    old11=pitch11/(vol11+1);
  }
  catch (NullPointerException npe) {
  }
  
      //DRUM BOX
    // will be hooked to makey makey, so that hitting the drum
    // lights the rectangle around the screen.
    if(myIns.getAmplitude(drumBoxInput) > drumBoxThreshold)
//    if (keyPressed)
    {
      stroke(255); 
      fill(255);
    } else
    {
      stroke(155);
      fill(0);
      //println("volb1 is "+volb1);
    }
    rectMode(CORNERS);
    rect(width/20, height*0.95, width-width/20, height*0.92);
    
} // draw()

// The following classes copied from DP_PitchAcrossScreenBall_SpeechChanges_EMM:
class LeadBall {
  float xLead;
  float yLead;
  LeadBall() {
    xLead = width;
    yLead = height*0.9;
  }
  void move () {
    xLead = xLead - speed*myIns.getAmplitude(whichInputMovesBalls) * amplify;      // amplify is a fraction
    //    xLead = xLead - myIns.getAdjustedFundAsHz(whichInputMovesBalls)/10;
    //    println("xLead = " + xLead + "; yLead = " + yLead);
    if (xLead < 0) {
      //      yLead = height*0.9-in.getAdjustedFundAsHz();
      yLead = (height * 0.9) - myIns.getAdjustedFundAsHz(whichInputMovesBalls);
      //      println("  set yLead to " + yLead);
      xLead = width;
    }//if x<0
    fill(200, 100, 200);
    ellipse(xLead, yLead, 10, 10);
  }  
  float getXpos() {
    return xLead;
  }
}

//makes a ball y=amp that will move across the screen
//once across screen, ball goes back to "start" and receives a new y=amp
class Ball {
  float x;
  float y;
  float speed;
  int ballNumber;
  int c;
  Ball(int inBallNumber) {
    ballNumber = inBallNumber;
    y = height*0.9;
    c = ballNumber*10;
    constrain(c, 0, 255);
  }
  void move() {
    fill(c, 100, 100);
    ellipse(x, y, 10, 10);
    if (ballNumber != 0) {
      x = (myLeadBall.getXpos() + ballNumber*(width)/balls) % (width);
      if ((x < 1)&&(x>-1)) {
        x = width;
        y = height*0.9-myIns.getAdjustedFundAsHz(whichInputMovesBalls);
        //y = in.getAdjustedFundAsHz();
        // println("  y = " + y);
      } // if ((x < 1)&&(x>-1))
    } // if (ballNumber != 0)
  }//move
}//Ball