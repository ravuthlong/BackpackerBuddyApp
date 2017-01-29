package ravtrix.backpackerbuddy.activities.otheruserbucket;

import java.util.List;

import ravtrix.backpackerbuddy.recyclerviewfeed.bucketlistrecyclerview.data.BucketListModel;

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
    void setBucketListModels(List<BucketListModel> bucketListModels);
    void setRecyclerView();
}
