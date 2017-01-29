package ravtrix.backpackerbuddy.activities.otheruserprofile;

/**
 * Created by Ravinder on 9/27/16.
 */

interface IOtherUserProfileInteractor {

    /**
     * Fetch user information about the user currently being viewd
     * @param userID                            - the user's userID
     * @param otherUserProfileRFListener        - listener for retrofit completion
     */
    void fetchOtherUserInfoRetrofit(int userID, OnRFOtherUserProfileListener otherUserProfileRFListener);
}
