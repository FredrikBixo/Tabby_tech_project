package com.google.ar.core.examples.java.helloar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Camera;
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
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

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

    //Camera stuff
    private Camera mCamera;
    private CameraPreview mPreview;


    //Mediaplayer
    private MediaPlayer ring, success, fail;

    //Butterfly
    ImageView blueImage;

    //Dialog builder
    private AlertDialog.Builder builderSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catch);

        //Butterflies
        blueImage = (ImageView) findViewById(R.id.blue);

        // Create an instance of Camera
        mCamera = getCameraInstance();

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_catch_preview);
        preview.addView(mPreview);

        rand = new Random();

        prompt = (TextView) findViewById(R.id.promptText);
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
        //Get  sounds
        ring = MediaPlayer.create(CatchActivity.this, R.raw.woosh);
        success = MediaPlayer.create(CatchActivity.this, R.raw.success);
        fail = MediaPlayer.create(CatchActivity.this, R.raw.fail);



    }

    @Override
    protected void onResume() {
        super.onResume();

        //accelerometer stuff
        if(isAccelerometerAvailable)
            sensorManager.registerListener((SensorEventListener) this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(gyroscopeEventListener, gyroscopeSensor, SensorManager.SENSOR_DELAY_FASTEST);
        //onResume we start our timer so it can start when the app comes from the background
        startTimer();



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

                        AlertDialog.Builder builderOOT = new AlertDialog.Builder(CatchActivity.this);
                        builderOOT.setTitle("Oh no...")
                                .setMessage("... the butterfly flew away.")
                                .setCancelable(false)
                                .setPositiveButton("Try again", new DialogInterface.OnClickListener() {
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
                        AlertDialog dialog1 = builderOOT.create();

                        if(!alertIsShowing){
                            if(!isFinishing()){

                                dialog1.show();
                                alertIsShowing = true;

                                if(!fail.isPlaying()){
                                    fail.start();
                                }

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
        currentX = sensorEvent.values[0];
        currentY = sensorEvent.values[1];
        currentZ = sensorEvent.values[2];


        if(itIsNotFirstTime) {
            xDifference = Math.abs(lastX - currentX);
            yDifference = Math.abs(lastY - currentY);
            zDifference = Math.abs(lastZ - currentZ);

            //SHAKE interaktion som kanske implementeras senare??

            if(!alertIsShowing){

            if ((xDifference > shakeThreshHold && yDifference > shakeThreshHold) ||/*
                    (xDifference > shakeThreshHold && zDifference > shakeThreshHold) ||*/
                    (yDifference > shakeThreshHold && zDifference > shakeThreshHold)) {

                int randomNum = ThreadLocalRandom.current().nextInt(0, 100 + 1);


                    if (!ring.isPlaying()) {
                        ring.start();
                    }


                if (correctAngle == true && randomNum <= 25 && lastY > currentY) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                        builderSuccess = new AlertDialog.Builder(CatchActivity.this);

                        builderSuccess.setTitle("Yay!")
                                .setMessage("You caught it!")
                                .setCancelable(false)
                                .setPositiveButton("Check it out", new DialogInterface.OnClickListener() {
                                    // This onclick method should reopen the AR-view, either by using
                                    // startActivity (as below) or by somehow going back from the current activity to
                                    // its parent-activity.
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(CatchActivity.this, CollectionActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                        //Set blue butterfly caught
                        MenuActivity.blue.setCaught();
                        MenuActivity.blue.count();


                        //Creating dialog box
                        prompt.setText("Yay!");
                        AlertDialog dialog1 = builderSuccess.create();

                        //if (!alertIsShowing) {
                            if (!isFinishing()) {
                                vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));

                                dialog1.show();
                                alertIsShowing = true;

                                if (!success.isPlaying()) {
                                    success.start();
                                }
                            }
                       // }


                    } else {
                        //vibrator.vibrate(500);
                        //deprecated in API 26 ??
                    }

                } else if (randomNum > 25 && randomNum <= 50) {
                    prompt.setText("Close!");
                } else if (randomNum > 50 && randomNum <= 75) {
                    prompt.setText("Try again!");
                } else if (randomNum > 75) {
                    prompt.setText("Maybe next swing!");
                }

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

    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    //Back to menu
    public void openMenu(View v) {
        
        finish();

    }

}