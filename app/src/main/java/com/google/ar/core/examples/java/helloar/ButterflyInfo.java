package com.google.ar.core.examples.java.helloar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ButterflyInfo extends AppCompatActivity {


    ImageView butterfly_image;
    TextView butterfly_name, butterfly_counter, butterfly_info;
    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_butterfly_info);

        ConstraintLayout constraintLayout = findViewById(R.id.butterflyInfo_layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        vibrator = (Vibrator)  ButterflyInfo.this.getSystemService(Context.VIBRATOR_SERVICE);

        Integer id;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                id = null;
            } else {
                id = extras.getInt("butterfly_id");
            }
        } else {
            id = (Integer) savedInstanceState.getSerializable("butterfly_id");
        }

        butterfly_image = (ImageView) findViewById(R.id.imageView3);
        butterfly_name = (TextView) findViewById(R.id.butterflyname);
        butterfly_counter = (TextView) findViewById(R.id.nrcaught);
        butterfly_info = (TextView) findViewById(R.id.info);

        switch (id) {
            case 1:
                butterfly_image.setImageResource(R.drawable.butterfly1);
                butterfly_name.setText(R.string.butterfly1_name);
                butterfly_counter.setText("Caught: " + MenuActivity.blueButterfly.getCount());
                butterfly_info.setText(R.string.butterfly1_info);
                break;
            case 2:
                butterfly_image.setImageResource(R.drawable.butterfly2);
                butterfly_name.setText(R.string.butterfly2_name);
                butterfly_counter.setText("Caught: " + MenuActivity.redButterfly.getCount());
                butterfly_info.setText(R.string.butterfly2_info);
                break;
            case 3:
                butterfly_image.setImageResource(R.drawable.butterfly3);
                butterfly_name.setText(R.string.butterfly3_name);
                butterfly_counter.setText("Caught: " + MenuActivity.whiteButterfly.getCount());
                butterfly_info.setText(R.string.butterfly3_info);
                break;

            case 4:
                butterfly_image.setImageResource(R.drawable.green);
                butterfly_name.setText(R.string.green_name);
                butterfly_counter.setText("Caught: " + MenuActivity.greenButterfly.getCount());
                butterfly_info.setText(R.string.green_info);
                break;
            default:
                break;
        }
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