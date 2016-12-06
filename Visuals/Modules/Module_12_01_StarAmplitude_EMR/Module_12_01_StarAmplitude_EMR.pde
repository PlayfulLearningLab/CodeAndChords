/**
  * Elena Ryan
  * First Module
  * Stars added randomly
  * dependent on amplitude
  * 10/25/2016 comment
  */

Input input;
float threshold;
double time;
float amp;
float r;
float r2;



void setup() {
  //input = new Input();
  input = new Input("Emily_CMajor-2016_9_2-16bit-44.1K Tuned.wav");
  stroke(255, 255, 185);
  threshold = .5;
  background(75, 45, 135);
  size(640, 360);
  time = millis() + 500;
  
}

void draw() {
  if (millis() >= time) {
  amp = input.getAmplitude();
  r = random(10, 640);
  r2 = random(10, 360);
  
  
  if (amp > threshold) {
    star(r, r2, 10, 4, 5);
  }
  time = millis() + 500;
  }
}


void star(float x, float y, float radius1, float radius2, int npoints) {
  float angle = TWO_PI / npoints;
  float halfAngle = angle/2.0;
  beginShape();
  for (float a = 0; a < TWO_PI; a += angle) {
    float sx = x + cos(a) * radius2;
    float sy = y + sin(a) * radius2;
    vertex(sx, sy);
    sx = x + cos(a+halfAngle) * radius1;
    sy = y + sin(a+halfAngle) * radius1;
    vertex(sx, sy);
  }
  endShape(CLOSE);
  fill(255, 255, 185);
}
    