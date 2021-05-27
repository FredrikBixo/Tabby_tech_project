package com.google.ar.core.examples.java.helloar;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

public class MenuActivity extends AppCompatActivity {

    public static MediaPlayer ring;
    public static Vibrator vibrator;
    private Button button;

    public static boolean isNotFirstTime, vibIsNotFirstTime;
    //Butterflies
    public static Butterfly blueButterfly, redButterfly, whiteButterfly, greenButterfly;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu); //I know there is an issue here i might be able to fix with dependencies


        vibrator = (Vibrator) MenuActivity.this.getSystemService(Context.VIBRATOR_SERVICE);

        ConstraintLayout constraintLayout = findViewById(R.id.menu_layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 12);


        //Knappar för onClick metod, startar activity
        button = (Button) findViewById(R.id.playButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goingToVibrate();
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


                Button aboutUsB = (Button) findViewById(R.id.aboutus);
        aboutUsB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAboutUs();
            }
        });

        ring = MediaPlayer.create(MenuActivity.this,R.raw.bubble);
        ring.setLooping(true);
        ring.start();

        //Create butterflies here so as to not have to use save state
        blueButterfly = new Butterfly("blue");
        redButterfly = new Butterfly("red");
        whiteButterfly = new Butterfly("white");
        greenButterfly = new Butterfly("green");

    }



    //Metoder för att starta specifik activity

    public void openPlay() {
        goingToVibrate();
        ring.pause();
        Intent intent = new Intent(this, HelloArActivity.class);
        startActivity(intent);

    }

    public void openAboutUs() {
        Intent intent = new Intent(this, AboutUs.class);
        startActivity(intent);

    }



    /*public void openAch() {
        Intent intent = new Intent(this, Achievement_Activity.class);
        startActivity(intent);
    }*/

    public void openSettings(View view) {
        goingToVibrate();
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivityForResult(intent, 1234);
    }


    public void openCollection(View v) {
        //ring.stop();
        goingToVibrate();
        Intent intent = new Intent(this, CollectionActivity.class);
        startActivity(intent);


    }

    public void openCatch(View v) {
        ring.stop();
        Intent intent = new Intent(this, CatchActivity.class);
        startActivity(intent);

    }

    public void goingToVibrate(){
        if(SettingsActivity.globalVibMute == true){


        }
        else{
            vibrator.vibrate(8);
        }
    }

}