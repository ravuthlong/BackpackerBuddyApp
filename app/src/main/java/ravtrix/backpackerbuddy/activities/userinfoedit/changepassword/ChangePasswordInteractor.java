package ravtrix.backpackerbuddy.activities.userinfoedit.changepassword;

import com.google.gson.JsonObject;

import java.util.HashMap;

import ravtrix.backpackerbuddy.helpers.RetrofitUserInfoSingleton;
import ravtrix.backpackerbuddy.retrofit.retrofitinterfaces.OnFinishedListenerRetrofit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ravinder on 1/24/17.
 */

class ChangePasswordInteractor implements IChangePasswordInteractor {

    @Override
    public void changePassword(HashMap<String, String> passwordHash, final OnFinishedListenerRetrofit onFinishedListenerRetrofit) {

        Call<JsonObject> passwordCheck = RetrofitUserInfoSingleton.getRetrofitUserInfo()
                .changePassword()
                .changePassword(passwordHash);

        passwordCheck.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                onFinishedListenerRetrofit.onFinished(response.body());
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                onFinishedListenerRetrofit.onError();
            }
        });
    }
}
