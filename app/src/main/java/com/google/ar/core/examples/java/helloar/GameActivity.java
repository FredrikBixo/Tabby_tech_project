package com.google.ar.core.examples.java.helloar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

import pl.droidsonroids.gif.GifImageView;

// Version of the game that doesn't disrupt the user's walks by leading them away.

public class GameActivity extends AppCompatActivity implements SensorEventListener {
    // Mean heading of the past 1000 samples is used to set butterfly location approximately in the
    // direction that the user is walking.
    long meanHeading;
    private Random rand;
    private Timer timer;
    private TimerTask alertEscapeTask;
    private TimerTask spawnButterflyTask;
    final private Handler handler = new Handler();

    private boolean alertIsShowing = false;

    //Accelerometer values, vissa ska slängas
    private TextView xTextView, yTextView, zTextView, directionText;
    private ConstraintLayout view;
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private boolean isAccelerometerAvailable, itIsNotFirstTime = false;
    private float currentX, currentY, currentZ, lastX, lastY, lastZ;
    private float xDifference, yDifference, zDifference;
    private float shakeThreshHold = 4f;
    private Vibrator vibrator;
    private String[] direction = {" ", " "};

    //Gyroscope values
    //private SensorManager sensorManager;
    private Sensor gyroscopeSensor;
    private SensorEventListener gyroscopeEventListener;
    private boolean correctAngle;

    private float heading = 0;
    private boolean butterflyIsInView = false;
    private final int sizeIncreasePerStep = 1;
    private int butterflySize = 0;
    private int catchThreshold = 30;
    private final float cameraAngle = 60f;
    private GifImageView butterfly;
    private int layoutWidth = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        butterfly = (GifImageView) findViewById(R.id.gifImageViewGame);
        layoutWidth = Resources.getSystem().getDisplayMetrics().widthPixels;

        rand = new Random();

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            isAccelerometerAvailable = true;
        } else {
            xTextView.setText("Accelerometer sensor is not available");
            isAccelerometerAvailable = false;
        }

        gyroscopeEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if(sensorEvent.values[0] < -0.5){
                    //getWindow().getDecorView().setBackgroundColor(Color.YELLOW);
                    correctAngle = true;

                }else{
                    //getWindow().getDecorView().setBackgroundColor(Color.BLUE);
                    correctAngle = false;
                }

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();

        //accelerometer stuff
        if(butterflyIsInView) {
            if (isAccelerometerAvailable)
                sensorManager.registerListener((SensorEventListener) this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
            sensorManager.registerListener(gyroscopeEventListener, gyroscopeSensor, SensorManager.SENSOR_DELAY_FASTEST);
        }
        //onResume we start our timer so it can start when the app comes from the background
        startTimer();
    }

    public void startTimer() {
        this.timer = new Timer();

        long delay = (long) ((15 + 30*rand.nextDouble())*1000);

        initializeSpawnTimerTask();

        timer.schedule(alertEscapeTask, delay);
    }

    public void initializeAlertTimerTask() {

        alertEscapeTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
                        builder.setTitle("Oh no...")
                                .setMessage("... the butterfly flew away.")
                                .setCancelable(false)
                                .setPositiveButton("Return to game", new DialogInterface.OnClickListener() {
                                    // This onclick method should reopen the AR-view, either by using
                                    // startActivity (as below) or by somehow going back from the current activity to
                                    // its parent-activity.
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(GameActivity.this, HelloArActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                        //Creating dialog box
                        AlertDialog dialog1 = builder.create();

                        if(!alertIsShowing){
                            if(!isFinishing()){
                                dialog1.show();
                                alertIsShowing = true;
                            }
                        }
                    }
                });
            }
        };
    }

    public void initializeSpawnTimerTask() {

        spawnButterflyTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        butterflyIsInView = true;

                        if(isAccelerometerAvailable)
                            sensorManager.registerListener((SensorEventListener) GameActivity.this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
                        sensorManager.registerListener(gyroscopeEventListener, gyroscopeSensor, SensorManager.SENSOR_DELAY_FASTEST);

                        updateButterfly();
                    }
                });
            }
        };
    }

    private void updateButterfly() {
        if (butterflyIsInView) {
            butterflySize += sizeIncreasePerStep;
            butterfly.setLayoutParams(new ViewGroup.LayoutParams(butterflySize, butterflySize));

        }
    }

//Accelerometer methods

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (butterflySize > catchThreshold) {
        /*xTextView.setText(Math.round(sensorEvent.values[0] * 100.0) / 100.0 + " m/s² ");
        yTextView.setText(Math.round(sensorEvent.values[1] * 100.0) / 100.0 + " m/s² ");
        zTextView.setText(Math.round(sensorEvent.values[2] * 100.0) / 100.0 + " m/s² ");*/

            currentX = sensorEvent.values[0];
            currentY = sensorEvent.values[1];
            currentZ = sensorEvent.values[2];

            if (itIsNotFirstTime) {
                xDifference = Math.abs(lastX - currentX);
                yDifference = Math.abs(lastY - currentY);
                zDifference = Math.abs(lastZ - currentZ);

                //SHAKE interaktion som kanske implementeras senare??

                if ((xDifference > shakeThreshHold && yDifference > shakeThreshHold) ||
                        (xDifference > shakeThreshHold && zDifference > shakeThreshHold) ||
                        (yDifference > shakeThreshHold && zDifference > shakeThreshHold)) {

                    int randomNum = ThreadLocalRandom.current().nextInt(0, 100 + 1);
                    MediaPlayer ring = MediaPlayer.create(GameActivity.this, R.raw.woosh);
                    ring.start();

                    if (correctAngle == true && randomNum <= 25) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
                            builder.setTitle("Yay!")
                                    .setMessage("You caught it!")
                                    .setCancelable(false)
                                    .setPositiveButton("Return to game", new DialogInterface.OnClickListener() {
                                        // This onclick method should reopen the AR-view, either by using
                                        // startActivity (as below) or by somehow going back from the current activity to
                                        // its parent-activity.
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(GameActivity.this, HelloArActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                            //Creating dialog box
                            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                            AlertDialog dialog1 = builder.create();
                            if (!alertIsShowing) {
                                if (!isFinishing()) {
                                    dialog1.show();
                                    alertIsShowing = true;
                                }
                            }
                        } else {
                            //vibrator.vibrate(500);
                            //deprecated in API 26 ??
                        }
                    } else if (randomNum > 25 && randomNum <= 50) {
                        //prompt.setText("Close!");
                    } else if (randomNum > 50 && randomNum <= 75) {
                        //prompt.setText("Try again!");
                    } else if (randomNum > 75) {
                        //prompt.setText("Maybe next swing!");
                    }
                }

                if (currentX < -0.5) {
                    direction[0] = "Right";
                } else if (currentX > 0.5) {
                    direction[0] = "Left";
                } else {
                    direction[0] = null;
                }

                // Gives direction depending on y-axis value
                if (currentY < -0.5) {
                    direction[1] = "Down";
                } else if (currentY > 0.5) {
                    direction[1] = "Up";
                } else {
                    direction[1] = null;
                }
            }
            lastX = currentX;
            lastY = currentY;
            lastZ = currentZ;

            itIsNotFirstTime = true;

            if (direction[0] != null || direction[1] != null) {

           /* if(direction[0] != null && direction[1] != null) {
                directionText.setText(direction[0] + " and " + direction[1]);
            } else if (direction[0] != null) {
                directionText.setText(direction[0]);
            } else if (direction[1] != null) {
                directionText.setText(direction[1]);
            }*/

                getWindow().getDecorView().setBackgroundColor(Color.parseColor("#86BBD8")); //  background to lightblue if  yellow
            } else {
                getWindow().getDecorView().setBackgroundColor(Color.parseColor("#A4C755")); //  background to Green
            }
        } else if (butterflyIsInView) {
            //position butterfly
            placeButterfly();
        }
    }

    private void placeButterfly() {
        int X = 0;
        int Y = 0;
        if (Math.abs(heading - meanHeading) < cameraAngle/2) {

        } else if (heading < 30f && meanHeading < (330f - heading)) {

        } else if (meanHeading < 30f && heading < (330f - meanHeading)) {

        }

        // placing at bottom right of touch
        butterfly.layout(X, Y, X+48, Y+48);

        //placing at center of touch
        int viewWidth = butterfly.getWidth();
        int viewHeight = butterfly.getHeight();
        viewWidth = viewWidth / 2;
        viewHeight = viewHeight / 2;

        butterfly.layout(X - butterflySize/2, Y - butterflySize/2, X + butterflySize/2, Y + butterflySize/2);
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(gyroscopeEventListener);
        if(isAccelerometerAvailable) {
            sensorManager.unregisterListener((SensorEventListener) this);
        }
    }

}