package Service.MotCryptexAPI;

import com.example.questease.Model.BDD.ChoseATrouverPrixJuste;
import com.example.questease.Model.BDD.MotCryptex;

public interface MotCryptexCallback {
    MotCryptex onMotCryptexReceived(MotCryptex motCryptex);

    void onFailure(String errorMessage);
}
