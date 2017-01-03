/**
 *  Visualize the frequency spectrum of live audio input\
 * 
 * NB - warning from Chrome: Remove support for the MediaStreamTrack.getSources() method. 
 *    This method was removed from the spec in favor of MediaDevices.enumerateDevices().
 * 
 * getEnergy(takes freq in Hz): http://p5js.org/reference/#/p5.FFT
 */

var mic, fft;

function setup() {
   createCanvas(512,400);
   noStroke();
   fill(0,255,255);

   mic = new p5.AudioIn();
   mic.start();
   fft = new p5.FFT();
   fft.setInput(mic);
}

function draw() {
   background(200);
   var spectrum = fft.analyze();

   var findFund = 0;
   
   beginShape();
   vertex(0, height);
   for (i = 0; i<spectrum.length; i++) 
   {
      vertex(i, map(spectrum[i], 0, 255, height, 0) );
      if(spectrum[i] > findFund)
      {
        findFund  = spectrum[i];
      }
   } // for - drawing spectrum
   endShape();
   
//   console.log("findFund = " + findFund);
   
   
   for(i = 4; i < spectrum.length; i++)
    {
 //     console.log("spectrum[" + i + "] = " + spectrum[i]);
      
        if(spectrum[i] > findFund) 
        {
//          console.log("found one!" + i + ": " + spectrum[i]);
          findFund  = spectrum[i];
          /*
            if(spectrum[i] - findFund < (10))
            {
                findFund    = spectrum[i];
                console.log(" -- findFund = " + findFund);
            } // if
            */
        } // if
    } // for

//  console.log("findFund = " + findFund);
//   background(findFund);
   

//   findFund(spectrum);
}

function pitch(spectrum)
{
    var findFund    = 0;

    for(i = 4; i < spectrum.length; i++)
    {
        if(spectrum[i] > findFund) 
        {
            if(spectrum[i] - findFund < (findFund / 2))
            {
                findFund    = spectrum[i];    
            } // if
        } // if
    } // for

    return findFund;
} // findFund