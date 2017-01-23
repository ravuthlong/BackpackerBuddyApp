package ravtrix.backpackerbuddy.activities.otheruserbucket;

/**
 * Created by Ravinder on 1/22/17.
 */

interface IOtherUserBucketListView {

    void showProgressBar();
    void hideProgressBar();
    void hideRecyclerView();
    void showRecyclerView();
    void displayErrorToast();
    void showTvNoBucket();
}
