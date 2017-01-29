package ravtrix.backpackerbuddy.fragments.discussionroom;

/**
 * Created by Ravinder on 1/26/17.
 */

interface IDiscussionRoomFragPresenter {

    void fetchDiscussionPosts(int userID);
    void fetchDiscussionPostsRefresh(int userID);
}
