package com.example.questease.controller;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

import com.example.questease.R;

public class Deplacement extends AppCompatActivity implements SensorEventListener {
    private TextView chronometerText, stepsText, directionText;
    private ImageView arrowImage;
    private Handler handler = new Handler(Looper.getMainLooper());
    private SensorManager sensorManager;
    private Sensor stepCounter;
    private long startTime, timeBuffer, updateTime = 0L;
    private int seconds, minutes, milliseconds;
    private int steps = 0, initialSteps = 0;
    private boolean isRunning = false;
    private String currentLine;
    private int lowerBound, upperBound;

    private static final float STEP_LENGTH = 0.75f; // Longueur moyenne d'un pas en mètres

    // Déclarez une instance de Random pour réutilisation
    private Random random = new Random();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deplacement);

        // Initialisation des vues

        stepsText = findViewById(R.id.steps_text);
        directionText = findViewById(R.id.direction_text);
        arrowImage = findViewById(R.id.arrow_image);
        Button startButton = findViewById(R.id.start_button);
        Button pauseButton = findViewById(R.id.pause_button);
        Button resetButton = findViewById(R.id.reset_button);

        // Configuration des listeners pour les boutons
        startButton.setOnClickListener(view -> startChronometer());
        pauseButton.setOnClickListener(view -> pauseChronometer());
        resetButton.setOnClickListener(view -> resetChronometer());

        // Initialisation du capteur de pas
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager != null) {
            stepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        }

        // Choix aléatoire de la ligne et définition des bornes de pas
        chooseRandomLine();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (stepCounter != null) {
            sensorManager.registerListener(this, stepCounter, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    // Méthodes pour le chronomètre
    private void startChronometer() {
        if (!isRunning) {
            startTime = SystemClock.uptimeMillis();
            handler.postDelayed(runnable, 0);
            isRunning = true;
        }
    }

    private void pauseChronometer() {
        if (isRunning) {
            timeBuffer += SystemClock.uptimeMillis() - startTime;
            handler.removeCallbacks(runnable);
            isRunning = false;
        }
    }

    private void resetChronometer() {
        timeBuffer = 0L;
        updateTime = 0L;
        startTime = SystemClock.uptimeMillis();
        steps = 0;
        initialSteps = 0;
        directionText.setText("");
        arrowImage.setImageResource(R.drawable.arrow_up); // Remplacer par la flèche par défaut
        updateUI();
        chronometerText.setText("00:00:000");
        chooseRandomLine(); // Choisit une nouvelle ligne après réinitialisation
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (isRunning) {
                updateTime = timeBuffer + (SystemClock.uptimeMillis() - startTime);
                seconds = (int) (updateTime / 1000);
                minutes = seconds / 60;
                seconds = seconds % 60;
                milliseconds = (int) (updateTime % 1000);

                chronometerText.setText(String.format("%02d:%02d:%03d", minutes, seconds, milliseconds));

                handler.postDelayed(this, 1000);
            }
        }
    };

    private void updateUI() {
        stepsText.setText(String.valueOf(steps));
    }

    private void chooseRandomLine() {
        // Choisit une ligne au hasard en réutilisant l'instance de Random
        int line = random.nextInt(3); // 0, 1, ou 2 pour les lignes spécifiées

        switch (line) {
            case 0:
                currentLine = "110";
                lowerBound = 48;
                upperBound = 53;
                break;
            case 1:
                currentLine = "117";
                lowerBound = 38;
                upperBound = 42;
                break;
            case 2:
                currentLine = "118";
                lowerBound = 28;
                upperBound = 32;
                break;
        }

        directionText.setText("Ligne choisie: " + currentLine);
        arrowImage.setImageResource(R.drawable.arrow_up); // Flèche initiale tout droit
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER && isRunning) {
            if (initialSteps == 0) {
                initialSteps = (int) event.values[0];
            }
            steps = (int) event.values[0] - initialSteps;
            updateUI();

            // Vérifie si le nombre de pas est dans les bornes pour changer la flèche
            if (steps >= lowerBound && steps <= upperBound) {
                arrowImage.setImageResource(R.drawable.arrow_left); // Changement de direction si nécessaire
                directionText.setText("Vous êtes presque arrivé, attendez...");
                handler.postDelayed(() -> {
                    directionText.setText("Vous êtes arrivé à destination !");
                    arrowImage.setImageResource(R.drawable.arrow_up); // Flèche d'arrivée
                }, 10000); // Attendre 10 secondes
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Pas besoin de gérer cela pour le moment
    }
}
