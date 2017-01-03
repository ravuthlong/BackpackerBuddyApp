package ravtrix.backpackerbuddy.fragments.userprofile;

import java.util.HashMap;

import ravtrix.backpackerbuddy.models.UserLocalStore;

/**
 * Created by Ravinder on 9/14/16.
 */
interface IUserProfilePresenter {

    void getUserInfo(int userID, String userImgURL);
    void updateTravelStatus(HashMap<String, String> userInfo, UserLocalStore userLocalStore);
    void onDestroy();

}
