package service.choseapi;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.questease.model.bdd.ChoseATrouverPrixJuste;

import service.RetrofitInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChoseHandler {
    private final Context context;

    public ChoseHandler(Context context) {
        this.context = context;
    }

    // Get random Chose with callback handling
    public void getRandomChose(ChoseCallBack callback) {
        ChoseAPI lobbyApi = RetrofitInstance.getRetrofitInstance().create(ChoseAPI.class);
        Call<ChoseATrouverPrixJuste> call = lobbyApi.getChoseRandom();
        call.enqueue(new Callback<ChoseATrouverPrixJuste>() {
            @Override
            public void onResponse(@NonNull Call<ChoseATrouverPrixJuste> call, @NonNull Response<ChoseATrouverPrixJuste> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onChoseReceived(response.body());
                } else {
                    Log.d("HandlerObjectAPI", "Request failed: " + response.code());
                    callback.onFailure("Request failed: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ChoseATrouverPrixJuste> call, @NonNull Throwable t) {
                Log.d("HandlerObjectAPI", "Error getting Chose object: " + t.getMessage());
                callback.onFailure("Error: " + t.getMessage());
            }
        });
    }
}