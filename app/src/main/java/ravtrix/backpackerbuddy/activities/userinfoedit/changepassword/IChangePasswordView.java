package ravtrix.backpackerbuddy.activities.userinfoedit.changepassword;

/**
 * Created by Ravinder on 1/24/17.
 */

interface IChangePasswordView {

    void passwordChangedToast();
    void displayWrongPassword();
    void displayErrorToast();
    void finished();
}
