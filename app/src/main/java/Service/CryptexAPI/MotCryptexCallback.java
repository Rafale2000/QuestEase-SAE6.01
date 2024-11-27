package Service.CryptexAPI;

import com.example.questease.Model.BDD.MotCryptex;

public interface MotCryptexCallback {
    void onMotCryptexReceived(MotCryptex motCryptex);

    void onFailure(String errorMessage);
}
