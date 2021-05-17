package com.google.ar.core.examples.java.helloar;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;

public class Butterfly_info extends AppCompatActivity {


    ImageView butterfly_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_butterfly_info);

        Integer id;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                id = null;
            } else {
                id = extras.getInt("butterfly_id");
            }
        } else {
            id = (Integer) savedInstanceState.getSerializable("butterfly_id");
        }

        butterfly_image = (ImageView) findViewById(R.id.imageView3);

        switch (id) {
            case 1:
                butterfly_image.setImageResource(R.drawable.butterfly1);
                break;
            case 2:
                butterfly_image.setImageResource(R.drawable.butterfly2);
                break;
            case 3:
                butterfly_image.setImageResource(R.drawable.butterfly3);
                break;
            default:
                break;
        }
    }
}