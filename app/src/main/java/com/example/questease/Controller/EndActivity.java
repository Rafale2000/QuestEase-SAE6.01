package com.example.questease.Controller;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.questease.R;

public class EndActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        TextView textView = findViewById(R.id.endMessage);
        textView.setText("Félicitations ! Vous avez découvert le code du coffre !");
    }
}
