package ravtrix.backpackerbuddy.fragments.userprofile;

import com.google.gson.JsonObject;

import ravtrix.backpackerbuddy.helpers.RetrofitUserInfoSingleton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ravinder on 9/14/16.
 */
public class UserProfileInteractor implements IUserProfileInteractor {

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
                    retrofitProfileListener.onSuccess();

                    responseJSON.get("firstname").getAsString();
                    responseJSON.get("lastname").getAsString();
                    retrofitProfileListener.onSetUsername(responseJSON.get("username").getAsString()); //set username

                    if (!responseJSON.get("detailOne").getAsString().isEmpty()) {
                        retrofitProfileListener.onSetDetailOneText(responseJSON.get("detailOne").getAsString());
                        retrofitProfileListener.onDetailOneAHint(false);
                    } else {
                        retrofitProfileListener.onSetDetailOneColor();
                        retrofitProfileListener.onSetDetailOneHint("Write a summary about yourself.");
                        //detailOne.setTextColor(ContextCompat.getColor(getContext(), R.color.grayHint));
                        //detailOne.setHint("Write a summary about yourself.");
                    }
                    if (!responseJSON.get("detailTwo").getAsString().isEmpty()) {
                        //detailTwo.setText(responseJSON.get("detailTwo").getAsString());
                        //isDetailTwoAHint = false;
                        retrofitProfileListener.onSetDetailTwoText(responseJSON.get("detailTwo").getAsString());
                        retrofitProfileListener.onDetailTwoAHint(false);
                    } else {
                        //detailTwo.setTextColor(ContextCompat.getColor(getContext(), R.color.grayHint));
                        //detailTwo.setHint("List down six backpacking items you must have while backpacking.");
                        retrofitProfileListener.onSetDetailTwoColor();
                        retrofitProfileListener.onSetDetailTwoHint("List down six backpacking items you must have while backpacking.");
                    }
                    if (!responseJSON.get("detailThree").getAsString().isEmpty()) {
                        //detailThree.setText(responseJSON.get("detailThree").getAsString());
                        //isDetailThreeAHint = false;
                        retrofitProfileListener.onSetDetailThreeText(responseJSON.get("detailThree").getAsString());
                        retrofitProfileListener.onDetailThreeAHint(false);
                    } else {
                        retrofitProfileListener.onSetDetailThreeColor();
                        retrofitProfileListener.onSetDetailThreeHint("Tell your potential backpacking buddy about your personality.");
                        //detailThree.setTextColor(ContextCompat.getColor(getContext(), R.color.grayHint));
                        //detailThree.setText("Tell your potential backpacking buddy about your personality.");
                    }
                    if (!responseJSON.get("detailFour").getAsString().isEmpty()) {
                        //detailFour.setText(responseJSON.get("detailFour").getAsString());
                        //isDetailFourAHint = false;
                        retrofitProfileListener.onSetDetailFourText(responseJSON.get("detailFour").getAsString());
                        retrofitProfileListener.onDetailFourAHint(false);
                    } else {
                        retrofitProfileListener.onSetDetailFourColor();
                        retrofitProfileListener.onSetDetailFourHint("Tell us how you would imagine your backpacking day to go.");
                        //detailFour.setTextColor(ContextCompat.getColor(getContext(), R.color.grayHint));
                        //detailFour.setText("Tell us how you would imagine your backpacking day to go.");
                    }
                } else {
                    retrofitProfileListener.onError();
                }

                if ((userImageURL == null) ||
                        (userImageURL.equals("0"))) {
                    retrofitProfileListener.onSetProfilePic("http://i.imgur.com/268p4E0.jpg");
                    //Picasso.with(getContext()).load("http://i.imgur.com/268p4E0.jpg").noFade().into(profilePic); // setProfilePic
                } else {
                    retrofitProfileListener.onSetProfilePic("http://backpackerbuddy.net23.net/profile_pic/" +
                            userID + ".JPG");
                    //Picasso.with(getContext()).load("http://backpackerbuddy.net23.net/profile_pic/" +
                           // userLocalStore.getLoggedInUser().getUserID() + ".JPG").noFade().into(profilePic);
                }
                // call
                retrofitProfileListener.onHideProgressBar();
                retrofitProfileListener.onSetViewVisible();
                //fragActivityProgressBarInterface.setProgressBarInvisible(); // hideprogressbar
                //v.setVisibility(View.VISIBLE); // setViewVisible
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                retrofitProfileListener.onError();
            }
        });
    }
}
