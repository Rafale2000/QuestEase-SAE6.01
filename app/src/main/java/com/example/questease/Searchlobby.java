package com.example.questease;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.util.TypedValue;
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
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Searchlobby extends Theme {
    private static final Logger log = LoggerFactory.getLogger(Searchlobby.class);
    private List<View> views = new ArrayList<>();
    private static final String DIFFICULTY = "difficulty";
    private WebSocketService webSocketService;
    private SharedPreferences sharedPreferences;
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
            SharedPreferences sharedPreferences = getSecurePreferences(context);
            Log.d("SearchLobby", "Broadcast received");
            if (intent.getAction().equals("WebSocketMessage")) {
                String jsonMessage = intent.getStringExtra("message");
                Log.d("SearchLobby", "Message reçu brut : " + jsonMessage);
                try {
                    JSONObject jsonObject = new JSONObject(jsonMessage);
                    String tag = jsonObject.getString("tag");
                    String message = jsonObject.getString("message");
                    if ("Lobbylist".equals(tag)) {
                        Log.d("SearchLobby", "Tag: " + tag);
                        Log.d("SearchLobby", "Message: " + message);
                        JSONObject messageObject = new JSONObject(message);
                        Map<String, Integer> lobbies = new HashMap<>();
                        Iterator<String> keys = messageObject.keys();
                        while (keys.hasNext()) {
                            String key = keys.next();
                            int value = messageObject.getInt(key);
                            lobbies.put(key, value);
                        }
                        Log.d("SearchLobby", "Lobbies: " + lobbies);
                        Log.d("info", "je vais essayer d'afficher les lobby");
                        addLobbyButtons(lobbies);
                    }
                    else if ("setnom".equals(tag)) {
                        if ("success".equalsIgnoreCase(message)) {
                            namePopupDialog.dismiss();
                            findViewById(R.id.main).setRenderEffect(null);
                            Log.d("SearchLobby", "je vais essayer récupérer la difficulty");

                            int difficulty  = sharedPreferences.getInt(DIFFICULTY,0);
                            Log.d("SearchLobby", "difficulty: " + difficulty);
                            Log.d("SearchLobby", "je vais envoyer un createlobby");
                            webSocketService.sendCreateLobby("createLobby", nom,difficulty);
                            Log.d("SearchLobby", "normalement c'est crée");
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

    private void addLobbyButtons(Map<String, Integer> lobbies) {
        Log.d("lobbybutton", "je suis dans le addlobbybutton");
        this.layoutLobbies = findViewById(R.id.layoutlobbies);
        layoutLobbies.removeAllViews();

        for (Map.Entry<String, Integer> entry : lobbies.entrySet()) {
            String lobby = entry.getKey();
            int status = entry.getValue(); // Valeur associée au lobby

            // Inflater le layout du bouton (FrameLayout contenant le bouton + badge)
            View lobbyView = LayoutInflater.from(this)
                    .inflate(R.layout.lobby_button, layoutLobbies, false);

            // Récupérer le bouton et le badge
            MaterialButton lobbyButton = lobbyView.findViewById(R.id.myButton);
            TextView badgeText = lobbyView.findViewById(R.id.badgeNumber);

            if (lobby != null) {
                lobbyButton.setText(lobby);
                lobbyButton.setOnClickListener(v -> {
                    ViewGroup rootView = findViewById(R.id.main);
                    this.requestedLobby = lobby;
                    shownamepopup(rootView, "joinLobby", lobby);
                });

                TypedValue typedValue = new TypedValue();
                android.content.res.Resources.Theme theme = getTheme();
                int attrColor;
                switch (status) {
                    case 1:
                        attrColor = R.attr.colorButtonBackground; // Lobby disponible
                        break;
                    case 2:
                        attrColor = R.attr.difficulty2; // Autre statut
                        break;
                    default:
                        attrColor = R.attr.exitButton; // Couleur par défaut
                        break;
                }

                theme.resolveAttribute(attrColor, typedValue, true);
                int color = typedValue.data;
                lobbyButton.setBackgroundTintList(ColorStateList.valueOf(color));

                badgeText.setText(String.valueOf(status));

                if (status == 0) {
                    badgeText.setVisibility(View.GONE);
                } else {
                    badgeText.setVisibility(View.VISIBLE);
                }

                layoutLobbies.addView(lobbyView);
            }
        }
    }
    public void shownamepopup(ViewGroup view, String action, String lobby) {
        SharedPreferences sharedPreferences = getSecurePreferences(this);

        RenderEffect blurEffect = RenderEffect.createBlurEffect(10, 10, Shader.TileMode.CLAMP);
        view.setRenderEffect(blurEffect);

        namePopupDialog.setCanceledOnTouchOutside(true);
        namePopupDialog.setOnCancelListener(dialog -> view.setRenderEffect(null));
        namePopupDialog.show();

        View dialogView = namePopupDialog.findViewById(android.R.id.content);
        if (dialogView == null) {
            Log.e("shownamepopup", "Erreur : la vue du Dialog est null !");
            return;
        }

        MaterialButton difficultyButton1 = dialogView.findViewById(R.id.difficultyButton1);
        MaterialButton difficultyButton2 = dialogView.findViewById(R.id.difficultyButton2);
        MaterialButton difficultyButton3 = dialogView.findViewById(R.id.difficultyButton3);

        if (difficultyButton1 == null || difficultyButton2 == null || difficultyButton3 == null) {
            Log.e("shownamepopup", "Erreur : impossible de récupérer les boutons de difficulté !");
            return;
        }
        if (action.equals("joinLobby")){
            difficultyButton1.setVisibility(View.GONE);
            difficultyButton2.setVisibility(View.GONE);
            difficultyButton3.setVisibility(View.GONE);
            MaterialTextView difficulty = dialogView.findViewById(R.id.difficultyText);
            difficulty.setVisibility(View.GONE);
        }
        difficultyButton1.setOnClickListener(v -> {
            sharedPreferences.edit().putInt(DIFFICULTY,1).apply();
            difficultyButton1.setAlpha(1.0f);
            difficultyButton2.setAlpha(0.3f);
            difficultyButton3.setAlpha(0.3f);
            Toast toast = Toast.makeText(this, "Difficulté sélectionnée : Facile", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 0, 100);
            toast.show();
        });

        difficultyButton2.setOnClickListener(v -> {
            Toast toast = Toast.makeText(this, "Difficulté sélectionnée : Moyenne", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 0, 100);
            toast.show();
            sharedPreferences.edit().putInt(DIFFICULTY,2).apply();
            difficultyButton1.setAlpha(0.3f);
            difficultyButton2.setAlpha(1.0f);
            difficultyButton3.setAlpha(0.3f);
        });

        difficultyButton3.setOnClickListener(v -> {
            Toast toast = Toast.makeText(this, "Difficulté sélectionnée : Difficile", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 0, 100);
            toast.show();
            sharedPreferences.edit().putInt(DIFFICULTY,3).apply();
            difficultyButton1.setAlpha(0.3f);
            difficultyButton2.setAlpha(0.3f);
            difficultyButton3.setAlpha(1.0f);
        });

        int difficulty = sharedPreferences.getInt(DIFFICULTY, 0);
        if (difficulty == 1 || difficulty == 0) {
            difficultyButton1.performClick();
        } else if (difficulty == 2) {
            difficultyButton2.performClick();
        } else if (difficulty == 3) {
            difficultyButton3.performClick();
        }

        MaterialButton validateButton = dialogView.findViewById(R.id.validateButton);
        if (validateButton == null) {
            Log.e("shownamepopup", "Erreur : validateButton est null !");
            return;
        }

        validateButton.setOnClickListener(v -> {
            TextInputLayout nameInput = dialogView.findViewById(R.id.textInputLayout);
            if (nameInput == null) {
                Log.e("shownamepopup", "Erreur : textInputLayout est null !");
                return;
            }

            String name = nameInput.getEditText().getText().toString().trim();
            if (name.isEmpty()) {
                Toast.makeText(Searchlobby.this, "Veuillez entrer un nom valide.", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                JSONObject jsonRequest = new JSONObject();
                if ("createLobby".equals(action)) {
                    jsonRequest.put("tag", "setnom");
                    jsonRequest.put("name", name);
                    webSocketService.sendMessage("setnom", name);
                    this.nom = name;
                } else if ("joinLobby".equals(action)) {
                    jsonRequest.put("tag", "joinLobby");
                    jsonRequest.put("name", name);
                    webSocketService.sendMessage("joinLobby", lobby);
                    this.nom = name;
                }
            } catch (Exception e) {
                Log.e("shownamepopup", "Erreur JSON : " + e.getMessage());
            }
        });
    }


}
