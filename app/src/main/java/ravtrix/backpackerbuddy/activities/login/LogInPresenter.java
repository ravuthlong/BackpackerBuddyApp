package ravtrix.backpackerbuddy.activities.login;

import ravtrix.backpackerbuddy.models.LoggedInUser;

/**
 * Created by Ravinder on 9/14/16.
 */
public class LogInPresenter implements ILogInPresenter {
    private ILogInView view;
    private LogInInteractor logInInteractor;

    public LogInPresenter(ILogInView view) {
        this.view = view;
        this.logInInteractor = new LogInInteractor();
    }

    @Override
    public void logUserIn(String username, String password) {

        view.showProgressDialog();

        logInInteractor.logInRetrofit(username, password, new OnRetrofitLogInListener() {

            @Override
            public void onSuccess(LoggedInUser loggedInUser) {
                view.hideProgressDialog();
                view.saveNewUser(loggedInUser);
                view.userCanLogIn();
            }

            @Override
            public void onError() {
                view.hideProgressDialog();
                view.showNoUserErrorMessage();
            }
        });
    }

    @Override
    public void onDestroy() {
        view = null;
    }
}
