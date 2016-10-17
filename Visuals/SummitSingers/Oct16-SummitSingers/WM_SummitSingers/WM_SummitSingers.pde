/*
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

//Creating variables for all the images used. Each picture is numbered based 
//on its spot in the grid (i.e. the top left picture is "one" and 
//the bottom right picture is "ten"). Pictures are also categorized by their scene number
//(i.e. the second scene has pictures titled "oneB", "twoB", etc.).
//Pictures with "blur" in the name are just the blurred version of the original picture
//(i.e. "oneBblur" is the blurred version of "oneB").
PImage      one;
PImage      two;
PImage      three;
PImage      four;
PImage      five;
PImage      six;
PImage      seven;
PImage      eight;
PImage      nine;
PImage      ten;

PImage      oneBlur;
PImage      twoBlur;
PImage      threeBlur;
PImage      fourBlur;
PImage      fiveBlur;
PImage      sixBlur; 
PImage      sevenBlur;
PImage      eightBlur;
PImage      nineBlur;
PImage      tenBlur;

PImage      oneB;
PImage      twoB;
PImage      threeB;
PImage      fourB;
PImage      sixB;
PImage      sevenB;
PImage      eightB;
PImage      nineB;
PImage      tenB;

PImage      oneC;
PImage      twoC;
PImage      threeC;
PImage      fourC;
PImage      sixC;
PImage      sevenC;
PImage      eightC;
PImage      nineC;
PImage      tenC;

PImage      oneD;
PImage      twoD;
PImage      threeD;
PImage      fourD;
PImage      sixD;
PImage      sevenD;
PImage      eightD;
PImage      nineD;
PImage      tenD;

PImage      oneBblur;
PImage      twoBblur;
PImage      threeBblur;
PImage      fourBblur;
PImage      sixBblur;
PImage      sevenBblur;
PImage      eightBblur;
PImage      nineBblur;
PImage      tenBblur;

PImage      oneCblur;
PImage      twoCblur;
PImage      threeCblur;
PImage      fourCblur;
PImage      sixCblur;
PImage      sevenCblur;
PImage      eightCblur;
PImage      nineCblur;
PImage      tenCblur;

PImage      oneDblur;
PImage      twoDblur;
PImage      threeDblur;
PImage      fourDblur;
PImage      sixDblur;
PImage      sevenDblur;
PImage      eightDblur;
PImage      nineDblur;
PImage      tenDblur;

//Creating universal variables for the pictures so they can be easily changed for each scene.

PImage      first;
PImage      second;
PImage      third;
PImage      fourth;
PImage      fifth;
PImage      sixth;
PImage      seventh;
PImage      eighth;
PImage      ninth;
PImage      tenth;

PImage      firstBlur;
PImage      secondBlur;
PImage      thirdBlur;
PImage      fourthBlur;
PImage      fifthBlur;
PImage      sixthBlur; 
PImage      seventhBlur;
PImage      eighthBlur;
PImage      ninthBlur;
PImage      tenthBlur;

Input       input;

//Creating variables for the 9 different sound inputs.
int oneLevel;
int twoLevel;
int threeLevel;
int fourLevel;
int fiveLevel;
int sixLevel;
int sevenLevel;
int eightLevel;
int nineLevel;
int tenLevel;

//Creating variables for the size of the dots used in the center picture.
int smallPoint = 4;
int largePoint = 4;

//Creating a variable to manage the scene number.
int scene = 1;

void setup()
{
  //Setting up background and making it full screen.
  fullScreen();
  background(0);

  //Loading all the images ... 
  one  = loadImage("1.jpg");
  two  = loadImage("2.jpg");
  three = loadImage("3.jpg");
  four = loadImage("4.jpg");
  five = loadImage("PIC.jpg");
  //five = loadImage("five.jpg");
  six = loadImage("5.jpg");
  seven = loadImage("6.jpg");
  eight = loadImage("7.jpg");
  nine = loadImage("8.jpg");
  ten = loadImage("9.jpg");

  oneB = loadImage("10.jpg");
  twoB = loadImage("11.jpg");
  threeB = loadImage("12.jpg");
  fourB = loadImage("13.jpg");
  sixB = loadImage("14.jpg");
  sevenB = loadImage("15.jpg");
  eightB = loadImage("16.jpg");
  nineB = loadImage("17.jpg");
  tenB = loadImage("18.jpg");

  oneC = loadImage("19.jpg");
  twoC = loadImage("20.jpg");
  threeC = loadImage("21.jpg");
  fourC = loadImage("22.jpg");
  sixC = loadImage("1.jpg");
  sevenC = loadImage("2.jpg");
  eightC = loadImage("3.jpg");
  nineC = loadImage("4.jpg");
  tenC = loadImage("5.jpg");

  oneBlur  = loadImage("1B.jpg");
  twoBlur  = loadImage("2B.jpg");
  threeBlur = loadImage("3B.jpg");
  fourBlur = loadImage("4B.jpg");
  //fiveBlur = loadImage("fiveBlur.jpg");
  sixBlur = loadImage("5B.jpg");
  sevenBlur = loadImage("6B.jpg");
  eightBlur = loadImage("7B.jpg");
  nineBlur = loadImage("8B.jpg");
  tenBlur = loadImage("9B.jpg");

  oneBblur  = loadImage("10B.jpg");
  twoBblur  = loadImage("11B.jpg");
  threeBblur = loadImage("12B.jpg");
  fourBblur = loadImage("13B.jpg");
  //fiveBlur = loadImage("fiveBblur.jpg");
  sixBblur = loadImage("14B.jpg");
  sevenBblur = loadImage("15B.jpg");
  eightBblur = loadImage("16B.jpg");
  nineBblur = loadImage("17B.jpg");
  tenBblur = loadImage("18B.jpg");

  oneCblur  = loadImage("19B.jpg");
  twoCblur  = loadImage("20B.jpg");
  threeCblur = loadImage("21B.jpg");
  fourCblur = loadImage("22B.jpg");
  sixCblur = loadImage("1B.jpg");
  sevenCblur = loadImage("2B.jpg");
  eightCblur = loadImage("3B.jpg");
  nineCblur = loadImage("4B.jpg");
  tenCblur = loadImage("5B.jpg");

  //Resizing all the images ...
  one.resize(width/4, height/4); 
  two.resize(width/2, height/4); 
  three.resize(width/4, height/4); 
  four.resize(width/4, height/2); 
  five.resize(width/2, height/2); 
  six.resize(width/4, height/2); 
  seven.resize(width/4, height/4); 
  eight.resize(width/4, height/4); 
  nine.resize(width/4, height/4);
  ten.resize(width/4, height/4);

  oneB.resize(width/4, height/4); 
  twoB.resize(width/2, height/4); 
  threeB.resize(width/4, height/4); 
  fourB.resize(width/4, height/2); 
  sixB.resize(width/4, height/2); 
  sevenB.resize(width/4, height/4); 
  eightB.resize(width/4, height/4); 
  nineB.resize(width/4, height/4);
  tenB.resize(width/4, height/4);

  oneC.resize(width/4, height/4); 
  twoC.resize(width/2, height/4); 
  threeC.resize(width/4, height/4); 
  fourC.resize(width/4, height/2); 
  sixC.resize(width/4, height/2); 
  sevenC.resize(width/4, height/4); 
  eightC.resize(width/4, height/4); 
  nineC.resize(width/4, height/4);
  tenC.resize(width/4, height/4);


  oneBlur.resize(width/4, height/4); 
  twoBlur.resize(width/2, height/4); 
  threeBlur.resize(width/4, height/4); 
  fourBlur.resize(width/4, height/2); 
  //fiveBlur.resize(width/2, height/2); 
  sixBlur.resize(width/4, height/2); 
  sevenBlur.resize(width/4, height/4); 
  eightBlur.resize(width/4, height/4); 
  nineBlur.resize(width/4, height/4);
  tenBlur.resize(width/4, height/4);

  oneBblur.resize(width/4, height/4); 
  twoBblur.resize(width/2, height/4); 
  threeBblur.resize(width/4, height/4); 
  fourBblur.resize(width/4, height/2); 
  sixBblur.resize(width/4, height/2); 
  sevenBblur.resize(width/4, height/4); 
  eightBblur.resize(width/4, height/4); 
  nineBblur.resize(width/4, height/4);
  tenBblur.resize(width/4, height/4);

  oneCblur.resize(width/4, height/4); 
  twoCblur.resize(width/2, height/4); 
  threeCblur.resize(width/4, height/4); 
  fourCblur.resize(width/4, height/2); 
  sixCblur.resize(width/4, height/2); 
  sevenCblur.resize(width/4, height/4); 
  eightCblur.resize(width/4, height/4); 
  nineCblur.resize(width/4, height/4);
  tenCblur.resize(width/4, height/4);

  //I did not do this part ... Emily? 
  input = new Input(13);
  

  noStroke();


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
}

void draw()
{

  //Keeping rack of the scene number. Scene is changed by pressing the spacebar.
  if (keyPressed) {
    scene ++;
    delay(500);
    println(scene);
  }


  //Changing which pictures show up based on scene ... the longhand way.
  if (scene == 1) {
    first = one;
    second = two;
    third = three;
    fourth = four;
    fifth = five;
    sixth = six;
    seventh = seven;
    eighth = eight;
    ninth = nine;
    tenth = ten;

    firstBlur = oneBlur;
    secondBlur = twoBlur;
    thirdBlur = threeBlur;
    fourthBlur = fourBlur;
    fifthBlur = fiveBlur;
    sixthBlur = sixBlur;
    seventhBlur = sevenBlur;
    eighthBlur = eightBlur;
    ninthBlur = nineBlur;
    tenthBlur = tenBlur;
  }

  if (scene == 2) {

    first = oneB;
    second = two;
    third = three;
    fourth = four;
    fifth = five;
    sixth = six;
    seventh = seven;
    eighth = eight;
    ninth = nine;
    tenth = ten;

    firstBlur = oneBblur;
    secondBlur = twoBlur;
    thirdBlur = threeBlur;
    fourthBlur = fourBlur;
    fifthBlur = fiveBlur;
    sixthBlur = sixBlur;
    seventhBlur = sevenBlur;
    eighthBlur = eightBlur;
    ninthBlur = nineBlur;
    tenthBlur = tenBlur;
  }

  if (scene == 3) {

    first = oneB;
    second = twoB;
    third = three;
    fourth = four;
    fifth = five;
    sixth = six;
    seventh = seven;
    eighth = eight;
    ninth = nine;
    tenth = ten;

    firstBlur = oneBblur;
    secondBlur = twoBblur;
    thirdBlur = threeBlur;
    fourthBlur = fourBlur;
    fifthBlur = fiveBlur;
    sixthBlur = sixBlur;
    seventhBlur = sevenBlur;
    eighthBlur = eightBlur;
    ninthBlur = nineBlur;
    tenthBlur = tenBlur;
  }

  if (scene == 4) {

    first = oneB;
    second = twoB;
    third = threeB;
    fourth = four;
    fifth = five;
    sixth = six;
    seventh = seven;
    eighth = eight;
    ninth = nine;
    tenth = ten;

    firstBlur = oneBblur;
    secondBlur = twoBblur;
    thirdBlur = threeBblur;
    fourthBlur = fourBlur;
    fifthBlur = fiveBlur;
    sixthBlur = sixBlur;
    seventhBlur = sevenBlur;
    eighthBlur = eightBlur;
    ninthBlur = nineBlur;
    tenthBlur = tenBlur;
  }

  if (scene == 5) {

    first = oneB;
    second = twoB;
    third = threeB;
    fourth = fourB;
    fifth = five;
    sixth = six;
    seventh = seven;
    eighth = eight;
    ninth = nine;
    tenth = ten;

    firstBlur = oneBblur;
    secondBlur = twoBblur;
    thirdBlur = threeBblur;
    fourthBlur = fourBblur;
    fifthBlur = fiveBlur;
    sixthBlur = sixBlur;
    seventhBlur = sevenBlur;
    eighthBlur = eightBlur;
    ninthBlur = nineBlur;
    tenthBlur = tenBlur;
  }

  if (scene == 6) {

    first = oneB;
    second = twoB;
    third = threeB;
    fourth = fourB;
    fifth = five;
    sixth = sixB;
    seventh = seven;
    eighth = eight;
    ninth = nine;
    tenth = ten;

    firstBlur = oneBblur;
    secondBlur = twoBblur;
    thirdBlur = threeBblur;
    fourthBlur = fourBblur;
    fifthBlur = fiveBlur;
    sixthBlur = sixBblur;
    seventhBlur = sevenBlur;
    eighthBlur = eightBlur;
    ninthBlur = nineBlur;
    tenthBlur = tenBlur;
  }

  if (scene == 7) {

    first = oneB;
    second = twoB;
    third = threeB;
    fourth = fourB;
    fifth = five;
    sixth = sixB;
    seventh = sevenB;
    eighth = eight;
    ninth = nine;
    tenth = ten;

    firstBlur = oneBblur;
    secondBlur = twoBblur;
    thirdBlur = threeBblur;
    fourthBlur = fourBblur;
    fifthBlur = fiveBlur;
    sixthBlur = sixBblur;
    seventhBlur = sevenBblur;
    eighthBlur = eightBlur;
    ninthBlur = nineBlur;
    tenthBlur = tenBlur;
  }

  if (scene == 8) {

    first = oneB;
    second = twoB;
    third = threeB;
    fourth = fourB;
    fifth = five;
    sixth = sixB;
    seventh = sevenB;
    eighth = eightB;
    ninth = nine;
    tenth = ten;

    firstBlur = oneBblur;
    secondBlur = twoBblur;
    thirdBlur = threeBblur;
    fourthBlur = fourBblur;
    fifthBlur = fiveBlur;
    sixthBlur = sixBblur;
    seventhBlur = sevenBblur;
    eighthBlur = eightBblur;
    ninthBlur = nineBlur;
    tenthBlur = tenBlur;
  }

  if (scene == 9) {

    first = oneB;
    second = twoB;
    third = threeB;
    fourth = fourB;
    fifth = five;
    sixth = sixB;
    seventh = sevenB;
    eighth = eightB;
    ninth = nineB;
    tenth = ten;

    firstBlur = oneBblur;
    secondBlur = twoBblur;
    thirdBlur = threeBblur;
    fourthBlur = fourBblur;
    fifthBlur = fiveBlur;
    sixthBlur = sixBblur;
    seventhBlur = sevenBblur;
    eighthBlur = eightBblur;
    ninthBlur = nineBblur;
    tenthBlur = tenBlur;
  }

  if (scene == 10) {

    first = oneB;
    second = twoB;
    third = threeB;
    fourth = fourB;
    fifth = five;
    sixth = sixB;
    seventh = sevenB;
    eighth = eightB;
    ninth = nineB;
    tenth = tenB;

    firstBlur = oneBblur;
    secondBlur = twoBblur;
    thirdBlur = threeBblur;
    fourthBlur = fourBblur;
    fifthBlur = fiveBlur;
    sixthBlur = sixBblur;
    seventhBlur = sevenBblur;
    eighthBlur = eightBblur;
    ninthBlur = nineBblur;
    tenthBlur = tenBblur;
  }

  if (scene == 11) {
    first = oneC;
    second = twoB;
    third = threeB;
    fourth = fourB;
    fifth = five;
    sixth = sixB;
    seventh = sevenB;
    eighth = eightB;
    ninth = nineB;
    tenth = tenB;

    firstBlur = oneCblur;
    secondBlur = twoBblur;
    thirdBlur = threeBblur;
    fourthBlur = fourBblur;
    fifthBlur = fiveBlur;
    sixthBlur = sixBblur;
    seventhBlur = sevenBblur;
    eighthBlur = eightBblur;
    ninthBlur = nineBblur;
    tenthBlur = tenBblur;
  }

  if (scene == 12) {

    first = oneC;
    second = twoC;
    third = threeB;
    fourth = fourB;
    fifth = five;
    sixth = sixB;
    seventh = sevenB;
    eighth = eightB;
    ninth = nineB;
    tenth = tenB;

    firstBlur = oneCblur;
    secondBlur = twoCblur;
    thirdBlur = threeBblur;
    fourthBlur = fourBblur;
    fifthBlur = fiveBlur;
    sixthBlur = sixBblur;
    seventhBlur = sevenBblur;
    eighthBlur = eightBblur;
    ninthBlur = nineBblur;
    tenthBlur = tenBblur;
  }

  if (scene == 13) {

    first = oneC;
    second = twoC;
    third = threeC;
    fourth = fourB;
    fifth = five;
    sixth = sixB;
    seventh = sevenB;
    eighth = eightB;
    ninth = nineB;
    tenth = tenB;

    firstBlur = oneCblur;
    secondBlur = twoCblur;
    thirdBlur = threeCblur;
    fourthBlur = fourBblur;
    fifthBlur = fiveBlur;
    sixthBlur = sixBblur;
    seventhBlur = sevenBblur;
    eighthBlur = eightBblur;
    ninthBlur = nineBblur;
    tenthBlur = tenBblur;
  }

  if (scene == 14) {

    first = oneC;
    second = twoC;
    third = threeC;
    fourth = fourC;
    fifth = five;
    sixth = sixB;
    seventh = sevenB;
    eighth = eightB;
    ninth = nineB;
    tenth = tenB;

    firstBlur = oneCblur;
    secondBlur = twoCblur;
    thirdBlur = threeCblur;
    fourthBlur = fourCblur;
    fifthBlur = fiveBlur;
    sixthBlur = sixBblur;
    seventhBlur = sevenBblur;
    eighthBlur = eightBblur;
    ninthBlur = nineBblur;
    tenthBlur = tenBblur;
  }

  if (scene == 15) {

    first = oneC;
    second = twoC;
    third = threeC;
    fourth = fourC;
    fifth = five;
    sixth = sixC;
    seventh = sevenB;
    eighth = eightB;
    ninth = nineB;
    tenth = tenB;

    firstBlur = oneCblur;
    secondBlur = twoCblur;
    thirdBlur = threeCblur;
    fourthBlur = fourCblur;
    fifthBlur = fiveBlur;
    sixthBlur = sixCblur;
    seventhBlur = sevenBblur;
    eighthBlur = eightBblur;
    ninthBlur = nineBblur;
    tenthBlur = tenBblur;
  }

  if (scene == 16) {

    first = oneC;
    second = twoC;
    third = threeC;
    fourth = fourC;
    fifth = five;
    sixth = sixC;
    seventh = sevenC;
    eighth = eightB;
    ninth = nineB;
    tenth = tenB;

    firstBlur = oneCblur;
    secondBlur = twoCblur;
    thirdBlur = threeCblur;
    fourthBlur = fourCblur;
    fifthBlur = fiveBlur;
    sixthBlur = sixCblur;
    seventhBlur = sevenCblur;
    eighthBlur = eightBblur;
    ninthBlur = nineBblur;
    tenthBlur = tenBblur;
  }

  if (scene == 17) {

    first = oneC;
    second = twoC;
    third = threeC;
    fourth = fourC;
    fifth = five;
    sixth = sixC;
    seventh = sevenC;
    eighth = eightC;
    ninth = nineB;
    tenth = tenB;

    firstBlur = oneCblur;
    secondBlur = twoCblur;
    thirdBlur = threeCblur;
    fourthBlur = fourCblur;
    fifthBlur = fiveBlur;
    sixthBlur = sixCblur;
    seventhBlur = sevenCblur;
    eighthBlur = eightCblur;
    ninthBlur = nineBblur;
    tenthBlur = tenBblur;
  }

  if (scene == 18) {

    first = oneC;
    second = twoC;
    third = threeC;
    fourth = fourC;
    fifth = five;
    sixth = sixC;
    seventh = sevenC;
    eighth = eightC;
    ninth = nineC;
    tenth = tenB;

    firstBlur = oneCblur;
    secondBlur = twoCblur;
    thirdBlur = threeCblur;
    fourthBlur = fourCblur;
    fifthBlur = fiveBlur;
    sixthBlur = sixCblur;
    seventhBlur = sevenCblur;
    eighthBlur = eightCblur;
    ninthBlur = nineCblur;
    tenthBlur = tenBblur;
  }

  if (scene == 19) {

    first = oneC;
    second = twoC;
    third = threeC;
    fourth = fourC;
    fifth = five;
    sixth = sixC;
    seventh = sevenC;
    eighth = eightC;
    ninth = nineC;
    tenth = tenC;

    firstBlur = oneCblur;
    secondBlur = twoCblur;
    thirdBlur = threeCblur;
    fourthBlur = fourCblur;
    fifthBlur = fiveBlur;
    sixthBlur = sixCblur;
    seventhBlur = sevenCblur;
    eighthBlur = eightCblur;
    ninthBlur = nineCblur;
    tenthBlur = tenCblur;
  }


  //Drawing blurred images over background
  image (firstBlur, 0, 0);
  image (secondBlur, width/4, 0);
  image (thirdBlur, (3*(width/4)), 0);
  image (fourthBlur, 0, height/4);
  // image (fifthBlur, width/4, height/4);
  image (sixthBlur, (3*(width/4)), height/4);
  image (seventhBlur, 0, (3*(height/4)));
  image (eighthBlur, width/4, (3*(height/4)));
  image (ninthBlur, width/2, (3*(height/4)));
  image (tenthBlur, 3*(width/4), (3*(height/4)));

  // volume of input:
  oneLevel  = (int)Math.floor(input.getAmplitude(1) / volumeAdjust);
  twoLevel  = (int)Math.floor(input.getAmplitude(2) / volumeAdjust);
  threeLevel  = (int)Math.floor(input.getAmplitude(3) / volumeAdjust);
  fourLevel  = (int)Math.floor(input.getAmplitude(4) / volumeAdjust);
  // fiveLevel  = (int)Math.floor(input.getAmplitude() * 1500);
  sixLevel  = (int)Math.floor(input.getAmplitude(5) / volumeAdjust);
  sevenLevel  = (int)Math.floor(input.getAmplitude(6) / volumeAdjust);
  eightLevel  = (int)Math.floor(input.getAmplitude(7) / volumeAdjust);
  nineLevel  = (int)Math.floor(input.getAmplitude(8) / volumeAdjust);
  tenLevel  = (int)Math.floor(input.getAmplitude(9) / volumeAdjust);

  //showing regular images as amplitude of each part is adjusted
  tint(255, (Math.min(oneLevel, 255)));
  image(first, 0, 0, width/4, height/4);

  tint(255, (Math.min(twoLevel, 255)));
  image(second, width/4, 0, width/2, height/4);

  tint(255, (Math.min(threeLevel, 255)));
  image(third, (3*(width/4)), 0, width/4, height/4);

  tint(255, (Math.min(fourLevel, 255)));
  image(fourth, 0, height/4, width/4, height/2);

  //Pointillizing center image 
  float pointillizeFive = map((Math.min(fiveLevel, 30)), 0, 30, smallPoint, largePoint);
  int randPixelNum = (int)(random(five.pixels.length));
  int randPixel = five.pixels[randPixelNum];
  int xFive = randPixelNum%five.width;
  int yFive = randPixelNum/five.width;
  color pixFive = five.get(xFive, yFive);
  int[] cornerXY = getImageXandY(4);

  //Making sure points are showing up only in the center area
  for (int i = 0; i < cornerXY.length; i++) {
    fill(pixFive, 128);
    ellipse(xFive + cornerXY[0], yFive + cornerXY[1], pointillizeFive, pointillizeFive);
  }

  //  tint(255, (Math.min(fiveLevel, 255)));
  //image(five, width/4, height/4, width/2, height/2);

  tint(255, (Math.min(sixLevel, 255)));
  image(sixth, (3*(width/4)), height/4, width/4, height/2);

  tint(255, (Math.min(sevenLevel, 255)));
  image(seventh, 0, (3*(height/4)), width/4, height/4);

  tint(255, (Math.min(eightLevel, 255)));
  image(eighth, width/4, (3*(height/4)), width/4, height/4);

  tint(255, (Math.min(nineLevel, 255)));
  image(ninth, width/2, (3*(height/4)), width/4, height/4);

  tint(255, (Math.min(tenLevel, 255)));
  image(tenth, 3*(width/4), 3*(height/4), width/4, height/4);
}

//I did not write this either ... Emily?
int[] getImageXandY(int imageNum)
{

  int x = queryArray(width, imageNum % 3);
  int y = queryArray(height, imageNum / 3);

  return new int[] { x, y };
} 


int queryArray(int x, int loc)
{
  int[] array = {0, x/4, 3*(x/4)  };

  if (loc > array.length) {  
    throw new IllegalArgumentException("WM_PointsMultiplePictures_EMM.queryArray: loc " + loc + " is out of bounds; should be less than array.length " + array.length);
  }

  return array[loc];
}