package ravtrix.backpackerbuddy.helpers;

import ravtrix.backpackerbuddy.retrofit.retrofitrequests.RetrofitAppInfo;

/**
 * Created by Ravinder on 1/8/17.
 */

public class RetrofitAppInfoSingleton {
    private static RetrofitAppInfo retrofitAppInfo = new RetrofitAppInfo();

    public static RetrofitAppInfo getRetrofitAppInfo() {
        return retrofitAppInfo;
    }
}
