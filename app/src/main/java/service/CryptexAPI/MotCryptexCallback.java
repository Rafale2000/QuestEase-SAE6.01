package service.CryptexAPI;

import com.example.questease.model.BDD.MotCryptex;

public interface MotCryptexCallback {
    void onMotCryptexReceived(MotCryptex motCryptex);

    void onFailure(String errorMessage);
}
