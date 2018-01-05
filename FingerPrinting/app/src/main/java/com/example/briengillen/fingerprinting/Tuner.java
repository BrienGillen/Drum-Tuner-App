package com.example.briengillen.fingerprinting;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;

/**
 * Created by briengillen on 11/09/2017.
 */

public class Tuner extends AppCompatActivity {

    Button micOn, micOff, home;
    TextView difference, current, targetText, warning;
    ImageView micImage;
    Spinner target;

    long targ, diff, curr;

    String[] frequencies;

    String instrument, from, mp3Path;

    Intent i;

    boolean runner = false;

    long currentPitch = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tuner);


        micOn = (Button) findViewById(R.id.micOn);
        micOff = (Button) findViewById(R.id.micOff);
        home = (Button) findViewById(R.id.tunerHome);

        micImage = (ImageView) findViewById(R.id.micImage);

        target = (Spinner) findViewById(R.id.target);
        difference = (TextView) findViewById(R.id.difference);
        current = (TextView) findViewById(R.id.current);

        current.setVisibility(View.INVISIBLE);
        difference.setVisibility(View.INVISIBLE);

        targetText = (TextView) findViewById(R.id.targetText);
        warning = (TextView) findViewById(R.id.warningMessage);

        difference.setText("");
        current.setText("");

        frequencies = new String[3];

        i = getIntent();

        Bundle bundle = i.getExtras();

        instrument = bundle.getString("inst");

        from = bundle.getString("from");

        mp3Path = bundle.getString("filePath");

        if(from.equalsIgnoreCase("template")){
            targetText.setVisibility(View.INVISIBLE);
            target.setVisibility(View.VISIBLE);


        if(instrument.equalsIgnoreCase("snare")) {
            frequencies = new String[]{"763", "783", "803"};
        } else if(instrument.equalsIgnoreCase("tom")){
            frequencies = new String[]{"567", "587", "607"};
        } else if(instrument.equalsIgnoreCase("floor")){
            frequencies = new String[]{"329", "349", "369"};
        } else if(instrument.equalsIgnoreCase("kick")){
            frequencies = new String[]{"144", "164", "184"};
        }

        ArrayAdapter<String> adapt = new ArrayAdapter<String>(this, R.layout.spinner_item, frequencies);
        adapt.setDropDownViewResource(R.layout.spinner_dropdown_item);

        target.setAdapter(adapt);

        targ = Long.parseLong(target.getSelectedItem().toString());

        } else if(from.equalsIgnoreCase("loader")){
            targetText.setVisibility(View.VISIBLE);
            target.setVisibility(View.INVISIBLE);
            targ = 530;
            targetText.setText(Long.toString(targ));
        }
        micProcess();


        // -- register click event with first button ---
        micOn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (!runner) {

                    micImage.setBackgroundResource(R.drawable.micon);

                    current.setVisibility(View.VISIBLE);
                    difference.setVisibility(View.VISIBLE);

                    runner = true;
            } else {
                    warning.setText("Microphone is already on");

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            warning.setText("");
                        }
                    }, 3000);
                }
            }
            });


        micOff.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (runner) {
                    micImage.setBackgroundResource(R.drawable.micoff);


                    current.setVisibility(View.INVISIBLE);
                    difference.setVisibility(View.INVISIBLE);
                    runner = false;
                } else {
                    warning.setText("Microphone is already off");

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            warning.setText("");
                        }
                    }, 3000);
                }
            }

        });

        home.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent home = new Intent(Tuner.this, MainActivity.class);
                startActivity(home);
                finish();
            }
        });

    }


    public void micProcess() {
        // Create dispatcher to select the input as the device microphone
        AudioDispatcher dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0);

        PitchDetectionHandler pdh = new PitchDetectionHandler() {
            @Override
            public void handlePitch(PitchDetectionResult res, AudioEvent e) {

                // Set a final float value to return the pitch in Hz of the event
                final float pitchInHz = res.getPitch();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Set the float variable curr (current pitch) equal to the pitchInHz output
                        curr = 3 * Math.round(pitchInHz);
                        if (instrument.equalsIgnoreCase("snare")) {
                            if (curr > 700 && curr < 900) {
                                currentPitch = curr;
                            }
                        } else if (instrument.equalsIgnoreCase("tom")) {
                            if (curr > 500 && curr < 700) {
                                currentPitch = curr;
                            }
                        } else if (instrument.equalsIgnoreCase("floor")) {
                            if (curr > 300 && curr < 500) {
                                currentPitch = curr;
                            }
                        } else if (instrument.equalsIgnoreCase("kick")) {
                            if (curr > 100 && curr < 300) {
                                currentPitch = curr;
                            }
                        }

                        current.setText(Long.toString(currentPitch));

                        diff = currentPitch - targ;
                        if (diff > 0) {
                            difference.setText("+" + Double.toString(diff));
                        } else {
                            difference.setText(Double.toString(diff));
                        }

                    }
                });
            }
        };

        // Set up a processor to get an estimate of the pitch given the hanlder specified above
        AudioProcessor pitchProcessor = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, pdh);

        // Set the processor of the dispatcher to run the pitch estimation algoritm on the microphone input
        dispatcher.addAudioProcessor(pitchProcessor);

        // Create and run a new thread to run audio so as not to back up UI thread
        Thread audioThread = new Thread(dispatcher, "Audio Thread");
        audioThread.start();
    }
}
