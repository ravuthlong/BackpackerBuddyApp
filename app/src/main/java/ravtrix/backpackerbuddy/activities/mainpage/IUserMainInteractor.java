package ravtrix.backpackerbuddy.activities.mainpage;

import ravtrix.backpackerbuddy.activities.login.OnRetrofitLogInListener;

/**
 * Created by Ravinder on 1/11/17.
 */

interface IUserMainInteractor {

    /**
     * Update local information by fetching from server first
     * @param userID                        - the userID
     * @param onRetrofitLogInListener       - listener for retrofit completion
     */
    void updateLocalstore(int userID, OnRetrofitLogInListener onRetrofitLogInListener);
}
