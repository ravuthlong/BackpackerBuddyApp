package ravtrix.backpackerbuddy.activities.bucketlist.newbucket;

import com.google.gson.JsonObject;

import java.util.HashMap;

import ravtrix.backpackerbuddy.helpers.RetrofitUserBucketListSingleton;
import ravtrix.backpackerbuddy.retrofit.retrofitinterfaces.OnFinishedListenerRetrofit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ravinder on 1/20/17.
 */

class BucketPostInteractor implements IBucketPostInteractor {

    @Override
    public void insertPostRetrofit(HashMap<String, String> bucketInfo, final OnFinishedListenerRetrofit onFinishedListenerRetrofit) {
        Call<JsonObject> retrofit = RetrofitUserBucketListSingleton.getRetrofitBucketList()
                .insertBucket()
                .insertBucket(bucketInfo);

        retrofit.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                // Send back to presenter
                onFinishedListenerRetrofit.onFinished(response.body());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                onFinishedListenerRetrofit.onError();
            }
        });
    }
}
