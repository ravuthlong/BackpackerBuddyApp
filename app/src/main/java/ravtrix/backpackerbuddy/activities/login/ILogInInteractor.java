package ravtrix.backpackerbuddy.activities.login;

/**
 * Created by Ravinder on 9/14/16.
 */
public interface ILogInInteractor {
    void logInRetrofit(String username, String password, OnRetrofitLogInListener onRetrofitLogInListener);
}
