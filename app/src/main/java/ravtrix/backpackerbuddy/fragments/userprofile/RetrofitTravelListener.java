package ravtrix.backpackerbuddy.fragments.userprofile;

/**
 * Created by Ravinder on 12/30/16.
 */

public interface RetrofitTravelListener {

    void onTravel();
    void onNotTravel();
    void onError();
    void onHideProgressDialog();
}
