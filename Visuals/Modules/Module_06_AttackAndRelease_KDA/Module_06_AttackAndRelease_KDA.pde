import interfascia.*;
/* 10-16-16
  Kristen Andrews
  
  Attack and Release Mod6
  When the input is below the threshold, the screen will remain black.
  When the input is above the threshold, white will appear on the screen.
  In time, the threshold level will be able to be controlled by a slider bar.
 */
GUIController controller;
Input testInput;
HScrollbar scrollThres, scrollAttack, scrollRelease;
IFLabel label, label2, label3;
IFTextField textField, textField2, textField3;

float threshold = 1.0;
float attack = 1.0;
float release = 1.0;
float scrollbarPos, scrollbarPos2, scrollbarPos3; 
int thresMin  = 0;
int thresMax  = 100;
int thres, att, rel;
int delay;


void setup(){
  controller   = new GUIController(this);
  testInput = new Input();
  size(600, 400);
  background(0);
 
  
  scrollThres = new HScrollbar(width-200, height - 50, 150, 18, 5);
  scrollAttack = new HScrollbar(width-375, height - 50, 150, 18, 5);
  scrollRelease = new HScrollbar(width-550, height - 50, 150, 18, 5);
  
  
  fill(0);
  textField = new IFTextField("Text Field", width - 200, height - 85, 90);
  label     = new IFLabel("Threshold: " + thres, width - 197, height - 81);
  
  fill(0);
  textField2 = new IFTextField("Text Field", width - 375, height - 85, 90);
  label2     = new IFLabel("Attack: " + att, width - 372, height - 81);
  
  fill(0);
  textField3 = new IFTextField("Text Field", width - 550, height - 85, 90);
  label3     = new IFLabel("Release: " + rel, width - 547, height - 81);
  
  controller.add(textField);
  controller.add(label);
  controller.add(textField2);
  controller.add(label2);
  controller.add(textField3);
  controller.add(label3);

  textField.addActionListener(this);
  textField2.addActionListener(this);
  textField3.addActionListener(this);

  delay  = millis();
  
}
void draw(){
  scrollThres.update();
  scrollThres.display();
  scrollAttack.update();
  scrollAttack.display();
  scrollRelease.update();
  scrollRelease.display();
  
  scrollbarPos = scrollThres.getPos();
  thres     = round(map(scrollbarPos, scrollThres.sposMin, scrollThres.sposMax, thresMin, thresMax))-42;
  println("Threshold = " + thres + "; bpmMin = " + thresMin + "; bpmMax = " + thresMax);
  label.setLabel("Threshold: " + thres);
  
  scrollbarPos2 = scrollAttack.getPos();
  att     = round(map(scrollbarPos2, scrollAttack.sposMin, scrollAttack.sposMax, thresMin, thresMax)) - 24;
  println("Attack = " + att + "; bpmMin = " + thresMin + "; bpmMax = " + thresMax);
  label2.setLabel("Attack " + att);
  
  scrollbarPos3 = scrollRelease.getPos();
  rel     = round(map(scrollbarPos3, scrollRelease.sposMin, scrollRelease.sposMax, thresMin, thresMax))-6;
  println("Release = " + rel + "; bpmMin = " + thresMin + "; bpmMax = " + thresMax);
  label3.setLabel("Release: " + rel);
  
  println("Input:" + testInput.getAmplitude());
  
  
  threshold = thres;
  attack = att;
  release = rel;
  //What I learned: for me talking to my computer, the input was around 30-40, the current threshold ranges from 0-112.
  //I believe this should be looked at more and discussed what a threshold "range" should be.
  
  
  
  if (testInput.getAmplitude()>=threshold)
  {  //white box appears when input is above threshold  
        stroke(255);
        fill(255, attack);
        rect(100, 100, 400, 200);
      
  }
  if (testInput.getAmplitude()<threshold)
  {  //white box appears when input is above threshold 
      stroke(0);
      fill(0, release);
      rect(100, 100, 400, 200);
      
  }
  
  

}