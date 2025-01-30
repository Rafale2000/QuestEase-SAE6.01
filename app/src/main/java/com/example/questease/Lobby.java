package com.example.questease;


import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.sax.StartElementListener;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Lobby extends Theme {
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "QuestEasePrefs";
    private WebSocketService webSocketService;
    private boolean isBound = false;
    private String lobbyname;
    private boolean ready;
    private int readyCount = 0;
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
            Log.d("Lobby.java", "Broadcast received");
            if (intent.getAction().equals("WebSocketMessage")) {
                String jsonMessage = intent.getStringExtra("message");
                Log.d("Lobby.java", "Message reçu brut : " + jsonMessage);
                try {
                    JSONObject jsonObject = new JSONObject(jsonMessage);
                    String tag = jsonObject.getString("tag");
                    String message = jsonObject.getString("message");
                    if ("setP2Name".equals(tag)) {
                        Button person2 = findViewById(R.id.Person2);
                        person2.setText(message);
                        Toast.makeText(Lobby.this, message + " à rejoint la partie", Toast.LENGTH_SHORT).show();
                    } else if ("p1Leaved".equals(tag)) {
                        finish();
                        Intent intentNewActivity = new Intent(Lobby.this, Searchlobby.class);
                        startActivity(intentNewActivity);
                        finish();
                    } else if ("p2Leaved".equals(tag)) {
                        Button person2 = findViewById(R.id.Person2);
                        person2.setText("");
                        Toast.makeText(Lobby.this, message + " à quitté la partie", Toast.LENGTH_SHORT).show();
                    } else if ("playerReady".equals(tag)) {
                        TextView textView = findViewById(R.id.text_joueurs_prets);
                        if (message.equals("true")) {
                            readyCount += 1;
                            checkReadyStatus();
                        } else {
                            readyCount -= 1;
                        }
                        textView.setText("Joueurs prêts " + readyCount + "/2");
                    } else if ("startActivity".equals(tag)) {
                        Intent intentgame = identifyActivity(message);
                        if (intentgame != null) {
                            startActivity(intentgame);
                            finish();
                        }
                    }
                } catch (Exception e) {
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Lobby", "Nouvelle instance créée");
        super.onCreate(savedInstanceState);
        sharedPreferences = getSecurePreferences(this);
        ApplyParameters(sharedPreferences);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lobby);
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
        views.add(findViewById(R.id.Person1));
        views.add(findViewById(R.id.Person2));
        views.add(findViewById(R.id.buttonReady));
        views.add(findViewById(R.id.text_joueurs_prets));
        if (sharedPreferences.getBoolean("tailleTexte", false)) {
            adjustTextSize(views);
        }
        if (sharedPreferences.getBoolean("dyslexie", false)) {
            applyFont(views);
        }
        this.readyCount = 0;
        this.ready = false;
        TextView textView = findViewById(R.id.text_joueurs_prets);
        Button jouer = findViewById(R.id.buttonReady);
        jouer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ready = !ready;
                if (ready) {
                    webSocketService.sendMessage("ready", "true");
                    readyCount += 1;
                    jouer.setText("Pas prêt");
                    TypedValue typedValue = new TypedValue();
                    getTheme().resolveAttribute(R.attr.colorButtonBackground2, typedValue, true);
                    int color = typedValue.data;
                    jouer.setBackgroundColor(color);
                } else {
                    webSocketService.sendMessage("ready", "false");
                    readyCount -= 1;
                    jouer.setText("Prêt");
                    TypedValue typedValue = new TypedValue();
                    getTheme().resolveAttribute(R.attr.colorButtonBackground, typedValue, true);
                    int color = typedValue.data;
                    jouer.setBackgroundColor(color);
                }
                textView.setText("Joueurs prêts " + readyCount + "/2");
            }
        });
        Intent intent = getIntent();
        String name = intent.getStringExtra("p1");
        String name2 = intent.getStringExtra("p2");
        String lobbyName = intent.getStringExtra("lobbyName");
        if (lobbyName != null) {
            this.lobbyname = lobbyName;
        }
        Button person1 = findViewById(R.id.Person1);
        Button person2 = findViewById(R.id.Person2);
        if (name2 != null) {
            person2.setText(name2);
        }
        if (name != null) {
            person1.setText(name);
        }
        Intent serviceIntent = new Intent(this, WebSocketService.class);
        startService(serviceIntent);
        bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);

        IntentFilter filter = new IntentFilter("WebSocketMessage");
        registerReceiver(messageReceiver, filter, Context.RECEIVER_EXPORTED);
        Log.d("Lobby", "lancement du BroadcastReceiver");
    }

    @Override
    protected void onStop() {
        Log.d("Lobby", "le big on stop est lancé la ");
        super.onStop();
        Log.d("Lobby", "le big on stop est passé par le super ");
        Log.d("lobbyname", lobbyname);
        if (webSocketService != null && lobbyname != null) {
            if (readyCount != 2) {
                webSocketService.sendMessage("leaveLobby", this.lobbyname);
                Log.d("Lobby", "Requête leaveLobby envoyée pour le lobby : " + lobbyname);
            }
        } else {
            Log.w("Lobby", "Impossible d'envoyer leaveLobby, service ou nom du lobby indisponible.");
        }
        if (isBound) {
            unbindService(connection);
            isBound = false;
        }
        try {
            unregisterReceiver(messageReceiver);
            Log.d("Lobby", "BroadcastReceiver unregistered");
        } catch (IllegalArgumentException e) {
            Log.e("Lobby", "BroadcastReceiver already unregistered", e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBound) {
            unbindService(connection);
            isBound = false;
        }
    }

    private void checkReadyStatus() {
        if (this.readyCount == 2) {
            webSocketService.sendMessage("startGame", "");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Lobby", "le big on pause est lancé la ");
        super.onStop();
        Log.d("Lobby", "le big on pause est passé par le super ");
        Log.d("lobbyname", lobbyname);
        if (webSocketService != null && lobbyname != null) {
            // Envoyer une requête pour quitter le lobby
            if (readyCount != 2) {
                webSocketService.sendMessage("leaveLobby", this.lobbyname);
                Log.d("Lobby", "Requête leaveLobby envoyée pour le lobby : " + lobbyname);
            }
        } else {
            Log.w("Lobby", "Impossible d'envoyer leaveLobby, service ou nom du lobby indisponible.");
        }
        if (isBound) {
            unbindService(connection);
            isBound = false;
        }
        try {
            unregisterReceiver(messageReceiver);
            Log.d("Lobby", "BroadcastReceiver unregistered");
        } catch (IllegalArgumentException e) {
            Log.e("Lobby", "BroadcastReceiver already unregistered", e);
        }
    }

}