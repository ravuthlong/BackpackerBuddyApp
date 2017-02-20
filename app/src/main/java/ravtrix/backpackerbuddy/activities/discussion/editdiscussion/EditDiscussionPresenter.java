package ravtrix.backpackerbuddy.activities.discussion.editdiscussion;

import com.google.gson.JsonObject;

import ravtrix.backpackerbuddy.retrofit.retrofitinterfaces.OnFinishedListenerRetrofit;

/**
 * Created by Ravinder on 1/20/17.
 */

class EditDiscussionPresenter implements IEditDiscussionPresenter {
    private IEditDiscussionView iEditDiscussionView;
    private EditDiscussionInteractor editDiscussionInteractor;

    EditDiscussionPresenter(IEditDiscussionView iEditDiscussionView) {
        this.iEditDiscussionView = iEditDiscussionView;
        this.editDiscussionInteractor = new EditDiscussionInteractor();
    }
    @Override
    public void editDiscussion(int discussionID, String newDiscussion, String countryTag) {
        editDiscussionInteractor.editDiscussionInteractor(discussionID, newDiscussion, countryTag, new OnFinishedListenerRetrofit() {
            @Override
            public void onFinished(JsonObject jsonObject) {
                if (jsonObject.get("status").getAsInt() == 1) {
                    iEditDiscussionView.setResultCode(1);
                    iEditDiscussionView.finished();
                } else {
                    iEditDiscussionView.displayErrorToast();
                }
            }

            @Override
            public void onError() {
                iEditDiscussionView.displayErrorToast();
            }
        });
    }
}
