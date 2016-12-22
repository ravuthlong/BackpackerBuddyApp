package ravtrix.backpackerbuddy.fragments.userprofile;

import com.google.gson.JsonObject;

import ravtrix.backpackerbuddy.helpers.RetrofitUserInfoSingleton;
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
                } else {
                    retrofitProfileListener.onError();
                }

                if ((userImageURL.equals("0"))) {
                    retrofitProfileListener.onSetProfilePic("http://s3.amazonaws.com/37assets/svn/765-default-avatar.png");
                } else {
                    retrofitProfileListener.onSetProfilePic("http://backpackerbuddy.net23.net/profile_pic/" +
                            userID + ".JPG");
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                retrofitProfileListener.onError();
            }
        });
    }
}
