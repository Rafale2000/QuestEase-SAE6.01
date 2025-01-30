package com.example.questease;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TrouveLeSon extends Theme {
    private MediaPlayer mediaPlayer;
    private String rulestitle = "Règles du jeu";
    private String rulescontent = "Le jeu est séparé en deux parties :\n\n - Le premier joueur est l'auditeur, il doit appuyer sur un bouton pour entendre un son et le reconnaître.\n \n - Le rédacteur recevra des messages de l'auditeur et devra entrer dans son terminal de quel élément provient le son.";
    private ViewGroup main;
    private WebSocketService webSocketService;
    private SharedPreferences sharedPreferences;
    private boolean isBound = false;
    private String indice1 = "Système d'exloitation";
    private String indice2 = "Sorti en 2003";
    private String indice3 = "Version de windows sortie entre Windows2000 et WindowsVista";
    private int indicestatus = 1;
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
            Log.d("RotatingPictures", "Broadcast received");
            if (intent.getAction().equals("WebSocketMessage")) {
                String jsonMessage = intent.getStringExtra("message");
                Log.d("RotatingPictures.java", "Message reçu brut : " + jsonMessage);
                try {
                    JSONObject jsonObject = new JSONObject(jsonMessage);
                    String tag = jsonObject.getString("tag");
                    String message = jsonObject.getString("message");
                    if("showTip".equals(tag)){
                        TextView indice1view = findViewById(R.id.indice1);
                        TextView indice2view = findViewById(R.id.indice2);
                        TextView indice3view = findViewById(R.id.indice3);
                        Toast.makeText(context, "le j2 a eu faux, voici un indice", Toast.LENGTH_SHORT).show();
                        if(indicestatus == 1){
                            indice1view.setText("Indice 1:"+indice1);
                        }
                        else if(indicestatus == 2){
                            indice2view.setText("Indice 2:"+indice2);
                        }
                        else if(indicestatus == 3){
                            indice3view.setText("Indice 3:"+indice3);

                        }
                        indicestatus ++;
                    } else if ("successPopup".equals(tag)) {
                        mediaPlayer = MediaPlayer.create(TrouveLeSon.this, R.raw.professor_layton_sucess);
                        mediaPlayer.start();
                        showTutorialPopup("\nFélicitations","Le mot était bien Windows Xp,un système d'exploitation sorti en 2003\n\nVous allez bientot être redirigé vers le prochain jeu",main);
                    }
                    else if ("startActivity".equals(tag)) {
                        Log.d("Lobby", "Message reçu pour startActivity : " + message);
                        Intent intentgame = identifyActivity(message);
                        startActivity(intentgame);
                        finish();
                    }
                } catch (Exception e) {
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trouve_le_son);
        MaterialButton regles = findViewById(R.id.Regles);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        main = findViewById(R.id.main);
        showTutorialPopup(rulestitle, rulescontent, main);
        Intent serviceIntent = new Intent(this, WebSocketService.class);
        startService(serviceIntent);
        bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);
        List<View> views = new ArrayList<>();
        MaterialButton button = findViewById(R.id.Regles);
        TextView role = findViewById(R.id.card_title);
        TextView indice1 = findViewById(R.id.indice1);
        TextView indice2 = findViewById(R.id.indice2);
        TextView indice3 = findViewById(R.id.indice3);
        Button sendbutton = findViewById(R.id.sendButton);
        views.add(button);
        views.add(role);
        views.add(indice1);
        views.add(indice2);
        views.add(indice3);
        views.add(sendbutton);
        SharedPreferences sharedPreferences = getSecurePreferences(this);
        ApplyParameters(sharedPreferences);
        if (sharedPreferences.getBoolean("tailleTexte", false)) {
            adjustTextSize(views);
        }
        if (sharedPreferences.getBoolean("dyslexie", false)) {
            applyFont(views);
        }
        IntentFilter filter = new IntentFilter("WebSocketMessage");
        registerReceiver(messageReceiver, filter, Context.RECEIVER_EXPORTED);
        Log.d("RotatingPictures2", "lancement du BroadcastReceiver");
        regles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTutorialPopup(rulestitle, rulescontent, main);
            }
        });
        mediaPlayer = MediaPlayer.create(this, R.raw.windows_xp_startup);
        MaterialButton playButton = findViewById(R.id.playbutton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.start();
            }
        });
        EditText editText = findViewById(R.id.messageEditText);
        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webSocketService.sendMessage("TrouveLeSonMessage", editText.getText().toString());
            }
        });
        ImageView quitter = findViewById(R.id.quitter);
        quitter.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBound) {
            unbindService(connection);
            isBound = false;
            try {
                unregisterReceiver(messageReceiver);
                Log.d("RotatingPictures2", "BroadcastReceiver unregistered");
            } catch (IllegalArgumentException e) {
                Log.e("RotatingPictures2", "BroadcastReceiver already unregistered", e);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            unregisterReceiver(messageReceiver);
            Log.d("RotatingPictures2", "BroadcastReceiver unregistered");
        } catch (IllegalArgumentException e) {
            Log.e("RotatingPictures2", "BroadcastReceiver already unregistered", e);
        }
    }

}