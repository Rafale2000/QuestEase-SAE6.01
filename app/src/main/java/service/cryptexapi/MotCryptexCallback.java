package service.cryptexapi;

import com.example.questease.model.bdd.MotCryptex;

public interface MotCryptexCallback {
    void onMotCryptexReceived(MotCryptex motCryptex);

    void onFailure(String errorMessage);
}
