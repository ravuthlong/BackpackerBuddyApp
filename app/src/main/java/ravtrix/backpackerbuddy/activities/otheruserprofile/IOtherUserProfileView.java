package ravtrix.backpackerbuddy.activities.otheruserprofile;

/**
 * Created by Ravinder on 9/27/16.
 */

public interface IOtherUserProfileView {

    //username.setText(responseJSON.get("username").getAsString());
    //userDetailOne.setText(responseJSON.get("detailOne").getAsString());
    //userDetailTwo.setText(responseJSON.get("detailTwo").getAsString());
    //userDetailThree.setText(responseJSON.get("detailThree").getAsString());
    //userDetailFour.setText(responseJSON.get("detailFour").getAsString());

    void setUsername(String username);
    void setUserDetailOne(String userDetailOne);
    void setUserDetailTwo(String userDetailTwo);
    void setUserDetailThree(String userDetailThree);
    void setUserDetailFour(String userDetailFour);
    void setUserLocation(String latitude, String longitude);
    void loadProfileImage(String imageURL);
    void fetchOtherUserProfile();
    void displayErrorToast();
    void showProgressbar();

}
