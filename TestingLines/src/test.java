// File: %Z%%M% %I% %E% 
// Copyright %G% Sun Microsystems, Inc. All Rights Reserved

import javax.sound.sampled.*;

public class test {

    static final int STATUS_PASSED = 0;
    static final int STATUS_FAILED = 2;
    static final int STATUS_TEMP = 95;
    
    public static void main(String argv[]) {
        int testExitStatus = run(argv, System.out) + STATUS_TEMP;
        System.exit(testExitStatus);
    }

    public static int run(String argv[], java.io.PrintStream out) {
        int testResult = STATUS_PASSED;
    
        out.println
            ("==> Test for Mixer.getMaxLines(Line.Info) method:");

        Mixer.Info[] installedMixersInfo = AudioSystem.getMixerInfo();
        
        if ( installedMixersInfo == null ) {
            out.println("## AudioSystem.getMixerInfo() returned unexpected result:");
            out.println("#  expected: an array of Mixer.Info objects (may be array of length 0);");
            out.println("#  produced: null;");
            return STATUS_FAILED;
        }

        if ( installedMixersInfo.length == 0 ) {
            // there are no mixers installed on the system - so this testcase can not be tested
            return STATUS_PASSED;
        }

        Mixer testedMixer = null;
        for (int i=0; i < installedMixersInfo.length; i++) {
            try {
                testedMixer = AudioSystem.getMixer(installedMixersInfo[i]);
            } catch (SecurityException securityException) {
                // installed Mixer is unavailable because of security restrictions
                continue;
            } catch (Throwable thrown) {
                out.println("## AudioSystem.getMixer() threw unexpected exception:");
                thrown.printStackTrace(out);
                testResult = STATUS_FAILED;
                continue;
            }

            try {
                testedMixer.open();
            } catch (LineUnavailableException lineUnavailableException) {
                // testedMixer is not available due to resource restrictions
                continue;
            } catch (SecurityException securityException) {
                // testedMixer is not available due to security restrictions
                continue;
            } catch (Throwable thrown) {
                out.println("## Mixer.open() threw unexpected exception:");
                out.println("#  Mixer = " + testedMixer);
                thrown.printStackTrace(out);
                testResult = STATUS_FAILED;
                continue;
            }
            Line.Info supportedSourceLineInfo[] = null;
            try {
                supportedSourceLineInfo = testedMixer.getSourceLineInfo();
            } catch (Throwable thrown) {
                out.println("## Mixer.getSourceLineInfo() threw unexpected exception:");
                out.println("#  Mixer = " + testedMixer);
                thrown.printStackTrace(out);
                testResult = STATUS_FAILED;
                testedMixer.close();
                continue;
            }
            if ( supportedSourceLineInfo == null ) {
                out.println("## Mixer.getSourceLineInfo() returned null array");
                out.println("#  Mixer = " + testedMixer);
                testResult = STATUS_FAILED;
                testedMixer.close();
                continue;
            }
            out.println("\n>>>  testedMixer["+i+"] = " + testedMixer);
            out.println("\n>>  supportedSourceLineInfo.length = " + supportedSourceLineInfo.length);

            for (int j=0; j < supportedSourceLineInfo.length; j++) {
                Line.Info testSourceLineInfo = supportedSourceLineInfo[j];
                out.println("\n>  testSourceLineInfo["+j+"] = " + testSourceLineInfo);

                int maxLinesNumberToOpen = testedMixer.getMaxLines(testSourceLineInfo);
                out.println("\n>  getMaxLines(testSourceLineInfo) = " + maxLinesNumberToOpen);
                if ( maxLinesNumberToOpen == AudioSystem.NOT_SPECIFIED ) {
                    // there is no limit for open lines number of this Line.Info type
                    // so this test case for this Line.Info type can not be tested
                    break;
                }
                if ( maxLinesNumberToOpen < 0 ) {
                    out.println("## Mixer.getMaxLines(Line.Info) FAILED:");
                    out.println("#  Mixer = " + testedMixer);
                    out.println("#  Line.Info = " + testSourceLineInfo);
                    out.println("#  unexpected negative int value is returned = " + maxLinesNumberToOpen);
                    testResult = STATUS_FAILED;
                    break;
                }

                int linesNumberToOpen = maxLinesNumberToOpen + 1;
                Line testSourceLine = null;
                Line openLines[] = new Line[linesNumberToOpen];
                int openLinesNumber = 0;
                for (int k=0; k < linesNumberToOpen; k++) {
                    try {
                        testSourceLine = testedMixer.getLine(testSourceLineInfo);
                    } catch (LineUnavailableException lineUnavailableException) {
                        // line is not available due to resource restrictions
                        break;
                    } catch (SecurityException securityException) {
                        // line is not available due to security restrictions
                        break;
                    } catch (Throwable thrown) {
                        out.println("## Mixer.getLine(Line.Info) threw unexpected Exception:");
                        out.println("#  Mixer = " + testedMixer);
                        out.println("#  Line.Info = " + testSourceLineInfo);
                        thrown.printStackTrace(out);
                        testResult = STATUS_FAILED;
                        break;
                    }

                    boolean openLineResult = false;
                    boolean isExpectedLineUnavailableException = false;
                    try {
                        openLineResult = openLine(testSourceLine);
                    } catch (LineUnavailableException lineUnavailableException) {
                        if ( k == maxLinesNumberToOpen ) {
                            isExpectedLineUnavailableException = true;
                        } else {
                            // line is not available due to resource restrictions
                            // so this test case for this line can not be tested
                            break;
                        }
                    } catch (Throwable thrown) {
                        out.println("## Unexpected Exception was thrown by Line.open():");
                        out.println("#  Line = " + testSourceLine);
                        thrown.printStackTrace(out);
                        testResult = STATUS_FAILED;
                        break;
                    }
                    if ( ! openLineResult ) {
                        if ( isExpectedLineUnavailableException ) {
                            // OK - expected LineUnavailableException was thrown while line openning
                            // because number of open lines has come up maxLinesNumberToOpen
                            out.println(">  OK - expected LineUnavailableException was thrown while line openning");
                            out.println(">  because number of open lines has come up maxLinesNumberToOpen");
                            out.println(">  Number of open Lines = " + openLinesNumber);
                            break;
                        }
                        // else
                        // Line was not opened due to security restrictions
                        out.println(">  Line of this type was not opened due to security restrictions");
                        break;
                    }
                    openLines[openLinesNumber] = testSourceLine;
                    openLinesNumber++;
                    if ( openLinesNumber > maxLinesNumberToOpen ) {
                        out.println("## Mixer.getMaxLines(Line.Info) FAILED:");
                        out.println("#  Mixer = " + testedMixer);
                        out.println("#  Line.Info = " + testSourceLineInfo);
                        out.println("#  Mixer.getMaxLines(Line.Info) = " + maxLinesNumberToOpen);
                        out.println("#  Number of open Lines         = " + openLinesNumber);
                        for (int m=0; m < openLinesNumber; m++) {
                            out.println("#  openLines[" + m + "] = " + openLines[m]);
                        }
                        testResult = STATUS_FAILED;
                    }

                }  // for (int k=0; k < linesNumberToOpen; k++)

                for (int k=0; k < openLinesNumber; k++) {
                    openLines[k].close();
                }
                
            }  // for (int j=0; j < supportedSourceLineInfo.length; j++)
            testedMixer.close();

        }  // for (int i=0; i < installedMixersInfo.length; i++)

        if ( testResult == STATUS_FAILED ) {
            out.println("\n==> test FAILED!");
        } else {
            out.println("\n==> test PASSED!");
        }
        return testResult;
    }
    
    static boolean openLine(Line lineToOpen) throws Throwable {
        boolean openResult = false;

        try {
            if ( (lineToOpen instanceof Clip) ) {
                int bufferSizeForOpen = 1;
                byte[] dataForOpen = new byte[bufferSizeForOpen];
                for (int i=0; i < bufferSizeForOpen; i++) {
                    dataForOpen[i] = (byte)1;
                }
                int offsetForOpen = 0;
                AudioFormat audioFormatForOpen =
                    new AudioFormat(((Clip)lineToOpen).getFormat().getEncoding(),  // AudioFormat.Encoding
                                    (float) 44100.0, // float SampleRate: the number of samples per second
                                    (int) 8, // int sampleSizeInBits
                                    (int) 1, // int channels
                                    (int) 1,    // int frameSize in bytes
                                    (float) 44100.0,    // float frameRate: the number of frames per second
                                    true    // boolean bigEndian
                                    );
                ((Clip)lineToOpen).open(audioFormatForOpen, dataForOpen, offsetForOpen, bufferSizeForOpen);
                openResult = true;
            }
            if ( (lineToOpen instanceof SourceDataLine) ) {
                AudioFormat audioFormatForOpen = ((SourceDataLine)lineToOpen).getFormat();
                ((SourceDataLine)lineToOpen).open(audioFormatForOpen);
                openResult = true;
            }
        } // try
        catch(Exception e) {
        	throw new IllegalArgumentException("test: caught Exception " + e);
        } // catch
        
        return openResult;
    } // openLine
}