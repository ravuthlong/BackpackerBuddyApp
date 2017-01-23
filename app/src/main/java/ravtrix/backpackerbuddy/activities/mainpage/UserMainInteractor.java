package ravtrix.backpackerbuddy.activities.mainpage;

import ravtrix.backpackerbuddy.activities.login.OnRetrofitLogInListener;
import ravtrix.backpackerbuddy.helpers.RetrofitUserInfoSingleton;
import ravtrix.backpackerbuddy.models.LoggedInUser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ravinder on 1/11/17.
 */

class UserMainInteractor implements IUserMainInteractor {

    @Override
    public void updateLocalstore(int userID, final OnRetrofitLogInListener onRetrofitLogInListener) {

        Call<LoggedInUser> retrofit = RetrofitUserInfoSingleton.getRetrofitUserInfo()
                .updateLocalstore()
                .updateLocalstore(userID);
        retrofit.enqueue(new Callback<LoggedInUser>() {
            @Override
            public void onResponse(Call<LoggedInUser> call, Response<LoggedInUser> response) {
                onRetrofitLogInListener.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<LoggedInUser> call, Throwable t) {
                onRetrofitLogInListener.onError();
            }
        });
    }
}
