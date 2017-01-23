package ravtrix.backpackerbuddy.activities.discussion.editdiscussioncomment;

import com.google.gson.JsonObject;

import ravtrix.backpackerbuddy.retrofit.retrofitinterfaces.OnFinishedListenerRetrofit;

/**
 * Created by Ravinder on 1/20/17.
 */

class EditDiscussionCommentPresenter implements IEditDiscussionCommentPresenter {

    private IEditDiscussionCommentView iEditDiscussionCommentView;
    private EditDiscussionCommentInteractor editDiscussionCommentInteractor;

    EditDiscussionCommentPresenter(IEditDiscussionCommentView iEditDiscussionCommentView) {
        this.iEditDiscussionCommentView = iEditDiscussionCommentView;
        this.editDiscussionCommentInteractor = new EditDiscussionCommentInteractor();
    }

    @Override
    public void updateComment(int commentID, String comment) {

        editDiscussionCommentInteractor.updateCommentRetrofit(commentID, comment, new OnFinishedListenerRetrofit() {
            @Override
            public void onFinished(JsonObject jsonObject) {
                if (jsonObject.get("status").getAsInt() == 1) {

                    // Hide soft keyboard
                    iEditDiscussionCommentView.hideKeyboard();
                    iEditDiscussionCommentView.setResultCode(1);
                    iEditDiscussionCommentView.finished();
                } else {
                    iEditDiscussionCommentView.displayErrorToast();
                }
            }
            @Override
            public void onError() {
                iEditDiscussionCommentView.displayErrorToast();
            }
        });
    }
}
