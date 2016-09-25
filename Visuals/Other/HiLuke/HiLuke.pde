void setup()
{
  fullScreen();
  
  background(150, 150, 350);
  stroke(255);
  text("Hi, Luke!", width/2, height/2);
} // setup()

void draw()
{
  background(150, 150, 255);
  ellipse(mouseX, mouseY, 100, 100);
} // draw()