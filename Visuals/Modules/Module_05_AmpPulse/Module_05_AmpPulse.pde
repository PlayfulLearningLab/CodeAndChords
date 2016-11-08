/*
  * 10/4/2016
  * Amanda Tenhoff
  * Amp Pulse
  *
  * This module involves balls which move across the screen
  * in response to to amplitude. This module is heavily concerned
  * with attack and response, creating a sort of visible pulse.
 */
  
Input testInput;
AudioInput in;

int VolAdjust = 10;
int PitAdjust = 10;

int colorDecay = 2;

int stackheight = 15;

int  volAdjust             = 20;       // divide amp by this.
float speed                = 20;       // divide amp by this to get a ball move speed.
float amplify              = 0.05;     // multiply amp by this to slow the ball speed.
//int  whichInputMovesBalls  = 2;        // this line moves the balls in the background.

int note;
int volume;

// variables for balls:
LeadBall myLeadBall;

Ball[] myBall;
//how many balls across the screen
int balls = 30;

void setup()
{
  testInput  = new Input();  //calls the Input_Class
    // setup for background balls:
  myLeadBall = new LeadBall();
  myBall = new Ball[balls];
  for (int i = 0; i < balls; i++) {
    myBall[i] = new Ball(i);
  }//for
}

void draw()
{
  background(0);
  
    // drawing the balls in the background:
  myLeadBall.move();
  //ARRAY OBJECTS STEP 4 (for loop)
  for (int b = 0; b < myBall.length; b++) {
    myBall[b].move();
  }//for
  
  note = round(testInput.getFreqAsHz());
  volume = round(testInput.getAmplitude());

/*

  int refy = round(3*height/4);
  println("refy is "+refy);
  int refx = round(width/11);
  int space = round(height/100);  //for universally determing the space between stacks
  int w = refx-space;  //for universally determining the width of the bars
  println("w is "+w);
  int h = round(height/30);  //for universally determining the height of the stacks
  println("h is "+h);
  //int off = space/2;
  int crefx = width/2;
  int halfwmin = crefx - w/2;
  int halfwplus = crefx + w/2;
  */
  
}

class LeadBall {          //first ball
  float xLead;
  float yLead;
  LeadBall() {
    xLead = width;        //starts at far right of screen
    yLead = height*0.9;   //and 1/10 of height from bottom of screen
  }
  void move () {
    xLead = xLead - speed*testInput.getAmplitude() * amplify;      // amplify is a fraction
              //ball moves across screen as a function of volume

    if (xLead < 0) {
      yLead = (height * 0.9) - testInput.getFreqAsHz();
      xLead = width;
    }//if x<0
     //when it gets to the leftmost side of the screen, it starts over on the right again
     //with a new height depending on the pitch
     
     if (testInput.getAmplitude() > note)
     {
       fill(255, 246, 198);
       ellipse(xLead, yLead, 30, 30);
     }
      fill(200, 100, 200);
      ellipse(xLead, yLead, 20, 20);
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
     if (testInput.getAmplitude() > note)
     {
       fill(255, 246, 198);
       ellipse(x, y, 30, 30);
     }
    fill(c, 100, 100);
    ellipse(x, y, 20, 20);
    if (ballNumber != 0) {     //not looking at LeadBall
      x = (myLeadBall.getXpos() + ballNumber*(width)/balls) % (width);
                               //spaces balls evenly, based on location of LeadBall
      if ((x < 1)&&(x>-1)) {
        x = width;
        y = height*0.9-testInput.getFreqAsHz();
      } // if ((x < 1)&&(x>-1))
    } // if (ballNumber != 0)
  }//move
}//Ball