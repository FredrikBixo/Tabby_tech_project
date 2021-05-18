package com.google.ar.core.examples.java.helloar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ButterflyInfo extends AppCompatActivity {


    ImageView butterfly_image;
    TextView butterfly_name;

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
        butterfly_name = (TextView) findViewById(R.id.textView4);

        switch (id) {
            case 1:
                butterfly_image.setImageResource(R.drawable.butterfly1);
                butterfly_name.setText(R.string.butterfly1_name);
                break;
            case 2:
                butterfly_image.setImageResource(R.drawable.butterfly2);
                butterfly_name.setText(R.string.butterfly2_name);
                break;
            case 3:
                butterfly_image.setImageResource(R.drawable.butterfly3);
                butterfly_name.setText(R.string.butterfly3_name);
                break;
            default:
                break;
        }
    }
}