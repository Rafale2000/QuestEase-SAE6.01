package com.example.questease;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.questease.Searchlobby;
import com.example.questease.Theme;
import com.example.questease.WebSocketService;
import com.example.questease.Parametres;

import android.content.SharedPreferences;
import android.widget.TextView;

import org.java_websocket.client.WebSocketClient;

import java.util.List;

public class MainActivity extends Theme {
    private WebSocketService webSocketService;
    private boolean isBound = false;
    private boolean isCreated = false;

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

        // Configurer les boutons
        Button jouer = findViewById(R.id.Jouer);
        Button parametres = findViewById(R.id.Parametres);
        TextView banner = findViewById(R.id.banner);

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
        });

        parametres.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, Parametres.class);
            startActivity(intent);
        });

        // Lancer et lier le service WebSocket
        Intent serviceIntent = new Intent(this, WebSocketService.class);
        startService(serviceIntent);
        bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);
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
