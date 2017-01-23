package ravtrix.backpackerbuddy.activities.discussion.discussioncomments;

import java.util.HashMap;

import ravtrix.backpackerbuddy.retrofit.retrofitinterfaces.OnFinishedListenerRetrofit;

/**
 * Created by Ravinder on 1/20/17.
 */

interface IDiscussionCommentsInteractor {

    /**
     * Notify the other user through Firebase Cloud Messaging (Push Notification)
     * @param userID                    - userID of the person to be notified
     * @param comment                   - the comment to be attached to notification
     * @param discussionID              - the ID of the host discussion which holds the comment
     */
    void notifyTheOwnerRetrofit(int userID, String comment, int discussionID);

    /**
     * Increment total comment count after commenting
     * @param discussionID              - the discussionID to increment the discussion
     */
    void incrementTotalComment(int discussionID);

    /**
     * Insert a new comment
     * @param discussionHash                - the information hash to be sent to server via POST method
     * @param onFinishedListenerRetrofit    - the callback when retrofit is done
     */
    void insertCommentRetrofit(HashMap<String, String> discussionHash, OnFinishedListenerRetrofit onFinishedListenerRetrofit);

    /**
     * Fetch all the discussion comments of a particular discussion
     * @param userID                        - the userID requesting
     * @param discussionID                  - the discussionID to fetch comments from
     * @param onRetrofitCommentModels       - the callback when retrofit is done
     */
    void fetchDiscussionComments(int userID, int discussionID, OnRetrofitCommentModels onRetrofitCommentModels);
}
