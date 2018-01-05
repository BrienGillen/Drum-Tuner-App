package com.example.briengillen.fingerprinting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Created by briengillen on 09/09/2017.
 */

public class TemplateInstrument extends AppCompatActivity {

    String choice, chosenType;

    Button snare, tom, floor, kick, home;

    ImageButton snareb, tomb, floorb, kickb;

    Intent intent, i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template_instrument);

        snare = (Button) findViewById(R.id.snareTemp);
        snareb = (ImageButton) findViewById(R.id.snareButton);

        tom = (Button) findViewById(R.id.tomTemp);
        tomb = (ImageButton) findViewById(R.id.rackButton);

        floor = (Button) findViewById(R.id.floorTemp);
        floorb = (ImageButton) findViewById(R.id.floorButton);

        kick = (Button) findViewById(R.id.kickTemp);
        kickb = (ImageButton) findViewById(R.id.kickButton);

        home = (Button) findViewById(R.id.homeTempI);

        i = getIntent();

        Bundle bundle = i.getExtras();

        if(bundle!=null) {
            chosenType = bundle.getString("KEY");
        }

        if(chosenType.equalsIgnoreCase("tune")){
            intent = new Intent(TemplateInstrument.this, Tuner.class);
        } else {
            intent = new Intent(TemplateInstrument.this, TemplateChoice.class);
        }

        snare.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                choice = "Snare";

                sendInstrument(choice, intent);
            }
        });

        snareb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                choice = "Snare";

                sendInstrument(choice, intent);
            }
        });

        tom.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                choice = "Tom";

                sendInstrument(choice, intent);
            }
        });

        tomb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                choice = "Tom";

                sendInstrument(choice, intent);
            }
        });

        floor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                choice = "Floor";

                sendInstrument(choice, intent);
            }
        });

        floorb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                choice = "Floor";

                sendInstrument(choice, intent);
            }
        });

        kick.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                choice = "Kick";

                sendInstrument(choice, intent);
            }
        });

        kickb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                choice = "Kick";

                sendInstrument(choice, intent);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent home = new Intent(TemplateInstrument.this, MainActivity.class);
                startActivity(home);
                finish();
            }
        });

    }

        public void sendInstrument(String choice, Intent intent){
            intent.putExtra("inst", choice);
            intent.putExtra("from", "template");
            startActivity(intent);

    }

}
