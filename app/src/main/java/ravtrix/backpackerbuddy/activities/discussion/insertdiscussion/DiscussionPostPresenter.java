package ravtrix.backpackerbuddy.activities.discussion.insertdiscussion;

import com.google.gson.JsonObject;

import java.util.HashMap;

import ravtrix.backpackerbuddy.retrofit.retrofitinterfaces.OnFinishedListenerRetrofit;

/**
 * Created by Ravinder on 1/20/17.
 */

class DiscussionPostPresenter implements IDiscussionPostPresenter {
    private IDiscussionPostView iDiscussionPostView;
    private DiscussionPostInteractor discussionPostInteractor;

    DiscussionPostPresenter(IDiscussionPostView iDiscussionPostView) {
        this.iDiscussionPostView = iDiscussionPostView;
        this.discussionPostInteractor = new DiscussionPostInteractor();
    }

    @Override
    public void insertDiscussion(HashMap<String, String> newDiscussion) {

        iDiscussionPostView.showProgressDialog();

        discussionPostInteractor.insertDiscussionRetrofit(newDiscussion, new OnFinishedListenerRetrofit() {
            @Override
            public void onFinished(JsonObject jsonObject) {
                if (jsonObject.get("status").getAsInt() == 1) { //success
                    iDiscussionPostView.startDiscussionPostActivity();
                } else { //error
                    iDiscussionPostView.displayErrorToast();
                }
                iDiscussionPostView.hideProgressDialog();
            }

            @Override
            public void onError() {
                iDiscussionPostView.displayErrorToast();
                iDiscussionPostView.hideProgressDialog();
            }
        });
    }
}
