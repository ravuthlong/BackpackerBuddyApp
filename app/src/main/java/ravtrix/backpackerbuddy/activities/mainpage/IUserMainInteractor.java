package ravtrix.backpackerbuddy.activities.mainpage;

import ravtrix.backpackerbuddy.activities.login.OnRetrofitLogInListener;

/**
 * Created by Ravinder on 1/11/17.
 */

interface IUserMainInteractor {
    void updateLocalstore(int userID, OnRetrofitLogInListener onRetrofitLogInListener);
}
