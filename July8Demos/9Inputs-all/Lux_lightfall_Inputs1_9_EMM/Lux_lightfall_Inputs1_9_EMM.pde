/* //<>//
  Started: 06/29/2016
 MRA
 Lux Aurumque
 - press space bar to change to next scene 
 - labels for when to change scene are in draw (text and measure number)
 P.S. I know I could use loops, but am not using them for now in case they don't play nicely with the live input class
 
 Edit 07/02/2016 by Emily Meuer:
 - adjusting calibration for new Input class.
 
 
 */


// Calibrate:
// Original calibration values made w/analog volume knobs at 75%.

int  adjustTenorSoloAmp = 400;  // number by which tenor amplitude will be divided
// before sending it to freqPoints, where it controls light size.
float fallThreshold = 3;    // candles go back to the top when their line's amp is lower than this.

int    sceneOneTenorAmpAdjust     = 1000;  // divide
int    sceneTwoTenorAmpAdjust     = 3;   // multiply
float  sceneThreeTenorAmpAdjust   = 1;      // divide by this
int    sceneFiveTenorAmpAdjust    = 500; // divide
int    sceneSixTenorAmpAdjust     = 1;    // divide
int    sceneSixAllFundAdjust      = 5000; // multiply by this number.

Input  myInput;
PImage  img;
Candle myCandle[];
int orbs = 9; //number of glowing orbs
int scene = 0;
//int scene = -1;
int waitUntil;
// changed fallThreshold from 0.01 to 10

void setup()
{
  fullScreen();

  //  myInput = new Input(9);
  myInput  = new Input(13);
  myCandle = new Candle[orbs];

  // Tenor soloist:
  myCandle[3] = new Candle(100, 70, 30, width/10*9, 1); //yellow/gold
  // Other voices:
  myCandle[0] = new Candle(100, 60, 60, width/10*1, 1); //rose
  myCandle[1] = new Candle(100, 50, 30, width/10*2, 1); //red
  myCandle[2] = new Candle(90, 90, 50, width/10*3, 1);  //orange
  //  myCandle[4] = new Candle(50, 100, 70, width/10*4, 1); //light green
  myCandle[4] = new Candle(20, 90, 80, width/10*5, 1); //forest green
  myCandle[5] = new Candle(20, 40, 100, width/10*6, 1); //periwinkle
  myCandle[6] = new Candle(80, 50, 80, width/10*7, 1); //violet
  myCandle[7] = new Candle(100, 20, 90, width/10*8, 1); //fuchsia
  myCandle[8] = new Candle(255, 130, 180, width/10*8, 1); //pink
  // Candle(redTint, greenTint, blueTint, xLocation for even spacing, sizeAdjustment);
}

void draw() {
  try
  {
  background(0);
  
//  println("myInput.getAmplitude(6) = " + myInput.getAmplitude(6));

  // SETS THE SCENE /////////////////////////////////////////
  // sets which picture goes with each scene
  if (keyPressed && millis() > waitUntil) {
    scene ++;
    //    scene = ((scene + 1) % 6) + 1;

    println("scene = " + scene);

    if (scene == 1 || scene == 2) {
      img = loadImage("starryNight.jpg");
      img.resize(width, height);
      img.loadPixels();
      loadPixels();
    }
    if (scene == 3) {
      img = loadImage("angels.jpg");
      img.resize(width, height);
      img.loadPixels();
      loadPixels();
    }
    if (scene == 4) {
      img = loadImage("angel1.jpg");
      img.resize(width, height);
      img.loadPixels();
      loadPixels();
    }
    if (scene == 5) {
      img = loadImage("angel2.jpg");
      img.resize(width, height);
      img.loadPixels();
      loadPixels();
    }
    if (scene == 6) {
      img = loadImage("holyFamily.jpg");
      img.resize(width, height);
      img.loadPixels();
      loadPixels();
    }

    if (scene > 6) {
      scene = 1;
    }

    waitUntil = millis()+500;
    //    println(scene);
  } // if (keyPressed)

  // SCENES ////////////////////////////////////////////////

  if (scene == 1) { //start of piece, measure 1
    // tenor solo:
    myCandle[3].freqPoints(myInput.getAdjustedFundAsHz(4), myInput.getAmplitude(4) / sceneOneTenorAmpAdjust); //tenor solo controlls this
 
    // other voices:
    for (int i = 0; i < 9; i++)
    {
      if(i == 3)  {  i = i + 1;  }
      
        if (myInput.getAmplitude(i + 1) > fallThreshold) {
          myCandle[i].fall();
        }
        if (myInput.getAmplitude(i + 1) < fallThreshold) {
          myCandle[i].resetYDrop();
        }
    } // for

    colorLightfall(true, 60);
  }

  if (scene == 2) { //at first "calida", measure 9
    myCandle[3].highlight(myInput.getAmplitude(4) * sceneTwoTenorAmpAdjust, width*0.6, height*0.15); //tenor1 controls this


    for (int i = 1; i < 9; i++)
    {
      if(i == 3)  {  i = i + 1;  }
      
      println("i = " + i);
      myCandle[i].circle(myInput.getAmplitude(i + 1), myInput.getAdjustedFundAsHz(i + 1), width*0.6, height*0.2);
    } // for

    colorLightfall(false, 60);
  }

  if (scene == 3) { //at first "pura", measure 17
    myCandle[3].highlight(myInput.getAmplitude(4) / sceneThreeTenorAmpAdjust, width*0.45, height*0.3); //tenor1 still controls this

    for (int i = 1; i < 9; i++)
    {
      if(i == 3)  {  i = i + 1;  }
      
      myCandle[i].hover(myInput.getAdjustedFundAsHz(i + 1), myInput.getAmplitude(i + 1));
    } // for

    colorLightfall(true, 50);
  }

  if (scene == 4) { //at first "canunt", measure 24
    for (int i = 0; i < 9; i++)
    {
      myCandle[i].hover(myInput.getAdjustedFundAsHz(i + 1), myInput.getAmplitude(i + 1));
    } // for

    colorLightfall(true, 50);
  }

  if (scene == 5) { //right after the grand pause after "angeli", measure 30
    myCandle[3].freqPoints(myInput.getAdjustedFundAsHz(4), myInput.getAmplitude(4) / sceneFiveTenorAmpAdjust); //tenor solo controls this

    for (int i = 1; i < 9; i++)
    {
      if(i == 3)  {  i = i + 1;  }
      
      if (myInput.getAmplitude(i + 1) > fallThreshold) {
        myCandle[i].fall();
      }
      if (myInput.getAmplitude(i + 1) < fallThreshold) {
        myCandle[i].resetYDrop();
      }
    } // for

    colorLightfall(true, 50);
  }

  if (scene == 6) { // at "natum" of lower voices (not tenor soli), measure 38
    myCandle[3].highlight(myInput.getAmplitude(4) / sceneSixTenorAmpAdjust, width*0.15, height*0.8); //tenor soli control this
    myCandle[1].highlight(myInput.getAmplitude(2) / sceneSixTenorAmpAdjust, width*0.15, height*0.8); //tenor soli control this

    for (int i = 2; i < 9; i++)
    {
      if(i == 1 || i == 3)  {  i = i + 1;  }
//      if(i == 3)  {  i = i + 1;  }
      
      myCandle[i].circle(myInput.getAmplitude(i + 1), myInput.getAdjustedFundAsHz(i + 1), width*0.2, height*0.6);
    } // for

    colorLightfall(false, 60);
  }
}catch(NullPointerException npe)  {}
}// draw


// colorLightfall( tinted orbs?, percent brightness of overall image)
void colorLightfall(boolean onTint, float inShowImage) {
  float xPos, yPos;
  for (int x = 0; x < img.width; x++) {
    for (int y = 0; y < img.height; y++ ) {
      int loc = x + y*img.width; // Calculate the 1D location from a 2D grid
      float r, g, b;
      float adjustBrightness = -255; //set lowest value adjust brightness can be, then the below functions change it
      int j = 0; //for unhiding the pixels only one time
      int k = 0;
      r = red (img.pixels[loc]); // Get the R,G,B values from image
      g = green (img.pixels[loc]);
      b = blue (img.pixels[loc]);
      //uses position of raindrops to determine location of glow
      for (int i = 0; i < myCandle.length; i++) {  //gets the position of each of the raindrops, and adjusts the pixels near it
        float tempBrightness = -255; //compares the brightness caused by one orb to the brightness of other orbs and keeps the brighter number
        //tempBrightness is the brightness of the current orb. adjustBrightness is the brightest value thus far
        xPos = myCandle[i].getXGlow();
        yPos = myCandle[i].getYGlow();
        float maxdist = myCandle[i].getSize();//dist(0,0,width,height);
        //        println("maxdist = " + maxdist);
        float d = dist(x, y, xPos, yPos);
        if (d <= 1.4*maxdist) { //only the pixels near the location of the raindrop are adjusted 
          //the number maxdist is multiplied by has to be balanced with the "if (adjustBrightness <..." before the pixels are constrained
          //if this isn't balanced, the tint will exceed the bubble
          if (j == 0) { //only want to tint the pixels one time (or else they get... well... very bright or dark)
            if (onTint == true) {
              r = myCandle[i].getRedTint()*r; 
              g = myCandle[i].getGreenTint()*g;
              b = myCandle[i].getBlueTint()*b;
              j++;
            }
            if (onTint == false) {
              r *= 1.0;
              g *= 0.7;
              b *= 0.3;
              j++;
            }
          }
          tempBrightness = 255*(maxdist-d)/maxdist; //brightness is determined by distance from (x, y) location of orb
          if (tempBrightness > adjustBrightness) { //keep the bigger one
            adjustBrightness = tempBrightness;
          }
        }//if pixels are near the raindrop location
      }//for candle array
      if (adjustBrightness < -100) {
        adjustBrightness = -255+255*inShowImage/100;
      }
      r += adjustBrightness;
      g += adjustBrightness;
      b += adjustBrightness;
      constrain(r, 0, 255);
      constrain(g, 0, 255);
      constrain(b, 0, 255);
      color c = color(r, g, b);
      pixels[y*width + x] = c;
    }//for y
  }//for x
  updatePixels();
} // colorLightfall