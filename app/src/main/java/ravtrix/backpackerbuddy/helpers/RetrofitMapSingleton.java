package ravtrix.backpackerbuddy.helpers;

import ravtrix.backpackerbuddy.retrofit.retrofitrequests.RetrofitMap;

/**
 * Created by Ravinder on 3/6/17.
 */

public class RetrofitMapSingleton {
    private static RetrofitMap retrofitMap = new RetrofitMap();

    public static RetrofitMap getRetrofitMap() {
        return retrofitMap;
    }
}
