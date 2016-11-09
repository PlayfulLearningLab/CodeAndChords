class RainingNumbers
{
  /*
  09/24/2016
   Emily Meuer
   
   Class whose instances rain down along a particular x value.
   */

  int     xPos;
  int     yDrawPos;
  int     yModPos;
  int     tail;      // how many trailing numbers should be left showing

  String  name;

  int     nextRain;
  int     delay; //        = 250; //500;
//  int[]   background   = new int[] { 200, 100, 100 };
  int[]   background       = new int[] { 0, 0, 0 };
  color   backgroundColor  = color(background[0], background[1], background[2]);
  color   textColor        = color(0, 204, 0);
  
  
  /**
   *  Constructor that creates a RainingNumbers object with a delay of 250 millis.
   *
   *  @param  xPos  an int that determines the x position of the RainingNumbers
   *  @param  name  a String that will appear at the top of the column
   *  @param  tail  an int that determines when the numbers will begin to fade out
   */
  RainingNumbers(int xPos, String name, int tail)
  {
    this(xPos, name, tail, 250);
  } // constructor

  /**
   *  Constructor that makes a RainingNumbers object.
   *
   *  @param  xPos  an int that determines the x position of the RainingNumbers
   *  @param  name  a String that will appear at the top of the column
   *  @param  tail  an int that determines when the numbers will begin to fade out
   *  @param  delay  an int that determines after how many milliseconds the next number should appear
   */
  RainingNumbers(int xPos, String name, int tail, int delay)
  {
    this.xPos      = xPos;
    this.yDrawPos  = 100;
    this.yModPos   = 0;
    this.tail      = tail + 1;    // we're dealing w/mod math, and the given tail is the *length* we want for the tail.
    this.delay     = delay;
    this.name      = name;
    
    fill(0);
    textSize(24);
    text(name, xPos, 35);
    
    textSize(18);
    this.nextRain  = millis() + delay;
  } // constructor(int)

  // in the future, could play with the z-axis of the text().
//  void rain(String value)
  void rain(float value)
  {
    int  tailDrawPos;            // position of value that will dissapear into background.
    int  tailModPos;             // used to calculate wrap-around to top of screen while keeping space for text at the top.
    
    // Print name of column at the top:
    fill(textColor);
    textSize(24);
    text(name, xPos, 55);
    
    if (millis() > this.nextRain)
    {
      // Draw the next value:
 //     stroke(0);
//      fill(0);

      textSize(18);
      text(value, xPos, yDrawPos);
      //      ellipse(xPos, yPos, 10, 10);
      yModPos   = (yModPos + 15) % (height - 100);
      yDrawPos  = yModPos + 100;
      
      /*
        The problem: I want the yPos values to go from 100 to height, then go back around.
        
        Mod by less than 100, and then add that much less? If I add then, makes a big gap every time.
        I need to add a small amt every time, except when I mod, which is when I need to add 100.
        Of course, I could do an if.  But what does it really mean?  It means that I --
        
        my position values: 100 - height.
        my mod values:      0 - (height - 100).
      */

      // Last value of the tail (to be completely blacked out this time around):
      tailDrawPos = ((height - 100) + yDrawPos - (this.tail * 15)) % (height - 100);
      tailModPos  = tailDrawPos - 100;
      println("RainingNumbers.rain: tailModPos = " + tailModPos);

      for (int i = 0; i < 8; i++)
      {
        // draw increasingly less transparent squares to reveal the numbers on the tail end:
        stroke(this.background[0], this.background[1], this.background[2], i * 30);
        fill(this.background[0], this.background[1], this.background[2], i * 30);
        rect(xPos, tailDrawPos, 175, 15);

        //        textSize(18);
        //        text(value, xPos, tailPos);

        //        ellipse(xPos, tailPos, 15, 15);

        tailModPos = ((height - 100) + tailModPos - 15) % (height - 100);
        tailDrawPos = (tailModPos + 100) % height;
      } // for


      this.nextRain = this.nextRain + this.delay;
    } // if - delay
  } // rain
} // class - RainingNumbers