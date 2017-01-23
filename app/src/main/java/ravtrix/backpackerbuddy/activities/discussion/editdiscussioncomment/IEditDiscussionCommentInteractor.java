package ravtrix.backpackerbuddy.activities.discussion.editdiscussioncomment;

import ravtrix.backpackerbuddy.retrofit.retrofitinterfaces.OnFinishedListenerRetrofit;

/**
 * Created by Ravinder on 1/20/17.
 */

interface IEditDiscussionCommentInteractor {

    /**
     * Update a comment of a discussion
     * @param commentID                     - the unique comment ID
     * @param comment                       - the new comment to be updated to
     * @param onFinishedListenerRetrofit    - callback for when retrofit is completed
     */
    void updateCommentRetrofit(int commentID, String comment, OnFinishedListenerRetrofit onFinishedListenerRetrofit);
}
