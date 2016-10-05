package ravtrix.backpackerbuddy.activities.otheruserprofile;

import com.google.gson.JsonObject;

import ravtrix.backpackerbuddy.helpers.RetrofitUserInfoSingleton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ravinder on 9/27/16.
 */

public class OtherUserProfileInteractor implements IOtherUserProfileInteractor {

    public OtherUserProfileInteractor() {};

    @Override
    public void fetchOtherUserInfoRetrofit(int userID, final OnRFOtherUserProfileListener otherUserProfileListener) {

        Call<JsonObject> returnedInfo = RetrofitUserInfoSingleton.getRetrofitUserInfo().getUserDetails().getUserDetails(userID);
        returnedInfo.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject responseJSON = response.body();

                // If success
                if (responseJSON.get("success").getAsInt() == 1) {

                    otherUserProfileListener.onSuccess(responseJSON);
                    //responseJSON.get("firstname").getAsString();
                    //responseJSON.get("lastname").getAsString();
                    //username.setText(responseJSON.get("username").getAsString());
                    //userDetailOne.setText(responseJSON.get("detailOne").getAsString());
                    //userDetailTwo.setText(responseJSON.get("detailTwo").getAsString());
                    //userDetailThree.setText(responseJSON.get("detailThree").getAsString());
                    //userDetailFour.setText(responseJSON.get("detailFour").getAsString());

                    // Set city based on latitude and longitude
                    //String latitude = responseJSON.get("latitude").getAsString().trim();
                    //String longitude = responseJSON.get("longitude").getAsString().trim();
                    //tvLocation.setText(Helpers.cityGeocoder(getApplicationContext(),
                            //Double.parseDouble(latitude), Double.parseDouble(longitude)));

                    /*
                    Picasso.with(getApplicationContext())
                            .load("http://backpackerbuddy.net23.net/profile_pic/" +
                                    postInfo.getInt("userID") + ".JPG")
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .into(profileImage, new com.squareup.picasso.Callback() {
                                @Override
                                public void onSuccess() {
                                    progressBar.setVisibility(View.GONE);
                                    relativeLayout.setVisibility(View.VISIBLE);
                                }
                                @Override
                                public void onError() {
                                    progressBar.setVisibility(View.GONE);
                                    relativeLayout.setVisibility(View.VISIBLE);
                                }
                            });*/
                }

            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                otherUserProfileListener.onFailure();
            }
        });
    }
}
