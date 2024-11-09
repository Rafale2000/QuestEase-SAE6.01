package com.example.questease;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import Controller.ControllerPrixJuste;
import Model.Jeu.PrixJusteJeu;
import Model.BDD.ChoseATrouverPrixJuste;
import Service.HandlerObjectAPI;
import Service.ChoseCallback;

public class PrixJuste extends AppCompatActivity {
    private TextView numberOfAttempts, previousNumber, indice;
    private EditText inputNumber;
    private Button btnExit, btnValider;
    private PrixJusteJeu prixJuste;
    private ControllerPrixJuste controllerPrixJuste;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prix_juste);

        // Fetch the random object and start the game
        HandlerObjectAPI handlerObjectAPI = new HandlerObjectAPI(this);
        handlerObjectAPI.GetRandomChose(new ChoseCallback() {
            @Override
            public void onChoseReceived(ChoseATrouverPrixJuste chose) {
                prixJuste = new PrixJusteJeu(10, chose.getValeur());
                controllerPrixJuste = new ControllerPrixJuste(prixJuste);
                initializeUI(chose);
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("PrixJuste", "Error retrieving data: " + errorMessage);
                Toast.makeText(PrixJuste.this, "Failed to load game data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Initialize the UI after retrieving the data
    private void initializeUI(ChoseATrouverPrixJuste chose) {
        numberOfAttempts = findViewById(R.id.tv_attempts);
        previousNumber = findViewById(R.id.tv_previous_number);
        inputNumber = findViewById(R.id.input_number);
        btnExit = findViewById(R.id.btn_exit);
        indice = findViewById(R.id.tv_hint);
        indice.setText(chose.getNom());
        btnValider = findViewById(R.id.btn_valider);
        //path de l'image
        imageView = findViewById(R.id.image_view);
        imageView.setImageResource(getResources().getIdentifier(chose.getCheminImage(), "drawable", getPackageName()));
        numberOfAttempts.setText("Nombre de coups restant : " + controllerPrixJuste.getRemainingAttempts());

        //TODO faire fonctionner
        Button btnSwitchInput = findViewById(R.id.btn_switch_input);
        btnSwitchInput.setOnClickListener(view -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showInputMethodPicker();
            }
        });



        btnValider.setOnClickListener(view -> {
            String userInput = inputNumber.getText().toString();
            if (!userInput.isEmpty()) {
                try {
                    int guess = Integer.parseInt(userInput);
                    String result = controllerPrixJuste.CheckGuess(guess);
                    numberOfAttempts.setText("Nombre de coups restant : " + controllerPrixJuste.getRemainingAttempts());
                    previousNumber.setText("Nombre essayé : " + guess + " - " + result);
                    inputNumber.setText("");
                } catch (NumberFormatException e) {
                    Toast.makeText(PrixJuste.this, "Veuillez entrer un nombre valide.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(PrixJuste.this, "Champ vide ! Veuillez entrer un nombre.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
