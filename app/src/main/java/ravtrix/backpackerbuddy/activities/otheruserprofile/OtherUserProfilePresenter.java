package ravtrix.backpackerbuddy.activities.otheruserprofile;

import com.google.gson.JsonObject;

/**
 * Created by Ravinder on 9/27/16.
 */

class OtherUserProfilePresenter implements IOtherUserProfilePresenter {

    private OtherUserProfileInteractor otherUserProfileInteractor;
    private IOtherUserProfileView view;

    OtherUserProfilePresenter(IOtherUserProfileView iOtherUserProfileView) {
        this.view = iOtherUserProfileView;
        otherUserProfileInteractor = new OtherUserProfileInteractor();
    }

    @Override
    public void fetchOtherUser(final int userID) {
        otherUserProfileInteractor.fetchOtherUserInfoRetrofit(userID, new OnRFOtherUserProfileListener() {
            @Override
            public void onSuccess(JsonObject otherUserJSON) {

                view.setUsername(otherUserJSON.get("username").getAsString());
                view.setUserDetailOne(otherUserJSON.get("detailOne").getAsString());
                view.setUserDetailTwo(otherUserJSON.get("detailTwo").getAsString());
                view.setUserDetailThree(otherUserJSON.get("detailThree").getAsString());
                view.setUserDetailFour(otherUserJSON.get("detailFour").getAsString());
                view.setUserLocation(otherUserJSON.get("latitude").getAsString().trim(),
                       otherUserJSON.get("longitude").getAsString().trim());
                view.loadProfileImage("http://backpackerbuddy.net23.net/profile_pic/" +
                        userID + ".JPG");
            }

            @Override
            public void onFailure() {
                view.displayErrorToast();
            }
        });
    }
}
