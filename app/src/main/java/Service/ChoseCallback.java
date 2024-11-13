package Service;


import com.example.questease.Model.BDD.ChoseATrouverPrixJuste;

public interface ChoseCallback {
    void onChoseReceived(ChoseATrouverPrixJuste chose);
    void onFailure(String errorMessage);
}
