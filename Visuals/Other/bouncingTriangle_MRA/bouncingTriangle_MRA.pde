/*
  Michaela Andrews
 07/15/2016
 
 Jumping Triangle
 */
PShape triangle;
float gravity = 1;
float xSpeed, ySpeed;
float x, y;

void setup()
{
  size(1000, 600);
  triangle = createShape(TRIANGLE, 10, 0, 0, 30, 25, 25);
  x = 0;
  y = height;
  xSpeed = 5;
} // setup()

void draw()
{
  background(70, 0, 100);
  shape(triangle, x, y);
  x = x + xSpeed;
  y = y + ySpeed;
  ySpeed += gravity;
  if (y == height) {
    ySpeed = -30;
  }
  if (x > width || x < 0) {
    xSpeed = -xSpeed;
  }
} // draw()


//could try completing this with this polygon code instead
/*
class Polygon {
 float sx, sy, radius, npoints, angle;
 color c;
 Polygon(float inRadius, int inPoints, color inColor){
 c = inColor;
 radius = inRadius;
 npoints = inPoints;
 angle = TWO_PI / npoints;
 }
 void show(float x, float y) {  
 beginShape();
 fill(c);
 stroke(150);
 strokeWeight(5);
 for (float a = 0; a < TWO_PI; a += angle) {
 sx = x + cos(a) * radius;
 sy = y + sin(a) * radius;
 vertex(sx, sy);
 }
 endShape(CLOSE);
 }
 }
 */