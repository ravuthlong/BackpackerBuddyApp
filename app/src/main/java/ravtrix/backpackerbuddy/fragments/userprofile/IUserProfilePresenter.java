package ravtrix.backpackerbuddy.fragments.userprofile;

/**
 * Created by Ravinder on 9/14/16.
 */
public interface IUserProfilePresenter {

    void getUserInfo(int userID, String userImgURL);
    void onDestroy();

}
