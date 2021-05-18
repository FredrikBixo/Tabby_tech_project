package com.google.ar.core.examples.java.helloar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

// Timer functionality based on https://examples.javacodegeeks.com/android/core/activity/android-timertask-example/#:~:text=%20Android%20TimerTask%20Example%20%201%20Create%20a,Open%20src%2Fcom.javacodegeeks.%204%20Android%20Manifest.%20%20More%20

// Activity for catching butterflies
// Has the beginnings of a timer that should trigger a dialog after 4-10 seconds that informs the
// user that the butterfly has escaped and is supposed to return to AR-view after the dialog is
// dismissed.
public class CatchActivity extends AppCompatActivity implements SensorEventListener {
    private Random rand;
    private Timer timer;
    private TimerTask task;
    final private Handler handler = new Handler();

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
    private TextToSpeech mTTS;
    private boolean alertIsShowing = false;
    private TextView prompt;

    //Gyroscope values
    //private SensorManager sensorManager;
    private Sensor gyroscopeSensor;
    private SensorEventListener gyroscopeEventListener;
    private boolean correctAngle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catch);

        rand = new Random();

        prompt = findViewById(R.id.promptText);
        //Accelerometer stuff
       /*xTextView = findViewById(R.id.X);
        yTextView = findViewById(R.id.Y);
        zTextView = findViewById(R.id.Z);
        directionText = findViewById(R.id.directionText);
        view = (ConstraintLayout) findViewById(R.id.acc_sensor);*/


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

        //Gyroscope
        if(gyroscopeSensor == null){
            Toast.makeText(this, "The device has no Gyroscope!", Toast.LENGTH_LONG).show();
            finish();
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
        if(isAccelerometerAvailable)
            sensorManager.registerListener((SensorEventListener) this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(gyroscopeEventListener, gyroscopeSensor, SensorManager.SENSOR_DELAY_FASTEST);
        //onResume we start our timer so it can start when the app comes from the background
        //startTimer();



    }

    public void startTimer() {
        this.timer = new Timer();

        long delay = (long) ((4 + 6*rand.nextDouble())*1000);

        initializeTimerTask();

        timer.schedule(task, delay);
    }

    public void initializeTimerTask() {

        task = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(CatchActivity.this);
                        builder.setTitle("Oh no...")
                                .setMessage("... the butterfly flew away.")
                                .setCancelable(false)
                                .setPositiveButton("Return to game", new DialogInterface.OnClickListener() {
                                    // This onclick method should reopen the AR-view, either by using
                                    // startActivity (as below) or by somehow going back from the current activity to
                                    // its parent-activity.
                                   @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(CatchActivity.this, HelloArActivity.class);
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

    //Accelerometer methods

   @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        /*xTextView.setText(Math.round(sensorEvent.values[0] * 100.0) / 100.0 + " m/s² ");
        yTextView.setText(Math.round(sensorEvent.values[1] * 100.0) / 100.0 + " m/s² ");
        zTextView.setText(Math.round(sensorEvent.values[2] * 100.0) / 100.0 + " m/s² ");*/

        currentX = sensorEvent.values[0];
        currentY = sensorEvent.values[1];
        currentZ = sensorEvent.values[2];

        if(itIsNotFirstTime){
            xDifference = Math.abs(lastX - currentX);
            yDifference = Math.abs(lastY - currentY);
            zDifference = Math.abs(lastZ - currentZ);

            //SHAKE interaktion som kanske implementeras senare??

            if((xDifference > shakeThreshHold && yDifference > shakeThreshHold )||
                    (xDifference > shakeThreshHold && zDifference > shakeThreshHold) ||
                    (yDifference > shakeThreshHold && zDifference > shakeThreshHold)){

                int randomNum = ThreadLocalRandom.current().nextInt(0, 100 + 1);
                MediaPlayer ring= MediaPlayer.create(CatchActivity.this,R.raw.woosh);
                ring.start();

                if(correctAngle == true && randomNum <= 25){

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(CatchActivity.this);
                    builder.setTitle("Yay!")
                            .setMessage("You caught it!")
                            .setCancelable(false)
                            .setPositiveButton("Return to game", new DialogInterface.OnClickListener() {
                                // This onclick method should reopen the AR-view, either by using
                                // startActivity (as below) or by somehow going back from the current activity to
                                // its parent-activity.
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(CatchActivity.this, HelloArActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                    //Creating dialog box
                    vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    AlertDialog dialog1 = builder.create();
                    if(!alertIsShowing){
                        if(!isFinishing()){
                            dialog1.show();
                            alertIsShowing = true;
                        }
                    }


                }



                else{
                    //vibrator.vibrate(500);
                    //deprecated in API 26 ??
                }

            }

                else if (randomNum > 25 && randomNum <= 50){
                    prompt.setText("Close!");
                }

                else if (randomNum > 50 && randomNum <= 75){
                    prompt.setText("Try again!");
                }

                else if (randomNum > 75){
                    prompt.setText("Maybe next swing!");
                }

            }

            if(currentX < -0.5) {
                direction[0] = "Right";
            } else if (currentX > 0.5) {
                direction[0] = "Left";
            } else {
                direction[0] = null;
            }

            // Gives direction depending on y-axis value
            if(currentY < -0.5) {
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


        if(direction[0] != null || direction[1] != null) {

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




    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }



    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(gyroscopeEventListener);
        if(isAccelerometerAvailable)
            sensorManager.unregisterListener((SensorEventListener) this);

    }

}