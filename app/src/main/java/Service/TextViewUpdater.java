package Service;

/*import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import androidx.media3.common.util.Log;

import com.example.questease.Model.BDD.MotCryptex;

import Service.MotCryptexApi;
import Service.RetrofitInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TextViewUpdater {

    private TextView textView;
    private Context context;
    private Handler handler;

    public TextViewUpdater(Context context, TextView textView) {
        this.context = context;
        this.textView = textView;
        handler = new Handler(Looper.getMainLooper());
    }

    public void updateTextView() {
        MotCryptexApi lobbyApi = RetrofitInstance.getRetrofitInstance().create(MotCryptexApi.class);
        Call<MotCryptex> call = lobbyApi.getRandom(); // Use the correct Call type
        call.enqueue(new Callback<MotCryptex>() { // Use the correct Callback type
            @Override
            public void onResponse(Call<MotCryptex> call, Response<MotCryptex> response) {
                if (response.isSuccessful()) {
                    MotCryptex motCryptex = response.body(); // Get the single MotCryptex object
                    if (motCryptex != null) {
                        final String mot = motCryptex.getMot();
                        handler.post(() -> textView.setText(mot));
                    }
                }
            }

            @Override
            public void onFailure(Call<MotCryptex> call, Throwable t) { // Use the correct Call type
                Log.e("TextViewUpdater", "Error updating TextView: " + t.getMessage());
            }
        });
    }
}*/