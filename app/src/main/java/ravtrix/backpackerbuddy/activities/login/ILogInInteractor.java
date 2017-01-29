package ravtrix.backpackerbuddy.activities.login;

/**
 * Created by Ravinder on 9/14/16.
 */
interface ILogInInteractor {

    /**
     * Log user in based on given credentials
     * @param username                      - user name given
     * @param password                      - password given
     * @param token                         - FireBase cloud token (updated on successful log in)
     * @param onRetrofitLogInListener       - listener for retrofit completion
     */
    void logInRetrofit(String username, String password, String token, OnRetrofitLogInListener onRetrofitLogInListener);
}
