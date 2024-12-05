package com.example.questease.Controller;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.questease.R;

import androidx.appcompat.app.AppCompatActivity;

import com.example.questease.R;
import com.example.questease.RotatingPictures;
import com.example.questease.RotatingPictures2;
import com.example.questease.Theme;
import com.example.questease.WebSocketService;

import android.media.MediaPlayer;

import org.json.JSONObject;

import java.util.HashSet;

public class GyroscopeActivity extends Theme implements SensorEventListener {
    private WebSocketService webSocketService;
    private SensorManager sensorManager;
    private Sensor rotationVectorSensor;
    private boolean isBound = false;
    private boolean isCreated = false;
    private ImageView lock, solidLock;
    private MediaPlayer mediaPlayer;
    private Button closeButton, rulesButton;
    private Dialog tutorialDialog; // Référence au dialog
    private TextView cardTitle;    // Référence au titre
    private TextView cardContent;  // Référence au contenu

    private boolean isErrorPopupVisible = false;
    private String rulestitle = "\n\n\"Règles du jeu\"";
    private String rulescontent = "\"Tournez votre téléphone pour aligner l'aiguille\n\n sur les bons angles : 45°, 180°, et 300°.\n\n \" +\n" +
            "                        \"Quand tous les codes sont trouvés, vous aurez terminé le jeu.\"";


    private final float[] solutions = {45f, 180f, 275f}; // Les solutions pour déverrouiller
    private final float tolerance = 5f; // Tolérance pour trouver une solution
    private HashSet<Float> foundSolutions = new HashSet<>(); // Garde trace des solutions trouvées

    private float[] rotationMatrix = new float[9];
    private float[] orientationAngles = new float[3];
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            WebSocketService.LocalBinder binder = (WebSocketService.LocalBinder) service;
            webSocketService = binder.getService();
            isBound = true;
            // Exemple : Envoyer un message une fois connecté
            if (webSocketService != null) {
                Log.e("test", "Envoi d'un message via WebSocketService");
                webSocketService.sendMessage("requestLobbies", "salut à tous c'est fanta");
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
                try {
                    JSONObject jsonObject = new JSONObject(jsonMessage);
                    String tag = jsonObject.getString("tag");
                    String message = jsonObject.getString("message");
                    if ("WebSocketError".equals(tag) && message.equals("WebSocket is not connected!")) {
                        if (!isErrorPopupVisible) {
                            ViewGroup view = findViewById(R.id.main);
                            showServerErrorPopUp(view);
                            isErrorPopupVisible = true;
                        }
                    } else if ("successPopup".equals(tag)) {
                        ViewGroup viewGroup = findViewById(R.id.main);
                        mediaPlayer = MediaPlayer.create(GyroscopeActivity.this, R.raw.professor_layton_sucess);
                        mediaPlayer.start();
                        showTutorialPopup(
                                "Félicitations !",
                                "Félicitation vous avez dévérouillé le coffre.\n\nVous allez passer voir son contenu dans quelques secondes",
                                viewGroup
                        );
                    } else if ("startActivity".equals(tag)) {
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gyroscopes);

        // Initialisation des vues
        lock = findViewById(R.id.lock);
        solidLock = findViewById(R.id.solidLock);
        closeButton = findViewById(R.id.closeButton);
        rulesButton = findViewById(R.id.rulesButton);
        SharedPreferences sharedPreferences = getSharedPreferences("QuestEasePrefs", MODE_PRIVATE);
        ApplyParameters(sharedPreferences);
        // Configuration du capteur de rotation vectorielle
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        ViewGroup layout = findViewById(R.id.main);
        showTutorialPopup(this.rulestitle, this.rulescontent, layout);

        if (rotationVectorSensor == null) {
            Toast.makeText(this, "Capteur de rotation vectorielle non disponible", Toast.LENGTH_SHORT).show();
            finish();
        }


        // Bouton pour quitter le jeu
        closeButton.setOnClickListener(v -> finish());

        // Bouton pour afficher les règles
        rulesButton.setOnClickListener(v -> showTutorialPopup(this.rulestitle, this.rulescontent, layout));
        Intent serviceIntent = new Intent(this, WebSocketService.class);
        startService(serviceIntent);
        bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);
        // Log pour vérifier quand le receiver est enregistré
        Log.d("MainActivity", "Enregistrement du BroadcastReceiver");
        IntentFilter filter = new IntentFilter("WebSocketMessage");
        registerReceiver(messageReceiver, filter, Context.RECEIVER_EXPORTED);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, rotationVectorSensor, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);
            SensorManager.getOrientation(rotationMatrix, orientationAngles);

            float azimuthDegrees = (float) Math.toDegrees(orientationAngles[0]);
            azimuthDegrees = (azimuthDegrees + 360) % 360;

            lock.setRotation(azimuthDegrees);
            checkForSolution(azimuthDegrees);
        }
    }

    private void checkForSolution(float angle) {
        for (float solution : solutions) {
            if (Math.abs(angle - solution) <= tolerance && !foundSolutions.contains(solution)) {
                foundSolutions.add(solution);
                Toast.makeText(this, "Solution trouvée : " + (int) solution + "°", Toast.LENGTH_SHORT).show();

                if (foundSolutions.size() == solutions.length) {
                    endGame();
                }
                break;
            }
        }
    }

    private void endGame() {
        int counter = 10; // Durée en secondes
        mediaPlayer = MediaPlayer.create(GyroscopeActivity.this, R.raw.professor_layton_sucess);
        mediaPlayer.start();
        ViewGroup viewGroup = findViewById(R.id.main);
        // Afficher le popup une première fois
        showTutorialPopup(
                "Félicitations !",
                "Félicitation vous avez dévérouillé le coffre.\n\nVous allez passer voir son contenu dans quelques secondes",
                viewGroup
        );

        webSocketService.sendMessage("successPopup", "");
        // Créer un compteur
        new CountDownTimer(counter * 1000, 1000) {
            int secondsRemaining = counter;

            @Override
            public void onTick(long millisUntilFinished) {
                secondsRemaining--;

                if (tutorialDialog != null && tutorialDialog.isShowing()) {
                    cardContent.setText(
                            "Vous avez trouvé la bonne orientation des plaques !\n\nIl est temps de passer au jeu suivant dans " + secondsRemaining + " secondes"
                    );
                }
            }

            @Override
            public void onFinish() {
                webSocketService.sendMessage("startGame", "");
                if (tutorialDialog != null) {
                    tutorialDialog.dismiss();
                }
            }
        }.start();
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
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Non utilisé
    }
}
