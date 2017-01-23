package ravtrix.backpackerbuddy.activities.notificationsettings;

import ravtrix.backpackerbuddy.retrofit.retrofitinterfaces.OnFinishedListenerRetrofit;

/**
 * Created by Ravinder on 1/22/17.
 */

interface INotificationInteractor {

    /**
     * Fetch the push notification status the user has
     * @param userID                        - the user ID
     * @param onFinishedListenerRetrofit    - call back for retrofit completion
     */
    void fetchNotificationStatus(int userID, OnFinishedListenerRetrofit onFinishedListenerRetrofit);

    /**
     * Update push comment notification of the user
     * @param userID                        - the user ID
     * @param onFinishedListenerRetrofit    - call back for retrofit completion
     */
    void updateCommentNotif(int userID, OnFinishedListenerRetrofit onFinishedListenerRetrofit);

    /**
     * Update push message notification of the user
     * @param userID                        - the user ID
     * @param onFinishedListenerRetrofit    - call back for retrofit completion
     */
    void updateMessageNotif(int userID, OnFinishedListenerRetrofit onFinishedListenerRetrofit);
}
