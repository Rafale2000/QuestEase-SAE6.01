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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CreationCompte extends Theme {
    SharedPreferences sharedPreferences;
    private WebSocketService webSocketService;
    private boolean isBound = false;
    private String pseudoString;
    private String passwordString;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            WebSocketService.LocalBinder binder = (WebSocketService.LocalBinder) service;
            webSocketService = binder.getService();
            isBound = true;
            Log.d("SearchLobby", "Service connected");
            webSocketService.sendMessage("requestLobbies", "");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            SharedPreferences sharedPreferences = getSecurePreferences(context);
            Log.d("SearchLobby", "Broadcast received");
            if (intent.getAction().equals("WebSocketMessage")) {
                String jsonMessage = intent.getStringExtra("message");
                Log.d("SearchLobby", "Message reçu brut : " + jsonMessage);
                try {
                    JSONObject jsonObject = new JSONObject(jsonMessage);
                    String tag = jsonObject.getString("tag");

                    if ("CreateSucess".equals(tag)) {
                        sharedPreferences.edit().putBoolean("connected", true).apply();
                        sharedPreferences.edit().putString("username", pseudoString).apply();
                        sharedPreferences.edit().putString("password", passwordString).apply();
                        Intent intentNewActivity = new Intent(CreationCompte.this, Lobby.class);
                        startActivity(intentNewActivity);
                        onStop();
                    }

                    else{
                        TextView errorMessage = findViewById(R.id.errorMessage);
                        errorMessage.setText("Pseudo déja existant");
                    }
                } catch (Exception e) {
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = getSecurePreferences(this);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_creation_compte);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent serviceIntent = new Intent(this, WebSocketService.class);
        startService(serviceIntent);
        bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);
        EditText pseudo = findViewById(R.id.et_pseudo);
        EditText password = findViewById(R.id.et_password);
        Button button = findViewById(R.id.btn_login);
        button.setOnClickListener(view -> {
            pseudoString = pseudo.getText().toString();
            passwordString = password.getText().toString();
            passwordString = hashPassword(passwordString);
            List<String> logs = new ArrayList<>();
            logs.addAll(Arrays.asList(pseudoString, passwordString));
            webSocketService.sendMessage("createAccount",logs.toString());
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("SearchLobby", "onStop() called");
        if (isBound) {
            unbindService(connection);

            isBound = false;
        }
        try {
            unregisterReceiver(messageReceiver);
            Log.d("SearchLobby", "BroadcastReceiver unregistered");
        } catch (IllegalArgumentException e) {
            Log.e("SearchLobby", "BroadcastReceiver already unregistered", e);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent serviceIntent = new Intent(this, WebSocketService.class);
        startService(serviceIntent);
        bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);

        IntentFilter filter = new IntentFilter("WebSocketMessage");
        registerReceiver(messageReceiver, filter, Context.RECEIVER_EXPORTED);
        Log.d("Lobby", "lancement du BroadcastReceiver");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("SearchLobby", "onResume() called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("SearchLobby", "onPause() called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBound) {
            unbindService(connection);
            isBound = false;
        }
    }
}