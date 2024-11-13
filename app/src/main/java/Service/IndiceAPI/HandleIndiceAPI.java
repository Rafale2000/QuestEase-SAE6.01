package Service.IndiceAPI;

import android.content.Context;
import android.util.Log;

import com.example.questease.Model.BDD.Indice;

import Service.RetrofitInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HandleIndiceAPI {
    private Context context;
    IndiceInterfaceAPI indAPI = RetrofitInstance.getRetrofitInstance().create(IndiceInterfaceAPI.class);

    public HandleIndiceAPI(Context context) {
        this.context = context;
    }

    // Fetch Indice by ID with a callback
    public void GetIndice(int id, IndiceCallBack callback) {
        Call<Indice> call = indAPI.GetIndiceById(id);

        call.enqueue(new Callback<Indice>() {
            @Override
            public void onResponse(Call<Indice> call, Response<Indice> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.OnIndiceReceived(response.body());  // Assuming this method exists in IndiceCallBack
                } else {
                    Log.d("HandleCryptexAPI", "Request failed: " + response.code());
                    callback.OnFailure("Request failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Indice> call, Throwable t) {
                Log.d("HandleCryptexAPI", "Error fetching Indice: " + t.getMessage());
                callback.OnFailure("Error: " + t.getMessage());
            }
        });
    }
}
