/*package com.example.questease;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import Controller.ControllerPrixJuste;
import Model.jeu.PrixJusteJeu;

public class PrixJuste extends AppCompatActivity {
    //TODO faire appel au controller pour recup le mot random puis lancer la partie
    private TextView numberOfAttempts, previousNumber, indice;
    private EditText inputNumber;
    private Button btnExit;
    private PrixJusteJeu prixJuste;
    private ControllerPrixJuste controllerPrixJuste;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Difficulté a prendre en compte
        prixJuste = new PrixJusteJeu(10);
        controllerPrixJuste = new ControllerPrixJuste(prixJuste);

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_prix_juste);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get references to UI elements
        numberOfAttempts = findViewById(R.id.tv_attempts);
        //TODO modifier avec la valeur du jeu
        previousNumber = findViewById(R.id.tv_previous_number);
        //Update a chaque essaie dans une boucle
        inputNumber = findViewById(R.id.input_number);
        btnExit = findViewById(R.id.btn_exit);
        indice = findViewById(R.id.tv_hint);
        //mettre a jour avec la valeur de la bdd
        Button btn_switch_input = findViewById(R.id.btn_switch_input);
        //button switch keyboard
        findViewById(btn_switch_input.getId()).setOnClickListener(view -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showInputMethodPicker();
            }
        });
    }
}*/