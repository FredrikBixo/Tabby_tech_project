package com.google.ar.core.examples.java.helloar;

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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

import pl.droidsonroids.gif.GifImageView;

// Version of the game that doesn't disrupt the user's walks by leading them away.

public class GameActivity extends AppCompatActivity implements SensorEventListener {
    // Mean heading of the past 1000 samples is used to set butterfly location approximately in the
    // direction that the user is walking.
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

    private Sensor magneticFieldSensor;

    //Gyroscope values
    //private SensorManager sensorManager;
    private Sensor gyroscopeSensor;
    private SensorEventListener gyroscopeEventListener;
    private boolean correctAngle;

    private float heading = 0;
    float meanHeading = 0;
    private float[] headings = {};
    private final int sampleNo = 10000; //How many samples to save
    private int headingsIndex = 0;

    private boolean butterflyIsInView = false;
    private final int sizeIncreasePerStep = 1;
    private int butterflySize = 0;
    private int catchThreshold = 30;
    private final float cameraAngle = 60f;
    private GifImageView butterfly;
    private GifImageView circle;

    // ALPHA is the lowpass-filter threshold constant. If ALPHA = 1, no filter applies.
    private static final float ALPHA = 1f;

    private float[] magSensorVals = {0, 0, 0};

    private float[] accSensorVals = {0, 0, 0};

    private float[] rotationMatrix = {0, 0, 0, 0, 0, 0, 0, 0, 0};

    private float[] orientationAngles = {0, 0, 0};

    //private int layoutWidth = 0;

    // TODO: Scemalägg ny spawn efter att fjäril fångats.
    // TODO: Schemalägg en alertEscapeTask när butterflySize > catchThreshold.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        butterfly = (GifImageView) findViewById(R.id.gifImageViewGame);
        circle = (GifImageView) findViewById(R.id.gifImageCircle);
        //layoutWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        butterfly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCatch();
            }
        });

        rand = new Random();

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        

        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {

            isAccelerometerAvailable = true;
        } else {
            xTextView.setText("Accelerometer sensor is not available");
            isAccelerometerAvailable = false;
        }

        gyroscopeEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (sensorEvent.values[0] < -0.5) {
                    //getWindow().getDecorView().setBackgroundColor(Color.YELLOW);
                    correctAngle = true;

                } else {
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
        if (butterflyIsInView) {
            if (isAccelerometerAvailable)
                sensorManager.registerListener((SensorEventListener) this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
            sensorManager.registerListener(gyroscopeEventListener, gyroscopeSensor, SensorManager.SENSOR_DELAY_FASTEST);

            magneticFieldSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
            if (magneticFieldSensor != null) {
                sensorManager.registerListener(this, magneticFieldSensor,
                        SensorManager.SENSOR_DELAY_UI);
            }
        }
        //onResume we start our timer so it can start when the app comes from the background
        startTimer();
    }

    public void startTimer() {
        this.timer = new Timer();

        long delay = (long) ((15 + 30 * rand.nextDouble()) * 1000);

        initializeSpawnTimerTask();

        timer.schedule(spawnButterflyTask, delay);
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

                        if (!alertIsShowing) {
                            if (!isFinishing()) {
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

                        if (isAccelerometerAvailable)
                            sensorManager.registerListener((SensorEventListener) GameActivity.this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
                        sensorManager.registerListener(gyroscopeEventListener, gyroscopeSensor, SensorManager.SENSOR_DELAY_FASTEST);

                        butterflySize = 10; //Test
                        updateButterfly();
                    }
                });
            }
        };
    }

    private void updateButterfly() {
        if (butterflyIsInView) {
            butterflySize += sizeIncreasePerStep;

            placeButterfly();
        }
    }

//Accelerometer methods

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        currentX = sensorEvent.values[0];
        currentY = sensorEvent.values[1];
        currentZ = sensorEvent.values[2];

        butterfly.setRotation(currentY);
        circle.setRotation(currentY);

        if (butterflySize > catchThreshold) {
        /*xTextView.setText(Math.round(sensorEvent.values[0] * 100.0) / 100.0 + " m/s² ");
        yTextView.setText(Math.round(sensorEvent.values[1] * 100.0) / 100.0 + " m/s² ");
        zTextView.setText(Math.round(sensorEvent.values[2] * 100.0) / 100.0 + " m/s² ");*/





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
            // Update heading
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                System.arraycopy(sensorEvent.values, 0, accSensorVals,
                        0, accSensorVals.length);
            } else if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                System.arraycopy(sensorEvent.values, 0, magSensorVals,
                        0, magSensorVals.length);
            }
            updateOrientationAngles();
            headings[headingsIndex] = heading;
            if (headingsIndex < sampleNo) {
                headingsIndex++;
            } else {
                headingsIndex = 0;
            }
            meanHeading = 0;
            for (float h : headings) {
                meanHeading += h;
            }
            meanHeading = meanHeading / headings.length;

            placeButterfly();
        }
    }

    protected void updateOrientationAngles() {
        SensorManager.getRotationMatrix(rotationMatrix, null, accSensorVals, magSensorVals);
        float[] orientation = SensorManager.getOrientation(rotationMatrix, orientationAngles);
        double degrees = (Math.toDegrees(orientation[0] + 360.0)) % 360.0;
        heading = (float) (Math.round(degrees * 10000) / 10000.0);
    }

    protected float[] lowPass(float[] input, float[] output) {
        if (output == null) return input;
        for (int i = 0; i < input.length; i++) {
            output[i] = output[i] + ALPHA * (input[i] - output[i]);
        }
        return output;
    }

    private void placeButterfly() {
        int X = 0;
        //int Y = 0;
        int Y = catchThreshold;
        if (Math.abs(heading - meanHeading) < cameraAngle / 2) {
            X = (int) (view.getWidth() * (1 / 2 + (meanHeading - heading) / cameraAngle));
        } else if ((heading < cameraAngle / 2 && meanHeading > (360f - cameraAngle / 2 - heading)) ||
                (meanHeading < cameraAngle / 2 && heading > (360f - cameraAngle / 2 - meanHeading))) {
            X = (int) (view.getWidth() * (1 / 2 + (meanHeading - heading - 360f) / cameraAngle));
        }

        butterfly.animate().translationX(X).translationY(Y).start();
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(gyroscopeEventListener);
        if (isAccelerometerAvailable) {
            sensorManager.unregisterListener((SensorEventListener) this);
        }
    }

    public void openCatch() {
        Intent intent = new Intent(this, CatchActivity.class);
        startActivity(intent);
    }

}