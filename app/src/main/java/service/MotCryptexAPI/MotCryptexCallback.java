package service.MotCryptexAPI;

import com.example.questease.model.bdd.MotCryptex;

public interface MotCryptexCallback {
    MotCryptex onMotCryptexReceived(MotCryptex motCryptex);

    void onFailure(String errorMessage);
}
