/**
 *  Emily Meuer
 *  08/08/2016
 *  
 *  Template for shapes that will respond to sound.
 *
 *  Using Interfascia for GUI; documentation here: http://interfascia.berg.industries/documentation/.
 *
 *  NB:
 *    - although Processing allows it, the Shape's centerX and centerY will stay within the sketch boundaries.
 *
 *  TODO:
 *    - uneven shapes?
 *
 *  So... what if I want to have one xHigh and xLow for the whole sketch, and potentially have
 *  multiple shapes in a sketch?
 *  That sounds like something that should happen in the driver.  I could have a "startButtons" method;
 *  don't call it more than once in a sketch -- bad practice, but okay for testing for now.
 */
 
class SoundShape
{
  float[]    pitchRange  = { 50, 800 };
  float[]    ampRange    = { 20, 800 };
  float[][]  ranges       = { pitchRange, ampRange };
  
  float  centerX;      // x coordinate of the shape's center
  float  centerY;      // y coordinate of the shape's center
  float  radius;       // the shape's radius
  int    numPoints;    // the number of points a shape has
  int    moveDelta;    // the speed with which the shape moves
  int    controlsX;    // 0 if pitch controls x axis, 1 if amplitude.
  int    controlsY;    // 0 if pitch controls y axis, 1 if amplitude.
  float    xLow, xHigh;  // the low and high bounds of the x axis.
  float    yLow, yHigh;  // the low and high bounds of the x axis.
  
  /*
    Set xLow or set this.ranges?  The point of this.ranges was to use them to set xLow/High,
    so I could continue that, having the user set them and then deriving xLow and xHigh from that.
    
    Or I could jsut go in and set xLow and xHigh themselves.
    They will no longer sync with ranges...
    
    No, I think that I want to change the ranges!
  */

  /**
   *  Constructor;  error checking automatically corrects centerX and centerY if out of sketch bounds.
   */
  SoundShape(float centerX, float centerY, float radius, int numPoints)
  {
    this(centerX, centerY, radius, numPoints, 0, 1);
  } // constructor(float, float, float, int)
  
  SoundShape(float centerX, float centerY, float radius, int numPoints, int controlsX, int controlsY)
  {
    if(centerX < 0) {
      centerX  = 0;
//      throw new IllegalArgumentException("SoundShape.constructor: centerX parameter is " + centerX + "; must be between 0 and " + width);
    }
    if(centerX > width) {
      centerX  = width;
//      throw new IllegalArgumentException("SoundShape.constructor: centerX parameter is " + centerX + "; must be between 0 and " + width);
    }
    if(centerY < 0) {
      centerY  = 0;
//      throw new IllegalArgumentException("SoundShape.constructor: centerX parameter is " + centerY + "; must be between 0 and " + height);
    }
    if(centerY > height) {
      centerY  = height;
//      throw new IllegalArgumentException("SoundShape.constructor: centerX parameter is " + centerY + "; must be between 0 and " + height);
    }
    if(radius < 0) {
      throw new IllegalArgumentException("SoundShape.constructor: radius is " + radius + "; must be greater than or equal to 0.");
    }
    if(numPoints < 0) {
      throw new IllegalArgumentException("SoundShape.constructor: numPoints is " + numPoints + "; must be greater than or equal to 0.");
    }
    
    this.centerX    = centerX;
    this.centerY    = centerY;
    this.radius     = radius;
    this.numPoints  = numPoints;
    
    this.controlsX  = controlsX;
    this.controlsY  = controlsY;
    
    this.xLow       = this.ranges[controlsX][0];
    this.xHigh      = this.ranges[controlsX][1];
    
    this.yLow       = this.ranges[controlsY][0];
    this.yHigh      = this.ranges[controlsY][1];
    
    println("this.xLow = " + this.xLow + "; this.xHigh = " + this.xHigh);
    println("this.yLow = " + this.yLow + "; this.yHigh = " + this.yHigh);
    
    this.moveDelta  = 3;  // how quickly the shape moves in response to stimuli;
                          // can be adjusted by moving the slider.
    
    this.drawShape();
  } // constructor(float, float, float, int, int, int)
  
  /**
   *  Positions the shape within the sketch with location based on pitch, amplitude, or both.
   */
  void positionShape()
  {
    float x;
    float y;
    
    if(this.controlsX == 0) {  x = input.getAdjustedFundAsHz(1);  }
    else                    {  x = input.getAmplitude(1);         }
    
    if(this.controlsY == 0) {  y = input.getAdjustedFundAsHz(1);  }
    else                    {  y = input.getAmplitude(1);         }
    
    // converts the pitch or amplitude values to a position within the sketch:
    x  = map(x, this.ranges[controlsX][0], this.ranges[controlsX][1], 0, width);
    y  = map(y, this.ranges[controlsY][0], this.ranges[controlsY][1], height - 200, 150);
    
    this.moveTo(x, y);
  } // positionShape

  /**
   *  Draws a shape at centerX and centerY with numPoints points,
   *  each one radius distance from (centerX, centerY).
   */
  void drawShape()
  {
    float angle = TWO_PI / this.numPoints;
    beginShape();
    for (float a = 0; a < TWO_PI; a += angle) 
    {
      float sx = this.centerX + cos(a) * this.radius;
      float sy = this.centerY + sin(a) * this.radius;
      vertex(sx, sy);
    } // for
    endShape(CLOSE);
  } // drawShape
  
  /**
   *  Moves the shape to the coordinates specified as parameters.
   */
  void moveTo(float xCoordinate, float yCoordinate)
  {
    if(xCoordinate < 0)      {  xCoordinate  = 0;  }
    if(xCoordinate > width)  {  xCoordinate  = width;  }
    
    if(yCoordinate < 0)      {  yCoordinate  = 0;  }
    if(yCoordinate > height)  {  yCoordinate  = height;  }
    
    int  delta  = 3;
    
    while(xCoordinate > (this.centerX + delta) || xCoordinate < (this.centerX- delta))
    {
      if(this.centerX > (xCoordinate + delta)) {
        this.centerX = this.centerX - delta;
      } else if(centerX < (xCoordinate - delta)) {
        this.centerX = this.centerX + delta;
      }
    } // while - x
    
    while(yCoordinate > (this.centerY + delta) || yCoordinate < (this.centerY - delta))
    {
      if(this.centerY > (yCoordinate + delta)) {
        this.centerY = this.centerY - delta;
      } else if(this.centerY < (yCoordinate - delta)) {
        this.centerY = this.centerY + delta;
      }
    } // while - y
    
    this.drawShape();
  } // moveTo(int,int)
  
  /**
   *  Sets xLow by changing the first value in either pitchRange or ampRange,
   *  whichever one is on the x axis.
   */
  void setXLow(int xLow)
  {
    this.ranges[controlsX][0]  = xLow;
  } // setXLow

  /**
   *  Method from an example; draws a polygon at x and y, with the given radius and num points.
   *  (I'm basing drawShape() on it.)
   */
  void polygon(float x, float y, float radius, int npoints) {
    float angle = TWO_PI / npoints;
    beginShape();
    for (float a = 0; a < TWO_PI; a += angle) {
      float sx = x + cos(a) * radius;
      float sy = y + sin(a) * radius;
      vertex(sx, sy);
    }
    endShape(CLOSE);
  } // polygon(float, float, float, int)
} // class