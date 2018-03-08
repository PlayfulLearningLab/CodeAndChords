import processing.core.PApplet;

class RainingNumbers
{
	/**
	 * 11/20/2016
	 * Adapted for Eclipse.
	 * 
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
	int[]	background      = new int[] { 0, 0, 0 };
//	int   backgroundColor; //	= parent.color(background[0], background[1], background[2]);
	int[]	text			= new int[] { 0, 0, 0 };
	int		titleSize		= 18;
	int		textSize		= 14;

	PApplet	parent;


	/**
	 *  Constructor that creates a RainingNumbers object with a delay of 250 millis.
	 *
	 *  @param  xPos  an int that determines the x position of the RainingNumbers
	 *  @param  name  a String that will appear at the top of the column
	 *  @param  tail  an int that determines when the numbers will begin to fade out
	 */
	RainingNumbers(PApplet parent, int xPos, String name, int tail)
	{
		this(parent, xPos, name, tail, 250);
	} // constructor

	/**
	 *  Constructor that makes a RainingNumbers object.
	 *
	 *  @param  xPos  an int that determines the x position of the RainingNumbers
	 *  @param  name  a String that will appear at the top of the column
	 *  @param  tail  an int that determines when the numbers will begin to fade out
	 *  @param  delay  an int that determines after how many milliseconds the next number should appear
	 */
	RainingNumbers(PApplet parent, int xPos, String name, int tail, int delay)
	{
		this.parent	= parent;
		this.xPos      = xPos;
		this.yDrawPos  = 100;
		this.yModPos   = 0;
		this.tail      = tail + 1;    // we're dealing w/mod math, and the given tail is the *length* we want for the tail.
		this.delay     = delay;
		this.name      = name;

		//    fill(0, 0, 0);
		parent.fill(text[0], text[1], text[2]);
		parent.textSize(this.titleSize);
		parent.text(name, xPos, 55);

		parent.textSize(this.textSize);
		this.nextRain  = parent.millis() + delay;
	} // constructor(int)

	// in the future, could play with the z-axis of the text().
	//  void rain(String value)
	void rain(float value)
	{
		// Problem: resetting background() is erasing them every time...
		// For this sketch, I just draw a new rect() instead of a new background color in draw();
		// if it continues to be a problem in other places, could make an array of past values.
		
		int  tailDrawPos;            // position of value that will dissapear into background.
		int  tailModPos;             // used to calculate wrap-around to top of screen while keeping space for text at the top.

/*
		// Print name of column at the top:
		parent.fill(text[0], text[1], text[2]);
		parent.textSize(this.titleSize);
		parent.text(name, xPos, 55);
*/

		parent.fill(text[0], text[1], text[2]);
		if (parent.millis() > this.nextRain)
		{
			// Draw the next value:
			parent.textSize(this.textSize);
			parent.text(value, xPos, yDrawPos);
			yModPos   = (yModPos + 15) % (parent.height - 100);
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
			tailDrawPos = ((parent.height - 100) + yDrawPos - (this.tail * 15)) % (parent.height - 100);
			tailModPos  = tailDrawPos - 100;
			
			for (int i = 0; i < 8; i++)
			{
				// draw increasingly less transparent squares to reveal the numbers on the tail end:
				parent.stroke(this.background[0], this.background[1], this.background[2], i * 30);
				parent.fill(this.background[0], this.background[1], this.background[2], i * 30);
				parent.rect(xPos, tailDrawPos, 175, 15);

				//        textSize(18);
				//        text(value, xPos, tailPos);

				//        ellipse(xPos, tailPos, 15, 15);

				tailModPos = ((parent.height - 100) + tailModPos - 15) % (parent.height - 100);
				tailDrawPos = (tailModPos + 100) % parent.height;
			} // for


			this.nextRain = this.nextRain + this.delay;
		} // if - delay
	} // rain

	public void setBackgroundColor(int red, int green, int blue)
	{
		//	  this.backgroundColor	= color(red, green, blue);
		this.background[0]	= red;
		this.background[1]	= green;
		this.background[2]	= blue;
	} // setBackgroundColor

	public void setTextColor(int red, int green, int blue)
	{
		this.text[0]	= red;
		this.text[1]	= green;
		this.text[2]	= blue;
	} // setTextColor
	
	public void setTitleSize(int titleSize)
	{
		this.titleSize = titleSize;
	} // set TitleSize
	
	public void setTextSize(int textSize)
	{
		this.textSize = textSize;
	} // setTextSize
} // class - RainingNumbers