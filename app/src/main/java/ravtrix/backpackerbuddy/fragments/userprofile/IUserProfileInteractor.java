package ravtrix.backpackerbuddy.fragments.userprofile;

import java.util.HashMap;

import ravtrix.backpackerbuddy.models.UserLocalStore;

/**
 * Created by Ravinder on 9/14/16.
 */
interface IUserProfileInteractor {

    void getUserIntoRetrofit(int userID, String userImageURL, RetrofitProfileListener retrofitProfileListener);
    void updateTravelStatus(HashMap<String, String> userInfo, UserLocalStore userLocalStore,
                            RetrofitTravelListener retrofitTravelListener);
}
