package com.example.briengillen.fingerprinting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by briengillen on 09/09/2017.
 */

public class TemplateChoice extends AppCompatActivity{

    // Buttons create
    Button create, old, home;

    // Intents created for the purpose receiving data into the context
    // and sending it out
    Intent intent, i;

    // String value to store instrument name
    String instrument, key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template_choice);

        // Asigning button values to ids
        create = (Button) findViewById(R.id.record);
        old = (Button) findViewById(R.id.temp);
        home = (Button) findViewById(R.id.tempChoiceH);

        // Setting i to the intent used to access this activity
        i = getIntent();

        // Bundle used to store extras sent from previous page
        Bundle bundle = i.getExtras();

        // Setting instrument to be drum name form template instrument
        instrument = bundle.getString("inst");

        key = bundle.getString("");



        create.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                intent = new Intent(TemplateChoice.this, SnareActivity.class);
                intent.putExtra("inst", instrument);
                startActivity(intent);
            }

        });

        old.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                intent = new Intent(TemplateChoice.this, LoadTemplate.class);
                intent.putExtra("inst", instrument);
                startActivity(intent);
            }

        });

        home.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent home = new Intent(TemplateChoice.this, MainActivity.class);
                startActivity(home);
            }
        });
    }
}
