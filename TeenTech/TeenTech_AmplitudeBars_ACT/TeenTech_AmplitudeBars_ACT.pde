/*
 * Amanda Tenhoff
 * 6 January 2017
 *
 * ** Uses minim. **
 *
 * Module 04: Amplitude Bars
 * This module takes a single input and creates stacked bars whose stackheight
 * depends on amplitude.
 * 
 * This module does not deal with pitch at all.
 *
 * FULLSCREEN can be changed by going into 'InputClassPitch_EMM'
 * looking in the 'void settings();'
 * and putting '//' in front of 'fullScreen()
 * then deleting the '//' in front of 'size(#,#)'
 * You determine the width and height of the screen by replacing
 * the numbers (#) with your own!
 * 
 */
  
Input testInput;            // global variable 'testInput' of type Input
AudioInput in;              // global variable 'in' of type AudioInput

float VolAdjust = 0.08;     // global variable 'VolAdjust' which can be manipulated
                            // to control the sensitivity of the bars stacking
                            // (control how much the volume has to change in order
                            // for more bars to stack)
                            
int stackheight = 15;       // maximum number of bars stacking in a column

void setup()                // this void loop returns nothing and is only run once
{
  testInput  = new Input();  //calls the Input_Class
}

void draw()                  // this void loop returns nothing and iterates continuously
{
  background(0);             // sets the background color to black (on scale from 0-255)
  
  float volume = testInput.getAmplitude();    // creates volume variable which is a numerical
                                              // representation of volume
  int amplitude = round(volume);              // turns from 'float' type to 'int' type
  int i = round(amplitude*VolAdjust);         // 'i' is used to determine how many bars are created

  if (i>stackheight)         // this 'if' statement checks to see if the number of bars to be
                             // created is higher than the maximum number of bars set previously
  {
    i=stackheight;           // if this is true, then the value of 'i' is set to the value of 'stackheight'
  }
  
  int refy = round(3*height/4);                // reference variable as a starting point for stacking bars
                                               // (can be used with any size screen)
                                               
  int refx = round(width/11);                  // reference variable for dividing the screen size into 11 chunks
                                               // (11 chunks because there are 9 bars, and 2 chunks of
                                               // equal space on either sides of the screen
                                               
  int space = round(height/100);               //for universally determing the space between stacks
  
  int w = refx-space;                          //for universally determining the width of the bars
  
  int h = round(height/30);                    //for universally determining the height of the stacks
  
  int crefx = width/2;                         // 'crefx' stands for 'center reference x', which
                                               // numerically determines the center of the screen
                                               // in the x direction
                                               
  int halfwmin = crefx - w/2;                  // 'halfwmin' stands for 'half width minus'
                                               // this is the left-hand side of the middle stack of bars
                                               
  int halfwplus = crefx + w/2;                 // 'halfwplus' stands for 'half width plus'
                                               // this is the right-hand side of the middle stack of bars
    
  //RED
  stroke(255);                                 // creates border color around boxes
  rectMode(CORNERS);                           // sets 'rect' function so that it takes input of locations
                                               // of four corners to create a box
                                               
  int oldj=0;                                  // 'oldj' is just a reference used for counting how many boxes
                                               // have been drawn (used in the following loop)
  
  for(oldj=0; oldj<i; oldj++)                  // this translates as 'given oldj starts at a value of 0, as long as
                                               // the value of oldj is less than the value of i, do the things listed
                                               // between the curly brackets and increase the value of oldj by 1
                                               // every time you loop through the brackets'
                                               
  {
    fill(255, 0, 0);                           // fills box with color (RGB value)
    
    rect(halfwmin-4*space-4*w,refy-(space+h)*oldj, halfwmin-4*space-3*w, (refy-h)-(space+h)*oldj); //makes boxes! See note below.
  }
  
  /* How the Boxes Work:
   *
   * The screen measures x distance to from the left, and y distance from the top.
   * In other words, the (0,0) coordinate is the upper-left-hand corner of the screen.
   *
   * The 'rect' function takes in 2 diagonal corner locations, which are as follows:
   * Given point 1 is at location (x1, y1) and point 2 is at location (x2, y2),
   * rect(x1, y1, x2, y2)
   * draws:
   
            * --------- 2
            |           |
            |           |
            1 --------- *
            
   * The boxes in this code are all drawn with reference to the center of the width of the screen
   * The bar in the exact middle of the screen is FOREST GREEN
   * (other boxes' coordinates will make more sense looking at FOREST GREEN)
   *
   * POINT 1:
   * So, since RED starts to the left of FOREST GREEN, it refers to 'halfwmin'
   * It is also 4 full bars away from FOREST GREEN (-4*w) and there are as many spaces
   * between bars as there are bars (-4*space)
   * Coordinate x1 is then halfwmin-4*w-4*space
   * If drawing the bottom-most bar in the stack, oldj is 0 (none have been drawn yet)
   * Then, y1 is just at location 'refy'.
   * If drawing, say, the third box in the stack, oldj is 2
   * Then, y1 is (2 heights of bars AND 2 spaces between bars) away from 'refy'
   *
   * POINT 2:
   * x2 is not 4 full bars to the left of FOREST GREEN, because it is on the right edge of RED
   * So, it is only (-3*w) away, however it is still 4 spaces away (-4*space)
   * y2 is similar to y1, except that it is one height distance (h) away from y1
   * 
   * Now we have a whole box!  
  */
  
  //ORANGE
  stroke(255);
  rectMode(CORNERS);
  for(oldj=0; oldj<i; oldj++)
  {
    fill(255, 145, 0);
    rect(halfwmin-3*space-3*w,refy-(space+h)*oldj,halfwmin-3*space-2*w,(refy-h)-(space+h)*oldj);
  }
  
  //YELLOW
  stroke(255);
  rectMode(CORNERS);
  for(oldj=0; oldj<i; oldj++)
  {
    fill(250, 255, 0);
    rect(halfwmin-2*space-2*w,refy-(space+h)*oldj,halfwmin-2*space-w,(refy-h)-(space+h)*oldj);
  }
  
  //LIGHT GREEN
  stroke(255);
  rectMode(CORNERS);
  for(oldj=0; oldj<i; oldj++)
  {
    fill(41, 255, 0);
    rect(halfwmin - space - w,refy-(space+h)*oldj,halfwmin - space,(refy-h)-(space+h)*oldj);
  }
  
  //FOREST GREEN
  stroke(255);
  rectMode(CORNERS);
  for(oldj=0; oldj<i; oldj++)
  {
    fill(0, 255, 232);
    rect(halfwmin,refy-(space+h)*oldj, halfwplus,(refy-h)-(space+h)*oldj);
  }
  
  //PERIWINKlE
  stroke(255);
  rectMode(CORNERS);
  for(oldj=0; oldj<i; oldj++)
  {
    fill(0, 129, 255);
    rect(halfwplus+space,refy-(space+h)*oldj,halfwplus+space+w,(refy-h)-(space+h)*oldj);
  }
  
  //VIOLET
  stroke(255);
  rectMode(CORNERS);
  for(oldj=0; oldj<i; oldj++)
  {
    fill(45, 0, 255);
    rect(halfwplus+2*space+w,refy-(space+h)*oldj,halfwplus+2*space+2*w,(refy-h)-(space+h)*oldj);
  }
  
  //FUCHSIA
  stroke(255);
  rectMode(CORNERS);
  for(oldj=0; oldj<i; oldj++)
  {
    fill(139, 0, 255);
    rect(halfwplus+3*space+2*w,refy-(space+h)*oldj,halfwplus+3*space+3*w,(refy-h)-(space+h)*oldj);
  }
  
  //PINK
  stroke(255);
  rectMode(CORNERS);
  for(oldj=0; oldj<i; oldj++)
  {
    fill(255, 0, 222);
    rect(halfwplus+4*space+3*w,refy-(space+h)*oldj,halfwplus+4*space+4*w,(refy-h)-(space+h)*oldj);
  }
  
}