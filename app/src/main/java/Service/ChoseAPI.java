package Service;


//TODO Reprendre les paramètrs du manifest

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ChoseAPI {
    @GET("/chose")
    Call<List<Model.BDD.ChoseATrouverPrixJuste>> getMotChose();

    @GET("/chose/random")
    Call<Model.BDD.ChoseATrouverPrixJuste> getChoseRandom();
}