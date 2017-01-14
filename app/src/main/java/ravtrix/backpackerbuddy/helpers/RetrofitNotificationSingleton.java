package ravtrix.backpackerbuddy.helpers;

import ravtrix.backpackerbuddy.retrofit.retrofitrequests.RetrofitNotification;

/**
 * Created by Ravinder on 1/10/17.
 */

public class RetrofitNotificationSingleton {
    private static RetrofitNotification retrofitNotification = new RetrofitNotification();

    public static RetrofitNotification getRetrofitNotification() {
        return retrofitNotification;
    }
}
