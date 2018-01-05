package com.example.briengillen.fingerprinting;

/**
 * Created by briengillen on 05/09/2017.
 */


import android.media.MediaRecorder;
import android.os.Handler;
import java.io.File;
import java.io.IOException;

/**
 * Author: Brien Gillen
 * Project: MSc Individual Final Project
 * Completion Date: 13/09/17
 */

public class AudioInput {

    // Output file to store the recorded audio from the user's microphone
    private static File outFile = null;

    // Media recorder used to interface between the phone microphone and
    // the app
    private static MediaRecorder recorder = null;

    // String used to represent the text that should be output when
    // the recorder is in a certain state
    String running;

    // int used to set the maximum length of the recording
    int maxRecordTime;


    // Empty constructor for the class
    public AudioInput(){
    }


    /**
     * Class used to start the recorder and store the results in a file
     *
     * @param audioFile - the name the user wishes to give the audiofile when it is stored
     * @param fullPath - the String that represents the exact location where the folder to contain
     *                 the mp3s is
     * @param directPath - the String that represents the exact path where the mp3 files are
     *
     */
    public String startRecord(String audioFile, String fullPath, String directPath) {


        // int used to set the amount of time the recorder can run for
        maxRecordTime = 5000;

        // Opening of try to surround the file information
        try {

            // If statement used to check if the recorder is already active
            if (recorder != null) {

                // Setting the string value for the situation where the user tries to
                // start the recorder when the recorder is already in an active state
                running = "Recorder is already running \nPlease wait until it is finished";

            } else {


                // String to check if an SD card is mounted on the device
                String state = android.os.Environment.getExternalStorageState();

                // If statement that throws an exception if there is no SD card on the device
                if (!state.equals(android.os.Environment.MEDIA_MOUNTED)) {

                } else {

                    // The directory for the storage of the audio files recorded by the application
                    File storageDir = new File(directPath);

                    // If statement to check if the directory above is real
                    if (storageDir.isDirectory()) {

                        // Setting the String value to let the user know that the recorder is active
                        running = "Recording...";

                        // outFile is a file for storing the audio data from the recording
                        outFile = new File(fullPath + "/" + audioFile);

                        // Creation of the mediarecorder
                        recorder = new MediaRecorder();

                        // The setting of the source of the audio to be the device microphone
                        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                        // The setting of the output file in terms of format, encoding,
                        // the maximum length of the recording and the path where it should be stored
                        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                        recorder.setMaxDuration(maxRecordTime);
                        recorder.setOutputFile(outFile.getAbsolutePath());

                        // Preparing and starting the media recorder
                        recorder.prepare();
                        recorder.start();

                        // Handler used to stop the recorder and change the String value after
                        // the maxRecordTime is up
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                stopRecord();
                            }
                        }, maxRecordTime);

                        // Else statement for if the directory doesn't exist
                    } else {

                        running = "Storage location does not exist... \nStoring to different location";


                    }

                }
            }
                // Catch to close off try surrounding file data
            } catch(IOException e){
                e.printStackTrace();
            }

            // return the String value to let the user know what state the recorder is in
            return running;
        }

    /**
     * Method used to stop the recording of audio
     */
    public String stopRecord(){

        // If statement to see if the recorder is active or not
        if(recorder != null) {

            // Set the String to empty if the recorder is not running
            running = "";

            // Stopping and releasing the media recorder
            recorder.stop();
            recorder.reset();
            recorder.release();
            recorder = null;

            // Else statement for if the recorder is not active
        } else {

            // Set the String value for when the user presses the stop button
            // when the recorder is already stopped
            running =  "Recording has already been stopped";

        }

        // return the String value to let the user know what state the recorder is in
        return running;
    }
}

