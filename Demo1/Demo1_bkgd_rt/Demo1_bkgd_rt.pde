/*
 * Amanda Tenhoff
 * 8/25/2016
 * Adjusting 'Daft Punk' code for 4 inputs
 * WITH background balls
 * ALL REALTIME
 *
 */

// Calibrate:
int  volAdjust             = 20;       // divide amp by this.
float speed                = 10;       // divide amp by this to get a ball move speed.
float amplify              = 0.05;     // multiply amp by this to slow the ball speed.
int  whichInputMovesBalls  = 2;        // this line moves the balls in the background.

int stackheight            = 15;       //easily adjusting number of stacks for bars

Input myIns;   //this is for the new input class

// variables for balls:
LeadBall myLeadBall;

Ball[] myBall;
//how many balls across the screen
int balls = 30;


int b1VolAdjust = 100;  //multiplier for volume calibration for Bass 1
int b2VolAdjust = 100;
int t1VolAdjust = 100;
int t2VolAdjust = 100;

int b1PitAdjust = 10;  //multiplier for pitch/frequency calibration for Bass 1
int b2PitAdjust = 10;
int t1PitAdjust = 10;
int t2PitAdjust = 10;

int  allPitchAdjust = 10;

int colorDecay = 7; //adjusting constant for color decay of bars (fade)

//so I don't think any of the following are necessary
//because they are declared and initialized elsewhere

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
  for (int i = 0; i < balls; i++) {
    myBall[i] = new Ball(i);
  }//for
}

void draw()
{
  try 
  {  
  background(0);

  //DRUM BOX
  // will be hooked to makey makey, so that hitting the drum
  // lights the rectangle around the screen.
  if (keyPressed)
  {
    stroke(255); 
    fill(255);
  } else
  {
    stroke(155);
    fill(0);
  }
  rectMode(CORNERS);
  rect(width/20, height*0.95, width-width/20, height*0.92);

  // drawing the balls in the background:
  myLeadBall.move();
  //ARRAY OBJECTS STEP 4 (for loop)
  for (int i = 0; i < myBall.length; i++) {
    myBall[i].move();
  }//for

//CENTERING FOR FULLSCREEN
  int refy = round(3*height/4);    //bars build from 1/4 the height, from the bottom of the screen
  int refx = round(width/11);      //each bar (and space) takes up 1/11 of the width of the screen
  int space = round(height/100);   //to space the bars
  int w = refx-space;              //for universally determining the width of the bars
  int h = round(height/30);        //each stack/box is 1/30 the height of the screen
  int crefx = width/2;             //creating a center reference for 9 bars (symmetrical)
  int halfwmin = crefx - w/2;      //determining middle bar width, making reference for bars to the left
  int halfwplus = crefx + w/2;     //" " " ", making reference for the bars to the right

  float rpvol = myIns.getAmplitude(1) / volAdjust;           //'volume' of bass1, used for columns RED and PINK
  float rppit = myIns.getAdjustedFund(1);                //'pitch' of bass1, used for columns RED and PINK

  float ofvol = myIns.getAmplitude(2) / volAdjust;
  float ofpit = myIns.getAdjustedFund(2);

  float ypvol = myIns.getAmplitude(3) / volAdjust;
  float yppit = myIns.getAdjustedFund(3);

  float ggpvol = myIns.getAmplitude(4) / volAdjust;
  float ggppit = myIns.getAdjustedFund(4);
  

//matching colors to inputs, getting volume
  /*float  redVol  = myIns.getAmplitude(1) / volAdjust;
  float  orangeVol  = myIns.getAmplitude(2) / volAdjust;
  float  yellowVol  = myIns.getAmplitude(3) / volAdjust;
  float  lightGreenVol  = myIns.getAmplitude(4) / volAdjust;
  float  forestGreenVol  = myIns.getAmplitude(5) / volAdjust;
  float  periwinkleVol  = myIns.getAmplitude(6) / volAdjust;
  float  purpleVol  = myIns.getAmplitude(7) / volAdjust;
  float  fuchsiaVol  = myIns.getAmplitude(8) / volAdjust;
  float  pinkVol  = myIns.getAmplitude(8) / volAdjust;
  */

//matching colors to inputs, getting pitches
  /*float  redPitch  = myIns.getAdjustedFund(1);
  float  orangePitch  = myIns.getAdjustedFund(2);
  float  yellowPitch  = myIns.getAdjustedFund(3);
  float  lightGreenPitch  = myIns.getAdjustedFund(4);
  float  forestGreenPitch  = myIns.getAdjustedFund(5);
  float  periwinklePitch  = myIns.getAdjustedFund(6);
  float  purplePitch  = myIns.getAdjustedFund(7);
  float  fuchsiaPitch  = myIns.getAdjustedFund(8);
  float  pinkPitch  = myIns.getAdjustedFund(8);
  */

  //RED
  stroke(255);
  rectMode(CORNERS);                            //arguments are all corner coordinates
  int pitchb1 = round(rppit/b1PitAdjust);    //division for creating color gradient for stacks
  int volb1 = round(rpvol);                    //turning volume into an integer for fractions
  if (volb1>stackheight)
  {
    volb1=stackheight;    //creates a ceiling for stacks, so they don't run off-screen
  }

  int j=0;                      //stack is from bottom to top
  int oldb1=pitchb1/(volb1+1);  //have to add 1 because sometimes volume is zero
                                //this is the color gradient factor
                                //so if volb1 was 9, from the volume, there would be 9 stacks
                                //and if pitchb1 was 1, oldb1 would be 1/10
                                //this is explained further after the for loop
  for (j=0; j<volb1; j++)
  {
    fill(224-colorDecay*oldb1, 36-colorDecay*oldb1, 71-colorDecay*oldb1);
    rect(halfwmin-4*space-4*w, refy-(space+h)*j, halfwmin-4*space-3*w, (refy-h)-(space+h)*j);
    //rect(left bottom corner, left upper corner, right bottom corner, right upper corner)
    //this rectangle starts at a point which is 4 bars and 4 spaces to the left of halfway across the screen
    //that is the lower left corner
    //(halfwmin = lefthand side of middle bar, 4 spaces between the bars, 4 bars = 4w)
    //refy-(space+h)*j means the y-coordinate for the lower left corner is at the point of refy
    //but for every box in the stack, they are separated by 'space'
    //and 'h' is the height of each box
    //j = 2 when drawing the third box, which stacks itself at refy-2*(space+h), leaving room for
    //the two boxes underneath it
    oldb1 = oldb1+pitchb1/(volb1+1);
  }
  oldb1 = pitchb1/(volb1+1);
   //for the first box, the fill would be the most color-accurate,
   //but for each box stacked on top, the fill gets more and more black
   //because of colorDecay and the oldb1 fraction/gradient

  //ORANGE
  stroke(255);
  rectMode(CORNERS);
  int pitchb2 = round(ofpit/b2PitAdjust);
  int volb2 = round(ofvol);
  if (volb2>stackheight)
  {
    volb2=stackheight;
  }

  int oldb2 = pitchb2/(volb2+1);
  for (j=0; j<volb2; j++)
  {
    fill(255-colorDecay*oldb2, 90-colorDecay*oldb2, 8);
    rect(halfwmin-3*space-3*w, refy-(space+h)*j, halfwmin-3*space-2*w, (refy-h)-(space+h)*j);
    oldb2 = oldb2 + pitchb2/(volb2+1);
  }
  oldb2=pitchb2/(volb2+1);

  //YELLOW
  stroke(255);
  rectMode(CORNERS);
  int pitcht1 = round(yppit/t1PitAdjust);
  int volt1 = round(ypvol);
  if (volt1>stackheight)
  {
    volt1=stackheight;
  }

  int oldt1 = pitcht1/(volt1+1);
  for (j=0; j<volt1; j++)
  {
    fill(255-colorDecay*oldt1, 234-colorDecay*oldt1, 44-colorDecay*oldt1);
    rect(halfwmin-2*space-2*w, refy-(space+h)*j, halfwmin-2*space-w, (refy-h)-(space+h)*j);
    oldt1 = oldt1+pitcht1/(volt1+1);
  }
  oldt1=pitcht1/(volt1+1);

  //LIGHT GREEN
  stroke(255);
  rectMode(CORNERS);
  int pitcht2 = round(ggppit/t2PitAdjust);
  int volt2 = round(ggpvol);
  if (volt2>stackheight)
  {
    volt2=stackheight;
  }
  int oldt2 = pitcht2/(volt2+1);
  for (j=0; j<volt2; j++)
  {
    fill(162-colorDecay*oldt2, 211-colorDecay*oldt2, 2);
    rect(halfwmin - space - w, refy-(space+h)*j, halfwmin - space, (refy-h)-(space+h)*j);
    oldt2=oldt2+pitcht2/(volt2+1);
  }
  oldt2=pitcht2/(volt2+1);

  //FOREST GREEN
  stroke(255);
  rectMode(CORNERS);
  int pitchForestGreen = round(ggppit / allPitchAdjust);
  int volForestGreen  = round(ggpvol);
  if (volForestGreen>stackheight)
  {
    volForestGreen=stackheight;
  }
  int oldForestGreen  = pitchForestGreen / (volForestGreen + 1);
  for (j=0; j<volForestGreen; j++)
  {
    fill(1, 100-colorDecay*oldForestGreen, 65-colorDecay*oldForestGreen);
    rect(halfwmin, refy-(space+h)*j, halfwplus, (refy-h)-(space+h)*j);
    oldForestGreen=oldForestGreen+pitchForestGreen/(pitchForestGreen+1);
  }
  oldt2=pitcht2/(volt2+1);

  //PERIWINKlE
  stroke(255);
  rectMode(CORNERS);
  int pitchPeriwinkle = round(ggppit / allPitchAdjust);
  int volPeriwinkle  = round(ggpvol);
  if (volPeriwinkle>stackheight)
  {
    volPeriwinkle=stackheight;
  }
  int oldPeriwinkle  = pitchPeriwinkle / (volPeriwinkle + 1);
  for (j=0; j<volPeriwinkle; j++)
  {
    fill(149-colorDecay*oldPeriwinkle, 176-colorDecay*oldPeriwinkle, 250-colorDecay*oldPeriwinkle);
    rect(halfwplus+space, refy-(space+h)*j, halfwplus+space+w, (refy-h)-(space+h)*j);
    oldPeriwinkle=oldPeriwinkle+pitchPeriwinkle/(volPeriwinkle+1);
  }
  volPeriwinkle=volPeriwinkle/(volPeriwinkle+1);

  //VIOLET
  stroke(255);
  rectMode(CORNERS);
  int pitchPurple = round(yppit / allPitchAdjust);
  int volPurple  = round(ypvol);
  if (volPurple>stackheight)
  {
    volPurple=stackheight;
  }
  int oldPurple  = pitchPurple / (volPurple + 1);
  for (j=0; j<volPurple; j++)
  {
    fill(122-colorDecay*oldPurple, 73-colorDecay*oldPurple, 155-colorDecay*oldPurple);
    rect(halfwplus+2*space+w, refy-(space+h)*j, halfwplus+2*space+2*w, (refy-h)-(space+h)*j);
    oldPurple=oldPurple+pitchPurple/(volPurple+1);
  }
  oldt1=pitcht1/(volt1+1);

  //FUCHSIA
  stroke(255);
  rectMode(CORNERS);
  int pitchFuchsia = round(ofpit / allPitchAdjust);
  int volFuchsia  = round(ofvol);
  if (volFuchsia>stackheight)
  {
    volFuchsia=stackheight;
  }
  int oldFuchsia  = pitchFuchsia / (volFuchsia + 1);
  for (j=0; j<volFuchsia; j++)
  {
    fill(247-colorDecay*oldFuchsia, 3, 222-colorDecay*oldFuchsia);
    rect(halfwplus+3*space+2*w, refy-(space+h)*j, halfwplus+3*space+3*w, (refy-h)-(space+h)*j);
    oldFuchsia=oldFuchsia+pitchFuchsia/(volFuchsia+1);
  }
  oldb2=pitchb2/(volb2+1);

  //PINK
  stroke(255);
  rectMode(CORNERS);
  int pitchPink = round(rppit / allPitchAdjust);
  int volPink  = round(rpvol);
  if (volPink>stackheight);
  {
    volPink=stackheight;
  }
  int oldPink  = pitchPink / (volPink + 1);
  for (j=0; j<volPink; j++)
  {
    fill(255-colorDecay*oldPink, 175-colorDecay*oldPink, 210-colorDecay*oldPink);
    rect(halfwplus+4*space+3*w, refy-(space+h)*j, halfwplus+4*space+4*w, (refy-h)-(space+h)*j);
    oldPink=oldPink+pitchb1/(volb1+1);
  }
  oldb1=pitchb1/(volb1+1);
  }

  catch (NullPointerException npe)  {}
} // draw()

// The following classes copied from DP_PitchAcrossScreenBall_SpeechChanges_EMM:
class LeadBall {          //first ball
  float xLead;
  float yLead;
  LeadBall() {
    xLead = width;        //starts at far right of screen
    yLead = height*0.9;   //and 1/10 of height from bottom of screen
  }
  void move () {
    xLead = xLead - speed*myIns.getAmplitude(whichInputMovesBalls) * amplify;      // amplify is a fraction
              //ball moves across screen as a function of volume

    if (xLead < 0) {
      yLead = (height * 0.9) - myIns.getAdjustedFundAsHz(whichInputMovesBalls);
      xLead = width;
    }//if x<0
     //when it gets to the leftmost side of the screen, it starts over on the right again
     //with a new height depending on the pitch
     
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
  float x;                     //coordinate
  float y;
  float speed;
  int ballNumber;
  int c;
  Ball(int inBallNumber) {
    ballNumber = inBallNumber; //counts number of balls(?)
    y = height*0.9;
    c = ballNumber*10;
    constrain(c, 0, 255);      //limits 'c' to be within range for colors
  }
  void move() {
    fill(c, 100, 100);
    ellipse(x, y, 10, 10);
    if (ballNumber != 0) {     //not looking at LeadBall
      x = (myLeadBall.getXpos() + ballNumber*(width)/balls) % (width);
                               //spaces balls evenly, based on location of LeadBall
      if ((x < 1)&&(x>-1)) {
        x = width;
        y = height*0.9-myIns.getAdjustedFundAsHz(whichInputMovesBalls);
      } // if ((x < 1)&&(x>-1))
    } // if (ballNumber != 0)
  }//move
}//Ball