package service.cryptexapi;

import android.content.Context;
import android.util.Log;


import androidx.annotation.NonNull;

import com.example.questease.model.bdd.MotCryptex;

import service.RetrofitInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HandlerMotCryptexAPI {
    private Context context;
    MotCryptexAPI mcAPI = RetrofitInstance.getRetrofitInstance().create(MotCryptexAPI.class);
    private String rq = "Request failed: ";

    public HandlerMotCryptexAPI(Context context) {
        this.context = context;
    }

    // Get random Chose with callback handling
    public void getRandomMotCryptex(MotCryptexCallback callback) {

        Call<MotCryptex> call = mcAPI.getMotCryptexRandom();
        call.enqueue(new Callback<MotCryptex>() {
            @Override
            public void onResponse(@NonNull Call<MotCryptex> call, @NonNull Response<MotCryptex> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onMotCryptexReceived(response.body());
                } else {
                    Log.d("HandlerObjectAPI", rq + response.code());
                    callback.onFailure(rq + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MotCryptex> call, @NonNull Throwable t) {
                Log.d("HandlerObjectAPI", "Error getting Chose object: " + t.getMessage());
                callback.onFailure("Error: " + t.getMessage());
            }

        });
    }

    public void fetchData(int id, MotCryptexCallback callback) {

        Call<MotCryptex> call = mcAPI.getMotCryptexById(id);

        call.enqueue(new Callback<MotCryptex>() {
            @Override
            public void onResponse(@NonNull Call<MotCryptex> call, @NonNull Response<MotCryptex> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onMotCryptexReceived(response.body());
                } else {
                    Log.d("HandlerMotCryptexAPI", rq + response.code());
                    callback.onFailure(rq + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MotCryptex> call, @NonNull Throwable t) {
                Log.d("HandlerMotCryptexAPI", "Error fetching MotCryptex: " + t.getMessage());
                callback.onFailure("Error: " + t.getMessage());
            }
        });
    }


}

