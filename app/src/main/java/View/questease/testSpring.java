package View.questease;

import android.annotation.SuppressLint;
import android.os.Bundle;

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


    private MotCryptexAdapter adapter;
    private List<MotCryptex> motCryptestList = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_spring);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MotCryptexAdapter(motCryptestList);
        recyclerView.setAdapter(adapter);

        // Appel à l'API
        MotCryptexApi lobbyApi = RetrofitInstance.getRetrofitInstance().create(MotCryptexApi.class);
        retrofit2.Call<List<MotCryptex>> call = lobbyApi.getLobbies();

        call.enqueue(new Callback<List<MotCryptex>>() {
            @Override
            public void onResponse(Call<List<MotCryptex>> call, Response<List<MotCryptex>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    motCryptestList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @OptIn(markerClass = UnstableApi.class)
            @Override
            public void onFailure(Call<List<MotCryptex>> call, Throwable t) {
                androidx.media3.common.util.Log.e("API Error", "Request failed: " + t.getMessage()); // Log the error
                // Optional: display an error message to the user
            }
        });
    }
}
