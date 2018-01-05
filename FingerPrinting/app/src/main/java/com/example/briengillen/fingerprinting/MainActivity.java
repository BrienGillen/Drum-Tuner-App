package com.example.briengillen.fingerprinting;

//Imports used throughout class
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


/**
 * Author: Brien Gillen
 * Project: MSc Individual Final Project
 * Completion Date: 13/09/17
 */

public class MainActivity extends AppCompatActivity {

    // Creation of Intent object to allow the app to access other activities
    Intent intent;

    // Creation of String type, which will be asigned as the type of Pitch Tracking the user
    // wants to do, i.e. using the tuner or the templates
    String type;

    // Override of onCreate method that is used to run the contained code on the creation of the class
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setContentView method is used to select the xml file which the Java relates to
        setContentView(R.layout.activity_main);

        // Defining variables of type Button using the findViewById to select the
        // required button from the xml file (where the id's are stored)
        Button tune = (Button) findViewById(R.id.button3);
        Button template = (Button) findViewById(R.id.button4);

        // Instantiation of intent using two contexts, the first is the context
        // the app is currently in, the second is the context the app should go to when the
        // intent is ran
        intent = new Intent(MainActivity.this, TemplateInstrument.class);


        // -- register click event with first button ---
        tune.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Set the String
                type = "tune";

                // Add the String type to the intent under the name KEY to allow the
                // system to access it in the next context
                intent.putExtra("KEY", type);

                // Run the intent
                startActivity(intent);

            }
        });

        // -- register click event with second button ---
        template.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Set the String
                type = "template";

                // Add the String type to the intent under the name KEY to allow the
                // system to access it in the next context
                intent.putExtra("KEY", type);

                // Run the intent
                startActivity(intent);

            }
        });
    }

}
