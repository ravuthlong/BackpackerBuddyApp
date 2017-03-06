package ravtrix.backpackerbuddy.activities.signup1;

import com.google.gson.JsonObject;

import java.util.HashMap;

import ravtrix.backpackerbuddy.helpers.RetrofitUserInfoSingleton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ravinder on 9/28/16.
 */

class SignUpPart1Interactor implements ISignUpPart1Interactor {

    @Override
    public void isUsernameTaken(String username, String email, final OnRetrofitSignUp1 onRetrofitSignUp1) {
        Call<JsonObject> retrofitCall = RetrofitUserInfoSingleton.getRetrofitUserInfo()
                .isUsernameOrEmailTaken()
                .isUsernameOrEmailTaken(username, email);

        retrofitCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                // Username is taken
                if (response.body().get("usernametaken").getAsInt() == 1 &&
                        response.body().get("emailtaken").getAsInt() == 1) {
                    onRetrofitSignUp1.onUsernameAndEmailTaken();
                } else if (response.body().get("usernametaken").getAsInt() == 1) {
                    onRetrofitSignUp1.onUsernameTaken();
                } else if (response.body().get("emailtaken").getAsInt() == 1) {
                    onRetrofitSignUp1.onEmailTaken();
                } else {
                    onRetrofitSignUp1.onUsernameAndEmailNotTaken();
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                onRetrofitSignUp1.onError();
            }
        });
    }

    @Override
    public void signUserUpRetrofit(final HashMap<String, String> userInfo, final OnRetrofitSignUp1SuccessFail onRetrofitSignUp1) {
        // Make Retrofit call to communicate with the server
        Call<JsonObject> returnedStatus = RetrofitUserInfoSingleton.getRetrofitUserInfo().signUserUpPart1().signedUpStatus(userInfo);
        returnedStatus.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject statusJSON = response.body();

                int userStatus = statusJSON.get("status").getAsInt();

                // Sign up success
                if (userStatus == 1) {
                    onRetrofitSignUp1.onSuccess(statusJSON);

                } else {
                    onRetrofitSignUp1.onFailure();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                onRetrofitSignUp1.onFailure();
            }
        });
    }

    @Override
    public void updateCountry(String username, String country, final OnRetrofitSignUp1SuccessFail onRetrofitSignUp1) {
        Call<JsonObject> retrofit = RetrofitUserInfoSingleton.getRetrofitUserInfo()
                .updateUserCountry()
                .updateUserCountry(username, country);
        retrofit.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body().get("status").getAsInt() == 1) {
                    onRetrofitSignUp1.onSuccess(response.body());
                } else {
                    onRetrofitSignUp1.onFailure();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                onRetrofitSignUp1.onFailure();
            }
        });
    }
}
