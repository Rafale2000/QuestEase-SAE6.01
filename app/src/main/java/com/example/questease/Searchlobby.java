package com.example.questease;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

public class Searchlobby extends Theme {

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
    }

}