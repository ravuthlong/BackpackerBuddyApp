package ravtrix.backpackerbuddy.activities.signup1;

/**
 * Created by Ravinder on 9/27/16.
 */

interface ISignUpPart1View {

    void startSignUpPart2Activity(String email, String username, String password);
    void displayUsernameTakenDialog();
    void displayEmailTakenDialog();
    void displayUsernameAndEmailTakenDialog();
    void displayErrorToast();
    void hideProgressDialog();
}
