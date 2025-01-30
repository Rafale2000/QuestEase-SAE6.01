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
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class RotatingPictures2 extends Theme {
    private WebSocketService webSocketService;
    private boolean isBound = false;
    private Dialog tutorialDialog; // Référence au dialog
    private TextView cardTitle;    // Référence au titre
    private TextView cardContent;  // Référence au contenu

    private String rulestitle = "\n\nBienvenue dans Raider Tomb";
    private String rulescontent = "Votre équipe est composée de deux personnes\n\n Le premier est l'archéologue, ce dernier à trouvé d'étranges plaques en pierre.\n\n" +
            "Cependant les plaques sont en partie effacées par le temps\n\n" +
            "Le libraire lui à trouvé dans un ancien livre leur ancienne représentation.\n\n" +
            "Les plaques peuvent tourner,il doit donc trouver la bonne orientation des plaques en alignant le bon nombre d'étoiles\n\n" +
            "Une fois la bonne orientation trouvée il doit envoyer son résultat à l'archéologue pour qu'il résolve cet ancien puzzle\n\n";
    private int dragonRotation = 0;
    private int chevalRotation = 1;
    private int epeeRotation = 2;
    private int craneRotation = 0;
    private MediaPlayer mediaPlayer;
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
            Log.d("RotatingPictures2", "Broadcast received");
            if (intent.getAction().equals("WebSocketMessage")) {
                String jsonMessage = intent.getStringExtra("message");
                Log.d("RotatingPictures2", "Message reçu brut : " + jsonMessage);
                try {
                    JSONObject jsonObject = new JSONObject(jsonMessage);
                    String tag = jsonObject.getString("tag");
                    String message = jsonObject.getString("message");
                    if ("RotatingPicOrientation".equals(tag)) {
                        JSONArray jsonArray = new JSONArray(message);
                        int[] array = new int[jsonArray.length()];

                        for (int i = 0; i < jsonArray.length(); i++) {
                            array[i] = jsonArray.getInt(i);
                        }
                        ImageView arrow1 = findViewById(R.id.arrow1);
                        ImageView arrow2 = findViewById(R.id.arrow2);
                        ImageView arrow3 = findViewById(R.id.arrow3);
                        ImageView arrow4 = findViewById(R.id.arrow4);
                        arrow1.setRotation(array[0] * 90);
                        arrow2.setRotation(array[1] * 90);
                        arrow3.setRotation(array[2] * 90);
                        arrow4.setRotation(array[3] * 90);
                        Toast.makeText(RotatingPictures2.this, "Le libraire à envoyé son étude", Toast.LENGTH_SHORT).show();
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

        Log.d("RotatingPictures2", "Nouvelle instance créée");
        SharedPreferences sharedPreferences = getSecurePreferences(this);
        ApplyParameters(sharedPreferences);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_rotating_pictures2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ViewGroup layout = findViewById(R.id.main);
        if (sharedPreferences.getBoolean("assistance_vocale", false)) {
            lireTextViews(layout);
        }
        ImageView dragon = findViewById(R.id.dragon);
        ImageView cheval = findViewById(R.id.cheval);
        ImageView epee = findViewById(R.id.epee);
        ImageView crane = findViewById(R.id.crane);
        dragon.setOnClickListener(v -> {
            dragon.setRotation(dragon.getRotation() + 90);
            dragonRotation += 1;
            if (dragonRotation > 3) {
                dragonRotation = 0;
            }
        });
        cheval.setOnClickListener(v -> {
            cheval.setRotation(cheval.getRotation() + 90);
            chevalRotation += 1;
            if (chevalRotation > 3) {
                chevalRotation = 0;
            }
        });
        epee.setOnClickListener(v -> {
            epee.setRotation(epee.getRotation() + 90);
            epeeRotation += 1;
            if (epeeRotation > 3) {
                epeeRotation = 0;
            }
        });
        crane.setOnClickListener(v -> {
            crane.setRotation(crane.getRotation() + 90);
            craneRotation += 1;
            if (craneRotation > 3) {
                craneRotation = 0;
            }
        });
        ViewGroup viewGroup = findViewById(R.id.main);
        showTutorialPopup(this.rulestitle, this.rulescontent, viewGroup);
        MaterialButton rulesButton = findViewById(R.id.Regles);
        rulesButton.setOnClickListener(v -> {
            showTutorialPopup(this.rulestitle, this.rulescontent, viewGroup);
        });
        Intent serviceIntent = new Intent(this, WebSocketService.class);
        startService(serviceIntent);
        bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);
        List<View> views = new ArrayList<>();
        Button button = findViewById(R.id.Regles);
        TextView role = findViewById(R.id.role);
        TextView consigne = findViewById(R.id.consigne);
        TextView instructions = findViewById(R.id.instructions);
        views.add(button);
        views.add(role);
        views.add(consigne);
        views.add(instructions);
        if (sharedPreferences.getBoolean("tailleTexte", false)) {
            adjustTextSize(views);
        }
        if (sharedPreferences.getBoolean("dyslexie", false)) {
            applyFont(views);
        }

        IntentFilter filter = new IntentFilter("WebSocketMessage");
        registerReceiver(messageReceiver, filter, Context.RECEIVER_EXPORTED);
        Log.d("SearchLobby", "lancement du BroadcastReceiver");
        Button sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener(v -> {
            Log.d("les valeurs des plaques sont : ", dragonRotation + " " + chevalRotation + " " + epeeRotation + " " + craneRotation + "");
            if (dragonRotation == 3 && chevalRotation == 0 && epeeRotation == 0 && craneRotation == 3) {
                int counter = 10; // Durée en secondes
                mediaPlayer = MediaPlayer.create(RotatingPictures2.this, R.raw.professor_layton_sucess);
                mediaPlayer.start();
                // Afficher le popup une première fois
                showTutorialPopup(
                        "Félicitations !",
                        "Vous avez trouvé la bonne orientation des plaques !\n\nIl est temps de passer au jeu suivant dans " + counter + " secondes",
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
                                    "Vous avez trouvé la bonne orientation des plaques !\n\nIl est temps de passer au jeu suivant dans " + secondsRemaining + " secondes"
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

        });
        ImageView quitter = findViewById(R.id.quitter);
        quitter.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBound) {
            unbindService(connection);
            isBound = false;
            try {
                unregisterReceiver(messageReceiver);
                Log.d("RotatingPictures2", "BroadcastReceiver unregistered");
            } catch (IllegalArgumentException e) {
                Log.e("RotatingPictures2", "BroadcastReceiver already unregistered", e);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            unregisterReceiver(messageReceiver);
            Log.d("RotatingPictures2", "BroadcastReceiver unregistered");
        } catch (IllegalArgumentException e) {
            Log.e("RotatingPictures2", "BroadcastReceiver already unregistered", e);
        }
    }
}