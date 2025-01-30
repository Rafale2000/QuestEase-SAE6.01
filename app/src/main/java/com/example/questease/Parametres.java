package com.example.questease;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.materialswitch.MaterialSwitch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Parametres extends Theme {
    private static final String DIFFICULTY = "difficulty";
    public SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "QuestEasePrefs";
    private boolean isCreated = false;
    //valeur string en constante
    private static final String ASSISTANCE_VOCALE_STRING = "assistance_vocale";
    private static final String DALTONISME_STRING = "daltonisme";
    private static final String DALTONISME_VALEUR_STRING = "Valeur de daltonisme: ";
    private static final String DYSLEXIE_STRING = "dyslexie";
    private static final String TAILLE_TEXTE_STRING = "tailleTexte";
    private static final String CONTRASTE_ELEVE_STRING = "contrasteEleve";
    private static final String SHARED_PREFS_STRING = "SharedPreferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        isCreated = true;
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        //sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        sharedPreferences = getSecurePreferences(this);
        // applique le theme
        ApplyParameters(sharedPreferences);
        setContentView(R.layout.activity_parametres);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ViewGroup layout = findViewById(R.id.main);
        if (sharedPreferences.getBoolean("assistance_vocale", false)) {
            lireTextViews(layout);
        }
        // elements a taille variable
        TextView colorSampleText = findViewById(R.id.colorSamplesText);
        TextView dyslexieText = findViewById(R.id.DyslexieText);
        TextView contrasteEleveText = findViewById(R.id.contrasteEleveText);
        TextView poledTexte = findViewById(R.id.poledTexte);
        TextView tailleTexteTexte = findViewById(R.id.tailleTexteTexte);
        CheckBox daltonisme = findViewById(R.id.checkbox_parent);
        CheckBox deuteranomalie = findViewById(R.id.deuteranomalie);
        CheckBox deuteranopie = findViewById(R.id.deuteranopie);
        CheckBox protanomalie = findViewById(R.id.protanomalie);
        CheckBox protanopie = findViewById(R.id.protanopie);
        MaterialButton difficultyButton1 = findViewById(R.id.difficultyButton1);
        MaterialButton difficultyButton2 = findViewById(R.id.difficultyButton2);
        MaterialButton difficultyButton3 = findViewById(R.id.difficultyButton3);

        List<View> views = new ArrayList<>(Arrays.asList(
                colorSampleText,
                dyslexieText,
                contrasteEleveText,
                poledTexte,
                daltonisme,
                deuteranomalie,
                deuteranopie,
                protanomalie,
                protanopie,
                tailleTexteTexte
        ));

        if (sharedPreferences.getBoolean(TAILLE_TEXTE_STRING, false)) {
            adjustTextSize(views);
        }
        if (sharedPreferences.getBoolean(DYSLEXIE_STRING, false)) {
            applyFont(views);
        }
        deuteranomalie.setVisibility(View.GONE);
        deuteranopie.setVisibility(View.GONE);
        protanomalie.setVisibility(View.GONE);
        protanopie.setVisibility(View.GONE);
        MaterialSwitch tailleTexte = findViewById(R.id.tailleTexteSwitch);
        MaterialSwitch dyslexie = findViewById(R.id.DyslexieSwitch);
        MaterialSwitch contrasteEleve = findViewById(R.id.contrasteEleveSwitch);
        MaterialSwitch vocal = findViewById(R.id.poled);
        vocal.setChecked(sharedPreferences.getBoolean(ASSISTANCE_VOCALE_STRING, false));
        contrasteEleve.setChecked(sharedPreferences.getBoolean(CONTRASTE_ELEVE_STRING, false));
        dyslexie.setChecked(sharedPreferences.getBoolean(DYSLEXIE_STRING, false));
        daltonisme.setChecked(sharedPreferences.getInt(DALTONISME_STRING, 0) != 0);
        if (sharedPreferences.getInt(DALTONISME_STRING, 0) != 0 && sharedPreferences.getInt(DALTONISME_STRING, 0) != 5) {
            deuteranomalie.setVisibility(View.VISIBLE);
            deuteranopie.setVisibility(View.VISIBLE);
            protanomalie.setVisibility(View.VISIBLE);
            protanopie.setVisibility(View.VISIBLE);
        }
        Log.e(SHARED_PREFS_STRING, DALTONISME_VALEUR_STRING + sharedPreferences.getInt(DALTONISME_STRING, 0));
        tailleTexte.setChecked(sharedPreferences.getBoolean(TAILLE_TEXTE_STRING, false));
        protanomalie.setChecked(sharedPreferences.getInt(DALTONISME_STRING, 0) == 1);
        protanopie.setChecked(sharedPreferences.getInt(DALTONISME_STRING, 0) == 2);
        deuteranomalie.setChecked(sharedPreferences.getInt(DALTONISME_STRING, 0) == 3);
        deuteranopie.setChecked(sharedPreferences.getInt(DALTONISME_STRING, 0) == 4);
        contrasteEleve.setChecked(sharedPreferences.getInt(DALTONISME_STRING, 0) == 5);


        initializeBooleanPreference(TAILLE_TEXTE_STRING, false);
        initializeBooleanPreference(ASSISTANCE_VOCALE_STRING, false);
        initializeBooleanPreference(CONTRASTE_ELEVE_STRING, false);
        initializeBooleanPreference(DYSLEXIE_STRING, false);
        initializePreference(DALTONISME_STRING, 0);

        LinearLayout layoutTailleTexte = findViewById(R.id.tailleTexte);
        LinearLayout layoutVocal = findViewById(R.id.layout_poled);
        LinearLayout layoutDyslexie = findViewById(R.id.Dyslexie);
        LinearLayout layoutContrasteEleve = findViewById(R.id.contrasteEleve);

        layoutTailleTexte.setOnClickListener(view -> tailleTexte.performClick());
        layoutContrasteEleve.setOnClickListener(view -> contrasteEleve.performClick());
        layoutVocal.setOnClickListener(view -> vocal.performClick());
        layoutDyslexie.setOnClickListener(view -> dyslexie.performClick());

        vocal.setOnClickListener(view -> {
            boolean isChecked = vocal.isChecked();
            sharedPreferences.edit().putBoolean(ASSISTANCE_VOCALE_STRING, isChecked).apply();
        });

        contrasteEleve.setOnClickListener(view -> {
            Log.i(TAILLE_TEXTE_STRING, String.valueOf(sharedPreferences.getInt(DALTONISME_STRING, 0)));
            //enlève le param pour le daltonisme
            if (contrasteEleve.isChecked()) {
                if (daltonisme.isChecked()) {
                    daltonisme.performClick();
                }
                sharedPreferences.edit().putInt(DALTONISME_STRING, 5).apply();

            } else {
                sharedPreferences.edit().putInt(DALTONISME_STRING, 0).apply();

            }
            recreate();
        });


        dyslexie.setOnClickListener(view -> {
            boolean isChecked = dyslexie.isChecked();
            sharedPreferences.edit().putBoolean(DYSLEXIE_STRING, isChecked).apply();
            recreate();
        });


        tailleTexte.setOnClickListener(view -> {

            boolean isChecked = tailleTexte.isChecked();
            sharedPreferences.edit().putBoolean(TAILLE_TEXTE_STRING, isChecked).apply();
            recreate();
        });

        dyslexie.setOnClickListener(view -> {
            boolean isChecked = dyslexie.isChecked();
            sharedPreferences.edit().putBoolean(DYSLEXIE_STRING, isChecked).apply();
            recreate();
        });

        daltonisme.setOnClickListener(view -> {
            if (daltonisme.isChecked()) {
                deuteranomalie.setVisibility(View.VISIBLE);
                deuteranopie.setVisibility(View.VISIBLE);
                protanomalie.setVisibility(View.VISIBLE);
                protanopie.setVisibility(View.VISIBLE);
                contrasteEleve.setChecked(false);
                Log.d(DALTONISME_STRING, "Case daltonisme cochee");
            } else {
                deuteranomalie.setVisibility(View.GONE);
                deuteranomalie.setChecked(false);
                deuteranopie.setVisibility(View.GONE);
                deuteranopie.setChecked(false);
                protanomalie.setVisibility(View.GONE);
                protanomalie.setChecked(false);
                protanopie.setVisibility(View.GONE);
                protanopie.setChecked(false);
                sharedPreferences.edit().putInt(DALTONISME_STRING, 0).apply();
                recreate();
            }
        });

        protanomalie.setOnClickListener(view -> {
            if (protanomalie.isChecked()) {
                sharedPreferences.edit().putInt(DALTONISME_STRING, 1).apply();
                deuteranomalie.setChecked(false);
                deuteranopie.setChecked(false);
                protanopie.setChecked(false);
            } else {
                sharedPreferences.edit().putInt(DALTONISME_STRING, 0).apply();
            }
            recreate();
            Log.e(SHARED_PREFS_STRING, DALTONISME_VALEUR_STRING + sharedPreferences.getInt(DALTONISME_STRING, 0));
        });

        protanopie.setOnClickListener(view -> {
            if (protanopie.isChecked()) {
                sharedPreferences.edit().putInt(DALTONISME_STRING, 2).apply();
                deuteranomalie.setChecked(false);
                deuteranopie.setChecked(false);
                protanomalie.setChecked(false);

            } else {
                sharedPreferences.edit().putInt(DALTONISME_STRING, 0).apply();

            }
            recreate();
            Log.e(SHARED_PREFS_STRING, DALTONISME_VALEUR_STRING + sharedPreferences.getInt(DALTONISME_STRING, 0));
        });

        deuteranomalie.setOnClickListener(view -> {
            if (deuteranomalie.isChecked()) {
                sharedPreferences.edit().putInt(DALTONISME_STRING, 3).apply();
                deuteranopie.setChecked(false);
                protanomalie.setChecked(false);
                protanopie.setChecked(false);

            } else {
                sharedPreferences.edit().putInt(DALTONISME_STRING, 0).apply();

            }
            recreate();
            Log.e(SHARED_PREFS_STRING, DALTONISME_VALEUR_STRING + sharedPreferences.getInt(DALTONISME_STRING, 0));
        });

        deuteranopie.setOnClickListener(view -> {
            if (deuteranopie.isChecked()) {
                sharedPreferences.edit().putInt(DALTONISME_STRING, 4).apply();
                protanomalie.setChecked(false);
                deuteranomalie.setChecked(false);
                protanopie.setChecked(false);
            } else {
                sharedPreferences.edit().putInt(DALTONISME_STRING, 0).apply();
            }
            recreate();
            Log.e(SHARED_PREFS_STRING, DALTONISME_VALEUR_STRING + sharedPreferences.getInt(DALTONISME_STRING, 0));
        });
        difficultyButton1.setOnClickListener(view -> {
            sharedPreferences.edit().putInt(DIFFICULTY,1).apply();
            difficultyButton1.setAlpha(1.0f);
            difficultyButton2.setAlpha(0.3f);
            difficultyButton3.setAlpha(0.3f);
            Toast toast = Toast.makeText(this, "difficultée sélectionnée : Facile", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 0, 100);
            toast.show();
        });
        difficultyButton2.setOnClickListener(view -> {
            sharedPreferences.edit().putInt(DIFFICULTY,2).apply();
            difficultyButton1.setAlpha(0.3f);
            difficultyButton2.setAlpha(1.0f);
            difficultyButton3.setAlpha(0.3f);
        });
        difficultyButton3.setOnClickListener(view -> {
            sharedPreferences.edit().putInt(DIFFICULTY,3).apply();
            difficultyButton1.setAlpha(0.3f);
            difficultyButton2.setAlpha(0.3f);
            difficultyButton3.setAlpha(1.0f);
        });
        int difficulty = sharedPreferences.getInt(DIFFICULTY, 0);
        if (difficulty == 1 || difficulty == 0) {
            difficultyButton1.performClick();
        } else if (difficulty == 2) {
            difficultyButton2.performClick();
        } else if (difficulty == 3) {
            difficultyButton3.performClick();
        }
    }

    public void initializePreference(String key, int defaultValue) {
        if (!sharedPreferences.contains(key)) {
            sharedPreferences.edit().putInt(key, defaultValue).apply();
        }
    }

    private void initializeBooleanPreference(String key, boolean defaultValue) {
        if (!sharedPreferences.contains(key)) {
            sharedPreferences.edit().putBoolean(key, defaultValue).apply();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (!isCreated) {
            recreate();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.isCreated = false;
    }
}
