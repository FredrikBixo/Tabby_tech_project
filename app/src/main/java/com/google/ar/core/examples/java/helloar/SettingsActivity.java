package com.google.ar.core.examples.java.helloar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {
    public Switch musicSwitch, soundSwitch, vibrationSwitch, hardModeSwitch;
    public Vibrator vibrator;


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
        musicSwitch.setChecked(sharedPreferences.getBoolean("SOUND_SWITCH_STATE", true));
        if(MenuActivity.ring.isPlaying()){
            soundSwitch.setChecked(true);
            musicSwitch.setChecked(true);

        }

        else{
            musicSwitch.setChecked(false);
        }
        musicSwitch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(musicSwitch.isChecked()){
                    unmute();
                    SharedPreferences.Editor editor = getSharedPreferences("SOUND_SWITCH_STATE", MODE_PRIVATE).edit();
                    editor.putBoolean("SOUND_SWITCH_STATE", true);
                    editor.commit();
                    //musicSwitch.setChecked(true);
                }
                else{
                    mute();
                    MenuActivity.ring.pause();
                    SharedPreferences.Editor editor = getSharedPreferences("SOUND_SWITCH_STATE", MODE_PRIVATE).edit();
                    editor.putBoolean("SOUND_SWITCH_STATE", false);
                    editor.commit();
                    //musicSwitch.setChecked(false);
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
                    MenuActivity.ring.start();
                    SharedPreferences.Editor editor = getSharedPreferences("MUSIC_SWITCH_STATE", MODE_PRIVATE).edit();
                    editor.putBoolean("MUSIC_SWITCH_STATE", true);
                    editor.commit();
                    //musicSwitch.setChecked(true);
                }
                else{
                    MenuActivity.ring.pause();
                    SharedPreferences.Editor editor = getSharedPreferences("MUSIC_SWITCH_STATE", MODE_PRIVATE).edit();
                    editor.putBoolean("MUSIC_SWITCH_STATE", false);
                    editor.commit();
                    //musicSwitch.setChecked(false);
                }
            }
        });

        //HardMode switch
        sharedPreferences = getSharedPreferences("HARDMODE_SWITCH_STATE", MODE_PRIVATE);
        hardModeSwitch.setChecked(sharedPreferences.getBoolean("HARDMODE_SWITCH_STATE", true));
        if(MenuActivity.ring.isPlaying()){
            musicSwitch.setChecked(true);
        }
        musicSwitch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(musicSwitch.isChecked()){
                    SharedPreferences.Editor editor = getSharedPreferences("HARDMODE_SWITCH_STATE", MODE_PRIVATE).edit();
                    editor.putBoolean("HARDMODE_SWITCH_STATE", true);
                    editor.commit();

                }
                else{
                    SharedPreferences.Editor editor = getSharedPreferences("HARDMODE_SWITCH_STATE", MODE_PRIVATE).edit();
                    editor.putBoolean("HARDMODE_SWITCH_STATE", false);
                    editor.commit();

                }
            }
        });


    }


    //Back to menu
    public void openMenu(View v) {
        vibrator.vibrate(8);
        finish();

    }

    private void mute() {
        //mute audio

        AudioManager amanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
    }

    public void unmute() {
        //unmute audio
        AudioManager amanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
    }
    //https://www.youtube.com/watch?v=RyiTx8lWdx0&t=202s

}