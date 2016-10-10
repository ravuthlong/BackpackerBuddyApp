package ravtrix.backpackerbuddy.activities.otheruserprofile;

/**
 * Created by Ravinder on 9/27/16.
 */

interface IOtherUserProfileInteractor {

    void fetchOtherUserInfoRetrofit(int userID, OnRFOtherUserProfileListener otherUserProfileRFListener);
}
