package ravtrix.backpackerbuddy.fragments.discussionroom;

import java.util.HashMap;
import java.util.List;

import ravtrix.backpackerbuddy.recyclerviewfeed.discussionroomrecyclerview.data.DiscussionModel;

/**
 * Created by Ravinder on 1/26/17.
 */

class DiscussionRoomFragPresenter implements IDiscussionRoomFragPresenter {

    private IDiscussionRoomFragView iDiscussionRoomFragView;
    private DiscussionRoomFragInteractor discussionRoomFragInteractor;

    DiscussionRoomFragPresenter(IDiscussionRoomFragView iDiscussionRoomFragView) {
        this.iDiscussionRoomFragView = iDiscussionRoomFragView;
        this.discussionRoomFragInteractor = new DiscussionRoomFragInteractor();
    }

    @Override
    public void fetchDiscussionPosts(int userID) {

        discussionRoomFragInteractor.fetchDiscussionPostsRetrofit(userID, new OnRetrofitDiscussionRoomFinished() {
            @Override
            public void onFinished(List<DiscussionModel> discussionModels) {
                if (discussionModels.get(0).getSuccess() == 1) {
                    iDiscussionRoomFragView.setDiscussionModels(discussionModels);
                } else {
                    iDiscussionRoomFragView.setDiscussionModelsEmpty();
                }
                iDiscussionRoomFragView.setRecyclerView();
                displayAfterLoading();
            }
            @Override
            public void onError() {
                iDiscussionRoomFragView.displayErrorToast();
            }
        });
    }

    @Override
    public void fetchDiscussionPostsRefresh(int userID) {

        discussionRoomFragInteractor.fetchDiscussionPostsRetrofit(userID, new OnRetrofitDiscussionRoomFinished() {
            @Override
            public void onFinished(List<DiscussionModel> discussionModels) {
                if (discussionModels.get(0).getSuccess() == 1) {
                    iDiscussionRoomFragView.swapData(discussionModels);
                } else {
                    iDiscussionRoomFragView.setDiscussionModelsEmpty();
                    iDiscussionRoomFragView.displayNoResultSnack();
                }
                displayAfterLoading();
            }

            @Override
            public void onError() {
                iDiscussionRoomFragView.displayErrorToast();
            }
        });
    }

    @Override
    public void fetchDiscussionPostsFilter(HashMap<String, String> postHash) {

        discussionRoomFragInteractor.fetchDiscussionFilterPostsRetrofit(postHash, new OnRetrofitDiscussionRoomFinished() {
            @Override
            public void onFinished(List<DiscussionModel> discussionModels) {
                if (discussionModels.get(0).getSuccess() == 1) {
                    iDiscussionRoomFragView.setDiscussionModels(discussionModels);
                    iDiscussionRoomFragView.swapData(discussionModels);
                } else {
                    iDiscussionRoomFragView.setDiscussionModelsEmpty();
                    iDiscussionRoomFragView.displayNoResultSnack();
                }
            }

            @Override
            public void onError() {
                iDiscussionRoomFragView.displayErrorToast();
            }
        });
    }

    private void displayAfterLoading() {
        // stop progress bar
        iDiscussionRoomFragView.showSwipeLayout();
        iDiscussionRoomFragView.hideProgressBar();

        // stop refreshing layout is it is running
        iDiscussionRoomFragView.stopSwipeRefreshing();
    }
}
