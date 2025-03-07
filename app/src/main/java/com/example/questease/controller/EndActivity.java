package com.example.questease.controller;

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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.questease.Profil;
import com.example.questease.R;
import com.example.questease.Theme;
import com.example.questease.WebSocketService;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import android.content.SharedPreferences;

public class EndActivity extends Theme {
    MediaPlayer mediaPlayer = new MediaPlayer();
    private WebSocketService webSocketService;
    private boolean isBound = false;
    private boolean isCreated = false;
    private boolean isErrorPopupVisible = false;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            WebSocketService.LocalBinder binder = (WebSocketService.LocalBinder) service;
            webSocketService = binder.getService();
            isBound = true;
            // Exemple : Envoyer un message une fois connecté
            if (webSocketService != null) {
                Log.e("test", "Envoi d'un message via WebSocketService");
                String username = getSecurePreferences(EndActivity.this).getString("username", "");
                if(webSocketService != null){
                    webSocketService.sendMessage("updateStats",""+username);
                }

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
            Log.d("MainActivity", "Broadcast received");
            if (intent.getAction().equals("WebSocketMessage")) {
                String jsonMessage = intent.getStringExtra("message");
                Log.d("MainActivity", "Message reçu brut : " + jsonMessage);
                SharedPreferences sharedPreferences = getSecurePreferences(context);
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
                    }
                    else if("updateStatsSuccess".equals(tag)){
                        Toast.makeText(context, "Stats mises à jour", Toast.LENGTH_SHORT).show();
                    }
                    else if("updateStatsError".equals(tag)){
                        Toast.makeText(context, "Erreur lors de la mise à jour des stats", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mediaPlayer = MediaPlayer.create(EndActivity.this, R.raw.yoshi_win_sound);
        mediaPlayer.start();
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSecurePreferences(this);
        setContentView(R.layout.activity_end);
        ApplyParameters(sharedPreferences);
        setContentView(R.layout.activity_profil);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent serviceIntent = new Intent(this, WebSocketService.class);
        startService(serviceIntent);
        bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);

        // Log pour vérifier quand le receiver est enregistré
        Log.d("EndActivity", "Enregistrement du BroadcastReceiver");
        IntentFilter filter = new IntentFilter("WebSocketMessage");
        registerReceiver(messageReceiver, filter, Context.RECEIVER_EXPORTED);
        if (sharedPreferences.getBoolean("assistance_vocale", false)) {
            Log.d("EndActivity", "Lancement de lireTextViews");
            //TODO lireTextViews(layout);
        }
        isCreated = true;

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
