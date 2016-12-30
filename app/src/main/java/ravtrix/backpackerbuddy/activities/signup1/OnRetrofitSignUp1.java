package ravtrix.backpackerbuddy.activities.signup1;

/**
 * Created by Ravinder on 9/28/16.
 */

public interface OnRetrofitSignUp1 {

    void onUsernameTaken();
    void onEmailTaken();
    void onUsernameAndEmailTaken();
    void onUsernameAndEmailNotTaken();
    void onError();
}
