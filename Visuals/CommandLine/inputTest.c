/** @file paex_record.c
	@ingroup examples_src
	@brief Record input into an array; Save array to a file; Playback recorded data.
	@author Phil Burk  http://www.softsynth.com
*/
/*
 * $Id$
 *
 * This program uses the PortAudio Portable Audio Library.
 * For more information see: http://www.portaudio.com
 * Copyright (c) 1999-2000 Ross Bencina and Phil Burk
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files
 * (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge,
 * publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
 * ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

/*
<<<<<<< HEAD
<<<<<<< HEAD
 * The text above constitutes the entire PortAudio license; however,
=======
 * The text above constitutes the entire PortAudio license; however, 
>>>>>>> cca125d... First attempts at getting audio with portaudio
=======
 * The text above constitutes the entire PortAudio license; however,
>>>>>>> 35e86f1... Trying 2 lines
 * the PortAudio community also makes the following non-binding requests:
 *
 * Any person wishing to distribute modifications to the Software is
 * requested to send the modifications to the original developer so that
<<<<<<< HEAD
<<<<<<< HEAD
 * they can be incorporated into the canonical version. It is also
 * requested that these non-binding requests be included along with the
=======
 * they can be incorporated into the canonical version. It is also 
 * requested that these non-binding requests be included along with the 
>>>>>>> cca125d... First attempts at getting audio with portaudio
=======
 * they can be incorporated into the canonical version. It is also
 * requested that these non-binding requests be included along with the
>>>>>>> 35e86f1... Trying 2 lines
 * license above.
 */

#include <stdio.h>
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> 65cc807... Can select device with portaudio
#include <math.h>
#include <stdlib.h>
#include "portaudio.h"


#ifdef WIN32
#include <windows.h>

#if PA_USE_ASIO
#include "pa_asio.h"
#endif
#endif

<<<<<<< HEAD
=======
#include <stdlib.h>
#include "portaudio.h"

>>>>>>> cca125d... First attempts at getting audio with portaudio
=======
>>>>>>> 65cc807... Can select device with portaudio
/* #define SAMPLE_RATE  (17932) // Test failure to open with this value. */
#define SAMPLE_RATE  (44100)
#define FRAMES_PER_BUFFER (512)
#define NUM_SECONDS     (5)
#define NUM_CHANNELS    (2)
/* #define DITHER_FLAG     (paDitherOff) */
#define DITHER_FLAG     (0) /**/
/** Set to 1 if you want to capture the recording to a file. */
#define WRITE_TO_FILE   (0)

/* Select sample format. */
#if 1
#define PA_SAMPLE_TYPE  paFloat32
typedef float SAMPLE;
#define SAMPLE_SILENCE  (0.0f)
#define PRINTF_S_FORMAT "%.8f"
#elif 1
#define PA_SAMPLE_TYPE  paInt16
typedef short SAMPLE;
#define SAMPLE_SILENCE  (0)
#define PRINTF_S_FORMAT "%d"
#elif 0
#define PA_SAMPLE_TYPE  paInt8
typedef char SAMPLE;
#define SAMPLE_SILENCE  (0)
#define PRINTF_S_FORMAT "%d"
#else
#define PA_SAMPLE_TYPE  paUInt8
typedef unsigned char SAMPLE;
#define SAMPLE_SILENCE  (128)
#define PRINTF_S_FORMAT "%d"
#endif

typedef struct
{
    int          frameIndex;  /* Index into sample array. */
    int          maxFrameIndex;
    SAMPLE      *recordedSamples;
}
paTestData;

<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> 65cc807... Can select device with portaudio
static void PrintSupportedStandardSampleRates(
        const PaStreamParameters *inputParameters,
        const PaStreamParameters *outputParameters )
{
    static double standardSampleRates[] = {
        8000.0, 9600.0, 11025.0, 12000.0, 16000.0, 22050.0, 24000.0, 32000.0,
        44100.0, 48000.0, 88200.0, 96000.0, 192000.0, -1 /* negative terminated  list */
    };
    int     i, printCount;
    PaError err;

    printCount = 0;
    for( i=0; standardSampleRates[i] > 0; i++ )
    {
        err = Pa_IsFormatSupported( inputParameters, outputParameters, standardSampleRates[i] );
        if( err == paFormatIsSupported )
        {
            if( printCount == 0 )
            {
                printf( "\t%8.2f", standardSampleRates[i] );
                printCount = 1;
            }
            else if( printCount == 4 )
            {
                printf( ",\n\t%8.2f", standardSampleRates[i] );
                printCount = 1;
            }
            else
            {
                printf( ", %8.2f", standardSampleRates[i] );
                ++printCount;
            }
        }
    }
    if( !printCount )
        printf( "None\n" );
    else
        printf( "\n" );
} // PrintSupportedStandardSampleRates

<<<<<<< HEAD
=======
>>>>>>> cca125d... First attempts at getting audio with portaudio
=======
>>>>>>> 65cc807... Can select device with portaudio
/* This routine will be called by the PortAudio engine when audio is needed.
** It may be called at interrupt level on some machines so don't do anything
** that could mess up the system like calling malloc() or free().
*/
static int recordCallback( const void *inputBuffer, void *outputBuffer,
                           unsigned long framesPerBuffer,
                           const PaStreamCallbackTimeInfo* timeInfo,
                           PaStreamCallbackFlags statusFlags,
                           void *userData )
{
    paTestData *data = (paTestData*)userData;
    const SAMPLE *rptr = (const SAMPLE*)inputBuffer;
    SAMPLE *wptr = &data->recordedSamples[data->frameIndex * NUM_CHANNELS];
    long framesToCalc;
    long i;
    int finished;
    unsigned long framesLeft = data->maxFrameIndex - data->frameIndex;
<<<<<<< HEAD
<<<<<<< HEAD

=======
	
>>>>>>> cca125d... First attempts at getting audio with portaudio
=======

>>>>>>> 35e86f1... Trying 2 lines
    (void) outputBuffer; /* Prevent unused variable warnings. */
    (void) timeInfo;
    (void) statusFlags;
    (void) userData;

    if( framesLeft < framesPerBuffer )
    {
        framesToCalc = framesLeft;
        finished = paComplete;
    }
    else
    {
        framesToCalc = framesPerBuffer;
        finished = paContinue;
    }

    if( inputBuffer == NULL )
    {
        for( i=0; i<framesToCalc; i++ )
        {
            *wptr++ = SAMPLE_SILENCE;  /* left */
            if( NUM_CHANNELS == 2 ) *wptr++ = SAMPLE_SILENCE;  /* right */
        }
    }
    else
    {
        for( i=0; i<framesToCalc; i++ )
        {
            *wptr++ = *rptr++;  /* left */
            if( NUM_CHANNELS == 2 ) *wptr++ = *rptr++;  /* right */
        }
    }
    data->frameIndex += framesToCalc;
    return finished;
<<<<<<< HEAD
<<<<<<< HEAD
} // recordCallback
=======
}
>>>>>>> cca125d... First attempts at getting audio with portaudio
=======
} // recordCallback
>>>>>>> 65cc807... Can select device with portaudio

/* This routine will be called by the PortAudio engine when audio is needed.
** It may be called at interrupt level on some machines so don't do anything
** that could mess up the system like calling malloc() or free().
*/
static int playCallback( const void *inputBuffer, void *outputBuffer,
                         unsigned long framesPerBuffer,
                         const PaStreamCallbackTimeInfo* timeInfo,
                         PaStreamCallbackFlags statusFlags,
                         void *userData )
{
    paTestData *data = (paTestData*)userData;
    SAMPLE *rptr = &data->recordedSamples[data->frameIndex * NUM_CHANNELS];
    SAMPLE *wptr = (SAMPLE*)outputBuffer;
    unsigned int i;
    int finished;
    unsigned int framesLeft = data->maxFrameIndex - data->frameIndex;

    (void) inputBuffer; /* Prevent unused variable warnings. */
    (void) timeInfo;
    (void) statusFlags;
    (void) userData;

    if( framesLeft < framesPerBuffer )
    {
        /* final buffer... */
        for( i=0; i<framesLeft; i++ )
        {
            *wptr++ = *rptr++;  /* left */
            if( NUM_CHANNELS == 2 ) *wptr++ = *rptr++;  /* right */
        }
        for( ; i<framesPerBuffer; i++ )
        {
            *wptr++ = 0;  /* left */
            if( NUM_CHANNELS == 2 ) *wptr++ = 0;  /* right */
        }
        data->frameIndex += framesLeft;
        finished = paComplete;
    }
    else
    {
        for( i=0; i<framesPerBuffer; i++ )
        {
            *wptr++ = *rptr++;  /* left */
            if( NUM_CHANNELS == 2 ) *wptr++ = *rptr++;  /* right */
        }
        data->frameIndex += framesPerBuffer;
        finished = paContinue;
    }
    return finished;
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> 65cc807... Can select device with portaudio
} // playCallback

int selectDevice()
{
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
    int     			i,
						numDevices,
						defaultDisplayed;
    const PaDeviceInfo 	*deviceInfo;
    PaStreamParameters 	inputParameters,
						outputParameters;
	PaStream*			stream;
    PaError 			err;

=======
    int     			i, 
						numDevices, 
=======
=======
>>>>>>> 726f4d5... Commit after aborting the rebase
    int     			i,
						numDevices,
>>>>>>> 35e86f1... Trying 2 lines
						defaultDisplayed;
    const PaDeviceInfo 	*deviceInfo;
    PaStreamParameters 	inputParameters,
						outputParameters;
	PaStream*			stream;
    PaError 			err;
<<<<<<< HEAD
	
>>>>>>> 65cc807... Can select device with portaudio
=======

<<<<<<< HEAD
<<<<<<< HEAD
=======
=======
    int     			i, 
						numDevices, 
=======
    int     			i,
						numDevices,
>>>>>>> 62b84d2... Trying 2 lines
						defaultDisplayed;
    const PaDeviceInfo 	*deviceInfo;
    PaStreamParameters 	inputParameters,
						outputParameters;
	PaStream*			stream;
    PaError 			err;
<<<<<<< HEAD
	
>>>>>>> 0e0e962... Can select device with portaudio
=======

>>>>>>> 726f4d5... Commit after aborting the rebase
>>>>>>> 62b84d2... Trying 2 lines
=======
>>>>>>> 35e86f1... Trying 2 lines
>>>>>>> d43f794... Second rebase abort
    paTestData          data;
    int                 totalFrames;
    int                 numSamples;
    int                 numBytes;
	char 				devSelection[4];
	int					devSelectInt;

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD

=======
    
>>>>>>> 0e0e962... Can select device with portaudio
=======

=======

=======
    
>>>>>>> 65cc807... Can select device with portaudio
=======

<<<<<<< HEAD
>>>>>>> 726f4d5... Commit after aborting the rebase
>>>>>>> 62b84d2... Trying 2 lines
=======
>>>>>>> 35e86f1... Trying 2 lines
>>>>>>> d43f794... Second rebase abort
    err = Pa_Initialize();
    if( err != paNoError )
    {
        printf( "ERROR: Pa_Initialize returned 0x%x\n", err );
        return -1;
    }
<<<<<<< HEAD
<<<<<<< HEAD

=======
    
>>>>>>> 65cc807... Can select device with portaudio
=======

>>>>>>> 35e86f1... Trying 2 lines
    printf( "PortAudio version: 0x%08X\n", Pa_GetVersion());
    printf( "Version text: '%s'\n", Pa_GetVersionInfo()->versionText );

    numDevices = Pa_GetDeviceCount();
    if( numDevices < 0 )
    {
        printf( "ERROR: Pa_GetDeviceCount returned 0x%x\n", numDevices );
        err = numDevices;
        return -1;
    }
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD

=======
    
>>>>>>> 65cc807... Can select device with portaudio
=======

<<<<<<< HEAD
=======

=======
    
>>>>>>> 0e0e962... Can select device with portaudio
=======

>>>>>>> 726f4d5... Commit after aborting the rebase
>>>>>>> 62b84d2... Trying 2 lines
=======
>>>>>>> 35e86f1... Trying 2 lines
>>>>>>> d43f794... Second rebase abort
    printf( "Number of devices = %d\n", numDevices );
    for( i=0; i<numDevices; i++ )
    {
        deviceInfo = Pa_GetDeviceInfo( i );
        printf( "--------------------------------------- device #%d\n", i );
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD

=======
                
>>>>>>> 65cc807... Can select device with portaudio
=======

<<<<<<< HEAD
=======

=======
                
>>>>>>> 0e0e962... Can select device with portaudio
=======

>>>>>>> 726f4d5... Commit after aborting the rebase
>>>>>>> 62b84d2... Trying 2 lines
=======
>>>>>>> 35e86f1... Trying 2 lines
>>>>>>> d43f794... Second rebase abort
    /* Mark global and API specific default devices */
        defaultDisplayed = 0;
        if( i == Pa_GetDefaultInputDevice() )
        {
            printf( "[ Default Input" );
            defaultDisplayed = 1;
        }
        else if( i == Pa_GetHostApiInfo( deviceInfo->hostApi )->defaultInputDevice )
        {
            const PaHostApiInfo *hostInfo = Pa_GetHostApiInfo( deviceInfo->hostApi );
            printf( "[ Default %s Input", hostInfo->name );
            defaultDisplayed = 1;
        }
<<<<<<< HEAD
<<<<<<< HEAD

=======
        
>>>>>>> 65cc807... Can select device with portaudio
=======

>>>>>>> 35e86f1... Trying 2 lines
        if( i == Pa_GetDefaultOutputDevice() )
        {
            printf( (defaultDisplayed ? "," : "[") );
            printf( " Default Output" );
            defaultDisplayed = 1;
        }
        else if( i == Pa_GetHostApiInfo( deviceInfo->hostApi )->defaultOutputDevice )
        {
            const PaHostApiInfo *hostInfo = Pa_GetHostApiInfo( deviceInfo->hostApi );
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
            printf( (defaultDisplayed ? "," : "[") );
=======
            printf( (defaultDisplayed ? "," : "[") );                
>>>>>>> 0e0e962... Can select device with portaudio
=======
            printf( (defaultDisplayed ? "," : "[") );
=======
            printf( (defaultDisplayed ? "," : "[") );
=======
            printf( (defaultDisplayed ? "," : "[") );                
>>>>>>> 65cc807... Can select device with portaudio
=======
            printf( (defaultDisplayed ? "," : "[") );
<<<<<<< HEAD
>>>>>>> 726f4d5... Commit after aborting the rebase
>>>>>>> 62b84d2... Trying 2 lines
=======
>>>>>>> 35e86f1... Trying 2 lines
>>>>>>> d43f794... Second rebase abort
            printf( " Default %s Output", hostInfo->name );
            defaultDisplayed = 1;
        }

        if( defaultDisplayed )
            printf( " ]\n" );

    /* print device info fields */
#ifdef WIN32
        {   /* Use wide char on windows, so we can show UTF-8 encoded device names */
            wchar_t wideName[MAX_PATH];
            MultiByteToWideChar(CP_UTF8, 0, deviceInfo->name, -1, wideName, MAX_PATH-1);
            wprintf( L"Name                        = %s\n", wideName );
        }
#else
        printf( "Name                        = %s\n", deviceInfo->name );
#endif
        printf( "Host API                    = %s\n",  Pa_GetHostApiInfo( deviceInfo->hostApi )->name );
        printf( "Max inputs = %d", deviceInfo->maxInputChannels  );
        printf( ", Max outputs = %d\n", deviceInfo->maxOutputChannels  );

        printf( "Default low input latency   = %8.4f\n", deviceInfo->defaultLowInputLatency  );
        printf( "Default low output latency  = %8.4f\n", deviceInfo->defaultLowOutputLatency  );
        printf( "Default high input latency  = %8.4f\n", deviceInfo->defaultHighInputLatency  );
        printf( "Default high output latency = %8.4f\n", deviceInfo->defaultHighOutputLatency  );

#ifdef WIN32
#if PA_USE_ASIO
/* ASIO specific latency information */
        if( Pa_GetHostApiInfo( deviceInfo->hostApi )->type == paASIO ){
            long minLatency, maxLatency, preferredLatency, granularity;

            err = PaAsio_GetAvailableLatencyValues( i,
		            &minLatency, &maxLatency, &preferredLatency, &granularity );

            printf( "ASIO minimum buffer size    = %ld\n", minLatency  );
            printf( "ASIO maximum buffer size    = %ld\n", maxLatency  );
            printf( "ASIO preferred buffer size  = %ld\n", preferredLatency  );

            if( granularity == -1 )
                printf( "ASIO buffer granularity     = power of 2\n" );
            else
                printf( "ASIO buffer granularity     = %ld\n", granularity  );
        }
#endif /* PA_USE_ASIO */
#endif /* WIN32 */

        printf( "Default sample rate         = %8.2f\n", deviceInfo->defaultSampleRate );

    /* poll for standard sample rates */
        inputParameters.device = i;
        inputParameters.channelCount = deviceInfo->maxInputChannels;
        inputParameters.sampleFormat = paInt16;
        inputParameters.suggestedLatency = 0; /* ignored by Pa_IsFormatSupported() */
        inputParameters.hostApiSpecificStreamInfo = NULL;
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD

=======
        
>>>>>>> 65cc807... Can select device with portaudio
=======

<<<<<<< HEAD
=======

=======
        
>>>>>>> 0e0e962... Can select device with portaudio
=======

>>>>>>> 726f4d5... Commit after aborting the rebase
>>>>>>> 62b84d2... Trying 2 lines
=======
>>>>>>> 35e86f1... Trying 2 lines
>>>>>>> d43f794... Second rebase abort
        outputParameters.device = i;
        outputParameters.channelCount = deviceInfo->maxOutputChannels;
        outputParameters.sampleFormat = paInt16;
        outputParameters.suggestedLatency = 0; /* ignored by Pa_IsFormatSupported() */
        outputParameters.hostApiSpecificStreamInfo = NULL;

        if( inputParameters.channelCount > 0 )
        {
            printf("Supported standard sample rates\n for half-duplex 16 bit %d channel input = \n",
                    inputParameters.channelCount );
            PrintSupportedStandardSampleRates( &inputParameters, NULL );
        }

        if( outputParameters.channelCount > 0 )
        {
            printf("Supported standard sample rates\n for half-duplex 16 bit %d channel output = \n",
                    outputParameters.channelCount );
            PrintSupportedStandardSampleRates( NULL, &outputParameters );
        }

        if( inputParameters.channelCount > 0 && outputParameters.channelCount > 0 )
        {
            printf("Supported standard sample rates\n for full-duplex 16 bit %d channel input, %d channel output = \n",
                    inputParameters.channelCount, outputParameters.channelCount );
            PrintSupportedStandardSampleRates( &inputParameters, &outputParameters );
        }
    } /* for numDevices */
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD


	printf("What input device would you like to use? ");
	fgets(devSelection, 4, stdin);
	devSelectInt	= atoi(devSelection);

=======
	
	
	printf("What input device would you like to use? ");	
	fgets(devSelection, 4, stdin);
	devSelectInt	= atoi(devSelection);
	
>>>>>>> 65cc807... Can select device with portaudio
=======
=======
>>>>>>> 726f4d5... Commit after aborting the rebase


	printf("What input device would you like to use? ");
	fgets(devSelection, 4, stdin);
	devSelectInt	= atoi(devSelection);

<<<<<<< HEAD
<<<<<<< HEAD
=======
=======
	
	
	printf("What input device would you like to use? ");	
	fgets(devSelection, 4, stdin);
	devSelectInt	= atoi(devSelection);
	
>>>>>>> 0e0e962... Can select device with portaudio
=======


	printf("What input device would you like to use? ");
	fgets(devSelection, 4, stdin);
	devSelectInt	= atoi(devSelection);

>>>>>>> 726f4d5... Commit after aborting the rebase
>>>>>>> 62b84d2... Trying 2 lines
=======
>>>>>>> 35e86f1... Trying 2 lines
>>>>>>> d43f794... Second rebase abort
	if(devSelectInt >= numDevices)
	{
		printf("Sorry; that number is out of the parameters; must be less than %d.", numDevices);
		return -1;
	} // if
<<<<<<< HEAD
<<<<<<< HEAD

	return devSelectInt;
} // selectDevice
=======
}
>>>>>>> cca125d... First attempts at getting audio with portaudio
=======
	
=======

>>>>>>> 35e86f1... Trying 2 lines
	return devSelectInt;
} // selectDevice
>>>>>>> 65cc807... Can select device with portaudio

/*******************************************************************/
int main(void);
int main(void)
{
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
	int					device;
=======
>>>>>>> cca125d... First attempts at getting audio with portaudio
=======
	int					device;
<<<<<<< HEAD
=======
	int					device;
=======
>>>>>>> fd9d8bb... First attempts at getting audio with portaudio
=======
	int					device;
>>>>>>> 726f4d5... Commit after aborting the rebase
>>>>>>> 0e0e962... Can select device with portaudio
=======
>>>>>>> 65cc807... Can select device with portaudio
>>>>>>> d43f794... Second rebase abort
    PaStreamParameters  inputParameters,
                        outputParameters;
    PaStream*           stream;
    PaStream*           stream1;
    PaError             err = paNoError;
    paTestData          data;
    paTestData          data1;
    int                 i;
    int                 totalFrames;
    int                 numSamples;
    int                 numBytes;
    SAMPLE              max, val;
    double              average;
<<<<<<< HEAD
<<<<<<< HEAD
    int					numDevices;
=======
>>>>>>> cca125d... First attempts at getting audio with portaudio
=======
    int					numDevices;
>>>>>>> 35e86f1... Trying 2 lines

    printf("patest_record.c\n"); fflush(stdout);

    data.maxFrameIndex = totalFrames = NUM_SECONDS * SAMPLE_RATE; /* Record for a few seconds. */
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> 726f4d5... Commit after aborting the rebase
<<<<<<< HEAD
	printf("maxFrameIndex = %d.\n", data.maxFrameIndex);
=======
<<<<<<< HEAD
>>>>>>> fd9d8bb... First attempts at getting audio with portaudio
<<<<<<< HEAD
=======
	printf("maxFrameIndex = %d.\n", data.maxFrameIndex);
>>>>>>> e4bfc26... The remainder of Emily on the Windows laptop for now
=======
>>>>>>> 726f4d5... Commit after aborting the rebase
=======
>>>>>>> cca125d... First attempts at getting audio with portaudio
>>>>>>> d43f794... Second rebase abort
    data.frameIndex = 0;
    numSamples = totalFrames * NUM_CHANNELS;
    numBytes = numSamples * sizeof(SAMPLE);
    data.recordedSamples = (SAMPLE *) malloc( numBytes ); /* From now on, recordedSamples is initialised. */
    if( data.recordedSamples == NULL )
    {
        printf("Could not allocate record array.\n");
        goto done;
    }
    for( i=0; i<numSamples; i++ ) data.recordedSamples[i] = 0;

    err = Pa_Initialize();
    if( err != paNoError ) goto done;

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
    numDevices	= 2;

	// input stream:
	device	= selectDevice();
    inputParameters.device = device;
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> 726f4d5... Commit after aborting the rebase
=======
    inputParameters.device = Pa_GetDefaultInputDevice(); /* default input device */
>>>>>>> cca125d... First attempts at getting audio with portaudio
=======
=======
    numDevices	= 2;

<<<<<<< HEAD
<<<<<<< HEAD
	// input stream 1:
>>>>>>> 62b84d2... Trying 2 lines
=======
	// input stream:
>>>>>>> b6f213c... More with ASIO input testing
	device	= selectDevice();
    inputParameters.device = device; /* default input device */
>>>>>>> 0e0e962... Can select device with portaudio
=======
>>>>>>> e4bfc26... The remainder of Emily on the Windows laptop for now
=======
	// input stream 1:
>>>>>>> 35e86f1... Trying 2 lines
	device	= selectDevice();
    inputParameters.device = device; /* default input device */
<<<<<<< HEAD
>>>>>>> 0e0e962... Can select device with portaudio
>>>>>>> 726f4d5... Commit after aborting the rebase
=======
>>>>>>> 65cc807... Can select device with portaudio
>>>>>>> d43f794... Second rebase abort
    if (inputParameters.device == paNoDevice) {
        fprintf(stderr,"Error: No default input device.\n");
        goto done;
    }
    inputParameters.channelCount = 2;                    /* stereo input */
    inputParameters.sampleFormat = PA_SAMPLE_TYPE;
    inputParameters.suggestedLatency = Pa_GetDeviceInfo( inputParameters.device )->defaultLowInputLatency;
    inputParameters.hostApiSpecificStreamInfo = NULL;

    /* Record some audio. -------------------------------------------- */
    err = Pa_OpenStream(
              &stream,
              &inputParameters,
              NULL,                  /* &outputParameters, */
              SAMPLE_RATE,
              FRAMES_PER_BUFFER,
              paClipOff,      /* we won't output out of range samples so don't bother clipping them */
              recordCallback,
              &data );
    if( err != paNoError ) goto done;

    err = Pa_StartStream( stream );
    if( err != paNoError ) goto done;
    printf("\n=== Now recording!! Please speak into the microphone. ===\n"); fflush(stdout);

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> 726f4d5... Commit after aborting the rebase
<<<<<<< HEAD
	// while active:
=======
>>>>>>> cca125d... First attempts at getting audio with portaudio
=======
	// input stream 2:
		device	= selectDevice();
	    inputParameters.device = device; /* default input device */
	    if (inputParameters.device == paNoDevice) {
	        fprintf(stderr,"Error: No default input device.\n");
	        goto done;
	    }
	    inputParameters.channelCount = 2;                    /* stereo input */
	    inputParameters.sampleFormat = PA_SAMPLE_TYPE;
	    inputParameters.suggestedLatency = Pa_GetDeviceInfo( inputParameters.device )->defaultLowInputLatency;
	    inputParameters.hostApiSpecificStreamInfo = NULL;

	    /* Record some audio. -------------------------------------------- */
	    err = Pa_OpenStream(
	              &stream1,
	              &inputParameters,
	              NULL,                  /* &outputParameters, */
	              SAMPLE_RATE,
	              FRAMES_PER_BUFFER,
	              paClipOff,      /* we won't output out of range samples so don't bother clipping them */
	              recordCallback,
	              &data1 );
	    if( err != paNoError ) goto done;

	    err = Pa_StartStream( stream1 );
	    if( err != paNoError ) goto done;
	    printf("\n=== Now recording!! Please speak into the microphone. ===\n"); fflush(stdout);

<<<<<<< HEAD
=======
>>>>>>> b6f213c... More with ASIO input testing
=======
>>>>>>> 726f4d5... Commit after aborting the rebase
	// while active:
>>>>>>> 35e86f1... Trying 2 lines
    while( ( err = Pa_IsStreamActive( stream ) ) == 1 )
    {
        Pa_Sleep(100);
        printf("index = %d; amplitude = %f\n", data.frameIndex, data.recordedSamples[data.frameIndex] ); fflush(stdout);
    }
    if( err < 0 ) goto done;

    err = Pa_CloseStream( stream );
    if( err != paNoError ) goto done;

	err = Pa_CloseStream( stream1 );
    if( err != paNoError ) goto done;

    /* Measure maximum peak amplitude. */
    max = 0;
    average = 0.0;
    for( i=0; i<numSamples; i++ )
    {
        val = data.recordedSamples[i];
        if( val < 0 ) val = -val; /* ABS */
        if( val > max )
        {
            max = val;
        }
        average += val;
    }

    average = average / (double)numSamples;

    printf("sample max amplitude = "PRINTF_S_FORMAT"\n", max );
    printf("sample average = %lf\n", average );

    /* Write recorded data to a file. */
#if WRITE_TO_FILE
    {
        FILE  *fid;
        fid = fopen("recorded.raw", "wb");
        if( fid == NULL )
        {
            printf("Could not open file.");
        }
        else
        {
            fwrite( data.recordedSamples, NUM_CHANNELS * sizeof(SAMPLE), totalFrames, fid );
            fclose( fid );
            printf("Wrote data to 'recorded.raw'\n");
        }
    }
#endif

    /* Playback recorded data.  -------------------------------------------- */
    data.frameIndex = 0;

    outputParameters.device = Pa_GetDefaultOutputDevice(); /* default output device */
    if (outputParameters.device == paNoDevice) {
        fprintf(stderr,"Error: No default output device.\n");
        goto done;
    }
    outputParameters.channelCount = 2;                     /* stereo output */
    outputParameters.sampleFormat =  PA_SAMPLE_TYPE;
    outputParameters.suggestedLatency = Pa_GetDeviceInfo( outputParameters.device )->defaultLowOutputLatency;
    outputParameters.hostApiSpecificStreamInfo = NULL;

    printf("\n=== Now playing back. ===\n"); fflush(stdout);
    err = Pa_OpenStream(
              &stream,
              NULL, /* no input */
              &outputParameters,
              SAMPLE_RATE,
              FRAMES_PER_BUFFER,
              paClipOff,      /* we won't output out of range samples so don't bother clipping them */
              playCallback,
              &data );
    if( err != paNoError ) goto done;

    if( stream )
    {
        err = Pa_StartStream( stream );
        if( err != paNoError ) goto done;
<<<<<<< HEAD
<<<<<<< HEAD

=======
        
>>>>>>> cca125d... First attempts at getting audio with portaudio
=======

>>>>>>> 35e86f1... Trying 2 lines
        printf("Waiting for playback to finish.\n"); fflush(stdout);

        while( ( err = Pa_IsStreamActive( stream ) ) == 1 ) Pa_Sleep(100);
        if( err < 0 ) goto done;
<<<<<<< HEAD
<<<<<<< HEAD

        err = Pa_CloseStream( stream );
        if( err != paNoError ) goto done;

=======
        
        err = Pa_CloseStream( stream );
        if( err != paNoError ) goto done;
        
>>>>>>> cca125d... First attempts at getting audio with portaudio
=======

        err = Pa_CloseStream( stream );
        if( err != paNoError ) goto done;

>>>>>>> 35e86f1... Trying 2 lines
        printf("Done.\n"); fflush(stdout);
    }

done:
    Pa_Terminate();
    if( data.recordedSamples )       /* Sure it is NULL or valid. */
        free( data.recordedSamples );
    if( err != paNoError )
    {
        fprintf( stderr, "An error occured while using the portaudio stream\n" );
        fprintf( stderr, "Error number: %d\n", err );
        fprintf( stderr, "Error message: %s\n", Pa_GetErrorText( err ) );
        err = 1;          /* Always return 0 or 1, but no other return codes. */
    }
    return err;
}

