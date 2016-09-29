class PitchHueSquares extends BradyBunchSquares
{
  PitchHueSquares(int numInputs) {
    super(numInputs);
  } // constructor

  void run(int inputNum)
  {
    if ((inputNum > this.squarePos.length) || (inputNum < 1)) {
      throw new IllegalArgumentException("PitchHueSquares.run(int): inputNum " + inputNum + " is out of bounds; should be between 1 and " + this.numSquares);
    }
    int  xPos  = this.squarePos[inputNum - 1][0];
    int  yPos  = this.squarePos[inputNum - 1][1];


    /*
    stroke(255);
     fill(inputNum * 20, 100, 255 - (inputNum * 20));
     rect(xPos, yPos, this.squareSize[0], this.squareSize[1]);
     //    println("xPos = " + xPos + "; yPos = " + yPos + "; this.squareSize[0] = " + this.squareSize[0] + " ; this.squareSize[1] = " + this.squareSize[1]);
     */
     
     if (input.getAmplitude() > threshold)
     {
     newNote = input.getAdjustedFundAsMidiNote();
     if(newNote == curNote) {
     //      println("  Note was held.");
     } else {
     //      println("New note! curNote = " + curNote + "; newNote = " + newNote);
     curNote = newNote;
     } // else - set curNote
     
     // The following finds the color from the particular note.
     // if want something other than C to be the "tonic" (i.e., red),
     // add some number before multiplying.
     newHuePos  = round(curNote % 12);
     newHue  = colors[newHuePos];
     //  newHue  = newHue * 30;  // this is for HSB, when newHue is the color's H value
     } else {
     newHue  = new float[] { 0, 0, 0 };
     } // else
     
     // set goalHue to the color indicated by the current pitch:
     if (newHuePos != goalHuePos)
     {
     goalHuePos  = newHuePos;
     } // if
     goalHue  = colors[goalHuePos];
     
     this.blur(curHue, goalHue, hueDelta);
     
     fill(curHue[0], curHue[1], curHue[2]);
     rect(this.squarePos[inputNum - 1][0], this.squarePos[inputNum - 1][1], this.squareSize[0], this.squareSize[1]);
     
     // Adjust saturation with amplitude by uncommenting the following lines and 
     // commenting the "curSaturation = saturationMax" line.
     // ** This no longer works because of RGB! :(
     // ^ Chillax and change the color mode.
    /*
     newSaturation  = (int)Math.min(input.getAmplitude(1), 300);
     println("input.getAmplitude(1) = " + input.getAmplitude(1));
     
     if(newSaturation != goalSaturation) {
     goalSaturation  = newSaturation;
     } //if
     
     if(curSaturation > goalSaturation) {
     curSaturation = curSaturation - changeInSaturation;
     } else if(curSaturation < goalSaturation) {
     curSaturation = curSaturation + changeInSaturation;
     } // if
     */

    curSaturation  = saturationMax;
  } // run

  void blur(float[] curHueArray, float[] goalHueArray, float hueDelta)
  {
    for (int i = 0; i < 3; i++)
    {
      if (curHueArray[i] > (goalHueArray[i] - hueDelta))
      {
        curHueArray[i] = curHueArray[i] - hueDelta;
        println("curHueArray[" + i + "] = " + curHueArray[i]);
      } else if (curHueArray[i] < (goalHueArray[i] + hueDelta)) //<>//
      {
        curHueArray[i]  = curHueArray[i] + hueDelta; //<>//
      } // if
    } // for
  } // blur
} // class