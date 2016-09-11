package ravtrix.backpackerbuddy.retrofit.retrofitinterfaces.usercountriesinterfaces;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;

import ravtrix.backpackerbuddy.recyclerviewfeed.mainrecyclerview.data.FeedItem;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Ravinder on 8/31/16.
 */
public class RetrofitUserCountriesInterfaces {

    public interface getNotLoggedInCountryPosts {
        @GET("/fetchActivities.php")
        Call<List<FeedItem>> countryPosts();
    }

    public interface insertTravelSpot {
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
}
