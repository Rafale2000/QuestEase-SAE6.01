package view.questease;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.questease.model.bdd.Indice;
import com.example.questease.model.bdd.MotCryptex;
import com.example.questease.R;
import com.example.questease.Theme;


import service.cryptexapi.HandlerMotCryptexAPI;
import service.cryptexapi.MotCryptexCallback;
import service.indiceapi.HandleIndiceAPI;
import service.indiceapi.IndiceCallBack;

public class MotCryptexActivity extends Theme {

    private MotCryptex mc;
    private TextView textViewIndice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mot_cryptex);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        this.textViewIndice = findViewById(R.id.indice_motCryptex); //indice_mot_cryptex
        Button buttonConfirm = findViewById(R.id.btn_confirm);
        HandlerMotCryptexAPI handlerMotCryptexAPI = new HandlerMotCryptexAPI(this);
        handlerMotCryptexAPI.getRandomMotCryptex(new MotCryptexCallback() {

            @Override
            public void onMotCryptexReceived(MotCryptex motCryptex) {
                mc = motCryptex;

                HandleIndiceAPI handleIndiceAPI = new HandleIndiceAPI(MotCryptexActivity.this);
                handleIndiceAPI.getIndice(motCryptex.getIndice().getId(), new IndiceCallBack() {

                    @Override
                    public void OnIndiceReceived(Indice indice) {
                        textViewIndice.setText(indice.getHint());
                    }

                    @Override
                    public void OnFailure(String errorMessage) {
                        Log.e("PrixJuste", "Error retrieving data: " + errorMessage);
                        Toast.makeText(MotCryptexActivity.this, "Failed to load game data", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("PrixJuste", "Error retrieving data: " + errorMessage);
                Toast.makeText(MotCryptexActivity.this, "Failed to load game data", Toast.LENGTH_SHORT).show();
            }
        });

        buttonConfirm.setOnClickListener(v -> {
            this.textViewIndice.getText().toString();
            if (this.textViewIndice.getText().toString().equals(mc.getIndice().getHint())) {
                Toast.makeText(this, "Bravo", Toast.LENGTH_SHORT).show();
            }


        });

        String title = "Comment jouer au jeu du Cryptex ?";
        String content = "Trouvez le mot de passe du cryptex grâce à l'indice qui " +
                "\n vous est donné. Une fois le cryptex ouvert, " +
                "\n vous obtiendrez un indice vous permettant de" +
                "\n compléter la chasse !";
        showTutorialPopup(title, content, findViewById(R.id.main));


    }


}