package service.indiceapi;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.questease.model.bdd.Indice;

import service.RetrofitInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HandleIndiceAPI {
    private Context context;
    IndiceInterfaceAPI indAPI = RetrofitInstance.getRetrofitInstance().create(IndiceInterfaceAPI.class);
    private final String rq = "Request failed: ";

    public HandleIndiceAPI(Context context) {
        this.context = context;
    }

    // Fetch Indice by ID with a callback
    public void getIndice(int id, IndiceCallBack callback) {
        Call<Indice> call = indAPI.GetIndiceById(id);

        call.enqueue(new Callback<Indice>() {
            @Override
            public void onResponse(@NonNull Call<Indice> call, @NonNull Response<Indice> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.OnIndiceReceived(response.body());  // Assuming this method exists in IndiceCallBack
                } else {
                    Log.d("HandleCryptexAPI", rq + response.code());
                    callback.OnFailure(rq + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Indice> call, @NonNull Throwable t) {
                Log.d("HandleCryptexAPI", "Error fetching Indice: " + t.getMessage());
                callback.OnFailure("Error: " + t.getMessage());
            }
        });
    }
}
