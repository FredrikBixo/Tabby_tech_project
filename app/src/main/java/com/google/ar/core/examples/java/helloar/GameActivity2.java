package com.google.ar.core.examples.java.helloar;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import pl.droidsonroids.gif.GifImageView;

public class GameActivity2 extends AppCompatActivity {

    private Random rand;

    private Timer timer;
    private TimerTask alertEscapeTask;
    private TimerTask spawnButterflyTask;
    private final Handler handler = new Handler();

    int displayWidth = 0;
    int displayHeight = 0;
    private GifImageView butterfly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayHeight = displayMetrics.heightPixels;
        int displayWidth = displayMetrics.widthPixels;

        rand = new Random();

        butterfly = (GifImageView) findViewById(R.id.gifImageViewGame);
    }

    @Override
    protected void onResume() {
        super.onResume();

        /*int X = (int) rand.nextDouble()*displayWidth;
        int Y = (int) rand.nextDouble()*displayHeight;

        int size = 40; // Test purposes
        butterfly.layout(X - size/2, Y - size/2, X + size/2, Y + size/2);*/

        ObjectAnimator animation = ObjectAnimator.ofFloat(butterfly, "translationX",-400f);
        animation.setDuration(2000);
        animation.start();
    }

    /*private void checkMetrics{
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayHeight = displayMetrics.heightPixels;
        int displayWidth = displayMetrics.widthPixels;

        Display display = getWindowManager().getDefaultDisplay();
        String displayName = display.getName();  // minSdkVersion=17+
        Log.i(TAG, "displayName  = " + displayName);

        // display size in pixels
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        Log.i(TAG, "width        = " + width);
        Log.i(TAG, "height       = " + height);

        // pixels, dpi
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int heightPixels = metrics.heightPixels;
        int widthPixels = metrics.widthPixels;
        int densityDpi = metrics.densityDpi;
        float xdpi = metrics.xdpi;
        float ydpi = metrics.ydpi;
        Log.i(TAG, "widthPixels  = " + widthPixels);
        Log.i(TAG, "heightPixels = " + heightPixels);
        Log.i(TAG, "densityDpi   = " + densityDpi);
        Log.i(TAG, "xdpi         = " + xdpi);
        Log.i(TAG, "ydpi         = " + ydpi);

        // deprecated
        int screenHeight = display.getHeight();
        int screenWidth = display.getWidth();
        Log.i(TAG, "screenHeight = " + screenHeight);
        Log.i(TAG, "screenWidth  = " + screenWidth);

        // orientation (either ORIENTATION_LANDSCAPE, ORIENTATION_PORTRAIT)
        int orientation = getResources().getConfiguration().orientation;
        Log.i(TAG, "orientation  = " + orientation);
    }*/
}
