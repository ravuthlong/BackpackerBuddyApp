package ravtrix.backpackerbuddy.fragments.findbuddy.findbuddynear;

import java.util.List;

import ravtrix.backpackerbuddy.helpers.RetrofitUserInfoSingleton;
import ravtrix.backpackerbuddy.models.UserLocationInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ravinder on 10/4/16.
 */

class FindBuddyNearInteractor implements IFindBuddyNearInteractor {

    @Override
    public void fetchNearbyUsersRetrofit(final int userID, int distance, final OnFindBuddyNearListener onFindBuddyNearListener) {
        Call<List<UserLocationInfo>> retrofitCall = RetrofitUserInfoSingleton.getRetrofitUserInfo ()
                .getNearbyUsers()
                .getNearbyUsers(userID, distance);
        retrofitCall.enqueue(new Callback<List<UserLocationInfo>>() {
            @Override
            public void onResponse(Call<List<UserLocationInfo>> call, Response<List<UserLocationInfo>> response) {
                List<UserLocationInfo> userList = response.body();
                // Pass userList back to the presenter
                onFindBuddyNearListener.onSuccess(userList);
            }

            @Override
            public void onFailure(Call<List<UserLocationInfo>> call, Throwable t) {
                onFindBuddyNearListener.onFailure();
            }
        });
    }
}
