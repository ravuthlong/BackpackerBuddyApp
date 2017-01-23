package ravtrix.backpackerbuddy.activities.bucketlist.editbucket;

import com.google.gson.JsonObject;

import ravtrix.backpackerbuddy.retrofit.retrofitinterfaces.OnFinishedListenerRetrofit;

/**
 * Created by Ravinder on 1/20/17.
 */

class EditBucketPresenter implements IEditBucketPresenter {
    private IEditBucketView iEditBucketView;
    private EditBucketInteractor editBucketInteractor;

    EditBucketPresenter(IEditBucketView iEditBucketView) {
        this.iEditBucketView = iEditBucketView;
        this.editBucketInteractor = new EditBucketInteractor();
    }

    @Override
    public void editBucket(int bucketID, String newBucket) {

        editBucketInteractor.editBucketRetrofit(bucketID, newBucket, new OnFinishedListenerRetrofit() {
            @Override
            public void onFinished(JsonObject jsonObject) {
                if (jsonObject.get("status").getAsInt() == 1) {
                    iEditBucketView.setResultCode(1); // pass result code back to previous activity/fragment
                    iEditBucketView.finished(); // go back
                } else {
                    iEditBucketView.displayErrorToast();
                }
            }
            @Override
            public void onError() {
                iEditBucketView.displayErrorToast();
            }
        });
    }
}
