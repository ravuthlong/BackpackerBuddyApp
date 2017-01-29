package ravtrix.backpackerbuddy.fragments.discussionroom;

import java.util.List;

import ravtrix.backpackerbuddy.recyclerviewfeed.discussionroomrecyclerview.data.DiscussionModel;

/**
 * Created by Ravinder on 1/26/17.
 */

interface IDiscussionRoomFragView {
    void setDiscussionModels(List<DiscussionModel> discussionModels);
    void swapData(List<DiscussionModel> discussionModels);
    void setDiscussionModelsEmpty();
    void setRecyclerView();
    void hideSwipeLayout();
    void showSwipeLayout();
    void displayErrorToast();
    void hideProgressBar();
    void showProgressBar();
    void stopSwipeRefreshing();
}
