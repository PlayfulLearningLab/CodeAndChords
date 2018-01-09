<<<<<<< HEAD
# CodeAndChords

Live music visualization project (in-progress) using the Processing language and Java Sound API; [here's a 3-minute overview.] (https://youtu.be/Y1x6bk8nvog)

## Quick Demos:
Download the appropriate "ModuleApps" folder for your OS ("MacApps", "PC-32bitApps" or "PC-64bitApps"); all the demos therein should run without requiring additional software.

## Everything Else:
 1. Download Processing (https://processing.org/download/)
 2. Open and run any of the files in Visuals/Processing/ProcessingModules.
 (Depending on the location of your default sketchbook, you may also have to import the Beads library.  To do this, go to "Sketch" => "Import Library" => "Add Library...", then search for and install Beads.)
 
We recommend starting with "Module_02_PitchHueBars_EMM", "Module_01_01_PitchHueBackgroundWBorder_HPS_EMM_CURRENT" (both link pitch and color), and "Module_04_03_AmplitudeBars_SS_1Voice_EMM."

(There are also Eclipse versions of some of the modules.)

### Notate Bene:
 * Volume must be turned down or off to avoid feedback while running sketches.
 * Sketches in Visuals/Modules that are marked "MultipleIns", as well as those in July8Demos, will only work correctly if JACK (http://jackaudio.org/) is installed and running.
 * Sketches marked "SamplePlayer" will play and respond to a scale (or other audio) at runtime.
=======
# CodeAndChords

Live music visualization project (in-progress) using Java, the Processing language, and PortAudio API's;
see this video for a 3-minute overview:

<a href="http://www.youtube.com/watch?feature=player_embedded&v=Y1x6bk8nvog/
" target="_blank"><img src="http://img.youtube.com/vi/Y1x6bk8nvog/0.jpg" 
alt="Code+Chords" width="240" height="180" border="10" /></a>

## Quick Demos:
Runnable .jar files for Module_01 and Module_02 can be found in the CodeAndChords/Visuals/RunnableJars folder.  For Module_01_PitchHue and Module_02_AmplitudeHSB, download the Module_01_ModuleTemplate_01_04_rev03 and Module_02_02_AmplitudeHSB folders, respectively, and run the enclosed .jar’s from within those folders (downloading the whole folder for each Module gives the .jar files access to the necessary data folder).

## Eclipse:
The bulk of the project resides in CodeAndChords/Visuals/Eclipse/workspace and requires that  portaudio be installed on your machine.  (However, Modules can be run with JavaSound by replacing the line that initalizes `this.input` [usually ```this.input	= new RealTimeInput(16, true, this);``` for multi-input Modules] with ```this.input	= new RealTimeInput(1, new AudioContext(), this);``` .)

To get the project into Eclipse, go to File -> Import -> “Existing Projects into Workspace,” and select CodeAndChords/Visuals/Eclipse/workspace/CodeAndChordsEclipseProject as the root directory.
>>>>>>> 346fdda528fc720bc3ef683871dafc344f6c9010
