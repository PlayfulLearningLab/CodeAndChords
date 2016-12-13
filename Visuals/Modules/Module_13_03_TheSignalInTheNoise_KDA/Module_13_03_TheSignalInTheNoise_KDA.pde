import interfascia.*;
/* 11-10-16
  Kristen Andrews
  
  The Signal In The Noise Mod13
  There will be a randlomy changing/morphing background. When an 
  input is recieved, tere will be a randomly placed disruption (star) in the field 
  
  NOTE PLEASE READ
  There are a few different versions in this version. 
  
  VERSION 1 (Line 73ish)
  The first 'if' statement is meant to act alone, creating random lines 
  if the input recieved is above one threshold (threshold).
  
  VERSION 2 (Line 91ish)
  Another version noted is having 3 different thresholds (threshold1, threshold2, threshold3)
  and only 3 different colored rectangles are appearing. The thresholds have a wider range here, trying to get louder sounds to pop
  but still recongize the less loud sounds.
  
  CURRENTLY VERSION TWO IS ACTIVE
  to change versions use the / and the * to block out the one you do not want
 */
GUIController controller;
Input testInput;

float threshold = 15.0;
float threshold1 = 50.0;
float threshold2 = 30.0;
float threshold3 = 8.0;
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
 //VERSION 1
 /*
 if(testInput.getAmplitude()>threshold){  
      fill(random(255), random(255), random(255));
      rect(random(width), 0, 7, height);
      rect(random(width), 0, 7, height);
      rect(random(width), 0, 7, height);
      rect(random(width), 0, 7, height);
      rect(random(width), 0, 7, height);
      rect(random(width), 0, 7, height);
      rect(random(width), 0, 7, height);
      rect(random(width), 0, 7, height);
      rect(random(width), 0, 7, height);
      rect(random(width), 0, 7, height);
      rect(random(width), 0, 7, height);
      rect(random(width), 0, 7, height);
       
  }
  */
 
 //VERSION 2
 
 if(testInput.getAmplitude()>threshold1){  
      fill(96, 245, 90);
      rect(random(width), 0, 7, height);
      rect(random(width), 0, 7, height);
      rect(random(width), 0, 7, height);
      rect(random(width), 0, 7, height);
      rect(random(width), 0, 7, height);
      rect(random(width), 0, 7, height);
      rect(random(width), 0, 7, height);
      rect(random(width), 0, 7, height);
      rect(random(width), 0, 7, height);
      rect(random(width), 0, 7, height);
      rect(random(width), 0, 7, height);
      rect(random(width), 0, 7, height);
       
  }
 
  if(testInput.getAmplitude()>threshold2 && testInput.getAmplitude()<threshold1){  
      fill(98, 17, 250);
      rect(random(width), 0, 7, height);
      rect(random(width), 0, 7, height);
      rect(random(width), 0, 7, height);
      rect(random(width), 0, 7, height);
      rect(random(width), 0, 7, height);
      rect(random(width), 0, 7, height);
      rect(random(width), 0, 7, height);
      rect(random(width), 0, 7, height);
      rect(random(width), 0, 7, height);
      rect(random(width), 0, 7, height);
      rect(random(width), 0, 7, height);
      rect(random(width), 0, 7, height);
       
  }
  if(testInput.getAmplitude()>threshold3 && testInput.getAmplitude()<threshold2){  
      fill(96, 245, 241);
      rect(random(width), 0, 7, height);
      rect(random(width), 0, 7, height);
      rect(random(width), 0, 7, height);
      rect(random(width), 0, 7, height);
      rect(random(width), 0, 7, height);
      rect(random(width), 0, 7, height);
      rect(random(width), 0, 7, height);
      rect(random(width), 0, 7, height);
      rect(random(width), 0, 7, height);
      rect(random(width), 0, 7, height);
      rect(random(width), 0, 7, height);
      rect(random(width), 0, 7, height);
       
  }
  

  
}