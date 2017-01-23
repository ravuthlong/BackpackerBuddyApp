package ravtrix.backpackerbuddy.activities.discussion.discussioncomments;

import java.util.List;

import ravtrix.backpackerbuddy.recyclerviewfeed.commentdiscussionrecyclerview.CommentModel;

/**
 * Created by Ravinder on 1/20/17.
 */

interface OnRetrofitCommentModels {
    void onFinished(List<CommentModel> commentModels, int status);
    void onError();
}
