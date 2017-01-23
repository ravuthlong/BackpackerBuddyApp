package ravtrix.backpackerbuddy.activities.bucketlist.newbucket;

/**
 * Created by Ravinder on 1/20/17.
 */

interface IBucketPostView {
    void displayErrorToast(String error);
    void setResultCode(int code);
    void finished();
}
