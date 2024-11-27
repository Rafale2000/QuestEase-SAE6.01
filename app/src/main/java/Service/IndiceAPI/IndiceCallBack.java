package Service.IndiceAPI;

import com.example.questease.Model.BDD.Indice;


public interface IndiceCallBack {
    void OnIndiceReceived(Indice indice);

    void OnFailure(String errorMessage);
}
