package ravtrix.backpackerbuddy.retrofit.retrofitinterfaces;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;

import ravtrix.backpackerbuddy.recyclerviewfeed.bucketlistrecyclerview.data.BucketListModel;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Ravinder on 1/1/17.
 */

public class RetrofitBucketListInterfaces {

    public interface InsertBucket {
        @FormUrlEncoded
        @POST("/insertBucketList.php")
        Call<JsonObject> insertBucket(@FieldMap HashMap<String, String> newBucket);
    }


    public interface DeleteBucket {
        @FormUrlEncoded
        @POST("/deleteBucketList.php")
        Call<JsonObject> deleteBucket(@Field("bucketID") int bucketID);
    }

    public interface UpdateBucket {
        @FormUrlEncoded
        @POST("/updateBucketList.php")
        Call<JsonObject> updateBucket(@Field("bucketID") int bucketID, @Field("post") String post);
    }

    public interface UpdateBucketStatus {
        @FormUrlEncoded
        @POST("/updateBucketStatus.php")
        Call<JsonObject> updateBucketStatus(@Field("bucketID") int bucketID, @Field("status") int status);
    }

    public interface FetchUserBucketList {
        @GET("/fetchBucketList.php?userID=[userID]")
        Call<List<BucketListModel>> fetchUserBucketList(@Query("userID") int userID);
    }

    public interface FetchUserBucketStatus {
        @GET("/fetchBucketStatus.php?userID=[userID]")
        Call<JsonObject> fetchUserBucketStatus(@Query("userID") int userID);
    }
}
