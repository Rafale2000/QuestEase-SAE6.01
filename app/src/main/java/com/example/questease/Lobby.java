package com.example.questease;



import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class Lobby extends Theme {
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "QuestEasePrefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lobby);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;


        });

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        List<View> views = new ArrayList<>();
        views.add(findViewById(R.id.Person1));
        views.add(findViewById(R.id.Person2));
        views.add(findViewById(R.id.button2));
        views.add(findViewById(R.id.text_joueurs_prets));
        if(sharedPreferences.getBoolean("tailleTexte",false)){
            adjustTextSize(views);
        }
        if(sharedPreferences.getBoolean("dyslexie",false)){
            applyFont(views);
        }
    }
}