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
}
