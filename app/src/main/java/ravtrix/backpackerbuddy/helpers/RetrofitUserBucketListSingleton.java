package ravtrix.backpackerbuddy.helpers;

import ravtrix.backpackerbuddy.retrofit.retrofitrequests.RetrofitBucketList;

/**
 * Created by Ravinder on 1/1/17.
 */

public class RetrofitUserBucketListSingleton {
    private static RetrofitBucketList retrofitBucketList = new RetrofitBucketList();

    public static RetrofitBucketList getRetrofitBucketList() {
        return retrofitBucketList;
    }

}
