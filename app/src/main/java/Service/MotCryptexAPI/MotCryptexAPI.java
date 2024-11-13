package Service.MotCryptexAPI;

import retrofit2.Call;
import com.example.questease.Model.BDD.ChoseATrouverPrixJuste;
import com.example.questease.Model.BDD.MotCryptex;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MotCryptexAPI {
    @GET("/chose")
    Call<List<MotCryptex>> getMotCryptexList();

    @GET("/motcryptex/random")
    Call<MotCryptex> getMotCryptexRandom();
}