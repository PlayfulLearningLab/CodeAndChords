class GameOfLife extends Scene
{
  /* //<>//
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

  color liveColor;
  color deadColor;

  int      tileSize;
  int[][]  newboard;
  int[][]  oldboard;
  int i=0;
  int j=0;

  float  red;
  float  green  = 50;
  float  blue;

  String  seed;
  int     waitUntil;
  int     genCount;

  GameOfLife(Input rightInput, int tenorCutoff, String seed)
  {
    this.input   = rightInput;
    this.tenorCutoff  = tenorCutoff;
    this.seed         = seed;

    this.tileSize = width/10;
    
    // It's fine until j = 89.
    println("height = " + height);
    
    /*
    this.newboard = new int[height/10][tileSize];
    this.oldboard = new int[height/10][tileSize];
    */

    this.newboard = new int[tileSize][height/10];
    this.oldboard = new int[tileSize][height/10];

    setLiveColor();
    this.deadColor  = color(255);

    this.genCount   = 0;
    this.waitUntil  = millis();
  } // constructor(Input, int, String)

  GameOfLife(Input rightInput, int tenorCutoff)
  {
    this(rightInput, tenorCutoff, "ring");
  } // constructor(Input, int)

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

    if (keyPressed && millis() > this.waitUntil)
    {
      nextGeneration();
      this.waitUntil += 100;
    } // keyPressed
  }// run()

  void setLiveColor()
  {
    //pitchColor();      // method inherited from Scene that sets red and blue depending on pitches of high and low inputs.
 //   green = (green + 30) % 255;
    //liveColor  = color(red, green, blue);
    liveColor = color(153,49,245);  //royal purple
  } // setLiveColor

  color getLiveColor() {  
    return liveColor;
  }

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

        else if (get(i*10+5, j*10+5) != deadColor)  //dealing with the living
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

  void seedSquare()
  {
    for (i=0; i<oldboard.length; i++)
    {
      for (j=0; j<oldboard[i].length; j++)
      {
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

  void seedRing()
  {
    for (i=0; i<oldboard.length; i++)
    {
      for (j=0; j<oldboard[i].length; j++)
      {
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

  void seedRPentomino()
  {
    for (i=0; i<oldboard.length; i++)
    {
      for (j=0; j<oldboard[i].length; j++)
      {
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
} // class