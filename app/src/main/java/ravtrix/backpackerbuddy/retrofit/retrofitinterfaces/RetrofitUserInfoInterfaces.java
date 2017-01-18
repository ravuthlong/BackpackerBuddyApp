package ravtrix.backpackerbuddy.retrofit.retrofitinterfaces;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;

import ravtrix.backpackerbuddy.models.LoggedInUser;
import ravtrix.backpackerbuddy.models.UserLocationInfo;
import retrofit2.Call;
import retrofit2.http.Field;
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

    public interface UpdateLocalstore {
        @GET("/userInfoLocalstore.php?userID=[userID]")
        Call<LoggedInUser> updateLocalstore(@Query("userID") int userID);
    }

    public interface LogUserInFacebook {
        @FormUrlEncoded
        @POST("/loginfacebook.php")
        Call<LoggedInUser> logUserInFacebook(@Field("email") String email, @Field("token") String token);
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
        @POST("/updateTravelStatus1.4.php")
        Call<JsonObject> updateTravelStatus(@FieldMap HashMap<String, String> userStatusInfo);
    }

    public interface UpdateBucketVisibility {
        @FormUrlEncoded
        @POST("/updateBucketVisibility.php")
        Call<JsonObject> updateBucketVisibility(@Field("userID") int userID, @Field("status") int status);
    }

    public interface GetNearbyUsers {
        @GET("/fetchNearbyUsers.php?userID=[userID]&radius=[radius]")
        Call<List<UserLocationInfo>> getNearbyUsers(@Query("userID") int userID, @Query("radius") int radius);
    }

    public interface GetNearbyUsersGuest {
        @GET("/fetchNearbyUsersGuest.php?latitude=[latitude]&longitude=[longitude]&radius=[radius]")
        Call<List<UserLocationInfo>> getNearbyUsersGuest(@Query("latitude") String latitude,
                                                    @Query("longitude") String longitude, @Query("radius") int radius);
    }

    public interface GetRecentlyOnlineUsers {
        @GET("/fetchRecentlyOnlineUsers.php?userID=[userID]")
        Call<List<UserLocationInfo>> getRecentlyOnlineUsers(@Query("userID") int userID);
    }

    public interface IsUsernameTaken {
        @GET("/checkUsernameTaken.php?username=[username]")
        Call<JsonObject> isUsernameTaken(@Query("username") String username);
    }

    public interface IsUsernameOrEmailTaken {
        @GET("/checkUsernameEmailTaken.php?username=[username]&email=[email]")
        Call<JsonObject> isUsernameOrEmailTaken(@Query("username") String username, @Query("email") String email);
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

    public interface ChangeUsername {
        @FormUrlEncoded
        @POST("/changeUsername.php")
        Call<JsonObject> changeusername(@FieldMap HashMap<String, String> user);
    }

    public interface ChangeUsernameFacebook {
        @FormUrlEncoded
        @POST("/changeUsernameFacebook.php")
        Call<JsonObject> changeusernameFacebook(@FieldMap HashMap<String, String> user);
    }

    public interface UpdateUserCountry {
        @FormUrlEncoded
        @POST("/updateUserCountry1.7.php")
        Call<JsonObject> updateUserCountry(@Field("username") String username, @Field("country") String country);
    }
}



