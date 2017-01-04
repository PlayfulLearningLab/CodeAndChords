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
HScrollbar scrollSize;
IFLabel label;
IFTextField textField;

float threshold = 15.0;
float threshold1 = 50.0;
float threshold2 = 30.0;
float threshold3 = 8.0;
float scrollbarPos;
int sizeMin  = 0;
int sizeMax  = 100;
int thres, size;
int delay;
int l;
int thresMin  = 0;
int thresMax  = 100;

void setup(){
  controller   = new GUIController(this);
  testInput = new Input();
  size(600, 360);
  background(0);
  noStroke();
  frameRate(9);
  
  scrollSize = new HScrollbar(width-200, height - 50, 150, 18, 5);
  fill(0);
  textField = new IFTextField("Text Field", width - 200, height - 85, 140);
  label     = new IFLabel("Size of Rectangles: " + thres, width - 197, height - 81);
  controller.add(textField);
  controller.add(label);
  textField.addActionListener(this);
  
  
  delay  = millis();
}
void draw(){
  
  
  for (int i = 0; i < width; i++) {
    float r = random(255) ;
    stroke(r);
    line(i, 0, i, height);
  }
  
  scrollSize.update();
  scrollSize.display();
  scrollbarPos = scrollSize.getPos();
  size     = round(round(map(scrollbarPos, scrollSize.sposMin, scrollSize.sposMax, sizeMin, sizeMax))/10)-3;
  println("Size of Rect = " + size + "; bpmMin = " + sizeMin + "; bpmMax = " + sizeMax);
  label.setLabel("Size of Rectangles: " + size);
  
  l = size;
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
      rect(random(width), 0, l, height);
      rect(random(width), 0, l, height);
      rect(random(width), 0, l, height);
      rect(random(width), 0, l, height);
      rect(random(width), 0, l, height);
      rect(random(width), 0, l, height);
      rect(random(width), 0, l, height);
      rect(random(width), 0, l, height);
      rect(random(width), 0, l, height);
      rect(random(width), 0, l, height);
      rect(random(width), 0, l, height);
      rect(random(width), 0, l, height);
       
  }
  */
 
 //VERSION 2
 
 if(testInput.getAmplitude()>threshold1){  
      fill(96, 245, 90);
      for(int i = 0; i < 13; i++)
      {
        rect(random(width), 0, l, height);
      } // for

  } // if
 
  if(testInput.getAmplitude()>threshold2 && testInput.getAmplitude()<threshold1){  
      fill(98, 17, 250);
      for(int i = 0; i < 13; i++)
      {
        rect(random(width), 0, l, height);
      } // for
 
  }
  if(testInput.getAmplitude()>threshold3 && testInput.getAmplitude()<threshold2){  
      fill(96, 245, 241);
      for(int i = 0; i < 13; i++)
      {
        rect(random(width), 0, l, height);
      } // for
  } // if
  

  
} // draw