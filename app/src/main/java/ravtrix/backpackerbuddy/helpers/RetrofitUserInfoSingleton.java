package ravtrix.backpackerbuddy.helpers;


import ravtrix.backpackerbuddy.retrofit.retrofitrequests.retrofituserinforequests.RetrofitUserInfo;

/**
 * Created by Ravinder on 8/24/16.
 */
public class RetrofitUserInfoSingleton {

    private static RetrofitUserInfo retrofitUserInfo = new RetrofitUserInfo();

    public static RetrofitUserInfo getRetrofitUserInfo() {
        return retrofitUserInfo;
    }

}
