package com.example.questease;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.example.questease.MainActivity;
public class Parametres extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "QuestEasePrefs";
    private boolean isCreated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        isCreated = true;
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        ApplyParameters(sharedPreferences);
        setContentView(R.layout.activity_parametres);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button gyroscope = findViewById(R.id.gyroscope);
        gyroscope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Parametres.this, Gyroscope.class);
                startActivity(intent);
            }
        });
        //ApplyParameters(sharedPreferences);
        CheckBox daltonisme = findViewById(R.id.checkbox_parent);
        CheckBox deuteranomalie = findViewById(R.id.deuteranomalie);
        CheckBox deuteranopie = findViewById(R.id.deuteranopie);
        CheckBox protanomalie = findViewById(R.id.protanomalie);
        CheckBox protanopie = findViewById(R.id.protanopie);
        deuteranomalie.setVisibility(View.GONE);
        deuteranopie.setVisibility(View.GONE);
        protanomalie.setVisibility(View.GONE);
        protanopie.setVisibility(View.GONE);
        MaterialSwitch albinisme = findViewById(R.id.albinisme);
        MaterialSwitch cecite = findViewById(R.id.cecite);
        MaterialSwitch vocal = findViewById(R.id.poled);
        MaterialSwitch peripherique = findViewById(R.id.peripherique);
        MaterialSwitch Diplopienystagmus = findViewById(R.id.Diplopienystagmus);
        MaterialSwitch Myopiepresbytie = findViewById(R.id.Myopiepresbytie);
        MaterialSwitch VisionCentraleReduite = findViewById(R.id.VisionCentraleReduite);
        VisionCentraleReduite.setChecked(sharedPreferences.getBoolean("vision_centrale_reduite", false));
        Myopiepresbytie.setChecked(sharedPreferences.getBoolean("myopie", false));
        Diplopienystagmus.setChecked(sharedPreferences.getBoolean("diplopie", false));
        peripherique.setChecked(sharedPreferences.getBoolean("vision_peripherique", false));
        vocal.setChecked(sharedPreferences.getBoolean("assistance_vocale", false));
        cecite.setChecked(sharedPreferences.getBoolean("cecite", false));
        albinisme.setChecked(sharedPreferences.getBoolean("albinisme", false));
        daltonisme.setChecked(sharedPreferences.getInt("daltonisme", 0) != 0);
        if (sharedPreferences.getInt("daltonisme",0)!=0){
            deuteranomalie.setVisibility(View.VISIBLE);
            deuteranopie.setVisibility(View.VISIBLE);
            protanomalie.setVisibility(View.VISIBLE);
            protanopie.setVisibility(View.VISIBLE);
        }
        Log.e("SharedPreferences", "Valeur de daltonisme: " + sharedPreferences.getInt("daltonisme", 0));
        protanomalie.setChecked(sharedPreferences.getInt("daltonisme", 0) == 1);
        protanopie.setChecked(sharedPreferences.getInt("daltonisme", 0) == 2);
        deuteranomalie.setChecked(sharedPreferences.getInt("daltonisme",0) == 3);
        deuteranopie.setChecked(sharedPreferences.getInt("daltonisme",0) == 4);

        // Vérifiez et créez des entrées si elles n'existent pas
        initializeBooleanPreference("vision_centrale_reduite", false);
        initializeBooleanPreference("myopie", false);
        initializeBooleanPreference("diplopie", false);
        initializeBooleanPreference("vision_peripherique", false);
        initializeBooleanPreference("assistance_vocale", false);
        initializeBooleanPreference("cecite", false);
        initializeBooleanPreference("albinisme", false);
        initializePreference("daltonisme",0);

        VisionCentraleReduite.setOnClickListener(view -> {
            boolean isChecked = VisionCentraleReduite.isChecked();
            sharedPreferences.edit().putBoolean("vision_centrale_reduite", isChecked).apply();

        });

        Myopiepresbytie.setOnClickListener(view -> {
            boolean isChecked = Myopiepresbytie.isChecked();
            sharedPreferences.edit().putBoolean("myopie", isChecked).apply();
        });

        Diplopienystagmus.setOnClickListener(view -> {
            boolean isChecked = Diplopienystagmus.isChecked();
            sharedPreferences.edit().putBoolean("diplopie", isChecked).apply();
        });

        peripherique.setOnClickListener(view -> {
            boolean isChecked = peripherique.isChecked();
            sharedPreferences.edit().putBoolean("vision_peripherique", isChecked).apply();
        });

        vocal.setOnClickListener(view -> {
            boolean isChecked = vocal.isChecked();
            sharedPreferences.edit().putBoolean("assistance_vocale", isChecked).apply();
        });

        cecite.setOnClickListener(view -> {
            boolean isChecked = cecite.isChecked();
            sharedPreferences.edit().putBoolean("cecite", isChecked).apply();
        });

        albinisme.setOnClickListener(view -> {
            boolean isChecked = albinisme.isChecked();
            sharedPreferences.edit().putBoolean("albinisme", isChecked).apply();
        });

        daltonisme.setOnClickListener(view -> {
            if (daltonisme.isChecked()) {
                deuteranomalie.setVisibility(View.VISIBLE);
                deuteranopie.setVisibility(View.VISIBLE);
                protanomalie.setVisibility(View.VISIBLE);
                protanopie.setVisibility(View.VISIBLE);
                Log.d("Daltonisme", "Case daltonisme cochée");
            } else {
                deuteranomalie.setVisibility(View.GONE);
                deuteranomalie.setChecked(false);
                deuteranopie.setVisibility(View.GONE);
                deuteranopie.setChecked(false);
                protanomalie.setVisibility(View.GONE);
                protanomalie.setChecked(false);
                protanopie.setVisibility(View.GONE);
                protanopie.setChecked(false);
                sharedPreferences.edit().putInt("daltonisme",0).apply();
                recreate();
            }
        });

        protanomalie.setOnClickListener(view -> {
            if (protanomalie.isChecked()) {
                sharedPreferences.edit().putInt("daltonisme", 1).apply();
                deuteranomalie.setChecked(false);
                deuteranopie.setChecked(false);
                protanopie.setChecked(false);
                recreate();
            }
            else{
                sharedPreferences.edit().putInt("daltonisme", 0).apply();
                recreate();
            }
            Log.e("SharedPreferences", "Valeur de daltonisme: " + sharedPreferences.getInt("daltonisme", 0));
        });

        protanopie.setOnClickListener(view -> {
            if (protanopie.isChecked()) {
                sharedPreferences.edit().putInt("daltonisme", 2).apply();
                deuteranomalie.setChecked(false);
                deuteranopie.setChecked(false);
                protanomalie.setChecked(false);
                recreate();
            }
            else{sharedPreferences.edit().putInt("daltonisme", 0).apply();recreate();
            }

            Log.e("SharedPreferences", "Valeur de daltonisme: " + sharedPreferences.getInt("daltonisme", 0));
        });

        deuteranomalie.setOnClickListener(view -> {

            if (deuteranomalie.isChecked()) {
                sharedPreferences.edit().putInt("daltonisme", 3).apply();
                deuteranopie.setChecked(false);
                protanomalie.setChecked(false);
                protanopie.setChecked(false);
                recreate();
            }
            else{sharedPreferences.edit().putInt("daltonisme", 0).apply();
                recreate();
            }


            Log.e("SharedPreferences", "Valeur de daltonisme: " + sharedPreferences.getInt("daltonisme", 0));
        });

        deuteranopie.setOnClickListener(view -> {
            if (deuteranopie.isChecked()) {
                sharedPreferences.edit().putInt("daltonisme",4).apply();
                protanomalie.setChecked(false);
                deuteranomalie.setChecked(false);
                protanopie.setChecked(false);
                recreate();
            }
            else{
                sharedPreferences.edit().putInt("daltonisme",0).apply();
                recreate();
            }
            Log.e("SharedPreferences", "Valeur de daltonisme: " + sharedPreferences.getInt("daltonisme", 0));
        });
    }

    private void initializePreference(String key, int defaultValue) {
        if (!sharedPreferences.contains(key)) {
            sharedPreferences.edit().putInt(key, defaultValue).apply();
        }
    }

    private void initializeBooleanPreference(String key, boolean defaultValue) {
        if (!sharedPreferences.contains(key)) {
            sharedPreferences.edit().putBoolean(key, defaultValue).apply();
        }
    }
    /*@Override
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
    }*/

    public void ApplyParameters(SharedPreferences sharedPreferences){
        //Protanomalie = 1
        //Protanopie = 2
        //deuteranomalie = 3
        //deuteranopie = 4
        Log.d("SharedPreferences", "Valeur de daltonisme: " + sharedPreferences.getInt("daltonisme", 0));
        Log.d("SharedPreferences","je vais essayer d'appliquer un thème");
        if(sharedPreferences.getInt("daltonisme",0)== 1) {
            setTheme(R.style.Theme_Questease_Protanomalie);}
        else if(sharedPreferences.getInt("daltonisme",0)== 2){
            setTheme(R.style.Theme_Questease_Protanopie);
        } else if (sharedPreferences.getInt("daltonisme",0)==3) {
            setTheme(R.style.Theme_Questease_Deuteranomalie);
        } else if (sharedPreferences.getInt("daltonisme",0) == 4) {
            setTheme(R.style.Theme_Questease_deuteranopie);
        }
    }
}
