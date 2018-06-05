# CodeAndChords

Live music visualization project (in-progress) using Java, the Processing language, and PortAudio API's;
see this video for a 3-minute overview:

<a href="http://www.youtube.com/watch?feature=player_embedded&v=Y1x6bk8nvog/
" target="_blank"><img src="http://img.youtube.com/vi/Y1x6bk8nvog/0.jpg" 
alt="Code+Chords" width="240" height="180" border="10" /></a>

_Notate bene: 1-input demos can be run on any Java-enable machine with a microphone, but multiple inputs require PortAudio.  For instructions on installing (hint: we have a handy-dandy helper script!), go to the [Installing PortAudio](#installing-portaudio) section._

## Quick Demos:
Links to 1-input, runnable .jar files are coming soon!

## Writing your own:

### Use the library
We hope to provide a .jar file for importing our core functionality shortly.

### Dig into the source code
The bulk of the project resides in CodeAndChords/Visuals/Eclipse/workspace and requires that portaudio be installed on your machine.  (However, Modules can be run with JavaSound by replacing the line that initalizes `this.input` [usually ```this.input	= new RealTimeInput(16, true, this);``` for multi-input Modules] with ```this.input	= new RealTimeInput(1, new AudioContext(), this);``` .)

To get the project into Eclipse, go to File -> Import -> “Existing Projects into Workspace,” and select CodeAndChords/Visuals/Eclipse/workspace/CodeAndChordsEclipseProject as the root directory.

## Installing PortAudio
### Mac and Linux
The script is all you need.  Depending on your settings, you may be able to run by double-clicking on the apprpriate "portaudioSetup" file for your OS.  If that doesn't work, open the terminal, navigate to CodeAndChords/Visuals and run with the following command:
```./portaudioSetup_mac.sh``` (or) ```./portaudioSetup_linux.sh```
(If the terminal doesn't like that, make it exectuble with the following command: ```chmod 744 ./portaudioSetup_mac.sh``` and try again.)

### Windows
Ahh, Windows.  This would be a great chance to dual-boot your machine with Linux!

In all seriousness, though, the process is not too bad, especially if you use Visual Studio (which we highly recommend):
 - Download PortAudio [here](http://www.portaudio.com/download.html) (not surpisingly, choose the one that says "You probably want this!!").
 - Build a portaudio_x--.dll following the directions [here](http://portaudio.com/docs/v19-doxydocs/compile_windows.html).
 - Move the resulting .dll file to CodeAndChords/Visuals/Eclipse/workspace/CodeAndChords/EclipseProject/libs.
 - Build a jportaudio_x--.dll by opening the project in portaudio/bindings/java/c/build/vs2010/PortAudioJNI and following the above instructions.

The errors from that last step, although inevitable, are not, as of yet, predictable to our team.  If that should that ever become the case, we will offer any helpful hints here.
