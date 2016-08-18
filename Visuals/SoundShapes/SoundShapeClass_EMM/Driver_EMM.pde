/*
  Next on the agenda: make the parameters changeable from inside the sketch!
  https://processing.org/examples/button.html
  
  (Biggest "looks like what you hear" problem right now: amplitude comes down so quickly.)
*/

SoundShape  sShape;
Input       input;
float         x;
float         y;

void settings()
{
  size(700,700);
} // settings()

void setup()
{
  background(50, 200, 50);
  stroke(255);
  
  sShape  = new SoundShape(width/4, height - height/4, 100, 4);
  input   = new Input();
  
  x  = input.getAdjustedFundAsHz();
  x  = map(x, 0, 800, 0, width);
  
  y  = input.getAmplitude();
  y  = map(y, 0, 800, height, 0);

} // setup()

void draw()
{
//  sShape.drawShape();

  if(input.getAmplitude() > 20)
  {
  background(75, 200, 50);
  
    x  = input.getAdjustedFundAsHz();
    x  = map(x, 0, 800, 0, width);
    
    y  = input.getAmplitude();
    println("y = " + y);
    y  = map(y, 0, 800, height - 200, 150);
  
    sShape.moveTo(x, y);
  } // if - amplitude
  
  
} // draw()