package ravtrix.backpackerbuddy.activities.otheruserprofile;

/**
 * Created by Ravinder on 9/27/16.
 */

interface IOtherUserProfileView {
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
    void showImgTravel();
    void showImgNotTravel();
    void showTxtTravel();
    void showTxtNotTravel();
    void hideImgTravel();
    void hideImgNotTravel();
    void hideTxtTravel();
    void hideTxtNotTravel();
    void showFloatingButtonBucket();
    void showFloatingButtonMessage();
}
