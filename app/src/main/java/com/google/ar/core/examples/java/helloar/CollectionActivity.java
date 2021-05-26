package com.google.ar.core.examples.java.helloar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

public class CollectionActivity extends AppCompatActivity {
    public ImageView blueImage;
    private Vibrator vibrator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        vibrator = (Vibrator)  CollectionActivity.this.getSystemService(Context.VIBRATOR_SERVICE);

        blueImage = findViewById(R.id.blue);

        if(MenuActivity.blue.getCount() == 1){

            blueImage.setImageResource(R.drawable.butterfly1);

        }
    }

    public void onClick1(View view) {
        if(MenuActivity.blue.getCount() == 1) {
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
    }
}