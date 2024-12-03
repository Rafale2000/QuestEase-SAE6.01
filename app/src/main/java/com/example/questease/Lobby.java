package com.example.questease;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Lobby extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lobby);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            Intent serviceIntent = new Intent(this, WebSocketService.class);
            startService(serviceIntent);
            bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);

            IntentFilter filter = new IntentFilter("WebSocketMessage");
            registerReceiver(messageReceiver, filter, Context.RECEIVER_EXPORTED);
            Log.d("Lobby", "lancement du BroadcastReceiver");
            return insets;
        });
    }
}