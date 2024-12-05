package com.example.questease.Controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.questease.R;
import androidx.appcompat.app.AppCompatActivity;
import com.example.questease.R;
import java.util.HashSet;

public class GyroscopeActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor rotationVectorSensor;

    private ImageView lock, solidLock;
    private Button closeButton, rulesButton;

    private final float[] solutions = {45f, 180f, 275f}; // Les solutions pour déverrouiller
    private final float tolerance = 5f; // Tolérance pour trouver une solution
    private HashSet<Float> foundSolutions = new HashSet<>(); // Garde trace des solutions trouvées

    private float[] rotationMatrix = new float[9];
    private float[] orientationAngles = new float[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gyroscopes);

        // Initialisation des vues
        lock = findViewById(R.id.lock);
        solidLock = findViewById(R.id.solidLock);
        closeButton = findViewById(R.id.closeButton);
        rulesButton = findViewById(R.id.rulesButton);

        // Configuration du capteur de rotation vectorielle
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        if (rotationVectorSensor == null) {
            Toast.makeText(this, "Capteur de rotation vectorielle non disponible", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Afficher les règles au lancement
        showRulesPopup();

        // Bouton pour quitter le jeu
        closeButton.setOnClickListener(v -> finish());

        // Bouton pour afficher les règles
        rulesButton.setOnClickListener(v -> showRulesPopup());
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
        // Affiche une pop-up et redirige vers une nouvelle activité
        new AlertDialog.Builder(this)
                .setTitle("Bien joué !")
                .setMessage("Vous avez découvert tous les codes du coffre.")
                .setPositiveButton("Continuer", (dialog, which) -> {
                    Intent intent = new Intent(this, EndActivity.class);
                    startActivity(intent);
                    finish();
                })
                .setCancelable(false)
                .show();
    }

    private void showRulesPopup() {
        new AlertDialog.Builder(this)
                .setTitle("Règles du jeu")
                .setMessage("Tournez votre téléphone pour aligner l'aiguille sur les bons angles : 45°, 180°, et 300°. " +
                        "Quand tous les codes sont trouvés, vous aurez terminé le jeu.")
                .setPositiveButton("D'accord", null)
                .show();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Non utilisé
    }
}
