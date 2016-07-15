/*
  07/15/2016
  Michaela Andrews
  
  trying out P3D with PShapes... can't get it to work yet...
*/


PShape triangle;
float x, y, z;

void setup() {
  size(400, 300, P3D);
  triangle = createShape(TRIANGLE, 10, 0, 0, 0, 30, 0, 25, 25, 0);
  x = width/2;
  y = height/2;
  z = 0;
} // setup()

void draw() {
  background(50,0,100);
  translate(x, y, z);
  shape(triangle, x, y);
  z++;
} // draw()