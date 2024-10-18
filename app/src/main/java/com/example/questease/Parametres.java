package com.example.questease;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.CheckBox;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;



public class Parametres extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_parametres);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        CheckBox daltonisme = findViewById(R.id.checkbox_parent);
        CheckBox deuteranomalie = findViewById(R.id.deuteranomalie);
        CheckBox deuteranopie = findViewById(R.id.deuteranopie);
        CheckBox protanomalie = findViewById(R.id.protanomalie);
        CheckBox protanopie = findViewById(R.id.protanopie);
        deuteranomalie.setVisibility(View.GONE);
        deuteranopie.setVisibility(View.GONE);
        protanomalie.setVisibility(View.GONE);
        protanopie.setVisibility(View.GONE);
        daltonisme.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (daltonisme.isChecked()) {

                    deuteranomalie.setVisibility(View.VISIBLE);
                    deuteranopie.setVisibility(View.VISIBLE);
                    protanomalie.setVisibility(View.VISIBLE);
                    protanopie.setVisibility(View.VISIBLE);
                } else {
                    deuteranomalie.setVisibility(View.GONE);
                    deuteranomalie.setChecked(false);

                    deuteranopie.setVisibility(View.GONE);
                    deuteranopie.setChecked(false);

                    protanomalie.setVisibility(View.GONE);
                    protanomalie.setChecked(false);

                    protanopie.setVisibility(View.GONE);
                    protanopie.setChecked(false);
                }


            }
        });

        protanomalie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTheme(R.style.Theme_Questease_Protanomalie);
                if (protanomalie.isChecked()) {
                    deuteranomalie.setChecked(false);
                    deuteranopie.setChecked(false);
                    protanopie.setChecked(false);
                }
            }

        });
        protanopie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTheme(R.style.Theme_Questease_Protanopie);
                if(protanopie.isChecked()) {
                    deuteranomalie.setChecked(false);
                    deuteranopie.setChecked(false);
                    protanomalie.setChecked(false);
                }
            }
        });
        deuteranomalie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTheme(R.style.Theme_Questease_deuteranomalie);
                if(deuteranomalie.isChecked()) {
                    protanomalie.setChecked(false);
                    deuteranopie.setChecked(false);
                    protanopie.setChecked(false);
                }
            }
        });
        deuteranopie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTheme(R.style.Theme_Questease_Deuteranopie);
                if(deuteranopie.isChecked()) {
                    protanomalie.setChecked(false);
                    deuteranomalie.setChecked(false);
                    protanopie.setChecked(false);
                }
            }
        });
        }





    }