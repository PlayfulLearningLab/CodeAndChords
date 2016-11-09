import interfascia.*;

/*
  Next on the agenda: make the parameters changeable from inside the sketch!
  https://processing.org/examples/button.html
  
  (Biggest "looks like what you hear" problem right now: amplitude comes down so quickly.)
*/ 


SoundShape  sShape;
Input       input;
float         x;
float         y;

GUIController  controller;
IFButton       setXLow;
IFTextField    setXLowText;

HScrollbar  scroll;

void settings()
{
  size(700,700);
} // settings()

void setup()
{
  background(204, 255, 204);
  fill(255);
  strokeWeight(5);
  stroke(102, 0, 102);
  
  sShape  = new SoundShape(width/4, height - height/4, 100, 4, 0, 0);
  input   = new Input();
  /*
  x  = input.getAdjustedFundAsHz();
  x  = map(x, 0, 800, 0, width);
  
  y  = input.getAmplitude();
  y  = map(y, 0, 800, height, 0);
  */
  
  controller  = new GUIController(this);
  setXLowText  = new IFTextField("setXLow", 50, height - 50, 100);
//  setXLow      = new IFButton("Set xLow"
  
//  controller.add(setXLowText);
  
//  scroll  = new HScrollbar(10, height - 26, width - 20, 16, 10);

} // setup()

void draw()
{
//  sShape.drawShape();

  if(input.getAmplitude() > 20)
  {
  background(204, 255, 204);
//    fill(0, 150, 25);
    
    sShape.positionShape();
   
  } // if - amplitude
  
   
   
//      println("setXLowText.getValue() = " + setXLowText.getValue());

//  scroll.update();
//  scroll.display();
} // draw()