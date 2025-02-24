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
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Profil extends Theme {
    private WebSocketService webSocketService;
    private boolean isBound = false;
    private boolean isCreated = false;
    private boolean isErrorPopupVisible = false;
    private String pseudo;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            WebSocketService.LocalBinder binder = (WebSocketService.LocalBinder) service;
            webSocketService = binder.getService();
            isBound = true;
            // Exemple : Envoyer un message une fois connecté
            if (webSocketService != null) {
                Log.e("test", "Envoi d'un message via WebSocketService");
                String username = getSecurePreferences(Profil.this).getString("username", "");
                webSocketService.sendMessage("getStats",""+username);
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
                            showServerErrorPopUp(view,sharedPreferences);
                            isErrorPopupVisible = true;
                        }
                    } else if ("ConnectionSuccess".equals(tag)) {
                        ImageView connexion = findViewById(R.id.connexion);
                        TextView username = findViewById(R.id.username);
                        pseudo = sharedPreferences.getString("username", "0");
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
                    else if("Lobbylist".equals(tag)){
                        if(sharedPreferences.getBoolean("connected",false)){
                            List<String> logs = new ArrayList<>();
                            logs.add(sharedPreferences.getString("username",""));
                            logs.add(sharedPreferences.getString("password",""));
                            webSocketService.sendMessage("connectAccount",logs.toString());
                        }
                    }
                    else if("getStatsSuccess".equals(tag)){
                        TextView playedGames = findViewById(R.id.playedGames);
                        TextView wonGames = findViewById(R.id.wonGames);
                        List<Integer> list = Arrays.stream(message.replaceAll("[\\[\\] ]", "").split(","))
                                .map(Integer::parseInt)
                                .collect(Collectors.toList());

                        playedGames.append(" "+list.get(0));
                        wonGames.append(" "+list.get(1));
                    }
                    else if("getStatsError".equals(tag)){
                        Toast.makeText(context, "Erreur lors de la récupération des stats", Toast.LENGTH_SHORT).show();
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
        SharedPreferences sharedPreferences = getSecurePreferences(this);
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
        Log.d("Profil", "Enregistrement du BroadcastReceiver");
        IntentFilter filter = new IntentFilter("WebSocketMessage");
        registerReceiver(messageReceiver, filter, Context.RECEIVER_EXPORTED);
        if (sharedPreferences.getBoolean("assistance_vocale", false)) {
            Log.d("Profil", "Lancement de lireTextViews");
            //TODO lireTextViews(layout);
        }

        isCreated = true;
        TextView username = findViewById(R.id.username);

        username.setText(sharedPreferences.getString("username",""));

        Button disconnectButton = findViewById(R.id.disconnectButton);

        disconnectButton.setOnClickListener(View -> {
            sharedPreferences.edit().putBoolean("connected",false).apply();
            sharedPreferences.edit().putString("username","").apply();
            sharedPreferences.edit().putString("password","").apply();
            Intent intent = new Intent(Profil.this, MainActivity.class);
            startActivity(intent);
            try {
                unregisterReceiver(messageReceiver);
                Log.d("MainActivity", "BroadcastReceiver unregistered");
            } catch (IllegalArgumentException e) {
                Log.e("MainActivity", "BroadcastReceiver already unregistered", e);
            }
        });
        Button deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(View -> {
            showServerErrorPopUp(this.findViewById(R.id.main),sharedPreferences);
        });

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
    @Override
    public void showTutorialPopup(String title, String content, ViewGroup view) {
        super.showTutorialPopup(title, content, view);
    }


    public void showServerErrorPopUp(ViewGroup view,SharedPreferences sharedPreferences) {
        RenderEffect blurEffect = RenderEffect.createBlurEffect(10, 10, Shader.TileMode.CLAMP);
        view.setRenderEffect(blurEffect);
        Dialog errorDialog = new Dialog(this);
        errorDialog.setContentView(R.layout.pop_up_error);
        TextView content = errorDialog.findViewById(R.id.cardContent);
        TextView titre = errorDialog.findViewById(R.id.cardTitle);
        MaterialButton closeButton = errorDialog.findViewById(R.id.closeButton);
        titre.setText("Supprimer le compte ?");
        content.setText("Voulez-vous vraiment supprimer votre compte ? \n Cette action est irréversible !");
        closeButton.setText("Confirmer");
        errorDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        errorDialog.setCanceledOnTouchOutside(true);
        errorDialog.setOnCancelListener(dialog -> view.setRenderEffect(null));

        closeButton.setOnClickListener(v -> {
            errorDialog.dismiss();
            view.setRenderEffect(null);
            webSocketService.sendMessage("deleteAccount",""+sharedPreferences.getString("username",""));
            Log.d("Profil", "Suppression du compte");
        });
        errorDialog.show();
    }
}