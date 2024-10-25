package com.example.questease;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
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
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.material.button.MaterialButton;

import org.w3c.dom.Text;

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

        /*if(sharedPreferences.getBoolean("myopie",false)){
            Resources res = getResources();
            jouer.setTextSize(TypedValue.COMPLEX_UNIT_PX, res.getDimension(R.dimen.text_size_large_large));
            paramètres.setTextSize(TypedValue.COMPLEX_UNIT_PX, res.getDimension(R.dimen.text_size_medium_large));
            TextView banner = findViewById(R.id.banner);
            banner.setTextSize(TypedValue.COMPLEX_UNIT_PX, res.getDimension(R.dimen.text_size_xlarge_large));
        }*/
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