package com.google.ar.core.examples.java.helloar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

// Timer functionality based on https://examples.javacodegeeks.com/android/core/activity/android-timertask-example/#:~:text=%20Android%20TimerTask%20Example%20%201%20Create%20a,Open%20src%2Fcom.javacodegeeks.%204%20Android%20Manifest.%20%20More%20

// Activity for catching butterflies
// Has the beginnings of a timer that triggers a dialog that informs the user that the butterfly has
// escaped and is supposed to return to AR-view
public class CatchActivity extends AppCompatActivity {
    private Random rand;
    private Timer timer;
    private TimerTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catch);

        rand = new Random();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //onResume we start our timer so it can start when the app comes from the background
        startTimer();
    }

    public void startTimer() {
        this.timer = new Timer();

        long delay = (long) (4 + 6*rand.nextDouble());

        initializeTimerTask();

        timer.schedule(task, delay);
    }

    public void initializeTimerTask() {

        task = new TimerTask() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(CatchActivity.this);
                builder.setTitle("Oh no...")
                        .setMessage("... the butterfly flew away.")
                        .setCancelable(false)
                        .setPositiveButton("Return to game", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(CatchActivity, HelloArActivity.class);
                                startActivity(intent);
                            }
                        });
                //Creating dialog box
                AlertDialog dialog  = builder.create();
                dialog.show();

            }
        };
    }
}