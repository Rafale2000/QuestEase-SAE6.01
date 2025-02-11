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

public class Connexion extends Theme {
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

                    if ("ConnectionSuccess".equals(tag)) {
                        sharedPreferences.edit().putBoolean("connected", true).apply();
                        sharedPreferences.edit().putString("username", pseudoString).apply();
                        sharedPreferences.edit().putString("password", passwordString).apply();
                        Intent newintent = new Intent(Connexion.this, MainActivity.class);
                        startActivity(newintent);
                        try {
                            unregisterReceiver(messageReceiver);
                            Log.d("MainActivity", "BroadcastReceiver unregistered");
                        } catch (IllegalArgumentException e) {
                            Log.e("MainActivity", "BroadcastReceiver already unregistered", e);
                        }
                    }
                    else if("ConnectionError".equals(tag)){
                        TextView errorMessage = findViewById(R.id.errorMessage);
                        errorMessage.setText("Login ou mot de passe incorect");
                        errorMessage.setVisibility(TextView.VISIBLE);
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
        setContentView(R.layout.activity_connexion);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent serviceIntent = new Intent(this, WebSocketService.class);
        startService(serviceIntent);
        bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);
        IntentFilter filter = new IntentFilter("WebSocketMessage");
        registerReceiver(messageReceiver, filter, Context.RECEIVER_EXPORTED);
        EditText pseudo = findViewById(R.id.et_pseudo);
        EditText password = findViewById(R.id.et_password);
        Button button = findViewById(R.id.btn_login);
        button.setOnClickListener(view -> {
            pseudoString = pseudo.getText().toString();
            passwordString = password.getText().toString();
            passwordString = hashPassword(passwordString);
            List<String> logs = new ArrayList<>();
            logs.addAll(Arrays.asList(pseudoString, passwordString));
            webSocketService.sendMessage("connectAccount",logs.toString());
        });
        TextView signup = findViewById(R.id.tv_signup);
        signup.setOnClickListener(view -> {
            Intent intent = new Intent(Connexion.this, CreationCompte.class);
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