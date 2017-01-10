package ravtrix.backpackerbuddy.fragments.userprofile;

import com.google.gson.JsonObject;

import java.util.HashMap;

import ravtrix.backpackerbuddy.helpers.RetrofitUserInfoSingleton;
import ravtrix.backpackerbuddy.models.UserLocalStore;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ravinder on 9/14/16.
 */
class UserProfileInteractor implements IUserProfileInteractor {

    @Override
    public void getUserIntoRetrofit(final int userID, final String userImageURL, final RetrofitProfileListener retrofitProfileListener) {
        Call<JsonObject> jsonObjectCall =
                RetrofitUserInfoSingleton
                        .getRetrofitUserInfo()
                        .getUserDetails()
                        .getUserDetails(userID);
        jsonObjectCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject responseJSON = response.body();
                // If success
                if (responseJSON.get("success").getAsInt() == 1) {
                    retrofitProfileListener.onSetUsername(responseJSON.get("username").getAsString()); //set username

                    if (!responseJSON.get("detailOne").getAsString().isEmpty()) {
                        retrofitProfileListener.onSetDetailOneText(responseJSON.get("detailOne").getAsString());
                        retrofitProfileListener.onDetailOneAHint(false);
                    } else {
                        retrofitProfileListener.onSetDetailOneColor();
                        retrofitProfileListener.onSetDetailOneHint("Write a summary about yourself.");
                    }
                    if (!responseJSON.get("detailTwo").getAsString().isEmpty()) {
                        retrofitProfileListener.onSetDetailTwoText(responseJSON.get("detailTwo").getAsString());
                        retrofitProfileListener.onDetailTwoAHint(false);
                    } else {
                        retrofitProfileListener.onSetDetailTwoColor();
                        retrofitProfileListener.onSetDetailTwoHint("List down six backpacking items you must have while backpacking.");
                    }
                    if (!responseJSON.get("detailThree").getAsString().isEmpty()) {
                        retrofitProfileListener.onSetDetailThreeText(responseJSON.get("detailThree").getAsString());
                        retrofitProfileListener.onDetailThreeAHint(false);
                    } else {
                        retrofitProfileListener.onSetDetailThreeColor();
                        retrofitProfileListener.onSetDetailThreeHint("Tell your potential backpacking buddy about your personality.");
                    }
                    if (!responseJSON.get("detailFour").getAsString().isEmpty()) {
                        retrofitProfileListener.onSetDetailFourText(responseJSON.get("detailFour").getAsString());
                        retrofitProfileListener.onDetailFourAHint(false);
                    } else {
                        retrofitProfileListener.onSetDetailFourColor();
                        retrofitProfileListener.onSetDetailFourHint("Tell us how you would imagine your backpacking day to go.");
                    }
                    retrofitProfileListener.onSetTravelStatus(responseJSON.get("traveling").getAsInt());
                } else {
                    retrofitProfileListener.onError();
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                retrofitProfileListener.onError();
            }
        });
    }


    @Override
    public void updateTravelStatus(HashMap<String, String> userInfo, final UserLocalStore userLocalStore,
                                   final RetrofitTravelListener retrofitTravelListener) {

        Call<JsonObject> retrofit = RetrofitUserInfoSingleton.getRetrofitUserInfo()
                .updateTravelingStatus()
                .updateTravelStatus(userInfo);

        retrofit.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body().get("newStatus").getAsInt() == 1) {
                    // User is now traveling
                    retrofitTravelListener.onTravel();
                } else {
                    // User no longer travels
                    retrofitTravelListener.onNotTravel();
                }
                retrofitTravelListener.onHideProgressDialog();
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                retrofitTravelListener.onError();
            }
        });

    }
}
