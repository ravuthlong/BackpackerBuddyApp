package ravtrix.backpackerbuddy.activities.discussion.editdiscussioncomment;

import com.google.gson.JsonObject;

import ravtrix.backpackerbuddy.helpers.RetrofitUserDiscussionSingleton;
import ravtrix.backpackerbuddy.retrofit.retrofitinterfaces.OnFinishedListenerRetrofit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ravinder on 1/20/17.
 */

class EditDiscussionCommentInteractor implements IEditDiscussionCommentInteractor {

    @Override
    public void updateCommentRetrofit(int commentID, String comment, final OnFinishedListenerRetrofit onFinishedListenerRetrofit) {
        Call<JsonObject> retrofit = RetrofitUserDiscussionSingleton.getRetrofitUserDiscussion()
                .updateComment()
                .updateComment(commentID, comment);
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
}
