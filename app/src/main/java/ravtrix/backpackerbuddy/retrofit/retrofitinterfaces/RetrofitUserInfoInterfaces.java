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

    public interface DeleteProfilePic {
        @FormUrlEncoded
        @POST("/deletePhotoFile.php")
        Call<JsonObject> deleteProfilePic(@FieldMap HashMap<String, String> userDetail);
    }

    public interface UpdateLocation {
        @FormUrlEncoded
        @POST("/updateUserLocation.php")
        Call<JsonObject> updateLocation(@FieldMap HashMap<String, String> userLocationDetail);
    }

    public interface UpdateTravelingStatus {
        @FormUrlEncoded
        @POST("/updateTravelingStatus.php")
        Call<JsonObject> updateTravelStatus(@FieldMap HashMap<String, String> userStatusInfo);
    }

    public interface GetNearbyUsers {
        @GET("/fetchNearbyUsers.php?userID=[userID]&radius=[radius]")
        Call<List<UserLocationInfo>> getNearbyUsers(@Query("userID") int userID, @Query("radius") int radius);
    }

    public interface GetRecentlyOnlineUsers {
        @GET("/fetchRecentlyOnlineUsers.php?userID=[userID]")
        Call<List<UserLocationInfo>> getRecentlyOnlineUsers(@Query("userID") int userID);
    }

    public interface IsUsernameTaken {
        @GET("/checkUsernameTaken.php?username=[username]")
        Call<JsonObject> isUsernameTaken(@Query("username") String username);
    }

    public interface ChangePassword {
        @FormUrlEncoded
        @POST("/changePassword.php")
        Call<JsonObject> changePassword(@FieldMap HashMap<String, String> user);
    }

    public interface ChangeEmail {
        @FormUrlEncoded
        @POST("/changeEmail.php")
        Call<JsonObject> changeEmail(@FieldMap HashMap<String, String> user);
    }

    public interface InsertNotificationToken {
        @FormUrlEncoded
        @POST("/insertNotificationToken.php")
        Call<JsonObject> insertToken(@FieldMap HashMap<String, String> user);
    }
}



