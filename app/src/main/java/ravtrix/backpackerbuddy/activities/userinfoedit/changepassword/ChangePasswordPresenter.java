package ravtrix.backpackerbuddy.activities.userinfoedit.changepassword;

import com.google.gson.JsonObject;

import java.util.HashMap;

import ravtrix.backpackerbuddy.retrofit.retrofitinterfaces.OnFinishedListenerRetrofit;

/**
 * Created by Ravinder on 1/24/17.
 */

class ChangePasswordPresenter implements IChangePasswordPresenter {

    private IChangePasswordView iChangePasswordView;
    private ChangePasswordInteractor changePasswordInteractor;

    ChangePasswordPresenter(IChangePasswordView iChangePasswordView) {
        this.iChangePasswordView = iChangePasswordView;
        this.changePasswordInteractor = new ChangePasswordInteractor();
    }

    @Override
    public void changePassword(HashMap<String, String> passwordHash) {

        changePasswordInteractor.changePassword(passwordHash, new OnFinishedListenerRetrofit() {
            @Override
            public void onFinished(JsonObject jsonObject) {
                if (jsonObject.get("status").getAsInt() == 1) {
                    iChangePasswordView.passwordChangedToast(); // changed success
                    iChangePasswordView.finished();
                }  else {
                    // Changed unsuccessfully
                    iChangePasswordView.displayWrongPassword();
                }
            }

            @Override
            public void onError() {
                iChangePasswordView.displayErrorToast();
            }
        });
    }
}
