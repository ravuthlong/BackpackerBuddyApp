package ravtrix.backpackerbuddy.retrofit.retrofitinterfaces;

import com.google.gson.JsonObject;

import java.util.List;

import ravtrix.backpackerbuddy.recyclerviewfeed.userinboxrecyclerview.data.FeedItemInbox;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Ravinder on 9/23/16.
 */

public class RetrofitUserChatInterfaces {

    public interface InsertNewChat {
        @FormUrlEncoded
        @POST("/insertNewChat.php")
        Call<JsonObject> insertNewChat(@Field("userOne") int userOne, @Field("userTwo") int userTwo);
    }

    public interface FetchUserInbox {
        @GET("/fetchUserChat.php?userID=[userID]")
        Call<List<FeedItemInbox>> fetchUserInbox(@Query("userID") int userID);
    }
}
