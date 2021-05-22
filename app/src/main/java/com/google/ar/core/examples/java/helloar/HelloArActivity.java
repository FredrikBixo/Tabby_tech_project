package com.google.ar.core.examples.java.helloar;

import android.Manifest;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class HelloArActivity extends AppCompatActivity implements SensorEventListener {

  private Camera mCamera;
  private CameraPreview mPreview;
  private TextView pedometerText;
  private SensorManager sensorManager;
  private Sensor pedometer;
  private int initialStepCount;
  private int stepCount = 0;
  private boolean firstBoot;

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

  }

  @Override
  protected void onResume() {
    super.onResume();
    sensorManager.registerListener(this, pedometer, SensorManager.SENSOR_DELAY_FASTEST);
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
      if(!firstBoot) {
        initialStepCount = (int) event.values[0];
        firstBoot = true;
      }
      stepCount = (int) event.values[0] - initialStepCount;
    //}

    pedometerText.setText("Steps: " + String.valueOf(stepCount));
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
}