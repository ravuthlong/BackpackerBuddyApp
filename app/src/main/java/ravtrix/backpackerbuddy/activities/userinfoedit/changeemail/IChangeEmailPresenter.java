package ravtrix.backpackerbuddy.activities.userinfoedit.changeemail;

import java.util.HashMap;

/**
 * Created by Ravinder on 1/24/17.
 */

interface IChangeEmailPresenter {

    void checkEmailTaken(String newEmail);
    void changeEmail(HashMap<String, String> emailHash);
}
