package ravtrix.backpackerbuddy.retrofit.retrofitinterfaces;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;

import ravtrix.backpackerbuddy.models.LoggedInUser;
import ravtrix.backpackerbuddy.models.UserLocationInfo;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Ravinder on 8/17/16.
 */

public class RetrofitUserInfoInterfaces {

    public interface LogUserIn {
            @FormUrlEncoded
            @POST("/login.php")
            Call<LoggedInUser> userInfo(@FieldMap HashMap<String, String> user);
    }

    public interface SignUserUpPart1 {
            @FormUrlEncoded
            @POST("/register.php")
            Call<JsonObject> signedUpStatus(@FieldMap HashMap<String, String> userInfo);
    }

    public interface UpdateUserDetail {
            @FormUrlEncoded
            @POST("/updateUserDetails.php")
            Call<JsonObject> updateUserDetail(@FieldMap HashMap<String, String> userDetail);
    }

    public interface GetUserDetails {
            @GET("/fetchUserDetails.php?userID=[userID]")
            Call<JsonObject> getUserDetails(@Query("userID") int userID);
    }
    public interface UpdateProfilePic {
            @FormUrlEncoded
            @POST("/updateProfilePhoto.php")
            Call<JsonObject> updateProfilePic(@FieldMap HashMap<String, String> userDetail);
    }
    public interface UpdateLocation {
            @FormUrlEncoded
            @POST("/updateUserLocation.php")
            Call<JsonObject> updateLocation(@FieldMap HashMap<String, String> userLocationDetail);
    }

    public interface GetNearbyUsers {
            @GET("/fetchNearbyUsers.php?userID=[userID]&radius=[radius]")
            Call<List<UserLocationInfo>> getNearbyUsers(@Query("userID") int userID, @Query("radius") int radius);
    }
}



