package ravtrix.backpackerbuddy.activities.discussion.discussioncomments;

import java.util.HashMap;

/**
 * Created by Ravinder on 1/20/17.
 */

interface IDiscussionCommentsPresenter {
    void notifyTheOwner(int userID, String comment, int discussionID);
    void notifyOtherUsers(int userID, int ownerID, String comment, int discussionID);
    void incrementTotalComment(int discussionID);
    void insertComment(HashMap<String, String> discussionHash, int userID, int discussionID, int ownerID);
    void fetchDiscussionComments(int userID, int discussionID);
    void fetchDiscussionCommentsRefresh(int userID, int discussionID);
}
