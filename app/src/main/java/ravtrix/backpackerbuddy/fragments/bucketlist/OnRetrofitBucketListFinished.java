package ravtrix.backpackerbuddy.fragments.bucketlist;

import java.util.List;

import ravtrix.backpackerbuddy.recyclerviewfeed.bucketlistrecyclerview.data.BucketListModel;

/**
 * Created by Ravinder on 1/26/17.
 */

public interface OnRetrofitBucketListFinished {

    void onFinished(List<BucketListModel> bucketListModelList);
    void onError();
}
