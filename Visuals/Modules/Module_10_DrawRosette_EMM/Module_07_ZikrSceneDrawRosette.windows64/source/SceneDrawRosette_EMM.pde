class DrawRosette extends Scene
{
  /*
    06/29/2016
   Emily Meuer
   
   This scene draws a rosette, either on a timer or on an audio-input-triggered event.
   */

  // Calibrate:
  int[]  freqThresholds  = new int[] {     // these are the frequencies that need to be crossed to draw a new line.
    440, 
    460, 
    480, 
    530, 
    560, 
    600, 
    650  };
  
  /*
  // July 8 thresholds:
  int[]  freqThresholds  = new int[] {     // these are the frequencies that need to be crossed to draw a new line.
    150, 
    160, 
    190, 
    205, 
    230, 
    255, 
    325  };
    */
    
  Input  input;
  int    stroke;
  float  thresholdFreq;
  float  time  = 100;
  int    waitUntil;

  /*
   Frequencies:
   Eb 3      -  155.56
   Fb (E) 3  -  164.81
   G 3       -  196.00
   Ab 3      -  207.65
   Bb 3      -  233.08
   C 4       -  261.63
   Fb (E) 4  -  329.63
   */
  
  /**
   *  Constructor; creates a DrawRosette with the given Input and tenorCutoff.
   *
   *  @param  input        an Input that can be used to control when or in what color the rosettes are drawn.
   *  @param  tenorCutoff  int that specifies which mics belong to the tenors (this and all higher are tenors; lower numbers are basses).
   */
  public DrawRosette(Input input, int tenorCutoff)
  {
    this.input         = input;
    this.tenorCutoff   = tenorCutoff;
    this.stroke        = 0;
    
    this.thresholdFreq = 100;
    waitUntil          = millis();
  } // constructor
  
  /**
   *  Called in draw in the Zikr_Scenes_Inputs1_9_EMM tab.
   */
  public void run()
  {
    translate(width/2, height/2);
    drawAndRaiseThreshold(300);
  } // run()
  
  /**
   *  Draws one more stroke of rosetteThree each time a pitch threshold is passed,
   *  and raises the threshold that must be passed for the next stroke.
   *
   *  @param  radius  ** a float specifiying how long each stroke of the rosette is.
   */
  void drawAndRaiseThreshold(float radius) 
  {
//    println("Draw.drawAndRaiseThreshold: input.getAverageFund(1, input.numInputs) = " + input.getAverageFund(1, input.numInputs));
    
    // Draws a line each time the pitch crosses a frequency threshold, and ups the threshold each time:
    if ( (stroke < 16) && (input.getAdjustedFundAsHz() > freqThresholds[stroke/3]) && millis() > waitUntil ) {
      waitUntil  = millis() + 300;
      drawRosetteThree(radius, stroke, originalThree);
  
      thresholdFreq += 100;
      stroke++;
    } // if
  } // drawAndRaiseThreshold(float)
  
  /**
   *  Draws one more stroke of rosetteThree every "time" milliseconds, 
   *  where "time" is a variable specified earlier in the class.
   *
   *  @param  radius  ** a float specifiying how long each stroke of the rosette is.
   */
  void drawOnDelay(float radius)
  {
    // draws a line every second:
    if ( millis() > time  && stroke < 16) {
      drawRosetteThree(radius, stroke, originalThree);
  
//      println("stroke = " + stroke + "; time = " + time + "; millis() = " + millis());
  
      time = millis() + 1000;
      stroke++;
    } // if
  } // drawOnDelay
  
  /**
   *  Draws one more stroke of rosetteThree each time a pitch threshold is passed.
   *
   *  @param  radius         ** a float specifiying how long each stroke of the rosette is.
   *  @param  thresholdFreq  a float specifying which pitch must be met or passed to draw a stroke.
   */
  void drawPastThreshold(float radius, float thresholdFreq)
  {
    // Force the threshold to be crossed multiple times before triggering?
   
    
    // Draws a line each time the pitch crossed a frequency threshold:
    if ( (input.getAdjustedFundAsHz() > thresholdFreq)  && (stroke < 16) ) {
      drawRosetteThree(radius, stroke, originalThree);
  
 //     println("stroke = " + stroke + "; input.getAdjustedFundAsHz() = " + input.getAdjustedFundAsHz(1) + "; thresholdFreq = " + thresholdFreq);
  
      stroke++;
    } // if
  } // drawPastThreshold(float)
  
  /**
   *  Draws the specified stroke of rosetteThree.
   *
   *  @param  radius         ** a float specifiying how long each stroke of the rosette is.
   *  @param  whichStroke    an int specifying which stroke should be drawn; must be between 0 and 15.
   *  @param  strokeColor    a color in which the rosette will be drawn.
   */
  void drawRosetteThree(float radius, int whichStroke, color strokeColor) {
    if (whichStroke > 15) {
      throw new IllegalArgumentException("Zikr_Scenes_EMM.drawRosetteThree: int parameter " + whichStroke + " is greater than the number of lines in the rosette.");
    }
  
    float x1 = radius*cos(PI/4*3*whichStroke);
    float x2 = radius*cos(PI/4*3*(whichStroke+1));
    float y1 = radius*sin(PI/4*3*whichStroke);
    float y2 = radius*sin(PI/4*3*(whichStroke+1));
    strokeWeight(3);
    stroke(220);
    line(x1, y1, x2, y2);
    strokeWeight(2.5);
    stroke(strokeColor);
    line(x1, y1, x2, y2);
  } // drawRosetteThree
  
  /**
   *  Sets stroke to 0.
   */
  void reset()
  {
    this.stroke = 0;
  } // reset
} // DrawRosette