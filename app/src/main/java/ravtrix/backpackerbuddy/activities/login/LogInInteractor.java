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
public class LogInInteractor implements ILogInInteractor {

    public LogInInteractor() {}

    @Override
    public void logInRetrofit(String username, String password, final OnRetrofitLogInListener onRetrofitLogInListener) {

        // Prepare HashMap of username and password to send to retrofit call
        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("username", username);
        arguments.put("password", password);

        Call<LoggedInUser> responseUser = RetrofitUserInfoSingleton.getRetrofitUserInfo().loggedInUser().userInfo(arguments);
        responseUser.enqueue(new Callback<LoggedInUser>() {
            @Override
            public void onResponse(Call<LoggedInUser> call, Response<LoggedInUser> response) {
                LoggedInUser user = response.body();

                if (user.getStatus() == 0) {
                    // User not found. 0 returned from PHP
                    onRetrofitLogInListener.onError();

                } else {
                    // User authenticated. Log user in
                    onRetrofitLogInListener.onSuccess(user);
                }
            }
            @Override
            public void onFailure(Call<LoggedInUser> call, Throwable t) {
                onRetrofitLogInListener.onError();

            }
        });
    }
}
