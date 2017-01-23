package ravtrix.backpackerbuddy.activities.bucketlist.editbucket;

import com.google.gson.JsonObject;

import ravtrix.backpackerbuddy.helpers.RetrofitUserBucketListSingleton;
import ravtrix.backpackerbuddy.retrofit.retrofitinterfaces.OnFinishedListenerRetrofit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ravinder on 1/20/17.
 */

class EditBucketInteractor implements IEditBucketInteractor {
    @Override
    public void editBucketRetrofit(int bucketID, String newBucket,
                                   final OnFinishedListenerRetrofit onFinishedListenerRetrofit) {

        Call<JsonObject> retrofit = RetrofitUserBucketListSingleton.getRetrofitBucketList()
                .updateBucket()
                .updateBucket(bucketID, newBucket);

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
