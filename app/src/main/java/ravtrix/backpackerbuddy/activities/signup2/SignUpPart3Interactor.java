package ravtrix.backpackerbuddy.activities.signup2;

import com.google.gson.JsonObject;

import java.util.HashMap;

import ravtrix.backpackerbuddy.helpers.RetrofitUserInfoSingleton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ravinder on 9/28/16.
 */

class SignUpPart3Interactor implements ISignUpPart3Interactor {

    @Override
    public void signUserUpRetrofit(HashMap<String, String> userInfo, final OnRetrofitSignUp3 onRetrofitSignUp3) {
        // Make Retrofit call to communicate with the server
        Call<JsonObject> returnedStatus = RetrofitUserInfoSingleton.getRetrofitUserInfo().signUserUpPart1().signedUpStatus(userInfo);
        returnedStatus.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject statusJSON = response.body();

                int userStatus = statusJSON.get("status").getAsInt();

                // Sign up success
                if (userStatus == 1) {
                    onRetrofitSignUp3.onSuccess(statusJSON);

                } else {
                    onRetrofitSignUp3.onFailure();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                onRetrofitSignUp3.onFailure();
            }
        });
    }
}
