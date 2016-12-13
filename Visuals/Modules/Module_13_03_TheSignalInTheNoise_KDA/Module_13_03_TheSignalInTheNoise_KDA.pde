import interfascia.*;
/* 11-10-16
  Kristen Andrews
  
  The Signal In The Noise Mod13
  There will be a randlomy changing/morphing background. When an 
  input is recieved, tere will be a randomly placed disruption (star) in the field 
 */
GUIController controller;
Input testInput;

float threshold = 2.0;
int thresMin  = 0;
int thresMax  = 100;
int thres;
int delay;

void setup(){
  controller   = new GUIController(this);
  testInput = new Input();
  size(600, 360);
  background(0);
  noStroke();
  frameRate(9);
  delay  = millis();
  
}
void draw(){
  
  for (int i = 0; i < width; i++) {
    float r = random(255) ;
    stroke(r);
    line(i, 0, i, height);
  }
 /*
  for (int i = height; i > 0; i--) {
   float r = random(255);
   stroke(r);
    line(i, 0, width, i);
  }
 for (int i = height; i > 0; i--){
    float r = random(255);
    stroke(r);
    line(height, 0, i, height);
 }
 for (int i = height; i > 0; i--){
     float r = random(255);
    stroke(r);
    line(height, width, 0, i);
 } */

  println("Input:" + testInput.getAmplitude());

//This if statement has random rectangles appearing if an input is above a threshold
 if(testInput.getAmplitude()>threshold){  
      fill(random(255), random(255), random(255));
      rect(random(height), 0, 5, height);
      rect(random(height), 0, 5, height);
      rect(random(height), 0, 5, height);
      rect(random(height), 0, 5, height);
      rect(random(height), 0, 5, height);
      rect(random(height), 0, 5, height);
      rect(random(height), 0, 5, height);
      rect(random(height), 0, 5, height);
      rect(random(height), 0, 5, height);
      rect(random(height), 0, 5, height);
      rect(random(height), 0, 5, height);
      rect(random(height), 0, 5, height);
       
  }
//This if statement is still in the works....trying to make a circle appear in the center
//and grow if the input continues to be large
 /*if(testInput.getAmplitude()>threshold){
    fill(255, 10);
    ellipse(320, 180, 50, 50);
  }*/
  
}