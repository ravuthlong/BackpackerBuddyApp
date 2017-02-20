package ravtrix.backpackerbuddy.fragments.discussionroom;

import java.util.HashMap;

/**
 * Created by Ravinder on 1/26/17.
 */

interface IDiscussionRoomFragPresenter {

    void fetchDiscussionPosts(int userID);
    void fetchDiscussionPostsRefresh(int userID);
    void fetchDiscussionPostsFilter(HashMap<String, String> postHash);
}
