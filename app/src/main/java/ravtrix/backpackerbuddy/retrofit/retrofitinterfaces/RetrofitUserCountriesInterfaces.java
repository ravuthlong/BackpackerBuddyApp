package ravtrix.backpackerbuddy.retrofit.retrofitinterfaces;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;

import ravtrix.backpackerbuddy.recyclerviewfeed.ausercountryrecyclerview.data.FeedItemAUserCountry;
import ravtrix.backpackerbuddy.recyclerviewfeed.travelpostsrecyclerview.data.FeedItem;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Ravinder on 8/31/16.
 */
public class RetrofitUserCountriesInterfaces {

    public interface GetNotLoggedInCountryPosts {
        @GET("/fetchActivities.php?userID=[userID]")
        Call<List<FeedItem>> countryPosts(@Query("userID") int userID);
    }

    public interface GetFilteredPosts {
        @GET("/fetchPostsWithFilter2.php?month=[month]&country=[country]")
        Call<List<FeedItem>> getFilterdPosts(@Query("month") int month,
                                             @Query("country") String country);
    }

    public interface GetAUserCountryPosts {
        @GET("/fetchAUserPosts.php?userID=[userID]")
        Call<List<FeedItemAUserCountry>> getAUserCountryPosts(@Query("userID") int userID);
    }

    public interface InsertTravelSpot {
        @FormUrlEncoded
        @POST("/insertTravelSpot.php")
        Call<JsonObject> travelSpot(@FieldMap HashMap<String, String> travelSpotInfo);
    }

    public interface InsertFavorite {
        @FormUrlEncoded
        @POST("/insertFavorite.php")
        Call<JsonObject> insertFavorite(@FieldMap HashMap<String, String> favoriteInfo);
    }

    public interface RemoveFavorite {
        @FormUrlEncoded
        @POST("/removeFromFavorite.php")
        Call<JsonObject> removeFavorite(@FieldMap HashMap<String, String> favoriteInfo);
    }

    public interface RemovePost {
        @FormUrlEncoded
        @POST("/removePost.php")
        Call<JsonObject> removePost(@Field("postID") int postID );
    }

    public interface UpdateTravelSpot {
        @FormUrlEncoded
        @POST("/updateTravelSpot.php")
        Call<JsonObject> updateTravelSpot(@FieldMap HashMap<String, String> travelSpotInfo);
    }
}
