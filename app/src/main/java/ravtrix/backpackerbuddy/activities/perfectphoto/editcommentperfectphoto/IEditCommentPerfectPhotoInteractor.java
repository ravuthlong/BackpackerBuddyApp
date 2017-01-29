package ravtrix.backpackerbuddy.activities.perfectphoto.editcommentperfectphoto;

import ravtrix.backpackerbuddy.retrofit.retrofitinterfaces.OnFinishedListenerRetrofit;

/**
 * Created by Ravinder on 1/24/17.
 */

public interface IEditCommentPerfectPhotoInteractor {

    /**
     * Update perfect photo comment
     * @param commentID                     - the commentID
     * @param newComment                    - the new comment
     * @param onFinishedListenerRetrofit    - listener for retrofit completion
     */
    void updateCommentRetrofit(int commentID, String newComment, OnFinishedListenerRetrofit onFinishedListenerRetrofit);
}
