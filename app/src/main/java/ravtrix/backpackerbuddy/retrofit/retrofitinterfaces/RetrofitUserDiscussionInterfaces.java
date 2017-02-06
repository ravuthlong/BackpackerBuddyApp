package ravtrix.backpackerbuddy.retrofit.retrofitinterfaces;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;

import ravtrix.backpackerbuddy.recyclerviewfeed.commentdiscussionrecyclerview.CommentModel;
import ravtrix.backpackerbuddy.recyclerviewfeed.discussionroomrecyclerview.data.DiscussionModel;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Ravinder on 12/22/16.
 */

public class RetrofitUserDiscussionInterfaces {

    public interface InsertDiscussion {
        @FormUrlEncoded
        @POST("/insertDiscussion.php")
        Call<JsonObject> insertDiscussion(@FieldMap HashMap<String, String> newDiscussion);
    }

    public interface DeleteDiscussion {
        @FormUrlEncoded
        @POST("/deleteDiscussion.php")
        Call<JsonObject> deleteDiscussion(@Field("discussionID") int discussionID);
    }

    public interface InsertAndUpdateLove {
        @FormUrlEncoded
        @POST("/insertAndUpdateLove.php")
        Call<JsonObject> insertAndUpdateLove(@FieldMap HashMap<String, String> discussionInfo);
    }

    public interface RemoveAndUpdateLove {
        @FormUrlEncoded
        @POST("/removeAndUpdateLove.php")
        Call<JsonObject> removeAndUpdateLove(@FieldMap HashMap<String, String> discussionInfo);
    }

    public interface GetDiscussions {
        @GET("/fetchDiscussions.php?userID=[userID]")
        Call<List<DiscussionModel>> getDiscussions(@Query("userID") int userID);
    }

    public interface GetOneDiscussion {
        @GET("/fetchADiscussion1.10.php?discussionID=[discussionID]&userID=[userID]")
        Call<List<DiscussionModel>> getADiscussion(@Query("discussionID") int discussionID, @Query("userID") int userID);
    }

    public interface GetAUserDiscussions {
        @GET("/fetchAUserDiscussion.php?userID=[userID]")
        Call<List<DiscussionModel>> getAUserDiscussions(@Query("userID") int userID);
    }

    public interface IncrementCommentCount {
        @FormUrlEncoded
        @POST("/incrementDiscussionComment.php")
        Call<JsonObject> incrementCommentCount(@Field("discussionID")String discussionID);
    }

    public interface GetDiscussionComments {
        @GET("/fetchDiscussionComments.php?userID=[userID]&discussionID=[discussionID]")
        Call<List<CommentModel>> getDiscussionComments(@Query("userID") int userID, @Query("discussionID") int discussionID);
    }

    public interface RemoveCommentAndDecrement {
        @FormUrlEncoded
        @POST("/removeCommentAndDecrement.php")
        Call<JsonObject> removeCommentAndDecrement(@FieldMap HashMap<String, String> commentInfo);
    }

    /**
     * DiscussionInfo (userID, discussionID, comment, time)
     */
    public interface InsertComment {
        @FormUrlEncoded
        @POST("/insertDiscussionComment.php")
        Call<JsonObject> insertComment(@FieldMap HashMap<String, String> discussionInfo);
    }

    public interface UpdateComment {
        @FormUrlEncoded
        @POST("/updateComment.php")
        Call<JsonObject> updateComment(@Field("commentID") int commentID, @Field("comment") String comment);
    }

    public interface UpdateDiscussion {
        @FormUrlEncoded
        @POST("/updateDiscussion.php")
        Call<JsonObject> updateDiscussion(@Field("discussionID") int discussionID, @Field("post") String post);
    }

    public interface SendNotification {
        @FormUrlEncoded
        @POST("/sendNotificationComment.php")
        Call<JsonObject> sendNotification(@Field("userID") int userID, @Field("message") String message,
                                          @Field("discussionID") int discussionID);
    }

    public interface SendNotificationToOtherUsers {
        @FormUrlEncoded
        @POST("/sendNotificationCommentsOtherUsers1.9.php")
        Call<JsonObject> sendNotificationToOtherUsers(@Field("userID") int userID, @Field("ownerID") int ownerID,
                                                      @Field("message") String message, @Field("discussionID") int discussionID);
    }
}
