package Service;

import Model.BDD.ChoseATrouverPrixJuste;

public interface ChoseCallback {
    void onChoseReceived(ChoseATrouverPrixJuste chose);
    void onFailure(String errorMessage);
}
