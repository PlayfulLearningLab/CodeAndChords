#!/bin/sh

#  run_Module_01_01.command
#
#
#  Created by Emily Meuer on 12/29/16.
#   This runs an already compiled class file.

cd "$(dirname "$0")"
pwd
#cd ..
#pwd
#cd ../bin/
#pwd

javac -cp .:../beads.jar:../beads-io.jar:../core.jar:../interfascia.jar:../org-jaudiolibs-audioservers.jar module_01_PitchHueBackground/Module_01_01_PitchHueBackground_EMM.java

java -cp .:../beads.jar:../beads-io.jar:../core.jar:../interfascia.jar:../org-jaudiolibs-audioservers.jar module_01_PitchHueBackground/Module_01_01_PitchHueBackground_EMM
