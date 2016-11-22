package ravtrix.backpackerbuddy.activities.login;

/**
 * Created by Ravinder on 9/14/16.
 */
interface ILogInPresenter {
    void logUserIn(String username, String password, String token);
    void onDestroy();
}
