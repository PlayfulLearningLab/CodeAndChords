/**
  * Elena Ryan
  * Testing HSB to RGB Functions
  * 11/9/17
  */
  
  void setup() {
    background(100,25,25);
    size(700, 700);
  }
  
  void draw() {
    /* if h is pressed, increase the hue by a set amount, if s is
     * pressed, increase the saturation by a set amount
     * if b is pressed, increase the brightness
     */
     if (keyPressed) {
      if (key == 'h' || key == 'H') {
      hu();
      }
      else if (key == 's' || key == 'S') {
        sat();
      }
      else if (key == 'b' || key == 'B') {
        bri();
      }
    } else {
    fill(255);
    }
  }
  
  
  void hu() {
    color c;
    c = g.backgroundColor;
    float v = hue(c)+3.0;
    float s = saturation(c);
    float b = brightness(c);
    colorMode(HSB);
    color n;
    n = color(v, s, b);
    background(n);
  }
  
  void sat() {
    color c;
    c = g.backgroundColor;
    float v = hue(c);
    float s = saturation(c);
    float b = brightness(c);
    colorMode(HSB);
    color n = color(v, s+2, b);
    background(n);
  }
  
  void bri() {
    color c;
    c = g.backgroundColor;
    float v = hue(c);
    float s = saturation(c);
    float b = brightness(c);
    colorMode(HSB);
    color n = color(v, s, b+1);
    background(n);
  }