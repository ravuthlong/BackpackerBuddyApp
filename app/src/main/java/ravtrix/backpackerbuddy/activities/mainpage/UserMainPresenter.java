package ravtrix.backpackerbuddy.activities.mainpage;

import ravtrix.backpackerbuddy.activities.login.OnRetrofitLogInListener;
import ravtrix.backpackerbuddy.models.LoggedInUser;

/**
 * Created by Ravinder on 10/4/16.
 */

class UserMainPresenter implements IUserMainPresenter {

    private IUserMainView view;
    private UserMainInteractor interactor;

    UserMainPresenter(IUserMainView view) {
        this.view = view;
        this.interactor = new UserMainInteractor();
    }

    @Override
    public void updateLocalstore(int userID) {

        interactor.updateLocalstore(userID, new OnRetrofitLogInListener() {
            @Override
            public void onSuccess(LoggedInUser loggedInUser) {
                view.updateLocalstore(loggedInUser);
            }
            @Override
            public void onError() {}
        });
    }
}
