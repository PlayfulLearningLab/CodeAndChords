/*
  Next on the agenda: make the parameters changeable from inside the sketch!
  https://processing.org/examples/button.html
  
  (Biggest "looks like what you hear" problem right now: amplitude comes down so quickly.)
*/

SoundShape  sShape;
Input       input;
float         x;
float         y;

HScrollbar  scroll;

void settings()
{
  size(700,700);
} // settings()

void setup()
{
  background(50, 200, 50);
  fill(0, 150, 25);
  stroke(255);
  
  sShape  = new SoundShape(width/4, height - height/4, 100, 4);
  input   = new Input();
  
  x  = input.getAdjustedFundAsHz();
  x  = map(x, 0, 800, 0, width);
  
  y  = input.getAmplitude();
  y  = map(y, 0, 800, height, 0);
  
  scroll  = new HScrollbar(10, height - 26, width - 20, 16, 10);

} // setup()

void draw()
{
//  sShape.drawShape();

  if(input.getAmplitude() > 20)
  {
    background(75, 200, 50);
    fill(0, 150, 25);
  stroke(255);
  
    x  = input.getAdjustedFundAsHz();
    x  = map(x, 0, 800, height - 200, 150);
    
    y  = input.getAmplitude();
 //   println("y = " + y);
    y  = map(y, 0, 800, 0, width);
  
//    sShape.moveTo(x, y);
    sShape.moveTo(y,x);
    
  } // if - amplitude
  
  
  scroll.update();
  scroll.display();
} // draw()