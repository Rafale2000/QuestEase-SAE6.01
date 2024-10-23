package com.example.questease;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Sincere_Menteur extends AppCompatActivity {

    private int id;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = 2;

        if (id==2) {
            partie2enigme();
        }
        Button buttonValider = findViewById(R.id.button5);

// Ajout d'un OnClickListener au bouton
        buttonValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Appel à la fonction de vérification des réponses ici
                verifierReponses();
            }
        });
        }

    private void verifierReponses() {
            // Récupérer les choix des joueurs pour Jacque
            RadioGroup radioGroupJacque = findViewById(R.id.radioGroup);
            int selectedIdJacque = radioGroupJacque.getCheckedRadioButtonId();

            // Récupérer les choix des joueurs pour Marie
            RadioGroup radioGroupMarie = findViewById(R.id.radioGroup2);
            int selectedIdMarie = radioGroupMarie.getCheckedRadioButtonId();

            // Variables pour stocker les réponses
            boolean jacqueSincere = selectedIdJacque == R.id.radioButtonSincere;
            boolean marieSincere = selectedIdMarie == R.id.radioButtonSincere2;
    }

    private void partie2enigme() {
        TextView tv1= findViewById(R.id.textView4);
        TextView tv2= findViewById(R.id.textView5);
        TextView tv3= findViewById(R.id.textView6);
        TextView tv4= findViewById(R.id.textView7);

        tv1.setText("Henri");
        tv2.setText("Jeanne");
        tv3.setText("Marie ment, mais je ne sais pas pour Jacque");
        tv4.setText("Jacque est sincère et Marie ment");

    }
}



