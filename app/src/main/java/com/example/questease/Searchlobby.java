package com.example.questease;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.constraintlayout.widget.Group;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Searchlobby extends Theme {
    private static final Logger log = LoggerFactory.getLogger(Searchlobby.class);
    private List<View> views = new ArrayList<>();
    private WebSocketService webSocketService;
    private boolean isBound = false;
    private String nom;
    LinearLayout layoutLobbies;
    Dialog namePopupDialog;
    private String requestedLobby;
    private Boolean isCreated = false;
    private Boolean isErrorPopupVisible = false;

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
            Log.d("SearchLobby", "Broadcast received");
            if (intent.getAction().equals("WebSocketMessage")) {
                String jsonMessage = intent.getStringExtra("message");
                Log.d("SearchLobby", "Message reçu brut : " + jsonMessage);
                try {
                    JSONObject jsonObject = new JSONObject(jsonMessage);
                    String tag = jsonObject.getString("tag");
                    String message = jsonObject.getString("message");

                    if ("Lobbylist".equals(tag)) {
                        JSONArray messageArray = new JSONArray(message);
                        List<String> lobbies = new ArrayList<>();
                        for (int i = 0; i < messageArray.length(); i++) {
                            lobbies.add(messageArray.getString(i));
                        }
                        Log.d("SearchLobby", "Tag: " + tag);
                        Log.d("SearchLobby", "Lobbies: " + lobbies);
                        Log.d("info", "je vais esssayer d'afficher les lobby");
                        addLobbyButtons(lobbies);

                    } else if ("setnom".equals(tag)) {
                        if ("success".equalsIgnoreCase(message)) {
                            namePopupDialog.dismiss();
                            findViewById(R.id.main).setRenderEffect(null);

                            webSocketService.sendMessage("createLobby", nom);
                            Intent intentNewActivity = new Intent(Searchlobby.this, Lobby.class);
                            intentNewActivity.putExtra("p1", nom);
                            intentNewActivity.putExtra("lobbyName", nom);
                            startActivity(intentNewActivity);
                            onStop();
                        } else {
                            Toast.makeText(Searchlobby.this, "Nom déjà pris ou invalide.", Toast.LENGTH_SHORT).show();
                        }
                    } else if ("lobbyJoined".equals(tag)) {
                        Intent intentNewActivity = new Intent(Searchlobby.this, Lobby.class);
                        intentNewActivity.putExtra("p1", message);
                        intentNewActivity.putExtra("p2", nom);
                        webSocketService.sendMessage("setP2Name", nom);
                        intentNewActivity.putExtra("lobbyName", requestedLobby);
                        startActivity(intentNewActivity);
                        onStop();
                    } else if ("LobbyRejected".equals(tag)) {
                        Toast.makeText(Searchlobby.this, "Lobby plein ou inexistant.", Toast.LENGTH_SHORT).show();
                    } else if ("lobbyLeaved".equals(tag)) {
                        Toast.makeText(Searchlobby.this, "Vous avez quitté le lobby.", Toast.LENGTH_SHORT).show();
                        webSocketService.sendMessage("requestLobbies", "salut à tous c'est fanta j'ai delete le lobby");
                    }

                    if ("WebSocketError".equals(tag)) {
                        if (!isErrorPopupVisible) {
                            ViewGroup view = findViewById(R.id.main);
                            showServerErrorPopUp(view);
                            isErrorPopupVisible = true;
                        }
                    }
                } catch (Exception e) {
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("SearchLobby", "Nouvelle instance créée");

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        SharedPreferences sharedPreferences = getSecurePreferences(this);
        ApplyParameters(sharedPreferences);
        setContentView(R.layout.activity_searchlobby);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ViewGroup layout = findViewById(R.id.main);
        if (sharedPreferences.getBoolean("assistance_vocale", false)) {
            lireTextViews(layout);
        }
        MaterialButton creerLobby = findViewById(R.id.creerLobby);
        creerLobby.setOnClickListener(view -> {
            ViewGroup rootView = findViewById(R.id.main);
            shownamepopup(rootView, "createLobby", "");
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
        registerReceiver(messageReceiver, filter, Context.RECEIVER_EXPORTED);
        Log.d("SearchLobby", "lancement du BroadcastReceiver");

        namePopupDialog = new Dialog(this);
        namePopupDialog.setContentView(R.layout.popname);
        namePopupDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Log.d("oui", "je suis crée");
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

    private void addLobbyButtons(List<String> lobbies) {
        Log.d("lobbybutton", "je suis dans le addlobbybutton");
        this.layoutLobbies = findViewById(R.id.layoutlobbies);
        layoutLobbies.removeAllViews();
        for (String lobby : lobbies) {
            MaterialButton lobbyButton = (MaterialButton) LayoutInflater.from(this)
                    .inflate(R.layout.lobby_button, layoutLobbies, false);
            if (lobby != null) {
                lobbyButton.setText(lobby);
                lobbyButton.setOnClickListener(v -> {
                    ViewGroup rootView = findViewById(R.id.main);
                    this.requestedLobby = lobby;
                    shownamepopup(rootView, "joinLobby", lobby);
                });
                layoutLobbies.addView(lobbyButton);
            }
        }
    }


    public void shownamepopup(ViewGroup view, String action, String lobby) {
        RenderEffect blurEffect = RenderEffect.createBlurEffect(10, 10, Shader.TileMode.CLAMP);
        view.setRenderEffect(blurEffect);
        namePopupDialog.setCanceledOnTouchOutside(true);
        namePopupDialog.setOnCancelListener(dialog -> view.setRenderEffect(null));
        namePopupDialog.show();
        ViewGroup layout = findViewById(R.id.main);
        MaterialButton validatebutton = namePopupDialog.findViewById(R.id.validateButton);
        if (action.equals("createLobby")) {
            validatebutton.setOnClickListener(v -> {
                TextInputLayout nameInput = namePopupDialog.findViewById(R.id.textInputLayout);
                String name = nameInput.getEditText().getText().toString().trim();

                if (!name.isEmpty()) {
                    JSONObject jsonRequest = new JSONObject();
                    try {
                        jsonRequest.put("tag", "setnom");
                        jsonRequest.put("name", name);
                        webSocketService.sendMessage("setnom", name);
                        this.nom = name;
                    } catch (Exception e) {
                    }
                } else {
                    Toast.makeText(Searchlobby.this, "Veuillez entrer un nom valide.", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (action.equals("joinLobby")) {
            validatebutton.setOnClickListener(v -> {
                TextInputLayout nameInput = namePopupDialog.findViewById(R.id.textInputLayout);
                String name = nameInput.getEditText().getText().toString().trim();
                if (!name.isEmpty()) {
                    JSONObject jsonRequest = new JSONObject();
                    try {
                        jsonRequest.put("tag", "joinLobby");
                        jsonRequest.put("name", name);
                        webSocketService.sendMessage("joinLobby", lobby);
                        this.nom = name;
                    } catch (Exception e) {
                    }
                } else {
                    Toast.makeText(Searchlobby.this, "Veuillez entrer un nom valide.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
