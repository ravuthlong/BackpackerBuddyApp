package ravtrix.backpackerbuddy.helpers;

import ravtrix.backpackerbuddy.retrofit.retrofitrequests.RetrofitUserDiscussion;

/**
 * Created by Ravinder on 12/22/16.
 */

public class RetrofitUserDiscussionSingleton {
    private static RetrofitUserDiscussion retrofitUserDiscussion = new RetrofitUserDiscussion();

    public static RetrofitUserDiscussion getRetrofitUserDiscussion() {
        return retrofitUserDiscussion;
    }
}
