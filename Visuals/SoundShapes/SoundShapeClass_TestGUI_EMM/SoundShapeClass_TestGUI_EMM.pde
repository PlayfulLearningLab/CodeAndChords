/**
 *  Emily Meuer
 *  08/08/2016
 *  
 *  Template for shapes that will respond to sound.
 *
 *  NB:
 *    - although Processing allows it, the Shape's centerX and centerY will stay within the sketch boundaries.
 *
 *  TODO:
 *    - uneven shapes?
 */
class SoundShape
{
  float  centerX;      // x coordinate of the shape's center
  float  centerY;      // y coordinate of the shape's center
  float  radius;       // the shape's radius
  int  numPoints;    // the number of points a shape has
  int    moveDelta;

  /**
   *  Constructor;  error checking automatically corrects centerX and centerY if out of sketch bounds.
   */
  SoundShape(float centerX, float centerY, float radius, int numPoints)
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
    
    this.moveDelta  = 3;  // how quickly the shape moves in response to stimuli;
                          // can be adjusted by moving the slider.
    
    this.drawShape();
  } //

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