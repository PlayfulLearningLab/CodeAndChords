SoundShape  sShape;

void settings()
{
  size(700,700);
} // settings()

void setup()
{
  background(0, 200, 50);
  stroke(255);
  
  sShape  = new SoundShape(width/4, height - height/4, 200, 3);

} // setup()

void draw()
{
  background(0, 200, 50);
//  sShape.drawShape();

  sShape.moveTo(mouseX,mouseY);
} // draw()