/*
  07/15/2016
  Michaela Andrews
  
  trying out P3D
  still not sure how to use PShape in P3D
*/

void setup() {
  size(640, 360, P3D);
}

void draw() {
  background(0);
  lights();
  //scale(0.5);
  translate(width/2, height/2, 0);
  stroke(255);
  fill(50, 0, 100);
  //rotateX(PI/2);
  //rotateZ(-PI/6);
  rotateY(radians(mouseY));
  
  beginShape();
    vertex(-100, -100, -100);
    vertex( 100, -100, -100);
    vertex(   0,    0,  100);
  endShape(CLOSE);
  beginShape();
    vertex( 100, -100, -100);
    vertex( 100,  100, -100);
    vertex(   0,    0,  100);
  endShape(CLOSE);
  beginShape();
    vertex( 100, 100, -100);
    vertex(-100, 100, -100);
    vertex(   0,   0,  100);
  endShape(CLOSE);
  beginShape();
    vertex(-100,  100, -100);
    vertex(-100, -100, -100);
    vertex(   0,    0,  100);
  endShape(CLOSE);
  beginShape();
    vertex(100, 100, -100);
    vertex(100, -100, -100);
    vertex(-100, -100, -100);
    vertex(-100, 100, -100);
  endShape(CLOSE);
}