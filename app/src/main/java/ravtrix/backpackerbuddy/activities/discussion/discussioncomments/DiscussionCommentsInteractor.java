package ravtrix.backpackerbuddy.activities.discussion.discussioncomments;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;

import ravtrix.backpackerbuddy.helpers.RetrofitUserDiscussionSingleton;
import ravtrix.backpackerbuddy.recyclerviewfeed.commentdiscussionrecyclerview.CommentModel;
import ravtrix.backpackerbuddy.retrofit.retrofitinterfaces.OnFinishedListenerRetrofit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ravinder on 1/20/17.
 */

class DiscussionCommentsInteractor implements IDiscussionCommentsInteractor {

    @Override
    public void insertCommentRetrofit(HashMap<String, String> discussionHash, final OnFinishedListenerRetrofit onFinishedListenerRetrofit) {

        Call<JsonObject> retrofit = RetrofitUserDiscussionSingleton.getRetrofitUserDiscussion()
                .insertComment()
                .insertComment(discussionHash);

        retrofit.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
               onFinishedListenerRetrofit.onFinished(response.body());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                onFinishedListenerRetrofit.onError();
            }
        });
    }

    @Override
    public void fetchDiscussionComments(int userID, int discussionID, final OnRetrofitCommentModels onRetrofitCommentModels) {
        Call<List<CommentModel>> retrofit = RetrofitUserDiscussionSingleton.getRetrofitUserDiscussion()
                .getDiscussionComments()
                .getDiscussionComments(userID, discussionID);

        retrofit.enqueue(new Callback<List<CommentModel>>() {
            @Override
            public void onResponse(Call<List<CommentModel>> call, Response<List<CommentModel>> response) {
                onRetrofitCommentModels.onFinished(response.body(), response.body().get(0).getSuccess());
            }

            @Override
            public void onFailure(Call<List<CommentModel>> call, Throwable t) {
                onRetrofitCommentModels.onError();
            }
        });
    }

    @Override
    public void notifyTheOwnerRetrofit(int userID, String comment, int discussionID) {
        Call<JsonObject> retrofit = RetrofitUserDiscussionSingleton.getRetrofitUserDiscussion()
                .sendNotification()
                .sendNotification(userID, comment, discussionID);

        retrofit.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {}
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });
    }

    @Override
    public void incrementTotalComment(int discussionID) {
        Call<JsonObject> retrofit = RetrofitUserDiscussionSingleton.getRetrofitUserDiscussion()
                .incrementCommentCount()
                .incrementCommentCount(Integer.toString(discussionID));

        retrofit.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {}
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {}
        });
    }
}
