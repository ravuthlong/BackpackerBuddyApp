package ravtrix.backpackerbuddy.fragments.bucketlist;

import com.google.gson.JsonObject;

import java.util.List;

import ravtrix.backpackerbuddy.helpers.RetrofitUserBucketListSingleton;
import ravtrix.backpackerbuddy.helpers.RetrofitUserInfoSingleton;
import ravtrix.backpackerbuddy.recyclerviewfeed.bucketlistrecyclerview.data.BucketListModel;
import ravtrix.backpackerbuddy.retrofit.retrofitinterfaces.OnFinishedListenerRetrofit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ravinder on 1/26/17.
 */

class BucketListFragInteractor implements IBucketListFragInteractor {

    @Override
    public void fetchUserBucketListRetrofit(int userID, final OnRetrofitBucketListFinished onRetrofitBucketListFinished) {

        Call<List<BucketListModel>> retrofit = RetrofitUserBucketListSingleton.getRetrofitBucketList()
                .fetchUserBucketList()
                .fetchUserBucketList(userID);

        retrofit.enqueue(new Callback<List<BucketListModel>>() {
            @Override
            public void onResponse(Call<List<BucketListModel>> call, Response<List<BucketListModel>> response) {
                onRetrofitBucketListFinished.onFinished(response.body());
            }
            @Override
            public void onFailure(Call<List<BucketListModel>> call, Throwable t) {
                onRetrofitBucketListFinished.onError();
            }
        });
    }

    @Override
    public void updateBucketVisibilityRetrofit(int userID, int status, final OnFinishedListenerRetrofit onFinishedListenerRetrofit) {

        Call<JsonObject> retrofit = RetrofitUserInfoSingleton.getRetrofitUserInfo()
                .updateBucketVisibility()
                .updateBucketVisibility(userID, status);

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
