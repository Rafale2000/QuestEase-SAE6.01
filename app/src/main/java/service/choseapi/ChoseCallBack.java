package service.choseapi;

import com.example.questease.model.bdd.ChoseATrouverPrixJuste;

public interface ChoseCallBack {
    void onChoseReceived(ChoseATrouverPrixJuste chose);

    void onFailure(String errorMessage);
}