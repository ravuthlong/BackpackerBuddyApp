package ravtrix.backpackerbuddy.fragments.discussionroom;

import java.util.List;

import ravtrix.backpackerbuddy.helpers.RetrofitUserDiscussionSingleton;
import ravtrix.backpackerbuddy.recyclerviewfeed.discussionroomrecyclerview.data.DiscussionModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ravinder on 1/26/17.
 */

class DiscussionRoomFragInteractor implements IDiscussionRoomFragInteractor {

    @Override
    public void fetchDiscussionPostsRetrofit(int userID, final OnRetrofitDiscussionRoomFinished onRetrofitDiscussionRoomFinished) {
        Call<List<DiscussionModel>> retrofitCall = RetrofitUserDiscussionSingleton.getRetrofitUserDiscussion()
                .getDiscussions()
                .getDiscussions(userID);

        retrofitCall.enqueue(new Callback<List<DiscussionModel>>() {
            @Override
            public void onResponse(Call<List<DiscussionModel>> call, Response<List<DiscussionModel>> response) {
                onRetrofitDiscussionRoomFinished.onFinished(response.body());
            }
            @Override
            public void onFailure(Call<List<DiscussionModel>> call, Throwable t) {
               onRetrofitDiscussionRoomFinished.onError();
            }
        });
    }
}
