package com.example.briengillen.fingerprinting;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


/**
 * Author: Brien Gillen
 * Project: MSc Individual Final Project
 * Completion Date: 13/09/17
 */

public class SnareActivity extends AppCompatActivity {

    // AudioInput object creation
    AudioInput record = new AudioInput();

    // String used to represent the instrument selected in the templateinstrument class
    String instrument;

    // Intent creation
    Intent i;

    // Creation of int used to set the length of time of a recording
    int maxRecordTime;

    // Textview used to display if the recorder is not running or already running
    TextView text;

    // ImageView used to show whether or not an application is recording
    ImageView image;

    // Override of onCreate method that is used to run the contained code on the creation of the class
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setContentView method is used to select the xml file which the Java relates to
        setContentView(R.layout.record_snare);

        // Setting the intent i to be the intent used to access this page
        i = getIntent();

        // Setting a bundle to the values of any extras passed through on the intent
        Bundle bundle = i.getExtras();

        // Setting the TextView to associate with id recText
        text = (TextView) findViewById(R.id.recText);

        // Setting the ImageView to associate with id stopImage
        image = (ImageView) findViewById(R.id.stopImage);

        // Setting the String instrument to be the name of the drum
        // selected in the teamplate instrument page
        instrument = bundle.getString("inst");

        // Set the value for the max recording time
        maxRecordTime = 5000;

        // Setting Buttons to relate to each button id in the xml
        Button start = (Button) findViewById(R.id.recStart);
        Button stop = (Button) findViewById(R.id.recStop);
        Button home = (Button) findViewById(R.id.recordH);
        Button contin = (Button) findViewById(R.id.recCont);


        // Blank the textview at the start of recording (if the text view reads about the recording being already in progress
        // or the recording already having been stopped the text will be cleared)
        text.setText("");

        // -- register click event with first button ---
        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                image.setBackgroundResource(R.drawable.rec);

                // Use the String instrument (taken from TemplateInstrument) to create a unique file name
                String myRecording = instrument + "-" + System.currentTimeMillis() + ".mp3";

                // Get the default file storage directory
                String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath();

                //Create a new folder within the directory with the name of instrument
                File outputFile = new File(fullPath, instrument + "/");

                // If the folder doesn't exist then create it
                    if(!outputFile.exists()) {
                        outputFile.mkdir();
                    }

                try {
                    FileOutputStream fos = new FileOutputStream(outputFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                // Create a String equal to the new full path for the mp3 file
                String secondPath = fullPath + "/" + instrument;

                // Start a recording using the variables assigned above and set the output
                // to be the TextView text
                text.setText(record.startRecord(myRecording, secondPath, fullPath));

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        image.setBackgroundResource(R.drawable.stop);
                        text.setText("");
                    }
                }, maxRecordTime);


            }
        });

        // -- register click event with second button ---
        stop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                image.setBackgroundResource(R.drawable.stop);

                text.setText(record.stopRecord());

            }
        });

        home.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent home = new Intent(SnareActivity.this, MainActivity.class);
                startActivity(home);
            }
        });

        contin.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent cont = new Intent(SnareActivity.this, LoadTemplate.class);
                cont.putExtra("inst", instrument);
                startActivity(cont);
            }
        });
    }

}
