package Service.MotCryptexAPI;

import android.content.Context;
import android.util.Log;

import com.example.questease.Model.BDD.MotCryptex;

import Service.RetrofitInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HandlerMotCryptexAPI {
    private Context context;
    CryptexAPI mcAPI = RetrofitInstance.getRetrofitInstance().create(CryptexAPI.class);

    public HandlerMotCryptexAPI(Context context) {
        this.context = context;
    }

    // Get random Chose with callback handling
    public void GetRandomMotCryptex(MotCryptexCallback callback) {

        Call<MotCryptex> call = mcAPI.getMotCryptexRandom();
        call.enqueue(new Callback<MotCryptex>() {
            @Override
            public void onResponse(Call<MotCryptex> call, Response<MotCryptex> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onMotCryptexReceived(response.body());
                } else {
                    Log.d("HandlerObjectAPI", "Request failed: " + response.code());
                    callback.onFailure("Request failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<MotCryptex> call, Throwable t) {
                Log.d("HandlerObjectAPI", "Error getting Chose object: " + t.getMessage());
                callback.onFailure("Error: " + t.getMessage());
            }

        });
    }

    public void fetchData(int id, MotCryptexCallback callback) {

        Call<MotCryptex> call = mcAPI.getMotCryptexById(id);

        call.enqueue(new Callback<MotCryptex>() {
            @Override
            public void onResponse(Call<MotCryptex> call, Response<MotCryptex> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onMotCryptexReceived(response.body());
                } else {
                    Log.d("HandlerMotCryptexAPI", "Request failed: " + response.code());
                    callback.onFailure("Request failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<MotCryptex> call, Throwable t) {
                Log.d("HandlerMotCryptexAPI", "Error fetching MotCryptex: " + t.getMessage());
                callback.onFailure("Error: " + t.getMessage());
            }
        });
    }


}

