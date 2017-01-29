package ravtrix.backpackerbuddy.activities.userinfoedit.changeusername;

import ravtrix.backpackerbuddy.models.UserLocalStore;

/**
 * Created by Ravinder on 1/24/17.
 */

interface IChangeUsernameView {

    void displayUsernameTaken();
    void displayUsernameChanged();
    void displayErrorToast();
    void displayErrorWrongPassword();
    UserLocalStore getUserLocalStore();
    String getOldUsername();
    String getNewUsername();
    String getPassword();
    void finished();
    void setNewLocalStore(String newUsername);
}
