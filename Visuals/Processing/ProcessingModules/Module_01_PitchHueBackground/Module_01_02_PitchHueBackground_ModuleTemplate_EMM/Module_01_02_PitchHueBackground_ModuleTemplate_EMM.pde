import interfascia.*;

/**
 1/4/2016
 Emily
 Current struggle = since they're on top of each other, they both contain....
 Probably set a boolean to see which if it goes to?
 
 * 08/01/2016
 * Emily Meuer
 
 *
 * Background changes hue based on pitch.
 *
 * (Adapted from Examples => Color => Hue.)
 */

// Choose input file here:
String  inputFile  = "Emily_CMajor-2016_09_2-16bit-44.1K Raw.wav";
//String  inputFile  = "Emily_CMajor-2016_09_2-16bit-44.1K Tuned.wav";
//String  inputFile  = "Emily_CMajor-2016_09_2-16bit-44.1K Kanye.wav";

// Lable for the scrollbar:
GUIController controller;
IFTextField   textField;
IFLabel       label;

int  hueMax;
int  saturationMax;
int  brightnessMax;

Input  input;
int  threshold;      // when amp is below this, the background will be black.

int  hue;
int  saturation;

float[]  newHue;
float[]  goalHue;
float[]  curHue;
float    attackTime;

int  newHuePos;
int  goalHuePos;
int  curHuePos;

int  newSaturation;
int  goalSaturation;
int  curSaturation;
int  changeInSaturation;

float[][]  colors;          // holds the RGB values for the colors responding to HSB: every 30th H with 100 S, 100 B

HScrollbar  scrollbar;
float       scrollbarPos;
float       attackTimeMax  = 20;
float       attackTimeMin  = 1;

PFont    consolas;

PShape  playButton;
PShape  stopButton;
boolean  showPlay;
boolean  showStop;

boolean  sidebarOut;
int      leftEdgeX;

HScrollbar[]  scrollbarArray;

HScrollbar  thresholdScroll;
HScrollbar  attackScroll;
HScrollbar  releaseScroll;
HScrollbar  transitionScroll;
HScrollbar  keyScroll;
HScrollbar  rootScroll;

int  scrollWidth1;

int    hideY;
int    thresholdY;
int    attackY;
int    releaseY;
int    transitionY;
int    keyY;
int    rootColorY;
int    colorStyleY;
int    pitchColorCodesY;
int    a_deY;
int    ab_eY;
int    b_fY;
int    cd_fgY;
int    d_gaY;
int    redModulateY;
int    greenModulateY;
int    blueModulateY;

int    playButtonX;
int    arrowX;
int    scaleX;

int    hideWidth;

int    rainbowX;
int    dichromaticX;
int    trichromaticX;
int    customX;

String[]  buttonLabels;
IFButton[]  buttons;
Buttons   buttonWrapper;

int    scrollbarX;


void setup() 
{
  size(925, 520);

  hueMax         = 360;
  saturationMax  = 300;
  brightnessMax  = 100;

  //  colorMode(HSB, hueMax, saturationMax, brightnessMax);
  colors  = new float[][] {
    { 255, 0, 0 }, 
    { 255, 127.5, 0 }, 
    { 255, 255, 0 }, 
    { 127.5, 255, 0 }, 
    { 0, 255, 0 }, 
    { 0, 255, 127.5 }, 
    { 0, 255, 255 }, 
    { 0, 127.5, 255 }, 
    { 0, 0, 255 }, 
    { 127.5, 0, 255 }, 
    { 255, 0, 255 }, 
    { 255, 0, 127.5 }, 
    { 255, 0, 0 }
  };

  //  input        = new Input(inputFile);
  input  = new Input();
  threshold    = 15;

  noStroke();
  background(0);

  //  curHuePos    = round(input.getAdjustedFundAsMidiNote(1) % 12) * 30;
  curHuePos    = round(input.getAdjustedFundAsMidiNote(1) % 12);
  curHue       = colors[curHuePos];
  // would like to change more quickly, but there's a weird flicker if attackTime gets bigger:
  attackTime  = 10;

  curSaturation       = (int)Math.min(input.getAmplitude(1), 300);
  changeInSaturation  = 10;
  String[] fontList = PFont.list();

  printArray(fontList);

  consolas  = createFont("Consolas.ttf", 24);
  textFont(consolas);
  // sets the text size used to display note names:
  textSize(24);

  // draws the legend
  legend();

  scrollbar = new HScrollbar(10, 45, (width / 2) - 10, 18, 5);

  controller = new GUIController(this);
  textField  = new IFTextField("Text Field", 10, 10, 150);
  label      = new IFLabel("attackTime: " + attackTime, 15, 15);
  /*
  controller.add(textField);
   controller.add(label);
   */
  textField.addActionListener(this);

  //  println("scrollbar.ratio = " + scrollbar.ratio);

  // Creating the PShape as a square. The corner 
  // is 0,0 so that the center is at 40,40 
  // syntax: createShape(TRIANGLE, x1, y1, x2, y2, x3, y3)
  int  playDistanceFromEdge  = 20;
  int  playWidth  = 25;
  int  playHeight  = 30;
  float[][]  playButtonVerts  = new float[][] { new float[] { width - playDistanceFromEdge - playWidth, playDistanceFromEdge }, 
    new float[] { width - playDistanceFromEdge - playWidth, playDistanceFromEdge + playHeight }, 
    new float[] { width - playDistanceFromEdge, playDistanceFromEdge + (playHeight / 2) }, 
    new float[] { width - playDistanceFromEdge - playWidth, playDistanceFromEdge }
  };
  this.playButton  = createShape(PShape.PATH);
  // Setting vcount to 4 means connects the first and last points with a side, 
  // making it look nice and making mousePressed/contains() functionality work.
  this.playButton.setPath(4, playButtonVerts);
  this.showPlay  = true;

  int  stopSideSize  = 30;
  int  stopDistanceFromEdge  = 20;
  float[][]  stopButtonVerts  = new float[][] { new float[] { width - stopDistanceFromEdge - stopSideSize, stopDistanceFromEdge }, 
    new float[] { width - stopDistanceFromEdge - stopSideSize, stopDistanceFromEdge + stopSideSize }, 
    new float[] { width - stopDistanceFromEdge, stopDistanceFromEdge + stopSideSize }, 
    new float[] { width - stopDistanceFromEdge, stopDistanceFromEdge }, 
    new float[] { width - stopDistanceFromEdge - stopSideSize, stopDistanceFromEdge }, 
  };
  this.stopButton  = createShape(PShape.PATH);
  this.stopButton.setPath(5, stopButtonVerts);
  this.showStop  = false;
  this.stopButton.setVisible(false);

  this.sidebarOut  = false;
  this.leftEdgeX   = 0;

  this.scrollbarX  = (width / 3) / 4;

  /*
    this.playButtonX  = ((width / 3) / 4) * 1;
   this.arrowX       = ((width / 3) / 4) * 2 + 15;
   this.scaleX       = ((width / 3) / 4) * 3 + 20;
   */
  playButtonX  = scrollbarX;
  arrowX       = scrollbarX + 80;
  scaleX       = scrollbarX + 140;

  hideWidth     = 75;
  hideY         = 40;
  thresholdY    = 65;
  attackY       = 85;
  releaseY      = 105;
  transitionY   = 125;
  keyY          = 145;
  rootColorY    = 165;
  colorStyleY   = 185;
  pitchColorCodesY  = 185;
  
  this.rainbowX     = scrollbarX;
  this.dichromaticX  = scrollbarX + 60;
  this.trichromaticX  = scrollbarX + 140;
  this.customX  = scrollbarX + 400;

  this.buttons  = new IFButton[] {
    // annoyingly, buttons have to be placed15 pts higher than their corresponding text
    new IFButton("PlayButton", playButtonX, hideY - 15, hideWidth),
    new IFButton("Arrow", arrowX, hideY - 15, hideWidth - 20), 
    new IFButton("Scale", scaleX, hideY - 15, hideWidth - 35), 
    new IFButton("Rainbow", rainbowX, colorStyleY - 15, 55), 
    new IFButton("Dichromatic", dichromaticX, colorStyleY - 15, 75), 
    new IFButton("Trichromatic", trichromaticX, colorStyleY - 15, 75), 
    new IFButton("Custom", customX, colorStyleY), 
  }; // buttons

  buttonWrapper  = new Buttons(controller, new IFLookAndFeel(IFLookAndFeel.DEFAULT), this.buttons);

  scrollWidth1  = 150;
//  thresholdScroll  = new HScrollbar(scrollbarX, thresholdY - 5, scrollWidth1);
  scrollbarArray  = new HScrollbar[]  {
    new HScrollbar(scrollbarX, thresholdY - 5, scrollWidth1),
    new HScrollbar(scrollbarX, attackY - 5, scrollWidth1),
    new HScrollbar(scrollbarX, releaseY - 5, scrollWidth1),
    new HScrollbar(scrollbarX, transitionY - 5, scrollWidth1),
    new HScrollbar(scrollbarX, keyY - 5, scrollWidth1),
    new HScrollbar(scrollbarX, rootColorY - 5, scrollWidth1),
  };
  
} // setup()

void draw()
{

  stroke(255);
  if (input.getAmplitude() > threshold)
  {
    // if want something other than C to be the "tonic" (i.e., red),
    // add some number before multiplying.
    newHuePos  = round(input.getAdjustedFundAsMidiNote(1) % 12);
    newHue  = colors[newHuePos];
    //  newHue  = newHue * 30;  // this is for HSB, when newHue is the color's H value
  } else {
    newHue  = new float[] { 0, 0, 0 };
  } // else

  // set goalHue to the color indicated by the current pitch:
  if (newHuePos != goalHuePos)
  {
    goalHuePos  = newHuePos;
  } // if
  goalHue  = colors[goalHuePos];

  for (int i = 0; i < 3; i++)
  {
    if (curHue[i] > (goalHue[i] - attackTime))
    {
      curHue[i] = curHue[i] - attackTime;
    } else if (curHue[i] < (goalHue[i] + attackTime))
    {
      curHue[i]  = curHue[i] + attackTime;
    } // if
  } // for

  //  background(curHue[0], curHue[1], curHue[2]);
  fill(curHue[0], curHue[1], curHue[2]);
  rect(leftEdgeX, 0, width - leftEdgeX, height);

  stroke(255);
  //  triangle( 710, 10, 710, 30, 730, 20);
  // draws the legend along the bottom of the screen:
  legend();

  //  scrollbar.update();
  //  scrollbar.display();

  scrollbarPos  = scrollbar.getPos();
  attackTime      = map(scrollbarPos, scrollbar.sposMin, scrollbar.sposMax, attackTimeMin, attackTimeMax);

  //  label.setLabel("attackTime: " + attackTime);

  shape(this.playButton);
  shape(this.stopButton);

  if (mousePressed && (mouseX < (width / 3)))
  {
    this.sidebarOut  = !this.sidebarOut;

    if (sidebarOut)
    {
      this.leftEdgeX  = width / 3;
      this.displaySidebar();
    } else { 
      this.leftEdgeX  = 0;
    } // if - leftEdgeX
  } // if - mousePressed
} // draw()

void displaySidebar()
{
  stroke(255);
  fill(0);
  rect(0, 0, leftEdgeX, height);
  
  int  textX  = 5;

  textSize(10);
  fill(255);
  text("Module_01_01_PitchHueBackground", 10, 15);
  text("Hide", textX, hideY);
  text("Threshold", textX, thresholdY);
  text("Attack", textX, attackY);
  text("Release", textX, releaseY);
  text("Transition", textX, transitionY);
  text("Key", textX, keyY);
  text("Root Color", textX, rootColorY);
  text("ColorStyle", textX, colorStyleY);
  // TODO: make buttons clickable only when sidebar is open?

/*
  thresholdScroll.update();  
  thresholdScroll.display();
  */
  for(int i = 0; i < scrollbarArray.length; i++)
  {
    scrollbarArray[i].update();
    scrollbarArray[i].display();
  } // for - update and display first set of scrollbars
} // displaySidebar

void legend()
{

  textSize(24);

  String[] notes = new String[] {
    "C", 
    "C#", 
    "D", 
    "D#", 
    "E", 
    "F", 
    "F#", 
    "G", 
    "G#", 
    "A", 
    "A#", 
    "B", 
    "C"
  }; // notes

  // 12/19: updating to be on the side.
  // 01/05: changing it back!
  float  sideWidth   = (width - leftEdgeX) / colors.length;
  float  sideHeight  = width / colors.length;
  //  float  side = height / colors.length;

  stroke(255);

  for (int i = 0; i < colors.length; i++)
  {
    fill(colors[i][0], colors[i][1], colors[i][2]);
    if (i == goalHuePos) {
      rect(leftEdgeX + (sideWidth * i), height - (sideHeight * 1.5), sideWidth, sideHeight * 1.5);
      //      rect(0, (side * i), side * 1.5, side);
    } else {
      rect(leftEdgeX + (sideWidth * i), height - sideHeight, sideWidth, sideHeight);
      //      rect(0, (side * i), side, side);
    }
    fill(0);
    text(notes[i], leftEdgeX + (sideWidth * i) + (sideWidth * 0.35), height - 20);
  } // for
} // legend

void mousePressed()
{
  // The following for loops stops either the SamplePlayers or the uGens getting audio input (and otherwise creating feedback):
  for (int i = 0; i < input.uGenArray.length; i++)
  {
    input.uGenArray[i].pause(true);
  } // for

  if (this.showPlay)
  {
    if (this.playButton.contains(mouseX, mouseY))
    {
      this.showPlay  = false;
      this.showStop  = true;

      this.input.uGenArrayFromSample("Emily_CMajor-2016_09_2-16bit-44.1K Tuned.wav");
    } // if - playButton
  } else {
    if (this.stopButton.contains(mouseX, mouseY))
    {
      this.showStop  = false;
      this.showPlay  = true;

      this.input.uGenArrayFromNumInputs(1);
    } // if - stopButton
  } // else - showPlay/showStop

  this.playButton.setVisible(this.showPlay);
  this.stopButton.setVisible(this.showStop);
} // mousePressed

// TODO: make buttons clickable only when sidebar is open?
void actionPerformed(GUIEvent e) {
  if (e.getMessage().equals("Completed")) {
    // This .setLabel prob. has value for getting values:
    label.setLabel(textField.getValue());
  }
} // actionPerformed