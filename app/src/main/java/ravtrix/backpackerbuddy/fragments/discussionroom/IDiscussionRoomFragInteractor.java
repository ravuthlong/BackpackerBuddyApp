package ravtrix.backpackerbuddy.fragments.discussionroom;

import java.util.HashMap;

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

    /**
     * Fetch discussion posts with filter of country tag
     * @param postHash                              - hash information containing userID and country tag
     * @param onRetrofitDiscussionRoomFinished      - callback for retrofit completion
     */
    void fetchDiscussionFilterPostsRetrofit(HashMap<String, String> postHash, OnRetrofitDiscussionRoomFinished onRetrofitDiscussionRoomFinished);
}
