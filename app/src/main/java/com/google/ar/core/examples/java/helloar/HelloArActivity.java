package com.google.ar.core.examples.java.helloar;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.concurrent.ThreadLocalRandom;

import pl.droidsonroids.gif.GifImageView;

public class HelloArActivity extends AppCompatActivity implements SensorEventListener {


  private Camera mCamera;
  private CameraPreview mPreview;
  private TextView pedometerText, prompt;
  private SensorManager sensorManager;
  private Sensor pedometer;
  private int initialStepCount;
  private int stepCount = 0;
  private boolean firstBoot;
  private GifImageView butterfly, circle;
  private Sensor rotationVectorSensor;
  private Vibrator vibrator;
  public static int choosebutterfly;
  public static MediaPlayer alert;
  public static boolean isHardModeON;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_ar);

    vibrator = (Vibrator)  HelloArActivity.this.getSystemService(Context.VIBRATOR_SERVICE);

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

    //Decide which butterfly
    choosebutterfly = ThreadLocalRandom.current().nextInt(4);


    butterfly = (GifImageView) findViewById(R.id.gifImageViewGame);

    if(choosebutterfly == 0){
      butterfly.setImageResource(R.drawable.butterfly_catch);
    }
    else if (choosebutterfly == 1){
      butterfly.setImageResource(R.drawable.peacock);
    }
    else if (choosebutterfly == 2){
      butterfly.setImageResource(R.drawable.whitebutterfly);
    }
    else{
      butterfly.setImageResource(R.drawable.greenbutterfly);
    }


    // create butterfly

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

    rotationVectorSensor =
            sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

    // move butterfly off screen
  /*  AnimatorSet butterflySpawn = new AnimatorSet();
    ObjectAnimator butterflyAnimation = ObjectAnimator.ofFloat(butterfly, "translationX", -500f);
    butterflyAnimation.setDuration(2000);
    ObjectAnimator circleAnimation = ObjectAnimator.ofFloat(circle, "translationX", -500f);
    circleAnimation.setDuration(2000);
    butterflySpawn.play(butterflyAnimation).with(circleAnimation);
    butterflySpawn.start();

*/
    prompt = findViewById(R.id.promptText);

    //alert sound
    alert = MediaPlayer.create(HelloArActivity.this, R.raw.attention);

    if(SettingsActivity.globalMute) {
      alert.setVolume(0,0);
    }else{
      alert.setVolume(0,1);
    }

  }

  @Override
  protected void onResume() {
    super.onResume();
    sensorManager.registerListener(this, pedometer, SensorManager.SENSOR_DELAY_FASTEST);
    sensorManager.registerListener(this,
            rotationVectorSensor, SensorManager.SENSOR_DELAY_NORMAL);

  }

  @Override
  protected void onPause() {
    super.onPause();
    sensorManager.unregisterListener(this);
  }

  @Override
  public void onSensorChanged(SensorEvent event) {

    if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {

      if (!firstBoot) {
        initialStepCount = (int) event.values[0];
        firstBoot = true;
      }
      stepCount = (int) event.values[0] - initialStepCount;

      if (stepCount > 2) {
        butterfly.setVisibility(View.VISIBLE);
        circle.setVisibility(View.VISIBLE);

        spawnButterfly();

        alert.start();
        //vibrator.vibrate(80);
        goingToVibrate();
        prompt.setText("There is a butterfly nearby! Find it!");

      }

    }


    if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {

      float[] rotationMatrix = new float[16];
      SensorManager.getRotationMatrixFromVector(
              rotationMatrix, event.values);

      float[] remappedRotationMatrix = new float[16];
      SensorManager.remapCoordinateSystem(rotationMatrix,
              SensorManager.AXIS_X,
              SensorManager.AXIS_Z,
              remappedRotationMatrix);

      // Convert to orientations
      float[] orientations = new float[3];
      SensorManager.getOrientation(remappedRotationMatrix, orientations);

      // Convert to degrees
      for (int i = 0; i < 3; i++) {
        orientations[i] = (float) (Math.toDegrees(orientations[i]));
      }

      float xRotation = orientations[0];
      float yRotation = orientations[1];

      DisplayMetrics displayMetrics = new DisplayMetrics();
      getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
      int width = displayMetrics.widthPixels;
      int height = displayMetrics.heightPixels;
      System.out.println("x:" + xRotation);
      System.out.println("y:" + yRotation);
      //  butterfly.animate().translationX(((360-degree)+40)*width/80).setDuration(200).start();

      if(isHardModeON) {
        //Hard mode
        butterfly.setX(((180 - xRotation*3) - 100) * width / 80);
        butterfly.setY(((180 - yRotation*3) - 100) * height / 80);

        circle.setX(((180 - xRotation*3) - 100) * width / 80);
        circle.setY(((180 - yRotation*3) - 100) * height / 80);
      }else{
      //Easy mode
      butterfly.setX(((180 - xRotation) - 100) * width / 80);
      butterfly.setY(((180 - yRotation) - 100) * height / 80);

      circle.setX(((180 - xRotation) - 100) * width / 80);
      circle.setY(((180 - yRotation) - 100) * height / 80);}
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

  //Back to menu
  public void openMenu(View v) {

    //Intent intent = new Intent(this, MenuActivity.class);
    //startActivity(intent);
    //vibrator.vibrate(8);
    goingToVibrate();

    if(SettingsActivity.musicMute == true){
      MenuActivity.ring.pause();

    }
    else{
      MenuActivity.ring.start();
    }
    finish();

  }

  public void goingToVibrate(){
    if(SettingsActivity.globalVibMute == true){


    }
    else{
      vibrator.vibrate(10);
    }
  }

}