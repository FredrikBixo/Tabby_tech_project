package com.google.ar.core.examples.java.helloar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu); //I know there is an issue here i might be able to fix with dependencies

        //Knappar för onClick metod, startar activity
        button = (Button) findViewById(R.id.playButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPlay();
            }
        });

        button = (Button) findViewById(R.id.achButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAch();
            }
        });

    }

    //Metoder för att starta specifik activity

    public void openPlay() {
        Intent intent = new Intent(this, HelloArActivity.class);
        startActivity(intent);
    }

    public void openAch() {
        Intent intent = new Intent(this, Achievement_Activity.class);
        startActivity(intent);
    }

}