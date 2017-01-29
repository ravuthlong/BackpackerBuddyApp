package ravtrix.backpackerbuddy.fragments.discussionroom;

import java.util.List;

import ravtrix.backpackerbuddy.recyclerviewfeed.discussionroomrecyclerview.data.DiscussionModel;

/**
 * Created by Ravinder on 1/26/17.
 */

interface OnRetrofitDiscussionRoomFinished {
    void onFinished(List<DiscussionModel> discussionModels);
    void onError();
}
