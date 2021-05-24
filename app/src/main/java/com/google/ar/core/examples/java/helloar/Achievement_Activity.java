package com.google.ar.core.examples.java.helloar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;



public class Achievement_Activity extends AppCompatActivity {

    MyRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement_);

        // data to populate the RecyclerView with

    }

    public void onClick1(View view) {
        Intent intent = new Intent(this, Butterfly_info.class);
        intent.putExtra("butterfly_id",1);
        startActivity(intent);
    }

    public void onClick2(View view) {
        Intent intent = new Intent(this, Butterfly_info.class);
        intent.putExtra("butterfly_id",2);
        startActivity(intent);
    }

    public void onClick3(View view) {
        Intent intent = new Intent(this, Butterfly_info.class);
        intent.putExtra("butterfly_id",3);
        startActivity(intent);
    }

    public void onClick4(View view) {

    }

    public void onClick5(View view) {

    }

    public void onClick6(View view) {

    }



}