package ravtrix.backpackerbuddy.activities.bucketlist.editbucket;

/**
 * Created by Ravinder on 1/20/17.
 */

interface IEditBucketPresenter {

    /**
     * Call edit bucket of interactor
     * @param bucketID              - unique id of bucket
     * @param newBucket             - the new post
     */
    void editBucket(int bucketID, String newBucket);
}
