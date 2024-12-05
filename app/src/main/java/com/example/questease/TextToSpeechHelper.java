package com.example.questease;

import android.speech.tts.TextToSpeech;
import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.View;
import android.os.Handler;

public class TextToSpeechHelper {
    private TextToSpeech textToSpeech;
    private boolean isTtsInitialized = false;
    private final int RETRY_DELAY = 1000; // Délai en millisecondes avant de réessayer

    public TextToSpeechHelper(Context context) {
        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    isTtsInitialized = true;
                    Log.d("TTS", "TextToSpeech initialisé avec succès");
                } else {
                    isTtsInitialized = false;
                    Log.e("TTS", "Échec de l'initialisation de TextToSpeech");
                }
            }
        });
    }

    public boolean isInitialized() {
        return isTtsInitialized;
    }

    // Méthode pour lire le texte depuis un ViewGroup
    public void lireTextViews(final ViewGroup layout) {
        if (!isTtsInitialized) {
            Log.e("TTS", "TextToSpeech non initialisé, réessayer après un délai");

            // Réessayer après un délai si TTS n'est pas prêt
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    lireTextViews(layout);
                }
            }, RETRY_DELAY); // Réessayer après 3 secondes
            return;
        }

        // Si TTS est initialisé, procéder à la lecture
        for (int i = 0; i < layout.getChildCount(); i++) {
            View view = layout.getChildAt(i);
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                String text = textView.getText().toString();
                Log.d("texte à lire", text);
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
            }
        }



    }

    // Méthode pour libérer les ressources
    public void shutdown() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
            isTtsInitialized = false;
        }
    }

    public void stop() {
        // Libérer les ressources de TextToSpeech
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }
}
