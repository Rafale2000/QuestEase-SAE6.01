package com.example.questease.controller;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.questease.R;

public class EndActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        mediaPlayer = MediaPlayer.create(EndActivity.this, R.raw.yoshi_win_sound);
        mediaPlayer.start();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);
    }
}
