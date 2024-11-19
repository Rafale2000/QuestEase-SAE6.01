    package com.example.questease;

    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.View;
    import android.widget.Button;
    import android.widget.CheckBox;
    import android.widget.LinearLayout;
    import android.widget.TextView;

    import androidx.activity.EdgeToEdge;
    import androidx.core.graphics.Insets;
    import androidx.core.view.ViewCompat;
    import androidx.core.view.WindowInsetsCompat;

    import com.google.android.material.materialswitch.MaterialSwitch;

    import java.util.ArrayList;
    import java.util.Arrays;
    import java.util.List;

    import View.questease.Gyroscope;

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
            TextView dyslexieText = findViewById(R.id.DyslexieText);
            TextView contrasteÉlevéText = findViewById(R.id.contrasteÉlevéText);
            TextView poledTexte = findViewById(R.id.poledTexte);
            TextView tailleTexteTexte = findViewById(R.id.tailleTexteTexte);
            CheckBox daltonisme = findViewById(R.id.checkbox_parent);
            CheckBox deuteranomalie = findViewById(R.id.deuteranomalie);
            CheckBox deuteranopie = findViewById(R.id.deuteranopie);
            CheckBox protanomalie = findViewById(R.id.protanomalie);
            CheckBox protanopie = findViewById(R.id.protanopie);
            List<View> views = new ArrayList<>(Arrays.asList(
                    colorSampleText,
                    dyslexieText,
                    contrasteÉlevéText,
                    poledTexte,
                    daltonisme,
                    deuteranomalie
                    ,deuteranopie
                    ,protanomalie
                    ,protanopie
                    ,tailleTexteTexte
            ));


            if(sharedPreferences.getBoolean("tailleTexte",false)){
                adjustTextSize(views);
            }
            if(sharedPreferences.getBoolean("dyslexie",false)){
                applyFont(views);
            }
            deuteranomalie.setVisibility(View.GONE);
            deuteranopie.setVisibility(View.GONE);
            protanomalie.setVisibility(View.GONE);
            protanopie.setVisibility(View.GONE);
            MaterialSwitch tailleTexte = findViewById(R.id.tailleTexteSwitch);
            MaterialSwitch dyslexie = findViewById(R.id.DyslexieSwitch);
            MaterialSwitch contrasteÉlevé = findViewById(R.id.contrasteÉlevéSwitch);
            MaterialSwitch vocal = findViewById(R.id.poled);


            vocal.setChecked(sharedPreferences.getBoolean("assistance_vocale", false));
            contrasteÉlevé.setChecked(sharedPreferences.getBoolean("contrasteEleve", false));
            dyslexie.setChecked(sharedPreferences.getBoolean("dyslexie", false));
            daltonisme.setChecked(sharedPreferences.getInt("daltonisme", 0) != 0);
            if (sharedPreferences.getInt("daltonisme",0)!=0){
                deuteranomalie.setVisibility(View.VISIBLE);
                deuteranopie.setVisibility(View.VISIBLE);
                protanomalie.setVisibility(View.VISIBLE);
                protanopie.setVisibility(View.VISIBLE);
            }
            Log.e("SharedPreferences", "Valeur de daltonisme: " + sharedPreferences.getInt("daltonisme", 0));
            tailleTexte.setChecked(sharedPreferences.getBoolean("tailleTexte", false));
            protanomalie.setChecked(sharedPreferences.getInt("daltonisme", 0) == 1);
            protanopie.setChecked(sharedPreferences.getInt("daltonisme", 0) == 2);
            deuteranomalie.setChecked(sharedPreferences.getInt("daltonisme",0) == 3);
            deuteranopie.setChecked(sharedPreferences.getInt("daltonisme",0) == 4);


            initializeBooleanPreference("tailleTexte", false);
            initializeBooleanPreference("assistance_vocale", false);
            initializeBooleanPreference("contrasteEleve", false);
            initializeBooleanPreference("dyslexie", false);
            initializePreference("daltonisme",0);


            LinearLayout layout_tailleTexte = findViewById(R.id.tailleTexte);
            LinearLayout layout_vocal = findViewById(R.id.layout_poled);
            LinearLayout layout_dyslexie = findViewById(R.id.Dyslexie);
            LinearLayout layout_contrasteÉlevé = findViewById(R.id.contrasteÉlevé);





            layout_tailleTexte.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tailleTexte.performClick();
                }
            });

            layout_contrasteÉlevé.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    contrasteÉlevé.performClick();
                }
            });

            layout_vocal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    vocal.performClick();
                }
            });
            layout_dyslexie.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dyslexie.performClick();
                }
            });
            vocal.setOnClickListener(view -> {
                boolean isChecked = vocal.isChecked();
                sharedPreferences.edit().putBoolean("assistance_vocale", isChecked).apply();
            });
            contrasteÉlevé.setOnClickListener(view -> {
                boolean isChecked = contrasteÉlevé.isChecked();
                sharedPreferences.edit().putBoolean("contrasteEleve", isChecked).apply();

            });

            tailleTexte.setOnClickListener(view -> {
                boolean isChecked = tailleTexte.isChecked();
                sharedPreferences.edit().putBoolean("tailleTexte", isChecked).apply();
                recreate();
            });
            layout_contrasteÉlevé.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    contrasteÉlevé.performClick();
                }
            });
            dyslexie.setOnClickListener(view -> {
                boolean isChecked = dyslexie.isChecked();
                sharedPreferences.edit().putBoolean("dyslexie", isChecked).apply();
                recreate();
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