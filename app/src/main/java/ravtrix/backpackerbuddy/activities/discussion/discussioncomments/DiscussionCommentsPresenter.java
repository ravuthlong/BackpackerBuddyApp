package ravtrix.backpackerbuddy.activities.discussion.discussioncomments;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;

import ravtrix.backpackerbuddy.recyclerviewfeed.commentdiscussionrecyclerview.CommentModel;
import ravtrix.backpackerbuddy.retrofit.retrofitinterfaces.OnFinishedListenerRetrofit;

/**
 * Created by Ravinder on 1/20/17.
 */

class DiscussionCommentsPresenter implements IDiscussionCommentsPresenter {
    private DiscussionCommentsInteractor discussionCommentsInteractor;
    private IDiscussionCommentsView iDiscussionCommentsView;

    DiscussionCommentsPresenter(IDiscussionCommentsView iDiscussionCommentsView) {
        this.iDiscussionCommentsView = iDiscussionCommentsView;
        this.discussionCommentsInteractor = new DiscussionCommentsInteractor();
    }

    @Override
    public void insertComment(final HashMap<String, String> discussionHash, final int userID, final int discussionID, final int ownerID) {

        iDiscussionCommentsView.showProgressDialog();

        discussionCommentsInteractor.insertCommentRetrofit(discussionHash, new OnFinishedListenerRetrofit() {
            @Override
            public void onFinished(JsonObject jsonObject) {
                if (jsonObject.get("status").getAsInt() == 0) {
                    iDiscussionCommentsView.displayErrorToast();
                } else {
                    // Hide keyboard
                    iDiscussionCommentsView.hideKeyboard();
                    iDiscussionCommentsView.clearEditText();
                    iDiscussionCommentsView.clearData();
                    // Success
                    incrementTotalComment(discussionID);
                    // Don't notify if user comments on their own post..
                    if (userID != ownerID) {
                        notifyTheOwner(ownerID, discussionHash.get("comment"), discussionID);
                    }
                    // Notify all other users that has commented in this post that is NOT the current poster
                    notifyOtherUsers(userID, ownerID, discussionHash.get("comment"), discussionID);
                    fetchDiscussionComments(userID, discussionID);
                }
                iDiscussionCommentsView.hideProgressDialog();
            }
            @Override
            public void onError() {
                iDiscussionCommentsView.displayErrorToast();
                iDiscussionCommentsView.hideProgressDialog();
            }
        });
    }

    @Override
    public void fetchDiscussionComments(int userID, int discussionID) {

        iDiscussionCommentsView.showProgressbar();
        discussionCommentsInteractor.fetchDiscussionComments(userID, discussionID, new OnRetrofitCommentModels() {
            @Override
            public void onFinished(List<CommentModel> commentModels, int status) {
                if (status == 1) {
                    iDiscussionCommentsView.setModels(commentModels);
                } else {
                    iDiscussionCommentsView.setModelsEmpty();
                }
                // Set recycler view/ adapter and show results
                iDiscussionCommentsView.setRecyclerView();
                iDiscussionCommentsView.showDisplayAfterLoading();
            }

            @Override
            public void onError() {
                iDiscussionCommentsView.displayErrorToast();
            }
        });
    }

    @Override
    public void fetchDiscussionCommentsRefresh(int userID, int discussionID) {

        iDiscussionCommentsView.showProgressbar();
        discussionCommentsInteractor.fetchDiscussionComments(userID, discussionID, new OnRetrofitCommentModels() {
            @Override
            public void onFinished(List<CommentModel> commentModels, int status) {
                iDiscussionCommentsView.hideProgressbar();
                if (status == 1) {
                    iDiscussionCommentsView.setModels(commentModels);
                } else {
                    iDiscussionCommentsView.setModelsEmpty();
                }
                // Load new models in
                iDiscussionCommentsView.swapModels(commentModels);
            }

            @Override
            public void onError() {
                iDiscussionCommentsView.hideProgressbar();
                iDiscussionCommentsView.displayErrorToast();
            }
        });
    }

    @Override
    public void notifyTheOwner(int userID, String comment, int discussionID) {
        discussionCommentsInteractor.notifyTheOwnerRetrofit(userID, comment, discussionID);
    }

    @Override
    public void notifyOtherUsers(int userID, int ownerID, String comment, int discussionID) {
        discussionCommentsInteractor.notifyOtherUsersRetrofit(userID, ownerID, comment, discussionID);
    }

    @Override
    public void incrementTotalComment(int discussionID) {
        discussionCommentsInteractor.incrementTotalComment(discussionID);
    }
}

