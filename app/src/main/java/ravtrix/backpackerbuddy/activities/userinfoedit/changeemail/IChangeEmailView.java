package ravtrix.backpackerbuddy.activities.userinfoedit.changeemail;

/**
 * Created by Ravinder on 1/24/17.
 */

interface IChangeEmailView {
    void displayEmailTaken();
    void displayEmailChanged();
    void displayWrongPassword();
    void finished();
    void changeEmail();
    void displayErrorToast();
    void changeEmailLocalstore();
}
