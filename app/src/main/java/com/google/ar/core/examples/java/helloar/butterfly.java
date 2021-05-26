package com.google.ar.core.examples.java.helloar;

import android.animation.AnimatorSet;
import android.widget.ImageView;

class Butterfly {
    private int counter;
    private boolean caught = false;
    private ImageView imageView;
    private String color;


    Butterfly(String color){
        this.color = color;

    }

    public void setImage(){
        imageView.setImageResource(R.drawable.butterfly1);
    }

    public boolean caught(){

        return caught;
    }

    public void setCaught(){

        caught = true;
    }

    public void count(){
        counter++;
    }

    public int getCount(){
        return counter;
    }
}
