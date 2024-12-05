package com.example.questease;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

public class TrouveLeSon2 extends Theme {
    private String rulestitle = "Règles du jeu";
    private String rulescontent = "Le jeu est séparé en deux parties :\n\n - Le premier joueur est l'auditeur, il doit appuyer sur un bouton pour entendre un son et le reconnaître.\n\n - Le rédacteur recevra des messages de l'auditeur et devra entrer dans son terminal de quel élément provient le son.";
    private ViewGroup main;
    private MediaPlayer mediaPlayer;
    private WebSocketService webSocketService;
    private SharedPreferences sharedPreferences;
    private boolean isBound = false;
    private Dialog tutorialDialog; // Référence au dialog
    private TextView cardTitle;    // Référence au titre
    private TextView cardContent;
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
                    if("TrouveLeSonMessage".equals(tag)){
                        LinearLayout messagesLayout = findViewById(R.id.messagesLayout);
                        if (messagesLayout != null) {
                            if(message != null){
                                View buttonTemplate = getLayoutInflater().inflate(R.layout.button_template, messagesLayout, false);
                                Button newButton = (Button) buttonTemplate;
                                newButton.setText(message);
                                messagesLayout.addView(newButton);
                            }
                        } else {
                            Log.e("TrouveLeSon", "messagesLayout introuvable !");
                        }
                    } else if ("successPopup".equals(tag)) {
                        ViewGroup viewGroup = findViewById(R.id.main);
                        mediaPlayer = MediaPlayer.create(TrouveLeSon2.this, R.raw.professor_layton_sucess);
                        mediaPlayer.start();
                        showTutorialPopup(
                                "Félicitations !",
                                "L'archéologue à bien trouvé la bonne orientation des plaques.\n\nVous allez passer au jeu suivant dans quelques secondes.",
                                viewGroup
                        );
                    }
                    else if ("startActivity".equals(tag)) {
                        Log.d("Lobby", "Message reçu pour startActivity : " + message);
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
        Log.d("TrouveLeSon2", "onCreate");
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trouve_le_son2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ViewGroup layout = findViewById(R.id.main);
        if(sharedPreferences.getBoolean("assistance_vocale", false)){
            lireTextViews(layout);
        }
        main = findViewById(R.id.main);
        showTutorialPopup(rulestitle, rulescontent, main);
        MaterialButton regles = findViewById(R.id.Regles);
        regles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTutorialPopup(rulestitle, rulescontent, main);
            }
        });

        Intent serviceIntent = new Intent(this, WebSocketService.class);
        startService(serviceIntent);
        bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);
        List<View> views = new ArrayList<>();
        MaterialButton button = findViewById(R.id.Regles);
        TextView messages = findViewById(R.id.messages);
        TextView role = findViewById(R.id.role);
        Button sendbutton = findViewById(R.id.sendButton);
        views.add(button);
        views.add(role);
        views.add(sendbutton);
        views.add(messages);
        sharedPreferences = getSharedPreferences("QuestEasePrefs", MODE_PRIVATE);
        ApplyParameters(sharedPreferences);
        if(sharedPreferences.getBoolean("tailleTexte",false)){
            adjustTextSize(views);
        }
        if(sharedPreferences.getBoolean("dyslexie",false)){
            applyFont(views);
        }
        IntentFilter filter = new IntentFilter("WebSocketMessage");
        registerReceiver(messageReceiver, filter, Context.RECEIVER_EXPORTED);
        Log.d("RotatingPictures2", "lancement du BroadcastReceiver");
        EditText editText = findViewById(R.id.messageEditText);
        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userInput = editText.getText().toString().trim().replaceAll("\\s+", " ").toLowerCase();
                if (userInput.equals("windows xp") || userInput.equals("windowsxp")) {
                    mediaPlayer = MediaPlayer.create(TrouveLeSon2.this, R.raw.professor_layton_sucess);
                    mediaPlayer.start();
                    showTutorialPopup("\nFélicitations","Le mot était bien Windows Xp,un système d'exploitation sorti en 2003\n\nVous allez bientot être redirigé vers le prochain jeu",main);
                    webSocketService.sendMessage("successPopup","Windows Xp");
                    int counter = 10; // Durée en secondes
                    new CountDownTimer(counter * 1000, 1000) {
                        int secondsRemaining = counter;
                        @Override
                        public void onTick(long millisUntilFinished) {
                            secondsRemaining--;
                        }
                        @Override
                        public void onFinish() {
                            webSocketService.sendMessage("startGame", "");
                            if (tutorialDialog != null) {
                                tutorialDialog.dismiss();
                            }
                        }
                    }.start();
                } else if (userInput.equals("")) {
                    Toast.makeText(webSocketService, "Le champ est vide", Toast.LENGTH_SHORT).show();
                } else {
                    mediaPlayer = MediaPlayer.create(TrouveLeSon2.this, R.raw.prof_layton_forbidden);
                    mediaPlayer.start();
                    Toast.makeText(TrouveLeSon2.this, "çe n'est pas la bonne réponse", Toast.LENGTH_SHORT).show();
                    webSocketService.sendMessage("showTip","");
                }
                webSocketService.sendMessage("TrouveLeSonMessage",messages.getText().toString());
            }
        });
        };
}


