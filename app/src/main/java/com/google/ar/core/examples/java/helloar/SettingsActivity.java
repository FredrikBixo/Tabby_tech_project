package com.google.ar.core.examples.java.helloar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {
    public Switch musicSwitch, soundSwitch, vibrationSwitch, hardModeSwitch;
    public Vibrator vibrator;
    public static boolean globalMute, musicMute, globalVibMute;



    boolean prevCheckMusic, prevCheckSound, prevCheckVib, prevCheckHard;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        vibrator = (Vibrator) SettingsActivity.this.getSystemService(Context.VIBRATOR_SERVICE);

        //Bind to respective switch
        soundSwitch = (Switch) findViewById(R.id.soundswitch);
        musicSwitch = (Switch) findViewById(R.id.musicswitch);
        vibrationSwitch = (Switch) findViewById(R.id.vibrationswitch);
        hardModeSwitch = (Switch) findViewById(R.id.hardmodeswitch);

        //Methods for if switches are on/off and saving their states

        //Sound Switch
        sharedPreferences = getSharedPreferences("SOUND_SWITCH_STATE", MODE_PRIVATE);
        soundSwitch.setChecked(sharedPreferences.getBoolean("SOUND_SWITCH_STATE", true));

        if(!MenuActivity.isNotFirstTime){
            soundSwitch.setChecked(true);
            MenuActivity.isNotFirstTime = true;

        }

        soundSwitch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(soundSwitch.isChecked()){


                    globalMute = false;
                    SharedPreferences.Editor editor = getSharedPreferences("SOUND_SWITCH_STATE", MODE_PRIVATE).edit();
                    editor.putBoolean("SOUND_SWITCH_STATE", true);
                    editor.commit();
                    //musicSwitch.setChecked(true);
                }
                else{

                    globalMute = true;
                    SharedPreferences.Editor editor = getSharedPreferences("SOUND_SWITCH_STATE", MODE_PRIVATE).edit();
                    editor.putBoolean("SOUND_SWITCH_STATE", false);
                    editor.commit();

                }
            }
        });

        //Music switch
        sharedPreferences = getSharedPreferences("MUSIC_SWITCH_STATE", MODE_PRIVATE);
        musicSwitch.setChecked(sharedPreferences.getBoolean("MUSIC_SWITCH_STATE", true));
        if(MenuActivity.ring.isPlaying()){
            musicSwitch.setChecked(true);
        }
        musicSwitch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(musicSwitch.isChecked()){
                    musicMute = false;
                    MenuActivity.ring.start();
                    SharedPreferences.Editor editor = getSharedPreferences("MUSIC_SWITCH_STATE", MODE_PRIVATE).edit();
                    editor.putBoolean("MUSIC_SWITCH_STATE", true);
                    editor.commit();
                    //musicSwitch.setChecked(true);
                }
                else{
                    musicMute = true;
                    MenuActivity.ring.pause();
                    SharedPreferences.Editor editor = getSharedPreferences("MUSIC_SWITCH_STATE", MODE_PRIVATE).edit();
                    editor.putBoolean("MUSIC_SWITCH_STATE", false);
                    editor.commit();
                    //musicSwitch.setChecked(false);
                }
            }
        });

        //Vibration switch
        sharedPreferences = getSharedPreferences("VIBRATION_SWITCH_STATE", MODE_PRIVATE);
        vibrationSwitch.setChecked(sharedPreferences.getBoolean("VIBRATION_SWITCH_STATE", true));

        if(!MenuActivity.vibIsNotFirstTime){
            vibrationSwitch.setChecked(true);
            MenuActivity.vibIsNotFirstTime = true;

        }

        vibrationSwitch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(vibrationSwitch.isChecked()){


                    globalVibMute = false;
                    SharedPreferences.Editor editor = getSharedPreferences("VIBRATION_SWITCH_STATE", MODE_PRIVATE).edit();
                    editor.putBoolean("VIBRATION_SWITCH_STATE", true);
                    editor.commit();
                    //musicSwitch.setChecked(true);
                }
                else{

                    globalVibMute = true;
                    SharedPreferences.Editor editor = getSharedPreferences("VIBRATION_SWITCH_STATE", MODE_PRIVATE).edit();
                    editor.putBoolean("VIBRATION_SWITCH_STATE", false);
                    editor.commit();

                }
            }
        });

        //HardMode switch
        sharedPreferences = getSharedPreferences("HARDMODE_SWITCH_STATE", MODE_PRIVATE);
        hardModeSwitch.setChecked(sharedPreferences.getBoolean("HARDMODE_SWITCH_STATE", false));

        hardModeSwitch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(hardModeSwitch.isChecked()){

                    HelloArActivity.isHardModeON = true;
                    SharedPreferences.Editor editor = getSharedPreferences("HARDMODE_SWITCH_STATE", MODE_PRIVATE).edit();
                    editor.putBoolean("HARDMODE_SWITCH_STATE", true);
                    editor.commit();

                }
                else{
                    HelloArActivity.isHardModeON = false;
                    SharedPreferences.Editor editor = getSharedPreferences("HARDMODE_SWITCH_STATE", MODE_PRIVATE).edit();
                    editor.putBoolean("HARDMODE_SWITCH_STATE", false);
                    editor.commit();

                }
            }
        });


    }


    //Back to menu
    public void openMenu(View v) {
        goingToVibrate();
        finish();

    }

    public void goingToVibrate(){
        if(SettingsActivity.globalVibMute == true){


        }
        else{
            vibrator.vibrate(8);
        }
    }


    //https://www.youtube.com/watch?v=RyiTx8lWdx0&t=202s

}