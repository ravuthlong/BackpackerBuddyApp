package ravtrix.backpackerbuddy.activities.login;

import ravtrix.backpackerbuddy.models.LoggedInUser;

/**
 * Created by Ravinder on 9/14/16.
 */
public interface ILogInView {
    void showNoUserErrorMessage();
    void showProgressDialog();
    void hideProgressDialog();
    void saveNewUser(LoggedInUser loggedInUser);
    void userCanLogIn();
}
