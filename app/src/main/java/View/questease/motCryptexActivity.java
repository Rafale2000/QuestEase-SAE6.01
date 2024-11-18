package View.questease;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.questease.Model.BDD.Indice;
import com.example.questease.Model.BDD.MotCryptex;
import com.example.questease.R;
import com.example.questease.Theme;

import Service.CryptexAPI.HandlerMotCryptexAPI;
import Service.CryptexAPI.MotCryptexCallback;
import Service.IndiceAPI.HandleIndiceAPI;
import Service.IndiceAPI.IndiceCallBack;

public class motCryptexActivity extends Theme {

    private MotCryptex mc;
    private Indice ind;
    public TextView textViewIndice;
    private Button buttonConfirm;

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

        TextView mot = (TextView) findViewById(R.id.input_word);
        this.textViewIndice = findViewById(R.id.indice_motCryptex); //indice_mot_cryptex
        this.buttonConfirm = findViewById(R.id.btn_confirm);
        HandlerMotCryptexAPI handlerMotCryptexAPI = new HandlerMotCryptexAPI(this);
        handlerMotCryptexAPI.GetRandomMotCryptex(new MotCryptexCallback() {

            @Override
            public void onMotCryptexReceived(MotCryptex motCryptex) {
                mc = motCryptex;

                HandleIndiceAPI handleIndiceAPI = new HandleIndiceAPI(motCryptexActivity.this);
                handleIndiceAPI.GetIndice(motCryptex.getIndice().getId(), new IndiceCallBack() { //nul pointeur execption de mc

                    @Override
                    public void OnIndiceReceived(Indice indice) {
                        textViewIndice.setText(indice.getHint());
                    }

                    @Override
                    public void OnFailure(String errorMessage) {
                        Log.e("PrixJuste", "Error retrieving data: " + errorMessage);
                        Toast.makeText(motCryptexActivity.this, "Failed to load game data", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("PrixJuste", "Error retrieving data: " + errorMessage);
                Toast.makeText(motCryptexActivity.this, "Failed to load game data", Toast.LENGTH_SHORT).show();
            }
        });

        buttonConfirm.setOnClickListener(v -> {
            if(this.textViewIndice.getText().toString() != null){
                if(this.textViewIndice.getText().toString().equals(mc.getIndice().getHint())){
                    Toast.makeText(this, "Bravo", Toast.LENGTH_SHORT).show();
                }


            }
        });






    }


}