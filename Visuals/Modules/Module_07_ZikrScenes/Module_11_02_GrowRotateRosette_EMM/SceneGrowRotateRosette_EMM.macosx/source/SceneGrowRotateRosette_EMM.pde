/*
  10/09/2016
  Emily Meuer
  
  TODO:
   - add buttons
   - put options
   - remove tenorCutoff from Scene?
  
  Remember: when having an ' "Input" class not found' error,
  make sure that you have setup() and draw()! :P ;)
*/

RosetteV3Rotate  rosetteV3Rotate;
Input            input;

void settings()
{  
  fullScreen();
} // settings()

void setup()
{ 
  input        = new Input();  
  rosetteV3Rotate  = new RosetteV3Rotate(input);
  
  background(0);
} // setup()


void draw()
{
  rosetteV3Rotate.run();
} // draw