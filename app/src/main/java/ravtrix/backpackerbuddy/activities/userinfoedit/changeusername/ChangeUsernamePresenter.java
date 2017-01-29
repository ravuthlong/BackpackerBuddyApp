package ravtrix.backpackerbuddy.activities.userinfoedit.changeusername;

import com.google.gson.JsonObject;

import java.util.HashMap;

import ravtrix.backpackerbuddy.models.UserLocalStore;
import ravtrix.backpackerbuddy.retrofit.retrofitinterfaces.OnFinishedListenerRetrofit;

/**
 * Created by Ravinder on 1/24/17.
 */

class ChangeUsernamePresenter implements IChangeUsernamePresenter {

    private IChangeUsernameView iChangeUsernameView;
    private ChangeUsernameInteractor changeUsernameInteractor;

    ChangeUsernamePresenter(IChangeUsernameView iChangeUsernameView) {
        this.iChangeUsernameView = iChangeUsernameView;
        this.changeUsernameInteractor = new ChangeUsernameInteractor();
    }

    @Override
    public void checkUsernameTaken(String newUsername) {

        changeUsernameInteractor.checkUsernameTakenRetrofit(newUsername, new OnFinishedListenerRetrofit() {
            @Override
            public void onFinished(JsonObject jsonObject) {
                if (jsonObject.get("usernametaken").getAsInt() == 1) {
                    iChangeUsernameView.displayUsernameTaken();
                } else {

                    // Get the userLocalStore from view
                    UserLocalStore userLocalStore = iChangeUsernameView.getUserLocalStore();

                    if (userLocalStore.getLoggedInUser().getIsFacebook() == 1) {
                        // Facebook user
                        HashMap<String, String> userInfo = new HashMap<>();
                        userInfo.put("userID", Integer.toString(userLocalStore.getLoggedInUser().getUserID()));
                        userInfo.put("newUsername", iChangeUsernameView.getNewUsername());
                        userInfo.put("username", iChangeUsernameView.getOldUsername());
                        changeUsernameFacebook(userInfo);
                    } else {
                        // Regular user
                        HashMap<String, String> userInfo = new HashMap<>();
                        userInfo.put("userID", Integer.toString(userLocalStore.getLoggedInUser().getUserID()));
                        userInfo.put("newUsername", iChangeUsernameView.getNewUsername());
                        userInfo.put("password", iChangeUsernameView.getPassword());
                        userInfo.put("username", iChangeUsernameView.getOldUsername());
                        changeUsername(userInfo); // Change username
                    }
                }
            }
            @Override
            public void onError() {
                iChangeUsernameView.displayErrorToast();
            }
        });
    }

    @Override
    public void changeUsername(final HashMap<String, String> userInfo) {

        changeUsernameInteractor.changeUsernameRetrofit(userInfo, new OnFinishedListenerRetrofit() {
            @Override
            public void onFinished(JsonObject jsonObject) {
                if (jsonObject.get("status").getAsInt() == 1) {
                    iChangeUsernameView.displayUsernameChanged();
                    iChangeUsernameView.setNewLocalStore(userInfo.get("newUsername"));
                    iChangeUsernameView.finished();
                } else {
                    iChangeUsernameView.displayErrorWrongPassword();
                }
            }
            @Override
            public void onError() {
                iChangeUsernameView.displayErrorToast();
            }
        });
    }

    @Override
    public void changeUsernameFacebook(final HashMap<String, String> userInfo) {

        changeUsernameInteractor.changeUsernameFacebookRetrofit(userInfo, new OnFinishedListenerRetrofit() {
            @Override
            public void onFinished(JsonObject jsonObject) {
                if (jsonObject.get("status").getAsInt() == 1) {
                    iChangeUsernameView.displayUsernameChanged();
                    iChangeUsernameView.setNewLocalStore(userInfo.get("newUsername"));
                    iChangeUsernameView.finished();
                } else {
                    iChangeUsernameView.displayErrorWrongPassword();
                }
            }
            @Override
            public void onError() {
                iChangeUsernameView.displayErrorToast();
            }
        });
    }
}
