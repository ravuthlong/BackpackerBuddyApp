package ravtrix.backpackerbuddy.activities.otheruserbucket;

import java.util.List;

import ravtrix.backpackerbuddy.recyclerviewfeed.bucketlistrecyclerview.data.BucketListModel;

/**
 * Created by Ravinder on 1/24/17.
 */

interface OnRetrofitBucketModelsFinished {

    void onFinished(List<BucketListModel> bucketListModels, int status);
    void onError();
}
