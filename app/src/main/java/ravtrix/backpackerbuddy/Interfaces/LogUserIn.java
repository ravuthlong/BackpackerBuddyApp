package ravtrix.backpackerbuddy.Interfaces;

import java.util.HashMap;

import ravtrix.backpackerbuddy.Models.LoggedInUser;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Ravinder on 8/17/16.
 */
public interface LogUserIn {
        @FormUrlEncoded
        @POST("/login.php")
        Call<LoggedInUser> userInfo(@FieldMap HashMap<String, String> user);

}
