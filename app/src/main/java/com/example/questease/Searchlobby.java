package com.example.questease;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class Searchlobby extends Theme {
    private List<View> views = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        SharedPreferences sharedPreferences = getSharedPreferences("QuestEasePrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        ApplyParameters(sharedPreferences);
        setContentView(R.layout.activity_searchlobby);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        MaterialButton creerLobby = findViewById(R.id.creerLobby);
        creerLobby.setOnClickListener(view -> {
            Intent intent = new Intent(Searchlobby.this, Lobby.class);
            startActivity(intent);
        });

        views.add(creerLobby);
        ScrollView scrollView = findViewById(R.id.scrollView);
        getButtons(scrollView);
        if(sharedPreferences.getBoolean("myopie",false)){
            adjustTextSize(views);
        }
        if(sharedPreferences.getBoolean("dyslexie",false)){
         applyFont(views);
        }
    }

    private void getButtons(ViewGroup parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            if (child instanceof MaterialButton) {
                views.add((MaterialButton) child);
            } else if (child instanceof ViewGroup) {
                getButtons((ViewGroup) child);
            }
        }
    }
}