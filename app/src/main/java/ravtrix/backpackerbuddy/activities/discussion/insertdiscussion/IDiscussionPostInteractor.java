package ravtrix.backpackerbuddy.activities.discussion.insertdiscussion;

import java.util.HashMap;

import ravtrix.backpackerbuddy.retrofit.retrofitinterfaces.OnFinishedListenerRetrofit;

/**
 * Created by Ravinder on 1/20/17.
 */

interface IDiscussionPostInteractor {

    /**
     * Insert a new discussion
     * @param newDiscussion                 - information about the new discussion pass to POST param
     * @param onFinishedListenerRetrofit    - callback listener for retrofit completion
     */
    void insertDiscussionRetrofit(HashMap<String, String> newDiscussion, OnFinishedListenerRetrofit onFinishedListenerRetrofit);
}
