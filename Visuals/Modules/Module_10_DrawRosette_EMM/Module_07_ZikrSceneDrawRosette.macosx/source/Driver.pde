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

DrawRosette  drawRosette;
Input        input;

void settings()
{  
  fullScreen();
} // settings()

void setup()
{ 
  input        = new Input();  
  drawRosette  = new DrawRosette(input, 1);
  
  background(0);
} // setup()


void draw()
{
  drawRosette.run();
} // draw