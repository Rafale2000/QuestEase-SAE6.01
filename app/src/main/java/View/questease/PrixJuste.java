package View.questease;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.questease.Controller.ControllerPrixJuste;
import com.example.questease.Controller.DrawView;
import com.example.questease.Model.BDD.ChoseATrouverPrixJuste;
import com.example.questease.Model.Jeu.PrixJusteJeu;
import com.example.questease.R;
import com.example.questease.Theme;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;


import Service.ChoseAPI.ChoseCallBack;
import Service.ChoseAPI.ChoseHandler;


public class PrixJuste extends Theme {
    private PrixJusteJeu intancePrixJuste;
    private ControllerPrixJuste controllerPrixJuste;
    private DrawView drawView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prix_juste);

        String titre = "règle du prix juste";
        String contenue = "Un indice est une image s'affiche, il faut trouver le bon prix";
        showTutorialPopup(titre, contenue, findViewById(R.id.main));

        drawView = findViewById(R.id.draw_view);
        Button bnt = findViewById(R.id.btn_reset);
        bnt.setOnClickListener(view -> drawView.clearCanvas());


        ChoseHandler handlerObjectAPI = new ChoseHandler(this);
        handlerObjectAPI.GetRandomChose(new ChoseCallBack() {
            @Override
            public void onChoseReceived(ChoseATrouverPrixJuste chose) {
                intancePrixJuste = new PrixJusteJeu(10, chose.getValeur());
                controllerPrixJuste = new ControllerPrixJuste(intancePrixJuste);
                initializeUI(chose);
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("PrixJuste", "Error retrieving data: " + errorMessage);
                Toast.makeText(PrixJuste.this, "Failed to load game data", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void recognizeHandwrittenText(EditText editText) {
        // Capture content from DrawView
        Bitmap bitmap = Bitmap.createBitmap(drawView.getWidth(), drawView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawView.draw(canvas);

        InputImage image = InputImage.fromBitmap(bitmap, 0);

        // Initialize TextRecognizer
        TextRecognizer textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        textRecognizer.process(image)
                .addOnSuccessListener(visionText -> {
                    String recognizedText = visionText.getText();


                    if (editText != null || !visionText.getText().isEmpty()) {
                        editText.setText(recognizedText);
                    }



                    Log.d("MLKit", "Recognized Text: " + recognizedText);
                    Toast.makeText(this, "Text: " + recognizedText, Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e("MLKit", "Text recognition failed", e);
                    Toast.makeText(this, "Failed to recognize text.", Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * Initialize l'interface utilisateur et met en place des écouteurs pour certaines fonctionnalitées.
     * @param chose
    */
    private void initializeUI(ChoseATrouverPrixJuste chose) {
        // Initialisation des TextViews
        TextView numberOfAttempts = findViewById(R.id.tv_attempts);
        TextView previousNumber = findViewById(R.id.tv_previous_number);
        TextView indice = findViewById(R.id.tv_hint);

        // Configuration de l'indice
        indice.setText(chose.getNom());

        // Initialisation des Buttons
        Button btnValider = findViewById(R.id.btn_valider);
        Button btnSwitchInput = findViewById(R.id.btn_switch_input);
        Button fleche = findViewById(R.id.btn_fleche);

        // Initialisation de l'EditText
        EditText inputNumber = findViewById(R.id.input_number);

        // Log de vérification pour le chemin de l'image
        Log.d(TAG, chose.getCheminImage());

        // Initialisation de l'ImageView et chargement de l'image
        ImageView imageView = findViewById(R.id.image_view);
        int imageResId = getResources().getIdentifier(chose.getCheminImage(), "drawable", getPackageName());
        imageView.setImageResource(imageResId);

        // Affichage du nombre de tentatives restantes
        numberOfAttempts.setText(getString(R.string.nbr_coup_restant, controllerPrixJuste.getRemainingAttempts()));

        // Bouton pour changer de méthode de saisie
        btnSwitchInput.setOnClickListener(view -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showInputMethodPicker();
            }
        });

        // Bouton pour reconnaître le texte manuscrit
        fleche.setOnClickListener(view -> recognizeHandwrittenText(inputNumber));

        // Bouton pour valider l'entrée utilisateur
        btnValider.setOnClickListener(view -> {
            String userInput = inputNumber.getText().toString().trim();
            if (!userInput.isEmpty()) {
                try {
                    // Conversion de l'entrée utilisateur en entier
                    int guess = Integer.parseInt(userInput);

                    // Vérification du nombre et mise à jour de l'interface
                    String result = controllerPrixJuste.CheckGuess(guess);
                    numberOfAttempts.setText(getString(R.string.nbr_coup_restant, controllerPrixJuste.getRemainingAttempts()));
                    previousNumber.setText(getString(R.string.nbr_essaye, guess, result));


                    // Réinitialisation du champ de saisie
                    inputNumber.setText("");
                    drawView.clearCanvas();

                    // TODO : Envoyer la proposition à l'autre joueur et gérer sa réponse

                } catch (NumberFormatException e) {
                    Toast.makeText(PrixJuste.this, "Veuillez entrer un nombre valide.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(PrixJuste.this, "Champ vide ! Veuillez entrer un nombre.", Toast.LENGTH_SHORT).show();
            }
        });
    }



}
