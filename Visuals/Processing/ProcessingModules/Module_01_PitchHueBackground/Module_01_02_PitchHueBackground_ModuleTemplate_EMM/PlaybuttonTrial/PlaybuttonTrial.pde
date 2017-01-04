import ddf.minim.*;
Minim minim;
AudioPlayer player;
boolean play;
int button_x = 40, button_y = 40, button_sz = 20;

void setup()
{
minim=new Minim(this);
  player=minim.loadFile("Congratulations.mp3");
}
void draw(){
  background(20, 0, 0);
  stroke(255, 0, 255);
  fill(50, 0, 120);
  rect(button_x, button_y, button_sz, button_sz);
  
  if (play)
    player.play();
    else
    player.pause();
}

void mousePressed(){
  if( mouseX > button_x && mouseX < button_x + button_sz &&
      mouseY > button_y && mouseY < button_y + button_sz){
  play = !play;//will switch false/true each click
      }
}