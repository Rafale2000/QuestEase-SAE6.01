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
import android.os.IBinder;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
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
import java.util.Objects;
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
    private Boolean isErrorPopupVisible = false;
    private boolean connected= false;


    private static final String SEARCH_LOBBY_STR = "SearchLobby";
    private static final String CREATE_LOBBY_STR = "createLobby";
    private static final String WEB_SOCKET_MSG_STR = "WebSocketMessage";
    private static final String SET_NOM_STR = "setnom";
    private static final String JOIN_LOBBY_STR = "joinLobby";



    private ServiceConnection connection = new ServiceConnection() {

        /**
         * est utilisé quand le web socket est connecté au serveur
         * @param name nom du web socket
         * @param service utilisé par le web socket
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            WebSocketService.LocalBinder binder = (WebSocketService.LocalBinder) service;
            webSocketService = binder.getService();
            isBound = true;
            Log.d(SEARCH_LOBBY_STR, "Service connected");
            webSocketService.sendMessage("requestLobbies", "");
        }

        /**
         * est utilisé quand le web socket est déconnecté
         * @param name nom du component
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {

        /**
         * est utilisé quand le web socket recoit quelque chose
         * @param context page sur la quel le web socket est
         * @param intent truc au pif
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(SEARCH_LOBBY_STR, "Broadcast received");
            if (Objects.equals(intent.getAction(), WEB_SOCKET_MSG_STR)) {
                String jsonMessage = intent.getStringExtra("message");
                Log.d(SEARCH_LOBBY_STR, "Message reçu brut : " + jsonMessage);
                try {
                    assert jsonMessage != null;
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
                        if(sharedPreferences.getBoolean("connected",false)){
                            List <String> logs = new ArrayList<>();
                            logs.add(sharedPreferences.getString("username",""));
                            logs.add(sharedPreferences.getString("password",""));
                            webSocketService.sendMessage("connectAccount",logs.toString());
                        }
                    }
                    else if ("ConnectionSuccess".equals(tag)) {
                        ImageView connexion = findViewById(R.id.connexion);
                        TextView username = findViewById(R.id.username);
                        String pseudo = sharedPreferences.getString("username", "0");
                        connexion.setVisibility(View.GONE);
                        username.setText(pseudo);
                        username.setVisibility(View.VISIBLE);
                        Toast.makeText(context, "Connecté en tant que "+pseudo, Toast.LENGTH_SHORT).show();
                        sharedPreferences.edit().putBoolean("connected",true).apply();
                    }
                    else if("ConnectionError".equals(tag)){
                        Toast.makeText(context, "Erreur de connexion au compte", Toast.LENGTH_SHORT).show();
                        sharedPreferences.edit().putBoolean("connected",false).apply();
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
                        }

                        else {
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
                    e.printStackTrace();
                }
            }
        }
    };

    /**
     * créer les élements de la page quand la page est lancé
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(SEARCH_LOBBY_STR, "Nouvelle instance créée");

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
            shownamepopup(rootView, CREATE_LOBBY_STR, "");
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

        IntentFilter filter = new IntentFilter(WEB_SOCKET_MSG_STR);
        registerReceiver(messageReceiver, filter, Context.RECEIVER_EXPORTED);
        Log.d(SEARCH_LOBBY_STR, "lancement du BroadcastReceiver");

        namePopupDialog = new Dialog(this);
        namePopupDialog.setContentView(R.layout.popname);
        Objects.requireNonNull(namePopupDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Log.d("oui", "je suis crée");
        namePopupDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
         if(sharedPreferences.getBoolean("connected",false)){
             this.connected = true;
         }
         ImageView connexion  = findViewById(R.id.connexion);
         connexion.setOnClickListener(view -> {
            Intent intent = new Intent(Searchlobby.this, Connexion.class);
            startActivity(intent);
            try {
                unregisterReceiver(messageReceiver);
                Log.d("SearchLobby", "BroadcastReceiver unregistered");
            } catch (IllegalArgumentException e) {
                Log.e("SearchLobby", "BroadcastReceiver already unregistered", e);
            }
        });
        TextView username = findViewById(R.id.username);
        username.setOnClickListener(view -> {
            Intent intent = new Intent(Searchlobby.this, Profil.class);
            startActivity(intent);
            try {
                unregisterReceiver(messageReceiver);
                Log.d("MainActivity", "BroadcastReceiver unregistered");
            } catch (IllegalArgumentException e) {
                Log.e("MainActivity", "BroadcastReceiver already unregistered", e);
            }
        });
    }

    /**
     * est utilisé quand le web socket se stop
     */
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(SEARCH_LOBBY_STR, "onStop() called");
        if (isBound) {
            unbindService(connection);

            isBound = false;
        }
        try {
            unregisterReceiver(messageReceiver);
            Log.d(SEARCH_LOBBY_STR, "BroadcastReceiver unregistered");
        } catch (IllegalArgumentException e) {
            Log.e(SEARCH_LOBBY_STR, "BroadcastReceiver already unregistered", e);
        }
    }

    /**
     * quand el web socket redémare
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        Intent serviceIntent = new Intent(this, WebSocketService.class);
        startService(serviceIntent);
        bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);

        IntentFilter filter = new IntentFilter(WEB_SOCKET_MSG_STR);
        registerReceiver(messageReceiver, filter, Context.RECEIVER_EXPORTED);
        Log.d("Lobby", "lancement du BroadcastReceiver");
    }

    /**
     *
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("SearchLobby", "onResume() called");
    }

    /**
     * quand le websocket se pose
     */
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(SEARCH_LOBBY_STR, "onPause() called");
    }

    /**
     * quand le websocket se détruit
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBound) {
            unbindService(connection);
            isBound = false;
        }
    }

    /**
     * renvoie les bouttons de la page
     * @param parent page de l'application android
     */
    private void getButtons(ViewGroup parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            if (child instanceof MaterialButton) {
                views.add(child);
            } else if (child instanceof ViewGroup) {
                getButtons((ViewGroup) child);
            }
        }
    }

    /**
     * ajoute des boutons au loby aka serveur
     * @param lobbies les lobbies qui existent déjà
     */
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
                    shownamepopup(rootView, JOIN_LOBBY_STR, lobby);
                });

                TypedValue typedValue = new TypedValue();
                android.content.res.Resources.Theme theme = getTheme();
                int attrColor;
                switch (status) {
                    case 1:
                        attrColor = R.attr.colorButtonBackground;
                        break;
                    case 2:
                        attrColor = R.attr.difficulty2;
                        break;
                    default:
                        attrColor = R.attr.exitButton;
                        break;
                }
            }
        }


        public void shownamepopup(ViewGroup view, String action, String lobby) {
        SharedPreferences sharedPreferences = getSecurePreferences(this);
        String username = sharedPreferences.getString("username", "");
        RenderEffect blurEffect = RenderEffect.createBlurEffect(10, 10, Shader.TileMode.CLAMP);
        view.setRenderEffect(blurEffect);

        namePopupDialog.setCanceledOnTouchOutside(true);
        namePopupDialog.setOnCancelListener(dialog -> view.setRenderEffect(null));
        namePopupDialog.show();

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
                    }
                    catch (Exception e) {
                        Log.e("shownamepopup", "Erreur JSON : " + e.getMessage());
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
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(Searchlobby.this, "Veuillez entrer un nom valide.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


}
