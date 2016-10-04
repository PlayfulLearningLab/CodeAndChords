/**
  * TODO: Clean up variable names (associate w/voice part, singer, or color?)
  *
  * 07/07
  * Added "drum box" - rectangle that lights on keyPress, to be hooked to Makey-Makey.
  *
  * Emily Meuer
  * 07/07/2016
  * Updates to allow for more than 4 inputs.
  *
  * Amanda Tenhoff
  *7/3/2016
  * adjusting for newest input class!
  * not sure how to work with the balls
  * are we able to combine the inputs for the bars and balls?
  * (ie. just one Input variable?)
  *
  * Amanda Tenhoff
  * 7/1/2016
  * adjusting colors and creating universal calibration constants
  * fixed: color names and associated variable names
  * added: easy-to-find calibration constants
  * pushed 7/3
  *
  * Emily Meuer
  * 6/25/2016
  * Adds balls from DP_PitchAcrossScreenBall_SpeedChanges_EMM to DP_FreqInputs_ACT.
  *
  * Comments from DP_FreqInputs_ACT:
  * 6/12/2016
  * Amanda Tenhoff
  * This particular file makes an equalizer which
  * - changes hue depending on frequency
  * - changes height depending on amplitude
  * - responds to multiple inputs
  * - adjusts for any screen size
 */
  
// Calibrate:
int  volAdjust             = 20;  // divide amp by this.
float speed                = 10;   // divide amp by this to get a ball move speed.
float amplify              = 1000;    // multiplied against amplitude to get a ball move speed.
int  whichInputMovesBalls  = 2;  // this line moves the balls in the background.

Input myIns;   //this is for the new input class

// variables for balls:
//Input  in;  //Michaela has this for new class? Or from old?
LeadBall myLeadBall;

Ball[] myBall;
//how many balls across the screen
int balls = 30;


int b1VolAdjust = 100;
int b2VolAdjust = 100;
int t1VolAdjust = 100;
int t2VolAdjust = 100;

int b1PitAdjust = 10;
int b2PitAdjust = 10;
int t1PitAdjust = 10;
int t2PitAdjust = 10;

int  allPitchAdjust = 10;

int colorDecay = 7; //adjusting constant for color decay of bars

  float  redVol;
  float  orangeVol;
  float  yellowVol;
  float  lightGreenVol;
  float  forestGreenVol;
  float  periwinkleVol;
  float  purpleVol;
  float  fuchsiaVol;
  float  pinkVol;
  
  float  redPitch;
  float  orangePitch;
  float  yellowPitch;
  float  lightGreenPitch;
  float  forestGreenPitch;
  float  periwinklePitch;
  float  purplePitch;
  float  fuchsiaPitch;
  float  pinkPitch;

void setup()
{
  fullScreen();
  
  // Current janky fix: parameter must be 4 > actually desired num of inputs.
  this.myIns = new Input(14);

  // setup for background balls:
  myLeadBall = new LeadBall();
  myBall = new Ball[balls];
  for(int i = 0; i < balls; i++) {
    myBall[i] = new Ball(i);
  }//for
}

void draw()
{
  background(0);
  
  //DRUM BOX
  // will be hooked to makey makey, so that hitting the drum
  // lights the rectangle around the screen.
  if(keyPressed)
  {
    stroke(255);    
  }
  else
  {
    stroke(155,155,155);
    //println("volb1 is "+volb1);
  }
  fill(0);
  rectMode(CORNERS);
  for(int pix = 7; pix>0; pix--)
  {
    rect(width/20-pix,height*0.95+pix,width-width/20+pix,height-height*0.95-pix);
  }
  
  // drawing the balls in the background:
  myLeadBall.move();
//ARRAY OBJECTS STEP 4 (for loop)
  for(int i = 0; i < myBall.length; i++) {
    myBall[i].move();
  }//for


  int refy = round(3*height/4);
  //println("refy is "+refy);
  int refx = round(width/11);
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
  
  float rpvol = myIns.getAmplitude(1) / volAdjust;  //'volume' of bass1, used for columns RED and PINK
 // float rpvol = b1VolAdjust*myIns.getAmplitude(1) / 200;  //'volume' of bass1, used for columns RED and PINK
  float rppit = myIns.getAdjustedFundAsHz(1);  //'pitch' of bass1, used for columns RED and PINK
  
  float ofvol = myIns.getAmplitude(2) / volAdjust;
 // float ofvol = b2VolAdjust*myIns.getAmplitude(2) / 200;
  float ofpit = myIns.getAdjustedFundAsHz(2);
  
  float ypvol = myIns.getAmplitude(3) / volAdjust;
//  float ypvol = t1VolAdjust*myIns.getAmplitude(3) / 200;
  float yppit = myIns.getAdjustedFundAsHz(3);
  
  float ggpvol = myIns.getAmplitude(4) / volAdjust;
//  float ggpvol = t2VolAdjust*myIns.getAmplitude(4) / 200;
  float ggppit = myIns.getAdjustedFundAsHz(4);
  
  
  float  redVol  = myIns.getAmplitude(1) / volAdjust;
  float  orangeVol  = myIns.getAmplitude(2) / volAdjust;
  float  yellowVol  = myIns.getAmplitude(3) / volAdjust;
  float  lightGreenVol  = myIns.getAmplitude(4) / volAdjust;
  float  forestGreenVol  = myIns.getAmplitude(9) / volAdjust;
  float  periwinkleVol  = myIns.getAmplitude(10) / volAdjust;
  float  purpleVol  = myIns.getAmplitude(11) / volAdjust;
  float  fuchsiaVol  = myIns.getAmplitude(12) / volAdjust;
  float  pinkVol  = myIns.getAmplitude(13) / volAdjust;
  
  float  redPitch  = myIns.getAdjustedFund(1);
  float  orangePitch  = myIns.getAdjustedFund(2);
  float  yellowPitch  = myIns.getAdjustedFund(3);
  float  lightGreenPitch  = myIns.getAdjustedFund(4);
  float  forestGreenPitch  = myIns.getAdjustedFund(9);
  float  periwinklePitch  = myIns.getAdjustedFund(10);
  float  purplePitch  = myIns.getAdjustedFund(11);
  float  fuchsiaPitch  = myIns.getAdjustedFund(12);
  float  pinkPitch  = myIns.getAdjustedFund(13);
  
  //realistically there will be ten of these
  //and I'm thinking about naming them after
  //the singers once we have their standing order
  //and then there will be one for the drum
  
  //RED
  stroke(255);
  rectMode(CORNERS);
//  int pitchb1 = round(rppit/b1PitAdjust);
//  int volb1 = round(rpvol);
  int pitchb1 = round(redPitch/b1PitAdjust);
  int volb1 = round(redVol);

  int j=0;                  //stack is from bottom to top
  int oldb1=pitchb1/(volb1+1); //have to add 1 because sometimes volume is zero
  for(j=0; j<volb1; j++)
  {
    fill(224-colorDecay*oldb1, 36-colorDecay*oldb1, 71-colorDecay*oldb1); //224 36 71 (255 0 0)
    //rect(width-9*refx-w,refy-(space+h)*j, width-8*refx-w-off, (refy-h)-(space+h)*j); //makes boxes!
    rect(halfwmin-4*space-4*w,refy-(space+h)*j, halfwmin-4*space-3*w, (refy-h)-(space+h)*j);
    oldb1 = oldb1+pitchb1/(volb1+1);
  }
  oldb1 = pitchb1/(volb1+1);
  
  //ORANGE
  stroke(255);
  rectMode(CORNERS);
//  int pitchb2 = round(ofpit/b2PitAdjust);
//  int volb2 = round(ofvol);
  int pitchb2 = round(orangePitch/b2PitAdjust);
  int volb2 = round(orangeVol);

  int oldb2 = pitchb2/(volb2+1);
  for(j=0; j<volb2; j++)
  {
    fill(255-colorDecay*oldb2, 90-colorDecay*oldb2, 8); //255 90 8 (255 145 0)
    //rect(width-8*refx-w,refy-(space+h)*j,width-7*refx-w-off,(refy-h)-(space+h)*j);
    rect(halfwmin-3*space-3*w,refy-(space+h)*j,halfwmin-3*space-2*w,(refy-h)-(space+h)*j);
    oldb2 = oldb2 + pitchb2/(volb2+1);
  }
  oldb2=pitchb2/(volb2+1);
  
  //YELLOW
  stroke(255);
  rectMode(CORNERS);
//  int pitcht1 = round(yppit/t1PitAdjust);
//  int volt1 = round(ypvol);
  int pitcht1 = round(yellowPitch/t1PitAdjust);
  int volt1 = round(yellowVol);

  int oldt1 = pitcht1/(volt1+1);
  for(j=0; j<volt1; j++)
  {
    fill(255-colorDecay*oldt1, 234-colorDecay*oldt1, 44-colorDecay*oldt1); //255 234 44 good how it is probably
    //rect(width-7*refx-w,refy-(space+h)*j,width-6*refx-w-off,(refy-h)-(space+h)*j);
    rect(halfwmin-2*space-2*w,refy-(space+h)*j,halfwmin-2*space-w,(refy-h)-(space+h)*j);
    oldt1 = oldt1+pitcht1/(volt1+1);
  }
  oldt1=pitcht1/(volt1+1);
  
  //LIGHT GREEN
  stroke(255);
  rectMode(CORNERS);
//  int pitcht2 = round(ggppit/t2PitAdjust);
//  int volt2 = round(ggpvol);
  int pitcht2 = round(lightGreenPitch/t2PitAdjust);
  int volt2 = round(lightGreenVol);
  int oldt2 = pitcht2/(volt2+1);
  for(j=0; j<volt2; j++)
  {
    fill(162-colorDecay*oldt2, 211-colorDecay*oldt2, 2); //162 211 2 (41 255 0)
    //rect(width-6*refx-w,refy-(space+h)*j,width-5*refx-w-off,(refy-h)-(space+h)*j);
    rect(halfwmin - space - w,refy-(space+h)*j,halfwmin - space,(refy-h)-(space+h)*j);
    oldt2=oldt2+pitcht2/(volt2+1);
  }
  oldt2=pitcht2/(volt2+1);
  
  //FOREST GREEN
  stroke(255);
  rectMode(CORNERS);
  // are these really necessary?  (Might be helpful for calibration.)
  int pitchForestGreen = round(forestGreenPitch / allPitchAdjust);
  int volForestGreen  = round(forestGreenVol);
  int oldForestGreen  = pitchForestGreen / (volForestGreen + 1);
  for(j=0; j<volForestGreen; j++)
  {
    fill(1, 100-colorDecay*oldForestGreen, 65-colorDecay*oldForestGreen); //1 100 65 (0 255 232 was CYAN)
    //rect(width-5*refx-w,refy-(space+h)*j, width-4*refx-w-off,(refy-h)-(space+h)*j);
    rect(halfwmin,refy-(space+h)*j, halfwplus,(refy-h)-(space+h)*j);
    oldForestGreen=oldForestGreen+pitchForestGreen/(pitchForestGreen+1);
  }
  oldt2=pitcht2/(volt2+1);
  
  //PERIWINKlE
  stroke(255);
  rectMode(CORNERS);
  int pitchPeriwinkle = round(periwinklePitch / allPitchAdjust);
  int volPeriwinkle  = round(periwinkleVol);
  int oldPeriwinkle  = pitchPeriwinkle / (volPeriwinkle + 1);
  for(j=0; j<volPeriwinkle; j++)
  {
    fill(149-colorDecay*oldPeriwinkle, 176-colorDecay*oldPeriwinkle, 250-colorDecay*oldPeriwinkle); //149 176 250 (0 129 255)
    //rect(width-4*refx-w,refy-(space+h)*j,width-3*refx-w-off,(refy-h)-(space+h)*j);
    rect(halfwplus+space,refy-(space+h)*j,halfwplus+space+w,(refy-h)-(space+h)*j);
    oldPeriwinkle=oldPeriwinkle+pitchPeriwinkle/(volPeriwinkle+1);
  }
  volPeriwinkle=volPeriwinkle/(volPeriwinkle+1);
  
  //VIOLET
  stroke(255);
  rectMode(CORNERS);
  int pitchPurple = round(purplePitch / allPitchAdjust);
  int volPurple  = round(purpleVol);
  int oldPurple  = pitchPurple / (volPurple + 1);
  for(j=0; j<volPurple; j++)
  {
    fill(122-colorDecay*oldPurple, 73-colorDecay*oldPurple, 155-colorDecay*oldPurple); //122 73 155 (45 0 255 was indigo)
    //rect(width-3*refx-w,refy-(space+h)*j,width-2*refx-w-off,(refy-h)-(space+h)*j);
    rect(halfwplus+2*space+w,refy-(space+h)*j,halfwplus+2*space+2*w,(refy-h)-(space+h)*j);
    oldPurple=oldPurple+pitchPurple/(volPurple+1);
  }
  oldt1=pitcht1/(volt1+1);
  
  //FUCHSIA
  stroke(255);
  rectMode(CORNERS);
  int pitchFuchsia = round(fuchsiaPitch / allPitchAdjust);
  int volFuchsia  = round(fuchsiaVol);
  int oldFuchsia  = pitchFuchsia / (volFuchsia + 1);
  for(j=0; j<volFuchsia; j++)
  {
    fill(247-colorDecay*oldFuchsia, 3, 222-colorDecay*oldFuchsia); //247 3 222 (139 0 255 was purple)
    //rect(width-2*refx-w,refy-(space+h)*j,width-refx-w-off,(refy-h)-(space+h)*j);
    rect(halfwplus+3*space+2*w,refy-(space+h)*j,halfwplus+3*space+3*w,(refy-h)-(space+h)*j);
    oldFuchsia=oldFuchsia+pitchFuchsia/(volFuchsia+1);
  }
  oldb2=pitchb2/(volb2+1);
  
  //PINK
  stroke(255);
  rectMode(CORNERS);
  int pitchPink = round(pinkPitch / allPitchAdjust);
  int volPink  = round(pinkVol);
  int oldPink  = pitchPink / (volPink + 1);
  for(j=0; j<volPink; j++)
  {
    fill(255-colorDecay*oldPink, 175-colorDecay*oldPink, 210-colorDecay*oldPink); //255 130 180 (255 0 222 was fuchsia)
    //rect(width-refx-w,refy-(space+h)*j,width-refx,(refy-h)-(space+h)*j);
    rect(halfwplus+4*space+3*w,refy-(space+h)*j,halfwplus+4*space+4*w,(refy-h)-(space+h)*j);
    oldPink=oldPink+pitchb1/(volb1+1);
  }
  oldb1=pitchb1/(volb1+1);
  
} // draw()

// The following classes copied from DP_PitchAcrossScreenBall_SpeechChanges_EMM:
class LeadBall {
  float xLead;
  float yLead;
  LeadBall() {
    xLead = width/15;
    yLead = height*0.9;
  }
  void move (){
    xLead = xLead - speed*myIns.getAmplitude(whichInputMovesBalls) * amplify;
//    xLead = xLead - myIns.getAdjustedFundAsHz(whichInputMovesBalls)/10;
//    println("xLead = " + xLead + "; yLead = " + yLead);
  if (xLead < width/15){
//      yLead = height*0.9-in.getAdjustedFundAsHz();
      yLead = (height * 0.9) - myIns.getAdjustedFundAsHz(whichInputMovesBalls);
//      println("  set yLead to " + yLead);
      xLead = width-width/15;
    }//if x<0
  fill(200,100,200);
  ellipse(xLead,yLead,10,10);
  }  
 float getXpos() {
  return xLead;
  }
}

//makes a ball y=amp that will move across the screen
//once across screen, ball goes back to "start" and receives a new y=amp
class Ball{
  float x;
  float y;
  float speed;
  int ballNumber;
  int c;
  Ball(int inBallNumber){
    ballNumber = inBallNumber;
    y = height*0.9;
    c = ballNumber*10;
    constrain(c,0,255);
  }
  void move(){
    fill(c,100,100); 
    ellipse(x,y,10,10);
    if (ballNumber != 0){
      x = (myLeadBall.getXpos() + ballNumber*(width-width/15)/balls) % (width-width/15);
        if ((x < width/20)) {
          x = width-width/15;
        y = height*0.9-myIns.getAdjustedFundAsHz(whichInputMovesBalls);
          //y = in.getAdjustedFundAsHz();
       // println("  y = " + y);
        }
    }
  }//move
}//Ball