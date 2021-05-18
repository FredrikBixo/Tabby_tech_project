package com.google.ar.core.examples.java.helloar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CollectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
    }

    public void onClick1(View view) {
        Intent intent = new Intent(this, ButterflyInfo.class);
        intent.putExtra("butterfly_id",1);
        startActivity(intent);
    }

    public void onClick2(View view) {
        Intent intent = new Intent(this, ButterflyInfo.class);
        intent.putExtra("butterfly_id",2);
        startActivity(intent);
    }

    public void onClick3(View view) {
        Intent intent = new Intent(this, ButterflyInfo.class);
        intent.putExtra("butterfly_id",3);
        startActivity(intent);
    }
}