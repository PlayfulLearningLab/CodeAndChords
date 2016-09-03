/*
  Emily Meuer
  07/14/2016
  
  Example visual file;
  basically just Erin's "follow.pde" (CantusProject-InProgress/Extra/trials/follow).
*/

void setup()
{
  size(500, 500);
} // setup()

void draw()
{
  background(255, 200, 255);
  triangle(mouseX, mouseY-50, (mouseX-50), (mouseY+50), (mouseX+50), (mouseY+50));
} // draw()