package com.example.questease;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.res.Configuration;
import android.os.Build;
import android.content.Context;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Pendu extends Theme {
    private String mot;
    private WebSocketService webSocketService;
    private boolean isBound = false;

    private MediaPlayer mediaPlayer;
    private int triesLeft = 12;
    private int letterstofind;
    private StringBuilder currentWordView;
    private Dialog tutorialDialog; // Référence au dialog
    private TextView cardTitle;    // Référence au titre
    private TextView cardContent;
    private ArrayList<String> tries;
    private String rulestitle = "\nBienvenue dans le pendu";
    private String rulescontent = "Normalement c'est pas très compliqué\n\nOn reste sur un pendu classique mais les deux joueurs jouent simultanément \n.";
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            WebSocketService.LocalBinder binder = (WebSocketService.LocalBinder) service;
            webSocketService = binder.getService();
            isBound = true;
            if (webSocketService != null) {
                webSocketService.sendMessage("getRandomMot", "");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };
    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("Pendu", "Broadcast received");
            if (intent.getAction().equals("WebSocketMessage")) {
                String jsonMessage = intent.getStringExtra("message");
                Log.d("RotatingPictures.java", "Message reçu brut : " + jsonMessage);
                try {
                    JSONObject jsonObject = new JSONObject(jsonMessage);
                    String tag = jsonObject.getString("tag");
                    String message = jsonObject.getString("message");
                    if ("setWord".equals(tag)) {
                        mot = message;
                        Log.d("Pendu", "Mot mystère =  : " + mot);
                        letterstofind = mot.length();
                        currentWordView = new StringBuilder();
                        for (int i = 0; i < mot.length(); i++) {
                            currentWordView.append("_ ");
                        }
                        TextView textView = findViewById(R.id.lemot);
                        textView.setText(currentWordView.toString());
                    } else if ("startActivity".equals(tag)) {
                        Intent intentgame = identifyActivity(message);
                        if (intentgame != null) {
                            startActivity(intentgame);
                            finish();
                        }

                    } else if ("PenduTry".equals(tag)) {
                        String input = message;
                        Toast.makeText(context, "l'autre joueur à joué une lettre", Toast.LENGTH_SHORT).show();
                        tries.add(input);
                        ArrayList<Integer> result = getTryResult(input);
                        if (result.isEmpty()) {
                            triesLeft -= 1;
                            if (triesLeft == 0) {
                                mediaPlayer = MediaPlayer.create(Pendu.this, R.raw.professor_layton_sucess);
                                mediaPlayer.start();
                                ViewGroup viewGroup = findViewById(R.id.main);
                                showTutorialPopup("\nPerdu", "Vous n'avez pas trouvé le mot à temps.\n Vous ne gagnerez pas de récompenses,\nCependant vous pouvez continuer à jouer\n", viewGroup);
                            }
                            updateImage();
                        }
                        updateTryView();
                        updateLeftTries();
                        updateWordView(result, input);
                        updateLeftTries();
                        runOnUiThread(() -> {
                            letterstofind -= result.size();
                        });

                    } else if ("successPopup".equals(tag)) {
                        ViewGroup viewGroup = findViewById(R.id.main);
                        mediaPlayer = MediaPlayer.create(Pendu.this, R.raw.professor_layton_sucess);
                        mediaPlayer.start();
                        showTutorialPopup(
                                "Félicitations !",
                                "Vous avez trouvé le bon mot .\n\nVous allez passer au jeu suivant dans quelques secondes.",
                                viewGroup
                        );
                    } else if ("startActivity".equals(tag)) {
                        Log.d("Lobby", "Message reçu pour startActivity : " + message);
                        Intent intentgame = identifyActivity(message);
                        startActivity(intentgame);
                        finish();
                    }
                } catch (Exception e) {
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Pendu", "Nouvelle instance créée");
        super.onCreate(savedInstanceState);
        this.tries = new ArrayList<>();
        SharedPreferences sharedPreferences = getSecurePreferences(this);
        ApplyParameters(sharedPreferences);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pendu);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ViewGroup layout = findViewById(R.id.main);
        if (sharedPreferences.getBoolean("assistance_vocale", false)) {
            lireTextViews(layout);
        }
        List<View> views = new ArrayList<>();
        MaterialButton button = findViewById(R.id.Regles);
        TextView consigne = findViewById(R.id.consigne);
        TextView leftTries = findViewById(R.id.leftTries);
        TextView lemot = findViewById(R.id.lemot);
        TextView lettersTry = findViewById(R.id.lettersTry);
        MaterialButton materialButton = findViewById(R.id.validateButton);
        views.add(button);
        views.add(consigne);
        views.add(leftTries);
        views.add(lemot);
        views.add(lettersTry);
        views.add(materialButton);
        if (sharedPreferences.getBoolean("tailleTexte", false)) {
            adjustTextSize(views);
        }
        if (sharedPreferences.getBoolean("dyslexie", false)) {
            applyFont(views);
        }
        Log.d("Pendu", "Nouvelle instance créée");

        Intent serviceIntent = new Intent(this, WebSocketService.class);
        startService(serviceIntent);
        bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);
        IntentFilter filter = new IntentFilter("WebSocketMessage");

        registerReceiver(messageReceiver, filter, Context.RECEIVER_EXPORTED);
        Log.d("Pendu", "lancement du BroadcastReceiver");
        TextInputEditText textInputEditText = findViewById(R.id.textInputEditText);
        MaterialButton validateButton = findViewById(R.id.validateButton);
        validateButton.setOnClickListener(v -> {
            String input = textInputEditText.getText().toString();
            Log.d("Pendu", "Lettre entrée : " + input);
            Log.d("Pendu", "Lettres déjà essayées : " + tries);
            Log.d("Pendu", "Taille de l'input" + input.length());
            Log.d("Pendu", "résultat du tries.contains(input)" + tries.contains(input));
            if (input.length() == 1 && tries.contains(input) == false) {
                webSocketService.sendMessage("PenduTry", input);
                tries.add(input);
                textInputEditText.setText("");
                ArrayList<Integer> result = getTryResult(input);
                if (!result.isEmpty()) {
                    updateWordView(result, input);
                    letterstofind -= result.size();


                    updateWordView(result, input);
                    if (letterstofind == 0) {
                        mediaPlayer = MediaPlayer.create(Pendu.this, R.raw.professor_layton_sucess);
                        mediaPlayer.start();
                        ViewGroup viewGroup = findViewById(R.id.main);
                        int counter = 10; // Durée en secondes
                        showTutorialPopup(
                                "Félicitations !",
                                "Vous avez trouvé le bon mot !\n\nIl est temps de passer au jeu suivant dans " + counter + " secondes",
                                viewGroup
                        );
                        webSocketService.sendMessage("successPopup", "");
                        new CountDownTimer(counter * 1000, 1000) {
                            int secondsRemaining = counter;

                            @Override
                            public void onTick(long millisUntilFinished) {
                                secondsRemaining--;

                                // Mettre à jour le contenu du popup
                                if (tutorialDialog != null && tutorialDialog.isShowing()) {
                                    cardContent.setText(
                                            "Vous avez trouvé le bon mot !\n\nIl est temps de passer au jeu suivant dans " + secondsRemaining + " secondes"
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
                } else {
                    triesLeft -= 1;
                    if (triesLeft == 0) {
                        mediaPlayer = MediaPlayer.create(Pendu.this, R.raw.professor_layton_sucess);
                        mediaPlayer.start();
                        ViewGroup viewGroup = findViewById(R.id.main);
                        showTutorialPopup("\nPerdu", "Vous n'avez pas trouvé le mot à temps.\n Vous ne gagnerez pas de récompenses,\nCependant vous pouvez continuer à jouer", viewGroup);
                    }
                    updateLeftTries();
                    updateImage();
                }
                updateTryView();

            } else {
                mediaPlayer = MediaPlayer.create(Pendu.this, R.raw.prof_layton_forbidden);
                mediaPlayer.start();
                Toast.makeText(this, "Lettre déjà utilisée ou non valide", Toast.LENGTH_SHORT).show();
            }

        });
        ViewGroup viewGroup = findViewById(R.id.main);
        showTutorialPopup(rulestitle, rulescontent, viewGroup);
        MaterialButton regles = findViewById(R.id.Regles);
        regles.setOnClickListener(v -> {
            showTutorialPopup(rulestitle, rulescontent, viewGroup);
        });
        ImageView quitter = findViewById(R.id.quitter);
        quitter.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }


    @Override
    protected void onStop() {
        super.onStop();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            try {
                unregisterReceiver(messageReceiver);
                Log.d("Pendu", "BroadcastReceiver unregistered after 3 seconds");
            } catch (IllegalArgumentException e) {
                Log.e("Pendu", "BroadcastReceiver already unregistered", e);
            }
        }, 3000); // Délai de 3 secondes
    }

    private ArrayList<Integer> getTryResult(String letter) {
        ArrayList<Integer> result = new ArrayList<>();
        char target = letter.charAt(0);
        for (int i = 0; i < mot.length(); i++) {
            if (mot.charAt(i) == target) {
                result.add(i);
            }
        }
        return result;
    }

    private void updateTryView() {
        TextView textView = findViewById(R.id.lettersTry);
        textView.setText("lettres essayées : " + tries.toString());
    }

    private void updateWordView(ArrayList<Integer> result, String letter) {
        for (int index : result) {
            currentWordView.setCharAt(index * 2, letter.charAt(0)); // Mettre à jour la lettre (multiplié par 2 pour tenir compte des espaces)
        }
        // Mettre à jour l'affichage dans le TextView
        TextView textView = findViewById(R.id.lemot);
        textView.setText(currentWordView.toString());
    }

    private void updateImage() {
        ImageView imageView = findViewById(R.id.penduImage);
        Context context = getApplicationContext();

        if (isDarkMode(context)) {
            String name = "darkpendu" + (12 - triesLeft);
            int resId = getResources().getIdentifier(name, "drawable", context.getPackageName());
            if (resId != 0) {
                imageView.setImageResource(resId);
            }
        } else {
            String name = "pendu" + (12 - triesLeft);
            int resId = getResources().getIdentifier(name, "drawable", context.getPackageName());
            if (resId != 0) {
                imageView.setImageResource(resId);
            }
        }


    }


    public void updateLeftTries() {
        TextView textView = findViewById(R.id.leftTries);
        textView.setText("essais restants: " + triesLeft);
    }

    public boolean isDarkMode(Context context) {
        // Vérifier la version Android
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            int nightMode = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            return nightMode == Configuration.UI_MODE_NIGHT_YES;
        } else {
            return false;
        }
    }


}
