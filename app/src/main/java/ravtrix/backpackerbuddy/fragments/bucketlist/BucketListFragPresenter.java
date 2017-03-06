package ravtrix.backpackerbuddy.fragments.bucketlist;

import com.google.gson.JsonObject;

import java.util.Collections;
import java.util.List;

import ravtrix.backpackerbuddy.BucketComparator;
import ravtrix.backpackerbuddy.recyclerviewfeed.bucketlistrecyclerview.adapter.BucketListAdapter;
import ravtrix.backpackerbuddy.recyclerviewfeed.bucketlistrecyclerview.data.BucketListModel;
import ravtrix.backpackerbuddy.retrofit.retrofitinterfaces.OnFinishedListenerRetrofit;

/**
 * Created by Ravinder on 1/26/17.
 */

class BucketListFragPresenter implements IBucketListFragPresenter {

    private IBucketListFragView iBucketListFragView;
    private BucketListFragInteractor bucketListFragInteractor;

    BucketListFragPresenter(IBucketListFragView iBucketListFragView) {
        this.iBucketListFragView = iBucketListFragView;
        this.bucketListFragInteractor = new BucketListFragInteractor();
    }

    @Override
    public void fetchUserBucketList(int userID) {

        bucketListFragInteractor.fetchUserBucketListRetrofit(userID, new OnRetrofitBucketListFinished() {
            @Override
            public void onFinished(List<BucketListModel> bucketListModelList) {
                iBucketListFragView.setProgressbarInvisible();

                if (bucketListModelList.get(0).getSuccess() == 0) {
                    // Empty no list
                    iBucketListFragView.setBucketModelsEmpty();
                    iBucketListFragView.setRecyclerView();
                    iBucketListFragView.setRecyclerViewInvisible();
                    iBucketListFragView.showTvNoBucket();
                } else {
                    Collections.sort(bucketListModelList, new BucketComparator());
                    iBucketListFragView.setBucketModels(bucketListModelList);
                    iBucketListFragView.setRecyclerView();
                    iBucketListFragView.setRecyclerViewVisible();
                }
            }

            @Override
            public void onError() {
                iBucketListFragView.setProgressbarInvisible();
                iBucketListFragView.displayErrorToast();
            }
        });
    }

    @Override
    public void fetchUserBucketListUpdate(int userID, final BucketListAdapter bucketListAdapter) {

        bucketListFragInteractor.fetchUserBucketListRetrofit(userID, new OnRetrofitBucketListFinished() {
            @Override
            public void onFinished(List<BucketListModel> bucketListModelList) {

                if (bucketListModelList.get(0).getSuccess() == 0) {
                    // Empty no list
                    iBucketListFragView.setRecyclerViewInvisible();
                    iBucketListFragView.showTvNoBucket();
                } else {
                    iBucketListFragView.setRecyclerViewVisible();
                    iBucketListFragView.hideTvNoBucket();
                    Collections.sort(bucketListModelList, new BucketComparator());
                    bucketListAdapter.swap(bucketListModelList);
                }
            }

            @Override
            public void onError() {
                iBucketListFragView.displayErrorToast();
            }
        });
    }

    @Override
    public void updateBucketVisibilityRetrofit(int userID, final int status) {

        bucketListFragInteractor.updateBucketVisibilityRetrofit(userID, status, new OnFinishedListenerRetrofit() {
            @Override
            public void onFinished(JsonObject jsonObject) {
                if (jsonObject.get("status").getAsInt() == 0) {
                    iBucketListFragView.displayErrorToast();
                } else {
                    iBucketListFragView.changeBucketStatLocalstore(status);
                }
            }

            @Override
            public void onError() {
                iBucketListFragView.displayErrorToast();
            }
        });
    }
}
