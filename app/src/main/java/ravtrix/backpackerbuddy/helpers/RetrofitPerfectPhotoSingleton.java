package ravtrix.backpackerbuddy.helpers;

import ravtrix.backpackerbuddy.retrofit.retrofitrequests.RetrofitPerfectPhoto;

/**
 * Created by Ravinder on 1/12/17.
 */

public class RetrofitPerfectPhotoSingleton {
    private static RetrofitPerfectPhoto retrofitPerfectPhoto = new RetrofitPerfectPhoto();

    public static RetrofitPerfectPhoto getRetrofitPerfectPhoto() {
        return retrofitPerfectPhoto;
    }
}
