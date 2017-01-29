package ravtrix.backpackerbuddy.fragments.bucketlist;

import ravtrix.backpackerbuddy.recyclerviewfeed.bucketlistrecyclerview.adapter.BucketListAdapter;

/**
 * Created by Ravinder on 1/26/17.
 */

interface IBucketListFragPresenter {

    void fetchUserBucketList(int userID);
    void fetchUserBucketListUpdate(int userID, BucketListAdapter bucketListAdapter);
    void updateBucketVisibilityRetrofit(int userID, int status);
}
