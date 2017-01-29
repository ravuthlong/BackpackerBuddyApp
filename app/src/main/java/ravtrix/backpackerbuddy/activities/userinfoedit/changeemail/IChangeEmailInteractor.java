package ravtrix.backpackerbuddy.activities.userinfoedit.changeemail;

import java.util.HashMap;

import ravtrix.backpackerbuddy.retrofit.retrofitinterfaces.OnFinishedListenerRetrofit;

/**
 * Created by Ravinder on 1/24/17.
 */

interface IChangeEmailInteractor {

    /**
     * Check if the email input has been taken
     * @param newEmail                          - the new email
     * @param onFinishedListenerRetrofit        - listener for retrofit completion
     */
    void checkEmailTaken(String newEmail, OnFinishedListenerRetrofit onFinishedListenerRetrofit);

    /**
     * Update user's email
     * @param emailHash                         - new email information
     * @param onFinishedListenerRetrofit        - listener for retrofit completion
     */
    void changeEmailRetrofit(HashMap<String, String> emailHash, OnFinishedListenerRetrofit onFinishedListenerRetrofit);
}
