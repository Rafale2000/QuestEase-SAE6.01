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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            WebSocketService.LocalBinder binder = (WebSocketService.LocalBinder) service;
            webSocketService = binder.getService();
            isBound = true;
            webSocketService.sendMessage("requestLobbies", "salut à tous c'est fanta de Lobby");
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };
    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("SearchLobby", "Broadcast received");
            if (intent.getAction().equals("WebSocketMessage")) {
                String jsonMessage = intent.getStringExtra("message");
                Log.d("SearchLobby", "Message reçu brut : " + jsonMessage);
                try {
                    JSONObject jsonObject = new JSONObject(jsonMessage);
                    String tag = jsonObject.getString("tag");
                    String message = jsonObject.getString("message");



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lobby);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        List<View> views = new ArrayList<>();
        views.add(findViewById(R.id.Person1));
        views.add(findViewById(R.id.Person2));
        views.add(findViewById(R.id.button2));
        views.add(findViewById(R.id.text_joueurs_prets));
        if(sharedPreferences.getBoolean("tailleTexte",false)){
            adjustTextSize(views);
        }
        if(sharedPreferences.getBoolean("dyslexie",false)){
            applyFont(views);
        }
        Button jouer = findViewById(R.id.button2);
        jouer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Lobby.this, TrouveLeSon.class));

            }
        });
        Intent intent = getIntent();
        String name = intent.getStringExtra("p1");
        String name2 = intent.getStringExtra("p2");
        String lobbyName = intent.getStringExtra("lobbyName");
        if (lobbyName != null){
            this.lobbyname = lobbyName;
        }
        Button person1 = findViewById(R.id.Person2);
        Button person2 = findViewById(R.id.Person1);
        if (name2!=null){
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
        Log.d("SearchLobby", "lancement du BroadcastReceiver");

    }
    @Override
    protected void onStop() {
        Log.d("Lobby", "le big on stop est lancé la ");
        super.onStop();
        Log.d("Lobby", "le big on stop est passé par le super ");
        Log.d("lobbyname",lobbyname);

        if (webSocketService != null && lobbyname != null) {
            // Envoyer une requête pour quitter le lobby
            webSocketService.sendMessage("leaveLobby", this.lobbyname);
            Log.d("Lobby", "Requête leaveLobby envoyée pour le lobby : " + lobbyname);
        } else {
            Log.w("Lobby", "Impossible d'envoyer leaveLobby, service ou nom du lobby indisponible.");
        }

    }





}