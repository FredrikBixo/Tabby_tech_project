package com.google.ar.core.examples.java.helloar;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import pl.droidsonroids.gif.GifImageView;

public class HelloArActivity extends AppCompatActivity implements SensorEventListener {

  private Camera mCamera;
  private CameraPreview mPreview;
  private TextView pedometerText;
  private SensorManager sensorManager;
  private Sensor pedometer;
  private int initialStepCount;
  private int stepCount = 0;
  private boolean firstBoot;
  private GifImageView butterfly, circle;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_ar);

    // Create an instance of Camera
    mCamera = getCameraInstance();

    // Create our Preview view and set it as the content of our activity.
    mPreview = new CameraPreview(this, mCamera);
    FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
    preview.addView(mPreview);

    firstBoot = false;

    // Ask for permission to use sensors
    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 45);

    pedometerText = (TextView) findViewById(R.id.pedometers);

    sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

    pedometer = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

    // create butterfly
    butterfly = (GifImageView) findViewById(R.id.gifImageViewGame);
    circle = (GifImageView) findViewById(R.id.gifImageCircle);
    butterfly.setVisibility(View.INVISIBLE);
    circle.setVisibility(View.INVISIBLE);

    // set tap interaction on butterfly
    butterfly.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        openCatch();
      }
    });

    // move butterfly off screen
    AnimatorSet butterflySpawn = new AnimatorSet();
    ObjectAnimator butterflyAnimation = ObjectAnimator.ofFloat(butterfly, "translationX", -500f);
    butterflyAnimation.setDuration(2000);
    ObjectAnimator circleAnimation = ObjectAnimator.ofFloat(circle, "translationX", -500f);
    circleAnimation.setDuration(2000);
    butterflySpawn.play(butterflyAnimation).with(circleAnimation);
    butterflySpawn.start();

  }

  @Override
  protected void onResume() {
    super.onResume();
    sensorManager.registerListener(this, pedometer, SensorManager.SENSOR_DELAY_FASTEST);
    sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
            SensorManager.SENSOR_DELAY_GAME);
  }

  @Override
  protected void onPause() {
    super.onPause();
    sensorManager.unregisterListener(this);
  }

  @Override
  public void onSensorChanged(SensorEvent event) {
    //if(event.values[0] < 0) {
    //  stepCount += event.values[0] * (-1);
    //} else {

    if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {

      if (!firstBoot) {
        initialStepCount = (int) event.values[0];
        firstBoot = true;
      }
      stepCount = (int) event.values[0] - initialStepCount;
      //}

      pedometerText.setText("Steps: " + String.valueOf(stepCount));

      if (stepCount > 25) {
        butterfly.setVisibility(View.VISIBLE);
        circle.setVisibility(View.VISIBLE);

        spawnButterfly();
        despawnButterfly();


      }

    }


    if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
      float degree = Math.round(event.values[0]);

      DisplayMetrics displayMetrics = new DisplayMetrics();
      getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
      int width = displayMetrics.widthPixels;

      butterfly.setX(((360-degree)-40)*width/80);
      circle.setX(((360-degree)-40)*width/80);

      //butterfly.animate().translationX(((360-degree)+40)*width/80).setDuration(200).start();

      //butterfly.animate().translationX(+degree).setDuration(200).start();
      //butterfly.setX(degree);
    }

  }

  private void spawnButterfly() {
    AnimatorSet butterflyAnimation = new AnimatorSet();
    ObjectAnimator butterflySpawn = ObjectAnimator.ofFloat(butterfly, "translationX", 500f);
    ObjectAnimator butterflyAway = ObjectAnimator.ofFloat(butterfly, "translationX", -500f);
    butterflySpawn.setDuration(2000);

    butterflyAnimation.play(butterflySpawn).before(butterflyAway);
    butterflyAnimation.start();

  }

  private void despawnButterfly() {


  }

  @Override
  public void onAccuracyChanged(Sensor sensor, int i) {

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

  public void openCatch() {
    Intent intent = new Intent(this, CatchActivity.class);
    startActivity(intent);
    finish();
  }
}