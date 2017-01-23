package ravtrix.backpackerbuddy.activities.discussion.editdiscussioncomment;

/**
 * Created by Ravinder on 1/20/17.
 */

interface IEditDiscussionCommentView {
    void displayErrorToast();
    void hideKeyboard();
    void setResultCode(int code);
    void finished();
}
