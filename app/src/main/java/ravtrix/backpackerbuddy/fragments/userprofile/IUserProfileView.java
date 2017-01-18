package ravtrix.backpackerbuddy.fragments.userprofile;

/**
 * Created by Ravinder on 9/14/16.
 */
interface IUserProfileView {

    void setUsername(String username);
    void setDetailOneText(String text);
    void setDetailOneHint(String hint);
    void setDetailTwoText(String text);
    void setDetailTwoHint(String hint);
    void setDetailThreeText(String text);
    void setDetailThreeHint(String hint);
    void setDetailFourText(String text);
    void setDetailFourHint(String hint);
    void hideProgressBar();
    void setViewVisible();
    void setDetailOneColor();
    void setDetailTwoColor();
    void setDetailThreeColor();
    void setDetailFourColor();
    void isDetailOneAHint(boolean hint);
    void isDetailTwoAHint(boolean hint);
    void isDetailThreeAHint(boolean hint);
    void isDetailFourAHint(boolean hint);
    void displayError();
    void hideImageTravel();
    void showImageTravel();
    void hideImageNotTravel();
    void showImageNotTravel();
    void showTextTravel();
    void hideTextTravel();
    void showTextNotTravel();
    void hideTextNotTravel();
    void hideProgressDialog();
    void setUserLocation(String country);
}

