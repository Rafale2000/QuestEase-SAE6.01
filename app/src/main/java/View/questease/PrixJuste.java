package View.questease;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.questease.controller.ControllerPrixJuste;
import com.example.questease.controller.DrawView;
import com.example.questease.model.bdd.ChoseATrouverPrixJuste;
import com.example.questease.model.jeu.PrixJusteJeu;
import com.example.questease.R;
import com.example.questease.Theme;
import com.example.questease.WebSocketService;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;


import org.json.JSONObject;

import service.choseapi.ChoseCallBack;
import service.choseapi.ChoseHandler;


public class PrixJuste extends Theme {
    private PrixJusteJeu intancePrixJuste;
    private ControllerPrixJuste controllerPrixJuste;
    private DrawView drawView;
    private WebSocketService webSocketService;
    private boolean isBound = false;
    private MediaPlayer mediaPlayer;
    private Dialog tutorialDialog; // Référence au dialog
    private TextView cardTitle;    // Référence au titre
    private TextView cardContent;  // Référence au contenu
    private boolean canPlay = false;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            WebSocketService.LocalBinder binder = (WebSocketService.LocalBinder) service;
            webSocketService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };
    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("PrixJuste", "Broadcast received");
            if (intent.getAction().equals("WebSocketMessage")) {
                String jsonMessage = intent.getStringExtra("message");
                Log.d("PrixJuste.java", "Message reçu brut : " + jsonMessage);
                try {
                    JSONObject jsonObject = new JSONObject(jsonMessage);
                    String tag = jsonObject.getString("tag");
                    String message = jsonObject.getString("message");

                    if ("startActivity".equals(tag)) {
                        Log.d("Lobby", "Message reçu pour startActivity : " + message);
                        Intent intentgame = identifyActivity(message);
                        startActivity(intentgame);
                        finish();
                    } else if ("PrixJusteTry".equals(tag)) {
                        handlePrixJusteTry(message);
                        canPlay = true;
                    } else if ("successPopup".equals(tag)) {
                        ViewGroup viewGroup = findViewById(R.id.main);
                        mediaPlayer = MediaPlayer.create(PrixJuste.this, R.raw.professor_layton_sucess);
                        mediaPlayer.start();
                        showTutorialPopup(
                                "Félicitations !",
                                "Vous avez trouvé le bon nombre, il est temps de passer au jeu suivant",
                                viewGroup
                        );
                    } else if ("beginGame".equals(tag)) {
                        canPlay = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prix_juste);

        String titre = "\n\nRègles du prix juste";
        String contenue = "Les deux joueurs vont avoir une image et un indice\n\nLe but est de trouver le prix de l'objet correspondant\n\n les joueurs vont donc jouer en alternance";

        showTutorialPopup(titre, contenue, findViewById(R.id.main));

        drawView = findViewById(R.id.draw_view);
        Button bnt = findViewById(R.id.btn_reset);
        bnt.setOnClickListener(view -> drawView.clearCanvas());

        ChoseHandler handlerObjectAPI = new ChoseHandler(this);
        handlerObjectAPI.getRandomChose(new ChoseCallBack() {
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
        Intent serviceIntent = new Intent(this, WebSocketService.class);
        startService(serviceIntent);
        bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);

        IntentFilter filter = new IntentFilter("WebSocketMessage");
        registerReceiver(messageReceiver, filter, Context.RECEIVER_EXPORTED);
        Log.d("PrixJuste", "lancement du BroadcastReceiver");

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
     *
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
                if (canPlay) {
                    try {

                        // Conversion de l'entrée utilisateur en entier
                        int guess = Integer.parseInt(userInput);

                        // Vérification du nombre et mise à jour de l'interface
                        String result = controllerPrixJuste.CheckGuess(guess);
                        if (result.equals("CORRECT")) {
                            int counter = 10;
                            ViewGroup viewGroup = findViewById(R.id.main);
                            // Afficher le popup une première fois
                            showTutorialPopup(
                                    "Félicitations !",
                                    "Vous avez trouvé le bon nombre !\n\nIl est temps de passer au jeu suivant dans " + counter + " secondes",
                                    viewGroup
                            );
                            webSocketService.sendMessage("successPopup", "");
                            // Créer un compteur
                            new CountDownTimer(counter * 1000, 1000) {
                                int secondsRemaining = counter;

                                @Override
                                public void onTick(long millisUntilFinished) {
                                    secondsRemaining--;

                                    // Mettre à jour le contenu du popup
                                    if (tutorialDialog != null && tutorialDialog.isShowing()) {
                                        cardContent.setText(
                                                "Vous avez trouvé le bon nombre !\n\nIl est temps de passer au jeu suivant dans " + secondsRemaining + " secondes"
                                        );
                                    }
                                }

                                @Override
                                public void onFinish() {
                                    webSocketService.sendMessage("startGame", "");
                                    if (tutorialDialog != null) {
                                        tutorialDialog.dismiss();
                                    }
                                }
                            }.start();
                        }
                        numberOfAttempts.setText(getString(R.string.nbr_coup_restant, controllerPrixJuste.getRemainingAttempts()));
                        previousNumber.setText(getString(R.string.nbr_essaye, guess, result));

                        // Réinitialisation du champ de saisie
                        inputNumber.setText("");
                        drawView.clearCanvas();
                        // TODO : Envoyer la proposition à l'autre joueur et gérer sa réponse
                        webSocketService.sendMessage("PrixJusteTry", "" + guess);
                        canPlay = false;
                    } catch (NumberFormatException e) {
                        Toast.makeText(PrixJuste.this, "Veuillez entrer un nombre valide.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PrixJuste.this, "C'est pas ton tour !", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(PrixJuste.this, "Champ vide ! Veuillez entrer un nombre.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handlePrixJusteTry(String message) {
        try {
            // Le message doit contenir le dernier essai
            int lastGuess = Integer.parseInt(message);

            // Réduire les essais restants
            controllerPrixJuste.reduceRemainingAttempts(); // Ajoutez cette méthode dans votre ControllerPrixJuste si elle n'existe pas encore

            // Mettre à jour les vues
            TextView numberOfAttempts = findViewById(R.id.tv_attempts);
            TextView previousNumber = findViewById(R.id.tv_previous_number);

            // Afficher les essais restants
            numberOfAttempts.setText(getString(R.string.nbr_coup_restant, controllerPrixJuste.getRemainingAttempts()));
            String res = controllerPrixJuste.CheckGuess(lastGuess).toString();
            // Afficher le dernier essai
            previousNumber.setText("Nombre essayé :"+ lastGuess +"-" +res);

        } catch (NumberFormatException e) {
            Log.e("PrixJuste", "Message reçu invalide : " + message, e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBound) {
            unbindService(connection);
            isBound = false;
        }
        unregisterReceiver(messageReceiver);
    }


}
