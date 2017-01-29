package ravtrix.backpackerbuddy.fragments.bucketlist;

import ravtrix.backpackerbuddy.retrofit.retrofitinterfaces.OnFinishedListenerRetrofit;

/**
 * Created by Ravinder on 1/26/17.
 */

interface IBucketListFragInteractor {

    /**
     * Get user's bucket  list
     * @param userID                            - the unique ID of the user
     * @param onRetrofitBucketListFinished      - listener for retrofit completion
     */
    void fetchUserBucketListRetrofit(int userID, OnRetrofitBucketListFinished onRetrofitBucketListFinished);

    /**
     * Update the visibility of user's bucket list
     * @param userID                            - the unique ID of the user
     * @param status                            - the status to be changed to
     * @param onFinishedListenerRetrofit        - listener for retrofit completion
     */
    void updateBucketVisibilityRetrofit(int userID, final int status, OnFinishedListenerRetrofit onFinishedListenerRetrofit);
}
