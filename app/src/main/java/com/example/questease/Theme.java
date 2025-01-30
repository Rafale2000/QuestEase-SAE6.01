package com.example.questease;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;


import com.example.questease.Controller.EndActivity;
import com.example.questease.Controller.GyroscopeActivity;
import com.example.questease.Controller.StandBy;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import View.questease.PrixJuste;


public abstract class Theme extends AppCompatActivity {

    private static final String DALTONISME_STRING = "daltonisme";
    private Dialog tutorialDialog; // Référence au dialog
    private TextView cardTitle;    // Référence au titre
    private TextView cardContent;

    private TextToSpeechHelper textToSpeechHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        textToSpeechHelper = new TextToSpeechHelper(this);
    }

    protected void lireTextViews(ViewGroup layout) {
        if (textToSpeechHelper == null) {
            Log.e("Theme", "TextToSpeechHelper est null.");
            return;
        }

        // Attendez que le TTS soit initialisé avant de continuer
        if (!textToSpeechHelper.isInitialized()) {
            Log.e("Theme", "TTS n'est pas encore prêt, réessayer...");
            // Vous pouvez rappeler la méthode après un délai si TTS n'est pas encore prêt
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    lireTextViews(layout);
                }
            }, 3000); // Réessayer après 3 secondes
            return;
        }

        // Si TTS est prêt, procéder à la lecture
        textToSpeechHelper.lireTextViews(layout);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (textToSpeechHelper != null) {
            textToSpeechHelper.stop();
        }
    }

    public void ApplyParameters(SharedPreferences sharedPreferences) {
        //Protanomalie = 1
        //Protanopie = 2
        //deuteranomalie = 3
        //deuteranopie = 4
        //contraste élevé = 5
        Log.d("SharedPreferences", "Valeur de daltonisme: " + sharedPreferences.getInt(DALTONISME_STRING, 0));
        Log.d("SharedPreferences", "je vais essayer d'appliquer un thème");
        switch (sharedPreferences.getInt(DALTONISME_STRING, 0)) {
            case 1:
                setTheme(R.style.Theme_Questease_Protanomalie);
                break;
            case 2:
                setTheme(R.style.Theme_Questease_Protanopie);
                break;
            case 3:
                setTheme(R.style.Theme_Questease_Deuteranomalie);
                break;
            case 4:
                setTheme(R.style.Theme_Questease_deuteranopie);
                break;
            case 5:
                setTheme(R.style.Theme_Questease_ContrasteEleve);
                break;
            default:
                setTheme(R.style.Theme_Questease);
                break;
        }
    }

    public void applyFont(List<View> views) {
        Typeface typeface = ResourcesCompat.getFont(this, R.font.opendyslexic_regular);
        for (View view : views) {
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                textView.setTypeface(typeface);
            } else if (view instanceof Button) {
                Button button = (Button) view;
                button.setTypeface(typeface);
            } else if (view instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) view;
                checkBox.setTypeface(typeface);
            }
        }
    }

    public void adjustTextSize(List<View> views) {
        // Convertir la taille de texte souhaitée en pixels pour la comparaison

        for (View view : views) {
            float textSize14spInPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, this.getResources().getDisplayMetrics());
            float textSize19spInPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 19, this.getResources().getDisplayMetrics());
            float textSize24spInPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 24, this.getResources().getDisplayMetrics());
            float textSize31spInPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 31, this.getResources().getDisplayMetrics());
            float textSize35spInPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 35, this.getResources().getDisplayMetrics());
            if (view instanceof TextView) {
                TextView textView = (TextView) view;

                if (textView.getTextSize() == textSize14spInPx) {
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize19spInPx);
                } else if (textView.getTextSize() == textSize19spInPx) {
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize24spInPx);
                } else if (textView.getTextSize() == textSize24spInPx) {
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize31spInPx);
                } else if (textView.getTextSize() == textSize31spInPx) {
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize35spInPx);
                }
            } else if (view instanceof Button) {
                Button button = (Button) view;
                if (button.getTextSize() == textSize14spInPx) {
                    button.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize19spInPx);
                } else if (button.getTextSize() == textSize19spInPx) {
                    button.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize24spInPx);
                } else if (button.getTextSize() == textSize24spInPx) {
                    button.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize31spInPx);
                } else if (button.getTextSize() == textSize31spInPx) {
                    button.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize35spInPx);
                }
            } else if (view instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) view;
                if (checkBox.getTextSize() == textSize14spInPx) {
                    checkBox.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize19spInPx);
                } else if (checkBox.getTextSize() == textSize19spInPx) {
                    checkBox.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize24spInPx);
                } else if (checkBox.getTextSize() == textSize24spInPx) {
                    checkBox.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize31spInPx);
                } else if (checkBox.getTextSize() == textSize31spInPx) {
                    checkBox.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize35spInPx);
                }
            }
        }
    }

    public void showTutorialPopup(String title, String content, ViewGroup view) {
        // Si le popup existe déjà, mets simplement à jour le contenu
        if (tutorialDialog != null && tutorialDialog.isShowing()) {
            cardTitle.setText(title);
            cardContent.setText(content);
            return;
        }

        // Appliquer l'effet de flou
        RenderEffect blurEffect = RenderEffect.createBlurEffect(
                10, 10, Shader.TileMode.CLAMP);
        view.setRenderEffect(blurEffect);

        // Créer un nouveau Dialog si nécessaire
        tutorialDialog = new Dialog(this);
        tutorialDialog.setContentView(R.layout.popuprules);
        tutorialDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        tutorialDialog.setCanceledOnTouchOutside(true);
        tutorialDialog.setOnCancelListener(dialog -> view.setRenderEffect(null));

        FloatingActionButton closeButton = tutorialDialog.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(v -> {
            tutorialDialog.dismiss();
            view.setRenderEffect(null);
        });

        // Initialiser les vues et les sauvegarder
        cardTitle = tutorialDialog.findViewById(R.id.cardTitle);
        cardContent = tutorialDialog.findViewById(R.id.cardContent);

        // Définir le contenu
        cardTitle.setText(title);
        cardContent.setText(content);

        // Afficher le popup
        tutorialDialog.show();
    }

    public void showServerErrorPopUp(ViewGroup view) {
        RenderEffect blurEffect = RenderEffect.createBlurEffect(
                10, 10, Shader.TileMode.CLAMP);
        view.setRenderEffect(blurEffect);
        Dialog errorDialog = new Dialog(this);
        errorDialog.setContentView(R.layout.pop_up_error);
        errorDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        errorDialog.setCanceledOnTouchOutside(true);
        errorDialog.setOnCancelListener(dialog -> view.setRenderEffect(null));
        MaterialButton closeButton = errorDialog.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(v -> {
            errorDialog.dismiss();
            view.setRenderEffect(null);
        });
        errorDialog.show();
    }

    protected Intent identifyActivity(String message) {
        Intent intentgame = null;
        if ("pendu1".equals(message) || "pendu2".equals(message)) {
            intentgame = new Intent(this, Pendu.class);
        } else if ("prix_juste1".equals(message) || "prix_juste2".equals(message)) {
            intentgame = new Intent(this, PrixJuste.class);
        } else if ("rotating_pictures1".equals(message)) {
            intentgame = new Intent(this, RotatingPictures.class);
        } else if ("rotating_pictures2".equals(message)) {
            intentgame = new Intent(this, RotatingPictures2.class);
        } else if ("menteur1".equals(message)) {
            intentgame = new Intent(this, Sincere_Menteur.class);
        } else if ("menteur2".equals(message)) {
            intentgame = new Intent(this, Sincere_Menteur2.class);
        } else if ("cryptex".equals(message)) {
            // intentgame = new Intent(Lobby.this, Cryptex.class);
        } else if ("son1".equals(message)) {
            intentgame = new Intent(this, TrouveLeSon.class);
        } else if ("son2".equals(message)) {
            intentgame = new Intent(this, TrouveLeSon2.class);
        } else if ("gyroscope1".equals(message)) {
            intentgame = new Intent(this, GyroscopeActivity.class);
        } else if ("gyroscope2".equals(message)) {
            intentgame = new Intent(this, StandBy.class);
        } else if ("endActivity1".equals(message) || "endActivity2".equals(message)) {
            intentgame = new Intent(this, EndActivity.class);
        } else {
            Log.e("Lobby", "Valeur inattendue pour message : " + message);
        }
        return intentgame;
    }
    protected static SharedPreferences getSecurePreferences(Context context) {
        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);

            return EncryptedSharedPreferences.create(
                    "secure_prefs", // Nom du fichier SharedPreferences
                    masterKeyAlias,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
