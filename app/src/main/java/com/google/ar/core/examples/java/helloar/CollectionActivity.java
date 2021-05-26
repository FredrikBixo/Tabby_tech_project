package com.google.ar.core.examples.java.helloar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class CollectionActivity extends AppCompatActivity {
    public ImageView blueImage, redImage, whiteImage, greenImage;
    private Vibrator vibrator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        vibrator = (Vibrator)  CollectionActivity.this.getSystemService(Context.VIBRATOR_SERVICE);

        blueImage = findViewById(R.id.blue);
        redImage = findViewById(R.id.red);
        whiteImage = findViewById(R.id.white);
        greenImage = findViewById(R.id.green);

        if(MenuActivity.blueButterfly.getCount() == 0) {
            blueImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickLock(view);
                }
            });
        }

        if(MenuActivity.blueButterfly.getCount() >= 1){

            blueImage.setImageResource(R.drawable.butterfly1);
            blueImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClick1(view);
                }
            });

        }

        //Red
        if(MenuActivity.redButterfly.getCount() == 0) {
            redImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickLock(view);
                }
            });
        }

        if(MenuActivity.redButterfly.getCount() >= 1){

            redImage.setImageResource(R.drawable.butterfly2);
            redImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClick2(view);
                }
            });

        }

        //White
        if(MenuActivity.whiteButterfly.getCount() == 0) {
            whiteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickLock(view);
                }
            });
        }

        if(MenuActivity.whiteButterfly.getCount() >= 1){

            whiteImage.setImageResource(R.drawable.butterfly3);
            whiteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClick3(view);
                }
            });

        }

        //Green
        if(MenuActivity.greenButterfly.getCount() == 0) {
            greenImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickLock(view);
                }
            });
        }

        if(MenuActivity.greenButterfly.getCount() >= 1){

            greenImage.setImageResource(R.drawable.green);
            greenImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClick4(view);
                }
            });

        }
    }

    public void onClick1(View view) {
        if(MenuActivity.blueButterfly.getCount() >= 1) {
            goingToVibrate();
        }
        Intent intent = new Intent(this, ButterflyInfo.class);
        intent.putExtra("butterfly_id",1);
        startActivity(intent);
    }

    public void onClick2(View view) {
        if(MenuActivity.redButterfly.getCount() >= 1) {
            goingToVibrate();
        }

        Intent intent = new Intent(this, ButterflyInfo.class);
        intent.putExtra("butterfly_id",2);
        startActivity(intent);
    }

    public void onClick3(View view) {
        if(MenuActivity.whiteButterfly.getCount() >= 1) {
            goingToVibrate();
        }
        Intent intent = new Intent(this, ButterflyInfo.class);
        intent.putExtra("butterfly_id",3);
        startActivity(intent);
    }

    public void onClick4(View view) {
        if(MenuActivity.greenButterfly.getCount() >= 1) {
            goingToVibrate();
        }
        Intent intent = new Intent(this, ButterflyInfo.class);
        intent.putExtra("butterfly_id",4);
        startActivity(intent);
    }

    public void openMenu(View v) {

        //Intent intent = new Intent(this, MenuActivity.class);
        //startActivity(intent);
        goingToVibrate();
        finish();

    }

    public void openPlay(View view) {
        goingToVibrate();
        Intent intent = new Intent(this, HelloArActivity.class);
        startActivity(intent);
        MenuActivity.ring.pause();
        finish();
    }

    public void onClickLock(View view) {
        //Display an alert about a DLC or something.

        new AlertDialog.Builder(CollectionActivity.this)
                .setTitle("This butterfly is locked")
                .setMessage("Catch this butterfly to unlock it!")
                .setCancelable(false)
                .setPositiveButton("Return", new DialogInterface.OnClickListener() {
                    // This onclick method should reopen the AR-view, either by using
                    // startActivity (as below) or by somehow going back from the current activity to
                    // its parent-activity.
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();

    }

    public void goingToVibrate(){
        if(SettingsActivity.globalVibMute == true){


        }
        else{
            vibrator.vibrate(8);
        }
    }
}