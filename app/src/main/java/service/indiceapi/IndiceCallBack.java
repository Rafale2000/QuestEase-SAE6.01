package service.indiceapi;

import com.example.questease.model.bdd.Indice;


public interface IndiceCallBack {
    void OnIndiceReceived(Indice indice);

    void OnFailure(String errorMessage);
}
