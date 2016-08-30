class RosetteV3Colors extends RosetteV3
{
  /*
    06/29/2016
   Emily Meuer
   
   Class that shows rosettes based on the pitch of one input
   and rotates the rosettes based on the pitch of another input.
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

   int[]  growFrequencies = {    // a rosette is drawn each time the "low voice" avg frequency
     150,                        // passes one of these points.
     550,
     600,
     650,
     700,
     750
   }; // growFrequencies
  RosetteV3Colors(Input leftInput, Input rightInput, int tenorCutoff)
  {
    super(leftInput, rightInput);
    
    this.tenorCutoff = tenorCutoff;
  } // RosetteV3

  void run()
  {
    background(0);
    translate(width/2, height/2);

    float pitch = leftInput.getAdjustedFundAsHz();
    // Want to add the tenor pitches to this one, too?  Since they aren't making it rotate?
    
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

    if (pitch > 160) {
      pushMatrix();
      rotate(radians(rotateBy));
      rosettePartThree(radius1, color(red, green, blue));
      popMatrix();
      green = (green + 50) % 255;
    }
    if (pitch > 190) {
      pushMatrix();
      rotate(radians(-rotateBy));
      rosettePartThree(radius2, color(red, green, blue));
      popMatrix();
      green = (green + 50) % 255;
    }
    if (pitch > 200) {
      pushMatrix();
      rotate(radians(rotateBy));
      rosettePartTwo(radius3, color(red, green, blue));
      popMatrix();
      green = (green + 50) % 255;
    }
    if (pitch > 225) {
      pushMatrix();
      rotate(radians(-rotateBy));
      rosettePartTwo(radius4, color(red, green, blue));
      popMatrix();
      green = (green + 50) % 255;
    }
    if (pitch > 255) {
      pushMatrix();
      rotate(radians(rotateBy));
      rosettePartOne(radius5, color(red, green, blue));
      popMatrix();
      green = (green + 50) % 255;
    }
    if (pitch > 325) {
      pushMatrix();
      rotate(radians(-rotateBy));
      rosettePartOne(radius6, color(red, green, blue));
      popMatrix();
      green = (green + 50) % 255;
    }
  } // run()
}// RosetteV3 