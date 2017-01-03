abstract class RosetteV3 extends Scene
{
  /*
    06/29/2016
   Emily Meuer
   
   Abstract, base rosette class.
   */

  // The following specify the length of the radius of each of the 6 rosettes:
  float radius1;
  float radius2;
  float radius3;
  float radius4;
  float radius5;
  float radius6;

  float  rotateBy;    // amt by which the rosette should be rotated.

  /**
   *  Constructor; makes a RosetteV3 with the given Input and pre-determined radii.
   *
   *  @param  input        an Input, used to get bass pitch.
   */
  RosetteV3(Input  input)
  {
//    this.leftInput  = leftInput;
//    this.rightInput = rightInput;
      this.input      = input;

    this.rotateBy  = 0;

    this.radius1 = 40;
    this.radius2 = 100;
    this.radius3 = 270;
    this.radius4 = radius3 * 1.4;
    this.radius5 = 500;
    this.radius6 = 725;
  } // RosetteV3

  /**
   *  Called in draw() in the Zikr_Scenes_Inputs1_9_EMM tab;
   *  draws 6 rosettes with the above radii and rotated by rotateBy and -rotateBy (every other).
   */
  void run()
  {
    background(0);
    translate(width/2, height/2);

    // pitch is controlled by the basses:
    float pitch = input.getAverageFund(1, this.tenorCutoff - 1);
// Again, want to add tenors?  Autem, nisi fallor, they don't sing here.

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
      rosettePartThree(radius1, originalThree);
      popMatrix();
    }
    if (pitch > 190) {
      pushMatrix();
      rotate(radians(-rotateBy));
      rosettePartThree(radius2, originalTwo);
      popMatrix();
    }
    if (pitch > 200) {
      pushMatrix();
      rotate(radians(rotateBy));
      rosettePartTwo(radius3, originalTwo);
      popMatrix();
    }
    if (pitch > 225) {
      pushMatrix();
      rotate(radians(-rotateBy));
      rosettePartTwo(radius4, originalTwo);
      popMatrix();
    }
    if (pitch > 255) {
      pushMatrix();
      rotate(radians(rotateBy));
      rosettePartOne(radius5, originalOne);
      popMatrix();
    }
    if (pitch > 325) {
      pushMatrix();
      rotate(radians(-rotateBy));
      rosettePartOne(radius6, originalOne);
      popMatrix();
    }

/*
// Do we actually want rotation here?  If so, we'll have to give it tenorCutoff.
// ^ No, we do this in RosetteV3Rotate.run() (in the SceneRosetteV3Rotate tab).
    if (input.getAverageFund(this.tenorCutoff, input.numInputs) > 3) {
      this.rotateBy = (this.rotateBy + (input.getAverageFund(this.tenorCutoff, input.numInputs) / 400)) % 360;
    }
    */
  } // run()
}// RosetteV3 