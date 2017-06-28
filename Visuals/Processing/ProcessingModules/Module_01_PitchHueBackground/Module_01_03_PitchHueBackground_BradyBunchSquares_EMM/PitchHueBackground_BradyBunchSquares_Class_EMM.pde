class PitchHueSquares extends BradyBunchSquares //<>// //<>//
{
  /*
    Emily Meuer
    09/27/2016
    
    Class to make a version of module01 (PitchHueBackground)
    in which each of up to 9 inputs each have their own color-changing square.
    
    09/29 issues:
     - I'm not even sure.  Doesn't seem to be getting notes, but the hardware is definitely hearing sound!
     
     To fix it: let's go back to simple; just draw white when it hears ... anything.
  */

  float[]  curNote;
  float[]  newNote;

  float[][]  newHue;
  float[][]  goalHue;
  float[][]  curHue;
  float[]    attackTime;

  int[]  newHuePos;
  int[]  goalHuePos;
  int[]  curHuePos;

  float[][] colors  = new float[][] {
    { 255, 0, 0 }, 
    { 255, 127.5, 0 }, 
    { 255, 255, 0 }, 
    { 127.5, 255, 0 }, 
    { 0, 255, 0 }, 
    { 0, 255, 127.5 }, 
    { 0, 255, 255 }, 
    { 0, 127.5, 255 }, 
    { 0, 0, 255 }, 
    { 127.5, 0, 255 }, 
    { 255, 0, 255 }, 
    { 255, 0, 127.5 }, 
    { 255, 0, 0 }
  };

  PitchHueSquares(int numInputs) {
    super(numInputs);
    
    println("PitchHueSquares.constructor: numInputs = " + numInputs);
    println("PitchHueSquares.constructor: numSquares = " + this.numSquares);

    this.curNote  = new float[numInputs];
    this.newNote  = new float[numInputs];

    this.newHue  = new float[numInputs][3];
    this.goalHue  = new float[numInputs][3];
    this.curHue  = new float[numInputs][3];
    this.attackTime  = new float[numInputs];

    this.newHuePos  = new int[numInputs];
    this.goalHuePos  = new int[numInputs];
    this.curHuePos  = new int[numInputs];

    for (int i = 0; i < numInputs; i++)
    {
      curHuePos[i]    = round(input.getAdjustedFundAsMidiNote(i + 1) % 12); //<>//
      println("curHuePos[" + i + "] = " + curHuePos[i]);
      curHue[i]       = colors[curHuePos[i]]; //<>//
      // would like to change more quickly, but there's a weird flicker if attackTime gets bigger:
      attackTime[i]  = 10;
    }
  } // constructor

  void run()
  {
    for (int i = 1; i <= phSquares.numSquares; i++)
    {
      this.run(i);
    } // for
  } // run()

  void run(int inputNum)
  {
    if ((inputNum > this.numSquares) || (inputNum < 1)) {
      throw new IllegalArgumentException("PitchHueSquares.run(int): inputNum " + inputNum + " is out of bounds; should be between 1 and " + this.numSquares);
    } // error checking

    // Numbers sent in range from 1-9, but we need 0-8 to access the arrays.
    int arrayInputNum  = inputNum - 1;

    int  xPos  = this.squarePos[arrayInputNum][0];
    int  yPos  = this.squarePos[arrayInputNum][1];

    /*
    stroke(255);
     fill(inputNum * 20, 100, 255 - (inputNum * 20));
     rect(xPos, yPos, this.squareSize[0], this.squareSize[1]);
     //    println("xPos = " + xPos + "; yPos = " + yPos + "; this.squareSize[0] = " + this.squareSize[0] + " ; this.squareSize[1] = " + this.squareSize[1]);
     */
     
     
//        println("curNote[" + arrayInputNum + "] = " + curNote[arrayInputNum]);

// Get rid of the one line comments to go to black w/silence
//    if (input.getAmplitude(inputNum) > threshold)
//    {
      /*
      newNote[arrayInputNum] = input.getAdjustedFundAsMidiNote(inputNum);
      if (newNote[arrayInputNum] == curNote[arrayInputNum]) {
        //      println("  Note was held.");
      } else {
        //      println("New note! curNote = " + curNote + "; newNote = " + newNote);
        curNote[arrayInputNum] = newNote[arrayInputNum];
      } // else - set curNote
*/
        curNote[arrayInputNum] = input.getAdjustedFundAsMidiNote(inputNum);
        println("curNote[" + arrayInputNum + "] = " + curNote[arrayInputNum]);

      newHuePos[arrayInputNum]  = round(curNote[arrayInputNum] % 12);
      newHue[arrayInputNum]  = colors[newHuePos[arrayInputNum]];
      //  newHue  = newHue * 30;  // this is for HSB, when newHue is the color's H value
//    } else {
//      newHue[arrayInputNum]  = new float[] { 0, 0, 0 };
//    } // else
    
//    println("inputNum = " + inputNum + "; Did we make it here?");

    // set goalHue to the color indicated by the current pitch:
    if (newHuePos[arrayInputNum] != goalHuePos[arrayInputNum])
    {
      goalHuePos[arrayInputNum]  = newHuePos[arrayInputNum];
    } // if
    goalHue[arrayInputNum]  = colors[goalHuePos[arrayInputNum]];

    this.blur(curHue[arrayInputNum], goalHue[arrayInputNum], attackTime[arrayInputNum]);

    fill(curHue[arrayInputNum][0], curHue[arrayInputNum][1], curHue[arrayInputNum][2]);
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

  void blur(float[] curHueArray, float[] goalHueArray, float attackTime)
  {
    for (int i = 0; i < 3; i++)
    {
      if (curHueArray[i] > (goalHueArray[i] - attackTime))
      {
        curHueArray[i] = curHueArray[i] - attackTime;
//        println("curHueArray[" + i + "] = " + curHueArray[i]);
      } else if (curHueArray[i] < (goalHueArray[i] + attackTime))
      {
        curHueArray[i]  = curHueArray[i] + attackTime;
      } // if
    } // for
  } // blur
} // class