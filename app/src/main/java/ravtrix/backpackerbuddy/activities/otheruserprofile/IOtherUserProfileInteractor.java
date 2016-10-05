package ravtrix.backpackerbuddy.activities.otheruserprofile;

/**
 * Created by Ravinder on 9/27/16.
 */

public interface IOtherUserProfileInteractor {

    void fetchOtherUserInfoRetrofit(int userID, OnRFOtherUserProfileListener otherUserProfileRFListener);
}
