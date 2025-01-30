package com.example.questease;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Sincere_Menteur extends Theme {

    private int id;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private SincereMenteurApi sincereMenteurApi;
    private WebSocketService webSocketService;
    private boolean isBound = false;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            WebSocketService.LocalBinder binder = (WebSocketService.LocalBinder) service;
            webSocketService = binder.getService();
            isBound = true;
            Log.d("SincereMenteur", "Service connected");
            webSocketService.sendMessage("getAllIDs", "obtient mon id");
            webSocketService.sendMessage("requestLobbies", "salut à tous c'est fanta de SM");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("WebSocketMessage")) {
                String jsonMessage = intent.getStringExtra("message");

                Log.d("SearchLobby", "Message reçu brut : " + jsonMessage);

                try {
                    // Analyser le message JSON
                    JSONObject jsonObject = new JSONObject(jsonMessage);
                    String tag = jsonObject.getString("tag");
                    String message = jsonObject.getString("message");

                    // Traiter le message uniquement si le tag est "getMyID"
                    switch (tag) {
                        case "getMyID":
                            // Traitement du message pour récupérer l'ID
                            id = Integer.parseInt(message);  // Si message contient l'ID
                            Log.d("SearchLobby", "Mon ID: " + id);
                            break;

                        case "getAllIDs":
                            // Analyser le message contenant une liste d'IDs
                            JSONArray idArray = new JSONArray(message);

                            // Vérification que la liste contient exactement 2 éléments
                            if (idArray.length() == 2) {
                                int firstID = idArray.getInt(0);
                                int secondID = idArray.getInt(1);

                                // Déterminer si l'ID du joueur est dans le premier ou le second élément
                                int id;
                                if (Sincere_Menteur.this.id == firstID) {
                                    id = 1;
                                } else if (Sincere_Menteur.this.id == secondID) {
                                    id = 2;
                                } else {
                                    id = -1; // Cas où l'ID du joueur n'est dans aucune des deux positions
                                }


                                // Afficher le résultat
                                Log.d("SearchLobby", "Position du joueur : " + id);
                            } else {
                                Log.e("SearchLobby", "La liste reçue ne contient pas exactement deux éléments !");
                            }
                            break;

                        case "startActivity":
                            Log.d("Lobby", "Message reçu pour startActivity : " + message);
                            Intent intentgame = identifyActivity(message);
                            startActivity(intentgame);
                            finish();


                            // Pas d'autres cas ici, vous pouvez en ajouter d'autres plus tard si nécessaire.
                        default:
                            Log.d("SearchLobby", "Message avec un tag inconnu : " + tag);
                            break;
                    }
                } catch (Exception e) {
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent serviceIntent = new Intent(this, WebSocketService.class);
        startService(serviceIntent);
        bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);

        IntentFilter filter = new IntentFilter("WebSocketMessage");
        registerReceiver(messageReceiver, filter, Context.RECEIVER_EXPORTED);
        Log.d("Lobby", "lancement du BroadcastReceiver");
        setContentView(R.layout.sincere_menteur);
        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        // Initialiser Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.56.1:8080")  // Remplace avec l'IP et le port du serveur
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        sincereMenteurApi = retrofit.create(SincereMenteurApi.class);

        if (id == 1) {
            partie2enigme();
        }

        Button buttonValider = findViewById(R.id.button5);
        buttonValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webSocketService.sendMessage("startGame", "au suivant !");


            }
        });
    }

    private void verifierReponses() {
        // Récupérer les choix des joueurs
        RadioGroup radioGroup1 = findViewById(R.id.radioGroup);
        int selectedId1 = radioGroup1.getCheckedRadioButtonId();

        RadioGroup radioGroup2 = findViewById(R.id.radioGroup2);
        int selectedId2 = radioGroup2.getCheckedRadioButtonId();

        boolean r1Sincere = (selectedId1 == R.id.radioButtonSincere);
        boolean r2Sincere = (selectedId2 == R.id.radioButtonSincere2);

        // Enregistrer les réponses dans la base de données locale
        dbHelper.addReponseSM(db, id, r1Sincere, r2Sincere);

        // Créer une Map pour envoyer les données
        Map<String, Object> data = new HashMap<>();
        data.put("id", id);
        data.put("reponse1", r1Sincere);
        data.put("reponse2", r2Sincere);

        // Envoyer les données au serveur
        envoyerReponses(data);
        webSocketService.sendMessage("messageTest", "ceci est un test, tuer moi");
    }

    private void envoyerReponses(Map<String, Object> data) {
        Call<Void> call = sincereMenteurApi.sendData(data);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(Sincere_Menteur.this, "Réponses envoyées avec succès", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Sincere_Menteur.this, "Erreur lors de l'envoi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(Sincere_Menteur.this, "Erreur de connexion au serveur", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void partie2enigme() {
        TextView tv1 = findViewById(R.id.textView4);
        TextView tv2 = findViewById(R.id.textView5);
        TextView tv3 = findViewById(R.id.textView6);
        TextView tv4 = findViewById(R.id.textView7);

        tv1.setText("Henri");
        tv2.setText("Jeanne");
        tv3.setText("Marie ment, mais je ne sais pas pour Jacque");
        tv4.setText("Jacque est sincère et Marie ment");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}






