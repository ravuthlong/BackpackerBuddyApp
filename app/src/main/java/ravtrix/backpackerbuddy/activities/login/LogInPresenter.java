package ravtrix.backpackerbuddy.activities.login;

import ravtrix.backpackerbuddy.models.LoggedInUser;

/**
 * Created by Ravinder on 9/14/16.
 */
class LogInPresenter implements ILogInPresenter {
    private ILogInView view;
    private LogInInteractor logInInteractor;

    LogInPresenter(ILogInView view) {
        this.view = view;
        this.logInInteractor = new LogInInteractor();
    }

    @Override
    public void logUserIn(String username, String password, String token) {

        view.showProgressDialog();

        logInInteractor.logInRetrofit(username, password, token, new OnRetrofitLogInListener() {

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
