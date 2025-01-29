package service.CryptexAPI;

import retrofit2.Call;

import com.example.questease.model.BDD.Indice;
import com.example.questease.model.BDD.MotCryptex;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MotCryptexAPI {
    @GET("/chose")
    Call<List<MotCryptex>> getMotCryptexList();

    @GET("/motcryptex/random")
    Call<MotCryptex> getMotCryptexRandom();

    @GET("/motcryptex/indice/{idMotCryptex}")
    Call<Indice> getData(@Path("id") Long idMotCryptex);

    @GET("/motcryptex/{id}")
    Call<MotCryptex> getMotCryptexById(@Path("id") int id);

}