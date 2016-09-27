class PitchHueSquares extends BradyBunchSquares
{
  PitchHueSquares(int numInputs) {
    super(numInputs);
  } // constructor
  
  void run(int inputNum)
  {
    if((inputNum > this.squarePos.length) || (inputNum < 1))  {
      throw new IllegalArgumentException("PitchHueSquares.run(int): inputNum " + inputNum + " is out of bounds; should be between 1 and " + this.numSquares);
    }
    int  xPos  = this.squarePos[inputNum - 1][0];
    int  yPos  = this.squarePos[inputNum - 1][1];
    
    fill(inputNum * 10, 100, 255 - (inputNum * 10));
    rect(xPos, yPos, this.squareSize[0], this.squareSize[1]);
    println("xPos = " + xPos + "; yPos = " + yPos + "; this.squareSize[0] = " + this.squareSize[0] + " ; this.squareSize[1] = " + this.squareSize[1]);
  } // run
} // class