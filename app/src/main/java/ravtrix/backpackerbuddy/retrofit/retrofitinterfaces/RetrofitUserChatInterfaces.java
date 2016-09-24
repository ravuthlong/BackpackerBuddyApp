package ravtrix.backpackerbuddy.retrofit.retrofitinterfaces;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Ravinder on 9/23/16.
 */

public class RetrofitUserChatInterfaces {

    public interface InsertNewChat {
        @FormUrlEncoded
        @POST("/insertNewChat.php")
        Call<JsonObject> insertNewChat(@Field("userOne") int userOne, @Field("userTwo") int userTwo);
    }
}
