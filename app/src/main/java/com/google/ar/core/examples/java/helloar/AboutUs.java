package com.google.ar.core.examples.java.helloar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;

public class AboutUs extends AppCompatActivity {
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        vibrator = (Vibrator) AboutUs.this.getSystemService(Context.VIBRATOR_SERVICE);

        ConstraintLayout constraintLayout = findViewById(R.id.aboutUs_layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(6000);
        animationDrawable.start();

    }

    //Return to previous activity
    public void back(View v) {
        goingToVibrate();
        finish();

    }

    public void goingToVibrate(){
        if(SettingsActivity.globalVibMute == true){


        }
        else{


            vibrator.vibrate(8);
        }
    }
}