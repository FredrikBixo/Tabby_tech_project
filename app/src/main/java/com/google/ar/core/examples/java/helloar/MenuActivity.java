package com.google.ar.core.examples.java.helloar;

import android.Manifest;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MenuActivity extends AppCompatActivity {
    private MediaPlayer ring;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu); //I know there is an issue here i might be able to fix with dependencies

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 12);

        //Knappar för onClick metod, startar activity
        button = (Button) findViewById(R.id.playButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               openPlay();
            }
        });


        /*button = (Button) findViewById(R.id.achButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAch();

        });

        button = (Button) findViewById(R.id.settings_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettings();
            }
        });


        });*/


        ring= MediaPlayer.create(MenuActivity.this,R.raw.bubble);
        ring.start();

    }



    //Metoder för att starta specifik activity

    public void openPlay() {
        ring.stop();
        Intent intent = new Intent(this, HelloArActivity.class);
        startActivity(intent);

    }

    public void openAch() {
        Intent intent = new Intent(this, Achievement_Activity.class);
        startActivity(intent);
    }

    public void openSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }


    public void openCollection(View v) {
        ring.stop();
        Intent intent = new Intent(this, CollectionActivity.class);
        startActivity(intent);

    }

    public void openCatch(View v) {
        ring.stop();
        Intent intent = new Intent(this, CatchActivity.class);
        startActivity(intent);

    }

}