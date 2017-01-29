package ravtrix.backpackerbuddy.fragments.discussionroom;

/**
 * Created by Ravinder on 1/26/17.
 */

interface IDiscussionRoomFragInteractor {

    /**
     * Fetch discussion posts of users
     * @param userID                                - unique userID of requesting user
     * @param onRetrofitDiscussionRoomFinished      - callback for retrofit completion
     */
    void fetchDiscussionPostsRetrofit(int userID, OnRetrofitDiscussionRoomFinished onRetrofitDiscussionRoomFinished);
}
