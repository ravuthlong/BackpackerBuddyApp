package ravtrix.backpackerbuddy.activities.otheruserbucket;

import java.util.Collections;
import java.util.List;

import ravtrix.backpackerbuddy.BucketComparator;
import ravtrix.backpackerbuddy.recyclerviewfeed.bucketlistrecyclerview.data.BucketListModel;

/**
 * Created by Ravinder on 1/22/17.
 */

class OtherUserBucketListPresenter implements IOtherUserBucketListPresenter {
    private IOtherUserBucketListView iOtherUserBucketListView;
    private OtherUserBucketListInteractor otherUserBucketListInteractor;

    OtherUserBucketListPresenter(IOtherUserBucketListView iOtherUserBucketListView) {
        this.iOtherUserBucketListView = iOtherUserBucketListView;
        this.otherUserBucketListInteractor = new OtherUserBucketListInteractor();
    }

    @Override
    public void fetchUserBucketList(String otherUserID) {
        iOtherUserBucketListView.showProgressBar();

        otherUserBucketListInteractor.fetchUserBucketListRetrofit(otherUserID, new OnRetrofitBucketModelsFinished() {
            @Override
            public void onFinished(List<BucketListModel> bucketListModels, int status) {
                iOtherUserBucketListView.hideProgressBar();

                if (status == 0) {
                    // Empty list
                    iOtherUserBucketListView.hideRecyclerView();
                    iOtherUserBucketListView.showTvNoBucket();
                } else {
                    Collections.sort(bucketListModels, new BucketComparator());
                    iOtherUserBucketListView.setBucketListModels(bucketListModels);
                    iOtherUserBucketListView.setRecyclerView();
                }
            }

            @Override
            public void onError() {
                iOtherUserBucketListView.hideProgressBar();
                iOtherUserBucketListView.displayErrorToast();
            }
        });
    }
}
