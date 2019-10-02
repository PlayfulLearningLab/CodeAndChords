#!/bin/bash

#
# Downloads and compiles PortAudio and JPortAudio.
# (PortAudio compilation taken from the AVS SDK setup script:
# https://github.com/jahianancyyang/AlarmBuddy/blob/master/amazon-skills-development/alexa-pi/fullsetup.sh)
#

PORT_AUDIO_FILE="pa_stable_v190600_20161030.tgz"
PORT_AUDIO_DOWNLOAD_URL="http://www.portaudio.com/archives/$PORT_AUDIO_FILE"

wget -c $PORT_AUDIO_DOWNLOAD_URL
tar zxf $PORT_AUDIO_FILE

cd portaudio
./configure --without-jack
make
