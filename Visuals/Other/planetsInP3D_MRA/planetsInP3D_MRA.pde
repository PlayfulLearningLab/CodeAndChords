/*
  07/20/2016
  Michaela Andrews

  Mercury, Venus, Earth, and Mars orbiting the Sun
  incorporates approximate data for relative orbit radius and relative speed
*/

int theta = 0;
float speedAdj = 0.25;
float radAdj = 0.5;

void setup() {
  size(1000, 500, P3D);
}

void draw() {
  background(0);
  ambientLight(50, 50, 50, 0, 0, 0);
  translate(0, height/2, -100);
//sun
  pushMatrix();
  pointLight(255, 255, 200, 0, 0, 0);
  spotLight(255, 255, 200, 0, 0, 600, 0, 0, -1, PI, 1.);
  fill(200, 200, 100);
  noStroke();
  sphere(100);
//mercury
  translate(387*radAdj*cos(radians(speedAdj/0.24*theta)), 0, 387*radAdj*sin(radians(speedAdj/0.24*theta)));
  fill(200, 150, 100);
  noStroke();
  sphere(10);
  popMatrix();
//venus
  pushMatrix();
  translate(723*radAdj*cos(radians(speedAdj/0.62*theta)), 0, 723*radAdj*sin(radians(speedAdj/0.62*theta)));
  fill(175, 150, 100);
  noStroke();
  sphere(24);
  popMatrix();
//earth
  pushMatrix();
  translate(1000*radAdj*cos(radians(speedAdj/1*theta)), 0, 1000*radAdj*sin(radians(speedAdj/1*theta)));
  fill(100, 150, 200);
  noStroke();
  sphere(24);
  popMatrix();
//mars
  pushMatrix();
  translate(1880*radAdj*cos(radians(speedAdj/1.88*theta)), 0, 1880*radAdj*sin(radians(speedAdj/1.88*theta)));
  fill(200, 100, 100);
  noStroke();
  sphere(14);
  popMatrix();
theta++;
  
}