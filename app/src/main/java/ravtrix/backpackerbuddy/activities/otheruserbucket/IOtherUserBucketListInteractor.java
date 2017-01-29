package ravtrix.backpackerbuddy.activities.otheruserbucket;

/**
 * Created by Ravinder on 1/22/17.
 */

interface IOtherUserBucketListInteractor {


    /**
     * Fetch the currently viewing user's bucket list
     * @param otherUserID                       - the user's ID to be fetched
     * @param onRetrofitBucketModelsFinished    - listener for retrofit completion
     */
    void fetchUserBucketListRetrofit(String otherUserID, OnRetrofitBucketModelsFinished onRetrofitBucketModelsFinished);
}
