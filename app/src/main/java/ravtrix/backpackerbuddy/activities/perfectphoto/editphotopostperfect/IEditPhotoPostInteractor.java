package ravtrix.backpackerbuddy.activities.perfectphoto.editphotopostperfect;

import ravtrix.backpackerbuddy.retrofit.retrofitinterfaces.OnFinishedListenerRetrofit;

/**
 * Created by Ravinder on 1/24/17.
 */

interface IEditPhotoPostInteractor {

    /**
     * Update post of perfect photo
     * @param photoID                           - the unique photoID
     * @param post                              - the new post
     * @param onFinishedListenerRetrofit        - listener for retrofit completion
     */
    void updatePhotoPostRetrofit(int photoID, String post, OnFinishedListenerRetrofit onFinishedListenerRetrofit);
}
