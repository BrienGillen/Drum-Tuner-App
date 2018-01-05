package com.example.briengillen.fingerprinting;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.felipecsl.gifimageview.library.GifImageView;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Author: Brien Gillen
 * Project: MSc Individual Final Project
 * Completion Date: 13/09/17
 */

public class LoadScreen extends AppCompatActivity {

    // Creation of object GifImageView from openResource felipecsl, which can
    // be found at https://github.com/felipecsl/GifImageView all credit goes to the creators
    private GifImageView gifImageView;


    // Override of onCreate method that is used to run the contained code on the creation of the class
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setContentView method is used to select the xml file which the Java relates to
        setContentView(R.layout.activity_load_screen);

        // Defining the variable for gifImageView using the id created in the xml
        gifImageView = (GifImageView) findViewById(R.id.gifImage);

        // int representing the amount of time the gif should run for
        int gifRunTime;

        try {
            //Set gif image resource
            InputStream inputStream = getAssets().open("loaderFast.gif");
            byte[] bytes = IOUtils.toByteArray(inputStream);
            gifImageView.setBytes(bytes);

            // Begin the animation for the gif
            gifImageView.startAnimation();
        }
        catch (IOException e){

        }

        // Setting the value of the gifRunTime to be 5000ms i.e 5 seconds
        gifRunTime = 5000;

        //Set wait time before starting app
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                // Create an Intent that chooses which context (activity) to open after the gif
                LoadScreen.this.startActivity(new Intent(LoadScreen.this, MainActivity.class));

                // Stop the loadscreen activity
                LoadScreen.this.finish();

                // Setting the wait time on the handler so that the gif
                // runs for the specified time
            }
        },gifRunTime);



    }

    @Override
    protected void onResume() {

        //so load screen does not run again when app is just being resumed
        super.onResume();

    }
}
