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
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Searchlobby extends Theme {
    private List<View> views = new ArrayList<>();
    private WebSocketService webSocketService;
    private boolean isBound = false;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            WebSocketService.LocalBinder binder = (WebSocketService.LocalBinder) service;
            webSocketService = binder.getService();
            isBound = true;
            Log.d("SearchLobby", "Service connected");
            webSocketService.sendMessage("requestLobbies", "salut à tous c'est fanta de searchlobby");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("WebSocketMessage")) {
                String jsonMessage = intent.getStringExtra("message");

                Log.d("SearchLobby", "Message reçu brut : " + jsonMessage);

                try {
                    // Analyser le message JSON
                    JSONObject jsonObject = new JSONObject(jsonMessage);
                    String tag = jsonObject.getString("tag");
                    String message = jsonObject.getString("message");

                    // Vous pouvez maintenant traiter la liste contenue dans le message JSON
                    JSONArray messageArray = new JSONArray(message);
                    List<String> lobbies = new ArrayList<>();
                    for (int i = 0; i < messageArray.length(); i++) {
                        lobbies.add(messageArray.getString(i));
                    }

                    Log.d("SearchLobby", "Tag: " + tag);
                    Log.d("SearchLobby", "Lobbies: " + lobbies);
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
        SharedPreferences sharedPreferences = getSharedPreferences("QuestEasePrefs", MODE_PRIVATE);
        ApplyParameters(sharedPreferences);
        setContentView(R.layout.activity_searchlobby);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MaterialButton creerLobby = findViewById(R.id.creerLobby);
        creerLobby.setOnClickListener(view -> {
            Intent intent = new Intent(Searchlobby.this, Lobby.class);
            startActivity(intent);
        });

        views.add(creerLobby);
        ScrollView scrollView = findViewById(R.id.scrollView);
        getButtons(scrollView);
        if (sharedPreferences.getBoolean("myopie", false)) {
            adjustTextSize(views);
        }
        if (sharedPreferences.getBoolean("dyslexie", false)) {
            applyFont(views);
        }

        Intent serviceIntent = new Intent(this, WebSocketService.class);
        startService(serviceIntent);
        bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);

        IntentFilter filter = new IntentFilter("WebSocketMessage");
        registerReceiver(messageReceiver, filter, Context.RECEIVER_NOT_EXPORTED);
        Log.d("SearchLobby", "lancement du BroadcastReceiver");


        Intent testIntent = new Intent("WebSocketMessage");
        testIntent.putExtra("message", "Test de réception");
        sendBroadcast(testIntent);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Désenregistrer le BroadcastReceiver
        unregisterReceiver(messageReceiver);

        // Délier le service
        if (isBound) {
            unbindService(connection);
            isBound = false;
        }
    }

    private void getButtons(ViewGroup parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            if (child instanceof MaterialButton) {
                views.add((MaterialButton) child);
            } else if (child instanceof ViewGroup) {
                getButtons((ViewGroup) child);
            }
        }
    }
}
