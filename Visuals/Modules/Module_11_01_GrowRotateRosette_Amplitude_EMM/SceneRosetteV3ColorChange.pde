class RosetteV3Colors extends RosetteV3
{
  /*
    06/29/2016
   Emily Meuer
   
   Class that shows rosettes based on the amplitude of an input
   and rotates the rosettes based on the amplitude of the same input.
   */
   
   // Calibrate:
 /*  int[]  growFrequencies = {    // a rosette is drawn each time the "low voice" avg frequency
     160,                        // passes one of these points.
     190,
     200,
     225,
     255,
     325
   }; // growFrequencies
*/ 

  float  amplitude   = 0;
  int    attackTime  = 100;

   int[]  growFrequencies = {    // a rosette is drawn each time the "low voice" avg frequency
     150,                        // passes one of these points.
     550,
     600,
     650,
     700,
     750
   }; // growFrequencies
   
  /**
   *  Constructor; makes a RosetteV3Colors with the given Input and tenorCutoff.
   *
   *  @param  input        an Input, used to get bass and tenor pitch and amp.
   *  @param  tenorCutoff  an int at which the mics divide into basses (below) and tenors (this and all above).
   */
  RosetteV3Colors(Input input)
  {
    super(input);
    
//    this.tenorCutoff = tenorCutoff;
  } // RosetteV3

  /**
   *  Called in draw() in the Zikr_Scenes_Inputs1_9_EMM tab;
   *  draws 6 rosettes with the above radii and rotated by rotateBy and -rotateBy (every other) -
   *  as in RosetteV3.run(), but with more green in each rosette.
   */
  void run()
  {
    background(0);
    translate(width/2, height/2);

    float pitch = input.getAdjustedFund();
    float  goalAmplitude = input.getAmplitude();
    if(amplitude < goalAmplitude + attackTime) 
    {
      amplitude = amplitude + attackTime;
    } else if (amplitude > goalAmplitude - attackTime)
    {
      amplitude = amplitude - attackTime;
    } // if
    println("amplitude = " + amplitude);
    
    pitchColor();
    green  = 0;

    /*
     Frequencies:
     Eb 3      -  155.56
     Fb (E) 3  -  164.81
     G 3       -  196.00
     Ab 3      -  207.65
     Bb 3      -  233.08
     C 4       -  261.63
     Fb (E) 4  -  329.63
     */

    if (amplitude > 0) {
      pushMatrix();
      rotate(radians(rotateBy));
      rosettePartThree(radius1, color(red, green, blue));
      popMatrix();
      green = (green + 50) % 255;
    }
    if (amplitude > 350) {
      pushMatrix();
      rotate(radians(-rotateBy));
      rosettePartThree(radius2, color(red, green, blue));
      popMatrix();
      green = (green + 50) % 255;
    }
    if (amplitude > 700) {
      pushMatrix();
      rotate(radians(rotateBy));
      rosettePartTwo(radius3, color(red, green, blue));
      popMatrix();
      green = (green + 50) % 255;
    }
    if (amplitude > 1100) {
      pushMatrix();
      rotate(radians(-rotateBy));
      rosettePartTwo(radius4, color(red, green, blue));
      popMatrix();
      green = (green + 50) % 255;
    }
    if (amplitude > 1450) {
      pushMatrix();
      rotate(radians(rotateBy));
      rosettePartOne(radius5, color(red, green, blue));
      popMatrix();
      green = (green + 50) % 255;
    }
    if (amplitude > 1800) {
      pushMatrix();
      rotate(radians(-rotateBy));
      rosettePartOne(radius6, color(red, green, blue));
      popMatrix();
      green = (green + 50) % 255;
    }
  } // run()
}// RosetteV3 