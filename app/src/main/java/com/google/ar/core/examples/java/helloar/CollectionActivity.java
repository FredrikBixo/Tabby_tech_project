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
    public ImageView blueImage;
    private Vibrator vibrator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        vibrator = (Vibrator)  CollectionActivity.this.getSystemService(Context.VIBRATOR_SERVICE);

        blueImage = findViewById(R.id.blue);

        if(MenuActivity.blue.getCount() == 0) {
            blueImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickLock(view);
                }
            });
        }

        if(MenuActivity.blue.getCount() >= 1){

            blueImage.setImageResource(R.drawable.butterfly1);
            blueImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClick1(view);
                }
            });

        }
    }

    public void onClick1(View view) {
        if(MenuActivity.blue.getCount() >= 1) {
            vibrator.vibrate(8);
        }
        Intent intent = new Intent(this, ButterflyInfo.class);
        intent.putExtra("butterfly_id",1);
        startActivity(intent);
    }

    public void onClick2(View view) {
        vibrator.vibrate(10);
        Intent intent = new Intent(this, ButterflyInfo.class);
        intent.putExtra("butterfly_id",2);
        startActivity(intent);
    }

    public void onClick3(View view) {
        vibrator.vibrate(10);
        Intent intent = new Intent(this, ButterflyInfo.class);
        intent.putExtra("butterfly_id",3);
        startActivity(intent);
    }

    public void openMenu(View v) {

        //Intent intent = new Intent(this, MenuActivity.class);
        //startActivity(intent);
        vibrator.vibrate(10);
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
}