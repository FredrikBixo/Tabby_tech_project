package com.google.ar.core.examples.java.helloar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu); //I know there is an issue here i might be able to fix with dependencies

        //Knappar för onClick metod, startar activity
        button = (Button) findViewById(R.id.playButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPlay();
            }
        });

        button = (Button) findViewById(R.id.SceneViewer);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openScene();
            }
        });

        /*button = (Button) findViewById(R.id.achButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAch();
            }
        });*/
    }

    //Metoder för att starta specifik activity

    public void openPlay() {
        Intent intent = new Intent(this, HelloArActivity.class);
        startActivity(intent);
    }

    public void openScene() {
        Intent sceneViewerIntent = new Intent(Intent.ACTION_VIEW);
        Uri intentUri =
                Uri.parse("https://arvr.google.com/scene-viewer/1.0").buildUpon()
                        .appendQueryParameter("file", "https://raw.githubusercontent.com/FredrikBixo/Tabby_tech_project/main/app/src/main/assets/models/butterfly.gltf")
                        .appendQueryParameter("mode", "ar_only")
                        .build();
        sceneViewerIntent.setData(intentUri);
        sceneViewerIntent.setPackage("com.google.ar.core");
        startActivity(sceneViewerIntent);
    }
    /*public void openAch() {
        Intent intent = new Intent(this, ÄNDRA_DEN_HÄR_KLASSEN.class);
        startActivity(intent);
    }*/
}