package ravtrix.backpackerbuddy.activities.discussion.insertdiscussion;

import com.google.gson.JsonObject;

import java.util.HashMap;

import ravtrix.backpackerbuddy.helpers.RetrofitUserDiscussionSingleton;
import ravtrix.backpackerbuddy.retrofit.retrofitinterfaces.OnFinishedListenerRetrofit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ravinder on 1/20/17.
 */

class DiscussionPostInteractor implements IDiscussionPostInteractor {

    @Override
    public void insertDiscussionRetrofit(HashMap<String, String> newDiscussion, final OnFinishedListenerRetrofit onFinishedListenerRetrofit) {
        Call<JsonObject> retrofitCall =  RetrofitUserDiscussionSingleton.getRetrofitUserDiscussion()
                .insertDiscussion()
                .insertDiscussion(newDiscussion);

        retrofitCall.enqueue(new Callback<JsonObject>() {
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
