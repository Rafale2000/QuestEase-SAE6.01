package service.ChoseAPI;

import com.example.questease.model.BDD.ChoseATrouverPrixJuste;

public interface ChoseCallBack {
    void onChoseReceived(ChoseATrouverPrixJuste chose);

    void onFailure(String errorMessage);
}