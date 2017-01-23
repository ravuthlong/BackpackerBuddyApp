package ravtrix.backpackerbuddy.activities.discussion.discussioncomments;

import java.util.List;

import ravtrix.backpackerbuddy.recyclerviewfeed.commentdiscussionrecyclerview.CommentModel;

/**
 * Created by Ravinder on 1/20/17.
 */

interface IDiscussionCommentsView {
    void hideKeyboard();
    void displayErrorToast();
    void setModelsEmpty();
    void setModels(List<CommentModel> commentModels);
    void setRecyclerView();
    void hideRecyclerView();
    void showDisplayAfterLoading();
    void hideProgressbar();
    void showProgressbar();
    void clearEditText();
    void clearData();
    void swapModels(List<CommentModel> commentModels);
}
