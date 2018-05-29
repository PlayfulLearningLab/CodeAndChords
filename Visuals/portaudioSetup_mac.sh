#!/bin/bash

#
# Downloads and compiles PortAudio and JPortAudio.
# (PortAudio compilation taken from the AVS SDK setup script:
# https://github.com/jahianancyyang/AlarmBuddy/blob/master/amazon-skills-development/alexa-pi/fullsetup.sh)
#
# Step-by-step JNI compliation instructions from https://medium.com/@bschlining/a-simple-java-native-interface-jni-example-in-java-and-scala-68fdafe76f5f.
#

PORT_AUDIO_FILE="pa_stable_v190600_20161030.tgz"
PORT_AUDIO_DOWNLOAD_URL="http://www.portaudio.com/archives/$PORT_AUDIO_FILE"

FRAMEWORKS="-framework JavaVM -framework CoreAudio -framework AudioToolbox -framework AudioUnit -framework CoreServices -framework Carbon"

curl -O $PORT_AUDIO_DOWNLOAD_URL
tar -xzf $PORT_AUDIO_FILE

cd portaudio
./configure --without-jack
make

# Build the jPortAudio lib (libjportaudio.jnilib) :
cc -c -Iinclude -Ibindings/java/c/src \
    ./bindings/java/c/src/com_portaudio_BlockingStream.c \
    ./bindings/java/c/src/com_portaudio_PortAudio.c \
    ./bindings/java/c/src/jpa_tools.c

cc -dynamiclib \
    -L./lib/.libs \
    -lportaudio \
    -o libjportaudio.jnilib com_portaudio_BlockingStream.o com_portaudio_PortAudio.o jpa_tools.o $FRAMEWORKS

# Move the Mac libs over to the Eclipse project:
cp ./lib/.libs/libportaudio.a ../Eclipse/workspace/CodeAndChordsEclipseProject/libs/
cp ./libjportaudio.jnilib ../Eclipse/workspace/CodeAndChordsEclipseProject/libs/
