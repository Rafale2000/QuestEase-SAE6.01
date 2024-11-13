package View.questease;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.questease.Model.BDD.MotCryptex;
import com.example.questease.PrixJusteActivity;
import com.example.questease.R;
import com.example.questease.Theme;

import Service.MotCryptexAPI.HandlerMotCryptexAPI;
import Service.MotCryptexAPI.MotCryptexCallback;

public class motCryptexActivity extends Theme {

    private MotCryptex Mc;

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

        HandlerMotCryptexAPI handlerMotCryptexAPI = new HandlerMotCryptexAPI(this);
        handlerMotCryptexAPI.GetRandomMotCryptex(new MotCryptexCallback() {

            @Override
            public MotCryptex onMotCryptexReceived(MotCryptex motCryptex) {
                Mc = motCryptex;
                return motCryptex;
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("PrixJuste", "Error retrieving data: " + errorMessage);
                Toast.makeText(motCryptexActivity.this, "Failed to load game data", Toast.LENGTH_SHORT).show();
            }
        });

        TextView indice = (TextView) findViewById(R.id.indice_motCryptex);
        TextView mot = (TextView) findViewById(R.id.input_word);

    }


}