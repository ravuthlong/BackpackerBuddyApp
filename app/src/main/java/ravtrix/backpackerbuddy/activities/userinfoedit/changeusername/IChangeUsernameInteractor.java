package ravtrix.backpackerbuddy.activities.userinfoedit.changeusername;

import java.util.HashMap;

import ravtrix.backpackerbuddy.retrofit.retrofitinterfaces.OnFinishedListenerRetrofit;

/**
 * Created by Ravinder on 1/24/17.
 */

interface IChangeUsernameInteractor {

    /**
     *
     * @param newUsername
     * @param onFinishedListenerRetrofit
     */
    void checkUsernameTakenRetrofit(final String newUsername, OnFinishedListenerRetrofit onFinishedListenerRetrofit);

    /**
     *
     * @param userInfo
     * @param onFinishedListenerRetrofit
     */
    void changeUsernameRetrofit(final HashMap<String, String> userInfo, OnFinishedListenerRetrofit onFinishedListenerRetrofit);

    /**
     *
     * @param userInfo
     * @param onFinishedListenerRetrofit
     */
    void changeUsernameFacebookRetrofit(final HashMap<String, String> userInfo, OnFinishedListenerRetrofit onFinishedListenerRetrofit);
}
