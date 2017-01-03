class GameOfLife extends Scene //<>//
{
  /*
   07/04/2016
   Emily Meuer
   Turning GameOfLife_ColorChange_EMM into a Scene class.
   
   From GameOfLife_ColorChange_EMM:
   * 07/03/2016
   * Emily Meuer
   *  Colors change each generation, based on LR inputs.
   *
   * Amanda Tenhoff
   * Emily Meuer
   * Game of Life
   * 6/26/2016
   * translation from JavaScript to Processing
   */

  color liveColor;    // color of live squares; currently purple.
  color deadColor;    // color of dead squares; currently white.

  int      tileSize;    //  the board is this many tiles wide.
  int[][]  newboard;    //  holds the values for the generation being currently calculated.
  int[][]  oldboard;    //  holds the values for the previous generation that will determine the values in newboard.
  int i=0;    // counts through new and oldboard.
  int j=0;    // counts through new and oldboard.

  float  red;
  float  green  = 50;
  float  blue;

  String  seed;        // specifies whether the board will seed with a "ring", "square", or "rPentomino". 
  int     waitUntil;   // keeps a long press from counting as many consecutive pressed.
  int     genCount;    // keeps track of the number of generations.

  /**
   *  Constructor; creates a GameOfLife with the given Input, tenorCutoff, and seed.
   *
   *  @param  input        an Input, previously used to control the color but not currently doing anything.
   *  @param  tenorCutoff  an int at which the mics divide into basses (below) and tenors (this and all above).
   *  @param  seed         a String specifying whether the board will seed with a "ring", "square", or "rPentomino".  
   */
  GameOfLife(Input input, int tenorCutoff, String seed)
  {
    this.input        = input;
    this.tenorCutoff  = tenorCutoff;
    this.seed         = seed;

    this.tileSize = width/10;

    // It's fine until j = 89.
    println("GameOfLife.constructor: height = " + height);

    /*
    this.newboard = new int[height/10][tileSize];
     this.oldboard = new int[height/10][tileSize];
     */

    this.newboard = new int[tileSize][height/10];
    this.oldboard = new int[tileSize][height/10];

    setLiveColor();
    this.deadColor  = color(0);

    this.genCount   = 0;
    this.waitUntil  = millis();
  } // constructor(Input, int, String)

  /**
   *  Constructor that creates a GameOfLife with the given Input and tenorCutoff and seeds with "ring".
   *
   *  @param  input        an Input, previously used to control the color but not currently doing anything.
   *  @param  tenorCutoff  an int at which the mics divide into basses (below) and tenors (this and all above).
   */
  GameOfLife(Input input, int tenorCutoff)
  {
    this(input, tenorCutoff, "ring");
  } // constructor(Input, int)

  /**
   *  Called in draw() in the Zikr_Scenes_Inputs1_9_EMM tab;
   *  if this is the first generation, it seeds the board (seed specified in constructor),
   *  else it creates the next generation on mouse press.
   */
  void run()
  {
    strokeWeight(1);

    if (genCount == 0)
    {
      if (seed == "ring") {
        this.seedRing();
      } else if (seed == "square") {
        this.seedSquare();
      } else if (seed == "rPentomino") {
        this.seedRPentomino();
      } else {
        throw new IllegalArgumentException("GameOfLife.constructor: seed " + seed + " is not recognized; please use either \"ring\", \"square\", or \"rPentomino\".");
      } // seed if's
    } // genCount == 0

    if (mousePressed && millis() > this.waitUntil)
    {
      nextGeneration();
      this.waitUntil += 100;
    } // mousePressed
  }// run()

  /**
   *  Setter for the liveColor instance variable;
   *  can be modified to use pitchColor to determine the color.
   */
  void setLiveColor()
  {
    //pitchColor();      // method inherited from Scene that sets red and blue depending on pitches of high and low inputs.
    //   green = (green + 30) % 255;
    //liveColor  = color(red, green, blue);
//    liveColor = color(153, 49, 245);  //royal purple
    liveColor = color(230, 230, 0);
  } // setLiveColor

  /**
   *  Getter for the liveColor instance variable.
   */
  color getLiveColor() {  
    return liveColor;
  }

  /**
   *  Loops through oldboard and determines the value of the corresponding cell in newboard
   *  based on how many dead and alive neighbors each oldboard cell has.
   */
  void nextGeneration()
  {
    setLiveColor();

    for (i=0; i<oldboard.length; i++)
    {
      for (j=0; j<oldboard[i].length; j++)
      {
        int deadCount = getDeadCount(i, j);
        int liveCount = getAliveCount(i, j);

        if (oldboard[i][j] == 0)              //dealing with the dead :o
        {
          if (deadCount==3)
          {
            newboard[i][j] = 1;
          }//if dead
          else
          {
            newboard[i][j] = 0;
          }//else dead
        }//if get

        if (oldboard[i][j] == 1)              //dealing with the living
        {
          if (liveCount<2 || liveCount>3)
          {
            newboard[i][j] = 0;
          } else if (liveCount>=2 && liveCount<4)
          {
            newboard[i][j] = 1;
          }
        }//else if alive
      }//for j
    }//for i


    for (i=0; i<oldboard.length; i++)
    {
      for (j=0; j<oldboard[i].length; j++)
      {
        //      setLiveColor();
        color curLiveColor  = getLiveColor();

        //  resets oldboard to the values in newboard (since newboard is now the oldboard for the next generation),
        //  and draws the squares corresponding to each array position / cell.
        if (newboard[i][j]==0)
        {
          oldboard[i][j]=0;
          stroke(0);
          fill(deadColor);
          rect(i*10, j*10, 10, 10);
        } else
        {
          oldboard[i][j]=1;
          stroke(0);
          fill(curLiveColor);
          rect(i*10, j*10, 10, 10);
        }
      }
    }

    this.genCount++;
  } // nextGeneration()

  /**
   *  Seeds the board with a square.
   */
  void seedSquare()
  {
    for (i=0; i<oldboard.length; i++)
    {
      for (j=0; j<oldboard[i].length; j++)
      {
        //   sets the middle 9 cells to alive and draws their squares in livecolor.
        if (i>=(oldboard.length)/2-1 && (i<=(oldboard.length)/2+1) &&(j<=(oldboard[i].length)/2+1) && (j>=(oldboard[i].length)/2-1))
        {
          stroke(0);
          fill(liveColor);
          rect(i*10, j*10, 10, 10);
          oldboard[i][j]  = 1;
        } else
        {
          stroke(0);
          fill(deadColor);
          rect(i*10, j*10, 10, 10);
          oldboard[i][j]  = 0;
        }
      } // for - j
    }// for - i
  } // seedSquare

  /**
   *  Seeds the board with a ring.
   */
  void seedRing()
  {
    for (i=0; i<oldboard.length; i++)
    {
      for (j=0; j<oldboard[i].length; j++)
      {
        //   sets the 8 cells around the center to alive and draws their squares in livecolor.
        if ((i==(oldboard.length)/2-1 && (j<=(oldboard[i].length)/2+1) && 
          (j>=(oldboard[i].length)/2-1)) || ((i==(oldboard.length)/2+1) && 
          (j<=(oldboard[i].length)/2+1) && (j>=(oldboard[i].length)/2-1)))
        {
          stroke(0);
          fill(liveColor);
          rect(i*10, j*10, 10, 10);
          oldboard[i][j]  = 1;
        } else if ((i==(oldboard.length)/2 && j==(oldboard[i].length)/2+1) || (i==(oldboard.length)/2 && j==(oldboard[i].length)/2-1))
        {
          stroke(0);
          fill(liveColor);
          rect(i*10, j*10, 10, 10);
          oldboard[i][j]  = 1;
        } else
        {
          stroke(0);
          fill(deadColor);
          rect(i*10, j*10, 10, 10);
          oldboard[i][j]  = 0;
        }
      } // for - j
    }// for - i
  } // seedRing

  /**
   *  Seeds the board with an r-pentomino shape.
   */
  void seedRPentomino()
  {
    for (i=0; i<oldboard.length; i++)
    {
      for (j=0; j<oldboard[i].length; j++)
      {
        //   sets the r-pentomino positions in the arrays to alive and draws their squares in livecolor.
        if (i==(oldboard.length)/2 && (j>=(oldboard[i].length)/2-1) &&(j<=(oldboard[i].length)/2+1))
        {
          stroke(0);
          fill(liveColor);
          rect(i*10, j*10, 10, 10);
          oldboard[i][j]  = 1;
        } else if (j==(oldboard[i].length)/2-1 && i==(oldboard.length)/2+1)
        {
          stroke(0);
          fill(liveColor);
          rect(i*10, j*10, 10, 10);
          oldboard[i][j]  = 1;
        } else if (j==(oldboard.length)/2 && i==(oldboard.length)/2-1)
        {
          stroke(0);
          fill(liveColor);
          rect(i*10, j*10, 10, 10);
          oldboard[i][j]  = 1;
        } else
        {
          stroke(0);
          fill(deadColor);
          rect(i*10, j*10, 10, 10);
          oldboard[i][j]  = 0;
        }
      } // for - j
    }// for - i (drawing the initial board, R-pentomino)
  } // seedRPentomino

  /**
   *  Counts how many dead neighbors a cell at a particular location has.
   *  
   *  @param  i  an int specifying the cell's location in the first dimension of the array.
   *  @param  j  an int specifying the cell's location in the second dimension of the array.
   *
   *  @return  int  the number of dead cells that surround the cell in question.
   */
  int getDeadCount(int i, int j)
  {
    int dead=0;
    int neigh, bors;
    for (neigh=i-1; neigh<i+2; neigh++)
    {
      for (bors=j-1; bors<j+2; bors++)
      {
        if ( neigh>=0 && bors>=0 && 
          neigh < oldboard.length && bors < oldboard[neigh].length &&
          (oldboard[neigh][bors] == 0) && neigh!=i && bors!=j)
        {
          dead++;
        }
      }
    }
    return dead;
  }//dead count

  /**
   *  Counts how many living neighbors a cell at a particular location has.
   *  
   *  @param  i  an int specifying the cell's location in the first dimension of the array.
   *  @param  j  an int specifying the cell's location in the second dimension of the array.
   *
   *  @return  int  the number of living cells that surround the cell in question.
   */
  int getAliveCount(int i, int j)
  {
    int alive=0;
    int neigh, bors;
    for (neigh=i-1; neigh<i+2; neigh++)
    {
      for (bors=j-1; bors<j+2; bors++)
      {
        if ( neigh>=0 && bors>=0 && 
          neigh < oldboard.length && bors < oldboard[neigh].length &&
          (oldboard[neigh][bors] == 0) && neigh!=i && bors!=j)
        {
          alive++;
        }
      }
    }
    return alive;
  }//live count
  
  /**
   *  Sets genCount to 0 and the background to black.
   */
  void reset()
  {
    this.genCount = 0;
    background(0);
  } // reset
} // class