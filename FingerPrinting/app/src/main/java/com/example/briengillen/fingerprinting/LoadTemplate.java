package com.example.briengillen.fingerprinting;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * Author: Brien Gillen
 * Project: MSc Individual Final Project
 * Completion Date: 13/09/17
 */

public class LoadTemplate extends AppCompatActivity {

    // Creation of String arrays to collect the mp3 filenames from the getTemplates method and
    // from the directory respectively
    private String[] mp3s, loadmp3;

    // File array used to collect the mp3 files from the directory
    File[] mp3Files;

    // Initialisation of Button objects
    Button cont, home;

    // Creation of Intent
    Intent i;

    // Creation of the String used to get the directory to create the folders for storing the mp3 Files
    String firstPath = Environment.getExternalStorageDirectory().getAbsolutePath();

    // Strings used to represent the instrument name taken from the templateinstrument class
    // through an extra added to the intent and to represent the full file path used
    // to access the stored mp3 files
    String instrument, mp3Path;

    // String used to represent the folder path where the mp3 files are to be stored
    String fullPath;

    // File used to create the directory for storing mp3 files
    File direct;

    // Creation of MediaPlayer object
    MediaPlayer media = new MediaPlayer();


    // Override of onCreate method that is used to run the contained code on the creation of the class
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setContentView method is used to select the xml file which the Java relates to
        setContentView(R.layout.load_template);

        // Defining variables of type Button using the findViewById to select the
        // required button from the xml file (where the id's are stored)
        cont = (Button) findViewById(R.id.contButton);
        home = (Button) findViewById(R.id.homeButton);

        // Assignment of i to the Intent used to get to this context
        i = getIntent();

        // Initially setting the continue button to be invisible
        cont.setVisibility(View.INVISIBLE);

        // Getting all the extras from Intent i
        Bundle bundle = i.getExtras();

        // Parsing the extras in Strings where the Key or name for the String is "inst"
        instrument = bundle.getString("inst");

        // Setting the fullPath to be the storage directory followed by the name of the instrument
        // selected in the TemplateInstrument class
        fullPath = firstPath + "/" + instrument;

        // On click listener used to set what happens when home button is clicked
        home.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                // if to check if the media player is currently active
                if(media.isPlaying()) {

                    // If the media player is active when home is clicked it is paused
                    // then reset so the file doesn't continue to play when the user
                    // is not on the page
                    media.pause();
                    media.seekTo(0);
                }

                // Creation, setting and running of Intent to return to home page
                Intent home = new Intent(LoadTemplate.this, MainActivity.class);
                startActivity(home);
            }
        });

        // Establishing the new file where the mp3 files should be stored
        direct = new File(fullPath);

        // if statement to check if the file directory exists
    if(direct.isDirectory()) {

        // The String array is set to be the return of the method getTemplates below
        mp3s = getTemplates();

        // If the String array from above is either empty of null then the emptyArray
        // method is run
        if (mp3s == null || mp3s.length<1){
            emptyArray();

            // else referring to if the String Array has contents
        } else {

            // Creation of ArrayAdapter object used to populate the listview
            final ArrayAdapter<String> mAdapter = new ArrayAdapter(this,
                    android.R.layout.simple_list_item_1, mp3s);

            // Creation of list view object using the id from the xml
            ListView list = (ListView) findViewById(R.id.mp3List);

            // Setting the adapter for the list, meaning the list is populated with the ArrayAdapter which is
            // in turn populated with it's arguement the String array mp3s
            list.setAdapter(mAdapter);

            // Separate thread to be run to update the ArrayAdapter
            runOnUiThread(new Runnable() {
                public void run() {
                    mAdapter.notifyDataSetChanged();
                }
            });


            // On click listener used to perform an action when an item on the list is clicked
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                    // Set continue button to be visible
                    cont.setVisibility(View.VISIBLE);

                    // Creation of string to hold selected mp3 file
                    String chosenMP3;

                    // Chose the mp3 file from the array which populated the list
                    // Here arg2 is the default arguement for a list.setOnItemClickListener
                    // where arg2 represents the int position of the selected item
                    chosenMP3 = mp3s[arg2];

                    // Setting the path of the mp3 to be the selected mp3 to allow it to be played
                    mp3Path = fullPath + "/" + chosenMP3;

                    try {
                        // Calling the playSong method with the mp3Path specified within
                        playSong(mp3Path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    } else {

        // Calling the emptyArray for if the directory where the mp3
        // files should be stored doesn.t exist
        emptyArray();

    }

    // On click listener used for if continue button is clicked
        cont.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                // Checking if the media player is playing and pausing and resetting
                // it if it is
                if(media.isPlaying()) {
                    media.pause();
                    media.seekTo(0);
                }

                // Creating a new intent to take the system to the Tuner class
                // Sending along with the Intent is the instrument name, the class it came from
                // and the filepath for the selected mp3
                Intent contin = new Intent(LoadTemplate.this, Tuner.class);
                contin.putExtra("inst", instrument);
                contin.putExtra("from", "loader");
                contin.putExtra("filePath", mp3Path);

                // Starting activity
                startActivity(contin);
            }
        });
    }

        private String[] getTemplates(){


            // The directory for the storage of the audio files recorded by the application
            File storageDir = new File(fullPath);

            // Populating and array with the names of all the files in the storage directory
            mp3Files = storageDir.listFiles();

            // Creating a second array to be populated with the name of every file from mp3Files
            loadmp3 = new String[mp3Files.length];

            // Running a for loop for each file within the directory
            for (int i=0; i<mp3Files.length; i++){

                // Checking that the file array contains something
                if(!loadmp3.equals(null) || loadmp3.length > 0) {

                    // If the file array isn't empty the String array gets populated with the file names
                    loadmp3[i] = mp3Files[i].getName();

                } else {

                    // If the file array is empty the String array is null
                    loadmp3 = null;
                }
             }

             // Return the String array populated with names of mp3 files from the directory
            // or null
            return loadmp3;
        }

     private void playSong(String path) throws IllegalArgumentException,
            IllegalStateException, IOException {

         // Clear the media player
         media.reset();

         // Set the media player source to be the input file
         media.setDataSource(path);
         media.prepare();

         // Check if the media player is already playing, if so pause and reset it
         if(media.isPlaying()) {
             media.pause();
             media.seekTo(0);
         } else {
             // If the media player isn't playing then start it
             media.start();
         }

    }

    private void emptyArray (){

        // String array containing one value
        String[] empty = new String[1];

        // Setting the value to be message to user
        empty[0] = "No Templates Recorded Yet";

        // Setting continue button to invisible
        cont.setVisibility(View.INVISIBLE);

        // Creation of ArrayAdapter object used to populate the listview
        final ArrayAdapter<String> mAdapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, empty);

        // Creation of list view object using the id from the xml
        ListView list = (ListView) findViewById(R.id.mp3List);

        // Setting the adapter for the list
        list.setAdapter(mAdapter);

        // On click listener used to perform an action when an item on the list is clicked
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                // Inflater and view used to set the toast style to be the custom
                // toast made in layout xml
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.toast_container,
                        (ViewGroup) findViewById(R.id.custom_toast));

                // Setting a text view to hold the Toast text
                TextView text = (TextView) layout.findViewById(R.id.toastText);
                text.setText("Please record a template before continuing");

                // Creating, generating setting and running toast method
                Toast toast = Toast.makeText(LoadTemplate.this,"Please record a template before continuing", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.setView(layout);
                toast.show();
            }
        });

    }
}


