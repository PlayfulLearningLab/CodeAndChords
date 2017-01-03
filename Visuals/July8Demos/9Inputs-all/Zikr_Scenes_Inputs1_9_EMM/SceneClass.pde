public abstract class Scene
{
  /*
    06/29/2016
   Emily Meuer
   
   Abstract class to allow a sketch to cycle through different scenes.
   
   (Could have a next and previous capability.)
   */
   
  // Calibrate:
  int  highPitch  = 500;    // num by which pitches will be divided before turning into a color;
                            // pitches above this will not be considered when changing the color,
                            // but the lower it is, the more each change in pitch will affect the color.

  float  red;      // red and blue are set in pitchColor;
  float  green;    // green must be set to 0 and += 30 each time something is drawn.
  float  blue;
  
  color  strokeColor;
  
  color  originalOne    = color(50, 50, 200);
  color  originalTwo    = color(50, 200, 50);
  color  originalThree  = color(200, 50, 50);
  // Original colors:
  //  one:    (50, 50, 200);
  //  two:    (50, 200, 50);
  //  three:  (200, 50, 50);

  float  x1, x2, y1, y2;  // Used for drawing the lines that make up each rosette

  Input  leftInput;
  Input  rightInput;
  
  Input  input;
  int    tenorCutoff;    // mics will be split into high and low, low mics all lines with numbers below this,
                         // high mics this mic and all with numbers above it.
                         // (e.g., if there are 9 mics and tenorCutoff = 5, low mics are 1 - 4, and high are 5 - 9.
  /**
   * All implementing subclasses must override this with their own run() functionality;
   * it will be called repeatedly in draw().
   */
  void run() {
  }

  /**
   * Sets the color based on the pitch of the low and high groups of mics (basses and tenors).
   */
  void pitchColor() {
    red   = Math.min(255 * (input.getAverageFund(1, this.tenorCutoff - 1) / highPitch), 255);
    blue  = Math.min(255 * (input.getAverageFund(this.tenorCutoff, input.numInputs) / highPitch), 255);
  } // pitchColor

  /**
   *  Outermost rosette.  Wide angles; has a large opening in the center.
   *
   *  @param  radius       ** a float with the length of the side (not actually a radius?)
   *  @param  strokeColor  color with which the rosette is to be drawn.
   */
  void rosettePartOne(float radius, color strokeColor) {
    for (int i = 0; i < 4; i++) {
      x1 = radius*cos(PI/2*i);
      x2 = radius*cos(PI/2*(i+1));
      y1 = radius*sin(PI/2*i);
      y2 = radius*sin(PI/2*(i+1));
      strokeWeight(4);
      stroke(220);
      line(x1, y1, x2, y2);
      strokeWeight(3);
      stroke(strokeColor);
      line(x1, y1, x2, y2);
    }
    for (int i = 0; i < 4; i++) {
      x1 = radius*cos(PI/2*i+PI/4);
      x2 = radius*cos(PI/2*(i+1)+PI/4);
      y1 = radius*sin(PI/2*i+PI/4);
      y2 = radius*sin(PI/2*(i+1)+PI/4);
      strokeWeight(4);
      stroke(220);
      line(x1, y1, x2, y2);
      strokeWeight(3);
      stroke(strokeColor);
      line(x1, y1, x2, y2);
    }
  } // rosettePartOne

  /**
   *  Middle rosette.
   *
   *  @param  radius       ** a float with the length of the side (not actually a radius?)
   *  @param  strokeColor  color with which the rosette is to be drawn.
   */
  void rosettePartTwo(float radius, color strokeColor) {
    for (int i = 0; i < 8; i++) {
      x1 = radius*cos(PI/4*3*i+PI/8);
      x2 = radius*cos(PI/4*3*(i+1)+PI/8);
      y1 = radius*sin(PI/4*3*i+PI/8);
      y2 = radius*sin(PI/4*3*(i+1)+PI/8);
      strokeWeight(4);
      stroke(220);
      line(x1, y1, x2, y2);
      strokeWeight(2);
      stroke(strokeColor);
      line(x1, y1, x2, y2);
    }
  } // rosettePartTwo

  /**
   *  Innermost rosette; has the smallest angles.
   *
   *  @param  radius       ** a float with the length of the side (not actually a radius?)
   *  @param  strokeColor  color with which the rosette is to be drawn.
   */
  void rosettePartThree(float radius, color strokeColor) {
    for (int i = 0; i < 16; i++) {
      x1 = radius*cos(PI/4*3*i);
      x2 = radius*cos(PI/4*3*(i+1));
      y1 = radius*sin(PI/4*3*i);
      y2 = radius*sin(PI/4*3*(i+1));
      strokeWeight(1);
      stroke(220);
      line(x1, y1, x2, y2);
      strokeWeight(.5);
      stroke(strokeColor);
      line(x1, y1, x2, y2);
    } // for
  } // rosettePartThree
} // Scene