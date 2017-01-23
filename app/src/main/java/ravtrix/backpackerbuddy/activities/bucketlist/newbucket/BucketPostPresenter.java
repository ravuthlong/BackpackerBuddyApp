package ravtrix.backpackerbuddy.activities.bucketlist.newbucket;

import com.google.gson.JsonObject;

import java.util.HashMap;

import ravtrix.backpackerbuddy.retrofit.retrofitinterfaces.OnFinishedListenerRetrofit;

/**
 * Created by Ravinder on 1/20/17.
 */

class BucketPostPresenter implements IBucketPostPresenter {

    private IBucketPostView iBucketPostView;
    private BucketPostInteractor bucketPostInteractor;

    BucketPostPresenter(IBucketPostView iBucketPostView) {
        this.iBucketPostView = iBucketPostView;
        this.bucketPostInteractor = new BucketPostInteractor();
    }

    @Override
    public void insertPost(HashMap<String, String> bucketInfo) {

        bucketPostInteractor.insertPostRetrofit(bucketInfo, new OnFinishedListenerRetrofit() {
            @Override
            public void onFinished(JsonObject jsonObject) {
                if (jsonObject.get("status").getAsInt() == 1) {
                    iBucketPostView.setResultCode(1); // Send back to calling activity, which is the Bucket fragment
                    iBucketPostView.finished();
                } else {
                    iBucketPostView.displayErrorToast("Loading issue...");
                }
            }
            @Override
            public void onError() {
                iBucketPostView.displayErrorToast("Loading issue...");
            }
        });
    }
}
