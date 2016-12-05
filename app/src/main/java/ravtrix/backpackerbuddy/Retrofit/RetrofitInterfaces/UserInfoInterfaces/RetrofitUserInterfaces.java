package ravtrix.backpackerbuddy.retrofit.retrofitinterfaces.UserInfoInterfaces;

import java.util.HashMap;

import ravtrix.backpackerbuddy.models.LoggedInUser;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Ravinder on 8/17/16.
 */


public class RetrofitUserInterfaces {

        public interface LogUserIn {
                @FormUrlEncoded
                @POST("/login.php")
                Call<LoggedInUser> userInfo(@FieldMap HashMap<String, String> user);
        }

        public interface SignUserUpPart1 {
                @FormUrlEncoded
                @POST("/register.php")
                Call<LoggedInUser> signUserUpPart1(@FieldMap HashMap<String, String> userInfo);
        }

        public interface SignUserUpPart2 {
                @FormUrlEncoded
                @POST("/register2.php")
                void signUserUpPart2(@FieldMap HashMap<String, String> moreUserInfo);
        }
}



