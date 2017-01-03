package ravtrix.backpackerbuddy.retrofit.retrofitrequests;

import ravtrix.backpackerbuddy.helpers.Helpers;
import ravtrix.backpackerbuddy.retrofit.retrofitinterfaces.RetrofitBucketListInterfaces;
import retrofit2.Retrofit;

/**
 * Created by Ravinder on 1/1/17.
 */

public class RetrofitBucketList {
    private Retrofit retrofit;

    public RetrofitBucketList() {
        retrofit = Helpers.retrofitBuilder(Helpers.ServerURL.SERVER_URL);
    }

    public RetrofitBucketListInterfaces.InsertBucket insertBucket() {
        return retrofit.create(RetrofitBucketListInterfaces.InsertBucket.class);
    }

    public RetrofitBucketListInterfaces.DeleteBucket deleteBucket() {
        return retrofit.create(RetrofitBucketListInterfaces.DeleteBucket.class);
    }

    public RetrofitBucketListInterfaces.UpdateBucket updateBucket() {
        return retrofit.create(RetrofitBucketListInterfaces.UpdateBucket.class);
    }

    public RetrofitBucketListInterfaces.UpdateBucketStatus updateBucketStatus() {
        return retrofit.create(RetrofitBucketListInterfaces.UpdateBucketStatus.class);
    }

    public RetrofitBucketListInterfaces.FetchUserBucketList fetchUserBucketList() {
        return retrofit.create(RetrofitBucketListInterfaces.FetchUserBucketList.class);
    }
}
