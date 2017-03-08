package ravtrix.backpackerbuddy.activities.otheruserprofile;

import com.google.gson.JsonObject;

import ravtrix.backpackerbuddy.helpers.RetrofitMapSingleton;
import ravtrix.backpackerbuddy.helpers.RetrofitUserBucketListSingleton;
import ravtrix.backpackerbuddy.helpers.RetrofitUserInfoSingleton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ravinder on 9/27/16.
 */

class OtherUserProfileInteractor implements IOtherUserProfileInteractor {

    OtherUserProfileInteractor() {};

    @Override
    public void fetchOtherUserInfoRetrofit(int userID, final OnRFOtherUserProfileListener otherUserProfileListener) {

        Call<JsonObject> returnedInfo = RetrofitUserInfoSingleton.getRetrofitUserInfo()
                .getUserDetails()
                .getUserDetails(userID);

        returnedInfo.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject responseJSON = response.body();

                // If success
                if (responseJSON.get("success").getAsInt() == 1) {
                    otherUserProfileListener.onSuccess(responseJSON);
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                otherUserProfileListener.onFailure();
            }
        });
    }

    @Override
    public void checkHasMap(int userID, final OnRFOtherUserProfileListener otherUserProfileRFListener) {

        Call<JsonObject> retrofit = RetrofitMapSingleton.getRetrofitMap()
                .checkHasMap()
                .checkHasMap(userID);

        retrofit.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                otherUserProfileRFListener.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                otherUserProfileRFListener.onFailure();
            }
        });
    }

    @Override
    public void checkHasBucket(int userID, final OnRFOtherUserProfileListener otherUserProfileRFListener) {


        Call<JsonObject> retrofit = RetrofitUserBucketListSingleton.getRetrofitBucketList()
                .checkHasBucket()
                .checkHasBucket(userID);


        retrofit.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                otherUserProfileRFListener.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                otherUserProfileRFListener.onFailure();
            }
        });
    }
}
