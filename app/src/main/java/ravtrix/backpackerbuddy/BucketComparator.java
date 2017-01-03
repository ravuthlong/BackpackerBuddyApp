package ravtrix.backpackerbuddy;

import java.util.Comparator;

import ravtrix.backpackerbuddy.recyclerviewfeed.bucketlistrecyclerview.data.BucketListModel;

/**
 * Created by Ravinder on 1/2/17.
 */

public class BucketComparator implements Comparator<BucketListModel> {

    @Override
    public int compare(BucketListModel bucketListModel, BucketListModel t1) {
        return bucketListModel.getStatus() - t1.getStatus();
    }
}
