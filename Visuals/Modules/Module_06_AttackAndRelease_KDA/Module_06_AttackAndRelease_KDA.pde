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
HScrollbar scroll;
IFLabel label;
IFTextField textField;

float threshold = 1.0;
float scrollbarPos; 
int thresMin  = 20;
int thresMax  = 80;
int thres;
int delay;

void setup(){
  controller   = new GUIController(this);
  testInput = new Input();
  size(600, 400);
  background(0);
  scroll = new HScrollbar(width-200, height - 50, 150, 18, 5);
  
  fill(0);
  textField = new IFTextField("Text Field", width - 200, height - 85, 90);
  label     = new IFLabel("Threshold: " + thres, width - 197, height - 81);
  
  controller.add(textField);
  controller.add(label);

  textField.addActionListener(this);

  delay  = millis();
  
}
void draw(){
  scroll.update();
  scroll.display();
  scrollbarPos = scroll.getPos();
  thres     = round(map(scrollbarPos, scroll.sposMin, scroll.sposMax, thresMin, thresMax));
  println("Threshold = " + thres + "; bpmMin = " + thresMin + "; bpmMax = " + thresMax);
  label.setLabel("Threshold: " + thres);
  
  threshold = thres;
  
  if (testInput.getAmplitude()>=threshold)
  {  //white box appears when input is above threshold 
      stroke(255);
      fill(255);
      rect(100, 100, 400, 200);
      
  }
  if (testInput.getAmplitude()<threshold)
  {  //white box appears when input is above threshold 
      stroke(0);
      fill(0);
      rect(100, 100, 400, 200);
      
  }

}