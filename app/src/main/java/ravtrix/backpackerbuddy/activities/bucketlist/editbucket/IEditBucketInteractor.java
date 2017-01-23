package ravtrix.backpackerbuddy.activities.bucketlist.editbucket;

import ravtrix.backpackerbuddy.retrofit.retrofitinterfaces.OnFinishedListenerRetrofit;

/**
 * Created by Ravinder on 1/20/17.
 */

interface IEditBucketInteractor {

    /**
     * Edit a user's bucket item in the server
     * @param bucketID                      - the bucketID, unique to the bucket item
     * @param newBucket                     - the new bucket post to be updated to
     * @param onFinishedListenerRetrofit    - retrofit callback listener
     */
    void editBucketRetrofit(int bucketID, String newBucket, OnFinishedListenerRetrofit onFinishedListenerRetrofit);
}
