package View.questease;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.util.UnstableApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.questease.R;

import java.util.ArrayList;
import java.util.List;

import Service.MotCryptexAdapter;
import Service.MotCryptexApi;
import Service.RetrofitInstance;
import Model.bdd.MotCryptex;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class testSpring extends AppCompatActivity {
    private TextView textView;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_spring);
        this.textView = findViewById(R.id.textView);

        // Appel à l'API
        MotCryptexApi lobbyApi = RetrofitInstance.getRetrofitInstance().create(MotCryptexApi.class);
        //retrofit2.Call<List<MotCryptex>> call = lobbyApi.getLobbies();
        retrofit2.Call<MotCryptex> callRdm = lobbyApi.getRandom();

    }
}
