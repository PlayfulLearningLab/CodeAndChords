abstract class BradyBunchSquares
{
  /*
    Emily Meuer
   09/26/2016
   
   Interface for creating a "Brady Bunch" square, 
   with the function of choice in each square.
   */

  int      numSquares;
  int[][]  squarePos;       // holds the x and y positions of each square.
  int[]    squareSize;      // [0] is the width and [1] the height of each square.

  BradyBunchSquares(int numInputs)
  {
    int  xLevel;
    int  yLevel;

    this.numSquares = numInputs;

    if (this.numSquares < 3) {
      xLevel           = width % 2;
      yLevel           = 1;
      this.squareSize  = new int[] {  
        (width / numInputs), 
        height  };
      this.squarePos   = new int[this.numSquares][2];
    } else if (this.numSquares < 5) {
      xLevel          = width % 2;
      yLevel          = height / 2;
      this.squareSize  = new int[] {  
        (width / 2), 
        (height / 2) };
      this.squarePos  = new int[4][2];
    } else if (this.numSquares < 9) {
      xLevel          = width % 4;
      yLevel          = height / 2;
      this.squareSize  = new int[] {  
        (width / 4), 
        (height / 2) };
      this.squarePos  = new int[8][2];
    } else if (this.numSquares == 9) {
      xLevel          = width % 3;
      yLevel          = height / 3;
      this.squareSize  = new int[] {  
        (width / 3), 
        (height / 3) };
      this.squarePos  = new int[9][2];
    } else {
      throw new IllegalArgumentException("BradyBunchSquares.constructor: numInputs is = " + numInputs + "; must be less than or equal to 9.");
    } // else

    for (int i = 0; i < this.squarePos.length; i++)
    {
      this.squarePos[i][0]  = xLevel * this.squareSize[0];
      this.squarePos[i][1]  = yLevel * this.squareSize[1];
    } // for - i

    /*
      // setting values that will let me do this in a loop:
     this.squarePos  = new int[4][2];
     this.squarePos[1][0]  = width / 2;
     this.squarePos[1][1]  = 0;
     this.squarePos[2][0]  = 0;
     this.squarePos[2][1]  = height / 2;
     this.squarePos[3][0]  = width / 2;
     this.squarePos[3][1]  = height / 2;
     */
  } // constructor

  void run(int inputNum) {
  }    // Each instance must have a run() method that takes an input.
  void run() {
  }    // This run() will do the same thing for each square.
} // BradyBunchSquares class