package com.example.questease;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class RotatingPictures extends Theme {
    private WebSocketService webSocketService;
    private boolean isBound = false;
    private MediaPlayer mediaPlayer;
    private String rulestitle = "\n\nBienvenue dans Raider Tomb";
    private String rulescontent = "Votre équipe est composée de deux personnes\n\n Le premier est l'archéologue, ce dernier à trouvé d'étranges plaques en pierre.\n\n" +
            "Cependant les plaques sont en partie effacées par le temps\n\n" +
            "Le libraire lui à trouvé dans un ancien livre leur ancienne représentation.\n\n" +
            "Les plaques peuvent tourner,il doit donc trouver la bonne orientation des plaques en alignant le bon nombre d'étoiles\n\n" +
            "Une fois la bonne orientation trouvée il doit envoyer son résultat à l'archéologue pour qu'il résolve cet ancien puzzle\n\n";
    private int dragonRotation = 1;
    private int chevalRotation = 1;
    private int epeeRotation = 2;
    private int craneRotation = 0;
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
            Log.d("RotatingPictures", "Broadcast received");
            if (intent.getAction().equals("WebSocketMessage")) {
                String jsonMessage = intent.getStringExtra("message");
                Log.d("RotatingPictures.java", "Message reçu brut : " + jsonMessage);
                try {
                    JSONObject jsonObject = new JSONObject(jsonMessage);
                    String tag = jsonObject.getString("tag");
                    String message = jsonObject.getString("message");
                    if ("RotatingPicOrientation".equals(tag)) {

                    } else if ("successPopup".equals(tag)) {
                        ViewGroup viewGroup = findViewById(R.id.main);
                        mediaPlayer = MediaPlayer.create(RotatingPictures.this, R.raw.professor_layton_sucess);
                        mediaPlayer.start();
                        showTutorialPopup(
                                "Félicitations !",
                                "L'archéologue à bien trouvé la bonne orientation des plaques.\n\nVous allez passer au jeu suivant dans quelques secondes.",
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
        Log.d("RotatingPictures", "Nouvelle instance créée");
        SharedPreferences sharedPreferences = getSecurePreferences(this);
        ApplyParameters(sharedPreferences);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_rotating_pictures);
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
        Button sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener(v -> {
            ArrayList<Integer> rotations = new ArrayList<>();
            rotations.add(dragonRotation);
            rotations.add(chevalRotation);
            rotations.add(epeeRotation);
            rotations.add(craneRotation);
            webSocketService.sendMessage("RotatingPicOrientation", rotations.toString());
        });
        Intent serviceIntent = new Intent(this, WebSocketService.class);
        startService(serviceIntent);
        bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);

        IntentFilter filter = new IntentFilter("WebSocketMessage");
        registerReceiver(messageReceiver, filter, Context.RECEIVER_EXPORTED);
        Log.d("RotatingPictures", "lancement du BroadcastReceiver");
        List<View> views = new ArrayList<>();
        Button button = findViewById(R.id.Regles);
        TextView role = findViewById(R.id.role);
        TextView consigne = findViewById(R.id.consigne);


        views.add(button);
        views.add(role);
        views.add(consigne);
        views.add(sendButton);
        if (sharedPreferences.getBoolean("tailleTexte", false)) {
            adjustTextSize(views);
        }
        if (sharedPreferences.getBoolean("dyslexie", false)) {
            applyFont(views);
        }
        ImageView quitter = findViewById(R.id.quitter);
        quitter.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            webSocketService.sendMessage("LeaveLobby", "");
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
                Log.d("RotatingPictures", "BroadcastReceiver unregistered");
            } catch (IllegalArgumentException e) {
                Log.e("RotatingPictures", "BroadcastReceiver already unregistered", e);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            unregisterReceiver(messageReceiver);
            Log.d("RotatingPictures", "BroadcastReceiver unregistered");
        } catch (IllegalArgumentException e) {
            Log.e("RotatingPictures", "BroadcastReceiver already unregistered", e);
        }
    }
}