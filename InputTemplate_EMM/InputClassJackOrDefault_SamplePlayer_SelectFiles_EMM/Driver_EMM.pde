import interfascia.*; //<>//

/*
  07/06/2016
 Emily Meuer
 
 Sketch to test which mic routes to which input line.
 */


Input input;
IFButton  ampControlButton;
boolean   ampControlBool;

void settings()
{
  size(800, 600);
} // settings

void setup()
{
  fill(150, 50, 150);
  //  input = new Input("Gittin.mp3");

  String[]  har  = {"Horse and Rider 1.mp3", 
    "Horse and Rider 2.mp3", 
    "Horse and Rider 3.mp3"
  };
//  input  = new Input(har);

    input  = new Input(true);
  ampControlBool  = false;

  //  ampControlButton  = new IFButton()
  textSize(18);

  //  selectInput("Select a file to process:", "fileSelected");

//  selectFolder("Select a folder to process:", "folderSelected");
} // setup()

void draw()
{
  background(255);
  fill(150, 50, 150);

  ellipse(100, height - input.getAdjustedFund(1), Math.min(input.getAmplitude(1) / 10, 200), Math.min(input.getAmplitude(1) / 10, 200));  
//  ellipse(400, height - input.getAdjustedFund(2), input.getAmplitude(2) / 10, input.getAmplitude(2) / 10);  
//  ellipse(700, height - input.getAdjustedFund(3), input.getAmplitude(3) / 10, input.getAmplitude(3) / 10);

  fill(0);
  text("'I will sing unto the Lord':", 50, 50);
  text("'The Lord, my God':", 300, 50);
  text("'The Lord is God and - ':", 550, 50);
} //<>// //<>// //<>//

void folderSelected(File selection) {
  if (selection == null) {
    println("Window was closed or the user hit cancel.");
  } else {
    println("User selected " + selection.getAbsolutePath());
  }
} // folderSelected
/*
void fileSelected(File selection) {
  if (selection == null) {
    println("Window was closed or the user hit cancel.");
  } else {
    println("User selected " + selection.getAbsolutePath());
  } // else
  
} // fileSelected
*/