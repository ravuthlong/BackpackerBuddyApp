package ravtrix.backpackerbuddy.activities.discussion.editdiscussion;

import com.google.gson.JsonObject;

import ravtrix.backpackerbuddy.helpers.RetrofitUserDiscussionSingleton;
import ravtrix.backpackerbuddy.retrofit.retrofitinterfaces.OnFinishedListenerRetrofit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ravinder on 1/20/17.
 */

public class EditDiscussionInteractor implements IEditDiscussionInteractor {
    @Override
    public void editDiscussionInteractor(int discussionID, String newDiscussion, String countryTag, final OnFinishedListenerRetrofit onFinishedListenerRetrofit) {
        Call<JsonObject> retrofit = RetrofitUserDiscussionSingleton.getRetrofitUserDiscussion()
                .updateDiscussion()
                .updateDiscussion(discussionID, newDiscussion, countryTag);

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
