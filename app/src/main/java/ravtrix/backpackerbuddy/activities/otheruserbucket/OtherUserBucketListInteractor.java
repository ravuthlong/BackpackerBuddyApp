package ravtrix.backpackerbuddy.activities.otheruserbucket;

import java.util.List;

import ravtrix.backpackerbuddy.helpers.RetrofitUserBucketListSingleton;
import ravtrix.backpackerbuddy.recyclerviewfeed.bucketlistrecyclerview.data.BucketListModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ravinder on 1/22/17.
 */

class OtherUserBucketListInteractor implements IOtherUserBucketListInteractor {

    @Override
    public void fetchUserBucketListRetrofit(final String otherUserID, final OnRetrofitBucketModelsFinished onRetrofitBucketModelsFinished) {
        Call<List<BucketListModel>> retrofit = RetrofitUserBucketListSingleton.getRetrofitBucketList()
                .fetchUserBucketList()
                .fetchUserBucketList(Integer.parseInt(otherUserID));

        retrofit.enqueue(new Callback<List<BucketListModel>>() {
            @Override
            public void onResponse(Call<List<BucketListModel>> call, Response<List<BucketListModel>> response) {
                onRetrofitBucketModelsFinished.onFinished(response.body(), response.body().get(0).getSuccess());
            }

            @Override
            public void onFailure(Call<List<BucketListModel>> call, Throwable t) {
                onRetrofitBucketModelsFinished.onError();
            }
        });
    }
}
