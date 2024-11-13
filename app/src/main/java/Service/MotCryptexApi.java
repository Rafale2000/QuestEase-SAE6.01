package Service;




import com.example.questease.Model.BDD.MotCryptex;

import java.util.List;


import retrofit2.Call;
import retrofit2.http.GET;

public interface MotCryptexApi {
    @GET("/motcryptex")
    Call<List<MotCryptex>> getMotCryptex();

    @GET("/motcryptex/random")
    Call<MotCryptex> getRandom();
}


