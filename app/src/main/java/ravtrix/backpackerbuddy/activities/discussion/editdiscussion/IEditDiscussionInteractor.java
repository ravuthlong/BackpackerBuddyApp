package ravtrix.backpackerbuddy.activities.discussion.editdiscussion;

import ravtrix.backpackerbuddy.retrofit.retrofitinterfaces.OnFinishedListenerRetrofit;

/**
 * Created by Ravinder on 1/20/17.
 */

interface IEditDiscussionInteractor {

    /**
     * Edit discussion post
     * @param discussionID                  - the discussionID unique to discussion
     * @param newDiscussion                 - the new discussion post
     * @param onFinishedListenerRetrofit    - the callback after retrofit completed
     */
    void editDiscussionInteractor(int discussionID, String newDiscussion, OnFinishedListenerRetrofit onFinishedListenerRetrofit);
}
