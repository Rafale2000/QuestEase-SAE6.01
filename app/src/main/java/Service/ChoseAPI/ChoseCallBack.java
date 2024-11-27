package Service.ChoseAPI;

import com.example.questease.Model.BDD.ChoseATrouverPrixJuste;

public interface ChoseCallBack {
    void onChoseReceived(ChoseATrouverPrixJuste chose);

    void onFailure(String errorMessage);
}