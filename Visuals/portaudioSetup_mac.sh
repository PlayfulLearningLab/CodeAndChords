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

FRAMEWORKS="-framework CoreAudio -framework AudioToolbox -framework AudioUnit -framework CoreServices -framework Carbon"

curl -O $PORT_AUDIO_DOWNLOAD_URL
tar -xzf $PORT_AUDIO_FILE

cd portaudio
./configure --without-jack
make

cc -dynamiclib \
    -I/usr/include \
    -I$JAVA_HOME/include \
    -I$JAVA_HOME/include/darwin \
    ./portaudio/bindings/java/c/src/com_portaudio_BlockingStream.c \
    ./portaudio/bindings/java/c/src/com_portaudio_PortAudio.c \
    ./portaudio/bindings/java/c/src/com_portaudio_jpa_tools.c \
    -o libjportaudio.jnilib \
    $FRAMEWORKS
