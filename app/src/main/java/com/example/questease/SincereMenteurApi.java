package com.example.questease;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SincereMenteurApi {
    // Méthode pour envoyer des données sous forme de Map
    @POST("sincereMenteur")
    // Remplace par le bon endpoint
    Call<Void> sendData(@Body Map<String, Object> data);
}


