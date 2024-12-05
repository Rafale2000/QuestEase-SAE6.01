package com.example.questease.Controller;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.questease.MainActivity;
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
