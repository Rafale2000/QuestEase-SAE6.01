package com.example.questease.controller;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.questease.R;

import java.util.Locale;

public class Lunettes extends AppCompatActivity {

    private BluetoothAdapter bluetoothAdapter;
    private TextToSpeech textToSpeech;
    private ImageView arrowImage;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        arrowImage = findViewById(R.id.arrow_image);

        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.setLanguage(Locale.getDefault());
            } else {
                Toast.makeText(this, "Synthèse vocale non disponible", Toast.LENGTH_SHORT).show();
            }
        });

        checkBluetoothConnection();
    }

    private void checkBluetoothConnection() {
        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            showDirection("droite");
        } else {
            Toast.makeText(this, "Activez le Bluetooth pour utiliser l'application", Toast.LENGTH_LONG).show();
        }
    }

    private void showDirection(String direction) {
        // Afficher l'image correspondante
        switch (direction.toLowerCase()) {
            case "devant":
                arrowImage.setImageResource(R.drawable.arrow_up);
                break;
            case "derrière":
                arrowImage.setImageResource(R.drawable.arrow_down);
                break;
            case "droite":
                arrowImage.setImageResource(R.drawable.arrow_right);
                break;
            case "gauche":
                arrowImage.setImageResource(R.drawable.arrow_left);
                break;
        }

        // Annoncer la direction
        textToSpeech.speak(direction, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}
