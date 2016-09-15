package ravtrix.backpackerbuddy.activities.login;

import ravtrix.backpackerbuddy.models.LoggedInUser;

/**
 * Created by Ravinder on 9/14/16.
 */
public interface OnRetrofitLogInListener {
    void onSuccess(LoggedInUser loggedInUser);
    void onError();
}
