/**
 *  Emily Meuer
 *  08/08/2016
 *  
 *  Template for shapes that will respond to sound.
 *
 *  TODO:
 *    - uneven shapes?
 */
class SoundShape
{
  int  centerX;      // x coordinate of the shape's center
  int  centerY;      // y coordinate of the shape's center
  int  radius;       // the shape's radius
  int  numPoints;    // the number of points a shape has

  /**
   *  Constructor; allows children to call super to simplify their constructor.
   */
  public SoundShape(int centerX, int centerY, int radius, int numPoints)
  {
    this.centerX    = centerX;
    this.centerY    = centerY;
    this.radius     = radius;
    this.numPoints  = numPoints;
    
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
  
  void moveTo(int xCoordinate, int yCoordinate)
  {
    int  delta  = 7;
    
    if(this.centerX > (xCoordinate + delta)) {
      this.centerX = this.centerX - delta;
    } else if(centerX < (xCoordinate - delta)) {
      this.centerX = this.centerX + delta;
    }
    
    if(this.centerY > (yCoordinate + delta)) {
      this.centerY = this.centerY - delta;
    } else if(this.centerY < (yCoordinate - delta)) {
      this.centerY = this.centerY + delta;
    }
    
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