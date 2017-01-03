/*
  08/08/2016
  Michaela Andrews
  
  Motion Damper
  - the start of an idea for a way to smoothen direction changes of moving visuals
*/

float rectX=0, rectY=0;

void setup() {
  size(800, 600);
}// setup()

void draw() {
  background(0);
  rect(rectX, rectY, 20, 20);
  if(mouseX > rectX){
    rectX ++;
  }
  if(mouseX < rectX){
    rectX --;
  }
  if(mouseY > rectY){
    rectY ++;
  }
  if(mouseY < rectY){
    rectY --;
  }
  
}// draw()