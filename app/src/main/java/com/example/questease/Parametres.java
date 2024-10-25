package com.example.questease;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.example.questease.MainActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parametres extends Theme {
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "QuestEasePrefs";
    private boolean isCreated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        isCreated = true;
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        //applique le thème
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
        //elements a taille variable
        TextView colorSampleText = findViewById(R.id.colorSamplesText);
        TextView albinismeText = findViewById(R.id.albinismeText);
        TextView ceciteTexte = findViewById(R.id.ceciteTexte);
        TextView poledTexte = findViewById(R.id.poledTexte);
        TextView peripheriqueText = findViewById(R.id.peripheriqueText);
        TextView DiplopienystagmusTexte = findViewById(R.id.DiplopienystagmusTexte);
        TextView MyopiepresbytieTexte = findViewById(R.id.MyopiepresbytieTexte);
        TextView VisionCentraleReduiteTexte = findViewById(R.id.VisionCentraleReduiteTexte);
        CheckBox daltonisme = findViewById(R.id.checkbox_parent);
        CheckBox deuteranomalie = findViewById(R.id.deuteranomalie);
        CheckBox deuteranopie = findViewById(R.id.deuteranopie);
        CheckBox protanomalie = findViewById(R.id.protanomalie);
        CheckBox protanopie = findViewById(R.id.protanopie);
        List<View> views = new ArrayList<>(Arrays.asList(
                colorSampleText,
                albinismeText,
                ceciteTexte,
                poledTexte,
                peripheriqueText,
                DiplopienystagmusTexte,
                MyopiepresbytieTexte,
                VisionCentraleReduiteTexte,
                daltonisme,
                deuteranomalie
                ,deuteranopie
                ,protanomalie
                ,protanopie
        ));


        if(sharedPreferences.getBoolean("myopie",false) == true){
            adjustTextSize(views);
        }
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

        initializeBooleanPreference("vision_centrale_reduite", false);
        initializeBooleanPreference("myopie", false);
        initializeBooleanPreference("diplopie", false);
        initializeBooleanPreference("vision_peripherique", false);
        initializeBooleanPreference("assistance_vocale", false);
        initializeBooleanPreference("cecite", false);
        initializeBooleanPreference("albinisme", false);
        initializePreference("daltonisme",0);

        LinearLayout layout_vision = findViewById(R.id.layout_vision_centrale_reduite);
        LinearLayout layout_myopie = findViewById(R.id.layout_myopie);
        LinearLayout layout_diplopie = findViewById(R.id.layout_diplopie);
        LinearLayout layout_peripherique = findViewById(R.id.layout_peripherique);
        LinearLayout layout_vocal = findViewById(R.id.layout_poled);
        LinearLayout layout_cecite = findViewById(R.id.layout_cecite);
        LinearLayout layout_albinisme = findViewById(R.id.layout_albinisme);



        layout_vision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisionCentraleReduite.performClick();
            }

        });
        VisionCentraleReduite.setOnClickListener(view -> {
            boolean isChecked = VisionCentraleReduite.isChecked();
            sharedPreferences.edit().putBoolean("vision_centrale_reduite", isChecked).apply();

        });
        layout_myopie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Myopiepresbytie.performClick();
            }
        });
        Myopiepresbytie.setOnClickListener(view -> {
            boolean isChecked = Myopiepresbytie.isChecked();
            sharedPreferences.edit().putBoolean("myopie", isChecked).apply();
            recreate();
        });
        layout_diplopie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Diplopienystagmus.performClick();
            }
        });
        Diplopienystagmus.setOnClickListener(view -> {
            boolean isChecked = Diplopienystagmus.isChecked();
            sharedPreferences.edit().putBoolean("diplopie", isChecked).apply();
        });
        layout_peripherique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                peripherique.performClick();
            }
        });
        peripherique.setOnClickListener(view -> {
            boolean isChecked = peripherique.isChecked();
            sharedPreferences.edit().putBoolean("vision_peripherique", isChecked).apply();
        });
        layout_vocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vocal.performClick();
            }
        });
        vocal.setOnClickListener(view -> {
            boolean isChecked = vocal.isChecked();
            sharedPreferences.edit().putBoolean("assistance_vocale", isChecked).apply();
        });
        layout_cecite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cecite.performClick();
            }
        });
        cecite.setOnClickListener(view -> {
            boolean isChecked = cecite.isChecked();
            sharedPreferences.edit().putBoolean("cecite", isChecked).apply();
        });
        layout_albinisme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                albinisme.performClick();
            }
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
}