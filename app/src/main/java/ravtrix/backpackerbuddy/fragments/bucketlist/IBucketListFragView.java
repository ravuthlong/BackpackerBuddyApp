package ravtrix.backpackerbuddy.fragments.bucketlist;

import java.util.List;

import ravtrix.backpackerbuddy.recyclerviewfeed.bucketlistrecyclerview.data.BucketListModel;

/**
 * Created by Ravinder on 1/26/17.
 */

interface IBucketListFragView {

    void setRecyclerViewVisible();
    void setRecyclerViewInvisible();
    void setProgressbarVisible();
    void setProgressbarInvisible();
    void setBucketModels(List<BucketListModel> bucketModels);
    void setBucketModelsEmpty();
    void setRecyclerView();
    void displayErrorToast();
    void swapBucketModels(List<BucketListModel> bucketModels);
    void changeBucketStatLocalstore(int status);
    void showTvNoBucket();
    void hideTvNoBucket();
}
