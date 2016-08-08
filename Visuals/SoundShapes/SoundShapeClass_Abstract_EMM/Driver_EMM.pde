SoundShapeEMM  sShape;

void settings()
{
  size(700,700);
} // settings()

void setup()
{
  background(0, 200, 50);
  stroke(255);
  
  sShape  = new SoundShapeEMM(width/4, height - height/4, 200, 8);

} // setup()

void draw()
{
  sShape.drawShape();
} // draw()