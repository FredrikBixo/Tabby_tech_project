package com.google.ar.core.examples.java.helloar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import pl.droidsonroids.gif.GifImageView;

public class GameActivity extends AppCompatActivity {

    private Random rand;

    private Timer timer;
    private TimerTask alertEscapeTask;
    private TimerTask spawnButterflyTask;
    private final Handler handler = new Handler();

    private GifImageView butterfly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        rand = new Random();

        butterfly = (GifImageView) findViewById(R.id.gifImageViewGame);
    }

    @Override
    protected void onResume() {
        super.onResume();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        int X = (int) rand.nextDouble()*width;
        int Y = (int) rand.nextDouble()*height;

        int size = 40; // Test purposes
        butterfly.layout(X - size/2, Y - size/2, X + size/2, Y + size/2);
    }
}