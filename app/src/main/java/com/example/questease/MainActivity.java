package com.example.questease;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.Image;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.SharedPreferences;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends Theme {
    private WebSocketService webSocketService;
    private boolean isBound = false;
    private boolean isCreated = false;
    private boolean isErrorPopupVisible = false;
    private static final String MAIN_ACTIVITY_STR = "MainActivity";
    private final ServiceConnection connection = new ServiceConnection() {
    private static final String DIFFICULTY = "difficulty";
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            WebSocketService.LocalBinder binder = (WebSocketService.LocalBinder) service;
            webSocketService = binder.getService();
            isBound = true;
            // Exemple : Envoyer un message une fois connecté
            if (webSocketService != null) {
                Log.e("test", "Envoi d'un message via WebSocketService");
                webSocketService.sendMessage("requestLobbies", "salut à tous c'est fanta");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };
    private final BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(MAIN_ACTIVITY_STR, "Broadcast received");
            if (Objects.equals(intent.getAction(), "WebSocketMessage")) {
                String jsonMessage = intent.getStringExtra("message");
                Log.d(MAIN_ACTIVITY_STR, "Message reçu brut : " + jsonMessage);
                try {
                    JSONObject jsonObject = new JSONObject(jsonMessage);
                    String tag = jsonObject.getString("tag");
                    String message = jsonObject.getString("message");
                    if ("WebSocketError".equals(tag) && message.equals("WebSocket is not connected!")) {
                        if (!isErrorPopupVisible) {
                            ViewGroup view = findViewById(R.id.main);
                            showServerErrorPopUp(view);
                            isErrorPopupVisible = true;
                        }
                    } else if ("ConnectionSuccess".equals(tag)) {
                        ImageView connexion = findViewById(R.id.connexion);
                        TextView username = findViewById(R.id.username);
                        String pseudo = sharedPreferences.getString("username", "");
                        connexion.setVisibility(View.GONE);
                        username.setText(pseudo);
                        username.setVisibility(View.VISIBLE);
                        Toast.makeText(context, "Connecté en tant que "+pseudo, Toast.LENGTH_SHORT).show();
                        sharedPreferences.edit().putBoolean("connected",true).apply();
                    }
                    else if("ConnectionError".equals(tag)){
                        Toast.makeText(context, "Erreur de connexion au compte", Toast.LENGTH_SHORT).show();
                        sharedPreferences.edit().putBoolean("connected",false).apply();
                    }
                    else if("Lobbylist".equals(tag)){
                        if(sharedPreferences.getBoolean("connected",false)){
                            List <String> logs = new ArrayList<>();
                            logs.add(sharedPreferences.getString("username",""));
                            logs.add(sharedPreferences.getString("password",""));
                            webSocketService.sendMessage("connectAccount",logs.toString());
                        }

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
        SharedPreferences sharedPreferences = getSharedPreferences("QuestEasePrefs", MODE_PRIVATE);
        ApplyParameters(sharedPreferences);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ViewGroup layout = findViewById(R.id.main);


        // Configurer les boutons
        Button jouer = findViewById(R.id.Jouer);
        Button parametres = findViewById(R.id.Parametres);
        LinearLayout banner = findViewById(R.id.bannerLayout);
        int difficulty = sharedPreferences.getInt(DIFFICULTY, 0);
        if (difficulty< 1 || difficulty > 3){
            sharedPreferences.edit().putInt(DIFFICULTY,3).apply();
        }

        List<View> views = List.of(jouer, parametres, banner);
        if (sharedPreferences.getBoolean("tailleTexte", false)) {
            adjustTextSize(views);
        }
        if (sharedPreferences.getBoolean("dyslexie", false)) {
            applyFont(views);
        }

        // Boutons pour naviguer
        jouer.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, Searchlobby.class);
            startActivity(intent);
            try {
                unregisterReceiver(messageReceiver);
                Log.d(MAIN_ACTIVITY_STR, "BroadcastReceiver unregistered");
            } catch (IllegalArgumentException e) {
                Log.e(MAIN_ACTIVITY_STR, "BroadcastReceiver already unregistered", e);
            }
        });

        parametres.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, Parametres.class);
            startActivity(intent);
        });

        // Lancer et lier le service WebSocket
        Intent serviceIntent = new Intent(this, WebSocketService.class);
        startService(serviceIntent);
        bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);

        // Log pour vérifier quand le receiver est enregistré
        Log.d(MAIN_ACTIVITY_STR, "Enregistrement du BroadcastReceiver");
        IntentFilter filter = new IntentFilter("WebSocketMessage");
        registerReceiver(messageReceiver, filter, Context.RECEIVER_EXPORTED);
        if (sharedPreferences.getBoolean("assistance_vocale", false)) {
            Log.d(MAIN_ACTIVITY_STR, "Lancement de lireTextViews");
            lireTextViews(layout);
        }

        isCreated = true;
        ImageView connexion  = findViewById(R.id.connexion);
        connexion.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, Connexion.class);
            startActivity(intent);
            try {
                unregisterReceiver(messageReceiver);
                Log.d("MainActivity", "BroadcastReceiver unregistered");
            } catch (IllegalArgumentException e) {
                Log.e("MainActivity", "BroadcastReceiver already unregistered", e);
            }
        });

        TextView username = findViewById(R.id.username);
        username.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, Profil.class);
            startActivity(intent);
            try {
                unregisterReceiver(messageReceiver);
                Log.d("MainActivity", "BroadcastReceiver unregistered");
            } catch (IllegalArgumentException e) {
                Log.e("MainActivity", "BroadcastReceiver already unregistered", e);
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBound) {
            unbindService(connection);
            isBound = false;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (!isCreated) {
            recreate();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.isCreated = false;
    }
}
