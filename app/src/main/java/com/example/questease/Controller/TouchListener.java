package com.example.questease.Controller;


import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class TouchListener implements View.OnTouchListener {
    TextView actTV;


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        String chno= String.valueOf(event.getAction());
        actTV.setText("miw");
        //Log.i("#NAS_ARG message","wselt");


        return false;
    }

    public void setActTV(TextView actTV) {
        this.actTV = actTV;
    }

}
