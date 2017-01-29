package ravtrix.backpackerbuddy.activities.userinfoedit.changeemail;

import com.google.gson.JsonObject;

import java.util.HashMap;

import ravtrix.backpackerbuddy.retrofit.retrofitinterfaces.OnFinishedListenerRetrofit;

/**
 * Created by Ravinder on 1/24/17.
 */

class ChangeEmailPresenter implements IChangeEmailPresenter {
    private IChangeEmailView iChangeEmailView;
    private ChangeEmailInteractor changeEmailInteractor;

    ChangeEmailPresenter(IChangeEmailView iChangeEmailView) {
        this.iChangeEmailView = iChangeEmailView;
        this.changeEmailInteractor = new ChangeEmailInteractor();
    }

    @Override
    public void checkEmailTaken(String newEmail) {

        changeEmailInteractor.checkEmailTaken(newEmail, new OnFinishedListenerRetrofit() {
            @Override
            public void onFinished(JsonObject jsonObject) {
                if (jsonObject.get("emailtaken").getAsInt() == 1) {
                    // email taken
                    iChangeEmailView.displayEmailTaken();
                } else {
                    // email not taken
                    iChangeEmailView.changeEmail();
                }
            }
            @Override
            public void onError() {}
        });
    }

    @Override
    public void changeEmail(HashMap<String, String> emailHash) {

        changeEmailInteractor.changeEmailRetrofit(emailHash, new OnFinishedListenerRetrofit() {
            @Override
            public void onFinished(JsonObject jsonObject) {
                if (jsonObject.get("status").getAsInt() == 1) {
                    iChangeEmailView.displayEmailChanged(); // email changed success toast
                    iChangeEmailView.changeEmailLocalstore();
                    iChangeEmailView.finished();
                }  else {
                    // Changed unsuccessfully
                    iChangeEmailView.displayWrongPassword();
                }
            }
            @Override
            public void onError() {
               iChangeEmailView.displayErrorToast();
            }
        });
    }
}
