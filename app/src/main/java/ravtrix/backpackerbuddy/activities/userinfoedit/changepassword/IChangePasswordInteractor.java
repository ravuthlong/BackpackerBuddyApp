package ravtrix.backpackerbuddy.activities.userinfoedit.changepassword;

import java.util.HashMap;

import ravtrix.backpackerbuddy.retrofit.retrofitinterfaces.OnFinishedListenerRetrofit;

/**
 * Created by Ravinder on 1/24/17.
 */

interface IChangePasswordInteractor {
    void changePassword(HashMap<String, String> passwordHash, OnFinishedListenerRetrofit onFinishedListenerRetrofit);
}
