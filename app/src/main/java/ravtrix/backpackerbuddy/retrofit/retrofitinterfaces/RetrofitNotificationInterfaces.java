package ravtrix.backpackerbuddy.retrofit.retrofitinterfaces;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Ravinder on 1/10/17.
 */

public class RetrofitNotificationInterfaces {

    public interface GetNotificationStatus {
        @GET("/fetchNotificationStatus.php?userID=[userID]")
        Call<JsonObject> getNotificationStatus(@Query("userID") int userID);
    }

    public interface UpdateMessageNotification {
        @FormUrlEncoded
        @POST("/updateMessageNotification.php")
        Call<JsonObject> updateMessageNotification(@Field("userID") int userID);
    }

    public interface UpdateCommentNotification {
        @FormUrlEncoded
        @POST("/updateCommentNotif.php")
        Call<JsonObject> updateCommentNotification(@Field("userID") int userID);
    }
}
