package ravtrix.backpackerbuddy.activities.perfectphoto.postperfectphoto;

import java.util.HashMap;

import ravtrix.backpackerbuddy.retrofit.retrofitinterfaces.OnFinishedListenerRetrofit;

/**
 * Created by Ravinder on 1/24/17.
 */

public interface IPostPerfectPhotoInteractor {

    /**
     * Post new perfect photo
     * @param perfectPhotoInfo                  - information about the photo
     * @param onFinishedListenerRetrofit        - listener for retrofit completion
     */
    void postNewPerfectPhotoRetrofit(HashMap<String, String> perfectPhotoInfo, OnFinishedListenerRetrofit onFinishedListenerRetrofit);
}
