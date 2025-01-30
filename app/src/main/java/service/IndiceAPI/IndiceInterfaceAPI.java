package service.IndiceAPI;

import com.example.questease.model.bdd.Indice;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IndiceInterfaceAPI {
    @GET("/indice")
    Call<List<Indice>> GetIndiceList();

    @GET("/indice/{id}")
    Call<Indice> GetIndiceById(@Path("id") int id);  // Corrected path and parameter
}
