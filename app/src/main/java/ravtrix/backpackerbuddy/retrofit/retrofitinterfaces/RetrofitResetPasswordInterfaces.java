package ravtrix.backpackerbuddy.retrofit.retrofitinterfaces;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Ravinder on 3/26/17.
 */

public class RetrofitResetPasswordInterfaces {

    public interface UpdateResetToken {
        @FormUrlEncoded
        @POST("/updateResetToken.php")
        Call<JsonObject> updateResetToken(@Field("email") String email, @Field("token") String token);
    }

    public interface SendResetEmail {
        @GET("/sendEmail.php")
        Call<Void> sendResetEmail(@Query("email") String email, @Query("token") String token);
    }
}
