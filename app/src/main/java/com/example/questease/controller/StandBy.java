package com.example.questease.controller;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.questease.R;
import com.example.questease.Theme;
import com.example.questease.WebSocketService;

import org.json.JSONObject;

public class StandBy extends Theme {
    private WebSocketService webSocketService;
    private boolean isBound = false;
    private boolean isCreated = false;
    private Dialog tutorialDialog; // Référence au dialog
    private TextView cardTitle;    // Référence au titre
    private TextView cardContent;
    private boolean isErrorPopupVisible = false;
    private MediaPlayer mediaPlayer;

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
    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("StandBy", "Broadcast received");
            if (intent.getAction().equals("WebSocketMessage")) {
                String jsonMessage = intent.getStringExtra("message");
                Log.d("StandBy", "Message reçu brut : " + jsonMessage);
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
                    } else if ("successPopup".equals(tag)) {
                        ViewGroup viewGroup = findViewById(R.id.main);
                        mediaPlayer = MediaPlayer.create(StandBy.this, R.raw.professor_layton_sucess);
                        mediaPlayer.start();
                        showTutorialPopup(
                                "Félicitations !",
                                "Félicitation vous avez dévérouillé le coffre.\n\nVous allez passer voir son contenu dans quelques secondes",
                                viewGroup
                        );
                    } else if ("startActivity".equals(tag)) {
                        Log.d("StandBy", "Message reçu pour startActivity : " + message);
                        Intent intentgame = identifyActivity(message);
                        startActivity(intentgame);
                        finish();
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
        setContentView(R.layout.standby);
        Log.d("StandBy", "Nouvelle instance créée");
        isCreated = true;

        Intent serviceIntent = new Intent(this, WebSocketService.class);
        startService(serviceIntent);
        bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);

        IntentFilter filter = new IntentFilter("WebSocketMessage");
        registerReceiver(messageReceiver, filter, Context.RECEIVER_EXPORTED);
        Log.d("RotatingPictures", "lancement du BroadcastReceiver");
    }
}