import interfascia.*;
/*
  10/24/2016
 Emily Meuer
 
 Wrapper class to make it easier to use interfascia buttons.
 */

class Button
{
  boolean  rect;          // true if button is rectangle
  /*
  int rectX, rectY;      // Position of square button
   int circleX, circleY;  // Position of circle button
   int rectSize = 90;     // Diameter of rect
   int circleSize = 93;   // Diameter of circle
   color rectColor, circleColor, baseColor;
   color rectHighlight, circleHighlight;
   color currentColor;
   */
  int    x;
  int    y;
  int    diameter;
  color  offColor;
  color  onColor;
  color  highlight;
  color[]  colors;
  boolean  over;
  boolean  on;
  private int      state;
  /*
  boolean rectOver = false;
   boolean circleOver = false;
   */

  Button(int x, int y, int diameter, color offColor, color onColor, color highlight, int state, boolean rect)
  {
    this.x  = x;
    this.x  = y;
    this.diameter  = diameter;
    this.offColor  = offColor;
    this.onColor   = onColor;
    this.highlight  = highlight;
    this.colors    = new color[] { offColor, onColor }; // colors
    this.over      = false;
    this.state     = state;
    this.on        = on;

    this.rect  = rect;
    ellipseMode(CENTER);

    update(mouseX, mouseY);
  } // constructor
  /*
  void setup() {
   size(640, 360);
   rectColor = color(0);
   rectHighlight = color(51);
   circleColor = color(255);
   circleHighlight = color(204);
   baseColor = color(102);
   currentColor = baseColor;
   circleX = width/2+circleSize/2+10;
   circleY = height/2;
   rectX = width/2-rectSize-10;
   rectY = height/2-rectSize/2;
   ellipseMode(CENTER);
   }
   
   void draw() {
   update(mouseX, mouseY);
   background(currentColor);
   
   if (rectOver) {
   fill(rectHighlight);
   } else {
   fill(rectColor);
   }
   stroke(255);
   rect(rectX, rectY, rectSize, rectSize);
   
   if (circleOver) {
   fill(circleHighlight);
   } else {
   fill(circleColor);
   }
   stroke(0);
   ellipse(circleX, circleY, circleSize, circleSize);
   }
   */
   
   void update()
   {
     this.update(this.x, this.y);
   } // update()

  void update(int x, int y) 
  {
    // determine whether or not the mouse is over the current button:
    if (rect)
    {
      over = overRect(x, y, diameter, diameter);
    } else {
      over = overCircle(x, y, diameter);
    } // else

    // determine the correct fill color:
    if (over) {
      fill(highlight);
    } else {
      fill(colors[state]);
    }
    stroke(255);

    // draw the button:
    if (rect)
    {
      rect(x, y, diameter, diameter);
    } else {
      ellipse(x, y, diameter, diameter);
    } // else = draw
  } // update(int x, int y) 

  void mousePressed() {
    if (over) {
      this.state  = (state + 1) % 2;
      println("button was pressed");
    }
  }

  boolean overRect(int x, int y, int width, int height) {
    if (mouseX >= x && mouseX <= x+width && 
      mouseY >= y && mouseY <= y+height) {
      return true;
    } else {
      return false;
    }
  } // overRect

  boolean overCircle(int x, int y, int diameter) {
    float disX = x - mouseX;
    float disY = y - mouseY;
    if (sqrt(sq(disX) + sq(disY)) < diameter/2 ) {
      return true;
    } else {
      return false;
    }
  } // overCircle

  boolean  getOn()
  {
    return this.on;
  } // getOn
} // Button