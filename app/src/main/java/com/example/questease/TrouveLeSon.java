package com.example.questease;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TrouveLeSon extends Theme {
    private MediaPlayer mediaPlayer;
    private String rulestitle ="Règles du jeu";
    private String rulescontent  ="Le jeu est séparé en deux parties :\n\n - Le premier joueur est l'auditeur, il doit appuyer sur un bouton pour entendre un son et le reconnaître.\n \n - Le rédacteur recevra des messages de l'auditeur et devra entrer dans son terminal de quel élément provient le son.";
    private ViewGroup main;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trouve_le_son);
        MaterialButton regles = findViewById(R.id.Regles);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        main = findViewById(R.id.main);
       showTutorialPopup(rulestitle,rulescontent,main);

        regles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTutorialPopup(rulestitle,rulescontent,main);
            }
        });
        mediaPlayer = MediaPlayer.create(this, R.raw.yoda_sound);
        MaterialButton playButton = findViewById(R.id.playbutton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.start();
            }
        });


    }


}