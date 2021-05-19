package com.google.ar.core.examples.java.helloar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
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

        /*button = (Button) findViewById(R.id.achButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAch();
            }
        });*/

        MediaPlayer ring= MediaPlayer.create(MenuActivity.this,R.raw.bubble);
        ring.start();
    }



    //Metoder för att starta specifik activity

    public void openPlay() {
        Intent intent = new Intent(this, HelloArActivity.class);
        startActivity(intent);
    }

    public void openCollection(View v) {
        Intent intent = new Intent(this, CollectionActivity.class);
        startActivity(intent);
    }

    public void openCatch(View v) {
        Intent intent = new Intent(this, CatchActivity.class);
        startActivity(intent);
    }
}