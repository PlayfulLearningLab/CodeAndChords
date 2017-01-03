/*
 * Amanda Tenhoff
 * 8/25/2016
 * Adjusting 'Daft Punk' code for 4 inputs
 * WITHOUT background balls
 * ALL BUILT-IN INPUTS
 * Input Method: MultipleInputsClass_EMM
 *
 */

AudioInput in;
MultipleInputs ins;

void setup()
{
  this.ins = new MultipleInputs(new String[] {"DP parts - Bass 1.mp3", "DP parts - Bass 2.mp3", "DP parts - Tenor 1.mp3", "DP parts - Tenor 2.mp3"});
}

void draw()
{
  background(0);
//CENTERING FOR FULLSCREEN
  int refy = round(3*height/4);    //bars build from 1/4 the height, from the bottom of the screen
  int refx = round(width/11);      //each bar (and space) takes up 1/11 of the width of the screen
  int space = round(height/100);   //to space the bars
  int w = refx-space;              //for universally determining the width of the bars
  int h = round(height/30);        //each stack/box is 1/30 the height of the screen
  int crefx = width/2;             //creating a center reference for 9 bars (symmetrical)
  int halfwmin = crefx - w/2;      //determining middle bar width, making reference for bars to the left
  int halfwplus = crefx + w/2;     //" " " ", making reference for the bars to the right
  
  /* rf = red and fuchsia
   * op = orange and purple
   * yi = yellow and indigo
   * gp = green and periwinkle
   */
  
  InputPitch bass1 = ins.get(0);
  float rfvol = 100*bass1.getAmplitude();
  float rfpit = bass1.getAdjustedFundAsHz();
  
  InputPitch bass2 = ins.get(1);
  float opvol = 100*bass2.getAmplitude();
  float oppit = bass2.getAdjustedFundAsHz();
  
  InputPitch tenor1 = ins.get(2);
  float yivol = 100*tenor1.getAmplitude();
  float yipit = tenor1.getAdjustedFundAsHz();
  
  InputPitch tenor2 = ins.get(3);
  float gpvol = 100*tenor2.getAmplitude();
  float gppit = tenor2.getAdjustedFundAsHz();
  
  //cyan will be just hanging out for now
  
  /*the following code creates rectangles which represent
   *the 9 different voices/inputs.
   *The rectangle needs the y-parameter to have the volume
   *subtracted by a given height since the y-axis starts
   *from the upper left-hand corner and is positive down.
   *Additionally, rectMode(CORNERS) lets me set the parameters
   *for each corner of the rectangle.
   */
   //int pitch = round(note/10);
   //int i = round(volume); //changed from what was previously volume%5
  
  //RED
  stroke(255);
  rectMode(CORNERS);
  int pitchb1 = round(rfpit/10);
  int volb1 = round(rfvol);
  int oldj=0; //for reversing color stack (darkest on bottom;
              //stack is from bottom to top)
  int oldpitchb1=pitchb1/(volb1+1); //have to add 1 because sometimes i is zero
  for(oldj=0; oldj<volb1; oldj++)
  {
    fill(255-5*oldpitchb1, 0, 0); //255 0 0
    rect(halfwmin-4*space-4*w,refy-(space+h)*oldj, halfwmin-4*space-3*w, (refy-h)-(space+h)*oldj); //makes boxes!
    oldpitchb1 = oldpitchb1+pitchb1/(volb1+1);
  }
  oldpitchb1 = pitchb1/(volb1+1);
  
 /* The magic formula here for the boxes is
  * rect([1], [2], [3], [4]) where:
  * [1] = starting x pixel, lower left corner
  * [2] = starting y pixel, lower left corner
  * [3] = ending x pixel, upper right corner
  * [4] = ending y pixel, upper right corner
  * Additonal information regarding these parameters:
  * [3] - [1] = width of the boxes/bars
  * [2] should be the starting bottom line (constant) subtracted by
  * the product of the difference in consecutive [2] values
  * and the count j. This is a lot of words. Addressing 'RED' above,
  * the boxes are meant to be 20 pixels tall and 10 pixels apart,
  * which means the first box [2] value is 0 and the second box [2]
  * value is 30. Thus, the [2] parameter is 800-30*j.
  * [4] is kind of the same deal but is at the top of your first
  * box, so in this case (knowing I want a height of 20 pixels)
  * would have 800-2-30*j, which simplifies to 780-30*j.
  * Knowing this, we can now make consecutive boxes and shapes of
  * all sizes! :)
  */
  
  //ORANGE
  stroke(255);
  rectMode(CORNERS);
  int pitchb2 = round(oppit/10);
  int volb2 = round(opvol);
  int oldb2 = pitchb2/(volb2+1);
  for(oldj=0; oldj<volb2; oldj++)
  {
    fill(255-5*oldb2, 145-5*oldb2, 0); //255 145 0
    rect(halfwmin-3*space-3*w,refy-(space+h)*oldj,halfwmin-3*space-2*w,(refy-h)-(space+h)*oldj);
    oldb2 = oldb2 + pitchb2/(volb2+1);
  }
  oldb2=pitchb2/(volb2+1);
  
  //YELLOW
  stroke(255);
  rectMode(CORNERS);
  int pitcht1 = round(yipit/10);
  int volt1 = round(yivol);
  int oldt1 = pitcht1/(volt1+1);
  for(oldj=0; oldj<volt1; oldj++)
  {
    fill(250-5*oldt1, 255-5*oldt1, 0); //250 255 0
    rect(halfwmin-2*space-2*w,refy-(space+h)*oldj,halfwmin-2*space-w,(refy-h)-(space+h)*oldj);
    oldt1 = oldt1+pitcht1/(volt1+1);
  }
  oldt1=pitcht1/(volt1+1);
  
  //ELECTRIC GREEN
  stroke(255);
  rectMode(CORNERS);
  int pitcht2 = round(gppit/10);
  int volt2 = round(gpvol);
  int oldt2 = pitcht2/(volt2+1);
  for(oldj=0; oldj<volt2; oldj++)
  {
    fill(41-5*oldt2, 255-5*oldt2, 0); //41 255 0
    rect(halfwmin - space - w,refy-(space+h)*oldj,halfwmin - space,(refy-h)-(space+h)*oldj);
    oldt2=oldt2+pitcht2/(volt2+1);
  }
  oldt2=pitcht2/(volt2+1);
  
  //CYAN
  stroke(255);
  rectMode(CORNERS);
  for(oldj=0; oldj<volt2; oldj++)
  {
    fill(0, 255-5*oldt2, 232-5*oldt2); //0 255 232
    rect(halfwmin,refy-(space+h)*oldj, halfwplus,(refy-h)-(space+h)*oldj);
    oldt2=oldt2+pitcht2/(volt2+1);
  }
  oldt2=pitcht2/(volt2+1);
  
  //PERIWINKlE
  stroke(255);
  rectMode(CORNERS);
  for(oldj=0; oldj<volt2; oldj++)
  {
    fill(0, 129-5*oldt2, 255-5*oldt2); //0 129 255
    rect(halfwplus+space,refy-(space+h)*oldj,halfwplus+space+w,(refy-h)-(space+h)*oldj);
    oldt2=oldt2+pitcht2/(volt2+1);
  }
  oldt2=pitcht2/(volt2+1);
  
  //INDIGO
  stroke(255);
  rectMode(CORNERS);
  for(oldj=0; oldj<volt1; oldj++)
  {
    fill(45-5*oldt1, 0, 255-5*oldt1); //45 0 255
    rect(halfwplus+2*space+w,refy-(space+h)*oldj,halfwplus+2*space+2*w,(refy-h)-(space+h)*oldj);
    oldt1=oldt1+pitcht1/(volt1+1);
  }
  oldt1=pitcht1/(volt1+1);
  
  //PURPLE
  stroke(255);
  rectMode(CORNERS);
  for(oldj=0; oldj<volb2; oldj++)
  {
    fill(139-5*oldb2, 0, 255-5*oldb2); //139 0 255
    rect(halfwplus+3*space+2*w,refy-(space+h)*oldj,halfwplus+3*space+3*w,(refy-h)-(space+h)*oldj);
    oldb2=oldb2+pitchb2/(volb2+1);
  }
  oldb2=pitchb2/(volb2+1);
  
  //FUCHSIA
  stroke(255);
  rectMode(CORNERS);
  for(oldj=0; oldj<volb1; oldj++)
  {
    fill(255-5*oldpitchb1, 0, 222-5*oldpitchb1); //255 0 222
    rect(halfwplus+4*space+3*w,refy-(space+h)*oldj,halfwplus+4*space+4*w,(refy-h)-(space+h)*oldj);
    oldpitchb1=oldpitchb1+pitchb1/(volb1+1);
  }
  oldpitchb1=pitchb1/(volb1+1);
  
}