package ravtrix.backpackerbuddy.retrofit.retrofitinterfaces;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;

import ravtrix.backpackerbuddy.recyclerviewfeed.discussionroomrecyclerview.DiscussionModel;
import retrofit2.Call;
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

    public interface GetDiscussions {
        @GET("/fetchDiscussions.php?userID=[userID]")
        Call<List<DiscussionModel>> getDiscussions(@Query("userID") int userID);
    }

}
