package ravtrix.backpackerbuddy.activities.signup1;

import com.google.gson.JsonObject;

import ravtrix.backpackerbuddy.helpers.RetrofitUserInfoSingleton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ravinder on 9/28/16.
 */

class SignUpPart1Interactor implements ISignUpPart1Interactor {

    @Override
    public void isUsernameTaken(String username, final OnRetrofitSignUp1 onRetrofitSignUp1) {
        Call<JsonObject> retrofitCall = RetrofitUserInfoSingleton.getRetrofitUserInfo()
                .isUsernameTaken()
                .isUsernameTaken(username);

        retrofitCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                // Username is taken
                if (response.body().get("taken").getAsInt() == 1) {
                    // interface user taken
                    onRetrofitSignUp1.onUsernameTaken();
                } else {
                    // user can continue
                    onRetrofitSignUp1.onUsernameNotTaken();
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                onRetrofitSignUp1.onError();
            }
        });
    }
}
