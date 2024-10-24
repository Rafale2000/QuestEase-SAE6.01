package Service;




import java.util.List;

import Model.bdd.MotCryptex;
import retrofit2.Call;
import retrofit2.http.GET;

public interface MotCryptexApi {
    @GET("/motcryptex")
    Call<List<MotCryptex>> getMotCryptex();

    @GET("/motcryptex/random")
    Call<MotCryptex> getRandom();
}


