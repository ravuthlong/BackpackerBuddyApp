package ravtrix.backpackerbuddy.activities.bucketlist.newbucket;

import java.util.HashMap;

import ravtrix.backpackerbuddy.retrofit.retrofitinterfaces.OnFinishedListenerRetrofit;

/**
 * Created by Ravinder on 1/20/17.
 */

interface IBucketPostInteractor {

    /**
     * Insert a new post
     * @param bucketInfo                    - HashMap containing information about the bucket post
     * @param onFinishedListenerRetrofit    - callback when retrofit is completed
     */
    void insertPostRetrofit(HashMap<String, String> bucketInfo, OnFinishedListenerRetrofit onFinishedListenerRetrofit);
}
