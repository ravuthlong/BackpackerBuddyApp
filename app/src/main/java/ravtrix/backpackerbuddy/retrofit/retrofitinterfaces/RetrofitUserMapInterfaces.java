package ravtrix.backpackerbuddy.retrofit.retrofitinterfaces;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;

import ravtrix.backpackerbuddy.activities.usermap.model.Location;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Ravinder on 3/6/17.
 */

public interface RetrofitUserMapInterfaces {

    interface InsertIntoMap {
        @FormUrlEncoded
        @POST("/insertMap.php")
        Call<JsonObject> insertIntoMap(@FieldMap HashMap<String, String> mapInfo); //userID, lat, long
    }

    interface GetUserMap {
        @GET("/fetchUserMap.php?userID=[userID]")
        Call<List<Location>> getUserMap(@Query("userID") int userID);
    }

    interface RemoveMapLocation {
        @FormUrlEncoded
        @POST("/deleteMapLocation.php")
        Call<JsonObject> removeMapLocation(@Field("mapID") int mapID);
    }

    interface CheckHasMap {
        @GET("/fetchMapExist.php?userID=[userID]")
        Call<JsonObject> checkHasMap(@Query("userID") int userID);
    }
}
