package com.example.questease;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.database.Cursor;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.datastore.preferences.core.Preferences;
import androidx.recyclerview.widget.RecyclerView;
import android.content.SharedPreferences;
public class MainActivity extends AppCompatActivity {
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
        Button paramètres = findViewById(R.id.Paramètres);

        jouer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Searchlobby.class);
                startActivity(intent);
            }
        });

        paramètres.setOnClickListener(new View.OnClickListener() {
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
    public void ApplyParameters(SharedPreferences sharedPreferences){
        //Protanomalie = 1
        //Protanopie = 2
        //deuteranomalie = 3
        //deuteranopie = 4
        Log.d("SharedPreferences", "Valeur de daltonisme: " + sharedPreferences.getInt("daltonisme", 0));
        Log.d("SharedPreferences","je vais essayer d'appliquer un thème");
        if(sharedPreferences.getInt("daltonisme",0)== 1) {
            setTheme(R.style.Theme_Questease_Protanomalie);
        }
        else if(sharedPreferences.getInt("daltonisme",0)== 2){
            setTheme(R.style.Theme_Questease_Protanopie);
        } else if (sharedPreferences.getInt("daltonisme",0)==3) {
            setTheme(R.style.Theme_Questease_Deuteranomalie);
        } else if (sharedPreferences.getInt("daltonisme",0) == 4) {
            setTheme(R.style.Theme_Questease_deuteranopie);
        }
    }



}