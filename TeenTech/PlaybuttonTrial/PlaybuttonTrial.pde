import ddf.minim.*;                                                      
//Import the audio library
Minim minim;                                                              
// Use the minim class within the library
AudioPlayer player;                                                         
// Get Audio player
boolean play;                                                               
// Declares the play variable to be either true or false
int button_xcoordinate = 50, button_ycoordinate = 50, button_size = 20;     
// Declares integer variables, change the numbers to change the position and size of the button

void setup()                                                               
// Set up the following
{
  size(120,120); 
  // Declare the size of the new window 
  //(number of pixels in the x direction, number of pixels in the y direction)                                    

minim=new Minim(this);
//Pass minim the file
  player=minim.loadFile("Thomas Rhett - Die A Happy Man (Home Free Cover).mp3");   
  //Select music file from folder
}
void draw(){                                                                 
  //Draw the following
  background(255, 0, 0);                                                      
  //Change color of the background
  // The numbers range from 0 to 255
  // (adds red, adds green, adds blue)
  
  stroke(0,255, 0);                                                       
  //Change color of button outline
  // The numbers range from 0 to 255
  // (adds red, adds green, adds blue)
  
  fill(0, 0, 255);                                                          
  //Change color of the button
  // The numbers range from 0 to 255
  // (adds red, adds green, adds blue)
  
  
  rect(button_xcoordinate, button_ycoordinate, button_size, button_size);    
  //Draws a rectangle
  // ( x coordinate, y coordinate, number of pixels in the x direction, number of pixels in the y direction)
  // change the variables above to change the cordinates and dimensions of the button
  
  if (play)                                                                 
  // If play is true then do the following
    player.play();                                                          
    // Play music
    else                                                                    
    // If play is false then do the following
    player.pause();                                                         
    // Pause music
}

void mousePressed(){
//When mouse is pressed do the following  
  if( mouseX > button_xcoordinate && mouseX < button_xcoordinate + button_size &&
      mouseY > button_ycoordinate && mouseY < button_ycoordinate + button_size)
      // if mouse pressed within the  bounds of the button change the true/false valuse of play
      {
  play = !play;      
  //will switch false/true each click
  // Play/Pause
      }
}