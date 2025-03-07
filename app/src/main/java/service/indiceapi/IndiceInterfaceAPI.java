package service.indiceapi;

import com.example.questease.model.bdd.Indice;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IndiceInterfaceAPI {

    @GET("/indice/{id}")
    Call<Indice> getIndiceById(@Path("id") int id);  // Corrected path and parameter
}
