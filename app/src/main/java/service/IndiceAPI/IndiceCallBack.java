package service.IndiceAPI;

import com.example.questease.model.BDD.Indice;


public interface IndiceCallBack {
    void OnIndiceReceived(Indice indice);

    void OnFailure(String errorMessage);
}
