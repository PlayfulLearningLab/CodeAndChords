/*
  07/21/2016
 Emily Meuer
 Update to add loops (rather than manually adjust which pictures blur).
 
 07/07/2016
 Emily Meuer
 Update to integrate more than 4 inputs.
 
 07/04/2016
 Erin Kern
 updates by Emily Meuer to WM_PointsAndBlurring_LR_EAK,
 integrating the new input class and, thus, multiple inputs through Jack.
 */

// Calibrate:
int  volumeAdjust  = 2;   // not currently used.

// The following is kept for the info it gives about picture spacing;
// however, variables are no longer used, as the images are loaded directly into their arrays.

// Previous version comments:
//Creating variables for all the images used. Each picture is numbered based 
//on its spot in the grid (i.e. the top left picture is "one" and 
//the bottom right picture is "ten"). Pictures are also categorized by their scene number
//(i.e. the second scene has pictures titled "oneB", "twoB", etc.).
//Pictures with "blur" in the name are just the blurred version of the original picture
//(i.e. "oneBblur" is the blurred version of "oneB").
PImage      five;

Input       input;

//Creating variables for the size of the dots used in the center picture.
int smallPoint = 4;
int largePoint = 4;

//Creating a variable to manage the scene number.
int scene = 0;

PImage[] displayed;
PImage[] displayedBlur;
PImage[] A;
PImage[] Ablur;
PImage[] B;
PImage[] Bblur;
PImage[] C;
PImage[] Cblur;
PImage[][]  allImages;
PImage[][]  allBlurs;
int[]    levels;

int  waitUntil;
// arrayLoc and picLoc used to determine which pics to change in each scene:
int  arrayLoc;
int  picLoc;

void setup()
{
  //Setting up background and making it full screen.
  fullScreen();
  background(0);

  displayed      = new PImage[9];
  displayedBlur  = new PImage[9];

  allImages  = new PImage[3][];
  allBlurs   = new PImage[3][];

  //Loading all the images ... 

  five  = loadImage("fiveRGB.jpg");

  A  = new PImage[] { 
    loadImage("one.jpeg"), 
    loadImage("two.jpg"), 
    loadImage("three.jpg"), 
    loadImage("four.jpg"), 
    //    loadImage("fiveRGB.jpg"), 
    loadImage("six.jpg"), 
    loadImage("seven.jpg"), 
    loadImage("eight.jpg"), 
    loadImage("nine.jpg"), 
    loadImage("ten.jpg"), 
  };
  allImages[0]  = A;


  B  = new PImage[] { 
    loadImage("oneB.jpg"), 
    loadImage("twoB.jpg"), 
    loadImage("threeB.jpg"), 
    loadImage("fourB.jpg"), 
    loadImage("sixB.jpg"), 
    loadImage("sevenB.jpg"), 
    loadImage("eightB.jpg"), 
    loadImage("nineB.jpg"), 
    loadImage("tenB.jpg"), 
  };
  allImages[1]  = B;

  C  = new PImage[] { 

    loadImage("oneC.jpg"), 
    loadImage("twoC.jpg"), 
    loadImage("threeC.jpg"), 
    loadImage("fourC.jpg"), 
    loadImage("sixC copy.jpg"), 
    loadImage("sevenC.jpg"), 
    loadImage("eightC.jpg"), 
    loadImage("nineC.jpeg"), 
    loadImage("tenC.jpeg"), 
  };
  allImages[2]  = C;


  Ablur  = new PImage[] {
    loadImage("oneBlur.jpg"), 
    loadImage("twoBlur.jpg"), 
    loadImage("threeBlur.jpg"), 
    loadImage("fourBlur.jpg"), 
    loadImage("sixBlur.jpg"), 
    loadImage("sevenBlur.jpg"), 
    loadImage("eightBlur.jpg"), 
    loadImage("nineBlur.jpg"), 
    loadImage("tenBlur.jpg"), 
  };
  allBlurs[0]  = Ablur;

  Bblur  = new PImage[] { 

    loadImage("oneBblur.jpg"), 
    loadImage("twoBblur.jpg"), 
    loadImage("threeBblur.jpg"), 
    loadImage("fourBblur.jpg"), 
    loadImage("sixBblur.jpg"), 
    loadImage("sevenBblur.jpg"), 
    loadImage("eightBblur.jpg"), 
    loadImage("nineBblur.jpg"), 
    loadImage("tenBblur.jpg"), 
  };
  allBlurs[1]  = Bblur;

  Cblur  = new PImage[] { loadImage("oneCblur.jpg"), 
    loadImage("twoCblur.jpg"), 
    loadImage("threeCblur.jpg"), 
    loadImage("fourCblur.jpg"), 
    loadImage("sixCblur.jpg"), 
    loadImage("sevenCblur.jpg"), 
    loadImage("eightCblur.jpg"), 
    loadImage("nineCblur.jpg"), 
    loadImage("tenCblur.jpg"), 
  };
  allBlurs[2]  = Cblur;

  //Resizing all the images ... (five is done separately because it isn't in the allImages or allBlurs array)

  five.resize(width/2, height/2);
  for (int k = 0; k < allImages.length; k++)
  {
    allImages[k][0].resize(width/4, height/4);  // one
    allImages[k][1].resize(width/2, height/4);  // two 
    allImages[k][2].resize(width/4, height/4);  // three
    allImages[k][3].resize(width/4, height/2);  // four
    allImages[k][4].resize(width/4, height/2);  // six
    allImages[k][5].resize(width/4, height/4);  // seven
    allImages[k][6].resize(width/4, height/4);  // eight
    allImages[k][7].resize(width/4, height/4);  // nine
    allImages[k][8].resize(width/4, height/4);  // ten

    allBlurs[k][0].resize(width/4, height/4);  // one
    allBlurs[k][1].resize(width/2, height/4);  // two 
    allBlurs[k][2].resize(width/4, height/4);  // three
    allBlurs[k][3].resize(width/4, height/2);  // four
    allBlurs[k][4].resize(width/4, height/2);  // six
    allBlurs[k][5].resize(width/4, height/4);  // seven
    allBlurs[k][6].resize(width/4, height/4);  // eight
    allBlurs[k][7].resize(width/4, height/4);  // nine
    allBlurs[k][8].resize(width/4, height/4);  // ten
  } // for - resizing all images


  //Creating a new Input object for 9 mics:
  input = new Input(9, new AudioContext());

  // prints the location of each image:
  for (int i = 0; i < 9; i++)
  {
    println(i + ": ");
    int[] loc = getImageXandY(i);
    for (int j = 0; j < loc.length; j++)
    {
      print("  " + j + ": " + loc[j]);
    } // for - j
    println();
  }

  waitUntil  = millis();
  noStroke();
} // setup()

void draw()
{

  //Keeping track of the scene number. Scene is changed by pressing the spacebar.
  if (keyPressed && (millis() > waitUntil)) {
    scene ++;
    println(scene);

    waitUntil = millis() + 500;
  } // if - scene change

  // Which set of images to draw from:
  arrayLoc  = scene / 9;
  // At which image to change over to the previous set of images:
  picLoc    = scene % 9;

  int i;

  // Set which images will be from the next set, and will will be from the previous set:
  for (i = 0; i < picLoc; i++)
  {
    if (arrayLoc+1 < allImages.length) {
      displayed[i]      = allImages[arrayLoc+1][i];
      displayedBlur[i]  = allBlurs[arrayLoc+1][i];
    } // if
  } // for

  for (i = picLoc; i < allImages[arrayLoc].length; i++)
  {
    println("i = " + i);
    if (arrayLoc < allImages.length) {
      displayed[i]      = allImages[arrayLoc][i];
      displayedBlur[i]  = allBlurs[arrayLoc][i];
    } // if
  } // for

  // coordinates used for displaying blurred and then clear images:
  int[]  coordinates;

  //Drawing blurred images over background
  for (i = 0; i < displayedBlur.length; i++)
  {
    // Since count starts at 0, call ignoreFive to include the 5th image connected to a voice --
    // level 5, voice 5, image 5, but at location 6, b/c of middle picture.
    int x = ignoreFive(i);

    coordinates  = getImageXandY(x);
    image(displayedBlur[i], coordinates[0], coordinates[1]);
  } // for - displayed blurred images

  // volume of input:
  levels  = new int[9];

  for (int j = 0; j < levels.length; j++)
  { 
    levels[j]  = (int)Math.floor(input.getAmplitude((j%2) + 1) / volumeAdjust);
  } // for - find amp levels

  //showing regular images as amplitude of each part is adjusted
  for (i = 0; i < displayed.length; i++)
  {
    tint(255, (Math.min(levels[i], 255)));

    // Since count starts at 0, call ignoreFive to include the 5th image connected to a voice --
    // level 5, voice 5, image 5, but at location 6, b/c of middle picture.
    int x = ignoreFive(i);

    coordinates  = getImageXandY(x);
    image(displayed[i], coordinates[0], coordinates[1]);
  } // for - display blurred-over-clear pics

  //Pointillizing center image 
  float pointillizeFive = map((Math.min(input.getAmplitude(1), 30)), 0, 30, smallPoint, largePoint);
  int randPixelNum = (int)(random(five.pixels.length));
  //  int randPixel = five.pixels[randPixelNum];
  int xFive = randPixelNum%five.width;
  int yFive = randPixelNum/five.width;
  color pixFive = five.get(xFive, yFive);
  int[] cornerXY = getImageXandY(4);

  //Making sure points are showing up only in the center area
  for (i = 0; i < cornerXY.length; i++) {
    fill(pixFive, 128);
    ellipse(xFive + cornerXY[0], yFive + cornerXY[1], pointillizeFive, pointillizeFive);
  }
} // draw()

int ignoreFive(int num)
{
  int x;

  if (num >= 4) {  
    x = num+1;
  } else {
    x = num;
  } // if - x

  return x;
} // ignoreFie

/**
 * Returns the x and y coordinates of the upper left corner of an image,
 * where (0,0) is the upper left-hand corner.
 *
 * @param   imageNum   An int specifying for which image to find the coordinates; numbered 0-9.
 * @return  int[]      The x and y coordinates of the corner of the image, in the 0 and 1 positions of the array, respectively.
 */
int[] getImageXandY(int imageNum)
{
  int x;
  int y;

  //  println("getImageXandY: imageNum = " + imageNum);
  // takes into account the different sizing of images on the bottom row:
  if (imageNum == 8)
  {
    // 9 is in the center of the screen:
    x = width / 2;
    y = queryArray(height, imageNum / 3);
  } else if (imageNum == 9)
  {
    // 10 (9) is in the position that 9 (8) would have been:
    x = queryArray(width, 2);
    y = queryArray(height, 2);
  } else {
    x = queryArray(width, imageNum % 3);
    y = queryArray(height, imageNum / 3);
  } // else

  return new int[] { x, y };
} 

/**
 * Determines whether either the x or y corner coordinate of an image is at 0, (height or width)/4, or 3*([height or width]/4).
 * (That is, is it in the first, second, or third row of images.)
 *
 * @param  x    int specifying whether to base the following calculations on height or on width.
 * @param  loc  int specifying which image is in question; must be between 0 and 2.
 *
 * @return int  0, x/4, or 3*(x/4), depending on whether the image is in the first, second, or third row or column.
 */
int queryArray(int x, int loc)
{
  if (loc < 0) {
    throw new IllegalArgumentException("WM_loops.queryArray: int parameter " + loc + " must be between 0 and 2.");
  } // if
  if (loc > 2) {
    throw new IllegalArgumentException("WM_loops.queryArray: int parameter " + loc + " must be between 0 and 2.");
  } // if

  int[] array = {0, x/4, 3*(x/4)  };

  if (loc > array.length) {  
    throw new IllegalArgumentException("WM_PointsMultiplePictures_EMM.queryArray: loc " + loc + " is out of bounds; should be less than array.length " + array.length);
  }

  return array[loc];
}