package View.questease;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.questease.DatabaseHelper;
import com.example.questease.R;
import android.content.SharedPreferences;
import android.widget.TextView;

import com.example.questease.Theme;
import java.util.List;

public class MainActivity extends Theme {
    DatabaseHelper dbHelper;
    private boolean isCreated = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("QuestEasePrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        ApplyParameters(sharedPreferences);
        isCreated = true;
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Configuration des boutons
        Button jouer = findViewById(R.id.Jouer);
        Button parametres = findViewById(R.id.Parametres);
        TextView banner = findViewById(R.id.banner);
        List<View> views = List.of(jouer, parametres, banner);
        if(sharedPreferences.getBoolean("tailleTexte",false)){
            adjustTextSize(views);
        }
        if(sharedPreferences.getBoolean("dyslexie",false)){
           applyFont(views);
        }
        jouer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Searchlobby.class);
                startActivity(intent);
            }
        });

        parametres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Parametres.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (!isCreated) {
            recreate();
        }
    }
    @Override
    protected void onPause(){
        super.onPause();
        this.isCreated = false;
    }







}