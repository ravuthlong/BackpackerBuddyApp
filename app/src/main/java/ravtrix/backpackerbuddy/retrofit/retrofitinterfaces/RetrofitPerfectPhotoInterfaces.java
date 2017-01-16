package ravtrix.backpackerbuddy.retrofit.retrofitinterfaces;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;

import ravtrix.backpackerbuddy.recyclerviewfeed.commentphotorecyclerview.PhotoCommentModel;
import ravtrix.backpackerbuddy.recyclerviewfeed.perfectphotorecyclerview.PerfectPhotoModel;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Ravinder on 1/12/17.
 */

public class RetrofitPerfectPhotoInterfaces {

    public interface InsertPerfectPhoto {
        @FormUrlEncoded
        @POST("/insetPerfectPhoto1.5.php")
        Call<JsonObject> insertPerfectPhoto(@FieldMap HashMap<String, String> perfectPhoto);
    }

    public interface GetPerfectPhotos {
        @GET("/fetchPerfectPhotos1.5.php?userID=[userID]")
        Call<List<PerfectPhotoModel>> getPerfectPhotos(@Query("userID") int userID);
    }

    public interface InsertAndUpdateLovePhoto {
        @FormUrlEncoded
        @POST("/insertAndUpdateLovePhoto1.5.php")
        Call<JsonObject> insertAndUpdateLovePhoto(@FieldMap HashMap<String, String> photoInfo);
    }

    public interface RemoveAndUpdateLovePhoto {
        @FormUrlEncoded
        @POST("/removeAndUpdateLovePhoto1.5.php")
        Call<JsonObject> removeAndUpdateLovePhoto(@FieldMap HashMap<String, String> photoInfo);
    }

    public interface IncrementPhotoCommentCount {
        @FormUrlEncoded
        @POST("/incrementPhotoComment1.5.php")
        Call<JsonObject> incrementPhotoCommentCount(@Field("photoID")String photoID);
    }

    public interface InsertPhotoComment {
        @FormUrlEncoded
        @POST("/insertPhotoComment1.5.php")
        Call<JsonObject> insertPhotoComment(@FieldMap HashMap<String, String> photoInfo);
    }

    public interface GetPhotoComments {
        @GET("/fetchPhotoComments1.5.php?photoID=[photoID]")
        Call<List<PhotoCommentModel>> getPhotoComments(@Query("photoID") int photoID);
    }

    public interface RemoveCommentAndDecrement {
        @FormUrlEncoded
        @POST("/removePhotoCommentDecrement1.5.php")
        Call<JsonObject> removeCommentAndDecrement(@Field("commentID") int commentID, @Field("photoID") int photoID);
    }

    public interface UpdatePhotoComment {
        @FormUrlEncoded
        @POST("/updatePhotoComment1.5.php")
        Call<JsonObject> updatePhotoComment(@Field("commentID") int commentID, @Field("comment") String comment);
    }

    public interface DeletePerfectPhoto {
        @FormUrlEncoded
        @POST("/deletePerfectPhoto1.5.php")
        Call<JsonObject> deletePerfectPhoto(@Field("photoID") int photoID, @Field("path") String path,
                                            @Field("deletePath") String deletePath);
    }

    public interface UpdatePhotoPost {
        @FormUrlEncoded
        @POST("/updatePerfectPhoto1.5.php")
        Call<JsonObject> updatePhotoPost(@Field("photoID") int photoID, @Field("post") String post);
    }
}
