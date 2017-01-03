/*
  05/30/2016
 Emily Meuer
 
 Daft Punk Visualization
 
 { Look into arcs: https://processing.org/reference/arc_.html }
 
 { Background boxes/stripes - like GeoDash }
 { Background line - GeoDash, Fractating/Fractal by Herzilo }
 
 *** Line through the shapes - to begin;
 Break at beat-boxing - hop out individually.
 
 Beat-boxing:
 - circle growing larger
 - circle w/in a circle / two circles moving away from each other
 
 Use PVector to move shapes?
 
 */

PShape   circle;
float    fund;
Input    input;
float    prevX  = width/2 - 50;
float    prevY  = height / 2;

int      threshold  = 1;

void setup()
{
//  fullScreen();
  size(800,800);
  background(0);

  input  = new Input();
  circle  = createShape(ELLIPSE, width / 2, height - height/4, 100, 100);
  
  color yellow = color(255, 255, 0);
  circle.setStroke(yellow);
  circle.setStrokeWeight(25);
  circle.setFill(color(0));
}

void draw()
{
//  background(0);


  println("freq: " + input.getAdjustedFundAsHz() + "; amplitude = " + input.getAmplitude());
  
  if(input.getAmplitude() > threshold)
  //if (input.getAmplitude() > 3)
  {
    fund  = input.getAdjustedFundAsHz();
    translate(width / 2, ((height - fund/2)));
//    circle.translate(400, 400);

    prevY  = fund;
  } else {
    circle.translate(prevX, ((height - prevY/2)));
  } // else

  shape(circle);

  //ellipse(width/2, input.getFreqAsHz()/2, 100, 100); //input.getAmplitude(), input.getAmplitude());
}