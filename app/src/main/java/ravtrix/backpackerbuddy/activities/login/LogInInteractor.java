package ravtrix.backpackerbuddy.activities.login;

import java.util.HashMap;

import ravtrix.backpackerbuddy.helpers.RetrofitUserInfoSingleton;
import ravtrix.backpackerbuddy.models.LoggedInUser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ravinder on 9/14/16.
 */
class LogInInteractor implements ILogInInteractor {

    LogInInteractor() {}

    @Override
    public void logInRetrofit(String username, String password, String token, final OnRetrofitLogInListener onRetrofitLogInListener) {

        // Prepare HashMap of username and password to send to retrofit call
        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("username", username);
        arguments.put("password", password);
        arguments.put("token", token);

        Call<LoggedInUser> responseUser = RetrofitUserInfoSingleton.getRetrofitUserInfo().loggedInUser().userInfo(arguments);
        responseUser.enqueue(new Callback<LoggedInUser>() {
            @Override
            public void onResponse(Call<LoggedInUser> call, Response<LoggedInUser> response) {
                LoggedInUser user = response.body();

                if (null != user && user.getStatus() == 1) {
                    // User authenticated. Log user in
                    onRetrofitLogInListener.onSuccess(user);
                } else {
                    // User not found. 0 returned from PHP
                    onRetrofitLogInListener.onError();
                }
            }
            @Override
            public void onFailure(Call<LoggedInUser> call, Throwable t) {
                onRetrofitLogInListener.onError();

            }
        });
    }
}
