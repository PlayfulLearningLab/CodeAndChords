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
