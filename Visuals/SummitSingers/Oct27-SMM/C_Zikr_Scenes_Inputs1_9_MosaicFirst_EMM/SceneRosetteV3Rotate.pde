class RosetteV3Rotate extends RosetteV3Colors
{
  /*
    06/29/2016
   Emily Meuer
   
   Class that shows rosettes based on the pitch of one input
   and rotates the rosettes based on the pitch of another input.
   */
   
   // Calibrate:
   int  amp  = 3;  // pitches with amplitudes below this will not change rotation.
   int  changeInRotation  = 400;  // higher number = slower rotation
                                  // (pitch is divided by this number, and the resulting decimal value is added to rotateBy).

  /**
   *  Constructor; makes a RosetteV3Rotate with the given Input and tenorCutoff.
   *
   *  @param  input        an Input, used to get bass and tenor pitch and amp.
   *  @param  tenorCutoff  an int at which the mics divide into basses (below) and tenors (this and all above).
   */
  RosetteV3Rotate(Input input, int tenorCutoff)
  {
    super(input, tenorCutoff);
    
    this.tenorCutoff  = tenorCutoff;
  } // RosetteV3Rotate

  /**
   *  Called in draw() in the Zikr_Scenes_Inputs1_9_EMM tab;
   *  calculates rotateBy based on the average tenor pitch and calls RosetteV3Colors.run().
   */
  void run()
  {
    if (input.getAverageAmp(1, input.getNumInputs()) > amp) {
      super.rotateBy = (super.rotateBy + (input.getAverageFund(1, input.getNumInputs()) / changeInRotation)) % 360;
    }
    
    super.run();
  } // run()
}// RosetteV3 